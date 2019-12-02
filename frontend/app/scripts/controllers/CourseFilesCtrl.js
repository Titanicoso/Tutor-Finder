'use strict';
define(['tutorFinder', 'services/courseFileService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('CourseFilesCtrl', CourseFilesCtrl);
	
	CourseFilesCtrl.$inject = ['$scope', '$rootScope', '$location', '$route', 'courseFileService', 'authService', 'toastService'];
	function CourseFilesCtrl($scope, $rootScope, $location, $route, courseFileService, authService, toastService) {
		$rootScope.appendTitle('COURSE_FILES');
		$scope.professorId = parseInt($route.current.params.professorId, 10);
		var subjectId = parseInt($route.current.params.subjectId, 10);

		if ($scope.professorId === undefined || subjectId === undefined ||
			 $scope.professorId !== $scope.professorId || subjectId !== subjectId) {
			toastService.showAction('INVALID_PARAMETERS');
			$location.url('/');
			return ;
		}

		if (!$scope.currentUser) { 
			$scope.currentUser = authService.getCurrentUser();
		}

		$scope.courseFile = {description: '', file: undefined, size: 0};

		$scope.$watch('courseFile.size', function(newVal, oldVal) {
			$scope.invalidSize = newVal > 10 * 1024 * 1024;
		}, true);

		var ctrl = this;
		$scope.upload = function(form) {
			if (form.$valid && !$scope.invalidSize) {
				courseFileService.upload($scope.professorId, subjectId, 
					$scope.courseFile.description, $scope.courseFile.file)
				.then(function() {
					$scope.courseFile = {description: '', file: undefined, size: 0};
					form.$setPristine();
					ctrl.refresh();
				})
				.catch(function(err) {
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						default: toastService.showAction('ERROR_UPLOADING_FILE'); break;
					}
				});
			}
		};

		this.refresh = function() {
			courseFileService.getCourseFiles($scope.professorId, subjectId)
			.then(function(files) {
				$scope.courseFiles = files; 
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					case 403: toastService.showAction('FORBIDDEN_COURSE_FILES'); $location.url('/'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.download = function(file) {
			courseFileService.getCourseFile($scope.professorId, subjectId, file.id)
			.then(function(data) {
				courseFileService.downloadFile(file, data);
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('ERROR_DOWNLOADING_FILE'); break;
				}
			});
		};

		$scope.delete = function(file) {
			courseFileService.delete($scope.professorId, subjectId, file.id)
			.then(function() {
				$scope.courseFiles = $scope.courseFiles
				.filter(function(element) {
					return element !== file;
				});
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('ERROR_DELETING_FILE'); break;
				}
			});
		};

		this.refresh();
	};
});
