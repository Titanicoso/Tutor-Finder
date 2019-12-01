'use strict';
define([
	'routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'angular-material',
	'angular-animate',
	'angular-aria',
	'angular-messages',
	'bootstrap',
	'angular-bootstrap',
	'angular-translate'
	],
	function(config, dependencyResolverFor, i18n) {
		var tutorFinder = angular.module('tutorFinder', [
			'ngRoute',
			'ngMaterial',
			'ngMessages',
			'pascalprecht.translate',
			'ui.bootstrap'
		]);
		tutorFinder.config([
			'$routeProvider',
			'$controllerProvider',
			'$compileProvider',
			'$filterProvider',
			'$provide',
			'$translateProvider',
			'$locationProvider',
			'$httpProvider',
			function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, $locationProvider, $httpProvider) {

				tutorFinder.controller = $controllerProvider.register;
				tutorFinder.directive = $compileProvider.directive;
				tutorFinder.filter = $filterProvider.register;
				tutorFinder.factory = $provide.factory;
				tutorFinder.service = $provide.service;

				if (config.routes !== undefined) {
					angular.forEach(config.routes, function(route, path) {
						var resolved = dependencyResolverFor(['controllers/' + route.controller]);
						angular.forEach(route.resolve, function(resolver, property) {
							resolved[property] = resolver;
						});

						$routeProvider.when(path, {
							templateUrl: route.templateUrl, 
							resolve: resolved, 
							controller: route.controller, gaPageTitle: route.gaPageTitle,
							roles: route.roles
						});
					});
				}
				if (config.defaultRoutePath !== undefined) {
					$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
				}

				$translateProvider.translations('preferredLanguage', i18n);
				$translateProvider.preferredLanguage('preferredLanguage');
			}])
			.run(['$rootScope', '$location', 'authService', 'toastService', function($rootScope, $location, authService, toastService) {
				$rootScope.$on('$routeChangeStart', function(event, next, current) {
					if (next && next.$$route) {
						var authorized = authService.checkRoles(next.$$route.roles);
						var path = next.$$route.originalPath;
						var params = next.params;

						if (!authorized.authorization && authorized.canPromote) {
							authService.setRedirectUrl(path, params);
							$location.url('/login');
						} else if (!authorized.authorization && !authorized.canPromote) {
							toastService.showAction('FORBIDDEN_ERROR');
							$location.url('/');
						} else if (path !== '/login' && path !== '/register') {
							authService.setRedirectUrl(undefined, undefined);
						}
					}
				});
			}])
			.value('apiBaseUrl', 'http://localhost:8080/api');
		return tutorFinder;
	}
);
