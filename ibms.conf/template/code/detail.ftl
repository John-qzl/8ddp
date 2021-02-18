<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/jslib/ibms/scriptMgr.js"></script>
<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/jslib/ibms/subform.js"></script>
<script type="text/javascript">
	function afterOnload(){
		var afterLoadJs=[
		     			'<#noparse>${ctx}</#noparse>/jslib/ibms/formdata.js',
		     			'<#noparse>${ctx}</#noparse>/jslib/ibms/subform.js'
		 ];
		ScriptMgr.load({
			scripts : afterLoadJs
		});
	}
</script>
${html}
