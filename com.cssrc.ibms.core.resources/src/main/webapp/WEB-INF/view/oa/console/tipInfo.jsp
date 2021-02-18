<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<%@include file="/commons/include/get.jsp" %>
	<title>友情提示</title>
</head>
<body>
<table align="center"  border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><img src="${ctx}/styles/default/images/error/error_top.jpg" /></td>
	</tr>
	  <tr>
	    <td height="200" align="center" valign="top" >
    		<table width="80%" border="0" cellspacing="0" cellpadding="0">
	        	<tr>
		          <td width="66%" valign="top" align="center">
		          	<table width="100%">
		          		<tr height="25"></tr>
		          		<tr height="25">
		          			<td>
		          			<span class="STYLE10">${content}</span>
		          			</td>
		          		</tr>
		          	</table>
		          	
		     	 </td>
		      </table>
	      </td>
	  </tr> 

</table>
</body>
</html>
