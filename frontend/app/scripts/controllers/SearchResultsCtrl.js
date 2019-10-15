'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('SearchResultsCtrl', SearchResultsCtrl);
	
	SearchResultsCtrl.$inject = ['$scope', '$rootScope'];
	function SearchResultsCtrl($scope, $rootScope) {

		$rootScope.appendTitle('SEARCH_RESULTS');
	};
});
