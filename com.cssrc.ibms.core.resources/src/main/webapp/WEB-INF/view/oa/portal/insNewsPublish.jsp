<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ib" uri="http://www.ibms.cn/formFun"%>
<%@taglib prefix="ui" uri="http://www.ibms.cn/formUI"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻公告编辑</title>
<%@include file="/commons/portalCustom.jsp"%>
</head>
<body>
	<div id="toolbar" class="mini-toolbar" style="padding: 2px;">
		<table style="width: 100%;">
			<tr>
				<td style="width: 100%;" id="toolbarBody">
				<a class="mini-button" iconCls="icon-save" plain="true" onclick="onPublish">发布</a> 
				<a class="mini-button" iconCls="icon-close" plain="true" onclick="onCancel">关闭</a></td>
			</tr>
		</table>
	</div>
	<div id="p1" class="form-outer">
		<form id="form1" method="post">
			<div style="padding: 5px;">
				<table class="table-detail" cellspacing="1" cellpadding="0">
					<caption>新闻发布</caption>
					<tr>
						<th>新闻标题</th>
						<td><input id="pkId" name="newId" class="mini-textboxlist" style="width: 80%;" value="${newId}" text="${newTitle}" readonly/></td>
					</tr>
					<tr>
						<th>发布栏目</th>
						<td><input id="issuedCols" name="issuedColIds" class="mini-treeselect" url="${ctx}/oa/portal/insColumn/getNewsColumns.do" multiSelect="true" textField="name" valueField="colId" parentField="pid" required="true" checkRecursive="false" style="width: 90%" showFolderCheckBox="false" expandOnLoad="true" showClose="true" oncloseclick="clearIssuedCols" popupWidth="400" value="${selColIds}" /></td>
					</tr>
					<tr>
						<th>是否长期 ：</th>
						<td><ui:radioBoolean name="isLongValid" id="timeValid" required="true" onValueChanged="change" /></td>
					</tr>
					<tr id="shortValid" >
						<th>发布的有效时间</th>
						<td colspan="3">从<input class="mini-datepicker" id="startTime" name="startTime" required="true" style="width: 130px" />至<input class="mini-datepicker" id="endTime" name="endTime" style="width: 130px" required="true" />
					</tr>
				</table>
			</div>
		</form>
	</div>
	<ib:formScript formId="form1" baseUrl="oa/portal/insNews" />
	<script type="text/javascript">
		var form = new mini.Form("form1");
		function onPublish(){
			form.validate();
			if (!form.isValid()) {
				return;
			}
			var formData = $("#form1").serializeArray();
			console.log(formData);
			_SubmitJson({
				url : __ctx + '/oa/portal/insNews/published.do',
				method : 'POST',
				data : formData,
				success : function(result) {
						CloseWindow('ok');
						return;
				}
			});
		}
	
	
	
		$(function() {
			$(".upload-file").on('click', function() {
				var img = this;
				_UserImageDlg(true, function(imgs) {
					if (imgs.length == 0)
						return;
					$(img).attr('src', '${ctx}/sys/core/file/imageView.do?thumb=true&fileId=' + imgs[0].fileId);
					$(img).siblings('input[type="hidden"]').val(imgs[0].fileId);

				});
			});
		});

		function change(e) {
			if ("NO" == e.sender.getValue()) {
				$("#shortValid").show();
			} else if ("YES" == e.sender.getValue()) {
				$("#shortValid").hide();
				$("#endTime").find("input").val("2026-01-01 00:00:00");
				$("#startTime").find("input").val("2015-01-01 00:00:00");
			}
			//console.log($("#endTime").find("input").val("2026-01-30 18:11:35"));
		}
		$(function() {
			$("#shortValid").hide();

		});

		function changeIsImg(ck) {
			if (ck.checked) {
				$("#imgRow").css("display", "");
			} else {
				$("#imgRow").css("display", "none");
			}
		}
		function clearIssuedCols() {
			issuedCols.setValue("");
			issuedCols.setText("");
		}
	</script>
</body>
</html>