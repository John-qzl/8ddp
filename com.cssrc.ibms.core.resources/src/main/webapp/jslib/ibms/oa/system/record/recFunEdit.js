/**
 * 数据模板构造方法
 */
var RecFunEdit = function() {
	this.index=100;
}
// 属性及函数
RecFunEdit.prototype = {
	/**
	 * 页面初始化,按钮表单的相关function
	 */
	init : function() {
		var _self = this;
		this.initButtonArr();	
		//index值修正
		var btr = $('#buttonItem tr[var=buttonTr]');
		if(btr.length>0){
			_self.index = $(btr.get(btr.length-1)).find('input[var=unique]').val();
		}
	},
	/*---------------初始化 ---------------*/
	initButtonArr : function(){
		var buttonArrInfo = $('#buttonArr').val();
		var buttonArr = jQuery.parseJSON(buttonArrInfo);
		if($.isEmpty(buttonArr)){
			return;
		}
		for(var i=0;i<buttonArr.length;i++){
			var button = buttonArr[i];
			this.appendButtonTr(i,button);
		}
	},
	/*---------------表单的按钮函数管理 ---------------*/
	//业务数据模板按钮初始化
	setDataTemplateButtonInfo : function(){
		var url = ctx+'/oa/system/recFun/getDataTemplateButton.do?';
		var result = this.validUrl();
		if(!result.success){
			$.ligerDialog.alert(result.message,'提示信息');
			return;
		}
		var displayId = this.getDisplayId();
		if($.isEmpty(displayId)){
			$.ligerDialog.alert("没有设置默认地址或默认地址中没有__displayId__参数！",'提示信息');
			return;
		}
		$.ajax({
			  type: "POST",
		      url:url,
			  data:{     
				  	displayId : displayId
			       },
			  dataType : "text",
		      async:false,
		      success:function(result){
		    	 		var data = jQuery.parseJSON(result);
		    	 		if(data.result==1){
		    				$('#buttonItem tbody').children().remove();
		    	 			var buttonArr = jQuery.parseJSON(data.message);
		    	 			for(var i=0;i<buttonArr.length;i++){
		    	 				var button = buttonArr[i];
		    	 				__RecFun__.appendButtonTr(i,button);
		    	 			}
		    	 			//index值修正
		    	 			var btr = $('#buttonItem tr[var=buttonTr]');
		    	 			if(btr.length>0){
		    	 				__RecFun__.index = $(btr.get(btr.length-1)).find('input[var=unique]').val();
		    	 			}
		    	 			$.ligerDialog.success("按钮初始化成功",'提示信息');
		    	 		}else{
		    	 			$.ligerDialog.error(data.message,'提示信息');
		    	 		}
			        },
			  error:function(XMLHttpRequest,textStatus,errorThrown){			    	 
		      	$.ligerDialog.success("getRefDataValue请求错误！",'提示信息');
			  }
		});
	},
	getDisplayId : function (){
		var url = $('#defaultUrl').val();
		return this.getUrlParam(url,'__displayId__');
	},
	validUrl : function(){
		var result = {};
		var url = $('#defaultUrl').val();
		if(url.indexOf("dataTemplate/preview.do")>-1){
			result.type = "preview";
			result.success = true;
			result.message = "";
		}else if(url.indexOf("dataTemplate/detailData.do")>-1){
			result.type = "detailData";
			result.success = false;
			result.message = "非业务数据模板页面，请使用【新增定制按钮】！";
		}else{
			result.type = "define";
			result.success = false;
			result.message = "非业务数据模板页面，请使用【新增定制按钮】！";
		}
		return result;
	},
	getUrlParam : function(url,name){
		url = url.trim().replace('?','&');
		var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
		var r = url.match(reg);
		if(r != null) return unescape(r[2]);
		return null;
	},
	appendButtonTr :function(i,button){
		var tr = $($("#buttonTemplate .table-detail tr")[0]).clone(true,
				true);
		$("[var='no']", tr).text(1+i);
		$("[var='unique']", tr).val(button.unique);
		$("[var='desc']", tr).val(button.desc);
		$("[var='name']", tr).val(button.name);
		$('#buttonItem tbody').append(tr);
	},
	singleDelete : function (obj){
		$(obj).parent().parent().remove();
	},
	addButton: function(){
		this.index++;
		var tr = $($("#buttonTemplate .table-detail tr")[1]).clone(true,true);
		$("[var='no']", tr).text(this.index-100);
		$("[var='unique']", tr).val(this.index);
		$('#buttonItem tbody').append(tr);
	}
};

var __RecFun__ = new RecFunEdit();// 默认生成一个对象

