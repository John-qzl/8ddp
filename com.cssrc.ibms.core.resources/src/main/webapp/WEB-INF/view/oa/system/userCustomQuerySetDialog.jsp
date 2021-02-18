<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>导出字段设置</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript">
var dialog = frameElement.dialog;
var querySetInfo = '${querySetInfo}';
$(function(){
	var arr = $.parseJSON(querySetInfo);
	var tBodyObj = $("#field tbody");
	for(var i=0;i<arr.length;i++){
		var obj = arr[i];
		var name = obj.name;
		var checked = obj.checked;
		var desc = obj.desc;
		var trObj = new StringBuffer();
		trObj.append("<tr var=\""+name+"\">");
		trObj.append("<td style=\"text-align:center\" var=\"index\">"+(i+1)+"</td>");
		trObj.append("<td style=\"text-align:center\" var=\"desc\">"+desc+"</td>");
		if(checked){
			trObj.append("<td style=\"text-align:center\" var=\"checked\"><input type=\"checkbox\"  checked=\"checked\" ></td>");
		}else{
			trObj.append("<td style=\"text-align:center\" var=\"checked\"><input type=\"checkbox\" ></td>");
		}
		tBodyObj.append(trObj.toString());
	}
	
});
function selectField(){
	var querySetInfoRtn =[];
	var arr = $.parseJSON(querySetInfo);
	for(var i=0;i<arr.length;i++){
		var objRtn = {};
		var colName = arr[i].name;
		var trObj = $("tr[var="+colName+"]");
		var desc = trObj.find("td[var=desc]").text().trim();
		var checked = trObj.find("td[var=checked] input").attr("checked");
		if($.isEmpty(checked)){
			checked = false;
		}else{
			checked = true;
		}
		objRtn ={name:colName,desc:desc,checked:checked};
		querySetInfoRtn.push(objRtn);
	}
	var obj={querySetInfo:querySetInfoRtn};
	try{
		dialog.get("sucCall")(obj);
	}catch(e){
		$.ligerDialog.warn("回调错误！");
	}
	dialog.close();
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">查询字段设置 </span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="btnSearch" onclick="selectField()" >保存</a>
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
								<th style="text-align:center" >序号<span class="required"></span>
								</th>
								<th style="text-align:center">字段信息<span class="required"></span>
								</th>
								<th style="text-align:center">是否显示<span class="required"></span>
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