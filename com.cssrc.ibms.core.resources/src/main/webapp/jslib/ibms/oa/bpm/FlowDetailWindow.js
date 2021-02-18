function FlowDetailWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/flow/processRun/get.do?tab=1&runId=" + conf.runId;
	url=url.getNewUrl();
	var vars="height=600,width=800,status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";	            
	var rtn=window.open(url,null,vars);
}