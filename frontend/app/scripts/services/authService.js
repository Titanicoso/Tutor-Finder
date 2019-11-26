'use strict';
define(['tutorFinder'], function(tutorFinder) {

    tutorFinder.service('authService', ['$window', '$http', '$q', '$rootScope', 'apiBaseUrl', function($window, $http, $q, $rootScope, apiUrl) {

		var currentUser;
		var accessToken;
		var self = this;

        this.getAccessToken = function() {

			if (this.accessToken) {
				return this.accessToken;
			}

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

			return {headers: {'Authorization': 'Bearer ' + token}};
		};

        this.setAccessToken = function(authorization, mustPersist) {
			var token = authorization.replace('Bearer ', '');
			if (mustPersist) {
				$window.localStorage.setItem('access_token', token);
			} else {
				$window.sessionStorage.setItem('access_token', token);
			}
			this.accessToken = token;
        };
        
        this.clearSession = function() {
            $window.localStorage.clear();
			$window.sessionStorage.clear();
			this.currentUser = undefined;
			this.accessToken = undefined;
		};

        this.login = function(username, password, rememberMe) {
			var service = this;
			return $http.post(apiUrl + '/authenticate', {username: username, password: password},
			 	{
					headers: {'Content-Type': 'application/x-www-form-urlencoded'},
					transformRequest: function(req) {
						var urlencoded = [];
						for (var data in req) {
							urlencoded.push(encodeURIComponent(data) + '=' + encodeURIComponent(req[data]));
						}
						return urlencoded.join('&');
					}
				})
				.then(function(response) {
					service.setAccessToken(response.headers('Authorization'), rememberMe);
					return $http.get(apiUrl + '/user', service.getAuthHeaders());
				})
				.then(function(response) {
					service.currentUser = response.data;
					$window.sessionStorage.setItem('current_user', JSON.stringify(service.currentUser));
					$rootScope.$broadcast('user_update');
					return service.currentUser;
				})
				.catch(function(response) {
					return $q.reject(response);
				});
		};

		this.logout = function() {
			self.clearSession();
			$rootScope.$broadcast('user_update');
		};

		this.getCurrentUser = function() {
			if (this.currentUser) {
				return this.currentUser;
			}

			var currentUser = JSON.parse($window.sessionStorage.getItem('current_user'));

			if (currentUser) {
				this.currentUser = currentUser;
				return currentUser;
			}

			var service = this;
			$http.get(apiUrl + '/user', service.getAuthHeaders())
			.then(function(response) {
				service.currentUser = response.data;
				$window.sessionStorage.setItem('current_user', JSON.stringify(service.currentUser));
				$rootScope.$broadcast('user_update');
				return service.currentUser;
			})
			.catch(function() {
				return null;
			});
		};

    }]);

});