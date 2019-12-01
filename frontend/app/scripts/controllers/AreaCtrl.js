'use strict';
define(['tutorFinder', 'services/areaService', 'directives/courseResults'], function(tutorFinder) {

	tutorFinder.controller('AreaCtrl', AreaCtrl);
	
	AreaCtrl.$inject = ['$scope', '$rootScope', '$route', 'areaService', 'toastService', '$location'];
	function AreaCtrl($scope, $rootScope, $route, areaService, toastService, $location) {
		$rootScope.appendTitle('AREA');
		var id = $route.current.params.id;
		var page = parseInt($route.current.params.page, 10);
		var self = this;

		$scope.current = {};
		$scope.current.page = page !== page ? 1 : page;
		$scope.current.previous = $scope.current.page;

		$scope.$on('$routeUpdate', function() {
			var page = parseInt($route.current.params.page, 10);
			page = page !== page ? 1 : page;
			if ($scope.current.previous !== page) {
				self.getCourses(page);
			}
		});

		areaService.getArea(id)
		.then(function(area) {
			$scope.area = area;
		})
		.catch(function(err) {
			switch (err.status) {
				case -1: toastService.showAction('NO_CONNECTION'); break;
				default: toastService.showAction('OOPS'); break;
			}
		});

		$scope.getPage = function(number) {
			$location.search({page: number});
		};

		this.getCourses = function(page) {
			areaService.getAreaCourses(id, page)
			.then(function(results) {
				$scope.courses = results;
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

		this.getCourses($scope.current.page);
	};
});
