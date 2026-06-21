<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${user != null}">
<div id="${user.id}" class="w3-card card w3-section">
  <div id="profileViewDiv" class="profile-card">
    <img src="${user.picture}" class="pavatar" alt="Avatar">
    <h4>${user.name}</h4>
    <c:if test="${not empty user.nickname}">
      <div class="handle">@${user.nickname}</div>
    </c:if>

    <div class="profile-meta">
      <c:if test="${not empty user.email}">
        <p><i class="fa-solid fa-envelope"></i> ${user.email}</p>
      </c:if>
      <c:if test="${not empty user.favoriteGame}">
        <p><i class="fa-solid fa-gamepad"></i> ${user.favoriteGame}</p>
      </c:if>
      <c:if test="${not empty user.birthDate}">
        <p><i class="fa-solid fa-cake-candles"></i> ${user.birthDate}</p>
      </c:if>
      <c:if test="${not empty user.role}">
        <p><i class="fa-solid fa-shield-halved"></i> ${user.role}</p>
      </c:if>
    </div>

    <button type="button" id="editProfileBtn" class="btn full" style="margin-top:16px;"><i class="fa-solid fa-pen-to-square"></i> &nbsp;Edit profile</button>
  </div>

  <div id="profileEditDiv" style="display:none;">
    <h4>Edit profile</h4>
    <form id="editProfileForm">
      <div class="field-group" style="margin-top:12px;">
        <label>Username</label>
        <input type="text" id="editName" value="${user.name}" required>
        <span class="profile-error w3-text-red w3-small" id="error-name"></span>
      </div>
      <div class="field-group" style="margin-top:12px;">
        <label>Nickname</label>
        <input type="text" id="editNickname" value="${user.nickname}" required>
        <span class="profile-error w3-text-red w3-small" id="error-nickname"></span>
      </div>
      <div class="field-group" style="margin-top:12px;">
        <label>Email</label>
        <input type="email" id="editEmail" value="${user.email}" required>
        <span class="profile-error w3-text-red w3-small" id="error-email"></span>
      </div>
      <div class="field-group" style="margin-top:12px;">
        <label>Favorite game</label>
        <input type="text" id="editFavoriteGame" value="${user.favoriteGame}">
        <span class="profile-error w3-text-red w3-small" id="error-favoriteGame"></span>
      </div>
      <div style="display:flex; gap:8px; margin-top:16px;">
        <button type="submit" class="btn sm"><i class="fa-solid fa-check"></i> Save</button>
        <button type="button" id="cancelEditProfile" class="btn sm ghost">Cancel</button>
      </div>
    </form>
  </div>
</div>
</c:when>
<c:otherwise>
<p></p>
</c:otherwise>
</c:choose>
</content>
