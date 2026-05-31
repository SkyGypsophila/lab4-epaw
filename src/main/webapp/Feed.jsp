<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	$('#feedIterator').load('FeedTweets');
});
</script>

<div class="w3-container w3-card w3-round w3-white w3-section">
	<h4><i class="fa fa-globe"></i> Friend Feed</h4>
	<p class="w3-opacity">Latest posts from people you follow</p>
</div>

<div id="feedIterator">
<!-- Friend tweets will be loaded here -->
</div>
