/**
 * 通用对话框。
 * @param alias		对话框别名。
 * @param callBack	回调函数。
 * 调用示例：
 * CommonDialog("role",function(data){
 * 
 * });
 * data:为json数组或者为json对象。
 * @param paramValueString	向对话框传递的"参数=值"字符串
 * 传入多个则需要使用"&"符号进行连接（user=admin&orgId=1）
 */

//增加选择后是否关闭参数isclose，默认不加参数直接关闭，如需保留dialog，将isclose设置为任意字符串
function CommonDialog(alias,callBack,paramValueString,isclose){
	//1毫秒内同时打开两次不允许
	if($("body").data("hasInited")) return;
	$("body").data("hasInited",true);
	window.setTimeout(function(){
		$("body").data("hasInited",false);
	},1);	
	window.__resultData__=0;
	if(alias==null || alias==undefined){
		$.ligerDialog.warn("别名为空！",'提示信息');
		return;
	}
	//将paramValueString中的参数rpcrefname进行传输。
	var url=__ctx + "/oa/form/formDialog/dialogObj.do?alias=" +alias+ "&" + encodeURI(paramValueString);
	url=url.getNewUrl();
	$.post(url,{"alias":alias},function(data){
		if(data.success==0){
			$.ligerDialog.warn("输入别名不正确！",'提示信息');
			return;
		}
		var obj=data.bpmFormDialog;
		var width=obj.width;
		var name=obj.name;
		var height=obj.height;
		var displayList=obj.displayfield.trim();
		var resultfield=obj.resultfield.trim();
		if( displayList==""){
			$.ligerDialog.warn("没有设置显示字段！",'提示信息');
			return;
		}
		if( resultfield==""){
			$.ligerDialog.warn("没有设置结果字段！",'提示信息');
			return;
		}
		//直接跳转到/view/oa/form/formDialogShow.jsp页面,然后执行这个页面的 showFrame.do 方法,代码在FormDialogController.java中
		var urlShow=__ctx + "/oa/form/formDialog/show.do?dialog_alias_=" +alias;
		if(!paramValueString==false){
			urlShow = urlShow + "&" + encodeURI(paramValueString) ;
		}
		urlShow=urlShow.getNewUrl();
		var frameDialogWindow = "";
		window.top.__resultData__=0;
		window.top.$.ligerDialog.open({ url:urlShow, height: height,width: width, title :name,name:"frameDialog_",
			buttons: [ { text: '确定', onclick: function (item, dialog) { 
					var result = window.top.__resultData__;
					//在formDialogShowFrame.jsp中给__resultData__赋值 :parent.parent.__resultData__=getResult();
					if(result==-1 || result==0){
						alert("还没有选择数据项！");
						return;
					}
					if(callBack){
						//调用CommonDialog.js的第二个参数callBack
						callBack(result);
					}
					if(!isclose){
						dialog.close();
					}
					//dialog.close();
			} }, 
			{ text: '重置', onclick: function (item, dialog) { 
				frameDialogWindow.resetMethod();
			} }, 
			{ text: '取消', onclick: function (item, dialog) { dialog.close(); } } ] });
		frameDialogWindow = window.top.$('[name="frameDialog_"]')[0].contentWindow;
	});
};
/**
 * 调用通用表单查询
 * @param alias 查询别名
 * @param callback 回调函数
 */
function CommonQuery(alias){
	window.__queryData__ = [];
	if(alias==null || alias==undefined){
		$.ligerDialog.warn("别名为空！",'提示信息');
		return;
	}
	var url=__ctx + "/oa/form/formQuery/queryObj.do?alias=" +alias;
	url=url.getNewUrl();
	$.post(url,{"alias":alias},function(data){
		if(data.success==0){
			$.ligerDialog.warn("输入别名不正确！",'提示信息');
			return;
		}
		var obj=data.bpmFormQuery;
		var name=obj.name;
		var conditionfield=obj.conditionfield.trim();
		var resultfield=obj.resultfield.trim();
		
		if( conditionfield==""){
			$.ligerDialog.warn("没有设置条件字段！",'提示信息');
			return;
		}
		if( resultfield==""){
			$.ligerDialog.warn("没有设置结果字段！",'提示信息');
			return;
		}
		
		var urlShow=__ctx + "/oa/form/formQuery/get.do?query_alias_=" +alias;
		urlShow=urlShow.getNewUrl();
		$.ligerDialog.open({ url:urlShow, height: 380,width: 600, isResize: true, title :name,name:"frameQuery_",
			buttons: [{ text: '关闭', onclick: function (item, dialog) { dialog.close(); } } ] });
	});
};

/** 
 * 执行查询
 * @param alias 查询别名
 * @param condition 查询条件
 * @param callback 查询完成后的回调
 * @param isSync 是否同步执行。
 */
function DoQuery(condition,callback,isSync){
	var url = __ctx + "/oa/form/formQuery/doQuery.do";
	
	var isAsync=true;
	if(isSync!=undefined && isSync==true){
		isAsync=false;
	}
	
	$.ajax({
		   type: "POST",
		   url: url,
		   async:isAsync,
		   data: condition,
		   success: function(data){
			   if(callback)
					callback(data);
		   }
	});
	
};