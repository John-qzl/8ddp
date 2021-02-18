<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>账号策略管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<%@ taglib prefix="ibms" uri="http://www.cssrc.com.cn/paging" %>

	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/CopyRoleDialog.js"></script>
    <script type="text/javascript">
    	function editStrategy(strategyId){
    		var href = "${ctx}/oa/system/accountStrategy/edit.do?strategyId="+strategyId;
    		var title = "编辑策略信息";
    		$.ligerDialog.open({
    			title:title, 
    			url:href, 
    			height:400,
    			width:600,
    			isResize:true,
    			sucCall:function(rtn){
    				//rtn作为回调对象，可进行定制和扩展
    				if(!(rtn == undefined || rtn == null || rtn == '')){
    					window.location.reload(true);
    				}
    			}
    		});
    	}
    </script>
</head>
<body>
	<div class="panel">
		<div class="panel-body">
		    <display:table name="accountStrategyList" id="accountStrategy" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
				<display:column property="id" title="序号" style="text-align:left;width:40px;" sortName="id"></display:column>
				<display:column property="strategy_name" title="策略名称" style="text-align:left" sortName="strategy_name"></display:column>
				<display:column property="strategy_explain" title="策略说明" style="text-align:left" sortName="strategy_explain"></display:column>
				<display:column title="启用状态" style="text-align:left" >
					<c:choose>
						<c:when test="${accountStrategy.is_enable == '1'}">
							<span style="color: green;">启用</span>
						</c:when>
						<c:otherwise>
							<span style="color: red;">禁用</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column property="strategy_value" title="策略值" style="text-align:left" sortName="strategy_value"></display:column>
				<display:column title="修改" media="html"  style="width:50px;text-align:center" class="rowOps">
					<a onclick="editStrategy(${accountStrategy.id})" class="link edit" >编辑</a>
				</display:column>
			</display:table>
		</div> 			
	</div> 
</body>
</html>


