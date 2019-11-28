'use strict';
define(['tutorFinder', 'services/userService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('ForgotPasswordCtrl', ForgotPasswordCtrl);
	
	ForgotPasswordCtrl.$inject = ['$scope', '$rootScope', '$route', '$location', 'userService', 'authService'];
	function ForgotPasswordCtrl($scope, $rootScope, $route, $location, userService, authService) {
		$rootScope.appendTitle('FORGOT_PASSWORD');
		var token = $route.current.params.token;
		$scope.newPassword = false;
		$scope.validToken = false;
		$scope.restoreResponse = {};

		if (token) {
			userService.forgotPasswordValidity(token)
			.then(function() {
				$scope.validToken = true;
			})
			.catch(function() {
				$scope.validToken = false;
			})
			.then(function() {
				$scope.newPassword = true;
			});
			$scope.restoreInput = {password: '', repeatPassword: ''};
		} else {
			$scope.restoreInput = {email: ''};
		}

		$scope.submit = function(form) {
			if (!form.$valid) {
				return;
			}

			if ($scope.newPassword && $scope.restoreInput.password === $scope.restoreInput.repeatPassword) {
				$scope.restoreResponse = {};

				authService.forgotPassword($scope.restoreInput.password, token)
				.then(function(response) {
					return authService.refreshAccessToken(response);
				})
				.then(function() {
					$location.url('/');
				})
				.catch(function(err) {
					console.log(err);
					$scope.restoreResponse.error = true;
				});
				return;
			}
			
			if (!$scope.newPassword) {
				$scope.restoreResponse = {};

				userService.forgotPasswordRequest($scope.restoreInput.email)
				.then(function() {
					$scope.restoreResponse.success = true;
				})
				.catch(function() {
					$scope.restoreResponse.error = true;
				});
			}
		};
	};
});
