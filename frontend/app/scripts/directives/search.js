'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.directive('search', function() {
		return {
			restrict: 'E',
			replace: 'true',
			templateUrl: 'views/searchResults.html',
			scope: {category: '=', term: '='},
			controller: ['$scope', function($scope) {

			}]
		};
	});
});
