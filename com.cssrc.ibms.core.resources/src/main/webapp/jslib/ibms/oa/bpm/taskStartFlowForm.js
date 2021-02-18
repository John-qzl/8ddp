
$(function(){
	// 设置表单。
	initForm();
	// 选择第一步任务的执行人
	chooseJumpType();
	// 启动流程事件绑定。
	$("a.run").click(function(){
		var flowNodes = $("input[name='flowNode']");
		if(flowNodes && flowNodes.length>1){
			var flowNode = $("input[type='radio']:checked");
			if(flowNode && flowNode.length==1){
				// 启动流程
				startWorkFlow();
			}
			else{
				$.ligerDialog.warn("请选择一个跳转节点!", '提示');
				return;
			}
		}else{
			// 启动流程
			startWorkFlow();
		}
	});
	// 保存表单
	$("a.save").click(function(){
		saveForm(this);
	});	
	// 重置表单
	$("a.reset").click(function(){
		var fieldName=$(this).attr("name");
		if(fieldName!=undefined&&fieldName!=null&&fieldName!=""){
			return;
		}
		$("#frmWorkFlow").resetForm();
		var parentObj = $(this).parent();
		$("input",parentObj).each(function(){
			$(this).val('');
		})
	});
	
	$("#flowNodeList").delegate("input", "click", function() {
		$("#startNode").val($(this).val());
	});
	// 初始化联动设置
	if(bpmGangedSets){
		FormUtil.InitGangedSet(bpmGangedSets);
	}
	// 隐藏意见控件
	// var opinion = $("textarea[opinionname]");
	// if(opinion){
	// opinion.parent().hide();
	// }
	

});

// 设置表单。
function initForm(){
	if(isFormEmpty) return;
	// 表单不为空的情况。
	if(isExtForm){
		form=$('#frmWorkFlow').form({excludes:"[type=append]"});
		var formUrl=$('#divExternalForm').attr("formUrl");
		$('#divExternalForm').load(formUrl, function() {
			hasLoadComplete=true;
			// 动态执行第三方表单指定执行的js
			try{
				afterOnload();
			}catch(e){}

			initSubForm();
		});
	}
	else{
		initSubForm();
	}
};

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

// 是否点击了开始按钮。
var isStartFlow=true;

function saveForm(obj){

	isStartFlow=false;
	var  action="";
	if($(obj).hasClass('isDraft')){
		action=__ctx+"/oa/flow/task/saveForm.do";
	}else{
		action=__ctx+"/oa/flow/task/saveData.do";
	}
	// 判断是否为保存草稿还是发起流程
	var operatorType=(isStartFlow)?1:6;
	// 前置事件处理
	var rtn=beforeClick(operatorType);
	if( rtn==false){
		return;
	}
	// 表单数据保存前的前处理
	if(!preSaveHandler()){
		return;
	};
	submitForm(action,"a.save");
}
// 启动流程
function startWorkFlow(){
	// startWorkFlow前执行函数
	if(!preStartWorkFlow()){
		return false;
	}
	isStartFlow=true;
	var  action=__ctx+"/oa/flow/task/startFlow.do";
	var operatorType=(isStartFlow)?1:6;
	// 前置事件处理
	var rtn=beforeClick(operatorType);
	if( rtn==false){
		return;
	}
	$.ligerDialog.confirm("确认启动流程吗?","提示",function(rtn){
		if(rtn){
			if(isNeedSubmitConfirm){
				$.ligerDialog.confirm("您确定执行此操作吗？","提示",function(rtn){
					submitForm(action,"a.run");
				});
			}
			else{
				submitForm(action,"a.run");	
				// startWorkFlow后执行函数
				postStartWorkFlow();
			}
		}
	});
}
//
function postStartWorkFlow(){
	return true;
}
function preSaveHandler(){
	// 添加保存前处理
	return true;
}
// 表单数据提交。
// action:表单提交到的URL
// button：点击按钮的样式。
function submitForm(action,button){
	var ignoreRequired=false;
	if(button=="a.save"){
		ignoreRequired=true;
	}			
	if($(button).hasClass("disabled"))return;
	if(isFormEmpty){
		$.ligerDialog.warn('流程表单为空，请先设置流程表单!',"提示信息");
		return;
	}
	$('#frmWorkFlow').ajaxForm({success:showResponse }); // terry
															// add

	$('#frmWorkFlow').attr("action",action);
    if(isExtForm){
    	// 提交第三方表单时检查该表单的参数
		var rtn = true;
		if(button!="a.save"){
			rtn=form.valid()
		}
		if(rtn){
			var frm=$('#frmWorkFlow');
			if(frm.setData)frm.setData();
			$(button).addClass("disabled");
			$(".l-tab-loading").show();
			$('#frmWorkFlow').submit();
		}
	}else{
		var rtn=CustomForm.validate({ignoreRequired:ignoreRequired,returnErrorMsg:true});
		if(!rtn.success){
			$.ligerDialog.warn("表单验证不成功,请检查表单是否正确填写:"+rtn.errorMsg,"提示信息");
			return;
		}
		if(button!="a.save"){
			// 子表必填检查(兼容新旧版本)
			rtn = SubtablePermission.subRequired();
			if(!rtn){	
				$.ligerDialog.warn("子表必须有一条记录，请检查！","提示信息");
				return;
			}
			// 关联表必填检查(兼容新旧版本)
			rtn = ReltablePermission.relRequired();
			if(!rtn){
				$.ligerDialog.warn("关联表必须有一条记录，请检查！","提示信息");
				return;
			}
		}
		// 获取自定义表单的数据
		var data=preGetDataHandler(CustomForm,CustomForm.getData());
		
		// WebSign控件提交。 有控件时才提交 xcx
		if(WebSignPlugin.hasWebSignField){
			WebSignPlugin.submit();
		}	
		
		$(button).addClass("disabled");
		
		var uaName=navigator.userAgent.toLowerCase();	
		// add by hxl 20151022
		var formKey = $("input[name='formKey']").val();
		var actDefId = $("input[name='actDefId']").val();
		var defId = $("input[name='defId']").val();
		var businessKey = $("input[name='businessKey']").val();
		var runId = $("input[name='runId']").val();
		var startNode = $("input[name='startNode']").val();
		var destTask = $("input[name='destTask']:[type='radio']:checked").val();
		// 2016-7-5 下个任务节点 yangbo
		var lastdestTaskIds = $("input[name='lastDestTaskId']:[type='hidden']").val();
		// 2016-7-5 下个任务执行人
		var spanSelectUser = $("input[name='"+lastdestTaskIds+"_userId']:[type='checkbox']:checked");
		var lastDestTaskUids=new Array();
		for(var i=0;i<spanSelectUser.length;i++){
			// 选定跳转目标执行人
			lastDestTaskUids[i]=spanSelectUser[i].value;
		}
		// 传进执行人字符串
		if(uaName.indexOf("firefox")>=0||uaName.indexOf("chrome")>=0){  // 火狐和谷歌
																		// 的文档提交
			// Office控件提交。 有可以提交的文档
			if(OfficePlugin.submitNum>0){
				OfficePlugin.submit();       
				// 火狐和谷歌 的文档提交包括了 业务提交代码部分（完成
				// OfficePlugin.submit()后面的回调 函数 有 业务提交代码），所以
				// 后面就不用加上业务提交代码
			}else{   
				// 没有可提交的文档时 直接做 业务提交代码
				formData=preGetDataHandler(CustomForm,CustomForm.getData());
				// 设置表单数据 前面加上
				// $('#frmWorkFlow').ajaxForm({success:showResponse
				// }); 不需要用 ajax 提交了
				$("#formData").val(formData);		
				$(".l-tab-loading").show();
				$('#frmWorkFlow').submit();
			}	
		}else{        // IE内核的等
			// Office控件提交。 有可以提交的文档
			if(OfficePlugin.submitNum>0){
				OfficePlugin.submit();
				// 当提交问题 等于 提交数量的变量 时 表示所有文档 都提交了 然后做 业务相关的提交
				if(OfficePlugin.submitNum == OfficePlugin.submitNewNum){    
					// 获取自定义表单的数据
					formData=preGetDataHandler(CustomForm,CustomForm.getData());
					// 设置表单数据 前面加上
					// $('#frmWorkFlow').ajaxForm({success:showResponse
					// }); 不需要用 ajax 提交了
					$("#formData").val(formData);
					$(".l-tab-loading").show();
					$('#frmWorkFlow').submit();
					OfficePlugin.submitNewNum = 0; // 重置 提交数量的变量
				}else{
					$.ligerDialog.warn($lang_bpm.ntkOffice.resetOfficeKj,$lang.tip.warn);
				}
			}else{
				// 获取自定义表单的数据
				formData=preGetDataHandler(CustomForm,CustomForm.getData());
				// 设置表单数据 前面加上
				// $('#frmWorkFlow').ajaxForm({success:showResponse
				// }); 不需要用 ajax 提交了
				$("#formData").val(formData);
				$(".l-tab-loading").show();
				$('#frmWorkFlow').submit();
			}
		}
	}
}

function showBpmImageDlg(){
	var actDefId=$("#actDefId").val();
	BpmImageDialog({"actDefId":actDefId});
}

function initSubForm(){
	$('#frmWorkFlow').ajaxForm({success:showResponse }); 
}

function showResponse(responseText){
	$(".l-tab-loading").hide();
	var data = jQuery.parseJSON(responseText);
    globalBusinessKey = data.businessKey;
    var runId=data.runId;
    if(data.success == "true"&&globalBusinessKey!=null&&globalBusinessKey!=''){
    	$("input[name='businessKey']").val(globalBusinessKey);
    	$("input[name='runId']").val(runId);
    }
	var button=(isStartFlow)? "a.run":"a.save";
	var operatorType=(isStartFlow)?1:6;
	$(button).removeClass("disabled");
	var obj=new com.ibms.form.ResultMessage(responseText);
	if(obj.isSuccess()){
		//表单新增时：文件上传的后处理
		var hasPk = $('input[name=hasPk]').val()=="true"?true:false;
		if(!hasPk){

			TableFile.updateFile(null,{keyId:data.message});
		}
		var msg=(isStartFlow)?"启动流程成功!":"保存表单数据成功!";
		$.ligerDialog.success(msg,'提示信息',function(){
			// 添加后置事件处理
			var rtn=afterClick(operatorType);
			if( rtn==false){
				return;
			}
			dialog.get("sucCall")("ok");
			dialog.close();
		});
		
	}
	else{
		var msg=(isStartFlow)?"启动流程失败!":"保存表单数据失败!";
		$.ligerDialog.err('提示信息',msg,obj.getMessage());
	}
}

function chooseJumpType(){
	var obj=$('#jumpDiv');
	/* var url=__ctx+"/oa/flow/task/tranTaskUserMap.do"; */
	var url=__ctx+"/oa/flow/task/tranTaskUserMap.do?isStart=1&&actDefId="+actDefId;
	url=url.getNewUrl();
	obj.html(obj.attr("tipInfo")).show().load(url);
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
	AddSecHandSignNoPromptX();          // WebSignPlugin JS类
}
// startWorkFlow前执行函数
function preStartWorkFlow(){
	return true;
}

// 定制按钮事件
function dingZhi(){
	return true;
}

// add by limei 20151209
function preGetDataHandler(CustomForm,data){
	// 添加保存前获取数据处理
	return data;
}
