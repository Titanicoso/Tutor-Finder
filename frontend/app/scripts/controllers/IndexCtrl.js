'use strict';

define(['tutorFinder'], function(tutorFinder) {
	
	tutorFinder.controller('IndexCtrl', IndexCtrl);
	
	IndexCtrl.inject = ['$scope', '$rootScope', '$translate'];
	function IndexCtrl($scope, $rootScope, $translate) {
		$scope.msg = 'This is the index view';
		
		$rootScope.appName = $translate.instant('APP_NAME');
		$rootScope.title = $rootScope.appName;
		
		/** Appends text to page title 
		 * subtitle: a valid key from the translations.XX.js file
		 */
		$rootScope.appendTitle = function(subtitle) {
			$rootScope.title = $rootScope.appName + ' | ' + 
								$translate.instant(subtitle);
		};
		
		$scope.reminder = function() {
			alert('TODO: search function');
		};
	};
});
