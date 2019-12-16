'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.directive('courseResults', function() {
		return {
			restrict: 'E',
			replace: 'true',
			templateUrl: 'views/components/courseResults.html',
			scope: {results: '='}
		};
	});
});
