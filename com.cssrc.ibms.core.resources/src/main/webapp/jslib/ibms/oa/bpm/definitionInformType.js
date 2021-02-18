var dialog = frameElement.dialog; // 调用页面的dialog对象(ligerui对象)
$(function() {
	var options = {};
	if (showResponse) {
		options.success = showResponse;
	}
	$('#informTypeForm').ajaxForm(options);
	editorMail = ckeditor('mailText');

	$(".save").click(function() {

		$(".ckeditor-editor").each(function() {
			$(this).val(CKEDITOR.instances[$(this).attr('name')].getData());
		});
		$("#informConf").val(getMsgConf());
		$('#informTypes').val(getMsgTypeList('informType'));
		$('#informTypeForm').submit();
	});
	$("#reminder-div-tab").ligerTab({});
});

function getMsgConf() {
	var msgConf = {};
	msgConf.mail = {};
	msgConf.mail.mailSubject = $("#mailSubject").val();
	msgConf.mail.copyTo = $("#copyTo").val();
	msgConf.mail.copyToID = $("input:hidden[name='copyToID']").val();
	msgConf.mail.formVarCopyTo = $("#formVarCopyTo").val();
	msgConf.mail.mailText = $("#mailText").val();
	msgConf.innerMsg = {};
	msgConf.RTX = {};
	return JSON2.stringify(msgConf);
}
function getMsgTypeList(id) {
	var msgTypeList = [];
	$("input[name='" + id + "']").each(function() {
		var me = $(this), val = me.val(), state = me.attr("checked");
		if (state)
			msgTypeList.push(val);
	});
	return msgTypeList.join(',');
}

function showResponse(responseText) {
	var obj = new com.ibms.form.ResultMessage(responseText);
	if (obj.isSuccess()) {
		$.ligerDialog.success(obj.getMessage(), "提示信息", function(rtn) {
			if (rtn) {
				dialog.close();
			}
		});
	} else {
		$.ligerDialog.err("出错信息", "编辑手机访问设置失败", obj.getMessage());
	}
}
/**
 * Select Template
 */
function slectTemplate(txtId, isText) {
	var objcondExpCode = document.getElementById(txtId);
	TemplateDialog({
		isText : isText,
		callback : function(content) {
			if (isText)
				jQuery.insertText(objcondExpCode, content);
			else {
				CKEDITOR.instances[txtId].setData(content);
			}
		}
	});
};

function selectMailFlowVar(obj, txtId) {

	var obj = $(obj);
	var opt = obj.find("option:selected")
	var fname = opt.attr("fname");
	var fdesc = opt.attr("fdesc");
	CKEDITOR.instances[txtId].insertHtml('<br>${' + fname + '}');

}
function selectFormVarCopyTo(obj, txtId) {
	var obj = $(obj);
	var opt = obj.find("option:selected")
	var fname = opt.attr("fname");
	var fdesc = opt.attr("fdesc");
	var formVarCopyTo = $("#" + txtId).val();
	if (formVarCopyTo != "") {
		formVarCopyTo += "," + fname;
	} else {
		formVarCopyTo = fname;
	}
	$("#" + txtId).val(formVarCopyTo);

}
