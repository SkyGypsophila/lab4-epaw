<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w3-container w3-card w3-round w3-white w3-section">
  <h3><i class="fa fa-users"></i> User Management</h3>
  <p class="w3-opacity">Admin panel — ban or unban users.</p>
</div>

<c:choose>
<c:when test="${empty users}">
  <div class="w3-container w3-card w3-round w3-white w3-section w3-padding">
    <p>No users found.</p>
  </div>
</c:when>
<c:otherwise>
<c:forEach var="u" items="${users}">
  <div id="${u.id}" class="w3-container w3-card w3-section w3-round w3-animate-opacity
    <c:if test="${u.banned}">w3-pale-red</c:if>
    <c:if test="${not u.banned}">w3-white</c:if>
  "><br>
    <img src="${u.picture}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
    <h4>
      ${u.name}
      <c:if test="${not empty u.nickname}">
        <span class="w3-opacity w3-small">@${u.nickname}</span>
      </c:if>
      <c:if test="${u.banned}">
        <span class="w3-tag w3-red w3-small w3-round">BANNED</span>
      </c:if>
    </h4>
    <c:if test="${not empty u.email}">
      <span class="w3-opacity w3-small">${u.email}</span>
    </c:if>
    <br>
    <hr class="w3-clear">

    <c:choose>
      <c:when test="${u.banned}">
        <p class="w3-text-red"><i class="fa fa-info-circle"></i> Reason: ${u.banReason}</p>
        <button type="button" class="unbanUser w3-button w3-green w3-margin-bottom w3-round">
          <i class="fa fa-check"></i> &nbsp;Unban
        </button>
      </c:when>
      <c:otherwise>
        <input type="text" class="banReason w3-input w3-border w3-light-grey w3-margin-bottom"
               placeholder="Reason for ban (optional)" style="max-width:400px; display:inline-block" />
        <button type="button" class="banUser w3-button w3-red w3-margin-bottom w3-round">
          <i class="fa fa-ban"></i> &nbsp;Ban
        </button>
      </c:otherwise>
    </c:choose>
  </div>
</c:forEach>
</c:otherwise>
</c:choose>
