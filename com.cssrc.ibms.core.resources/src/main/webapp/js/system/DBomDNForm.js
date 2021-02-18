/**
 * DBom动态子节点选择界面
 * @author : weilei
 */
define(function(require, exports, module){
	
	exports.init = function(params){
		var panel = new DBomDNForm(params);
		return panel;
	};
	
	DBomDNForm = Ext.extend(Ext.Window, {
		
		constructor : function(a){
			Ext.applyIf(this, a);
			this.initUIComponents();
			DBomDSForm.superclass.constructor.call(this, {
				id : 'DBomDNForm',
				title : '选择字段',
				modal : true,
				width : 750,
				height: 450,
				layout: 'fit',
				items : [this.gridPanel],
				iconCls : 'menu-role',
				buttonAlign : 'center',
				buttons : this.buttons
			});
		},
		/**
		 * 初始化组件事件
		 */
		initUIComponents : function(){
			
			/**DN gridPanel*/
			this.gridPanel = new Orient.GridPanel({
				url : __ctxPath + '/oa/system/dbomNode/getShowFiled.do',
				region : 'center',
				border : false,
				fields : ['fieldName', 'fieldDesc', 'tableName', 'tableDesc'],
				columns: [
					{header:'字段名称', dataIndex:'fieldName'},
					{header:'字段描述', dataIndex:'fieldDesc'},
					{header:'所属数据源', dataIndex:'tableDesc'}
				],
				loadMask : true,
				baseParams : {
					tableName : this.tableName
				}
			});
			this.gridPanel.getTopToolbar().hide();
						
			/**DN buttons*/
			this.buttons = [{
				text : '置空关联关系',
				scope : this,
				iconCls : 'reset',
				handler : this.reset
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
		 * 选择数据源事件
		 */
		select : function() {
			
			var selections = this.gridPanel.getSelectionModel().getSelections();
			if(selections.length < 1){
				Ext.ux.Toast.msg('提示','请选择字段！');
				return;
			}
			var result = new Array();
			for(var index=0; index<selections.length; index++){
				var fieldName = selections[index].data.fieldName;
				fieldName = this.tableName.indexOf('W_')>-1?'F_'+fieldName:fieldName;
				var fieldDesc = selections[index].data.fieldDesc;
				result.push({
					fieldName : this.tableName + '.' + fieldName,
					fieldDesc : fieldDesc
				});
			}
			if(this.callback){
				this.callback.call(this.scope, result);
			}
			this.close();
		},
		/**
		 * 清空查询条件事件
		 */
		reset : function() {
			
			var callback = this.callback;
			var window = this;
			Ext.Msg.confirm('信息确认', '确定要置空动态子节点？', function(btn){
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