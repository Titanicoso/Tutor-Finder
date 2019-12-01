'use strict';
define(['tutorFinder', 'services/userService'], function(tutorFinder) {

	tutorFinder.controller('ReservationsCtrl', ReservationsCtrl);
	
	ReservationsCtrl.$inject = ['$scope', '$rootScope', 'userService', 'toastService'];
	function ReservationsCtrl($scope, $rootScope, userService, toastService) {
		$rootScope.appendTitle('RESERVATIONS');

		$scope.currentPage = 1;

		$scope.getPage = function(number) {
			userService.getFullReservations(number)
			.then(function(reservations) {
				$scope.reservations = reservations;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.getPage($scope.currentPage);
	};
});
