'use strict';
define(['tutorFinder', 'services/authService', 'services/professorService', 'services/courseService', 'controllers/CreateCourseCtrl', 'controllers/ModifyProfileCtrl'], function(tutorFinder) {

	tutorFinder.controller('ProfileCtrl', ProfileCtrl);
	
	ProfileCtrl.$inject = ['$scope', '$rootScope', '$route', '$uibModal', 'authService', 'professorService', 'courseService'];
	function ProfileCtrl($scope, $rootScope, $route, $uibModal, authService, professorService, courseService) {
		$rootScope.appendTitle('PROFILE');
		var username = $route.current.params.username;
		var currentUser = $scope.currentUser;
		$scope.showEditOptions = false;

		var currentPage = 1;

		if (!username || (currentUser && currentUser.username === username)) {
			$scope.showEditOptions = true;
			$scope.professor = currentUser;
			username = currentUser.username;
		}

		$scope.refresh = function() {
			professorService.getProfessor(username)
			.then(function(response) {
				$scope.professor = response;
				return professorService.getProfessorCourses(username, currentPage);
			})
			.then(function(response) {
				$scope.courses = response;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.refresh();

		$scope.getPage = function(number) {
			professorService.getProfessorCourses(username, number)
			.then(function(courses) {
				$scope.courses = courses;
				currentPage = number;
			})
			.catch(function(err) {
				console.log(err);
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
					$scope.getPage(currentPage);
				}
			}, function(err) { 
				console.log(err);
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
				console.log(err);
			});
		};

		$scope.deleteCourse = function(course) {
			courseService.delete(course.professor.id, course.subject.id)
			.then(function() {
				$scope.getPage(currentPage);
			})
			.catch(function(err) { 
				console.log(err);
			});
		};
	};
});
