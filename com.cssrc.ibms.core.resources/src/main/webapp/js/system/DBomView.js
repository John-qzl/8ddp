/**
 * DBom管理界面
 * @author : weilei
 */
define(function(require, exports, module){

	var dbomForm = require('./DBomForm');
	var dbomDSForm = require('./DBomDSForm');
	var dbomDNForm = require('./DBomDNForm');
	var dbomDRForm = require('./DBomNRForm');
	var dBomURLForm= require('./DBomURLForm');
	var panel;
	exports.init = function(params){
		panel = new DBomView(params);
		return panel;
	};
	
	DBomView = Ext.extend(Ext.Panel, {
		constructor : function(a){
			Ext.applyIf(this, a);
			this.initUIComponent();
			DBomView.superclass.constructor.call(this, {
				id : 'DBomView',
				title : a.text,
				layout : 'border',
				iconCls : a.iconCls,
				autoScroll : true,
				items : [this.leftPanel, this.centerPanel]
			});
		},
		
		/**
		 * 初始化组件事件
		 */
		initUIComponent : function(){
			
			/**DBom leftPanel-ToolBar*/
			this.DBomTbar = new Ext.Toolbar({
				items : [{
					text : '新建分类',
					scope : this,
					iconCls : 'btn-add',
					handler : this.addDBom
				},{
					text : '编辑分类',
					scope : this,
					iconCls : 'btn-edit',
					handler : this.editDBom
				},{
					text : '删除分类',
					scope : this,
					iconCls : 'btn-del',
					handler : this.delDBom
				}]
			});
			
			/**DBom Combo Store*/
			this.comboDBomStore = new Ext.data.JsonStore({
				url : __ctxPath + '/oa/system/dbom/combo.do',
				fields : ['id', 'name','code'],
				autoLoad : true
			});
			
			/**DBom Combo*/
			this.comboDBom = new Ext.form.ComboBox({
				mode : 'local',
				store : this.comboDBomStore,
				editable : true,
				valueField : 'id',
				displayField : 'name',
				triggerAction : 'all',
				listeners : {
					scope : this,
					select: this.selectComboDBom
				}
			});
			
			/**DBom topLeftPanel*/
			this.topLeftPanel = new Ext.Panel({
				region : 'north',
				layout : 'fit',
				height : 23, 
				border : false,
				items  : [this.comboDBom]
			});
			
			/**DBom centerLeftPanel*/
			this.centerLeftPanel = new Orient.ux.TreePanelEditor({
				url : __ctxPath + '/oa/system/dbomNode/tree.do',
				split : true,
				scope : this,
				params : {pCode:'-1',nodeType:'-1'},
				region : 'center',
				layout : 'fit',
				treeType : 'dbom',
				autoScroll : true,
				contextMenuItems : [{
					text : '新建节点',
					scope : this,
					iconCls : 'btn-add',
					handler : this.addDyDBomNode
				},{
					text : '编辑节点',
					scope : this,
					iconCls : 'btn-edit',
					handler : this.editDBomNode
				},{
					text : '删除节点',
					scope : this,
					iconCls : 'btn-del',
					handler : this.delDBomNode
				}],
				onclick : this.clickDBomNode
			});
			
			/**DBom leftPanel*/
			this.leftPanel = new Ext.Panel({
				tbar : this.DBomTbar,
				title : 'DBom管理',
				split : true,
				width : 260,
				scope : this,
				region : 'west',
				layout : 'border',
				autoScroll : true,
				collapsible : true,
				items : [this.topLeftPanel, this.centerLeftPanel]
			});
			
			/**DBom centerPanel-ToolBar*/
			this.centerTBar = new Ext.Toolbar({
				items : [{
					text : '保存',
					scope : this,
					iconCls : "toolbarsave",
					handler : this.saveDBomNode
				}]
			});
			
			/**font Tag[*]*/
			var fontTag = '<font style="color:red">*</font>';
			
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			
			/**DBom centerPanel*/
			this.centerPanel = new Ext.form.FormPanel({
				tbar : this.centerTBar,
				region : 'center',
				defaults : {anchor:'98%'},
				disabled : true,
				bodyStyle : 'padding:5px',
				labelWidth : 120,
				autoScroll : true,
				items : [{
					name : 'id', 
					xtype : 'hidden'
				},{
					name : 'pcode', 
					xtype : 'hidden'
				},{
					name : 'nodeType', 
					xtype : 'hidden'
				},{
					name : 'code', 
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '代号',
					allowBlank : true,
					regex : /^[a-zA-Z0-9]+$/,
					regexText : '请输入英文字母和数字'
				},{
					name : 'name', 
					anchor : '50%',
					xtype : 'textfield',
					fieldLabel : '名称',
					allowBlank : true
				},{
					name : 'dataSource', 
					xtype : 'textfield',
					editable : true,
					allowBlank : true,
					fieldLabel: '节点数据源',
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.loadDataSource)
						}
					}
				},{
					name : 'showFiled', 
					xtype : 'textfield',
					editable : true,
					allowBlank : true,
					fieldLabel: '节点显示列',
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.loadDataSourceShowText)
						}
					}
				},{
					name : 'nodeKey', 
					xtype : 'textfield',
					editable : true,
					allowBlank : true,
					fieldLabel: '节点KEY',
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.loadNodeKey)
						}
					}
				},{
					name : 'pNodeKey', 
					xtype : 'textfield',
					editable : true,
					allowBlank : true,
					fieldLabel: '父节点KEY',
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.loadPNodeKey)
						}
					}
				},{
					name : 'targetDataSource', 
					xtype : 'textfield',
					editable : true,
					fieldLabel: '目标数据源',
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.loadClickDataSource)
						}
					}
				},{
					name : 'targetDataRelation', 
					xtype : 'textarea',
					height : 150,
					fieldLabel: '关联关系',
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.setNodeRelation)
						}
					}
				},{
					name : 'url', 
					xtype : 'textarea',
					fieldLabel : '目标URL',
					height : 150,
					allowBlank : true,
					listeners : {
						scope : this,
						render:function(p){
							p.getEl().on('dblclick',this.setDataTemplateURL)
						}
					}
				},{
					name : 'display', 
					mode : 'local', 
					xtype : 'combo',
					store : [['Tab页', 'Tab页'], ['树形', '树形']],
					editable : true,
					fieldLabel : '显示方式',
					triggerAction : 'all',
					value : 'Tab页'				
				},{
					name : 'description', 
					xtype : 'textarea',
					height : 100,
					fieldLabel : '描述'
				},{
					name : 'hiddenDataSource', //节点数据源
					xtype : 'hidden'
				},{
					name : 'hiddenShowFiled', //节点show text
					xtype : 'hidden'
				},{
					name : 'hiddenNodeKey', //节点Key
					xtype : 'hidden'
				},{
					name : 'hiddenPNodeKey', //父节点Key
					xtype : 'hidden'
				},{
					name : 'hiddenTargetDataSource', //目标数据源
					xtype : 'hidden'
				},{
					name : 'dbomId', //dbom 分类code
					xtype : 'hidden'
				}]
			});
			
		},
		/**
		 * 初始化DBom-combo事件
		 */
		loadComboDBom : function(store){
			
			if (store.getCount() > 0) {
				var record = store.getAt(0);
				var dbomId = record.data.id;
				this.comboDBom.setValue(dbomId);
			}
		},
		/**
		 * 新增DBom事件
		 */
		addDBom : function(){
			
			var params = {
				scope : this, 
				dbomId : '',
				callback : this.reloadDBom
			};
			dbomForm.init(params).show();
		},
		/**
		 * 编辑DBom事件
		 */
		editDBom : function(){
			var dbomId = this.comboDBom.getValue();
			if(dbomId == '' || dbomId == undefined || dbomId == null || dbomId == 'undefined'){
				Ext.ux.Toast.msg("操作信息", "请选择需要编辑的分类！");
				return;
			}
			var params = {
				scope : this, 
				dbomId : dbomId,
				callback : this.reloadDBom
			};
			dbomForm.init(params).show();
		},
		/**
		 * 删除DBom事件
		 */
		delDBom : function(){
			
			var dbomId = this.comboDBom.getValue();
			if(dbomId == '' || dbomId == undefined || dbomId == null || dbomId == 'undefined'){
				Ext.ux.Toast.msg("操作信息", "请选择需要删除的分类！");
				return;
			}
			var options = {
				ids : dbomId,
				url : __ctxPath + '/oa/system/dbom/delete.do',
				msg : '您确认要删除所选分类吗？</br>【将同时删除该分类关联的DBom节点信息】',
				scope : this, 
				callback : this.reloadDBom
			};
			$postDel(options);
		},
		/**
		 * 重新加载DBom事件
		 */
		reloadDBom : function(){
			
			this.comboDBom.clearValue();
			this.comboDBom.getStore().reload();
			this.reloadTree();
		},
		/**
		 * 选择DBom事件
		 */
		selectComboDBom : function(combo){
			
			var dbomId = combo.getValue();
			//获取当前选中DBom的数据
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			var url = __ctxPath + '/oa/system/dbom/get.do?id='+dbomId;
			conn.open('post', url, false);
			conn.send();
			var dbom = Ext.decode(conn.responseText);
			var loader = this.centerLeftPanel.getLoader();
			loader.baseParams.pCode = dbom.data.code;
			loader.baseParams.nodeType = '-1';
			this.reloadTree();
		},
		/**
		 * 重新加载DBom树事件
		 */
		reloadTree : function(){
			this.centerLeftPanel.getRootNode().reload();
			this.centerLeftPanel.getRootNode().expand(true,false);
			
		},
		/**
		 * 单击DBom树节点事件
		 */
		clickDBomNode : function(){
			
			this.centerPanel.setDisabled(true);
			var selNode = this.centerLeftPanel.selectedNode;
			var basicForm = this.centerPanel.getForm();
			var type = selNode.attributes.type;
			if(type != '-1'){
				basicForm.reset();
				var id = selNode.attributes.id;
				this.centerPanel.loadData({
					url : __ctxPath + '/oa/system/dbomNode/get.do?id=' + id, 
					root : 'data'
				});
			}
		},
		/**
		 * 新增DBom节点
		 */
		addDyDBomNode : function(){
			var dbomId = this.comboDBom.getValue()
			this.centerPanel.setDisabled(false);
			var selNode = this.centerLeftPanel.selectedNode;
			var code = selNode.attributes.code;
			var nodeType = selNode.attributes.nodeType;
			var basicForm = this.centerPanel.getForm();
			basicForm.reset();
			basicForm.findField('pcode').setValue(code);
			basicForm.findField('dbomId').setValue(dbomId);
		},

		/**
		 * 编辑DBom节点事件
		 */
		editDBomNode : function(){
			var basicForm = this.centerPanel.getForm();
			basicForm.reset();
			var dbomId = this.comboDBom.getValue();
			this.centerPanel.setDisabled(true);
			var selNode = this.centerLeftPanel.selectedNode;
			var code = selNode.attributes.code;
			var type = selNode.attributes.type;
			basicForm.findField('dbomId').setValue(dbomId);
			if(type != '-1'){
				this.centerPanel.setDisabled(false);
				var id = selNode.attributes.id;
				this.centerPanel.loadData({
					url : __ctxPath + '/oa/system/dbomNode/get.do?id=' + id, 
					root : 'data'
				});
			}else{
				Ext.ux.Toast.msg('提示','当前节点不可编辑！');
			}
		},
		/**
		 * 保存DBom节点事件
		 */
		saveDBomNode : function(){
			//代号唯一性验证
			var basicForm = this.centerPanel.getForm();
			var code = basicForm.findField('code').getValue();
			if(code != '' && code != null ){
				var id = basicForm.findField('id').getValue();
				var result = this.checkDBomNode(id, code);
				if(result){
					Ext.ux.Toast.msg('提示','代号已存在，请重新输入！');
					return;
				}
			}
			//判断要追加的URL个数是否超过数据源或子节点数据源的个数，如果超过则提示添加失败。
			var dataSource = basicForm.findField('hiddenDataSource').getValue();
			var targetDataSource = basicForm.findField('hiddenTargetDataSource').getValue();
			if(targetDataSource == '' || targetDataSource == null){
				dataSource = dataSource;
			}else{
				dataSource = targetDataSource;
			}
			/*
			 * var total = dataSource==''?0:dataSource.split(',').length;
			var url = basicForm.findField('url').getValue();
			var urlNum = url==''?0:url.split(',').length;
			if(parseInt(total) != parseInt(urlNum)){
				Ext.ux.Toast.msg('提示', '表单数据保存失败，原因是业务数据模板地址个数不等于数据源或子节点数据源个数！');
				return;
			}*/
			//提交表单数据
			var options = {
				url : __ctx + '/oa/system/dbomNode/save.do',
				scope : this,
				waitMsg : '正在提交数据...',
				formPanel : this.centerPanel,
				callback  : this.reloadTree
			};
			$postForm(options);
		},
		/**
		 * 检查DBom节点代号是否存在事件
		 */
		checkDBomNode : function(id, code){
			
			var checkURL = __ctx + '/oa/system/dbomNode/check.do?id='+id+'&code='+code;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open('post', checkURL, false);
			conn.send();
			return Ext.decode(conn.responseText);
		},
		/**
		 * 删除DBom节点事件
		 */
		delDBomNode : function(){
			
			var selNode = this.centerLeftPanel.selectedNode;
			var type = selNode.attributes.type;
			if(type != '-1'){
				var id = selNode.attributes.id;
				var options = {
					ids : id,
					url : __ctxPath + '/oa/system/dbomNode/delete.do',
					msg : '您确认要删除所选DBom节点吗？</br>【将同时删除该DBom节点与DBom子节点等相关信息】',
					scope : this, 
					callback : function(){
						var basicForm = this.centerPanel.getForm();
						basicForm.reset();
						this.reloadTree();
					}
				};
				$postDel(options);
			}else{
				Ext.ux.Toast.msg('提示','当前节点不可删除！');
			}
		},
		
		/**
		 * 加载节点数据源事件
		 */
		loadDataSource : function(){
			var basicForm = panel.centerPanel.form;
			var params = {
				scope : this,
				mainTableName : '',
				singleSelect : true,
				callback : function(result){
					if(result == null){
						//置空节点数据源
						basicForm.findField('dataSource').setValue('');
						basicForm.findField('hiddenDataSource').setValue('');
						//置目标数据与节点数据源关联关系
						basicForm.findField('showFiled').setValue('');
						basicForm.findField('nodeKey').setValue('');
						basicForm.findField('pNodeKey').setValue('');
						
						//置目标数据URL
						basicForm.findField('url').setValue('');
					}else{
						var tableName = '';
						var tableDesc = '';
						for(var index=0; index<result.length; index++){
							var modelType = result[index].modelType;
							var prefix = modelType=='0'?'W_':'V_';
							tableName += prefix + result[index].tableName + ',';
							tableDesc += result[index].tableDesc + ',';
						}
						tableName = tableName==''?'':tableName.substring(0, tableName.length-1);
						tableDesc = tableDesc==''?'':tableDesc.substring(0, tableDesc.length-1);
						basicForm.findField('dataSource').setValue(tableDesc);
						basicForm.findField('hiddenDataSource').setValue(tableName);
						basicForm.findField('nodeKey').setValue('');
						basicForm.findField('pNodeKey').setValue('');
						basicForm.findField('showFiled').setValue('');
					}
				}
			};
			dbomDSForm.init(params).show();
		},
		
		/**
		 * 节点 KEY
		 */
		loadNodeKey : function(){
			var basicForm = panel.centerPanel.form;
			var dataSource = basicForm.findField('hiddenDataSource').getValue();
			if( dataSource == '' || dataSource == null){
				Ext.ux.Toast.msg('提示', '请先选择数据源！');
				return;
			}else{
				var dataSource = dataSource.split(',');
				if(dataSource.length > 1) dataSource = dataSource[0];
			}
			var params = {
				scope : this,
				tableName: dataSource, 
				callback : function(result){
					if(result == null){
						basicForm.findField('hiddenNodeKey').setValue('');
						basicForm.findField('nodeKey').setValue('');
					}else{
						var fieldName = '';
						var fieldDesc = '';
						for(var index=0; index<result.length; index++){
							fieldName += result[index].fieldName + ',';
							fieldDesc += result[index].fieldDesc + ',';
						}
						fieldName = fieldName==''?'':fieldName.substring(0, fieldName.length-1);
						fieldDesc = fieldDesc==''?'':fieldDesc.substring(0, fieldDesc.length-1);
						basicForm.findField('hiddenNodeKey').setValue(fieldName);
						basicForm.findField('nodeKey').setValue(fieldName);
					}
				}
			};
			dbomDNForm.init(params).show();
		},
		
		/**
		 * 父节点 KEY
		 */
		loadPNodeKey : function(){
			var basicForm = panel.centerPanel.form;
			var dataSource = basicForm.findField('hiddenDataSource').getValue();
			if( dataSource == '' || dataSource == null){
				Ext.ux.Toast.msg('提示', '请先选择数据源！');
				return;
			}else{
				var dataSource = dataSource.split(',');
				if(dataSource.length > 1) dataSource = dataSource[0];
			}
			var params = {
				scope : this,
				tableName: dataSource, 
				callback : function(result){
					if(result == null){
						basicForm.findField('hiddenPNodeKey').setValue('');
						basicForm.findField('pNodeKey').setValue('');
					}else{
						var fieldName = '';
						var fieldDesc = '';
						for(var index=0; index<result.length; index++){
							fieldName += result[index].fieldName + ',';
							fieldDesc += result[index].fieldDesc + ',';
						}
						fieldName = fieldName==''?'':fieldName.substring(0, fieldName.length-1);
						fieldDesc = fieldDesc==''?'':fieldDesc.substring(0, fieldDesc.length-1);
						basicForm.findField('hiddenPNodeKey').setValue(fieldName);
						basicForm.findField('pNodeKey').setValue(fieldName);
					}
				}
			};
			dbomDNForm.init(params).show();
		},
		
		/**
		 * 节点数据源 show text
		 */
		loadDataSourceShowText : function(){
			var basicForm = panel.centerPanel.form;
			var dataSource = basicForm.findField('hiddenDataSource').getValue();
			if( dataSource == '' || dataSource == null){
				Ext.ux.Toast.msg('提示', '请先选择数据源！');
				return;
			}else{
				var dataSource = dataSource.split(',');
				if(dataSource.length > 1) dataSource = dataSource[0];
			}
			var params = {
				scope : this,
				tableName: dataSource, 
				callback : function(result){
					if(result == null){
						basicForm.findField('hiddenShowFiled').setValue('');
						basicForm.findField('showFiled').setValue('');
					}else{
						var fieldName = '';
						var fieldDesc = '';
						for(var index=0; index<result.length; index++){
							fieldName += result[index].fieldName + ',';
							fieldDesc += result[index].fieldDesc + ',';
						}
						fieldName = fieldName==''?'':fieldName.substring(0, fieldName.length-1);
						fieldDesc = fieldDesc==''?'':fieldDesc.substring(0, fieldDesc.length-1);
						basicForm.findField('hiddenShowFiled').setValue(fieldName);
						basicForm.findField('showFiled').setValue(fieldName);
					}
				}
			};
			dbomDNForm.init(params).show();
		},
		
		/**
		 * 选择目标数据源
		 */
		loadClickDataSource : function(){
			var basicForm = panel.centerPanel.form;
			var params = {
				scope : this,
				mainTableName: basicForm.findField('hiddenDataSource').getValue(),
				callback : function(result, operationType){
					if(result == null){//置空数据源
						basicForm.findField('targetDataSource').setValue('');
						basicForm.findField('hiddenTargetDataSource').setValue('');
						basicForm.findField('targetDataRelation').setValue('');
						basicForm.findField('url').setValue('');
					}else{
						var tableName = '';
						var tableDesc = '';
						for(var index=0; index<result.length; index++){
							var modelType = result[index].modelType;
							var prefix = modelType=='0'?'W_':'V_';
							tableName += prefix + result[index].tableName + ',';
							tableDesc += result[index].tableDesc + ',';
						}
						tableName = tableName==''?'':tableName.substring(0, tableName.length-1);
						tableDesc = tableDesc==''?'':tableDesc.substring(0, tableDesc.length-1);
						if(operationType == 'append'){
							var oldSubDataSource = basicForm.findField('targetDataSource').getValue();
							var oldHiddenSubDataSource = basicForm.findField('hiddenTargetDataSource').getValue();
							tableDesc = oldSubDataSource==''?tableDesc:(oldSubDataSource+','+tableDesc);
							tableName = oldHiddenSubDataSource==''?tableName:(oldHiddenSubDataSource+','+tableName);
						}
						//暂时没必要显示表中文描述
						//basicForm.findField('targetDataSource').setValue(tableDesc);
						basicForm.findField('targetDataSource').setValue(tableName);
						basicForm.findField('hiddenTargetDataSource').setValue(tableName);
						
					}
				}
			};
			dbomDSForm.init(params).show();
		},
		
		/**
		 * 设置节点关联关系事件
		 */
		setNodeRelation : function(){
			var basicForm = panel.centerPanel.form;
			var dataSource = basicForm.findField('hiddenDataSource').getValue();
			var targetDataSource = basicForm.findField('hiddenTargetDataSource').getValue();
			var displayDataSource = basicForm.findField('dataSource').getValue();
			if(dataSource == '' || dataSource == null){
				Ext.ux.Toast.msg('提示', '请先选择数据源！');
				return;
			}
			if(targetDataSource == '' || targetDataSource == null){
				Ext.ux.Toast.msg('提示', '请先选择目标数据源！');
				return;
			}
			var id = basicForm.findField('id').getValue();
			if(id == '' || id == null){
				id = 0;
			}
			var params = {
				id : id,
				scope : this,
				dataSource: dataSource, 
				targetDataSource : targetDataSource,
				displayDataSource : displayDataSource,
				callback : function(relValue, displayValue,append){
					if(append){
						var oldClickNodeRelation = basicForm.findField('targetDataRelation').getValue();
						var targetDataRelation = oldClickNodeRelation==''?relValue:(oldClickNodeRelation+',\n'+relValue);
						basicForm.findField('targetDataRelation').setValue(targetDataRelation);
					}else{
						basicForm.findField('targetDataRelation').setValue(relValue);
					}
				}
			};
			dbomDRForm.init(params).show();
		},
		
		/**
		 * 设置业务数据模板地址选择事件
		 */
		setDataTemplateURL : function(){
			var basicForm = panel.centerPanel.form;
			var dataSource = basicForm.findField('hiddenDataSource').getValue();
			var targetDataSource = basicForm.findField('hiddenTargetDataSource').getValue();
			if( targetDataSource != null &&targetDataSource != ''){
				dataSource = targetDataSource;
			}
			var params = {
				scope : this,
				dataSource : dataSource,
				callback : function(url, operationType){
					if(operationType == 'append'){
						var oldUrl = basicForm.findField('url').getValue();
						//判断要追加的URL个数是否超过数据源或子节点数据源的个数，如果超过则提示添加失败。
						/*var total  = dataSource.split(',').length;
						var oldNum = oldUrl==''?0:oldUrl.split(',').length;
						var newNum = url.split(',').length;
						if(parseInt(total)<parseInt(oldNum)+parseInt(newNum)){
							Ext.ux.Toast.msg('提示', '当前操作失败，原因是业务数据模板地址个数超过数据源或子节点数据源个数！');
							return;
						}*/
						url = oldUrl==''?url:(oldUrl+',\n'+url);
					}else if(operationType == 'select'){
						//判断要添加的URL个数是否超过数据源或子节点数据源的个数，如果超过则提示添加失败。
						var total  = dataSource.split(',').length;
						var newNum = url.split(',').length;
						if(parseInt(total)<parseInt(newNum)){
							Ext.ux.Toast.msg('提示', '当前操作失败，原因是业务数据模板地址个数超过数据源或子节点数据源个数！');
							return;
						}
					}
					basicForm.findField('url').setValue(url);
				}
			};
			dBomURLForm.init(params).show();
		}
	});
	
});