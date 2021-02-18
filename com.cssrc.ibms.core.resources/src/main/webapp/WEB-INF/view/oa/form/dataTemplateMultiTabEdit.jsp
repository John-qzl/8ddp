<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>明细多Tab设置</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/DataTemplateMultiTabEdit.js"></script>
<script type="text/javascript">
	$(function() {
           $("#multiTab").ligerTab();
           __MultiTab__.init();
           //保存
           $("a.save").click(function() {
	   			var form=$('#multiTabForm');
	   			if(saveChange(form)){
	   		   		var afterSubmit={success:submitForm};
	   				customFormSubmit(form,afterSubmit);	
	   			}
   		   });
           
           //初始化附件模板
           $("a.reload").click(function() {
				$.ligerDialog.confirm( "该操作会初始化模板，是否确定操作！","提示信息", function(rtn) {
					if(rtn){
						var url=location.href+"&reload=1";
				    	location.href=url.getNewUrl();
					}else {
						dialog.close();
					}
	   			});
		  });         
	});	
	function showResponse(responseText) {
		var result = new com.ibms.form.ResultMessage(responseText);
		if (!result.isSuccess()) {
			$.ligerDialog.err('出错信息',"明细多Tab设置编辑失败",result.getMessage());
			return;
		} else {
			$.ligerDialog.success(result.getMessage(),'提示信息',function(){
				//window.close();
			});
		}
	}
	function saveChange(form){
		var recRightField= JSON2.stringify(__MultiTab__.getRecRightField());
		$('#recRightField').val(recRightField);
		return true;
	}
	//预览
	function preview(id){
		var url=__ctx+ "/oa/form/dataTemplate/preview.do?__displayId__="+id;
		var winArgs="dialogWidth=800px;dialogHeight=600px;help=0;status=0;scroll=0;center=1";
		url=url.getNewUrl();
		$.openFullWindow(url);
	}
	/**
	 * 自定义外部表单并提交-用于提交表单权限相关设置
	 * @return void
	 */
	function customFormSubmit(form,options){
		InitMirror.save();
		var recRightField = $('#recRightField').val();
		var multiTabHtml = $('#multiTabTempHtml').text();
		var json={
			type : "frist",
			formKey:'${bpmDataTemplate.formKey}',
			recRightField:recRightField
		};	
		var form = $('<form method="post" action="multiTabSettingSave.do"></form>');
		var input = $("<input type='hidden' name='json'/>");
		var jsonStr=JSON2.stringify(json);
		input.val(jsonStr);
		form.append(input);
		form.ajaxForm(options);
		form.submit();
	}
	/**
	 * 表单提交-用于提交Html内容
	 * @return void
	 */
	function submitForm(){
		InitMirror.save();
		var options={};
		if(showResponse){
			options.success=showResponse;
		}
		var frm=$('#multiTabForm').form();
		frm.ajaxForm(options);
		if(frm.valid()){
			frm.submit();
		}
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">明细多Tab设置</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSearch">保存</a></div>
					
					<div class="group"><a class="link reload" id="reload">HTML初始化</a></div>
					<div class="group"><a class="link preview" href="####" onclick="preview(${bpmDataTemplate.id})">预览</a></div>
					<div class="group"><a class="link del" onclick="window.close()">关闭</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="multiTabForm" method="post" action="multiTabHtmlSave.do">
				<div id ="hidden">
					<input type="hidden" id="formKey" name="formKey" value="${bpmDataTemplate.formKey}"class="inputText" /> 
					<!-- 表单类型信息 -->
					<textarea style="display: none;" id="recTypes" name="recTypes" >${fn:escapeXml(recTypes)}</textarea>
					<!--  明细多tab关联关系设置字段-->
					<textarea style="display: none;" id="recRightField" name="recRightField" >${fn:escapeXml(bpmDataTemplate.recRightField)}</textarea>
					<!-- 关联表字段信息 -->
					<textarea style="display: none;" id="fieldList" name="fieldList" >${fn:escapeXml(fieldList)}</textarea>
				</div>
				<div id="multiTab">
				<div tabid="relationSetting" title="关联关系设置">
					<table class="table-detail" id="recRightTb" cellpadding="0" cellspacing="0" border="0"  style="border-width: 0!important;">
								<tr>
									<th width ="10%">详细表单打开类型：</th>
									<td> 
										<input type="radio" name="openType" value="frist" >打开一个浏览器窗口
										<input type="radio" name="openType" value="second">在当前窗口打开
										<input type="radio" name="openType" value="third" checked="checked">弹出一个窗口
									</td>
								</tr>
								<tr>
								<th width ="10%">关联类型：</th>
								<td> 
									<input type="radio" name="relType" value="table"  onclick="__MultiTab__.setTabRelation()" checked="checked">表关联
									<input type="radio" name="relType" value="column"  onclick="__MultiTab__.setColRelation()" >字段关联
								</td>
								</tr>
								<tr id = "relationTableTr">
									<th >关联表：</th>
									<td id = "relationTableTd"> 
										${bpmFormTable.tableDesc}
									</td>
								</tr>
								<!-- 字段关联时：字段隐藏-->
								<tr id = "recTypeTr">
									<th >关联表单类型：</th>
									<td id = "recTypeTd"> 
										<!-- js拼接select-->
									</td>
								</tr>
								<!-- 表关联时：字段隐藏-->
								<tr  style=" display: none;" id = "relationColumnTr">
									<th >关联字段：</th>
									<td id = "relationColumnTd"> 
										<!-- js拼接select-->
									</td>
								</tr>
						</table>
						<!-- 表关联时：字段隐藏-->
						<table id="relationColumnSettingTable"  class="table-grid"  style="display: none;">
							<thead>
								<tr>
									<th width="5%">序号</th>
									<th width="15%">字段枚举值</th>
									<th width="20%">表单类型</th>
								</tr>
							</thead>
							<tbody>
								<!-- js拼接tr-->
							</tbody>
						 </table>	
					</div>
					<div tabid="multiTabHtml" title="多Tab明细HTML">
						<table class="table-detail" cellpadding="0" cellspacing="0"
							border="0">
							<tr>
								<th width="20%">HTML编辑内容:<span class="required">*</span>
								</th>
								<td><textarea id="multiTabHtml" name="multiTabTempHtml"
										codemirror="true" mirrorheight="600px" rows="10" cols="80">${fn:escapeXml(multiTabHtml)}</textarea>
								</td>
							</tr>
						</table>
					</div>
					</div>
			</form>

		</div>
	</div>
</body>
</html>
