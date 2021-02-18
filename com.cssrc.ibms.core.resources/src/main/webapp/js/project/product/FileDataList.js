function showDetail(pk){
	DialogUtil.open({
		url:__ctx+"/oa/form/dataTemplate/detailData.do?__displayId__=10000021410561"+"&__pk__="+pk,
		height: 600,
		width: 1000,
		title:"验收报告",
		isResize: true,
		sucCall:function(rtn){	
			 //reFresh() ;
		}
	});
}
