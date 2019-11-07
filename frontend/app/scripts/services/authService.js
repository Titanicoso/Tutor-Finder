'use strict';
define(['tutorFinder'], function(tutorFinder) {

    tutorFinder.service('authService', ['$window', '$http', '$q', 'apiBaseUrl', function($window, $http, $q, apiUrl) {

        this.getAccessToken = function() {
            var accessToken = $window.localStorage.getItem('access_token');

            if (!accessToken) {
                accessToken = $window.sessionStorage.getItem('access_token');
            }

            return accessToken;
		};
		
		this.getAuthHeaders = function() {

			var token = this.getAccessToken();
			if (!token || token === '') {
				return null;
			}

			return {headers: {'Authorization': 'Bearer' + token}};
		};

        this.setAccessToken = function(authorization, mustPersist) {
			var token = authorization.replace('Bearer ', '');
			if (mustPersist) {
				$window.localStorage.setItem('access_token', token);
			} else {
				$window.sessionStorage.setItem('access_token', token);
			}
        };
        
        this.clearSession = function() {
            $window.localStorage.clear();
            $window.sessionStorage.clear();
        };

        this.login = function(username, password, rememberMe) {
			var service = this;
            return $http.get(apiUrl + '/authenticate?username=' + username + '&password=' + password)
				.then(function(response) {
					service.setAccessToken(response.headers('Authorization'), rememberMe);
					return $http.get(apiUrl + '/user', service.getAuthHeaders());
				})
				.then(function(response) {
					return response.data;
				})
				.catch(function(response) {
					return $q.reject(response);
				});
		};
    }]);

});