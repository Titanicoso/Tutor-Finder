'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.controller('ConversationsCtrl', ConversationsCtrl);
	
	ConversationsCtrl.$inject = ['$scope', '$rootScope'];
	function ConversationsCtrl($scope, $rootScope) {

		$rootScope.appendTitle('CONVERSATIONS');
	};
});
