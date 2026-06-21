window.App = window.App || {};
App.initRegisterValidation = function (serverErrors) {

	// Defer so jQuery finishes inserting the fragment into the DOM
	// before we try to query elements with getElementById
	setTimeout(function () {

		const form = document.getElementById('registerForm');
		const initialPassword = document.getElementById('initialPassword');
		const confirmPassword = document.getElementById('confirmPassword');

		if (!form || !initialPassword || !confirmPassword) return;

		const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

		function checkPasswordStrength() {
			initialPassword.setCustomValidity(
				initialPassword.value && !PASSWORD_REGEX.test(initialPassword.value)
					? "Minimum 8 characters, including uppercase, numbers, and a special character (@#$%^&*)."
					: ""
			);
		}

		function checkPasswordsMatch() {
			confirmPassword.setCustomValidity(
				confirmPassword.value && confirmPassword.value !== initialPassword.value
					? "Passwords do not match."
					: ""
			);
		}

		initialPassword.addEventListener('input', function () {
			checkPasswordStrength();
			checkPasswordsMatch();
		});
		confirmPassword.addEventListener('input', checkPasswordsMatch);

		// Apply server-side errors
		Object.entries(serverErrors).forEach(function ([field, message]) {
			const input = document.getElementsByName(field)[0];
			if (input) {
				input.setCustomValidity(message);
				input.reportValidity();
				input.addEventListener('input', function () {
					input.setCustomValidity('');
					input.reportValidity();
				});
			}
		});

		form.addEventListener('submit', function (event) {
			checkPasswordStrength();
			checkPasswordsMatch();
			if (!form.checkValidity()) {
				event.preventDefault();
				form.reportValidity();
			}
		});

	}, 0);

};
