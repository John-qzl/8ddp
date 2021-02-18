<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	function showXmlWindow(obj) {
		var url = "";
		if ($(obj).text().trim() == 'BPMN-XML') {
			url = "${ctx}/oa/flow/definition/bpmnXml.do?defId=${bpmDefinition.defId}";
		} else {
			url = "${ctx}/oa/flow/definition/designXml.do?defId=${bpmDefinition.defId}";
		}
		url = url.getNewUrl();
		var argms="toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no";
		window.open(url, "",argms);
	}
</script>
<style>
body {
	padding: 0px;
	margin: 0;
	overflow-x: hidden;
}
</style>

<div useSclFlw="false">
	<div class="panel-toolbar">
		<div class="panel-top-left">流程定义管理->${bpmDefinition.subject }</div>
		<div class="panel-top-right">
			<div class="toolBar" style="margin: 0;">
				
				<div class="group">
					<a class="link xml-bpm" onclick="showXmlWindow(this);">ACTIVITI-XML</a>
				</div>
				
				<div class="group">
					<a class="link xml-design" onclick="showXmlWindow(this);">DESIGN-XML</a>
				</div>
				
				<div class="group">
					<c:choose>
						<c:when test="${not empty param.defIdForReturn}">
							<a class="link back"
								href="${ctx}/oa/flow/definition/versions.do?defId=${param.defIdForReturn}">返回</a>
						</c:when>
						<c:otherwise>
							<a class="link back"
								href="<%=request.getParameter("returnUrl").replace("@", "&")%>">返回</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>