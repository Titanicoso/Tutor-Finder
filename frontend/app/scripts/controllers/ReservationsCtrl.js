'use strict';
define(['tutorFinder', 'services/userService'], function(tutorFinder) {

	tutorFinder.controller('ReservationsCtrl', ReservationsCtrl);
	
	ReservationsCtrl.$inject = ['$scope', '$rootScope', 'userService', 'toastService', '$location', '$route'];
	function ReservationsCtrl($scope, $rootScope, userService, toastService, $location, $route) {
		$rootScope.appendTitle('RESERVATIONS');

		var page = parseInt($route.current.params.page, 10);
		var self = this;

		$scope.current = {};
		$scope.current.page = page !== page ? 1 : page;
		$scope.current.previous = $scope.current.page;

		$scope.$on('$routeUpdate', function() {
			var page = parseInt($route.current.params.page, 10);
			page = page !== page ? 1 : page;
			if ($scope.current.previous !== page) {
				self.getReservations(page);
			}
		});

		$scope.getPage = function(number) {
			$location.search({page: number});
		};

		this.getReservations = function(page) {
			userService.getFullReservations(page)
			.then(function(reservations) {
				$scope.reservations = reservations;
				$scope.current.page = page;
				$scope.current.previous = page;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		this.getReservations($scope.current.page);
	};
});
