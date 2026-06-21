<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="w3-card card w3-section">
	<form id="registerForm" class="auth-form" action="Register" method="POST" enctype="multipart/form-data">

		<h2>Create your account</h2>
		<p class="sub">Join the EPAW community.</p>

		<div class="field-group">
			<label for="name">Username</label>
			<input class="w3-input" type="text" id="name" name="name" required minlength="5" maxlength="20"
				value="${user.name}" title="Username must be between 5 and 20 characters." />
		</div>

		<div class="field-group">
			<label for="surname">Surname</label>
			<input class="w3-input" type="text" id="surname" name="surname" required minlength="1" maxlength="20"
				value="${user.surname}" title="Surname must be between 1 and 20 characters." />
		</div>

		<div class="field-group">
			<label for="nickname">Nickname</label>
			<input class="w3-input" type="text" id="nickname" name="nickname" required minlength="1" maxlength="20"
				value="${user.nickname}" title="Nickname cannot be empty." />
		</div>

		<div class="field-group">
			<label for="email">Email</label>
			<input class="w3-input" type="email" id="email" name="email" required
				value="${user.email}" title="Must be a valid email." />
		</div>

		<div class="field-group">
			<label for="password">Password</label>
			<input class="w3-input" type="password" id="password" name="password" required
				pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$" value="${user.password}"
				title="Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*)." />
		</div>

		<div class="field-group">
			<label for="confirmPassword">Repeat password</label>
			<input class="w3-input" type="password" id="confirmPassword" name="confirmPassword" required
				value="${user.password}" title="Passwords must match" />
		</div>

		<div class="field-group">
			<label for="favoriteGame">Favorite game</label>
			<input class="w3-input" type="text" id="favoriteGame" name="favoriteGame" value="${user.favoriteGame}" />
		</div>

		<div class="field-group">
			<label for="birthDate">Birth date</label>
			<input class="w3-input" type="date" id="birthDate" name="birthDate" required
				value="${user.birthDate}" title="You must enter a birth date." />
		</div>

		<div class="field-group">
			<label for="picture">Profile picture</label>
			<input class="w3-input" type="file" id="picture" name="picture" accept="image/*" />
		</div>

		<button type="submit" class="btn lg full">Create account</button>
		<p class="auth-foot">Already have an account? <a href="Login" class="menu">Log in</a></p>

	</form>
</div>

<script>
	App.Errors = {
		  <c:forEach var="error" items="${errors}">
		    "${error.key}": "${error.value}",
		  </c:forEach>
	};
	App.initRegisterValidation(App.Errors);
</script>
</content>
