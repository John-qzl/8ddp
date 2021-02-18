function SetRelationWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/form/formTable/setRelation.do?tableId="+conf.tableId+"&dsName=" + conf.dsName;
	
	var dialogWidth=600;
	var dialogHeight=350;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1,reload:true},conf);
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '选择自定义表',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(conf.reload){
				location.reload();
			}
		}
	});
}