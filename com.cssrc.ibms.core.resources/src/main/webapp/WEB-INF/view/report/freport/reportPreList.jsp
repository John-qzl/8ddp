<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>预览${reportTemplate.title}列表报表</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	//cjkEncode方法的实现代码
	function cjkEncode(text) {
		if (text == null) {
			return "";
		}
		var newText = "";
		for (var i = 0; i < text.length; i++) {
			var code = text.charCodeAt(i);
			if (code >= 128 || code == 91 || code == 93) { //91 is "[", 93 is "]".       
				newText += "[" + code.toString(16) + "]";
			} else {
				newText += text.charAt(i);
			}
		}
		return newText;
	}

	function handlerSearchAjax() {
		//var params = $("#searchForm").serialize();
		var mill = (parseInt(Math.random() * 10000)).toString();
		//拼接出最终报表访问路径，并对完整的路径进行编码转换，防止乱码问题
		var reportURL = "${reportTemplate.reportSeverlet}${reportTemplate.fileName}";
		var pk = "${pk}"==""?"-1":"${pk}";
		reportURL += "&pk="+pk+"&mill=" + mill;
		reportURL = cjkEncode(reportURL);
		$("#reportFrame").attr("src", reportURL);
		$("#reportFrame").load();
	}
	$(function() {
		handlerSearchAjax();
	})

</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${reportTemplate.title}</span>
			</div>
		</div>
		${queryHtml}
		<iframe id="reportFrame" name="reportFrame" width="100%" height="100%"></iframe>
	</div>
</body>
</html>
