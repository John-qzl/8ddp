function RunAliasScript(conf){
	if(!conf) conf={};
	var url=__ctx + '/oa/system/aliasScript/executeAliasScript.do';
	var json={};
	$.ajaxSetup({async:false});  //同步
	$.post(url,conf,function(data){
		json = eval("("+data+")");
		/*if(json.isSuccess==0){
			return json.result;
		}else{
			alert(json.msg);
			return "";
		}*/
	});
	return json
	$.ajaxSetup({async:true}); //异步
}