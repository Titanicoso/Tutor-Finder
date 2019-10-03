'use strict';
define([
	'routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'angular-cookies',
	'bootstrap',
	'angular-translate'],
	function(config, dependencyResolverFor, i18n) {
		var tutorFinder = angular.module('tutorFinder', [
			'ngRoute',
			'ngCookies',
			'pascalprecht.translate'/* ,
			'ui.bootstrap' */
		]);
		tutorFinder.config([
			'$routeProvider',
			'$controllerProvider',
			'$compileProvider',
			'$filterProvider',
			'$provide',
			'$translateProvider',
			'$locationProvider', /* capaz innecesario */
			function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, $locationProvider) {

				tutorFinder.controller = $controllerProvider.register;
				tutorFinder.directive = $compileProvider.directive;
				tutorFinder.filter = $filterProvider.register;
				tutorFinder.factory = $provide.factory;
				tutorFinder.service = $provide.service;

				if (config.routes !== undefined) {
					angular.forEach(config.routes, function(route, path) {
						$routeProvider.when(path, {
							templateUrl: route.templateUrl, 
							resolve: dependencyResolverFor(['controllers/' + route.controller]), 
							controller: route.controller, gaPageTitle: route.gaPageTitle
						});
					});
				}
				if (config.defaultRoutePath !== undefined) {
					$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
				}

				$translateProvider.translations('preferredLanguage', i18n);
				$translateProvider.preferredLanguage('preferredLanguage');
		}]);
		/* tutorFinder.run([
			'$rootScope', 
			'$location', 
			'$cookies', 
			'$http',
			function($rootScope, $location, $cookies, $http) {
				// keep user logged in after page refresh
				$rootScope.globals = $cookies.getObject('globals') || {};
				if ($rootScope.globals.currentUser) {
					$http.defaults.headers.common.Authorization = 'Basic ' + $rootScope.globals.currentUser.authdata;
				}
		
				$rootScope.$on('$locationChangeStart', function (event, next, current) {
					// redirect to login page if not logged in and trying to access a restricted page
					var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
					var loggedIn = $rootScope.globals.currentUser;
					if (restrictedPage && !loggedIn) {
						$location.path('/login');
					}
				});
			}
		]); */
		return tutorFinder;
	}
);
