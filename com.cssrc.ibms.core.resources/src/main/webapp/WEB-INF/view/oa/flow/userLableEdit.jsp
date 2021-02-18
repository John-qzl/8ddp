<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="ibms" uri="http://www.cssrc.com.cn/paging" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=IE8" />
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<f:link href="web.css"></f:link>
<f:link href="jquery/plugins/rowOps.css"></f:link><!--é¼ æ æ»å¨css  -->
<f:js pre="jslib/lang/common" ></f:js>
<f:js pre="jslib/lang/js" ></f:js>
<html>
<head>
<title>脚本设置</title>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	function save(){
		$("#userlabelForm").ajaxSubmit({
			success:function(responseText, statusText){
				var json = eval('(' + responseText + ')');
				alert(json.message)
				dialog.close();
			}
		});
	}
	
	
</script>
</head>
<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">

				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" onclick="save()">
								
								保存
							</a>
						</div>
						
						<div class="group">
							<a class="link del" onclick="javasrcipt:dialog.close()">
								
								关闭
							</a>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="userlabelForm" action="save.do">
				<input type="hidden" id="setId" name="setId" value="${setId}" />
				<table class="formTable" border="1" cellpadding="2" cellspacing="0">
					<tbody>

						<tr>
							<td style="width: 20%;" class="formTitle" align="right" nowrap="nowarp">tip提示:</td>
							<td class="formInput" style="width: 80%;">
								<span name="editable-input" style="display: inline-block;" isflag="tableflag">
									<input name="tip" lablename="tip提示" class="inputText" type="text" value="${userlabel.tip}">
								</span>
							</td>
						</tr>
						<tr>
							<td style="width: 20%;" class="formTitle" align="right" nowrap="nowarp">label标签:</td>
							<td class="formInput" style="width: 80%;">
								<span name="editable-input" style="display: inline-block;" isflag="tableflag">
									<input name="userlabel" lablename="label标签" class="inputText" type="text" value="${userlabel.userlabel}">
								</span>
							</td>
						</tr>

					</tbody>
				</table>

			</form>
		</div>

	</div>
</body>
</html>