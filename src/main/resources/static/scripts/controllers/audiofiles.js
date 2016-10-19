'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('AudioFilesCtrl', function($scope, $http, $state) {

    $scope.audioList=[];
      $scope.errorMsg=undefined;

    $scope.init = function(){
      $http({
        method: 'GET',
        url: '/audio/'
      }).then(function (response) {
          $scope.audioList = response.data;
      }, function errorCallback(response) {

      });
    };

      $scope.gotoUpload = function(){
          $state.go("upload");
      };


      $scope.play = function (id){
        $http({
          method: 'GET',
          url: '/audio/play/' + id
        }).then(function (response) {
          $scope.audioList = response.data;
        }, function errorCallback(response) {

        });
      };

      $scope.delete = function(id){
        bootbox.confirm("M&ouml;chten Sie die Datei wirklich l&ouml;schen?", function(result) {
          if (result){
            $http({
              method: 'DELETE',
              url: '/audio/' + id
            }).then(function (response) {
              $scope.init();
            }, function errorCallback(response) {

            });
          }
        });
      };


      $scope.toggle =function (id, state){
        var url = "/audio/activate/";
        if (!state){
          url = "/audio/deactivate/"
        }
        $http({
          method: 'PUT',
          url: url + id
        }).then(function (response) {
          $scope.init();
        }, function errorCallback(response) {

        });

      }

      $scope.init();

  });
