define(function(require, exports, module) {
	var SysFileFolderForm = require("ibmsJs/js/system/SysFileFolderForm");
	exports.init = function(params) {
		var panel = new AdminSysFileAttachView(params);
		return panel;
	}
	AdminSysFileAttachView = Ext.extend(Ext.Panel, {
	constructor : function(a) {
		Ext.applyIf(this, a);
		this.initUIComponent();
		AdminSysFileAttachView.superclass.constructor.call(this, {
					id : "AdminSysFileAttachView",
					layout : "border",
					region : "center",
					autoScroll : true,
					title : a.text,
					iconCls : a.iconCls,
					items : [this.leftPanel, this.outPanel]
				});
	},
	initUIComponent : function() {
		var self = this;
		
		this.organizationTreePanel = new Orient.ux.TreePanelEditor({
						url : __ctxPath + "/oa/system/sysOrg/orgUserTree.do?demId=1",
						scope : this,
						autoScroll : true,
						region :"west",
						width : 200,
					    split : true,
					    border : false,
						onclick : this.userNodeClick
					});
		
		this.treePanel = new Orient.ux.TreePanelEditor({
					url : __ctxPath
							+ "/oa/system/sysFileFolder/tree.do?userId=" + curUserInfo.userId,
					scope : this,
					autoScroll : true,
					onclick : this.nodeClick,
					enableDD : true,
					region :"center",
					width : 200,
					split : true,
					border : false
				});
		this.leftPanel = new Ext.Panel({
					region : "west",
					title : "附件列表",
					layout : "border",
					width : 400,
					split : true,
					border : false,
					items : [this.organizationTreePanel,this.treePanel]
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
								name : "Q_filename_S_LK",
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
							},{
								xtype : "hidden",
								name : "searchPanel",
								value :"searchPanel"
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
					rowActions : true,
					url :  __ctxPath + "/oa/system/sysFileFolder/treeNodeFile.do",
					baseParams : {
							userId :curUserInfo.userId
						},
					fields : [{
								name : "fileId",
								type : "int"
							}, "filename", "filepath", "createtime", "ext",
							"type", "creator", "fileType",
							"globalType", "delFlag","shared","creatorId"],
					columns : [{
								header : "fileId",
								dataIndex : "fileId",
								hidden : true
							}, {
								header : "文件名",
								dataIndex : "filename"
							}, {
								header : "文件路径",
								dataIndex : "filepath"
							}, {
								header : "创建时间",
								dataIndex : "createtime"
							}, {
								header : "扩展名",
								dataIndex : "ext"
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
										return '<font color="green">可用</font>';
									}
								}
							}, {
								header : "是否分享",
								dataIndex : "shared",
								renderer : function(a) {
									if (a) {
										if (a == 1) {
											return '<font color="red">已分享</font>';
										} else {
											return '<font color="green">未分享</font>';
										}
									} else {
										return '<font color="green">未分享</font>';
									}
								}
							}, new Ext.ux.grid.RowActions({
										header : "管理",
										width : 100,
										actions : [{
														iconCls : "btn-download",
														qtip : "下载",
														style : "margin:0 3px 0 3px"
													},
													{
														iconCls : "btn-shared",
														qtip : "分享",
														style : "margin:0 3px 0 3px",
														fn : function(a) { 
															var c = a.data.shared;
															var b = a.data.delFlag;
															if (c == 1||b == 1) {
																return false;
															}
															return true;
														}
													},
													{
														iconCls : "btn-delete",
														qtip : "删除",
														style : "margin:0 3px 0 3px",
														fn : function(a) { 
															var c = a.data.delFlag;
															var userId = curUserInfo.userId;
															if (c == 1||userId!=a.data.creatorId) {
																return false;
															}
															return true;
														}									
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
	       	var a = this.organizationTreePanel.selectedNode;
	       	var b = this.treePanel.selectedNode;
			if(a==null||a.id.indexOf("p")<0||b==null||a.id.substr(1,a.id.length)!=curUserInfo.userId){
			  Ext.ux.Toast.msg("操作信息", "请先选择自己的文件分类树");
			  return ;
			}
			new FlexUploadDialog({
					file_cat : null,
					fileTypeId : b.id,
					scope : this,
					callback : function() {
						this.reloadView(b);
					}
				}).show();
		
	},
	remove : function(a) {
		$postDel({
					url : __ctxPath + "/oa/system/sysFile/deleteFile.do?isFlex=true",
					ids : a,
					grid : this.gridPanel
				});
	},
	downLoad : function(a) { 
		var vars="top=5,left=5,height="+10+",width="+10+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
				            
		window.open(__ctxPath + "/oa/system/sysFile/download.do?fileId=" + a,"",vars);
	},
	share : function (a){
		  var b = this.gridPanel;
	   	  Ext.Msg.confirm("信息确认", "您确实要分享该文件吗", function(c) {
				if (c == "yes") {
					var url = __ctxPath + "/oa/system/sysFile/share.do";
					Ext.Ajax.request({
								url : url,
								params : {
									fileId : a
								},
								method : "POST",
								success : function(e, f) {
									var d = Ext.util.JSON.decode(e.responseText);
									if (d.success) {
										 Ext.ux.Toast.msg("操作信息", "分享成功！");
										 b.getStore().reload();
									} else {
										Ext.ux.Toast.msg("操作信息", d.message);
									}
								},
								failure : function(d, e) {
									Ext.ux.Toast.msg("操作信息", "操作出错，请联系管理员！");
								}
							});
				}
			});
	},
	nodeClick : function(b) {
	  this.reloadView(b);
	},
	onRowAction : function(c, a, d, e, b) {
		switch (d) {
			case "btn-detail" :
				this.detail(a.data.fileId);
				break;
		    case "btn-download" :
				this.downLoad(a.data.fileId);
				break;
			case "btn-delete" :
				this.remove(a.data.fileId);
				break;
			case "btn-shared" :
				this.share(a.data.fileId);
				break;
			default :
				break;
		}
	},
	reloadView : function(a) {
		var b = this.gridPanel.getStore();
		    b.url = __ctxPath + "/oa/system/sysFileFolder/treeNodeFile.do";
		    b.baseParams = {};
	        b.reload({
				params : {
					start : 0,
					limit : 25,
					type : "file",
					"folderId" : a.id,
					"userId" :   curUserInfo.userId
				}
			});
	},
	//用户节点click
	userNodeClick : function(b){
		if(b.id.indexOf("p")>=0){
		 this.treePanel.loader.url= __ctxPath + "/oa/system/sysFileFolder/tree.do?userId=" +  b.id.substr(1,b.id.length);
		 this.treePanel.root.reload();
		 var a = this.gridPanel.getStore();
		  a.url = __ctxPath + "/oa/system/sysFileFolder/treeNodeFile.do";
		  a.baseParams = {"userId" : b.id.substr(1,b.id.length)};
  		  a.reload({
			params : {
			start : 0,
			limit : 25,
			type : "file",
			"folderId" : 0,
			"userId" : b.id.substr(1,b.id.length)
		   }
		 });	
		}
	}
});
});

