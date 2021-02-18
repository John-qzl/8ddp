var dialog = frameElement.dialog; // 调用页面的dialog对象(ligerui对象)
function showRequest(formData, jqForm, options) {
	return true;
}

$(function() {
	$("input.day-input").focus(function() {
		$(this).select();
	});
	// total page layout
	$("#reminder-layout").ligerLayout({
		rightWidth : 210
	});
	// 是否发送催办信息的checkbox
	$("#needSendMsg").change(function() {
		var me = $(this), sendMsg = me.attr("checked");

		if (sendMsg) {
			$(".send-msg-tr").show();
		} else {
			$("select[name='times']").val(0);
			$(".send-msg-tr").hide();
		}
	});
	if (quarzCron||quarzCron!=""||curTime > 0) {
		$("#needSendMsg").attr("checked", "checked").trigger("change");
	}
	// TaskReminder form Edit Layout
	$("#reminder-div-tab").ligerTab({});
	// reminder action change handle
	change();
	// save reminder
	$("a.save").click(save);
	// new reminder
	$("a.add").click(add);
	// ckeditor Editor
	editorMail = ckeditor('mailText');
	editorMsg = ckeditor('msgContent');

	setTimeout(function() {
		var height = $("#condExp").height();
		condExpScriptEditor = CodeMirror.fromTextArea(document
				.getElementById("condExp"), {
			mode : "text/x-groovy",
			lineNumbers : true,
			matchBrackets : true
		});
		condExpScriptEditor.setSize(null, height);
	}, 0);
	setTimeout(function() {
		var height = $("#script").height();
		actionScriptEditor = CodeMirror.fromTextArea(document
				.getElementById("script"), {
			mode : "text/x-groovy",
			lineNumbers : true,
			matchBrackets : true
		});
		actionScriptEditor.setSize(null, height);
	}, 0);

	var _click = function() {
		var id = $(this).find("input.pk").val();
		url = __ctx + "/oa/flow/taskReminder/edit.do?actDefId=" + actDefId
				+ "&nodeId=" + nodeId + "&id=" + id;
		document.getElementById('goLocation').href = url;
		document.getElementById('goLocation').click();
	}
	$("#reminders-list-table tbody tr").click(_click);

	// 添加预警行
	$("#addWarning").click(function() {
		var tr = $("#warningTemplate").val();
		var WarningTable = $("#taskWarningSetList");
		WarningTable.append($(tr));
	});

});
/**
 * Action Change handler
 */
function change() {
	var s = $("#action").val();
	$(".sub").hide();
	$(".choose-assigner").hide();
	if (s == 7) {// 选择执行脚本
		$(".sub").show();
		if (actionScriptEditor) {
			actionScriptEditor.refresh();
		}
	}
	if (s == 5) {// 选择交办
		$(".choose-assigner").show();
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

/**
 * 添加
 */
function add() {
	url = __ctx + "/oa/flow/taskReminder/edit.do?actDefId=" + actDefId
			+ "&nodeId=" + nodeId + "&id=0";
	document.getElementById('goLocation').href = url;
	document.getElementById('goLocation').click();
}
/**
 * 保存
 */
function save() {
	handtaskWarningSet();
	condExpScriptEditor.save();
	actionScriptEditor.save();
	var name = $("#name").val();
	if (!name) {
		$.ligerDialog.warn("请输入任务任务催办名称", "提示信息");
		return;
	}
	var ctime = getTotalMinute($("#completeTr"));
	var stime = getTotalMinute($("#startTr"));
	// 每次时间间隔*催办次数。
	var etime = getTotalMinute($("#endTr")) * (parseInt($("#times").val()) - 1);
	if (ctime < stime + etime) {
		$.ligerDialog.warn('办结时间不能比催办时间短', "提示信息");
		return;
	}
	$(".ckeditor-editor").each(function() {
		$(this).val(CKEDITOR.instances[$(this).attr('name')].getData());
	});
	var rtn = $("#taskReminderForm").valid();
	if (!rtn)
		return;
	var url = __ctx + "/oa/flow/taskReminder/save.do";
	var para = $('#taskReminderForm').serialize();
	$.post(url, para, showResult);
}

function handtaskWarningSet() {
	var warningTable = $("#taskWarningSetList");
	var warningSetList = [];
	var tbody = $(warningTable.children()[1]);
	tbody.children().each(function() {
		var tr = $(this);
		var name = $("[name='warnName']", tr).val();
		if (name) {
			var reminderDueDay = $("[name='reminderDueDay']", tr).val();
			var reminderDueHour = $("[name='reminderDueHour']", tr).val();
			var reminderDueMinute = $("[name='reminderDueMinute']", tr).val();
			var relativeType = $("[name='relativeType']", tr).val();
			var level = $("[name='level']", tr).val();
			warningSetList.push({
				name : name,
				reminderDueDay : reminderDueDay,
				reminderDueHour : reminderDueHour,
				reminderDueMinute : reminderDueMinute,
				relativeType : relativeType,
				level : level
			});
		}
	});
	$("#warningSetJson").val(JSON.stringify(warningSetList));
}

function showResult(responseText) {
	var obj = new com.ibms.form.ResultMessage(responseText);
	if (!obj.isSuccess()) {
		$.ligerDialog.err('出错信息', "任务催办提醒失败", obj.getMessage());
		return;
	} else {
		$.ligerDialog.confirm(obj.getMessage() + ',是否继续操作?', '提示信息', function(
				rtn) {
			if (!rtn) {
				dialog.close();
			} else {
				url = __ctx + "/oa/flow/taskReminder/edit.do?actDefId="
						+ actDefId + "&nodeId=" + nodeId + "&id=0";
				document.getElementById('goLocation').href = url;
				document.getElementById('goLocation').click();
			}
		});
	}
}

/**
 * 
 */
function scriptSelectScript(obj) {
	ScriptDialog({
		callback : function(script) {
			var pos = scriptScriptEditor.getCursor();
			scriptScriptEditor.replaceRange(script, pos);
		}
	});
}
/**
 * 
 */
function condExpSelectScript(obj) {
	ScriptDialog({
		callback : function(script) {
			var pos = condExpScriptEditor.getCursor();
			condExpScriptEditor.replaceRange(script, pos);
		}
	});
}

function getTotalMinute(e) {
	var t = 0;
	$(e).find(".dayInput").each(function() {
		t += parseInt(3600 * this.value);
	});
	$(e).find(".hourInput").each(function() {
		t += parseInt(60 * this.value);
	});
	$(e).find(".minuteInput").each(function() {
		t += parseInt(this.value);
	});
	return t;
}

function constructFlowOperate(type) {
	var select = $("select[name='flowOperate']");
	select.html("");
	type = type.toLowerCase();
	switch (type) {
	case 'int':
	case 'number':
	case 'date':
		var eq = $("<option value='eq'>等于</option>");
		var ne = $("<option value='ne'>不等于</option>");
		var gt = $("<option value='gt'>大于</option>");
		var lt = $("<option value='lt'>小于</option>");
		select.append(eq);
		select.append(ne);
		select.append(gt);
		select.append(lt);
		break;
	case 'varchar':
		var eq = $("<option value='eq'>等于</option>");
		var ne = $("<option value='ne'>不等于</option>");
		select.append(eq);
		select.append(ne);
		break;
	}
}

function dateTimePicker() {
	WdatePicker({
		dateFmt : 'yyyy-MM-dd HH:mm:ss',
		alwaysUseStartDate : true
	});
	$(this).blur();
}
/**
 * Select a different flow var
 * 
 * @params obj,target dom object
 */
function selectFlowVar(obj, type) {
	var obj = $(obj);
	obj.qtip("destroy");
	if (type == 1) {
		var fname = obj.val();
		if (!fname) {
			return;
		}
		var ftype = obj.find("option:selected").attr("ftype");
		constructFlowOperate(ftype);

		var valueInput = $("<input name='flowValue' class='flowvalue' />");
		var oldValueInput = $("input[name='flowValue']");

		if ('date' == ftype.toLowerCase()) {
			valueInput.addClass("date");
			valueInput.focus(dateTimePicker);
		}
		oldValueInput.replaceWith(valueInput);

	} else if (type == 2) {
		var fname = obj.val();
		var pos = actionScriptEditor.getCursor();
		actionScriptEditor.replaceRange(fname, pos);
	}
	var opt = obj.find("option:selected")
	var fname = opt.attr("fname");
	var fdesc = opt.attr("fdesc");
	var ftype = opt.attr("ftype");
	ftype = dbTypeToGroovyType(ftype);
	var content = "" + "<table class='table-detail'>" + "<tr>" + "<th>名称</th>"
			+ "<td>" + fname + "</td>" + "</tr>" + "<tr>" + "<th>注释</th>"
			+ "<td>" + fdesc + "</td>" + "</tr>" + "<tr>" + "<th>类型</th>"
			+ "<td>" + ftype + "</td>" + "</tr>" + "</table>";
	obj.qtip({
		content : content
	});
}

function selectMailFlowVar(obj, txtId) {
	var obj = $(obj);
	var opt = obj.find("option:selected")
	var fname = opt.attr("fname");
	var fdesc = opt.attr("fdesc");
	CKEDITOR.instances[txtId].insertHtml("<br>${"+fname+"}");

}
function selectFormVarCopyTo(obj, txtId) {
	var obj = $(obj);
	var opt = obj.find("option:selected")
	var fname = opt.attr("fname");
	var fdesc = opt.attr("fdesc");
	var formVarCopyTo=$("#"+txtId).val();
	if(formVarCopyTo!=""){
		formVarCopyTo+=","+fname;
	}else{
		formVarCopyTo=fname;
	}
	$("#"+txtId).val(formVarCopyTo);

}
/**
 * Generate Express from Gui setting
 * 
 * @params obj,target dom object
 */
function generateExpress(obj) {
	var div = $(obj).closest("div.condExp-control");
	var flowVar = div.find("select[name='flowVar']").find("option:selected");
	var flowVarName = flowVar.val();
	var flowVarType = flowVar.attr("ftype");
	var flowVarOperate = div.find("select[name='flowOperate']").find(
			"option:selected").val();
	var flowVarValue = div.find("input[name='flowValue']").val();

	if (!flowVarName) {
		$.ligerDialog.warn("请选择流程变量！");
		return;
	}
	if (!flowVarOperate) {
		$.ligerDialog.warn("请选择变量操作类型变量！");
		return;
	}
	if (!flowVarValue) {
		$.ligerDialog.warn("请输入流程变量值！");
		return;
	}
	var exp = null;
	flowVarType = flowVarType.toLowerCase();
	switch (flowVarType) {
	case 'int':
	case 'number':
		exp = "Long userId=0l;\nif(" + flowVarName
				+ "!=null)userId=Long.valueOf(" + flowVarName + ");\n";
		switch (flowVarOperate) {
		case "eq":
			exp += "userId == " + flowVarValue+";\n";
			break;
		case "ne":
			exp += "userId != " + flowVarValue+";\n";
			break;
		case "gt":
			exp += "userId > " + flowVarValue+";\n";
			break;
		case "ge":
			exp += "userId >= " + flowVarValue+";\n";
			break;
		case "lt":
			exp += "userId < " + flowVarValue+";\n";
			break;
		case "le":
			exp += "userId <= " + flowVarValue+";\n";
			break;
		}
		break;
	case 'date':
		flowVarValue = "com.ibms.core.util.TimeUtil.convertString(\""
				+ flowVarValue + '\","yyyy-MM-dd")';
		switch (flowVarOperate) {
		case "eq":
			exp = flowVarValue + ".compareTo((Date)" + flowVarName + ") == 0;\n";
			break;
		case "ne":
			exp = flowVarValue + ".compareTo((Date)" + flowVarName + ") !=0;\n";
			break;
		case "gt":
			exp = flowVarValue + ".compareTo((Date)" + flowVarName + ") < 0;\n";
			break;
		case "lt":
			exp = flowVarValue + ".compareTo((Date)" + flowVarName + ") > 0;\n";
			break;
		}
		break;
	case 'varchar':
		switch (flowVarOperate) {
		case "eq":
			exp = flowVarName + ".equals(\"" + flowVarValue + "\")";
			break;
		case "ne":
			exp = " !" + flowVarName + ".equals(\"" + flowVarValue + "\")";
			break;
		}
		break;
	}
	var pos = condExpScriptEditor.getCursor();
	condExpScriptEditor.replaceRange("\n"+exp, pos);
}

/**
 * 数据库类型到Groovy类型的转换
 * 
 * @params type relation data type
 * @return groovy type
 */
function dbTypeToGroovyType(type) {
	type = type.toLowerCase();
	var t;
	switch (type) {
	case 'int':
		t = 'int';
		break;
	case 'number':
		t = 'double';
		break;
	case 'date':
		t = 'java.lang.Date';
		break;
	case 'varchar':
	case 'clob':
		t = 'java.lang.String';
		break;
	default:
		t = type;
	}
	return t;
};
// 选择交办人
function chooseAssigner() {
	UserDialog({
		isSingle : true,
		callback : function(userId, fullname) {
			if (userId == '' || userId == null || userId == undefined)
				return;
			$("input[name='assignerId']").val(userId);
			$("input[name='assignerName']").val(fullname);
		}
	});
};

function delWaringLine(me) {
	$(me).closest("tr").remove();
}
