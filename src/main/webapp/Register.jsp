<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<form id="registerForm" action="Register" method="POST" enctype="multipart/form-data">

    <div>
        <label for="name" class="w3-text-theme">Username</label>
        <input class="w3-input w3-border w3-light-grey" type="text" id="name" name="name" required minlength="5" maxlength="20"
            value="${user.name}" title="Username must be between 5 and 20 characters." />
    </div>

    <div>
        <label for="surname" class="w3-text-theme">Surname</label>
        <input class="w3-input w3-border w3-light-grey" type="text" id="surname" name="surname" required minlength="1" maxlength="20"
            value="${user.surname}" title="Surname must be between 1 and 20 characters." />
    </div>

    <div>
        <label for="nickname" class="w3-text-theme">Nickname</label>
        <input class="w3-input w3-border w3-light-grey" type="text" id="nickname" name="nickname" required minlength="1" maxlength="20"
            value="${user.nickname}" title="Nickname cannot be empty." />
    </div>

    <div>
        <label for="email" class="w3-text-theme">Email</label>
        <input class="w3-input w3-border w3-light-grey" type="email" id="email" name="email" required
            value="${user.email}" title="Must be a valid email." />
    </div>

    <div>
        <label for="password" class="w3-text-theme">Password</label>
        <input class="w3-input w3-border w3-light-grey" type="password" id="password" name="password" required
            pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$" value="${user.password}"
            title="Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*)." />
    </div>

    <div>
        <label for="confirmPassword" class="w3-text-theme">Repeat password</label>
        <input class="w3-input w3-border w3-light-grey" type="password" id="confirmPassword"
            name="confirmPassword" required value="${user.password}"
            title="Passwords must match" />
    </div>

    <div>
        <label for="favoriteGame" class="w3-text-theme">Favorite Game</label>
        <input class="w3-input w3-border w3-light-grey" type="text" id="favoriteGame" name="favoriteGame"
            value="${user.favoriteGame}" />
    </div>

    <div>
        <label for="birthDate" class="w3-text-theme">Birth Date</label>
        <input class="w3-input w3-border w3-light-grey" type="date" id="birthDate" name="birthDate" required
            value="${user.birthDate}" title="You must enter a birth date." />
    </div>

    <div>
        <label for="picture" class="w3-text-theme">Profile Picture</label>
        <input class="w3-input w3-border w3-light-grey" type="file" id="picture" name="picture" accept="image/*" />
    </div>

    <button type="submit" class="w3-button w3-theme w3-section">Submit Registration</button>

</form>

<script>
	App.Errors = {
		  <c:forEach var="error" items="${errors}">
		    "${error.key}": "${error.value}",
		  </c:forEach>
	};
    App.initRegisterValidation(App.Errors);
</script>
