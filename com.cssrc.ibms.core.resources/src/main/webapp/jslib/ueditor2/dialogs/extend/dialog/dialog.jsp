<%@page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML >
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/commons/include/form.jsp" %>
<link rel="stylesheet" type="text/css" href="../input.css">
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/ueditor2/dialogs/internal.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.dragdiv.js"></script><!-- 可拖拽div插件,可与zTree的拖拽树配合使用 -->
<f:link href="jquery/plugins/jquery.dragdiv.css" ></f:link>
<link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.cssyle.css" type="text/css" />
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js"></script><!-- 构建“自定义对话框”设置页面，右下角的可拖动面板 -->
<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
</head>
<body>
	<div id="inputPanel" style="height:100%;overflow: auto;">
	<!-- 对话框设置 -->
		<fieldset class="base">
			<legend><var id="lang_dialog_setting"></var></legend>
			<table>
				<tr>
					<th><var id="lang_choose_dialog"></var></th>
					<td>
					<select id="dialog-type" onchange="dialogChange()">
						<option value="0"></option>
					</select>
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">
						<div id="fieldContainer">
							<table id="fieldTable"></table>
						</div>
					</td>
				</tr>
			</table>
		</fieldset>
		<!-- 回填值设置 -->
		<fieldset class="base">
			<legend><var id="lang_return_setting"></var></legend>
			<div class="fields-div">
				<ul id="fields-tree" class="ztree field-ul"></ul>
			</div>
			<div class="domBtnDiv"></div>
		</fieldset>
	</div>
	
	<textarea id="templateTr" style="display:none;">
	<#list data as a>
		<#if (!a.items)>
		<tr class="fieldTr">
			<th>
			<#if (a_index == 0)>主表参数</#if>
			</th>
			<td>
				<input type="checkbox" style="width:10px;" class="fieldCheck" />&nbsp;
				<!-- 主表字段 -->
				<input type="text" class="mainField" style="width:116px" value="\${a.name}" readonly id="\${a.id}"/>&nbsp;
				<select class="fieldSelect" style="width:95px;display:none;">
					<option></option>
				</select>
			</td>
		</tr>
		<#else>
			<#list a.items as sub>
			<tr class="fieldTr">
				<th><#if (sub_index == 0)>子表参数</#if></th>
				<td>
					<input type="checkbox" style="width:10px;" class="fieldCheck" />&nbsp;
					<input type="text" class="subField" style="width:60px" value="\${sub.name}" readonly id="\${sub.id}"/>&nbsp;
					<select class="fieldSelect" style="width:95px;display:none;">
						<option></option>
					</select>
				</td>
			</tr>
			</#list>
		</#if>
	</#list>
	</textarea>
	<textarea id="templateOption" style="display:none;">
	<#list data as o>
		<option value="\${o.field}">\${o.field}</option>
	</#list>
	</textarea>
	<script type="text/javascript">
	<!--在/WebRoot/jslib/ueditor2/_src/ui/editor.js 中 _onInputDialogButtonClick 方法中赋值。-->
	
		var obj = $(editor.curInput);
		var bodyObj=obj.parents('body');
		var setting = {
				edit: {
					enable: true,
					showRemoveBtn: false,
					showRenameBtn: false,	
					drag:{}
				},				
				data: {
					keep: {
						parent: true,
						leaf: true
					},
					simpleData: {
						enable: true
					}
				},				
				view: {
					selectedMulti: false
				}
			},
			dragDiv;

		$(function() {
			$(".button-td").bind("mouseenter mouseleave", function() {
				$(this).toggleClass("button-td-hover");
			});
			//获取自定义对话框   初始化 “对话框设置” 里的 “选择对话框” 列表信息
			getDialogs();
			
			$('.fieldCheck','.fieldTr').live("click",function(){
				$(this).siblings('.fieldSelect').each(function(){
					if($(this).css('display')!='none'){
						$(this).hide() ;
					}else{
						$(this).show() ;
					}
				});
				
			});
			
		});		
		//编辑时绑定数据
		function bindData(dialogStr) {
			var dialog = eval("("+dialogStr+")" ),field;
			if(!dialog)return;
			$("#dialog-type").find("option[value='"+dialog.name+"']").each(function(){
					$(this).attr("selected","selected");
					dialogChange();
				}
			);
			while(field=dialog.fields.pop()){
				var src=field.src,
					targets=field.target.split(','),target;
				while(target=targets.pop()){
					var item = $("span.item-span[itemId='"+target+"']").toggleClass("item-span item-span-Disabled");
					if(item.length>0){
						var node = {id:target, name: item.text()};
						addNode(src,node);
					}
				}
			}
			var dialogQueryArr = dialog.query;
			for(var i=0;i<dialogQueryArr.length;i++){
				var isMain = dialogQueryArr[i].isMain ;
				$(":text#"+dialogQueryArr[i].id,".fieldTr").each(function(){
					//字段isMain和属性class==mainfield同时为true或者false时
					if((isMain=="true" ^ $(this).attr("class")=="mainField")==0){
						$(this).siblings(".fieldCheck").click() ;
						$(this).siblings(".fieldSelect").find("option[value='"+dialogQueryArr[i].name+"']").each(function(){
							$(this).attr("selected","selected");
						});
					}
				});
			}
		};
		//添加树节点
		function addNode(id,node){
			var zTree = $.fn.zTree.getZTreeObj('fields-tree');
			if(!zTree)return;
			var parentNode = zTree.getNodeByParam("id",id,null);
			if(parentNode) zTree.addNodes(parentNode,node);
		};
		
		//获取自定义对话框
		function getDialogs(){
			var url = __ctx + '/oa/form/formDialog/getAllDialogs.do';
			$.get(url,function(data){
				if (data) {
					for(var i=0,c;c=data[i++];){
						var opt = $('<option dialogtype="'+c.dialogType+'" value="'+c.alias+'" fields="'+c.returnFields+'" conditionfield="'+c.conditionfield
								+'" istable="'+c.istable+'" objname="'+c.objname+'" dsname="'+c.dsalias+'" >'+c.name+'</option>');
						opt.data("fields",c.resultfield);
						opt.data("conditionfield",c.conditionfield);
						$("#dialog-type").append(opt);
						//$("#dialog-type").append('<option value="'+c.alias+'" fields="'+c.returnFields+'">'+c.name+'</option>');
					}
					//加载字段面板
					getFileds(editor.tableId);
				}
			});
		};		
		//选择不同的对话框
		function dialogChange(){
			//var v=$("#dialog-type").find("option:selected").attr("fields");
			var dia=$("#dialog-type").find("option:selected");
			var v = dia.data("fields");
			var c = dia.data("conditionfield");

			if(v){
				var nodes=[];
				var fields = $.parseJSON(v);
				for(var i=0;i<fields.length;i++){
					var f=fields[i];
					nodes.push({id:f.field,pid:0,name:f.comment,isParent: true, open:true});
				}

				$("span.item-span-Disabled").each(function(){
						$(this).toggleClass("item-span-Disabled");
						$(this).toggleClass("item-span");
					}						
				);
				var tree = $.fn.zTree.init($("#fields-tree"), setting, nodes);
				//设置拖拽 树对象
				if(dragDiv)dragDiv.dragdiv('bind','fields-tree');
			}
			if(c){
				var conditionfield = $.parseJSON(c);
				//生成“绑定参数”下拉框选项
				var template = $('textarea#templateOption').val() ;
				var templateOptionHtml = easyTemplate(template,conditionfield).toString();
				$('#inputPanel table').find('.fieldSelect').html(templateOptionHtml);
			}
			
		}

		dialog.onok = function() {
			var dialogtype = $("#dialog-type").find("option:selected").attr("dialogtype");
			var name=$("#dialog-type").val();
			if(!name){
				$(editor.curInput).removeAttr("dialog");
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("fields-tree"),
			nodes=zTree.getNodes(),fields=[];
			
			for(var i=0,c;c=nodes[i++];){
				if(!c.children)continue;
				var target=[],child=null;				
				while(child=c.children.pop()){
					target.push(child.id);
				}
				if(target.length>0){
					var sub="{src:'"+c.id+"',target:'"+target.join(',')+"'}";
				}
				fields.push(sub);
			}
			var queryArr = [] ;
			$(':checkbox:checked','table .fieldTr').each(function(){
				var queryObj = {} ;
				queryObj.id = $(this).siblings(':text').attr('id') ;
				queryObj.name = $(this).siblings('select').val() ;
				if($(this).siblings(':text').attr('class')=='mainField'){
					queryObj.isMain = 'true' ;
				}else{
					queryObj.isMain = 'false' ;
				}
				queryArr.push(queryObj);
			});
			var queryString = JSON2.stringify(queryArr).replaceAll("\"","'") ;
			var json="{name:'"+name+"',fields:["+fields.join(',')+"],query:"+queryString+"}";
			editor.curInput.setAttribute("dialog",json);
			editor.curInput.setAttribute("dialogtype",dialogtype);
			editor.curInput = null;
		};
		//初始化字段面板 右下角
		function initFieldsDiv(data){
			//
			//主表字段
			var mainTable = data.table, data = {};
			data.name = mainTable.tableDesc + '('+editor.getLang("customdialog.main")+')';
			data.id = mainTable.tableName;
			data.desc = mainTable.tableId;
			//获取页面上所有当前表的字段
			var formMainField=$(bodyObj).find('[name*="'+mainTable.tableName+'"]');
			
			var items = [];
			//循环后台取得的表字段列 c
			for ( var i = 0, c; c = mainTable.fieldList[i++];) {
				if(c.isHidden!=0) continue ;
				//判断当前字段是否在表单上显示，如果存在这显示，
				if(formMainField.length != 0 && removeObj(formMainField,c.fieldName))continue;
				items.push({
					name : c.fieldDesc,
					id : c.fieldName
				});
			}
			// sub子表字段 
			//
			for ( var i = 0, c; c = mainTable.subTableList[i++];) {
				var sub = {};
				sub.name = c.tableDesc + '('+editor.getLang("customdialog.sub")+')';
				sub.id = c.tableName;
				sub.desc = c.tableId;
				var currSubField=$(bodyObj).find('[name*="'+c.tableName+'"]');
				var subItems = [];
				for ( var y = 0, t; t = c.fieldList[y++];) {
					//判断当前字段是否在表单上显示，如果存在这显示，不存在这不显示
					if(formMainField.length != 0 && removeObj(currSubField,t.fieldName))continue;
					subItems.push({
						name : t.fieldDesc,
						id : t.fieldName
					});
				}
				sub.items = subItems;
				items.push(sub);
			}
			 //rel从表字段
			 for ( var i = 0, c; c = mainTable.relTableList[i++];) {
				//
				var rel = {};
				rel.name = c.tableDesc + '('+editor.getLang("customdialog.rel")+')';
				rel.id = c.tableName;
				rel.desc = c.tableId;
				var currRelField=$(bodyObj).find('[name*="'+c.tableName+'"]');
				var relItems = [];
				for ( var y = 0, t; t = c.fieldList[y++];) {
					//判断当前字段是否在表单上显示，如果存在这显示，不存在这不显示
					if(formMainField.length != 0 && removeObj(currRelField,t.fieldName))continue;
					relItems.push({
						name : t.fieldDesc,
						id : t.fieldName
					});
				}
				rel.items = relItems;
				items.push(rel);
			}
			//生成“绑定参数”行
			var template = $('textarea#templateTr').val() ;
			var templateTrHtml = easyTemplate(template,items).toString();
			$('#fieldTable').append(templateTrHtml);
			
			var parentTableClass = $(editor.curInput).closest('div[type="subtable"]') ;
			if(!parentTableClass || parentTableClass.length<=0){
				//若为空，表示主表，则隐藏子表字段
				$('.subField').each(function(){
					$(this).closest('.fieldTr').hide() ;
				})
			}
			
			var parentrelTableClass = $(editor.curInput).closest('div[type="reltable"]') ;
			if(!parentrelTableClass || parentrelTableClass.length<=0){
				//若为空，表示主表，则隐藏rel表字段
				$('.relField').each(function(){
					$(this).closest('.fieldTr').hide() ;
				})
			}
			
			
			data.items = items;
			//初始化字段面板
			//
			dragDiv = $(".domBtnDiv").dragdiv('init',{data : data});
			var dialogStr = $(editor.curInput).get(0).getAttribute("dialog");
			if (dialogStr) {
				bindData(dialogStr);
			}
		};

		//加载字段面板
		function getFileds(tableId) {
			if(tableId){
				var url = __ctx
						+ '/oa/form/formTable/getTableById.do?tableId='
						+ tableId;
				$.post(url, function(data) {
					//初始化字段面板   自定义对话框的 右下角区域 界面
					initFieldsDiv(data);
				});
			}
			else{	//编辑器设计表单时获取字段并验证字段
				var html = editor.getContent();		
				var params={};
				params.html=html;
				params.formDefId=editor.formDefId;
				
				//对表单的html进行验证，验证html是否合法。
				$.post(__ctx + '/oa/form/formDef/validDesign.do', params, function(data){
					if(data.valid){
						initFieldsDiv(data);
					}
					else{
						alert(data.errorMsg);
					}
				});
			}
		};
		
		//判断当前字段是否在表单上显示，如果存在这显示，不存在这不显示
		function removeObj(obj,fieldName){
			//当前表单所有字段
			//
			var AllField=$(obj);
			for( var i=0 ;i<AllField.length; i++){
				//获取页面上的字段name
				var formName=$(AllField[i]).attr("name");
				if(!formName)continue;
				var name=formName.split(":");
				if(name[2]==fieldName){
					return false;
				}else{
					continue;
				}
			}
			return true;
		};
	</script>
</body>
</html>