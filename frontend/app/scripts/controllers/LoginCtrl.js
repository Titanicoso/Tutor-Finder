'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('LoginCtrl', LoginCtrl);
	
	LoginCtrl.$inject = ['$scope', '$location'];
	function LoginCtrl($scope, $location) {
        var vm = this;
        vm.msg = 'This is the login view';
	};
});
