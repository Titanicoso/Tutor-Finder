'use strict';
define(['tutorFinder', 'services/areaService', 'directives/courseResults'], function(tutorFinder) {

	tutorFinder.controller('AreaCtrl', AreaCtrl);
	
	AreaCtrl.$inject = ['$scope', '$rootScope', '$route', 'areaService'];
	function AreaCtrl($scope, $rootScope, $route, areaService) {
		$rootScope.appendTitle('AREA');
		var id = $route.current.params.id;
		
		var currentPage = 1;

		areaService.getArea(id)
		.then(function(area) {
			$scope.area = area;
		})
		.catch(function(err) {
			console.log(err);
		});

		$scope.getPage = function(number) {
			areaService.getAreaCourses(id, number)
			.then(function(results) {
				$scope.courses = results;
				currentPage = number;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.getPage(currentPage);
	};
});
