var app = angular.module('auth', ['auth0','ngStorage', 'angular-jwt'])
    .config(function(authProvider) {
        authProvider.init({
            domain: 'xaerozero.eu.auth0.com',
            clientID: 'WIXPBPHwr9BYcOvERocaB6SQWznhJBdm',
            loginUrl: '/login'
        });
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('authInterceptor');
    })
    .run(function($rootScope, auth, $localStorage, jwtHelper) {
        auth.hookEvents();

        $rootScope.$on('$locationChangeStart', function() {
            var token = $localStorage.token;
            if (token) {
                if (!jwtHelper.isTokenExpired(token)) {
                    if (!auth.isAuthenticated) {
                        auth.authenticate($localStorage.profile, token).then(function (profile) {
                            console.log("Logged in via refresh token and got profile");
                            console.log(profile);
                            // Successful login, now redirect to secured content.
                        }, function (err) { });;
                    }
                } else {
                    // Either show Login page or use the refresh token to get a new idToken
                }
            }
        });
    });;app.controller('HomeController',['$scope','$location','auth','$localStorage','apiService'],
    function($scope,$location,auth,$localStorage,apiService){
    $scope.message = null;

    $scope.signin = function() {
        auth.signin({
            authParams: {scope: 'openid profile'}
        }, function(profile, idToken, accessToken, state, refreshToken) {
            $localStorage.token = idToken;
            $localStorage.profile = profile;
            $location.path('/')
        }, function(err) {
            console.log("Error", err);
        });
    }

    $scope.signout = function(){
        delete $localStorage.token;
        delete $localStorage.profile;
    }

    $scope.publicEndpoint = function(){
        apiService.getPublicEndpoint()
            .success(function(response){
                setMessage(response);
            })
            .error(function(response){
                setMessage(response);
            });
    }

    $scope.adminEndpoint = function(){
        apiService.getAdminEndpoint()
            .success(function(response){
                setMessage(response);
            })
            .error(function(response){
                setMessage(response);
            });
    }

    $scope.userEndpoint = function(){
        apiService.getUserEndpoint()
            .success(function(response){
                setMessage(response);
            })
            .error(function(response){
                setMessage(response);
            });
    }

    function setMessage(response){
        $scope.message = JSON.stringify(response,null,2);
        console.log(JSON.stringify(response));
    }
    
    $scope.clearMessage = function(){
        $scope.message = null;
    }
});;;app.factory('authInterceptor', function($location, $q, $window,$localStorage) {
    return {
        request: function(config) {
            config.headers = config.headers || {};
            if(typeof $localStorage.token != 'undefined')
                config.headers.Authorization = 'Bearer '+$localStorage.token;
            return config;
        }
    };
});;;app.service('apiService',['$http',function($http){
    this.getPublicEndpoint = function() {
        return $http.get('/api/public');
    };

    this.getAdminEndpoint = function() {
        return $http.get('/api/admin');
    };

    this.getUserEndpoint = function() {
        return $http.get('/api/user');
    };
}]);