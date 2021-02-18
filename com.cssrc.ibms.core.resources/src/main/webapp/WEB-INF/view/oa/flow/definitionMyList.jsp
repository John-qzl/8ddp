<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/commons/include/get.jsp" %>
<title>新建流程</title>
<script type="text/javascript" src="${ctx}/js/ibms/oa/bpm/FlowUtil.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/js/ibms/oa/bpm/SelectUtil.js" ></script>
</head>
<body>      
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a href="javascript:;" class="link reset" onclick="$.clearQueryForm();">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" class="plat-from" method="get" action="myList.do">
					<ul class="row plat-row">
						<input type="hidden" name="typeId" value="${param['typeId']}" title="流程分类ID"></input>
						<li>					
						<span class="label">流程名称:</span><input  type="text" name="Q_subject_SL"  class="inputText"  value="${param['Q_subject_SL']}"/>
						</li>
					</ul>
				</form>
			</div>
		</div>
		</div>
		<div class="panel-body">
			    <display:table name="bpmDefinitionList" id="bpmDefinitionItem" requestURI="myList.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
					<display:column titleKey="序号" media="html" style="width:20px;" >
						  	${bpmDefinitionItem_rowNum}
					</display:column>
					<display:column titleKey="流程名称" sortable="true" sortName="subject" >
						<a href="javaScript:void(0)"  onclick="FlowUtil.startFlow(${bpmDefinitionItem.defId},'${bpmDefinitionItem.actDefId}')" title="${bpmDefinitionItem.subject}" >${f:subString(bpmDefinitionItem.subject)}</a>	
					</display:column>
					<display:column titleKey="类型名称" sortable="true" sortName="typeId">
							${bpmDefinitionItem.typeName}
					</display:column>
					<display:column property="versionNo" titleKey="版本" sortable="true" sortName="versionNo" style="width:30px；"></display:column>
				</display:table>
				<ibms:paging tableId="bpmDefinitionItem"></ibms:paging>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


