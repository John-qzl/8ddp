if (typeof FormDef == 'undefined') {
	FormDef = {};
}
var editor;
FormDef.isSourceMode = false;

/**
 * 
 * @param {} conf
 * {lang }
 */
FormDef.getEditor = function(conf) {
	var h = $(window).height(),
		w = $(window).width(),
		lang = conf.lang?conf.lang:'zh_cn';
	h = conf.height?(h-conf.height):h;
	w = conf.width?(w-conf.width):w;	
	editor = new baidu.editor.ui.Editor({minFrameHeight:h,initialFrameWidth:w,lang:conf.lang});
	editor.addListener("sourceModeChanged",function(t,m){
		FormDef.isSourceMode = m;
	});
};

FormDef.openWin = function(title, width, height, url, buttons, frameId) {
	var left = ($(window).width() - width) / 2;
	var top = ($(window).height() - height) / 2;
	var p = {
		url : url,
		width : width,
		height : height,
		left : left,
		top : top,
		title : title,
		buttons : buttons,
		name : frameId
	};
	$.ligerDialog.open(p);
};

/**
 * 根据表获取字段和子表，构建树。
 * 
 * @param tableId
 */
FormDef.getFieldsByTableId = function(tableId) {
	if($("#colstree").length<=0)return ;//不存在树，则直接返回
		var iconFolder = __ctx + '/styles/tree/';
	$.post('getAllFieldsByTableId.do?tableId=' + tableId, function(data) {
		var json = eval("("+data+")"),
			treeData = [];
		$('#tableName').val(json.mainname);
		
		for(var i=0,c;c=json.mainfields[i++];){
			if(c.isHidden == 0){
				c.tableId = json.mainid;
				c.name = c.fieldDesc;
				c.id = c.fieldId;
				c.pId = 0;
				c.icon = iconFolder + c.fieldType + '.png';
				//主表标志
				c.nodeType = "main";
				treeData.push(c);
			}
		}
		
		for(var i=0,c;c=json.subtables[i++];){
			c.icon = iconFolder + 'table.png';
			c.pId = 0;
			c.tableId = c.id;
			//子表标志
			c.nodeType = "sub";
			treeData.push(c);
			for(var j = 0,m;m=c.subfields[j++];){
				m.tableId = c.id;
				m.pId = c.id;
				m.name = m.fieldDesc;
				m.icon = iconFolder + m.fieldType + '.png';
				//子表标志
				c.nodeType = "sub";
				treeData.push(m);
			}
		}
		for(var i=0,c;c=json.reltables[i++];){
			c.icon = iconFolder + 'table_rel.png';
			c.pId = 0;
			c.tableId = c.id;
			//关系表标志
			c.nodeType = "rel";
			treeData.push(c);
			for(var j = 0,m;m=c.relfields[j++];){
				m.tableId = c.id;
				m.tablename = c.tablename;
				m.pId = c.id;
				m.name = m.fieldDesc;
				m.icon = iconFolder + m.fieldType + '.png';
				//关系表标志
				c.nodeType = "rel";
				treeData.push(m);
			}
		}
		for(var i=0,c;c=json.parenttables[i++];){
			c.icon = iconFolder + 'table_rel.png';
			c.pId = 0;
			c.tableId = c.id;
			//父亲表标志
			c.nodeType = "parent";
			treeData.push(c);
			for(var j = 0,m;m=c.parentfields[j++];){
				m.tableId = c.id;
				m.tablename = c.tablename;
				m.pId = c.id;
				m.name = m.fieldDesc;
				m.icon = iconFolder + m.fieldType + '.png';
				//关系表标志
				c.nodeType = "parent";
				treeData.push(m);
			}
		}
		var setting = {       				    					
   				data: {
   					key : {
   						name: "name"
   					},
   					simpleData: {
   						enable: true,
   						idKey: "id",
   						pIdKey: "pId",
						rootPId: 0
   					}
   				},
   				
   				callback : {
   					beforeClick : function(treeId, treeNode, clickFlag) {
   						FormDef.insertHtml(editor, treeNode);
   						return false;
   					}
   				}
   			};
		glTypeTree = $.fn.zTree.init($("#colstree"),setting, treeData);
	});
};

/**
 * 重新生成html模版。
 * 
 * @param tableId
 * @param templateAlias
 */
FormDef.genByTemplate = function(tableId, templateAlias) {
	$.post(__ctx+'/oa/form/formDef/genByTemplate.do', {
		templateTableId : tableId,
		templateAlias : templateAlias
	}, function(data) {
		editor.setContent(data);
	});
};

/*
 * 编辑器页面
 */
var controls = {};
var controlsF = {};
var controlsR = {};

FormDef.insertHtml = function(editor, node) {

	if (node.fieldType) {
		// 如果是主表字段
		if (!controls[node.fieldName]) {
			//如果是关联表字段
			if(!controlsF[node.fieldName]){
				//如果是字表字段
				if(!controlsR[node.fieldName]){
					//获取父节点的nodeType
					if(node.getParentNode()!=undefined&&node.getParentNode()!=null&&node.getParentNode()!="")
						var fieldNodeType = node.getParentNode().nodeType;
					
					var templatesId = $('#templatesId').val();	
					var	templateAlias = FormDef.parseTemplateAlias(templatesId,node.tableId);
					if(!$.isEmpty(templateAlias)){
						//如果fieldNodeType有值
						if(fieldNodeType!=undefined&&fieldNodeType!=null&&fieldNodeType!=""){
							FormDef.insertField(editor,node,templateAlias,fieldNodeType);
						}else{
							FormDef.insert(editor, node,templateAlias);
						}
					}else{//如果获取当前的模板ID则重新选择模板
						//如果fieldNodeType有值
						if(fieldNodeType!=undefined&&fieldNodeType!=null&&fieldNodeType!=""){
							FormDef.selectTemplateField(editor,node,fieldNodeType);
						}else{
							FormDef.selectTemplate(editor,node);
						}
					}
				}else{
					editor.execCommand('inserthtml', controlsR[node.fieldName],1);
				}
			}else{
				editor.execCommand('inserthtml', controlsF[node.fieldName],1);
			}
		} else {
				editor.execCommand('inserthtml', controls[node.fieldName],1);
		}
	} else {
		FormDef.selectTableTemplate(editor,node);
	}
};

/**
 * 插入
 * @param {} editor
 * @param {} node
 * @param {} templateId
 */
FormDef.insert = function (editor, node,templateAlias){
	$.post('getControls.do', {
		templateAlias : templateAlias,
		tableId : node.tableId
	}, function(data) {
		if( $.isEmptyObject(data)||data.length == 0)
			return FormDef.selectTemplate(editor,node);
		if ($.isEmpty(data) )
			return FormDef.selectTemplate(editor,node);
		controls = data;
		editor.execCommand('inserthtml',controls[node.fieldName],1);
	});
};
/**
 * 插入（字段存在nodeType时）
 * @param {} editor
 * @param {} node
 * @param {} templateId
 * @param {} fieldNodeType
 */
FormDef.insertField = function (editor,node,templateAlias,fieldNodeType){
	$.post('getControls.do', {
		templateAlias : templateAlias,
		tableId : node.tableId,
		fieldNodeType : fieldNodeType
	}, function(data) {
		
		if( $.isEmptyObject(data)||data.length == 0)
			return FormDef.selectTemplate(editor,node);
		if ($.isEmpty(data) )
			return FormDef.selectTemplate(editor,node);
		if(fieldNodeType=="rel"){
			for(var key in data){
				if(controlsR.hasOwnProperty(key))continue;//有相同的就略过
				controlsR[key]=data[key];
			}
			editor.execCommand('inserthtml',controlsR[node.fieldName],1);
		}else{
			for(var key in data){
				if(controlsF.hasOwnProperty(key))continue;//有相同的就略过
				controlsF[key]=data[key];
			}
			editor.execCommand('inserthtml',controlsF[node.fieldName],1);
		}
	});
};
/**
 * 选择模板
 */
FormDef.selectTemplate = function(editor,node){
	FormDef.showSelectTemplate('selectTemplate.do?tableId=' + node.tableId+ '&isSimple=1&nodeType='+node.nodeType,
			function(item, dialog) {
				var form = $(document.getElementById('selectTemplate').contentDocument);
				if($.isIE()&&(!form||form.length==0))
					form = $(document.frames['selectTemplate'].document);
				
				var templatesId = FormDef.getTemplatesId(form);
				$('#templatesId').val(templatesId);
				var	templateAlias = FormDef.parseTemplateAlias(templatesId,node.tableId);
				dialog.close();
				FormDef.insert(editor, node,templateAlias);
			});
}
/**
 * 选择模板（字段存在nodeType时）
 */
FormDef.selectTemplateField = function(editor,node,fieldNodeType){
	FormDef.showSelectTemplate('selectTemplate.do?tableId=' + node.tableId+ '&isSimple=1' + '&fieldNodeType=' + fieldNodeType,
			function(item, dialog) {
				var form = $(document.getElementById('selectTemplate').contentDocument);
				if($.isIE()&&(!form||form.length==0))
					form = $(document.frames['selectTemplate'].document);
				
				var templatesId = FormDef.getTemplatesId(form);
				$('#templatesId').val(templatesId);
				var	templateAlias = FormDef.parseTemplateAlias(templatesId,node.tableId);
				
				dialog.close();
				FormDef.insertField(editor, node,templateAlias,fieldNodeType);
			});
}

/**
 * 关系表， 子表， 父亲都是调用这个方法 
 */
FormDef.selectTableTemplate = function(editor, node) {
	var selectTemplateCallBack=function(item, dialog) {
		var form = $(document.getElementById('selectTemplate').contentDocument);
		if($.isIE()&&(!form||form.length==0)){
			form = $(document.frames['selectTemplate'].document);
		}
		var templateAlias = $('select[templateId="templateId"]', form).val();
		dialog.close();
		$.post('genByTemplate.do', {
			templateTableId : node.tableId,
			templateAlias : templateAlias,
			nodeType:node.nodeType
		}, function(data) {
			editor.execCommand('inserthtml', data,1);
		});
	};
	var url = 'selectTemplate.do?tableId=' + node.tableId
			+ '&isSimple=1&nodeType=' + node.nodeType;
	FormDef.showSelectTemplate(url, selectTemplateCallBack);
}
/**
 * 如果是关系表,选择关系表模板
 *//*
FormDef.selectRelTemplate = function(editor, node) {
	var url = 'selectTemplate.do?tableId=' + node.tableId
			+ '&isSimple=1&nodeType=' + node.nodeType;
	FormDef.showSelectTemplate(url, FormDef.selectTemplateCallBack);
}

*//**
 * 如果是关系表,选择关系表模板
 *//*
FormDef.selectParentTemplate = function(editor, node) {
	var url = 'selectTemplate.do?tableId=' + node.tableId
			+ '&isSimple=1&nodeType=' + node.nodeType;
	FormDef.showSelectTemplate(url, FormDef.selectTemplateCallBack);
}*/

/**
 * 解析模板别名
 * @param {} templatesId
 * @param {} tableId
 */
FormDef.parseTemplateAlias = function(templatesId,tableId){
	if($.isEmpty(templatesId)) return '' ;
	var t = templatesId.split(";");
	for(var i=0,c;c=t[i++];){
		var s =c.split(",");
		if(s[0] == tableId){
			return s[1];
		}
	}
};

FormDef.getTemplatesId = function(form){
	var aryTemplateId = [];
	$("select[templateId='templateId']", form).each(function(i) {
		var tableId= $(this).attr("tableid"),templateId =$(this).val();
		aryTemplateId.push(tableId+","+templateId);
	});

	return  aryTemplateId.join(";");
};

// 显示选择模板窗口
FormDef.showSelectTemplate = function(url, callback) {
	if (!callback)
		callback = FormDef.onOk;
	var buttons = [ {
		text : '确定',
		onclick : callback
	} ];
	var newUrl = url+'&templatesId='+$('#templatesId').val();
	FormDef.openWin('选择模板', 550, 350, newUrl, buttons, "selectTemplate");
};

FormDef.onOk = function(item, dialog) {
	var form = $(document.getElementById('selectTemplate').contentDocument);
	if($.isIE()&&(!form||form.length==0))
		form = $(document.frames['selectTemplate'].document);
	var aryTableId = [],aryTemplateId = [],templatesId=[];

	$("select[templateId='templateId']", form).each(function(i) {
		var tableId= $(this).attr("tableid"),templateId =$(this).val();
		aryTableId.push(tableId);
		aryTemplateId.push(templateId);
		templatesId.push(tableId+","+templateId);
	});

	FormDef.genByTemplate(aryTableId.join(","), aryTemplateId.join(","));
	$('#templatesId').val(templatesId.join(";"));
	dialog.close();
};

FormDef.showResponse = function(data) {
	var obj = new com.ibms.form.ResultMessage(data);
	if (obj.isSuccess()) {// 成功
		$.ligerDialog.success('保存成功!', '提示', function() {
			window.onbeforeunload = null;
			if(window.opener){
				if(window.opener.parent.reload){
					window.opener.parent.reload();
				}
				else if(window.opener.reload){
					window.opener.reload();
				}
			}
			window.close();
		});
	} else {// 失败
		$.ligerDialog.err('提示', obj.getMessage());
	}
};
