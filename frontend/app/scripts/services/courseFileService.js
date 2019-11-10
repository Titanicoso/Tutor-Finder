'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('courseFileService', ['restApiService', function(restService) {
        
        this.getCourseFiles = function(professorId, subjectId) {
            return restService.get('courses/' + professorId + '_' + subjectId + '/files', false);
        };

        // TODO: GET AND PUT

        this.delete = function(professorId, subjectId, id) {
            return restService.delete('courses/' + professorId + '_' + subjectId + '/files/' + id);
        };
    }]);

});
