'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ProfileCtrl', ProfileCtrl);
	
	ProfileCtrl.$inject = ['$scope', '$rootScope'];
	function ProfileCtrl($scope, $rootScope) {

		$rootScope.appendTitle('PROFILE');
	};
});
