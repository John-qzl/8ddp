<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title><title>表单页</title>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@include file="/commons/include/color.jsp"%>
<link rel="stylesheet" href="${ctx}/layui/css/layui.css">
<script type="text/javascript" src="${ctx}/layui/layui.js"></script>
 <script type="text/javascript" charset="utf-8" src="${ctx}/editor/jquery.js"></script>
 <script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
   <script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
   <link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-all.css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/web.css">

<style type="text/css">

input.btn {
    		background:url(${ctx}/dpImg/edit.png) no-repeat;
    		width:62px;height:21px;
    		border: none;
    		margin: 5px 10px;
    		padding:0;
    	}

td{
	white-space: nowrap;
}
.dpCheckbox {
    vertical-align: middle!important;
    margin: 0 2px;
}

.dpInputBtn {
	vertical-align: middle!important;
	background-color: #3EAAF5;
	color: white;
	border: none!important;
	margin: 0 2px;
}

.dpInputText {
	vertical-align: middle!important;
	width: 60px!important;
	height:22px!important;
	margin: 0 2px;
}

</style>
<script type="text/javascript">

	function formEdit(){
	//
		var id=$("#id").val();
		var url="${ctx}/dp/form/existInstance.do";
		var params={id:id};
		$.post(url,params,function(data){
			if(data.success=="true"){

				DialogUtil.open({
					url:"${ctx}/dp/form/modifyTable.do?id="+id,
					width:1050,
					height:700,
					title:"修改检查表",
					sucCall:function(rtn){
						window.location.reload();
					}
				});

			}else{
				$.ligerDialog.error(data.msg,"修改失败");
			}
		});
	}
</script>
</head>
<body>
  <div class="panel-toolbar" style="background:none;">
    <%-- <div class="toolBar">
      <div class="group"><a class="link edit" formId="${id}" href="####" onclick="javascript:formEdit()">修改</a></div>
    </div> --%>
  </div>
	${html}<br/>
	<input type="hidden"  id="id" name="id" value="${id }"/>
</body>
</html>
