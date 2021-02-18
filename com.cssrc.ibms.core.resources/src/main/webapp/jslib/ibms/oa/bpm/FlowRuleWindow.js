
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

function FlowRuleWindow(conf)
{	
	var url=__ctx + "/oa/flow/nodeRule/edit.do?deployId="+conf.deployId+"&actDefId=" + conf.actDefId +"&nodeId=" + conf.nodeId+"&nodeName=" +encodeURIComponent(conf.nodeName);
	if(conf.parentActDefId){
		url += "&parentActDefId="+conf.parentActDefId;
	}
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:650,
		width: 800,
		title : '任务会签规则',
		url: url, 
		isResize: true,
		sucCall:function(rtn){
			if (conf.callback) {
				conf.callback.call(this,rtn);
			}
		}
	});
}