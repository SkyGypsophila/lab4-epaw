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

| Selector / id        | Action                              | Reloads |
|----------------------|-------------------------------------|---------|
| `.menu`              | nav click → `#content`.load(href)   | content |
| `form` submit        | generic AJAX POST (multipart)       | nav + content |
| `.followUser`        | POST `Follow` (id from parent)      | content=Followed, rcolumn=NotFollowed |
| `.unfollowUser`      | POST `Unfollow`                     | same |
| `#addTweet`          | POST `AddTweet` (text from `#tweetContent`) | content=Timeline |
| `.delTweet`          | POST `DelTweet` (id from parent)    | whichever iterator is present |
| `.banUser`/`.unbanUser` | POST Ban/Unban (id+`.banReason`) | content=GestioUsuaris |
| `.likeTweet`         | POST `LikeTweet` (toggle)           | current iterator |
| `.commentTweetBtn`   | POST `AddTweet` w/ `parentId` + `.commentText` | current iterator |
| `#editProfileBtn` / `#cancelEditProfile` | toggle `#profileViewDiv`/`#profileEditDiv` | — |
| `#editProfileForm` submit | POST `UpdateProfile` (JSON resp) | lcolumn=Profile, nav=Menu |
| `.editTweetBtn`/`.cancelTweetEdit`/`.saveTweetEdit` | inline tweet edit, POST `EditTweet` | current iterator |

**Critical**: these ids/classes are the contract between JS and the JSPs. Restyling must
keep them. The iterators the JS looks for: `#feedIterator`, `#iterator`, `#globalFeedIterator`,
`#userWallIterator` (+ hidden `#userWallId`).

### Server = classic MVC behind each AJAX call
`controller/` servlets → `service/` (business logic, singletons) → `repository/` (SQLite).
Models: `User`, `Tweet`. Session holds the logged-in `User` (`sessionScope.user`).

`Menu` servlet picks the fragment by role: `MenuNotLogged` / `MenuLogged` / `MenuAdmin`
(role `ADMINISTRATOR`) / `MenuBanned`. `Content` returns `Login` (anon), `Timeline.jsp`
(logged), or `Banned.jsp` (banned).

### Views (fragments, not full pages)
- **Feeds**: `Feed.jsp`→`FeedTweets.jsp` (friends), `GlobalFeed.jsp`→`GlobalFeedTweets`,
  `Timeline.jsp`→`Tweets.jsp` (own posts + compose box), `UserWall.jsp`→`UserWallTweets`
  (reuses `FeedTweets.jsp`). Feed/GlobalFeed have working Friend/Global toggle tabs.
- **Tweet card** (`FeedTweets.jsp` / `Tweets.jsp`): avatar image, author link (→UserWall),
  timestamp, content, inline edit box, Like/Unlike (count), Edit+Delete (owner or admin),
  one level of nested comments, and a reply input. Same loop shape in both files.
- **Users**: `Profile.jsp` (own profile view + edit form, in `#lcolumn`),
  `NotFollowed.jsp` (suggestions + name search, `#rcolumn`), `Followed.jsp` (buddies),
  `UserWall.jsp` (public profile).
- **Auth**: `Login.jsp`, `Register.jsp`, `Welcome.jsp`.
- **Admin**: `GestioUsuaris.jsp` (ban/unban with reason). `Banned.jsp` (suspended notice).

## What ACTUALLY exists (the only things we may surface in the UI)

- Auth: register (name, surname, nickname, email, password, favoriteGame, birthDate,
  picture upload), login, logout.
- Profile: view + edit (name, nickname, email, favoriteGame); also shows birthDate, role,
  picture. **No** follower/following counts, bio, location, website, "joined" date.
- Tweets: post, edit (own/admin), delete (own/admin), like/unlike, one-level reply/comment.
  Fields: `id, uid, uname, upicture, parentId, postDateTime, content, likeCount, liked,
  comments[]`. **No** share, no media, no reply-count field, timestamp is raw `postDateTime`.
- Follow / unfollow + suggestions (with client-side name search) + buddies list.
- Admin: ban / unban users with a reason. **No** tweet-moderation table.

## Mockup features that DO NOT exist in the backend — must NOT be added
(User constraint: don't add buttons/functionality the code doesn't implement.)

- Share button on tweets.
- Compose toolbar icons (image / emoji / poll / calendar) — purely decorative in mockup.
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
</content>
</invoke>
