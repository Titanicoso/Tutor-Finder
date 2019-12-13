'use strict';
define(['tutorFinder', 'services/courseService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('CreateCourseCtrl', CreateCourseCtrl);
	
	CreateCourseCtrl.$inject = ['$scope', '$uibModalInstance', 'course', 'courseService', 'toastService', 'authService', '$location', '$route', 'availableSubjects'];
	function CreateCourseCtrl($scope, $modal, course, courseService, toastService, authService, $location, $route, availableSubjects) {
		$scope.isModifying = course !== undefined && course !== null;
		
		if ($scope.isModifying) {
			$scope.courseInput = {description: course.description, price: course.price};
		} else {
			$scope.availableSubjects = availableSubjects;
			$scope.courseInput = {subject: undefined, description: undefined, price: undefined};
		}

		$scope.submit = function(form) {
			if (form.$valid) {
				var request = $scope.isModifying ? courseService.modify(course.professor.id, course.subject.id, 
					$scope.courseInput .description, $scope.courseInput .price) : 
					courseService.create($scope.courseInput .subject, $scope.courseInput .description, $scope.courseInput .price);

				request.then(function() {
					$scope.courseInput = {subject: undefined, description: undefined, price: undefined};
					form.$setPristine();
					$modal.close(true);
				})
				.catch(function(err) {
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						case 401: {
							var request = $scope.isModifying ? courseService.modify : courseService.create;
							var params = $scope.isModifying ? [course.professor.id, course.subject.id, 
								$scope.courseInput.description, $scope.courseInput.price] : 
								[$scope.courseInput.subject, $scope.courseInput.description, $scope.courseInput.price];

							if ($scope.currentUser) {
								toastService.showAction('SESSION_EXPIRED'); 
							}
							authService.setRedirectUrl($location.path(), $route.current.params);
							authService.logout();
							authService.setRequestRedo({
								fun: request,
								params: params,
								message: $scope.isModifying ? 'ERROR_MODIFYING_COURSE' : 'ERROR_CREATING_COURSE'
							});
							$location.url('/login');
							$modal.close(false);
							break;
						}
						default: toastService.showAction($scope.isModifying ? 'ERROR_MODIFYING_COURSE' : 'ERROR_CREATING_COURSE'); break;
					}
				});
			}
		};

		$scope.close = function() {
			$modal.close(false);
		};
	};
});
