# Frontend Implementation — Next Steps

Goal: port the dark-mode mockups in [`mockups/index.html`](index.html) into the real JSP app without rewriting backend logic. The 3-column AJAX architecture in [`src/main/webapp/index.html`](../src/main/webapp/index.html) stays exactly as it is — we are only changing the **look**.

Work in this order. Each step is independently shippable, so you can stop at any point and still have a coherent UI.

---

## Step 1 · Swap in design tokens and fonts

**Files**
- `src/main/webapp/index.html`
- `src/main/webapp/css/style.css`

**What to do**

1. In `index.html`, replace the W3.CSS blue theme line:
   ```html
   <link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-blue.css">
   ```
   with **nothing** — we are overriding the theme in our own CSS.

2. Keep the W3.CSS base (`w3.css`) for the grid (`w3-row-padding`, `w3-col m3/m6`) — it's doing real work and removing it would explode the layout.

3. Add Inter + Font Awesome 6 in `<head>`:
   ```html
   <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
   <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
   ```
   You can remove the FA 4.7 line — FA 6 is backwards compatible for the icons we use.

4. At the top of `style.css`, add the token block (copy from the mockup):
   ```css
   :root {
     --bg: #0E1217;
     --surface: #181D24;
     --surface-2: #1F262E;
     --border: #262E38;
     --text: #E7E9EA;
     --muted: #8B98A5;
     --accent: #6E56CF;
     --accent-2: #5A46B5;
     --like: #F91880;
     --success: #00BA7C;
     --danger: #EF4444;
   }
   body { background: var(--bg); color: var(--text); font-family: 'Inter', sans-serif; }
   ```

**Done when**: the page loads dark with Inter font, even before any other changes.

---

## Step 2 · Override W3.CSS where it fights the theme

**File**: `css/style.css`

W3.CSS classes we use (`w3-theme`, `w3-card`, `w3-white`, `w3-button`, `w3-input`) all paint things light. Override the few that matter:

```css
.w3-theme        { background: var(--bg) !important; color: var(--text) !important; }
.w3-white        { background: var(--surface) !important; color: var(--text) !important; }
.w3-card         { background: var(--surface) !important; border: 1px solid var(--border); border-radius: 12px; box-shadow: none !important; }
.w3-button       { border-radius: 999px; font-weight: 700; }
.w3-input        { background: var(--surface); color: var(--text); border: 1px solid var(--border); border-radius: 12px; }
.w3-input:focus  { border-color: var(--accent); outline: none; }
footer.w3-theme  { background: var(--bg) !important; border-top: 1px solid var(--border); color: var(--muted); }
```

> Keep the existing `input.w3-input:valid` / `:invalid` rules — they're the green/red border validation already used by `LoginValidation.js`. Just confirm they still look right against the new dark inputs (they will).

**Done when**: existing screens look dark but still functional — no layout shift.

---

## Step 3 · Update menu fragments

**Files**
- `MenuNotLogged.html`
- `MenuLogged.html`
- `MenuAdmin.html`
- `MenuBanned.html`

The mockup's top bar / left nav is what the menu fragment becomes. The simplest move: keep the existing `w3-bar` structure (because `index.html` loads it into `#navigation`) but restyle:

```css
#navigation {
  background: var(--bg);
  border-bottom: 1px solid var(--border);
  padding: 8px 24px;
}
#navigation .w3-bar-item {
  color: var(--muted);
  border-radius: 999px;
  padding: 8px 14px;
}
#navigation .w3-bar-item:hover { background: var(--surface-2); color: var(--text); }
#navigation .w3-bar-item.active { color: var(--text); background: var(--surface-2); }
```

**`MenuLogged.html`** — no Settings entry, keep it minimal:

```html
<a class="menu w3-bar-item w3-button" href="Feed"><i class="fa-solid fa-house"></i> Home</a>
<a class="menu w3-bar-item w3-button" href="GlobalFeed"><i class="fa-solid fa-globe"></i> Global</a>
<a class="menu w3-bar-item w3-button" href="Profile"><i class="fa-solid fa-user"></i> Profile</a>
<a class="menu w3-bar-item w3-button" href="Followed"><i class="fa-solid fa-user-group"></i> Following</a>
<a class="menu w3-bar-item w3-button" href="Logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
```

**`MenuNotLogged.html`** — topbar is just the logo + the two auth buttons. **No** Global / Explore / About inline links:

```html
<div class="logo"><div class="logo-mark">C</div></div>
<div class="auth">
  <a class="menu btn ghost" href="Login">Log in</a>
  <a class="menu btn" href="Register">Sign up</a>
</div>
```

The anonymous left column (`#lcolumn`) only contains a single **Global** nav item — no Popular / Tags / About.

**`MenuAdmin.html`** — only 3 entries: Users, Tweets, Back to app:

```html
<a class="menu w3-bar-item w3-button" href="GestioUsuaris"><i class="fa-solid fa-users"></i> Users</a>
<a class="menu w3-bar-item w3-button" href="GestioTweets"><i class="fa-solid fa-comment-dots"></i> Tweets</a>
<a class="menu w3-bar-item w3-button" href="Feed"><i class="fa-solid fa-arrow-left"></i> Back to app</a>
```

**Done when**: navigation looks like the mockup's top bar on every page state.

---

## Step 4 · Restyle the tweet card

**Files**: `FeedTweets.jsp`, `Tweets.jsp`, plus the partial that renders each tweet (search for the loop with `.w3-card`).

The mockup tweet has 3 parts: avatar (left), header row (name + handle + time), content, and an actions row (reply / like / share). Adapt the existing markup:

```html
<div class="w3-card tweet" id="${tweet.id}">
  <div class="avatar md av-violet">${fn:substring(tweet.username, 0, 1)}</div>
  <div class="body">
    <div class="head">
      <span class="name">${tweet.name}</span>
      <span class="muted">@${tweet.username}</span>
      <span class="dot"></span>
      <span class="muted">${tweet.timeAgo}</span>
    </div>
    <div class="content">${tweet.content}</div>
    <div class="actions">
      <button class="act commentTweetBtn"><i class="fa-regular fa-comment"></i> ${tweet.replyCount}</button>
      <button class="act likeTweet ${tweet.liked ? 'liked' : ''}"><i class="fa-solid fa-heart"></i> ${tweet.likes}</button>
      <c:if test="${tweet.ownedBy == user.id}">
        <button class="act delTweet"><i class="fa-solid fa-trash"></i></button>
      </c:if>
    </div>
  </div>
</div>
```

Copy the `.tweet`, `.avatar`, `.head`, `.content`, `.actions`, `.act.liked` CSS straight from the mockup. **The existing JS event handlers (`.likeTweet`, `.commentTweetBtn`, `.delTweet`) keep working — class names are unchanged.**

Avatar color: pick deterministically from `username.hashCode() % 7` so the same user always gets the same color (the mockup defines 7: `av-violet`, `av-pink`, `av-green`, `av-blue`, `av-amber`, `av-red`, `av-teal`).

**Done when**: a single tweet looks like the mockup. Then it propagates everywhere.

---

## Step 5 · Compose box and page headers

**Files**: `Feed.jsp`, `GlobalFeed.jsp`, `Timeline.jsp`

Replace the current `<h4>` headers with the page-header pattern from the mockup (title + sub + tab buttons), and put the compose form inside its own `.compose` card right under the header.

The compose markup:

```html
<form id="composeForm" class="w3-card compose">
  <div class="avatar md av-violet">${user.initial}</div>
  <div class="field">
    <textarea id="tweetContent" placeholder="What's happening?" maxlength="280"></textarea>
    <div class="compose-bar">
      <div class="tools">
        <i class="fa-regular fa-image"></i>
        <i class="fa-regular fa-face-smile"></i>
      </div>
      <span class="count">280</span>
      <button id="addTweet" class="btn">Tweet</button>
    </div>
  </div>
</form>
```

The existing `$('#addTweet').click(...)` handler in `index.html` continues to work as-is.

**Done when**: the home feed page matches frame 03 of the mockup.

---

## Step 6 · Profile and right rail

**Files**
- `Profile.jsp` — left column (`#lcolumn`) profile card
- `NotFollowed.jsp` — right column "Who to follow"
- A new dedicated profile screen if you want the banner-style view from frame 04 (optional — the existing mini-profile in `#lcolumn` is enough for the mandatory requirement)

For the small left-column profile card, just restyle the existing card with the new tokens (avatar + name + @handle + stats row + Edit button).

For the full profile page (frame 04), you can reuse `Profile.jsp` and render it in `#content` instead of `#lcolumn` when the route is "view another user's page". The banner is just `<div class="profile-banner"></div>` with the gradient — pure CSS, no images needed.

`NotFollowed.jsp` — wrap each suggested user in a `.who-row` and the Follow button stays `class="btn sm followUser"` so the existing JS handler still fires.

The right rail contains **only** the search bar and the Who-to-Follow card. There is no "What's happening" / trends card — it was cut from the mockup to keep scope tight (no trending data on the backend).

**Done when**: every page has the right rail and the profile screen matches the mockup.

---

## Step 7 · Login, Register, Welcome

**Files**: `Welcome.jsp`, `Login.jsp`, `Register.jsp`

The split hero + tabbed form from frame 02 is the goal, but a single-column centered version is fine if you want to save effort — the grading rubric doesn't reward this screen specifically.

Minimum change: wrap the form in `.auth-form`, swap `w3-input` for the styled inputs, and use the `.btn.lg.full` for the submit. Use `.field-group label` for labels — the green/red `:valid` / `:invalid` borders still work because they're scoped to `input.w3-input`.

**Done when**: login and register look dark and consistent.

---

## Step 8 · Admin panel

**Files**: `GestioUsuaris.jsp`, `Banned.jsp`, and a new `GestioTweets.jsp` (+ matching servlet)

The admin panel has **two** tables side-by-side in scope: **Users** and **Tweets**. No stats cards, no analytics — keep it lean.

### 8a · Users table (`GestioUsuaris.jsp`)

Frame 05's user table is the target. The existing per-user row already has `class="banUser"` / `class="unbanUser"` and the `<input class="banReason">` — keep all of those. Just wrap each row in `.user-row` with the new grid template, and add the status pill (`status ok` / `warn` / `banned`):

```html
<div class="user-row" id="${u.id}">
  <div class="avatar sm av-violet">${fn:substring(u.username,0,1)}</div>
  <div class="uinfo"><b>${u.name}</b><span>@${u.username} · ${u.tweetCount} tweets</span></div>
  <span class="status ${u.banned ? 'banned' : 'ok'}">${u.banned ? 'Banned' : 'Active'}</span>
  <span class="when">${u.lastActive}</span>
  <div class="row-actions">
    <button class="icon-btn"><i class="fa-regular fa-eye"></i></button>
    <button class="icon-btn"><i class="fa-solid fa-pen-to-square"></i></button>
    <c:choose>
      <c:when test="${u.banned}">
        <button class="icon-btn unbanUser" style="color:var(--success)"><i class="fa-solid fa-rotate-left"></i></button>
      </c:when>
      <c:otherwise>
        <button class="icon-btn danger banUser"><i class="fa-solid fa-ban"></i></button>
      </c:otherwise>
    </c:choose>
  </div>
</div>
```

### 8b · Tweets table (`GestioTweets.jsp` — new)

Same layout as Users but for posts: avatar of the author · tweet preview + author handle + stats · status pill (`Visible` / `Reported` / `Removed`) · timestamp · view / edit / delete actions.

You need a new servlet `GestioTweets` that returns the JSP fragment (mirror of `GestioUsuaris`). The delete action can reuse the existing `DelTweet` servlet — admin just hits it with any tweet id, not just their own. Add an admin guard in `DelTweet.doPost` (`if (session.role == ADMIN || tweet.ownerId == session.userId) ...`).

```html
<div class="user-row" id="${t.id}">
  <div class="avatar sm av-pink">${fn:substring(t.username,0,1)}</div>
  <div class="uinfo"><b>"${fn:substring(t.content,0,60)}..."</b><span>@${t.username} · ${t.likes} likes · ${t.replies} replies</span></div>
  <span class="status ${t.removed ? 'banned' : (t.reported ? 'warn' : 'ok')}">
    ${t.removed ? 'Removed' : (t.reported ? 'Reported' : 'Visible')}
  </span>
  <span class="when">${t.timeAgo}</span>
  <div class="row-actions">
    <button class="icon-btn"><i class="fa-regular fa-eye"></i></button>
    <button class="icon-btn"><i class="fa-solid fa-pen-to-square"></i></button>
    <button class="icon-btn danger delTweet"><i class="fa-solid fa-trash"></i></button>
  </div>
</div>
```

The `Banned.jsp` page (shown to a banned user when they try to log in) just needs the dark theme — no structural change.

**Done when**: admin nav has only Users / Tweets / Back to app, both tables look like frame 05, and existing ban/unban/delete handlers still work.

---

## Step 9 · Polish pass

Once everything is in:

- Avoid stray `!important` flags by removing W3.CSS classes you no longer need (e.g. `w3-theme-blue` references in JSPs).
- Set `<meta name="theme-color" content="#0E1217">` so the browser chrome on mobile matches.
- Sanity check at 1280 px and 768 px widths — the W3.CSS `w3-hide-small` already hides the side columns on mobile, so the center will become full-width automatically. Verify the compose and tweet cards don't overflow.
- Run the [WAI Easy Checks](https://www.w3.org/WAI/test-evaluate/preliminary/) on Home, Profile and Login — most failures will be color contrast on muted text (the palette is already above AA, but verify with the actual rendered output).

---

## What we are deliberately **not** doing

- Not rewriting the AJAX layer — `App.bindEvents()` in `src/main/webapp/index.html` keeps every handler.
- Not changing servlet URLs or form names — the look changes, the wiring doesn't.
- Not building a component library — straight CSS classes only. Easy to grade, easy to debug.
- Not introducing a build step. Plain CSS, plain JSPs.

---

## Order summary (for the report)

| Step | File group | Visual impact | Risk |
| ---- | ---------- | ------------- | ---- |
| 1    | tokens + fonts | Whole site goes dark | low |
| 2    | W3 overrides   | Cards/buttons match | low |
| 3    | menus          | Top nav matches | low |
| 4    | tweet card     | Feed looks right | medium (touches partial markup) |
| 5    | compose + header | Home feed done | low |
| 6    | profile + rail | Profile + sidebar done | medium |
| 7    | auth screens   | Login/Register polished | low |
| 8    | admin panel    | Admin done | medium |
| 9    | polish         | Production-ready | low |

Stop after step 5 and you already have ~80 % of the visual upside. Steps 6–9 are extra polish for the demo.
