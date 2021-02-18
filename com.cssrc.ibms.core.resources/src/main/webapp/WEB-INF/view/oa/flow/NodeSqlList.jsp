<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>流程定义扩展管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	function openEditPage(id,nodeId,actdefId,defId){
		
		var url=__ctx+"/oa/flow/NodeSql/edit.do?id="+id+"&nodeId="+nodeId+"&actdefId="+actdefId+"&defId="+defId;
		DialogUtil.open({
			height:700,
			width: 800,
			title : "节点sql设置",
			url: url,
			isResize: true
		});
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-container">
 			<div class="panel-title">
 				<jsp:include page="incDefinitionHead.jsp">
 					<jsp:param value="流程节点sql" name="title"/>
			 	</jsp:include>
			</div>
	  		<f:tab curTab="nodeSql" tabName="flow%"/>
			<div class="panel-body">
				<display:table name="bpmNodeSqlList" id="bpmNodeSqlItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
				  		<input type="checkbox" class="pk" name="id" value="${bpmNodeSqlItem.id}">
					</display:column>
					<display:column property="name" title="名称" sortable="true" sortName="NAME" maxLength="80"></display:column>
					<display:column property="nodeName" title="节点ID" sortable="true" sortName="NODEID" maxLength="80"></display:column>
					<display:column property="dsAlias" title="数据源别名" sortable="true" sortName="DSALIAS" maxLength="80"></display:column>
					<display:column property="action" title="触发时机"></display:column>
					<display:column property="sql" title="SQL语句" maxLength="80"></display:column>
					<display:column title="管理" media="html" style="width:220px">
						<a href="del.do?id=${bpmNodeSqlItem.id}" class="link del">删除</a>
						<a href="javaScript:openEditPage('${bpmNodeSqlItem.id}','${bpmNodeSqlItem.nodeId}','${bpmNodeSqlItem.actdefId}','${param.defId}');" class="link edit">编辑</a>
					</display:column>
				</display:table>
			</div>	
		</div>	
	</div> <!-- end of panel -->
</body>
</html>


