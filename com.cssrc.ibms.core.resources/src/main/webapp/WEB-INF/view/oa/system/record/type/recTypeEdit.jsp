<%--
	表单类别表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 表单类别</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=recType"></script>
	<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${recType.typeId==null}){
				valid(showRequest,showResponse,1);
				$("#typeName").blur(function(){
					var obj=$(this);
					autoPingin(obj);
				});
			}else{
				valid(showRequest,showResponse);
			}
			
			$("a.save").click(function() {
				$('#recTypeForm').submit(); 
			});
		});
		function autoPingin(obj){
			var value=obj.val();
			if(value!=""&&value!=undefined){
				Share.getPingyin({
					input:value,
					postCallback:function(data){
						$("#alias").val(data.output);
					}
				});
			}
		}
		function showResponse(responseText){
			var json=eval("("+responseText+")");
			if(json.result==1){
				if(json.operate=='add'){
					$.ligerDialog.confirm('添加功能类别成功,继续添加吗?','提示信息',function(rtn){
						if(rtn){
							$("#typeName,#alias,#typeDesc").val("");
						}
						else{
							location.href=ctx +"/oa/system/recType/get.do?typeId="+ json.typeId;
						}
					});
				}
				else{
					$.ligerDialog.success('编辑功能类别成功!','提示信息');
				}
			}
			else{
				$.ligerDialog.err('出错信息',"编辑功能类别失败",json.message);
			}
		}
	</script>
</head>
<body>
<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">
					<c:if test="${recType.typeId==null }">添加类别</c:if>
					<c:if test="${recType.typeId!=null }">编辑类别</c:if>  
					</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
						
						<div class="group"><a class="link back" href="list.do">返回</a></div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			
				<form id="recTypeForm" method="post" action="save.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">表单类型名称: </th>
							<td><input type="text" id="typeName" name="typeName" value="${recType.typeName}"  class="inputText"/></td>
						</tr>
						<tr>
							<th width="20%">表单类型别名: </th>
							<td><input type="text" id="alias" name="alias" value="${recType.alias}"  class="inputText"/></td>
						</tr>						
						<tr>
							<th width="20%">表单类型描述: </th>
							<td>
								<textarea rows="3" cols="50" id="typeDesc" name="typeDesc">${recType.typeDesc}</textarea>
						</tr>
					</table>
					<input type="hidden" name="typeId" value="${recType.typeId}" />
				</form>
			
		</div>
</div>
</body>
</html>
