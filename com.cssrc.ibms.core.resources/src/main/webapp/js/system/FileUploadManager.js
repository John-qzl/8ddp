FileUploadManager = Ext.extend(Ext.Window, {
	constructor : function(a) {
		Ext.applyIf(this, a);
		this.fileType = this.permitted_extensions;
		this.isImage = this.judgeImage(this.fileType);
		this.initUIComponent();
		FileUploadManager.superclass.constructor.call(this, {
					id : "fileUploadManager",
					layout : "fit",
					title : this.isImage == true ? "图片分类管理" : "附件分类管理",
					iconCls : "menu-file",
					width : 720,
					minWidth : 720,
					height : 550,
					minHeight : 550,
					maximizable : true,
					border : false,
					modal : true,
					items : [this.panel],
					buttonAlign : "center",
					buttons : [{
								text : "确定",
								iconCls : "btn-ok",
								scope : this,
								handler : this.submit
							}, {
								text : "取消",
								iconCls : "btn-cancel",
								scope : this,
								handler : this.close
							}]
				});
	},
	initUIComponent : function() {
		this.panel = new Ext.Panel({
					iconCls : "menu-find-doc",
					layout : "border",
					region : "center",
					border : false
				});
		this.treePanel = new Orient.ux.TreePanelEditor({
			region : "west",
			title : this.isImage == true ? "图片分类" : "附件分类",
			collapsible : true,
			autoScroll : true,
			split : true,
			width : 180,
			url : __ctxPath
					+ "/oa/system/sysFileFolder/tree.do?catKey=ATTACHFILE_TYPE&userId="
					+ curUserInfo.userId,
			scope : this,
			onclick : this.nodeClick
		});
		this.treePanel.on("contextmenu", this.onContextmenu, this);
		this.panel.add(this.treePanel);
		this.topbar = new Ext.Toolbar({
					height : 30,
					defaultType : "button",
					items : ["-", {
								text : "flex版上传",
								iconCls : "btn-upload",
								handler : this.flexUploadFile,
								scope : this
							}, {
								xtype : "hidden",
								id : "fileIsImage",
								value : this.isImage
							}, "-", {
								xtype : "label",
								id : "fileType",
								text : "总分类"
							}, "-", {
								xtype : "hidden",
								id : "fileCat",
								value : this.file_cat == null
										? "others"
										: this.file_cat
							}]
				});
		 
		if (!this.isImage) {
			this.gridPanel = new Orient.GridPanel({
						id : "fileUploadManagerGridPanel",
						region : "center",
						tbar : this.topbar,
						rowActions : true,
						url : __ctxPath + "/oa/system/sysFileFolder/treeNodeFile.do",
						baseParams : {
							userId :curUserInfo.userId
						},
						fields : [{
									name : "fileId",
									type : "int"
								}, {
									name : "filename",
									mapping : "filename"
								}, "ext", "note", "fileType", "filePath",
								"createtime", "totalBytes"],
						columns : [{
									header : "fileId",
									dataIndex : "fileId",
									hidden : true
								}, {
									header : "fileType",
									dataIndex : "fileType",
									hidden : true
								}, {
									header : "filePath",
									dataIndex : "filePath",
									hidden : true
								}, {
									header : "附件名称",
									dataIndex : "filename"
								}, {
									header : "上传时间",
									dataIndex : "createtime",
									format : "y-m-d"
								}, {
									header : "大小",
									dataIndex : "note"
								}, new Ext.ux.grid.RowActions({
											header : "管理",
											width : 80,
											actions : [{
														iconCls : "btn-showDetail",
														qtip : "查看",
														style : "margin:0 3px 0 3px"
													}, {
														iconCls : "btn-download",
														qtip : "下载",
														style : "margin:0 3px 0 3px"
													}],
											listeners : {
												scope : this,
												"action" : this.onRowAction
											}
										})],
						listeners : {
							scope : this,
							"rowdblclick" : this.onRowdblclick
						}
					});
			this.panel.add(this.gridPanel);
		} else {
			this.imageStore = new Ext.data.JsonStore({
						id : "id",
						url : __ctxPath + "/oa/system/sysFileFolder/treeNodeFile.do",
						baseParams : {
							userId :curUserInfo.userId,
							type:'image'
						},
						root : "result",
						totalProperty : "totalCounts",
						fields : [{
									name : "fileId",
									type : "int"
								}, {
									name : "filename",
									mapping : "filename"
								}, {
									name : "filePath",
									mapping : "filePath"
								}]
					});
			this.imageStore.load({
						params : {
							start : 0,
							limit : 10
						}
					});
			this.tpl = new Ext.XTemplate(
					'<tpl for=".">',
					'<div style="width:105px; height : 105px;" class="thumb-wrap" id="{fileId}">',
					'<img align="middle" src="'
							+ __ctxFilePath
							+ '{filePath}" style="width:90px;height:90px;margin-left:7px;" title="{filename}"/>',
					'<center><span style="margin-top:3px;">{filename}</span><center>',
					"</div>", "</tpl>"), this.dataView = new Ext.DataView({
						id : "fileUploadManagerDataView",
						layout : "form",
						region : "center",
						store : this.imageStore,
						tpl : this.tpl,
						multiSelect : true,
						overClass : "x-view-over",
						itemSelector : "div.thumb-wrap",
						bodyStyle : "padding:4px",
						emptyText : "目前尚无记录",
						listeners : {
							"dblclick" : {
								fn : this.imageDbClick.createCallback(this),
								scope : this
							}
						}
					});
			this.dataPanel = new Ext.Panel({
						layout : "form",
						region : "center",
						tbar : this.topbar,
						layout : "fit",
						defaults : {
							anchor : "96%,96%"
						},
						items : this.dataView,
						bbar : new Ext.PagingToolbar({
									pageSize : 10,
									store : this.imageStore,
									displayInfo : true,
									displayMsg : "当前显示从{0}至{1}， 共{2}条记录",
									emptyMsg : "当前没有记录"
								})
					});
			this.panel.add(this.dataPanel);
		}
		this.panel.doLayout();
	},
	judgeImage : function(b) {
		if (!Ext.isEmpty(b)) {
			for (var a = 0; a < b.length; a++) {
				var c = b[a].toLowerCase();
				if (c == "bmp" || c == "png" || c == "jpeg" || c == "jpg"
						|| c == "tiff" || c == "gif") {
					return true;
				}
			}
		}
		return false;
	},
	nodeClick : function(c) {
		if (c != null) {
			var a = "";
			if (c.getDepth() > 1 && c.attributes.nodeKey != "file-type") {
				a = c.attributes.nodeKey;
			}
			var d = "";
			var b = "";
			c.bubble(function(e) {
						if (e.text != undefined) {
							if (e.text == "总分类") {
								d = "总分类/" + d;
							} else {
								d = e.text + "/" + d;
								b = e.attributes.nodeKey + "/" + b;
							}
						}
					});
			d = d.substring(0, d.length - 1);
			Ext.getCmp("fileType").setText("/" + d);
			b = b.substring(0, b.length - 1);
			Ext.getCmp("fileCat").setValue(b);
			Ext.getCmp("fileCat").setValue(a);
			this.reloadView(a);
		}
	},
	reloadView : function(a) {
		if (a != null && a == "others") {
			a = null;
		}
		if (this.isImage) {
			var b = this.dataView.getStore();
			b.url = __ctxPath + "/oa/system/sysFileFolder/tree.do?type=image&catKey=ATTACHFILE_TYPE&userId="
					+ curUserInfo.userId;
			b.reload({
						params : {
							start : 0,
							limit : 10,
							type : "image",
							fileType : a
						}
					});
		} else {
			var b = this.gridPanel.getStore();
			b.url = __ctxPath + "/oa/system/sysFileFolder/tree.do?catKey=ATTACHFILE_TYPE&userId="
					+ curUserInfo.userId;
			 b.reload({
					params : {
						start : 0,
						limit : 25,
						type : "file",
						"folderId" : a.id,
						"userId" :   curUserInfo.userId
					}
				});
		}
	},
	onContextmenu : function(a, b) {
		if (a.attributes.isPublic == "false" || a.id == "0") {
			this.selectedNode = new Ext.tree.TreeNode({
						id : a.id,
						text : a.text
					});
			var c = new Ext.menu.Menu({
						items : []
					});
			c.clearMons();
			c.add({
						text : "新增",
						iconCls : "btn-add",
						scope : this,
						handler : this.addNode
					});
			if (a.id > 0) {
				c.add({
							text : "修改",
							iconCls : "btn-edit",
							scope : this,
							handler : this.editNode
						}, {
							text : "删除",
							iconCls : "btn-del",
							scope : this,
							handler : this.delNode
						});
			}
			c.showAt(b.getXY());
		}
	},
	addNode : function() {
		var a = this.treePanel;
		var c = this.selectedNode.id;
		seajs.use("ibmsJs/js/system/SysFileFolderForm",function(SysFileFolderForm){
	 	    		sysFileFolderForm = SysFileFolderForm.init({
					       pid : c,
					       scope : this,
						   callback : function() {
								 a.root.reload();
							}
					     });
					 sysFileFolderForm.show();
	 		    	});
		 
	},
	editNode : function() {
		if (!Ext.isEmpty(this.selectedNode)) {
			var b = this.treePanel;
			var a = this.selectedNode.id;
			seajs.use("ibmsJs/js/system/SysFileFolderForm",function(SysFileFolderForm){
	 	    		sysFileFolderForm = SysFileFolderForm.init({
					       pid : a,
					       scope : this,
						   callback : function() {
								 b.root.reload();
							}
					     });
					 sysFileFolderForm.show();
	 		    	});
		}
	},
	delNode : function() {
		if (!Ext.isEmpty(this.selectedNode)) {
			var b = this.treePanel;
			var a = this.selectedNode.id;
			Ext.Msg.confirm("操作提示", "你确定删除该数据?", function(c) {
				if (c == "yes") {
					Ext.Ajax.request({
						url : __ctxPath + "/oa/system/sysFileFolder/delete.do",
						params : {
									id : a
								},
						success : function(d, f) {
							var e = Ext.util.JSON.decode(d.responseText);
							if (e.success == false) {
								Ext.ux.Toast.msg("操作提示", e.message);
							} else {
								Ext.ux.Toast.msg("操作提示", "删除成功!");
							}
							b.root.reload();
						},
						failure : function(d, e) {
						}
					});
				}
			}, this);
		} else {
			Ext.ux.Toast.msg("请选择对应数据！", "操作提示");
		}
	},
	onRowAction : function(c, a, d, e, b) {
		switch (d) {
			case "btn-showDetail" :
				this.showDetail(a.data.fileId);
				break;
			case "btn-download" :
				this.downLoad(a.data.fileId);
				break;
			default :
				break;
		}
	},
	onRowdblclick : function(a, c, b) {
		a.getSelectionModel().each(function(d) {
					this.showDetail(d.data.fileId);
				}, this);
	},
	showDetail : function(a) {
		FileAttachDetail.show(a);
	},
	downLoad : function(a) {
		var vars="top=5,left=5,height="+10+",width="+10+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";	            
		window.open(__ctxPath + "/oa/system/sysFile/download.do?fileId=" + a,"",vars);
	},
	flexUploadFile : function() {
		var d = this.treePanel;
		var c = d.getSelectionModel().getSelectedNode();
		var b = 0;
		if (c != null && c.id > 0) {
			b = c.id;
		}
		var a = Ext.getCmp("fileCat").value;
		if (Ext.isEmpty(a)) {
			a = this.file_cat != null ? this.file_cat : "others";
		}
		new FlexUploadDialog({
					file_cat : a,
					fileTypeId : b,
					scope : this,
					callback : function() {
						this.reloadView(a);
					}
				}).show();
	},
	upLoadFile : function() {
		var c = this.callback;
		var a = Ext.getCmp("fileCat").value;
		if (Ext.isEmpty(a)) {
			a = this.file_cat != null ? this.file_cat : "others";
		}
		var b = new Ext.ux.UploadDialog.Dialog({
					file_cat : a,
					url : this.url,
					scope : this,
					callback : function(d) {
						if (d != null && d.length > 0) {
							this.reloadView(this.file_cat);
							if (c != null) {
								c.call(this, d);
							}
						}
					}
				});
		b.show("queryWindow");
	},
	submit : function() {
		if (this.dialog) {
			this.dialog.close();
		}
		var d = this.scope ? this.scope : this;
		var b = null;
		if (this.isImage) {
			b = this.dataView.getSelectedRecords();
		} else {
			b = this.gridPanel.getSelectionModel().getSelections();
		}
		var a = new Array();
		if (b != null && b.length > 0) {
			for (var c = 0; c < b.length; c++) {
				a.push(b[c].data);
			}
		}
		if (this.callback != null) {
			this.callback.call(d, a);
		}
		this.close();
	},
	imageDbClick : function(b) {
		var a = b.dataView.getSelectedNodes();
		if (a != "" && a != null && a != "undefined") {
			new FileUploadImageDetail({
						fileId : a[0].id
					}).show();
		}
	}
});