function userDetail(userid){
	var height=600;
	var width=600;
	var itop=(window.screen.availHeight-30-height)/2
	var ileft=(window.screen.availWidth-10-width)/2
	var url = __ctx+"/oa/system/sysUser/getInfo.do?canReturn=2&hasClose=1&userId="+userid;
	var winArgs = "top="+itop+",left="+ileft+",height="+height+",width="+width+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
	url = url.getNewUrl();
	window.open(url, "", winArgs);
};