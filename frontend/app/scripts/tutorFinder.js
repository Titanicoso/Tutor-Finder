'use strict';
define([
	'routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'angular-cookies',
	'bootstrap',
	'angular-translate'
	],
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
				}])
				.value('apiBaseUrl', 'http://localhost:8080/api');
		return tutorFinder;
	}
);
