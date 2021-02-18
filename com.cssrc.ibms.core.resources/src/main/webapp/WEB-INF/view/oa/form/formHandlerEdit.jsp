<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@include file="/commons/include/get.jsp" %>
<%@include file="/commons/include/ueditor.jsp" %>
<html>
<head>
<title>表单预览</title>
<!-- 页面首先执行customForm.jsp中的FormInit.js。初始化表单  绑定"自定义对话框"，绑定"自定义查询"，绑定外部链接link ，权限等 -->
<!-- 页面首先执行customForm.jsp中的SelectorInit.js。 人员、角色、组织、岗位选择器 html初始化-->
<%@include file="/commons/include/customForm.jsp" %>
<!--start  自定义js文件，css文件  -->
	${headHtml}
<!--end    自定义js文件，css文件  -->
<script type="text/javascript">
$(function() {
	var win;
	$("a.save").click(function() {
		var rtn=CustomForm.validate();
		if(rtn){
			var data=CustomForm.getData();
			//设置表单数据
			$("#txtJsonData").val(data);
			if(!win){
				var obj=$("#divJsonData");
				win= $.ligerDialog.open({ target:obj , height: 300,width:500, modal :true}); 
			}
			win.show();
		}
		
	});
});

function closeWin(){
	
	try{
		frameElement.dialog.close();
		}catch(e){
			window.close();
		}
}
</script>


</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">表单预览</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">查看数据</a>
					</div>
				
					<div class="group">
						<a class="link close" href="javascript:closeWin()">关闭</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div id="divJsonData" style="display: none;">
			<textarea id="txtJsonData" rows="10" cols="80" style="height: 225px;width:480px"></textarea>
			</div>
			<form >
				<div type="custform">
					<div class="panel-detail">
						${bpmFormDef.html}
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>

