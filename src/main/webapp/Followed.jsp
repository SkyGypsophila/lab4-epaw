<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w3-card card w3-section">
	<div class="page-header">
		<div class="titles">
			<h1>Following</h1>
			<p>People you follow</p>
		</div>
	</div>
</div>

<c:choose>
<c:when test="${empty users}">
	<div class="w3-card card empty-state">
		<i class="fa-solid fa-user-group"></i>
		<p>You are not following anyone yet.</p>
	</div>
</c:when>
<c:otherwise>
<div class="w3-card card who-card">
<c:forEach var="u" items="${users}">
	<div id="${u.id}" class="who-row">
		<img src="${u.picture}" alt="Avatar" class="avatar sm">
		<div class="meta">
			<c:choose>
				<c:when test="${u.banned}">
					<b><span class="muted">${u.name} <span class="status banned" style="font-size:0.75em">banned</span></span></b>
				</c:when>
				<c:otherwise>
					<b><a href="UserWall?id=${u.id}" class="menu">${u.name}</a></b>
				</c:otherwise>
			</c:choose>
		</div>
		<button type="button" class="unfollowUser btn sm ghost"><i class="fa-solid fa-user-minus"></i> Unfollow</button>
	</div>
</c:forEach>
</div>
</c:otherwise>
</c:choose>
</content>
