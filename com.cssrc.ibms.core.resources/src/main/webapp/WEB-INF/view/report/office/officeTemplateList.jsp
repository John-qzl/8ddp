<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>报表模板管理</title>
<%@include file="/commons/include/get.jsp"%>


</head>
<body>

	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">报表列表管理</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link search" id="btnSearch">查询</a>
					</div>
					
					<div class="group">
						<a class="link add" href="edit.do?typeid=${typeid}">添加</a>
					</div>
					<div class="group">
						<a class="link add" href="uploadTemp.do?typeid=${typeid}">上传</a>
					</div>
					
					<div class="group">
						<a class="link update" id="btnUpd" action="edit.do">编辑</a>
					</div>
					
					<div class="group">
						<a class="link del" action="del.do">删除</a>
					</div>
					
					<div class="group">
						<a href="javascript:;" class="link reset"
							onclick="$.clearQueryForm();">重置</a>
					</div>
				</div>
			</div>

			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
						<li><span class="label">模板名称:</span><input type="text"
							name="Q_title_SL" class="inputText"
							value="${param['Q_title_SL']}" /></li>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
	
			<display:table name="officeTemplateList" id="officeTemplateItem"
				requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"
				class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="officeid"
						value="${officeTemplateItem.officeid}">
				</display:column>
				<display:column property="title" title="模板名称" sortable="true"
					sortName="title"></display:column>
				<display:column property="filepath" title="文件路径"></display:column>
				<display:column property="filepath" title="数据视图"></display:column>
				<display:column property="officeType" title="模板类别"></display:column>
				<display:column property="typeName" title="分类"></display:column>
				<display:column title="创建时间" sortable="true" sortName="CREATE_TIME">
					<fmt:formatDate value="${officeTemplateItem.createtime}"
						pattern="yyyy-MM-dd" />
				</display:column>
				<display:column title="创建人">
					<fmt:formatDate value="${officeTemplateItem.createtime}"
						pattern="yyyy-MM-dd" />
				</display:column>
				
				<display:column title="修改时间" >
					<fmt:formatDate value="${officeTemplateItem.updatetime}"
						pattern="yyyy-MM-dd" />
				</display:column>
				<display:column title="管理" media="html"
					style="width:180px;text-align:center" class="rowOps">
					<a href="edit.do?officeid=${officeTemplateItem.officeid}"
						class="link edit">编辑</a>
					<a href="del.do?officeid=${officeTemplateItem.officeid}"
						class="link del">删除</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="officeTemplateItem" />

		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->

</body>
</html>


