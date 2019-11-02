'use strict';
define(['tutorFinder','services/sampleService'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope','sampleService'];
	function HomeCtrl($scope, $rootScope, service) {
		$rootScope.appendTitle('HOME');
		service.areas();
		$scope.msg = 'This is your homepage';
	};
});
