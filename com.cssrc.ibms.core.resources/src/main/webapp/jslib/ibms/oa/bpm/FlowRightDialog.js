/**
 * 流程分配权限。
 * 
 * @param defId		流程定义ID
 * @param type		流程类型(0,流程定义ID,1,分类类型)
 * @param defKey	流程定义key
 */
function FlowRightDialog(id,type,defKey,isParent){
	var url=__ctx +"/oa/flow/defRight/list.do?id=" + id +"&type=" +type;
	if(typeof defKey!='undefined'&&defKey!=''){
		url+="&defKey=" +defKey;
	}
	if(typeof isParent!='undefined'){
		url+="&isParent=" +isParent;
	}
	url=url.getNewUrl();
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '',
		url: url, 
		isResize: true
	});
};
/**
 * 业务数据浏览模板权限分配
 * @param id 模板ID
 * @param type 分类搜索类型(0-->模板ID;1-->表单类型)
 */
function FlowTemplateDialog(id,type){
 	var url=__ctx +"/oa/form/tableTemprights/list.do?id=" + id+"&type=" +type;
	url=url.getNewUrl();
 	
 	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '',
		url: url, 
		isResize: true
	});
}