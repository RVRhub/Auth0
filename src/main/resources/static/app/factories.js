// Фабрика (интерцептор/перехватчик) для доабвления токена во все запросы
app.factory('authInterceptor', function($location, $q, $window,$localStorage) {
    return {
        request: function(config) {
            config.headers = config.headers || {};
            // Если такого нет
            if(typeof $localStorage.token != 'undefined')
                config.headers.Authorization = 'Bearer '+$localStorage.token;
            return config;
        }
    };
});