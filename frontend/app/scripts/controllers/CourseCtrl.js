'use strict';
define(['tutorFinder', 'services/courseService', 'services/authService', 'controllers/ReserveClassCtrl'], function(tutorFinder) {

	tutorFinder.controller('CourseCtrl', CourseCtrl);
	
	CourseCtrl.$inject = ['$scope', '$rootScope', '$route', '$uibModal', 'courseService', 'authService', 'toastService'];
	function CourseCtrl($scope, $rootScope, $route, $uibModal, courseService, authService, toastService) {

		$rootScope.appendTitle('COURSE');
		$scope.professorId = $route.current.params.professorId;
		var subjectId = $route.current.params.subjectId;

		if (!$scope.currentUser) { 
			$scope.currentUser = authService.getCurrentUser();
		}

		$scope.contactInput = {body: ''};
		$scope.contactSuccess = false;

		$scope.commentInput = {body: '', rating: undefined};
		$scope.commentError = undefined;
		$scope.canComment = false;

		$scope.current = {};
		$scope.current.page = 1;

		courseService.getCourse($scope.professorId, subjectId)
		.then(function(course) {
			$scope.course = course;
			$scope.canComment = $scope.currentUser === null || course.canComment;
			return courseService.getComments($scope.professorId, subjectId, 1);
		})
		.then(function(comments) {
			$scope.comments = comments;
		})
		.catch(function(err) {
			switch (err.status) {
				case -1: toastService.showAction('NO_CONNECTION'); break;
				default: toastService.showAction('OOPS'); break;
			}
		});

		$scope.getPage = function(number) {
			courseService.getComments($scope.professorId, subjectId, number)
			.then(function(comments) {
				$scope.comments = comments;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
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
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						default: toastService.showAction('ERROR_SENDING_MESSGE'); break;
					}
				});
			}
		};

		$scope.reserveClass = function() {
			$uibModal.open({
				controller: 'ReserveClassCtrl',
				templateUrl: 'views/reserveClass.html',
				backdrop: 'static',
				resolve: {
					course: function() {
						return $scope.course;
					}
				 }
			}).result.then(function() {
				
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.comment = function(form) {
			if (form.$valid) {
				courseService.comment($scope.professorId, subjectId, $scope.commentInput.body, $scope.commentInput.rating)
				.then(function() {
					$scope.commentInput.body = '';
					$scope.commentInput.rating = undefined;
					$scope.commentError = undefined;
					form.$setPristine();
					$scope.getPage($scope.current.page);
				})
				.catch(function(err) {
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						case 403: $scope.commentError = 'FORBIDDEN_COMMENT'; break;
						default: toastService.showAction('ERROR_COMMENTING'); break;
					}
				});
			}
		};
	};
});
