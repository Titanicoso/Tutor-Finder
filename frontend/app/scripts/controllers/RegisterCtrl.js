'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('RegisterCtrl', RegisterCtrl);
	
	RegisterCtrl.$inject = ['$scope', '$location']; /* , 'UserService' /*  , 'FlashService' , '$rootScope'];*/
	function RegisterCtrl($scope, $location /* , UserService /* , FlashService , $rootScope */) {
        
        $scope.msg = 'This is the register view';

		$scope.hello = function($scope) {
            $scope.msg = 'hello';
        };
        
        /* $scope.myCookieVal = $cookies.get('cookie');
		$scope.setCookie = function(val) {
            $cookies.put('cookie', val);
        }; */
        
        /* $scope.register = function($scope) {
            $scope.msg = 'Dont press that!';
            $scope.dataLoading = true;
            UserService.create($scope.user)
                .then(function (response) {
                    if (response.success) {
                        FlashService.success('Registration successful', true);
                        $location.path('#!/login');
                    } else {
                        FlashService.Error(response.message);
                        $scope.dataLoading = false;
                    }
                });
        }; */
	};
});
