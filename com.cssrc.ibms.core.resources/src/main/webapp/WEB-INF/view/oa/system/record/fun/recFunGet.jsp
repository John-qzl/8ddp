
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>子系统功能点明细</title>
	<%@include file="/commons/include/getById.jsp" %>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/record/recFunEdit.js"></script>
<script type="text/javascript">
	$(function() {
			__RecFun__.init();
	});
</script>
</head>
<body>
	<c:if test="${recFun!=null}">
		<div class="panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">${recFun.funName}</span>
				</div>
				
			</div>
			<div class="panel-body">
				
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th width="20%">功能点名称:</th>
								<td>${recFun.funName}</td>
							</tr>
							<tr>
								<th width="20%">功能点别名:</th>
								<td>${recFun.alias}</td>
							</tr>
							
							<tr>
								<th width="20%">功能点图标:</th>
								<td>
									<img alt="" src="${recFun.icon}">
								</td>
							</tr>
							<tr>
								<th width="20%">默认地址:</th>
								<td>${recFun.defaultUrl}</td>
							</tr>
							<tr>
								<th width="20%">是否有子节点:</th>
								<td>
									<c:if test="${recFun.isFolder==0}">否</c:if>
									<c:if test="${recFun.isFolder==1}">是</c:if>
								</td>
							</tr>
							<tr style="display:none">
								<th width="20%">显示到菜单:</th>
								<td>
									<c:if test="${recFun.isDisplayInMenu==0}">否</c:if>
									<c:if test="${recFun.isDisplayInMenu==1}">是</c:if>
								</td>
							</tr>
							<tr>
								<th width="20%">默认打开:</th>
								<td>
									<c:if test="${recFun.isOpen==0}">否</c:if>
									<c:if test="${recFun.isOpen==1}">是</c:if>
								</td>
							</tr>
							<tr>
								<th width="20%">是否打开新窗口:</th>
								<td>
									<c:if test="${recFun.isNewOpen==0}">否</c:if>
									<c:if test="${recFun.isNewOpen==1}">是</c:if>
								</td>
							</tr>
							<tr>
								<th width="20%">同层顺序:</th>
								<td>
									${recFun.sn}
								</td>
							</tr>
					</table>
				
			</div>	
			<textarea style="display: none;" id="buttonArr" name="buttonArr" >${fn:escapeXml(recFun.buttonArr)}</textarea>					
			<div class="panel-page">
			
					<table id="buttonItem" class="table-grid table-list" id="0" cellpadding="1" cellspacing="1">
				   		<thead>
					   		<tr>
					   			<th width="10%">序号</th>
					   			<th width="10%">按钮ID</th>
					   			<th width="30%">名称</th>
					    		<th width="50%">别名</th>				    	
					    	</tr>
				    	</thead>
				    	<tbody>
				    	</tbody>
				   	 </table>
				   	 </br>
				   	 </br>
				   	 </br>
			   	 <c:if test="${recFun.buttonArr==[]||recFun.buttonArr==''}">
			   	 	<div id="notSetButton"  width="90%">
				    	当前没有按钮信息
				    </div>
				 </c:if>					
			</div>
		</div>
		<!-- 按钮模板 -->
		  <div  id="buttonTemplate"  style="display: none;">
				<table cellpadding="1" cellspacing="1"  class="table-detail">
					<tr var="buttonTr">
							<td style="text-align: center;" var="no">
			    				${cnt.count+1}
			    			</td>
			    			<td><input readonly="readonly" type="text" var="unique" value=""></td>
				    		<td style="text-align: center;">
			    				<input class="inputText" readonly="readonly" type="text" style="width: 95%;"  var="desc" value="">
			    			</td>
			    			<td style="text-align: center;">
			    				<input class="inputText" readonly="readonly" type="text" style="width: 95%;"  var="name" value="">
			    			</td>
					</tr>
				</table>
			</div>
		</div>
	</c:if>
</body>
</html>
