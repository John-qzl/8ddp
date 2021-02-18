Ext.ns("App");
Ext.ns("AppUtil");
var jsCache = new Array();
function strToDom(a) {
	if (window.ActiveXObject) {
		var b = new ActiveXObject("Microsoft.XMLDOM");
		b.async = "false";
		b.loadXML(a);
		return b;
	} else {
		if (document.implementation && document.implementation.createDocument) {
			var c = new DOMParser();
			var b = c.parseFromString(a, "text/xml");
			return b;
		}
	}
}
function newView(viewName, params) {
	var str = "new " + viewName;
	if (params != null) {
		str += "(params);";
	} else {
		str += "();";
	}
	return eval(str);
}
function $ImportJs(viewName, callback, params) {
	var b = jsCache[viewName];
	if (b != null) {
		var view = newView(viewName, params);
		callback.call(this, view);
	} else {
		var jsArr = eval("App.importJs." + viewName);
		if (jsArr == undefined || jsArr.length == 0) {
			try {
				var view = newView(viewName, params);
				callback.call(this, view);
			} catch (e) {
			}
			return;
		}
		ScriptMgr.load({
					scripts : jsArr,
					callback : function() {
						jsCache[viewName] = 0;
						var view = newView(viewName, params);
						callback.call(this, view);
					}
				});
	}
}
function $ImportSimpleJs(a, c, b) {
	ScriptMgr.load({
				scripts : a,
				scope : b,
				callback : function() {
					if (c) {
						c.call(this);
					}
				}
			});
}
function $parseDate(b) {
	if (typeof b == "string") {
		var a = b.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);
		if (a && a.length > 3) {
			return new Date(parseInt(a[1]), parseInt(a[2]) - 1, parseInt(a[3]));
		}
		a = b
				.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);
		if (a && a.length > 6) {
			var c = a[4].match(/^ *(0)(\d{1})*$/);
		}
		if (c && c.length > 2) {
			a[4] = c[2];
		}
		return new Date(parseInt(a[1]), parseInt(a[2]) - 1, parseInt(a[3]),
				parseInt(a[4]), parseInt(a[5]), parseInt(a[6]));
		a = b
				.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2})\.(\d{1,9}) *$/);
		if (a && a.length > 7) {
			return new Date(parseInt(a[1]), parseInt(a[2]) - 1, parseInt(a[3]),
					parseInt(a[4]), parseInt(a[5]), parseInt(a[6]),
					parseInt(a[7]));
		}
	}
	return null;
}
function $formatDate(b) {
	if (typeof b == "string") {
		b = parseDate(b);
	}
	if (b instanceof Date) {
		var k = b.getFullYear();
		var a = b.getMonth() + 1;
		var j = b.getDate();
		var g = b.getHours();
		var e = b.getMinutes();
		var f = b.getSeconds();
		var c = b.getMilliseconds();
		if (a < 10) {
			a = "0" + a;
		}
		if (j < 10) {
			j = "0" + j;
		}
		if (g < 10) {
			g = "0" + g;
		}
		if (e < 10) {
			e = "0" + e;
		}
		if (f < 10) {
			f = "0" + f;
		}
		if (c > 0) {
			return k + "-" + a + "-" + j + " " + g + ":" + e + ":" + f + "."
					+ c;
		}
		if (g > 0 || e > 0 || f > 0) {
			return k + "-" + a + "-" + j + " " + g + ":" + e + ":" + f;
		}
		return k + "-" + a + "-" + j;
	}
	return "";
}
function compareTime(b, a, d) {
	if (Ext.isEmpty(b)) {
		return null;
	}
	if (Ext.isEmpty(a)) {
		return null;
	}
	if (!d) {
		d = "H:i";
	}
	var e = Date.parseDate(b, d);
	var c = Date.parseDate(a, d);
	if (e > c) {
		return 1;
	}
	if (e = c) {
		return 0;
	}
	if (e < c) {
		return -1;
	}
}
function $getTableInputCmpName(h) {
	var f = [];
	for (var e = 0; e < h.rows.length; e++) {
		var l = h.rows[e];
		for (var c = 0; c < l.cells.length; c++) {
			var g = l.cells[c];
			var d;
			for (var b = 0; b < g.childNodes.length; b++) {
				if (g.childNodes[b].getAttribute
						&& g.childNodes[b].getAttribute("name")) {
					d = g.childNodes[b];
					if (d) {
						var a = d.getAttribute("name");
						f.push(a);
					}
				}
			}
		}
	}
	return f;
}
App.getContentPanel = function() {
	var a = Ext.getCmp("centerTabPanel");
	return a;
};
AppUtil.removeTab = function(a) {
	var b = App.getContentPanel();
	var c = b.getItem(a);
	if (c != null) {
		b.remove(c, true);
	}
};
AppUtil.activateTab = function(a) {
	var b = App.getContentPanel();
	b.activate(a);
};
AppUtil.addTab = function(a) {
	var b = App.getContentPanel();
	b.add(a);
	b.activate(a);
};
App.createUploadDialog = function(b) {
	var a = {
		file_cat : b.file_cat ? b.file_cat : "others",
		url : __ctxPath + "/file-upload",
		reset_on_hide : false,
		upload_autostart : false,
		scope : b.scope ? b.scope : this,
		modal : true
	};
	Ext.apply(a, b);
	var c = new FileUploadManager(a);
	return c;
};
App.createUploadDialog2 = function(b) {
	var a = {
		file_cat : "others",
		url : __ctxPath + "/file-upload",
		reset_on_hide : false,
		upload_autostart : false,
		modal : true
	};
	Ext.apply(a, b);
	var c = new Ext.ux.UploadDialog.Dialog(a);
	return c;
};
function uniqueArray(e) {
	e = e || [];
	var b = {};
	for (var d = 0; d < e.length; d++) {
		var c = e[d];
		if (typeof(b[c]) == "undefined") {
			b[c] = 1;
		}
	}
	e.length = 0;
	for (var d in b) {
		e[e.length] = d;
	}
	return e;
}
function setCookie(b, d, a, f, c, e) {
	document.cookie = b + "=" + escape(d)
			+ ((a) ? "; expires=" + a.toGMTString() : "")
			+ ((f) ? "; path=" + f : "") + ((c) ? "; domain=" + c : "")
			+ ((e) ? "; secure" : "");
}
function getCookie(b) {
	var d = b + "=";
	var e = document.cookie.indexOf(d);
	if (e == -1) {
		return null;
	}
	var a = document.cookie.indexOf(";", e + d.length);
	if (a == -1) {
		a = document.cookie.length;
	}
	var c = document.cookie.substring(e + d.length, a);
	return unescape(c);
}
function deleteCookie(a, c, b) {
	if (getCookie(a)) {
		document.cookie = a + "=" + ((c) ? "; path=" + c : "")
				+ ((b) ? "; domain=" + b : "")
				+ "; expires=Thu, 01-Jan-70 00:00:01 GMT";
	}
}
String.prototype.trim = function() {
	return (this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""));
};
function $request(a) {
	Ext.Ajax.request({
				url : a.url,
				params : a.params,
				method : a.method == null ? "POST" : a.method,
				success : function(b, c) {
					if (a.success != null) {
						a.success.call(this, b, c);
					}
				},
				failure : function(b, c) {
					Ext.ux.Toast.msg("操作信息", "操作出错，请联系管理员！");
					if (a.success != null) {
						a.failure.call(this, b, c);
					}
				}
			});
}
function asynReq() {
	var a = Ext.Ajax.getConnectionObject().conn;
	a.open("GET", url, false);
	a.send(null);
}
AppUtil.addPrintExport = function(a) {
};
function $getConfFromTable(table) {
	var map = new Ext.util.MixedCollection(true);
	if (table.rows.length != 2) {
		return map;
	}
	var row1 = table.rows[0];
	var row2 = table.rows[1];
	for (var i = 0; i < row1.cells.length; i++) {
		var cell = row2.cells[i];
		var control;
		for (var j = 0; j < cell.childNodes.length; j++) {
			if (cell.childNodes[j].getAttribute
					&& cell.childNodes[j].getAttribute("name")) {
				control = cell.childNodes[j];
				break;
			}
		}
		if (!control) {
			continue;
		}
		var name = control.getAttribute("name");
		var xtype = control.getAttribute("xtype");
		var header = row1.cells[i].innerHTML;
		var format = control.getAttribute("dataformat");
		var itemsName = control.getAttribute("txtitemname");
		var isnotnull = control.getAttribute("txtisnotnull");
		var issingle = control.getAttribute("issingle");
		var iscurrent = control.getAttribute("iscurrent");
		var txtvaluetype = control.getAttribute("txtvaluetype");
		var type = control.type;
		var vals = [];
		if (type && type.indexOf("select") != -1) {
			xtype = "comboselect";
			var opts = control.options;
			for (var v = 0; v < opts.length; v++) {
				var opt = opts[v];
				var o = [];
				o.push(opt.value);
				o.push(opt.text);
				vals.push(o);
			}
		}
		var conf;
		switch (xtype) {
			case "numberfield" :
				conf = {
					xtype : "numbercolumn",
					txtvaluetype : txtvaluetype,
					sortable : false,
					format : format ? format : "",
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					isSingle : issingle == 1 ? true : false,
					header : header
				};
				break;
			case "comboselect" :
				conf = {
					xtype : "gridcolumn",
					dataIndex : name,
					sortable : false,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					header : header,
					datas : vals
				};
				break;
			case "datefield" :
				conf = {
					xtype : "datecolumn",
					sortable : false,
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					defaultValue : iscurrent ? new Date() : null,
					format : format == "yyyy-MM-dd" ? "Y-m-d" : "Y-m-d H:i:s",
					iscurrent : iscurrent,
					header : header
				};
				break;
			case "userselector" :
				conf = {
					xtype : "gridcolumn",
					sortable : false,
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					iscurrent : iscurrent,
					defaultValue : iscurrent ? App.auth.fullName : null,
					isSingle : issingle == 1 ? true : false,
					format : format ? format : "",
					header : header
				};
				break;
			case "depselector" :
				conf = {
					xtype : "gridcolumn",
					sortable : false,
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					iscurrent : iscurrent,
					defaultValue : iscurrent ? App.auth.orgName : null,
					isSingle : issingle == 1 ? true : false,
					format : format ? format : "",
					header : header
				};
				break;
			case "posselector" :
				conf = {
					xtype : "gridcolumn",
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					sortable : false,
					iscurrent : iscurrent,
					defaultValue : iscurrent ? App.auth.posName : null,
					isSingle : issingle == 1 ? true : false,
					format : format ? format : "",
					header : header
				};
				break;
			case "diccombo" :
				conf = {
					xtype : "gridcolumn",
					sortable : false,
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					proTypeId : itemsName,
					width : control.getAttribute("width") * 0.9,
					header : header
				};
				break;
			default :
				conf = {
					xtype : "gridcolumn",
					sortable : false,
					dataIndex : name,
					isNotNull : isnotnull == 1 ? true : false,
					dxtype : xtype,
					isSingle : issingle == 1 ? true : false,
					format : format ? format : "",
					header : header
				};
				break;
		}
		var callfunction = control.getAttribute("callbackfunction");
		if (callfunction) {
			try {
				var callf = eval(callfunction);
				if (callf) {
					conf["callbackfunction"] = callf;
				}
			} catch (e) {
			}
		}
		var cfunction = control.getAttribute("changefunction");
		if (cfunction && cfunction !== undefined) {
			try {
				var callf = eval(cfunction);
				if (callf) {
					conf["changefunction"] = callf;
				}
			} catch (e) {
			}
		}
		map.add(name, conf);
	}
	return map;
}
function $getTableInputCmpName(h) {
	var f = [];
	for (var e = 0; e < h.rows.length; e++) {
		var l = h.rows[e];
		for (var c = 0; c < l.cells.length; c++) {
			var g = l.cells[c];
			var d;
			for (var b = 0; b < g.childNodes.length; b++) {
				if (g.childNodes[b].getAttribute
						&& g.childNodes[b].getAttribute("name")) {
					d = g.childNodes[b];
					if (d) {
						var a = d.getAttribute("name");
						f.push(a);
					}
				}
			}
		}
	}
	return f;
}
function $converCmp(fElements, jsonData, rightJson, addFlag, readOnly) {
	var flag = "on";
	var formPanel = this.formPanel;
	var map = new Ext.util.MixedCollection();
	var arrays = new Array();
	var removeArray = [];
	var sumTotal = {
		valueName : "",
		operNames : ["", ""],
		valueEl : null,
		operEls : [],
		operaType : ""
	};
	Ext.each(fElements, function(element, index) {
		var name, type, value, xtype, height;
		var right = null;
		if (!element) {
			return;
		}
		name = element.name;
		type = element.type;
		if (rightJson && rightJson[name]) {
			right = rightJson[name];
		}
		if (type == "button" || type == "hidden") {
			return;
		}
		if (element.style.display == "none") {
			return;
		}
		xtype = element.getAttribute("xtype");
		if (name) {
			name = name.replace(/(^\s*)|(\s*$)/g, "");
		}
		var parent = element.parentNode;
		if (type == "radio" || type == "checkbox") {
			var value;
			if (jsonData && jsonData[name]) {
				value = jsonData[name];
			}
			if (element.value && value) {
				var str1 = element.value + "|", str2 = value + "|";
				var isContain = str2.indexOf(str1) > -1;
				if ((element.value == value) || isContain) {
					element.checked = true;
					element.setAttribute("isSelect", "true");
				} else {
					element.checked = false;
				}
			}
			if (readOnly || (right && right == 1)) {
				element.onclick = function() {
					return false;
				};
			} else {
				if (right && right == 0) {
					element.style.display = "none";
				}
			}
			return;
		} else {
			if (jsonData && jsonData[name]) {
				element.value = jsonData[name];
			}
		}
		if (type && type.indexOf("select") != -1) {
			if (readOnly || (right && right == 1)) {
				element.disabled = true;
				element.onfocus = function() {
					this.blur();
				};
			} else {
				if (readOnly || (right && right == 0)) {
					element.style.display = "none";
				}
			}
			return;
		}
		var width = element.getAttribute("width");
		var isNotNull = element.getAttribute("txtisnotnull");
		if (right && right == 3) {
			isNotNull = 1;
			element.setAttribute("txtisnotnull", "1");
		}
		if (!width) {
			width = parent.offsetWidth;
		}
		if (width < 300 && parent.offsetWidth > 300) {
			width = 300;
		}
		if (element.tagName == "INPUT") {
			if (element.name == sumTotal.valueName) {
				sumTotal.valueEl = element;
			}
			if (sumTotal.operNames.indexOf(element.name) != -1) {
				sumTotal.operEls.push(element);
			}
		}
		var readable = readOnly || (right && right == 1);
		if (readable) {
			readable = true;
		} else {
			readable = false;
		}
		if (readable && xtype == "fckeditor") {
			var children = parent.children;
			var height;
			if (children.length == 1) {
				height = parent.scrollHeight;
			} else {
				height = element.scrollHeight;
			}
			var html = element.value;
			var p = document.createElement("div");
			p.setAttribute("style",
					"color:black;font-size:14px;text-align:left;");
			p.style.width = "100%";
			p.style.height = height + "px";
			p.innerHTML = html;
			parent.insertBefore(p, element);
			element.style.display = "none";
			return;
		}
		if (readable && xtype == "fileattach") {
			var html = "";
			if (element.value != "") {
				var files = element.value.split(",");
				for (var i = 0; i < files.length; i++) {
					var vo = files[i].split("|");
					var id = vo[0];
					var name = vo[1];
					if (!isNaN(vo[0])) {
						html += '<a href="' + __ctxPath
								+ "/file-download?fileId=" + vo[0] + '">'
								+ vo[1] + "</a>";
					}
				}
			}
			if (html == "") {
				html = element.value;
			}
			var p = document.createElement("span");
			p.innerHTML = html;
			parent.insertBefore(p, element);
			element.style.display = "none";
			return;
		}
		if (!right || right != 0) {
			if (xtype == "datefield") {
				var format = element.getAttribute("dataformat");
				var iscurrent = element.getAttribute("iscurrent");
				var span = document.createElement("div");
				var obj = {
					parentNode : parent,
					oldEl : element,
					newEl : span
				};
				removeArray.push(obj);
				var div = document.createElement("div");
				div.setAttribute("style", "width:" + width + "px");
				span.appendChild(div);
				try {
					var cmp;
					if (format == "yyyy-MM-dd HH:mm:ss") {
						cmp = new Cls.form.DateTimeField({
									name : name,
									width : 200,
									readOnly : readable,
									autoWidth : false,
									sortable : false,
									boxMaxWidth : 200,
									format : "Y-m-d H:i:s",
									value : iscurrent == 1 ? new Date() : "",
									allowBlank : isNotNull == 1 && !readable
											? false
											: true
								});
					} else {
						cmp = new Ext.form.DateField({
									name : name,
									height : 21,
									readOnly : readable,
									sortable : false,
									width : 100,
									boxMaxWidth : 100,
									autoWidth : false,
									format : "Y-m-d",
									value : iscurrent == 1 ? new Date() : "",
									allowBlank : isNotNull == 1 && !readable
											? false
											: true
								});
					}
					cmp.remove;
					arrays.push("datefield" + index);
					cmp.on("resize", function(c, width1, height, owidth) {
								cmp.setWidth(width);
							}, this);
					map.add("datefield" + index, span);
					map.add("datefield" + index + "-cmp", cmp);
					map.add("datefield" + index + "-type", "datefield");
					if (element.value) {
						cmp.setValue($parseDate(element.value));
					}
				} catch (e) {
				}
			} else {
				if (xtype == "diccombo") {
					try {
						var itemname = element.getAttribute("txtitemname");
						var span = document.createElement("span");
						var obj = {
							parentNode : parent,
							oldEl : element,
							newEl : span
						};
						removeArray.push(obj);
						var div = document.createElement("div");
						span.appendChild(div);
						var cmp = new DicCombo({
									name : name,
									readOnly : readable,
									sortable : false,
									proTypeId : itemname,
									displayField : "itemName",
									valueField : "itemName",
									width : width * 0.9,
									allowBlank : isNotNull == 1 && !readable
											? false
											: true
								});
						arrays.push("diccombo" + index);
						cmp.on("resize", function(c, width1, height, owidth) {
									cmp.setWidth(width);
								}, this);
						map.add("diccombo" + index, span);
						map.add("diccombo" + index + "-cmp", cmp);
						map.add("diccombo" + index + "-type", "diccombo");
						if (element.value) {
							cmp.setValue(element.value);
						}
					} catch (e) {
					}
				} else {
					if (xtype == "commoneditor") {
						height = parent.offsetHeight;
						var h = element.getAttribute("txtheight");
						var scl = false;
						if (h && h > 0) {
							height = parent.offsetHeight;
							scl = true;
						} else {
							height = "";
						}
						var span = document.createElement("div");
						var obj = {
							parentNode : parent,
							oldEl : element,
							newEl : span
						};
						removeArray.push(obj);
						var div = document.createElement("div");
						span.appendChild(div);
						var cmp = new Ext.ux.form.CommentEditor({
									name : name,
									readOnly : readable,
									sortable : false,
									width : width,
									autoScroll : scl,
									height : height,
									value : element.value ? element.value : ""
								});
						arrays.push("commoneditor" + index);
						cmp.on("resize", function(c, width1, height, owidth) {
									cmp.setWidth(width);
								}, this);
						map.add("commoneditor" + index, span);
						map.add("commoneditor" + index + "-cmp", cmp);
						map.add("commoneditor" + index + "-type",
								"commoneditor");
					} else {
						if (xtype == "fckeditor") {
							height = parent.offsetHeight;
							var span = document.createElement("div");
							var obj = {
								parentNode : parent,
								oldEl : element,
								newEl : span
							};
							removeArray.push(obj);
							var div = document.createElement("div");
							span.appendChild(div);
							var cmp = new Ext.form.CKEditor({
										name : name,
										readOnly : readable,
										sortable : false,
										height : height,
										value : element.value
												? element.value
												: ""
									});
							arrays.push("fckeditor" + index);
							map.add("fckeditor" + index, span);
							map.add("fckeditor" + index + "-cmp", cmp);
							map.add("fckeditor" + index + "-type", "fckeditor");
						} else {
							if (xtype == "officeeditor") {
								try {
									var span = document.createElement("div");
									height = parent.offsetHeight;
									this.hiddenF = new Ext.form.Hidden({
												name : name
											});
									this.hiddenF.render(span);
									var obj = {
										parentNode : parent,
										oldEl : element,
										newEl : span
									};
									removeArray.push(obj);
									Ext.useShims = true;
									var cmp = {
										isOfficePanel : true,
										right : right,
										sortable : false,
										showToolbar : right == 1 ? false : true,
										width : width,
										height : height,
										fileId : element.value,
										doctype : "doc",
										readOnly : readable,
										unshowMenuBar : true
									};
									arrays.push("officeeditor" + index);
									map.add("officeeditor" + index, span);
									map.add("officeeditor" + index + "-cmp",
											cmp);
									map.add("officeeditor" + index + "-type",
											"officeeditor");
									if (element.value) {
										this.hiddenF.setValue(element.value);
										this.fileId = element.value;
									}
								} catch (e) {
								}
							} else {
								if (xtype == "userselector") {
									try {
										var span = document
												.createElement("div");
										var isSingle = element
												.getAttribute("issingle");
										var iscurrent = element
												.getAttribute("iscurrent");
										var hiddenF = new Ext.form.Hidden({
													value : iscurrent == 1
															? curUserInfo.userId
															: "",
													name : name + "UId"
												});
										if (jsonData) {
											var id = jsonData[name + "UId"];
											if (id) {
												hiddenF.setValue(id);
											}
										}
										hiddenF.render(span);
										var obj = {
											parentNode : parent,
											oldEl : element,
											newEl : span
										};
										removeArray.push(obj);
										var txtf = new Ext.form.TextField({
											name : name,
											margins : Ext.isChrome
													? "0 10 0 0"
													: "0 3 0 0",
											readOnly : true,
											sortable : false,
											value : curUserInfo.fullname,
											allowBlank : isNotNull == 1
													&& !readable ? false : true,
											width : width ? (width - 90 > 0
													? width - 90
													: width) : width
										});
										if (isSingle == 0) {
											txtf = new Ext.form.TextArea({
														name : name,
														margins : Ext.isChrome
																? "0 10 3 0"
																: "0 3 0 0",
														readOnly : true,
														sortable : false,
														allowBlank : isNotNull == 1
																&& !readable
																? false
																: true,
														value : curUserInfo.fullname,
														width : width
																? (width - 90 > 0
																		? width
																				- 90
																		: width)
																: width
													});
										}
										var cmp = new Ext.form.CompositeField({
											width : width,
											items : [txtf, {
												xtype : "button",
												width : 78,
												border : false,
												disabled : readable,
												text : "选择人员",
												iconCls : "btn-sel",
												handler : function() {
													new UserDialog({
														scope : this,
														single : isSingle == 1
																? true
																: false,
														callback : function(id,
																name) {
															txtf.setValue(name);
															hiddenF
																	.setValue(id);
														}
													}).show();
												}
											}]
										});
										arrays.push("userselector" + index);
										cmp.on("resize", function(c, width1,
														height, owidth) {
													cmp.setWidth(width);
												}, this);
										map.add("userselector" + index, span);
										map
												.add(	"userselector" + index
																+ "-cmp", cmp);
										map.add("userselector" + index
														+ "-type",
												"userselector");
										if (element.value) {
											txtf.setValue(element.value);
										}
									} catch (e) {
									}
								} else {
									if (xtype == "depselector") {
										var isSingle = element
												.getAttribute("issingle");
										var iscurrent = element
												.getAttribute("iscurrent");
										try {
											var span = document
													.createElement("div");
											var hiddenF = new Ext.form.Hidden({
														value : iscurrent == 1
																? App.auth.orgId
																: "",
														name : name + "Did"
													});
											if (jsonData) {
												var id = jsonData[name + "Did"];
												if (id) {
													hiddenF.setValue(id);
												}
											}
											hiddenF.render(span);
											var obj = {
												parentNode : parent,
												oldEl : element,
												newEl : span
											};
											removeArray.push(obj);
											var txtf = new Ext.form.TextField({
														name : name,
														margins : Ext.isChrome
																? "0 10 0 0"
																: "0 3 0 0",
														readOnly : true,
														sortable : false,
														value : curUserInfo.depName,
														allowBlank : isNotNull == 1
																&& !readable
																? false
																: true,
														width : width
																? (width - 90 > 0
																		? width
																				- 90
																		: width)
																: width
													});
											if (isSingle == 0) {
												txtf = new Ext.form.TextArea({
													name : name,
													sortable : false,
													margins : Ext.isChrome
															? "0 10 3 0"
															: "0 3 0 0",
													readOnly : true,
													value : curUserInfo.depName,
													allowBlank : isNotNull == 1
															&& !readable
															? false
															: true,
													width : width
															? (width - 90 > 0
																	? width
																			- 90
																	: width)
															: width
												});
											}
											var cmp = new Ext.form.CompositeField(
													{
														width : txtf.width + 80,
														items : [txtf, {
															xtype : "button",
															border : false,
															width : 78,
															disabled : readable,
															text : "选择组织",
															iconCls : "btn-users",
															handler : function() {
																DepSelector
																		.getView(
																				function(
																						id,
																						name) {
																					txtf
																							.setValue(name);
																					hiddenF
																							.setValue(id);
																				},
																				isSingle == 1
																						? true
																						: false)
																		.show();
															}
														}]
													});
											arrays.push("depselector" + index);
											cmp.on("resize", function(c,
															width1, height,
															owidth) {
														cmp.setWidth(width);
													}, this);
											map
													.add("depselector" + index,
															span);
											map.add("depselector" + index
															+ "-cmp", cmp);
											map.add("depselector" + index
															+ "-type",
													"depselector");
											if (element.value) {
												txtf.setValue(element.value);
											}
										} catch (e) {
										}
									} else {
										if (xtype == "posselector") {
											var isSingle = element
													.getAttribute("issingle");
											var iscurrent = element
													.getAttribute("iscurrent");
											try {
												var span = document
														.createElement("div");
												var hiddenF = new Ext.form.Hidden(
														{
															value : iscurrent == 1
																	? App.auth.orgId
																	: "",
															name : name + "Did"
														});
												if (jsonData) {
													var id = jsonData[name
															+ "Did"];
													if (id) {
														hiddenF.setValue(id);
													}
												}
												hiddenF.render(span);
												var obj = {
													parentNode : parent,
													oldEl : element,
													newEl : span
												};
												removeArray.push(obj);
												var txtf = new Ext.form.TextField(
														{
															name : name,
															sortable : false,
															margins : Ext.isChrome
																	? "0 10 0 0"
																	: "0 3 0 0",
															readOnly : true,
															value : curUserInfo.posName,
															allowBlank : isNotNull == 1
																	&& !readable
																	? false
																	: true,
															width : width
																	? (width
																			- 90 > 0
																			? width
																					- 90
																			: width)
																	: width
														});
												if (isSingle == 0) {
													txtf = new Ext.form.TextArea(
															{
																name : name,
																sortable : false,
																margins : Ext.isChrome
																		? "0 10 3 0"
																		: "0 3 0 0",
																readOnly : true,
																value : curUserInfo.posName,
																allowBlank : isNotNull == 1
																		&& !readable
																		? false
																		: true,
																width : width
																		? (width
																				- 90 > 0
																				? width
																						- 90
																				: width)
																		: width
															});
												}
												var cmp = new Ext.form.CompositeField(
														{
															width : txtf.width
																	+ 80,
															items : [txtf, {
																xtype : "button",
																border : false,
																width : 78,
																disabled : readable,
																text : "选择岗位",
																iconCls : "btn-users",
																handler : function() {
																	new PositionDialog(
																			{
																				scope : this,
																				single : isSingle == 1
																						? true
																						: false,
																				callback : function(
																						ids,
																						names) {
																					txtf
																							.setValue(names);
																					hiddenF
																							.setValue(ids);
																				}
																			})
																			.show();
																}
															}]
														});
												arrays.push("posselector"
														+ index);
												cmp
														.on(
																"resize",
																function(c,
																		width1,
																		height,
																		owidth) {
																	cmp
																			.setWidth(width);
																}, this);
												map.add("posselector" + index,
														span);
												map.add("posselector" + index
																+ "-cmp", cmp);
												map.add("posselector" + index
																+ "-type",
														"posselector");
												if (element.value) {
													txtf
															.setValue(element.value);
												}
											} catch (e) {
											}
										} else {
											if (xtype == "fileattach") {
												try {
													var span = document
															.createElement("div");
													span.setAttribute("style",
															"height:63px;");
													var hiddenF = new Ext.form.Hidden(
															{
																name : name
															});
													hiddenF.render(span);
													var obj = {
														parentNode : parent,
														oldEl : element,
														newEl : span
													};
													removeArray.push(obj);
													var r = width
															? (width - 90 > 0
																	? width
																			- 90
																	: width)
															: width;
													var txtf = new Ext.Panel({
																bodyStyle : "width:"
																		+ r
																		+ "px;",
																height : 60,
																margins : Ext.isChrome
																		? "0 3 3 0"
																		: "0 3 3 0",
																border : true,
																autoScroll : true,
																html : ""
															});
													var cmp = new Ext.form.CompositeField(
															{
																width : r + 84,
																items : [txtf,
																		{
																			xtype : "button",
																			width : 78,
																			disabled : readable,
																			text : "选择附件",
																			iconCls : "menu-attachment",
																			handler : function() {
																				var dialog = App
																						.createUploadDialog(
																								{
																									file_cat : "flow",
																									callback : function(
																											data) {
																										for (var i = 0; i < data.length; i++) {
																											if (hiddenF
																													.getValue() != "") {
																												hiddenF
																														.setValue(hiddenF
																																.getValue()
																																+ ",");
																											}
																											hiddenF
																													.setValue(hiddenF
																															.getValue()
																															+ data[i].fileId
																															+ "|"
																															+ data[i].fileName);
																											Ext.DomHelper
																													.append(
																															txtf.body,
																															'<span><a href="####" onclick="FileAttachDetail.show('
																																	+ data[i].fileId
																																	+ ')">'
																																	+ data[i].fileName
																																	+ '</a> <img class="img-delete" src="'
																																	+ __ctxPath
																																	+ '/images/system/delete.gif" onclick="AppUtil.removeFile(this,'
																																	+ data[i].fileId
																																	+ ')"/>&nbsp;|&nbsp;</span>');
																										}
																									}
																								});
																				dialog
																						.show(this);
																			}
																		}]
															});
													cmp.on("resize", function(
																	c, width1,
																	height,
																	owidth) {
																cmp
																		.setWidth(width);
															}, this);
													arrays.push("fileattach"
															+ index);
													map.add("fileattach"
																	+ index,
															span);
													map.add("fileattach"
																	+ index
																	+ "-cmp",
															cmp);
													map.add("fileattach"
																	+ index
																	+ "-type",
															"fileattach");
													AppUtil.removeFile = function(
															obj, fileId,
															fileName) {
														var fileIds = hiddenF;
														var value = fileIds
																.getValue();
														if (value.indexOf(",") < 0) {
															fileIds
																	.setValue("");
														} else {
															value = value
																	.replace(
																			","
																					+ fileId
																					+ "|"
																					+ fileName,
																			"")
																	.replace(
																			fileId
																					+ "|"
																					+ fileName
																					+ ",",
																			"");
															fileIds
																	.setValue(value);
														}
														var el = Ext
																.get(obj.parentNode);
														el.remove();
													};
													cmp.on("render",
															function() {
																if (element.value) {
																	hiddenF
																			.setValue(element.value);
																	var filea = element.value
																			.split(",");
																	for (var i = 0; i < filea.length; i++) {
																		var ss = filea[i];
																		var as = ss
																				.split("|");
																		var fileId = as[0];
																		var fileName = as[1];
																		var delImg = "";
																		if (!readable) {
																			delImg = '<img class="img-delete" src="'
																					+ __ctxPath
																					+ '/images/system/delete.gif" onclick="AppUtil.removeFile(this,'
																					+ fileId
																					+ ",'"
																					+ fileName
																					+ "')\"/>";
																		}
																		Ext.DomHelper
																				.append(
																						txtf.body,
																						'<span><a href="####" onclick="FileAttachDetail.show('
																								+ fileId
																								+ ')">'
																								+ fileName
																								+ "</a> "
																								+ delImg
																								+ "&nbsp;|&nbsp;</span>");
																	}
																}
															}, this);
												} catch (e) {
												}
											} else {
												if (readable) {
													element.setAttribute(
															"readOnly", "true");
													element
															.setAttribute(
																	"txtisnotnull",
																	"0");
													element.readOnly = true;
													element.onclick = function() {
														return false;
													};
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (right && right == 0) {
			element.style.display = "none";
			var p = document.createElement("div");
			p.setAttribute("style", "width:" + element.style.width + ";height:"
							+ element.style.height > 23
							? element.style.height
							: 23 + ";background-color:#E9E9E9");
			p.innerHTML = '<div style="height:23px;"><font color="red" align="center">无权限</font></div>';
			parent.insertBefore(p, element);
		}
		element.onblur = function() {
			$validField.call(this, element);
		};
	}, this);
	var valueEl = sumTotal.valueEl;
	var opersEl = sumTotal.operEls;
	var operType = sumTotal.operaType;
	for (var b = 0; b < opersEl.length; b++) {
		var gel = opersEl[b];
		gel.onblur = function() {
			if (isNaN(opersEl[0].value)) {
				opersEl[0].value = 0;
				return false;
			}
			var q = opersEl[1];
			var invalidClass = " x-form-invalid";
			var oldClass = q.getAttribute("class");
			if (isNaN(opersEl[1].value) || opersEl[1].value > 12) {
				opersEl[1].value = 0;
				if (oldClass) {
					if (oldClass.indexOf(invalidClass) == -1) {
						oldClass = oldClass + invalidClass;
					}
				} else {
					oldClass = invalidClass;
				}
				q.setAttribute("class", oldClass);
				q.qclass = "x-form-invalid-tip";
				q.qtip = "该值不能超过12";
				Ext.ux.Toast.msg("表单验证信息", "该值不能超过12");
				return false;
			} else {
				if (oldClass) {
					q.setAttribute("class", oldClass.replace(invalidClass, ""));
				}
				q.qtip = "";
				q.qclass = "";
			}
			var valuew = 0;
			var val1 = parseFloat(opersEl[0].value);
			var val2 = parseFloat(opersEl[1].value);
			if (isNaN(val1)) {
				val1 = 0;
			}
			if (isNaN(val2)) {
				val2 = 0;
			}
			var str = String.format("{0}" + operType + "{1}", val1, val2);
			valuew = eval(str);
			valueEl.value = parseFloat((valuew == NaN) ? 0 : valuew).toFixed(3);
			var sum = document.getElementsByName("sum");
			var elss = document.getElementsByName(valueEl.name);
			var dval = 0;
			for (var q = 0; q < elss.length; q++) {
				var qel = elss[q];
				var qval = qel.value;
				if (isNaN(qval)) {
					qval = 0;
				}
				dval += parseFloat(qval);
			}
			sum[0].value = dval.toFixed(3);
		};
	}
	for (var g = 0; g < removeArray.length; g++) {
		var obj = removeArray[g];
		try {
			obj.parentNode.replaceChild(obj.newEl, obj.oldEl);
		} catch (e) {
			alert(e);
		}
	}
	if (arrays.length > 0 && map.length > 0) {
		Ext.each(arrays, function(its, index) {
					var cmp = map.get(its + "-cmp");
					var span = map.get(its);
					var type = map.get(its + "-type");
					if (cmp.isOfficePanel) {
						mySubmitType = "ProcessRunStart";
						var o = new NtkOfficePanel(cmp);
						if (!o.flag) {
							Ext.ux.Toast.msg("提示信息", o.msg);
							return;
						}
						o.panel.on("resize",
								function(c, width1, height, owidth) {
									o.panel.setWidth(cmp.width);
								}, this);
						o.panel.render(span);
						this.officePanel = o;
						myNewOffice = o;
					} else {
						try {
							var divs = document.createElement("div");
							var div22 = document.createElement("div");
							divs.appendChild(div22);
							span.appendChild(divs);
							cmp.render(div22);
							if (type == "fckeditor") {
								formPanel.add(cmp);
							}
						} catch (e) {
							alert(e);
						}
					}
				}, this);
	}
	return flag;
}
var validForm = {
	isValid : true,
	messge : "",
	el : null
};
/*FileAttachDetail.setGrids = function(a) {
	this.grids = a;
};
FileAttachDetail.delFile = function(c, g, b, h) {
	var e = this.grids.get(c);
	var d = e.getSelectionModel().getSelections();
	var a = d[0];
	var f = a.get(g);
	if (f.indexOf(",") < 0) {
		a.set(g, "");
	} else {
		f = f.replace("," + b + "|" + h, "").replace(b + "|" + h + ",", "");
		a.set(g, f);
	}
};
FileAttachDetail.upload = function(b, f) {
	var e = this.grids.get(b);
	var d = e.getSelectionModel().getSelections();
	var a = d[0];
	var c = App.createUploadDialog({
				file_cat : "flow",
				callback : function(h) {
					var k = a.get(f);
					if (k) {
						k += ",";
					} else {
						k = "";
					}
					var j = "";
					for (var g = 0; g < h.length; g++) {
						if (g > 0) {
							j += ",";
						}
						j += h[g].fileId + "|" + h[g].fileName;
					}
					a.set(f, k + j);
				}
			});
	c.show(this);
};*/
function $converDetail(jsonData, rightJson, readOnly, subRightjson) {
	var form = this.formPanel.getForm().getEl().dom;
	var tables = form.getElementsByTagName("table");
	this.detailGrids = new Ext.util.MixedCollection();
	var formobjs = [];
	var removeTables = [];
	this.formValidCmp = new Array();
	for (var i = 0; i < tables.length; i++) {
		var isdetail = tables[i].getAttribute("isdetail");
		var isgrid = tables[i].getAttribute("isgrid");
		var gridName = tables[i].getAttribute("txtname");
		var parent = tables[i].parentNode;
		if (isdetail != null && "true" == isgrid) {
			var hide = rightJson && rightJson[gridName];
			if (!hide || hide == 0) {
				var detailPanel = $converDetailGrid.call(this, tables[i],
						gridName, rightJson, readOnly, subRightjson);
				this.detailGrids.add(gridName, detailPanel);
				if (jsonData) {
					var data = jsonData["WF_" + gridName + "s"];
					if (data) {
						detailPanel.getStore().loadData({
									result : data
								});
					}
				}
			} else {
				var p = document.createElement("div");
				p.setAttribute("style", "width:" + tables[i].style.width
								+ ";height:" + tables[i].style.height
								+ ";background-color:#E9E9E9");
				p.innerHTML = '<font color="red">无权限</font>';
				parent.insertBefore(p, tables[i]);
			}
			removeTables.push(tables[i]);
		} else {
			if (isdetail != null && "false" == isgrid) {
				try {
					var hide = rightJson && rightJson[gridName];
					if (!hide || hide == 0) {
						var names = $getTableInputCmpName(tables[i]);
						var datas;
						var pkName;
						if (this.taskId) {
							var pkKey = document.getElementById("WF_"
									+ gridName + "_" + this.taskId);
							var pkKeyVar = pkKey.value;
							if (jsonData) {
								datas = jsonData["WF_" + gridName + "s"];
								pkName = pkKeyVar;
								if (false && datas) {
									var obj = data[0];
									var flag = false;
									var pkKeyValue = obj[pkKeyVar];
									for (var w = 0; w < names.length; w++) {
										if (names[w] == pkKeyVar) {
											flag = true;
										}
										jsonData[names[w]] = obj[names[w]];
									}
								}
							}
						} else {
							if (this.runId) {
								var pkKey = document.getElementById("WF_"
										+ gridName + "_" + this.runId);
								var pkKeyVar = pkKey.value;
								if (jsonData) {
									datas = jsonData["WF_" + gridName + "s"];
									pkName = pkKeyVar;
								}
							}
						}
						var attributes = tables[i].attributes;
						var tableHtml = "<table ";
						for (var v = 0; v < attributes.length; v++) {
							var d = attributes[v];
							if (typeof d == "object") {
								tableHtml += " " + d.name + "='" + d.value
										+ "' ";
							}
						}
						tableHtml += ">";
						tableHtml += tables[i].innerHTML;
						tableHtml += "</table>";
						var html = tableHtml;
						var obj = {
							innerhtml : html,
							parentNode : parent,
							gridName : gridName,
							elsName : names,
							jsonDatas : datas,
							pkName : pkName,
							sortable : false,
							rightJson : rightJson,
							readOnly : readOnly
						};
						var cfunction = tables[i]
								.getAttribute("delformfunction");
						if (cfunction) {
							try {
								var callf = eval(cfunction);
								if (callf) {
									obj["delformfunction"] = callf;
								}
							} catch (e) {
							}
						}
						formobjs.push(obj);
					} else {
						var p = document.createElement("div");
						p.setAttribute("style", "width:"
										+ tables[i].style.width + ";height:"
										+ tables[i].style.height
										+ ";background-color:#E9E9E9");
						p.innerHTML = '<font color="red">无权限</font>';
						parent.insertBefore(p, tables[i]);
					}
					removeTables.push(tables[i]);
				} catch (e) {
					alert(e);
				}
			}
		}
	}
	FileAttachDetail.setGrids(this.detailGrids);
	for (var i = 0; i < removeTables.length; i++) {
		var table = removeTables[i];
		var parent = table.parentNode;
		parent.removeChild(table);
	}
	var fElements = form.elements
			|| (document.forms[form] || Ext.getDom(form)).elements;
	$converCmp.call(this, fElements, jsonData, rightJson, false, readOnly);
	$converFormDetail.call(this, formobjs, readOnly);
}
function $checkSubButtonRight(b) {
	if (!b) {
		return true;
	}
	var a = true;
	Ext.Ajax.request({
				url : __ctxPath + "/flow/checkRightFormButtonRight.do",
				params : {
					rightId : b
				},
				method : "POST",
				async : false,
				success : function(c, d) {
					var e = Ext.decode(c.responseText);
					a = e.success;
				},
				failure : function(c, d) {
					a = false;
				}
			});
	if (a) {
		return a;
	}
	return false;
}
function $converDetailGrid(u, v, j, q, m) {
	var i = u.parentNode;
	var g = u.getAttribute("txtname");
	var p = [];
	var c = [];
	if (this.taskId) {
		var t = document.getElementById("WF_" + v + "_" + this.taskId);
		if (t != null && t != "" && t != undefined) {
			var y = t.value;
			p.push(y);
			c.push({
						dataIndex : y,
						header : y,
						hidden : true,
						sortable : false
					});
		}
	}
	var z = $getConfFromTable.call(this, u);
	var n = z.keys;
	var r = 0;
	var w = true, e = true;
	for (var s = 0; s < n.length; s++) {
		var h = z.get(n[s]);
		Ext.apply(h, {
					gridName : v
				});
		var x = null;
		if (j && j[n[s]]) {
			x = j[n[s]];
		}
		if (x == 1) {
			r--;
			h["uneditable"] = true;
		} else {
			if (x == 3) {
				h.isNotNull = true;
				r++;
			} else {
				r++;
			}
		}
		if (q) {
			h["uneditable"] = true;
		}
		if (h.xtype == "datecolumn") {
			p.push({
						name : n[s],
						sortable : false,
						type : "date",
						isCurrent : h.iscurrent,
						defaultValue : h.defaultValue,
						dateFormat : h.format,
						allowBlank : h.isNotNull == true ? false : true,
						header : h.header,
						callbackfunction : h.callbackfunction,
						changefunction : h.changefunction
					});
		} else {
			p.push({
						name : n[s],
						sortable : false,
						allowBlank : h.isNotNull == true ? false : true,
						header : h.header,
						defaultValue : h.defaultValue,
						callbackfunction : h.callbackfunction,
						changefunction : h.changefunction
					});
		}
		if (x != 0) {
			r--;
			if (h.dxtype == "fileattach") {
				h["renderer"] = $renderAttach;
			}
			if (h.dxtype == "diccombo") {
				h["itemname"] = h.itemsname;
				h.returnName = true;
			}
			c.push(h);
		}
	}
	var l = document.createElement("div");
	i.appendChild(l);
	var b = new Ext.Toolbar({
				disabled : q == true ? true : false,
				frame : true,
				items : []
			});
	var o = new Ext.Button({
				text : "添加记录",
				iconCls : "btn-add",
				scope : this,
				gridName : v,
				handler : function(D) {
					var C = this.detailGrids.get(D.gridName);
					var F = C.getStore().recordType;
					var A = new F();
					var f = A.fields.items;
					for (var B = 0; B < f.length; B++) {
						var E = f[B];
						if (E.defaultValue) {
							A.set(E.name, E.defaultValue);
						}
					}
					C.getStore().add(A);
				}
			});
	var a = new Ext.Button({
				text : "删除记录",
				iconCls : "btn-del",
				scope : this,
				gridName : v,
				handler : $detailDel
			});
	if (m) {
		w = $checkSubButtonRight(m[g + "-1"]);
		e = $checkSubButtonRight(m[g + "-2"]);
	}
	if (w) {
		b.addItem(o);
	}
	if (e) {
		b.addItem(a);
	}
	var k = new HT.EditorGridPanel({
				renderTo : l,
				hiddenSm : q,
				tbar : new Ext.Toolbar({
							disabled : q == true ? true : false,
							frame : true,
							items : [b]
						}),
				clicksToEdit : 1,
				width : u.offsetWidth,
				showPaging : false,
				autoHeight : true,
				fields : p,
				columns : c,
				listeners : {
					scope : this,
					"rowclick" : function(A, f, B) {
						this.clickRow = f;
					},
					"cellclick" : function(A, E, f, D) {
						if (!q) {
							var B = A.getColumnModel().getColumnById(f);
							if (B && B.dxtype == "fileattach") {
								return;
							}
							var C = $converCmpInColumn.call(this, B, E);
							if (B.dxtype == "diccombo") {
								B.renderer = function(I, G, H) {
									if (B.returnName) {
										return I;
									}
									var J = C.getStore();
									var F = J.find("itemId", I);
									if (F != -1) {
										return J.getAt(F).data.itemName;
									}
									return H.get("itemName");
								};
							}
							if (B && C) {
								B.setEditor(C);
							}
						}
					}
				}
			});
	if (!q) {
		var d = k.getStore();
		d.on("update", $changeRecord, this);
		d.on("remove", $changeRecord, this);
	}
	return k;
}
function $renderAttach(k, b, g, j, n, l) {
	var c = this.uneditable;
	var i = "<span>";
	if (k) {
		var a = k.split(",");
		if (a) {
			for (var h = 0; h < a.length; h++) {
				var m = a[h].split("|");
				var d = m[0];
				var e = m[1];
				i += '<a href="####" onclick="FileAttachDetail.show(' + d + ')">'
						+ e + "</a>";
				if (!c) {
					i += '<img class="img-delete" src="'
							+ __ctxPath
							+ '/images/system/delete.gif" title="删除附件" qtip="删除附件" onclick="FileAttachDetail.delFile(\''
							+ this.gridName + "','" + this.dataIndex + "'," + d
							+ ",'" + e + "')\"/>" + "&nbsp;&nbsp;";
				}
				i += "<br/>";
			}
		}
	}
	if (!c) {
		i += '<a href="####" onclick="FileAttachDetail.upload(\'' + this.gridName
				+ "','" + this.dataIndex + "')\">添加</a></span>";
	}
	return i;
}
function $detailDel(d) {
	var b = this.detailGrids.get(d.gridName);
	var a = d.gridName;
	var c = this.taskId;
	Ext.Msg.confirm("信息确认", "您确认要删除所选记录吗？", function(f) {
				if (f == "yes") {
					var j = null;
					if (c) {
						j = document.getElementById(a + "_" + c).value;
					}
					var o = b.getStore();
					var m = b.getSelectionModel().getSelections();
					var e = [];
					var g = [];
					var h = document.getElementById("WF_" + a + "_" + c);
					var n;
					if (h) {
						n = h.value;
					}
					for (var l = 0; l < m.length; l++) {
						if (m[l].data != null) {
							if (n) {
								var k = m[l].data[n];
								if (k) {
									e.push(k);
								}
							}
							g.push(m[l]);
						}
					}
					if (e.length) {
						Ext.Ajax.request({
									url : __ctxPath
											+ "/flow/delItemsProcessActivity.do",
									params : {
										tableId : j,
										ids : e
									},
									method : "POST",
									success : function(i, p) {
										Ext.ux.Toast.msg("操作信息", "成功删除该记录！");
										o.remove(g);
									},
									failure : function(i, p) {
										Ext.ux.Toast
												.msg("操作信息", "操作出错，请联系管理员！");
									}
								});
					} else {
						o.remove(g);
					}
				}
			});
}
function $changeRecord(store, record) {
	return record.fields.find(function(f) {
				if (f.changefunction !== undefined) {
					var funName = f.changefunction;
					var fun = eval(funName);
					if (typeof fun == "function") {
						fun.call(this, store, record, record.data[f.name],
								f.name, f.header, this.activityName);
						return true;
					}
				}
			}, this);
}
function $converCmpInColumn(c, i) {
	var e = null;
	if (!c || c.uneditable == true) {
		return e;
	}
	var a = this;
	var b = c.dxtype;
	switch (b) {
		case "numberfield" :
			var h = c.format;
			var g = 2;
			var f = 0.01;
			var d = 10000000000000;
			if (h) {
				g = h.lastIndexOf(".");
				if (g != -1 && g < h.length) {
					g = h.length - g - 1;
				} else {
					if (g == -1) {
						g = 0;
						f = 0;
						h.length;
					}
				}
			}
			e = new Ext.form.NumberField({
						sortable : false,
						allowBlank : c.isNotNull == true ? false : true,
						decimalPrecision : g,
						minValue : f,
						maxValue : d
					});
			break;
		case "datefield" :
			if (c.format == "Y-m-d") {
				e = new Ext.form.DateField({
							sortable : false,
							format : "Y-m-d",
							value : c.iscurrent ? new Date() : null,
							allowBlank : c.isNotNull == true ? false : true
						});
			} else {
				e = new Cls.form.DateTimeField({
							sortable : false,
							format : "Y-m-d H:i:s",
							value : c.iscurrent ? new Date() : null,
							allowBlank : c.isNotNull == true ? false : true
						});
			}
			break;
		case "userselector" :
			e = new Ext.form.TriggerField({
						triggerClass : "x-form-browse-trigger",
						gridName : c.gridName,
						isSingle : c.isSingle,
						dataIndexName : c.dataIndex,
						editable : false,
						sortable : false,
						value : c.iscurrent ? App.auth.fullName : "",
						allowBlank : c.isNotNull == true ? false : true,
						onTriggerClick : function(l) {
							var j = a.detailGrids.get(this.gridName);
							var k = this.dataIndexName;
							new UserDialog({
										scope : this,
										single : this.isSingle,
										callback : function(p, o) {
											var n = j.getStore();
											var m = n.getAt(i);
											m.set(k, o);
										}
									}).show();
							j.stopEditing();
						}
					});
			break;
		case "depselector" :
			e = new Ext.form.TriggerField({
						triggerClass : "x-form-browse-trigger",
						gridName : c.gridName,
						isSingle : c.isSingle,
						dataIndexName : c.dataIndex,
						editable : false,
						sortable : false,
						allowBlank : c.isNotNull == true ? false : true,
						onTriggerClick : function(l) {
							var j = a.detailGrids.get(this.gridName);
							var k = this.dataIndexName;
							DepSelector.getView(function(p, o) {
										var n = j.getStore();
										var m = n.getAt(i);
										m.set(k, o);
									}, this.isSingle).show();
							j.stopEditing();
						}
					});
			break;
		case "posselector" :
			e = new Ext.form.TriggerField({
						triggerClass : "x-form-browse-trigger",
						gridName : c.gridName,
						isSingle : c.isSingle,
						dataIndexName : c.dataIndex,
						editable : false,
						sortable : false,
						allowBlank : c.isNotNull == true ? false : true,
						onTriggerClick : function(l) {
							var j = a.detailGrids.get(this.gridName);
							var k = this.dataIndexName;
							new PositionDialog({
										scope : this,
										single : this.isSingle,
										callback : function(o, p) {
											var n = j.getStore();
											var m = n.getAt(i);
											m.set(k, p);
										}
									}).show();
							j.stopEditing();
						}
					});
			break;
		case "comboselect" :
			e = new Ext.form.ComboBox({
						mode : "local",
						triggerAction : "all",
						gridName : c.gridName,
						dataIndexName : c.dataIndex,
						editable : false,
						sortable : false,
						allowBlank : c.isNotNull == true ? false : true,
						store : new Ext.data.SimpleStore({
									fields : ["code", "value"],
									data : c.datas
								}),
						displayField : "value",
						valueField : "value"
					});
			break;
		case "diccombo" :
			e = new DicCombo({
						dataIndexName : c.dataIndex,
						gridName : c.gridName,
						readOnly : c.readable,
						proTypeId : c.proTypeId,
						width : c.width * 0.9,
						sortable : false,
						allowBlank : c.isNotNull == 1 && !c.readable
								? false
								: true,
						isDisplayItemName : true,
						returnName : c.returnName
					});
			break;
		case "fileattach" :
			break;
		default :
			e = new Ext.form.TextField({
						allowBlank : c.isNotNull == true ? false : true,
						sortable : false
					});
			break;
	}
	return e;
}
function $converForm(n, q) {
	var h = n.innerhtml;
	var s = n.parentNode;
	var c = n.gridName;
	var l = n.rightJson;
	var b = document.createElement("div");
	b.setAttribute("style", "border:1px solid #C1DAD7;");
	s.appendChild(b);
	var j = n.jsonDatas;
	var g = n.pkName;
	var o = true;
	if (j && g) {
		for (var d = 0; d < j.length; d++) {
			var p = document.createElement("div");
			p.setAttribute("style", "border:1px solid #99BBE8;margin:3px;");
			b.appendChild(p);
			p.setAttribute("class", "tipDiv");
			var t = document.createElement("form");
			t.setAttribute("belongName", c);
			t.setAttribute("pkName", g);
			t.setAttribute("pkValue", j[d][g]);
			if (!q) {
				p.appendChild($addDelButton(b, p, c, t, this.taskId, j[d][g],
						n.delformfunction));
			}
			t.innerHTML = h;
			try {
				var a = $converCmp.call(this, t.elements, j[d], l, true, q);
				if (a == "un") {
					o = false;
				}
			} catch (m) {
				alert(m);
			}
			p.appendChild(t);
		}
	} else {
		var p = document.createElement("div");
		p.setAttribute("style", "border:1px solid #99BBE8;margin:3px;");
		var t = document.createElement("form");
		t.setAttribute("belongName", c);
		if (!q) {
			p.appendChild($addDelButton(b, p, c, t, this.taskId, null,
					n.delformfunction));
		}
		t.innerHTML = h;
		try {
			var a = $converCmp.call(this, t.elements, null, l, true, q);
			if (a == "un") {
				o = false;
			}
		} catch (m) {
		}
		p.appendChild(t);
		b.appendChild(p);
	}
	if (o && !q) {
		var k = document.createElement("div");
		b.appendChild(k);
		var f = new Ext.Button({
			renderTo : k,
			text : "添加",
			tableHtml : h,
			gridName : c,
			addButtonDiv : k,
			parentNode : b,
			rightJson : l,
			iconCls : "btn-add",
			delformfunction : n.delformfunction,
			scope : this,
			handler : function(e) {
				var r = document.createElement("div");
				r.setAttribute("style", "border:1px solid #99BBE8;margin:3px;");
				e.parentNode.insertBefore(r, e.addButtonDiv);
				var i = document.createElement("form");
				i.setAttribute("belongName", c);
				i.innerHTML = h;
				$converCmp.call(this, i.elements, null, e.rightJson, true);
				r.appendChild($addDelButton(e.parentNode, r, c, i, null, null,
						e.delformfunction));
				r.appendChild(i);
			}
		});
	}
}
function $converFormDetail(a, c) {
	for (var b = 0; b < a.length; b++) {
		$converForm.call(this, a[b], c);
	}
}
function $addDelButton(a, g, d, b, f, c, e) {
	var i = document.createElement("b");
	var h = document.createElement("div");
	h.setAttribute("class", "x-btn-text btn-del");
	h.setAttribute("style", "float:right;height:20px;width:20px;right:-20px;");
	h.qtip = "删除";
	h.owerDiv = a;
	h.removeDiv = g;
	h.gridName = d;
	h.taskId = f;
	h.targetForm = b;
	h.pkValue = c;
	h.delfunction = e;
	h.onclick = function() {
		try {
			Ext.Msg.confirm("信息确认", "您确认要删除所选记录吗？", function(m) {
				if (m == "yes") {
					if (this.pkValue) {
						var n = null;
						if (f) {
							n = document.getElementById(this.gridName + "_"
									+ this.taskId).value;
						}
						Ext.Ajax.request({
							url : __ctxPath
									+ "/flow/delItemsProcessActivity.do",
							params : {
								tableId : n,
								ids : this.pkValue
							},
							method : "POST",
							scope : this,
							success : function(q, s) {
								Ext.ux.Toast.msg("操作信息", "成功删除该记录！");
								var t = Ext.Ajax.serializeForm(this.targetForm);
								var r = Ext.urlDecode(t);
								var p = this.owerDiv.childNodes.length - 1;
								if (this.delfunction) {
									this.delfunction.call(this, r, p);
								}
								this.owerDiv.removeChild(this.removeDiv);
							},
							failure : function(p, q) {
								Ext.ux.Toast.msg("操作信息", "操作出错，请联系管理员！");
							}
						});
					} else {
						var o = Ext.Ajax.serializeForm(this.targetForm);
						var l = Ext.urlDecode(o);
						var k = this.owerDiv.childNodes.length - 1;
						if (this.delfunction) {
							this.delfunction.call(this, l, k);
						}
						this.owerDiv.removeChild(this.removeDiv);
					}
				}
			}, this);
		} catch (j) {
			alert(j);
		}
	};
	i.appendChild(h);
	return i;
}
function $validField(e) {
	if (e.style.display == "none") {
		return true;
	}
	var l = e.getAttribute("txtisnotnull");
	var d = e.getAttribute("xtype");
	var k = e.getAttribute("txtsize");
	var g = e.getAttribute("dataformat");
	var n = true;
	var c;
	if (l == 1) {
		if (e.value == "") {
			c = "此选项为必填项";
			n = false;
		}
	}
	if (n && k && e.value.toString().length > k) {
		c = "此项内容不得超过" + k;
		n = false;
	}
	if (false && n) {
		var m = e.value;
		if (m != "") {
			var j = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			n = j.test(m);
			c = "此项内容为邮件格式：XXX@XX.com";
		}
	}
	if (g && n) {
		var m = e.value;
		if (m != "") {
			var b = new RegExp(g);
			n = b.test(m);
			c = "此项内容的格式不正确";
		}
	}
	if (d == "numberfield" && n) {
		var m = e.value;
		var a = e.getAttribute("txtvaluetype");
		if (m != "") {
			if (a == "int" || a == "bigint" || a == "smallint") {
				var i = /^[-\+]?\d+$/;
				n = i.test(m);
				c = "此项内容应为整数";
			} else {
				var h = /^-?\d+\.?\d*$/;
				n = h.test(m);
				c = "此项内容应为数字";
			}
		}
	}
	var o = " x-form-invalid";
	var f = e.getAttribute("class");
	if (!n) {
		if (f) {
			if (f.indexOf(o) == -1) {
				f = f + o;
			}
		} else {
			f = o;
		}
		e.setAttribute("class", f);
		e.qtip = c;
		e.qclass = "x-form-invalid-tip";
		return false;
	} else {
		if (f) {
			e.setAttribute("class", f.replace(o, ""));
		}
		e.qtip = "";
		e.qclass = "";
		return true;
	}
}
function $validForm() {
	var c = this.formPanel.getForm().getEl().dom;
	var a = c.elements || (document.forms[c] || Ext.getDom(c)).elements;
	var s = true;
	Ext.each(a, function(i, f) {
				s = s && $validField.call(this, i);
			});
	var h = c.getElementsByTagName("form");
	for (var u = 0; u < h.length; u++) {
		var v = h[u];
		var g = v.elements;
		Ext.each(g, function(i, f) {
					s = s && $validField.call(this, i);
				});
	}
	validForm = {
		isValid : true,
		messge : "",
		el : null
	};
	if (typeof checkElementFunction == "function") {
		var o = checkElementFunction(this.activityName);
		if (typeof o == "object") {
			validForm = o;
		} else {
			if (typeof o == "boolean") {
				if (!o) {
					Ext.ux.Toast.msg("表单验证信息", "有信息未填写");
					return false;
				}
			} else {
				if (typeof o == "string") {
					if (o) {
						Ext.ux.Toast.msg("表单验证信息", o);
						return false;
					}
				}
			}
		}
		if (!validForm.isValid) {
			if (validForm.els) {
				var d = validForm.els[0];
				var r = " x-form-invalid";
				if (d) {
					var n = d.getAttribute("class");
					if (n) {
						if (n.indexOf(r) == -1) {
							n = n + r;
						}
					} else {
						n = r;
					}
					d.setAttribute("class", n);
					d.qtip = validForm.messge
							? validForm.message
							: validForm.errorMsg;
					d.qclass = "x-form-invalid-tip";
				}
				var l = validForm.messge
						? validForm.message
						: validForm.errorMsg;
				Ext.ux.Toast.msg("表单验证信息", l ? l : "验证不通过！");
			}
			return false;
		}
	}
	var q = false;
	if (this.detailGrids) {
		var k = this.detailGrids.keys;
		for (var t = 0; t < k.length; t++) {
			var p = this.detailGrids.get(k[t]);
			var m = p.getStore();
			for (var u = 0; u < m.getCount(); u++) {
				var e = m.getAt(u);
				var b = $isValidField.call(this, m, e, this.activityName);
				if (b) {
					p.getSelectionModel().selectRecords([e]);
					Ext.ux.Toast.msg("表单验证信息", b.errorMsg
									? b.errorMsg
									: "列表验证出错");
					q = true;
					break;
				}
			}
			if (q) {
				break;
			}
		}
	}
	if (q) {
		s = false;
	}
	if (s) {
		return true;
	} else {
		return false;
	}
}
function $isValidField(b, a, d) {
	if (a != null) {
		var c = a.fields.find(function(e) {
					if (e.allowBlank === false && Ext.isEmpty(a.data[e.name])) {
						e["errorMsg"] = e.header + "不能为空";
						return e;
					} else {
						if (typeof e.callbackfunction == "function") {
							var g = e.callbackfunction.call(this, b, a,
									a.data[e.name], e.name, e.header, d);
							if (g && (g.passed == false)) {
								e.errorMsg = g.errorMsg;
								return e;
							}
						}
					}
				}, this);
	} else {
		var c = b.fields.find(function(e) {
					if (typeof e.callbackfunction == "function") {
						var g = e.callbackfunction.call(this, b, null, null,
								e.name, e.header, d);
						if (g && (g.passed == false)) {
							e.errorMsg = g.errorMsg;
							return e;
						}
					}
				}, this);
	}
	return c;
}
function $checkDeleValid(d, e) {
	var c = d.elements || (document.forms[d] || Ext.getDom(d)).elements;
	var a = new Array();
	Ext.each(c, function(g, f) {
				if (g.type == "radio" || g.type == "checkbox") {
					var h = g.getAttribute("name");
					if (!g.checked && g.hasAttribute("isSelect")) {
						a[h] = 1;
					} else {
						if (g.checked && a[h]) {
							a[h] = 0;
						}
					}
				}
			});
	for (var b in a) {
		if (a[b] == 1) {
			e[b] = "null";
		}
	}
}
function setHTInterval(a, c) {
	try {
		clearInterval(interval);
	} catch (b) {
	}
	interval = window.setInterval(a, c);
}