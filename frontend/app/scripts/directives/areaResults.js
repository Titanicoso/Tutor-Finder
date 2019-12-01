'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.directive('areaResults', function() {
		return {
			restrict: 'E',
			replace: 'true',
			templateUrl: 'views/components/areaResults.html',
			scope: {results: '='}
		};
	});
});
