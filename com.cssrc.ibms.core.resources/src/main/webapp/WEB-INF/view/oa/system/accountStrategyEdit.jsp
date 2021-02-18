<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>策略信息编辑窗口</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript">
		var dialog = null; //调用页面的dialog对象(ligerui对象)
		var strategyId = null;
		$(function() {
			strategyId = $("#strategyId").val();
			if (frameElement) {
				dialog = frameElement.dialog;
			}
			
			//回填checkbox值
			var strategyValueId = $("#strategyValueId").val();
			var strategyValues = strategyValueId.split(",");
			for(var j=0;j<strategyValues.length;j++){
				var i = j + 1;
				var liOrder = "li"+i;
				var checkboxOrder = "checkbox" + i;
				if(strategyValues[j] == "1"){
					$("#"+liOrder).css("display","block");
					$("#"+checkboxOrder).attr("checked",'true');
				}
			}
		});
		//保存策略信息
		function saveStrategy(){
			var strategyExplain = $("#strategyExplain").val();
			var isEnable = $("#isEnable").val();
			var strategyValue = '';
			var strategyValue1 = $("#strategyValue").val();
			if(strategyValue1!=undefined){
				strategyValue = strategyValue1;
			}else{
				var strategyValue2 = $("input[name='strategyValue']");
				var strategyExplain1 = '';
				for(var i=0;i<strategyValue2.length;i++){
					var j = i + 1;
					var liOrder = "li"+j;
					if(strategyValue2[i].checked){
						strategyValue += "1,";
						strategyExplain1 +=　$("#"+liOrder).text();
					}else{
						strategyValue += "0,";
					}
				}
				strategyExplain = strategyExplain1;
				strategyValue = strategyValue.substring(0,9);
			}
			
			$.ajax({
				url : "${ctx}/oa/system/accountStrategy/save.do",
				type : "POST",
				data : {
					strategyExplain : strategyExplain,
					isEnable : isEnable,
					strategyValue : strategyValue,
					strategyId : strategyId
				},
				dataType:'json',
				async : true,
				success : function(data) {
					if (data.success=='false') {
						$.ligerDialog.error(data.message);
					}else{
						$.ligerDialog.confirm(data.message+",是否继续操作", "提示信息",function(rtn) {
							if (!rtn) {
								dialog.get("sucCall")('ok');
								dialog.close();
							}
						});
					}
				}
			});
		}
		
		//checkbox状态改变事件
		function changeState(box){
			var i = $(box).val();
			var liOrder = "li"+i;
			if($(box)[0].checked){
				$("#"+liOrder).css("display","block");
			}else{
				$("#"+liOrder).css("display","none");
			}
		}
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="accountStrategySave" onclick="saveStrategy()">保存</a></div>
				
				<div class="group"><a class="link close" href="javascript:dialog.get('sucCall')('ok');dialog.close();">关闭</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="accountStrategyForm" method="post" action="save.do">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
				<tr>
					<th width="20%">策略名称: </th>
			       	<td>
			       		<span>${accountStrategy.strategy_name}</span>
			       	</td>
				</tr>
				<tr>
					<th width="40%">策略说明: </th>
					<td>
						<c:choose>
							<c:when test="${accountStrategy.id == 2}">
								<div id="strategyExplain" name="strategyExplain" style="width:400px;height:95px;" validate="{required:false}">
									<li id="li1" style="display:none">必须包含特殊字符;</li>
									<li id="li2" style="display:none">必须包含大写字母;</li>
									<li id="li3" style="display:none">必须包含小写字母;</li>
									<li id="li4" style="display:none">必须包含数字;</li>
									<li id="li5" style="display:none">不包含用户的用户名;</li>
								</div>
							</c:when>
							<c:otherwise>
								<textarea id="strategyExplain" name="strategyExplain" style="width:400px;height:60px;" validate="{required:false}">${accountStrategy.strategy_explain}</textarea>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th width="20%">策略启用状态: </th>
					<td>
						<select id="isEnable" name="isEnable" class="select" value="${accountStrategy.is_enable}" style="width:70px;">
							<option value="1" <c:if test="${accountStrategy.is_enable == '1' }">selected</c:if>>启用 </option>
							<option value="0" <c:if test="${accountStrategy.is_enable == '0' }">selected</c:if>>禁用 </option>
						</select>
					</td>
				</tr>
				<tr>
					<th width="20%">策略值: </th>
					<td>
						<c:choose>
							<c:when test="${accountStrategy.id == 2}">
								<label><input id="checkbox1" type="checkbox" name="strategyValue" value="1" onclick="changeState(this)"/>必须包含特殊字符</label>
								<br/>
								<label><input id="checkbox2" type="checkbox" name="strategyValue" value="2" onclick="changeState(this)"/>必须包含大写字母</label>
								<br/>
								<label><input id="checkbox3" type="checkbox" name="strategyValue" value="3" onclick="changeState(this)"/>必须包含小写字母</label>
								<br/>
								<label><input id="checkbox4" type="checkbox" name="strategyValue" value="4" onclick="changeState(this)"/>必须包含数字</label>
								<br/>
								<label><input id="checkbox5" type="checkbox" name="strategyValue" value="5" onclick="changeState(this)"/>不包含用户的用户名</label>
							</c:when>
							<c:otherwise>
								<input type="text" onkeyup="value=value.replace(/[^(\d)]/g,'')" id="strategyValue" value='${accountStrategy.strategy_value}' style="width:53px;" class="inputText" validate="{required:false}"  />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<input type="hidden" id="strategyId" value="${accountStrategy.id}" />
			<input type="hidden" id="strategyValueId" value="${accountStrategy.strategy_value}" />
		</form>
	</div>
</div>
</body>
</html>
