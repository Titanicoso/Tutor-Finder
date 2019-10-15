'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ForgotPasswordCtrl', ForgotPasswordCtrl);
	
	ForgotPasswordCtrl.$inject = ['$scope', '$rootScope'];
	function ForgotPasswordCtrl($scope, $rootScope) {

		$rootScope.appendTitle('HOME');
	};
});
