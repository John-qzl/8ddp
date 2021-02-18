<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 报表模板</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=reportTemplate"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>

<script type="text/javascript">
function setFileName(name){
	$("#reportlocation").val(name);
}
$(function(){
	function showRequest(formData, jqForm, options) { 
		return true;
	}
	if(${reportTemplate.reportid ==null}){
		valid(showRequest,showResponse,1);
	}else{
		valid(showRequest,showResponse);
	}
	
	$("a.save").click(function() {
		var path = $("#reportlocation").val();
		var strs=path.split(".");
		var extName = strs[strs.length-1];
		console.log(strs);
		console.log(extName);
		if(path==''){
			$.ligerDialog.warn("请上传模板文件");
		}else if(!(extName=='cpt'||extName=='jasper')){
			$.ligerDialog.warn("请选择*.cpt或*.jasper报表模板文件");
		}else{
			$("#reportTemplateForm").submit();
		}
	});
	var navtab=$("#tabMyInfo").ligerTab({});
	if(${setparam==1}){
		navtab.selectTabItem("paramsetting");
	}
});
function editParam(paramid){
	if(!paramid){
		var trCheckeds=$(":checkbox[name='paramid'][checked]");
		if(trCheckeds.length<1){
			$.ligerDialog.warn("请选择单条进行编辑");
			return;
		}else if(trCheckeds.length>1){
			$.ligerDialog.warn("已经选择了多项，请选择单条进行编辑");
			return;
		}
		paramid=trCheckeds.val();
	}
	ReportAddParamDialog({paramid:paramid,edit:true,reportid:"${reportTemplate.reportid}",callback:function(obj){
		location.reload();
	}});
}

function addParam(){
	ReportAddParamDialog({reportid:"${reportTemplate.reportid}",edit:true,callback:function(obj){
		location.reload();
	}});
}
function detailParam(paramid){
	ReportAddParamDialog({paramid:paramid,edit:false,reportid:"${reportTemplate.reportid}",callback:function(obj){
		location.reload();
	}});
}
function delParam(paramid){
	if(!paramid){
		var trCheckeds=$(":checkbox[name='paramid'][checked]");
		if(trCheckeds.length<1){
			$.ligerDialog.warn("请选择记录进行操作");
			return;
		}
		paramid=trCheckeds.val();
	}else{
		$(":checkbox[name='paramid'][value='"+paramid+"']").attr("checked",true)
	}
	$.ligerDialog.confirm( "您确定要删除记录吗？","提示信息",function(rtn){
		if(rtn){
			$("#reportParamForm").attr("action","${ctx}/oa/system/reportParam/del.do")
			$("#reportParamForm").ajaxSubmit({
				success:function(responseText, statusText){
					var json = eval('(' + responseText + ')');
					if(json.result){
						location.reload();
					}else{
						$.ligerDialog.alert(json.message,"删除失败");
					}
				}
			});
		}
	});
}

</script>
</head>
<body>
	<div id="tabMyInfo" class="panel-nav" style="overflow: hidden; position: relative;">
		<div title="基本信息" tabid="reportdetail">
			<div class="panel-top">
				<div class="tbar-title">
					<c:choose>
						<c:when test="${reportTemplate.reportid !=null }">
							<span class="tbar-label">编辑报表模板</span>
						</c:when>
						<c:otherwise>
							<span class="tbar-label">添加报表模板</span>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" href="javascript:;">
								
								保存
							</a>
						</div>
						
						<div class="group">
							<a class="link back" href="list.do">
								
								返回
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-body">
				<form id="reportTemplateForm" method="post" action="save.do" enctype="multipart/form-data">
					<input type="hidden" id="createtime" name="createtime" value="${f:longDate(reportTemplate.createtime)}">
					<input type="hidden" id="reporttype" name="reporttype" value="${reportTemplate.reporttype}">
					<input type="hidden" id="reporttype" name="reportid" value="${reportTemplate.reportid}" />

					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">
								标题:
								<span class="required">*</span>
							</th>
							<td>
								<input type="text" id="title" name="title" value="${reportTemplate.title}" class="inputText" />
							</td>
						</tr>
						<tr>
							<th width="20%">描述:</th>
							<td>
								<textarea rows="5" cols="60" id="descp" name="descp" class="textarea">${reportTemplate.descp}</textarea>
							</td>
						</tr>
						<c:if test="${reportTemplate.reportid !=null }">
							<tr>
								<th width="20%">当前报表模板路径:</th>
								<td>${reportTemplate.reportlocation}</td>
							</tr>
						</c:if>
						<tr>
							<th width="20%">
								报表模板路径:
								<span class="required">*</span>
							</th>
							<td>
								<input type="text" id="reportlocation" style="width: 500px" name="reportlocation" value="${reportTemplate.reportlocation}">
						</tr>

						<tr>
							<th width="20%">报表类型:</th>
							<td>
								<select id="typeid" name="typeid">
									<c:forEach var="type" items="${typelist}">
										<c:if test="${type.parentId!=0}">
											<c:if test="${reportTemplate.typeid==type.typeId}">
												<option selected="selected" value="${type.typeId}">${type.typeName}</option>
											</c:if>
											<c:if test="${reportTemplate.typeid!=type.typeId}">
												<option value="${type.typeId}">${type.typeName}</option>
											</c:if>
										</c:if>
									</c:forEach>
								</select>
							</td>
						</tr>

					</table>

				</form>
			</div>
		</div>
		<c:if test="${reportTemplate.reportid!=null}">
			<div title="参数设置" tabid="paramsetting">
				<div class="panel-top">
					<div class="panel-toolbar">
						<div class="toolBar">
							
							<div class="group">
								<a class="link add" href="javascript:addParam();">
									
									添加
								</a>
							</div>
							
							<div class="group">
								<a class="link update" id="btnUpd" href="javascript:editParam();">
									
									修改
								</a>
							</div>
							
							<div class="group">
								<a class="link del" href="javascript:delParam();">
									
									删除
								</a>
							</div>
						</div>
					</div>
				</div>
				<form id="reportParamForm" method="post">
					<display:table name="reportParamList" id="reportParamItem" sort="external" cellpadding="1" cellspacing="1" export="false" class="table-grid">
						<display:column title="${checkAll}" media="html" style="width:30px;">
							<input type="checkbox" class="pk" name="paramid" value="${reportParamItem.paramid}">
						</display:column>
						<display:column property="reportid" title="所属报表" sortable="false"></display:column>
						<display:column property="name" title="参数名称" sortable="false"></display:column>
						<display:column property="descp" title="描述" sortable="false"></display:column>
						<display:column property="value_" title="缺省值" sortable="false"></display:column>
						<display:column property="paramtype" title="类型" sortable="false"></display:column>
						<display:column title="管理" media="html" style="width:180px;text-align:center">
							<a href="javascript:delParam(${reportParamItem.paramid});" class="link del">删除</a>
							<a href="javascript:editParam(${reportParamItem.paramid});" class="link edit">编辑</a>
							<a href="javascript:detailParam(${reportParamItem.paramid});" class="linkdetail">明细</a>
						</display:column>
					</display:table>
				</form>
			</div>
		</c:if>
	</div>
</body>
</html>
