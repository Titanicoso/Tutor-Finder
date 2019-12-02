'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope', '$location'];
	function HomeCtrl($scope, $rootScope, $location) {
		$rootScope.appendTitle('HOME');

		$scope.query = {query: '', category: 'course'};

		$scope.search = function() {
			$location.path('/searchResults').search($scope.query);
		};
	};
});
