
<%--
	time:2012-12-19 15:38:01
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>自定义表代码模版明细</title>
<%@include file="/commons/include/getById.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统脚本详细信息</span>
			</div>
			<c:if test="${canReturn==0}">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
					<div class="group">
						<a href="####" class="tipinfo">
					        <span>
			                 <br/> 因为jdbcDao在app-beans.xml文件中注册过。
			                 <br/>因此在groovy脚本中直接写：jdbcDao.upd("UPDATE  CWM_SYS_USER SET PHOTO =15061542312 WHERE USERID = -1")。就能够成功运行。
					       </span>
				        </a>
			       </div>
				</div>
			</div>
			</c:if>
		</div>
		<div class="panel-body">
		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
			 
			<tr>
				<th width="20%">脚本名称:</th>
				<td>${sysScript.name}</td>
			</tr>
 
			<tr>
				<th width="20%">脚本分类:</th>
				<td>${sysScript.category}</td>
			</tr>
			
			<tr>
				<th width="20%">脚本备注:</th>
				<td>${sysScript.memo}</td>
			</tr>
			
			<tr>
				<th width="20%">脚本内容:</th>
				<td>
					<textarea name="script" cols="120" rows="15" readonly="readonly" >${fn:escapeXml(sysScript.script)}</textarea>
				</td>
			</tr>
		</table>
		</div>
		
	</div>
</body>
</html>

