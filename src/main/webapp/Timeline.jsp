<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	$('#iterator').load('Tweets');
});
</script>

<div class="w3-card card w3-section">
	<div class="page-header">
		<div class="titles">
			<h1>My posts</h1>
			<p>${user.name}, what are you thinking?</p>
		</div>
	</div>
</div>

<div class="w3-card card compose w3-section">
	<img src="${user.picture}" alt="Avatar" class="avatar md">
	<div class="field">
		<p id="tweetContent" contenteditable="true" data-placeholder="What's happening?"></p>
		<div class="compose-bar">
			<button id="addTweet" type="button" class="btn"><i class="fa-solid fa-feather"></i> &nbsp;Post</button>
		</div>
	</div>
</div>

<div id="iterator">
<!-- Tweets will be loaded here -->
</div>
</content>
