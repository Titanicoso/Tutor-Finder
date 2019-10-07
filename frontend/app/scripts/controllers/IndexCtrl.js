'use strict';

define(['tutorFinder'], function(tutorFinder) {
	
	tutorFinder.controller('IndexCtrl', function($scope) {
		$scope.msg = 'This is the index view';
		$scope.search_input = '';

		$scope.reminder = function() {
			alert('TODO: search function');
		};
	});
});
