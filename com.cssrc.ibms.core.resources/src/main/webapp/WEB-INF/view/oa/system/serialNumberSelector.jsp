<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html style="height:100%">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<title>选择流水号</title>
	<script type="text/javascript">
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)

		function getTable(Alias,Name){
			var obj={alias:Alias,name:Name};
			dialog.get('sucCall')(obj);
			dialog.close();
		}
	</script>
	
</head>
<body style="height:100%;overflow:hidden;">
	<iframe src="showlist.do" width="100%" height="100%" frameborder="0"></iframe>
</body>
</html>