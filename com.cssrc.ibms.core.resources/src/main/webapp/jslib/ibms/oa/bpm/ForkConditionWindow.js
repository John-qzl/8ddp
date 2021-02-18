
/**
 * 设置网关跳转规则。
 * conf参数属性：
 * deployId:activiti流程定义ID
 * defId:流程定义ID
 * nodeId:流程节点ID
 * @param conf
 */
function ForkConditionWindow(conf){
	var url=__ctx + "/oa/flow/definition/setCondition.do?deployId="+conf.deployId+"&defId=" + conf.defId +"&nodeId=" + conf.nodeId;
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:600,
		width: 700,
		title : '设置网关跳转规则',
		url: url, 
		isResize: true
	});
}