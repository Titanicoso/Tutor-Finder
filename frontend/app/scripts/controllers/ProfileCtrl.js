'use strict';
define(['tutorFinder', 'services/authService', 'services/professorService', 'services/courseService', 'controllers/CreateCourseCtrl', 'controllers/ModifyProfileCtrl'], function(tutorFinder) {

	tutorFinder.controller('ProfileCtrl', ProfileCtrl);
	
	ProfileCtrl.$inject = ['$scope', '$rootScope', '$route', '$uibModal', 'authService', 'professorService', 'courseService', 'toastService', '$location'];
	function ProfileCtrl($scope, $rootScope, $route, $uibModal, authService, professorService, courseService, toastService, $location) {
		$rootScope.appendTitle('PROFILE');
		var username = $route.current.params.username;

		if (!$scope.currentUser) { 
			$scope.currentUser = authService.getCurrentUser();
		}
		
		var currentUser = $scope.currentUser;
		$scope.showEditOptions = false;

		$scope.current = {};
		$scope.current.page = 1;

		if (!username || (currentUser && currentUser.username === username)) {
			$scope.showEditOptions = true;
			$scope.professor = currentUser;
			username = currentUser.username;
		}

		$scope.refresh = function() {
			professorService.getProfessor(username)
			.then(function(response) {
				$scope.professor = response;
				return professorService.getProfessorCourses(username, $scope.current.page);
			})
			.then(function(response) {
				$scope.courses = response;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.refresh();

		$scope.getPage = function(number) {
			professorService.getProfessorCourses(username, number)
			.then(function(courses) {
				$scope.courses = courses;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.modifyCourse = function(course) {
			$uibModal.open({
				controller: 'CreateCourseCtrl',
				templateUrl: 'views/createCourse.html',
				backdrop: 'static',
				resolve: {
					course: function() {
						return course;
					}
				 }
			}).result.then(function(answer) {
				if (answer) {
					$scope.getPage($scope.current.page);
				}
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};
		
		$scope.editProfile = function() {
			$uibModal.open({
				controller: 'ModifyProfileCtrl',
				templateUrl: 'views/modifyProfile.html',
				backdrop: 'static',
				resolve: {
					professor: function() {
						return $scope.professor;
					}
				 }
			}).result.then(function(answer) {
				if (answer) {
					$scope.professor.image_url = undefined;
					$scope.refresh();
				}
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.deleteCourse = function(course) {
			courseService.delete(course.professor.id, course.subject.id)
			.then(function() {
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
							fun: courseService.delete,
							params: [course.professor.id, course.subject.id],
							message: 'ERROR_DELETING_COURSE'}
						);
						$location.url('/login');
						break;
					}
					default: toastService.showAction('ERROR_DELETING_COURSE'); break;
				}
			});
		};
	};
});
