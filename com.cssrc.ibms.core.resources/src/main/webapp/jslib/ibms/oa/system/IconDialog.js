function IconDialog(conf){
	if(!conf) conf={};	
	var url=__ctx + "/oa/system/resources/dialog.do";
	if(conf.params)
		url += "?" + conf.params;
	var outerWindow =window.top;
	var dialogHeight = outerWindow.document.documentElement.clientHeight*0.9;
	var dialogWidth = outerWindow.document.documentElement.clientWidth*0.9;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '选择图标',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				conf.callback.call(this,rtn.iconVal); 
			}
		}
	});
};

