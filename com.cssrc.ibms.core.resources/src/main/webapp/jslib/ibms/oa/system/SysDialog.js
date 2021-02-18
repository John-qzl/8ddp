
/**
 * scope 权限参数 这个属性定制的时候可以添加 例如 var cof={scope:xxxxx，其他参数方法} xxxDialog(cof)
 *
 */

/**
 * 组织选择器
 * @param conf
 * 
 * conf 参数
 * 
 * orgId：组织ID
 * orgName:组织名称
 * @returns
 */
function OrgDialog(conf)
{
	var dialogWidth=900;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	//if(conf.scope) scope = (conf.scope).replace(/\'/g, '"');
	var url=__ctx + '/oa/system/sysOrg/dialog.do?isSingle=' + conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	//存放scope信息  在 WEB-INF/view/oa/system/sysOrgDialog.jsp使用
	arrys.push("scope="+scope);
	//存放Users信息
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	
	
	url += "&url=oa/system/sysFilePDFPreview.jsp";
	
	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '组织选择器',
        url: url, 
        isResize: conf.isResize,
        //自定义参数
        arrys: arrys,
        scope : scope,
        //回调函数
        sucCall:function(rtn){
        	if(conf.callback){
        		conf.callback.call(that,rtn.orgId,rtn.orgName,rtn.orgJson);
            	//conf.callback.call(that,rtn.orgId,rtn.orgName);
        	}
        }
    });
}


/**
 * 用户选择器 .
 * UserDialog({callback:function(userIds,fullnames,emails,mobiles){},selectUsers:[{id:'',name:''}]})
 */
/**
 * 用户选择器 .
 * UserDialog({scope:"",callback:function(userIds,fullnames,emails,mobiles){},selectUsers:[{id:'',name:''}]})
 * 
 * 组织级别范围，为json字符串{type:"system,script",value:""}。
 * 			这个value还可以扩展
 * scope说明：
 * {
 * 	type:system,
 * 	value: 
 * 		all 全部组织,
 * 		self: 当前登录组织
 * 		grade: 当前组织往上找找到级别1
 * 		company：当前组织往上找找到级别2
 * 		department：当前组织往上找找到级别3
 * 		group：当前组织往上找找到级别4
 * 		other:当前组织往上找找到级别5
 * 		up: 当前组织上级组织
 * 	type:script
 * 	value:为脚本，返回一个组织ID
 * 		
 * }
 */
function UserDialog(conf){
	var dialogWidth=900;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	var dataId="";
	var dataType="";
	if(conf.dataId!=""&&conf.dataId!=null){
		dataId = conf.dataId;
	}
	if(conf.dataType!=""&&conf.dataType!=null){
		dataType = conf.dataType;
	}
	var scope="{type:\"system\",value:\"all\"}";
	var relvalue = "";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	//若用户配置了级联关系则获取相关属性
	if(conf.relvalue!=""&&conf.relvalue!=null){
		relvalue = conf.relvalue;
	}
	
	var url=__ctx + "/oa/system/sysUser/dialog.do?isSingle=" + conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var selectUsers="";
	if(  conf.selectUserIds && conf.selectUserNames){
		selectUsers={
				selectUserIds:conf.selectUserIds ,
				selectUserNames:conf.selectUserNames
		}
	}else if(conf.selectUsers){
		var arr = conf.selectUsers ;
		var ids = '';
		var names = '';
		for ( var i = 0; i < arr.length; i++) {
			if(i!=0){
				ids+=',';
				names+=',';
			}
			ids+=arr[i].id;
			names+=arr[i].name;
		}
		selectUsers={
				selectUserIds:ids ,
				selectUserNames:names
		}
	}	
	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '用户选择器',
        url: url, 
        dataId:dataId,
        dataType:dataType,
        scope : scope,
        relvalue : relvalue,
        isResize: conf.isResize,
        //自定义参数
        selectUsers: selectUsers,
        sucCall:function(rtn){
        	if(conf.callback){
        		var userIds=rtn.userIds;
        		var fullnames=rtn.fullnames;
        		var emails=rtn.emails;
        		var mobiles=rtn.mobiles;
        		conf.callback.call(that,userIds,fullnames,emails,mobiles,rtn);
        	}
        }
    });
 }

/**
 * 这个选择器只用户在流程那里选择人员或部门。
 * 调用方法：
 * 
 * 
 * FlowUserDialog({selectUsers:[{type:'',id:'',name:''}],callback:function(aryType,aryId,aryName){}});
 * selectUsers，表示之前选择的人员，使用json数组来表示。
 * 数据格式:{type:'',id:'',name:''}
 * type:选择的类型。可能的值 user,org,role,pos .
 * id:选择的ID
 * name:显示的名称。
 * 
 * JSON数组：
 * 这个回调函数包括三个参数 ，这三个参数都为数组。
 * objType：返回的类型,可能的值(user,org,role,pos) 。
 * objIds:对象的Id。
 * objNames：对象的名称。
 */
function FlowUserDialog(conf){
	var dialogWidth=900;
	var dialogHeight=500;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var url=__ctx + "/oa/system/sysUser/flowDialog.do";
	url=url.getNewUrl();
	//重新选择的时候，展现上次数据,必须传入
	var selectUsers="";
	if(  conf.selectUsers!=undefined && conf.selectUsers!=null && conf.selectUsers!=""){
		selectUsers=conf.selectUsers;
	}
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '人员或部门',
        url: url, 
        scope:scope,
        isResize: conf.isResize,
        //自定义参数
        selectUsers: selectUsers,
        sucCall:function(rtn){
        	if(conf.callback){
        		conf.callback.call(that,rtn.objType,rtn.objIds,rtn.objNames);
        	}
        }
    });
}


/**
 * 角色选择器 
 */
function RoleDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=500;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var url=__ctx + '/oa/system/sysRole/dialog.do?isSingle=' + conf.isSingle+'&isGrade='+ conf.isGrade;;
	url=url.getNewUrl();
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	
	
	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '角色选择器 ',
        url: url,
        scope : scope,
        isResize: conf.isResize,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	if(conf.callback){
            	conf.callback.call(that,rtn.roleId,rtn.roleName);
        	}
        }
    });
}


/**
 * 岗位选择器
 * conf.callback
 * posId：岗位ID
 * posName:岗位名称
 * @returns
 */
function PosDialog(conf)
{	
	var dialogWidth=800;
	var dialogHeight=600;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var url=__ctx + '/oa/system/position/dialog.do?isSingle=' + conf.isSingle;
	url=url.getNewUrl();
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '岗位选择器',
		url: url, 
		scope : scope,
		isResize: conf.isResize,
		//自定义参数
		arrys: arrys,
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(that,rtn.posId,rtn.posName);
				}
			}
		}
	});
}



/**
 * 职务选择器
 * conf.callback
 * jobId：职务ID
 * jobName:职务名称
 * @returns
 */
function JobDialog(conf)
{	
	var dialogWidth=800;
	var dialogHeight=600;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var url=__ctx + '/oa/system/job/selector.do?isSingle=' + conf.isSingle;
	url=url.getNewUrl();
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '职务选择器',
		url: url, 
		scope : scope,
		isResize: conf.isResize,
		//自定义参数
		arrys: arrys,
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(that,rtn.jobId,rtn.jobName);
				}
			}
		}
	});
}


/**
 * 用户参数选择器 
 * @param conf
 * dialogWidth：对话框高度 650
 * dialogHeight：对话框高度 500
 * 
 */
function UserParamDialog(conf){
	var dialogWidth=650;
	var dialogHeight=500;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var args={cmpIds:conf.cmpIds,cmpNames:conf.cmpNames};
	var url=__ctx + '/oa/system/sysUserParam/dialog.do?nodeUserId='+conf.nodeUserId;
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '用户参数选择器',
		url: url, 
		isResize: conf.isResize,
		//自定义参数
		scope : scope,
		args: args,
		sucCall:function(rtn){
			if(conf.callback){
				conf.callback.call(that,rtn.paramValue1,rtn.paramValue2);
			}
		}
	});
}


/**
 * 组织参数选择器 
 */
function OrgParamDialog(conf){
	var dialogWidth=650;
	var dialogHeight=500;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var args={cmpIds:conf.cmpIds,cmpNames:conf.cmpNames};
	var url=__ctx + '/oa/system/sysOrgParam/dialog.do?nodeUserId='+conf.nodeUserId;
	url=url.getNewUrl();
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '组织参数',
		url: url, 
		isResize: conf.isResize,
		scope : scope,
		//自定义参数
		args: args,
		sucCall:function(rtn){
			if(conf.callback){
				 conf.callback.call(that,rtn.paramValue1,rtn.paramValue2);
			}
		}
	});
}


/**
 * 上下级选择器 
 */

function UplowDialog(conf){
	var dialogWidth=650;
	var dialogHeight=500;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	var url=__ctx + '/oa/system/nodeUserUplow/dialog.do';
	url=url.getNewUrl();
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '上下级选择器',
		url: url, 
		isResize: conf.isResize,
		scope : scope,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				conf.callback.call(that,rtn.json,rtn.show);
			}
		}
	});
}

/**
 *上级部门类型选择器
 */

function typeSetDialog(conf){
	var dialogWidth=500;
	var dialogHeight=360;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	var args={cmpIds:conf.cmpIds,cmpNames:conf.cmpNames};
	var url=__ctx + '/oa/flow/definition/typeSetDialog.do';
	url=url.getNewUrl();
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '上级部门类型选择器',
		url: url, 
		isResize: conf.isResize,
		scope : scope,
		//自定义参数
		args: args,
		sucCall:function(rtn){
			if(conf.callback){
				conf.callback.call(that,rtn.json,rtn.show);
			}
		}
	});
}

/**
 * 打开选择引用流程实例的对话框
 * @param conf
 */
function ActInstDialog(conf){
	var dialogWidth=900;
	var dialogHeight=700;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);

	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var url=__ctx + '/oa/flow/referDefinition/actInstDialog.do?defId='+conf.defId+'&isSingle=' + conf.isSingle;
	url=url.getNewUrl();
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '打开选择引用流程实例的对话框',
		url: url,
		scope : scope,
		isResize: conf.isResize,
		//自定义参数
		arguments: conf.arguments,
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					 conf.callback.call(that,rtn);
					}
			}
		}
	});
};

/**
 * 级联设置
 */
function GangedSetCascade(data,callback){		
	var dialogWidth=550;
	var dialogHeight=400;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1});

	var url=__ctx + "/oa/flow/gangedSetCascade.do";
	url=url.getNewUrl();
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '级联设置',
		url: url,
		scope : scope,
		isResize: conf.isResize,
		//自定义参数
		data: data,
		sucCall:function(rtn){
			if(callback){
				if(rtn!=undefined){
					callback.call(that,rtn);
				}
			}
		}
	});
};	
	
	/**
	 * 分级用户选择器
	 * GradeUserDialog({callback:function(userIds,fullnames,emails,mobiles){},selectUsers:[{id:'',name:''}]})
	 */
	function GradeUserDialog(conf){
		
		var dialogWidth=1000;
		var dialogHeight=500;
		
		conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);

		if(!conf.isSingle)conf.isSingle=false;
		
		if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
		
		var url=__ctx + "/oa/system/sysUser/gradeDialog.do?isSingle=" + conf.isSingle;
		url=url.getNewUrl();
		
		var scope="{type:\"system\",value:\"all\"}";
		//若用户自己定义了scope便不用默认的
		if(conf.scopeValue!=""&&conf.scopeValue!=null){
			scope = conf.scopeValue;
		}
		
		//重新选择的时候，展现上次数据
		var selectUsers="";
		if(  conf.selectUserIds && conf.selectUserNames){
			selectUsers={
					selectUserIds:conf.selectUserIds ,
					selectUserNames:conf.selectUserNames
			}
		}else if(conf.selectUsers){
			var arr = conf.selectUsers ;
			var ids = '';
			var names = '';
			for ( var i = 0; i < arr.length; i++) {
				if(i!=0){
					ids+=',';
					names+=',';
				}
				ids+=arr[i].id;
				names+=arr[i].name;
			}
			selectUsers={
					selectUserIds:ids ,
					selectUserNames:names
			}
		}	
		
		var that =this;
		DialogUtil.open({
	        height:conf.dialogHeight,
	        width: conf.dialogWidth,
	        title : '用户选择器',
	        url: url, 
	        isResize: conf.isResize,
	        scope : scope,
	        //自定义参数
	        selectUsers: selectUsers,
	        sucCall:function(rtn){
	        	if(conf.callback){
	        		var userIds=rtn.userIds;
		    		var fullnames=rtn.fullnames;
		    		var emails=rtn.emails;
		    		var mobiles=rtn.mobiles;
		    		conf.callback.call(that,userIds,fullnames,emails,mobiles);
	        	}
	        	
	        }
	    });	
}

/**
 * 报表添加参数窗口。
 * 调用方法：
 * ReportAddParamDialog({paramid:'',callback:function(obj){}});
 * 这个回调函数参数为Object。object属性
 * obj.types=''
 * obj.objIds=''
 * obj.objNames=''
 * obj.value_=''
 * obj.paramtype=''
 */	
function ReportAddParamDialog(conf){
	var dialogWidth=900;
	var dialogHeight=700;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);

	if(!conf.isSingle)conf.isSingle=false;
	
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
	
	var url=__ctx + '/oa/system/reportParam/edit.do?isSingle='+ conf.isSingle;
	if(conf.paramid){
		url=url+'&paramid='+conf.paramid;
	}
	if(conf.reportid){
		url=url+'&reportid='+conf.reportid;
	}
	if(conf.edit){
		url=url+'&edit='+conf.edit;
	}
	url=url.getNewUrl();
	
	var scope="{type:\"system\",value:\"all\"}";
	//若用户自己定义了scope便不用默认的
	if(conf.scopeValue!=""&&conf.scopeValue!=null){
		scope = conf.scopeValue;
	}
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '编辑 报表参数',
		url: url, 
		isResize: conf.isResize,
		scope : scope,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(that,rtn);
				}
			}
		}
	});
}

/**
 * 调用流程启动窗口
 * by YangBo
 * @param conf
 */
function TaskStartFlowForm(conf){
	conf = conf || {};
	var	url= conf.url;
		width= conf.width||1000,
		height=conf.height||800;
		
	if(conf.isResize==null)conf.isResize=true;/**默认是可移动窗口 yangbo(伴随DialogUtil的判断更改)**/
		
	DialogUtil.open({
        height:height,
        width: width,
        url: url, 
        isResize: conf.isResize,
        sucCall:function(rtn){
			if(!(rtn == undefined || rtn == null || rtn == '')){
				location.href=location.href.getNewUrl();
			}
		}
    });
}




