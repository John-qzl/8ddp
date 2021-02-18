<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>发送邮件</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
<script type="text/javascript">
	//添加联系人
	function addLinkMan(email){
			var url="${ctx}/oa/oa/oaLinkman/addNew.do?emailName="+email;
			DialogUtil.open({
		        height:600,
		        width: 800,
		        title : '添加联系人',
		        url: url, 
		        isResize: true
		    });
	}
</script>
<f:link href="from-jsp.css"></f:link></head>
<body>
	<div class="panel">
	   <div class="panel-toolbar">
			<div class="toolBar">
				
				<div class="group"><a class="link back" href="edit.do">返回</a></div>
			</div>
		</div>
		
		<div class="common">
		  	<c:choose>
		  		<c:when test="${ message.result==1}">
		  			<div class="result success">${message.message }<br><a href="edit.do" css="link">是否继续?</a></div>
		  		</c:when>
		  		<c:otherwise>
		  			<span class="fail"></span><span >${message.message }</span>
		  		</c:otherwise>
		  	</c:choose>
		</div>
		
		<div class="common"  style="text-align: center;margin-top: 20px;"  >
		
			<c:if test="${not empty  addrList}">
			    <div style="font-size: medium;padding-bottom: 10px;">亲！以下朋友还不是您的联系人，可将它们收入联系人列表哟！</div>
			    <table id="tab" cellpadding="1" class="table-grid table-list" cellspacing="1">
				   <tr>
				   		<th width="20%"  style="text-align:center;">邮件地址</th><th width="20%"  style="text-align:center;">添加联系人</th>
				   	</tr>
				   	<c:forEach items="${addrList}" var="email" >
				     <tr>
			           <td  style="text-align:center;" >${email }</td><td   style="text-align:center;"><a class="link add"  onclick="addLinkMan('${email}')">添加</a></td>
				     </tr>
				   	</c:forEach>
				</table>
			</c:if>
		
		</div>
		
		
	</div>
</body>
</html>


