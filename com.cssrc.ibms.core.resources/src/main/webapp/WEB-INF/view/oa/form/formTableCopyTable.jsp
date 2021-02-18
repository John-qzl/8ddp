
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<%@include file="/commons/include/get.jsp" %>
<title>自定义表复制</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	dialog.set('title','自定义表复制');
	var valid;
	$(function(){
		valid=$("#bpmFormTableCopy").form();
		
	});
	
	function save() {
		var rtn = valid.valid();
		if (!rtn){
			return;			
		}
		var jsonObj=[],json=[],isTableName=[];
		$("input[name='tableName']").each(function(){
			var me = $(this),tableName = me.val(),obj ={};
			obj.tableId = me.attr("tableid");
			obj.tableName = tableName ;
			jsonObj.push(obj);
			isTableName.push(tableName);

		});
		$("input[name='tableDesc']").each(function(){
			var me = $(this),tableId = me.attr("tableid"),tableDesc = me.val(),obj ={};
			obj.tableDesc = tableDesc;
			for(var i=0,c;c=jsonObj[i++];){
				if(c.tableId == tableId){
					$.extend(c,obj)
					json.push(c);
				}
			}	
		});
		
		var url = "saveCopy.do";
		var para ={json:JSON2.stringify(json),addSync:"${addSync}"};
		$.ligerDialog.waitting('正在保存中,请稍候...');
		$.post(url, para, function(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {//成功
				$.ligerDialog.closeWaitting();
				$.ligerDialog.success(obj.getMessage(), '提示', function() {
					dialog.get('sucCall')("/");
					dialog.close();
				});
			} else {//失败
				$.ligerDialog.closeWaitting();
				$.ligerDialog.error(obj.getMessage(),'提示');
			}
		});
	}
	
	
	//根据注释生成表名
	function autoGetTableKey(inputObj,origObj){
		var url=__ctx + '/oa/form/formTable/getTableKey.do' ;
		var subject=inputObj.value,origObj = $('#'+origObj);
		if($.trim(subject).length<1) return;
		$.post(url,{'subject':subject,'tableId':''},function(response){
			var json=eval('('+response+')');
			if(json.result==1 ){
				if($.trim(	origObj.val()).length<1){
					origObj.val(json.message);
				};
			}else{
				$.ligerDialog.error(json.message,'提示');
			}
		 });
	}
	
	</script>
</head>
<body style="overflow-x:hidden;">
  <div class="hide-panel">
	<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">自定义表复制</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" href="####" id="btnSave" onclick="save()">保存</a></div>
					
					<div class="group"><a class="link del" href="####" onclick="javasrcipt:dialog.close()">关闭</a></div>
				
				</div>	
			</div>
	</div>
  </div>	
	<div class="panel-body">
		<form id="bpmFormTableCopy" class="plat-form" name="bpmFormTableCopy" method="post" >
			<div class="row">	 
			<input id="tableId" type="hidden" value="${bpmFormTable.tableId}" />
			<input id="addSync" type="hidden" value="${addSync}" />
			<fieldset style="margin: 5px 0px 5px 0px;" zone="team" >
					<legend>
						<span>主表  ${bpmFormTable.tableDesc}</span>
					</legend>
							
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th width="15%">原注释:</th>
						<td width="30%"><input type="text" class="inputText" disabled="disabled" value="${bpmFormTable.tableDesc}"  /></td>
						<th width="15%">新注释:</th>
						<td><input type="text" class="inputText"  id="tableDesc" name="tableDesc" tableid="${bpmFormTable.tableId}"  validate="{required:true}" onblur="autoGetTableKey(this,'tableName')" /></td>
					</tr>
					<tr>
						<th width="15%">旧表名:</th>
						<td><input type="text" class="inputText" disabled="disabled"  value="${bpmFormTable.tableName}" /></td>
						<th width="15%">新表名<span class="required">*</span>:</th>
						<td><input type="text" disabled="disabled" id="tableName" name="tableName" validate="{required:true,fields:true}" tableid="${bpmFormTable.tableId}"  class="inputText" value="${bpmFormTable.tableName}_sync"/></td>
					</tr>
				</table>	
			</fieldset>				
			<c:if test="${isSubTable}" >
				<c:forEach items="${bpmFormTable.subTableList}" var="subTableItem" varStatus="v"  >
				
					<fieldset style="margin: 5px 0px 5px 0px;" zone="team" >
							<legend>
								<span>子表${subTableItem.tableDesc}</span>
							</legend>
							<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<th width="15%">旧注释:</th>
									<td width="30%"><input type="text" class="inputText" disabled="disabled" value="${subTableItem.tableDesc}" /></td>
									<th width="15%">新注释:</th>
									<td><input type="text" class="inputText" id="tableDesc" name="tableDesc" validate="{required:true}" tableid="${subTableItem.tableId}"   onblur="autoGetTableKey(this,'subTableName${v.index}')"   /></td>
								</tr>
								<tr>
									<th width="15%">旧表名:</th>
									<td><input type="text" class="inputText" disabled="disabled"  value="${subTableItem.tableName}" /></td>
									<th width="15%">新表名<span class="required">*</span>:</th>
									<td><input type="text" disabled="disabled" id="subTableName${v.index}" name="tableName" validate="{required:true,fields:true}"  tableid="${subTableItem.tableId}" class="inputText" value="${subTableItem.tableName}_sync" /></td>
								</tr>
							</table>	
					</fieldset>
				</c:forEach>
			</c:if>
			</div>
	    </form>
	</div><!-- end of panel-body -->				
</body>
</html>