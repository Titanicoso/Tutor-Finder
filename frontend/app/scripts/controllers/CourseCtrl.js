'use strict';
define(['tutorFinder', 'services/courseService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('CourseCtrl', CourseCtrl);
	
	CourseCtrl.$inject = ['$scope', '$rootScope', '$route', 'courseService', 'authService'];
	function CourseCtrl($scope, $rootScope, $route, courseService, authService) {

		$rootScope.appendTitle('COURSE');
		$scope.professorId = $route.current.params.professorId;
		var subjectId = $route.current.params.subjectId;

		$scope.contactInput = {body: ''};
		$scope.contactSuccess = false;

		$scope.commentInput = {body: '', rating: undefined};
		$scope.commentError = undefined;

		var currentPage = 1;

		courseService.getCourse($scope.professorId, subjectId)
		.then(function(course) {
			$scope.course = course;
			return courseService.getComments($scope.professorId, subjectId, 1);
		})
		.then(function(comments) {
			$scope.comments = comments;
		})
		.catch(function(err) {
			console.log(err);
		});

		$scope.getPage = function(number) {
			courseService.getComments($scope.professorId, subjectId, number)
			.then(function(comments) {
				$scope.comments = comments;
				currentPage = number;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.contact = function(form) {
			if (form.$valid) {
				courseService.contact($scope.professorId, subjectId, $scope.contactInput.body)
				.then(function() {
					$scope.contactInput.body = '';
					form.$setPristine();
					$scope.contactSuccess = true;
				})
				.catch(function(err) {
					console.log(err.data);
				});
			}
		};

		$scope.comment = function(form) {
			if (form.$valid) {
				courseService.comment($scope.professorId, subjectId, $scope.commentInput.body, $scope.commentInput.rating)
				.then(function() {
					$scope.commentInput.body = '';
					$scope.commentInput.rating = undefined;
					$scope.commentError = undefined;
					form.$setPristine();
					$scope.getPage(currentPage);
				})
				.catch(function(err) {
					if (err.status === 403) {
						$scope.commentError = 'FORBIDDEN_COMMENT';
					} else {
						console.log(err.data);
					}
				});
			}
		};
	};
});
