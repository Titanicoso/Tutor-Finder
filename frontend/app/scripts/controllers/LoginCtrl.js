'use strict';
define(['tutorFinder', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('LoginCtrl', LoginCtrl);
	
	LoginCtrl.$inject = ['$scope', '$rootScope', '$location', 'authService'];
	function LoginCtrl($scope, $rootScope, $location, authService) {
		$rootScope.appendTitle('LOGIN');

		$scope.loginForm = { 
			username: '', 
			password: '', 
			rememberMe: false 
		};

		$scope.error = false;
				
		$scope.login = function() {
			if ($scope.form.$valid) {
				
				authService.login($scope.loginForm.username, $scope.loginForm.password, $scope.loginForm.rememberMe)
				.then(function() {
					$location.url('/');
				})
				.catch(function(err) {
					$scope.error = true;
				});
			}
		};
	};
});
