# Project context — lab4-epaw (Chirp)

A Twitter-like social app for UPF's EPAW course. Java servlets + JSP, SQLite, served by
Maven Jetty (`mvn jetty:run`, http://localhost:8080). The UI is a **Single Page Application
shell** that loads HTML fragments over AJAX with jQuery.

## How the app is wired

### The shell — `src/main/webapp/index.html`
The only full HTML page. It defines a fixed 3-column layout using W3.CSS grid:

```
#navigation  (top bar, fixed)        <- loads "Menu"
#lcolumn  (m3, left)   #content (m6, center)   #rcolumn (m3, right)
```

On `App.init()` (document ready) it fires:
- `#navigation` ← `Menu`
- `#content`    ← `Content`
- `#lcolumn`    ← `Profile`
- `#rcolumn`    ← `NotFollowed`

All interactivity lives in `App.bindEvents()` using **event delegation** on `document`,
so dynamically loaded fragments keep working. The handlers (and their hook selectors) are:

| Selector / id                        | Action                                              | Reloads |
|--------------------------------------|-----------------------------------------------------|---------|
| `.menu`                              | nav click → `#content`.load(href)                   | content |
| `form` submit                        | generic AJAX POST (multipart)                       | nav + content |
| `.followUser`                        | POST `Follow` (id from parent)                      | content=Followed, rcolumn=NotFollowed |
| `.unfollowUser`                      | POST `Unfollow`                                     | same |
| `#addTweet`                          | FormData POST `AddTweet` (text + optional image)    | content=Timeline |
| `#tweetImageInput` change            | show filename + preview; enable `#clearTweetImage`  | — |
| `#clearTweetImage`                   | reset file input, hide preview                      | — |
| `.delTweet`                          | POST `DelTweet` (id from parent)                    | whichever iterator is present |
| `.banUser`/`.unbanUser`              | POST Ban/Unban (id+`.banReason`)                    | content=GestioUsuaris |
| `.likeTweet`                         | POST `LikeTweet` (toggle)                           | current iterator |
| `.commentTweetBtn`                   | POST `AddTweet` w/ `parentId` + `.commentText`      | current iterator |
| `#editProfileBtn` / `#cancelEditProfile` | toggle `#profileViewDiv`/`#profileEditDiv`      | — |
| `#editProfileForm` submit            | POST `UpdateProfile` (JSON resp)                    | lcolumn=Profile, nav=Menu |
| `.editTweetBtn`/`.cancelTweetEdit`   | toggle inline edit container                        | — |
| `.editTweetImage` change             | show selected filename in `.editImgName`             | — |
| `.saveTweetEdit`                     | FormData POST `EditTweet` (content + optional image/removeImage flag) | current iterator |
| `.removeImageBtn`                    | POST `RemoveTweetImage` (admin only, id from parent) | current iterator |

**Critical**: these ids/classes are the contract between JS and the JSPs. Restyling must
keep them. The iterators the JS looks for: `#feedIterator`, `#iterator`, `#globalFeedIterator`,
`#userWallIterator` (+ hidden `#userWallId`).

### Server = classic MVC behind each AJAX call
`controller/` servlets → `service/` (business logic, singletons) → `repository/` (SQLite).
Session holds the logged-in `User` (`sessionScope.user`).

**Models**:
- `User`: `id, roleId, email, name, surname, nickname, password, picture, birthDate, favoriteGame, role (String), banned (boolean)`
- `Tweet`: `id, uid, uname, upicture, parentId, postDateTime, content, image, likeCount, liked, banned, comments[]`
  — `formattedTime()` returns `"dd MMM yyyy, HH:mm"`. `banned` = true when the author is in the `bans` table.
- `UserRepository.findNotFollowed()` has an `includeAll` flag: when `true` (admin), banned users are NOT filtered out of the suggestions list.

`Menu` servlet picks the fragment by role: `MenuNotLogged` / `MenuLogged` / `MenuAdmin`
(role `ADMINISTRATOR`) / `MenuBanned`. `Content` returns `Login` (anon), `Timeline.jsp`
(logged), or `Banned.jsp` (banned).

### Views (fragments, not full pages)
- **Feeds**: `Feed.jsp`→`FeedTweets.jsp` (friends), `GlobalFeed.jsp`→`GlobalFeedTweets`,
  `Timeline.jsp`→`Tweets.jsp` (own posts + compose box), `UserWall.jsp`→`UserWallTweets`
  (reuses `FeedTweets.jsp`). Feed has Friend/Global toggle tabs (hidden for ADMINISTRATOR).
- **Tweet card** (`FeedTweets.jsp` / `Tweets.jsp`): avatar image, author link (→UserWall,
  or grey non-clickable + "banned" badge for banned authors), timestamp (dd MMM yyyy, HH:mm),
  content, optional tweet image (`<div class="tweet-img-wrap">`), inline edit box (with
  image replace/remove controls), Like/Unlike (count), Edit+Delete (owner or admin),
  admin "Remove image" button (if image present), one level of nested comments, reply input.
- **Users**: `Profile.jsp` (own profile view + edit form, in `#lcolumn`),
  `NotFollowed.jsp` (suggestions + name search, `#rcolumn`; Follow button hidden for
  banned users and admins), `Followed.jsp` (buddies; banned followees shown non-clickable),
  `UserWall.jsp` (public profile).
- **Auth**: `Login.jsp`, `Register.jsp`, `Welcome.jsp`.
- **Admin**: `GestioUsuaris.jsp` (ban/unban with reason + name/email search including
  banned users). `Banned.jsp` (suspended notice). `MenuAdmin` shows "Who to Follow" list
  that includes banned users (admin needs to see everyone).

## Tweet images

Optional single image per tweet. Storage: `src/main/webapp/img/tweets/{tweet_id}.{ext}`.
Served as static files alongside user avatars. Relative path stored in `tweets.image` column.

**Accepted formats**: `.jpg` / `.jpeg` / `.svg` only (PNG rejected — too heavy).
Client-side: file inputs have `accept=".jpg,.jpeg,.svg"`; the `change` handler checks the extension
and on failure clears the input, shows "Only .jpg or .svg files are allowed." in red
(`.img-type-error` CSS class, `!important` needed to beat `.muted`), and shows no preview.
Server-side: `TweetService.getExtension()` returns `null` for disallowed types and skips saving.

**Schema**: `image VARCHAR(255)` — nullable, no DEFAULT. Old `INSERT INTO tweets` statements
that omit the column get `NULL` automatically (SQLite behaviour). All 5 SELECT queries
(`findById`, `findByUser`, `findByFollowedUsers`, `findAllGlobalTweets`, `findReplies`)
include `t.image`; `mapTweet()` sets `tweet.setImage(rs.getString("image"))`.

**Servlets involved**:
- `AddTweet` — `@MultipartConfig`; reads `Part tweetImage`; calls `tweetService.add(tweet, filePart, imgDir)`.
- `EditTweet` — `@MultipartConfig`; reads `Part tweetImage` + `String removeImage`; calls `tweetService.update(id, content, filePart, imgDir, removeImage)`.
- `DelTweet` — passes `getRealPath("/")` to service so the image file is deleted alongside the tweet.
- `RemoveTweetImage` — admin-only, `@WebServlet("/RemoveTweetImage")`; calls `tweetService.removeTweetImage(tweetId, webappRoot)`.

**TweetService helpers**: `add()` saves file after INSERT (uses generated id as filename);
`update()` handles three outcomes (remove / replace / keep); `deleteImageFile()` uses
`Files.deleteIfExists()` with the relative DB path resolved against the webapp root.

**CSS**: `.tweet-img-wrap { height: 260px; border-radius: 12px; overflow: hidden; }` +
`img { object-fit: cover; width/height 100%; }` — "image fill" aesthetic sizing.

## DB persistence across restarts

`DBManager` deletes `lab4.db` on every server start and re-executes `DB.txt`.
`DBLogger` appends each INSERT/UPDATE/DELETE at runtime to `DB.txt` so data survives restarts.
Image files in `src/main/webapp/img/tweets/` are static and also survive.

## Feed ordering

All feeds (`findByFollowedUsers`, `findAllGlobalTweets`, `findByUser`) order by
`t.created_at DESC`. The timestamp bug (SQLite sorting TEXT before INTEGER when seed data
uses TEXT and runtime used `setTimestamp()` which stores INTEGER ms-epoch) was fixed by
switching `TweetRepository` to `setString()` with `TS_FMT` (`"yyyy-MM-dd HH:mm:ss"`),
making all values TEXT that sort lexicographically = chronologically.

## Role-based UI summary

| Feature                  | Guest | Registered | Banned | Admin |
|--------------------------|-------|------------|--------|-------|
| See global feed          | ✓     | ✓          | ✗      | ✓     |
| lcolumn on Global        | Login | Profile    | —      | Profile |
| Friend/Global tabs       | ✗     | ✓          | ✗      | ✗ (hidden) |
| Post / like / comment    | ✗     | ✓          | ✗      | ✓     |
| Follow button (Who to Follow) | ✗ | ✓        | ✗      | ✗     |
| Edit/delete own tweets   | ✗     | ✓          | ✗      | ✓ all |
| Remove tweet image       | ✗     | ✗          | ✗      | ✓     |
| Who to Follow includes banned | ✗ | ✗        | ✗      | ✓     |
| Admin panel (GestioUsuaris) | ✗  | ✗          | ✗      | ✓     |

## What ACTUALLY exists (the only things we may surface in the UI)

- Auth: register (name, surname, nickname, email, password, favoriteGame, birthDate,
  picture upload), login, logout.
- Profile: view + edit (name, nickname, email, favoriteGame); also shows birthDate, role,
  picture. **No** follower/following counts, bio, location, website, "joined" date,
  Following/Buddies link (removed from Profile.jsp).
- Tweets: post (with optional .jpg/.svg image), edit (own/admin; can replace or remove
  image), delete (own/admin; deletes image file too), like/unlike, one-level reply/comment.
  Fields: `id, uid, uname, upicture, parentId, postDateTime, content, image, likeCount,
  liked, banned, comments[]`. Timestamp displayed as `formattedTime` (dd MMM yyyy, HH:mm).
  Banned authors shown as grey non-clickable text + "banned" badge.
- Follow / unfollow + suggestions (with client-side name search) + buddies list.
  Banned followees shown non-clickable in Followed.jsp.
- Admin: ban / unban users with a reason; search by name or email (includes banned users);
  see all users in "Who to Follow" including banned; remove inappropriate tweet images.

## Mockup features that DO NOT exist in the backend — must NOT be added
(User constraint: don't add buttons/functionality the code doesn't implement.)

- Share button on tweets.
- Emoji / poll / calendar compose toolbar icons — purely decorative in mockup.
- Profile banner with bio / location / website / joined / follower & following counts.
- `GestioTweets` admin tweet table + servlet, tweet "Reported"/"Removed" statuses.
- Admin per-row "view" / "edit" icon buttons (no backend).
- Letter/gradient avatars — the app uses **uploaded profile images**; keep `<img>`.
- "Popular" / "Most liked" / trending tabs, "What's happening" card.

## Restyle approach
Port the dark mockup look (`mockups/index.html` tokens) into `css/style.css` and the JSP
markup, **changing only the look**. Keep every JS hook id/class. Lean on CSS overrides of
W3.CSS classes so untouched fragments still render dark. Tokens:
`--bg #0E1217, --surface #181D24, --surface-2 #1F262E, --border #262E38, --text #E7E9EA,
--muted #8B98A5, --accent #6E56CF, --accent-2 #5A46B5, --like #F91880, --success #00BA7C,
--danger #EF4444`. Font: Inter. Icons: Font Awesome 6.
