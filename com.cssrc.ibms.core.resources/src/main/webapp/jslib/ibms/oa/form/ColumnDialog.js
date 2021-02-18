function ColumnDialog(conf){
	if(!conf) conf={};
	var hw = $.getWindowRect();
	var dialogHeight=hw.height*19/20;
	var dialogWidth=hw.width*19/20;

	var url=__ctx + '/oa/form/formTable/columnDialog.do?isAdd=' ;
	url+=conf.isAdd?"1":"0";
	url+="&isMain="+conf.isMain;
	url=url.getNewUrl();
	
	DialogUtil.open({
		height:dialogHeight,
		width: dialogWidth,
		title : '添加列',
		url: url, 
		isResize: true,
		//自定义参数
		conf: conf
	});
}