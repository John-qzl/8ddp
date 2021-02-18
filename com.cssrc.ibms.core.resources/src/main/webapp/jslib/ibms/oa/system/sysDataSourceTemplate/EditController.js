var app = angular.module('app',['baseServices','SysDataSourceTemplateService','commonListService','arrayToolService']);
app.controller("EditController",['$scope','BaseService','SysDataSourceTemplate','CommonListService','ArrayToolService',function($scope,BaseService,SysDataSourceTemplate,CommonListService,ArrayToolService){
	$scope.CommonList=CommonListService;
	$scope.ArrayTool=ArrayToolService;
	
	$scope.prop={};//prop 等于一个数据库模板初始化
	
	//如果id不为空，获取初始化数据,利用发请求的方式
	if(id!=""){
		SysDataSourceTemplate.detail(id,function(data){
			$scope.prop=data;
			$scope.prop.settingJson=JSON.parse(data.settingJson);
		});
	}
	
	//根据classPath获取其setter字段
	$scope.getAttr=function(classPath){
		SysDataSourceTemplate.getAttr(classPath,function(data){
			$scope.prop.settingJson=data;
			//console.info(JSON.stringify(data));
	    });
	}
	
	$scope.save=function(prop){
		SysDataSourceTemplate.save(prop,function(data){
			// console.info(data);
			if(data.result==1){
				$.ligerDialog.confirm(data.message+",是否继续操作", "提示信息",
					function(rtn) {
						if (rtn) {
							window.location.reload();
						} else {
							window.location.href = "list.do";
						}
					});
			}
			else{
				$.ligerDialog.err(data.message, "提示信息");
			}
		});
	}
}]);