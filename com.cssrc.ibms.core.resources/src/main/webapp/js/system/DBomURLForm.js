/**
 * DBom业务数据模板地址选择界面
 * @author : weilei
 */
define(function(require, exports, module){
	
	exports.init = function(params){
		var panel = new DBomURLForm(params);
		return panel;
	};
	
	DBomURLForm = Ext.extend(Ext.Window, {
		
		constructor : function(a){
			Ext.applyIf(this, a);
			this.initUIComponents();
			DBomURLForm.superclass.constructor.call(this, {
				id : 'DBomURLForm',
				title : '选择业务数据模板',
				modal : true,
				width : 750,
				height: 450,
				layout: 'border',
				items : [this.searchPanel, this.gridPanel],
				iconCls : 'menu-role',
				buttonAlign : 'center',
				buttons : this.buttons
			});
		},
		/**
		 * 初始化组件事件
		 */
		initUIComponents : function(){
			
			/**URL searchPanel*/
			this.searchPanel = new Orient.SearchPanel({
				layout : 'form',
				region : 'north',
				colNums : 5,
				keys : [{
					key : Ext.EventObject.ENTER,
					fn : this.search,
					scope : this
				}, {
					key : Ext.EventObject.ESC,
					fn : this.reset,
					scope : this
				}],
				labelWidth : 60,
				items : [{
					name : 'tableName', 
					xtype : "textfield",
					width : 150,
					fieldLabel : "对应表"
				}, {
					name : 'subject', 
					xtype : "textfield",
					width : 150,
					fieldLabel : "表单标题"
				}, {
					text : "查询",
					xtype : "button",
					style : "padding-left:5px;padding-right:5px;",
					iconCls : "search",
					handler : this.search,
					scope : this
				}, {
					xtype: "button",
					text : "重置",
					style : "padding-left:5px;padding-right:5px;",
					iconCls : "reset",
					handler : function(){
						this.searchPanel.getForm().reset();
					},
					scope : this
				}]
			});
			
			/**URL gridPanel*/
			this.gridPanel = new Orient.GridPanel({
				url : __ctxPath + '/oa/system/dbomNode/getFormDefList.do',
				region : 'center',
				border : false,
				fields : ['subject', 'tableName', 'categoryName', 'dataTemplateURL'],
				columns: [
					{header:'对应表',   dataIndex:'tableName'},
					{header:'表单标题', dataIndex:'subject'},
					{header:'表单分类', dataIndex:'categoryName'}
				],
				loadMask : true,
				baseParams : {
					subject : '',
					tableName : '',
					dataSource : this.dataSource
				}
			});
			
			/**URL buttons*/
			this.buttons = [{
				text : '置空业务数据模板地址',
				scope : this,
				iconCls : 'reset',
				handler : this.reset
			},{
				text : '追加',
				scope : this,
				iconCls : 'toolbarsave',
				handler : this.append
			},{
				text : '确定',
				scope : this,
				iconCls : 'toolbarsave',
				handler : this.select
			},{
				text : '取消',
				scope : this,
				iconCls : 'toolbarcancel',
				handler : function(){this.close();}
			}];
		},
		/**
		 * 追加业务数据模板事件
		 */
		append : function() {
			
			this.commonAdd('append');
		},
		/**
		 * 选择业务数据模板事件
		 */
		select : function() {
			
			this.commonAdd('select');
		},
		/**
		 * 通用添加数业务数据模板事件
		 */
		commonAdd : function(operationType){
			
			var selections = this.gridPanel.getSelectionModel().getSelections();
			if(selections.length < 1){
				Ext.ux.Toast.msg('提示','请选择业务数据模板记录！');
				return;
			}
			var url = '';
			for(var index=0; index<selections.length; index++){
				var categoryName = selections[index].data.categoryName;
				if(categoryName == '数据视图'){
					url += '/oa/form/querySetting/preview.do?__displayId__=' + selections[index].data.dataTemplateURL + ',\n';
				}else{
					url += '/oa/form/dataTemplate/preview.do?__displayId__=' + selections[index].data.dataTemplateURL + ',\n';
				}
			}
			url = url==''?'':url.substring(0, url.length-2);
			if(this.callback){
				this.callback.call(this.scope, url, operationType);
			}
			this.close();
		},
		/**
		 * 查询数业务数据模板事件
		 */
		search : function() {
			
			$search({
				searchPanel : this.searchPanel,
				gridPanel : this.gridPanel
			});
		},
		/**
		 * 置空业务数据模板地址事件
		 */
		reset : function() {
			
			var callback = this.callback;
			var window = this;
			Ext.Msg.confirm('信息确认', '置空业务数据模板地址？', function(btn){
				if(btn == 'yes'){
					if(callback){
						callback.call(this, null);
					}
					window.close();
				}
			});
		}
	});
	
});