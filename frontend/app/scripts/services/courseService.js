'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('courseService', ['restApiService', '$q', function(restService, $q) {
        
        this.getCourse = function(professorId, subjectId) {
            return restService.get('courses/' + professorId + '_' + subjectId, false);
        };

        this.getComments = function(professorId, subjectId, page) {
            return restService.get('courses/' + professorId + '_' + subjectId + '/comments?page=' + page, true);
        };

        // TODO: Filter
        /* this.filterCourses = function() {
            return restService.get('conversations?page=' + page, true);
        } */

        this.contact = function(professorId, subjectId, message) {
            return restService.post('courses/' + professorId + '_' + subjectId, {message: message}, false);
        };

        this.comment = function(professorId, subjectId, message, rating) {
            return restService.post('courses/' + professorId + '_' + subjectId, {commentBody: message, rating: rating}, false);
        };

        this.create = function(subjectId, description, price) {
            return restService.post('courses', {subject: subjectId, description: description, price: price}, false);
        };

        this.reserve = function(professorId, subjectId, day, startHour, endHour) {
            return restService.post('courses/' + professorId + '_' + subjectId + '/reservations', 
                    {startHour: startHour, endHour: endHour, day: day}, false);
        };

        this.modify = function(subjectId, description, price) {
            return restService.put('courses', {subject: subjectId, description: description, price: price}, false);
        };

        this.delete = function(professorId, subjectId) {
            return restService.delete('courses/' + professorId + '_' + subjectId, false);
        };
    }]);

});
