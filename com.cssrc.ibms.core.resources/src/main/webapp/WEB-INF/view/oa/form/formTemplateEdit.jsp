 
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 表单模板</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript"
	src="${ctx}/servlet/ValidJs?form=bpmFormTemplate"></script>
	<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
<script type="text/javascript">
        var returnUrl1=("${returnUrl1}"==null||"${returnUrl1}"=="")?("${preUrl}"):("${returnUrl1}");
		$(function() {
			function showRequest(formData, jqForm, options) { //
				return true;
			} 
			if(${bpmFormTemplate.templateId==null}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {//
				if(InitMirror.editor!=null){
					$('#html').text(InitMirror.editor.getCode());
				}
				$('#bpmFormTemplateForm').submit(); 
			});
			
			$('#selectMacro').change(function() {
				changeDesc();
			});
			changeDesc();
			
			$("a.back").click(function(){//
			
		      location.href=returnUrl1;
		    });
		});
		
		function show(templateType) {
			if(templateType =='main'|| templateType =='subTable') {
				$('#macro').css('display', '');
				changeDesc();
			} else {
				$('#macro').css('display', 'none');
			}
			
			if(templateType=='dataTemplate'||templateType=='queryDataTemplate'){
				$('#headHtml').parents('tr').css('display', '');
			}else{
				$('#headHtml').parents('tr').css('display', 'none');
			}
		}
		
		function changeDesc() {
			$('#macroDesc').text(($('#macroDescs').val($('#selectMacro').val()).find('option:selected').text()));
		}
		
		
	</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label"> <c:choose>
						<c:when test="${bpmFormTemplate.templateId==null}">
							添加表单模板
						</c:when>
						<c:otherwise>
							编辑表单模板
						</c:otherwise>
					</c:choose>
				</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="javascript:void(0);" >返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="bpmFormTemplateForm" method="post" action="save.do">

				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<th width="10%">模板名:<span class="required">*</span>
						</th>
						<td><input type="text" id="templateName" name="templateName"
							value="${bpmFormTemplate.templateName}" class="inputText" /></td>
							
						<th width="5%" rowspan ="7">说明:</th>
						<td rowspan ="7">
							<textarea style="width: 320px;" id="templateDesc" name="templateDesc" rows=5>${bpmFormTemplate.templateDesc}</textarea>
						</td>
					</tr>
					<tr>
						<th width="10%">别名:<span class="required">*</span>
						</th>
						<td><input type="text" id="alias" name="alias"
							value="${bpmFormTemplate.alias}" class="inputText" /><span
							id="aliasInfo" style="color: red"></span></td>
					</tr>
					<tr>
						<th width="10%">模板类型<span class="required">*</span>
						</th>
						<td><input type="radio" name="templateType" value="main"
							onclick="show('main')"
							<c:if test="${bpmFormTemplate.templateType=='main' }"> checked="checked"</c:if> />
							主表模板 &nbsp; <input type="radio" name="templateType"
							value="subTable" onclick="show('subTable')"
							<c:if test="${bpmFormTemplate.templateType=='subTable' }"> checked="checked"</c:if> />
							子表模板 &nbsp; <input type="radio" name="templateType" value="macro"
							onclick="show('macro')"
							<c:if test="${bpmFormTemplate.templateType=='macro' }"> checked="checked"</c:if> />
							宏模板 &nbsp; <input type="radio" name="templateType" value="list"
							onclick="show('list')"
							<c:if test="${bpmFormTemplate.templateType=='list' }"> checked="checked"</c:if> />
							列表模板 &nbsp; <input type="radio" name="templateType"
							value="detail" onclick="show('detail')"
							<c:if test="${bpmFormTemplate.templateType=='detail' }"> checked="checked"</c:if> />
							明细模板 &nbsp; <input type="radio" name="templateType"
							value="tableManage" onclick="show('tableManage')"
							<c:if test="${bpmFormTemplate.templateType=='tableManage' }"> checked="checked"</c:if> />
							表管理模板
							 &nbsp; <input type="radio" name="templateType"
							value="dataTemplate" onclick="show('dataTemplate')"
							<c:if test="${bpmFormTemplate.templateType=='dataTemplate' }"> checked="checked"</c:if> />
							业务数据模板
							 &nbsp; <input type="radio" name="templateType"
							value="queryDataTemplate" onclick="show('queryDataTemplate')"
							<c:if test="${bpmFormTemplate.templateType=='queryDataTemplate' }"> checked="checked"</c:if> />
							业务数据多表查询模板</td>
					</tr>
					<tbody id="macro"
						style="<c:if test="${bpmFormTemplate.templateType =='macro'}">display:none</c:if>
													<c:if test="${bpmFormTemplate.templateType =='list'}">display:none</c:if>
													<c:if test="${bpmFormTemplate.templateType =='detail'}">display:none</c:if>">
						<tr>
							<th width="10%">控件宏:</th>
							<td colspan="3"><select name="macroTemplateAlias" id="selectMacro">
									<c:forEach items="${macroTemplates}" var="template">
										<option value="${template.alias}"
											<c:if test="${bpmFormTemplate.macroTemplateAlias == template.alias}">selected</c:if>>${template.templateName}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<th width="10%">宏说明:</th>
							<td id="macroDesc" colspan="3"></td>
						</tr>
					</tbody>
					<tr style="<c:if test="${bpmFormTemplate.templateType !='dataTemplate'
								&&bpmFormTemplate.templateType !='queryDataTemplate'}">display:none</c:if>">
						<th width="10%">模板headHtml:</th>
						<td colspan="3">
							<textarea id="headHtml" name="headHtml" style="width:80%;height:100px" >${fn:escapeXml(bpmFormTemplate.headHtml)}</textarea>
							<a href="####" class="tipinfo">
								<span>
										作用:添加css、js文件</br>	
										1.	css添加</br>	
										 <b>  ${fn:escapeXml("<link href='${ctx}/jslib/pictureShow/css/jquery.fancybox.css' rel='stylesheet' />")}</b></br>	
										2.	js添加</br>	
										 <b>  ${fn:escapeXml("< script type='text/javascript' src='${ctx}/jslib/ibms/oa/desktop/test.js'/>")}</b></br>	
								</span>
							</a>
						</td>
					</tr>
					<tr>
						<th width="10%">模板html:</th>
						<td colspan="3"><textarea id="html" name="html" cols=500 rows=25 codemirror="true" mirrorheight="600px">${fn:escapeXml(bpmFormTemplate.html)}</textarea></td>
					</tr>
					
				</table>
				<input type="hidden" name="templateId"	value="${bpmFormTemplate.templateId}" />
					<input type="hidden" id="returnUrl" value="${preUrl}" />
			</form>

		</div>
	</div>
	<select id="macroDescs" style="display: none">
		<c:forEach items="${macroTemplates}" var="template">
			<option value="${template.alias}">${template.templateDesc}</option>
		</c:forEach>
	</select>
</body>
</html>
