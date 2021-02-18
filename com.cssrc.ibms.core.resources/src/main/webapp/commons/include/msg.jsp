<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.cssrc.ibms.core.web.controller.BaseController"%>
<%@page import="com.cssrc.ibms.core.util.result.ResultMessage"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/lg/plugins/ligerDialog.js"></script>
<%
ResultMessage _obj_=(ResultMessage)session.getAttribute(BaseController.Message);
if(_obj_!=null){
	session.removeAttribute(BaseController.Message);
%>
<script type="text/javascript">
$(function(){
	<%
	  if(_obj_.getResult()==ResultMessage.Success){
	%>
		$.ligerDialog.success('<p><font color="green"><%=_obj_.getMessage()%></font></p>');
	
	<%}
	  else{
		if(!"".equals(_obj_.getCause())){
	%>
		$.ligerDialog.err('','<%=_obj_.getMessage()%>','<%=_obj_.getCause()%>');
		<%}else{%>
		$.ligerDialog.warn('<p><font color="red"><%=_obj_.getMessage()%></font></p>');
	<%   }
	   }%>
});
</script>
<%
} %>

<%-- <%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/lg/plugins/ligerDialog.js"></script>
<script type="text/javascript">
$(function(){
	if(message!=null){
		if(${message.result==1}){
			$.ligerDialog.success('<p><font color="green">${message.message}</font></p>');
		}else{
			if(${message.cause!=""}){
				$.ligerDialog.err('','${message.message}','${message.cause}');
			}else{
				$.ligerDialog.warn('<p><font color="red">${message.cause}</font></p>');
			}
		}
	}
});
</script>
 --%>
