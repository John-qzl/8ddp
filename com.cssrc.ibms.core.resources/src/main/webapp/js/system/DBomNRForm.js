/**
 * DBom动态子节点中间关系界面
 * @author : weilei
 */
define(function(require, exports, module){
	
	exports.init = function(params){
		var panel = new DBomNRForm(params);
		return panel;
	};
	
	DBomNRForm = Ext.extend(Ext.Window, {
		
		constructor : function(a){
			Ext.applyIf(this, a);
			this.initUIComponents();
			DBomDSForm.superclass.constructor.call(this, {
				id : 'DBomNRForm',
				title : '设置目标数据源关联关系',
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
			
			/**DR parRNCombo*/
			this.parRNCombo = new Ext.form.ComboBox({
				mode : 'remote',
				store : new Ext.data.JsonStore({
					url : __ctxPath + '/oa/system/dbomNode/getShowFiled.do?tableName=' + this.dataSource,
					fields : ['fieldName', 'fieldDesc'],
					root:"result",
					autoDestroy : true
				}),
				editable : false,
				valueField : 'fieldName',
				displayField : 'fieldDesc',
				triggerAction : 'all',
				listeners : {
					scope : this,
					select : this.selectParRNCombo
				}
			});
			
			/**DR subDSCombo*/
			this.subDSCombo = new Ext.form.ComboBox({
				mode : 'remote',
				store : new Ext.data.JsonStore({
					url : __ctxPath + '/oa/system/dbomNode/getDataSourceByName.do?dataSource=' + this.targetDataSource,
					fields : ['name', 'desc'],
					autoDestroy : true
				}),
				editable : false,
				valueField : 'name',
				displayField : 'desc',
				triggerAction : 'all',
				listeners : {
					scope : this,
					select : this.selectSubDSCombo
				}
			});
			
			/**DR subRNCombo*/
			this.subRNCombo = new Ext.form.ComboBox({
				mode : 'remote',
				store : new Ext.data.JsonStore({
					url : __ctxPath + '/oa/system/dbomNode/getShowFiled.do',
					fields : ['fieldName', 'fieldDesc'],
					autoDestroy : true,
					root:"result",
					baseParams : {
						tableName : this.tableName
					}
				}),
				editable : false,
				valueField : 'fieldName',
				displayField : 'fieldDesc',
				triggerAction : 'all',
				listeners : {
					scope : this,
					select : this.selectSubRNCombo
				}
			});
			
			/**DR gridPanel-toolbar*/
			this.toolbar = new Ext.Toolbar({
				items : [{
					text : '添加',
					scope : this,
					iconCls : 'btn-add',
					handler : this.addRow
				},{
					text : '删除',
					scope : this,
					iconCls : 'btn-del',
					handler : this.delRow
				}]
			});
			
			/**DR gridPanel*/
			this.gridPanel = new Orient.EditorGridPanel({
				url : __ctxPath + '/oa/system/dbomNode/getTargetDataRelation.do?id=' + this.id,
				tbar : this.toolbar,
				region : 'center',
				border : false,
				fields : ['pField', 'pFHidden', 'sTable', 'sTHidden', 'sField', 'sFHidden'],
				columns: [
					{header:'节点key', dataIndex:'pField', editor:this.parRNCombo},
					{header:'目标数据源', dataIndex:'sTable', editor:this.subDSCombo},
					{header:'目标外键字段', dataIndex:'sField', editor:this.subRNCombo}
				],
				loadMask : true,
				baseParams : {
					tableName : this.tableName
				}
			});
			this.gridPanel.getBottomToolbar().hide();
			
			/**DR buttons*/
			this.buttons = [{
				text : '置空动关联关系关系',
				scope : this,
				iconCls : 'reset',
				handler : this.reset
			},{
				text : '确定',
				scope : this,
				iconCls : 'toolbarsave',
				handler : this.select
			},{
				text : '追加',
				scope : this,
				iconCls : 'toolbarsave',
				handler : this.append
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
			this.commonAdd();
		},
		
		/**
		 * 选择数据源事件
		 */
		commonAdd : function(append) {
			
			var relValue = '';
			var displayValue = '';
			var dataSource = this.dataSource;
			var displayDataSource = this.displayDataSource;
			this.gridPanel.getStore().each(function(record){
				var pFHidden = record.data.pFHidden;
				pFHidden = (dataSource.indexOf('W_')>-1&&pFHidden!='ID')?'F_'+pFHidden:pFHidden;
				var sTHidden = record.data.sTHidden;
				var sFHidden = record.data.sFHidden;
				sFHidden = sTHidden.indexOf('W_')>-1?'F_'+sFHidden:sFHidden;
				//关联关系真实值与显示值
				relValue += dataSource + '.' + pFHidden + '=' + sTHidden + '.' + sFHidden + ',';
				displayValue +=  displayDataSource + '.' + record.data.pField + '=' + record.data.sTable + '.' + record.data.sField + ',';
			});
			relValue = relValue.length<2?relValue:relValue.substring(0, relValue.length-1);
			displayValue = displayValue.length<2?displayValue:displayValue.substring(0, displayValue.length-1);
			displayValue += '';
			if(this.callback){
				this.callback.call(this.scope, relValue, displayValue,append);
			}
			this.close();
		},
		/**
		 * 清空查询条件事件
		 */
		reset : function() {
			
			var callback = this.callback;
			var window = this;
			Ext.Msg.confirm('信息确认', '确定要置空关联关系？', function(btn){
				if(btn == 'yes'){
					if(callback){
						callback.call(this, '', '');
					}
					window.close();
				}
			});
		},
		/**
		 * 添加事件
		 */
		addRow : function(){
			
			var record = new Ext.data.Record();
			this.gridPanel.getStore().add(record);
		},
		/**
		 * 删除事件
		 */
		delRow : function(){
			
			var selections = this.gridPanel.getSelectionModel().getSelections();
			if(selections.length < 1){
				Ext.ux.Toast.msg('提示','请选择删除的关联字段记录！');
				return;
			}
			this.gridPanel.getStore().remove(selections);
		},
		/**
		 * 选择字段事件
		 */
		selectParRNCombo : function(combo){
			
			combo.gridEditor.record.set('pFHidden', combo.getValue());	
			combo.setValue(combo.lastSelectionText);
		},
		/**
		 * 选择子节点数据源事件
		 */
		selectSubDSCombo : function(combo){
			
			var dataSource = combo.getValue();
			combo.gridEditor.record.set('sTHidden', dataSource);	
			var store = this.subRNCombo.getStore();
			this.subRNCombo.clearValue();
			store.baseParams.tableName=dataSource;
			//store.reload({params:{tableName:dataSource}});
			combo.setValue(combo.lastSelectionText);	
		},
		/**
		 * 选择子节点字段事件
		 */
		selectSubRNCombo : function(combo){
			
			combo.gridEditor.record.set('sFHidden', combo.getValue());	
			combo.setValue(combo.lastSelectionText);
		}
	});
	
});