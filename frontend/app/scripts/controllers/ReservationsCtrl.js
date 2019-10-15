'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ReservationsCtrl', ReservationsCtrl);
	
	ReservationsCtrl.$inject = ['$scope', '$rootScope'];
	function ReservationsCtrl($scope, $rootScope) {

		$rootScope.appendTitle('RESERVATIONS');
	};
});
