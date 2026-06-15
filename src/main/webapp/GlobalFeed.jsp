<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	$('#globalFeedIterator').load('GlobalFeedTweets');
	
	$(document).off("click", "#showFriendFeedGlobal").on("click", "#showFriendFeedGlobal", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showGlobalFeedGlobal").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitleGlobal").html('<i class="fa-solid fa-globe"></i> Friend Feed');
		$("#feedSubtitleGlobal").text('Latest posts from people you follow');
		$('#globalFeedIterator').load('FeedTweets');
	});
	
	$(document).off("click", "#showGlobalFeedGlobal").on("click", "#showGlobalFeedGlobal", function(){
		$(this).addClass("w3-theme").removeClass("w3-transparent");
		$("#showFriendFeedGlobal").removeClass("w3-theme").addClass("w3-transparent");
		$("#feedTitleGlobal").html('<i class="fa-solid fa-users"></i> Global Feed');
		$("#feedSubtitleGlobal").text('Descubre lo que está pasando en toda la red');
		$('#globalFeedIterator').load('GlobalFeedTweets');
	});
});
</script>

<c:if test="${not empty sessionScope.user}">
<div class="w3-bar w3-card w3-round w3-margin-bottom" style="background-color: rgba(30, 41, 59, 0.9); padding: 5px;">
  <button id="showFriendFeedGlobal" class="w3-bar-item w3-button w3-transparent w3-round w3-margin-right w3-mobile">Friend Feed</button>
  <button id="showGlobalFeedGlobal" class="w3-bar-item w3-button w3-theme w3-round w3-mobile">Global Feed</button>
</div>
</c:if>

<div class="w3-container w3-card w3-round w3-white w3-section">
	<h4 id="feedTitleGlobal"><i class="fa-solid fa-users"></i> Global Feed</h4>
	<p id="feedSubtitleGlobal" class="w3-opacity">Descubre lo que está pasando en toda la red</p>
</div>
<div id="globalFeedIterator"></div>