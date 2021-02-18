//任务补签窗口  依赖于LigerWindow,UserDialog
function TaskAddSignWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx+'/oa/flow/task/toAddSign.do?taskId=' + conf.taskId;
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:200,
		width: 500,
		title : '任务补签窗口',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					 conf.callback.call(that);
				}
			}
		}
	});
};



