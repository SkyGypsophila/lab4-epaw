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

<div class="w3-container w3-card w3-round w3-white w3-section w3-padding">
    <h6 class="w3-opacity">Find Users</h6>
    <input type="text" id="searchUsersInput" class="w3-input w3-border w3-round" placeholder="Search by name...">
</div>

<div id="suggestionsContainer">
<c:choose>
<c:when test="${empty users}">
<p class="w3-opacity w3-center">No suggestions</p>
</c:when>
<c:otherwise>
<c:forEach var="u" items="${users}">       
<div id="${u.id}" class="suggestion-card w3-container w3-card w3-round w3-white w3-center w3-section" data-name="${u.name}">
	<p>Friend Suggestion</p>
    <img src="${u.picture}" alt="Avatar" style="width:50%"><br>
    <div><a href="UserWall?id=${u.id}" class="menu w3-hover-text-theme" style="text-decoration:none;">${u.name}</a></div>
   	<button type="button" class="followUser w3-row w3-button w3-green w3-section"><i class="fa-solid fa-user-plus"></i> &nbsp;Follow</button> 
</div>
</c:forEach>
</c:otherwise>
</c:choose>
</div>