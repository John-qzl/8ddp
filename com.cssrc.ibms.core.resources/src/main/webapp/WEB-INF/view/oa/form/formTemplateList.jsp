
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>表单模板管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerWindow.js" ></script>
    <script type="text/javascript" src="${ctx }/jslib/ibms/oa/form/copyTemplateDialog.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js"></script>
    <script type="text/javascript">
    	$(function(){//设置不能编辑的行不能选中
    		$("tr").live("click",function(event){
    			var checkbox = $(this).find("input.pk");
    			if(checkbox.attr("disabled")=="disabled"){
   				   checkbox.attr("checked",false);
    			}
    		});
    		
    		handlerInit();
    	});
    	
    	//处理初始化模板
	    function handlerInit()
	    {
	    	$("a.link.init").click(function(){
	    		var action=$(this).attr("action");
	    		if($(this).hasClass('disabled')) return false;
	    		
	    		$.ligerDialog.confirm('初始化表单模板将会导致系统模板重新生成，确定初始化吗？','提示信息',function(rtn){
	    			if(rtn){
	    				var form=new com.ibms.form.Form();
	    				form.creatForm('form', action);
	    				form.submit();
	    			}
	    		});
	    		
	    	});
	    }

    	function copyTemplate(templateId,templateName,alias){
    		CopyTemplateDialog({templateId:templateId,templateName:templateName,alias:alias});
    	}
    	function  edit(templateId,returnUrl){
    	href="edit.do?templateId="+templateId+"&returnUrl="+returnUrl;
    	location.href=href;
    	
    	}
    	
    	//批量导入模板
    	function ImportXml(){
    		var url=__ctx + "/oa/form/formTemplate/import.do";
    		ImportExportXml.showModalDialog({url:url});
    	}
    	
    	// 导出自定义模板
		function exportXml(){
    		var formTemplateIds = ImportExportXml.getChkValue('pk');
    		var url=__ctx + "/oa/form/formTemplate/exportXml.do?formTemplateIds="+formTemplateIds;
    		if (formTemplateIds ==''){
    			$.ligerDialog.warn('还没有选择,请选择一个自定义模板!','提示信息');
    			return;
    		}else{
				$.ligerDialog.confirm('确认导出吗？', '提示信息', function(rtn) {
				if(rtn){
				var form = new com.ibms.form.Form();
				form.creatForm("form", url);
				form.submit();
				}
			});
    	}
	}
    	
    	//导入自定义模板
		function importXml(){
    		var url = __ctx + "/oa/form/formTemplate/importAll.do";
    		ImportExportXml.showModalDialog({
    			url : url,
				title : '导入表单模板'
			});	
    	}
					
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">表单模板管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					
					<div class="group">
					<a onclick="exportXml()" href="####" class="link export">导出</a>
					</div>
			   
					<div class="group">
					<a onclick="importXml()" href="####" class="link import">导入</a>
					</div>
					
					<div class="group"><a class="link init" id="bntInit" action="init.do">初始化模板</a></div>
					<div class="group"><a class="link import" title="导入的xml由数据迁移功能生成" href="javascript:void(0)" onclick="ImportXml()">批量导入模板</a></div>
				</div>	
			</div>
			
			<div class="panel-search">
				<form id="searchForm" class="plat-form" method="get" action="list.do">
					<ul class="row plat-row">
						<li><span class="label">模板名:</span><input type="text" name="Q_templateName_SL"  class="inputText" value="${param['Q_templateName_SL']}"/></li>
						<li><span class="label">模板类型:</span>
						<select name="Q_templateType_S" value="${param['Q_templateType_S']}">
							<option value="">全部</option>
							<option value="main" <c:if test="${param['Q_templateType_S'] == 'main'}">selected</c:if>>主表模板</option>
							<option value="subTable" <c:if test="${param['Q_templateType_S'] == 'subTable'}">selected</c:if>>子表模板</option>
							<option value="relTable" <c:if test="${param['Q_templateType_S'] == 'relTable'}">selected</c:if>>关系表模板</option>
							<option value="macro" <c:if test="${param['Q_templateType_S'] == 'macro'}">selected</c:if>>宏模板</option>
							<option value="list" <c:if test="${param['Q_templateType_S'] == 'list'}">selected</c:if>>列表模板</option>
							<option value="detail" <c:if test="${param['Q_templateType_S'] == 'detail'}">selected</c:if>>明细模板</option>
							<option value="tableManage" <c:if test="${param['Q_templateType_S'] == 'tableManage'}">selected</c:if>>表管理模板</option>
							<option value="dataTemplate" <c:if test="${param['Q_templateType_S'] == 'dataTemplate'}">selected</c:if>>数据模板</option>
							<option value="queryDataTemplate" <c:if test="${param['Q_templateType_S'] == 'queryDataTemplate'}">selected</c:if>>业务数据多表查询模板</option>
							<option value="groupopinion" <c:if test="${param['Q_templateType_S'] == 'groupopinion'}">selected</c:if>>审批意见</option>
						</select></li>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="bpmFormTemplateList" id="bpmFormTemplateItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					  	<input type="checkbox" class="pk"  name="templateId" value="${bpmFormTemplateItem.templateId}"  <c:if test="${bpmFormTemplateItem.canEdit==0}">disabled="disabled"</c:if>  >
				</display:column>
				<display:column property="alias" title="别名" sortable="true" sortName="alias" style="text-align:left"></display:column>
				<display:column property="templateName" title="模板名" sortable="true" sortName="templateName" style="text-align:left"></display:column>
				<display:column property="templateDesc" title="说明" sortable="true" sortName="templateDesc" style="text-align:left"></display:column>
				<display:column title="模板类型" style="text-align:left">
					<c:if test="${bpmFormTemplateItem.templateType=='groupopinion'}">审批意见</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='list'}">列表模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='detail'}">明细模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='subTable'}">子表模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='relTable'}">关系表模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='main'}">主表模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='macro'}">宏模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='tableManage'}">表管理模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='dataTemplate'}">数据模板</c:if>
					<c:if test="${bpmFormTemplateItem.templateType=='queryDataTemplate'}">业务数据多表查询模板</c:if>
				</display:column>
				<display:column title="模板来源" style="text-align:left">
					<c:choose>
						<c:when test="${bpmFormTemplateItem.canEdit==0}"><span class="red">系统模板</span></c:when>
						<c:when test="${bpmFormTemplateItem.canEdit==1}"><span class="green">自定义模板</span></c:when>
					</c:choose>
				</display:column>
				<display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
					<c:choose>
						<c:when test="${bpmFormTemplateItem.canEdit==0}">
							<a  class="link del disabled">删除</a>
							<a  class="link edit disabled">编辑</a>
							<a  class="link backUp disabled" >备份</a>	
						</c:when>
						<c:otherwise >
							<f:a alias="delTemplateForm" href="del.do?templateId=${bpmFormTemplateItem.templateId}" css="link del">删除</f:a>
							<a  href="javascript:void(0);"    onclick="edit('${bpmFormTemplateItem.templateId}','${returnUrl}');" class="link edit">编辑</a>
							<a  href="backUp.do?templateId=${bpmFormTemplateItem.templateId}" class="link backUp" >备份</a>
							
						</c:otherwise>
					</c:choose>
					<a href="get.do?templateId=${bpmFormTemplateItem.templateId}" class="link detail">明细</a>
					<a href="####" onclick="copyTemplate('${bpmFormTemplateItem.templateId}','${bpmFormTemplateItem.templateName}','${bpmFormTemplateItem.alias}')"  class="link copy">复制</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="bpmFormTemplateItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


