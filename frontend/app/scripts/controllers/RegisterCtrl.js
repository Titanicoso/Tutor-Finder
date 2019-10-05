'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('RegisterCtrl', RegisterCtrl);
	
	RegisterCtrl.$inject = ['$scope', '$location'];
	function RegisterCtrl($scope, $location) {
        var vm = this;
        vm.msg = 'This is the login view';
	};
});
