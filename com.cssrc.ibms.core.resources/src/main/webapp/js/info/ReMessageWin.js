ReMessageWin = Ext.extend(Ext.Window, {
			constructor : function(a) {
				Ext.applyIf(this, a);
				this.initUI();
				ReMessageWin.superclass.constructor.call(this, {
							id : "ReMessageWin ",
							iconCls : "btn-mail_reply",
							title : "回复",
							width : 350,
							height : 200,
							layout : "fit",
							border : false,
							items : this.formPanel,
							buttons : [{
										text : "发送",
										iconCls : "menu-mail_send",
										scope : this,
										handler : this.send
									}, {
										text : "重置",
										iconCls : "reset",
										scope : this,
										handler : this.reset
									}]
						});
			},
			initUI : function() {
				this.formPanel = new Ext.form.FormPanel({
							frame : false,
							bodyStyle : "padding:5px 20px 0",
							width : 275,
							height : 180,
							defaultType : "textarea",
							layout : "absolute",
							items : [{
										xtype : "hidden",
										name : "userId",
										value : this.id
									}, {
										x : 0,
										y : 10,
										xtype : "label",
										text : "收信人:"
									}, {
										x : 40,
										y : 10,
										xtype : "field",
										width : "80%",
										name : "userFullname",
										allowBlank : false,
										readOnly : true,
										value : this.name
									}, {
										x : 0,
										y : 40,
										xtype : "label",
										text : "内容:"
									}, {
										x : 40,
										y : 40,
										xtype : "textarea",
										width : "80%",
										height : "60%",
										name : "content",
										allowBlank : false
									}]
						});
			},
			send : function() {
				$postForm({
							formPanel : this.formPanel,
							scope : this,
							waitMsg : "正在 发送信息",
							successMsg : "信息发送成功！",
							url : __ctxPath + "/info/sendShortMessage.do",
							callback : function(a, b) {
								if (this.callback) {
									this.callback.call(this.scope);
								}
								this.close();
							}
						});
			},
			reset : function() {
				this.formPanel.getForm().findField("content").reset();
			}
		});