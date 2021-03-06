'use strict';
define(['tutorFinder', 'services/conversationService'], function(tutorFinder) {

	tutorFinder.controller('ConversationsCtrl', ConversationsCtrl);
	
	ConversationsCtrl.$inject = ['$scope', '$rootScope', 'conversationService', 'toastService', '$location', '$route', 'authService'];
	function ConversationsCtrl($scope, $rootScope, conversationService, toastService, $location, $route, authService) {
		$rootScope.appendTitle('CONVERSATIONS');

		var page = parseInt($route.current.params.page, 10);
		var self = this;

		$scope.current = {};
		$scope.current.page = page !== page ? 1 : page;
		$scope.current.previous = $scope.current.page;

		$scope.$on('$routeUpdate', function() {
			var page = parseInt($route.current.params.page, 10);
			page = page !== page ? 1 : page;
			if ($scope.current.previous !== page) {
				self.getConversations(page);
			}
		});

		$scope.getPage = function(number) {
			$location.search({page: number});
		};

		this.getConversations = function(page) {
			$scope.loading = true;
			conversationService.getConversations(page)
			.then(function(conversations) {
				$scope.conversations = conversations;
				$scope.current.page = page;
				$scope.current.previous = page;
			})
			.catch(function(err) {
				switch (err.status) {
					case -1: toastService.showAction('NO_CONNECTION'); break;
					case 401: {
						if ($scope.currentUser) {
							toastService.showAction('SESSION_EXPIRED'); 
						} 
						authService.setRedirectUrl($location.path(), $route.current.params);
						authService.logout();
						$location.url('/login');
						break;
					}
					default: toastService.showAction('OOPS'); break;
				}
			})
			.then(function() {
				$scope.loading = false;
			});
		};

		this.getConversations($scope.current.page);
	};
});
