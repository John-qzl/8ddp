<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<html>
<head>
<title>选择图标</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	
	function selectIcon(t){
		if(t){
			dialog.get("sucCall")(t);
		}
		dialog.close();
	}
</script>
</head>
<body>
	<iframe src="icon.jsp" width="100%" frameborder="0" height="100%"></iframe>
</body>
</html>