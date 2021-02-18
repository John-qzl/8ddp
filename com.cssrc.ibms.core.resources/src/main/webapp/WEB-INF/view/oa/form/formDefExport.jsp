<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>自定义表单导出</title>
<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript">
	var dialog = frameElement.dialog; 
			
	$(function(){	
		$("#btnSave").click(function(){
			$("#exportForm").submit();
		});
	});
	
	
	</script>
</head>
<base target="download"> 
<body>
	<div class="panel">
	<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">自定义表单导出</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSave">导出</a></div>
					
					<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">关闭</a></div>
				
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="exportForm" class="plat-form" name="exportForm" method="post" target="download" action="exportXml.do">
			<div class="row">
			 <input type="hidden" name="formDefIds" value="${formDefIds}" >
			 <table id="tableid" class="table-detail" cellpadding="0" cellspacing="0" border="0">
			 	<tr>
			 		<th width="30%">自定义表单：</th>
			 		<td width="70%"><input type="checkbox"  name="bpmFormDef" checked="checked" disabled="disabled"></td>	
			 	</tr>
			 	<tr>
			 		<th>对应的自定义表：</th>
			 		<td><input type="checkbox"  name="bpmFormTable" value="true" >&nbsp;&nbsp;&nbsp;&nbsp;
			 			<a href="javascript:;" class="tipinfo"><span>如选择，则导出对应表的详细信息；</br> 如不选择，则只会导出对应表的ID。</span></a>
			 		</td>	
			 	</tr>		
				<tr>
					<th>其它版本的&nbsp;&nbsp;&nbsp;&nbsp;</br>自定义表单：</th>
					<td><input type="checkbox"  name="bpmFormDefOther" checked="checked" value="true"></td>						
				</tr>
				<tr>
					<th>表单权限：</th>
					<td><input type="checkbox"  name="bpmFormRights" checked="checked" value="true" ></td>						
				</tr>
				<tr>
					<th>数据模板：</th>
					<td><input type="checkbox"  name="bpmTableTemplate" checked="checked" value="true" ></td>						
				</tr>
				<tr>
					<th>业务设置：</th>
					<td><input type="checkbox"  name="sysBusEvent" checked="checked" value="true" ></td>						
				</tr>
			</table>				
			
			</div>
	    </form>
	</div><!-- end of panel-body -->
	</div>
	<iframe id="download" name="download" height="0px" width="0px"></iframe>				
</body>
</html>