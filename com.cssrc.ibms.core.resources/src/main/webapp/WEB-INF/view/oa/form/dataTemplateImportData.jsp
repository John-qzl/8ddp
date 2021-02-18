<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title>
数据导入
</title>
<f:js pre="jslib/lang/view/oa/form" ></f:js>
	<script type="text/javascript">
	window.name="win";
			
	$(function(){
		//valid(showResponse);
		
		$("#btnSave").click(function(){
			var path = $('#file').val();
			var extName = path.substring(path.length-4, path.length);
			if(extName=='.xls' || extName == 'xlsx'){
				$.ligerDialog.waitting("正在导入，请稍候...");
				$("#importForm").submit();	
				$.ligerDialog.closeWaitting();
			}else{
				$.ligerDialog.warn("文件格式不符合要求，只支持xls和xlsx");
			}
		});
	});
	
	function showResponse(responseText){
		var obj=new com.ibms.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			$.ligerDialog.closeWaitting();			
			$.ligerDialog.success(obj.getMessage(),$lang.tip.msg);
	    }else{//失败
			$.ligerDialog.closeWaitting();
	    	$.ligerDialog.error(obj.getMessage(),$lang.tip.error);
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
	<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">数据导入</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link import" id="btnSave">导入</a></div>
					
					<div class="group"><a class="link del" onclick="javasrcipt:window.close()">取消</a></div>
				
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="importForm"  name="importForm" method="post" target="win" action="importSave.do" enctype="multipart/form-data">
			<div class="row">
			 	<input type="hidden" id="__displayId__" name="__displayId__" value="${__displayId__}">
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
	(1)对于需要设置唯一约束的数据列，请在<font color=red>业务表定义</font>--><font color=red>编辑</font>,勾选设置<font color=red>'唯一约束'</font>.<br/>
    (2).对于人员,角色,组织,岗位,数据列的导入,需要在excel中<font color=red>手工补充</font>对应的ID列,并填充具体的ID值。<br/>
    (3).本导入方式，<font color=red>仅导入excel中有的数据列</font>。<br/>
	</div>
	<!-- end of panel-body -->				
</body>
</html>