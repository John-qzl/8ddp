//保存常用语设置
function save() {
	// 获取那个是选择的tab
	var id = tab.getSelectedTabItemID();
	var selectDiv = $("div[tabid='" + id + "']");
	var type = $("#type", selectDiv).val();
	var approvalItem = $("#approvalItem", selectDiv).val();
	var flowTypeId, defKey,defNodeKey,default_,code;
	if (type == 2) {
		var aryTypeId = [];
		$("span[id^='type_']", selectDiv).each(function(i, ch) {
			aryTypeId.push($(ch).attr("typeId"));
		});
		flowTypeId = aryTypeId.join(",");
	} else if (type == 3) {
		var aryDefKey = [];
		$("span[id^='ref_']", selectDiv).each(function(i, ch) {
			aryDefKey.push($(ch).attr("referKey"));
		});
		defKey = aryDefKey.join(",");
	} else if(type == 5){
		//表示是根据流程节点设置审批意见模板
		var aryDefNodeKey = [];
		$("span[id^='defnode_']", selectDiv).each(function(i, ch) {
			aryDefNodeKey.push($(ch).attr("defNodeKey"));
		});
		defNodeKey = aryDefNodeKey.join(",");
		default_=$("#approvalItem_default_").get(0).checked?1:0;
		code=$("#approvalItem_code").val();
	}
	var param = {
		type : type,
		approvalItem : approvalItem,
		flowTypeId : flowTypeId,
		defKey : defKey,
		defNodeKey : defNodeKey,
		default_ : default_,
		code : code,
		defNodeKey : defNodeKey
	}
	var url = __ctx + "/oa/flow/taskApprovalItems/save.do";
	$.post(url, param, function(msg) {
		var obj = new com.ibms.form.ResultMessage(msg);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm('操作成功,继续操作吗?', '提示', function(rtn) {
				if (!rtn) {
					location.href = 'list.do?isAdmin=' + isAdmin;
				} else {
					location.href = location.href.getNewUrl();
				}
			});
		} else {
			$.ligerDialog.error(obj.getMessage(), '提示');
		}
	});
}
// 选择流程
function referDefinition(defId) {
	var url = __ctx + '/oa/flow/definition/defReferSelector.do?defId=' + defId;
	referDef = $.ligerDialog.open({
		title : '选择流程',
		mask : true,
		isResize : true,
		height : 500,
		url : url,
		width : 700,
		buttons : [ {
			text : '确定',
			onclick : returnFlow
		}, {
			text : '取消',
			onclick : closeWin
		} ]
	});
};
// 删除流程
function delRefer(refKey) {
	$('#ref_' + refKey).remove();
	var jsonTemp = $("input#refDefKey").val();
	var json = JSON2.parse(jsonTemp);
	// 删除json对应的数据
	delete json[refKey];
	$("input#refDefKey").val(JSON2.stringify(json));
}
// 返回选择的流程
var returnFlow = function(item, dialog) {
	var contents = $("iframe", dialog.dialog).contents()
	var chKeys = contents.find("input.pk[name=defKey]:checked");
	var defId = contents.find("#bpmDefId").val();
	var aryDefKey = [];
	var json = {};
	var defKeyTemp, subjectTmep, spanHtml = '';
	$.each(chKeys, function(i, ch) {
		aryDefKey.push($(ch).val());
		json[$(ch).val()] = $(ch).attr("defSubject");

	});
	var defKeyJson = $("input#refDefKey").val();
	if (defKeyJson) {
		// 处理先选择的数据和以前的数据是否有重复，去除重复
		var jsontemp = JSON2.parse(defKeyJson);
		for ( var i in jsontemp) {
			// console.info(jsontemp[i]);
			for ( var j in json) {
				if (i != j) {
					json[i] = jsontemp[i];
				}
			}
		}
	}
	for ( var k in json) {
		spanHtml = spanHtml + "<span id='ref_" + k + "' referKey=" + k + ">"
				+ json[k]
				+ "<a href='javascript:void(0);' onclick='delRefer(\"" + k
				+ "\")'>删除</a>&nbsp;&nbsp;&nbsp;&nbsp;</span>"
	}
	if (aryDefKey.length > 0) {
		$("span#refDefArray").html(spanHtml);
		$("input#refDefKey").val(JSON2.stringify(json));
		dialog.close();
	} else {
		$.ligerDialog.warn('请选择引用流程!', '提示');
	}
}
// 选择流程分类
function flowTypeSelector() {
	var url = __ctx + '/oa/system/globalType/flowTypeSelector.do';
	referDef = $.ligerDialog.open({
		title : '选择流程分类',
		mask : true,
		isResize : true,
		height : 500,
		url : url,
		width : 700,
		buttons : [ {
			text : '确定',
			onclick : returnFlowType
		}, {
			text : '取消',
			onclick : closeWin
		} ]
	});
}

// 返回选择的流程分类信息
var returnFlowType = function(item, dialog) {
	var contents = $("iframe", dialog.dialog).contents()
	var chIds = contents.find("input.pk[name=typeId]:checked");
	var arytypeId = [];
	var json = {};
	var typeIdTemp, subjectTmep, spanHtml = '';
	$.each(chIds, function(i, ch) {
		arytypeId.push($(ch).val());
		json[$(ch).val()] = $(ch).attr("defSubject");
	});

	var flowJson = $("input#refFlowKey").val();
	if (flowJson) {
		// 处理先选择的数据和以前的数据是否有重复，去除重复
		var jsontemp = JSON2.parse(flowJson);
		for ( var i in jsontemp) {
			// console.info(jsontemp[i]);
			for ( var j in json) {
				if (i != j) {
					json[i] = jsontemp[i];
				}
			}
		}
	}
	for ( var k in json) {
		spanHtml = spanHtml + "<span id='type_" + k + "' typeId=" + k + ">"
				+ json[k] + "<a href='javascript:void(0);' onclick='delType(\""
				+ k + "\")'>删除</a>&nbsp;&nbsp;&nbsp;&nbsp;</span>"
	}

	if (arytypeId.length > 0) {
		$("span#flowTypeArray").html(spanHtml);
		$("input#refFlowKey").val(JSON2.stringify(json));
		dialog.close();
	} else {
		$.ligerDialog.warn('请选择流程分类!', '提示');
	}
}
// 删除流程分类
function delType(typeId) {
	$('#type_' + typeId).remove();
	var jsonTemp = $("input#refFlowKey").val();
	var json = JSON2.parse(jsonTemp);
	// 删除json对应的数据
	delete json[typeId];
	$("input#refFlowKey").val(JSON2.stringify(json));
}

// 选择流程节点
function referFlowTask(defId) {
	var url = __ctx + '/oa/flow/nodeSet/nodesetDefReferSelector.do?defId='
			+ defId;
	referDef = $.ligerDialog.open({
		title : '选择流程',
		mask : true,
		isResize : true,
		height : 500,
		url : url,
		width : 700,
		buttons : [ {
			text : '确定',
			onclick : returnNodeSet
		}, {
			text : '取消',
			onclick : closeWin
		} ]
	});
};
var returnNodeSet=function(item, dialog){
	var contents = $("iframe", dialog.dialog).contents()
	var chIds = contents.find("input.pk[name=nodeId]:checked");
	var arytypeId = [];
	var json = {};
	var typeIdTemp, subjectTmep, spanHtml = '';
	$.each(chIds, function(i, ch) {
		arytypeId.push($(ch).val());
		json[$(ch).val()] = $(ch).attr("nodeSubject");
	});

	var flowJson = $("input#defNodeKey").val();
	if (flowJson) {
		// 处理先选择的数据和以前的数据是否有重复，去除重复
		var jsontemp = JSON2.parse(flowJson);
		for ( var i in jsontemp) {
			// console.info(jsontemp[i]);
			for ( var j in json) {
				if (i != j) {
					json[i] = jsontemp[i];
				}
			}
		}
	}
	for ( var k in json) {
		spanHtml = spanHtml + "<span id='defnode_" + k + "' defNodeKey=" + k + ">"
				+ json[k] + "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' onclick='delNodeSet(\""
				+ k + "\")'>删除</a>&nbsp;&nbsp;&nbsp;&nbsp;</span>"
	}

	if (arytypeId.length > 0) {
		$("span#defNodeArray").html(spanHtml);
		$("input#defNodeKey").val(JSON2.stringify(json));
		dialog.close();
	} else {
		$.ligerDialog.warn('请选择流程节点!', '提示');
	}

}
//删除流程节点
function delNodeSet(defNodeKey) {
	$('#defnode_' + defNodeKey).remove();
	var jsonTemp = $("input#defNodeKey").val();
	var json = JSON2.parse(jsonTemp);
	// 删除json对应的数据
	delete json[defNodeKey];
	$("input#defNodeKey").val(JSON2.stringify(json));
}
// 关闭对话框
var closeWin = function(item, dialog) {
	dialog.close();
}