'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('RegisterAsProfessorCtrl', RegisterAsProfessorCtrl);
	
	RegisterAsProfessorCtrl.$inject = ['$scope', '$rootScope'];
	function RegisterAsProfessorCtrl($scope, $rootScope) {

		$rootScope.appendTitle('REGISTER_AS_PROFESSOR');
	};
});
