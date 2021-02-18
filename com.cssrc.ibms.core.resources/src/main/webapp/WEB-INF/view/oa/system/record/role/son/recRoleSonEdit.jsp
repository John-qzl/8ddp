<%--
	desc:edit the 表单角色表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 记录角色表</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=recRoleSon"></script>
	<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options){ 
				return true;
			} 
			if(${recRoleSon.roleSonId==null}){
				valid(showRequest,showResponse,1);
				$("#roleSonName").blur(function(){
					var obj=$(this);
					autoPingin(obj);
				});
			}else{
				valid(showRequest,showResponse);
			}
			
			$("a.save").click(function() {
				$('#recRoleSonForm').submit(); 
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
					$.ligerDialog.confirm('添加角色成功,继续添加吗?','提示信息',function(rtn){
						if(rtn){
							$("#roleSonName,#alias,#roleSonDesc").val("");
						}
						else{
							location.href="${ctx}/oa/system/recRoleSon/list.do?";
						}
					});
				}
				else{
					$.ligerDialog.success('编辑角色成功!','提示信息');
				}
			}
			else{
				$.ligerDialog.err('出错信息',"编辑角色失败",json.message);
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
					<c:if test="${recRoleSon.roleSonId==null }">添加记录角色</c:if>
					<c:if test="${recRoleSon.roleSonId!=null }">编辑记录角色</c:if>  
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
			
				<form id="recRoleSonForm" method="post" action="save.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">角色名称: </th>
							<td><input type="text" id="roleSonName" name="roleSonName" value="${recRoleSon.roleSonName}"  class="inputText"/></td>
						</tr>
						<tr>
							<th width="20%">角色别名: </th>
							<td><input type="text" id="alias" name="alias" value="${recRoleSon.alias}"  class="inputText"/></td>
						</tr>						
						<tr>
							<th width="20%">备注: </th>
							<td>
								<textarea rows="3" cols="50" id="roleSonDesc" name="roleSonDesc">${recRoleSon.roleSonDesc}</textarea>
						</tr>					
					</table>
					<input type="hidden" name="roleSonId" value="${recRoleSon.roleSonId}" />
					<input type="hidden" name="typeId" value="${recRoleSon.typeId}" />
					<input type="hidden" name="dataTemplateId" value="${recRoleSon.dataTemplateId}" />
					<input type="hidden" name="dataId" value="${recRoleSon.dataId}" />
					<input type="hidden" name="typeName" value="${recRoleSon.typeName}" />
				</form>
			
		</div>
</div>
</body>
</html>
