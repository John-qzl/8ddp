<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">

</script>
</head>
<body>
	<div>
		<a href="${ctx}/oa/task/business/show.do">待办任务(${dataNum})</a>
	</div>
</body>
</html>
