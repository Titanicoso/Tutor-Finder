'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('CreateCourseCtrl', CreateCourseCtrl);
	
	CreateCourseCtrl.$inject = ['$scope', '$rootScope'];
	function CreateCourseCtrl($scope, $rootScope) {

		$rootScope.appendTitle('CREATE_COURSE');
	};
});
