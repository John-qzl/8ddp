DicCombo = Ext.extend(Ext.form.ComboBox, {
	constructor : function(a) {
		Ext.apply(this, a);
		DicCombo.superclass.constructor.call(this, {
					editable : this.editable ? this.editable : true,
					forceSelection : this.forceSelection
							? this.forceSelection
							: true,
					selectOnFocus : true,
					typeAhead : true,
					mode : "local",
					triggerAction : "all",
					store : new Ext.data.ArrayStore({
								autoLoad : this.autoLoad ? this.autoLoad : true,
								baseParams : {
									nodeKey : this.nodeKey,
									typeId : this.typeId
								},
								url : __ctxPath
										+ "/system/loadItemDictionary.do",
								fields : ["itemId", "itemName"]
							}),
					displayField : "itemName",
					valueField : (this.returnName) ? "itemName" : "itemId"
				});
	}
});
Ext.reg("diccombo", DicCombo);