<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/get.jsp" %>
<title>流程代理设置列表</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/UserInfo.js"></script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">流程代理管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form class="plat-from" id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
					<div class="row_date">
					<li>
						<span class="label" title="代理开始时间从:" >代理开始时间从:</span><input  name="Q_beginstartdate_DL"  class="inputText datePicker"  datetype="1" />
					</li>
					<li>
						<span class="label">到:</span><input  name="Q_endstartdate_DG"  class="inputText datePicker"  datetype="2" />
					</li>
					</div>
					<li>
						<span class="label">启用状态:</span>
							<select name="Q_enabled_N">
								<option value="">全部</option>
								<option value="0">禁用</option>
								<option value="1">启用</option>
							</select>
					</li>
					<div class="row_date">
					<li>
						<span class="label">结束时间&nbsp;从:</span><input  name="Q_beginenddate_DL"  class="inputText datePicker"  datetype="1" />
					</li>
					<li>
						<span class="label">到 :</span> <input  name="Q_endenddate_DG"  class="inputText datePicker"  datetype="2" />
					</li>
					</div>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="agentSettingList" id="agentSettingItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${agentSettingItem.id}">
				</display:column>
				<display:column titleKey="授权类型" sortable="true" sortName="authtype">
					<c:choose>
						<c:when test="${agentSettingItem.authtype==0}">全权</c:when>
						<c:when test="${agentSettingItem.authtype==1}">部分</c:when>
						<c:when test="${agentSettingItem.authtype==2}">条件</c:when>
						
					</c:choose>
				</display:column>
				
				<display:column  title="开始时间" sortable="true" sortName="startdate">
					<fmt:formatDate value="${agentSettingItem.startdate}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column  title="结束时间" sortable="true" sortName="enddate">
					<fmt:formatDate value="${agentSettingItem.enddate}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column title="流程名称" sortable="true"  property="flowname" sortName="flowname"></display:column>
				<display:column  title="是否启用" sortable="true" sortName="enabled">
					<c:choose>
						<c:when test="${agentSettingItem.enabled==0}">禁用</c:when>
						<c:otherwise >启用</c:otherwise>
					</c:choose>
				</display:column>
				
				<display:column  title="代理人" sortable="true" sortName="agent">
					<a href="javascript:userDetail('${agentSettingItem.agentid}');">${agentSettingItem.agent}</a>
				</display:column>
				<display:column titleKey="创建时间" sortable="true" sortName="createtime">
					<fmt:formatDate value="${agentSettingItem.createtime}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column titleKey="管理" media="html" style="width:180px;line-height:21px;">
					<a href="del.do?id=${agentSettingItem.id}" class="link del">删除</a>
					<a href="edit.do?id=${agentSettingItem.id}" class="link edit">编辑</a>
				<%--
					<a href="get.do?id=${agentSettingItem.id}" class="link detail">明细</a>
					 --%>
				</display:column>
			</display:table>
			<ibms:paging tableId="agentSettingItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


