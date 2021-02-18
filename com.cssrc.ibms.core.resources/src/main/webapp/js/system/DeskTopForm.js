define(function(require, exports, module) { 
	exports.init = function(params) {
		var panel = new DeskTopForm(params);
		return panel;
	}
	DeskTopForm = Ext.extend(Ext.Window, {
		constructor : function(a) {
			Ext.applyIf(this, a);
			this.initUIComponents();
			DeskTopForm.superclass.constructor.call(this, {
						layout : "fit",
						items : this.formPanel,
						modal : true,
						id : "DeskTopFormWin",
						title : "选择显示模块",
						iconCls : "menu-role",
						width : 500,
						height : 300,
						buttonAlign : "center",
						buttons : this.buttons
					});
		},
		initUIComponents : function() {
			this.formPanel = new Ext.FormPanel({
						layout : "form",
						border : false,
						bodyStyle : "padding:5px;",
						defaults : {
							anchor : "96%,96%"
						},
						defaultType : "textfield",
						items : []
					}); 
			var self = this;
			var allItems = [];
			this.operationArr = [];
			Ext.Ajax.request({
							url : __ctxPath + "/oa/system/deskTop/list.do",
							method : "POST",
							params:{},
							scope : this,
							success : function(e, f) {
								var responseText = Ext.util.JSON.decode(e.responseText);
								allItems = responseText.result;
								var counts = responseText.totalCounts;
								var its = [];
								var colNum = 3;
								var j = null;
								var oper = {
									xtype : "checkboxgroup",
									layout : "hbox",
									fieldLabel : "",
									items : its
								};
								for(var i=0;i<allItems.length;i++){
								  var checkItem = {
												xtype : "checkbox",
												name : allItems[i].portalId,
												boxLabel : allItems[i].title,
												style : "text-align:left;padding: 3px 3px 3px 0;",
												checked:allItems[i].actived=="1"?true:false,
												disabled:allItems[i].actived=="1"?true:false,
												listeners : {
													scope : this,
													"check" : function(cb){ 
														if(cb.checked){
															self.pushItem(cb.name,allItems);
														}else{
															self.removeItem(cb.name,allItems);
														}
													}
												}
											};
									if (i % colNum == 0) {
										j = {
											xtype : "compositefield", 
											items : [],
											defaults : {
												style : "margin:10px 0 0 5px"
											}
										};										
								       its.push(j);
									}
									j.items.push(checkItem); 
								}
								self.formPanel.add(oper);
								self.formPanel.doLayout();
							}
						});
			this.buttons = [{
						text : "保存",
						iconCls : "toolbarsave",
						scope : this,
						handler : this.saveForm
					}, {
						text : "取消",
						iconCls : "toolbarcancel",
						scope : this,
						handler : function() {
							this.close();
						}
					}];
		},
		saveForm : function() {
			if (this.callback) {
				this.callback.call(this.scope);
			}
			this.close();
		},
		
	   pushItem :function(itemId,allItems){
		   for(var i = 0; i < allItems.length; i++){
		       if(allItems[i].portalId == itemId){
		        this.returnResult.push(allItems[i]);
		     break;
		    }
		   }
	   },
	   
	   removeItem :function(itemId,allItems){
	   	    for(var i = 0; i < this.returnResult.length; i++){
		      if(this.returnResult[i].portalId == itemId){
		        this.returnResult.remove(this.returnResult[i]);
		        break;
		    }
		   }
	   }
	});
});