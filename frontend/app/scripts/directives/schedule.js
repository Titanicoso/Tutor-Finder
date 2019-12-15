'use strict';
define(['tutorFinder'], function(tutorFinder) {
	tutorFinder.directive('schedule', function() {
		return {
			restrict: 'E',
			replace: 'true',
			templateUrl: 'views/components/schedule.html',
			scope: {schedule: '='},
			controller: ['$scope', function($scope) {
				
				$scope.availableHours = [];
				var self = this;

				this.getAvailableHours = function() {
					var hours = [];

					if (!$scope.schedule) {
						return;
					}

					$scope.schedule.days.forEach(function(element) {
						element.ranges.forEach(function(range) {
							for (var index = range.start; index < range.end; index++) {
								if (!hours.includes(index)) {
									hours.push(index);
								}
							}
						});
					});

          hours.sort(sortNumber);
					$scope.availableHours = hours;
				};

        function sortNumber(a, b) {
          return a - b;
        }
        
				this.getAvailableHours();

				
				$scope.$watch('schedule', function(newVal, oldVal) {
					self.getAvailableHours();
				}, true);
				
				$scope.range = function(start, end) {
					var range = [];
					for (var index = start; index < end; index++) {
						range.push(index);
					}
					return range;
				};

				$scope.dayContains = function(day, hour) {
					if (!$scope.schedule) {
						return false;
					}
		
					var filtered = $scope.schedule.days.filter(function(element) {
						return element.day === day;
					});

					if (filtered === null || filtered.length === 0) {
						return false;
					}

					return filtered[0].ranges.some(function(range) { 
						return range.start <= hour && range.end > hour;
					});
				};
			}]
		};
	});
});