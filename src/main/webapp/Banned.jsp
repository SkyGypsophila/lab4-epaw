<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#lcolumn').html('<p></p>');
	$('#rcolumn').html('<p></p>');
});
</script>

<div class="w3-card card w3-section" style="text-align:center; padding:40px 24px;">
	<div style="font-size:40px; color:var(--danger); margin-bottom:12px;"><i class="fa-solid fa-ban"></i></div>
	<h2>Account banned</h2>
	<p class="muted" style="font-size:15px;">Your account has been suspended by an administrator.</p>
	<c:if test="${not empty user.banReason}">
		<p><span class="status banned">Reason</span> &nbsp; ${user.banReason}</p>
	</c:if>
	<p class="muted">If you believe this is a mistake, please contact the platform administrators.</p>
</div>
</content>
