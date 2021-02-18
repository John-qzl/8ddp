<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>新建表单</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="Aqua/css/ligerui-all.css"></f:link>

<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTree.js"></script>

<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/catCombo.js"></script>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=bpmFormDef"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormTableDialog.js"></script>

<script type="text/javascript">
	window.name="frmEdit";
	var designType="table_create",dType=${designType};
	
	$(function(){
		function showRequest(formData, jqForm, options) {
			return true;
		}
		valid(showRequest,showResponse);		
		$("#dataFormSave").click(function(){
			var rtn=$('#bpmFormDefForm').valid();
			if(!rtn) return;
			//'表单类型'必须填写
			var fieldTypeName=$("#typeName").val();
			if(!fieldTypeName || fieldTypeName =='0'){
					$.ligerDialog.error("请选择表单类型","提示信息");
					return;
			}
			//'表单别名'必须填写
			var fieldFormAlias=$("#formAlias").val();
			if(!fieldFormAlias || fieldFormAlias =='0'){
					$.ligerDialog.error("表单别名不能为空","提示信息");
					return;
			}
			//选择要生成的表
			if(designType=='table_create'){
				var tableId=$("#tableId").val();
				if(!tableId){
					$.ligerDialog.error("请您选择要生成的表","提示信息");
					return;
				}
				$("#bpmFormDefForm").attr("action","selectTemplate.do");
			}
			else{
				$("#bpmFormDefForm").attr("action","chooseDesignTemplate.do");
			}
			$('#bpmFormDefForm')[0].submit();
		});
	
		if(dType==1){
			$("#table_tr").hide();
			designType='editor_design';
		}
			
	});
	
	function selectTable(){
		var callBack=function(tableId,tableName){		
			$("#tableId").val(tableId);
			$("#tableName").val(tableName);
		}
		FormTableDialog({callBack:callBack});
	}
	function resetTable(){
		$("#tableId").val('');
		$("#tableName").val('');
	};
	function designTypeChange(){
		var val = $("input:radio[name='designType']:checked").attr("id");
		if(!val)return;
		designType = val;
		if(designType=="editor_design"){
			$("#table_tr").hide();
		}
		else{
			$("#table_tr").show();
		}
	};
	//根据注释生成表名
	function autoGetTableKey(inputObj){
		var url=__ctx + '/oa/form/formDef/getTableAlias.do' ;
		var subject=$(inputObj).val();
		if($.trim(subject).length<1) return;
		$.post(url,{'subject':subject},function(response){
			var json=eval('('+response+')'); 
			if(json.result==1 ){
				if($.trim(	$('#formAlias').val()).length<1){
					$('#formAlias').val(json.message);
				};
			}else{
				$.ligerDialog.error(json.message,"提示");
			}
		 });
	};
</script>

</head>
<body >
	<div class="panel">
	
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
					<a class="link run" id="dataFormSave" href="####">下一步</a>
					</div>
				</div>
			</div>
		</div>
	

		<div class="panel-body">
			<form  id="bpmFormDefForm" method="post" action="selectTemplate.do" >
				 
				 <table cellpadding="1" cellspacing="1" class="table-detail">
					<tr>
						<th width="150">表单标题:</th> 
						<td><input id="subject" type="text" name="subject" class="inputText" size="30" value="${subject}" onblur="autoGetTableKey(this)"/></td>
					</tr>
					<tr>
						<th width="150">表单别名:</th>
						<td>
							<input id="formAlias" type="text" name="formAlias" class="inputText" size="30" value="${formAlias}" />
						</td>
					</tr>
					<tr>
						<th width="150">表单类型:</th>
						<td>
							<input class="catComBo" catKey="FORM" valueField="categoryId" catValue="${categoryId}" id="typeName"  name="typeName" height="120" width="200"/>
						</td>
					</tr>
					<tr>
						<th width="150">备注:</th>
						<td>
							<textarea rows="3" cols="35" id="formDesc" name="formDesc" class="textarea">${formDesc}</textarea>
						</td>
					</tr>
					<tr style="display: none;">
						<th width="150">设计类型:</th>
						<td>
							<input id="table_create"  onclick="designTypeChange()" name="designType" type="radio" <c:if test="${designType==0}"> checked="checked"</c:if> /><label for="table_create">通过表生成</label>
							<input id="editor_design" onclick="designTypeChange()" name="designType" type="radio" <c:if test="${designType==1}"> checked="checked"</c:if>  /><label for="editor_design">编辑器设计</label>
						</td>
					</tr>
					<tr id="table_tr">
						<th width="150">主表:</th>
						<td>
							<div class="input-content">
								<input type="text" id="tableName" class="inputText" name="tableName" value="" readonly="readonly" style="width:60%;">
								<input type="hidden" id="tableId" name="tableId" value="">
								<a href='####' class='link search ryxzq-a'  onclick="selectTable()" ></a>
								<a href='####' class='link redo ryxzq-a' style='margin-left:10px;' onclick="resetTable()"></a>
							</div>	
						</td>
					</tr>
				</table>
				
			</form>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


