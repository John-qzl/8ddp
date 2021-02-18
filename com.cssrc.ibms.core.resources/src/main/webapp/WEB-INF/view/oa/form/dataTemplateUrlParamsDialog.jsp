<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>url参数设置</title>
<%@include file="/commons/include/form.jsp"%>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/catCombo.js"></script>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=bpmFormDef"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormTableDialog.js"></script>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	$(function() {
		$("#urlParamsFormSave").click(function() {
			save();
		});
		var conf = dialog.get("conf");
		__DataTemplate__.initUrlParamsData(conf);
	});
	function save() {
		var jsonArrStr = getJsonArrStr();
		dialog.get("sucCall")(jsonArrStr);
		dialog.close();
	}
	//装箱json数组
	function getJsonArrStr() {
		var jsonArr = new Array();
		$("#urlParamsTbl tbody tr").each(function(index) {
			var me = $(this);
			var isCustomParam = 1;
			if (typeof me.find("[var='isCustomParam']").attr("checked") == 'undefined') {
				isCustomParam = 0;
			} 
			var key = me.find("[var='key']").val();
			var value = me.find("[var='value']").val();
			var urlParams = new urlParamsObj(isCustomParam,key,value);
			jsonArr.push(urlParams);
		});
		return JSON2.stringify(jsonArr);
	}
	//urlParams obj
	function urlParamsObj(isCustomParam,key, value) {
		var obj = new Object();
		obj.isCustomParam = isCustomParam;
		obj.key = key;
		obj.value = value;
		return obj;
	}
</script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/DataTemplateEdit.js"></script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">url参数设置</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="urlParamsFormSave" href="####">保存</a>
						</div>
						<div class="group">
							<a class="link close" href="####" onclick="dialog.close();">关闭</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="urlParamsForm" method="post" action="save.do">
				<!-- 设置按钮  start-->
				<div tabid="urlParamsSetting" id="table" title="设置按钮">
					<div class="table-top-left">
						<div class="toolBar" style="margin: 0;">
							<div class="group">
								<a class="link add" id="btnSearch"
									onclick="__DataTemplate__.addUrlParams()">添加</a>
							</div>
							
							<div class="group">
								<a class="link del " id="btnSearch"
									onclick="__DataTemplate__.delUrlParams();">删除</a>
							</div>
						</div>
					</div>
					<table id="urlParamsTbl" class="table-grid">
						<thead>
							<tr>
								<th width="5%">选择</th>
								<th width="5%">自定义参数</th>
								<th width="15%">key鍵</th>
								<th width="10%">value值</th>
								<th width="5%">管理</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</form>
			<!-- 设置url  end-->
		</div>
	</div>
	<!-- url模板 -->
	<div id="urlParamsTemplate" style="display: none;">
		<table cellpadding="1" cellspacing="1" class="table-detail">
			<tr var="urlParamsTr">
				<td var="index"><input class="pk" type="checkbox" name="select">
				</td>
				<td var=""><input type="checkbox" checked="checked" var="isCustomParam" onclick="__DataTemplate__.changeTd(this);">
				</td>
				<td var=""><input type="text" var="key" value=""></td>
				<td var="valueTd"><input type="text" var="value" value="">
				</td>
				<td><a class="link del" href="####"
					onclick="__DataTemplate__.delTr(this)"></a></td>
			</tr>
		</table>
	</div>
	<!-- 字段选择模板  -->
	<div id="selectFieldOrTextTemplate" style="display: none;">
		<select var="value" id="selectField" name="selectField">
			<c:forEach var="field" items="${dataTemplateFields}" varStatus="i">
				<option value="${field.fieldName}" >${field.fieldName}</option>
			</c:forEach>
		</select>
		<input type="text" id="valueTemplate" var="value" value="">
	</div>
</body>
</html>