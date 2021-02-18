if (typeof FlowUtil == 'undefined') {
	FlowUtil = {};
}

/**
 * 启动流程。
 * @param defId 流程定义ID。
 */
FlowUtil.startFlow=function(defId,actDefId){
	var url= __ctx +"/oa/flow/definition/getCanDirectStart.do";
	var params={defId:defId};
	$.post(url,params,function(data){
		if(data){
			var callBack=function(rtn){
				if(!rtn) return;
				var flowUrl= __ctx +"/oa/flow/task/startFlow.do";
				var parameters={actDefId:actDefId};
				$.post(flowUrl,parameters,function(responseText){
					var obj=new com.ibms.form.ResultMessage(responseText);
					if(obj.isSuccess()){//成功
						$.ligerDialog.success("启动流程成功!",'提示信息');
					}
					else{
						$.ligerDialog.error("启动流程失败!",'出错了');
					}
				});
			};
			$.ligerDialog.confirm("需要启动流程吗?",'提示信息',callBack);
		}else{
			var url=__ctx +"/oa/flow/task/startFlowForm.do?defId="+defId;
			//jQuery.openFullWindow(url);
			var height=window.top.document.documentElement.clientHeight;
			var width=window.top.document.documentElement.clientWidth;
			TaskStartFlowForm({height:height, width:width, url:url});
		}
	});
};



/**
 * 流程追回。
 * @param runId
 * @param memo
 */
FlowUtil.recover=function(conf){
	if(!conf) conf={};	
	var url= __ctx +"/oa/flow/processRun/checkRecover.do";
	if(conf.backToStart==0){
		url= __ctx +"/oa/flow/processRun/checkRedo.do";
	}	
	var params={runId:conf.runId ,backToStart: conf.backToStart};
	$.post(url,params,function(data){
		 var obj=new com.ibms.form.ResultMessage(data);
		 if(obj.isSuccess()){
				var url=__ctx + '/oa/flow/processRun/recoverDialog.do?runId=' + conf.runId +'&backToStart=' + conf.backToStart;
				if(conf.backToStart==0){
					url=__ctx + '/oa/flow/processRun/redoDialog.do?runId=' + conf.runId +'&backToStart=' + conf.backToStart;
				}
				var dialogWidth=500;
				var dialogHeight=300;
				conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1,reload:true},conf);
				url=url.getNewUrl();
				
				DialogUtil.open({
					height:conf.dialogHeight,
					width: conf.dialogWidth,
					title : '流程追回',
					url: url, 
					isResize: true,
					//自定义参数
					sucCall:function(rtn){
						if(rtn && conf.callback){
							conf.callback(this);
						}
					}
				});
		 }
		 else{
			 $.ligerDialog.err("提示","撤销失败!",obj.getMessage());
		 }
	});
};

