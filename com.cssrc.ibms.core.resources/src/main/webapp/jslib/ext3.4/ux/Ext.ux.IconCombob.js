Ext.ux.IconCombob = Ext.extend(Ext.form.ComboBox, {
	initComponent : function() {
		Ext.ux.IconCombob.superclass.initComponent.call(this);
		Ext.apply(this, {
					tpl : '<tpl for=".">'
							+ '<div class="x-combo-list-item ux-icon-combo-item '
							+ "{" + this.valueField + '}">{'
							+ this.displayField + "}</div></tpl>"
				});
	},
	onRender : function(b, a) {
		Ext.ux.IconCombob.superclass.onRender.call(this, b, a);
		this.wrap.applyStyles({
					position : "relative"
				});
		this.el.setStyle("padding-left", "25px");
		this.icon = Ext.DomHelper.append(this.el.up("div.x-form-field-wrap"), {
					tag : "div",
					style : "position:absolute"
				});
	},
	setIconCls : function() {
		var a = this.store.query(this.valueField, this.getValue()).itemAt(0);
		if (a) {
			this.icon.className = "ux-icon-combo-icon "
					+ a.get(this.valueField);
		}
	},
	setValue : function(a) {
		Ext.ux.IconCombob.superclass.setValue.call(this, a);
		this.setIconCls();
	}
});
Ext.reg("iconcomb", Ext.ux.IconCombob);