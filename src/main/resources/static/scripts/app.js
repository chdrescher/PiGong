'use strict';

/**
 * @ngdoc overview
 * @name yapp
 * @description
 * # yapp
 *
 * Main module of the application.["ui.router", "snap", "ngAnimate"]
 */
 angular
 .module('yapp', [
    'ui.router',
    'snap',
    'ngAnimate'
    ])
 .config(function($stateProvider, $urlRouterProvider) {


    $urlRouterProvider.otherwise('/login');

    $stateProvider
    .state('login', {
      url: '/login',
      templateUrl: 'views/login.html',
      controller: 'LoginCtrl'
  })
    .state('audiofiles', {
        url: '/audiofiles',
        templateUrl: 'views/dashboard/overview.html',
            controller: 'AudioFilesCtrl'
    })
        .state('upload', {
            url: '/upload',
            templateUrl: 'views/dashboard/upload.html',
            controller: 'UploadCtrl'
        });

});
