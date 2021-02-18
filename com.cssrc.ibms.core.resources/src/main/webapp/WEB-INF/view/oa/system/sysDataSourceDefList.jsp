<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>SYS_DATA_SOURCE_DEF管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js" charset="UTF-8"></script>	
<script type="text/javascript">

//导出系统数据源
function exportXml(){
	var tableIds = ImportExportXml.getChkValue('pk');
	var url=__ctx + "/oa/system/sysDataSourceDef/exportXml.do?tableIds="+tableIds;
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
//导入系统数据源
function importXml(){
	var url=__ctx + "/oa/system/sysDataSourceDef/import.do";
	ImportExportXml.showModalDialog({url:url,title:'导入系统数据源'});
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">SYS_DATA_SOURCE_DEF管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group">
					<a onclick="exportXml()"  class="link export">导出</a>
					</div>
			   		
					<div class="group">
					<a onclick="importXml()"  class="link import">导入</a>
					</div>
					
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<span class="label">名称:</span><input type="text" name="Q_name_SL"  class="inputText" value="${param['Q_name_SL'] }" />
						<span class="label">别名:</span><input type="text" name="Q_alias_SL"  class="inputText" value="${param['Q_alias_SL'] }" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysDataSourceDefList" id="sysDataSourceDefItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${sysDataSourceDefItem.id}">
				</display:column>
				<display:column property="name" title="名称" sortable="true" sortName="NAME_"></display:column>
				<display:column property="alias" title="别名" sortable="true" sortName="ALIAS_"></display:column>
				<display:column property="dbType" title="数据库类型" sortable="true" sortName="DB_TYPE_"></display:column>
				<display:column property="initContainer" title="初始化容器" sortable="true" sortName="INIT_ON_START_"></display:column>
				<display:column property="isEnabled" title="是否生效" sortable="true" sortName="ENABLED_"></display:column>
				<display:column title="管理" media="html" style="width:220px">
					<a href="edit.do?id=${sysDataSourceDefItem.id}" class="link edit">编辑</a>
					<a href="del.do?id=${sysDataSourceDefItem.id}" class="link del">删除</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="sysDataSourceDefItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


