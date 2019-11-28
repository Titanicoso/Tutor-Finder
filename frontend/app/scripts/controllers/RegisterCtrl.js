'use strict';
define(['tutorFinder', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('RegisterCtrl', RegisterCtrl);
	
	RegisterCtrl.$inject = ['$scope', '$rootScope', '$location', 'authService'];
	function RegisterCtrl($scope, $rootScope, $location, authService) {
		$rootScope.appendTitle('REGISTER');
		$scope.regex = '[a-zA-ZñÑáéíóúÁÉÍÓÚ\s]+';
		$scope.register = {name: '', lastname: '', email: '', username: '', password: '', repeatPassword: ''};

		$scope.submit = function(form) {
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
				$location.url('/');
			})
			.catch(function(err) {
				console.log(err);
			});
		};
	};
});
