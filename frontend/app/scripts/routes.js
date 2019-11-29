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
                controller: 'ConversationsCtrl'
            },
            '/conversation/:id': {
                templateUrl: '/views/conversation.html',
                controller: 'ConversationCtrl'
            },
            '/course': {
                templateUrl: '/views/course.html',
                controller: 'CourseCtrl'
            },
            '/forgotPassword': {
                templateUrl: '/views/forgotPassword.html',
                controller: 'ForgotPasswordCtrl'
            },
            '/login': {
                templateUrl: '/views/login.html',
                controller: 'LoginCtrl'
            },
            '/course/files': {
                templateUrl: '/views/courseFiles.html',
                controller: 'CourseFilesCtrl'
            },
            '/requests': {
                templateUrl: '/views/myClasses.html',
                controller: 'RequestsCtrl'
            },
            '/profile': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl'
            },
            '/professor/:username': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl'
            },
            '/register': {
                templateUrl: '/views/register.html',
                controller: 'RegisterCtrl'
            },
            '/reservations': {
                templateUrl: '/views/reservations.html',
                controller: 'ReservationsCtrl'
            },
            '/reserveClass': {
                templateUrl: '/views/reserveClass.html',
                controller: 'ReserveClassCtrl'
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