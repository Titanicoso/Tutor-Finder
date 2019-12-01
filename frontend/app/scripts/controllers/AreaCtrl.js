'use strict';
define(['tutorFinder', 'services/areaService', 'directives/courseResults'], function(tutorFinder) {

	tutorFinder.controller('AreaCtrl', AreaCtrl);
	
	AreaCtrl.$inject = ['$scope', '$rootScope', '$route', 'areaService', 'toastService'];
	function AreaCtrl($scope, $rootScope, $route, areaService, toastService) {
		$rootScope.appendTitle('AREA');
		var id = $route.current.params.id;
		
		$scope.currentPage = 1;

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
			areaService.getAreaCourses(id, number)
			.then(function(results) {
				$scope.courses = results;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.getPage($scope.currentPage);
	};
});
