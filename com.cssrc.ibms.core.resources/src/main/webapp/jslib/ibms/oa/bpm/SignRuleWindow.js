/**
 * 任务跳转规则设置。
 * conf参数属性：
 * actDefId act流程定义ID
 * nodeId:流程节点ID
 * @param conf
 */
function SignRuleWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx+ "/oa/flow/nodeSign/edit.do?actDefId=" + conf.actDefId +"&nodeId=" +conf.activitiId+"&defId="+conf.defId;
	
	var dialogWidth=800;
	var dialogHeight=600;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	
	url=url.getNewUrl();
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '任务会签规则',
		url: url, 
		isResize: true
	});
}