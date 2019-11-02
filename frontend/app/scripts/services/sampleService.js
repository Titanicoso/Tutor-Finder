'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.service('sampleService',['$http', '$q', 'apiBaseUrl', function($http, $q, apiBaseUrl) {
		this.get = function(url) {
			return $http.get(apiBaseUrl + '/' + url)
				.then(function(response) {
					return response.data;
				})
				.catch(function(response) {
					$q.reject(response);
				});
			};
		}]);
});
