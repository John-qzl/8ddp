<%@page  isErrorPage="true" pageEncoding="UTF-8"%>
<%
	String basePath=request.getContextPath();
%>
<%@include file="/commons/include/get.jsp"%>
<html>
	<head>
		<title>您访问的页面不存在</title>
	</head>
	<body>
	 <table border="0" align="center" cellpadding="0" cellspacing="0">
	  <tr>
    	<td><!-- <img src="<%=basePath%>/styles/default/images/error/error_top.jpg" /> --></td>
  	  </tr>
	  <tr>
	    <!-- <td height="170" align="center" valign="top" background="<%=basePath%>/styles/default/images/error/error_bg.jpg"> -->
	    <td height="170" align="center" valign="top" >
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	        <tr>
	          <td width="100%" valign="top" align="center">
	          	<table width="100%"  align="center">
	          		<tr height="25" align="center">
	          			<td >
	          			<img src="<%=basePath%>/styles/default/images/error/error.jpg" style="margin-top:20px;"></td>
	          			</td>
	          		</tr>
	          		<tr height="40" align="center">
	          			<td>
	          			对不起，您访问的页面不存在！
	          			</td>
	          		</tr>
	          		<tr height="25"  align="center">
		          		<td>
		          			<div style="margin:0 100px 0 100px;">
		          		  		<a class="link reloaded" href="javascript:;" onclick="javascript:location.href='<%=basePath%>/logout';">重 新 登 录</a>
			        	  		<a class="link backed" href="javascript:history.back();" style="margin-left:30px;">后 退</a>
		          			</div>
		          		</td>
	          		</tr>
	          	</table>
	          	
	     	 </td>
	      </table>
	      </td>
	  </tr>    	 
	  <tr>
    	<td><!-- <img src="<%=basePath%>/styles/default/images/error/error_bootom.jpg" /> --></td>
      </tr>
	</table>
	</body>
</html>