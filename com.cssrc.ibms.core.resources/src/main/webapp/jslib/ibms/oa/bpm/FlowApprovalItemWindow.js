/**
 * 设置流程常用语对话框。
 * conf参数属性：
 * activitiId:节点ID
 * defId:流程定义ID
 * @param conf
 */
function FlowApprovalItemWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/taskApprovalItems/edit.do?nodeId=" + conf.activitiId +"&defId=" + conf.defId
		+"&actDefId="+conf.actDefId;
	var dialogWidth=600;
	var dialogHeight=300;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '设置流程常用语',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if (conf.callback) {
				conf.callback.call(that,rtn);
			}
		}
	});
}