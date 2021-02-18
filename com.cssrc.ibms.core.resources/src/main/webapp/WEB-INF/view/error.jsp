<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%
	String basePath=request.getContextPath();
%>
<html>
	<head>
		<%@include file="/commons/include/get.jsp" %>
		<title>页面出错了</title>
			<script type="text/javascript">
				function showErrorMsg(){
					var url="<%=basePath%>/oa/system/sysErrorLog/geterror.do";
					var param={id:${errorCode}};
					$.post(url,param,function(data){
						var obj = $.parseJSON(data);
						$.ligerDialog.err("错误信息","错误码："+${errorCode},obj.error);
					}); 
				}
			</script>
	</head>
	<body>
	
	<table  border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
	    	<td><img src="${ctx}/styles/default/images/error/error_top.jpg" /></td>
	  	  </tr>
		  <tr>
		    <!--  <td height="200" align="center" valign="top" background="${ctx}/styles/default/images/error/error_bg.jpg"> -->
		    <td height="200" align="center" valign="top" >
		    	<table width="80%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td width="66%" valign="top" align="center">
		          	<table width="100%">
		          		<tr height="25"></tr>
		          		<tr height="25">
		          			<td>
		          			<span class="STYLE10">出错了呦,快快联系管理员!</span>
		          			</td>
		          		</tr>
		          		<tr height="70">
		          			<td>
		          			悄悄告诉你哦, 错误码：<a class="link" href="javascript:;" onclick="showErrorMsg()"  >${errorCode}</a>
		          			</td>
		          		</tr>
		          	</table>
		          	
		     	 </td>
		      </table>
		      </td>
		  </tr>    	 
		  <tr>
	    	<td><!-- <img src="${ctx}/styles/default/images/error/error_bootom.jpg" />--></td>
	      </tr>
		</table>
	</body>
</html>