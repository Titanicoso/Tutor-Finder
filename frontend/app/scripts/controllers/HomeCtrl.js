'use strict';
define(['tutorFinder', 'directives/search'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope'];
	function HomeCtrl($scope, $rootScope) {
		$rootScope.appendTitle('HOME');

		$scope.query = {text: '', category: 'course'};
		$scope.complete = false;

		$scope.search = function() {
		};
	};
});
