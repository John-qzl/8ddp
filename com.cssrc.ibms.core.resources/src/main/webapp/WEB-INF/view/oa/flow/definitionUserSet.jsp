<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/commons/include/get.jsp" %>
	<title>人员设置</title>
	<%@include file="/commons/include/nodeUserConditionJS.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ScriptDialog.js" ></script>
	<script type="text/javascript">
		$(function(){
			$("a.del").unbind("click");
			$("div.group > a.link.update").unbind('click');
			$('#btnReflesh').attr('href',window.location.href)

			$("body").delegate("input[name=groupNo]","change",changeGroupNo);
		});
		

		function refresh(){
			location.href=location.href.getNewUrl();
		};

		/**
		* 保存分组号
		*/
		function saveGroupNo(nodeId){
			var conditionIds=[],
				groupNos=[];
			var url = __ctx+"/oa/flow/userCondition/updateGroup.do";
			var id = "table_" + nodeId;
			var table = $('#' + id);
			table.find("tbody.data").find("input[name=groupNo]").each(function(){
				var _this = $(this);
				if(_this.val()!=_this.attr("ivalue")){
					var groupNo = _this.val();
					var tr = _this.closest("tr");
					var conditionId = $("input[name=conditionId]", tr).val();
					conditionIds.push(conditionId);
					groupNos.push(groupNo);
				}
			});
			if(conditionIds.length==0){
				return;
			}
			var params = {
					conditionIds:conditionIds.join(","),
					groupNos:groupNos.join(",")
				};
			$.post(url,params,function(data){
				var obj = new com.ibms.form.ResultMessage(data);
				if (obj.isSuccess()) {
					$.ligerDialog.success( obj.getMessage(),function(){
						window.location.reload();
					});
					
				} else {
					$.ligerDialog.err('出错了!',obj.getMessage());
				}			
			});
		};

		/**
		* 分组号值变更
		*/
		function changeGroupNo(){
			var _this=$(this);
			//_this.addClass("inputChange");

			var td = _this.closest("td");
			var tr = _this.closest("tr");
		
			var groupNo = _this.val();
			
			groupNo = groupNo.replace(/(^\s*0*)|(\s*$)/g,"");
			if(!/^\d+$/.test(groupNo)){
				groupNo=1;
			}
			_this.val(groupNo);
			
			$("div[name=groupNo]",td).text(groupNo).show();

			var url = __ctx+"/oa/flow/userCondition/updateGroup.do";

			var conditionId = $("input[name=conditionId]",tr).val();
			var params = {
				conditionId:conditionId,
				groupNo:groupNo
			};

			var oldGroup = _this.attr("ivalue");
			//_this.hide();

			if(oldGroup==groupNo){
				tr.removeClass("inputChange");
				return;
			}else{
				if(!tr.hasClass("inputChange"))
					tr.addClass("inputChange");
			}

		};
	</script>
	
	<base target="_self" />
</head>
<body>	
<div class="panel">
	<c:if test="${empty nodeTag}">
		<div class="panel-container">
			<div class="panel-top">
				<jsp:include page="incDefinitionHead.jsp">
			    	<jsp:param value="人员设置" name="title"/>
			    	<jsp:param value="${returnUrl }" name="returnUrl"/>
				</jsp:include>
			</div>
	  		<f:tab curTab="userSet" tabName="flow%"/>
	</c:if>
	<c:if test="${!empty nodeTag}">
	      <div class="panel-container">
	</c:if>
			<div class="panel-body">
				<h2 class="setting">流程定义节点人员设置</h2>
				<a type="hidden" id="btnReflesh" onclick="refresh()"></a>
				<form action="saveUser.do" method="post" id="defUserForm" style="margin: 0 5px;">
					<!--
	                   	作者：rzq
	                   	时间：2017-05-16
	                   	描述：给form增加margin：0 5px。优化样式
                   -->
					<input type="hidden" name="defId" value="${defId}"/>
					<input type="hidden" name="nodeId" value="${nodeId}"/>
					<input type="hidden" id="parentActDefId" value="${parentActDefId}"/>
					
					<c:forEach items="${nodeUserMapList}" var="nodeUserMap" varStatus="i">
							  <div class="table-top">
									<div class="table-top-left">${nodeUserMap.nodeName}(${nodeUserMap.nodeId})</div>
									<div class="table-top-right">
												
										<div class="toolBar" style="margin:0;">
											<div class="group"><a class="link add" id="btnSearch" onclick="conditionDialog('table_${nodeUserMap.nodeId}')">添加</a></div>
											
											<div class="group"><a class=" update link " onclick="conditionDialog('table_${nodeUserMap.nodeId}',true)">修改</a></div>
											
											<div class="group"><a class="link del " id="btnSearch" onclick="delRows('table_${nodeUserMap.nodeId}');">删除</a></div>
											
											<div class="group"><a class="link save" onclick="saveGroupNo('${nodeUserMap.nodeId}')" id="btnSaveGroupNo" >保存批次号</a></div>
										</div>								
								    </div>
								</div>						
							   <%@include file="/commons/include/nodeUserCondition.jsp"%>
					</c:forEach>
	
					<div style="height:40px"></div>
				</form>
			</div>				
		</div>
</div>
<div id="divScriptData" style="display: none;">
	<a href="####" id="btnScript" class="link var" title="常用脚本" onclick="selectScript()">常用脚本</a>
	<ul>
		<li>可以使用的流程变量,[startUser],开始用户,<li>[startUser],上个任务的用户[prevUser]。</li>
		<li>表达式必须返回Set&lt;String&gt;集合类型的数据,集合元素为用户Id。</li>
	</ul>
	<textarea id="txtScriptData" rows="10" cols="80" style="height: 200px;width:480px"></textarea>
</div>
</body>
</html>
