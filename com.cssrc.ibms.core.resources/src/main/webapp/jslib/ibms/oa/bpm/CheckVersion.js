//检查版本升级
function checkVersion(conf){
	var u = __ctx +'/oa/flow/definition/checkVersion.do';
	$.post(u,{defId:conf.defId},function(res){
		var result = eval("("+res+")");		
		if(result.success){
			var url = "";
			if(	conf.type)
				url = __ctx +'/oa/flow/task/startFlowForm.do?draftId='+conf.draftId+'&defId='+conf.defId;
	 		else
	 			url = __ctx +'/oa/flow/task/startFlowForm.do?defId='+conf.defId+'&copyKey='+conf.businessKey;

			if(result.isMain == 1){
				//jQuery.openFullWindow(url);
				var height= window.top.document.documentElement.clientHeight;
				var width=window.top.document.documentElement.clientWidth;
				DialogUtil.open({
			        height:height,
			        width: width,
			        url: url, 
			        isResize: true,
			        sucCall:function(rtn){
						if(!(rtn == undefined || rtn == null || rtn == '')){
							location.href=location.href.getNewUrl();
						}
					}
			    });
			}else{
				var msg =  conf.type? '已发布了新版本!<br/>此版本已过期，无法启动!':'已发布了新版本!<br/>此版本已过期，无法复制!';
				$.ligerDialog.warn(msg,'提示')
			}
		}else{
			$.ligerDialog.error(result.msg,'提示');
		}
	});
}