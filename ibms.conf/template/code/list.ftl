<#assign class=table.variable.class>
<#assign package=table.variable.package>
<#assign comment=table.tableDesc>
<#assign classVar=table.variable.classVar>
<#assign fieldList=table.fieldList>
<#function getJavaType dataType>
<#assign dbtype=dataType?lower_case>
<#assign rtn>
<#if  dbtype=="number" >
Long
<#elseif (dbtype=="varchar"||dbtype=="clob")  >
String
<#elseif (dbtype=="date")>
java.util.Date
</#if></#assign>
 <#return rtn?trim>
</#function>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>${comment }管理</title>
<%@include file="/commons/include/get.jsp" %>
<#if flowKey?exists>
<script type="text/javascript">
	function startFlow(id){
		$.post("run.do?isList=1&<#if table.isExternal==0>id<#else>${table.pkField}</#if>="+id,function(responseText){
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.success("启动流程成功！", "成功", function(rtn) {
					if(rtn){
						this.close();
					}
					window.location.href = "<#noparse>${ctx}</#noparse>/${system}/${package}/${classVar}/list.do";
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		});
	}
</script>
</#if>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${comment }管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">修改</a></div>
					<#if !flowKey?exists>
				
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					</#if>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<#list fieldList as field>
						<#if field.isQuery==1>
						<#if (field.fieldType=="date")>
						<span class="label">${field.fieldDesc} 从:</span> <input  name="Q_begin${field.fieldName}_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_end${field.fieldName}_DG" class="inputText date" />
						<#elseif field.fieldType="number">
						<span class="label">${field.fieldDesc}:</span><input type="text" name="Q_${field.fieldName}_L"  class="inputText" />
						<#else>
						<span class="label">${field.fieldDesc}:</span><input type="text" name="Q_${field.fieldName}_S"  class="inputText" />
						</#if>
						</#if>
						</#list>
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="${classVar}List" id="${classVar}Item" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="<#noparse>${checkAll}</#noparse>" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="<#noparse>${</#noparse>${classVar}Item.<#if table.isExternal==0>id<#else>${table.pkField}</#if>}">
				</display:column>
				<#list fieldList as field>
				<#if field.isList==1&&(field.fieldName?lower_case!=table.pkField?lower_case)&&field.isHidden==0>
				<#if (field.fieldType=="date")>
				<display:column  title="${field.fieldDesc}" sortable="true" sortName="<#if table.isExternal==0>F_</#if>${field.fieldName?upper_case}">
					<fmt:formatDate value="<#noparse>${</#noparse>${classVar}Item.${field.fieldName}}" pattern="yyyy-MM-dd"/>
				</display:column>
				<#else>
				<display:column property="${field.fieldName}" title="${field.fieldDesc}" sortable="true" sortName="<#if table.isExternal==0>F_</#if>${field.fieldName?upper_case}"></display:column>
				</#if>
				</#if>
				</#list>
				<display:column title="管理" media="html" style="width:220px">
					<#if flowKey?exists>
					<c:if test="<#noparse>${</#noparse>${classVar}Item.runId==0}">
						<a href="del.do?id=<#noparse>${</#noparse>${classVar}Item.<#if table.isExternal==0>id<#else>${table.pkField}</#if>}" class="link del">删除</a>
						<a href="####" onclick="startFlow('<#noparse>${</#noparse>${classVar}Item.<#if table.isExternal==0>id<#else>${table.pkField}</#if>}')" class="link run"><span></span>提交</a>
					</c:if>
					<#else>
					<a href="del.do?id=<#noparse>${</#noparse>${classVar}Item.<#if table.isExternal==0>id<#else>${table.pkField}</#if>}" class="link del">删除</a>
					</#if>
					<a href="edit.do?id=<#noparse>${</#noparse>${classVar}Item.<#if table.isExternal==0>id<#else>${table.pkField}</#if>}" class="link edit"><span></span>编辑</a>
					<a href="get.do?id=<#noparse>${</#noparse>${classVar}Item.<#if table.isExternal==0>id<#else>${table.pkField}</#if>}" class="link detail"><span></span>明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="${classVar}Item"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


