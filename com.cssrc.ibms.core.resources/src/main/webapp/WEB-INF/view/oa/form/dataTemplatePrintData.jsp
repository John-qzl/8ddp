<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
 <head>
  <title>${reportName_REC}-报表</title>
 </head>
 <body>
 	<iframe id="reportFrame" width="100%" height="800" src="/ibms-Report/ReportServer?reportlet=${reportName_REC}_REC.cpt&ID=${requestScope.id}" scrolling="no"></iframe>
  </body>
</html>
 --%>