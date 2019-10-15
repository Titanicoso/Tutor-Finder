'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ResetPasswordCtrl', ResetPasswordCtrl);
	
	ResetPasswordCtrl.$inject = ['$scope', '$rootScope'];
	function ResetPasswordCtrl($scope, $rootScope) {

		$rootScope.appendTitle('RESET_PASSWORD');
	};
});
