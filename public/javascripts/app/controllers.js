'use strict';

var HomeController = function($scope) {

	    var modules = {
		    "apps":[
		    {
		        "name":"REST", 
		        "link":"rest", 
		        "cls":""
		    },

		    {
		        "name":"SOAP", 
		        "link":"soap", 
		        "cls":""
		    },

		    {
		        "name":"SMTP", 
		        "link":"smtp", 
		        "cls":""
		    }
		    ],
		    "srvs":[
		    {
		        "name":"GIT", 
		        "link":"git", 
		        "cls":""
		    },

		    {
		        "name":"Subversion", 
		        "link":"svn", 
		        "cls":""
		    },

		    {
		        "name":"JCR", 
		        "link":"jcr", 
		        "cls":""
		    },

		    {
		        "name":"Email", 
		        "link":"email", 
		        "cls":""
		    }
		    ]
		};

		$scope.apps = modules.apps;
		$scope.srvs = modules.srvs;


	    $scope.currentApp = null;

	    $scope.openApp = function(app){

	        if($scope.currentApp){
	            $scope.currentApp.cls='';
	        }

	        app.cls='active';
	        $scope.currentApp = app;
	    };

	    
};

var RestController = function($scope, $http) {

	$scope.requests = [];
	$scope.services = [];

	$http({method: 'GET', url: '/rest/list'}).
	  success(function(data, status, headers, config) {
	  	$scope.services = data.result;
	  }).
	  error(function(data, status, headers, config) {
	  	console.debug(status);
	  });


    var source = new EventSource('/live');

	source.onmessage = function(message) {
	  var data = JSON.parse(message.data);
	  console.log(data);
	};

	source.addEventListener('request', function(message) {

		var data = JSON.parse(message.data);

	  	$scope.requests.push(data);
	  	
	  	$scope.$digest();

	}, false);

	source.addEventListener('response', function(message) {

  		var data = JSON.parse(message.data);

	  	var req = _.find($scope.requests, function(req){
	  		return data.requestId == req.requestId;
	  	});

	  	req.response = data;
	  	
	  	$scope.$digest();

	}, false);
	
};

var SoapController = function($scope, greeter, user) {
	
};