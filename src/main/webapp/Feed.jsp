<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	$('#feedIterator').load('FeedTweets');

	$(document).off("click", "#showFriendFeed").on("click", "#showFriendFeed", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showGlobalFeed").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitle").html('<i class="fa-solid fa-house"></i> Friend Feed');
		$("#feedSubtitle").text('Latest posts from people you follow');
		$('#feedIterator').load('FeedTweets');
	});

	$(document).off("click", "#showGlobalFeed").on("click", "#showGlobalFeed", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showFriendFeed").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitle").html('<i class="fa-solid fa-globe"></i> Global Feed');
		$("#feedSubtitle").text('See what is happening across the whole network');
		$('#feedIterator').load('GlobalFeedTweets');
	});
});
</script>

<div class="w3-card card w3-section">
	<div class="page-header">
		<div class="titles">
			<h1 id="feedTitle"><i class="fa-solid fa-house"></i> Friend Feed</h1>
			<p id="feedSubtitle">Latest posts from people you follow</p>
		</div>
		<div class="tabs">
			<button id="showFriendFeed" type="button" class="w3-button w3-theme">Friends</button>
			<button id="showGlobalFeed" type="button" class="w3-button w3-transparent">Global</button>
		</div>
	</div>
</div>

<div id="feedIterator">
<!-- Tweets will be loaded here -->
</div>
</content>
