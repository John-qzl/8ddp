/**
 * 流程节点分发及汇总设置
{actDefId:actDefId,nodeId:activitiId,activityName:activityName}
 * @param conf
 */
FlowForkJoinWindow=function(conf){
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/definition/editForkJoin.do?actDefId=" + conf.actDefId+"&nodeId=" + conf.nodeId;
	var dialogWidth=600;
	var dialogHeight=300;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '流程节点分发及汇总',
		url: url, 
		isResize: true
	});
};