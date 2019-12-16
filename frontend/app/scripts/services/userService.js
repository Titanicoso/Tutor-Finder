'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('userService', ['restApiService', function(restService) {
        
        this.getUser = function() {
            return restService.get('user', false);
        };

        this.getReservations = function(page) {
            return restService.get('user/reservations?page=' + page, true);
        };

        this.getFullReservations = function(page) {
            return restService.get('user/reservations?page=' + page + '&fullDetail=true', true);
        };

        this.getReservation = function(id) {
            return restService.get('user/reservations/' + id, false);
        };

        this.forgotPasswordRequest = function(email) {
            return restService.post('user/forgot_password', {email: email});
        };

        this.forgotPasswordValidity = function(token) {
            return restService.get('user/forgot_password/' + token);
        };

        this.modify = function(description, picture) {
            var data = new FormData();

            if (picture) {
                data.append('picture', picture);
            }
            data.append('description', new Blob([description], {type: 'application/json'}));

            return restService.put('user/', data, true);
        };

        this.upgrade = function(description, picture) {
            var data = new FormData();

            data.append('picture', picture);
            data.append('description', new Blob([description], {type: 'application/json'}));
            return restService.post('user/upgrade', data, true);
        };
    }]);

});
