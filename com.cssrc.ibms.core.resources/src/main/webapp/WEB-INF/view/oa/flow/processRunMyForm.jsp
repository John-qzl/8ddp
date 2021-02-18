<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的草稿</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	function checkFormChange(runId,defId){
		var height=screen.availHeight*0.7;
		var width=screen.availWidth*0.7;
		var url="checkForm.do?runId="+runId;
		$.post(url,function(result){
			if(result){
				$.ligerDialog.confirm('草稿表单版本发生变化时候加载旧版本数据?','提示信息',function(rtn){
					if(rtn){
						//jQuery.openFullWindow('${ctx}/oa/flow/task/startFlowForm.do?defId='+defId)
						var url= '${ctx}/oa/flow/task/startFlowForm.do?runId='+runId;
						TaskStartFlowForm({height:height, width:width, url:url});
					}else{
						var url= '${ctx}/oa/flow/task/startFlowForm.do?runId='+runId+'&isNewVersion=1';
						//jQuery.openFullWindow('${ctx}/oa/flow/task/startFlowForm.do?defId='+defId+'&isNewVersion=1')
						TaskStartFlowForm({height:height, width:width, url:url});
					}
					this.close();
				})
			}else{
				//jQuery.openFullWindow('${ctx}/oa/flow/task/startFlowForm.do?defId='+defId)
				var url= '${ctx}/oa/flow/task/startFlowForm.do?runId='+runId+'&isNewVersion=1';
				TaskStartFlowForm({height:height, width:width, url:url});
			}
		})
	}
	function copyDraft(runId){
		$.post("copyDraft.do?runId="+runId,function(data){
			var obj=new com.ibms.form.ResultMessage(data);
			if(obj.isSuccess()){
				$.ligerDialog.success(obj.getMessage(),"提示信息",function(rtn){
					if(rtn){
						window.location.reload();
					}
				});
			}else{
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		});
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">我的草稿列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><f:a alias="delDraft" css="link del" action="delDraft.do" showNoRight="false">删除</f:a></div>
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
					
				</div>	 
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="myForm.do">
					<ul class="row plat-row">
					<li>
						<span class="label" style='overflow:inherit;'>流程定义名称:</span><input type="text" name="Q_processName_SL"  class="inputText" value="${param['Q_processName_SL']}"/>
					</li>
					<li>
						<span class="label" style='overflow:inherit;'>流程实例标题:</span><input type="text" name="Q_subject_SL"  class="inputText" value="${param['Q_subject_SL']}"/>
					</li>
					<div class="row_date">
					<li>
						<span class="label">创建时间 从:</span> <input  name="Q_begincreatetime_DL"  class="inputText date" value="${param['Q_begincreatetime_DL']}"/>
					</li>
					<li>
						<span class="label">至: </span><input  name="Q_endcreatetime_DG" class="inputText date" value="${param['Q_endcreatetime_DG']}">
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
		    <display:table name="processRunList" id="processRunItem" requestURI="myForm.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					  	<input type="checkbox" class="pk" name="runId" value="${processRunItem.runId}">
				</display:column>
				<display:column property="subject" title="流程实例标题"  sortable="true" sortName="subject" style="text-align:left"></display:column>
				<display:column property="processName" title="流程定义名称" sortable="true" sortName="processName" style="text-align:left"></display:column>
				<display:column property="creator" title="创建人" sortable="true" sortName="creator" style="text-align:left"></display:column>
				<display:column  title="创建时间" sortable="true" sortName="createtime">
					<fmt:formatDate value="${processRunItem.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
					<f:a alias="delDraft" href="delDraft.do?runId=${processRunItem.runId}" css="link del">删除</f:a>					
					<a href="javascript:;" onclick="checkFormChange('${processRunItem.runId}','${processRunItem.defId}')" class="link run">启动流程</a>
					<c:if test="${processRunItem.formDefId!=0}">
						<a href="javascript:;" onclick="copyDraft('${processRunItem.runId}')" class="link copy">复制</a>
					</c:if>
				</display:column>
			</display:table>
			<ibms:paging tableId="processRunItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


