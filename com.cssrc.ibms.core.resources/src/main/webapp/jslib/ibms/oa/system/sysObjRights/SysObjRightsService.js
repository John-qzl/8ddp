angular.module('SysObjRightsService', ['baseServices'])
.service('SysObjRights', ['$rootScope','BaseService', '$http',function($rootScope,BaseService,$http) {
    var service = {    		
    		//获取SysObjRights的详情
    		detail : function(json,callback){
    			BaseService.post(__ctx +'/oa/system/sysObjRights/getObject.do',json,function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		save:function(json,callback){
     			BaseService.post(__ctx +'/oa/system/sysObjRights/save.do',json,function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//根据beanId获取类型列表
    		getRightType:function(beanId,callback){
    			BaseService.post(__ctx +'/oa/system/sysObjRights/getRightType.do',{beanId:beanId},function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//
    		//根据beanId获取类型列表
    		getByObjTypeAndObjectId:function(objType,objectId,callback){
    			BaseService.post(__ctx +'/oa/system/sysObjRights/getObject.do',{objType:objType,objectId:objectId},function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		}
        }
    return service;
}]);