'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.directive('professorResults', function() {
		return {
			restrict: 'E',
			replace: 'true',
			templateUrl: 'views/components/professorResults.html',
			scope: {results: '='}
		};
	});
});
