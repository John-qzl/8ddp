DepDialog = Ext.extend(Ext.Window, {
			constructor : function(a) {
				Ext.applyIf(this, a);
				this.scope = this.scope ? this.scope : this;
				this.single = this.single != null ? this.single : false;
				this.initUI();
				DepDialog.superclass.constructor.call(this, {
							title : this.title ? this.title : "部门选择器",
							iconCls : "menu-department",
							width : 630,
							height : 380,
							maximizable : true,
							modal : true,
							layout : "border",
							modal : true,
							border : false,
							buttonAlign : "center",
							items : [this.panel],
							buttons : [{
										iconCls : "btn-ok",
										text : "确定",
										scope : this,
										handler : this.confirm
									}, {
										text : "取消",
										iconCls : "btn-cancel",
										handler : this.close,
										scope : this
									}]
						});
			},
			initUI : function() {
				this.treePanel = new htsoft.ux.TreePanelEditor({
							title : "部门信息显示",
							region : "west",
							width : 180,
							height : 300,
							split : true,
							collapsible : true,
							autoScroll : true,
							url : __ctxPath + "/system/listDepartment.do",
							scope : this,
							onclick : this.treeClick
						});
				this.gridPanel = new HT.GridPanel({
							width : 400,
							height : 300,
							region : "center",
							title : "部门列表(双击选中)",
							shim : true,
							trackMouseOver : true,
							disableSelection : false,
							singleSelect : true,
							loadMask : true,
							viewConfig : {
								forceFit : true,
								enableRowBody : false,
								showPreview : false
							},
							url : __ctxPath + "/system/selectDepartment.do",
							fields : [{
										name : "depId",
										type : "int"
									}, "depName", {
										name : "depLevel",
										type : "int"
									}, "chargeIds", "chargeNames"],
							columns : [{
										header : "depId",
										dataIndex : "depId",
										hidden : true
									}, {
										header : "部门名称",
										dataIndex : "depName",
										scope : this,
										renderer : this.depNameRenderer,
										width : 60
									}, {
										dataIndex : "chargeIds",
										hidden : true
									}, {
										dataIndex : "chargeNames",
										hidden : true
									}],
							listeners : {
								scope : this,
								"rowdblclick" : this.rowdblclick
							}
						});
				this.selectedDepGrid = new HT.GridPanel({
							title : "已选部门(双击删除)",
							layout : "form",
							region : "center",
							width : "100%",
							height : "100%",
							autoWidth : true,
							autoHeight : true,
							autoScroll : true,
							border : false,
							trackMouseOver : true,
							showPaging : false,
							fields : ["depId", "depName", "chargeIds",
									"chargeNames"],
							columns : [{
										header : "部门名称",
										dataIndex : "depName"
									}, {
										dataIndex : "chargeIds",
										hidden : true
									}, {
										dataIndex : "chargeNames",
										hidden : true
									}],
							listeners : {
								scope : this,
								"rowdblclick" : this.selectRowdblclick
							}
						});
				if (this.depIds) {
					var b = this.selectedDepGrid.getStore();
					var g = this.depIds.split(",");
					var f = this.depNames.split(",");
					if (g[0] == "") {
						var a = this.depIds.length;
						this.depIds = this.depIds.substring(1, a - 1);
						g = this.depIds.split(",");
					}
					for (var d = 0; d < g.length; d++) {
						if (g[d] != "") {
							var e = {
								depId : g[d],
								depName : f[d]
							};
							var c = new b.recordType(e);
							this.selectedDepGrid.stopEditing();
							b.add(c);
						}
					}
				}
				this.selectedPanel = new Ext.Panel({
							layout : "border",
							region : "east",
							width : "200",
							height : "100%",
							border : false,
							autoScroll : true,
							items : [new Ext.Panel({
												region : "west",
												frame : true,
												width : 40,
												layout : {
													type : "vbox",
													pack : "center",
													align : "stretch"
												},
												defaults : {
													height : 20,
													xtype : "button"
												},
												items : [{
															iconCls : "add-all",
															text : "",
															scope : this,
															handler : this.addAll
														}, {
															iconCls : "rem-all",
															text : "",
															scope : this,
															handler : this.removeAll
														}]
											}), {
										region : "center",
										autoScroll : true,
										items : [this.selectedDepGrid]
									}]
						});
				this.panel = new Ext.Panel({
							id : "contactPanel",
							layout : "border",
							region : "center",
							border : false,
							anchor : "96%,96%",
							items : [this.treePanel, this.gridPanel]
						});
				if (this.single != null && this.single == true) {
					this.panel.add(this.selectedPanel);
					this.panel.doLayout();
				}
			},
			treeClick : function(b) {
				if (!Ext.isEmpty(b)) {
					var a = this.gridPanel.getStore();
					a.baseParams = {
						depId : b.id
					};
					this.gridPanel.getBottomToolbar().moveFirst();
				}
			},
			depNameRenderer : function(d, c, a) {
				var e = "";
				var f = a.data.depLevel;
				if (f != null && !isNaN(f)) {
					for (var b = 2; b <= f; b++) {
						e += '<img src="' + __ctxPath
								+ '/images/system/down.gif"/>';
					}
				}
				e += d;
				return e;
			},
			rowdblclick : function(f, j, g) {
				var c = f.getStore();
				var b = c.getAt(j);
				var a = this.selectedDepGrid.getStore();
				for (var d = 0; d < a.getCount(); d++) {
					if (a.getAt(d).data.depId == b.data.depId) {
						Ext.ux.Toast.msg("操作信息", "选项已被选中！");
						return;
					}
				}
				var h = a.recordType;
				a.add(new h(b.data));
			},
			selectRowdblclick : function(c, f) {
				var a = this.selectedDepGrid.getStore();
				var d = this.selectedDepGrid.getSelectionModel()
						.getSelections();
				for (var b = 0; b < d.length; b++) {
					this.selectedDepGrid.stopEditing();
					a.remove(d[b]);
				}
			},
			addAll : function() {
				var k = this.gridPanel;
				var a = this.selectedDepGrid;
				var m = a.getStore();
				var o = k.getSelectionModel().getSelections();
				for (var c = 0; c < o.length; c++) {
					var e = o[c].data.depId;
					var l = o[c].data.depName;
					var d = o[c].data.chargeIds;
					var h = o[c].data.chargeNames;
					var g = false;
					for (var b = 0; b < m.getCount(); b++) {
						if (m.getAt(b).data.depId == e) {
							g = true;
							break;
						}
					}
					if (!g) {
						var n = {
							depId : e,
							depName : l,
							chargeIds : d,
							chargeNames : h
						};
						var f = new m.recordType(n);
						a.stopEditing();
						m.add(f);
					}
				}
			},
			removeAll : function() {
				var c = this.selectedDepGrid;
				var d = c.getSelectionModel().getSelections();
				var a = c.getStore();
				for (var b = 0; b < d.length; b++) {
					c.stopEditing();
					a.remove(d[b]);
				}
			},
			confirm : function() {
				var g = "";
				var h = "";
				var f = "";
				var d = "";
				if (this.single) {
					var a = this.selectedDepGrid.getStore();
					for (var c = 0; c < a.getCount(); c++) {
						if (c > 0) {
							g += ",";
							h += ",";
							f += ",";
							d += ",";
						}
						g += a.getAt(c).data.depId;
						h += a.getAt(c).data.depName;
						f += a.getAt(c).data.chargeIds;
						d += a.getAt(c).data.chargeNames;
					}
				} else {
					var b = this.gridPanel;
					var e = b.getSelectionModel().getSelections();
					for (var c = 0; c < e.length; c++) {
						g = e[c].data.depId;
						h = e[c].data.depName;
						f = e[c].data.chargeIds;
						d = e[c].data.chargeNames;
					}
				}
				if (this.callback) {
					this.callback.call(this.scope, g, h);
				}
				this.close();
			}
		});