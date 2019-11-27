'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('SearchCtrl', SearchCtrl);
	
	SearchCtrl.$inject = ['$scope', '$rootScope'];
	function SearchCtrl($scope, $rootScope) {

		$rootScope.appendTitle('SEARCH_RESULTS');
	};
});
