
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>添加 数据字典</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=dictionary"></script>
	<script type="text/javascript">
		/*KILLDIALOG*/
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	
		var isAdd=${isAdd};
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			valid(showRequest,showResponse);
			$("a.save").click(function() {
				$('#dictionaryForm').submit(); 
			});
			
			function showResponse(responseText){
				var obj=new com.ibms.form.ResultMessage(responseText);
				if(obj.isSuccess()){//成功
					if(isAdd==1){
						$("#itemName,#itemKey,#itemValue").val("");
						$.ligerDialog.success("添加字典成功!",'提示信息',function(){
							dialog.close();
						});
					}else{
						$.ligerDialog.success("编辑字典成功!",'提示信息',function(){
							dialog.close();
						});
					}
					var conf=dialog.get("conf");
					if(conf.callBack){
						conf.callBack();
					}
				}
				else{
					$.ligerDialog.err('出错信息',"保存字典信息失败",obj.getMessage());
				}
			}
		});
		
		function autoGetDictionaryKey(inputObj){
			var url=__ctx + '/oa/system/dictionary/getDictionaryKey.do';
			var subject=$(inputObj).val();
			if($.trim(subject).length<1) return;
			$.post(url,{'subject':subject},function(response){
				var json=eval('('+response+')');
					
						$('#itemKey').val(json.message);
				
			 });
		}
		
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">
					<c:choose><c:when test="${isAdd==1}">添加数据字典</c:when><c:otherwise>编辑数据字典</c:otherwise></c:choose>  
				</span>  
			</div>
			<c:if test="${canReturn==0}">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
				</div>
			</div>
			</c:if>
		</div>
		<div class="panel-body">
			<div class="panel-detail">
				<form id="dictionaryForm" method="post" action="save.do">
					<table border="0" cellspacing="0" cellpadding="0" class="table-detail">
						<tr>
							<th width="25%">项名:<span class="required">*</span></th>
							<td ><input type="text" id="itemName" name="itemName" value="${dictionary.itemName}" onblur="autoGetDictionaryKey(this)" class="inputText"/></td>
						</tr>	
						<tr>
							<th width="25%">字典关键字:<span class="required">*</span></th>
							<td ><input type="text" id="itemKey" name="itemKey" value="${dictionary.itemKey}" class="inputText"/>
							</td>
						</tr>				
						<tr>
							<th width="25%">项值:<span class="required">*</span></th>
							<td ><input type="text" id="itemValue" name="itemValue" value="${dictionary.itemValue}"   class="inputText"/></td>
						</tr>
					
					</table>
					<input type="hidden" name="dicId" value="${dictionary.dicId}">
					<input type="hidden" name="typeId" value="${dictionary.typeId}">
					<input type="hidden" name="parentId" value="${dictionary.parentId}">
					<input type="hidden" name="type" value="${dictionary.type}">
					
				</form>
			</div>
		</div>
</div>
</body>
</html>
