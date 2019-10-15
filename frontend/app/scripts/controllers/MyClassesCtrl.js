'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('MyClassesCtrl', MyClassesCtrl);
	
	MyClassesCtrl.$inject = ['$scope', '$rootScope'];
	function MyClassesCtrl($scope, $rootScope) {

		$rootScope.appendTitle('MYCLASSES');
	};
});
