<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>数据模板设置</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/lang/view/oa/form/zh_CN.js"></script>
<f:link href="jquery.qtip.css"></f:link>
<f:link href="dataTemplateEdit/dataTemplateEdit.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.fix.clone.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/formdata.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/codemirror/lib/util/matchbrackets.js"></script>
<script type="text/javascript" src="${ctx}/jslib/codemirror/mode/groovy/groovy.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ScriptDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/AddResourceDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/BpmDefinitionDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/displaytag.js" ></script><!-- 表单按钮和管理标签绑定事件js -->
<script type="text/javascript">
	var tab = "";
	var isTempId=${!empty bpmDataTemplate.id};
	$(function(){
		//Tag Layout
		tab =$("#tab").ligerTab({
			//onBeforeSelectTabItem:onBeforeSelectTabItem
		});	
		__DataTemplate__.init();

		var options={};
		if(showResponse){
			options.success=showResponse;
		}	

		$("a.save").click(function() {
			var form=$('#dataTemplateForm');
			if(!form.valid())
				return;	
			if(saveChange(form)){
				if(isTempId){
					$.ligerDialog.confirm( "保存会覆盖模板，如果修改了模板请手动保存表单模板后再进行保存业务数据模板，是否继续保存？","提示信息", function(rtn) {
						if(rtn){
							customFormSubmit(form,options);	
							}
					});
				}else{
					customFormSubmit(form,options);	
				}
			}
		});
		// 批量授权
		$("select.rightselect").change(setPermision);
	});
	
	function manageFieldValid(){
		var name =new Array();
		$("#manageTbl").find("[var='name']").each(function() {
			name.push( $(this).val());
		});
		return isRepeat(name);
	}
	//验证排序字段
	function sortFieldValid(){
		var i = 0;
		$("#sortTbl tbody tr").each(function(){
			i++;
		});
		if(i>3)
			return true;
		return false;		
	}
	
	function isRepeat(arr) {
	    var hash = {};
	    for(var i in arr) {
	        if(hash[arr[i]]) {
	            return true;
	        }
	        // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
	        hash[arr[i]] = true;
	    }
	    return false;
	}
	//导出字段
	function getExportField(){
		var json = [];
		$("#exportFieldTbl tr[var='exportTableTr']").each(function(){
			var me = $(this),table={},tableName=$("[var='tablename']",me).val(),fields = [];
				table.tableName =tableName;
				table.tableDesc =$("[var='tabledesc']",me).val();
				table.isMain =$("[var='ismain']",me).val();
				$("#exportFieldTbl tr[var='exportFieldTr']").each(function(){
					var self = $(this),obj={},
						name  = $("[var='tablename']",self).val();
					if(tableName == name){
					
						obj.name =$("[var='name']",self).html();
						obj.desc =$("[var='desc']",self).val();
						obj.type =$("[var='type']",self).val();
						obj.style =$("[var='style']",self).val();
						obj.tableName =tableName;
						obj.isMain =$("[var='ismain']",self).val();
						obj.right = getRightJson(self,2);
						fields.push(obj)
					}
				});
				table.fields = fields;
				json.push(table);
		});
		return json;
	}	
	
	//显示字段
	function getDisplayField(){
		var json = [];
		$("#displayFieldTbl tr[var='displayFieldTr']").each(function(){
			var me = $(this),obj={};
			obj.name =$("[var='name']",me).html();
			obj.desc =$("[var='desc']",me).val();
			obj.type =$("[var='type']",me).val();
			obj.style =$("[var='style']",me).val();
			obj.controltype =$("[var='controltype']",me).val();
			obj.right = getRightJson(me,1);
			//新增 displayType、action、onclick、urlParams
			obj.displayType = $("[var='displayType']", me).children('option:selected').val();
			obj.action = $("[var='action']",me).val();
			obj.onclick = $("[var='onclick']",me).val();
			obj.urlParams = $("[var='setUrlParams']", me).val();
			//新增固定列属性 isFix:{true,false}
			obj.isFix = $("[var='isFix']", me).val();
			
			json.push(obj);
		});
		return json;
	}	
	function getRightJson(me,flag){
		var rightJson = [];
		if(flag ==1){
			var fieldRight = $("[var='fieldRight']",me);
			var printRight = $("[var='printRight']",me);
			rightJson.push(getRight(fieldRight,0));
			rightJson.push(getRight(printRight,1));
		}else{
			var exportRight = $("[var='exportRight']",me);
			rightJson.push(getRight(exportRight,2));
		}
		return rightJson;
	}
	
	function getRight(rightTd,s){
		var obj={};
		obj.s= s;
		obj.type =$("[var='right']",rightTd).val();
		obj.id =$("[var='rightId']",rightTd).val();
		obj.name =$("[var='rightName']",rightTd).val();
		obj.script =$("[var='rightScript']",rightTd).val();
		return obj;
	}
	
	
	function getConditionField(){
		var fields=new Array();
	
		$("#conditionTbl tbody tr").each(function(){
			var tr=$(this);
			var field=$.parseJSON(tr.find("input[type='hidden']").val());
			//var jt=tr.find("[name='jt']").val();
			var ct =tr.find("[name='ct']").val();
			var op =tr.find("[name='op']").val();
			var vf =parseInt(tr.find("[name='vf']").val());
			var va =tr.find("[name='va']").val();
			var cm =tr.find("[name='cm']").val();
			var qt =__DataTemplate__.getQueryType(field.ty,op);
			//field.jt=jt;
			field.op=op;
			field.ct=ct;
			field.vf=vf;
			field.va=va;
			field.cm=cm;
			field.qt=qt;
			fields.push(field);
		});
		return fields;
	}
	
	//排序字段
	function getSortField(){
		var fields=new Array();
		$("#sortTbl tbody tr").each(function(){
			var me=$(this),obj={};
			obj.name =$("[var='name']",me).html();
			obj.desc =$("[var='desc']",me).html();
			obj.sort =$("[var='sort']",me).val();
			obj.source =$("[var='source']",me).val();
			fields.push(obj);
		});
		return fields;
	}
	
	//子表排序字段
	function getSubSortField(){
		var tabs=new Array();
		
		$("table[id*='subSortTbl-']").each(function(i,tabdom){
			var tableid=$(tabdom).attr("id").replace("subSortTbl-","");
			var tab={};
			var fields=new Array();
			$(tabdom).children("tbody").children("tr").each(function(){
				var me=$(this),obj={};
				obj.name =$("[var='name']",me).html();
				obj.desc =$("[var='desc']",me).html();
				obj.sort =$("[var='sort']",me).val();
				obj.source =$("[var='source']",me).val();
				fields.push(obj);
			});
			tab[tableid]=fields;
			tabs.push(tab);
		})
		return tabs;
	}
	function getRelSortField(){
		var tabs = [];
		$("table[id*='relSortTbl-']").each(function(i,tabdom){
			var tableid=$(tabdom).attr("id").replace("relSortTbl-","");
			var tab={};
			var fields=new Array();
			$(tabdom).children("tbody").children("tr").each(function(){
				var me=$(this),obj={};
				obj.name =$("[var='name']",me).html();
				obj.desc =$("[var='desc']",me).html();
				obj.sort =$("[var='sort']",me).val();
				obj.source =$("[var='source']",me).val();
				fields.push(obj);
			});
			tab[tableid]=fields;
			tabs.push(tab);
		})
		return tabs;
	}
	//过滤字段
	function getFilterField(){
		var fields=new Array();
		$("#filterTbl tbody tr").each(function(){
			var me=$(this),obj={},rightJson = [];
			obj.name =$("[var='name']",me).html();
			obj.key=$("[var='key']",me).html();
			obj.type=$("[var='type']",me).val();
			obj.condition = $("[var='condition']",me).val();
			rightJson.push(getRight($("[var='filterRight']",me),3));
			obj.right = rightJson;
			fields.push(obj);
		});
		return fields;
	}
	
	//管理字段
	function getManageField(){
		var fields=new Array();
		$("#manageTbl tbody tr").each(function(){
			var me=$(this),obj={},rightJson = [];
			obj.desc =$("[var='desc']",me).val();
			obj.unique =$("[var='unique']",me).val();
			obj.name =$("[var='name']",me).val();
			obj.paramscript =$("textarea[var='paramscript']",me).val();
			obj.prescript =$("textarea[var='prescript']",me).val();
			obj.afterscript =$("textarea[var='afterscript']",me).val();
			rightJson.push(getRight($("[var='manageRight']",me),4));
			obj.right = rightJson;
			fields.push(obj);
		});
		return fields;
	}
	function saveChange(form){
		//判断排序字段太多报错问题
		if(sortFieldValid()){
			$.ligerDialog.error("排序字段不能设置超过3个，请检查！","提示信息");
			tab.selectTabItem("sortSetting");
			return false;
		}
		//判断管理字段
		// 不在校验按钮类型是否重复
		/* if(manageFieldValid()){
			$.ligerDialog.error("功能按钮出现重复的类型，请检查！","提示信息");
			tab.selectTabItem("manageSetting");
			return ;
		} */
		var needPage=$("input[name='needPage']:checked").val();
		
		var templateAlias=$("#templateAlias").val();
		if(templateAlias== ""|| needPage ==""){
			tab.selectTabItem("baseSetting");
			form.valid();
			return false;	
		}
		//显示字段
		var displayField= JSON2.stringify(getDisplayField());	
		//条件字段
		var conditionField= JSON2.stringify(getConditionField());
		//排序字段
		var sortField= JSON2.stringify(getSortField());
		//子表排序字段
		var subSortField= JSON2.stringify(getSubSortField());
		//关系表排序字段
		var relSortField = JSON2.stringify(getRelSortField());
		//过滤字段字段
		var filterField= JSON2.stringify(getFilterField());
		//管理字段
		var manageField= JSON2.stringify(getManageField());
		//导出字段
		var exportField= JSON2.stringify(getExportField());


		$('#displayField').val(displayField);
		$('#conditionField').val(conditionField);
		$('#sortField').val(sortField);
		$('#subSortField').val(subSortField);
		$('#relSortField').val(relSortField);
		$('#filterField').val(filterField);
		$('#manageField').val(manageField);
		$('#exportField').val(exportField);
		return true;
	}
	
	/**
	 * 自定义外部表单并提交
	 * @return void
	 */
	function customFormSubmit(form,options){
		var id=$("#id").val();
		var tableId=$("#tableId").val();
		var formKey=$("#formKey").val();
		var source=$("#source").val();
		var defId=$("#defId").val();
		var isQuery=$("#isQuery").val();
		var isFilter=$("#isFilter").val();
		var isBakData=$("#isBakData").val();
		
		var needPage=$("input[name='needPage']:checked").val();
		var pageSize=$("#pageSize").val();

		var templateAlias=$("#templateAlias").val();
		//显示字段
		var displayField= $('#displayField').val();
		//条件字段
		var conditionField= $('#conditionField').val();
		//排序字段
		var sortField= $('#sortField').val();
		//子表排序字段
		var subSortField= $('#subSortField').val();
		//关联表排序字段
		var relSortField= $('#relSortField').val();
		//过滤字段字段
		var filterField= $('#filterField').val();
		//管理字段
		var manageField= $('#manageField').val();
		var exportField = $('#exportField').val();
		var json={
			id:id,
			tableId:tableId,
			formKey:formKey,
			source:source,
			defId:defId,
			isQuery:isQuery,
			isFilter:isFilter,
			isBakData:isBakData,
			needPage:needPage,
			pageSize:pageSize,
			displayField:displayField,
			conditionField:conditionField,
			sortField:sortField,
			subSortField:subSortField,
			relSortField:relSortField,
			filterField:filterField,
			manageField:manageField,
			exportField:exportField,
			templateAlias:templateAlias
		};
		var form = $('<form method="post" action="save.do"></form>');
		var input = $("<input type='hidden' name='json'/>");
		var jsonStr=JSON2.stringify(json);		
		input.val(jsonStr);
		form.append(input);
		form.ajaxForm(options);
		form.submit();
	}
	
	
	function showResponse(responseText) {
		$.ligerDialog.closeWaitting();
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm( obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
				if(rtn){
					window.location.href =  location.href.getNewUrl();
				}else{
					window.close();
				}
			});
			
		} else {
			$.ligerDialog.err("提示信息","",obj.getMessage());
		}
	}
	//预览
	function preview(id){
		if($.isEmpty(id)){
			$.ligerDialog.error("请设置完信息保存后预览!","提示信息");
			return ;
		}
		var url=__ctx+ "/oa/form/dataTemplate/preview.do?__displayId__="+id;
		var winArgs="dialogWidth=800px;dialogHeight=600px;help=0;status=0;scroll=0;center=1";
		url=url.getNewUrl();
		$.openFullWindow(url);
	}
	//编辑模板
	function editTemplate(id){
		if($.isEmpty(id)){
			$.ligerDialog.error("请设置完信息保存后编辑模板!","提示信息");
			return ;
		}
		var url=__ctx+ "/oa/form/dataTemplate/editTemplate.do?id="+id;
		//var winArgs="he=800px;dialogHeight=600px;help=0;status=0;scroll=0;center=1";
		url=url.getNewUrl();
		$.openFullWindow(url);
	}
	//添加菜单
	function addToResource(id){
		var url="/oa/form/dataTemplate/preview.do?__displayId__="+id;
		AddResourceDialog({addUrl:url});
	}
	
	/**
	* 选择流程
	*/
	function selectFlow(){
		BpmDefinitionDialog({isSingle:true,callback:function(defIds,subjects){
			$("#defId").val(defIds);
			$("#subject").val(subjects);
		}});
	};
	function cancel(){
		$("#defId").val("");
		$("#subject").val("");
	}

	//批量设置权限。
	function setPermision(){
		var me = $(this),
			val= me.val(),
			right = me.attr('right');
			if(val=="") return;
			$("span[name='r_span'],span[name='w_span'],span[name='b_span']",obj).hide();
			$('td[var="'+right+'"]').each(function(){
				var self = $(this),
					rightSelect =$("[var='right']",self);
				rightSelect.next().hide();
				rightSelect.next().next().hide();
				if(val ==0 ){
					rightSelect.val('none');
				}else if(val==1){
					rightSelect.val('everyone');
				}		
			});
			me.val("");
		}
</script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/DataTemplateEdit.js"></script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">数据模板设置</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
				<c:if test="${!empty bpmDataTemplate.id}">
				<div class="group"><a class="link preview" href="####" onclick="preview(${bpmDataTemplate.id})">预览</a></div>
				<div class="group"><a class="link edit" href="####" onclick="editTemplate(${bpmDataTemplate.id})">编辑模板</a></div>	
				<div class="group"><a class="link collapse" href="####" onclick="addToResource(${bpmDataTemplate.id})">添加为菜单</a></div>		
				</c:if>
				<div class="group"><a class="link close" href="####" onclick="window.close();">关闭</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="dataTemplateForm" method="post" action="save.do" >
	
			<div id="tab">
			<!-- 基本信息  start-->
			<div tabid="baseSetting" id="table" title="基本信息">
				<div >
					<div class="tbar-title">
						<span class="tbar-label">基本信息</span>
					</div>
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main" style="border-width: 0!important;">
						<tr>
							<th  width="10%">绑定流程: </th>
							<td>
								<div class="input-content">
									<input type="hidden" id="defId" name="defId" value="${bpmDataTemplate.defId}"  class="inputText"/>
									<input type="text" id="subject" name="subject" readonly="readonly" value="${bpmDataTemplate.subject}"  class="inputText"  style="width: 60%;" />
									<a onclick="selectFlow()" class="ryxzq-a link ok" href="####"></a>
									<a  onclick="cancel()" class="ryxzq-a link reset" href="####"></a>
								</div>
							</td>
						</tr>
						<tr>
							<th >是否分页: </th>
							<td>
								<input type="radio" name="needPage" value="0"  onclick="__DataTemplate__.switchNeedPage(this);" <c:if test="${bpmDataTemplate.needPage==0}">checked="checked"</c:if> >不分页
								<input type="radio" name="needPage" value="1" onclick="__DataTemplate__.switchNeedPage(this);" <c:if test="${bpmDataTemplate.needPage==1}">checked="checked"</c:if>>分页
								<span style="color:red;<c:if test="${bpmDataTemplate.needPage==0}">display:none;</c:if>" id="spanPageSize" name="spanPageSize">
									分页大小：
									  <select id="pageSize" name="pageSize" >
									  		<option value="5" <c:if test="${bpmDataTemplate.pageSize==5}">selected="selected"</c:if> >5</option>
											<option value="10" <c:if test="${bpmDataTemplate.pageSize==10}">selected="selected"</c:if>>10</option>
											<option value="15" <c:if test="${bpmDataTemplate.pageSize==15}">selected="selected"</c:if> >15</option>
											<option value="20" <c:if test="${bpmDataTemplate.pageSize==20}">selected="selected"</c:if>>20</option>
											<option value="50" <c:if test="${bpmDataTemplate.pageSize==50}">selected="selected"</c:if>>50</option>	
											<option value="100" <c:if test="${bpmDataTemplate.pageSize==100}">selected="selected"</c:if>>100</option>	
											<option value="150" <c:if test="${bpmDataTemplate.pageSize==150}">selected="selected"</c:if>>150</option>	
											<option value="200" <c:if test="${bpmDataTemplate.pageSize==200}">selected="selected"</c:if>>200</option>	
											<option value="500" <c:if test="${bpmDataTemplate.pageSize==500}">selected="selected"</c:if>>500</option>						  
									  </select>
								 </span>
							</td>
						</tr>
						<tr>
							<th>是否初始查询: </th>
							<td>
								<select name="isQuery" id="isQuery"  validate="{required:true}">
									 	<option value="0" <c:if test="${bpmDataTemplate.isQuery==0}">selected="selected"</c:if> >是</option>
										<option value="1" <c:if test="${bpmDataTemplate.isQuery==1}">selected="selected"</c:if>>否</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>没有过滤条件<br/>是否需要默认过滤: </th>
							<td>
								<select name="isFilter" id=isFilter  validate="{required:true}">
									 	<option value="0" <c:if test="${bpmDataTemplate.isFilter==0}">selected="selected"</c:if> >是</option>
										<option value="1" <c:if test="${bpmDataTemplate.isFilter==1}">selected="selected"</c:if>>否</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>是否默认备份历史数据: </th>
							<td>
								<select name="isBakData" id="isBakData"  validate="{required:true}">
									 	<option value="1" <c:if test="${bpmDataTemplate.isBakData==1}">selected="selected"</c:if> >是</option>
										<option value="0" <c:if test="${bpmDataTemplate.isBakData==0}">selected="selected"</c:if>>否</option>
										<option value="2" <c:if test="${bpmDataTemplate.isBakData==2}">selected="selected"</c:if>>用户选择</option>
								
								</select>
							</td>
						</tr>
						<tr>
							<th>数据模板: </th>
							<td>
								<select name="templateAlias" id="templateAlias"  validate="{required:true}">
									<option value="">--请选择数据模板--</option>
										<c:forEach items="${templates}" var="template">
										<option  value="${template.alias}" <c:if test="${bpmDataTemplate.templateAlias==template.alias}">selected="selected"</c:if>>${template.templateName}</option>
									</c:forEach>
								</select>
							<div class="tipbox"><a href="####" class="tipinfo"><span>添加更多数据模板，请到自定义表单模板中添加类型为"业务数据模板"的模板</span></a></div>
						<input type="hidden" id="id" name="id" value="${bpmDataTemplate.id}">
						<input type="hidden" id="tableId" name="tableId" value="${bpmDataTemplate.tableId}">
						<input type="hidden" id="formKey" name="formKey" value="${bpmDataTemplate.formKey}">
						<input type="hidden" name="source" id="source" value="${bpmDataTemplate.source}">
						<textarea style="display: none;" id="displayField" name="displayField" >${fn:escapeXml(bpmDataTemplate.displayField)}</textarea>
						<textarea style="display: none;" id="conditionField" name="conditionField" >${fn:escapeXml(bpmDataTemplate.conditionField)}</textarea>
						<textarea style="display: none;" id="sortField" name="sortField" >${fn:escapeXml(bpmDataTemplate.sortField)}</textarea>
						<textarea style="display: none;" id="manageField" name="manageField" >${fn:escapeXml(bpmDataTemplate.manageField)}</textarea>
						<textarea style="display: none;" id="filterField" name="filterField" >${fn:escapeXml(bpmDataTemplate.filterField)}</textarea>
						<textarea style="display: none;" id="exportField" name="exportField" >${fn:escapeXml(bpmDataTemplate.exportField)}</textarea>
						<textarea style="display: none;" id="subSortField" name="subSortField" >${fn:escapeXml(bpmDataTemplate.subSortField)}</textarea>
						<textarea style="display: none;" id="relSortField" name="relSortField" >${fn:escapeXml(bpmDataTemplate.relSortField)}</textarea>
						</td>
						</tr>
											
					</table>
				</div>
			</div>
		</form>
					<!-- 基本信息  end-->
					<!-- 显示字段  start-->		
					<div tabid="displaySetting" id="table" title="显示列字段">
						<div>
							<table id="displayFieldTbl"  class="table-grid">
								<thead>
									<tr>
										<th width="5%">序号</th>
										<th width="15%">列名</th>
										<th width="10%">注释</th>
										<th width="10%">显示权限<select  class="rightselect" right="fieldRight" ><option selected="selected" value=""></option><option  value="0">无</option><option value="1">所有人</option></select></th>
									    <th >显示类型</th>
									    <th width="10%">click事件</th>
									    <th width="10%">url</th>
									    <th width="5%">固定列
										    <a href="javascript:void(0)"  class="tipinfo">
												<span>
													显示列是否固定；</br>	
													固定列只能位于列表左侧，且固定列之间都是固定列。</br>	
												</span>	
											</a>
									    </th>
									    <th width="10%">管理</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					
					<!-- 显示字段  end-->
					<!-- 查询字段  start-->		
					<div tabid="conditionSetting" id="table" title="查询条件字段">
							<div  class="condition-cols">
							<div>
								<div class="condition-cols-div">
										<c:set var="checkAll">
											<input type="checkbox" id="chkall" />
										</c:set>
										<table id="condition-columnsTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
											<thead>
												<tr class="leftHeader">
													<th style="width:30px;" >${checkAll}</th>
													<th>列名</th>
													<th>注释</th>
													<th>类型</th>
												</tr>
											</thead>
											<tbody>
												<c:if test="${!empty bpmFormTable}">
													<c:forEach items="${bpmFormTable.fieldList}" var="field" varStatus="status">	
														<tr  <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="even"</c:if>>
															<td>
																<input class="pk" type="checkbox" name="select" fieldname="${field.fieldName}" fieldfmt='${field.ctlProperty}' fieldtype="${field.fieldType}" fielddesc="${field.fieldDesc}"  controltype="${field.controlType}">
															</td>
															<td>${field.fieldName}</td>
															<td>${field.fieldDesc}</td>
															<td>${field.fieldType}</td>
														</tr>
													</c:forEach>
												</c:if>
											</tbody>
										</table>
									</div>
								</div>
								</div>
								<div class="condition-conds">
									<div class="condition-conds-div condition-conds-build" id="condition-build-div">
										<div class="condition-conds-div-left">
											<div class="condition-conds-div-left-div">
												<a style="margin-top: 180px;" id="selectCondition" href="####" class="button">
													<span>==></span>
												</a>
											</div>
										</div>
										<div class="condition-conds-div-right">
											<div class="condition-conds-div-right-div">
												<table id="conditionTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
													<thead>
														<tr class="leftHeader">
															<th  nowrap="nowrap">列名</th>
															<th  nowrap="nowrap">显示名</th>
															<th  nowrap="nowrap" style="width: 120px;">控件类型</th>
															<th  nowrap="nowrap">条件</th>
															<th  nowrap="nowrap">值来源</th>
															<th  nowrap="nowrap">值</th>
															<th  nowrap="nowrap">管理</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
											</div>
										</div>
								</div>
							</div>
					</div>
					<!-- 查询字段  end-->	
					<!-- 排序字段 start-->		
					<div tabid="sortSetting" id="table" title="排序字段">
							<div class="sort-cols">
									<div class="sort-cols-div">
										<table id="sort-columnsTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
											<thead>
												<tr class="leftHeader">
													<th>选择</th>
													<th>列名</th>
													<th>注释</th>
												</tr>
											</thead>
											<tbody>
												<c:if test="${!empty bpmFormTable}">
													<c:forEach items="${bpmFormTable.fieldList}" var="field" varStatus="status">	
														<tr  <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="odd"</c:if>>
															<td>
																<input class="pk" type="checkbox" name="select" fieldname="${field.fieldName}" fieldtype="${field.fieldType}" fielddesc="${field.fieldDesc}">
															</td>
															<td>${field.fieldName}</td>
															<td>${field.fieldDesc}</td>
														</tr>
													</c:forEach>
												</c:if>
											</tbody>
										</table>
									</div>
								</div>
								<div class="sort-conds">
									<div class="sort-conds-div sort-conds-build" id="sort-build-div">
										<div class="sort-conds-div-left">
											<div class="sort-conds-div-left-div">
												<a style="margin-top: 180px;" id="selectSort" href="####" class="button">
													<span>==></span>
												</a>
											</div>
										</div>
										<div class="sort-conds-div-right">
											<div class="sort-conds-div-right-div">
												<table id="sortTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
													<thead>
														<tr class="leftHeader">
															<th  nowrap="nowrap">列名</th>
															<th  nowrap="nowrap">注释</th>
															<th  nowrap="nowrap">排序</th>
															<th  nowrap="nowrap">管理</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
											</div>
										</div>
								</div>
							</div>
							
					</div>
					<!-- 排序字段 end-->
					
					<%--字表排序字段 start --%>
					<c:if test="${fn:length(bpmFormTable.subTableList)>0}">
					<div tabid="subTabSortSetting" id="subTabSortSetting" title="子表排序字段">
					<c:forEach var="subTab" items="${bpmFormTable.subTableList}">
						<div class="sort-cols">
							<div class="sort-cols-div">
								<table id="sub-sort-columnsTbl-${subTab.tableId}" cellpadding="0" cellspacing="0" border="0" class="table-detail">
									<thead>
										<tr class="leftHeader">
											<th>选择</th>
											<th>列名</th>
											<th>注释</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${!empty subTab}">
											<c:forEach items="${subTab.fieldList}" var="field" varStatus="status">	
												<tr  <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="odd"</c:if>>
													<td>
														<input class="pk" type="checkbox" name="select" fieldname="${field.fieldName}" fieldtype="${field.fieldType}" fielddesc="${field.fieldDesc}">
													</td>
													<td>${field.fieldName}</td>
													<td>${field.fieldDesc}</td>
												</tr>
											</c:forEach>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
						<div class="sort-conds">
							<div class="sort-conds-div sort-conds-build" id="sort-build-div">
								<div class="sort-conds-div-left">
									<div class="sort-conds-div-left-div">
										<a style="margin-top: 180px;" id="selectSubSort-${subTab.tableId}" href="javascript:;" class="button">
											<span>==></span>
										</a>
									</div>
								</div>
								<div class="sort-conds-div-right">
									<div class="sort-conds-div-right-div">
										<table id="subSortTbl-${subTab.tableId}" cellpadding="0" cellspacing="0" border="0" class="table-detail">
											<thead>
												<tr class="leftHeader">
													<th  nowrap="nowrap">列名</th>
													<th  nowrap="nowrap">注释</th>
													<th  nowrap="nowrap">排序</th>
													<th  nowrap="nowrap">管理</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>					
					</c:forEach>

					</div>
					</c:if>
					<%--子表排序字段end --%>
					
					
					<%-- 关联表字段排序 --%>
					<c:if test="${fn:length(bpmFormTable.relTableList)>0}">
					<div tabid="relTabSortSetting" id="relTabSortSetting" title="关联表排序字段">
					<c:forEach var="relTab" items="${bpmFormTable.relTableList}">
						<div class="sort-cols">
							<div class="sort-cols-div">
								<table id="rel-sort-columnsTbl-${relTab.tableId}" cellpadding="0" cellspacing="0" border="0" class="table-detail">
									<thead>
										<tr class="leftHeader">
											<th>选择</th>
											<th>列名</th>
											<th>注释</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${!empty relTab}">
											<c:forEach items="${relTab.fieldList}" var="field" varStatus="status">	
												<tr  <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="odd"</c:if>>
													<td>
														<input class="pk" type="checkbox" name="select" fieldname="${field.fieldName}" fieldtype="${field.fieldType}" fielddesc="${field.fieldDesc}">
													</td>
													<td>${field.fieldName}</td>
													<td>${field.fieldDesc}</td>
												</tr>
											</c:forEach>
										</c:if>
									</tbody>
								</table>
							</div>
						</div>
						<div class="sort-conds">
							<div class="sort-conds-div sort-conds-build" id="sort-build-div">
								<div class="sort-conds-div-left">
									<div class="sort-conds-div-left-div">
										<a style="margin-top: 180px;" id="selectRelSort-${relTab.tableId}" href="javascript:;" class="button">
											<span>==></span>
										</a>
									</div>
								</div>
								<div class="sort-conds-div-right">
									<div class="sort-conds-div-right-div">
										<table id="relSortTbl-${relTab.tableId}" cellpadding="0" cellspacing="0" border="0" class="table-detail">
											<thead>
												<tr class="leftHeader">
													<th  nowrap="nowrap">列名</th>
													<th  nowrap="nowrap">注释</th>
													<th  nowrap="nowrap">排序</th>
													<th  nowrap="nowrap">管理</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>					
					</c:forEach>

					</div>					
					
					</c:if>
					<%-- 关联表字段排序 end --%>
					
					
					<!-- 过滤条件 start	-->
					<div tabid="filterSetting" id="table" title="过滤条件">
						<div class="table-top-left">				
							<div class="toolBar" style="margin:0;">
								<div class="group"><a class="link add" id="btnSearch" onclick="__DataTemplate__.addFilter()">添加</a></div>
							<!--	
								<div class="group"><a class="link edit  " onclick="filterDialog()">修改</a></div>
							-->	
								<div class="group"><a class="link del " id="btnSearch" onclick="__DataTemplate__.delFilter();">删除</a></div>
							</div>								
					    </div>
						<table id="filterTbl"  class="table-grid">
								<thead>
									<tr>
										<th width="5%">选择</th>
										<th width="10%">名称</th>
										<th width="10%">Key</th>
										<th width="10%">类型</th>
										<th>权限<select  class="rightselect" right="filterRight" ><option selected="selected" value=""></option><option  value="0">无</option><option value="1">所有人</option></select></th>
										<th width="10%">管理</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
						</table>
					</div>
					<!-- 过滤条件 end-->
					<!-- 导出字段  start-->		
					<div tabid="exportSetting" id="table" title="导出字段">
						<div>
							<table id="exportFieldTbl"  class="table-grid">
								<thead>
									<tr>
										<th width="5%">序号</th>
										<th width="15%">列名</th>
										<th width="20%">注释</th>
										<th width="20%">导出权限<select  class="rightselect" right="exportRight" ><option selected="selected" value=""></option><option  value="0">无</option><option value="1">所有人</option></select></th>
									    <th width="10%">管理</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<!-- 导出字段  end-->			
					<!-- 功能按钮设置 start-->		
					<div tabid="manageSetting" id="table" title="功能按钮">
						<div class="table-top-left">				
							<div class="toolBar" style="margin:0;">
								<div class="group"><a class="link add" id="btnSearch" onclick="__DataTemplate__.addManage()">添加</a></div>
							<!--	
								<div class="group"><a class="link edit  " onclick="filterDialog()">修改</a></div>
							-->	
								<div class="group"><a class="link del " id="btnSearch" onclick="__DataTemplate__.delManage();">删除</a></div>
							</div>								
					    </div>
						<table id="manageTbl"  class="table-grid">
							<thead>
								<tr>
									<th width="5%">选择</th>
									<th width="5%">按钮ID</th>
									<th width="10%">名称</th>
									<th width="10%">类型</th>
									<th>权限<select  class="rightselect" right="manageRight" ><option selected="selected" value=""></option><option  value="0">无</option><option value="1">所有人</option></select></th>
									<th width="20%">参数脚本</th>
									<th width="20%">前置脚本</th>
									<th width="20%">后置脚本</th>
									<th width="10%">管理</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!-- 管理列设置 end-->	
				</div>
	</div><!-- end of panel-body -->	
</div>
<div class="hidden">
		<!-- 显示字段模板 -->
		<div  id="displayFieldTemplate" >
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="displayFieldTr">
					<input type="hidden"  var="type"  value= "" />
					<input type="hidden"  var="style"  value=""  />
					<input type="hidden"  var="controltype"  value=""/>
					<td var="index">&nbsp;</td>
					<td var="name">&nbsp;</td>
					<td ><input type="text"  var="desc" class="plat-input"  value= "" /></td>
					<td var="fieldRight">
					</td>
					<td>&nbsp;
						<select class="plat-select-type" var="displayType" onchange="__DataTemplate__.displayType(this)">
							<option value="simple">普通</option>
							<option value="hyperlink">超链接</option>
							<option value="processWatch">流程监控</option>
							<option value="detail">详细</option>
						</select>
					</td>
					<td>
						<input type="text" class="plat-input"  var="onclick"  value= "" />
					</td>
					<td>
						<input type="text" class="plat-input"  var="action"  value= "" />
						<input style="display: none;"
							id="setUrlParams" var="setUrlParams" />
						<a class="link edit" href="####"
							onclick="__DataTemplate__.addOrEditUrlParams(this)"></a>
					</td>
					<td>
						<select class="plat-select-isfalse" var="isFix" onchange="__DataTemplate__.fixMove(this)">
							<option value="false">否</option>
							<option value="true">是</option>
						</select>
					</td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__DataTemplate__.moveTr(this,true)"></a>
						<a class="link movedown" href="####"  title="下移" onclick="__DataTemplate__.moveTr(this,false)"></a> 
					</td>
				</tr>
			</table>
		</div>
			<!-- 导出字段模板 -->
		<div  id="exportFieldTemplate" >
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="exportFieldTr">
					<input type="hidden"  var="type"  value=  ""/>
					<input type="hidden"  var="style"  value=  ""/>
					<input type="hidden"  var=tablename  value= "" />
					<input type="hidden"  var=ismain  value= "" />
					<td var="index">&nbsp;</td>
					<td var="name">&nbsp;</td>
					<td ><input class="plat-input" type="text"  var="desc"  value= "" /></td>
					<td var="exportRight">
					</td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__DataTemplate__.moveTr(this,true)"></a>
						<a class="link movedown" href="####"  title="下移" onclick="__DataTemplate__.moveTr(this,false)"></a> 
					</td>
				</tr>
			</table>
			<table cellpadding="1" cellspacing="1"  class="table-list">
				<tr var="exportTableTr">
					<input type="hidden"  var="tablename"  value= "" />
					<input type="hidden"  var="tabledesc"  value=  ""/>
					<input type="hidden"  var="ismain"  value= "" />
					<td var="table" colspan="6"></td>
				</tr>
			</table>
		</div>
		
		<!-- 排序模板 -->
		<div  id="sortTemplate"  style="display: none;">
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="sortTr">
					<input type="hidden"  var="source"  value="${bpmDataTemplate.source}"/>
					<td var="name">&nbsp;</td>
					<td var="desc">&nbsp;</td>
					<td ><select class="plat-select-type" var="sort" >
							<option value="ASC">升序</option>
							<option value="DESC">降序</option>
						</select></td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__DataTemplate__.moveTr(this,true)"></a>
						<a class="link movedown" href="####"  title="下移" onclick="__DataTemplate__.moveTr(this,false)"></a>
						<a class="link del" href="####" title="删除" onclick="__DataTemplate__.delTr(this)"></a>
					</td>
				</tr>
			</table>
		</div>
		<!-- 过滤条件模板 -->
		<div  id="filterTemplate"  style="display: none;">
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="filterTr">
					<td var="index"><input class="pk" type="checkbox" name="select"/>
					<input  type="hidden" var="type">
					<textarea style="display: none;" var="condition" ></textarea></td>
					<td var="name"></td>
					<td var="key"></td>
					<td var="typeshow"></td>
					<td var="filterRight">
					</td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__DataTemplate__.moveTr(this,true)"></a>
						<a class="link movedown" href="####" title="下移"  onclick="__DataTemplate__.moveTr(this,false)"></a>
						<a class="link edit" href="####"  title="编辑"  onclick="__DataTemplate__.editFilter(this)"></a>
						<a class="link del" href="####"  title="删除" onclick="__DataTemplate__.delTr(this)"></a>
					</td>
				</tr>
			</table>
		</div>
		
		
		<!-- 功能按钮模板 -->
		<div  id="manageTemplate"  style="display: none;">
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="manageTr">
					<td var="index"><input class="pk plat-input" type="checkbox" name="select"></td>
					<td var="index"><input type="text" class="plat-input" readOnly="readOnly" var = "unique" value="" /></td>
					<td >
						<input class="plat-input" type="text"  var="desc"  value="新增" >
					</td>
					<td>&nbsp;
						<select class="plat-select-type" var="name" >
							<option value="add">新增</option>
							<option value="edit">编辑</option>
							<option value="del">删除</option>
							<option value="detail">明细</option>
							<option value="attach">附件</option>
							<option value="process">流程监控</option>
							<option value="export">导出</option>
							<option value="exportAll">导出全部数据</option>
							<option value="exportSelect">导出选中数据</option>
							<option value="exportPage">导出当页数据</option>
							<option value="expprint">导出报表</option>
							<option value="import">导入</option>
							<option value="print">打印</option>
							<option value="start">启动流程</option>
							<option value="start1">启动1</option>
							<option value="start2">启动2</option>
							<option value="start3">启动3</option>
							<option value="reportData">上报</option>
							<option value="accept">采纳</option>
							<option value="decline">拒收</option>
							<option value="feedBack">反馈</option>
							<option value="approvals">审批通过</option>
							<option value="complexDetail">复杂表单明细</option>
							<option value="forceDelete">强制删除</option>
							<option value="importModuleTeam">excel导入型号团队</option>
							<option value="uploadFileInstance">上传实例文件</option>
						</select>
					</td>
					<td var="manageRight">
					</td>
					<td var="paramscript">
						<textarea class="plat-textarea" rows="4" cols="20" var="paramscript"></textarea>
					</td>
					<td var="prescript">
						<textarea class="plat-textarea" rows="4" cols="20" var="prescript"></textarea>
					</td>
					<td var="afterscript">
						<textarea class="plat-textarea" rows="4" cols="20" var="afterscript"></textarea>
					</td>
					<td>
						<a class="link moveup" href="####" onclick="__DataTemplate__.moveTr(this,true)"></a>
						<a class="link movedown" href="####" onclick="__DataTemplate__.moveTr(this,false)"></a>
						<a class="link del" href="####" onclick="__DataTemplate__.delTr(this)"></a>
					</td>
				</tr>
			</table>
			
			<div class="tipbox print paramscript">
				<a href="javascript:;" class="tipinfo " style="position:absolute"> 
					<span style="width:300px">简单例子：
						<p>参数类型为Object。param和report,param属性可任意设置，report必须按照要求设置</p>
						<p>var conf=new Object();</p>
						<p>conf.report.title='报表模板标题';</p>
						<p>如果需要下载报表，请设置ext属性，如果是在线浏览则不需要设置</p>
						<p>conf.report.ext='pdf|word|excel|text|svg|csv|image';</p>
						<p>如果ext是excel 则 extype='null|simple|sheet' null表示分页导出;simple原样导出;sheet分页分sheet导出;</p>
						<p>如果ext是image 则extype='PNG|JPG|BMP|GIT';</p>
						<p>conf.report.extype='simple|...';</p>
						<p>param可以根据报表模板任意设置值</p>
						<p>conf.pram.id='参数id值';</p>
						<p>conf.pram.name='参数name值';</p>
						<p>conf.pram.key='参数key值';</p>
						<p>conf.rpcrefname='远程接口bean的ID';</p>
						<p>return conf;</p>
					</span>
				</a>
			</div>
			<div class="tipbox attach paramscript">
				<a href="javascript:;" class="tipinfo " style="position:absolute"> 
					<span style="width:300px">简单例子：
						<p>参数对象为obj</p>
						<p>var obj=new Object();</p>
						<p>obj.fileFolder=1或0 是否需要显示文件夹1表示显示0表示不显示(现已失效，通过表单权限控制)</p>
						<p>obj.nodekey=文件分类树节点名 数据从CWM_SYS_GLTYPE.NODEKEY中获取</p>
						<p>obj.dimension=是否启用维度显示（0：否，1：是）</p>
						<p>obj.dimensionKey=默认加载的维度；eg：obj.dimensionKey="wjlx";</p>
						<p>obj.maindata=1或0 1表示显示该条记录主表所有附件，默认0表示只显示该条记录所挂载的附件</p>
						<p>obj.mainfield="fjsc";  (fjsc为该记录中附件上传的字段)</p>
						<p>obj.reldata="qzglb:glqzb";(qzglb表示关联表,glqzb关联表字段，如果想关联多表加@，如qzglb:glqzb@qzglb1:glqzb1)</p>
						<p>obj.reldatafield="qzglb:glqzb:fjzd";(该属性多加了fjzd,即附加了关联表的关联记录的附件上传字段的附件。添加多个多表也是加@)</p>
						<p>return obj;(最后切记加上这步)</p>
					</span>
				</a>
			</div>
			<div class="tipbox start1 paramscript">
				<a href="javascript:;" class="tipinfo " style="position:absolute"> 
					<span style="width:300px">简单例子：
						<p>参数对象为obj</p>
						<p>var obj=new Object();</p>
						<p>obj.flowKey="xxxx" 填写监控流程的别名--flowKey</p>
						<p>return obj;(最后切记加上这步)</p>
					</span>
				</a>
			</div>
			<div class="tipbox start2 paramscript">
				<a href="javascript:;" class="tipinfo " style="position:absolute"> 
					<span style="width:300px">简单例子：
						<p>参数对象为obj</p>
						<p>var obj=new Object();</p>
						<p>obj.flowKey="xxxx" 填写监控流程的别名--flowKey</p>
						<p>return obj;(最后切记加上这步)</p>
					</span>
				</a>
			</div>
			<div class="tipbox start3 paramscript">
				<a href="javascript:;" class="tipinfo " style="position:absolute"> 
					<span style="width:300px">简单例子：
						<p>参数对象为obj</p>
						<p>var obj=new Object();</p>
						<p>obj.flowKey="xxxx" 填写监控流程的别名--flowKey</p>
						<p>return obj;(最后切记加上这步)</p>
					</span>
				</a>
			</div>
		</div>
</div>

</body>
</html>