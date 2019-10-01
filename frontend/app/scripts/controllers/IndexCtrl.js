'use strict';

define(['tutorFinder'], function(tutorFinder) {
	
	tutorFinder.controller('IndexCtrl', function($scope) {
		$scope.welcomeText = 'Welcome to your main tutorFinder page';
		$scope.logoInvertPath = '/images/logo_invert.jpg';
	});
});
