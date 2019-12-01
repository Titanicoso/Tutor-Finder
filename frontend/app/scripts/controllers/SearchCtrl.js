'use strict';
define(['tutorFinder', 'services/areaService', 'services/courseService', 'services/professorService', 'directives/professorResults', 'directives/courseResults', 'directives/areaResults'], function(tutorFinder) {

	tutorFinder.controller('SearchCtrl', SearchCtrl);
	
	SearchCtrl.$inject = ['$scope', '$rootScope', '$route', '$location', 'areaService', 'courseService', 'professorService', 'toastService'];
	function SearchCtrl($scope, $rootScope, $route, $location, areaService, courseService, professorService, toastService) {
		$rootScope.appendTitle('SEARCH_RESULTS');

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
			} else if (tempDays) {
				parameters.days.push(tempDays);
			}
		
			return {
				query: parameters.query ? parameters.query : '',
				category: parameters.category,
				minPrice: parameters.minPrice === parameters.minPrice ? parameters.minPrice : undefined,
				maxPrice: parameters.maxPrice === parameters.maxPrice ? parameters.maxPrice : undefined,
				startHour: parameters.startHour === parameters.startHour ? parameters.startHour : undefined,
				endHour: parameters.endHour === parameters.endHour ? parameters.endHour : undefined,
				days: parameters.days,
				page: parameters.page !== parameters.page ? 1 : parameters.page
			};
		};

		$scope.filters = this.getFilters();

		$scope.lastSearch = {query: $scope.filters.query, category: $scope.filters.category};
		var self = this;

		angular.copy($scope.filters, $scope.previous);

		$scope.$on('$routeUpdate', function() {
			var filters = self.getFilters();

			if (JSON.stringify($scope.previous) !== JSON.stringify(filters)) {
				self.search(filters);
			}
		});

		$scope.getPage = function() {
			$location.search($scope.filters);
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

			request.then(function(results) {
				$scope.results = results;
				$scope.filters = filters;
				angular.copy(filters, $scope.previous);
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		this.search($scope.filters);
	};
});
