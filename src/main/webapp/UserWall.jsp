<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	var targetId = $('#userWallId').val();
	if (targetId) {
		$('#userWallIterator').load('UserWallTweets?id=' + targetId);
	}
});
</script>

<div class="w3-card card w3-section">
  <c:choose>
    <c:when test="${not empty targetUser}">
      <input type="hidden" id="userWallId" value="${targetUser.id}" />
      <div class="profile-card">
        <c:if test="${not empty targetUser.picture}">
          <img src="${targetUser.picture}" class="pavatar" alt="Avatar">
        </c:if>
        <h4>${targetUser.name} ${targetUser.surname}</h4>
        <c:if test="${not empty targetUser.nickname}">
          <div class="handle">@${targetUser.nickname}</div>
        </c:if>
        <div class="profile-meta">
          <c:if test="${not empty targetUser.email}">
            <p><i class="fa-solid fa-envelope"></i> ${targetUser.email}</p>
          </c:if>
          <c:if test="${not empty targetUser.favoriteGame}">
            <p><i class="fa-solid fa-gamepad"></i> ${targetUser.favoriteGame}</p>
          </c:if>
          <c:if test="${not empty targetUser.birthDate}">
            <p><i class="fa-solid fa-cake-candles"></i> ${targetUser.birthDate}</p>
          </c:if>
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <p class="muted">User not found.</p>
    </c:otherwise>
  </c:choose>
</div>

<div class="w3-card card w3-section">
	<div class="page-header">
		<div class="titles">
			<h1><i class="fa-solid fa-pen-nib"></i> Posts</h1>
			<p>Post history</p>
		</div>
	</div>
</div>

<div id="userWallIterator">
<!-- Target user tweets will be loaded here -->
</div>
</content>
