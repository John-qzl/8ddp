OnlineUserDialog = Ext.extend(Ext.Window, {
	constructor : function(a) {
		Ext.applyIf(this, a);
		this.scope = this.scope ? this.scope : this;
		this.single = this.single != null ? this.single : true;
		this.initUI();
		OnlineUserDialog.superclass.constructor.call(this, {
					title : "选择在线用户",
					iconCls : "menu-appuser",
					width : 440,
					height : 420,
					border : false,
					layout : "fit",
					modal : true,
					buttonAlign : "center",
					buttons : [{
								iconCls : "btn-ok",
								text : "确定",
								scope : this,
								handler : this.confirm
							}, {
								text : "取消",
								iconCls : "btn-cancel",
								scope : this,
								handler : function() {
									this.close();
								}
							}],
					items : [this.contactPanel]
				});
	},
	initUI : function() {
		this.depTreePanel = new Ext.tree.TreePanel({
					title : "按部门分类 ",
					iconCls : "dep-user",
					autoScroll : true,
					loader : new Ext.tree.TreeLoader({
								url : __ctxPath + "/system/listDepartment.do"
							}),
					root : new Ext.tree.AsyncTreeNode({
								expanded : true
							}),
					rootVisible : false,
					listeners : {
						scope : this,
						"click" : this.clickDepNode
					}
				});
		this.roleTreePanel = new Ext.tree.TreePanel({
					iconCls : "role-user",
					title : "按角色分类 ",
					autoScroll : true,
					loader : new Ext.tree.TreeLoader({
								url : __ctxPath + "/system/treeAppRole.do"
							}),
					root : new Ext.tree.AsyncTreeNode({
								expanded : true
							}),
					rootVisible : false,
					listeners : {
						scope : this,
						"click" : this.clickRoleNode
					}
				});
		this.onlineTreePanel = new Ext.Panel({
					autoScroll : true,
					iconCls : "online-user",
					title : "所有在线人员  ",
					listeners : {
						scope : this,
						"expand" : this.clickOnlinePanel
					}
				});
		this.gridPanel = new HT.GridPanel({
					autoScroll : true,
					height : 345,
					singleSelect : this.single,
					url : __ctxPath + "/system/onlineAppUser.do",
					fields : [{
								name : "userId",
								type : "int"
							}, "fullname", "title"],
					columns : [{
						header : "用户名",
						dataIndex : "fullname",
						renderer : function(b, c, a) {
							var d = a.data.title;
							if (d == "1") {
								return '<img src="' + __ctxPath
										+ '/images/flag/man.png"/>&nbsp;' + b;
							} else {
								return '<img src="' + __ctxPath
										+ '/images/flag/women.png"/>&nbsp;' + b;
							}
						},
						width : 60
					}]
				});
		this.contactPanel = new Ext.Panel({
					id : "contactPanel",
					width : 460,
					height : 400,
					layout : "border",
					border : false,
					items : [{
						region : "west",
						split : true,
						header : false,
						collapsible : true,
						width : 160,
						layout : "accordion",
						items : [this.depTreePanel, this.roleTreePanel,
								this.onlineTreePanel]
					}, {
						region : "center",
						layout : "fit",
						width : 250,
						items : [this.gridPanel]
					}]
				});
	},
	confirm : function() {
		var b = this.gridPanel;
		var d = b.getSelectionModel().getSelections();
		var e = "";
		var c = "";
		for (var a = 0; a < d.length; a++) {
			if (a > 0) {
				e += ",";
				c += ",";
			}
			e += d[a].data.userId;
			c += d[a].data.fullname;
		}
		if (this.callback != null) {
			this.callback.call(this, e, c);
		}
		this.close();
	},
	clickDepNode : function(b) {
		if (b != null) {
			var a = this.gridPanel.getStore();
			a.baseParams = {
				depId : b.id
			};
			a.load();
		}
	},
	clickRoleNode : function(b) {
		if (b != null) {
			var a = this.gridPanel.getStore();
			a.baseParams = {
				roleId : b.id
			};
			a.load();
		}
	},
	clickOnlinePanel : function() {
		var a = this.gridPanel.getStore();
		a.baseParams = {
			depId : null,
			roleId : null
		};
		a.load();
	}
});