'use strict';
define(['tutorFinder', 'services/conversationService'], function(tutorFinder) {

	tutorFinder.controller('ConversationsCtrl', ConversationsCtrl);
	
	ConversationsCtrl.$inject = ['$scope', '$rootScope', 'conversationService'];
	function ConversationsCtrl($scope, $rootScope, conversationService) {
		$rootScope.appendTitle('CONVERSATIONS');

		var currentPage = 1;

		$scope.getPage = function(number) {
			conversationService.getConversations(number)
			.then(function(conversations) {
				$scope.conversations = conversations;
				currentPage = number;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		$scope.getPage(currentPage);
	};
});
