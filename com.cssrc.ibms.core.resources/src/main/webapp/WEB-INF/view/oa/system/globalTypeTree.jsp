<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>总分类</title>
	<%@include file="/commons/include/form.jsp" %>
	<base target="_self"/> 
	<f:link href="tree/zTreeStyle.css"></f:link>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/GlobalMenu.js"></script>
	<script type="text/javascript">
		var catId=${sysTypeKey.typeKeyId};
	</script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/GlobalType.js"></script>
 	
</head>
<body>
	<div id="layout">
		<div position="left" title="分类管理"  >
		   <div style="width:100%;">
		        <select id="dkey" style="width:99.8% !important;">  
		              <c:forEach var="key" items="${typeList}">  
		             	 <option style="text-align:left" value="${key.typeKeyId}">${key.typeName}</option>  
		              </c:forEach>  
		        </select>
		   </div>
			<div class="tree-toolbar tree-title">
				<span class="toolBar" style="text-overflow:ellipsis;overflow:hidden;white-space:nowrap">
					<div class="group"><a class="link reload" id="treeFresh" href="javascript:refresh();" >刷新</a></div>
					<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" >展开</a></div>
					<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" >收起</a></div>
				</span>
			</div>
			<span position="left" style="color:red;white-space:nowrap">注：已逻辑删除的数据后面有*标识</span>
			<div id="glTypeTree" class="ztree" style="margin-top:2px;"></div>
		</div>
		<div position="center">
			<div class="l-layout-header">系统分类信息</div>
			<iframe id="listFrame" src="getEmpty.do" frameborder="no" width="100%" height="100%"></iframe>
		</div>
	</div>
</body>
</html>

