<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title>
	表单模板历史数据导入
</title>
<f:js pre="jslib/lang/view/oa/form" ></f:js>
<script type="text/javascript">
	
	var dialog = frameElement.dialog;	
	var slid = <%=request.getParameter("slid")%>;
	
	$(function(){
		valid(showResponse);
		$("#btnSave").click(function(){
			var path = $('#file').val();
			var extName = path.substring(path.length-4, path.length);
			if(extName=='.xls'){
				$.ligerDialog.waitting("正在导入，请稍候...");
				$("#importForm").submit();	
			}else{
				$.ligerDialog.warn("文件格式不符合要求，只支持xls文件");
			}
		}); 
	});
	
	function showResponse(responseText){
		$.ligerDialog.closeWaitting();
		var obj=new com.ibms.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			$.ligerDialog.success("导入成功！",function(){
				dialog.close();
			});
	    }else{//失败
			$.ligerDialog.err("错误提示","",obj.getMessage());
	    } 
	}
	
	function valid(showResponse){
		
		var options={success:showResponse,url:__ctx+'/dp/form/importExcel.do?slid='+slid};
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
	<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">数据导入</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link import" id="btnSave">导入</a></div>
					
					<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">取消</a></div>
				
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="importForm"  name="importForm" method="post" target="win" enctype="multipart/form-data">
			<div class="row">
			 <input type="hidden" id="fcId" name="fcId">
			 <table id="tableid" class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width="22%">选择文件：</th>
					<td width="78%"><input type="file" size="40" name="file" id="file"/></td>						
				</tr>
			</table>				
			
			</div>
	    </form>
	</div>
	 <div class="panel-body">
	    导入说明：<br/>
	 <span>	 
	 	只支持原表单模板导出.xls文件，不能自定义修改，导入的模板将更新对应的检查结果数据。<br/>
	 </span>
	</div>
	<!-- end of panel-body -->				
</body>
</html>