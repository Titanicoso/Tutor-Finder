
'use strict';
define(['tutorFinder', 'directives/fileRead', 'services/userService'], function(tutorFinder) {

	tutorFinder.controller('ModifyProfileCtrl', ModifyProfileCtrl);
	
	ModifyProfileCtrl.$inject = ['$scope', '$uibModalInstance', 'professor', 'userService'];
	function ModifyProfileCtrl($scope, $modal, professor, userService) {
		$scope.isModifying = professor !== undefined && professor !== null;
		$scope.invalidType = false;
		
		if ($scope.isModifying) {
			$scope.professorInput = {description: professor.description, picture: undefined, size: 0};
		} else {
			$scope.professorInput = {description: undefined, picture: undefined, size: 0};
		}

		$scope.$watch('professorInput.size', function(newVal, oldVal) {
			$scope.invalidSize = newVal > 80 * 1024;
			if ($scope.professorInput.picture && $scope.professorInput.picture.type) {
				var type = $scope.professorInput.picture.type;
				$scope.invalidType = type !== 'image/jpeg' && type !== 'image/png' && type !== 'image/jpg';
			}
			
		}, true);

		$scope.submit = function(form) {
			if (form.$valid && !$scope.invalidSize && !$scope.invalidType) {

				var request = $scope.isModifying ? userService.modify($scope.professorInput.description, $scope.professorInput.picture) :
				 userService.upgrade($scope.professorInput.description, $scope.professorInput.picture);

				request.then(function() {
					$scope.professorInput = {description: undefined, picture: undefined, size: 0};
					form.$setPristine();
					$modal.close(true);
				})
				.catch(function(err) {
					console.log(err);
				});
			}
		};

		$scope.close = function() {
			$modal.close(false);
		};
	};
});
