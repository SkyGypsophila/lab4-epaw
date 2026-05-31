<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script type="text/javascript">
$(document).ready(function(){
	$('#lcolumn').html('<p/>');
	$('#rcolumn').html('<p/>');
});
</script>

<div class="w3-container w3-padding-24 w3-white w3-center">
  <div class="w3-panel w3-red w3-padding-24 w3-round-large">
    <h2><i class="fa fa-ban"></i> Account Banned</h2>
    <hr class="w3-border-white">
    <p class="w3-large">Your account has been suspended by an administrator.</p>
    <c:if test="${not empty user.banReason}">
      <p><strong>Reason:</strong> ${user.banReason}</p>
    </c:if>
    <p class="w3-opacity">If you believe this is a mistake, please contact the platform administrators.</p>
  </div>
</div>
