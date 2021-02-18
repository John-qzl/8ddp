<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择模版</title>
<script type="text/javascript">
	/*KILLDIALOG*/
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)

	function selectTemplate(template){
		dialog.get('sucCall')(template);
		
		dialog.close();
	}
</script>
</head>
<body>
	<iframe src="dialog.do" width="100%" height="100%" frameborder="0"></iframe>
</body>
</html>


