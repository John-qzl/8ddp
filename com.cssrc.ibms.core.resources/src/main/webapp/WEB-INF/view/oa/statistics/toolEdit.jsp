<%--
	统计工具
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 统计工具</title>
<%@include file="/commons/include/form.jsp"%>
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=tool"></script>
<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${tool.toolId==null}){
				valid(showRequest,showResponse,1);
				$("#name").blur(function(){
					var obj=$(this);
					autoPingin(obj);
				});
			}else{
				valid(showRequest,showResponse);
			}
			
			$("a.save").click(function() {
				$('#toolForm').submit(); 
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
					$.ligerDialog.confirm('添加统计工具成功,继续添加吗?','提示信息',function(rtn){
						if(rtn){
							$("#name,#alias,#toolDesc").val("");
						}
						else{
							location.href=ctx +"/oa/statistics/tool/edit.do?toolId="+ json.toolId;
						}
					});
				}
				else{
					$.ligerDialog.success('编辑统计工具成功!','提示信息');
				}
			}
			else{
				$.ligerDialog.err('出错信息',"编辑统计工具失败",json.message);
			}
		}
	</script>
</head>
<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label"> <c:if test="${tool.toolId==null }">添加工具</c:if>
						<c:if test="${tool.toolId!=null }">编辑工具</c:if>
					</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" href="javascript:;">保存</a>
						</div>
						
						<div class="group">
							<a class="link back" href="list.do">返回</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="toolForm" method="post" action="save.do">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<th width="20%">工具名称:</th>
						<td><input type="text" id="name" name="name"
							value="${tool.name}" class="inputText" /></td>
					</tr>
					<tr>
						<th width="20%">别名:</th>
						<td><input type="text" id="alias" name="alias"
							value="${tool.alias}" class="inputText" /></td>
					</tr>
					<tr>
						<th width="20%">说明:</th>
						<td><textarea rows="3" cols="50" id="toolDesc"
								name="toolDesc">${tool.toolDesc}</textarea></td>
					</tr>
				</table>
				<input type="hidden" name="toolId" value="${tool.toolId}" />
			</form>
		</div>
	</div>
</body>
</html>
