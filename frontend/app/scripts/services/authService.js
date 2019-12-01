'use strict';
define(['tutorFinder'], function(tutorFinder) {

    tutorFinder.service('authService', ['$window', '$http', '$q', '$rootScope', 'apiBaseUrl', function($window, $http, $q, $rootScope, apiUrl) {

		var currentUser;
		var accessToken;
		var redirectUrl;
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

		this.getRedirectUrl = function() {
			return this.redirectUrl;
		};

		this.setRedirectUrl = function(redirectUrl, params) {
			return this.redirectUrl = {url: redirectUrl, params: params};
		};

		this.checkRoles = function(roles) {
			var roleCheck = {authorization: false, canPromote: false};

			return this.getUser()
			.then(function(user) {
				self.currentUser = user;
			})
			.catch(function() {
				self.currentUser = undefined;
			})
			.then(function() {
				if (!roles) {
					roleCheck.authorization = true;
				} else if (!roles.loggedIn) {
					roleCheck.authorization = self.currentUser === undefined;
				} else if (roles.needsProfessor) {
					roleCheck.authorization = self.currentUser !== undefined && self.currentUser.professor;
					roleCheck.canPromote = self.currentUser === undefined;
				} else {
					roleCheck.authorization = self.currentUser !== undefined;
					roleCheck.canPromote = true;
				}

				if (!roleCheck.authorization && roleCheck.canPromote) {
					return $q.reject(true);
				} else if (!roleCheck.authorization && !roleCheck.canPromote) {
					return $q.reject(false);
				}
	
				return $q.resolve(true);
			});
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

		this.forgotPassword = function(password, token) {
            return $http.post(apiUrl + '/user/forgot_password/' + token, JSON.stringify({password: password}));
        };

		this.refreshAccessToken = function(response) {
			var service = this;
			service.setAccessToken(response.headers('Authorization'), false);
			$http.get(apiUrl + '/user', service.getAuthHeaders())
			.then(function(response) {
				service.currentUser = response.data;
				$rootScope.$broadcast('user_update');
				return service.currentUser;
			})
			.catch(function(response) {
				return $q.reject(response);
			});
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
					$rootScope.$broadcast('user_update');
					return service.currentUser;
				})
				.catch(function(response) {
					return $q.reject(response);
				});
		};

		this.register = function(name, lastname, email, username, password) {
        	return $http.post(apiUrl + '/user/', JSON.stringify({name: name, lastname: lastname, email: email, username: username, password: password}));
        };

		this.logout = function() {
			self.clearSession();
			$rootScope.$broadcast('user_update');
		};

		this.getCurrentUser = function(force) {
			if (this.currentUser && !force) {
				return this.currentUser;
			}

			var service = this;
			if (!service.getAccessToken()) {
				return null;
			}

			$http.get(apiUrl + '/user', service.getAuthHeaders())
			.then(function(response) {
				service.currentUser = response.data;
				$rootScope.$broadcast('user_update');
				return service.currentUser;
			})
			.catch(function() {
				return null;
			});
		};

		this.getUser = function() {
			if (this.currentUser) {
				return $q.resolve(this.currentUser);
			}

			var service = this;
			if (!service.getAccessToken()) {
				return $q.resolve(undefined);
			}

			return $http.get(apiUrl + '/user', service.getAuthHeaders());
		};

    }]);

});