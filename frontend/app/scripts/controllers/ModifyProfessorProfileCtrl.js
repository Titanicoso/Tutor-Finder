
'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ModifyProfessorProfileCtrl', ModifyProfessorProfileCtrl);
	
	ModifyProfessorProfileCtrl.$inject = ['$scope', '$rootScope'];
	function ModifyProfessorProfileCtrl($scope, $rootScope) {

		$rootScope.appendTitle('MODIFY_PROFESSOR_PROFILE');
	};
});
