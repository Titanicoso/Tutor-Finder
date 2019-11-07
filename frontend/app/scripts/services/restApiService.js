'use strict';
define(['tutorFinder', 'services/authService'], function(tutorFinder) {

    tutorFinder.service('restApiService', ['$http', '$q', 'apiBaseUrl', 'authService', function($http, $q, apiBaseUrl, authService) {

        function resolveLinks(headers) {
            const header = headers('link');

            if (!header || header == '') {
                return ;
            }

            let links = header.split(',');
            let resolved = [];
            links.forEach(function (link) {
                let url = link.match('<.*>')[0].replace(/<(.*)>/, '$1').trim();
                let number = url.match(/page=([0-9]+)/)[1];
                let rel = link.match('rel=.*')[0].replace(/rel="(.*)"/, '$1').trim();
                resolved.push({url: url, number: number, rel: rel});
            });

            return resolved;
        };

        this.get = function(url, hasLinks = false) {

            const authHeaders = authService.getAuthHeaders();

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

            const authHeaders = authService.getAuthHeaders();
            return $http.delete(apiBaseUrl + '/' + url, authHeaders)
                .then(function(response) {
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };

        this.post = function(url, data) {

            const authHeaders = authService.getAuthHeaders();

            return $http.post(apiBaseUrl + '/' + url, JSON.stringify(data), authHeaders)
                .then(function(response) {
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };

        this.put = function(url, data) {

            const authHeaders = authService.getAuthHeaders();

            return $http.put(apiBaseUrl + '/' + url, JSON.stringify(data), authHeaders)
                .then(function(response) {
                    return response.data;
                })
                .catch(function(err) {
                    return $q.reject(err);
                });
        };
    }]);

});
