'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('RegisterCtrl', function($scope) {
		$scope.msg = 'register msg';
		$scope.register = function(){
			alert("Registered. Not!")
		}
	});
});
