<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>职务表管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
var type ="";
var typeVal="";
var scope =eval("("+dialog.get("scope")+")"); 
if(scope){
	type = scope.type;
	typeVal = scope.value; 
}
var isSingle='${param.isSingle}';

//选择按钮
function  selectJob(){

	var pleaseSelect= "请选择职务!";
	var aryJob;
	if(isSingle=='true'){
		aryJob = $('#jobItem').find(":input[name='jobid'][checked]");
		if(aryJob.length>1){
			alert("请选择一条数据");
			return;
		}
	}else{
		aryJob = $('#jobItem').find(":input[name='jobid'][checked]");
	}
	if(aryJob.length==0){
		alert("请选择职务");
		return;
	}
	var aryId=[];
	var aryName=[];
	var jobJson = [];
	aryJob.each(function(){
		var jobid=$(this).val();
		var jobName=$(this).parent().next()[0];
		jobName=jobName.innerHTML;
		aryId.push(jobid);
		aryName.push(jobName);
		jobJson.push({id:jobid,name:jobName});
	});
	var jobIds=aryId.join(",");
	var jobNames=aryName.join(",");
	
	var obj={};
	obj.jobId=jobIds;
	obj.jobName=jobNames;
	obj.jobJson = jobJson;
	
	dialog.get("sucCall")(obj);
	dialog.close();
}

</script>




</head>
<body>
	<div class="panel"> 
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">职务表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link search" id="btnSearch">
							
							查询
						</a>
					</div>
					
					<div class="group">
						<a class="link reset" onclick="$.clearQueryForm()">
							
							重置
						</a>
					</div>
				</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="selector.do">
					<div class="row">
						<span class="label">名称:</span>
						<input type="text" name="Q_jobname_SL" class="inputText" value="${param['Q_jobname_SL']}" />
						<span class="label">代码:</span>
						<input type="text" name="Q_jobcode_SL" class="inputText" value="${param['Q_jobcode_SL']}" />
						<span class="label">描述:</span>
						<input type="text" name="Q_jobdesc_SL" class="inputText" value="${param['Q_jobdesc_SL']}" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
			<display:table name="jobList" id="jobItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="jobid" value="${jobItem.jobid}">
				</display:column>
				<display:column property="jobname" title="职务名称" sortable="true" sortName="JOBNAME"></display:column>
				<display:column property="jobcode" title="代码" sortable="true" sortName="JOBCODE"></display:column>
				<display:column property="grade" title="级别" sortable="true" sortName="GRADE"></display:column>

				<display:column title="管理" media="html" style="width:220px">
					<a href="get.do?jobid=${jobItem.jobid}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="jobItem" />
		</div>
		<!-- end of panel-body -->				
			<div position="bottom" class="bottom" style='margin-top: -56px;'>
				<a href='javascript:;' class='button' onclick="selectJob()" style="margin-right: 10px;">
					<span class="icon ok"></span>
					<span>选择</span>
				</a>
				<a href="javascript:;" class="button" onclick="clearJob()">
					<span class="icon cancel"></span>
					<span class="cance">清空</span>
				</a>
				<a href='javascript:;' class='button' style='margin-left: 10px;' onclick="dialog.close()">
					<span class="icon cancel"></span>
					<span>取消</span>
				</a>
		   </div>
	</div>
	<!-- end of panel -->
</body>
</html>


