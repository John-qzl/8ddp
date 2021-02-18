/**
 * 流程节点手机支持设置
{actDefId:actDefId,nodeId:activitiId,activityName:activityName}
 * @param conf
 */
MobileSetWindow=function(conf){
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/definition/mobileSet.do?actDefId=" + conf.actDefId+"&nodeId=" + conf.nodeId;
	var dialogWidth=400;
	var dialogHeight=200;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '流程节点手机支持设置',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if (conf.callback) {
				conf.callback.call(this,rtn);
			}
		}
	});
};