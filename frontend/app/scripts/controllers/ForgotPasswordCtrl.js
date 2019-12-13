'use strict';
define(['tutorFinder', 'services/userService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('ForgotPasswordCtrl', ForgotPasswordCtrl);
	
	ForgotPasswordCtrl.$inject = ['$scope', '$rootScope', '$route', '$location', 'userService', 'authService', 'toastService'];
	function ForgotPasswordCtrl($scope, $rootScope, $route, $location, userService, authService, toastService) {
		$rootScope.appendTitle('FORGOT_PASSWORD');
		var token = $route.current.params.token;
		$scope.newPassword = false;
		$scope.validToken = false;
		$scope.restoreResponse = {};

		if (token) {
			$scope.newPassword = true;
			userService.forgotPasswordValidity(token)
			.then(function() {
				$scope.validToken = true;
			})
			.catch(function() {
				$scope.validToken = false;
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
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						default: $scope.restoreResponse.error = true; break;
					}
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
