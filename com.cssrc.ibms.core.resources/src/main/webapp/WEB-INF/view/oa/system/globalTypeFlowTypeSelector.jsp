<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>选择流程分类</title>
<f:link href="from-jsp.css"></f:link><script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessUrgeDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/foldBox.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/displaytag.js" ></script>
<script type="text/javascript">

	
	
</script>
</head>
<body>
	<div position="center" >
		<div class="panel">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link search" id="btnSearch">查询</a>  
					</div>
				</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="flowTypeSelector.do">
					<input type="hidden" name="defId" id="bpmDefId" value="${defId}"/>
					<div class="row">
						<span class="label">分类名称:</span><input type="text" name="Q_typeName_SL"  class="inputText" style="width:120px;"/>
					</div>
				</form>
			</div>
			<div class="panel-body">
	    	    <c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
				</c:set>
				
			    <display:table name="globalTypeList" id="globalTypeItem" requestURI="flowTypeSelector.do" sort="external" 
			    	cellpadding="1" cellspacing="1" export="false"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
						  		<input type="checkbox" class="pk" name="typeId" value="${globalTypeItem.typeId}" defSubject="${globalTypeItem.typeName}">
					</display:column>
					<display:column property="typeName"  titleKey="分类名称" sortable="true" sortName="typeName" style="text-align:center"></display:column>
					
				</display:table>
				<ibms:paging tableId="globalTypeItem"></ibms:paging>
			</div>		
		</div>
	</div>
</body>
</html>

