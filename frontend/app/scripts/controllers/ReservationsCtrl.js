'use strict';
define(['tutorFinder', 'services/userService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('ReservationsCtrl', ReservationsCtrl);
	
	ReservationsCtrl.$inject = ['$scope', '$rootScope', 'userService', 'toastService', '$location', '$route', 'authService'];
	function ReservationsCtrl($scope, $rootScope, userService, toastService, $location, $route, authService) {
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
			$scope.loading = true;
			userService.getFullReservations(page)
			.then(function(reservations) {
				$scope.reservations = reservations;
				$scope.current.page = page;
				$scope.current.previous = page;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					case 401: {
						if ($scope.currentUser) {
							toastService.showAction('SESSION_EXPIRED'); 
						}
						authService.setRedirectUrl($location.path(), $route.current.params);
						authService.logout();
						$location.url('/login');
						break;
					}
					default: toastService.showAction('OOPS'); break;
				}
			})
			.then(function() {
				$scope.loading = false;
			});
		};

		this.getReservations($scope.current.page);
	};
});
