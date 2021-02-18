/**
 * 通知方式
{actDefId:actDefId,nodeId:activitiId,activityName:activityName}
 * @param conf
 */
InformTypeWindow=function(conf){
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/definition/informType.do?actDefId=" + conf.actDefId+"&nodeId=" + conf.nodeId;
	var dialogWidth=800;
	var dialogHeight=600;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	if(conf.parentActDefId){
		url += "&parentActDefId="+conf.parentActDefId;
	}
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : "通知方式",
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