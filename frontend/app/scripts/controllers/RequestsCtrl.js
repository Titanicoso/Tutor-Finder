'use strict';
define(['tutorFinder', 'services/professorService'], function(tutorFinder) {

	tutorFinder.controller('RequestsCtrl', RequestsCtrl);
	
	RequestsCtrl.$inject = ['$scope', '$rootScope', 'professorService', 'toastService', '$location', '$route'];
	function RequestsCtrl($scope, $rootScope, professorService, toastService, $location, $route) {
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
			professorService.getFullRequests(page)
			.then(function(requests) {
				$scope.requests = requests;
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

		$scope.approve = function(request) {
			professorService.approveRequest(request.id)
			.then(function() {
				request.status = 'APPROVED';
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
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
					default: toastService.showAction('ERROR_DENYING'); break;
				}
			});
		};

		this.getRequests($scope.current.page);
	};
});
