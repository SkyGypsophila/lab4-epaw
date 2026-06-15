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

<div class="w3-container w3-card w3-round w3-white w3-section w3-center">
  <c:choose>
    <c:when test="${not empty targetUser}">
      <input type="hidden" id="userWallId" value="${targetUser.id}" />
      <h4>Public Profile: ${targetUser.name}</h4>
      <c:if test="${not empty targetUser.picture}">
        <p><img src="${targetUser.picture}" class="w3-circle" style="height:106px;width:106px" alt="Avatar"></p>
      </c:if>
      <hr>
      <p class="w3-left-align"><i class="fa fa-id-card fa-fw w3-margin-right"></i> ${targetUser.name} ${targetUser.surname}</p>
      <c:if test="${not empty targetUser.nickname}">
        <p class="w3-left-align"><i class="fa fa-at fa-fw w3-margin-right"></i> ${targetUser.nickname}</p>
      </c:if>
      <c:if test="${not empty targetUser.email}">
        <p class="w3-left-align"><i class="fa fa-envelope fa-fw w3-margin-right"></i> ${targetUser.email}</p>
      </c:if>
      <c:if test="${not empty targetUser.favoriteGame}">
        <p class="w3-left-align"><i class="fa fa-gamepad fa-fw w3-margin-right"></i> ${targetUser.favoriteGame}</p>
      </c:if>
      <c:if test="${not empty targetUser.birthDate}">
        <p class="w3-left-align"><i class="fa fa-birthday-cake fa-fw w3-margin-right"></i> ${targetUser.birthDate}</p>
      </c:if>
    </c:when>
    <c:otherwise>
      <p class="w3-opacity">User not found.</p>
    </c:otherwise>
  </c:choose>
</div>

<div class="w3-container w3-card w3-round w3-white w3-section">
	<h4><i class="fa fa-pencil-square-o"></i> Posts</h4>
	<p class="w3-opacity">Historial de publicaciones</p>
</div>

<div id="userWallIterator">
<!-- Target user tweets will be loaded here -->
</div>
