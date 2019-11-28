'use strict';
define(['tutorFinder', 'services/conversationService'], function(tutorFinder) {

	tutorFinder.controller('ConversationCtrl', ConversationCtrl);
	
	ConversationCtrl.$inject = ['$scope', '$rootScope', '$route', 'conversationService'];
	function ConversationCtrl($scope, $rootScope, $route, conversationService) {
		$rootScope.appendTitle('CONVERSATION');
		var id = $route.current.params.id;

		$scope.message = {text: ''};

		this.refresh = function() {
			conversationService.getMessages(id)
			.then(function(messages) {
				$scope.messages = messages;
			})
			.catch(function(err) {
				console.log(err);
			});
		};

		var ctrl = this;
		$scope.send = function(form) {
			if (form.$valid) {
				conversationService.sendMessage(id, $scope.message.text)
				.then(function() {
					$scope.message = {text: ''};
					form.$setPristine();
					ctrl.refresh();
				})
				.catch(function(err) {
					console.log(err);
				});
			}
		};

		this.refresh();
	};
});
