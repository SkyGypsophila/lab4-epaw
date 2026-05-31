<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${user != null}">
<div id="${user.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
  <h4>My Profile</h4>
  <p><img src="${user.picture}" class="w3-circle" style="height:106px;width:106px" alt="Avatar"></p>
  <hr>
  <p class="w3-left-align"><i class="fa fa-id-card fa-fw w3-margin-right"></i> ${user.name}</p>
  <c:if test="${not empty user.nickname}">
    <p class="w3-left-align"><i class="fa fa-at fa-fw w3-margin-right"></i> ${user.nickname}</p>
  </c:if>
  <c:if test="${not empty user.email}">
    <p class="w3-left-align"><i class="fa fa-envelope fa-fw w3-margin-right"></i> ${user.email}</p>
  </c:if>
  <c:if test="${not empty user.favoriteGame}">
    <p class="w3-left-align"><i class="fa fa-gamepad fa-fw w3-margin-right"></i> ${user.favoriteGame}</p>
  </c:if>
  <c:if test="${not empty user.birthDate}">
    <p class="w3-left-align"><i class="fa fa-birthday-cake fa-fw w3-margin-right"></i> ${user.birthDate}</p>
  </c:if>
  <c:if test="${not empty user.role}">
    <p class="w3-left-align"><i class="fa fa-shield fa-fw w3-margin-right"></i> ${user.role}</p>
  </c:if>
</div>
<br>
</c:when>
<c:otherwise>
<p/>
</c:otherwise>
</c:choose>
