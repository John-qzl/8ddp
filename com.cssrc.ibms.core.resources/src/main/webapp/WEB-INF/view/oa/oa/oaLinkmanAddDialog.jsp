<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>联系人明细</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">添加联系人</span>
			</div>
		</div>
		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
		    <c:forEach items="${arryData}" var="arryData">
			    <tr>
					<th width="18%">姓名: <span class="required red">*</span></th>
					<td ><input type="text" required="required" id="name" name="name" style="width:240px !important" class="inputText"/></td>
					<th>邮箱地址: </th>
				    <td ><input type="text" readonly="readonly" id="email" name="email" value="${arryData}" style="width:240px !important" class="inputText"/></td>
				</tr>
		    </c:forEach>
		</table>
		</div>
	</div>
</body>
</html>

