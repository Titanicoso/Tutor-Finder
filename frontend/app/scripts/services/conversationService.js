'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('conversationService', ['restApiService', '$q', function(restService, $q) {
        
        this.getConversation = function(id) {
            return restService.get(`conversations/${id}`);
        }

        this.getMessages = function(id) {
            return restService.get(`conversations/${id}/messages`);
        }

        this.getConversations = function(page = 1) {
            return restService.get(`conversations?page=${page}`, true);
        }

        this.sendMessage = function(id, message) {
            return restService.post(`conversations/${id}`, {message: message});
        }
    }]);

});
