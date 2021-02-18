$(function() {
	var dialog = window;// 调用页面的dialog对象(ligerui对象)
	if (frameElement) {
		dialog = frameElement.dialog;
	}
	if (isExtForm) {
		var formUrl = $('#divExternalForm').attr("formUrl");
		$('#divExternalForm').load(formUrl, function() {
		});
	}
	$(".taskopinion").each(
		function() {
			$(this).removeClass("taskopinion");
			var actInstId = $(this).attr("instanceId");
			var url=__ctx+"/oa/flow/taskOpinion/listform.do?actInstId="+ actInstId
			$(this).load(url);
		}
	);
	// ibms签章
	var conf = {
		signArea : 'ibms-sign-panel'
	}
	var ibmsSign = new IbmsWebSign(conf);
	ibmsSign.loadSign(signId);
});

// 显示审批历史
function showProcessRunInfo(obj) {
	var url = $(obj).attr("action");
	var title = $(obj).html().replace("<span></span>", "");
	/* jQuery.openFullWindow(url,"审批历史"); */
	DialogUtil.open({
		url : url,
		title : title,
		height : '800',
		width : '900',
		isResize : true
	});
};

// 催办
function urge(id) {
	ProcessUrgeDialog({
		actInstId : id
	});
};
// 撤销
function recover(runId) {
	FlowUtil.recover({
		runId : runId,
		backToStart : 1,
		callback : function() {
			// location.reload();
			if (window.opener) {
				try {
					window.opener.location.href = window.opener.location.href.getNewUrl();
				} catch (e) {
				}
				window.close();
			}
		}
	});
};
// 重新提交
function executeTask(procInstId) {
	var url = __ctx+"/oa/flow/task/toStart.do?instanceId=" + procInstId
			+ "&voteArgee=34";
	jQuery.openFullWindow(url);
};

// 打印表单
function printForm(runId) {
	var url = __ctx+"/oa/flow/processRun/printForm.do?runId=" + runId;
	jQuery.openFullWindow(url);
}

// 删除
function delByInstId(instanceId) {
	var url = __ctx+"/oa/flow/processRun/delDialog.do?instanceId=" + instanceId;
	url = url.getNewUrl();
	DialogUtil.open({
		height : 250,
		width : 500,
		title : '删除',
		url : url,
		isResize : true,
		// 自定义参数
		sucCall : function(rtn) {
			if (rtn != undefined) {
				try {
					window.opener.location.href = window.opener.location.href
							.getNewUrl();
				} catch (e) {
				}
				;
				dialog.close();
			}
		}
	});
};
function onClose(obj) {
	if (window.opener) {
		try {
			window.opener.location.href = window.opener.location.href
					.getNewUrl();
		} catch (e) {
		}
	}
	dialog.close();
};

// 转发
function divert() {
	forward({
		runId : runId
	});
}

function forward(conf) {
	if (!conf){
		conf = {};
	}
	var url = __ctx + '/oa/flow/proCopyto/forward.do?runId=' + conf.runId;
	var dialogWidth = 500;
	var dialogHeight = 250;
	conf = $.extend({}, {
		dialogWidth : dialogWidth,
		dialogHeight : dialogHeight,
		help : 0,
		status : 0,
		scroll : 0,
		center : 1,
		reload : true
	}, conf);

	url = url.getNewUrl();

	DialogUtil.open({
		height : conf.dialogHeight,
		width : conf.dialogWidth,
		title : '转发窗口',
		url : url,
		isResize : true,
		sucCall : function(rtn) {
		}
	});
}

// 追回
function redo(runId) {
	FlowUtil.recover({
		runId : runId,
		backToStart : 0,
		callback : function() {

		}
	});
}

// 导出word文档
function downloadToWord(runId) {
	var cl = $(".panel-body").clone();
	var form = cl.children();
	handFile(form);
	var frm = new com.ibms.form.Form();
	var url= __ctx + "/oa/flow/processRun/downloadToWord.do";
	frm.creatForm("bpmPreview",url);
	frm.addFormEl("form", cl.html());
	frm.addFormEl("runId", runId);
	frm.submit();
}

// 处理附件上传框，变成只显示附件名称
function handFile(form) {
	debugger
	$("div[name='div_attachment_container']", form).each(function() {
		var me = $(this);
		var attachment = $("a.attachment", me);
		var title = attachment.attr("title");
		me.empty();
		me.text(title);
	})
}