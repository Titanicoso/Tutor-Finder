'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/area/:id': {
                templateUrl: '/views/area.html',
                controller: 'AreaCtrl'
            },
            '/error': {
                templateUrl: '/views/error.html',
                controller: 'HomeCtrl'
            },
            '/conversations': {
                templateUrl: '/views/conversations.html',
                controller: 'ConversationsCtrl',
                roles: {loggedIn: true, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/conversation/:id': {
                templateUrl: '/views/conversation.html',
                controller: 'ConversationCtrl',
                roles: {loggedIn: true, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/course': {
                templateUrl: '/views/course.html',
                controller: 'CourseCtrl'
            },
            '/forgotPassword': {
                templateUrl: '/views/forgotPassword.html',
                controller: 'ForgotPasswordCtrl',
                roles: {loggedIn: false, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/login': {
                templateUrl: '/views/login.html',
                controller: 'LoginCtrl',
                roles: {loggedIn: false, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/course/files': {
                templateUrl: '/views/courseFiles.html',
                controller: 'CourseFilesCtrl',
                roles: {loggedIn: true, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/requests': {
                templateUrl: '/views/myClasses.html',
                controller: 'RequestsCtrl',
                roles: {loggedIn: true, needsProfessor: true},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/profile': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl',
                roles: {loggedIn: true, needsProfessor: true},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/professor/:username': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl'
            },
            '/register': {
                templateUrl: '/views/register.html',
                controller: 'RegisterCtrl',
                roles: {loggedIn: false, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/reservations': {
                templateUrl: '/views/reservations.html',
                controller: 'ReservationsCtrl',
                roles: {loggedIn: true, needsProfessor: false},
                resolve: {
                    roleCheck: ['$route', 'authService', function($route, authService) {
                        var roles = $route.current.$$route.roles;
						return authService.checkRoles(roles);
					}]
                }
            },
            '/searchResults': {
                templateUrl: '/views/searchResults.html',
                controller: 'SearchCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});