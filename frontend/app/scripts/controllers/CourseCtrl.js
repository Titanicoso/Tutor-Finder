'use strict';
define(['tutorFinder', 'services/courseService', 'services/authService', 'controllers/ReserveClassCtrl', 'services/professorService'], function(tutorFinder) {

	tutorFinder.controller('CourseCtrl', CourseCtrl);
	
	CourseCtrl.$inject = ['$scope', '$rootScope', '$document', '$route', '$uibModal', 'courseService', 'authService', 'toastService', '$location', 'professorService'];
	function CourseCtrl($scope, $rootScope, $document, $route, $uibModal, courseService, authService, toastService, $location, professorService) {

		$rootScope.appendTitle('COURSE');
		$scope.professorId = parseInt($route.current.params.professorId, 10);
		var subjectId = parseInt($route.current.params.subjectId, 10);

		if ($scope.professorId === undefined || subjectId === undefined ||
			 $scope.professorId !== $scope.professorId || subjectId !== subjectId) {
			toastService.showAction('INVALID_PARAMETERS');
			$location.url('/');
			return;
		}

		if (!$scope.currentUser) { 
			$scope.currentUser = authService.getCurrentUser();
		}

		$scope.contactInput = {body: ''};
		$scope.contactSuccess = false;

		$scope.commentInput = {body: '', rating: undefined};
		$scope.commentError = undefined;
		$scope.canComment = $scope.currentUser === null;

		$scope.current = {};
		$scope.current.page = 1;
		var self = this;

		this.getSchedule = function (professor) {
			professorService.getProfessorSchedule(professor.username)
			.then(function(response) {
				$scope.schedule = response;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		courseService.getCourse($scope.professorId, subjectId)
		.then(function(course) {
			$scope.course = course;
			$scope.canComment = $scope.canComment || course.canComment;
			self.getSchedule(course.professor);
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
						case 401: {
							if ($scope.currentUser) {
								toastService.showAction('SESSION_EXPIRED'); 
							} 
							authService.setRedirectUrl($location.path(), $route.current.params);
							authService.logout();
							authService.setRequestRedo({
								fun: courseService.contact,
								params: [$scope.professorId, subjectId, $scope.contactInput.body],
								message: 'ERROR_SENDING_MESSGE'}
							);
							$location.url('/login');
							break;
						}
						default: toastService.showAction('ERROR_SENDING_MESSGE'); break;
					}
				});
			}
		};

		$scope.reserveClass = function() {
			var parent = angular.element($document[0].querySelector('.staticClass'));
			$uibModal.open({
				controller: 'ReserveClassCtrl',
				templateUrl: 'views/reserveClass.html',
				appendTo: parent,
				backdrop: 'static',
				resolve: {
					course: function() {
						return $scope.course;
					},
					schedule: function() {
						return $scope.schedule;
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
						case 401: {
							if ($scope.currentUser) {
								toastService.showAction('SESSION_EXPIRED'); 
							} 
							authService.setRedirectUrl($location.path(), $route.current.params);
							authService.logout();
							authService.setRequestRedo({
								fun: courseService.comment,
								params: [$scope.professorId, subjectId, $scope.commentInput.body, $scope.commentInput.rating],
								message: 'ERROR_DENYING',
								errorMessage: 'FORBIDDEN_COMMENT'
							});
							$location.url('/login');
							break;
						}
						case 403: $scope.commentError = 'FORBIDDEN_COMMENT'; break;
						default: toastService.showAction('ERROR_COMMENTING'); break;
					}
				});
			}
		};
	};
});
