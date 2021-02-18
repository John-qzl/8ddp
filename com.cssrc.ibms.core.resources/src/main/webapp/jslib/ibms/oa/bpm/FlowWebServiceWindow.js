/**
 * webService设置。 conf参数属性： actDefId： act流程定义ID nodeId:流程节点ID
 * 
 * @param conf
 */
function FlowWebServiceWindow(conf) {
	if (!conf)
		conf = {};
	var url = __ctx + "/oa/flow/nodeWebService/edit.do?actDefId="
			+ conf.actDefId + "&nodeId=" + conf.nodeId+"&defId="+conf.defId;
	url = url.getNewUrl();
	jQuery.openFullWindow(url);
}