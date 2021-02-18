angular.module('SysDataSourceDefService', ['baseServices'])
.service('SysDataSourceDef', ['$rootScope','BaseService', '$http',function($rootScope,BaseService,$http) {
    var service = {    		
    		detail : function(id,callback){
    			//获取SysDataSource的详情
    			BaseService.post(__ctx +'/oa/system/sysDataSourceDef/getById.do',{id:id},function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		save:function(SysDataSourceDef,callback){
    			$http.post(__ctx +'/oa/system/sysDataSourceDef/save.do',SysDataSourceDef).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		checkConnection:function(SysDataSourceDef,callback){
    			$http.post(__ctx +'/oa/system/sysDataSourceDef/checkConnection.do',SysDataSourceDef).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//获取系统所有数据源
    		getAllSysDS:function(callback){
    			$http.post(__ctx +'/oa/system/sysDataSourceDef/getAll.do').success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		}
        }
    return service;
}]);