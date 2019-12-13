'use strict';
define(['tutorFinder', 'services/authService', 'services/professorService', 'services/courseService', 'controllers/CreateCourseCtrl', 'controllers/ModifyProfileCtrl', 'controllers/TimeslotCtrl', 'directives/schedule', 'services/subjectService'], function(tutorFinder) {

	tutorFinder.controller('ProfileCtrl', ProfileCtrl);
	
	ProfileCtrl.$inject = ['$scope', '$rootScope', '$document', '$route', '$uibModal', 'authService', 'professorService', 'courseService', 'toastService', '$location', 'subjectService'];
	function ProfileCtrl($scope, $rootScope, $document, $route, $uibModal, authService, professorService, courseService, toastService, $location, subjectService) {
		$rootScope.appendTitle('PROFILE');
		var username = $route.current.params.username;
		
		$scope.$on('user_update', function() {
			if (!username || ($scope.currentUser && $scope.currentUser.username === username)) {
				$scope.showEditOptions = true;
				$scope.professor = $scope.currentUser;
				username = $scope.currentUser.username;
			}
		});

		$scope.showEditOptions = false;

		$scope.current = {};
		$scope.current.page = 1;
		var self = this;

		if (!username || ($scope.currentUser && $scope.currentUser.username === username)) {
			$scope.showEditOptions = true;
			$scope.professor = $scope.currentUser;
			username = $scope.currentUser.username;
		}
		
		this.getSchedule = function () {
			professorService.getProfessorSchedule(username)
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

		this.getAvailableSubjects = function(course, isRemove) {
			if ($scope.showEditOptions) {

				if (course) {
					$scope.availableSubjects = $scope.availableSubjects ? $scope.availableSubjects : [];
					if (isRemove) {
						$scope.availableSubjects = $scope.availableSubjects.filter(function(subject) {
							return subject !== course.subject;
						});
					} else {
						$scope.availableSubjects = $scope.availableSubjects.push(course.subject);
					}
				}

				subjectService.getAvailable()
				.then(function(subjects) {
					$scope.availableSubjects = subjects;
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
							$location.url('/login');
							break;
						}
						default: toastService.showAction('OOPS'); break;
					}
				});
			}
		};

		$scope.refresh = function() {
			self.getSchedule();
			self.getAvailableSubjects();
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

		$scope.deleteTimeslot = function() {
			var parent = angular.element($document[0].querySelector('.staticProfile'));
			$uibModal.open({
				controller: 'TimeslotCtrl',
				templateUrl: 'views/timeslotManagement.html',
				appendTo: parent,
				backdrop: 'static',
				resolve: {
					schedule: function() {
						return $scope.schedule;
					},
					isDelete: function() {
						return true;
					}
				 }
			}).result.then(function(answer) {
				if (answer) {
					self.getSchedule();
				}
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.addTimeslot = function() {
			var parent = angular.element($document[0].querySelector('.staticProfile'));
			$uibModal.open({
				controller: 'TimeslotCtrl',
				templateUrl: 'views/timeslotManagement.html',
				appendTo: parent,
				backdrop: 'static',
				resolve: {
					schedule: function() {
						return $scope.schedule;
					},
					isDelete: function() {
						return false;
					}
				 }
			}).result.then(function(answer) {
				if (answer) {
					self.getSchedule();
				}
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.modifyCourse = function(course) {
			var parent = angular.element($document[0].querySelector('.staticProfile'));
			$uibModal.open({
				controller: 'CreateCourseCtrl',
				templateUrl: 'views/createCourse.html',
				appendTo: parent,
				backdrop: 'static',
				resolve: {
					course: function() {
						return course;
					},
					availableSubjects: function() {
						return $scope.availableSubjects;
					}
				 }
			}).result.then(function(answer) {
				if (answer) {
					if (!course) {
						self.getAvailableSubjects();
					}
					$scope.getPage($scope.current.page);
				}
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};
		
		$scope.editProfile = function() {
			var parent = angular.element($document[0].querySelector('.staticProfile'));
			$uibModal.open({
				controller: 'ModifyProfileCtrl',
				templateUrl: 'views/modifyProfile.html',
				appendTo: parent,
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
				self.getAvailableSubjects(course.subject, false);
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
