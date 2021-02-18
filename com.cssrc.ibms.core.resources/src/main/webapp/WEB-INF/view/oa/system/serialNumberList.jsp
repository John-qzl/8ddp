<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>流水号生成管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js" charset="UTF-8"></script>	
	<script type="text/javascript">
	//导出流水号
	function exportXml(){	
		var tableIds = ImportExportXml.getChkValue('pk');
		var url=__ctx + "/oa/system/serialNumber/exportXml.do?tableIds="+tableIds;
		if (tableIds  ==''){
			$.ligerDialog.confirm('还没有选择，是否导出全部？','提示信息',function(rtn) {
				if(rtn) {
					var form=new com.ibms.form.Form();
					form.creatForm("form", url);
					form.submit();
				}
			});
			
		}else{
		$.ligerDialog.confirm('确认导出吗？','提示信息',function(rtn) {
			if(rtn) {
				var form=new com.ibms.form.Form();
				form.creatForm("form", url);
				form.submit();
			}
		});
		}
	}
	//导入流水号
	function importXml(){
		var url=__ctx + "/oa/system/serialNumber/import.do";
		ImportExportXml.showModalDialog({url:url,title:'导入流水号'});
	}
	</script>
</head>
<body>		
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">流水号生成管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					
					<div class="group">
					<a onclick="exportXml()"  class="link export">导出</a>
					</div>
	   				
					<div class="group">
					<a onclick="importXml()"  class="link import">导入</a>
					</div>
					
			        <div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
						<ul class="row plat-row">
								<li><span class="label">名称:</span><input type="text" name="Q_name_SL"  class="inputText" value="${param['Q_name_SL']}"/></li>
								<li><span class="label">别名:</span><input type="text" name="Q_alias_SL"  class="inputText" value="${param['Q_alias_SL']}"/></li>
						</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
				<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table name="serialNumberList" id="serialNumberItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
						  	<input type="checkbox" class="pk" name="id" value="${serialNumberItem.id}">
					</display:column>
					<display:column property="name" title="名称" sortable="true" sortName="name"></display:column>
					<display:column property="alias" title="别名" sortable="true" sortName="alias"></display:column>
					<display:column property="rule" title="规则" sortable="true" sortName="REGULATION"></display:column>
					<display:column  title="生成类型" style="text-align:center" >
						<c:choose>
							<c:when test="${serialNumberItem.genType==1}">
								<span class="green">每天生成</span>
							</c:when>
							<c:when test="${serialNumberItem.genType==2}">
								<span class="green">每月生成</span>
							</c:when>
							<c:when test="${serialNumberItem.genType==3}">
								<span class="green">每年生成</span>
							</c:when>
							<c:otherwise>
								<span class="red">递增</span>
							</c:otherwise>
						</c:choose>
					</display:column>
					<display:column property="noLength" title="流水号长度" sortable="true" sortName="noLength"></display:column>
					<display:column property="initValue" title="初始值" sortable="true" sortName="initValue"></display:column>
				
					<display:column title="管理" media="html"  style="width:180px;text-align:center">
						<f:a alias="delSerialNo" href="del.do?id=${serialNumberItem.id}" css="link del">删除</f:a>
						<a href="edit.do?id=${serialNumberItem.id}" class="link edit">编辑</a>
						<a href="get.do?id=${serialNumberItem.id}" class="link detail">明细</a>
					</display:column>
				</display:table>
				<ibms:paging tableId="serialNumberItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


