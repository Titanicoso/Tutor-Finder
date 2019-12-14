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
							roles: route.roles,
							reloadOnSearch: route.reloadOnSearch !== undefined ? route.reloadOnSearch : true
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
				$rootScope.$on('$routeChangeSuccess', function(event, current, previous) {
					if (current && current.$$route) {
						var path = current.$$route.originalPath;

						if (path !== '/login' && path !== '/register') {
							authService.setRedirectUrl(undefined, undefined);
							authService.setRequestRedo(undefined);
						}

						if (path !== '/searchResults') {
							$rootScope.resetFilters();
						}
					}
				});

				$rootScope.$on('$routeChangeError', function(event, current, previous, error) {
					if (current && current.$$route) {
						var path = current.$$route.originalPath;

						if (Object.keys(current.pathParams).length > 0) {
							for (var key in current.pathParams) {
								path = path.replace(':' + key, current.pathParams[key]);
							}
						} else {
							var params = current.params;
						}


						if (error) {
							authService.setRedirectUrl(path, params);
							$location.url('/login');
						} else {
							toastService.showAction('FORBIDDEN_ERROR');
							$location.url('/');
						}
					}
				});
			}])
			.value('apiBaseUrl', 'http://localhost:8080/api');
		return tutorFinder;
	}
);
