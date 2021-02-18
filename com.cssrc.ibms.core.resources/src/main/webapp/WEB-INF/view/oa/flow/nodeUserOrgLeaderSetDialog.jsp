<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>发起人或上个任务执行人</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js"></script>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	$(function() {

	});

	function save() {
		var objUserType = $("[name='userType']:checked");
		var objType = $("[name='type']:checked");
		var strategy = $("[name='strategy']:checked");
		if (objUserType.length == 0) {
			$.ligerDialog.warn('请选择人员类型!', '提示');
			return;
		}
		if (objType.length == 0) {
			$.ligerDialog.warn('请选择领导类型!', '提示');
			return;
		}
		if (strategy.length == 0) {
			$.ligerDialog.warn('请选择查找策略!', '提示');
			return;
		}

		var varType = objType.val();
		var varTypeName = objType.attr("memo");
		var varUserType = objUserType.val();
		var varUserTypeName = objUserType.attr("memo");
		var strategyType = strategy.val();
		var obj = {};
		obj.json = "{userType:\"" + varUserType + "\",type:\"" + varType+ "\",strategy:\""+strategyType
				+ "\"}";
		obj.description = varUserTypeName + "的" + varTypeName;
		dialog.get("sucCall")(obj);
		dialog.close();
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">人员表单变量</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" onclick="save();" href="####">
								
								保存
							</a>
						</div>
						
						<div class="group">
							<a class="link close" onclick="dialog.close()" href="####">
								
								关闭
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="text-align: center; padding-top: 10px;">
			<table class="table-grid" width="90%">
				<tr>
					<td>人员类型</td>
					<td align="left">
						<input type="radio" name="userType" value="start" memo="发起人" <c:if test="${userType eq 'start'}">checked="checked"</c:if> />
						发起人
						<input type="radio" name="userType" value="prev" memo="上一任务执行人" <c:if test="${userType eq 'prev'}">checked="checked"</c:if> />
						上一任务执行人
					</td>
				</tr>

				<tr>
					<td>领导类型</td>
					<td align="left">
						<input type="radio" name="type" value="all" memo="分管领导" <c:if test="${type eq 'all'}">checked="checked"</c:if> />
						分管领导
						<input type="radio" name="type" value="leader" memo="分管主领导" <c:if test="${type eq 'leader'}">checked="checked"</c:if> />
						分管主领导
						<input type="radio" name="type" value="viceLeader" memo="分管副领导" <c:if test="${type eq 'viceLeader'}">checked="checked"</c:if> />
						分管副领导
					</td>
				</tr>

				<tr>
					<td>查找策略</td>
					<td align="left">
						<input type="radio" name="strategy" value="1" memo="为空时继续往上级组织查找" <c:if test="${strategy ==1}">checked="checked"</c:if> />
						为空时继续往上级组织查找
						<input type="radio" name="strategy" value="0" memo="为空时不继续往上查找" <c:if test="${strategy ==0}">checked="checked"</c:if> />
						为空时不继续往上查找
					</td>
				</tr>
			</table>

		</div>
	</div>
</body>
</html>