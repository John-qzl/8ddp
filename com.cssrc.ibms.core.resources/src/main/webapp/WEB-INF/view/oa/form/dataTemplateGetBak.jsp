<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>

<table style="width: 100%">
	<thead>
		<th style="padding-left: 10px; align:center; width: 35%">备份时间</th>
		<th style="padding-left: 10px; align:center; width: 10%">版本</th>
		<th style="padding-left: 10px; align:center; width: 40%">数据</th>
		<th style="padding-left: 10px; align:center; width: 15%">备注</th>
		
	</thead>
	<tbody>
		<c:forEach items="${bakList}" var="bak">
			<tr>
				<td style="padding-left: 10px; align:center; width: 35%">${f:longDate(bak.bakDate)}</td>
				<td style="padding-left: 10px; align:center; width: 10%">${bak.version}</td>
				<td style="padding-left: 10px; align:center; width: 40%">${bak.bakData}</td>
				<td style="padding-left: 10px; align:center; width: 15%">${bak.remark}</td>
			</tr>
		</c:forEach>
	</tbody>

</table>
