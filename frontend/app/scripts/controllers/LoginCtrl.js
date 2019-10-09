'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('LoginCtrl', LoginCtrl);
	
	LoginCtrl.$inject = ['$scope', '$rootScope'];
	function LoginCtrl($scope, $rootScope) {
		$scope.msg = 'This is the login view';

		$rootScope.appendTitle('LOGIN');		
	};
});
