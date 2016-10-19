'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('UploadCtrl', function($scope,$rootScope, $state, $http) {

        $scope.errorMsg = undefined;
      $scope.upload = function() {

        var fd = new FormData();
        //Take the first selected file
        var file = document.getElementById("audiofile").files[0];

        fd.append("file", file);
        fd.append("name", $scope.name);

        $http.post("/audio/", fd, {
          transformRequest: angular.identity,
          headers: {'Content-Type': undefined}
        })
            .success(function(){
              $state.go("audiofiles");
            })
            .error(function(response){
                console.log(response);
                $scope.errorMsg = response.data;
            });


      };

  })

