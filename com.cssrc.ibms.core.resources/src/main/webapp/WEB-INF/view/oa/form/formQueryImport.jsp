<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>自定义查询导入</title>
<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)	
	window.name="win";
	$(function(){
		valid(showResponse);
		
		$("#btnSave").click(function(){

			var path = $('#xmlFile').val();
			var extNaem = path.substring(path.length-3, path.length);
			if(extNaem!='xml'){
				$.ligerDialog.warn("请选择 *.xml文件进行导入!");
			}else{
				$.ligerDialog.waitting('正在导入中,请稍候...');
				$("#importForm").submit();
			}
		});
	});
	
	function showResponse(responseText){
		var obj=new com.ibms.form.ResultMessage(responseText);
		if(obj.isSuccess()){//成功
			$.ligerDialog.closeWaitting();
			var message = obj.getMessage();
			if(message){
				message =message.replaceAll("###","\'").replaceAll("!!!","<font ").replaceAll("%%%","</font>").replaceAll("&gt;",">").replaceAll("&lt;","<");   
			}
		  	 $.ligerDialog.tipDialog('提示信息',"导入结果如下:",message,null,function(){
		  		dialog.get('sucCall')("/");
				dialog.close();
				});
	    }else{//失败
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
	<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">自定义查询导入</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSave">导入</a></div>
					
					<div class="group"><a class="link del" onclick="javasrcipt:frameElement.dialog.close()">关闭</a></div>
				</div>	
			</div>
	</div>
	<div class="panel-body">
		<form id="importForm"  name="importForm" method="post" target="win" action="importXml.do" enctype="multipart/form-data">
			<div class="row">
			 
			 <table id="tableid" class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width="22%">选择文件：</th>
					<td width="78%"><input type="file" size="40" name="xmlFile" id="xmlFile"/></td>						
				</tr>
			</table>				
			<br>
			提示：<font color=red>对alias同名的自定义查询导入，请先手工删掉已有的自定义查询，再导入</font>
			</div>
	    </form>
	</div><!-- end of panel-body -->				
</body>
</html>