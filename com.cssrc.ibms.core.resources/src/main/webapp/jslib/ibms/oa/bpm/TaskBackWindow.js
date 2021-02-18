/**
 * 任务回退窗口
 */
function TaskBackWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/task/back.do?taskId="+conf.taskId;

	var dialogWidth=400;
	var dialogHeight=200;
	
	$.extend(conf, {dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1});

	url=url.getNewUrl();

	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '任务回退窗口',
		url: url, 
		isResize: true
	});
}






