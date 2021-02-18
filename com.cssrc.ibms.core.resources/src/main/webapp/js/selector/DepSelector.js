var DepSelector = {
	getView : function(e, i, a, b, n) {
		if (typeof(e) == "object") {
			this.scope = e.scope;
			this.callback = e.callback;
		} else {
			this.scope = this;
			this.callback = e;
		}
		if (i == null || i == undefined) {
			i = false;
		}
		var p = new Ext.tree.TreePanel({
					title : "部门信息显示",
					region : "west",
					width : 180,
					height : 300,
					split : true,
					collapsible : true,
					autoScroll : true,
					tbar : new Ext.Toolbar({
								items : [{
											xtype : "button",
											iconCls : "btn-refresh",
											text : "刷新",
											handler : function() {
												p.root.reload();
											}
										}, "-", {
											xtype : "button",
											text : "展开",
											iconCls : "btn-expand",
											handler : function() {
												p.expandAll();
											}
										}, "-", {
											xtype : "button",
											text : "收起",
											iconCls : "btn-collapse",
											handler : function() {
												p.collapseAll();
											}
										}]
							}),
					loader : new Ext.tree.TreeLoader({
								url : __ctxPath + "/system/listDepartment.do"
							}),
					root : new Ext.tree.AsyncTreeNode({
								expanded : true
							}),
					rootVisible : false,
					listeners : {
						"click" : function(w) {
							if (w != null) {
								var x = Ext.getCmp("DepSelectorGrid");
								var v = x.getStore();
								v.proxy.conn.url = __ctxPath
										+ "/system/selectDepartment.do";
								v.baseParams = {
									depId : w.id
								};
							}
						}
					}
				});
		var t = null;
		if (i) {
			var t = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
		} else {
			t = new Ext.grid.CheckboxSelectionModel();
		}
		var k = new Ext.grid.ColumnModel({
					columns : [t, new Ext.grid.RowNumberer(), {
								header : "depId",
								dataIndex : "depId",
								hidden : true
							}, {
								header : "部门名称",
								dataIndex : "depName",
								renderer : function(y, x, v) {
									var z = "";
									var A = v.data.depLevel;
									if (A != null && !isNaN(A)) {
										for (var w = 2; w <= A; w++) {
											z += '<img src="'
													+ __ctxPath
													+ '/images/system/down.gif"/>';
										}
									}
									z += y;
									return z;
								},
								width : 60
							}, {
								dataIndex : "chargeIds",
								hidden : true
							}, {
								dataIndex : "chargeNames",
								hidden : true
							}]
				});
		var f = new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
								url : __ctxPath + "/system/selectDepartment.do"
							}),
					reader : new Ext.data.JsonReader({
								root : "result",
								totalProperty : "totalCounts",
								id : "depId",
								fields : [{
											name : "depId",
											type : "int"
										}, "depName", {
											name : "depLevel",
											type : "int"
										}, "chargeIds", "chargeNames"]
							}),
					remoteSort : true
				});
		var r = new Ext.grid.GridPanel({
					id : "DepSelectorGrid",
					width : 400,
					height : 300,
					region : "center",
					title : "部门列表(双击选中)",
					store : f,
					shim : true,
					trackMouseOver : true,
					disableSelection : false,
					loadMask : true,
					cm : k,
					sm : t,
					viewConfig : {
						forceFit : true,
						enableRowBody : false,
						showPreview : false
					},
					bbar : new HT.PagingBar({
								store : f
							})
				});
		r.on("rowdblclick", function(v, F, D) {
					var H = Ext.getCmp("DepSelectorGrid");
					var w = Ext.getCmp("selectedDepGrid");
					var I = w.getStore();
					var K = H.getSelectionModel().getSelections();
					for (var y = 0; y < K.length; y++) {
						var A = K[y].data.depId;
						var G = K[y].data.depName;
						var z = K[y].data.chargeIds;
						var E = K[y].data.chargeNames;
						var C = false;
						for (var x = 0; x < I.getCount(); x++) {
							if (I.getAt(x).data.depId == A) {
								C = true;
								break;
							}
						}
						if (!C) {
							var J = {
								depId : A,
								depName : G,
								chargeIds : z,
								chargeNames : E
							};
							var B = new I.recordType(J);
							w.stopEditing();
							I.add(B);
						}
					}
				});
		f.load({});
		var q = new Ext.grid.CheckboxSelectionModel();
		var u = new Ext.grid.EditorGridPanel({
					id : "selectedDepGrid",
					title : "已选部门(双击删除)",
					layout : "form",
					region : "center",
					width : "100%",
					autoWidth : true,
					height : "100%",
					autoHeight : true,
					autoScroll : true,
					border : false,
					store : new Ext.data.ArrayStore({
								fields : ["depId", "depName", "chargeIds",
										"chargeNames"]
							}),
					trackMouseOver : true,
					sm : q,
					columns : [q, new Ext.grid.RowNumberer(), {
								header : "部门名称",
								dataIndex : "depName"
							}, {
								dataIndex : "chargeIds",
								hidden : true
							}, {
								dataIndex : "chargeNames",
								hidden : true
							}]
				});
		if (b && b.length > 0) {
			var s = u.getStore();
			var l = b.split(",");
			var c = n.split(",");
			for (var g = 0; g < l.length; g++) {
				var m = {
					depId : l[g],
					depName : c[g]
				};
				var d = new s.recordType(m);
				u.stopEditing();
				s.add(d);
			}
		}
		u.addListener("rowdblclick", function(x, z) {
					var x = Ext.getCmp("selectedDepGrid");
					var v = x.getStore();
					var y = x.getSelectionModel().getSelections();
					for (var w = 0; w < y.length; w++) {
						x.stopEditing();
						v.remove(y[w]);
					}
				});
		var h = new Ext.Panel({
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
								items : [u]
							}]
				});
		var o = new Ext.Panel({
					id : "contactPanel",
					layout : "border",
					region : "center",
					border : false,
					anchor : "96%,96%",
					items : [p, r]
				});
		if (i != null && i == false) {
			o.add(h);
			o.doLayout();
		}
		var j = new Ext.Window({
					id : "depSelectWindow",
					title : "部门选择器",
					iconCls : "menu-department",
					width : 630,
					height : 380,
					layout : "border",
					border : false,
					items : [o],
					modal : true,
					buttonAlign : "center",
					maximizable : true,
					buttons : [{
						iconCls : "btn-ok",
						text : "确定",
						scope : this,
						handler : this.submit.createCallback(this.callback, i,
								a)
					}, {
						text : "取消",
						iconCls : "btn-cancel",
						handler : this.close,
						scope : this
					}]
				});
		return j;
	},
	addAll : function() {
		var k = Ext.getCmp("DepSelectorGrid");
		var a = Ext.getCmp("selectedDepGrid");
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
		var c = Ext.getCmp("selectedDepGrid");
		var d = c.getSelectionModel().getSelections();
		var a = c.getStore();
		for (var b = 0; b < d.length; b++) {
			c.stopEditing();
			a.remove(d[b]);
		}
	},
	submit : function(k, e, b) {
		var h = "";
		var g = "";
		var d = "";
		var f = "";
		if (e == null || e == false) {
			var j = Ext.getCmp("selectedDepGrid").getStore();
			for (var c = 0; c < j.getCount(); c++) {
				if (c > 0) {
					h += ",";
					g += ",";
					d += ",";
					f += ",";
				}
				h += j.getAt(c).data.depId;
				g += j.getAt(c).data.depName;
				d += j.getAt(c).data.chargeIds;
				f += j.getAt(c).data.chargeNames;
			}
		} else {
			var a = Ext.getCmp("DepSelectorGrid");
			var l = a.getSelectionModel().getSelections();
			for (var c = 0; c < l.length; c++) {
				if (c > 0) {
					h += ",";
					g += ",";
					d += ",";
					f += ",";
				}
				h += l[c].data.depId;
				g += l[c].data.depName;
				d += l[c].data.chargeIds;
				f += l[c].data.chargeNames;
			}
		}
		if (k != null) {
			k.call(this, h, g);
		}
		Ext.getCmp("depSelectWindow").close();
	},
	close : function() {
		Ext.getCmp("depSelectWindow").close();
	}
};