'use strict';
define(['tutorFinder', 'services/professorService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('TimeslotCtrl', TimeslotCtrl);
	
	TimeslotCtrl.$inject = ['$scope', '$uibModalInstance', 'professorService', 'toastService', 'authService', '$location', '$route'];
	function TimeslotCtrl($scope, $modal, professorService, toastService, authService, $location, $route) {
		
		$scope.availableStartTimes = [];
		$scope.availableEndTimes = [];
		$scope.timeslot = {day: undefined, start: undefined, end: undefined};
		$scope.firstSelect = false;

		this.initStartTimes = function() {
			for (var i = 7; i <= 22; i++) {
				$scope.availableStartTimes.push(i);
			}
		};

		this.initStartTimes();

		$scope.startSelected = function() {
			if (parseInt($scope.timeslot.end, 10) <= parseInt($scope.timeslot.start, 10)) {
				$scope.timeslot.end = undefined;
			}
			
			if ($scope.timeslot.start) {
				$scope.firstSelect = true;
				var endTimes = [];
				for (var i = (parseInt($scope.timeslot.start, 10) + 1); i <= 23; i++) {
					endTimes.push(i);
				}
				$scope.availableEndTimes = endTimes;
			}
		};

		$scope.submit = function(form) {
			if (form.$valid) {
				professorService.addTimeslot($scope.timeslot.day, $scope.timeslot.start, $scope.timeslot.end)
				.then(function() {
					$scope.timeslot = {day: undefined, start: undefined, end: undefined};
					form.$setPristine();
					$modal.close(true);
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
								fun: professorService.addTimeslot,
								params: [$scope.timeslot.day, $scope.timeslot.start, $scope.timeslot.end],
								message: 'ERROR_ADDING_TIMESLOT'
							});
							$location.url('/login');
							$modal.close(false);
							break;
						}
						default: toastService.showAction('ERROR_ADDING_TIMESLOT'); break;
					}
				});
			}
		};

		$scope.close = function() {
			$modal.close(false);
		};
	};
});
