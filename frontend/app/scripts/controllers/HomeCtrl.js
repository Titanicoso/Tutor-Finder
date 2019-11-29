'use strict';
define(['tutorFinder', 'directives/search'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope', '$location'];
	function HomeCtrl($scope, $rootScope, $location) {
		$rootScope.appendTitle('HOME');

		$scope.query = {text: '', category: 'course'};

		$scope.search = function() {
			$location.path('/searchResults').search({q: $scope.query.text, c: $scope.query.category});
		};
	};
});
