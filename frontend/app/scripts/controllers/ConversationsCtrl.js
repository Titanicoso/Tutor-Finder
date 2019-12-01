'use strict';
define(['tutorFinder', 'services/conversationService'], function(tutorFinder) {

	tutorFinder.controller('ConversationsCtrl', ConversationsCtrl);
	
	ConversationsCtrl.$inject = ['$scope', '$rootScope', 'conversationService', 'toastService'];
	function ConversationsCtrl($scope, $rootScope, conversationService, toastService) {
		$rootScope.appendTitle('CONVERSATIONS');

		$scope.currentPage = 1;

		$scope.getPage = function(number) {
			conversationService.getConversations(number)
			.then(function(conversations) {
				$scope.conversations = conversations;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					default: toastService.showAction('OOPS'); break;
				}
			});
		};

		$scope.getPage($scope.currentPage);
	};
});
