angular.module('BpmNodeSqlService', ['baseServices'])
.service('NodeSql', ['$rootScope','BaseService', '$http',function($rootScope,BaseService,$http) {
    var service = {
    		//获取BpmNodeSql的详情
    		detail : function(json,callback){
    			BaseService.post(__ctx +'/oa/flow/NodeSql/getObject.do',json,function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//保存
    		save:function(NodeSql,callback){
    			$http.post(__ctx +'/oa/flow/NodeSql/save.do',NodeSql).success(function(data, status, headers, config){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//根据defId获取
    		getTable:function(json,callback){
    			BaseService.post(__ctx +'/oa/flow/NodeSql/getTable.do',json,function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
    		//根据actdefId跟nodeId获取节点数据，主要是为了判断是否是开始节点
    		getNodeType:function(json,callback){
    			BaseService.post(__ctx +'/oa/flow/NodeSql/getNodeType.do',json,function(data){
    				if(callback){
	    				callback(data);
	    			 }
    			});
    		},
        }
    return service;
}]);