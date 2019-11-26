'use strict';
define(['tutorFinder','services/areaService'], function(tutorFinder) {

	tutorFinder.controller('HomeCtrl', HomeCtrl);
	
	HomeCtrl.inject = ['$scope', '$rootScope','areaService'];
	function HomeCtrl($scope, $rootScope, areaService) {
		$rootScope.appendTitle('HOME');
		areaService.getAreas('', 1)
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
