
/**
 * 表单模板打印按钮
 */
function printReport(name, pk, displayId) {
	var result = true;
	// getPreScript 在 表单模板DataTemplate.ftl中设计
	result = getPreScript(name);
	if (result) {
		var conf = getParam(name);
		if (!conf.report) {
			$.ligerDialog.warn("conf.report 参数不能为空");
			return;
		}
		if (conf.report.type == 'report') {
			goReport(conf, name, displayId, pk);
		} else if (conf.report.type == 'office') {
			if(conf.report.titleUrl){
				$.ajax({
					url : __ctx + conf.report.titleUrl + "?pk="+pk,
					type : "post",
					async:false,
					success : function(title){
						conf.report.title=title;
					}
					,error : function(){
						$.ligerDialog.warn("连接超时，请联系系统管理员！","提示信息");
					} 
				})
			}
			goOffice(conf, pk, name);
		} else {
			goReport(conf, name, displayId, pk);
		}
		// getAfterScript 在 表单模板DataTemplate.ftl中设计
		result = getAfterScript(name);
		if (!result) {
			$.ligerDialog.warn("后置脚本执行出错");
		}
	} else {
		$.ligerDialog.warn("前置脚本执行出错");
	}
}

/**
 * 流程task打印按钮
 * 
 * @param operatorType
 */
function printReportFlow(operatorType) {
	// taskToPrint(__formDefId__,__pk__)
	if (operatorType) {
		var result = true;
		// beforeClick 在oa/flow/incToolBarStart.jsp
		result = beforeClick(operatorType);
		if (result) {
			var conf = getParam(operatorType);
			if (conf.report.type == 'report') {
				goReport(conf, name, null, null);
			} else if (conf.report.type == 'office') {
				goOffice(conf, name);
			} else {
				goReport(conf, name, null, null);
			}
			// afterClick 在oa/flow/incToolBarStart.jsp
			result = afterClick(operatorType);
			if (!result) {
				$.ligerDialog.warn("后置脚本执行出错");
			}
		} else {
			preViewByFormDef();
		}
	} else {
		preViewByFormDef();
	}
}

function preViewByFormDef() {
	var mill = (parseInt(Math.random() * 10000)).toString();
	var reportUrl = __ctx + '/oa/system/reportTemplate/' + __formDefId__
			+ '/formDefIdView.do?'
	reportUrl += 'mill=' + mill;
	reportUrl += "&pk=" + __pk__;
	window.open(reportUrl);
}

/**
 * 在线浏览或者下载报表函数
 * 
 * @param conf
 * @param name
 */
function goReport(conf, name, displayId, pk) {
	var mill = (parseInt(Math.random() * 10000)).toString();
	if (conf.refUserSign) {
		// 签名下载
		var reportUrl = __ctx + '/oa/system/reportTemplate/'
				+ conf.report.title + '/signdown.do?'
		reportUrl += 'refUserSign=' + conf.refUserSign;
		reportUrl += '&mill=' + mill;
		location.href = reportUrl;
	} else {
		if (conf.report.ext) {
			downloadReport(conf, name, displayId, pk);
		} else {
			var reportUrl = __ctx + '/oa/system/reportTemplate/'
					+ conf.report.title + '/titleView.do?'
			reportUrl += 'mill=' + mill;
			if (conf.rpcrefname) {
				reportUrl = reportUrl + "&rpcrefname=" + conf.rpcrefname;
			}
			if (displayId && displayId != null) {
				reportUrl += "&displayId=" + displayId;
			}
			if (pk && pk != null) {
				reportUrl += "&pk=" + pk;
			}
			window.open(reportUrl);
		}

	}

}

/**
 * 下载report 报表
 * 
 * @param conf
 * @param name
 */
function downloadReport(conf, name, displayId, pk) {
	var mill = (parseInt(Math.random() * 10000)).toString();
	$.ajax({
		type : "POST",
		url : __ctx + '/oa/system/reportTemplate/' + conf.report.title
				+ '/get.do?mill=' + mill,
		success : function(responseText, statusText) {
			var result = eval('(' + responseText + ')');
			result = result[0];
			if (result.result == 1) {
				var mill = (parseInt(Math.random() * 10000)).toString();
				var reportUrl=result.report.reportSeverlet+result.report.fileName;
				reportUrl+="&mill="+mill;
				// 设置参数以及默认参数
				$.each(result.params, function(i, obj) {
					if (obj.name) {
						var pval_ = getPro(obj.name, conf.param);
						if (pval_ != null) {
							// 用户已经设置参数值
							reportUrl = reportUrl + "&" + obj.name + "="
									+ pval_;
						} else if (obj.value_) {
							// 取后台默认参数值
							reportUrl = reportUrl + "&" + obj.name + "="
									+ obj.value_;
						} else {
							// 报表模板中没有参数名设置
							reportUrl = reportUrl + "&" + obj.name + "=";
						}
					}
				})
				// 下载格式
				reportUrl = reportUrl + "&format=" + conf.report.ext;
				// 默认参数 dataId 以及 displayId
				reportUrl += '&id=' + pk + "&displayId=" + displayId;
				// 分页输出格式
				if (conf.report.extype && conf.report.extype != null
						&& conf.report.extype != '') {
					reportUrl = reportUrl + "&extype=" + conf.report.extype;
				}
				reportUrl = cjkEncode(reportUrl);
				location.href = reportUrl;
			} else {
				$.ligerDialog.warn(result.message);
			}

		}

	})
}
/**
 * 匹配参数name获取值
 * 
 * @param key
 * @param obj
 * @returns
 */
function getPro(key, obj) {
	for ( var p in obj) {
		if (p == key) {
			return obj[p];
		}
	}
	return null;
}

function goOffice(conf, pk, name) {
	var officeId = "";
	var mill = (parseInt(Math.random() * 10000)).toString();
	$.ajax({
		type : "POST",
		async : false,
		url : __ctx + '/oa/system/officeTemplate/' + conf.report.title
				+ '/get.do?mill=' + mill,
		success : function(responseText, statusText) {
			var result = eval('(' + responseText + ')');
			if (result.result == 1) {
				officeId = result.office.officeid;
			} else {
				$.ligerDialog.warn(result.message);
			}
		}
	});
	if(officeId!=""){
		var url = __ctx + "/oa/system/officeTemplate/" + officeId + "/" + pk
		+ "/extOffice.do?mill="+mill;
		if(conf.report.ext){
			url+="&ext="+conf.report.ext;
		}
		var sucCall=function(rtn) {
			if(rtn.result == 1){
				window.location.href = __ctx+"/file/read?ext=ext&opath=/office/" + rtn.filename;
			}
		}
		var conf = {
			height : 800,
			width : 800,
			title : 'office报表模板',
			url : url,
			isResize : true,
			// 自定义参数
			sucCall : sucCall
		}
		DialogUtil.open(conf);
	}
}
// cjkEncode对中文参数重新编码
function cjkEncode(text) {
	if (text == null) {
		return "";
	}
	var newText = "";
	for (var i = 0; i < text.length; i++) {
		var code = text.charCodeAt(i);
		if (code >= 128 || code == 91 || code == 93) { // 91 is "[", 93 is "]".
			newText += "[" + code.toString(16) + "]";
		} else {
			newText += text.charAt(i);
		}
	}
	return newText;
}



// 顶部工具条导出报表按钮
function expprint(name,displayId) {
	var __url=location.href;
	var panel=$("div[displayid='"+displayId+"']")[0];
	var filterkey=$(panel).attr("filterkey");
	var result = true;
	// getPreScript 在 表单模板DataTemplate.ftl中设计
	result = getPreScript(name);
	if (result) {
		var conf = getParam(name);
		if (!conf.report) {
			$.ligerDialog.warn("conf.report 参数不能为空");
			return;
		}
		var mill = (parseInt(Math.random() * 10000)).toString();
		var reportUrl = __ctx + '/oa/system/reportTemplate/'
		+ conf.report.title + '/titleList.do?'
		reportUrl += 'mill=' + mill;
		if (conf.rpcrefname) {
			reportUrl = reportUrl + "&rpcrefname=" + conf.rpcrefname;
		}
		if (displayId && displayId != null) {
			reportUrl += "&displayId=" + displayId;
		}
		if(filterkey){
			reportUrl += "&filterkey=" + filterkey;
		}
		var selectPK="";
		$("input[type='checkbox'][disabled!='disabled'][class='pk']:checked").each(function(i,ck){
			selectPK+=$(ck).val()+",";
		})
		if(selectPK!=""){
			selectPK=selectPK.substring(0,selectPK.length-1);
			reportUrl += "&pk=" + selectPK;
		}
		window.open(reportUrl);

		// getAfterScript 在 表单模板DataTemplate.ftl中设计
		result = getAfterScript(name);
		if (!result) {
			$.ligerDialog.warn("后置脚本执行出错");
			
		}
	} else {
		$.ligerDialog.warn("前置脚本执行出错");
	}
}