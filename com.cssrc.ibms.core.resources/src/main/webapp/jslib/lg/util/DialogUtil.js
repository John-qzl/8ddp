/**
 * 为了ligerUi的顶层dialog显示
 */
var DialogUtil = {
	open : function(opts){
		var outerWindow =window.top;
//		var _opts = $.extend({},{outer:true},opts);
		//对参数做出限制，不会大于当前页面大小
		var _height = outerWindow.document.documentElement.clientHeight;
		var _width = outerWindow.document.documentElement.clientWidth;
		opts.height>_height?opts.height = _height:null;
		opts.width>_width?opts.width = _width:null;
		
		//majing add 关闭dialog时是否仅仅隐藏添加默认值
		opts.isHidden = opts.isHidden||false;
		
		outerWindow.$.ligerDialog.open(opts);
		outerWindow.$('.l-dialog-winbtn').addClass('abc');
		outerWindow.$('.l-dialog-icon').addClass('abc');
		setTimeout(function () {
			outerWindow.$('.l-dialog-winbtn').removeClass("abc");
			outerWindow.$('.l-dialog-icon').removeClass('abc');
		},400) 
		
		return false;
	},
	warn : function(opts){
		var outerWindow = window.top;
		outerWindow.$.ligerDialog.warn(opts);
	},
	success : function(opts){
		var outerWindow = window.top;
		outerWindow.$.ligerDialog.success(opts);
	},
	waitting : function(opts){
		var outerWindow = window.top;
		outerWindow.$.ligerDialog.waitting(opts);
	},
	closeWaitting : function(){
		var outerWindow = window.top;
		outerWindow.$.ligerDialog.closeWaitting();
	},
}