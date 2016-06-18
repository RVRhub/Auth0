// HTTP сервисы для получения данных от сервера
app.service('apiService',['$http',function($http){
    // Публичный
    this.getPublicEndpoint = function() {
        return $http.get('/api/public');
    };
    // Админский
    this.getAdminEndpoint = function() {
        return $http.get('/api/admin');
    };
    // Пользовательский
    this.getUserEndpoint = function() {
        return $http.get('/api/user');
    };
}]);