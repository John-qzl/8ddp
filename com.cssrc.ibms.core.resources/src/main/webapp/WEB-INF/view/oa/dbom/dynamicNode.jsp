<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>动态子节点数据设置</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
	var modelType="${modelType}";
	function ok(){
		var files="";
		var filesfm="";
		$(":checkbox:checked").each(function(i,ck){
			files+=$(ck).val()+",";
			if(modelType==1){
				filesfm+="W_"+$(ck).attr("fm")+"."+"F_"+$(ck).val()+",";
			}else{
				filesfm+=$(ck).attr("fm")+"."+$(ck).val()+",";
			}
		})
		if(files!=""){
			files=files.substring(0,files.length-1);
			filesfm=filesfm.substring(0,filesfm.length-1);
		}
		var retunrval=new Object();
		retunrval.files=files;
		retunrval.filesfm=filesfm;
		window.returnValue = retunrval;
		window.close();
	}
	function resetDataSource(){
		var retunrval=new Object();
		retunrval.reset=true;
		window.returnValue = retunrval;
		window.close();
	}
</script>
</head>
<body>

	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">报表列表管理</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link search" id="btnSearch">查询</a>
						</div>
						
						<div class="group">
							<a class="link del" href="javascript:;" onclick="ok()">确认</a>
						</div>
						
						<div class="group">
							<a href="javascript:;" class="link reset"
								onclick="resetDataSource();">重置动态子节点数据源</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			
			<display:table name="dataList" id="dataItem"
				requestURI="dynamicNode.do" sort="external" cellpadding="1"
				cellspacing="1" class="table-grid">
				<display:column media="html" style="width:30px;">
					<input type="checkbox" class="pk" value="${dataItem.fieldName}" fm="${dataItem.tableDesc}">
				</display:column>
				<display:column property="fieldName" title="字段名称" sortable="true"
					sortName="fieldName"></display:column>
				<display:column property="fieldDesc" title="字段描述" sortable="true"
					sortName="fieldDesc"></display:column>
				<display:column property="tableDesc" title="所属数据源" sortable="true"
					sortName="tableDesc"></display:column>
			</display:table>
			<ibms:paging tableId="dataItem" />
		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->

</body>
</html>

