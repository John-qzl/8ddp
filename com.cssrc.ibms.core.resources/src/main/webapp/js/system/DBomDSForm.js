/**
 * DBom数据源选择界面
 * @author : weilei
 */
define(function(require, exports, module){
	
	exports.init = function(params){
		var panel = new DBomDSForm(params);
		return panel;
	};
	
	DBomDSForm = Ext.extend(Ext.Window, {
		
		constructor : function(a){
			Ext.applyIf(this, a);
			this.initUIComponents();
			DBomDSForm.superclass.constructor.call(this, {
				id : 'DBomDSForm',
				title : '选择数据源',
				modal : true,
				width : 800,
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
			
			/**DS searchPanel*/
			this.searchPanel = new Orient.SearchPanel({
				layout : 'form',
				region : 'north',
				colNums : 6,
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
				items : [
				{
					name : 'modelType', 
					mode : 'local',
					xtype : 'combo',
					width : 100,
					store : [['数据类', '数据类'], ['数据视图', '数据视图']],
					editable : false,
					fieldLabel : '模型类型',
					triggerAction : 'all',
					value : '数据类'
				}, {
					name : 'relationType', 
					mode : 'local',
					xtype : 'combo',
					width : 100,
					store : [['全部', '全部'], ['关系表', '关系表']],
					editable : false,
					fieldLabel : '关系类型',
					triggerAction : 'all',
					value : '全部'
				}, {
					xtype : "textfield",
					name : 'tableName', 
					fieldLabel : "表名",
					labelWidth : 40,
					width : 100
				}, {
					xtype : "textfield",
					name : 'tableDesc', 
					fieldLabel : "描述",
					labelWidth : 40,
					width : 100
				}, {
					xtype : "button",
					text : "查询",
					style : "padding-left:5px;padding-right:5px;",
					iconCls : "search",
					handler : this.search,
					scope : this
				}, {
					xtype : "button",
					text : "重置",
					style : "padding-left:5px;padding-right:5px;",
					iconCls : "reset",
					handler : function(){
						this.searchPanel.getForm().reset();
					},
					scope : this
				}]
			});
			
			/**DS gridPanel*/
			this.gridPanel = new Orient.GridPanel({
				url : __ctxPath + '/oa/system/dbomNode/getDataSource.do',
				region : 'center',
				border : false,
				fields : ['tableName', 'tableDesc', 'modelType', 'relationType'],
				columns: [
					{header:'表名', dataIndex:'tableName'},
					{header:'描述', dataIndex:'tableDesc'},
					{header:'模型类型', dataIndex:'modelType'}
				],
				loadMask : true,
				baseParams : {
					tableName : '',
					tableDesc : '',
					mainTableName : this.mainTableName
				},
				singleSelect : this.singleSelect
			});
			
			/**DS buttons*/
			this.buttons = [{
				text : '置空数据源',
				scope : this,
				iconCls : 'reset',
				handler : this.reset
			},{
				text : '追加',
				scope : this,
				hidden : this.singleSelect,
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
		 * 追加数据源事件
		 */
		append : function() {
			
			this.commonAdd('append');
		},
		/**
		 * 选择数据源事件
		 */
		select : function() {
			
			this.commonAdd('select');
		},
		/**
		 * 通用添加数据源事件
		 */
		commonAdd : function(operationType) {
			
			var selections = this.gridPanel.getSelectionModel().getSelections();
			if(selections.length < 1){
				Ext.ux.Toast.msg('提示','请选择数据源！');
				return;
			}
			var result = new Array();
			for(var index=0; index<selections.length; index++){
				var tableName = selections[index].data.tableName;
				var tableDesc = selections[index].data.tableDesc;
				var modelType = selections[index].data.modelType;
				modelType = modelType=='数据类'?'0':'1';
				result.push({
					tableName : tableName,
					tableDesc : tableDesc,
					modelType : modelType
				});
			}
			if(this.callback){
				this.callback.call(this.scope, result, operationType);
			}
			this.close();
		},
		/**
		 * 查询数据源事件
		 */
		search : function() {
			
			$search({
				searchPanel : this.searchPanel,
				gridPanel : this.gridPanel
			});
		},
		/**
		 * 清空查询条件事件
		 */
		reset : function() {
			
			var callback = this.callback;
			var window = this;
			Ext.Msg.confirm('信息确认', '确定要置空数据源？', function(btn){
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