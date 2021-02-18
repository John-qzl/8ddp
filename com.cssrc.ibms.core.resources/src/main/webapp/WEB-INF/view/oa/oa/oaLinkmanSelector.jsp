<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>联系人管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bus/BusQueryRuleUtil.js" ></script>
</head>
<body>
	<div class="panel">
		<c:if test="${!empty busQueryRule.filterList && fn:length(busQueryRule.filterList) >1}">
			<div class="l-tab-links">
				<ul style="left: 0px; ">
					<c:forEach items="${busQueryRule.filterList}" var="filter">
						<li tabid="${filter.key}" <c:if test="${busQueryRule.filterKey ==filter.key}"> class="l-selected"</c:if>>
							<a href="list.do?__FILTERKEY__=${filter.key}" title="${filter.name}">${filter.desc}</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">联系人管理列表</span>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="selector.do">
					<ul class="row plat-row">
					<li>
						<span class="label">姓名:</span><input type="text" name="Q_name_SL"  class="inputText" value="${param['Q_name_SL']}" />
						<span class="label">邮箱:</span><input type="text" name="Q_email_SL"  class="inputText" value="${param['Q_email_SL']}" />
						&nbsp;<a href="javascript:;" onclick="$('#searchForm').submit()" class='button'><span>查询</span></a>
					</li>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="oaLinkmanList" id="oaLinkmanItem" requestURI="list.do?__FILTERKEY__=${busQueryRule.filterKey}&__FILTER_FLAG__=${busQueryRule.filterFlag}" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
				  		<input type="checkbox" class="pk" name="linkmandata" value="${oaLinkmanItem.id}#${oaLinkmanItem.name}#${oaLinkmanItem.email}#${oaLinkmanItem.company}">
					</display:column>
					<display:column property="name" title="姓名" sortable="true" sortName="NAME"></display:column>
					<display:column property="email" title="邮箱" sortable="true" sortName="EMAIL"></display:column>
					<display:column property="company" title="公司" sortable="true" sortName="COMPANY"></display:column>
					<display:column property="phone" title="电话" sortable="true" sortName="PHONE" ></display:column>
				    <div position="bottom"  class="bottom" style="margin-top:10px;"></div>	
			</display:table>
			<ibms:paging tableId="oaLinkmanItem"/>
		</div><!-- end of panel-body -->
			
	</div> <!-- end of panel -->
</body>
</html>


