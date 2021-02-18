
/**
 * 任务会签规则设置。
 * conf:参数如下：
 * deployId:流程定义ID
 * actDefId：act流程节点Id
 * nodeId：流程节点Id
 * dialogWidth：窗口宽度，默认值500
 * dialogWidth：窗口宽度，默认值300
 * @param conf
 */

function UserSetWindow(conf)
{	
	var url=__ctx + "/oa/flow/definition/userSet.do?defId=" + conf.defId +"&nodeId=" + conf.nodeId;
	url=url.getNewUrl();
	DialogUtil.open({
		height:600,
		width: 800,
		title : '任务会签规则设置',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if (conf.callback) {
				conf.callback.call(this,rtn);
			}
		}
	});
}