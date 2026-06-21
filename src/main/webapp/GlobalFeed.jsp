<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	<c:choose>
	<c:when test="${not empty sessionScope.user}">
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	</c:when>
	<c:otherwise>
	$('#lcolumn').load('Login');
	</c:otherwise>
	</c:choose>
	$('#globalFeedIterator').load('GlobalFeedTweets');

	$(document).off("click", "#showFriendFeedGlobal").on("click", "#showFriendFeedGlobal", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showGlobalFeedGlobal").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitleGlobal").html('<i class="fa-solid fa-house"></i> Friend Feed');
		$("#feedSubtitleGlobal").text('Latest posts from people you follow');
		$('#globalFeedIterator').load('FeedTweets');
	});

	$(document).off("click", "#showGlobalFeedGlobal").on("click", "#showGlobalFeedGlobal", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showFriendFeedGlobal").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitleGlobal").html('<i class="fa-solid fa-globe"></i> Global Feed');
		$("#feedSubtitleGlobal").text('See what is happening across the whole network');
		$('#globalFeedIterator').load('GlobalFeedTweets');
	});
});
</script>

<div class="w3-card card w3-section">
	<div class="page-header">
		<div class="titles">
			<h1 id="feedTitleGlobal"><i class="fa-solid fa-globe"></i> Global Feed</h1>
			<p id="feedSubtitleGlobal">See what is happening across the whole network</p>
		</div>
		<c:if test="${not empty sessionScope.user && sessionScope.user.role != 'ADMINISTRATOR'}">
		<div class="tabs">
			<button id="showFriendFeedGlobal" type="button" class="w3-button w3-transparent">Friends</button>
			<button id="showGlobalFeedGlobal" type="button" class="w3-button w3-theme">Global</button>
		</div>
		</c:if>
	</div>
</div>

<div id="globalFeedIterator"></div>
</content>
