<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="w3-card card w3-section">
	<form id="loginForm" class="auth-form" action="Login" method="POST">

		<h2>Welcome back</h2>
		<p class="sub">Log in to your Chirp account.</p>

		<div class="field-group">
			<label for="name">Name</label>
			<input type="text" class="w3-input" id="name" name="name" required minlength="5" maxlength="20"
				value="${user.name}" title="Username must be between 5 and 20 characters." />
		</div>

		<div class="field-group">
			<label for="password">Password</label>
			<input type="password" class="w3-input" id="password" name="password" required
				pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$"
				value="${user.password}"
				title="Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*)." />
		</div>

		<button type="submit" class="btn lg full">Log in</button>
		<p class="auth-foot">No account yet? <a href="Register" class="menu">Sign up</a></p>

	</form>
</div>

<script>
	App.Errors = {
	  <c:forEach var="error" items="${errors}">
	    "${error.key}": "${error.value}",
	  </c:forEach>
	};
	App.initLoginValidation(App.Errors);
</script>
</content>
