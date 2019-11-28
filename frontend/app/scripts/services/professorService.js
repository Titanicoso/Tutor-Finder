'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('professorService', ['restApiService', function(restService) {
        
        this.getProfessors = function(query, page) {
            return restService.get('professors?q=' + query + '&page=' + page, true);
        };

        this.getProfessor = function(username) {
            return restService.get('professors/' + username, false);
        };

        this.getProfessorCourses = function(username, page) {
            return restService.get('professors/' + username + '/courses?page=' + page, true);
        };

        this.getProfessorSchedule = function(username) {
            return restService.get('professors/' + username + '/schedule', false);
        };

        // To show image use img with ng-src and image url directly
        this.getProfessorImage = function(username) {
            return restService.get('professors/' + username + '/image', false);
        };

        this.getCourses = function(page) {
            return restService.get('user/courses?page=' + page, true);
        };

        this.getSchedule = function() {
            return restService.get('user/schedule', false);
        };

        this.getFullRequests = function(page) {
            return restService.get('user/requests?page=' + page + '&fullDetail=true', true);
        };

        this.getRequests = function(page) {
            return restService.get('user/requests?page=' + page, true);
        };

        this.getRequest = function(id) {
            return restService.get('user/requests/' + id, false);
        };

        this.approveRequest = function(id) {
            return restService.put('user/requests/' + id, '');
        };

        this.denyRequest = function(id) {
            return restService.delete('user/requests/' + id);
        };
    }]);

});
