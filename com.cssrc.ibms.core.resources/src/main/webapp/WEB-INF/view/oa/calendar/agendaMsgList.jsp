<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/include/html_doctype.html"%>
<html>
  <head>
	<%@include file="/commons/include/get.jsp"%>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
    <title>日程列表</title>
	<script type="text/javascript">
	</script>
  </head>
  <body>
  	<div class="panel">
		<div class="panel-body">
			<display:table name="agendaMsgList" id="agendaMsgItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column property="subject" title="日程标题" style="width:150px;text-align:center"></display:column>
				<display:column property="contents" title="内容" style="text-align:center"></display:column>
				<display:column property="replyer" title="来源" style="width:150px;text-align:center"></display:column>
				<display:column title="发表时间" style="width:150px;text-align:center" >
					<fmt:formatDate value="${agendaMsgItem.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
			</display:table>
			<ibms:paging tableId="agendaMsgItem"/>
		</div>
	</div>
  </body>
</html>
