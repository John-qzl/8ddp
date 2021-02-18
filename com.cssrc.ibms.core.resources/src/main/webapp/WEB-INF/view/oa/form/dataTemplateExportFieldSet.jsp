<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>导出字段设置</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript">
var dialog = frameElement.dialog;
var colSetting = '${colSetting}';
var colList = $.parseJSON('${colList}');
$(function(){
	var obj = $.parseJSON(colSetting);
	var tBodyObj = $("#field tbody");
	for(var i=0;i<colList.length;i++){
		var colName = colList[i];
		var colObj = obj[colName];
		var checked = colObj.checked;
		var desc = colObj.desc;
		var trObj = new StringBuffer();
		trObj.append("<tr var=\""+colName+"\">");
		trObj.append("<td var=\"index\">"+(i+1)+"</td>");
		trObj.append("<td var=\"desc\">"+desc+"</td>");
		if(checked){
			trObj.append("<td var=\"checked\"><input type=\"checkbox\"  checked=\"checked\" ></td>");
		}else{
			trObj.append("<td var=\"checked\"><input type=\"checkbox\" ></td>");
		}
		tBodyObj.append(trObj.toString());
	}
	
});
function selectField(){

	var colSettingInfo ={};
	for(var i=0;i<colList.length;i++){
		var colName = colList[i];
		var trObj = $("tr[var="+colName+"]");
		var desc = trObj.find("td[var=desc]").text().trim();
		var checked = trObj.find("td[var=checked] input").attr("checked");
		if($.isEmpty(checked)){
			checked = false;
		}else{
			checked = true;
		}
		colSettingInfo[colName]={desc:desc,checked:checked};
	}
	var obj={colSettingInfo:colSettingInfo};
	try{
		dialog.get("sucCall")(obj);
	}catch(e){
		$.ligerDialog.warn("选择用户回调错误！");
	}
	dialog.close();
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">导出字段设置 </span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="btnSearch" onclick="selectField()" >确定</a>
					</div>
					
					<div class="group">
						<a class="link del" onclick="dialog.close()">关闭</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="field" method="post">
				<div  title="field">
					<table class="table-detail" cellpadding="0" cellspacing="0"
						border="0">
						<thead>
							<tr>
								<th>序号<span class="required"></span>
								</th>
								<th>字段信息<span class="required">*</span>
								</th>
								<th>是否导出<span class="required">*</span>
								</th>
							</tr>
						<thead>
						<tbody>
							
						</tbody>
					</table>
				</div>
			</form>

		</div>
	</div>
</body>
</html>