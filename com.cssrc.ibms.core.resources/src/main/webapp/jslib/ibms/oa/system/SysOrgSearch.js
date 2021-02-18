function SysOrgSearch(conf){
	url=__ctx + "/oa/system/sysOrg/allList.do";
	var winArgs="dialogWidth:"+600+"px;dialogHeight:"+500
	+"px;help:0 ;status:0;scroll: 1;center:1";
	url=url.getNewUrl();
	
	var that =this;
	DialogUtil.open({
		height:570,
		width: 650,
		title : '组织树搜索',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(rtn && conf.callback){
				var orgId=rtn.orgId;
				conf.callback.call(that,orgId);
			}
		}
	});

}