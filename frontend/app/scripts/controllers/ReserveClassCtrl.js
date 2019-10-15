'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ReserveClassCtrl', ReserveClassCtrl);
	
	ReserveClassCtrl.$inject = ['$scope', '$rootScope'];
	function ReserveClassCtrl($scope, $rootScope) {

		$rootScope.appendTitle('RESERVECLASS');
	};
});
