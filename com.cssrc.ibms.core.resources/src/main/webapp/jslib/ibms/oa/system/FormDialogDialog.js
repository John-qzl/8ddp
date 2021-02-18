function FormDialogDialog(conf)
{
	if(!conf) conf={};	
	var url=__ctx + "/oa/form/formDialog/dialog.do";	//脚本设计器
	
	var dialogWidth=800;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '自定义对话框配置',
		url: url, 
		isResize: true,
		//自定义参数
		conf:conf,
		sucCall:function(rtn){
			if(rtn!=undefined){
				if(conf.callback){
					conf.callback.call(that,rtn);
				}
			}
		}
	});
}

