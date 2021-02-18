<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>维度信息管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerWindow.js" ></script>
	<script type="text/javascript" >
		function openDialog(demId){
			var url="${ctx }/oa/system/sysOrgType/list.do?demId="+demId;
			url=url.getNewUrl();	
			DialogUtil.open({
				height:540,
				width: 800,
				title : '维度的组织类型管理',
				url: url, 
				isResize: true
			});
		}
	</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">维度信息管理列表
				</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add"  href="edit.do?demId=0">添加</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
							<ul class="row plat-row">
										<li><span class="label">维度名称:</span><input type="text" name="Q_demName_SL"  class="inputText" value="${param['Q_demName_SL']}"/></li>
									
										<li><span class="label">维度描述:</span><input type="text" name="Q_demDesc_SL"  class="inputText" value="${param['Q_demDesc_SL']}"/></li>
							</ul>
					</form>
			</div>
		</div>
		<div class="panel-body">
			
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="demensionList" id="demensionItem" requestURI="list.do" 
		    	sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">
					  	<input type="checkbox" class="pk" name="demId" value="${demensionItem.demId}" ${demensionItem.demName=="行政维度"?"disabled='disabled'":""}>
				</display:column>
				<display:column property="demName" title="维度名称" sortable="true" sortName="demName" style="text-align:left"></display:column>
				<display:column property="demDesc" title="维度描述" sortable="true" sortName="demDesc" style="text-align:left"></display:column>
				<display:column title="管理" media="html" style="width:180px;white-space:nowrap;" >
				
				<c:choose>
				<c:when test='${demensionItem.demName eq "行政维度"}'>
					<a class="link setting" onclick="openDialog(${demensionItem.demId})">类型管理</a>
				</c:when>
				<c:otherwise>
					<a href='del.do?demId=${demensionItem.demId}'  class='link del'>删除</a>
					<a href='edit.do?demId=${demensionItem.demId}' class="link edit">编辑</a>
					<a class="link setting" onclick="openDialog(${demensionItem.demId})">类型管理</a>
				</c:otherwise>
				</c:choose>
					
				</display:column>
			</display:table>
			<ibms:paging tableId="demensionItem"/>
			
		</div>			
	</div>
</body>
</html>


