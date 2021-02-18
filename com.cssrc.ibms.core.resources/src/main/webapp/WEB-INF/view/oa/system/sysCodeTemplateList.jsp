<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代码模版管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
$(function(){
	handlerInit();
});
//处理初始化模板
function handlerInit()
{
	$("a.link.init").click(function(){
		var action=$(this).attr("action");
		if($(this).hasClass('disabled')) return false;
		
		$.ligerDialog.confirm('初始化表单模板将会导致系统模板重新生成，确定初始化吗？','提示信息',function(rtn){
			if(rtn){
				var form=new com.ibms.form.Form();
				form.creatForm('form', action);
				form.submit();
			}
		});
		
	});
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">代码模版管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link init" id="btnInit" action="init.do">初始化模版</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
						<li><span class="label">模版名称:</span><input type="text" name="Q_templateName_SL" value="${param['Q_templateName_SL']}"  class="inputText" /></li>
						<li><span class="label">别名:</span><input type="text" name="Q_templateAlias_SL" value="${param['Q_templateAlias_SL']}"  class="inputText" /></li>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysCodeTemplateList" id="sysCodeTemplateItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${sysCodeTemplateItem.id}">
				</display:column>
				<display:column property="templateName" title="模版名称" sortable="true" sortName="TEMPLATE_NAME"></display:column>
				<display:column property="templateAlias" title="别名" sortable="true" sortName="TEMPLATE_ALIAS"></display:column>
				<display:column property="memo" title="备注" sortable="true" sortName="memo"></display:column>
				<display:column  title="模版类型" sortable="true" sortName="TEMPLATE_TYPE">
				<c:choose >
					<c:when test="${sysCodeTemplateItem.templateType==1}"><span class="red">系统模版</span></c:when>
					<c:otherwise>自定义模版</c:otherwise>
				</c:choose>
				</display:column>
				<display:column title="管理" media="html" style="width:180px;text-align:center">
					<c:if test="${sysCodeTemplateItem.templateType==0}">
					<a href="del.do?id=${sysCodeTemplateItem.id}" class="link del">删除</a>
					<a href="edit.do?id=${sysCodeTemplateItem.id}" class="link edit">编辑</a>
					</c:if>
					<a href="get.do?id=${sysCodeTemplateItem.id}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="sysCodeTemplateItem"/>
		</div> 			
	</div>
</body>
</html>


