'use strict';
define(['tutorFinder', 'services/areaService', 'directives/courseResults'], function(tutorFinder) {

	tutorFinder.controller('AreaCtrl', AreaCtrl);
	
	AreaCtrl.$inject = ['$scope', '$rootScope', '$route', 'areaService', 'toastService', '$location'];
	function AreaCtrl($scope, $rootScope, $route, areaService, toastService, $location) {
		$rootScope.appendTitle('AREA');
		var id = parseInt($route.current.params.id, 10);
		var page = parseInt($route.current.params.page, 10);
		var self = this;

		if (id === undefined || id !== id) {
			toastService.showAction('INVALID_PARAMETERS');
			$location.url('/');
			return;
		}
		
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

		$scope.loading = true;
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
				$scope.loading = false;
				$scope.courses = results;
				$scope.current.page = page;
				$scope.current.previous = page;
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

		this.getCourses($scope.current.page);
	};
});
