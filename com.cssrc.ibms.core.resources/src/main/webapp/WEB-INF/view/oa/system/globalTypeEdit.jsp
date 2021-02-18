<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>添加分类</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=globalType"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
	<script type="text/javascript">
		var dialog;
		
		//判断是否为dialog弹出框
		if(frameElement!=undefined&&frameElement!=null){
			dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		}
		
		var isAdd=${isAdd};
		function showRequest(formData, jqForm, options) { 
			return true;
		} 
		  
		$(function() {
			valid(showRequest,showResult);
			$("a.save").click(function(){
				var key=$("#nodeKey").val();
			/* 	var reg = new RegExp("^[0-9]*$");
				if(key!=''&&reg.test(key)){
					$.ligerDialog.error("请输入非数字","提示信息");
				}else */ 
				if(key.indexOf(',')!=-1){
					$.ligerDialog.error("不能输入符号类字符","提示信息");
				}else{
					$("#globalTypeForm").submit();
					
				}
			});
			
			$("#typeName").change(function(){   
				 getFullSpell($(this).val());  				   
			  }); 
		});
		
		function getFullSpell(typeName){
			if($("#nodeKey").val()!="") return;
// 			var url="${ctx}/oa/system/globalType/getPingyin.do";
// 			var params={typeName:typeName};
// 			$.post(url,params,function(result){
// 				$("#nodeKey").val(result); 
// 			});
			
// 			var value=obj.val();
			Share.getPingyin({
				input:typeName,
				postCallback:function(data){
					$("#nodeKey").val(data.output);
				}
			});
		}
		//保存后回调函数
		function showResult(responseText){
			
			var obj=new com.ibms.form.ResultMessage(responseText);
			if(!obj.isSuccess()) {
				$.ligerDialog.warn(obj.getMessage(),'提示信息');
				return;
			}else{
				if(dialog!=undefined&&dialog!=null){
					dialog.get("sucCall")(obj);
				}
			};
			if(isAdd){
				$.ligerDialog.confirm("添加成功，继续添加吗?",'提示信息',function(rtn){
					if(rtn){
						$("#typeName").val('');
						$("#nodeKey").val('');
					}else{
						if(dialog!=undefined&&dialog!=null){
							dialog.close();
						}
					}
				});
			}else{
				$.ligerDialog.success("修改分类成功!",'提示信息',function(){
					if(dialog!=undefined&&dialog!=null){
						dialog.close();
					}
				});
			}
		}
	</script>
</head>
<body>
	<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label"><c:choose><c:when test="${isAdd}">添加分类</c:when><c:otherwise>编辑分类</c:otherwise></c:choose></span>
				</div>
				<div class="panel-toolbar">  
					<div class="toolBar">
						<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
					</div>
				</div>
		</div>
		<form id="globalTypeForm" method="post" action="save.do">
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<c:if test="${isAdd}">
								<tr>
									<th width="20%">父节点: </th>
									<td >${parentName}</td>
								</tr>
							</c:if>
							<tr>
								<th width="20%">名称:  <span class="required">*</span></th>
								<td ><input type="text" id="typeName" name="typeName" value="${globalType.typeName}"  class="inputText"/></td>
							</tr>
							<tr>
								<th width="20%">节点Key:  <span class="required">*</span></th>
								<td ><input type="text" id="nodeKey" name="nodeKey" value="${globalType.nodeKey}"  class="inputText"/></td>
							</tr>
							<c:if test="${isDict}">
							<tr>
								<th width="20%">字典类型:</th>
								<td>
									<input type="radio" name="type" value="0"  <c:if test="${globalType.type==0}">checked="checked"</c:if>  />平铺
									<input type="radio" name="type" value="1" <c:if test="${globalType.type==1}">checked="checked"</c:if> />树形
								</td>
							</tr>
							</c:if>
					</table>
				</div>
				<input type="hidden" id="isRoot" name="isRoot" value="${isRoot}"/>
				<input type="hidden" id="parentId" name="parentId" value="${parentId}"/>
				<input type="hidden" id="typeId" name="typeId" value="${globalType.typeId}" />
				<input type="hidden" id="dataId" name="dataId" value="${globalType.dataId}" />
		</form>
</body>
</html>