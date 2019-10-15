'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ProfessorProfileCtrl', ProfessorProfileCtrl);
	
	ProfessorProfileCtrl.$inject = ['$scope', '$rootScope'];
	function ProfessorProfileCtrl($scope, $rootScope) {

		$rootScope.appendTitle('PROFESSOR_PROFILE');
	};
});
