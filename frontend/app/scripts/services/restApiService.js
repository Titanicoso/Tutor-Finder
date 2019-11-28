'use strict';
define(['tutorFinder', 'services/authService'], function(tutorFinder) {

    tutorFinder.service('restApiService', ['$http', '$q', 'apiBaseUrl', 'authService', function($http, $q, apiBaseUrl, authService) {

        function resolveLinks(headers) {
            var header = headers('link');

            if (!header || header === '') {
                return;
            }

            var links = header.split(',');
            var resolved = {next: undefined, prev: undefined, first: undefined, last: undefined};
            links.forEach(function (link) {
                var url = link.match('<.*>')[0].replace(/<(.*)>/, '$1').trim();
                var number = url.match(/page=([0-9]+)/)[1];
                var rel = link.match('rel=.*')[0].replace(/rel="(.*)"/, '$1').trim();

                switch (rel) {
                    case 'next': resolved.next = {url: url, number: number}; break;
                    case 'prev': resolved.prev = {url: url, number: number}; break;
                    case 'last': resolved.last = {url: url, number: number}; break;
                    default: resolved.first = {url: url, number: number}; break;
                }
            });

            return resolved;
        };

        this.absoluteGet = function(url, hasLinks) {

            var authHeaders = authService.getAuthHeaders();

            return $http.get(url, authHeaders)
                .then(function(response) {
                    if (hasLinks) {
                        return {data: response.data, links: resolveLinks(response.headers)};
                    } 
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };

        this.get = function(url, hasLinks) {

            var authHeaders = authService.getAuthHeaders();

            return $http.get(apiBaseUrl + '/' + url, authHeaders)
                .then(function(response) {
                    if (hasLinks) {
                        return {data: response.data, links: resolveLinks(response.headers)};
                    } 
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };

        this.delete = function(url) {

            var authHeaders = authService.getAuthHeaders();
            return $http.delete(apiBaseUrl + '/' + url, authHeaders)
                .then(function(response) {
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };

        this.post = function(url, data, isMultipart) {

            var authHeaders = authService.getAuthHeaders();
            var payload = data;

            if (isMultipart) {
                authHeaders.headers['Content-type'] = undefined;
            } else {
                payload = JSON.stringify(data);
            }

            return $http.post(apiBaseUrl + '/' + url, payload, authHeaders)
                .then(function(response) {
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };

        this.put = function(url, data, isMultipart) {

            var authHeaders = authService.getAuthHeaders();
            var payload = data;

            if (isMultipart) {
                authHeaders.headers['Content-type'] = undefined;
            } else {
                payload = JSON.stringify(data);
            }

            return $http.put(apiBaseUrl + '/' + url, payload, authHeaders)
                .then(function(response) {
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };
    }]);

});
