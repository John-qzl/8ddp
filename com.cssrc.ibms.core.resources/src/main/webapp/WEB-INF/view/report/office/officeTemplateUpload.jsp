<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 报表参数</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=reportTemplate"></script>
<script type="text/javascript">
	//选择数据类，表作为数据源
	function showDataSource() {
		var url = "dataSource.do?tableId=${officeTemplate.tableId}";
		var conf = 'dialogHeight=800px; dialogWidth=800px;help=no;status=no;scroll';
		var rvalue = window.showModalDialog(url, null, conf);
		if (rvalue != undefined) {
			var tabs = rvalue.tabs;
			var tabids = rvalue.tabIds;
			$("#tableId").val(tabids);
			$("#dataEntry").removeAttr("disabled");
			$("#dataEntry option").remove();
			$(tabs).each(
					function(i, tab) {
						var html = "<option value='"+tab.ID+"'>" + tab.NAME_
								+ "</option>";
						$("#dataEntry").append(html);
					})
		}
	}
	$(function() {
		function showRequest(formData, jqForm, options) { 
			return true;
		}
		if(${officeTemplate.officeid ==null}){
			valid(showRequest,showResponse,1);
		}else{
			valid(showRequest,showResponse);
		}
		$("a.save").click(function() {
			if($("#office_file").val()!=null){
				$('#reportTemplateForm').submit(); 
			}else{
				$.ligerDialog.warn("请选择模板文件！");
			}
		});
	});
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">上传模板</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="javascript:;">
							
							保存
						</a>
					</div>
					
					<div class="group">
						<a class="link back" href="javascript:cancel();">
							
							取消
						</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="reportTemplateForm" method="post" action="saveUpload.do" enctype="multipart/form-data">
				<input type="hidden" id="officeid" name="officeid" value="${officeTemplate.officeid}" />
				<input type="hidden" id="filepath" name="filepath" value="${officeTemplate.filepath}" />
				<input type="hidden" id="tableId" name="tableId" value="${officeTemplate.tableId}" />
				<input type="hidden" id="typeId" name="typeId" value="${officeTemplate.typeId}" />
				<input type="hidden" id="allmarks" name="allmarks" />
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">
								标题:
								<span class="required">*</span>
							</th>
							<td width="80%">
								<input type="text" id="title" name="title" class="inputText" />
							</td>
						</tr>
						<tr>
							<th width="20%">
								描述:
								<span class="required">*</span>
							</th>
							<td width="80%">
								<textarea rows="5" cols="60" id="descp" name="descp" class="textarea">${reportTemplate.descp}</textarea>
							</td>
						</tr>
						<tr>
							<th width="20%">
								报表模板文件:
								<span class="required">*</span>
							</th>
							<td width="80%">
								<input id="office_file" name="office_file" type="file" />
							</td>
						</tr>
						<tr>
							<th width="20%">
								数据入口:
								<span class="required">*</span>
							</th>
							<td width="15%" nowrap="nowrap">
								<select id="dataEntry" name="dataEntry" style="width: 150px;">
									<c:forEach items="${tables}" var="item">
										<option value="${item.ID}" <c:if test="${officeTemplate.dataEntry==item.ID}">selected="selected"</c:if>>${item.NAME_}</option>
									</c:forEach>
								</select>
								<a href="javascript:;" onClick="showDataSource();">选择数据源</a>
								<font color="red" size="2px;">(只有通过该父数据类才能生成报告)</font>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
