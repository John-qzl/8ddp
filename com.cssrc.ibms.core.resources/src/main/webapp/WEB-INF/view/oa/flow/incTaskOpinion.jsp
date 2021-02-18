<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<table class="table-detail">
	<tr>
		<th style="width: 100px" id="approvalOpinions">意见</th>
		<td>
			<c:if test="${!empty taskAppItems}">
						常用语选择:
						<select id="selTaskAppItem" onchange="addComment(${empty spyjModel})">
					<option value="" style="text-align: center;">-- 请选择 --</option>
					<c:forEach var="item" items="${taskAppItems}">
						<option value="${item}">${item}</option>
					</c:forEach>
				</select>
				<br>
			</c:if>
			<input type="hidden" id="voteContent" name="voteContent" value="${taskOpinion.opinion}">
			<input type="hidden" id="voteContent_code" name="voteContent_code" value="${taskOpinion.voteCode}">
			<div id="spyj_voteContent" name="spyj_voteContent" style="width: 80%; min-height: 50px; height:auto;word-break: break-all">
				<%--动态加载审批意见模板 --%>
			</div>
		</td>
	</tr>
</table>




