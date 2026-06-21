<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$(document).off("input", "#searchUsersInput").on("input", "#searchUsersInput", function() {
		var val = $(this).val().toLowerCase();
		$(".suggestion-card").each(function() {
			var name = $(this).attr("data-name").toLowerCase();
			if (name.indexOf(val) !== -1) {
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	});
});
</script>

<div class="search w3-section">
	<i class="fa-solid fa-magnifying-glass"></i>
	<input type="text" id="searchUsersInput" placeholder="Search users">
</div>

<div class="w3-card card who-card">
	<div class="who-title">
		<h3 class="card-title">Who to follow</h3>
		<p class="card-sub">Discover people you might like</p>
	</div>
	<div id="suggestionsContainer">
	<c:choose>
	<c:when test="${empty users}">
		<div class="who-row"><span class="muted">No suggestions</span></div>
	</c:when>
	<c:otherwise>
	<c:forEach var="u" items="${users}">
		<div id="${u.id}" class="suggestion-card who-row" data-name="${u.name}">
			<img src="${u.picture}" alt="Avatar" class="avatar sm">
			<div class="meta">
				<b><a href="UserWall?id=${u.id}" class="menu">${u.name}</a></b>
				<span>Friend suggestion</span>
			</div>
			<button type="button" class="followUser btn sm"><i class="fa-solid fa-user-plus"></i> Follow</button>
		</div>
	</c:forEach>
	</c:otherwise>
	</c:choose>
	</div>
</div>
</content>
