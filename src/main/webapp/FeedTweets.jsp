<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty tweets}">
  <div class="w3-card card empty-state">
    <i class="fa-regular fa-comments"></i>
    <p>No posts yet. Follow some users to see their tweets here!</p>
  </div>
</c:when>
<c:otherwise>
<c:forEach var="t" items="${tweets}">
  <div id="${t.id}" class="w3-card tweet card w3-animate-opacity">
    <img src="${t.upicture}" alt="Avatar" class="avatar md">
    <div class="body">
      <div class="head">
        <c:choose>
          <c:when test="${t.banned}">
            <span class="name muted">${t.uname} <span class="status banned" style="font-size:0.75em">banned</span></span>
          </c:when>
          <c:otherwise>
            <a href="UserWall?id=${t.uid}" class="menu name">${t.uname}</a>
          </c:otherwise>
        </c:choose>
        <span class="time"><i class="fa-regular fa-clock"></i> ${t.formattedTime}</span>
      </div>

      <p class="tweetText content">${t.content}</p>

      <c:if test="${not empty t.image}">
        <div class="tweet-img-wrap">
          <img src="${t.image}" alt="Tweet image">
        </div>
      </c:if>

      <div class="editTweetContainer" style="display:none; margin-bottom: 10px;">
        <textarea class="editTweetText w3-input w3-border w3-round">${t.content}</textarea>
        <c:if test="${not empty t.image}">
          <div class="current-img-row" style="margin-top:8px; display:flex; align-items:center; gap:8px;">
            <img src="${t.image}" alt="" style="height:48px; width:72px; object-fit:cover; border-radius:6px;">
            <label style="display:flex; align-items:center; gap:4px; cursor:pointer;">
              <input type="checkbox" class="removeImageCheck"> Remove image
            </label>
          </div>
        </c:if>
        <div style="margin-top:8px;">
          <label class="btn sm ghost" style="cursor:pointer;">
            <i class="fa-solid fa-image"></i> Replace image
            <input type="file" class="editTweetImage" accept=".jpg,.jpeg,.svg" style="display:none">
          </label>
          <span class="editImgName muted" style="font-size:0.8em; margin-left:6px;"></span>
        </div>
        <div class="actions" style="margin-top:8px;">
          <button type="button" class="saveTweetEdit act"><i class="fa-solid fa-check"></i> Save</button>
          <button type="button" class="cancelTweetEdit act"><i class="fa-solid fa-xmark"></i> Cancel</button>
        </div>
      </div>

      <div class="actions">
        <c:choose>
          <c:when test="${empty sessionScope.user}">
            <span class="act"><i class="fa-regular fa-heart"></i> ${t.likeCount}</span>
          </c:when>
          <c:otherwise>
            <button type="button" class="likeTweet act ${t.liked ? 'liked' : ''}">
              <i class="${t.liked ? 'fa-solid' : 'fa-regular'} fa-heart"></i> ${t.likeCount}
            </button>
          </c:otherwise>
        </c:choose>
        <c:if test="${t.uid == sessionScope.user.id || sessionScope.user.role == 'ADMINISTRATOR'}">
          <button type="button" class="editTweetBtn act edit"><i class="fa-solid fa-pen-to-square"></i> Edit</button>
          <button type="button" class="delTweet act danger"><i class="fa-solid fa-trash"></i> Delete</button>
        </c:if>
        <c:if test="${sessionScope.user.role == 'ADMINISTRATOR' && not empty t.image}">
          <button type="button" class="removeImageBtn act danger"><i class="fa-solid fa-image"></i> Remove image</button>
        </c:if>
      </div>

      <c:if test="${not empty t.comments}">
        <div class="replies">
        <c:forEach var="c" items="${t.comments}">
          <div id="${c.id}" class="reply">
            <img src="${c.upicture}" alt="Avatar" class="avatar sm">
            <div class="body">
              <div class="head">
                <c:choose>
                  <c:when test="${c.banned}">
                    <span class="name muted">${c.uname} <span class="status banned" style="font-size:0.75em">banned</span></span>
                  </c:when>
                  <c:otherwise>
                    <a href="UserWall?id=${c.uid}" class="menu name">${c.uname}</a>
                  </c:otherwise>
                </c:choose>
                <span class="time">${c.formattedTime}</span>
              </div>
              <p class="content">${c.content}</p>
              <c:if test="${c.uid == sessionScope.user.id || sessionScope.user.role == 'ADMINISTRATOR'}">
                <div class="actions">
                  <button type="button" class="delTweet act danger"><i class="fa-solid fa-trash"></i></button>
                </div>
              </c:if>
            </div>
          </div>
        </c:forEach>
        </div>
      </c:if>

      <c:if test="${not empty sessionScope.user}">
        <div class="reply-box">
          <input type="text" class="commentText" placeholder="Write a reply…">
          <button type="button" class="commentTweetBtn btn sm"><i class="fa-solid fa-reply"></i></button>
        </div>
      </c:if>
    </div>
  </div>
</c:forEach>
</c:otherwise>
</c:choose>
</content>
