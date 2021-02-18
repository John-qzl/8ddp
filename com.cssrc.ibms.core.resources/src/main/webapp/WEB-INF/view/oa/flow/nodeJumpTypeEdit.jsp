<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
	<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<f:link href="web.css"></f:link>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<f:link href="form.css"></f:link>
	<f:js pre="jslib/lang/common"></f:js>
	<f:js pre="jslib/lang/js"></f:js>

	<head>
		<title></title>
		<f:link href="Aqua/css/ligerui-all.css"></f:link>
		<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>

		<script src="${ctx}/jslib/util/util.js"></script>
		<script src="${ctx}/jslib/lg/base.js" type="text/javascript"></script>
		<script src="${ctx}/jslib/lg/ligerui.all.js"></script>
		<script src="${ctx}/jslib/lg/plugins/ligerResizable.js" type="text/javascript"></script>
		<script src="${ctx}/jslib/lg/plugins/ligerCheckBox.js" type="text/javascript"></script>
		<script src="${ctx}/jslib/lg/plugins/ligerComboBox.js" type="text/javascript"></script>
		<script src="${ctx}/jslib/lg/plugins/ligerGrid.js" type="text/javascript"></script>
		<script src="${ctx}/jslib/lg/plugins/ligerFilter.js" type="text/javascript"></script>
		<script src="${ctx}/jslib/lg/TreeDeptData.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>  <!--panel-body高度计算-->
		<script type="text/javascript">
			var dialog_;
			try {
				if(frameElement) {
					dialog_ = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
				}
			} catch(e) {

			}
			var manager;
			var url = "${ctx}/oa/flow/nodeJumpType/getData.do?nodeId=${nodeId}&actdefId=${actdefId}";
			$(function() {
				window['g'] = manager = $("#maingrid").ligerGrid({
					columns: [{
						display: '节点ID',
						name: 'nodeId',
						width: 150,
						isSort: false,
						align: 'left',
						type: 'text'
					}, {
						display: '节点名称',
						name: 'nodeName',
						type: 'text',
						width: 100,
						isSort: false,
						type: 'text',
						align: 'left'
					}, {
						display: '对应流程KEY',
						name: 'flowkey',
						type: 'text',
						width: 100,
						isSort: false,
						type: 'text',
						align: 'left'
					}, {
						display: '对应流程名称',
						name: 'subject',
						type: 'text',
						width: 100,
						isSort: false,
						type: 'text',
						align: 'left'
					}, {
						display: '节点类型',
						name: 'nodeType',
						type: 'text',
						isSort: false,
						width: 100,
						align: 'left'
					}, {
						display: '跳转名称',
						name: 'jumpName',
						isSort: false,
						type: 'text',
						width: 100,
						align: 'left',
						editor: {
							align: 'center',
							type: 'text'
						}

					}],

					enabledEdit: true,
					isScroll: false,
					checkbox: true,
					usePager: false,
					rownumbers: true,
					width: '100%',
					height: 'auto',
					allowHideColumn: false,
					alternatingRow: true,
					url: url,
					isSelected: function(row) {
						if(row.checked) {
							return true;
						} else {
							return false;
						}

					},
					tree: {
						columnName: 'nodeId'
					}
				});
			});
			function save() {
				var jumpSetting = manager.getCheckedRows();
				if(!jumpSetting || jumpSetting.length < 1) {
					alert("请至少选择一个节点");
					return;
				}
				var jumpSetting_ = new Array();
				$(jumpSetting).each(function(i, set) {
					var obj = new Object();
					obj.id = set.id;
					obj.nodeId = set.nodeId;
					obj.nodeName = set.nodeName;
					obj.nodeType = set.nodeType;
					obj.flowkey = set.flowkey;
					obj.subject = set.subject;
					obj.jumpName = set.jumpName;
					obj.prentFlowKey = set.prentFlowKey;
					obj.opinion = set.opinion;
					obj.relTab = set.relTab;
					obj.callActivity = set.callActivity;
					obj.checked = true;
					jumpSetting_.push(obj);
				})
				$.ajax({
					url: "save.do",
					method: "post",
					data: {
						"jumpSetting": JSON.stringify(jumpSetting_),
						"setId": $("#setId").val(),
						"actdefId": $("#actdefId").val()
					},
					success: function(reponse) {
						reponse = eval("(" + reponse + ")");
						alert(reponse.message);
						dialog_.close();
					}
				})
			}
		</script>
	</head>

	<body style="padding: 4px">
		<input type="hidden" id="setId" name="setId" value="${setId}" />
		<input type="hidden" id="actdefId" name="actdefId" value="${actdefId}" />
		<div class="panel">
			<div class="panel-top">
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" href="javascript:;" onclick="save()">
								 保存
							</a>
						</div>
						
						<div class="group">
							<a class="link back" href="javascript:;" onclick="dialog_.close()">
								 取消
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-body">
				<div id="maingrid"></div>
			</div>
			
		</div>
	</body>

</html>