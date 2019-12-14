'use strict';
define(['tutorFinder', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('RegisterCtrl', RegisterCtrl);

	RegisterCtrl.$inject = ['$scope', '$rootScope', '$location', 'authService', 'toastService'];
	function RegisterCtrl($scope, $rootScope, $location, authService, toastService) {
		$rootScope.appendTitle('REGISTER');
    $('#register-name').focus();

    $scope.regex = '[a-zA-ZñÑáéíóúÁÉÍÓÚ\s]+';
		$scope.register = {name: '', lastname: '', email: '', username: '', password: '', repeatPassword: ''};

		$scope.submit = function(form) {
			$scope.repeatedEmail = false;
			$scope.repeatedUsername = false;
			if (!form.$valid || $scope.register.password !== $scope.register.repeatPassword) {
				return;
			}

			authService.register($scope.register.name, $scope.register.lastname, $scope.register.email,
				$scope.register.username, $scope.register.password)
			.then(function(response) {
				return authService.refreshAccessToken(response);
			})
			.then(function() {
				$scope.register = {name: '', lastname: '', email: '', username: '', password: '', repeatPassword: ''};
				var redirect = authService.getRedirectUrl();
				if (redirect.url) {
					$location.path(redirect.url).search(redirect.params);
				} else {
					$location.url('/');
				}
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					case 409: {
						var email = err.data.errors.some(function(error) { 
							return error.field === 'email';
						});

						var username = err.data.errors.some(function(error) { 
							return error.field === 'username';
						});

						if (email) {
							$scope.repeatedEmail = true;
						}
						if (username) {
							$scope.repeatedUsername = true;
						}
						break;
					}
					default: toastService.showAction('OOPS'); break;
				}
			});
		};
	};
});
