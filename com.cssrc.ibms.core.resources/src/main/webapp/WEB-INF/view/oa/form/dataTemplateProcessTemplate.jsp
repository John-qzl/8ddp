<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>流程监控脚本编辑</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
<script type="text/javascript">
	var defId="${defId}";
	$(function() {
        //Tab
         $("#tabHtml").ligerTab({height:600});
         $("a.save").click(function() {
			$("#processHtmlForm").attr("action","processSave.do");
			submitForm();
		  });
         
       //初始化流程监控模板
         $("a.reload").click(function() {
    			$.ligerDialog.confirm( "该操作只会初始化模板内容，不会格式化流程监控条件，是否确定操作！","提示信息", function(rtn) {
    				if(rtn){
    					var url=location.href;
    					href=url+"&reload=1";
    			    	location.href=href;
    				}else {
    					dialog.close();
    				}
     		});
     	});
	});
	
	function submitForm(){
		InitMirror.save();
		var options={};
		if(showResponse){
			options.success=showResponse;
		}
		var frm=$('#processHtmlForm').form();
		frm.ajaxForm(options);
		if(frm.valid()){
			frm.submit();
		}
	}
	
	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (!obj.isSuccess()) {
			$.ligerDialog.err('出错信息',"流程监控模板编辑HTML编辑失败",obj.getMessage());
			return;
		} else {
			$.ligerDialog.success('流程监控模板HTML编辑成功!','提示信息',function() {
				window.close();
			});
		}
	}
	
</script>
</head>
<body>
	<c:set var="processHtml" value="${htmlMap.processHtml}"></c:set>
	<c:set var="processCondition" value="${htmlMap.processCondition}"></c:set>

	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label"> 流程监控HTML模板编辑 </span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="btnSearch">保存</a>
					</div>
					
					<div class="group">
						<a class="link reload" id="reload">初始化</a>
					</div>
					
					<div class="group">
						<a class="link del" onclick="window.close()">关闭</a>
					</div>
					

				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="processHtmlForm" method="post" action="processSave.do">
				<div id="tabHtml">
					<div tabid="processHtml" title="流程监控HTML">
						
						<table class="table-detail" cellpadding="0" cellspacing="0"
							border="0">
							<tr>
								<th width="20%" style="text-align: left;line-height: 1.5em;">流程监控条件:
								<br><span style="color: red;">*条件格式必须为:[{flowkey:"test1",onlyShowLatest:"true",businessKeySql:""},{...}]</span>
								<br><span style="color: red;">*flowkey:监控流程别名；onlyShowLatest:是否显示最新记录；businessKeySql:关联关系(没有可以不填)</span>
								<br><span style="color: red;">*如果没有条件，将只显示当前记录对应的主流程信息</span>
								</th>
								<td><textarea id="processCondition" name="processConditionTemp" 
									codemirror="true" mirrorheight="60px" rows="10" cols="80">${processCondition}</textarea>		
								</td>
							</tr>
							<tr>
								<th width="20%">HTML编辑内容:<span class="required">*</span>
								</th>
								<td><textarea id="processHtml" name="processTempHtml"
										 codemirror="true" mirrorheight="430px" rows="10" cols="80">${fn:escapeXml(processHtml)}</textarea>
								</td>
							</tr>
						</table>
					</div>
					<input type="hidden" id="formKey" name="formKey" value="${formKey}"class="inputText" /> 
					<input type="hidden" id="tableId" name="tableId" value="${tableId}" class="inputText" />
					<input type="hidden" id="dataTempId" name="dataTempId" value="${dataTempId}" class="inputText" />
				</div>
			</form>

		</div>
	</div>
</body>
</html>
