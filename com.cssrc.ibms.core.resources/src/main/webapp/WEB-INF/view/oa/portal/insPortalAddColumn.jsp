<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/formFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>门户编辑</title>
<%@include file="/commons/portalCustom.jsp"%>
</head>
<body>
	<ibms:toolbar toolbarId="toolbar1" pkId="${insPortal.portId}" excludeButtons="remove,prevRecord,nextRecord" />
	<div id="p1" class="form-outer">
		<form id="form1" method="post">
			<div class="form-inner">
				<input id="pkId" name="portId" class="mini-hidden" value="${insPortal.portId}" />
				<table class="table-detail" cellspacing="1" cellpadding="0">
					<caption>请选择本门户显示的栏目</caption>
						<td><div id="insPortCols" name="insPortCols" class="mini-checkboxlist" repeatItems="4" repeatLayout="table" textField="name" valueField="colId" value="${colId}" url="${ctx}/oa/portal/insColumn/getByIsClose.do"></div></td>
					</tr>
				</table>
			</div>
		</form>
	</div>
	<script type="text/javascript">
        mini.parse();
    </script>
	<ibms:formScript formId="form1" baseUrl="oa/portal/insPortal" />
</body>
</html>