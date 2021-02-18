function ConditionScriptEditDialog(conf)
{	
	var dialogWidth=730;
	var dialogHeight=500;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/oa/system/conditionScript/editDialog.do';
	url=url.getNewUrl();
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '脚本',
		url: url, 
		isResize: true,
		//自定义参数
		data: conf.data,
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn && rtn.status){
					var data={
						script:	rtn.condition
					};
					 conf.callback.call(that,data);
				}
			}
		}
	});
};