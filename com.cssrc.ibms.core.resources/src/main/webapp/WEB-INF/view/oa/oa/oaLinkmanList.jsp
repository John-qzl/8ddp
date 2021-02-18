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
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					<div class="group" style="float: right;">
						<f:a onclick="BusQueryRuleUtil.saveFilter({tableName:'${busQueryRule.tableName}',filterKey:'${busQueryRule.filterKey}',filterFlag:'${busQueryRule.filterFlag}'})" alias="saveFilter_${busQueryRule.tableName}" css="link save"  showNoRight="false">保存条件</f:a>
						<f:a onclick="BusQueryRuleUtil.myFilter({tableName:'${busQueryRule.tableName}',url:'${busQueryRule.url}'})" alias="myFilter_${busQueryRule.tableName}" css="link ok"  showNoRight="false">过滤器</f:a>
						<f:a onclick="BusQueryRuleUtil.eidtDialog({tableName:'${busQueryRule.tableName}'})" alias="customQuery_${busQueryRule.tableName}" css="link setting" showNoRight="false" >高级查询</f:a>
					</div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do?__FILTERKEY__=${busQueryRule.filterKey}&__IS_QUERY__=0">
					<div class="row">
						<span class="label">姓名:</span><input type="text" name="Q_name_SL" value="${param['Q_name_SL']}" class="inputText" />
						<span class="label">性别:</span>
						<select name = "Q_sex_S">
						   <option value="男" <c:if test="${param['Q_sex_S']=='男'}">selected</c:if>>男</option>
						   <option value="女" <c:if test="${param['Q_sex_S']=='女'}">selected</c:if>>女</option>
						</select>
						<span class="label">电话:</span><input type="text" name="Q_phone_SL" value="${param['Q_phone_SL']}" class="inputText" />
						<span class="label">邮箱:</span><input type="text" name="Q_email_SL"  value="${param['Q_email_SL']}" class="inputText" />
						<span class="label">公司:</span><input type="text" name="Q_company_SL"  value="${param['Q_company_SL']}" class="inputText" />
						<span class="label">工作:</span><input type="text" name="Q_job_SL" value="${param['Q_job_SL']}" class="inputText" />
						<span class="label">地址:</span><input type="text" name="Q_address_SL" value="${param['Q_address_SL']}"  class="inputText" />
						<span class="label">创建时间 从:</span> <input  name="Q_begincreatetime_DL" value="${param['Q_begincreatetime_DL']}"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endcreatetime_DG" value="${param['Q_endcreatetime_DG']}"  class="inputText date" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="oaLinkmanList" id="oaLinkmanItem" requestURI="list.do?__FILTERKEY__=${busQueryRule.filterKey}&__FILTER_FLAG__=${busQueryRule.filterFlag}" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<f:col name="id">
					<display:column title="${checkAll}" media="html" style="width:30px;">
				  		<input type="checkbox" class="pk" name="id" value="${oaLinkmanItem.id}">
					</display:column>
				</f:col>
				<f:col name="name">
					<display:column property="name" title="姓名" sortable="true" sortName="NAME"></display:column>
				</f:col>
				<f:col name="sex">
					<display:column property="sex" title="性别" sortable="true" sortName="SEX"></display:column>
				</f:col>
				<f:col name="phone">
					<display:column property="phone" title="电话" sortable="true" sortName="PHONE"></display:column>
				</f:col>
				<f:col name="email">
					<display:column property="email" title="邮箱" sortable="true" sortName="EMAIL"></display:column>
				</f:col>
				<f:col name="company">
					<display:column property="company" title="公司" sortable="true" sortName="COMPANY"></display:column>
				</f:col>
				<f:col name="createtime">
					<display:column  title="创建时间" sortable="true" sortName="CREATETIME">
						<fmt:formatDate value="${oaLinkmanItem.createtime}" pattern="yyyy-MM-dd"/>
					</display:column>
				</f:col>
				<f:col name="status">
					<display:column  title="状态" sortable="true" ><c:if test="${oaLinkmanItem.status==1}">启用</c:if><c:if test="${oaLinkmanItem.status==0}">禁用</c:if></display:column>
				</f:col>
				<display:column title="管理" media="html" style="width:220px">
					<a href="del.do?id=${oaLinkmanItem.id}" class="link del">删除</a>
					<a href="edit.do?id=${oaLinkmanItem.id}" class="link edit">编辑</a>
					<a href="get.do?id=${oaLinkmanItem.id}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="oaLinkmanItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


