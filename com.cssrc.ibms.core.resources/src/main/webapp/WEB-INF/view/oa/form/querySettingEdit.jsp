<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>查询数据模板设置</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/lang/view/oa/form/zh_CN.js"></script>
<f:link href="fromJsp/querySettingEdit.css" ></f:link>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.fix.clone.js"></script>
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
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysQuerySetting.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>

<script type="text/javascript">
	var tab = "";
	
	$(function(){
		 tab =$("#tab").ligerTab({
			 contextmenu:false
		});	
		// 初始化
		__SysQuerySetting__.init();
		var options={};
		if(showResponse){
			options.success=showResponse;
		}	 
		//绑定保存事件
		$("a.save").click(function() {
			var form=$('#sysQuerySetting');
			if(!form.valid())
				return;	
			if(saveChange(form))
				customFormSubmit(form,options);	
		}); 
		// 批量授权
		$("select.rightselect").change(setPermision);
	});
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
	//显示字段
	function getDisplayField(){
		var json = [];
		$("#displayFieldTbl tr[var='displayFieldTr']").each(function(){
			var me = $(this),obj={},rightJson = [];
			obj.name =$("[var='name']",me).html();
			obj.desc =$("[var='desc']",me).val();
			obj.type =$("[var='type']",me).val();
			obj.style =$("[var='style']",me).val();
			obj.id=$("[var='id']",me).val();
			rightJson.push(getRight($("[var='fieldRight']",me),0 ));
			obj.right = rightJson;
			json.push(obj);
		});
		return json;
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
	// 导出字段
	function getExportField(){
		var json = [];
		$("#exportFieldTbl tr[var='exportFieldTr']").each(function(){
			var me = $(this),obj={},rightJson = [];
			obj.name =$("[var='name']",me).html();
			obj.desc =$("[var='desc']",me).val();
			obj.type =$("[var='type']",me).val();
			obj.style =$("[var='style']",me).val();
			obj.id=$("[var='id']",me).val();
			rightJson.push(getRight($("[var='exportRight']",me),2 ));
			obj.right = rightJson;
			json.push(obj);
		});
		return json;
	}	
	// 查询条件字段
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
			var qt =__SysQuerySetting__.getQueryType(field.ty,op);
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
			fields.push(obj);
		});
		return fields;
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
			obj.name =$("[var='name']",me).val();
			obj.desc =$("[var='desc']",me).html();
			obj.className =$("[var='className']",me).html();
			obj.clickName =$("[var='clickName']",me).html();
			obj.urlPath =$("[var='urlPath']",me).html();
			obj.urlParams =$.parseJSON($("[var='urlParams']",me).val());
			obj.paramscript =$("textarea[var='paramscript']",me).val();
			obj.prescript =$("textarea[var='prescript']",me).val();
			obj.afterscript =$("textarea[var='afterscript']",me).val();
			rightJson.push(getRight($("[var='manageRight']",me),4));
			obj.right = rightJson;
			fields.push(obj);
		});
		return fields;
	} 
	// 设置改变
	function saveChange(form){
		//判断排序字段太多报错问题
		if(sortFieldValid()){
			$.ligerDialog.error("排序字段不能设置超过3个，请检查！","提示信息");
			tab.selectTabItem("sortSetting");
			return false;
		}
		var needPage=$("input[name='needPage']:checked").val();
		var templateAlias=$("#templateAlias").val();
		if(templateAlias=="" || needPage ==""){
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
		//过滤字段字段
		var filterField= JSON2.stringify(getFilterField());
		//管理字段
		var manageField= JSON2.stringify(getManageField());
		//导出字段
		var exportField= JSON2.stringify(getExportField());
		$('#displayField').val(displayField);
		$('#conditionField').val(conditionField);
		$('#sortField').val(sortField);
		$('#filterField').val(filterField);
		$('#exportField').val(exportField); 
		$('#manageField').val(manageField); 
		return true;
	} 
	
	/**
	 * 自定义外部表单并提交
	 * @return void
	 */
	function customFormSubmit(form,options){
		var id=$("#id").val();
		var sqlId=$("#sqlId").val();
		var name=$("#name").val();
		var alias=$("#alias").val();
		var isQuery=$("#isQuery").val();
		
		var needPage=$("input[name='needPage']:checked").val();
		var pageSize=$("#pageSize").val();
		//模板别名
		var templateAlias=$("#templateAlias").val();
		//显示字段
		var displayField= $('#displayField').val();
		//条件字段
		var conditionField= $('#conditionField').val();
		//排序字段
		var sortField= $('#sortField').val();
		//过滤字段字段
		var filterField= $('#filterField').val();
		//导出字段
		var exportField = $('#exportField').val();
		//管理字段
		var manageField= $('#manageField').val();
		var sysQuerySettingJson={
			id:id,
			sqlId:sqlId,
			name:name,
			alias:alias,	
			isQuery:isQuery,
			needPage:needPage,
			pageSize:pageSize,
			templateAlias:templateAlias,
			displayField:displayField,
			conditionField:conditionField,
			sortField:sortField,
			filterField:filterField,
			exportField:exportField,
			manageField:manageField
		};
		var form = $('<form method="post" action="save.do"></form>');
		var input1 = $("<input type='hidden' name='sysQuerySettingJson'/>");
		var jsonStr=JSON2.stringify(sysQuerySettingJson);
		input1.val(jsonStr);
		form.append(input1);
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
			$.ligerDialog.err(obj.getMessage(),"提示信息");
		}
	} 
	//预览
	function preview(id){
		if($.isEmpty(id)){
			$.ligerDialog.error("请设置完信息保存后预览!","提示信息");
			return ;
		}
		var url=__ctx+ "/oa/form/querySetting/preview.do?__displayId__="+id;
		url=url.getNewUrl();
		$.openFullWindow(url);
	} 
	//编辑模板
	 function editTemplate(id){
		if($.isEmpty(id)){
			$.ligerDialog.error("请设置完信息保存后编辑模板!","提示信息");
			return ;
		}
		var url=__ctx+ "/oa/form/querySetting/editTemplate.do?id="+id;
		url=url.getNewUrl();
		$.openFullWindow(url);
	} 
	//添加菜单
	function addToResource(id){
		var url="/oa/form/querySetting/preview.do?__displayId__="+id;
		AddResourceDialog({addUrl:url});
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
</head>
<body>
<div class="panel">
<div class="hide-panel">
	<div class="panel-top">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">查询数据模板设置</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
					<c:if test="${!empty sysQuerySetting.id}">
						
						<div class="group"><a class="link preview" href="####" onclick="preview('${sysQuerySetting.id}')">预览</a></div>
						
						<div class="group"><a class="link edit" href="####" onclick="editTemplate('${sysQuerySetting.id}')">编辑模板</a></div>	
						
						<div class="group"><a class="link collapse" href="####" onclick="addToResource('${sysQuerySetting.id}')">添加为菜单</a></div>	
					</c:if>
					
					<div class="group"><a class="link close" href="####" onclick="window.close();">关闭</a></div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<div class="panel-body">
		<form id="sysQuerySetting" method="post" action="save.do" >
				<div id="tab">
				<!-- 基本信息  start-->
				<div tabid="baseSetting" id="table" title="基本信息">
					<div >
							<div class="tbar-title">
								<span class="tbar-label">基本信息</span>
							</div>
							<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main" style="border-width: 0!important;">
								<tr>
									<th  width="10%">名称: </th>
									<td>
										<input type="text" id="name" name="name" value="${sysQuerySetting.name}" validate="{required:true}"  class="inputText" />
									</td>
								</tr> 
								<tr>
									<th  width="10%">别名: </th>
									<td>
										<input type="text" id="alias" name="alias" value="${sysQuerySetting.alias}" class="inputText" />
									</td>
								</tr> 
								<tr>
									<th >是否分页: </th>
									<td>
										<input type="radio" name="needPage" value="0"  onclick="__SysQuerySetting__.switchNeedPage(this);" <c:if test="${sysQuerySetting.needPage==0}">checked="checked"</c:if> >不分页
										<input type="radio" name="needPage" value="1" onclick="__SysQuerySetting__.switchNeedPage(this);" <c:if test="${sysQuerySetting.needPage==1}">checked="checked"</c:if>>分页
										<span style="color:red;<c:if test="${sysQuerySetting.needPage==0}">display:none;</c:if>" id="spanPageSize" name="spanPageSize">
											分页大小：
											  <select id="pageSize" name="pageSize" >
											  		<option value="5" <c:if test="${sysQuerySetting.pageSize==5}">selected="selected"</c:if> >5</option>
													<option value="10" <c:if test="${sysQuerySetting.pageSize==10}">selected="selected"</c:if>>10</option>
													<option value="15" <c:if test="${sysQuerySetting.pageSize==15}">selected="selected"</c:if> >15</option>
													<option value="20" <c:if test="${sysQuerySetting.pageSize==20}">selected="selected"</c:if>>20</option>
													<option value="50" <c:if test="${sysQuerySetting.pageSize==50}">selected="selected"</c:if>>50</option>
													<option value="100" <c:if test="${sysQuerySetting.pageSize==100}">selected="selected"</c:if>>100</option>
													<option value="150" <c:if test="${sysQuerySetting.pageSize==150}">selected="selected"</c:if>>150</option>
													<option value="200" <c:if test="${sysQuerySetting.pageSize==200}">selected="selected"</c:if>>200</option>
													<option value="500" <c:if test="${sysQuerySetting.pageSize==500}">selected="selected"</c:if>>500</option>						  
											  </select>
										 </span>
									</td>
								</tr>
								<tr>
									<th>是否初始查询: </th>
									<td>
										<select name="isQuery" id="isQuery"  validate="{required:true}">
											 	<option value="0" <c:if test="${sysQuerySetting.isQuery==0}">selected="selected"</c:if> >是</option>
												<option value="1" <c:if test="${sysQuerySetting.isQuery==1}">selected="selected"</c:if>>否</option>
										</select>
									</td>
								</tr>
								<tr>
									<th>数据模板: </th>
									<td>
										<select name="templateAlias" id="templateAlias"  validate="{required:true}">
											<option value="">--请选择数据模板--</option>
												<c:forEach items="${templates}" var="template">
												<option  value="${template.alias}" <c:if test="${sysQuerySetting.templateAlias==template.alias}">selected="selected"</c:if>>${template.templateName}</option>
											</c:forEach>
										</select>
										<div class="tipbox"><a href="####" class="tipinfo"><span>添加更多数据模板，请到自定义表单模板中添加类型为"业务数据多表查询模板"的模板</span></a></div>
										<input type="hidden" id="id" name="id" value="${sysQuerySetting.id}">
										<input type="hidden" id="sqlId" name="sqlId" value="${param.sqlId}">
										<textarea style="display: none;" id="displayField" name="displayField" >${fn:escapeXml(sysQuerySetting.displayField)}</textarea>
										<textarea style="display: none;" id="conditionField" name="conditionField" >${fn:escapeXml(sysQuerySetting.conditionField)}</textarea>
										<textarea style="display: none;" id="filterField" name="filterField" >${fn:escapeXml(sysQuerySetting.filterField)}</textarea>
										<textarea style="display: none;" id="sortField" name="sortField" >${fn:escapeXml(sysQuerySetting.sortField)}</textarea>
										<textarea style="display: none;" id="exportField" name="exportField" >${fn:escapeXml(sysQuerySetting.exportField)}</textarea> 
										<textarea style="display: none;" id="manageField" name="manageField" >${fn:escapeXml(sysQuerySetting.manageField)}</textarea>
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
										<th width="20%">注释</th>
										<th width="20%">显示权限<select  class="rightselect" right="fieldRight" ><option selected="selected" value=""></option><option  value="0">无</option><option value="1">所有人</option></select></th>
										<th width="10%">管理</th>
									</tr>
								</thead>
							</table>
						</div>
					</div> 
					<!-- 显示字段  end-->
					<!-- 查询字段  start-->		
					<div tabid="conditionSetting" id="table" title="查询条件字段">
							<div  class="condition-cols" style ="display:none">
							<div>
								<div class="condition-cols-div">
										<c:set var="checkAll">
											<input type="checkbox" id="chkall" />
										</c:set>
										<table id="condition-columnsTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
											<thead>
												<tr class="leftHeader">
													<th style="width:30px;" ></th>
													<th>列名</th>
													<th>注释</th>
													<th>类型</th>
												</tr>
											</thead>
											<tbody>
												<c:if test="${!empty sysQueryFields}">
													<c:forEach items="${sysQueryFields}" var="field" varStatus="status">	
														<tr  <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="odd"</c:if>>
															<td>
																<input class="pk" type="checkbox" name="select" fieldId ="${field.id}" fieldname="${field.name}" fieldfmt='${field.format}' fieldtype="${field.type}" fielddesc="${field.fieldDesc}">
															</td>
															<td>${field.name}</td>
															<td>${field.fieldDesc}</td>
															<td>${field.type}</td>
														</tr>
													</c:forEach>
												</c:if>
											</tbody>
										</table>
									</div>
								</div>
								</div>
								<div >
									<div>
										<div class="condition-conds-div-left" style ="display:none">
											<div class="condition-conds-div-left-div">
												<a style="margin-top: 180px;" id="selectCondition" href="####" class="button">
													<span>==></span>
												</a>
											</div>
										</div>
										<div >
											<div class="">
												<table id="conditionTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
													<thead>
														<tr class="leftHeader">
															<th  nowrap="nowrap">列名</th>
															<th  nowrap="nowrap">显示名</th>
															<th  nowrap="nowrap">控件类型</th>
															<th  nowrap="nowrap">条件</th>
															<th  nowrap="nowrap">值来源</th>
															<th  nowrap="nowrap">值</th>
															<th  width ="15%" nowrap="nowrap">管理</th>
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
												<c:if test="${!empty sysQueryFields}">
													<c:forEach items="${sysQueryFields}" var="field" varStatus="status">	
														<tr  <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="odd"</c:if>>
															<td>
																<input class="pk" type="checkbox" name="select" fieldname="${field.name}" fieldtype="${field.type}" fielddesc="${field.fieldDesc}">
															</td>
															<td>${field.name}</td>
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
					<!-- 过滤条件 start	-->
					<div tabid="filterSetting" id="table" title="过滤条件">
						<div class="table-top-left">				
							<div class="toolBar" style="margin:0;">
								<div class="group"><a class="link add" id="btnSearch" onclick="__SysQuerySetting__.addFilter()">添加</a></div>
								
								<div class="group"><a class="link edit  " onclick="filterDialog()">修改</a></div>
								
								<div class="group"><a class="link del " id="btnSearch" onclick="__SysQuerySetting__.delFilter();">删除</a></div>
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
						<div>
							<table id="manageTbl"  class="table-grid">
								<thead>
									<tr>
										<th width="2%">序号</th>
										<th width="10%">显示名称</th>
										<th width="10%">class名称</th>
										<th width="10%">click函数名称</th>
										<th width="10%">url路径</th>
										<th>导出权限<select  class="rightselect" right="manageRight" ><option selected="selected" value=""></option><option  value="0">无</option><option value="1">所有人</option></select></th>
										<th width="20%">参数脚本</th>
										<th width="20%">前置脚本</th>
										<th >后置脚本</th>
										<th width="10%">管理</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
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
					<input type="hidden"  var="id"  value=""  />
					<input type="hidden"  var="type"  value=""  />
					<input type="hidden"  var="style"  value=""  />
					<td var="index">&nbsp;</td>
					<td var="name">&nbsp;</td>
					<td ><input type="text" class="plat-input"  var="desc"  value=""  /></td>
					<td var="fieldRight">
					</td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__SysQuerySetting__.moveTr(this,true)"></a>
						<a class="link movedown" href="####"  title="下移" onclick="__SysQuerySetting__.moveTr(this,false)"></a> 
					</td>
				</tr>
			</table>
		</div>
			<!-- 导出字段模板 -->
		<div  id="exportFieldTemplate" >
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="exportFieldTr">
					<input type="hidden"  var="id"  value=""  />
					<input type="hidden"  var="type"  value=""  />
					<input type="hidden"  var="style"  value=""  /> 
					<td var="index">&nbsp;</td>
					<td var="name">&nbsp;</td>
					<td ><input type="text" class="plat-input"  var="desc"  value=""  /></td>
					<td var="exportRight">
					</td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__SysQuerySetting__.moveTr(this,true)"></a>
						<a class="link movedown" href="####"  title="下移" onclick="__SysQuerySetting__.moveTr(this,false)"></a> 
					</td>
				</tr>
			</table>
		</div>	
		<!-- 排序模板 -->
		<div  id="sortTemplate"  style="display: none;">
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="sortTr">
					<td var="name">&nbsp;</td>
					<td var="desc">&nbsp;</td>
					<td ><select var="sort" >
							<option value="ASC">升序</option>
							<option value="DESC">降序</option>
						</select></td>
					<td>
						<a class="link moveup" href="####" title="上移" onclick="__SysQuerySetting__.moveTr(this,true)"></a>
						<a class="link movedown" href="####"  title="下移" onclick="__SysQuerySetting__.moveTr(this,false)"></a>
						<a class="link del" href="####" title="删除" onclick="__SysQuerySetting__.delTr(this)"></a>
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
						<a class="link moveup" href="####" title="上移" onclick="__SysQuerySetting__.moveTr(this,true)"></a>
						<a class="link movedown" href="####" title="下移"  onclick="__SysQuerySetting__.moveTr(this,false)"></a>
						<a class="link edit" href="####"  title="编辑"  onclick="__SysQuerySetting__.editFilter(this)"></a>
						<a class="link del" href="####"  title="删除" onclick="__SysQuerySetting__.delTr(this)"></a>
					</td>
				</tr>
			</table>
		</div>
		<!-- 功能按钮模板 -->
		<div  id="manageTemplate" >
			<table cellpadding="1" cellspacing="1"  class="table-detail">
				<tr var="manageTr">
					<input type="hidden"  var="urlParams"  value=""  />
					<input type="hidden"  var="name"  value=""  />
					<td var="index">&nbsp;</td>
					<td var="desc">&nbsp;</td>
					<td var="className">&nbsp;</td>
					<td var="clickName">&nbsp;</td>
					<td var="urlPath">&nbsp;</td>
					<td var="manageRight"></td>
					<td var="paramscript">
						<textarea rows="4" cols="20" var="paramscript"></textarea>
					</td>
					<td var="prescript">
						<textarea rows="4" cols="20" var="prescript"></textarea>
					</td>
					<td var="afterscript">
						<textarea rows="4" cols="20" var="afterscript"></textarea>
					</td>
					<td>
						<a class="link moveup" href="####" onclick="__SysQuerySetting__.moveTr(this,true)"></a>
						<a class="link movedown" href="####" onclick="__SysQuerySetting__.moveTr(this,false)"></a>
						<a class="link del" href="####" onclick="__SysQuerySetting__.delTr(this)"></a>
					</td>
				</tr>
			</table>
		</div>	
		
</div>

</body>
</html>