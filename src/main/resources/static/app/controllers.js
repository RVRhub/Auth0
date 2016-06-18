// Контроллер для модуля
app.controller('HomeController', function($scope,$location,auth,$localStorage,apiService){
    // Тестовое сообщение, ответ от сервра
    $scope.message = null;
    // Профиль пользователя для показа логин/логаут данных
    $scope.profile = $localStorage.profile;

    /**
     * Экшен для входа в систему, показывает интерфейс Auth0 Lock
     */
    $scope.signin = function() {
        auth.signin({
            authParams: {scope: 'openid profile'} // Скоуп запроса, который будет добавлятся в токен
        }, function(profile, idToken, accessToken, state, refreshToken) {
            $localStorage.token = idToken; // Сохраняем токен
            $localStorage.profile = profile; // Сохраняем профиль
            $scope.profile = profile; // Ставим локальной переменной профиль для UI
            $location.path('/'); // Переводим на индекс
        }, function(err) {
            console.log("Error", err);
        });
    }

    /**
     * Выход из системы, очищаем все данные
     * */
    $scope.signout = function(){
        delete $localStorage.token;
        delete $localStorage.profile;
        $scope.profile = null;
        $location.path('/');
    }

    /**
     * Публичная точка REST
     * */
    $scope.publicEndpoint = function(){
        apiService.getPublicEndpoint()
            .success(function(response){
                setMessage(response);
            })
            .error(function(response){
                setMessage(response);
            });
    }

    /**
     * Админская точка REST
     * */
    $scope.adminEndpoint = function(){
        apiService.getAdminEndpoint()
            .success(function(response){
                setMessage(response);
            })
            .error(function(response){
                setMessage(response);
            });
    }

    /**
     * Пользовательская точка REST
     * */
    $scope.userEndpoint = function(){
        apiService.getUserEndpoint()
            .success(function(response){
                setMessage(response);
            })
            .error(function(response){
                setMessage(response);
            });
    }

    /**
     * Получение ответа от сервера
     * */
    function setMessage(response){
        $scope.message = JSON.stringify(response,null,2);
        //console.log(JSON.stringify(response));
    }

    /**
     * Очистка поля сообщений
     * */
    $scope.clearMessage = function(){
        $scope.message = null;
    }
});