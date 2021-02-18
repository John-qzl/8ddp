/*
 * 自定义查询对话框
 */
function BpmQueryDialog(conf)
{
	if(!conf) conf={};
	var url=__ctx +"/oa/form/formQuery/dialog.do";
	var dialogWidth=460;
	var dialogHeight=400;
	$.extend(conf, {dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1});
	url=url.getNewUrl();

	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '通用表单查询管理',
		url: url, 
		isResize: true,
		conf: conf,
		sucCall:function(rtn){
			if(rtn!=undefined){
				conf.callback(rtn);
			}
			
		}
	});
}