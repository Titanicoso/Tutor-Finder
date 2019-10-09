'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('RegisterCtrl', RegisterCtrl);
	
	RegisterCtrl.$inject = ['$scope', '$rootScope'];
	function RegisterCtrl($scope, $rootScope) {
		$scope.msg = 'This is the register view';
		
		$rootScope.appendTitle('REGISTER');
	};
});
