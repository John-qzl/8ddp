<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>报表模板管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
function reportPreview(reportid,reportSeverlet,fileName){
	var mill=(parseInt(Math.random()*10000)).toString();
	var url='${ctx}/oa/system/reportTemplate/'+reportid+'/preView.do?mill='+mill;
	location.href=url;
}
</script>
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
						<li><span class="label">标题:</span><input type="text"
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
			<display:table name="reportTemplateList" id="reportTemplateItem"
				requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"
				class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="reportid"
						value="${reportTemplateItem.reportid}">
				</display:column>
				<display:column property="title" title="标题" sortable="true"
					sortName="title"></display:column>
				<display:column property="descp" title="描述" sortable="true"
					sortName="descp"></display:column>
				<display:column property="reportlocation" title="报表模板文件路径"
					sortable="true" sortName="reportlocation"></display:column>
				<display:column title="创建时间" sortable="true" sortName="createtime">
					<fmt:formatDate value="${reportTemplateItem.createtime}"
						pattern="yyyy-MM-dd" />
				</display:column>
				<display:column title="修改时间" sortable="true" sortName="updateTime">
					<fmt:formatDate value="${reportTemplateItem.updatetime}"
						pattern="yyyy-MM-dd" />
				</display:column>

				<display:column title="管理" media="html"
					style="width:180px;text-align:center" class="rowOps">
					<a href="javascript:reportPreview('${reportTemplateItem.reportid}','${reportTemplateItem.reportSeverlet}','${reportTemplateItem.fileName}');"
						class="link detail">预览</a>
					<a href="edit.do?reportid=${reportTemplateItem.reportid}&setparam=1"
						class="link detail">参数设置</a>
					<a href="edit.do?reportid=${reportTemplateItem.reportid}"
						class="link edit">编辑</a>
					<a href="del.do?reportid=${reportTemplateItem.reportid}"
						class="link del">删除</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="reportTemplateItem" />

		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->

</body>
</html>


