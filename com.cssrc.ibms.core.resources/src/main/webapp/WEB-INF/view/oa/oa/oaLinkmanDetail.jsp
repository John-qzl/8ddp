<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/jslib/ibms/scriptMgr.js"></script>
<script type="text/javascript">
	function afterOnload(){
		var afterLoadJs=[
		     			'${ctx}/jslib/ibms/formdata.js',
		     			'${ctx}/jslib/ibms/subform.js'
		 ];
		ScriptMgr.load({
			scripts : afterLoadJs
		});
	}
</script>
<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
	
	<tr>
		<th width="20%">NAME:</th>
		<td>${oaLinkman.name}</td>
	</tr>
	
	<tr>
		<th width="20%">性别:</th>
		<td>${oaLinkman.sex}</td>
	</tr>
	
	<tr>
		<th width="20%">电话:</th>
		<td>${oaLinkman.phone}</td>
	</tr>
	
	<tr>
		<th width="20%">邮箱:</th>
		<td>${oaLinkman.email}</td>
	</tr>
	
	<tr>
		<th width="20%">公司:</th>
		<td>${oaLinkman.company}</td>
	</tr>
	
	<tr>
		<th width="20%">工作:</th>
		<td>${oaLinkman.job}</td>
	</tr>
	
	<tr>
		<th width="20%">地址:</th>
		<td>${oaLinkman.address}</td>
	</tr>
	
	<tr>
		<th width="20%">创建时间:</th>
		<td>
		<fmt:formatDate value="${oaLinkman.createtime}" pattern="yyyy-MM-dd"/>
		</td>
	</tr>
	
	<tr>
		<th width="20%">所属组:</th>
		<td>${oaLinkman.groupid}</td>
	</tr>
</table>
