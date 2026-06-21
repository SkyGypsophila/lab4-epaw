<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="w3-card card w3-section">
  <div class="page-header">
    <div class="titles">
      <h1><i class="fa-solid fa-users"></i> User management</h1>
      <p>Admin panel — ban or unban users.</p>
    </div>
  </div>
</div>

<c:choose>
<c:when test="${empty users}">
  <div class="w3-card card empty-state"><p>No users found.</p></div>
</c:when>
<c:otherwise>
<div class="w3-card card who-card">
<c:forEach var="u" items="${users}">
  <div id="${u.id}" class="user-row">
    <img src="${u.picture}" alt="Avatar" class="uavatar">
    <div class="uinfo">
      <b>${u.name}
        <c:if test="${not empty u.nickname}"><span class="muted">@${u.nickname}</span></c:if>
      </b>
      <span>${u.email}</span>
    </div>
    <div class="row-actions">
      <span class="status ${u.banned ? 'banned' : 'ok'}">${u.banned ? 'Banned' : 'Active'}</span>
      <c:choose>
        <c:when test="${u.banned}">
          <span class="muted w3-small"><i class="fa-solid fa-circle-info"></i> ${u.banReason}</span>
          <button type="button" class="unbanUser btn sm"><i class="fa-solid fa-rotate-left"></i> Unban</button>
        </c:when>
        <c:otherwise>
          <input type="text" class="banReason" placeholder="Reason (optional)">
          <button type="button" class="banUser btn sm danger"><i class="fa-solid fa-ban"></i> Ban</button>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</c:forEach>
</div>
</c:otherwise>
</c:choose>
</content>
