/**
 * 节点sql设置。
 * conf:参数如下：
 * nodeId:节点ID
 * actDefId：act流程节点Id
 * dialogWidth：窗口宽度，默认值800
 * dialogWidth：窗口宽度，默认值700
 * @param conf
 */
function BpmNodeSqlWindow(conf){
	if(!conf) conf={};
	DialogUtil.open({
		height:700,
		width: 800,
		title : '节点sql设置',
		url: __ctx+"/oa/flow/NodeSql/edit.do?nodeId="+conf.nodeId+"&actdefId="+conf.actDefId, 
		isResize: true
	});
}