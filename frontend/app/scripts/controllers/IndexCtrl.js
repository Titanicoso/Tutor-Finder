'use strict';

define(['tutorFinder', 'services/authService', 'controllers/ModifyProfileCtrl', 'services/toastService'], function(tutorFinder) {
	
	tutorFinder.controller('IndexCtrl', IndexCtrl);
	
	IndexCtrl.inject = ['$scope', '$rootScope', '$translate', '$location', 'authService', '$uibModal', 'toastService'];
	function IndexCtrl($scope, $rootScope, $translate, $location, authService, $uibModal, toastService) {
		$rootScope.appName = $translate.instant('APP_NAME');
		$rootScope.title = $rootScope.appName;

		$scope.currentUser = authService.getCurrentUser();

		$scope.filters = {query: '', category: 'course'};

		$scope.showDropdown = false;
		
		$rootScope.appendTitle = function(subtitle) {
			$rootScope.title = $rootScope.appName + ' | ' + 
								$translate.instant(subtitle);
		};

		$scope.home = function() {
			$scope.filters = {query: '', category: 'course'};
			$location.url('/');
		};

		$scope.$on('user_update', function() {
			$scope.currentUser = authService.getCurrentUser();
		});

		$scope.toggleDropdown = function() {
			$scope.showDropdown = !$scope.showDropdown;
		};

		$scope.logout = function() {
			authService.logout();
			$location.url('/');
		};

		$scope.registerAsProfessor = function() {
			$scope.showDropdown = false;
			$uibModal.open({
				controller: 'ModifyProfileCtrl',
				templateUrl: 'views/modifyProfile.html',
				backdrop: 'static',
				resolve: {
					professor: function() {
						return undefined;
					}
				 }
			}).result.then(function(answer) {
				if (answer) {
					$scope.currentUser = authService.getCurrentUser(true);
				}
			}, function(err) { 
				switch (err.status) {
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.search = function() {
			$location.path('/searchResults').search($scope.filters);
		};
	};
});
