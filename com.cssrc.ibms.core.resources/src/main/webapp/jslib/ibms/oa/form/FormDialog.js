/**
 * 功能:表单选择器
 * 
 * 参数：
 * conf:配置为一个JSON
 * 
 * dialogWidth:对话框的宽度。
 * dialogHeight：对话框高度。
 * 
 * callback：回调函数
 * 回调函数支持的参数
 * formId：表单ID
 * formName:表单名称
 */
function FormDialog(conf){
	var url=__ctx + '/oa/form/formDef/dialog.do';
	url=url.getNewUrl();
	DialogUtil.open({
		height:540,
		width: 750,
		title : '表单选择器',
		url: url, 
		isResize: true,
		condition:conf.condition,
		//自定义参数
		sucCall:function(rtn){
			if(rtn!=undefined){
				if(conf.callback){
					conf.callback.call(this,rtn.formKey,rtn.formName,rtn.tableId);
				}
			}
		}
	});
}