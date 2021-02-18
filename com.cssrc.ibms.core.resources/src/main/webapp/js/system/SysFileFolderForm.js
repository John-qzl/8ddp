define(function(require, exports, module) { 
	exports.init = function(params) {
		var panel = new SysFileFolderForm(params);
		return panel;
	}
	SysFileFolderForm = Ext.extend(Ext.Window, {
		constructor : function(a) {
			Ext.applyIf(this, a);
			this.initUIComponents();
			SysFileFolderForm.superclass.constructor.call(this, {
						layout : "fit",
						items : this.formPanel,
						modal : true,
						id : "SysFileFolderFormWin",
						title : "文件夹节点信息",
						iconCls : "menu-role",
						width : 500,
						height : 300,
						buttonAlign : "center",
						buttons : this.buttons
					});
		},
		initUIComponents : function() {
			var a = '<font style="color: red;">*</font>';
			this.formPanel = new Ext.FormPanel({
						layout : "form",
						border : false,
						bodyStyle : "padding:5px;",
						defaults : {
							anchor : "96%,96%"
						},
						defaultType : "textfield",
						items : [{
									name : "id",
									xtype : "hidden",
									value : this.id == null ? "" : this.id
								},{
									name : "pid",
									xtype : "hidden",
									value : this.pid == null ? "" : this.pid
								},{
									name : "path",
									xtype : "hidden",
									value : this.path == null ? "" : this.path
								},{
									name : "depth",
									xtype : "hidden",
									value : this.depth == null ? "" : this.depth
								},{
									name : "creatorId",
									xtype : "hidden",
									value : this.creatorId == null ? "" : this.creatorId
								},{
									name : "createtime",
									xtype : "hidden",
									value : this.createtime == null ? "" : this.createtime
								},{
									name : "updateId",
									xtype : "hidden",
									value : this.updateId == null ? "" : this.updateId
								},{
									name : "delflag",
									xtype : "hidden",
									value : this.delflag == null ? 0 : this.delflag
								},{
									name : "sharedNode",
									xtype : "hidden",
									value : this.sharedNode == null ? 0 : this.sharedNode
								},{
									name : "systemNode",
									xtype : "hidden",
									value : this.systemNode == null ? 0 : this.systemNode
								}, {
									fieldLabel : "文件夹名称" + a,
									name : "name",
									allowBlank : false
								}, {
									fieldLabel : "描述",
									xtype : "textarea",
									name : "notes"
								}]
					});
			if (this.id != null && this.id != "undefined") {
				this.formPanel.loadData({
					url : __ctxPath + "/oa/system/sysFileFolder/get.do?id=" + this.id, 
					root : "data"
				});
			} 
			var self = this;
			this.buttons = [{
						text : "保存",
						iconCls : "toolbarsave",
						scope : this,
						handler : this.save
					}, {
						text : "取消",
						iconCls : "toolbarcancel",
						scope : this,
						handler : function() {
							this.close();
						}
					}];
		},
		save : function() {
		 	$postForm({
						formPanel : this.formPanel,
						waitMsg : "正在提交数据...",
						scope : this,
						url : __ctxPath + "/oa/system/sysFileFolder/save.do?userId=" +  curUserInfo.userId,
						params : {
							isCopy : this.isCopy
						},
						callback : function(a, b) {
							if (this.callback) {
								this.callback.call(this.scope);
							}
							this.close();
						}
					}); 
		}
	});
});