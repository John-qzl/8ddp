
// 补签
function showAddSignWindow(){
	TaskAddSignWindow({taskId:taskId,callback:function(sign){
		loadTaskSign(taskId);
	}});
}

// 加载会签数据。
function loadTaskSign(taskId){
	var url=__ctx+'/oa/flow/task/toSign.do?taskId='+taskId;
	//'${ctx}/oa/flow/task/toSign.do?taskId=${task.id}';
	$(".taskOpinion").load(url);
}

// 显示流程图
function showTaskUserDlg(actInstId){
	TaskImageUserDialog({actInstId:actInstId});
}
// 显示沟通意见。
function showTaskCommunication(conf){
	var obj = {data:preGetDataHandler(CustomForm,CustomForm.getData())};
	isTaskEnd(function(){
		if(!conf) conf={};
		// 输入子页面
		var url=__ctx + "/oa/flow/task/toStartCommunicate.do?taskId="+taskId+"&btnname="+conf;
		url=url.getNewUrl();
		DialogUtil.open({
			height:350,
			width: 500,
			title : conf,
			url: url, 
			isResize: true,
			// 自定义参数
			obj: obj
		});
	})
}
// 显示流转意见
function showTaskTransTo(conf) {

	var obj = {data:preGetDataHandler(CustomForm,CustomForm.getData())};
	isTaskEnd(function(){
		if(!conf) conf={};
		// 输入子页面
		var url=__ctx + "/oa/flow/task/toTransTo.do?taskId="+taskId+"&curUserId=${curUserId}";
		url=url.getNewUrl();
		DialogUtil.open({
			height:400,
			width: 550,
			title : '显示流转意见',
			url: url, 
			isResize: true,
			// 自定义参数
			obj: obj,
			sucCall:function(rtn){
				if(rtn=="ok"){
					handJumpOrClose();
				}
			}
		});
	})
}

function isTaskEnd(callBack){
	var url=__ctx+"/oa/flow/task/isTaskExsit.do";
	var params={taskId:taskId};
	
	$.post(url,params,function(responseText){
		var obj=new com.ibms.form.ResultMessage(responseText);			
		if(obj.isSuccess()){
			callBack.apply(this);
		}
		else{
			$.ligerDialog.warn("这个任务已经完成或被终止了!",'提示');
		}
	});
}
function submitForm(action,button){
	var ignoreRequired=false;
	if(button=="#btnSave"){
		ignoreRequired=true;
	}
	
	if($(button).hasClass("disabled"))return;
	if(isEmptyForm){
		$.ligerDialog.error("还没有设置表单!",'提示信息');
		return;
	}
	
	$('#frmWorkFlow').attr("action",action);
	// 重新设置审批意见
	voteContent.setVoteContent();
	$('#frmWorkFlow').ajaxForm({success:showResponse }); // terry add
	if(isExtForm){
		$(button).addClass("disabled");
		$(".l-tab-loading").show();
		$('#frmWorkFlow').submit();
	}else{
		var rtn=CustomForm.validate({ignoreRequired:ignoreRequired});
		if(button=="#btnRejectToStart"){   //付勇 定制如果当前是驳回则不需要验证表单
			rtn=true;
		}
		if(!rtn){
			$.ligerDialog.warn("表单验证不成功,请检查表单是否正确填写!","提示信息");
			return;
		}
		
		var data=preGetDataHandler(CustomForm,CustomForm.getData());
		var rtn=CustomForm.validate();
		if(button=="#btnRejectToStart"){//付勇 定制如果当前是驳回则不需要验证表单
			rtn=true;
		}
		if(!rtn) return;
		
		// 子表必填检查(兼容新旧版本)
		rtn = SubtablePermission.subRequired();
		/*if(button=="#btnRejectToStart"){//付勇 定制如果当前是驳回则不需要验证表单
			rtn=true;
		}*/
		if(!rtn){	
			$.ligerDialog.warn("请填写子表表单数据！","提示信息");
			return;
		}
		// 关联表必填检查(兼容新旧版本)
		rtn = ReltablePermission.relRequired();
		if(!rtn){	
				$.ligerDialog.warn("关联表必须有一条记录，请检查！","提示信息");
				return;
		}
		// var rtn=CustomForm.validate();
		// if(!rtn) return;
		if(button!="#btnSave"){
			// 子表必填检查(兼容新旧版本)
			rtn = SubtablePermission.subRequired();
			if(!rtn){	
				$.ligerDialog.warn("请填写子表表单数据！","提示信息");
				return;
			}
			// 关联表必填检查(兼容新旧版本)
			rtn = ReltablePermission.relRequired();
			if(!rtn){	
				$.ligerDialog.warn("关联表必须有一条记录，请检查！","提示信息");
				return;
			}
		}
		
		var data=preGetDataHandler(CustomForm,CustomForm.getData());
		// WebSign控件提交。 有控件时才提交 xcx
		//
		if(WebSignPlugin.hasWebSignField){
			WebSignPlugin.submit();
		}	
		// IBMS sign 控件保存 签名
		if(ibmsSign.init){
			ibmsSign.saveSigns();
			var signIds=ibmsSign.getSignIds();
			$("#ibmsSignIds").val(signIds);
		}
		// IBMS sign 控件保存 end
		
		$(button).addClass("disabled");
		var uaName=navigator.userAgent.toLowerCase();				
		if(uaName.indexOf("firefox")>=0||uaName.indexOf("chrome")>=0){  // 异步处理
			// Office控件提交。 有可以提交的文档
			if(OfficePlugin.submitNum>0){
				OfficePlugin.submit();             // 火狐和谷歌 的文档提交包括了
													// 业务提交代码部分（完成
													// OfficePlugin.submit()后面的回调
													// 函数 有 业务提交代码），所以
													// 后面就不用加上业务提交代码
			}else{   // 没有可提交的文档时 直接做 业务提交代码
				data=preGetDataHandler(CustomForm,CustomForm.getData());
				// 设置表单数据
				$("#formData").val(data);
				// 流程提交
				$(".l-tab-loading").show();
				$('#frmWorkFlow').submit();
			}					
		}else{   // 同步处理
			// Office控件提交。 有可以提交的文档
			if(OfficePlugin.submitNum>0){
				OfficePlugin.submit();
				// 当提交问题 等于 提交数量的变量 时 表示所有文档 都提交了 然后做 业务相关的提交
				if(OfficePlugin.submitNum == OfficePlugin.submitNewNum){    
					// 获取自定义表单的数据
					data=preGetDataHandler(CustomForm,CustomForm.getData());
					// 设置表单数据
					$("#formData").val(data);	
					$(".l-tab-loading").show();
					$('#frmWorkFlow').submit();
					OfficePlugin.submitNewNum = 0; // 重置 提交数量的变量
				}else{
					$.ligerDialog.warn("提交失败,OFFICE控件没能正常使用，请重新安装 ！！！","提示");
				}
			}else{
				// 获取自定义表单的数据
				data=preGetDataHandler(CustomForm,CustomForm.getData());
				// 设置表单数据
				$("#formData").val(data);
				$(".l-tab-loading").show();
				$('#frmWorkFlow').submit();
			}		
		}
		
	}
}

function saveData(){
	var operatorObj=getByOperatorType();
	var button="#" +operatorObj.btnId;
	
	var rtn=beforeClick(operatorType);
	
	if( rtn==false){
		return;
	}
	if(isExtForm){// 提交第三方表单时检查该表单的参数
		var frm=$('#frmWorkFlow');
		if(!frm.valid()) return ;
		if(frm.setData)frm.setData();
	}
	
	var action=__ctx+"/oa/flow/task/saveData.do";
	submitForm(action,button);
}

// 完成当前任务。
function completeTask(){
	var nextPathId=$("input[name='nextPathId']");
	if(nextPathId.length>0){
		var v=$("input[name='nextPathId']:checked").val();
		if(!v){
			$.ligerDialog.error("在同步条件后的执行路径中，您至少需要选择一条路径!",'提示信息');
			return;	
		}
	}
	//根据跳转节点设置是否是跳转回子节点
	var result=setBackSubFlowNode();
	if(!result){
		return;
	}
	var operatorObj=getByOperatorType();
	var button="#" +operatorObj.btnId;
	var action=__ctx+"/oa/flow/task/complete.do";
	// 执行前置脚本
	var rtn=beforeClick(operatorType);
	if( rtn==false){
		return;
	}
	// 确认执行任务。
	if(isNeedSubmitConfirm){
		$.ligerDialog.confirm("您确定执行此操作吗？","提示",function(rtn){
			if(rtn){
				submitForm(action,button);
			}
		});
	}
	else{
		submitForm(action,button);	
	}
}


function getByOperatorType(){
	var obj={};
	switch(operatorType){
		// 同意
		case 1:
			obj.btnId="btnAgree";
			obj.msg="执行任务成功!";
			break;
		// 反对
		case 2:
			obj.btnId="btnNotAgree";
			obj.msg="执行任务成功!";
			break;
		// 弃权
		case 3:
			obj.btnId="btnAbandon";
			obj.msg="执行任务成功!";
			break;
			// 驳回
		case 4:
			obj.btnId="btnReject";
			obj.msg="驳回任务成功!";
			break;
		// 驳回到发起人
		case 5:
			obj.btnId="btnRejectToStart";
			obj.msg="驳回到发起人成功!";
			break;
		// 保存表单
		case 8:
			obj.btnId="btnSave";
			obj.msg="保存表单数据成功!";
			break;
		
	}
	return obj;
}

function getErrorByOperatorType(){
	var rtn="";
	switch(operatorType){
		// 同意
		case 1:
		// 反对
		case 2:
		// 弃权
		case 3:
			rtn="执行任务失败!";
			break;
		// 驳回
			// 驳回
		case 4:
			rtn="驳回任务失败!";
			break;
		// 驳回到发起人
		case 5:
			rtn="驳回到发起人失败!";
			break;
		// 保存表单
		case 8:
			rtn="保存表单数据失败!";
			break;
	}
	return rtn;
}

function showResponse(responseText){
	$(".l-tab-loading").hide();
	var operatorObj=getByOperatorType();
	$("#" +operatorObj.btnId).removeClass("disabled");
	var obj=new com.ibms.form.ResultMessage(responseText);
	debugger;
	if(obj.isSuccess()){
		$.ligerDialog.success(operatorObj.msg,'提示信息',function(){
			var rtn=afterClick(operatorType);
			if( rtn==false){
				return;
			}
			handJumpOrClose();
		});
	}else{
		var title=getErrorByOperatorType();
		$.ligerDialog.err('出错信息',title,obj.getMessage());
	}
}

// 弹出回退窗口 TODO 去除
function showBackWindow(){
	new TaskBackWindow({taskId:taskId}).show();
}
$(function(){
	initForm();
	// 显示路径
	if(isHandChoolse){
		chooseJumpType(2);
	}else{
		chooseJumpType(1);
	}
	resizeIframe();
	// 初始化联动设置
	if(bpmGangedSets&&bpmGangedSets.length>0){
		FormUtil.InitGangedSet(bpmGangedSets);
	}
	// ibms签章
	var conf = {
		addBtn : $(".ibmsSign"),
		signArea : 'sign-panel-body'
	}
	ibmsSign = new IbmsWebSign(conf);
	// 审批意见控件
	// 如果需要指定审批意见模板，new
	// VoteContent({spyjModels:${spyjModels},mcode:"test001"});
	voteContent=new VoteContent({spyjModels:spyjModels});
	voteContent.init();
});	

// 获取是否允许直接结束。
function getIsDirectComplete(){
	var isDirectComlete = false;
	if($("#chkDirectComplete").length>0){
		if($("#chkDirectComplete").attr("checked")=="checked"){
			isDirectComlete = true;
		}
	}
	return isDirectComlete;
}

// 提交第三方表单时检查该表单的参数
function setExtFormData(){
	if(isExtForm){
		var frm=$('#frmWorkFlow');
		if(!frm.valid()) return ;
		if(frm.setData)frm.setData();
	}
}

function initBtnEvent(){
	// 0，弃权，1，同意，2反对。
	var objVoteAgree=$("#voteAgree");
	var objBack=$("#back");
	
	// 同意
	if($("#btnAgree").length>0){
			$("#btnAgree").click(function(){
				$.ligerDialog.confirm("确认同意？","提示",function(rtn){
					if(rtn){
						if(preAgree()){
							setExtFormData();
							var isDirectComlete = getIsDirectComplete();
							operatorType=1;
							// 同意:5，一票通过。
							var tmp =isDirectComlete?"5":"1";
							objVoteAgree.val(tmp);
							objBack.val("0");
							completeTask();
						}
					}
				});
			});
		
	}
	// 反对
	if($("#btnNotAgree").length>0){
		debugger;
		$("#btnNotAgree").click(function(){
			if(voteContent.hasVote()){
				$.ligerDialog.confirm("确认反对？","提示",function(rtn){
					if(rtn){
						/*setExtFormData();*/
						var isDirectComlete = getIsDirectComplete();
						operatorType=2;
						// //直接一票通过
						var tmp =isDirectComlete?'6':'2';
						objVoteAgree.val(tmp);
						objBack.val("0");
						completeTask();
					}
				});
			}
			else{
				$.ligerDialog.warn("请填写反对意见","提示信息");
			}

		});
	}
	// 弃权
	if($("#btnAbandon").length>0){
		$("#btnAbandon").click(function(){
			$.ligerDialog.confirm("确认弃权？","提示",function(rtn){
				if(rtn){
					setExtFormData();
					operatorType=3;
					objVoteAgree.val(0);
					objBack.val("");
					completeTask();
				}
			});
		})
	}
	// 驳回
	if($("#btnReject").length>0){
		$("#btnReject").click(function(){
			if(voteContent.hasVote()){
				/*setExtFormData();*/
				operatorType=4;
				$.ligerDialog.confirm("确认驳回？","提示",function(rtn){
					if(rtn){
						objVoteAgree.val(3);
						objBack.val(1);
						completeTask();
					}
				});
			}else{
				$.ligerDialog.warn("请填写驳回意见","提示信息");
			}
		})
	}
	// 驳回到发起人
	if($("#btnRejectToStart").length>0){
		$("#btnRejectToStart").click(function(){
			if(voteContent.hasVote()){
				/*setExtFormData();*/
				operatorType=5;
				// 驳回到发起人
				$.ligerDialog.confirm("确认驳回到发起人？","提示",function(rtn){
					if(rtn){
						objVoteAgree.val(3);
						objBack.val(2);
						completeTask();
					}
				});
			}else{
				$.ligerDialog.warn("请填写驳回意见","提示信息");
			}
		})
	}
	
	// 保存表单
	if($("#btnSave").length>0){
		$("#btnSave").click(function(){
			// 保存表单前的预处理
			preSaveHandler();
			// 提交第三方表单时检查该表单的参数
			setExtFormData();
			
			operatorType=8;
			saveData();
			// postSaveHandler();
		})
	}
	
	// 终止流程
	$("#btnEnd").click(function(){
		operatorType=18;
		isTaskEnd(endTask);
	});
	// 终止流程
	$("#btnPrint").click(function(){
		operatorType=18;
		isTaskEnd(endTask);
	});
}
// 保存表单前的预处理
function preSaveHandler(){
	
}
// 终止流程。
function endTask(){
	var url=__ctx + "/oa/flow/task/toStartEnd.do";
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:250,
		width: 400,
		title : '终止流程',
		url: url, 
		isResize: true,
		// 自定义参数
		sucCall:function(rtn){
			var rtn=beforeClick(operatorType);
			if( rtn==false){
				return;
			}
			var url=__ctx+"/oa/flow/task/endProcess.do?taskId="+taskId;
			var params={taskId:taskId,memo:rtn};
			$.post(url,params,function(responseText){
				var obj=new com.ibms.form.ResultMessage(responseText);
				if(!obj.isSuccess()){
					$.ligerDialog.err("提示信息","终止任务失败!",obj.getMessage());
					return;
				}
				$.ligerDialog.success(obj.getMessage(),"提示信息",function(){
					// 执行后置脚本
					var rtn=afterClick(operatorType);
					if( rtn==false){
						return;
					}					
					handJumpOrClose();
				});
			});
		}
	});
}

function handJumpOrClose(){
	if(window.opener){
		   window.opener.location.href=window.opener.location.href.getNewUrl();
		   window.close();
		}else {
			dialog.get("sucCall")("ok");
			window.parent.location.href=window.parent.location.href.getNewUrl();
			dialog.close();
			
		}	
}

function initForm(){
	// 初始化按钮事件。
	initBtnEvent();
	// 初始化审批意见控件
	
	if(isEmptyForm) return;
	
	if(isExtForm){				
		var formUrl=$('#divExternalForm').attr("formUrl");
		$('#divExternalForm').load(formUrl, function() {
			hasLoadComplete=true;
			// 动态执行第三方表单指定执行的js
			try{
				afterOnload();
			}catch(e){}
			initSubForm();
		});
	}else{
		$(".taskopinion").each(function(){
			$(this).removeClass("taskopinion");
			var actInstId=$(this).attr("instanceId");
			$(this).load(__ctx+"/oa/flow/taskOpinion/listform.do?actInstId="+actInstId);
		});
		initSubForm();
	}
}

function initSubForm(){
	$('#frmWorkFlow').ajaxForm({success:showResponse }); 
}

function showRoleDlg(){
	RoleDialog({callback:function(roleId,roleName){$('#forkUserUids').val(roleId);}}); 
}

function chooseJumpType(val){
	if(isHidePath&&isManage==0) return;
	var obj=$('#jumpDiv');
	var url="";
	if(val==1){
		url=__ctx+"/oa/flow/task/tranTaskUserMap.do?taskId="+taskId+"&selectPath=0";
	}
	// 选择路径跳转
	else if(val==2){
		url=__ctx+"/oa/flow/task/tranTaskUserMap.do?taskId="+taskId;
	}
	// 自由跳转
	else if(val==3){
		url=__ctx+"/oa/flow/task/freeJump.do?taskId="+taskId;
	}
	url=url.getNewUrl();
	if(val==3){  // 自由跳转
		$.ajaxSetup({async:false});  // 同步 获得结果后 再去查询相关的人员
		obj.html(obj.attr("tipInfo")).show().load(url);
		$.ajaxSetup({async:true}); // 异步
		// 自由跳转 时 从下拉节点的默认的值 中查出相关的人员
		var destTask=$('#destTask');   // 默认的选中的对象
		changeDestTask(destTask);    // 查出相关的人员 并改显示出来
	}else{
		obj.html(obj.attr("tipInfo")).show().load(url);
	}
	
};

// 为目标节点选择执行的人员列表
function selExeUsers(obj,nodeId){
	var span=$(obj).siblings("span");
	var aryChk=$(":checkbox",span);
	var selectExecutors =[];  
	aryChk.each(function(){   
		var val=$(this).val();
	    var k=val.split("^");
		var userObj={};
		userObj.type=k[0];
		userObj.id=k[1];
		userObj.name=k[2];
		selectExecutors.push(userObj);    
	});    

	FlowUserDialog({selectUsers:selectExecutors,callback:function(types,objIds,objNames){
		if(objIds==null) return;
		var aryTmp=[];
		for(var i=0;i<objIds.length;i++){
			var check="<input type='checkbox' name='" + nodeId + "_userId' checked='checked' value='"+types[i] +"^"+  objIds[i]+"^"+objNames[i] +"'/>&nbsp;"+objNames[i];
			aryTmp.push(check);
		}
		span.html(aryTmp.join(''));
	}});
}

function selectExeUsers(obj){
	var destTask=$("#destTask");
	if(destTask){
		$("#lastDestTaskId").val(destTask.val());
	}
	selExeUsers(obj,destTask.val());
}
// 显示审批历史
function showTaskOpinions(){
	var url=__ctx+"/oa/flow/taskOpinion/list.do?runId="+runId+"&isOpenDialog=1";
	url=url.getNewUrl();
	DialogUtil.open({
		url:url,
		title:'审批历史',
		height:'600',
		width:'800',
		arg:"taskOpinion"
	});
}
// 更改
function changeDestTask(sel){
	var nodeId=sel.value;
	if(typeof nodeId == 'undefined'){    //对象不是用原始JS的，而是通过Jquery获取的对象
		nodeId = sel.val();
	}
	if(typeof nodeId == 'undefined' || nodeId==null || nodeId==""){
		$('#jumpUserDiv').html("");
		$('#lastDestTaskId').val("");
		return;
	}
	$('#lastDestTaskId').val(nodeId);
	
	
/*	var url=__ctx+"/oa/flow/task/getTaskUsers.do?taskId="+taskId+"&nodeId="+nodeId;
	$.getJSON(url, function(dataJson){
		var data=eval(dataJson);
		var aryHtml=[];
		for(var i=0;i<data.length;i++){
		  var span="<input type='checkbox' name='" + nodeId + "_userId' checked='checked' value='"+data[i].type+"^"+data[i].executeId+"^"+data[i].executor+"'/>&nbsp;"+data[i].executor;
		  aryHtml.push(span);
		}
		$('#jumpUserDiv').html(aryHtml.join(''));
	});
*/	
}


//更改
function changeCustomDestTask(sel){
	var nodeId=sel.value;
	if(typeof nodeId == 'undefined'){    //对象不是用原始JS的，而是通过Jquery获取的对象
		nodeId = sel.val();
	}
	if(typeof nodeId == 'undefined' || nodeId==null || nodeId==""){
		$('#jumpUserDiv').html("");
		$('#lastDestTaskId').val("");
		return;
	}
	$('#lastDestTaskId').val(nodeId);
	var url=__ctx+"/oa/flow/task/getTaskUsers.do?taskId="+taskId+"&nodeId="+nodeId;
	$.getJSON(url, function(dataJson){
		var data=eval(dataJson);
		var aryHtml=[];
		for(var i=0;i<data.length;i++){
		  var span="<input type='checkbox' name='" + nodeId + "_userId' checked='checked' value='"+data[i].type+"^"+data[i].executeId+"^"+data[i].executor+"'/>&nbsp;"+data[i].executor;
		  aryHtml.push(span);
		}
		$('#jumpUserDiv').html(aryHtml.join(''));
	});
	
}

function isTaskEnd(callBack){
	var url=__ctx+"/oa/flow/task/isTaskExsit.do";
	var params={taskId:taskId};
	
	$.post(url,params,function(responseText){
		var obj=new com.ibms.form.ResultMessage(responseText);			
		if(obj.isSuccess()){
			callBack.apply(this);
		}
		else{
			$.ligerDialog.warn("当前任务已经完成或被终止","提示信息");
		}
	});
}

// 转交待办
function changeAssignee(){
	if(isCanAssignee){
		isTaskEnd(BpmTaskExeAssignDialog({taskId:taskId,callback:function(rtn){
			if(rtn){
				handJumpOrClose();
			}
		}
		}));
	}
	else{
		$.ligerDialog.warn("当前任务不能转办!","提示信息");
	}
};


function resizeIframe(){
	if($("#frameDetail").length==0) return;
	$("#frameDetail").load(function() { 
		$(this).height($(this).contents().height()+20); 
	}) ;
}

function openForm(formUrl){
	var winArgs="dialogWidth=500px;dialogHeight=400px;help=0;status=0;menu=0;scroll=0;center=1";
	var url=formUrl.getNewUrl();
	window.open(url,"",winArgs);
}

function reference(){
	var url=__ctx+"/oa/flow/processRun/getRefList.do?defId=" +defId;
	var params="height=400,width=700,status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
	window.open(url);
}

function openHelpDoc(fileId){
	var h=window.top.document.documentElement.clientHeight;
	var w=window.top.document.documentElement.clientWidth;
	var vars="top=0,left=0,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
	var showUrl = __ctx+"/oa/form/office/get.do?fileId=" + fileId;
	window.open(showUrl,"myWindow",vars);	
}

// 增加Web签章
function addWebSigns(){
	AddSecSignFromServiceX();          // WebSignPlugin JS类
}

// 增加手写签章
function addHangSigns(){
	AddSecHandSignNoPromptX();          //WebSignPlugin JS类
}

// add by hxl 20151116
function preAgree(){
	return true;
}

// add by limei 20151209
function preGetDataHandler(CustomForm,data){
	// 添加保存前获取数据处理
	return data;
}

function setBackSubFlowNode(){

	var subFolwPk="";
	var prentFlowKey=$("#customDestTaskdiv").find("option:selected").attr("prentFlowKey");
	if(!prentFlowKey||prentFlowKey==''){
		return true;
	}else if(prentFlowKey==curFlowKey){
		//则表示是跳转到子流程，必须选择子流程流程实例
		$("[type='reltable']").each(function(i,tab){
			var ck=$(tab).find("input[name='r:"+$(tab).attr("tablename")+":id']:checked");
			if(ck.length>0){
				$(ck).each(function(j,subpk){
					subFolwPk+=$(subpk).val()+",";
				})
				subFolwPk=subFolwPk.substring(0,subFolwPk.length-1);
				$("#isBackToExtSub").val(1);
				$("#subbusinessKey").val(subFolwPk);
			}
		})
		if(subFolwPk.length<1){
			alert("请选择一个子流程实例");
			return false;
		}
	}
	return true;
	
}

//显示审批历史
function opinionHistory(actInstId,nodeId){
	var url=__ctx+"/oa/flow/taskOpinion/getNodeList.do?actInstId="+actInstId+"&nodeId="+nodeId;
	url=url.getNewUrl();
	DialogUtil.open({
		url:url,
		title:'审批历史',
		height:'600',
		width:'800',
		arg:"taskOpinion"
	});
}


function superform(actInstId){
	jQuery.openFullWindow(__ctx+'/oa/flow/processRun/get.do?tab=1&actInstId='+actInstId);
}