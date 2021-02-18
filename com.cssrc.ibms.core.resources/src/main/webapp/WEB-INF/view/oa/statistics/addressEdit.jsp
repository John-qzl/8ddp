<%--
	访问路径
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 访问路径</title>
<%@include file="/commons/include/form.jsp"%>
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=address"></script>
<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${address.addressId==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			
			$("a.save").click(function() {
				$('#addressForm').submit(); 
			});
		});
		function showResponse(responseText){
			var json=eval("("+responseText+")");
			if(json.result==1){
				if(json.operate=='add'){
					$.ligerDialog.confirm('添加访问路径成功,继续添加吗?','提示信息',function(rtn){
						if(rtn){
							$("#url,#alias,#addressDesc").val("");
						}
						else{
							location.href="${ctx}/oa/statistics/address/list.do?toolId=${address.toolId}";							
						}
					});
				}
				else{
					$.ligerDialog.success('编辑访问路径成功!','提示信息');
				}
			}
			else{
				$.ligerDialog.err('出错信息',"编辑访问路径失败",json.message);
			}
		}
	</script>
</head>
<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label"> <c:if
							test="${address.addressId==null }">添加url</c:if> <c:if
							test="${address.addressId!=null }">编辑url</c:if>
					</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" href="javascript:;">保存</a>
						</div>
						
						<div class="group">
							<a class="link back" href="list.do?toolId=${address.toolId}">返回</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">

			<form id="addressForm" method="post" action="save.do">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<th width="20%">路径别名:</th>
						<td><input style="width:800px" type="text" id="alias" name="alias"
							value="${address.alias}" class="inputText" /></td>						
					</tr>
					<tr>
						<th width="20%">访问路径:</th>
						<td><input style="width:800px" type="text" id="url" name="url"
							value="${address.url}" class="inputText" /></td>
					</tr>
					<tr>
						<th width="20%">访问路径说明:</th>
						<td><textarea  style="width:800px" rows="3" cols="50" id="addressDesc"
								name="addressDesc">${address.addressDesc}</textarea></td>
					</tr>
				</table>
				<input type="hidden" name="addressId" value="${address.addressId}" />
				<input type="hidden" name="toolId" value="${address.toolId}" />
			</form>
		</div>
	</div>
</body>
</html>
