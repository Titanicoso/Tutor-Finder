'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('areaService', ['restApiService', '$q', function(restService, $q) {
        
        this.getArea = function(id) {
            return restService.get(`areas/${id}`);
        }

        function getAreaImage(id) {
            return restService.get(`areas/${id}/image`);
        }

        this.getAreaImage = function (id) {
            getAreaImage(id);
        }

        this.getAreaCourses = function(id, page = 1) {
            return restService.get(`areas/${id}/courses?page=${page}`, true);
        }

        this.getAreas = function(query, page = 1) {
            return restService.get(`areas?q=${query}&page=${page}`, true);
        }

        /*this.getAreasWithImages = function(query, page) {
            return restService.get(`areas?q=${query}&page=${page}`, true)
            .then(function(response) {
                let images = [];
                response.data.forEach(function (area) {
                    images.push(getAreaImage(area.id));
                });
                
                Promise.all(images).then(function (data) {
                    return $q.resolve({response: response, images: data});
                });
            });
        }*/
    }]);

});
