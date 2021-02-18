<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>导出关联表选择</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript">
var dialog = frameElement.dialog;
var mainRelTables = ${mainRelTables};
var rel = mainRelTables.rel;//获得关联表的整个json数组
var relLength = eval(rel).length;//获得关联表的长度
$(function(){
	var tBodyObj = $("#field tbody");
	for(var i=0; i<relLength; i++){
		var relTableDesc = mainRelTables.rel[i].desc;
		var relTbleId = mainRelTables.rel[i].ID;
		var relTableName = mainRelTables.rel[i].name;
		var trObj = new StringBuffer();
		trObj.append("<tr>");
		trObj.append("<td>"+(i+1)+"</td>");
		trObj.append("<td>"+relTableDesc+"</td>");
		trObj.append("<td><input type=\"radio\" name=\"radio\" value=\""+relTbleId+","+relTableName+","+relTableDesc+"\"></td>");
		trObj.append("</tr>");
		tBodyObj.append(trObj.toString());
	}
	
});

function selectRelTable(){
	var val = $('input:radio[name="radio"]:checked').val();
	if(val==null){
		alert("请选中一张关联表!");
		return false;
	}else{

		var arr = val.split(',');
		var relTableId = arr[0];
		var relTableName = arr[1];
		var relTableDesc = arr[2];
		generateExcel(relTableId,relTableName,relTableDesc);
	}
	dialog.close();
}

function generateExcel(relTableId,relTableName,relTableDesc){
	
	$.ajax({		
		type : "post",
		url : "/ibms/oa/form/dataTemplate/exportRelTable.do",
		data : {displayId:'${displayId}',id:relTableId,name:relTableName,relTableDesc:relTableDesc},
		success : function(){
			
		}
	});
}

</script>
</head>
<body>
	<div tableid="baseSetting" id="table" title="主表">

		<div class="panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">导出关联表选择 </span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="btnSearch" onclick="selectRelTable()">确定</a>
						</div>

						<div class="group">
							<a class="link del" onclick="dialog.close()">关闭</a>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-body">
				<form id="field" method="post">
					<div title="field">
						<table class="table-detail" cellpadding="0" cellspacing="0"
							border="0">
							<thead>
								<tr>
									<th>序号<span class="required"></span>
									</th>
									<th>关联表名<span class="required">*</span>
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
	</div>
</body>
</html>