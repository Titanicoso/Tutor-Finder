'use strict';
define(['tutorFinder'], function(tutorFinder) {

    tutorFinder.service('authService', ['$window', '$http', '$q', 'apiBaseUrl', function($window, $http, $q, apiUrl) {

        this.getAccessToken = function() {
            let accessToken = $window.localStorage.getItem('access_token');

            if (!accessToken) {
                accessToken = $window.sessionStorage.getItem('access_token');
            }

            return accessToken;
		}
		
		this.getAuthHeaders = function() {
			return {headers: {'Authorization': `Bearer ${this.getAccessToken()}`}}
		}

        this.setAccessToken = function(authorization, mustPersist) {
			let token = authorization.replace('Bearer ', '')
			if (mustPersist)
				$window.localStorage.setItem('access_token', token);
			else
				$window.sessionStorage.setItem('access_token', token);
        }
        
        this.clearSession = function() {
            $window.localStorage.clear();
            $window.sessionStorage.clear();
        }

        this.login = function(username, password, rememberMe) {
			let service = this;
            return $http.get(`${apiUrl}/authenticate?username=${username}&password=${password}`)
				.then(function(response) {
					service.setAccessToken(response.headers('Authorization'), rememberMe);
					//TODO: add user get to know role
					return $http.get(`${apiUrl}/user`, service.getAuthHeaders());
				})
				.catch(function(response) {
					return $q.reject(response);
				});
		};
    }]);

});