<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>自定义多表查询管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerWindow.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js" charset="UTF-8"></script>

<script type="text/javascript">
//配置
function setting(sqlId){
	var url=__ctx+"/oa/form/querySetting/edit.do?sqlId="+sqlId;
	$.openFullWindow(url);
}
</script>
<script type="text/javascript">
//导出自定义Sql查询
function exportXml(){
	//
	var tableIds = ImportExportXml.getChkValue('pk');
	var url=__ctx + "/oa/form/querySql/exportXml.do?tableIds="+tableIds;
	if (tableIds  ==''){
		$.ligerDialog.confirm('还没有选择，是否导出全部？','提示信息',function(rtn) {
			if(rtn) {
				var form=new com.ibms.form.Form();
				form.creatForm("form", url);
				form.submit();
			}
		});
		
	}else{
	
	$.ligerDialog.confirm('确认导出吗？','提示信息',function(rtn) {
		if(rtn) {
			var form=new com.ibms.form.Form();
			form.creatForm("form", url);
			form.submit();
		}
	});
	}
}
//导入自定义Sql查询
function importXml(){
	var url=__ctx + "/oa/form/querySql/import.do";
	ImportExportXml.showModalDialog({url:url,title:'导入自定义Sql查询'});
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">自定义多表查询管理</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link search" id="btnSearch">查询</a>
					</div>
					
					<div class="group">
						<a class="link add" href="edit.do">添加</a>
					</div>
					
					<div class="group">
						<a class="link update" id="btnUpd" action="edit.do">编辑</a>
					</div>
					
					<div class="group">
					<a onclick="exportXml()"  class="link export">导出</a>
				    </div>
			        
				    <div class="group">
					<a onclick="importXml()"  class="link import">导入</a>
				    </div>
				</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<span class="label">名称:</span><input type="text" name="Q_name_SL"
							class="inputText" value="${param['Q_name_SL']}" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
			<display:table name="sysQuerySqlList" id="sysQuerySqlItem"
				requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"
				class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="id"
						value="${sysQuerySqlItem.id}">
				</display:column>
				<display:column property="categoryName" title="分类"></display:column>
				<display:column property="name" title="名称" sortable="true"
					sortName="NAME" maxLength="80"></display:column>
					
				<display:column property="alias" title="别名" sortable="true"
					sortName="alias" maxLength="80"></display:column>
				<display:column property="sql" title="SQL语句" sortable="true"
					sortName="SQL" maxLength="80"></display:column>
				<display:column title="管理" media="html" style="width:220px">
					<a href="del.do?id=${sysQuerySqlItem.id}" class="link del">删除</a>
					<a href="edit.do?id=${sysQuerySqlItem.id}" class="link edit">编辑</a>
					<%-- <a href="get.do?id=${sysQuerySqlItem.id}" class="link detail">明细</a> --%>
					<a href="javascript:;" onclick="setting(${sysQuerySqlItem.id})"
						class="link preview">配置</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="sysQuerySqlItem" />
		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->
</body>
</html>


