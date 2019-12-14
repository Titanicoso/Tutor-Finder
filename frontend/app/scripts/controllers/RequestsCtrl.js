'use strict';
define(['tutorFinder', 'services/professorService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('RequestsCtrl', RequestsCtrl);
	
	RequestsCtrl.$inject = ['$scope', '$rootScope', 'professorService', 'toastService', '$location', '$route', 'authService'];
	function RequestsCtrl($scope, $rootScope, professorService, toastService, $location, $route, authService) {
		$rootScope.appendTitle('REQUESTS');

		var page = parseInt($route.current.params.page, 10);
		var self = this;

		$scope.current = {};
		$scope.current.page = page !== page ? 1 : page;
		$scope.current.previous = $scope.current.page;

		$scope.$on('$routeUpdate', function() {
			var page = parseInt($route.current.params.page, 10);
			page = page !== page ? 1 : page;
			if ($scope.current.previous !== page) {
				self.getRequests(page);
			}
		});

		$scope.getPage = function(number) {
			$location.search({page: number});
		};

		this.getRequests = function(page) {
			$scope.loading = true;
			professorService.getFullRequests(page)
			.then(function(requests) {
				$scope.requests = requests;
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

		$scope.approve = function(request) {
			professorService.approveRequest(request.id)
			.then(function() {
				request.status = 'APPROVED';
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
						authService.setRequestRedo({
							fun: professorService.approveRequest,
							params: [request.id],
							message: 'ERROR_APPROVING'
						});
						$location.url('/login');
						break;
					}
					default: toastService.showAction('ERROR_APPROVING'); break;
				}
			});
		};

		$scope.deny = function(request) {
			professorService.denyRequest(request.id)
			.then(function() {
				request.status = 'DENIED';
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
						authService.setRequestRedo({
							fun: professorService.denyRequest,
							params: [request.id],
							message: 'ERROR_DENYING'
						});
						$location.url('/login');
						break;
					}
					default: toastService.showAction('ERROR_DENYING'); break;
				}
			});
		};

		this.getRequests($scope.current.page);
	};
});
