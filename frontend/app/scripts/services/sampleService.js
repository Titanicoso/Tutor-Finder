'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.service('SampleService',['$http', '$q', 'apiBaseUrl', function($http, $q, apiBaseUrl) {
		this.areas = function() {
			$http.get(apiBaseUrl + '/areas')
				.then(function(response) {
					console.log(response);
				})
				.catch(function(response) {
					console.log(response);
				});
			};
		}]);
});
