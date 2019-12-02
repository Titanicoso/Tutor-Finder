'use strict';
define(['tutorFinder', 'services/conversationService', 'services/authService'], function(tutorFinder) {

	tutorFinder.controller('ConversationCtrl', ConversationCtrl);
	
	ConversationCtrl.$inject = ['$scope', '$rootScope', '$route', '$location', 'conversationService', 'authService', 'toastService'];
	function ConversationCtrl($scope, $rootScope, $route, $location, conversationService, authService, toastService) {
		$rootScope.appendTitle('CONVERSATION');
		var id = parseInt($route.current.params.id, 10);

		if (id === undefined || id !== id) {
			toastService.showAction('INVALID_PARAMETERS');
			$location.url('/');
			return;
		}

		if (!$scope.currentUser) { 
			$scope.currentUser = authService.getCurrentUser();
		}

		$scope.message = {text: ''};

		this.refresh = function() {
			conversationService.getMessages(id)
			.then(function(messages) {
				$scope.messages = messages;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					case 403: toastService.showAction('FORBIDDEN_CONVERSATION'); $location.url('/'); break;
					default: toastService.showAction('OOPS'); break;
				}
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
					switch (err.status) {
						case -1: toastService.showAction('NO_CONNECTION'); break;
						case 403: toastService.showAction('FORBIDDEN_CONVERSATION'); $location.url('/'); break;
						default: toastService.showAction('ERROR_SENDING_MESSGE'); break;
					}
				});
			}
		};

		this.refresh();
	};
});
