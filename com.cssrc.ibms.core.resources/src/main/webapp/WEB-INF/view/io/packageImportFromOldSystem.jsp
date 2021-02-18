<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title>
	数据包系统迁移导入
</title>
<f:js pre="jslib/lang/view/oa/form" ></f:js>
	<script type="text/javascript">
	window.name="数据包系统迁移导入";
	var dialog = frameElement.dialog;		
	$(function(){
//
		var fcId = dialog.options.params.fcId;
		$('#fcId').val(fcId);
		valid(showResponse,fcId);		
		$("#btnSave").click(function(){
			var path = $('#file').val();
			var extName = path.substring(path.length-4, path.length);
			if(extName=='.zip'){
//
				$.ligerDialog.waitting("正在导入，请稍候...");
				$("#importForm").submit();	
				$.ligerDialog.closeWaitting();
			}else{
				$.ligerDialog.warn("文件格式不符合要求，只支持zip文件");
			}
		});
	});
	
	function showResponse(responseText){
		var obj=new com.ibms.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			if(obj.data.log==""){
				$.ligerDialog.success("导入成功！");
			}else{
				$.ligerDialog.err("信息提示","",obj.data.log);
			}
	    }else{//失败
			$.ligerDialog.closeWaitting();
			$.ligerDialog.err("错误提示","",obj.getMessage());
	    }
	}
	
	function valid(showResponse,fcId){
		var options={success:showResponse,url:__ctx+'/dataPackage/tree/ptree/importPackagesFromOldSystem.do'+"?fcId="+fcId};
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
				<span class="tbar-label">数据包系统迁移导入</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link import" id="btnSave">导入</a></div>
					
					<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">取消</a></div>
				
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="importForm"  name="importForm" method="post" target="win"  enctype="multipart/form-data">
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
	(1).导入的部署包必须是一期的数据包系统导出的压缩包，否则将无法导入。<br/>
	(2).此功能为技术人员内置功能，普通用户请谨慎操作。<br/>
    <!-- (3).本导入方式，<font color=red>仅导入excel中有的数据列</font>。<br/> -->
	</div>
	<!-- end of panel-body -->				
</body>
</html>