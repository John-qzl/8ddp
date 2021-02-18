/**
 * 功能:流程运行对话框
 * 
 * 参数：
 * conf:配置为一个JSON
 * 
 * dialogWidth:对话框的宽度。
 * dialogHeight：对话框高度。
 * 
 * actDefId:流程定义ID。
 * 
 */
function ProcessUrgeDialog(conf)
{
	if(!conf) conf={};	
	var url=__ctx + '/oa/flow/processRun/urgeOwner.do?actInstId=' + conf.actInstId;
	
	var dialogWidth=450;
	var dialogHeight=250;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1,reload:true},conf);
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '流程运行',
		url: url, 
		isResize: true
	});
}