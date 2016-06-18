// Иициализация модуля-приложения, с зависимостями
var app = angular.module('auth', ['auth0','ngStorage', 'angular-jwt'])
// Конфигурируем Auth0 модуль
    .config(function(authProvider) {
        authProvider.init({
            domain: 'xaerozero.eu.auth0.com',
            clientID: 'WIXPBPHwr9BYcOvERocaB6SQWznhJBdm',
            loginUrl: '/login'
        });
    })
    // Добавляем интерсептор, чтоб посылать токен со всеми запросами если он есть
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('authInterceptor');
    })
    .run(function($rootScope, auth, $localStorage, jwtHelper,$location) {
        auth.hookEvents(); // подключаем все эвенты для Auth0 модуля

        // При старте смотрим есть ли токен в сторейдже, и авторизацируем пользователя
        $rootScope.$on('$locationChangeStart', function() {
            var token = $localStorage.token;
            if (token) {
                if (!jwtHelper.isTokenExpired(token)) {
                    if (!auth.isAuthenticated) {
                        auth.authenticate($localStorage.profile, token).then(function (profile) {
                            console.log("Logged in via refresh token and got profile");
                            $location.path('/');
                        }, function (err) { });;
                    }
                }
            }
        });
    });