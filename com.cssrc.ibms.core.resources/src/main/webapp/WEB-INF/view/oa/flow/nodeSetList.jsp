<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>节点设置管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormDialog.js"></script>

<script type="text/javascript">
	//流程定义ID
	var actDefId = "${bpmDefinition.actDefId}";
	var parentActDefId = "${parentActDefId}";
	function save() {
		var type = $("#bpmFormType").val();
		var isEmpty = isEmptyForm();
		if (!isEmpty) {
			if (type == -1) {
				$.ligerDialog.error('请设置流程实例业务表单!', '提示信息');
				return;
			} else if (type == 0) {
				var bpmFormKey = $("#bpmFormKey").val();
				if (!bpmFormKey || bpmFormKey == 0) {
					$.ligerDialog.error('请设置流程实例业务表单!', '提示信息');
					return;
				}
			} else if (type == 1) {
				var bpmFormUrl = $("#bpmFormUrl").val();
				if (!bpmFormUrl || bpmFormUrl == '') {
					$.ligerDialog.error('请设置流程实例业务表单!', '提示信息');
					return;
				}
			}
		}
		$('#dataForm')[0].submit();
	}

	function isEmptyForm() {
		var isEmpty = true;
		var globalFormObj = $("div[name='globalFormUrl']");
		var globalUrlObj = globalFormObj.find('input[name="formUrlGlobal"]');
		var globalDetailObj = globalFormObj
				.find('input[name="detailUrlGlobal"]');
		var globalUrl = (globalUrlObj != undefined) ? globalUrlObj.val() : "";
		var globalDetailUrl = (globalDetailObj != undefined) ? globalDetailObj
				.val() : "";
		var globalFormKey = ($("#defaultFormKey") != undefined) ? $(
				"#defaultFormKey").val() : 0;
		if (globalUrl != "" || globalFormKey != 0 || globalDetailUrl != "") {
			isEmpty = false;
		}
		$("div[name='nodeForm']")
				.each(
						function() {
							var formKeyObj = $(this).find(
									'input[name="formKey"]');
							var formKey = (formKeyObj != undefined) ? formKeyObj
									.val()
									: 0;
							var formUrlObj = $(this).find(
									'input[name="formUrl"]');
							var formUrl = (formUrlObj != undefined) ? formUrlObj
									.val()
									: "";
							var formDetailObj = $(this).find(
									'input[name="detailUrl"]');
							var formDetail = (formDetailObj != undefined) ? formDetailObj
									.val()
									: "";
							if (formKey != 0 || formUrl != ""
									|| formDetail != "") {
								isEmpty = false;
							}
						});
		return isEmpty;
	}

	//选择全局表单
	function selectGlobalForm(me) {
		var globalFormObj = $(me).closest("div[name='globalForm']");
		FormDialog({
			callback : function(ids, names, tableId) {
				var bpmFormObj = $("div[name='bpmForm']");

				$("select[name='bpmFormType']").val("0");
				bpmFormObj.show();
				$("div.url_bpmForm").hide();

				$("input.formKey", globalFormObj).val(ids);
				$("input.formKey", bpmFormObj).val(ids);
				$("input.formDefName", globalFormObj).val(names);
				$("input.formDefName", bpmFormObj).val(names);
				//给表单添加 超链接，使之能看到表单明细
				var namesUrl = "<a target='_blank' href=" + __ctx
						+ "/oa/form/formHandler/edit.do?formDefId=" + ids
						+ " >" + names + "</a>";
				$("span[name='spanForm']", globalFormObj).html(namesUrl);
				$("span[name='spanForm']", bpmFormObj).html(namesUrl);
			}
		})
	}

	//选择业务表单
	function selectBpmForm(obj) {
		FormDialog({
			callback : function(ids, names, tableId) {
				var tdObj = $(obj).parent();
				$("input.formKey", tdObj).val(ids);
				$("input.formDefName", tdObj).val(names);
				//给表单添加 超链接，使之能看到表单明细
				var namesUrl = "<a target='_blank' href=" + __ctx
						+ "/oa/form/formHandler/edit.do?formDefId=" + ids
						+ " >" + names + "</a>";
				$("span[name='spanForm']", tdObj).html(namesUrl);
			}
		})
	}

	//选择节点表单
	function selectNodeForm(obj) {
		FormDialog({
			callback : function(ids, names, tableId) {
				var tdObj = $(obj).parent();
				$("input.formKey", tdObj).val(ids);
				$("input.formDefName", tdObj).val(names);
				$("input.tableId", tdObj).val(tableId);
				//给表单添加 超链接，使之能看到表单明细
				var namesUrl = "<a target='_blank' href=" + __ctx
						+ "/oa/form/formHandler/edit.do?formDefId=" + ids
						+ " >" + names + "</a>";
				$("span[name='spanForm']", tdObj).html(namesUrl);
				// 是否显示子表授权功能
				$.ajax({
					type : "POST",
					url : __ctx + "/oa/form/formDef/isSubTable.do",
					data : {
						formKey : ids
					},
					dataType : "json",
					success : function(res) {
						var result = eval('(' + res + ')');
						if (result.success && obj.id == 'subNodeSel') {
							$(obj).siblings("a.grant").show();
						} else {
							$(obj).siblings("a.grant").hide();
						}
					},
					error : function(res) {

					}
				});
			}
		})
	}
	//全局表单授权
	function authorizeDialog(aurl) {
		var url = aurl;
		url = url.getNewUrl();
		DialogUtil.open({
			height : 600,
			width : 850,
			title : '全局表单授权',
			url : url,
			isResize : true
		});
	}

	//清除表单
	function clearForm(obj) {
		var btn = $(obj);
		var tdObj = btn.parent();
		$("input.formKey", tdObj).val('');
		$("input.formDefName", tdObj).val('');
		$("span[name='spanForm']", tdObj).text('');
		$(obj).siblings("a.grant").hide();
	}
	/**
	 * 表单授权
	 * @param obj 事件源
	 * @param nodeId 节点ID
	 * @param platform 平台  可选值：0:"PC",1:"mobile"。默认值为0。
	 */
	function authorize(obj, nodeId, platform) {
		if (!platform)
			platform = 0;
		var btn = $(obj);
		var tdObj = btn.parent();
		var objDefId = $("input.formKey", tdObj);
		if (objDefId.val() == "" || objDefId.val() == 0) {
			$.ligerDialog.warn('请先选择表单!', '提示信息');
			return;
		}
		var formKey = objDefId.val();

		var url = __ctx + '/oa/form/formDef/rightsDialog.do?actDefId='
				+ actDefId + '&nodeId=' + nodeId + '&formKey=' + formKey
				+ "&platform=" + platform;
		if (parentActDefId) {
			url += "&parentActDefId=" + parentActDefId;
		}
		if (nodeId.length > 0) {
			var oldformKey = $("input.oldFormKey", tdObj).val();
			if (oldformKey != formKey)
				url += '&isAlert=true'
		}
		authorizeDialog(url, nodeId, formKey);
	}

	// 弹出子表授权脚本填写页面
	function subDataGrant(obj, nodeId) {
		var btn = $(obj);
		var tdObj = btn.parent();
		var objDefId = $("input.formKey", tdObj);
		var formKey = objDefId.val();
		var tableId = $("input.tableId", tdObj).val();
		var url = __ctx + '/oa/form/formDef/subRightsDialog.do?actDefId='
				+ actDefId + '&nodeId=' + nodeId + '&formKey=' + formKey
				+ '&tableId=' + tableId;
		if (parentActDefId) {
			url += "&parentActDefId=" + parentActDefId;
		}
		authorizeDialog(url, nodeId, formKey);
	}

	$(function() {

		$('#ckJumpType').on('click', function() {
			$('.jumpType').attr('checked', this.checked);
		});

		$('#ckHidenOption').on('click', function() {
			var checked = this.checked;
			$('.hideOption').attr('checked', checked);
		});

		$('#ckHidenPath').on('click', function() {
			var checked = this.checked;
			$('.hidePath').attr('checked', checked);
		});

		$('#existSubTable').each(function() {
			var obj = $(this);
			if (obj.val() == 0) {
				obj.siblings("a.grant").hide();
			}
		});

		//处理表单类型
		handFormType();
		//验证handler
		validHandler();

		showHelper();

		//是否默认选中  隐藏执行路径
		var isNew = '${isNew}';
		if (isNew == 'yes') { //没有绑定表单时都要默认选中  隐藏执行路径
			$('#ckHidenPath').attr('checked', true);
			$('#ckHidenPath').click();
		}

		//提示文本
		$(".help.tipinfo").mouseover(function() {
			$(this).ligerTip({
				content : $("#help_tipinfo_html").html(),
				width : 1200
			});
			$(this).mouseout(function() {
				$(this).ligerHideTip();
			})

		})
	});

	function handFormType() {
		//业务表单
		$("select[name='bpmFormType']").change(function() {
			var value = $(this).val();
			var nodeId = $(this).parents(".formBox").find("#nodeId").val();
			if (value == -1) {
				$("#formBox_" + nodeId).hide();
				$(".url_" + nodeId).hide();
			} else {
				if (value == 0) {
					$(".form_" + nodeId).show();
					$(".url_" + nodeId).hide();
				} else if (value == 1) {
					$(".form_" + nodeId).hide();
					$(".url_" + nodeId).show();
				} else {
					$(".form_" + nodeId).hide();
					$(".url_" + nodeId).hide();
				}
			}
		});
		//节点表单
		$("select[name='formType']").change(function() {
			var value = $(this).val();
			var nodeId = $(this).parents(".formBox").find("#nodeId").val();
			if (value == -1) {
				$("#formBox_" + nodeId).hide();
			} else {
				$("#formBox_" + nodeId).show();
				if (value == 0) {
					$(".form_" + nodeId).show();
					$(".url_" + nodeId).hide();
				} else if (value == 1) {
					$(".form_" + nodeId).hide();
					$(".url_" + nodeId).show();
				} else {
					$(".form_" + nodeId).hide();
					$(".url_" + nodeId).hide();
				}
			}
		});

		$("select[name='globalFormType']").change(function() {
			var obj = $("select[name='bpmFormType']");
			var value = $(this).val();
			var nodeId = $(this).parents(".formBox").find("#nodeId").val();
			var objNodeId = obj.parents(".formBox").find("#nodeId").val();
			obj.val(value);
			if (value == -1) {
				$("#formBox_" + nodeId).hide();
				$(".form_" + objNodeId).hide();
				$(".url_" + objNodeId).hide();
			} else {
				$("#formBox_" + nodeId).show();
				if (value == 0) {
					$(".form_" + nodeId).show();
					$(".url_" + nodeId).hide();
					$(".form_" + objNodeId).show();
					$(".url_" + objNodeId).hide();
				} else if (value == 1) {
					$(".form_" + nodeId).hide();
					$(".url_" + nodeId).show();
					$(".form_" + objNodeId).hide();
					$(".url_" + objNodeId).show();
				} else {
					$(".form_" + nodeId).hide();
					$(".url_" + nodeId).hide();
					$(".form_" + objNodeId).hide();
					$(".url_" + objNodeId).hide();
				}
			}
		});
	}

	function validHandler() {
		$("input.handler").blur(function() {
			var obj = $(this);
			var val = obj.val();
			if (val.trim() == "") {
				return;
			}
			var params = {
				handler : val
			};
			$.post("validHandler.do", params, function(data) {
				var json = eval("(" + data + ")");
				if (json.result != '0') {
					$.ligerDialog.warn(json.msg, "提示信息", function() {
						obj.focus();
					});
				}
			});
		});
	}

	function showHelper() {
		var cookie = $.getCookie("help");
		if (cookie == "hidden") {
			$("ul.help").hide();
			return;
		}
		$("ul.help").show();
	}

	function toggleHelp() {
		var display = $("ul.help").css("display");
		if (display == "none") {
			$("ul.help").show();
			$.setCookie("help", "show");
		} else {
			$("ul.help").hide();
			$.setCookie("help", "hidden");
		}
	}
	// 显示字段
	function showField(nodeId) {
		var formKey = $("#defaultFormKey").val();
		if (formKey == "" || formKey == 0) {
			$.ligerDialog.warn('请先选择全局表单!', '提示信息');
			return;
		}
		var url = __ctx + '/oa/form/formDef/fieldDialog.do?formKey=' + formKey;
		DialogUtil.open({
			height : 540,
			width : 750,
			title : '字段选择',
			url : url,
			isResize : true,
			//自定义参数
			sucCall : function(rtn) {
				var name = "opinionField_" + nodeId;
				$("input[name='" + name + "']").val(rtn);
			}
		});
	}

	function clearField(nodeId) {
		var name = "opinionField_" + nodeId;
		$("input[name='" + name + "']").val("");
	}
	/**
	 * 自定义跳转路径
	 * @param actdefId 流程定义ID
	 * @param nodeId 节点ID
	 */
	function setjumpType(nodeId, actdefId) {
		var url = __ctx + "/oa/flow/nodeJumpType/edit.do?nodeId=" + nodeId
				+ "&actdefId=" + actdefId;
		url = url.getNewUrl();
		DialogUtil.open({
			height : 700,
			width : 800,
			title : '自定义跳转规则设置',
			url : url,
			isResize : true
		});
	}
</script>
<f:link href="from-jsp.css"></f:link>

</head>
<body>
	<div class="panel">
		<!-- <div class="panel-container"> -->
			<div class="panel-title">
				<jsp:include page="incDefinitionHead.jsp">
					<jsp:param value="节点表单设置" name="title" />
				</jsp:include>
			</div>
	  		<f:tab curTab="form" tabName="flow%"/>
			<div class="panel-top">
				<div style="width: 1667px;" class="tbar-title">
					<span class="tbar-label">流程表单设置</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link init" href="####" onclick="save()">
								
								保存
							</a>
						</div>
						<a href="####" class="help tipinfo"></a>
						<div class="content" id="help_tipinfo_html" style="background: none repeat scroll 0 0 #fff; display: none;">
							<ul class="help">
								<li>
									表单
									<ul>
										<li>在线表单,为系统自定义表单。</li>
										<li>
											url表单,是外部表单。地址写法规则为：如果表单页面平台在同一个应用中，路径从根开始写，
											<span class="red">不需要上下文路径</span>
											，例如 ：/form/addUser.do。 如果需要使用的表单不再同一个应用下，则需要写完整路径如:http://***/crm/addUser.do 。
										</li>
									</ul>
								</li>

								<li>
									处理器
									<ul>
										<li>
											处理器是需要开发人员定制的,一般情况下
											<span class="red">URL表单的处理器需要填写</span>
											。处理器的写法是 service类名首字母小写 +"." + 方法名称。 需要注意的是这个service是需要通过spring容器管理的。另外方法的参数必须是ProcessCmd类。
											例如：userService.add,这个表示是UserService类中有一个，add(ProcessCmd cmd)的方法。
										</li>
										<li>
											<span class="red">前置和后置处理器</span>
											区别在于执行时机不同，前置处理器是在启动流程或完成下一步这个动作之前执行的。 后置处理器在启动流程或完成下一步之后执行的。
											<span class="red">---该功能待完善</span>
										</li>
										<li>
											<span class="red">节点表单--表单脚本--初始化</span>
											用于设置表单加载后的初始化设置。如在当前审批人和审批时间框中自动填入当前系统用户和系统时间。
										</li>
										<li>
											<span class="red">节点表单--跳转类型</span>
											如果"勾选"跳转类型的checkbox，一定不能勾选隐藏路径。
										</li>
										<li>
											<span class="red">节点表单--跳转类型</span>
											为使第一个节点"勾选"的跳转类型为“选择路径跳转”或“自由跳转”有效，需要在“其他”tab标签中选中
											<span class="red">“流程启动选择执行人”</span>
											。
										</li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>

			<div class="panel-body">
				<form action="save.do" method="post" id="dataForm">
					<input type="hidden" name="defId" value="${bpmDefinition.defId}" />
					<input type="hidden" name="parentActDefId" value="${parentActDefId}" />
					<div class="foldBox formBox">
						<input id="nodeId" type="hidden" value="globalFormType" />
						<div class="title">
							全局表单
							<select name="globalFormType" class="selectForm">
								<c:choose>
									<c:when test="${globalForm==null }">
										<option value="-1" selected="selected">请选择..</option>
										<option value="0">在线表单</option>
										<option value="1">URL表单</option>
									</c:when>
									<c:otherwise>
										<option value="-1" <c:if test="${globalForm.formType==-1 }">selected="selected"</c:if>>请选择..</option>
										<option value="0" <c:if test="${globalForm.formType==0 }">selected="selected"</c:if>>在线表单</option>
										<option value="1" <c:if test="${globalForm.formType==1 }">selected="selected"</c:if>>URL表单</option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
						<div class="content">
							<table id="formBox_globalFormType" class="table-noborder" <c:if test="${globalForm==null}">style="display: none" </c:if>>
								<tr>
									<td nowrap="nowrap" colspan="4" style="padding: 5px 10px;">
										<div name="globalForm" class="form_globalFormType" <c:if test="${globalForm!=null && globalForm.formType!=0 }">style="display: none" </c:if>>
											<span style="padding: 0px 10px 0px 72px;">表单:</span>
											<input id="defaultFormKey" class="formKey" type="hidden" name="defaultFormKey" value="${globalForm.formKey }">
											<input id="defaultFormName" class="formDefName" type="hidden" name="defaultFormName" value="${globalForm.formDefName }">
											<span name="spanForm">
												<a target="_blank" href="${ctx}/oa/form/formHandler/edit.do?formDefId=${globalForm.formKey}">${globalForm.formDefName }</a>
											</span>
											<a href="javascript:;" class="link get" onclick="selectGlobalForm(this)">选择</a>
											<a href="javascript:;" class="link clean" onclick="clearForm(this)">重选</a>
											<a href="javascript:;" class="link auth" onclick="authorize(this,'')">表单授权</a>
											<a href="####" class="tipinfo">
												<span>设置全局表单授权，多个流程定义绑定该表单时，则使用该表单的授权信息一致；倘若其他流程定义绑定该表单且需使用不同表单权限控制则不需设置全局表单授权。</span>
											</a>
										</div>

										<div name="globalFormUrl" <c:if test="${globalForm!=null && globalForm.formType!=1 }">style="display: none" </c:if>
											class="url_globalFormType">
											<table class="table-noborder">
												<tr>
													<th style="padding-left: 50px;">表单URL:</th>
													<td>
														<input type="text" name="formUrlGlobal" value="${globalForm.formUrl }" class="inputText" style="width: 250px;" />
													</td>
													<th style="padding-left: 45px;">明细URL:</th>
													<td>
														<input type="text" name="detailUrlGlobal" value="${globalForm.detailUrl }" class="inputText" style="width: 250px;" />
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
								<!-- 手机全局表单
								<tr>
									<th nowrap="nowrap">手机表单:</th>
									<td>
										<div name="globalForm" class="form_globalFormType" <c:if test="${globalForm!=null && globalForm.formType!=0 }">style="display: none" </c:if>>
											<input id="defaultMobileFormKey" class="formKey"  type="hidden" name="defaultMobileFormKey" value="${globalForm.mobileFormDef.formKey }" >
											<input id="defaultMobileFormName" class="formDefName"  type="hidden" name="defaultMobileFormName" value="${globalForm.mobileFormDef.subject }">
											<span name="spanForm"><a target="_blank" href="${ctx}/oa/form/formHandler/edit.do?formDefId=${globalForm.mobileFormDef.formKey}" >${globalForm.mobileFormDef.subject }</a></span>
											<a href="javascript:;" mobile="mobile" class="link get" onclick="selectGlobalForm(this)">选择</a>
											<a href="javascript:;" mobile="mobile" class="link clean" onclick="clearForm(this)">重选</a>
											<a href="javascript:;" mobile="mobile" class="link auth" onclick="authorize(this,'',1)">表单授权</a>
										</div>
									</td>
								</tr>
								-->
								<tr>
									<th nowrap="nowrap">前置处理器:</th>
									<td>
										<input type="text" name="beforeHandlerGlobal" value="${globalForm.beforeHandler }" class="inputText handler" size="40" />

									</td>
									<th nowrap="nowrap">后置处理器:</th>
									<td>
										<input type="text" name="afterHandlerGlobal" value="${globalForm.afterHandler }" class="inputText handler" size="40" />

									</td>
								</tr>
								<tr>
									<th nowrap="nowrap">表单初始化脚本:</th>
									<td colspan="3">
										<textarea name="initScriptHandlerGlobal" class="inputText" cols="78" rows="2">${globalForm.initScriptHandler}</textarea>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="foldBox formBox">
						<input type="hidden" id="nodeId" value="bpmForm" />
						<div class="title">
							流程实例业务表单:
							<select name="bpmFormType" id="bpmFormType" class="selectForm">
								<c:choose>
									<c:when test="${bpmForm==null }">
										<option value="-1" selected="selected">请选择..</option>
										<option value="0">在线表单</option>
										<option value="1">URL表单</option>
									</c:when>
									<c:otherwise>
										<option value="-1" <c:if test="${bpmForm.formType==-1 }">selected="selected"</c:if>>请选择..</option>
										<option value="0" <c:if test="${bpmForm.formType==0 }">selected="selected"</c:if>>在线表单</option>
										<option value="1" <c:if test="${bpmForm.formType==1 }">selected="selected"</c:if>>URL表单</option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
						<div class="content">
							<div id="formBox_bpmForm" name="bpmForm" class="form_bpmForm" <c:if test="${empty bpmForm || bpmForm.formType!=0}">style="display: none"</c:if>>
								表单：
								<input id="bpmFormKey" class="formKey" type="hidden" name="bpmFormKey" value="${bpmForm.formKey}">
								<input id="bpmFormName" class="formDefName" type="hidden" name="bpmFormName" value="${bpmForm.formDefName}">
								<span name="spanForm">
									<a target="_blank" href="${ctx}/oa/form/formHandler/edit.do?formDefId=${bpmForm.formKey}">${bpmForm.formDefName}</a>
								</span>
								<a href="javascript:;" class="link get" onclick="selectBpmForm(this)">选择</a>
								<a href="javascript:;" class="link clean" onclick="clearForm(this)">重选</a>
							</div>
							<div class="url_bpmForm" <c:if test="${empty bpmForm || bpmForm.formType!=1}">style="display: none"</c:if>>
								表单明细URL:
								<input type="text" id="bpmFormUrl" name="bpmFormUrl" value="${bpmForm.formUrl}" class="inputText" size="40" />
							</div>
						</div>
					</div>
					<div class="foldBox">
						<div class="title">节点表单</div>
						<div class="content" style="width: 100%; height:400px;overflow-x: auto;">
							<table cellpadding="1" cellspacing="1" class="table-grid table-list">
								<thead>
									<tr>
										<th width="10%">节点名</th>
										<th width="15%">
											<label>
												<input type="checkbox" id="ckJumpType">
												跳转类型
											</label>
										</th>
										<th width="7%">
											<label title="隐藏表单意见">
												<input type="checkbox" id="ckHidenOption">
												隐藏意见
											</label>
										</th>
										<th width="7%">
											<label title="隐藏执行路径">
												<input type="checkbox" id="ckHidenPath">
												隐藏路径
											</label>
										</th>
										<th width="10%">处理器</th>
										<th width="10%">表单脚本</th>
										<th width="32%">表单</th>
									</tr>
								</thead>
								<c:forEach items="${bpmNodeSetList}" var="item" varStatus="status">
									<tr <c:if test="${status.index%2=='0' }">class="odd"</c:if>>
										<td>
											<input type="hidden" name="nodeId" value="${item.nodeId}" />
											<input type="hidden" name="nodeName" value="${item.nodeName}" />${item.nodeName}
										</td>
										<td nowrap="nowrap">

											<ul>
												<li>
													<input type="checkbox" class="jumpType" name="jumpType_${item.nodeId}" value="1"
														<c:if test="${fn:indexOf(item.jumpType,'1')!=-1}">checked="checked"</c:if> />
													正常跳转
												</li>
												<li>
													<input type="checkbox" class="jumpType" name="jumpType_${item.nodeId}" value="2"
														<c:if test="${fn:indexOf(item.jumpType,'2')!=-1}">checked="checked"</c:if> />
													选择路径跳转
												</li>
												<li>
													<input type="checkbox" class="jumpType" name="jumpType_${item.nodeId}" value="3"
														<c:if test="${fn:indexOf(item.jumpType,'3')!=-1}">checked="checked"</c:if> />
													自由跳转
												</li>
												<li>
													<input type="checkbox" class="jumpType" name="jumpType_${item.nodeId}" value="4"
														<c:if test="${fn:indexOf(item.jumpType,'4')!=-1}">checked="checked"</c:if> />
													自定义跳转
													<a href="javascript:;" onclick="setjumpType('${item.nodeId}','${bpmDefinition.defId}')">设置跳转路径</a>

												</li>
											</ul>
											<ul>
												<li>
													驳回到指定节点：
													<select name="backNode" class="selectForm">
														<option value="">请选择..</option>
														<c:forEach items="${bpmNodeSetList}" var="node">
															<option value="${node.nodeId}" <c:if test="${node.nodeId==item.backNode}">selected</c:if>>${node.nodeName}</option>
														</c:forEach>
													</select>
												</li>
											</ul>

										</td>
										<td>
											<ul>
												<li>
													<input type="checkbox" class="hideOption" name="isHideOption_${item.nodeId}" value="1"
														<c:if test="${item.isHideOption==1}">checked="checked"</c:if> />
												</li>
												<li>
													<input type="text" class="inputText" name="opinionField_${item.nodeId}" value="${item.opinionField }" style="width: 120px;"
														readonly="readonly" />
													<br>
													<a href="javascript:showField('${item.nodeId}');">意见回填字段</a>
													<a href="javascript:clearField('${item.nodeId}');">清除</a>
												</li>
											</ul>
										</td>
										<td>
											<input type="checkbox" class="hidePath" name="isHidePath_${item.nodeId}" value="1"
												<c:if test="${item.isHidePath==1}">checked="checked"</c:if> />
										</td>

										<td>
											<table class="table-detail">
												<tr>
													<td nowrap="nowrap" class="head">前置:</td>
													<td>
														<input type="text" name="beforeHandler" value="${item.beforeHandler}" class="inputTexts handler" size="20" />

													</td>
												</tr>
												<tr>
													<td nowrap="nowrap" class="head">后置:</td>
													<td>
														<input type="text" name="afterHandler" value="${item.afterHandler}" class="inputTexts handler" size="20" />

													</td>
												</tr>
											</table>
										</td>

										<td>
											<table class="table-detail">
												<tr>
													<td nowrap="nowrap" class="head">初始化:</td>
													<td>
														<input type="text" name="initScriptHandler" value="${item.initScriptHandler}" class="inputTexts" size="20" />
													</td>
												</tr>
											</table>
										</td>
										<td class='formBox'>
											<input id="nodeId" type="hidden" value="${item.nodeId}" />
											<table class="table-detail">
												<tr>
													<td nowrap="nowrap" class="head">表单类型:</td>
													<td>
														<select name="formType" class="selectForm">
															<option value="-1" <c:if test="${item.formType==-1}">selected</c:if>>请选择..</option>
															<option value="0" <c:if test="${item.formType==0}">selected</c:if>>在线表单</option>
															<option value="1" <c:if test="${item.formType==1}">selected</c:if>>URL表单</option>
														</select>
													</td>
												</tr>
											</table>
											<div id="formBox_${item.nodeId}" name="nodeForm" <c:if test="${item.formType==-1}">style="display:none"</c:if>>
												<table class="table-detail table-noborder">
													<tr class="form_${item.nodeId}" <c:if test="${item!=null && item.formType!=0 }">style="display: none" </c:if>>
														<td nowrap="nowrap">表单:</td>
														<td>
															<div>
																<input type="hidden" class="formKey" name="formKey" value="${item.formKey}">
																<input type="hidden" class="oldFormKey" name="oldFormKey" value="${item.formKey}">
																<input type="hidden" class="formDefName" name="formDefName" value="${item.formDefName}">
																<input type="hidden" class="tableId" name="tableId" value="${item.mainTableId }">
																<span name="spanForm">
																	<a target="_blank" href="${ctx}/oa/form/formHandler/edit.do?formDefId=${item.formKey}">${item.formDefName}</a>
																</span>
																<a href="javascript:;" class="link get" onclick="selectNodeForm(this)" id="subNodeSel">选择</a>
																<a href="javascript:;" class="link clean" onclick="clearForm(this)">重选</a>
																<a href="javascript:;" class="link auth" onclick="authorize(this,'${item.nodeId}')">表单授权</a>
																<input type="hidden" id="existSubTable" value="${item.existSubTable }">
																<a href="javascript:;" class="link grant" onclick="subDataGrant(this,'${item.nodeId}')">子表数据授权</a>
															</div>
														</td>
													</tr>
													<!-- 
														<tr class="form_${item.nodeId}" <c:if test="${item!=null && item.formType!=0 }">style="display: none" </c:if>>
															<td nowrap="nowrap">手机表单:</td>
															<td>
																<div>
																	<input type="hidden" class="formKey" name="mobileFormKey" value="${item.mobileFormDef.formKey}">
																	<input type="hidden" class="oldFormKey" name="mobileOldFormKey" value="${item.mobileFormDef.formKey}">
																	<input type="hidden" class="formDefName" name="mobileFormDefName" value="${item.mobileFormDef.subject}">
																	<input type="hidden" class="tableId" name="tableId" value="${item.mainTableId }">
																	<span name="spanForm"><a target="_blank" href="${ctx}/oa/form/formHandler/edit.do?formDefId=${item.formKey}" >${item.formDefName}</a></span>
																	<a href="javascript:;" class="link get" onclick="selectNodeForm(this)" id="subNodeSel">选择</a>
																	<a href="javascript:;" class="link clean" onclick="clearForm(this)">重选</a>
																	<a href="javascript:;" class="link auth" onclick="authorize(this,'${item.nodeId}',1)">表单授权</a>
																</div>
															</td>
														</tr>
													-->
												</table>
												<table class="table-detail table-noborder">
													<tr <c:if test="${item!=null && item.formType!=1 }">style="display: none" </c:if> class="url_${item.nodeId}">
														<td>表单URL</td>
														<td>
															<input type="text" name="formUrl" value="${item.formUrl}" class="inputTexts" size="30" />
														</td>
													</tr>
													<tr <c:if test="${item!=null && item.formType!=1 }">style="display: none" </c:if> class="url_${item.nodeId}">
														<td>明细URL</td>
														<td>
															<input type="text" name="detailUrl" value="${item.detailUrl}" class="inputTexts" size="30" />
														</td>
													</tr>
												</table>
											</div>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</form>
			</div>
		<!-- </div> -->
	</div>
</body>
</html>


