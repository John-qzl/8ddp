<%--
	time:2012-12-19 15:38:01
	desc:edit the 自定义表代码模版
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 自定义表代码模版</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_api.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormDef.js"></script>

<script type="text/javascript">
	$(function() {
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		var frm = $('#sysCodeTemplateForm').form();
		/*$("a.save").click(function() {
			frm.ajaxForm(options);
			if (frm.valid()) {
				$('#sysCodeTemplateForm').submit();
			}
		});*/
		$("a.save").click(save);
	});

	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作","提示信息",
							function(rtn) {
								if (rtn) {
									this.close();
									$("#sysCodeTemplateForm").resetForm();
								} else {
									window.location.href = "${ctx}/oa/system/sysCodeTemplate/list.do";

								}
							});
		} else {
			$.ligerDialog.error(obj.getMessage(),'提示信息');
		}
	}
	function save(){
		//var valRes=validata();
    	//	 if(!valRes) return;
    		 var rtn=$("#sysCodeTemplateForm").valid();
    		 if(!rtn) return;
    		 var url=__ctx+ "/oa/system/sysCodeTemplate/save.do";
    		 var data = editor.getContent();
	  		 $('#html').val(data);
    		 var para=$('#sysCodeTemplateForm').serialize();
    		 $.post(url,para,showResult);
	}
	function showResult(responseText)
	{			
		var obj=new com.ibms.form.ResultMessage(responseText);
		
		if(!obj.isSuccess()){
			$.ligerDialog.error( obj.getMessage(),"出错了");
			return;
		}else{
			$.ligerDialog.success(obj.getMessage(),'提示信息',function(rtn){
				if(rtn) window.close();						
			});
		}
	}
	function validata(){
		     var isFlag = true;
	    	 $(".token-input").each(function(){
		    	 var _this = $(this);
		    	 var datas = _this.tokenInput("get");
		    	 if(!datas||datas.length>0){
		    		 isFlag=false;
		    	 }
	    	 });

	    	 if(isFlag){
	    		 $.ligerDialog.warn('至少需要填写一种消息发送方式的参数','提示信息');
	    		 return false;
	    	 }
	    	 var mailDatas = $("#receiver_mail").tokenInput("get");
	    	 if(mailDatas && mailDatas.length>0){
	    		 if($("#subject_mail").val()==""){
    				 $.ligerDialog.warn('请输入邮件的主题','提示信息');
    				 return false;
       			}
	    	 }else{
	    		 if($("#subject_mail").val()!=""){
	    			 $.ligerDialog.warn('请设置邮件接收人','提示信息');
    				 return false;
	    		 }
	    	 }


	    	 var innerDatas = $("#receiver_inner").tokenInput("get");
	    	 if(innerDatas && innerDatas.length>0){
	    		 if($("#subject_inner").val()==""){
	    			 $.ligerDialog.warn('请输入站内信息的主题','提示信息');
    				 return false;
    			}
	    	 }else{
	    		 if($("#subject_inner").val()!=""){
	    			 $.ligerDialog.warn('请设置站内信息接收人','提示信息');
    				 return false;
	    		 }
	    	 }
	    	 
	    	 return true;
	     }

	
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<c:choose>
					<c:when test="${sysCodeTemplate.id !=null}">
						<span class="tbar-label">编辑自定义表代码模版</span>
					</c:when>
					<c:otherwise>
						<span class="tbar-label">添加自定义表代码模版</span>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysCodeTemplateForm" method="post" action="save.do">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0" type="main">
					<tr>
						<th width="20%">模版名称:</th>
						<td><input type="text" id="templateName" name="templateName" value="${sysCodeTemplate.templateName}" class="inputText" validate="{required:true,maxlength:200}" /></td>
					</tr>
					<tr>
						<th width="20%">别名:</th>
						<td><input type="text" id="templateAlias" name="templateAlias" value="${sysCodeTemplate.templateAlias}" class="inputText" validate="{required:true,maxlength:200}" /></td>
					</tr>
					<tr>
						<th width="20%">从表也生成:</th>
						<td>
							<input type="radio" name="isSub" value="0" <c:if test="${sysCodeTemplate.isSub eq 0 or empty sysCodeTemplate.isSub}"> checked='checked' </c:if>>否
							&nbsp;&nbsp;
							<input type="radio" name="isSub" value="1" <c:if test="${sysCodeTemplate.isSub eq 1 }"> checked='checked' </c:if>>是
						</td>
					</tr>
					<tr>
						<th width="20%">备注:</th>
						<td><input type="text" id="memo" name="memo" value="${sysCodeTemplate.memo}" class="inputText" validate="{required:false,maxlength:200}" /></td>
					</tr>
					
					<tr>
						<th width="20%">模版生成的文件路径:</th>
						<td><input type="text" id="fileDir" name="fileDir" value="${sysCodeTemplate.fileDir}" class="inputText" validate="{required:true,maxlength:200}" style="width: 350px;" /></td>
					</tr>
					
					<tr>
						<th width="20%">模版生成的文件名称:</th>
						<td><input type="text" id="fileName" name="fileName" value="${sysCodeTemplate.fileName}" class="inputText" validate="{required:true,maxlength:200}" style="width: 250px;"  /></td>
					</tr>

					<tr>
						<th width="20%">模版HTML:</th>
						<td>
							<div id="editor" position="center"  style="overflow:hidden;height:100%;">
								<textarea  id= "html" name="html"  style="width:800px;height:250px;">${fn:escapeXml(sysCodeTemplate.html)}</textarea>
							</div>
							<script type="text/javascript">
								var locale='zh_cn';
								FormDef.getEditor({
									height:240,
									width:227,
									lang:locale
								});
								editor.render("html");
							</script>
						</td>
					</tr>
				</table>
				<input type="hidden" name="id" value="${sysCodeTemplate.id}" />
			</form>

		</div>
	</div>
</body>
</html>
