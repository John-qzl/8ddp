var flag=true;

if($("input[name='m:bcsyrwb:rwmc']").val()=="")	flag=false;
if($("input[name='m:bcsyrwb:rwdh']").val()=="")	flag=false;
if($("input[name='m:bcsyrwb:zrbm']").val()=="")	flag=false;
if($("input[name='m:bcsyrwb:sydw']").val()=="")	flag=false;
if(!flag){
	$.ligerDialog.warn("有必填项为空!",'提示信息');
}
return flag;