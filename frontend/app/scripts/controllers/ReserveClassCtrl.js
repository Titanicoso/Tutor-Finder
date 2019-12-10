'use strict';
define(['tutorFinder', 'services/courseService'], function(tutorFinder) {

	tutorFinder.controller('ReserveClassCtrl', ReserveClassCtrl);	
	ReserveClassCtrl.$inject = ['$scope', '$uibModalInstance', 'courseService', 'course', 'schedule'];
	function ReserveClassCtrl($scope, $modal, courseService, course, schedule) {

		$scope.minDate = new Date();
		var self = this;
		var firstSubmit = false;
		$scope.availableDays = function(date) {

			if (!schedule) {
				return false;
			}

			var day = date.getDay();

			var filtered = schedule.days.filter(function(element) {
				return element.day === day;
			});

    		return filtered !== null && filtered.length > 0;
		};

		$scope.availableStartTimes = [];
		$scope.availableEndTimes = [];
		$scope.validRange = true;
		$scope.reservation = {date: undefined, start: undefined, end: undefined};
		$scope.firstSelect = false;
		$scope.reservationError = undefined;

		$scope.daySelected = function() {
			if ($scope.reservation.date === undefined) {
				$scope.availableStartTimes = [];
				$scope.availableEndTimes = [];
				return;
			}

			var day = $scope.reservation.date.getDay();

			var filtered = schedule.days.filter(function(element) {
				return element.day === day;
			});
				
			if (filtered === null || filtered.length < 1) {
				$scope.availableStartTimes = [];
				$scope.availableEndTimes = [];
				return;
			}

			for (var i = 7; i <= 22; i++) {
				var contained = filtered[0].ranges.some(function(range) { 
					return range.start <= i && range.end > i;
				});

				if (contained) {
					$scope.availableStartTimes.push(i);
				}
			}
		};

		this.initStartTimes = function() {
			for (var i = 7; i <= 22; i++) {
				$scope.availableStartTimes.push(i);
			}
		};

		$scope.startSelected = function() {
			if (parseInt($scope.reservation.end, 10) <= parseInt($scope.reservation.start, 10)) {
				$scope.reservation.end = undefined;
			}

			if ($scope.reservation.date === undefined) {
				$scope.availableStartTimes = [];
				$scope.availableEndTimes = [];
				return;
			}

			var day = $scope.reservation.date.getDay();

			var filtered = schedule.days.filter(function(element) {
				return element.day === day;
			});
				
			if (filtered === null || filtered.length < 1) {
				$scope.availableStartTimes = [];
				$scope.availableEndTimes = [];
				return;
			}
			
			if ($scope.reservation.start) {
				$scope.firstSelect = true;
				var endTimes = [];
				for (var i = (parseInt($scope.reservation.start, 10) + 1); i <= 23; i++) {
					var contained = filtered[0].ranges.some(function(range) { 
						return range.start < i && range.end >= i;
					});
	
					if (contained) {
						endTimes.push(i);
					}
				}
				$scope.availableEndTimes = endTimes;
			}
		};

		$scope.close = function() {
			$modal.close(false);
		};

		this.checkRange = function(day) {
			return day.ranges.some(function(range) { 
				return range.start <= $scope.reservation.start && range.end >= $scope.reservation.end;
			});
		};

		this.validate = function() {
			if ($scope.reservation.date === undefined) {
				return false;
			}

			var day = $scope.reservation.date.getDay();

			var filtered = schedule.days.filter(function(element) {
				return element.day === day;
			});
				
			if (filtered === null || filtered.length < 1) {
				return false;
			}

			return self.checkRange(filtered[0]);
		};

		$scope.change = function() { 
			if (self.firstSubmit) {
				$scope.validRange = self.validate();
			}
		};

		$scope.submit = function(form) {
			$scope.validRange = self.validate();
			self.firstSubmit = true;
			if (form.$valid && $scope.validRange) {
				courseService.reserve(course.professor.id, course.subject.id, $scope.reservation.date, 
					$scope.reservation.start, $scope.reservation.end)
				.then(function() {
					$scope.reservation = {date: undefined, start: undefined, end: undefined};
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
								fun: courseService.reserve,
								params: [course.professor.id, course.subject.id, $scope.reservation.date, 
									$scope.reservation.start, $scope.reservation.end],
								message: 'ERROR_RESERVING'
							});
							$location.url('/login');
							break;
						}
						case 403: $scope.reservationError = 'SAME_USER_RESERVATION'; break;
						default: toastService.showAction('ERROR_RESERVING'); break;
					}
				});
			}
		};
	};
});
