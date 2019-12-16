'use strict';
define(['tutorFinder', 'services/areaService', 'services/courseService', 'services/professorService', 'directives/professorResults', 'directives/courseResults', 'directives/areaResults'], function(tutorFinder) {

	tutorFinder.controller('SearchCtrl', SearchCtrl);
	
	SearchCtrl.$inject = ['$scope', '$rootScope', '$route', '$location', 'areaService', 'courseService', 'professorService', 'toastService'];
	function SearchCtrl($scope, $rootScope, $route, $location, areaService, courseService, professorService, toastService) {
		$rootScope.appendTitle('SEARCH_RESULTS');
		$scope.loading = false;

		this.getFilters = function() {
			var parameters = {};
			parameters.query = $route.current.params.query;
			parameters.category = $route.current.params.category;
			parameters.minPrice = parseFloat($route.current.params.minPrice);
			parameters.maxPrice = parseFloat($route.current.params.maxPrice);
			parameters.startHour = parseInt($route.current.params.startHour, 10);
			parameters.endHour = parseInt($route.current.params.endHour, 10);
			parameters.page = parseInt($route.current.params.page, 10);
	
			var tempDays = $route.current.params.days;
			if (tempDays && tempDays.length > 1) {
				parameters.days = [];
				parameters.days = tempDays.filter(function(day) { 
					var tempDay = parseInt(day, 10);
					return tempDay === tempDay;
				}).map(function(day) {
					return parseInt(day, 10);
				});
				parameters.days = parameters.days.filter(function(day, index) {
					return parameters.days.indexOf(day) === index;
				});
			} else if (tempDays) {
				parameters.days = [];
				var tempDay = parseInt(tempDays, 10);
				if (tempDay === tempDay) {
					parameters.days.push(tempDay);
				}
			}
		
			return {
				query: parameters.query ? parameters.query : '',
				category: parameters.category,
				minPrice: parameters.minPrice === parameters.minPrice ? parameters.minPrice : undefined,
				maxPrice: parameters.maxPrice === parameters.maxPrice ? parameters.maxPrice : undefined,
				startHour: parameters.startHour === parameters.startHour ? JSON.stringify(parameters.startHour) : undefined,
				endHour: parameters.endHour === parameters.endHour ? JSON.stringify(parameters.endHour) : undefined,
				days: parameters.days,
				page: parameters.page !== parameters.page ? 1 : parameters.page
			};
		};

		$rootScope.filters = this.getFilters();

		$scope.availableStartTimes = [];
		$scope.availableEndTimes = [];

		this.initStartTimes = function() {
			for (var i = 1; i <= 23; i++) {
				$scope.availableStartTimes.push(i);
			}
		};

		this.initStartTimes();
		
		$scope.startSelected = function() {
			if (parseInt($rootScope.filters.endHour, 10) <= parseInt($rootScope.filters.startHour, 10)) {
				$rootScope.filters.endHour = undefined;
			}
			
			if ($rootScope.filters.startHour) {
				var endTimes = [];
				for (var i = (parseInt($rootScope.filters.startHour, 10) + 1); i <= 23; i++) {
					endTimes.push(i);
				}
				$scope.availableEndTimes = endTimes;
			}
		};

		$scope.startSelected();

		$scope.lastSearch = {query: $rootScope.filters.query, category: $rootScope.filters.category};
		var self = this;

		angular.copy($rootScope.filters, $scope.previous);

		$scope.$on('$routeUpdate', function() {
			var filters = self.getFilters();

			if (JSON.stringify($scope.previous) !== JSON.stringify(filters)) {
				self.search(filters);
			}
		});

		$scope.getPage = function() {
			$location.search($rootScope.filters);
		};

		this.search = function(filters) {
			$scope.lastSearch = {query: filters.query, category: filters.category};
			var request;
			switch (filters.category) {
				case 'professor': request = professorService.getProfessors(filters.query, filters.page); break;
				case 'area': request = areaService.getAreas(filters.query, filters.page); break;
				default: request = courseService.filterCourses(filters.query, filters.startHour, filters.endHour,
					 filters.minPrice, filters.maxPrice, filters.days, filters.page); break;
			}
			$scope.loading = true;
			request.then(function(results) {
				$scope.results = results;
				$rootScope.filters = filters;
				angular.copy(filters, $scope.previous);
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			})
			.then(function() {
				$scope.loading = false;
			});
		};

		this.search($rootScope.filters);
	};
});
