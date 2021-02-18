<%--
	desc:edit the 职务表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 职务表</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="Aqua/css/ligerui-all.css" ></f:link>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/dicCombo.js"></script>
	
	<script type="text/javascript">
		$(function() {
			$("#jobname").blur(function(){
				var obj=$(this);
				//autoPingin(obj);
			});
			
			$("a.save").click(function() {
				$("#jobForm").attr("action","save.do");
				submitForm();
			});
		});
		//提交表单
		function submitForm(){
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#jobForm').form();
			frm.ajaxForm(options);
			if(frm.valid()){
				frm.submit();
			}
		}
		
		function showResponse(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.success(obj.getMessage(),"提示信息", function(rtn) {
					if(rtn){
						if(window.opener){
							window.opener.location.reload();
							window.close();
						}else{
							this.close();
							window.location.href="list.do";
						}
					}
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
		
		function autoPingin(obj){
			var value=obj.val();
			if(value!=""&&value!=undefined){
				Share.getPingyin({
					input:value,
					postCallback:function(data){
						$("#jobcode").val(data.output);
					}
				});
			}
		}
		//根据过滤名称生成过滤key
		function autoGetCode(inputObj){
			var url=__ctx + '/oa/form/formTable/getTableKey.do' ;
			var subject=$(inputObj).val();
			if($.trim(subject).length<1)
				return;
			$.post(url,{'subject':subject,'tableId':'${tableId}'},function(response){
				var json=eval('('+response+')'); 
				if(json.result==1 ){
					if($.trim(	$('#jobcode').val()).length<1){
						$('#jobcode').val(json.message);
					};
				}else{
					$.ligerDialog.error(json.message,"提示");
				}
			 });
		}
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
		    <c:choose>
			    <c:when test="${job.jobid !=null}">
			        <span class="tbar-label">编辑职务表</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label">添加职务表</span>
			    </c:otherwise>			   
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
				
				<div class="group"><a class="link back" href="list.do">返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="jobForm" method="post" action="save.do">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
				<tr>
					<th width="20%">职务名称: <span class="required">*</span></th>
					<td><input type="text" id="jobname" name="jobname" value="${job.jobname}" validate="{required:true,maxlength:100}" class="inputText" onblur="autoGetCode(this)"></td>
				</tr>
				<tr>
					<th width="20%">职务代码: </th>
					<td><input type="text" id="jobcode" name="jobcode" value="${job.jobcode}" validate="{required:false,maxlength:100}" class="inputText"></td>
				</tr>
				<tr>
					<th width="20%">级别: </th>
					<td>
					 <c:choose>
					    <c:when test="${empty dicList}">
					        	请在数据字典定义一个nodeKey为zwjb的分类
					    </c:when>
					    <c:otherwise>
					    	<select name="grade" class="inputText">
					    		<option value="">--请选择--</option>
					    		<c:forEach items="${dicList }" var="dic">
					    			<option value="${dic.itemValue }" <c:if test="${dic.itemValue eq job.grade}">selected="selected"</c:if>>${dic.itemName }</option>
					    		</c:forEach>
					    	</select>
					    </c:otherwise>			   
				    </c:choose>
					</td>
				</tr>
				<tr>
					<th width="20%">职务描述: </th>
					<td><textarea  id="jobdesc" name="jobdesc" value="${job.jobdesc}" validate="{required:false}" class="inputText">${job.jobdesc}</textarea></td>
				</tr>
			</table>
			<input type="hidden" name="jobid" value="${job.jobid}" />					
		</form>
	</div>
</div>
</body>
</html>