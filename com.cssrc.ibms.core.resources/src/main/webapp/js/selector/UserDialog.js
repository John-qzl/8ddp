UserDialog = Ext.extend(Ext.Window, {
	constructor : function(a) {
		Ext.applyIf(this, a);
		this.scope = this.scope ? this.scope : this;
		this.single = this.single != null ? this.single : true;
		this.initUI();
		UserDialog.parent.superclass.constructor.call(this, {
					title : this.title ? this.title : "用户选择器",
					height : 450,
					width : 650,
					maximizable : true,
					modal : true,
					layout : "border",
					items : [this.westPanel, this.searchPanel, this.gridPanel],
					buttonAlign : "center",
					buttons : [{
								text : "确定",
								iconCls : "btn-ok",
								scope : this,
								handler : this.confirm
							}, {
								text : "取消",
								iconCls : "btn-cancel",
								scope : this,
								handler : this.close
							}, {
								text : "发起人",
								id : "startUser",
								iconCls : "menu-subuser",
								scope : this,
								handler : function() {
									this.callback
											.call(this, "__start", "[发起人]");
									this.close();
								},
								hidden : true
							}]
				});
		if (this.isForFlow) {
			Ext.getCmp("startUser").hidden = false;
		}
		if (!this.single) {
			this.add(this.southPanel);
			this.doLayout();
		}
	},
	orgTreeClick : function() {
		var a = this.orgTreePanel.selectedNode.id;
		var b = this.gridPanel.getStore();
		var c = null;
		if (a != 0) {
			c = {
				"Q_orgs.orgId_L_EQ" : a
			};
		} else {
			c = {};
		}
		c.start = 0;
		c.limit = b.baseParams.limit;
		b.baseParams = c;
		this.gridPanel.getBottomToolbar().moveFirst();
	},
	posTreeClick : function() {
		var a = this.posTreePanel.selectedNode.id;
		var b = this.gridPanel.getStore();
		var c = null;
		if (a != 0) {
			c = {
				"Q_positions.posId_L_EQ" : a
			};
		} else {
			c = {};
		}
		c.start = 0;
		c.limit = b.baseParams.limit;
		b.baseParams = c;
		this.gridPanel.getBottomToolbar().moveFirst();
	},
	roleTreeClick : function() {
		var b = this.rolePanel.selectedNode.id;
		var a = this.gridPanel.getStore();
		var c = null;
		if (b != 0) {
			c = {
				"Q_roles.roleId_L_EQ" : b
			};
		} else {
			c = {};
		}
		c.start = 0;
		c.limit = a.baseParams.limit;
		a.baseParams = c;
		this.gridPanel.getBottomToolbar().moveFirst();
	},
	onlineClick : function() {
		var a = this.gridPanel.getStore();
		a.proxy.conn.url = __ctxPath + "/system/onlineAppUser.do";
		a.load({
					params : {
						start : 0,
						limit : 200
					}
				});
	},
	demensionSel : function(d, a, b) {
		var c = d.getValue();
		this.orgTreePanel.loader = new Ext.tree.TreeLoader({
					baseParams : {
						demId : c
					},
					dataUrl : __ctxPath + "/system/treeOrganization.do",
					requestMethod : "GET"
				});
		this.orgTreePanel.selectedNode = null;
		this.orgTreePanel.root.reload();
	},
	initUI : function() {
		this.demStore = new Ext.data.ArrayStore({
					autoLoad : true,
					url : __ctxPath + "/system/comboDemension.do",
					fields : ["id", "name"]
				});
		this.demensionCombo = new Ext.form.ComboBox({
					displayField : "name",
					valueField : "id",
					editable : false,
					emptyText : "所有维度",
					mode : "local",
					triggerAction : "all",
					store : this.demStore,
					listeners : {
						scope : this,
						"select" : this.demensionSel
					}
				});
		this.orgTreePanel = new htsoft.ux.TreePanelEditor({
					border : false,
					url : __ctxPath + "/system/treeOrganization.do",
					region : "center",
					scope : this,
					autoScroll : true,
					onclick : this.orgTreeClick
				});
		this.posTreePanel = new htsoft.ux.TreePanelEditor({
					title : "按岗位查找",
					border : false,
					iconCls : "dep-user",
					url : __ctxPath + "/system/treePosition.do",
					scope : this,
					autoScroll : true,
					onclick : this.posTreeClick
				});
		this.orgPanel = new Ext.Panel({
					border : false,
					title : "按组织架构查找",
					iconCls : "menu-OrgSetting",
					layout : "fit",
					items : [{
								xtype : "panel",
								layout : "border",
								border : false,
								items : [{
											xtype : "panel",
											region : "north",
											border : false,
											autoHeight : true,
											layout : "fit",
											items : this.demensionCombo
										}, this.orgTreePanel]
							}]
				});
		this.rolePanel = new htsoft.ux.TreePanelEditor({
					border : false,
					title : "按角色查找",
					iconCls : "role-user",
					url : __ctxPath + "/system/treeAppRole.do",
					scope : this,
					autoScroll : true,
					onclick : this.roleTreeClick
				});
		this.onlinePanel = new Ext.Panel({
					collapsible : false,
					border : false,
					autoScroll : true,
					iconCls : "online-user",
					title : "在线人员  ",
					listeners : {
						scope : this,
						"expand" : this.onlineClick
					}
				});
		this.westPanel = new Ext.Panel({
					split : true,
					collapsible : true,
					split : true,
					region : "west",
					header : false,
					width : 185,
					layout : "accordion",
					collapsible : true,
					items : [this.orgPanel, this.posTreePanel, this.rolePanel,
							this.onlinePanel]
				});
		this.searchPanel = new HT.SearchPanel({
					region : "north",
					layout : "hbox",
					layoutConfig : {
						padding : "5",
						align : "middle"
					},
					defaults : {
						xtype : "label",
						border : false,
						margins : {
							top : 0,
							right : 4,
							bottom : 4,
							left : 4
						}
					},
					items : [{
								text : "用户姓名："
							}, {
								xtype : "textfield",
								width : 400,
								name : "Q_fullname_S_LK"
							}, {
								xtype : "button",
								iconCls : "btn-search",
								text : "查询",
								scope : this,
								handler : function() {
									$search({
												searchPanel : this.searchPanel,
												gridPanel : this.gridPanel
											});
								}
							}]
				});
		this.gridPanel = new HT.GridPanel({
					singleSelect : this.single,
					title : "可选用户",
					region : "center",
					isShowTbar : false,
					url : __ctxPath + "/system/dialogAppUser.do?curDep=true",
					fields : [{
								name : "userId",
								type : "int"
							}, "fullname", "title", "mobile"],
					columns : [{
						header : "用户名",
						dataIndex : "fullname",
						renderer : function(j, k, i) {
							var l = i.data.title;
							if (l == 1) {
								return '<img src="' + __ctxPath
										+ '/images/flag/man.png"/>&nbsp;' + j;
							} else {
								return '<img src="' + __ctxPath
										+ '/images/flag/women.png"/>&nbsp;' + j;
							}
						},
						width : 60
					}]
				});
		if (!this.single) {
			this.gridPanel.addListener("rowdblclick", this.gridPanelRowClick,
					this);
			var h = new Ext.grid.CheckboxSelectionModel();
			this.selGridPanel = new Ext.grid.EditorGridPanel({
				title : "已选用户(双击行移除)",
				split : true,
				isShowTbar : false,
				width : 160,
				height : 430,
				showPaging : false,
				autoScroll : true,
				store : new Ext.data.ArrayStore({
							fields : ["userId", "fullname", "title"]
						}),
				trackMouseOver : true,
				sm : h,
				columns : [h, new Ext.grid.RowNumberer(), {
					header : "用户名",
					dataIndex : "fullname",
					renderer : function(j, k, i) {
						var l = i.data.title;
						if (l == 1) {
							return '<img src="' + __ctxPath
									+ '/images/flag/man.png"/>&nbsp;' + j;
						} else {
							if (l == 0) {
								return '<img src="' + __ctxPath
										+ '/images/flag/women.png"/>&nbsp;' + j;
							} else {
								return j;
							}
						}
					}
				}],
				listeners : {
					scope : this,
					"rowdblclick" : this.selGridPanelRowDbClick
				}
			});
			this.southPanel = new Ext.Panel({
				width : 200,
				region : "east",
				layout : "column",
				border : false,
				items : [new Ext.Panel({
									frame : true,
									width : 35,
									height : 430,
									layout : {
										type : "vbox",
										pack : "center",
										align : "stretch"
									},
									defaults : {
										margins : "0 0 5 0"
									},
									defaultType : "button",
									items : [{
												iconCls : "add-all",
												text : "",
												scope : this,
												handler : this.addAll
											}, {
												iconCls : "rem-all",
												text : "",
												scope : this,
												handler : this.selGridPanelRowDbClick
											}]
								}), {
							autoScroll : true,
							items : [this.selGridPanel]
						}]
			});
			if (this.userIds) {
				var b = this.selGridPanel.getStore();
				var g = this.userIds.split(",");
				var f = this.userName.split(",");
				if (g[0] == "") {
					var a = this.userIds.length;
					this.userIds = this.userIds.substring(1, a - 1);
					g = this.userIds.split(",");
				}
				for (var d = 0; d < g.length; d++) {
					if (g[d] != "") {
						var e = {
							userId : g[d],
							fullname : f[d],
							title : 3
						};
						var c = new b.recordType(e);
						this.selGridPanel.stopEditing();
						b.add(c);
					}
				}
			}
		}
	},
	selGridPanelRowDbClick : function() {
		var a = this.selGridPanel.getStore();
		var c = this.selGridPanel.getSelectionModel().getSelections();
		for (var b = 0; b < c.length; b++) {
			this.selGridPanel.stopEditing();
			a.remove(c[b]);
		}
	},
	gridPanelRowClick : function(f, j, g) {
		var c = f.getStore();
		var b = c.getAt(j);
		var a = this.selGridPanel.getStore();
		for (var d = 0; d < a.getCount(); d++) {
			if (a.getAt(d).data.userId == b.data.userId) {
				Ext.ux.Toast.msg("操作信息", "选项已被选中！");
				return;
			}
		}
		var h = a.recordType;
		a.add(new h(b.data));
	},
	addAll : function(a, k, h) {
		var m = this.selGridPanel.getStore();
		var p = this.gridPanel.getSelectionModel().getSelections();
		for (var c = 0; c < p.length; c++) {
			var d = p[c].data.userId;
			var n = p[c].data.fullname;
			var l = p[c].data.title;
			var g = false;
			for (var b = 0; b < m.getCount(); b++) {
				if (m.getAt(b).data.userId == d) {
					g = true;
					break;
				}
			}
			if (!g) {
				var o = {
					userId : d,
					fullname : n,
					title : l
				};
				var f = new m.recordType(o);
				this.selGridPanel.stopEditing();
				m.add(f);
			}
		}
	},
	confirm : function() {
		var e = "";
		var c = "";
		var f = [];
		if (this.single) {
			var d = this.gridPanel.getSelectionModel().getSelections();
			for (var b = 0; b < d.length; b++) {
				if (b > 0) {
					e += ",";
					c += ",";
				}
				e += d[b].data.userId;
				c += d[b].data.fullname;
				f.push(d[b].data);
			}
		} else {
			var a = this.selGridPanel.getStore();
			for (var b = 0; b < a.getCount(); b++) {
				if (b > 0) {
					e += ",";
					c += ",";
				}
				e += a.getAt(b).data.userId;
				c += a.getAt(b).data.fullname;
				f.push(a.getAt(b).data);
			}
		}
		if (this.callback) {
			this.callback.call(this.scope, e, c, f);
		}
		this.close();
	}
});