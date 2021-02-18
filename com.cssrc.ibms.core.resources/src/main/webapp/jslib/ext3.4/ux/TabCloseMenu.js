/*
 * Ext JS Library 3.4.0 Copyright(c) 2006-2011 Sencha Inc. licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.ux.TabCloseMenu = Ext.extend(Object, {
			closeTabText : "Close Tab",
			closeOtherTabsText : "Close Other Tabs",
			showCloseAll : true,
			closeAllTabsText : "Close All Tabs",
			constructor : function(a) {
				Ext.apply(this, a || {});
			},
			init : function(a) {
				this.tabs = a;
				a.on({
							scope : this,
							contextmenu : this.onContextMenu,
							destroy : this.destroy
						});
			},
			destroy : function() {
				Ext.destroy(this.menu);
				delete this.menu;
				delete this.tabs;
				delete this.active;
			},
			onContextMenu : function(b, c, g) {
				this.active = c;
				var a = this.createMenu(), d = true, h = true, f = a
						.getComponent("closeall");
				a.getComponent("close").setDisabled(!c.closable);
				b.items.each(function() {
							if (this.closable) {
								d = false;
								if (this != c) {
									h = false;
									return false;
								}
							}
						});
				a.getComponent("closeothers").setDisabled(h);
				if (f) {
					f.setDisabled(d);
				}
				g.stopEvent();
				a.showAt(g.getPoint());
			},
			createMenu : function() {
				if (!this.menu) {
					var a = [{
								itemId : "close",
								iconCls : "btn-close",
								text : this.closeTabText,
								scope : this,
								handler : this.onClose
							}];
					if (this.showCloseAll) {
						a.push("-");
					}
					a.push({
								itemId : "closeothers",
								iconCls : "btn-close",
								text : this.closeOtherTabsText,
								scope : this,
								handler : this.onCloseOthers
							});
					if (this.showCloseAll) {
						a.push({
									itemId : "closeall",
									iconCls : "btn-close",
									text : this.closeAllTabsText,
									scope : this,
									handler : this.onCloseAll
								});
					}
					this.menu = new Ext.menu.Menu({
								items : a
							});
				}
				return this.menu;
			},
			onClose : function() {
				this.tabs.remove(this.active);
			},
			onCloseOthers : function() {
				this.doClose(true);
			},
			onCloseAll : function() {
				this.doClose(false);
			},
			doClose : function(b) {
				var a = [];
				this.tabs.items.each(function(c) {
							if (c.closable) {
								if (!b || c != this.active) {
									a.push(c);
								}
							}
						}, this);
				Ext.each(a, function(c) {
							this.tabs.remove(c);
						}, this);
			}
		});
Ext.preg("tabclosemenu", Ext.ux.TabCloseMenu);