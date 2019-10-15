'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('CourseCtrl', CourseCtrl);
	
	CourseCtrl.$inject = ['$scope', '$rootScope'];
	function CourseCtrl($scope, $rootScope) {

		$rootScope.appendTitle('COURSE');
	};
});
