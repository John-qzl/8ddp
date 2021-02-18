/**
 * 设置流程事件脚本对话框。
 * conf参数属性：
 * actDefId： act流程定义ID
 * activitiId:节点ID
 * defId:流程定义ID
 * @param conf
 */
function FlowEventWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/nodeScript/edit.do?type="+conf.type+"&actDefId=" + conf.actDefId+"&nodeId=" + conf.activitiId +"&defId=" + conf.defId;

	var dialogWidth=800;
	var dialogHeight=380;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	if(conf.parentActDefId){
		url += "&parentActDefId="+conf.parentActDefId;
	}
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '设置流程事件脚本',
		url: url, 
		isResize: true,
		 sucCall:function(rtn){
			 if (conf.callback) {
					conf.callback.call(this,rtn);
				}
	        }
	});
}