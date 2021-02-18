
$(function(){
	// 设置表单。
	initForm();
});

// 设置表单。
function initForm(){
	if(isFormEmpty){
		//表单为空
		return;
	} 
	if(isExtForm){
		//外部表单
		form=$('#frmWorkFlow').form({excludes:"[type=append]"});
		var formUrl=$('#divExternalForm').attr("formUrl");
		$('#divExternalForm').load(formUrl, function() {
			hasLoadComplete=true;
			// 动态执行第三方表单指定执行的js
			try{
				afterOnload();
			}catch(e){}
			$('#frmWorkFlow').ajaxForm({success:showResponse}); 
		});
	}
	else{
		//在线表单
		$('#frmWorkFlow').ajaxForm({success:showResponse}); 

	}
};

// 表单数据提交。
// action:表单提交到的URL
// button：点击按钮的样式。
function executeAction(action){
	if(isFormEmpty){
		$.ligerDialog.warn('流程表单为空，请先设置流程表单!',"提示信息");
		return;
	}
	var url=__ctx+"/oa/flow/task/execute.do";
	$('#frmWorkFlow').attr("action",url);
	$("#actionName").val($(action).attr("actionName"));
    if(isExtForm){
    	// 提交第三方表单时检查该表单的参数
		if(form.valid()){
			var frm=$('#frmWorkFlow');
			if(frm.setData)frm.setData();
			$(button).addClass("disabled");
			$(".l-tab-loading").show();
			$('#frmWorkFlow').submit();
		}
	}else{
		var rtn=CustomForm.validate({ignoreRequired:true,returnErrorMsg:true});
		if(!rtn.success){
			$.ligerDialog.warn("表单验证不成功,请检查表单是否正确填写:"+rtn.errorMsg,"提示信息");
			return;
		}
		// WebSign控件提交。 有控件时才提交 xcx
		if(WebSignPlugin.hasWebSignField){
			WebSignPlugin.submit();
		}	
		var uaName=navigator.userAgent.toLowerCase();	
		//传进执行人字符串
		if(uaName.indexOf("firefox")>=0||uaName.indexOf("chrome")>=0){  // 火狐和谷歌
			// Office控件提交。 有可以提交的文档
			if(OfficePlugin.submitNum>0){
				OfficePlugin.submit();       
				// 火狐和谷歌 的文档提交包括了 业务提交代码部分（完成
				// OfficePlugin.submit()后面的回调 函数 有 业务提交代码），所以
				// 后面就不用加上业务提交代码
			}else{   
				// 设置表单数据 前面加上
				// $('#frmWorkFlow').ajaxForm({success:showResponse
				// }); 不需要用 ajax 提交了
				$(".l-tab-loading").show();
				$("#formData").val(CustomForm.getData());
				$('#frmWorkFlow').submit();
			}	
		}else{        // IE内核的等
			// Office控件提交。 有可以提交的文档
			if(OfficePlugin.submitNum>0){
				OfficePlugin.submit();
				// 当提交问题 等于 提交数量的变量 时 表示所有文档 都提交了 然后做 业务相关的提交
				if(OfficePlugin.submitNum == OfficePlugin.submitNewNum){    
					// $('#frmWorkFlow').ajaxForm({success:showResponse
					// }); 不需要用 ajax 提交了
					$(".l-tab-loading").show();
					$("#formData").val(CustomForm.getData());
					$('#frmWorkFlow').submit();
					OfficePlugin.submitNewNum = 0; // 重置 提交数量的变量
				}else{
					$.ligerDialog.warn($lang_bpm.ntkOffice.resetOfficeKj,$lang.tip.warn);
				}
			}else{
				// $('#frmWorkFlow').ajaxForm({success:showResponse
				// }); 不需要用 ajax 提交了
				$(".l-tab-loading").show();
				$("#formData").val(CustomForm.getData());
				$('#frmWorkFlow').submit();
			}
		}
	}
}


function showBpmImageDlg(){
	var actDefId=$("#actDefId").val();
	BpmImageDialog({"actDefId":actDefId});
}



function showResponse(handlerResult){
	$(".l-tab-loading").hide();
	//
	if(handlerResult.result){
	    globalBusinessKey = handlerResult.businessKey;
    	$("input[name='businessKey']").val(globalBusinessKey);
		$.ligerDialog.success(handlerResult.msg,'提示信息',function(){
			// 添加后置事件处理
			var rtn=afterClick(operatorType);
			if( rtn==false){
				return;
			}
			dialog.get("sucCall")("ok");
			dialog.close();
		});
	}else{
		$.ligerDialog.err('提示信息',handlerResult.msg,handlerResult.error);
	}
}


function openHelpDoc(fileId){
	var h=screen.availHeight-35;
	var w=screen.availWidth-5;
	var vars="top=0,left=0,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
	var showUrl = __ctx+"/oa/form/office/get.do?fileId=" + fileId;
	window.open(showUrl,"myWindow",vars);	
}

// 增加Web签章
function addWebSigns(){
	AddSecSignFromServiceX();          // WebSignPlugin JS类
}

//增加手写签章
function addHangSigns(){
	AddSecHandSignNoPromptX();          // WebSignPlugin JS类
}

