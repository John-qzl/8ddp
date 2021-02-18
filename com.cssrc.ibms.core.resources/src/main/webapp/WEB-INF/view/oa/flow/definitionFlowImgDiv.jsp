<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

	<script type="text/javascript">
	var _height=${shapeMeta.height};
	function setIframeHeight(){
		var mainIFrame = window.parent.document.getElementById("flowchart");
		if(!mainIFrame)return;
		mainIFrame.style.height=_height+100;
	};
	
	$(function(){
		if(self!=top){
			setIframeHeight();
		}
	});
	</script>
	<div class="panel" style="padding-top: 1px;">
		<div style="overflow:auto;height:100%;background-color: white;">
			<div style="padding-top:40px">
				<div style="position: relative;background:url('${ctx}/bpmImage?definitionId=${actDefId}') no-repeat;width:${shapeMeta.width}px;height:${shapeMeta.height}px;"></div>
			</div>
		</div>
	</div>