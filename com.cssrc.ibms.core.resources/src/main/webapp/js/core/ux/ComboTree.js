Orient.ComboTree = Ext.extend(Ext.form.TriggerField, {
			triggerClass : "x-form-arrow-trigger",
			shadow : "sides",
			lazyInit : true,
			currNode : null,
			initComponent : function() {
				Orient.ComboTree.superclass.initComponent.call(this);
				this.addEvents("expand", "collapse", "beforeselect", "select");
			},
			onRender : function(b, a) {
				Orient.ComboTree.superclass.onRender.call(this, b, a);
				this.name = this.name ? this.name : this.hiddenName;
				if (this.hiddenName) {
					this.hiddenField = this.el.insertSibling({
								tag : "input",
								type : "hidden",
								name : this.hiddenName,
								id : (this.hiddenId || this.hiddenName)
							}, "before", true);
					this.el.dom.removeAttribute("name");
				}
				if (Ext.isGecko) {
					this.el.dom.setAttribute("autocomplete", "off");
				}
				if (!this.lazyInit) {
					this.initList();
				} else {
					this.on("focus", this.initList, this, {
								single : true
							});
				}
				if (!this.editable) {
					this.editable = true;
					this.setEditable(false);
				}
			},
			initValue : function() {
				Orient.ComboTree.superclass.initValue.call(this);
				if (this.hiddenField) {
					this.hiddenField.value = this.hiddenValue !== undefined
							? this.hiddenValue
							: this.value !== undefined ? this.value : "";
				}
			},
			initList : function() {
				if (this.list) {
					return;
				}
				this.list = new Ext.Layer({
							cls : "x-combo-list",
							constrain : false
						});
				this.list.setWidth(Math.max(this.wrap.getWidth(), 70));
				this.mon(this.list, "mouseover", this.onViewOver, this);
				if (!this.tree) {
					this.root = new Ext.tree.AsyncTreeNode({
								expanded : true
							});
					this.loader = this.loader
							? this.loader
							: new Ext.tree.TreeLoader({
										url : this.url
									});
					this.tree = new Ext.tree.TreePanel({
								autoScroll : true,
								height : 200,
								border : false,
								root : this.root,
								rootVisible : false,
								loader : this.loader
							});
					delete this.loader;
				}
				this.tree.on({
							scope : this,
							click : this.onTreeClick
						});
				this.tree.render(this.list);
				this.el.dom.setAttribute("readOnly", true);
				this.el.addClass("x-combo-noedit");
			},
			expandNode : function(e, b) {
				var d = e.childNodes;
				for (var c = 0, a = d.length; c < a; c++) {
					if (d[c].id == b) {
						return true;
					}
					if (expandNode(d[c], b)) {
						d[c].expand();
						return true;
					}
				}
				return false;
			},
			onDestroy : function() {
				if (this.wrap) {
					this.wrap.remove();
				}
				if (this.tree) {
					this.tree.destroy();
				}
				if (this.list) {
					this.list.destroy();
				}
				Orient.ComboTree.superclass.onDestroy.call(this);
			},
			isExpanded : function() {
				return this.list && this.list.isVisible();
			},
			collapse : function() {
				if (this.isExpanded()) {
					this.list.hide();
				}
				this.fireEvent("collapse", this);
			},
			expand : function() {
				this.list.alignTo(this.wrap, "tl-bl?");
				this.list.show();
				this.mon(Ext.getDoc(), {
							scope : this,
							mousewheel : this.collapseIf,
							mousedown : this.collapseIf
						});
				this.fireEvent("expand", this);
			},
			collapseIf : function(a) {
				if (!this.isDestroyed && !a.within(this.wrap)
						&& !a.within(this.list)) {
					this.collapse();
				}
			},
			onSelect : function(a) {
				if (this.fireEvent("beforeselect", this, a) !== false) {
					this.setValue(a);
					this.collapse();
					this.fireEvent("select", this, a);
				}
			},
			onTreeClick : function(a) {
				if (a) {
					this.onSelect(a);
				} else {
					this.collapse();
				}
			},
			assertValue : function() {
				if (this.currNode) {
					this.setValue(this.currNode);
				}
			},
			beforeBlur : function() {
				this.assertValue();
			},
			postBlur : function() {
				Orient.ComboTree.superclass.postBlur.call(this);
				this.collapse();
			},
			mimicBlur : function(a) {
				if (!this.isDestroyed && !this.wrap.contains(a.target)
						&& this.validateBlur(a)) {
					this.triggerBlur();
				}
			},
			validateBlur : function(a) {
				return !this.list || !this.list.isVisible();
			},
			onViewOver : function(b, a) {
				a = Ext.get(a);
				if (a.hasClass("x-tree-node-el")) {
					var c = a.getAttribute("ext:tree-node-id");
					if (c) {
						this.currNode = this.tree.getNodeById(c);
					}
				}
			},
			setValue : function(a) {
				var b = a;
				if (typeof a === "object") {
					this.hiddenField.value = ((this.root && a.id == this.root.id)
							? ""
							: a.id);
					b = a.text;
				}
				Orient.ComboTree.superclass.setValue.call(this, b);
				this.currNode = a;
			},
			getValue : function() {
				return this.currNode ? this.currNode.id : "";
			},
			getSelected : function() {
				return this.currNode;
			},
			clearValue : function() {
				if (this.hiddenField) {
					this.hiddenField.value = "";
				}
				this.setValue("");
			},
			initEvents : function() {
				Orient.ComboTree.superclass.initEvents.call(this);
				this.el.on("mousedown", this.onTriggerClick, this);
			},
			onTriggerClick : function() {
				if (this.disabled) {
					return;
				}
				if (this.isExpanded()) {
					this.collapse();
					this.el.focus();
				} else {
					this.onFocus({});
					this.expand();
					this.el.focus();
				}
			}
		});
Ext.reg("combotree", Orient.ComboTree);