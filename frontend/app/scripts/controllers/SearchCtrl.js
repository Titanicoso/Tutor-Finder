'use strict';
define(['tutorFinder', 'services/areaService', 'services/courseService', 'services/professorService', 'directives/professorResults', 'directives/courseResults', 'directives/areaResults'], function(tutorFinder) {

	tutorFinder.controller('SearchCtrl', SearchCtrl);
	
	SearchCtrl.$inject = ['$scope', '$rootScope', '$route', '$location', 'areaService', 'courseService', 'professorService', 'toastService'];
	function SearchCtrl($scope, $rootScope, $route, $location, areaService, courseService, professorService, toastService) {
		$rootScope.appendTitle('SEARCH_RESULTS');

		var query = $route.current.params.query;
		var category = $route.current.params.category;
		var minPrice = parseFloat($route.current.params.minPrice);
		var maxPrice = parseFloat($route.current.params.maxPrice);
		var startHour = parseInt($route.current.params.startHour, 10);
		var endHour = parseInt($route.current.params.endHour, 10);
		var page = parseInt($route.current.params.page, 10);

		var tempDays = $route.current.params.days;
		var days = [];
		if (tempDays && tempDays.length > 1) {
			days = tempDays.filter(function(day) { 
				var tempDay = parseInt(day, 10);
				return tempDay === tempDay;
			}).map(function(day) {
				return parseInt(day, 10);
			});
		} else if (tempDays) {
			days.push(tempDays);
		}
		
		$scope.filters = {
			query: query ? query : '',
			category: category,
			minPrice: minPrice === minPrice ? minPrice : undefined,
			maxPrice: maxPrice === maxPrice ? maxPrice : undefined,
			startHour: startHour === startHour ? startHour : undefined,
			endHour: endHour === endHour ? endHour : undefined,
			days: days
		};

		$scope.lastSearch = {query: $scope.filters.query, category: $scope.filters.category};

		$scope.currentPage = page !== page ? 1 : page;

		$scope.search = function() {
			$scope.getPage($scope.currentPage);
		};

		$scope.getPage = function(number) {
			$scope.lastSearch = {query: $scope.filters.query, category: $scope.filters.category};
			var request;
			switch ($scope.filters.category) {
				case 'professor': request = professorService.getProfessors($scope.filters.query, number); break;
				case 'area': request = areaService.getAreas($scope.filters.query, number); break;
				default: request = courseService.filterCourses($scope.filters.query, startHour, endHour, minPrice, maxPrice, days, number); break;
			}

			request.then(function(results) {
				$scope.results = results;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.search();
	};
});
