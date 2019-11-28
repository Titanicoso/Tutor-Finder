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
						$scope.size = changeEvent.target.files[0].size;
					});
				});
			}
		};
	});
});