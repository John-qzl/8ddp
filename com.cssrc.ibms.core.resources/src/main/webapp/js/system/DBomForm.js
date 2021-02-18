/**
 * DBom分类新增/编辑表单
 * @author : weilei
 */
define(function(require, exports, module){
	
	exports.init = function(params){
		var panel = new DBomForm(params);
		return panel;
	};
	
	DBomForm = Ext.extend(Ext.Window, {
		
		constructor : function(a){
			Ext.applyIf(this, a);
			this.initUIComponents();
			DBomForm.superclass.constructor.call(this, {
				id : 'DBomFormWin',
				title : 'DBom分类信息',
				modal : true,
				width : 500,
				height: 300,
				layout: 'fit',
				items : [this.formPanel],
				iconCls : 'menu-role',
				buttonAlign : 'center',
				buttons : this.formButtons
			});
		},
		/**
		 * 初始化组件事件
		 */
		initUIComponents : function(){
			
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			
			/**font Tag[*]*/
			var fontTag = '<font style="color:red">*</font>';
			
			/**DBom FormPanel*/
			this.formPanel = new Ext.form.FormPanel({
				border : false,
				defaults : {anchor:'96%'},
				bodyStyle : {padding:'5px'},
				items : [{
					name : 'id', 
					xtype : 'hidden',
					value : this.dbomId
				},{
					name : 'code', 
					xtype : 'textfield',
					fieldLabel : '代号' + fontTag,
					allowBlank : false,
					regex : /^[a-zA-Z0-9]+$/,
					regexText : '请输入英文字母和数字'
				},{
					name : 'name', 
					xtype : 'textfield',
					fieldLabel : '名称' + fontTag,
					allowBlank : false
				},{
					name : 'description', 
					xtype : 'textarea',
					fieldLabel : '描述'
				}]
			});
			
			/**DBom FormButtons*/
			this.formButtons = [{
				text : '保存',
				scope : this,
				iconCls : 'toolbarsave',
				handler : this.saveDBom
			},{
			text : '取消',
				scope : this,
				iconCls : 'toolbarcancel',
				handler : function(){this.close();}
			}];
			
			/**DBom Data Init*/
			if(this.dbomId != ''){
				this.formPanel.loadData({
					url : __ctxPath + '/oa/system/dbom/get.do?id=' + this.dbomId, 
					root : 'data'
				});
			}
			
		},
		/**
		 * 保存DBom分类事件
		 */
		saveDBom : function(){
			
			var basicForm = this.formPanel.getForm();
			var id = basicForm.findField('id').getValue();
			var code = basicForm.findField('code').getValue();
			if(code != '' && code != null ){
				var checkURL = __ctx + '/oa/system/dbom/check.do?code='+code+'&id='+id;
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open('post', checkURL, false);
				conn.send();
				var result = Ext.decode(conn.responseText);
				if(result){
					Ext.ux.Toast.msg('提示','代号已存在，请重新输入！');
					return;
				}
			}
			var options = {
				url : __ctx + '/oa/system/dbom/save.do',
				scope : this,
				waitMsg : '正在提交数据...',
				formPanel : this.formPanel,
				callback  : function(a, b){
					if(this.callback){
						this.callback.call(this.scope);
					}
					this.close();
				}
			};
			$postForm(options);
		}
	});

});