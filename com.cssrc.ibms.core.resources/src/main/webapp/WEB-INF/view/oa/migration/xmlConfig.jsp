<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#createXml').click(function() {
			//$.ligerDialog.waitting('正在导入中,请稍候...');
			if (checkAndDeal()) {
				$('#dataform').submit();
			}
			//$.ligerDialog.closeWaitting();
		});
	})
	function checkAndDeal() {
		var rtn = check();
		if (rtn.flag) {
			var action = $('#dataform').attr("action");
			var type = $('#type :checked').val();
			var version = $('#version :checked').val();
			var xmltype = $('#xmlType :checked').val();
			if (action.indexOf("type") == -1) {
				action += "?type=" + type;
				action += "&version=" + version;
				action += "&xmltype=" + xmltype;
			}
			$('#dataform').attr('action', action);
			return true
		} else {
			$.ligerDialog.error(rtn.msg);
			return false;
		}
	}
	function check() {
		var rtn = {
			flag : false,
			msg : ""
		}
		var schemaFile = $('#schemaFile').val();
		if ($.isEmpty(schemaFile)) {
			rtn.msg = "请上传文件！";
			return rtn;
		}
		if (schemaFile.indexOf('.schema') == -1) {
			rtn.msg = "上传的文件必须是schema类型！";
			return rtn;
		}
		rtn.flag = true;
		return rtn;
	}
	function getVersion(obj) {

	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="createXml">生成Xml文件</a>
					</div>
					
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="dataform" action="creatXml.do" onsubmit="checkAndDeal()"
				method="POST" enctype="multipart/form-data">
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0"
						border="0">
						<tr>
							<td>数据迁移类型：</td>
							<td><select id="type" name="type"
								onchange="getVersion(this);">
									<c:forEach items="${typeList}" var="type">
										<option value="${type.value}">${type.name}</option>>
						</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>版本：</td>
							<td><select id="version" name="version">
									<c:forEach items="${versionList}" var="version">
										<option value="${version.value}">${version.name}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>xml类型：</td>
							<td><select id="xmlType" name="xmlType">
									<c:forEach items="${xmlTypeList}" var="xmlType">
										<option value="${xmlType.value}">${xmlType.name}</option>>
						</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>schema：</td>
							<td><input type="file" size="40" name="schemaFile"
								id="schemaFile" /></td>
						</tr>
						<!-- 			<tr>
				<td>defXml：</td>
				<td><input type="file" size="40" name="defXmlFile"
					id="defXmlFile" /></td>
			    </tr> -->
			    						<tr>
							<td colspan='2'>
							<div>
							</br></br></br>
<textarea readOnly="readOnly" style="width:80%;align:left;height:100px">
表单模板、表单设计批量生成使用说明:
  1.选择生成的xml类型，选择在业务表结构中使用的schema文件;
  2.如果选择的是表单模板，在表单管理-表单模板中使用批量导入功能。
  3.如果选择的是表单设计（含业务数据模板），在表单管理-表单设计中使用批量导入功能。						
</textarea>
							</div>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>	
</body>
</html>