<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>编辑 报表参数</title>
<%@include file="/commons/include/form.jsp"%>
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

	function autoSubmit() {
		var params = "";
		var mill = (parseInt(Math.random() * 10000)).toString();
		$(".reportpram").each(function(i, param) {
			var name = $(param).attr("name");
			var value = $(param).val();
			if (value != null && value != "") {
				params += "&" + name + "=" + value;
			}
		});
		//拼接出最终报表访问路径，并对完整的路径进行编码转换，防止乱码问题
		var reportURL = "${reportTemplate.reportSeverlet}${reportTemplate.fileName}";
		reportURL += params + "&mill=" + mill;
		reportURL = cjkEncode(reportURL);
		$("#reportFrame").attr("src", reportURL)
	}
	$(function() {
		autoSubmit();
	})
	function back() {
		location.href = "${requestUri}";
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${reportTemplate.title}</span>
			</div>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group">
					<a class="link" id="btnSearch" href="javascript:autoSubmit();">
						
						预览
					</a>
				</div>
				
			</div>
		</div>
		<div class="panel-search">
			<div class="drop"></div>
			<ul class="row plat-row">
				<c:forEach items="${params}" var="item">
					<li>
						<span class="label">${item.name}:</span>
						<c:if test="${item.paramtype=='date'}">
							<c:choose>
								<c:when test="${item.paramSize==8}">
									<input type="text" id="${item.name}" name="${item.name}" class="Wdate reportpram" size="13" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
								</c:when>
								<c:when test="${item.paramSize==10}">
									<input type="text" id="${item.name}" name="${item.name}" class="Wdate reportpram" size="15" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH'})" />
								</c:when>
								<c:when test="${item.paramSize==12}">
									<input type="text" id="${item.name}" name="${item.name}" class="Wdate reportpram" size="20" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" />
								</c:when>
								<c:when test="${item.paramSize==14}">
									<input type="text" id="${item.name}" name="${item.name}" class="Wdate reportpram" size="20" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</c:when>
								<c:otherwise>
									<input type="text" id="${item.name}" name="${item.name}" class="Wdate reportpram" size="15" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${item.paramtype=='text'}">
							<input type="text" name="${item.name}" class="inputText reportpram" value="${item.value_}" />
						</c:if>
						<c:if test="${item.paramtype==null}">
							<input type="text" name="${item.name}" class="inputText reportpram" value="${item.value_}" />
						</c:if>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<iframe id="reportFrame" name="reportFrame" width="100%" height="100%"></iframe>
</body>
</html>
