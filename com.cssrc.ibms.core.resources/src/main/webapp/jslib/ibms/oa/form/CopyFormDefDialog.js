
/**
 * 复制表单选择窗口。
 * dialogWidth：窗口宽度，默认值350
 * dialogWidth：窗口宽度，默认值150
 * callback：回调函数
 * 回调参数如下：
 * key:参数key
 * name:参数名称
 * 使用方法如下：
 * 
 * CopyRoleDialog({callback:function(content){
 *		//回调函数处理
 *	}});
 * @param conf
 */
function CopyFormDefDialog(conf)
{
	if(!conf) conf={};
	var formDefId=conf.formDefId;
	var url=__ctx + "/oa/form/formDef/copy.do?formDefId=" +formDefId;
	
	$.extend(conf, {help:0,status:0,scroll:0,center:1});
    
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:350,
		width: 600,
		title : '复制表单选择',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf){
				location.reload();
			}
		}
	});
}


