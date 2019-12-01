'use strict';
define(['tutorFinder', 'services/professorService'], function(tutorFinder) {

	tutorFinder.controller('RequestsCtrl', RequestsCtrl);
	
	RequestsCtrl.$inject = ['$scope', '$rootScope', 'professorService', 'toastService'];
	function RequestsCtrl($scope, $rootScope, professorService, toastService) {
		$rootScope.appendTitle('REQUESTS');

		var currentPage = 1;

		$scope.getPage = function(number) {
			professorService.getFullRequests(number)
			.then(function(requests) {
				$scope.requests = requests;
				currentPage = number;
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

		$scope.getPage(currentPage);
	};
});
