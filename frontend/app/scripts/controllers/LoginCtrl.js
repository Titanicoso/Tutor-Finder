'use strict';
define(['tutorFinder', 'services/authService', 'services/toastService'], function(tutorFinder) {

	tutorFinder.controller('LoginCtrl', LoginCtrl);
	
	LoginCtrl.$inject = ['$scope', '$rootScope', '$location', 'authService', 'toastService'];
	function LoginCtrl($scope, $rootScope, $location, authService, toastService) {
		$rootScope.appendTitle('LOGIN');

		$scope.loginForm = { 
			username: '', 
			password: '', 
			rememberMe: false 
		};

		$scope.error = false;
		var self = this;
	
		$scope.login = function() {
			if ($scope.form.$valid) {
				$scope.error = false;
				authService.login($scope.loginForm.username, $scope.loginForm.password, $scope.loginForm.rememberMe)
				.then(function() {
					var redirect = authService.getRedirectUrl();
					var requestRedo = authService.getRequestRedo();

					if (requestRedo !== undefined) {
						var request = requestRedo.fun.apply(null, requestRedo.params);
						request.then(function() {
							self.redirect(redirect);
						})
						.catch(function(err) {
							if (requestRedo.errorMessage) {
								toastService.showAction(requestRedo.errorMessage);
							} else {
								toastService.showAction(requestRedo.message);
							}
							self.redirect(redirect);
						});
					} else {
						self.redirect(redirect);
					}
				})
				.catch(function(err) {
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						default: $scope.error = true; break;
					}
				});
			}
		};

		this.redirect = function(redirect) {
			if (redirect && redirect.url) {
				$location.path(redirect.url).search(redirect.params);
			} else {
				$location.url('/');
			}
		};
	};

});
