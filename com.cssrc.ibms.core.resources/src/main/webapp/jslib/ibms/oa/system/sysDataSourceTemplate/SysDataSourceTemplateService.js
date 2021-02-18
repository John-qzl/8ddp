angular.module('SysDataSourceTemplateService', ['baseServices'])
.service('SysDataSourceTemplate', ['$rootScope','BaseService', '$http',function($rootScope,BaseService,$http) {
    var service = {    		
    		detail : function(id,callback){
    			//获取SysDataSourceTemplate的详情
    			BaseService.post(__ctx +'/oa/system/sysDataSourceTemplate/getById.do',{id:id},function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		save:function(SysDataSourceTemplate,callback){
    			$http.post(__ctx +'/oa/system/sysDataSourceTemplate/save.do',SysDataSourceTemplate).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//获取字段信息
    		getAttr:function(classPath,callback){
    			BaseService.post(__ctx +'/oa/system/sysDataSourceTemplate/getSetterFields.do',{classPath:classPath},function(data){
    				if(callback){
        				callback(data);
        			 }
    			});
    		},
    		//获取字段信息
    		getAll:function(callback){
    			BaseService.post(__ctx +'/oa/system/sysDataSourceTemplate/getAll.do',{},function(data){
    				if(callback){
        				callback(data);
        			 }
    			});
    		}
        }
    return service;
}]);