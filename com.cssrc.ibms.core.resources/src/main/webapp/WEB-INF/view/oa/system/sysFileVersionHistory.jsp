<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<title>附件版本</title>
<f:link href="from-jsp.css"></f:link>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ImageQtip.js" ></script>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	$(function() {
		$("#sysFileItem  tr.even,#sysFileItem tr.odd").bind('click', function() {
			var trObj=$(this);
			var obj=$(":checkbox[name='fileId']",trObj);
			if(obj.length>0){
				window.parent.selectMulti(obj);
			}
			
		});
	});
	
</script>
</head>
<body >
<div class="panel">
	<div class="panel-body">
		<c:set var="checkAll">
			<input type="checkbox" id="chkall" />
		</c:set>
		<display:table name="sysFileList" id="sysFileItem" requestURI="versionHistory.do" sort="external" cellpadding="1"
			cellspacing="1" export="false" class="table-grid">
			<display:column title="${checkAll}" media="html" style="width:30px;">
				<input type="checkbox" class="pk" name="fileId" value="${sysFileItem.fileId}">
				<textarea name="fileData" style="display: none;">{fileId:'${sysFileItem.fileId}',filename:'${sysFileItem.filename}.${sysFileItem.ext}'}</textarea>
			</display:column>
			<display:column title="文件名" sortable="true" sortName="filename">
				<a href="${ctx}/oa/system/sysFile/download.do?fileId=${sysFileItem.fileId }" css="link download">${sysFileItem.filename}.${sysFileItem.ext}</a>
			</display:column>
			<display:column title="创建时间" sortable="true" sortName="createtime">
						<fmt:formatDate value="${sysFileItem.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</display:column>
			<display:column property="ext" title="扩展名" sortable="true" sortName="ext" />
			<display:column property="note" title="大小" sortable="true" sortName="note" maxLength="80"></display:column>
			<display:column property="version" title="版本" sortable="true" sortName="version"></display:column>
			
			<display:column property="creator" title="上传者"  ></display:column>
			<display:column title="归档"  >
				<c:choose>
					<c:when test="${sysFileItem.filing>0}">
						<span class="green">已归档</span>
					</c:when>
					<c:otherwise>
						<span class="red">未归档</span>
					</c:otherwise>
				</c:choose>
			</display:column>
		</display:table>
		<ibms:paging tableId="sysFileItem" showExplain="false"/>
	</div>
</div>
</body>
</html>


