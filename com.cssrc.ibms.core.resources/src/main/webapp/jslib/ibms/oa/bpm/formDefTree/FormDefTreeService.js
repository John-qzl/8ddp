angular.module('FormDefTreeService', ['baseServices'])
.service('FormDefTree', ['$rootScope','BaseService', '$http',function($rootScope,BaseService,$http) {
    var service = {
    		//获取FormDefTree的详情
    		getObject : function(json,callback){
    			BaseService.post(__ctx +'/oa/form/formDefTree/getObject.do',json,function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		save:function(FormDefTree,callback){
    			$http.post(__ctx +'/oa/form/formDefTree/save.do',FormDefTree).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//删除数据 pk多数据以,分割 eg:1,2,3...
    		delData:function(pk,alias,callback){
    			BaseService.post(__ctx +'/oa/form/formDefTree/delData_'+alias+'.do',{pk:pk},function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		}
        }
    return service;
}]);