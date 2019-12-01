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
            '/conversations': {
                templateUrl: '/views/conversations.html',
                controller: 'ConversationsCtrl',
                roles: {loggedIn: true, needsProfessor: false}
            },
            '/conversation/:id': {
                templateUrl: '/views/conversation.html',
                controller: 'ConversationCtrl',
                roles: {loggedIn: true, needsProfessor: false}
            },
            '/course': {
                templateUrl: '/views/course.html',
                controller: 'CourseCtrl'
            },
            '/forgotPassword': {
                templateUrl: '/views/forgotPassword.html',
                controller: 'ForgotPasswordCtrl',
                roles: {loggedIn: false, needsProfessor: false}
            },
            '/login': {
                templateUrl: '/views/login.html',
                controller: 'LoginCtrl',
                roles: {loggedIn: false, needsProfessor: false}
            },
            '/course/files': {
                templateUrl: '/views/courseFiles.html',
                controller: 'CourseFilesCtrl',
                roles: {loggedIn: true, needsProfessor: false}
            },
            '/requests': {
                templateUrl: '/views/myClasses.html',
                controller: 'RequestsCtrl',
                roles: {loggedIn: true, needsProfessor: true}
            },
            '/profile': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl',
                roles: {loggedIn: true, needsProfessor: true}
            },
            '/professor/:username': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl'
            },
            '/register': {
                templateUrl: '/views/register.html',
                controller: 'RegisterCtrl',
                roles: {loggedIn: false, needsProfessor: false}
            },
            '/reservations': {
                templateUrl: '/views/reservations.html',
                controller: 'ReservationsCtrl',
                roles: {loggedIn: true, needsProfessor: false}
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