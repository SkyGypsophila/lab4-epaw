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
		<div id="tweetImgPreview" style="display:none; margin-bottom:8px;">
			<div class="tweet-img-wrap" style="height:180px;">
				<img id="tweetImgPreviewImg" src="" alt="Preview">
			</div>
		</div>
		<div class="compose-bar">
			<label class="btn sm ghost" for="tweetImageInput" style="cursor:pointer; margin-right:6px;">
				<i class="fa-solid fa-image"></i>
			</label>
			<input type="file" id="tweetImageInput" accept=".jpg,.jpeg,.svg" style="display:none">
			<span id="tweetImageName" class="muted" style="font-size:0.8em; margin-right:auto;"></span>
			<button id="clearTweetImage" type="button" class="act danger" style="display:none; margin-right:6px;">✕</button>
			<button id="addTweet" type="button" class="btn"><i class="fa-solid fa-feather"></i> &nbsp;Post</button>
		</div>
	</div>
</div>

<div id="iterator">
<!-- Tweets will be loaded here -->
</div>
</content>
