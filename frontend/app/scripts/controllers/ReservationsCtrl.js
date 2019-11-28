'use strict';
define(['tutorFinder', 'services/userService'], function(tutorFinder) {

	tutorFinder.controller('ReservationsCtrl', ReservationsCtrl);
	
	ReservationsCtrl.$inject = ['$scope', '$rootScope', 'userService'];
	function ReservationsCtrl($scope, $rootScope, userService) {
		$rootScope.appendTitle('RESERVATIONS');

		var currentPage = 1;

		$scope.getPage = function(number) {
			userService.getFullReservations(number)
			.then(function(reservations) {
				$scope.reservations = reservations;
				currentPage = number;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.getPage(currentPage);
	};
});
