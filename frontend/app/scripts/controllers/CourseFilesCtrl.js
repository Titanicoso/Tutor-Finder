'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('CourseFilesCtrl', CourseFilesCtrl);
	
	ModifyCourseCtrl.$inject = ['$scope', '$rootScope'];
	function CourseFilesCtrl($scope, $rootScope) {

		$rootScope.appendTitle('COURSE_FILES');
	};
});
