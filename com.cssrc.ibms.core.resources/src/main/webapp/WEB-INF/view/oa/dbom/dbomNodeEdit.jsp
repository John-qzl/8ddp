<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 报表参数</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript"
	src="${ctx}/servlet/ValidJs?form=dbomNode"></script>
<script type="text/javascript" src="${ctx}/js/dbom/dbomNode.js"></script>


<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${dbomNode.id ==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				$('#dbomForm').submit(); 
			});
			//数据源选择事件
			$("#dataSource").focus(function() {
				showDataSource();
				this.blur();
			});
			//动态子节点数据选择
			$("#dynamicNode").focus(function() {
				showDynamicNode();
				this.blur();
			});
			//动态子节点数据选择
			$("#url").focus(function() {
				showUrl();
				this.blur();
			});
		});

		function showResponse(responseText, statusText)  { 
			var self=this;
			var json = eval('(' + responseText + ')');
			if(json.result==1){//成功
				$.ligerDialog.confirm( json.message+",是否继续操作","提示信息",function(rtn){
					if(!rtn){
						location.reload();
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
					<c:when test="${dbomNode.id!=null}">
						<span class="tbar-label">编辑dbom节点信息</span>
					</c:when>
					<c:otherwise>
						<span class="tbar-label">新增dbom节点信息</span>
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
								value="${dbomNode.code}" class="inputText" style="width: 500px" /></td>
						</tr>
						<tr>
							<th width="20%">名称:<span class="required">*</span></th>
							<td><input type="text" id="name" name="name"
								value="${dbomNode.name}" class="inputText" style="width: 500px" /></td>
						</tr>
						<tr>
							<th width="20%">数据源:<span class="required">*</span></th>
							<td><input type="text" id="dataSource" name="dataSource"
								value="${dbomNode.dataSource}" class="inputText"
								style="width: 500px" /></td>
						</tr>
						<th width="20%">动态子节点:<span class="required">*</span></th>
						<td><input type="text" id="dynamicNode" name="dynamicNode"
							value="${dbomNode.dynamicNode}" class="inputText"
							style="width: 500px" /></td>
						</tr>
						<th width="20%">子节点自定义函数:<span class="required">*</span></th>
						<td><input type="text" id="dataFormat" name="dataFormat"
							value="${dbomNode.dataFormat}" class="inputText"
							style="width: 500px" /></td>
						</tr>
						<th width="20%">业务数据模板地址:<span class="required">*</span></th>
						<td><input type="text" id="url" name="url"
							value="${dbomNode.url}" class="inputText" style="width: 500px" /></td>
						</tr>
						<tr>
							<th width="20%">描述: <span class="required">*</span></th>
							<td><textarea style="width: 1239px; height: 144px;"
									autocomplete="off" id="description" name="description">
							${dbomNode.description}
							</textarea>
						</tr>
					</table>
				</div>

				<input type="hidden" name="id" value="${dbomNode.id}" /> 
				<input type="hidden" name="pcode" value="${dbomNode.pcode}" /> 
				<input type="hidden" name="nodeType" value="${dbomNode.nodeType}" />
				<input type="hidden" id="modelType" name="modelType"/>
			</form>
		</div>
	</div>
</body>
</html>
