
'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ModifyProfileCtrl', ModifyProfileCtrl);
	
	ModifyProfessorProfileCtrl.$inject = ['$scope', '$rootScope'];
	function ModifyProfileCtrl($scope, $rootScope) {

		$rootScope.appendTitle('MODIFY_PROFESSOR_PROFILE');
	};
});
