'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ModifyCourseCtrl', ModifyCourseCtrl);
	
	ModifyCourseCtrl.$inject = ['$scope', '$rootScope'];
	function ModifyCourseCtrl($scope, $rootScope) {

		$rootScope.appendTitle('MODIFY_COURSE');
	};
});
