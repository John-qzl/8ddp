<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<title>联动设置</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/gangedSetList.js"></script>

<script type="text/javascript">
	var nodes =${nodes};
	var bpmGangedSetList = '${bpmGangedSetList}'
</script>
</head>
<body>
	<div class="panel">
		
			<div class="panel-title">
				<jsp:include page="incDefinitionHead.jsp">
					<jsp:param value="联动设置" name="title" />
				</jsp:include>
			</div>	
		
  		<f:tab curTab="gangedSet" tabName="flow%"/>
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">联动设置</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" href="javascript:;">保存</a>
					</div>

					<div class="group">
						<a class="link add" href="javascript:;">添加</a>
					</div>

					<div class="group">
						<a class="link clean" href="javascript:;">删除</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<input type="hidden" name="defid" value="${defid}" />
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
			<div class="panel-table">
				<table id="bpmGangedSetItem" cellpadding="1" cellspacing="1" border="1" style="border-collapse: collapse;" class="table-grid">
					<thead>
						<tr>
							<th><input type="checkbox" id="chkall"></th>
							<th>节点名</th>
							<th>所选字段</th>
							<th>所选值</th>
							<th>变换字段</th>
							<th>变换类型</th>
						</tr>
					</thead>
					<tbody>
						<tr class="empty">
							<td colspan="6">
								<div class="loading-div">正在加载...</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>