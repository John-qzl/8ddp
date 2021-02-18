/**
 * 分级角色选择器。
 * 回调函数
 * 返回角色ID和角色名称。
 */
function GradeRoleSelectDialog(conf)
{
	var url=__ctx + '/oa/system/grade/roleSelector.do?orgId=' + conf.orgId;
	url=url.getNewUrl();
	var that =this;
	DialogUtil.open({
		height:500,
		width: 695,
		title : '分级角色选择器',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf.callback){
				if(rtn!=undefined){
					 conf.callback.call(that,rtn.roleId,rtn.roleName);
				}
			}
		}
	});
}