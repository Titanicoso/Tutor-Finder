'use strict';

define(['tutorFinder', 'services/authService'], function(tutorFinder) {
	
	tutorFinder.controller('IndexCtrl', IndexCtrl);
	
	IndexCtrl.inject = ['$scope', '$rootScope', '$translate', 'authService'];
	function IndexCtrl($scope, $rootScope, $translate, authService) {
		$rootScope.appName = $translate.instant('APP_NAME');
		$rootScope.title = $rootScope.appName;

		$scope.currentUser = authService.getCurrentUser();
		$scope.showDropdown = false;
		
		$rootScope.appendTitle = function(subtitle) {
			$rootScope.title = $rootScope.appName + ' | ' + 
								$translate.instant(subtitle);
		};

		$scope.$on('user_update', function() {
			$scope.currentUser = authService.getCurrentUser();
		});

		$scope.toggleDropdown = function() {
			$scope.showDropdown = !$scope.showDropdown;
		};

		$scope.logout = function() {
			authService.logout();
		};
		
		$scope.reminder = function() {
			// alert('TODO: search function');
		};
	};
});
