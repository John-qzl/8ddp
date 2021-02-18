var TableEXT = function(){};

/**
 * 添加列-保存时触发
 * purpose：将扩展的属性添加到field中
 */
TableEXT.getField =function(field){
	try{
		//将扩展的属性值放入field中
		var fileCheck = $('input#fileDisplay').attr("checked")?true:false;
		if(fileCheck){
			var ctlObj = $.parseJSON(field.ctlProperty);
			ctlObj.fileDisplay = "list";
			field.ctlProperty = JSON2.stringify(ctlObj);
		}
		return true;
	}catch(e){
		console.log(e);
		return false;
	}
}
/**
 * 业务表定义-添加列、列编辑时触发
 * 扩展属性的初始化
 * conf：{isAdd:false, 
		isMain:isMain,
		allowEditColName:TableRow.allowEditColName||curTr.hasClass("newColumn"), 
		fieldManage:TableRow.fieldManage,
		field:tmpField,
		isFlowVar:isFlowVar,
		isExternal:TableRow.isExternal,
		callBack : function(){}
		}
 */
TableEXT.columnInit = function(conf){
	var field = conf.field;
	uploadInit(field);
}
/**
 * 添加列界面-初始化时执行
 * 控件事件绑定扩展
 */
TableEXT.controlTypeChange = function(ft,vf,ct){ // {ft:字段类型,vf：值来源,ct：控件类型}
	if(ct=='9'){
		$('#trFileDisplay').show();
	}else{
		$('#trFileDisplay').hide();
	}
}
/**
 * 添加列界面-保存-是 时触发
 * 重置
 */
TableEXT.resetField = function(){
	$('#trFileDisplay').hide();
	$('input#fileDisplay').attr("checked",false);
}




/**----------------------函数声明----------------------*/
function uploadInit(field){
	var tbodyObj = $('#controlType').parents('tbody');
	//在tr后边添加 一行 ： 文件展示方式，没有trFileDisplay，添加一个；有责
	var trHtml = '<tr id="trFileDisplay" style="display:none">';
	 	trHtml+= '<th width="120">';
	 	trHtml+= '是否列表方式:';
	 	trHtml+= '</th>';
	 	trHtml+= '<td colspan="3">';
	 	trHtml+= '<label><input id="fileDisplay" name="fileDisplay" type="checkbox" title="勾选时：展示方式为表格形式(点击更多，展示表格)；"></label>';
	 	trHtml+= '</td></tr>';
	tbodyObj.append($(trHtml));
	if(field){
		var cltStr = field.ctlProperty;
		var cltObj = $.parseJSON(cltStr);
		var clt = field.controlType;
		if(clt=="9"){
			$('#trFileDisplay').show();
		}
		if(!$.isEmpty(cltObj.fileDisplay)&&cltObj.fileDisplay=="list"){
			$('input#fileDisplay').attr("checked",true);
		}
	}
}