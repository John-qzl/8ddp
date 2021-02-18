function PersonScriptDialog(conf)
{
	var dialogWidth=730;
	var dialogHeight=500;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/oa/system/personScript/dialog.do';
	url=url.getNewUrl();
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '脚本窗口',
		url: url, 
		isResize: true,
		//自定义参数
		data: conf.data,
		sucCall:function(rtn){
			if(conf.callback)
			{
				if(rtn && rtn.id){
					 conf.callback.call(this,rtn);
				}
			}
		}
	});
}