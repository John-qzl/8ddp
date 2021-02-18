/**
 * 自定义对话框
 * @function
 * @name baidu.editor.execCommands
 * @param    {String}    cmdName     cmdName="customdialog"
 * 1.在 /WebRoot/jslib/ueditor2/_src/extend/ExtendConf.js中 指定了映射关系:
 * 'customdialog': '~/dialogs/extend/dialog/dialog.jsp' 
 * 2.弹出的自定义对话框jsp在  /WebRoot/jslib/ueditor2/dialogs/extend/dialog/dialog.jsp 
 */
UE.commands['customdialog'] = {	
	execCommand : function(cmdName) {
		var me=this;
		
		if(!me.ui._dialogs['customdialogDialog']){
			baidu.editor.ui['customdialog'](me);
		}
		me.ui._dialogs['customdialogDialog'].open();
	},
	queryCommandState : function() {
		return 0;
	}
}