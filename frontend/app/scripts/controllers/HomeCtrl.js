'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope'];
	function HomeCtrl($scope, $rootScope) {
		$rootScope.appendTitle('HOME');

		$scope.msg = 'This is your homepage';
	};
});
