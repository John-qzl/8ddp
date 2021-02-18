/**
 * 设置操作按钮对话框。
 * conf参数属性：
 * actDefId： act流程定义ID
 * nodeId:节点ID
 * defId:流程定义ID
 * @param conf
 */
function BpmNodeButtonWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/nodeButton/getByNode.do?buttonFlag=false&nodeId=" + conf.nodeId +"&defId=" + conf.defId;

	var dialogWidth=800;
	var dialogHeight=380;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1},conf);
	url=url.getNewUrl();

	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '设置操作按钮',
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