<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty tweets}">
  <div class="w3-card card empty-state">
    <i class="fa-regular fa-pen-to-square"></i>
    <p>You haven't posted anything yet. Write your first post above!</p>
  </div>
</c:when>
<c:otherwise>
<c:forEach var="t" items="${tweets}">
  <div id="${t.id}" class="w3-card tweet card w3-animate-opacity">
    <img src="${user.picture}" alt="Avatar" class="avatar md">
    <div class="body">
      <div class="head">
        <a href="UserWall?id=${t.uid}" class="menu name">${t.uname}</a>
        <span class="time"><i class="fa-regular fa-clock"></i> ${t.postDateTime}</span>
      </div>

      <p class="tweetText content">${t.content}</p>

      <div class="editTweetContainer" style="display:none; margin-bottom: 10px;">
        <textarea class="editTweetText w3-input w3-border w3-round">${t.content}</textarea>
        <div class="actions" style="margin-top:8px;">
          <button type="button" class="saveTweetEdit act"><i class="fa-solid fa-check"></i> Save</button>
          <button type="button" class="cancelTweetEdit act"><i class="fa-solid fa-xmark"></i> Cancel</button>
        </div>
      </div>

      <div class="actions">
        <c:choose>
          <c:when test="${t.liked}">
            <button type="button" class="likeTweet act liked"><i class="fa-solid fa-heart"></i> ${t.likeCount}</button>
          </c:when>
          <c:otherwise>
            <button type="button" class="likeTweet act"><i class="fa-regular fa-heart"></i> ${t.likeCount}</button>
          </c:otherwise>
        </c:choose>
        <c:if test="${t.uid == user.id || user.role == 'ADMINISTRATOR'}">
          <button type="button" class="editTweetBtn act edit"><i class="fa-solid fa-pen-to-square"></i> Edit</button>
          <button type="button" class="delTweet act danger"><i class="fa-solid fa-trash"></i> Delete</button>
        </c:if>
      </div>

      <c:if test="${not empty t.comments}">
        <div class="replies">
        <c:forEach var="c" items="${t.comments}">
          <div id="${c.id}" class="reply">
            <img src="${c.upicture}" alt="Avatar" class="avatar sm">
            <div class="body">
              <div class="head">
                <a href="UserWall?id=${c.uid}" class="menu name">${c.uname}</a>
                <span class="time">${c.postDateTime}</span>
              </div>
              <p class="content">${c.content}</p>
              <c:if test="${c.uid == user.id || user.role == 'ADMINISTRATOR'}">
                <div class="actions">
                  <button type="button" class="delTweet act danger"><i class="fa-solid fa-trash"></i></button>
                </div>
              </c:if>
            </div>
          </div>
        </c:forEach>
        </div>
      </c:if>

      <div class="reply-box">
        <input type="text" class="commentText" placeholder="Reply to your own post…">
        <button type="button" class="commentTweetBtn btn sm"><i class="fa-solid fa-reply"></i></button>
      </div>
    </div>
  </div>
</c:forEach>
</c:otherwise>
</c:choose>
</content>
