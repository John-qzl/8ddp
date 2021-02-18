Ext.ns("FileUploadImageDetail");
FileUploadImageDetail = Ext.extend(Ext.Window, {
			constructor : function(a) {
				Ext.apply(this, a);
				this.initComponents();
				FileUploadImageDetail.superclass.constructor.call(this, {
							name : "FileUploadImageDetail",
							width : 600,
							heigth : 500,
							modal : true,
							autoScroll : true,
							maximizable : true,
							title : "图片详细信息",
							iconCls : "menu-file",
							layout : "form",
							region : "center",
							buttonAlign : "center",
							buttons : this.buttons,
							items : this.displayPanel
						});
			},
			initComponents : function() {
				this.displayPanel = new Ext.Panel({
							flex : 1,
							id : "FileAttachDetailPanel",
							autoScroll : true,
							border : false,
							autoLoad : {
								url : __ctxPath
										+ "/system/fileAttachDetail.do?fileId="
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