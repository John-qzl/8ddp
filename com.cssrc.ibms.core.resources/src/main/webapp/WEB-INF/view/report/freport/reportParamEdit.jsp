<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 报表参数</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript"
	src="${ctx}/servlet/ValidJs?form=reportParam"></script>
<script type="text/javascript">
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${reportParam.paramid ==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				$('#reportParamForm').submit(); 
			});
		});
		function cancel(){
			var result=new Object();
			result.result=1;
			result.flush=true;
			
			var rtn=result;
			dialog.get("sucCall")(rtn);
			dialog.close();
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
					<c:when test="${!edit}">
						<span class="tbar-label">报表参数明细</span>
					</c:when>
					<c:when test="${reportParam.paramid !=null }">
						<span class="tbar-label">编辑报表参数</span>
					</c:when>
					<c:otherwise>
						<span class="tbar-label">添加报表参数</span>
					</c:otherwise>
				</c:choose>
			</div>
			<c:if test="${edit}">
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
			</c:if>
		</div>
		<div class="panel-body">
			<form id="reportParamForm" method="post" action="save.do">
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0"
						border="0">
						<tr>
							<th width="20%">所属报表: <span class="required">*</span></th>
							<td><input type="text" id="reportid" name="reportid"
								value="${reportParam.reportid}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">参数名称: <span class="required">*</span></th>
							<td><input type="text" id="name" name="name"
								value="${reportParam.name}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">缺省值:</th>
							<td><input type="text" id="value_" name="value_"
								value="${reportParam.value_}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">类型: <span class="required">*</span></th>
							<td><select id="paramtype" name="paramtype">
									<option value="text"
										<c:if test="${reportParam.paramtype=='text'}">selected="selected" </c:if>>文本</option>
									<option value="date"
										<c:if test="${reportParam.paramtype=='date'}"> selected="selected" </c:if>>时间</option>
							</select></td>
						</tr>
						<tr>
							<th width="20%">长度:</th>
							<td><input type="text" id="paramSize" name="paramSize"
								value="${reportParam.paramSize}" class="inputText"/></td>
						</tr>
					</table>
				</div>
				<input type="hidden" name="paramid" value="${reportParam.paramid}" />
			</form>
		</div>
	</div>
</body>
</html>
