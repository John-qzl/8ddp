<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript">

	$(function(){
	 	$('#save').click(function(){
			if(checkAndDeal()){
				$('#dataform').submit();
			}
		});
	})
	function checkAndDeal() {
		var rtn = check();
		if (rtn.flag) {
			var url = '${ctx}/oa/migration/deal.do';
			var type = $('#type').val();
			var version = $('#version').val();
			url += "?type=" + type;
			url += "&version=" + version;
			$('#dataform').attr('action', url);
			return true;
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
</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">	
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="save">导入表结构</a>
					</div>
					
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="dataform" method="POST" onsubmit="checkAndDeal()" enctype="multipart/form-data">
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0"border="0">
						<tr>
							<td>数据迁移类型：</td>
							<td>
								<select id="type" name="type" onchange="getVersion(this);">
									<c:forEach items="${typeList}" var="type">
									     <option  value="${type.value}">${type.name}</option>>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>版本：</td>
							<td>
								<select id="version" name="version">
									<c:forEach items="${versionList}" var="version">
									     <option  value="${version.value}">${version.name}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>schema：</td>
							<td><input type="file" size="40" name="schemaFile"
								id="schemaFile" /></td>
						</tr>
						<!-- <tr>
							<td>defXml：</td>
							<td><input type="file" size="40" name="defXmlFile"
								id="defXmlFile" /></td>
						</tr> -->
						
						<tr>
							<td colspan='2'>
							<div>
							</br></br></br>
<textarea readOnly="readOnly" style="width:80%;align:left;height:100px">
数据迁移使用说明:
  1.在数据迁移-业务表结构中，选择TDM项目schema文件，点击提交。
  2.处理完成后，返回日志；查看日志中是否有出错信息；有错误，根据日志上的错误提示，进行修改。
  3.在表单管理-业务表定义，对生成的表进行查看，调整；如确认无误后，进行批量生成物理表。							
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