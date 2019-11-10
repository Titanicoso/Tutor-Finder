'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('courseService', ['restApiService', '$q', function(restService, $q) {
        
        this.getCourse = function(professorId, subjectId) {
            return restService.get('courses/' + professorId + '_' + subjectId, false);
        };

        this.getComments = function(professorId, subjectId, page) {
            return restService.get('courses/' + professorId + '_' + subjectId + '/comments?page=' + page, true);
        };

        this.filterCourses = function(query, startHour, endHour, minPrice, maxPrice, days, page) {

            var parameters = '?page=' + page;

            if (query && query.length > 0) {
                parameters = parameters + '&q=' + query;
            }

            if (startHour) {
                parameters = parameters + '&start=' + startHour;
            }

            if (endHour) {
                parameters = parameters + '&end=' + endHour;
            }

            if (minPrice) {
                parameters = parameters + '&min=' + minPrice;
            }

            if (maxPrice) {
                parameters = parameters + '&max=' + maxPrice;
            }

            if (days && days.length > 0) {
                days.forEach(function(day) {
                    parameters = parameters + '&days=' + day;
                });
            }

            return restService.get('courses/' + parameters, true);
        };

        this.contact = function(professorId, subjectId, message) {
            return restService.post('courses/' + professorId + '_' + subjectId, {message: message});
        };

        this.comment = function(professorId, subjectId, message, rating) {
            return restService.post('courses/' + professorId + '_' + subjectId, {commentBody: message, rating: rating});
        };

        this.create = function(subjectId, description, price) {
            return restService.post('courses', {subject: subjectId, description: description, price: price});
        };

        this.reserve = function(professorId, subjectId, day, startHour, endHour) {
            return restService.post('courses/' + professorId + '_' + subjectId + '/reservations', 
                    {startHour: startHour, endHour: endHour, day: day});
        };

        this.modify = function(subjectId, description, price) {
            return restService.put('courses', {subject: subjectId, description: description, price: price});
        };

        this.delete = function(professorId, subjectId) {
            return restService.delete('courses/' + professorId + '_' + subjectId);
        };
    }]);

});
