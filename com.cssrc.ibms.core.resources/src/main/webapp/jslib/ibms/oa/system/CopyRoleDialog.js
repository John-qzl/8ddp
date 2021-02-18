
/**
 * 复制角色选择窗口。
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
function CopyRoleDialog(conf)
{
	if(!conf) conf={};
	var roleId=conf.roleId;
	var url=__ctx + "/oa/system/sysRole/copy.do?roleId=" +roleId;
	$.extend(conf, {help:0,status:0,scroll:0,center:1});

	url=url.getNewUrl();
	
	DialogUtil.open({
		height:250,
		width: 550,
		title : '复制角色',
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


