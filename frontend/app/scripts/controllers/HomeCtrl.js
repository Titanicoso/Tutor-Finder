'use strict';
define(['tutorFinder','services/sampleService'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope','sampleService'];
	function HomeCtrl($scope, $rootScope, sampleService) {
		$rootScope.appendTitle('HOME');
		sampleService.get('areas')
		.then(
			function(data) {
				$scope.areas = data;
			}
		).catch(
			function(err) {
				console.log(err);
			}
		);
		$scope.msg = 'This is your homepage';
	};
});
