<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>执行计划管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
$(function() {
$("[name='startHref']").click(function(){
	if($(this).hasClass('disabled')) return false;
	var ele = this;
	$.ligerDialog.confirm('确认启用该计划吗？','提示信息',function(rtn) {
		if(rtn) {
			if(ele.click) {
				$(ele).unbind('click');
				ele.click();
			} else {
				location.href=ele.href;
			} 
		}
	});
	return false;
});
$("[name='pauseHref']").click(function(){
	if($(this).hasClass('disabled')) return false;
	var ele = this;
	$.ligerDialog.confirm('确认停用该计划吗？','提示信息',function(rtn) {
		if(rtn) {
			if(ele.click) {
				$(ele).unbind('click');
				ele.click();
			} else {
				location.href=ele.href;
			} 
		}
	});
	return false;
});
});

</script>
</head>
<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">执行计划管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<!-- <div class="group"><a class="link search" id="btnSearch">查询</a></div>
					 -->
					<div class="group"><a class="link add" href="planeEdit.do?name=${quartzName}">添加</a></div>
					<div class="group"><a class="link back" href="list.do" >返回</a></div>
				</div>	
			</div>
		</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
			  <display:table name="sysJobPlaneList" id="TriggerObject" requestURI="planeList.do?name=${TriggerObject.name}" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
                  <display:column property="name" title="计划名称" ></display:column>
                  <display:column property="group" title="计划组" ></display:column>
                  <display:column property="cron" title="cron表达式" ></display:column>
                  <display:column property="cronDescription" title="执行时间" ></display:column>
                  <display:column property="status" title="当前状态" ></display:column>
                  <display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
                     <c:if test="${TriggerObject.status=='BLOCKED'}">
                       <%--  <a href="planePause.do?name=${TriggerObject.name}" name="pauseHref">暂停</a> --%>
                        <f:a alias="delJobPlaneDialog" href="planeDel.do?name=${TriggerObject.name}" css="link del">删除</f:a>
                     </c:if>
                     <c:if test="${TriggerObject.status=='COMPLETE'}">
                      <%--    <a href="planePause.do?name=${TriggerObject.name}" name="pauseHref">暂停</a> --%>
                        <f:a alias="delJobPlaneDialog" href="planeDel.do?name=${TriggerObject.name}" css="link del">删除</f:a>
                     </c:if>
                     <c:if test="${TriggerObject.status=='ERROR'}">
                       <%--  <a  href="planeStart.do?name=${TriggerObject.name}" name="startHref" >启动</a> --%>
                        <f:a alias="delJobPlaneDialog" href="planeDel.do?name=${TriggerObject.name}" css="link del">删除</f:a>
                     </c:if>
                     <c:if test="${TriggerObject.status=='NONE'}">
                        <f:a alias="delJobPlaneDialog" href="planeDel.do?name=${TriggerObject.name}" css="link del">删除</f:a>
                     </c:if>
                     <c:if test="${TriggerObject.status=='NORMAL'}">
                          <a href="planePause.do?name=${TriggerObject.name}" name="pauseHref">暂停</a>
                        <f:a alias="delJobPlaneDialog" href="planeDel.do?name=${TriggerObject.name}" css="link del">删除</f:a>
                     </c:if>
                     <c:if test="${TriggerObject.status=='PAUSED'}">
                          <a  href="planeStart.do?name=${TriggerObject.name}" name="startHref" >启动</a>
                       <f:a alias="delJobPlaneDialog" href="planeDel.do?name=${TriggerObject.name}" css="link del">删除</f:a>
                     </c:if>
		             
                  </display:column>
			</display:table>
		</div> 			
	</div>
</body>
</html>


