<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${user != null}">
<div id="${user.id}" class="w3-container w3-card w3-round w3-white w3-section w3-center">
  <div id="profileViewDiv" style="padding-bottom: 10px;">
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
    <button type="button" id="editProfileBtn" class="w3-button w3-theme w3-round w3-small" style="width:100%;"><i class="fa fa-pencil"></i> Edit Profile</button>
  </div>

  <div id="profileEditDiv" style="display:none; text-align: left; padding: 10px 0;">
    <h4>Edit Profile</h4>
    <form id="editProfileForm">
      <div class="w3-section">
        <label class="w3-small"><b>Username</b></label>
        <input class="w3-input w3-border w3-round" type="text" id="editName" value="${user.name}" required>
        <span class="profile-error w3-text-red w3-small" id="error-name"></span>
      </div>
      <div class="w3-section">
        <label class="w3-small"><b>Nickname</b></label>
        <input class="w3-input w3-border w3-round" type="text" id="editNickname" value="${user.nickname}" required>
        <span class="profile-error w3-text-red w3-small" id="error-nickname"></span>
      </div>
      <div class="w3-section">
        <label class="w3-small"><b>Email</b></label>
        <input class="w3-input w3-border w3-round" type="email" id="editEmail" value="${user.email}" required>
        <span class="profile-error w3-text-red w3-small" id="error-email"></span>
      </div>
      <div class="w3-section">
        <label class="w3-small"><b>Favorite Game</b></label>
        <input class="w3-input w3-border w3-round" type="text" id="editFavoriteGame" value="${user.favoriteGame}">
        <span class="profile-error w3-text-red w3-small" id="error-favoriteGame"></span>
      </div>
      <button type="submit" class="w3-button w3-theme w3-round w3-small">Save</button>
      <button type="button" id="cancelEditProfile" class="w3-button w3-red w3-round w3-small">Cancel</button>
    </form>
  </div>
</div>
<br>
</c:when>
<c:otherwise>
<p/>
</c:otherwise>
</c:choose>
