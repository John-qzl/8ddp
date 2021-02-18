<%--
	desc:edit the 表单角色表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 表单角色表</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=recRole"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ScriptDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/record/RecRoleEdit.js"></script>
	<script type="text/javascript">
		$(function() {
			//初始化角色过滤条件
			__RecRole__.init();			
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${recRole.roleId==null}){
				valid(showRequest,showResponse,1);
				$("#roleName").blur(function(){
					var obj=$(this);
					autoPingin(obj);
				});
			}else{
				valid(showRequest,showResponse);
			}
			
			$("a.save").click(function() {
				saveChange();
				$('#recRoleForm').submit(); 
			});
		});
		function saveChange(){			
			//角色过滤条件字段
			var filterField= JSON2.stringify(getFilterJson($(".table-detail td[var=filter]"),4));
			$('#filterField').val(filterField);
			$('input#filter').val(filterField);
		}
		function getFilterJson(filter,flag){
			var json = [];
			json.push(getFilter(filter,4));
			return json;
		}
		
		function getFilter(filterTd,s){
			var obj={};
			obj.s = s;
			obj.type =$("[var='right']",filterTd).val();
			obj.id =$("[var='rightId']",filterTd).val();
			obj.name =$("[var='rightName']",filterTd).val();
			obj.script =$("[var='rightScript']",filterTd).val();
			return obj;
		}
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
							$("#roleName,#alias,#roleDesc").val("");
						}
						else{
							location.href="${ctx}/oa/system/recRole/list.do?typeId="+ json.typeId;
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
					<c:if test="${recRole.roleId==null }">添加表单角色</c:if>
					<c:if test="${recRole.roleId!=null }">编辑表单角色</c:if>  
					</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
						
						<div class="group"><a class="link back" href="list.do?typeId=${typeId}">返回</a></div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			
				<form id="recRoleForm" method="post" action="save.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">角色名称: </th>
							<td><input type="text" id="roleName" name="roleName" value="${recRole.roleName}"  class="inputText"/></td>
						</tr>
						<tr>
							<th width="20%">角色别名: </th>
							<td><input type="text" id="alias" name="alias" value="${recRole.alias}"  class="inputText"/></td>
						</tr>
						
						<tr>
							<th width="20%">备注: </th>
							<td>
								<textarea rows="3" cols="50" id="roleDesc" name="roleDesc">${recRole.roleDesc}</textarea>
						</tr>
						<tr>
							<th width="20%">允许删除: </th>
							<td>
								<input type="radio" id="allowDel" name="allowDel" value="1" checked="checked" <c:if test="${recRole.allowDel==1}"> checked="checked" </c:if> />允许
								<input type="radio" id="allowDel" name="allowDel" value="0"  <c:if test="${recRole.allowDel==0}"> checked="checked" </c:if> >不允许
							</td>
						</tr>
						<tr>
							<th width="20%">允许编辑: </th>
							<td>
							    <input type="radio" id="allowEdit" name="allowEdit" value="1" checked="checked" <c:if test="${recRole.allowEdit==1}"> checked="checked" </c:if> />允许
								<input type="radio" id="allowEdit" name="allowEdit" value="0"  <c:if test="${recRole.allowEdit==0}"> checked="checked" </c:if> >不允许
							</td>
						</tr>
						<tr>
							<th width="20%">允许记录权限设置: </th>
							<td>
							    <input type="radio" id="allowSet" name="allowSet" value="1" checked="checked" <c:if test="${recRole.allowSet==1}"> checked="checked" </c:if> />允许
								<input type="radio" id="allowSet" name="allowSet" value="0"  <c:if test="${recRole.allowSet==0}"> checked="checked" </c:if> >不允许
							</td>
						</tr>
						<tr style="display:none">
							<th width="20%">是否启用: </th>
							<td>
							    <input type="radio" id="status" name="status" value="1" checked="checked" <c:if test="${recRole.status==1}"> checked="checked" </c:if> />启用
								<input type="radio" id="status" name="status" value="0"  <c:if test="${recRole.status==0}"> checked="checked" </c:if> >禁用
							</td>
						</tr>
						<tr >
							<th width="20%">记录角色列表中是否隐藏: </th>
							<td>							
								<input type="radio" id="isHide" name="isHide" value="0"  checked="checked"<c:if test="${recRole.isHide==0}"> checked="checked" </c:if> >不隐藏
								<input type="radio" id="isHide" name="isHide" value="1"  <c:if test="${recRole.isHide==1}"> checked="checked" </c:if> />隐藏
							</td>
						</tr>
						<tr >
							<th width="20%">角色人员信息 </th>
							<td var="filter">
							</td>
						</tr>
					</table>
					<input type="hidden" name="roleId" value="${recRole.roleId}" />
					<input type="hidden" name="typeId" value="${typeId}" />
					<input type="hidden" id="filter" name="filter" value="" />
					<textarea style="display:none;" id="filterField" name="filterField" >${fn:escapeXml(recRole.filter)}</textarea>
				</form>
			
		</div>
</div>
</body>
</html>
