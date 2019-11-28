'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.directive('file', function() {
		return {
			scope: {
				file: '=',
				size: '='
			},
			link: function($scope, element, attributes) {
				element.bind('change', function (changeEvent) {
					$scope.$apply(function () {
						$scope.file = changeEvent.target.files[0];
						if ($scope.file) {
							$scope.size = changeEvent.target.files[0].size;
						} else {
							$scope.size = 0;
						}
					});
				});
			}
		};
	});
});