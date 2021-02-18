<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<meta charset="utf-8">
<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
   Remove this if you use the .htaccess -->
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="initial-scale=1.0, target-densitydpi=device-dpi" />
<!-- this is for mobile (Android) Chrome -->
<meta name="viewport" content="initial-scale=1.0, width=device-height">
<!--  mobile Safari, FireFox, Opera Mobile  -->
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script src="${ctx}/jslib/ibmssign/jSignature.min.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibmssign/flashcanvas.js"></script>

<!--[if lt IE 9]>
<script type="text/javascript" src="../libs/flashcanvas.js"></script>
<![endif]-->
<style type="text/css">
div {
	margin-top: 1em;
	margin-bottom: 1em;
}

input {
	padding: .5em;
	margin: .5em;
}

select {
	padding: .5em;
	margin: .5em;
}

#signatureparent {
	color: darkblue;
	background-color: darkgrey;
	/*max-width:600px;*/
	padding: 20px;
}

/*This is the div within which the signature canvas is fitted*/
#signature {
	border: 2px dotted black;
	background-color: lightgrey;
}

/* Drawing the 'gripper' for touch-enabled devices */
html.touch #content {
	float: left;
	width: 92%;
}

html.touch #scrollgrabber {
	float: right;
	width: 4%;
	margin-right: 2%;
	background-image:
		url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAFCAAAAACh79lDAAAAAXNSR0IArs4c6QAAABJJREFUCB1jmMmQxjCT4T/DfwAPLgOXlrt3IwAAAABJRU5ErkJggg==)
}

html.borderradius #scrollgrabber {
	border-radius: 1em;
}
</style>
</head>
<body>
	<div>
		<div id="content">
			<div id="signatureparent">
				<div id="signature"></div>
			</div>
			<div id="tools">
				<input type="button" value="预览" id="pre" />
				<input type="button" value="确定" id="ok" />
				<input type="button" value="重写" id="reset" />
			</div>
			<div>
				<p>display area:</p>
				<div id="displayarea"></div>
			</div>
		</div>
		<div id="scrollgrabber"></div>
	</div>
	<script>
		var dialog;

		$(function() {
			dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
			var $sigdiv = $("#signature").jSignature({
				'UndoButton' : true
			})
			$("#pre").click(function() {
				//将画布内容转换为图片
				var datapair = $sigdiv.jSignature("getData", "image");
				var i = new Image();
				i.src = "data:" + datapair[0] + "," + datapair[1];
				$("#displayarea").html("");
				$(i).appendTo($("#displayarea")); // append the image (SVG) to DOM.
			});
			//datapair = $sigdiv.jSignature("getData","base30");
			//$sigdiv.jSignature("setData", "data:" + datapair.join(","));
			$("#ok").click(function() {
				var datapair = $sigdiv.jSignature("getData", "svg");
				var data = "svg=" + datapair[1];
				var mill = (parseInt(Math.random() * 10000)).toString();
				var url = "${ctx}/oa/system/sign/savePng.do?mill=" + mill;
				$.ajax({
					url : url,
					type : "post",
					async : false,
					data : data,
					success : function(result) {
						var json = eval('(' + result + ')');
						dialog.get("succcall")(json);
						dialog.close();
					},
					error : function() {
						$.ligerDialog.warn("连接超时，请联系系统管理员！", "提示信息");
					}
				})
			});
			$("#reset").click(function() {
				$sigdiv.jSignature("reset"); //重置画布，可以进行重新作画.
				$("#displayarea").html("");
			});
		});
	</script>
</body>
</html>
