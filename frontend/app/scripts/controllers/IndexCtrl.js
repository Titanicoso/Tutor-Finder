'use strict';

define(['tutorFinder'], function(tutorFinder) {
	
	tutorFinder.controller('IndexCtrl', function($scope, $cookies) {
		$scope.msg = 'This is the index view';
		$scope.logoInvertPath = '/images/logo_invert.jpg';

		$scope.myCookieVal = $cookies.get('cookie');
		$scope.setCookie = function(val) {
			$cookies.put('cookie', val);
		};
	});
});
