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
		$("#feedTitle").html('<i class="fa-solid fa-globe"></i> Friend Feed');
		$("#feedSubtitle").text('Latest posts from people you follow');
		$('#feedIterator').load('FeedTweets');
	});
	
	$(document).off("click", "#showGlobalFeed").on("click", "#showGlobalFeed", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showFriendFeed").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitle").html('<i class="fa-solid fa-users"></i> Global Feed');
		$("#feedSubtitle").text('Descubre lo que está pasando en toda la red');
		$('#feedIterator').load('GlobalFeedTweets');
	});
});
</script>

<div class="w3-bar w3-card w3-round w3-margin-bottom" style="background-color: rgba(30, 41, 59, 0.9); padding: 5px;">
  <button id="showFriendFeed" class="w3-bar-item w3-button w3-theme w3-round w3-margin-right w3-mobile">Friend Feed</button>
  <button id="showGlobalFeed" class="w3-bar-item w3-button w3-transparent w3-round w3-mobile">Global Feed</button>
</div>

<div class="w3-container w3-card w3-round w3-white w3-section">
	<h4 id="feedTitle"><i class="fa-solid fa-globe"></i> Friend Feed</h4>
	<p id="feedSubtitle" class="w3-opacity">Latest posts from people you follow</p>
</div>

<div id="feedIterator">
<!-- Tweets will be loaded here -->
</div>
