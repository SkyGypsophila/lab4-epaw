<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#lcolumn').load('Profile');
});
</script>

<div class="w3-card card w3-section">
	<div class="page-header">
		<div class="titles">
			<h1><i class="fa-solid fa-circle-check" style="color:var(--success)"></i> Login successful</h1>
			<p>Hello <strong>${user.name}</strong>, you can now enjoy all the features.</p>
		</div>
	</div>
</div>
</content>
