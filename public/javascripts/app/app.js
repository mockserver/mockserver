angular.module('mockserver.service', []).
  value('greeter', {
    salutation: 'Hello',
    localize: function(localization) {
      this.salutation = localization.salutation;
    },
    greet: function(name) {
      return this.salutation + ' ' + name + '!';
    }
  }).
  value('user', {
    load: function(name) {
      this.name = name;
    }
  });
 
angular.module('mockserver.directive', []);
 
angular.module('mockserver.filter', []);
 
angular.module('mockserver', ['mockserver.service', 'mockserver.directive', 'mockserver.filter']).
 config(['$routeProvider', function($routeProvider) {

  $routeProvider.
  	when('/home', {templateUrl: 'assets/partials/home.html',   controller: HomeController}).
      when('/rest', {templateUrl: 'assets/partials/rest.html',   controller: RestController}).
      when('/soap', {templateUrl: 'assets/partials/soap.html', controller: SoapController}).
      otherwise({redirectTo: '/rest'});
 }]);
