define(function(require, exports, module) {
	exports.init = function(params) {
		var panel = new FileAttachView(params);
		return panel;
	}
	FileAttachView = Ext.extend(Ext.Panel, {
	constructor : function(a) {
		Ext.applyIf(this, a);
		this.initUIComponent();
		FileAttachView.superclass.constructor.call(this, {
					id : "FileAttachView",
					title : a.text,
					iconCls : a.iconCls,
					layout : "border",
					region : "center",
					autoScroll : true,
					items : [this.leftPanel, this.outPanel]
				});
	},
	initUIComponent : function() {
		this.treePanel = new Orient.ux.TreePanelEditor({
					url : __ctxPath
							+ "/system/treeGlobalType.do?catKey=ATTACHFILE_TYPE",
					scope : this,
					autoScroll : true,
					onclick : this.nodeClick,
					enableDD : true
				});
		this.leftPanel = new Ext.Panel({
					region : "west",
					title : "附件分类",
					layout : "fit",
					collapsible : true,
					split : true,
					border : false,
					width : 200,
					items : [this.treePanel]
				});
		this.searchPanel = new Orient.SearchPanel({
					layout : "form",
					region : "north",
					colNums : 7,
					keys : [{
								key : Ext.EventObject.ENTER,
								fn : this.search,
								scope : this
							}, {
								key : Ext.EventObject.ESC,
								fn : this.reset,
								scope : this
							}],
					labelWidth : 50,
					items : [{
								xtype : "textfield",
								fieldLabel : "文件名",
								name : "Q_fileName_S_LK",
								width : 80,
								maxLength : 125
							}, {
								xtype : "datefield",
								fieldLabel : "创建时间",
								name : "Q_createtime_D_GE",
								format : "Y-m-d H:i:s",
								labelWidth : 60,
								width : 130,
								maxLength : 125
							}, {
								xtype : "datefield",
								fieldLabel : "至：",
								name : "Q_createtime_D_LE",
								format : "Y-m-d H:m:s",
								labelWidth : 20,
								width : 130,
								maxLength : 125
							}, {
								xtype : "textfield",
								fieldLabel : "扩展名",
								name : "Q_ext_S_LK",
								labelWidth : 50,
								width : 80,
								maxLength : 125
							}, {
								xtype : "textfield",
								fieldLabel : "上传者",
								name : "Q_creator_S_LK",
								labelWidth : 50,
								width : 80,
								maxLength : 125
							}, {
								xtype : "button",
								text : "查询",
								style : "padding-left:5px;padding-right:5px;",
								iconCls : "search",
								handler : this.search,
								scope : this
							}, {
								xtype : "button",
								text : "重置",
								style : "padding-left:5px;padding-right:5px;",
								iconCls : "reset",
								handler : this.reset,
								scope : this
							}]
				});
		this.gridPanel = new Orient.GridPanel({
					region : "center",
					tbar : [{
								iconCls : "btn-upload",
								text : "上传附件",
								xtype : "button",
								scope : this,
								handler : this.uploadFile
							}, {
								iconCls : "btn-upload",
								text : "上传图片",
								xtype : "button",
								scope : this,
								handler : this.uploadImage
							}],
					rowActions : true,
					url : __ctxPath + "/system/listAllFileAttach.do",
					fields : [{
								name : "fileId",
								type : "int"
							}, "fileName", "filePath", "createtime", "ext",
							"type", "note", "creator", "fileType",
							"globalType", "delFlag"],
					columns : [{
								header : "fileId",
								dataIndex : "fileId",
								hidden : true
							}, {
								header : "文件名",
								dataIndex : "fileName"
							}, {
								header : "文件路径",
								dataIndex : "filePath"
							}, {
								header : "创建时间",
								dataIndex : "createtime"
							}, {
								header : "扩展名",
								dataIndex : "ext"
							}, {
								header : "附件类型",
								dataIndex : "fileType"
							}, {
								header : "类型名称",
								sortable : false,
								dataIndex : "globalType",
								renderer : function(a) {
									if (a != null) {
										return a.typeName;
									} else {
										return "";
									}
								}
							}, {
								header : "说明",
								dataIndex : "note"
							}, {
								header : "上传者",
								dataIndex : "creator"
							}, {
								header : "状态",
								dataIndex : "delFlag",
								renderer : function(a) {
									if (a) {
										if (a == 1) {
											return '<font color="red">已删除</font>';
										} else {
											return '<font color="green">可用</font>';
										}
									} else {
										return "";
									}
								}
							}, new Ext.ux.grid.RowActions({
										header : "管理",
										width : 100,
										actions : [{
													iconCls : "btn-detail",
													qtip : "查看",
													style : "margin:0 3px 0 3px"
												}],
										listeners : {
											scope : this,
											"action" : this.onRowAction
										}
									})]
				});
		this.outPanel = new Ext.Panel({
					region : "center",
					title : "附件列表",
					layout : "border",
					items : [this.searchPanel, this.gridPanel]
				});
	},
	detail : function(a) {
		if (a != null && a != "" && a != "undefined") {
			FileAttachDetail.show(a);
		}
	},
	search : function() {
		$search({
					searchPanel : this.searchPanel,
					gridPanel : this.gridPanel
				});
	},
	reset : function() {
		this.searchPanel.getForm().reset();
	},
	uploadFile : function() {
		var b = this.treePanel.selectedNode;
		var a = null;
		if (!Ext.isEmpty(b)) {
			if (b.getDepth() > 1 && b.attributes.nodeKey != "file-type") {
				a = b.attributes.nodeKey;
			}
		}
		App.createUploadDialog({
					file_cat : a,
					scope : this,
					callback : function(c) {
						this.search;
					}
				}).show();
	},
	uploadImage : function() {
		var b = this.treePanel.selectedNode;
		var a = null;
		if (!Ext.isEmpty(b)) {
			if (b.getDepth() > 1 && b.attributes.nodeKey != "file-type") {
				a = b.attributes.nodeKey;
			}
		}
		App.createUploadDialog({
			file_cat : a,
			permitted_extensions : ["jpg", "gif", "jpeg", "png", "bmp", "JPG",
					"GIF", "JPEG", "PNG", "BPM"],
			scope : this,
			callback : function(c) {
				this.search;
			}
		}).show();
	},
	removeAll : function() {
		$delGridRs({
					url : __ctxPath + "/system/multiDelFileAttach.do",
					grid : this.gridPanel,
					idName : "fileId"
				});
	},
	remove : function(a) {
		$postDel({
					url : __ctxPath + "/system/multiDelFileAttach.do",
					ids : a,
					grid : this.gridPanel
				});
	},
	nodeClick : function(b) {
		if (b != null) {
			var a = "";
			if (b.getDepth() > 1 && b.attributes.nodeKey != "file-type") {
				a = b.attributes.nodeKey;
			}
			this.gridPanel.getStore().reload({
						params : {
							"Q_fileType_S_LK" : a
						}
					});
		}
	},
	onRowAction : function(c, a, d, e, b) {
		switch (d) {
			case "btn-detail" :
				this.detail(a.data.fileId);
				break;
			case "btn-del" :
				this.remove(a.data.fileId);
				break;
			default :
				break;
		}
	}
});

});
