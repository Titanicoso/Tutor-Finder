'use strict';
define(['tutorFinder'], function(tutorFinder) {
	tutorFinder.directive('schedule', function() {
		return {
			restrict: 'E',
			replace: 'true',
			templateUrl: 'views/components/schedule.html',
			scope: {schedule: '='}
		};
	});
});