# Plan: Tweet Image Support

## Overview

Add optional image attachments to tweets. Users can attach one image per tweet at creation time, swap or remove it when editing, and admins can wipe an image without deleting the tweet.

---

## 1. Database — Schema Change

Add a nullable `image` column to `tweets`:

```sql
ALTER TABLE tweets ADD COLUMN image VARCHAR(255);
```

Added to `DB.txt` right after the `tweets` table `CREATE` statement so every fresh DB rebuild includes the column. Existing seed tweets get `NULL` (no image).

**DBLogger impact**: all `INSERT INTO tweets` log lines already include explicit column lists, so they are unaffected. The `UPDATE tweets` log line in `editTweet` will be extended to include `image`.

---

## 2. Model — Tweet.java

Add:
```java
private String image;   // "img/tweets/42.jpg" or null
public String getImage()          { return image; }
public void setImage(String img)  { this.image = img; }
```

---

## 3. File Storage

Save tweet images to **`src/main/webapp/img/tweets/`** (already served as static files by Jetty, same as user avatars under `img/`).

Naming convention: `{tweet_id}.{ext}` — simple, unique, easy to find and delete.

Path stored in DB: `img/tweets/42.jpg`.

---

## 4. Repository — TweetRepository.java

### 4a. All SELECT queries
Add `t.image` to every `SELECT` that goes through `mapTweet()`:
- `findById`
- `findByUser` (overloaded)
- `findByFollowedUsers`
- `findAllGlobalTweets`
- `findReplies`

Update `mapTweet()`:
```java
tweet.setImage(rs.getString("image"));
```

### 4b. save()
Add `image` to the INSERT:
```sql
INSERT INTO tweets (user_id, content, created_at, parent_id, image) VALUES (?, ?, ?, ?, ?)
```
`statement.setString(5, tweet.getImage());` — null when no file attached.

DBLogger line updated accordingly.

### 4c. update()
Change to:
```sql
UPDATE tweets SET content = ?, image = ? WHERE tweet_id = ?
```
Three variants of the update behaviour (see §7 Edit flow).

### 4d. New method — removeImage()
```java
public void removeImage(Integer tweetId) {
    // UPDATE tweets SET image = NULL WHERE tweet_id = ?
    // DBLogger.append(...)
}
```

---

## 5. Service — TweetService.java

- `add(Tweet tweet, Part filePart, String imgDir)` — save file if present, set `tweet.setImage(path)`, call `tweetRepository.save(tweet)`.
- `edit(Integer tweetId, String content, Part filePart, String imgDir, boolean removeImage)` — three outcomes:
  - `removeImage = true` → set image to NULL (delete old file).
  - `filePart` has content → replace image (delete old file, save new).
  - Otherwise → keep existing image, update content only.
- `removeImage(Integer tweetId)` — admin-only wipe; deletes the file and sets DB column to NULL.

File deletion helper: `deleteImageFile(String imagePath, String webappRoot)` — construct absolute path, call `Files.deleteIfExists()`.

---

## 6. Controllers

### 6a. AddTweet.java
- Add `@MultipartConfig`.
- Read `request.getPart("tweetImage")`.
- Pass part + `getServletContext().getRealPath("/img/tweets/")` to `tweetService.add(...)`.

### 6b. EditTweet.java
- Add `@MultipartConfig`.
- Read `request.getPart("tweetImage")` and `request.getParameter("removeImage")`.
- Call `tweetService.edit(id, content, filePart, imgDir, removeFlag)`.

### 6c. New — RemoveTweetImage.java
- Admin-only (`role == ADMINISTRATOR`).
- `@WebServlet("/RemoveTweetImage")`.
- `doPost` → `tweetService.removeImage(tweetId)`, respond `"ok"`.

---

## 7. Frontend — Compose (Timeline.jsp / Feed.jsp)

The tweet compose box gains a file-attach button:

```html
<label class="btn sm ghost" for="tweetImageInput">
  <i class="fa-solid fa-image"></i>
</label>
<input type="file" id="tweetImageInput" accept="image/*" style="display:none">
<span id="tweetImageName" class="muted w3-small"></span>
<button id="clearTweetImage" style="display:none">✕</button>
```

JS in `index.html`:
- File selected → show filename + clear button.
- Clear button → reset input, hide preview.
- `#addTweet` click → `FormData` including the file part POSTed via `$.ajax` (already uses `FormData` for the profile form — same pattern).

---

## 8. Frontend — Display (FeedTweets.jsp, Tweets.jsp)

Below `<p class="tweetText content">` add:

```jsp
<c:if test="${not empty t.image}">
  <img src="${t.image}" alt="Tweet image" class="tweet-img">
</c:if>
```

Same block inside the comments (`c` variable) loop.

CSS class `.tweet-img`: `max-width: 100%; border-radius: 12px; margin-top: 8px;`.

---

## 9. Frontend — Edit (inline edit container)

The existing edit container in `FeedTweets.jsp` / `Tweets.jsp` gains:

```jsp
<c:if test="${not empty t.image}">
  <div class="current-tweet-img">
    <img src="${t.image}" class="tweet-img-thumb">
    <label><input type="checkbox" class="removeImageCheck"> Remove image</label>
  </div>
</c:if>
<input type="file" class="editTweetImage" accept="image/*">
```

JS (`index.html` `.saveTweetEdit` handler): build a `FormData` with `content`, `removeImage` checkbox value, and optional new file, POST to `EditTweet`.

---

## 10. Frontend — Admin "Remove Image" button

In `FeedTweets.jsp` / `Tweets.jsp`, inside the admin actions block:

```jsp
<c:if test="${sessionScope.user.role == 'ADMINISTRATOR' && not empty t.image}">
  <button type="button" class="removeImageBtn act danger">
    <i class="fa-solid fa-image"></i> Remove image
  </button>
</c:if>
```

JS in `index.html`:
```js
$(document).on("click", ".removeImageBtn", function() {
  var id = $(this).closest("[id]").attr("id");
  $.post("RemoveTweetImage", { id: id }, function() {
    App.reloadTweets();
  });
});
```

---

## 11. DB.txt Persistence

- Schema: `image VARCHAR(255)` added to `CREATE TABLE tweets`.
- `save()` DBLogger: `INSERT INTO tweets (tweet_id, user_id, content, created_at, parent_id, image) VALUES (..., 'img/tweets/42.jpg')`.
- `removeImage()` DBLogger: `UPDATE tweets SET image = NULL WHERE tweet_id = 42`.
- `editTweet()` DBLogger: `UPDATE tweets SET content = ..., image = ... WHERE tweet_id = ...`.

---

## 12. Edge Cases Handled

| Case | Handling |
|------|----------|
| No image attached | `image` column = NULL; no `<img>` rendered |
| Non-image file uploaded | `accept="image/*"` on input; server-side extension check |
| Tweet deleted | `tweetService.delete()` also calls file deletion helper |
| Image replaced on edit | Old file deleted before saving new |
| Admin removes image | File deleted + column set to NULL; tweet content untouched |
| DB rebuild (server restart) | Logged SQL preserves exact image path; files in `img/tweets/` survive restarts |
| Comments (replies) with images | Same SELECT queries include image; same JSP block renders it |

---

## 13. Implementation Order

1. `DB.txt` — add `image` column to schema  
2. `Tweet.java` — add field  
3. `TweetRepository.java` — SELECTs, save, update, removeImage  
4. `TweetService.java` — file handling logic  
5. `AddTweet.java` — multipart + file  
6. `EditTweet.java` — multipart + remove flag  
7. `RemoveTweetImage.java` — new servlet  
8. `FeedTweets.jsp` / `Tweets.jsp` — display + edit UI + admin button  
9. `Timeline.jsp` / `Feed.jsp` — compose UI with file input  
10. `index.html` — JS handlers for compose, edit, admin remove  
11. CSS — `.tweet-img` style  
