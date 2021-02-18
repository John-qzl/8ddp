<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
 <head>
  <title>${reportName_LIST}-报表</title>
 </head>
 <body>
 	<iframe id="reportFrame" width="100%" height="800" src="/ibms-Report/ReportServer?reportlet=${reportName_LIST}.cpt&CON_SQL=${CON_SQL}" scrolling="no"></iframe>
  </body>
</html>
