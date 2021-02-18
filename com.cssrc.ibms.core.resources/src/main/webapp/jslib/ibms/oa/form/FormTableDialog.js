/**
 * 功能:选择表话框
 * 
 */
function FormTableDialog(conf){
	var url=__ctx + '/oa/form/formTable/selector.do';
	if(conf.isExternal!=undefined){
		url+="?isExternal=" +conf.isExternal;
	}
	url=url.getNewUrl();

	var that =this;
	DialogUtil.open({
		height:500,
		width: 800,
		title : '选择表对话框',
		url: url, 
		isResize: false,
		//自定义参数
		sucCall:function(rtn){
			if(rtn!=undefined){
				if(conf.callBack)
					conf.callBack.call(that,rtn.tableId,rtn.tableName);
			}
		}
	});
}