'use strict';
define(['tutorFinder', 'services/courseService'], function(tutorFinder) {

	tutorFinder.controller('ReserveClassCtrl', ReserveClassCtrl);	
	ReserveClassCtrl.$inject = ['$scope', '$uibModalInstance', 'courseService', 'course'];
	function ReserveClassCtrl($scope, $modal, courseService, course) {

		$scope.minDate = new Date();
		$scope.availableDays = function(date) {
			// TODO: Match with schedule
			return true;
		};

		$scope.availableStartTimes = [];
		$scope.availableEndTimes = [];
		$scope.validRange = true;
		$scope.reservation = {date: undefined, start: undefined, end: undefined};
		$scope.firstSelect = false;

		this.initStartTimes = function() {
			for (var i = 7; i <= 22; i++) {
				$scope.availableStartTimes.push(i);
			}
		};

		this.initStartTimes();

		$scope.startSelected = function() {
			if (parseInt($scope.reservation.end, 10) <= parseInt($scope.reservation.start, 10)) {
				$scope.reservation.end = undefined;
			}
			
			if ($scope.reservation.start) {
				$scope.firstSelect = true;
				var endTimes = [];
				for (var i = (parseInt($scope.reservation.start, 10) + 1); i <= 23; i++) {
					endTimes.push(i);
				}
				$scope.availableEndTimes = endTimes;
			}
		};

		$scope.close = function() {
			$modal.close(false);
		};

		$scope.submit = function(form) {
			if (form.$valid && $scope.validRange) {
				courseService.reserve(course.professor.id, course.subject.id, $scope.reservation.date, 
					$scope.reservation.start, $scope.reservation.end)
				.then(function() {
					$scope.reservation = {date: undefined, start: undefined, end: undefined};
					form.$setPristine();
					$modal.close(true);
				})
				.catch(function(err) {
					console.log(err);
				});
			}
		};
	};
});
