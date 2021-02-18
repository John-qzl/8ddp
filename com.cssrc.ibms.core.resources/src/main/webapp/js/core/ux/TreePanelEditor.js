Ext.ns("Orient.ux");
Orient.ux.TreePanelEditor = Ext.extend(Ext.tree.TreePanel, {
			showContextMenu : true,
			url : null,
			params : null,
			contextMenu : null,
			contextMenuItems : null,
			selectedNode : null,
			enableDD : null,
			constructor : function(a) {
				if (!a) {
					a = {};
				}
				Ext.apply(this, a);
				Orient.ux.TreePanelEditor.superclass.constructor.call(this, {
							tbar : new Ext.Toolbar({
										items : [{
													xtype : "button",
													iconCls : "toolbarrefresh",
													text : "刷新",
													scope : this,
													handler : function() {
														this.root.reload();
													}
												}, {
													xtype : "button",
													text : "展开",
													iconCls : "toolbarexpand",
													scope : this,
													handler : function() {
														this.expandAll();
													}
												}, {
													xtype : "button",
													text : "收起",
													iconCls : "toolbarcollapse",
													scope : this,
													handler : function() {
														this.collapseAll();
													}
												}]
									}),
							loader : new Ext.tree.TreeLoader({
										url : this.url,
										requestMethod : "POST",
										baseParams : this.params
									}),
							root : new Ext.tree.AsyncTreeNode({
										expanded : true
									}),
							listeners : {
								scope : this,
								"click" : function(c) {
									if (a.onclick) {
										this.selectedNode = c;
										var b = this.scope ? this.scope : this;
										a.onclick.call(b, c);
									}
								},
								"dblclick" : function(c, d) {
									if (a.dblclick) {
										this.selectedNode = c;
										var b = this.scope ? this.scope : this;
										a.dblclick.call(b, c);
									}
								},
								"movenode" : function(b, g, d, f, c) {
									if (a.movenode) {
										this.selectedNode = g;
										var e = this.scope ? this.scope : this;
										a.movenode.call(e, b, g, d, f, c);
									}
								},
								"nodedragover" : function(c) {
									if (a.nodedragover) {
										this.selectedNode = c.data;
										var b = this.scope ? this.scope : this;
										a.nodedragover.call(b, c);
									}
								}
							},
							rootVisible : false,
							enableDD : this.enableDD ? true : false
						});
				if (this.contextMenuItems) {
					this.initContextMenu();
				}
			},
			initContextMenu : function() {
				if (this.showContextMenu) {
					this.contextMenu = new Ext.menu.Menu({});
					if (this.contextMenuItems != null) {
						this.contextMenu.add(this.contextMenuItems);
					}
					this.on("contextmenu", this.contextHandler, this);
				}
			},
			contextHandler : function contextmenu(a, b) {
				if (this.treeType == "org") {
					this.selectedNode = new Ext.tree.TreeNode({
								id : a.id,
								text : a.text,
								orgType : a.attributes.orgType,
								orgDem : a.attributes.orgDem
							});
				} else if (this.treeType == "dbom") {
					this.selectedNode = new Ext.tree.TreeNode({
								id : a.id,
								text : a.text,
								code : a.attributes.code,
								type : a.attributes.type
							});
				} else {
					this.selectedNode = new Ext.tree.TreeNode({
								id : a.id,
								text : a.text
							});
				}
				this.contextMenu.showAt(b.getXY());
			}
		});
Ext.reg("treePanelEditor", Orient.ux.TreePanelEditor);