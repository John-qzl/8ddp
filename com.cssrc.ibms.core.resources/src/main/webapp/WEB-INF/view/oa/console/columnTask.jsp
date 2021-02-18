<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>日常任务</title>
	<link rel="stylesheet" href="${ctx}/styles/home/home.css">
	
	<script type="text/javascript" src="${ctx}/js/lib/jquery.js"></script>
	
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
</head>
<body>
	<div class="block-III clear">
			<div class="basic-tabMenu-wrap clearFix">
				<ul class="basic-tabMenu-ul fl clearFix">
				
					<li class="basic-tabMenu basic-tabMenu-selected">
					
						<a href="javascript:void(0);" type="pendingMatters" onclick="loadContent(this)">代办（6）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:;" >草稿（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="alreadyMatters" onclick="loadContent(this)">已办（21）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="completedMatters" onclick="loadContent(this)">办结（33）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="accordingMatters" onclick="loadContent(this)">转办代理（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:;" >加签流转（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="myList" onclick="loadContent(this)">抄送转发（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
				</ul>
				<%-- 
				<div class="fr clearFix">
					<a href="${ctx}/oa/flow/task/pendingMatters.do" class="basic-tabMenu-more fl">+more</a>
					<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
				</div> --%>
			</div>
			
			<div class="basic-tabCont-wrap clearFix">
			<div class="task-list">
				<ol>
					<c:forEach items="${tasklist}" var="list" >							 				
							 <li>
								<a href="####" onclick="javascript:jQuery.openFullWindow('${ctx}/oa/flow/task/toStart.do?taskId=${list.id}')" class="fl msg">${list.subject}</a>
								<a href="####" class="fl uname">${list.creator}</a>
								<span class="fl date">${list.createTime}</span>
								<span class="fr unread">待办</span>
							</li>											
					</c:forEach> 
				</ol>
			</div>
			</div>
		</div>
</body>
</html>