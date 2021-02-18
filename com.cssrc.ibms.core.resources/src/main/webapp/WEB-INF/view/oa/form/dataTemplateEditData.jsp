<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑表单</title>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>  <!--panel-body高度计算-->
</head>
<body style="overflow: auto">
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">
				<c:choose>
					<c:when test="${hasPk}">
						编辑${tableName}
					</c:when>
					<c:otherwise>
						添加${tableName}
					</c:otherwise>
				</c:choose>
			</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group">
					<a class="link save" id="dataFormSave" href="####" onclick="check()">
						
						保存
					</a>
				</div>
				
				<div class="group">
					<a class="link close" href="javascript:close();">
						
						关闭
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="frmData" name="frmData" method="post" style="height:100%" action="${ctx}/oa/form/formHandler/save.do">
			<input type="hidden" name="formData" id="formData" />
			<input type="hidden" name="${pkField}" id="${pkField}" value="${requestScope.id}" />
			<input id='tableId' name='tableId' type='hidden' value='${tableId}' />
			<input id='tableName' name='tableName' type='hidden' value='${tableName}' />
			<input type="hidden" id="jsPreScript" value="${sysBusEvent.jsPreScript }" />
			<input type="hidden" id="jsAfterScript" value="${sysBusEvent.jsAfterScript }" />
			<input id='alias' name='alias' type='hidden' value='${alias}' />
			<input id='listenfileds' name='listenfileds' type='hidden' />
			<input id='isBackData' name='isBackData' type='hidden' value='${isBackData}' />
			<input type="hidden" name="ctx" value="${ctx}" />
			<input type="hidden" name="formKey" value="${alias}" />
			<input type="hidden" name="dataId" value="${id}" />
			<input type="hidden" name="pageType" value="edit" />
			<!-- DBOM中使用，目的在于在新增表单数据时，自动将型号类型带过来 -->
			<input type="hidden" name="flag" value="${flag}"/>
			

			<c:if test="${not empty dbomFKName}">
				<!-- DBOM中使用，目的在于在新增表单数据时，父节点字段数据值自动带过来 -->
				<input type="hidden" name="${dbomFKName}" value="${dbomFKValue}" />
				<input type="hidden" name="dbomFKName" value="${dbomFKName}" />
				<input type="hidden" name="dbomFKValue" value="${dbomFKValue}" />
				<input type="hidden" name="tcpx" value="${tcpx}"/>
			</c:if>

			<div style="height:100%;padding: 0 10px;box-sizing: border-box;">${bpmFormDef.html}</div>
			<!--2017-05-16:添加padding样式(rzq)-->
		</form>
	
		<div id="bak_data_tip" width="700px" style="display:none"></div>
	</div>
</div>
<script type="text/javascript">
	var dialog = null; //调用页面的dialog对象(ligerui对象)
	$(function() {

		function showRequest(formData, jqForm, options) {
			return true;
		}
		pShow = null
		initSubForm();//该方法在本jsp的下面
		if (frameElement) {
			dialog = frameElement.dialog;
		}

		if (!dialog && dialog != undefined) {
			$('.close').remove();
			dialog.set('title', '编辑表单');
		}
		$(".taskopinion").each(function(){
			$(this).removeClass("taskopinion");
			var actInstId=$(this).attr("instanceId");
			$(this).load("${ctx}/oa/flow/taskOpinion/listform.do?actInstId="+actInstId);
		});
		//初始化流程图
		$(".flowchart").each(function(){
			$(this).removeClass("flowchart");
			var url=$(this).attr("url");
			if(url){
	            $(this).load(url);
	        }
		});
		$("[listenedit='true']").each(function(i, f) {

			$(this).mouseover(function() {
				clearTimeout(pShow);
				pShow = setTimeout(function() {
					var finfo=$(f).attr("name");
					var finfos=finfo.split(":");
					var url="${ctx}/oa/form/dataTemplate/getBak.do?";
					if(finfos.length==3){
						var tablename=finfos[1];
						var filedname=finfos[2];
						if(finfos[0]=='m'){
							//取主表
							var mid=$(f).attr("m_table_pkvalue");
							url+="pkvalue="+mid;
						}else if(finfos[0]=='s'){
							//取字表
							var sid=$(f).attr("s_table_pkvalue");
							url+="pkvalue="+sid;

						}else if(finfos[0]=='r'){
							//取关联表
							var rid=$(f).attr("r_table_pkvalue");
							url+="pkvalue="+rid;
						}
						url+="&tablename="+tablename;
						url+="&filedname="+filedname;

					}
					//$(f).slideDown(300).fadeIn(300);
					$("#bak_data_tip").load(url,function(){
						if($("#bak_data_tip").find("tr").length>0){
							$(f).ligerTip({
								content : $("#bak_data_tip").html(),
								width:410
							});
						}else{
							return;
						}
					})
				}, 1000);

				$(f).mouseout(function() {
					clearTimeout(pShow);
					$(f).ligerHideTip();
					//$(f).find('p').slideUp(150).fadeOut(150)
				})

			})

		});

	});
	function close(){
		var hasPk = $('input[name=dataId]').val()!=""?true:false;
		if(!hasPk){
			TableFile.delFile();
			dialog.get('sucCall')('ok');
			dialog.close();
		}else{
			dialog.get('sucCall')('ok');
			dialog.close();
		}
	}
	function beforeSubmit() {
		var jsPreScript = $("#jsPreScript").val();
		if (jsPreScript != null && $.trim(jsPreScript) != "") {
			${sysBusEvent.jsPreScript}
		}
	}

	function getFiledsBakData() {
		var fileds = "";
		$("[listenedit='true']").each(function(i, f) {
			fileds += f.name + ",";
		})
		if (fileds != "") {
			fileds = fileds.substring(0, fileds.length - 1);
			$("#listenfileds").val(fileds);
		}
	}
	//调用后台检验方法
	//满足唯一性约束返回0.否则返回1
	function checkUnique(column, columnValue,lablename,datefmt) {
		var result;
		$.ajax({
			url : "${ctx}/oa/form/formHandler/checkUnique.do",
			type : "POST",
			data : {
				tableId : $('#tableId').val(),
				displayId : "${displayId}",
				pkId : '${requestScope.id}',
				columnValue : $("input[name='" + column + "']").val(),
				lablename : lablename,
				name : column,
				datefmt : datefmt
			},
			dataType:'json',
			async : false,
			success : function(data) {
				result = data;
			}
		});
		return result;
	}

	//校验有唯一性约束条件的数据
	function check() {
		//获取具有唯一性约束条件的输入框内容
		var unique = $("input[isunique='1']");
		var column, columnValue,lablename,datefmt;
		var isunique = 1;
		var flag = true;
		//没有满足唯一性约束条件就跳过校验
		if (unique.length == 0) {
			saveHandler();
		} else {
			//循环获取所有的满足唯一性约束条件数据
			$.each(unique, function(i, value) {
				var attribute = value.attributes;
				lablename = $(value).parents('td').prev().text();
				for (var n = 0; n < attribute.length; n++) {
					if (attribute[n].name == "name") {
						column = attribute[n].value;
					}
					if (attribute[n].name == "value") {
						columnValue = attribute[n].value;
					}
					if (attribute[n].name == "datefmt") {
						datefmt = attribute[n].value;
					}
				}
				//调用检验方法，若不满足则返回1
				var resultMessge = checkUnique(column, columnValue,lablename,datefmt);
				//只要有一条数据不满足就报错，并且不进行保存操作
				if (resultMessge.success=='false') {
					$.ligerDialog.error(resultMessge.message);
					flag = false;
					//终结each循环
					return false;
				}
			});
			//所有唯一性约束条件都满足，继续执行保存
			if (flag){
				saveHandler();
			}
		}
	}

	function saveHandler() {
		//提交前表单验证
		var ignoreRequired = false;
		var rtn = CustomForm.validate({
			ignoreRequired : ignoreRequired,
			returnErrorMsg : true
		});
		if (rtn.success) {
			//表单数据保存前的前处理
			if (!preSaveHandler()) {
				return;
			};
			var rtn = beforeSubmit();
			if (rtn == false) {
				return;
			}
			$("#dataFormSave").attr("disabled", "disabled");// 防止重复提交数据

			//Office控件提交。
			OfficePlugin.submit();
			//WebSign控件提交。
			WebSignPlugin.submit();
			//获取表单数据
			var data = preGetDataHandler(CustomForm, CustomForm.getData());
			//设置表单数据
			$("#formData").val(data);
			//$('#frmData').submit();
			//by weilei:更改表单提交方式
			var isBackData=$("#isBackData").val();
			if(isBackData==2){
				$.ligerDialog.prompt("是否需要对旧数据进行备份",true,
					function(yes,value) {
						if (yes) {
							var bakData = 1;
							submit(data,bakData,value);
						} else {
							var bakData = 0;
							submit(data,bakData,value);
						}
					}
				);
			}else{
				submit(data,null,null);
			}
			
		} else {
			$.ligerDialog.warn("表单验证不成功,请检查表单是否正确填写:" + rtn.errorMsg, "提示信息");
		}
	};


	function submit(data, bakData, value) {
		var data = {
			formData : data,
			tableId : $('#tableId').val(),
			__bakData__ : bakData,
			__remark__ : value,
			alias : '${alias}',
			'${pkField}' : '${requestScope.id}'
		}
		if (bakData == null) {
			delete data.__bakData__;
		}
		if (value == null) {
			delete data.value;
		}
		$.ajax({
			url : "${ctx}/oa/form/formHandler/save.do",
			type : "POST",
			data : data,
			async : true,
			success : function(data) {
				showResponse(data);
			}
		});
	}
	function initSubForm(opitons) {
		opitons = $.extend({}, {
			success : showResponse
		}, opitons);
		//ajaxForm增加所有需要的事件监听，为ajax提交表单做好准备。ajaxform不能提交表单。
		//在document的ready函数中，用ajaxForm为ajax提交表单做准备。
		$('#frmData').ajaxForm(opitons);
	};

	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			if (dialog) {
				//window.top.document.getElementById('10000000410001').contentWindow.reFresh();
				var megs = obj.getMessage();
				//保存成功后返回的信息："提示信息&&&*&*keyId"
				var megsArray = megs.split('&&&*&*');
				var message = '';
				var keyId = '';
				if (megsArray.length > 1) {
					message = megsArray[0];
					keyId = megsArray[1];
				} else {
					keyId = '';
					message = megs;
				}
				//获取表单数据
				var data = preGetDataHandler(CustomForm, CustomForm.getData());
				var tableId = $('#tableId').val();
				//表单数据保存后的后处理

				var params = $.extend({}, {
					tableId : tableId
				}, {
					keyId : keyId
				});
				var saveResult = afterSaveHandler(data, params);
				if (saveResult == true) {
					/* 
					$.ligerDialog.confirm(message + ",是否继续操作", "提示信息",
							function(rtn) {
								if (!rtn) {
									dialog.get("sucCall")('ok');
									dialog.close();
								} else {
									//确认继续操作就将防止重复提交权限打开
									$("#dataFormSave").removeAttr("disabled");
									TableFile.refresh();
								}
							});
					  */
					$.ligerDialog.success(message , "提示信息",
							function(rtn) {
								if(rtn){
									dialog.get("sucCall")('ok');
									dialog.close();
								}
							});
				} else if (saveResult == false) {
					$.ligerDialog.warn("业务后处理失败！", "提示信息", function(rtn) {
						if (!rtn) {
							dialog.close();
						}
					});
				}
			} else {
				$.ligerDialog.success(obj.getMessage(), "提示信息", function() {
					window.parent.refreshTheTree();//刷新左侧树
					window.returnValue = "ok";
					window.close();
				});
			}
		} else {
			$.ligerDialog.error("出错了", "系统保存数据错误!", obj.getMessage());
		}
	};

	function preSaveHandler() {
		//添加保存前处理
		return true;
	};

	function preGetDataHandler(CustomForm, data) {
		//添加保存前获取数据处理
		return data;
	};

	function afterSaveHandler(data, params) {
		//表单新增时：文件上传的后处理
		var hasPk = $('input[name=hasPk]').val()=="true"?true:false;
		if(!hasPk){
			TableFile.updateFile(data,params);
		}
		var jsAfterScript = $("#jsAfterScript").val();
		if (jsAfterScript != null && $.trim(jsAfterScript) != "") {
			${sysBusEvent.jsAfterScript}
		}
		return true;
	};
</script>
<!--start  自定义js文件，css文件  -->
	${headHtml}
<!--end    自定义js文件，css文件  -->
</body>
</html>

