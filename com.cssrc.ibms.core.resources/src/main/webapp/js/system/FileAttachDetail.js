Ext.ns("FileAttachDetail");
FileAttachDetail.show = function(a) {
	return new FileAttachDetailDialog({
				fileId : a
			}).show();
};
FileAttachDetailDialog = Ext.extend(Ext.Window, {
			formPanel : null,
			displayPanel : null,
			constructor : function(a) {
				Ext.apply(this, a);
				this.initComponents();
				FileAttachDetailDialog.superclass.constructor.call(this, {
							id : "FileAttachDetailWin",
							title : "附件详细信息",
							layout : "fit",
							modal : true,
							iconCls : "menu-attachment",
							items : this.displayPanel,
							autoHeight : true,
							autoScroll : true,
							maximizable : true,
							width : 600,
							buttonAlign : "center",
							buttons : this.buttons
						});
			},
			initComponents : function() {
				this.displayPanel = new Ext.Panel({
							flex : 1,
							id : "FileAttachDetailPanel",
							autoScroll : true,
							border : false,
							autoLoad : {
								url : __ctxPath + "/oa/system/sysFile/detail.do?fileId="
										+ this.fileId
							}
						});
				this.buttons = [{
							text : "取消",
							iconCls : "btn-cancel",
							scope : this,
							handler : this.cancel
						}];
			},
			cancel : function() {
				this.close();
			}
		});