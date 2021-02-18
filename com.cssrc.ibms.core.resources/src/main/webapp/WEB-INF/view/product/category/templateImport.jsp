<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>

<html>
<head>
	<title> 产品类别导入 </title>
	<%@include file="/commons/include/form.jsp" %>
	<f:js pre="jslib/lang/view/oa/form" ></f:js>
	<script type="text/javascript">
		var dialog = frameElement.dialog;
		dialog.set('title','模板导入');
		$(function(){
			
			valid(showResponse);

			$("#btnSave").click(function(){
				debugger;
				var path = $('#file').val();
				var extName = path.substring(path.length-4, path.length);
				if(extName=='.xls' || extName == 'xlsx'){
					$.ligerDialog.waitting("正在导入，请稍候...");
					$("#importForm").attr("action",'${ctx}/product/category/batch/importCategories.do';
					var obj = {success:showResponse} ;
					$("#importForm").submit();
				} else{
					$.ligerDialog.warn("文件格式不符合要求，只支持xls和xlsx");
				}
			});
			$("#btnClose").click(function(){
				dialog.get('sucCall')('ok');
				dialog.close();
			});
		});
	
		function showResponse(responseText){
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
                $.ligerDialog.closeWaitting();
                $.ligerDialog.success(obj.getMessage(),"提示信息", function(rtn) {
					dialog.get('sucCall')('ok');
					dialog.close();
                });
			}else{ //失败
				$.ligerDialog.closeWaitting();
		    	$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}

		function valid(showResponse){
			var options={success:showResponse};
			__valid=$("#importForm").validate({
				rules: {},
				messages: {},
				submitHandler:function(form){
					$(form).ajaxSubmit(options);
				},
				success: function(label) {}
			});
		}
	
	</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">模板导入</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSave">导入</a></div>
					
					<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">关闭</a></div>
				
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="importForm"  name="importForm" method="post" target="win" action="importCategories.do" enctype="multipart/form-data">
			<div class="row">
			 
			 <table id="tableid" class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width="22%">选择文件：</th>
					<td width="78%"><input type="file" size="40" name="file" id="file"/></td>						
				</tr>
			</table>				
			<br>
			提示：<br/><font color=red>
					1.&nbsp;本导入方式，<font color=red>仅导入excel中有的数据列</font>.<br/>
					2.&nbsp;兼容Excel2003及2007版本。<br/>
			</font>
			</div>
	    </form>
	</div>
	<!-- end of panel-body -->
</body>
</html>