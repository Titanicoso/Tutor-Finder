'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/conversations': {
                templateUrl: '/views/conversations.html',
                controller: 'ConversationsCtrl',
            },
            '/course': {
                templateUrl: '/views/course.html',
                controller: 'CourseCtrl',
            },
            '/createCourse': {
                templateUrl: '/views/createCourse.html',
                controller: 'CreateCourseCtrl',
            },
            '/forgotPassword': {
                templateUrl: '/views/forgotPassword.html',
                controller: 'ForgotPasswordCtrl',
            },
            '/login': {
                templateUrl: '/views/login.html',
                controller: 'LoginCtrl',
            },
            '/modifyCourse': {
                templateUrl: '/views/modifyCourse.html',
                controller: 'ModifyCourseCtrl',
            },
            '/modifyProfessorProfile': {
                templateUrl: '/views/modifyProfessorProfileForm.html',
                controller: 'ModifyProfessorProfileCtrl',
            },
            '/myClasses': {
                templateUrl: '/views/myClasses.html',
                controller: 'MyClassesCtrl',
            },
            '/profile': {
                templateUrl: '/views/profile.html',
                controller: 'ProfileCtrl',
            },
            '/profileForProfessor': { // TODO: route should be just '/profile'
                templateUrl: '/views/profileForProfessor.html',
                controller: 'ProfessorProfileCtrl',
            },
            '/register': {
                templateUrl: '/views/register.html',
                controller: 'RegisterCtrl',
            },
            '/registerAsProfessor': {
                templateUrl: '/views/registerAsProfessorForm.html',
                controller: 'RegisterAsProfessorCtrl',
            },
            '/reservations': {
                templateUrl: '/views/reservations.html',
                controller: 'ReservationsCtrl',
            },
            '/reserveClass': {
                templateUrl: '/views/reserveClass.html',
                controller: 'ReserveClassCtrl',
            },
            '/resetPassword': {
                templateUrl: '/views/resetPassword.html',
                controller: 'ResetPasswordCtrl',
            },
            '/searchResults': {
                templateUrl: '/views/searchResults.html',
                controller: 'SearchResultsCtrl',
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});