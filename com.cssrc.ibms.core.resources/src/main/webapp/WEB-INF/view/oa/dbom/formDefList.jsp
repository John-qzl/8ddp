<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>dbom数据源</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
	function checkBoxClick(obj) {
		if ($(obj).attr("checked")) {
			$(":checkbox[value!=" + $(obj).val() + "]").attr("checked", false);
		}
	}
	$(function() {
		$(":checkbox").each(function(i, ck) {
			$(ck).parent().on("click", function() {
				checkBoxClick(ck);
			});
		})
	})
	function ok(){
		var retunrval=new Object();
		var tablename=$(":checkbox:checked").val();
		retunrval.tablename=tablename;
		retunrval.modelType=${param['Q_modelType_SN']=="数据视图"}?0:1;
		window.returnValue = retunrval;
		window.close();
	}
	function resetDataSource(){
		var retunrval=new Object();
		retunrval.reset=true;
		window.returnValue = retunrval;
		window.close();
	}
</script>
</head>
<body>

	<div class="panel">
		<div class="hide-panel">
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
							<a class="link save" href="javascript:;" onclick="ok()">确认</a>
						</div>
						<div class="group">
							<a href="javascript:;" class="link reset" onclick="resetDataSource();">重置数据源</a>
						</div>
						<div class="group">
							<a href="javascript:;" class="link reset" onclick="$.clearQueryForm();">重置</a>
						</div>
					</div>
				</div>

				<div class="panel-search">
					<form id="searchForm" method="post" action="dataSource.do">
						<ul class="row plat-row">
							<li><span class="label">对应表:</span><input type="text"
								name="Q_tableName_SL" class="inputText"
								value="${param['Q_tableName_SL']}" /></li>
							<li><span class="label">表单标题:</span><input type="text"
								name="Q_tableName_SL" class="inputText"
								value="${param['Q_subject_SL']}" /></li>
						</ul>
					</form>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<display:table name="dataList" id="dataItem"
				requestURI="dataSource.do" sort="external" cellpadding="1"
				cellspacing="1" class="table-grid">
				<display:column media="html" style="width:30px;">
					<input type="checkbox" class="pk" value="${dataItem.tableName}"
						onclick="checkBoxClick(this)">
				</display:column>
				<display:column property="tableName" title="对应表" sortable="true"
					sortName="tableName"></display:column>
				<display:column property="tableDesc" title="表单标题" sortable="true"
					sortName="tableDesc"></display:column>
				<display:column property="modelType" title="表单分类" sortable="true"
					sortName="modelType"></display:column>
			</display:table>
			<ibms:paging tableId="dataItem" />
		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->

</body>
</html>

