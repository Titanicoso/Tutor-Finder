'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('subjectService', ['restApiService', '$q', function(restService, $q) {
        
        this.getAvailable = function() {
            return restService.get('subjects/available', false);
        };
    }]);

});
