function FlowSetWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx+ "/oa/flow/definition/subFlowDetail.do?defId="+conf.defId+"&actDefId=" + conf.actDefId +"&returnUrl=" + conf.returnUrl +"&nodeId=" +conf.nodeId;
	url=url.getNewUrl();
	var winArgs="height=600,width=800,status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes";
	var rtn=window.open(url,null,winArgs);
}