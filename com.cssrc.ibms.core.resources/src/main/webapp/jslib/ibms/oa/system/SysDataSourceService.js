angular.module('SysDataSourceService', ['baseServices'])
.service('SysDataSource', ['$rootScope','BaseService', '$http',function($rootScope,BaseService,$http) {
    var service = {    		
    		detail : function(id,callback){
    			//获取SysDataSource的详情
    			BaseService.post(__ctx +'/oa/system/sysDataSource/getById.do',{id:id},function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		save:function(SysDataSource,callback){
    			$http.post(__ctx +'/oa/system/sysDataSource/save.do',SysDataSource).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		checkConnection:function(SysDataSource,callback){
    			$http.post(__ctx +'/oa/system/sysDataSource/checkConnection.do',SysDataSource).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//获取系统所有数据源
    		getAllSysDS:function(callback){
    			$http.post(__ctx +'/oa/system/sysDataSource/getAll.do').success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		}
        }
    return service;
}]);