'use strict';
define(['tutorFinder', 'services/professorService'], function(tutorFinder) {

	tutorFinder.controller('RequestsCtrl', RequestsCtrl);
	
	RequestsCtrl.$inject = ['$scope', '$rootScope', 'professorService'];
	function RequestsCtrl($scope, $rootScope, professorService) {
		$rootScope.appendTitle('REQUESTS');

		var currentPage = 1;

		$scope.getPage = function(number) {
			professorService.getFullRequests(number)
			.then(function(requests) {
				$scope.requests = requests;
				currentPage = number;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.approve = function(request) {
			professorService.approveRequest(request.id)
			.then(function() {
				request.status = 'APPROVED';
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.deny = function(request) {
			professorService.denyRequest(request.id)
			.then(function() {
				request.status = 'DENIED';
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.getPage(currentPage);
	};
});
