Orient.AttachPanel = Ext.extend(Ext.Panel, {
	constructor : function(a) {
		Ext.applyIf(this, a);
		this.scope = this.scope ? this.scope : this;
		this.initUI();
		Orient.AttachPanel.superclass.constructor.call(this, {
					layout : "hbox",
					border : false,
					width : this.leftWidth ? this.leftWidth + 250 : 650,
					layoutConfig : {
						padding : "5 5 5 0",
						align : "top"
					},
					autoHeight : true,
					defaults : {
						margins : "0 5 0 0"
					},
					items : [this.attachPanel, {
								xtype : "button",
								iconCls : "menu-attachment",
								scope : this,
								handler : this.addFile,
								text : "添加"
							}, {
								xtype : "button",
								iconCls : "reset",
								scope : this,
								text : "清除所选",
								handler : this.clearSelectedFiles
							}, {
								xtype : "button",
								iconCls : "btn-cancel",
								scope : this,
								handler : this.clearFile,
								text : "清除所有"
							}]
				});
	},
	initUI : function() {
		this.attachPanel = new Orient.GridPanel({
					url : __ctxPath + "/oa/system/sysFile/loadByIds.do",
					root : this.root ? this.root : "fileAttachs",
					fields : [{
								type : "int",
								name : "fileId"
							}, "filename"],
					bodyStyle : "padding:4px 4px 4px 0px",
					name : "fileAttachPanel",
					autoHeight : true,
					showPaging : false,
					isShowTbar : false,
					rowActions : true,
					width : this.leftWidth ? this.leftWidth : 400,
					hideHeaders : true,
					columns : [{
								header : "fileId",
								hidden : true,
								dataIndex : "fileId"
							}, {
								header : "filename",
								dataIndex : "filename",
								renderer : function(c, b, a) {
									return '<a href="' + __ctxPath
											+ "/file-download?fileId="
											+ a.data.fileId
											+ '", target="_blank">' + c
											+ "</a>";
								}
							}, new Ext.ux.grid.RowActions({
										header : "管理",
										width : 100,
										actions : [{
													iconCls : "btn-del",
													qtip : "删除",
													style : "margin:0 3px 0 3px"
												}],
										listeners : {
											scope : this,
											"action" : this.onRowAction
										}
									})]
				});
		this.attachPanel.addListener("rowdblclick", this.rowClick);
	},
	addFile : function() {
		var a = this.attachPanel;
		var b = this;
		App.createUploadDialog({
					file_cat : this.fileCat ? this.fileCat : "",
					scope : this.scope,
					callback : function(f) {
						var c = a.getStore();
						var d = a.getStore().recordType;
						for (var e = 0; e < f.length; e++) {
							var g = new d();
							g.set("fileId", f[e].fileId);
							g.set("filename", f[e].filename);
							g.commit();
							c.insert(c.getCount(), g);
						}
						a.getView().refresh();
						b.doLayout();
					}
				}).show();
	},
	clearFile : function() {
		this.attachPanel.getStore().removeAll();
		this.attachPanel.getView().refresh();
		this.fileIds = [];
		this.fileNames = [];
		this.doLayout();
	},
	clearSelectedFiles : function() {
		var a = this.attachPanel.getStore();
		var b = this.attachPanel.getSelectionModel().getSelections();
		for (var c = 0; c < b.length; c++) {
			a.remove(b[c]);
		}
		this.attachPanel.getView().refresh();
		this.doLayout();
	},
	rowClick : function(b, a, c) {
		b.getSelectionModel().each(function(d) {
					FileAttachDetail.show(d.data.fileId);
				}, this);
	},
	removeRs : function(a) {
		var b = this.attachPanel.getStore();
		b.remove(a);
		this.attachPanel.getView().refresh();
		this.doLayout();
	},
	onRowAction : function(c, a, d, e, b) {
		switch (d) {
			case "btn-del" :
				this.removeRs.call(this, a);
				break;
			default :
				break;
		}
	},
	getFileIds : function() {
		var b = this.attachPanel.getStore();
		var c = "";
		for (var d = 0; d < b.getCount(); d++) {
			var a = b.getAt(d);
			c += a.get("fileId") + ",";
		}
		return c;
	},
	getFileNames : function() {
		var c = this.attachPanel.getStore();
		var b = "";
		for (var d = 0; d < c.getCount(); d++) {
			var a = c.getAt(d);
			b += a.get("filename") + ",";
		}
		return b;
	},
	getAttachStore : function() {
		return this.attachPanel.getStore();
	},
	loadByResults : function(a) {
		this.attachPanel.getStore().loadData(a);
		this.attachPanel.getView().refresh();
		this.doLayout();
	},
	loadByIds : function(a) {
		this.attachPanel.getStore().load({
					params : {
						ids : a
					},
					callback : function() {
						this.doLayout();
					},
					scope : this
				});
	}
});
Ext.reg("attachpanel", Orient.AttachPanel);