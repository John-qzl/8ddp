<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 报表参数</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=dbom"></script>
<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${dbom.id ==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				$('#dbomForm').submit(); 
			});
		});
		function cancel(){
			var result=new Object();
			result.result=1;
			result.flush=true;
			window.returnValue=result;
			this.close();
		}
		function showResponse(responseText, statusText)  { 
			var self=this;
			var json = eval('(' + responseText + ')');
			if(json.result==1){//成功
				$.ligerDialog.confirm( json.message+",是否继续操作","提示信息",function(rtn){
					if(!rtn){
						cancel();
					}
					else{
						if(self.isReset==1){
							__valid.resetForm();
						}
					}
				});
				
		    }else{//失败
		    	$.ligerDialog.error(json.message,"出错了");
		    }
		} 
	</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<c:choose>
					<c:when test="${dbom.id!=null}">
						<span class="tbar-label">编辑dbom分类</span>
					</c:when>
					<c:otherwise>
						<span class="tbar-label">新增dbom分类</span>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="javascript:;">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="javascript:cancel();">取消</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="dbomForm" method="post" action="save.do">
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0"
						border="0">
						<tr>
							<th width="20%">代号: <span class="required">*</span></th>
							<td><input type="text" id="code" name="code"
								value="${dbom.code}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">名称:<span class="required">*</span></th>
							<td><input type="text" id="name" name="name"
								value="${dbom.name}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">描述: <span class="required">*</span></th>
							<td><input type="text" id="description" name="description"
								value="${dbom.description}" class="inputText" /></td>
						</tr>
					</table>
				</div>
				
				<input type="hidden" name="id" value="${dbom.id}" /> <input
					type="hidden" name="modifiedTime" value="${f:longDate(dbom.modifiedTime)}" />
				<input type="hidden" name="username" value="${dbom.username}" />
			</form>
		</div>
	</div>
</body>
</html>
