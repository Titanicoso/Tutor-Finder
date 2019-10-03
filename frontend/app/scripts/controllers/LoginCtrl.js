'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('LoginCtrl', LoginCtrl);
	
	LoginCtrl.$inject = ['$scope', '$location']; /* 'AuthenticationService', 'FlashService' ]; */
	function LoginCtrl($scope, $location /* , AuthenticationService /* , FlashService */) {
        $scope.msg = 'This is the login view';
        /* 
        var vm = this;
		vm.login = login;
        vm.msg = 'This is the login view';
 */
		/* (function initController() {
            // reset login status
            AuthenticationService.ClearCredentials();
        })(); */

        /* function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.username, vm.password, function (response) {
                if (response.success) {
                    AuthenticationService.SetCredentials(vm.username, vm.password);
                    $location.path('/');
                } else {
                    FlashService.Error(response.message);
                    vm.dataLoading = false;
                }
            });
        }; */
	};
});
