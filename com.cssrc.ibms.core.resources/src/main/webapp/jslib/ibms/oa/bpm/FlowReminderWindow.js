/**
 * 流程节点催办时间设置窗口。
 * conf:参数如下：
 * actDefId：流程定义ID
 * nodeId：流程节点Id
 * dialogWidth：窗口宽度，默认值650
 * dialogWidth：窗口宽度，默认值400
 * @param conf
 */

function FlowReminderWindow(conf)
{
	
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/taskReminder/edit.do?actDefId="+conf.actDefId+"&nodeId=" + conf.nodeId;
	var dialogWidth=900;
	var dialogHeight=690;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1},conf);
	if(conf.parentActDefId){
		url += "&parentActDefId="+conf.parentActDefId;
	}
	url=url.getNewUrl();

	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '流程节点',
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