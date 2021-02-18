function IdentityList(callBack){
	var url=__ctx + '/oa/system/serialNumber/selector.do';
	url=url.getNewUrl();
	
	var that =this;
	DialogUtil.open({
		height:700,
		width: 800,
		title : '流水号',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(rtn!=undefined){
				callBack.call(that,rtn.alias,rtn.name);
			}
		}
	});
}