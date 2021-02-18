
function selectScript(){
	var objConditionCode=$("#txtScriptData")[0];
	ScriptDialog({callback:function(script){
		jQuery.insertText(objConditionCode,script);
	}});
}

function addUserSet(){
	var html=$("#selectUserRow").html();
	$("#tbodyUserSet").last().append(html);
}

function delRows(rowId){
	var cks=$("#table_"+rowId + " input[name=nodeUserCk]:checked");
	if(cks.length==0){
		$.ligerDialog.warn("请选择删除的记录!","提示信息");
		return ;
	}
	for(var i=cks.length-1;i>=0;i--){
		var nodeUserId=$(cks[i]).siblings("input[name='nodeUserId']").val();
		if(nodeUserId!='' && nodeUserId!=undefined){
			var url=__ctx +  '/oa/flow/definition/delBpmNodeUser.do';
			$.post(url,{nodeUserId:nodeUserId});
		}
		$(cks[i]).parent().parent().remove();
	}
}

/**
 * 对不同类型的运算类型做不同的设置。
 */
function selectCmp(obj){
	var actDefId=$("#actDefId").val();
	var defId=$("#defId").val();
	var nodeId=$("#nodeId").val();
	var parentActDefId=$('#parentActDefId').val();
	var trObj=$(obj).closest("tr");
	var cmpIds=$("textarea[name='cmpIds']",trObj);
	var cmpNames=$("textarea[name='cmpNames']",trObj);
	var assignType=$('[name="assignType"]',trObj).val();
	var nodeUserId=$('[name="nodeUserId"]',trObj).val();
	 
		
	var callback=function(ids,names){
		cmpIds.val(ids);
		cmpNames.val(names);
	};
	
	switch(assignType){
		//用户选择
		case "users":
			UserDialog({
				selectUserIds:cmpIds.val(),
	    	    selectUserNames:cmpNames.val(),
				callback:callback			
			});
			break;
		//角色选择
		case "role":
			RoleDialog({
				ids:cmpIds.val(),
	    	    names:cmpNames.val(),
				callback:callback
			});
			break;
		//职务
		case "job":
			JobDialog({
				ids:cmpIds.val(),
	    	    names:cmpNames.val(),
				callback:callback
			});
			break;
		//组织
		case "org":
		case "orgCharge":
			OrgDialog({
				ids:cmpIds.val(),
		    	names:cmpNames.val(),
				callback:callback
			});
			break;
		//岗位
		case "pos":
			PosDialog({
				ids:cmpIds.val(),
	    	    names:cmpNames.val(),
				callback:callback
			});
			break;
		//与已执行节点相同执行人  与其他节点相同执行人
		case "sameNode":
			showOtherNodeDlg({callback:callback,actDefId:actDefId, nodeId:nodeId});
			break;
		//脚本对话框
		//case "script":
			//showScript(cmpNames);
			//break;
		//脚本对话框
		case "script":
			//showScript(cmpNames);
			showScriptDialog({callback:callback,cmpNames:cmpNames,defId:defId,parentActDefId:parentActDefId});
			break;
		//部门的上级类型部门的负责人
		case "orgTypeUserLeader":
			typeSetDialog({callback:callback,nodeUserId:nodeUserId,cmpIds:cmpIds.val(),cmpNames:cmpNames.val()});
			break;
		//发起人或上一节点执行人部门的分管领导
		case "orgLeader":
			orgLeaderSetDialog({callback:callback,nodeUserId:nodeUserId,cmpIds:cmpIds.val(),cmpNames:cmpNames.val()});
			break;
		//上下级
		case "upLow":
			UplowDialog({callback:callback});
			break;
		//用户属性
		case "userAttr":
			UserParamDialog({callback:callback,nodeUserId:nodeUserId,cmpIds:cmpIds.val(),cmpNames:cmpNames.val()});
			break;
		//组织属性
		case "orgAttr":
			OrgParamDialog({callback:callback,nodeUserId:nodeUserId,cmpIds:cmpIds.val(),cmpNames:cmpNames.val()});
			break;
		//表单变量
		case "formVar":
			showFormVar({callback:callback,nodeUserId:nodeUserId,defId:defId,cmpIds:cmpIds.val(),parentActDefId:parentActDefId});
			break;
		//发起人或上一任务执行人
		case "startOrPrev":
			startOrPrevDialog({callback:callback,nodeUserId:nodeUserId,defId:defId,cmpIds:cmpIds.val()});
			break;
		//发起人或上一任务执行人的组织
		case "startOrPrevWithOrg":
			startOrPrevWithOrgDialog({callback:callback,nodeUserId:nodeUserId,defId:defId,cmpIds:cmpIds.val()});
			break;
		//与已执行节点执行人相同部门
		case "sameNodeDepartment":
			showOtherNodeDlg({callback:callback,actDefId:actDefId, nodeId:nodeId});
			break;
		//指定节点的执行人的负责人
		case "sameNodeUserLeader":
			showOtherNodeDlg({callback:callback,actDefId:actDefId, nodeId:nodeId});
			break;
	}
}

//处理是否显示  抽取用户 的选项
function handlerExtractUser(obj){
	var extractDiv = $(obj).parents("tr").find("div.extractDiv"),
		selectOpt = $(obj).find("option:selected"),
		hasExtractSetting = selectOpt.attr("hasExtractSetting");

	extractDiv.hide();
	if(hasExtractSetting!="true")return;
	extractDiv.show();
};

/**
 * 修改人员计算类型执行的脚本。
 */
function assignTypeChange(obj){
	handlerExtractUser(obj);
	var trObj=$(obj).closest("tr");
	var cmpIds=$("textarea[name='cmpIds']",trObj);
	var cmpdNames=$("textarea[name='cmpNames']",trObj);
	var selButtons=$(".button",trObj); 
	var assignUserType=obj.value;
	if(obj.value=="startUser"||obj.value=="sameDepart"||obj.value=="sameJob"||obj.value=="samePosition"||obj.value=="directLeader" || obj.value=="prevUserOrgLeader" 
	|| obj.value=="startUserLeader" || obj.value=="prevUserLeader" || obj.value=="approve" ){
		selButtons.hide();
		cmpdNames.hide();
	}else{
		selButtons.show();
		cmpdNames.show();
	}
	cmpIds.val('');
	cmpdNames.val('');
}

var win;
function showScript(obj){
	$("#txtScriptData").val(obj.val());
	
	var divObj=$("#divScriptData");
	win= $.ligerDialog.open({ target:divObj , height: 350,width:500, modal :true,
		buttons: [ { text: '确定', onclick: function (item, dialog) { 
				obj.val($("#txtScriptData").val());
				dialog.hide();
			} 
		}, 
		{ text: '取消', onclick: function (item, dialog) { dialog.hide(); } } ] }); 
	
}

function changeVar(obj){
	var val=$(obj).val();
	var objScript=$("#txtScriptData")[0];
	jQuery.insertText(objScript,val);
}

function showScriptDialog(conf){
	url=__ctx + "/oa/flow/definition/showScript.do?defId="+conf.defId;
	if(conf.parentActDefId){
		url += "&parentActDefId="+conf.parentActDefId;
	}
	url=url.getNewUrl();
	
	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '脚本选择',
		url: url, 
		//自定义参数
		cmpNames:conf.cmpNames.val(),
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.cmpNames.val(rtn.returnVal);
				}	
			}
		}
	});
}

//显示其他节点的对话框
function showOtherNodeDlg(conf){
	url=__ctx + "/oa/flow/definition/taskNodes.do?actDefId="+conf.actDefId+"&nodeId="+conf.nodeId;
	url=url.getNewUrl();
	
	var that = this;
	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '执行人',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(this,rtn.nodeId,rtn.nodeName);
				}	
			}
		}
	});
}

function showCompesite(conf){
	url=__ctx + "/oa/flow/nodeUser/relativeCal.do?nodeUserId=" +conf.nodeUserId +"&defId=" +conf.defId;
	url=url.getNewUrl();
	
	$.ligerDialog.open({
		height:600,
		width: 650,
		title : '',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					var jsonStr=JSON2.stringify(rtn.json);
					var desc=rtn.desc;
					conf.callback.call(this,jsonStr,desc);
				}	
			}
		}
	});
	
	
}

function  showFormVar(conf){
	url=__ctx + "/oa/flow/nodeUser/formVar.do?nodeUserId=" +conf.nodeUserId +"&defId=" +conf.defId +"&cmpIds=" +conf.cmpIds;
	if(conf.parentActDefId){
		url += "&parentActDefId="+conf.parentActDefId;
	}
	url=url.getNewUrl();
	
	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '人员表单变量',
		url: url, 
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(this,rtn.json,rtn.description);
				}	
			}
		}
	});
}
//发起人或上一任务执行人
function  startOrPrevDialog(conf){
	url=__ctx + "/oa/flow/nodeUser/startOrPrevDialog.do?nodeUserId=" +conf.nodeUserId +"&defId=" +conf.defId +"&cmpIds=" +conf.cmpIds;
	url=url.getNewUrl();

	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '发起人或上个任务执行人',
		url: url, 
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(this,rtn.json,rtn.description);
				}	
			}
		}
	});
}


//发起人或上一任务执行人
function  orgLeaderSetDialog(conf){
	url=__ctx + "/oa/flow/nodeUser/orgLeaderSetDialog.do?nodeUserId=" +conf.nodeUserId +"&defId=" +conf.defId +"&cmpIds=" +conf.cmpIds;
	url=url.getNewUrl();
	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '发起人或上一任务执行人的所属组织分管领导',
		url: url, 
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(this,rtn.json,rtn.description);
				}	
			}
		}
	});
}

//发起人或上一任务执行人的组织
function  startOrPrevWithOrgDialog(conf){
	url=__ctx + "/oa/flow/nodeUser/startOrPrevWithOrgDialog.do?nodeUserId=" +conf.nodeUserId +"&defId=" +conf.defId +"&cmpIds=" +conf.cmpIds;
	url=url.getNewUrl();
	
	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '发起人或上个任务执行人的组织',
		url: url, 
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(this,rtn.json,rtn.description);
				}	
			}
		}
	});
}

//与已执行节点执行人相同部门
function  sameNodeDepartmentDialog(conf){
	url=__ctx + "/oa/flow/nodeUser/sameNodeDepartmentDialog.do?nodeUserId=" +conf.nodeUserId +"&defId=" +conf.defId +"&cmpIds=" +conf.cmpIds;
	url=url.getNewUrl();

	$.ligerDialog.open({
		height:500,
		width: 650,
		title : '新窗口',
		url: url, 
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					conf.callback.call(this,rtn.json,rtn.description);
				}	
			}
		}
	});
}

$(function(){
	$("a.del").unbind("click");
});
