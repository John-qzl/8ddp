
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看版本</title>
<%@include file="/commons/include/get.jsp" %>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript">
	$(function(){
		handPublish();
		handNewVersion();
	});
	
	function handPublish(){
		$.confirm("a.link.deploy",'确认发布吗？');
	}
	
	function handNewVersion(){
		$.confirm("a.link.newVersion",'确认新建版本吗？');
	}
	function handDelete(formDefId,isDefault){
		if(isDefault=='1'){
			$.ligerDialog.warn("设置为默认的表单设计版本，无法删除!","提示");
			return;
		}
		var url = '${ctx}/oa/form/formDef/delByFormDefId.do?';
		url+= 'formDefId='+formDefId;
		url+= '&returnUrl=${returnUrl}';
		$.ligerDialog.confirm('确认删除此版本吗？','提示信息',function(rtn){
			if(rtn) {
				$.get(url,function(data) {
					showResponse(data);
				});
			}
		});
	}
	function showResponse(responseText){
		var obj=new com.ibms.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			$.ligerDialog.closeWaitting();
			$.ligerDialog.success('<p><font color="green">'+obj.getMessage()+'</font></p>','提示信息',function(){
				location.reload(true);
			});
	    }else{//失败
	    	$.ligerDialog.err('出错信息',obj.getMessage());
	    }
	}		
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${formName}--版本管理</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link back " href="${returnUrl}">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<display:table name="versions" id="bpmFormDefItem" requestURI="versions.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
				<display:column property="versionNo" title="版本号" sortable="true" sortName="versionNo"></display:column>
				<display:column property="publishedBy" title="发布人" sortable="true" sortName="publishedBy"></display:column>
				<display:column title="发布时间" sortable="true" sortName="publishTime">
					<fmt:formatDate value="${bpmFormDefItem.publishTime}"/>
				</display:column>
				<display:column title="是否默认" sortable="true" sortName="isDefault">
					<c:choose>
					 	<c:when test="${bpmFormDefItem.isDefault==1}"><span class="green">是</span></c:when>
					 	<c:otherwise>
					 		<span class="red">否</span>
					 	</c:otherwise>
					</c:choose>
				</display:column>
				<display:column title="是否发布" sortable="true" sortName="isDefault">
					<c:choose>
					 	<c:when test="${bpmFormDefItem.isPublished==1}"><span class="green">已发布</span></c:when>
					 	<c:otherwise>
					 		<span class="red">未发布</span>
					 	</c:otherwise>
					</c:choose>
				</display:column>
			
				<display:column title="管理" media="html" >
					<c:choose>
						<c:when test="${bpmFormDefItem.isDefault==1}">
							<a class="link setting " style="color:gray;">设置默认</a>
						</c:when>
						<c:otherwise>
							<a  class="link setting" href="setDefaultVersion.do?formDefId=${bpmFormDefItem.formDefId }&formKey=${bpmFormDefItem.formKey}&returnUrl=${returnUrl}"><span >设置默认</span></a>
						</c:otherwise>
					</c:choose>
					<c:if test="${bpmFormDefItem.isPublished==0&&bpmFormDefItem.designType==0}">
							<a  class="link deploy" href="publish.do?formDefId=${bpmFormDefItem.formDefId }&returnUrl=${returnUrl}">发布</a>
					</c:if>
					<c:if test="${bpmFormDefItem.isPublished== 1}">
						<a href="newVersion.do?formDefId=${bpmFormDefItem.formDefId}&returnUrl=${returnUrl}"  class="link newVersion">新建版本</a>
					</c:if>	
					
					<a href="####" onclick="javascript:handDelete('${bpmFormDefItem.formDefId}','${bpmFormDefItem.isDefault}');"  class="link delVersion">删除</a>		
								
					<c:choose>
						<c:when test="${bpmFormDefItem.designType==0 }">
							<a href="####" onclick="javascript:jQuery.openFullWindow('edit.do?formDefId=${bpmFormDefItem.formDefId}');" class="link edit">编辑</a>
						</c:when>
						<c:otherwise >
							<a href="####" onclick="javascript:jQuery.openFullWindow('designEdit.do?formDefId=${bpmFormDefItem.formDefId}');" class="link edit">编辑</a>
						</c:otherwise>
					</c:choose>
					
					<%-- <a href="get.do?formDefId=${bpmFormDefItem.formDefId}"  class="link detail">查看</a> --%>
					
					<c:choose>
							<c:when test="${bpmFormDefItem.designType==0 }">
								<a target="_blank" href="${ctx}/oa/form/formHandler/edit.do?formDefId=${bpmFormDefItem.formDefId}" class="link preview">预览</a>
							</c:when>
							<c:when test="${bpmFormDefItem.designType==1 }">
								<a href="####" onclick="javascript:jQuery.openFullWindow('preview.do?formDefId=${bpmFormDefItem.formDefId}');" class="link edit">预览</a>
							</c:when>
					</c:choose>
					
					<input type="hidden" id="bpmFormDefId"value="${bpmFormDefItem.formDefId}">
				</display:column>
			</display:table>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


