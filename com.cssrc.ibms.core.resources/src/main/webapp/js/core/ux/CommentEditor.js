Ext.ns("Ext.ux.form");
Ext.ux.form.CommentEditor = Ext.extend(Ext.form.Field, {
	allowBlank : true,
	blankText : Ext.form.TextArea.prototype.blankText,
	readOnly : false,
	defaultAutoCreate : {
		tag : "div"
	},
	initComponent : function() {
		Ext.ux.form.CommentEditor.superclass.initComponent.call(this);
	},
	onRender : function(c, b) {
		Ext.ux.form.CommentEditor.superclass.onRender.call(this, c, b);
		var a = this.fs = new Ext.Panel({
					renderTo : this.el,
					height : this.height,
					autoScroll : this.autoScroll,
					width : this.width,
					border : false,
					style : "padding:0;",
					layout : "form"
				});
		this.store = new Ext.data.JsonStore({
					fields : ["un", "ui", "c", "v"]
				});
		this.view = new Ext.DataView({
			autoScroll : false,
			store : this.store,
			multiSelect : false,
			singleSelect : false,
			border : true,
			tpl : new Ext.XTemplate(
					'<tpl if="xcount &gt; 0">',
					'<tpl for=".">',
					'<div class="thumb-w" style="padding:2px;"><span><font>{un}({c}):</font><span><span>{v}</span></div>',
					"</tpl>", "</tpl>"),
			scope : this
		});
		a.add(this.view);
		this.inputField = new Ext.form.TextArea({
					width : "98%",
					hideLabel : true,
					hidden : this.readOnly,
					allowBlank : this.allowBlank
				});
		a.add(this.inputField);
		this.inputField.on("change", this.onFieldChange, this);
		this.hiddenName = this.name || Ext.id();
		var d = {
			tag : "input",
			type : "hidden",
			value : "",
			name : this.hiddenName
		};
		this.hiddenField = this.el.createChild(d);
		this.hiddenField.dom.disabled = this.hiddenName != this.name;
		a.doLayout();
	},
	onFieldChange : function(e, a, c) {
		var h = [];
		for (var d = 0; d < this.store.getCount(); d++) {
			var g = this.store.getAt(d);
			h.push(g.data);
		}
		if (a) {
			h.push({
						ui : curUserInfo.userId,
						un : curUserInfo.fullname,
						c : formatDate(new Date(), "yyyy-MM-dd HH:mm"),
						v : a
					});
		}
		var b = Ext.encode(h);
		this.fireEvent("change", this, b, this.hiddenField.dom.value);
		this.hiddenField.dom.value = b;
		this.validate();
	},
	setValue : function(a) {
		this.hiddenField.dom.value = "";
		if (!a || (a == "")) {
			return;
		}
		var b = Ext.decode(a);
		try {
			this.store.loadData(b);
		} catch (c) {
			alert(c);
		}
		this.hiddenField.dom.value = a;
		this.validate();
	},
	disable : function() {
		this.disabled = true;
		this.hiddenField.dom.disabled = true;
		this.fs.disable();
	},
	enable : function() {
		this.disabled = false;
		this.hiddenField.dom.disabled = false;
		this.fs.enable();
	},
	destroy : function() {
		Ext.destroy(this.fs);
		Ext.ux.form.CommentEditor.superclass.destroy.call(this);
	}
});
Ext.reg("commenteditor", Ext.ux.form.CommentEditor);