'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('conversationService', ['restApiService', '$q', function(restService, $q) {
        
        this.getConversation = function(id) {
            return restService.get('conversations/' + id, false);
        };

        this.getMessages = function(id) {
            return restService.get('conversations/' + id + '/messages', false);
        };

        this.getConversations = function(page) {
            return restService.get('conversations?page=' + page, true);
        };

        this.sendMessage = function(id, message) {
            return restService.post('conversations/' + id, {message: message}, false);
        };
    }]);

});
