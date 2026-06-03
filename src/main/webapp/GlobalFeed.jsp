<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(document).ready(function(){
	$('#rcolumn').load('NotFollowed');
	$('#lcolumn').load('Profile');
	$('#globalFeedIterator').load('GlobalFeedTweets');
});
</script>
<div class="w3-container w3-card w3-round w3-white w3-section">
	<h4><i class="fa fa-users"></i> Global Feed</h4>
	<p class="w3-opacity">Descubre lo que está pasando en toda la red</p>
</div>
<div id="globalFeedIterator"></div>