/**
 * 消息参数设置。
 * conf参数属性：
 * actDefId： act流程定义ID
 * nodeId:流程节点ID
 * @param conf
 */
function FlowMessageWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/nodeMessage/edit.do?actDefId=" + conf.actDefId +"&nodeId=" + conf.nodeId;
	var dialogWidth=780;
	var dialogHeight=580;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1},conf);
	
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '消息参数设置',
		url: url, 
		isResize: true
	});


}