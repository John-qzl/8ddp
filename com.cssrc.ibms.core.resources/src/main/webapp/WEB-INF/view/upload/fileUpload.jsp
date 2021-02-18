<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.cssrc.com.cn/functions" prefix="ibms"%>
	
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<ibms:sysparam paramname="maxUploadSize" alias="maxUploadSize"></ibms:sysparam>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@include file="/commons/include/html_doctype.html"%>
<%
	String max = request.getParameter("max");
	String type = request.getParameter("type");
%>
<script>
function isFlash(){
	var isIE=!-[1,];
	if(isIE){
		try{
			var swf1=new ActiveXObject('ShonckwaveFlash.ShowckwaveFlash');
			return true;
		}catch(e){
			return false;
		}
	}else{
		try{
			var swf2=navigator.plugins['Shockwave Flash'];
			if(swf2== undefined){
				return false;
			}else{
				return true;
			}
		}catch(e){
			return false;
		}
	}
}

if(window.applicationCache){
	location.href="${ctx}/upload/html5FileUpload.do";
}else if(isFlash()){
	location.href="${ctx}/upload/flashFileUpload.do";
}else{
	location.href="${ctx}/upload/formFileUpload.do";
}

</script>





