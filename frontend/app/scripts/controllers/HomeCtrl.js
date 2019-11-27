'use strict';
define(['tutorFinder', 'directives/search'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope'];
	function HomeCtrl($scope, $rootScope) {
		$rootScope.appendTitle('HOME');

		$scope.searchTerm = '';
		$scope.selectOption = 'course';
		$scope.complete = false;

		$scope.search = function() {
		};
	};
});
