'use strict';
define(['tutorFinder', 'services/subjectService', 'services/courseService'], function(tutorFinder) {

	tutorFinder.controller('CreateCourseCtrl', CreateCourseCtrl);
	
	CreateCourseCtrl.$inject = ['$scope', '$uibModalInstance', 'course', 'subjectService', 'courseService'];
	function CreateCourseCtrl($scope, $modal, course, subjectService, courseService) {
		$scope.isModifying = course !== undefined && course !== null;
		
		if ($scope.isModifying) {
			$scope.courseInput = {description: course.description, price: course.price};
		} else {
			subjectService.getAvailable()
			.then(function(subjects) {
				$scope.availableSubjects = subjects;
			})
			.catch(function(err) {
				console.log(err);
			});

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
					console.log(err);
				});
			}
		};

		$scope.close = function() {
			$modal.close(false);
		};
	};
});
