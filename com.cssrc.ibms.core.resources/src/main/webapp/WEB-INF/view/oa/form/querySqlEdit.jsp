<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>数据模板设置</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/lang/view/oa/form/zh_CN.js"></script>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.fix.clone.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript">
	var tab = "";
	$(function() {
		//Tag Layout
		tab = $("#tab").ligerTab({
			contextmenu:false
		});
		// 初始化
		_SysQuerySqlEdit_.init();
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		$("a.save").click(function() {
			var alias=$("#alias").val();
			if(alias==""){
				$.ligerDialog.warn("别名不能为空");
			}else if(/.*[\u4e00-\u9fa5]+.*$/.test(alias)){
				$.ligerDialog.warn("别名不能为中文");
			}else{
				var form = $('#sysQuerySqlForm');
				if (!form.valid())
					return;
				if (saveChange(form)&&validSql(false)) {
					customFormSubmit(form, options);
				}
			}
		});
		$("#btnValid").click(function() {
			validSql(true);
		});
	});

	/**
	 * 自定义外部表单并提交
	 * @return void
	 */
	function customFormSubmit(form, options) {
		var id = $("#id").val();
		var name = $("#name").val();
		var sql = $("#sql").val();
		var dsname = $("#dsname").val();
		var alias = $("#alias").val();
		var categoryId = $("#categoryId").val();
		var urlParams = $("#urlParams").val();
		var fieldJson = $('#fieldJson').val();
		var json = {
			id : id,
			name : name,
			sql : sql,
			alias : alias,
			dsname : dsname,
			categoryId: categoryId,
			urlParams : urlParams
		};
		var $form = $('<form method="post" action="save.do"></form>');
		var $input = $("<input type='hidden' name='jsonStr'/>");
		var $input2 = $("<input type='hidden' name='fieldJson'/>");
		var jsonStr = JSON2.stringify(json);
		$input.val(jsonStr);
		$input2.val(fieldJson);
		$form.append($input).append($input2);
		$form.ajaxForm(options);
		$form.submit();
	}
	// 验证字段和保存更改
	function saveChange(form) {
		// 设置url
		var urlParams = JSON2.stringify(getUrlParams());
		$('#urlParams').val(urlParams);
		// 设置字段
		var fieldJson = getFieldJson();
		$('#fieldJson').val(fieldJson);
		return true;
	}
	// 装箱url设置
	function getUrlParams() {
		var json = [];
		$("#urlTbl tr[var='urlTr']").each(function() {
			var me = $(this), obj = {};
			obj.name = $("[var='name']", me).val();
			obj.desc = $("[var='desc']", me).val();
			obj.className = $("[var='className']", me).val();
			obj.clickName = $("[var='clickName']", me).val();
			obj.urlPath = $("[var='urlPath']", me).val();
			obj.urlParams = $("[var='setUrlParams']", me).val();
			json.push(obj);
		});
		return json;
	}
	// 保存后的回调
	function showResponse(responseText) {
		$.ligerDialog.closeWaitting();
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作", "提示信息",
					function(rtn) {
						if (rtn) {
							window.location.reload();
						} else {
							window.location.href = "list.do";
						}
					});
		} else {
			$.ligerDialog.error(obj.getMessage(), "保存出错!");
		}
	}
	// 验证sql
	function validSql(remind) {
		var sql = $('#sql').val();
		var alias = $('#alias').val();
		var dsname = $("#dsname").val();
		var params = {
			sql : sql,
			alias : alias,
			dsname : dsname
		};
		var flag = true;
		$.ajax({
		      url: 'validSql.do',
		      type: 'POST',
		      dataType: 'json',
		      data:params,
		      async:false,
		      success: function(data){
	    	  if (data) {
	    		  	if(remind){
						$.ligerDialog.success('<p><font color="green">验证通过!<br></font></p>');
	    		  	}
				} else {
					$.ligerDialog.error('<p><font color="red">验证不通过!<br></font></p>');
					flag = false;
				}
		      }
      	});
		return flag;
	}

	//装箱field字段json数组
	function getFieldJson() {
		var jsonArr = new Array();
		$(".sequence").each(
				function(index) {
					var id = $("#field_id_" + index).val();
					var sqlId = $("#sqlId").val();
					var name = $("#field_name_" + index).html();
					var type = $("#field_type_" + index).html();
					var fieldDesc = $("#field_fieldDesc_" + index).val();
					var isShowChecked = $("#field_isShow_" + index).attr(
							"checked");
					var isShow = 0;
					if (isShowChecked == 'checked') {
						isShow = 1;
					}
					var isSearchChecked = $("#field_isSearch_" + index).attr(
							"checked");
					var isSearch = 0;
					if (isSearchChecked == 'checked') {
						isSearch = 1;
					}
					var ct=$("#field_ct_"+id).val();

					var fieldObj = new field(id, sqlId, name, type, fieldDesc,
							isShow,isSearch,ct);
					jsonArr.push(fieldObj);
				});
		return JSON2.stringify(jsonArr);
	}
	//field对象
	function field(id, sqlId, name, type, fieldDesc, isShow, isSearch,ct) {
		var obj = new Object();
		obj.id = id;
		obj.sqlId = sqlId;
		obj.name = name;
		obj.type = type;
		obj.fieldDesc = fieldDesc;
		obj.isShow = isShow;
		obj.isSearch = isSearch;
		obj.ct=ct;
		return obj;
	}
	//打开编辑细节页
	function openEditDetailDialog(id) {
		var url= "${ctx}/oa/form/queryField/editDetail.do?id="+id;
		DialogUtil.open({
			height:400,
			width: 700,
			title : '设置字段信息',
			url: url, 
			isResize: true,
			succCall:function(rtn){
				$("#field_ct_"+id).val(rtn.ct);
			}
		});
	}
	//自动生成别名
	function getKeyName(obj){
	    var value=$(obj).val();
	    if(!value)return false;
		Share.getPingyin({
			input:value,
			postCallback:function(data){
				var inputObj=	$("input[name='alias']");
				//当alias为空或者为中文时
				if(inputObj.val().length<1 || /.*[\u4e00-\u9fa5]+.*$/.test(inputObj.val())){
					inputObj.val(data.output);
				}
			}
		});
	}
</script>
<script type="text/javascript"
	src="${ctx}/jslib/ibms/oa/system/sysQuerysqlEdit.js"></script>
</head>
<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="panel-top">
					<div class="tbar-title">
						<span class="tbar-label">数据模板设置</span>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group">
								<a class="link save" id="dataFormSave" href="####">保存</a>
							</div>
							<div class="group">
								<a class="link back" href="list.do">返回</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysQuerySqlForm" method="post" action="save.do">
				<input type="hidden" name="id" id="id" value="${sysQuerySql.id}" />
				<input type="hidden" name="sqlId" id="sqlId"
					value="${sysQuerySql.id}" />
				<textarea style="display: none;" id="urlParams" name="urlParams">${fn:escapeXml(sysQuerySql.urlParams)}</textarea>
				<textarea style="display: none;" id="fieldJson" name="fieldJson"></textarea>
				<div id="tab">
					<!-- 基本信息  start-->
					<div tabid="baseSetting" id="table" title="基本信息">
						<div>
							<div class="tbar-title">
								<span class="tbar-label">基本信息</span>
							</div>
							<table class="table-detail" cellpadding="0" cellspacing="0"
								border="0" type="main" style="border-width: 0 !important;">
								<tr>
									<th width="20%">名称：<span class="required">*</span></th>
									<td><input type="text" id="name" name="name" value="${sysQuerySql.name}" validate="{required:true}" class="inputText" onblur="getKeyName(this)"/>
									</td>
									
									<th width="20%">别名：<span class="required">*</span></th>
									<td>
										<input type="text"  id="alias" name="alias" 
										value="${sysQuerySql.alias}" validate="{required:true}" 
										class="inputText" />
									</td>
								</tr>
								<tr>
									<th width="20%">数据源:</th>
									<td><select id="dsname" name="dsname">
											<c:forEach items="${dsList}" var="ds">
												<option value="${ds.alias}"
													<c:if test="${ds.alias eq sysQuerySql.dsname}">selected="selected"</c:if>>${ds.name}</option>
											</c:forEach>
									     </select>
									</td>
									
									<th width="20%">分类：</th>
									<td colspan="3">
										<select id="categoryId" name="categoryId">
											<option value="">无</option>
											<c:forEach items="${globalTypeList}" var="globalType">
												<option value="${globalType.typeId}"
													<c:if test="${globalType.typeId eq sysQuerySql.categoryId}">selected="selected"</c:if>>${globalType.typeName}</option>
											</c:forEach>
										</select></td>	
								</tr>
								<tr>
									<th width="20%">SQL语句：<span class="required">*</span>
									</th>
									<td colspan="3" valign="top"><textarea id="sql" name="sql"
											rows="12" style="width: 80%" validate="{required:true}">${sysQuerySql.sql}</textarea>
										<a class="button" id="btnValid"><span class="icon valid"></span><span>验证查询语句</span>
									</a></td>
								</tr>
							</table>
						</div>
					</div>
			</form>
			<!-- 基本信息  end-->
			<!-- 设置按钮  start-->
			<c:if test="${sysQuerySql.id ne null}"> 
				<div tabid="urlSetting" id="table" title="设置按钮">
					<div class="table-top-left">
						<div class="toolBar" style="margin: 0;">
							<div class="group">
								<a class="link add" id="btnSearch"
									onclick="_SysQuerySqlEdit_.addUrl()">添加</a>
							</div>
							
							<div class="group">
								<a class="link del " id="btnSearch"
									onclick="_SysQuerySqlEdit_.delUrl();">删除</a>
							</div>
						</div>
					</div>
					<table id="urlTbl" class="table-grid">
						<thead>
							<tr>
								<th width="3%">选择</th>
								<th width="8%">名称</th>
								<th width="8%">显示名称</th>
								<th width="8%">class名称</th>
								<th width="20%">click函数名称</th>
								<th>url路径</th>
								<th width="10%">管理</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 设置url  end-->
				<!-- 设置field start -->
				<div tabid="fieldSetting" id="table" title="设置字段">
					<table id="fieldTbl" class="table-grid">
						<thead>
							<tr>
								<th width="5%">序号</th>
								<th width="10%">列名</th>
								<th width="5%">类型</th>
								<th width="10%">描叙</th>
								<th width="5%">是否显示</th>
								<th width="5%">是否查询</th>
								<th width="5%">管理</th>
							</tr>
						</thead>
					</table>
					<table cellpadding="1" cellspacing="1" class="table-detail">
						<c:forEach var="field" items="${sysQueryFields}" varStatus="i">
							<input type="hidden" id="field_ct_${field.id}" name="field_ct_${field.id}"
								value="${field.controlType}" />
							<input type="hidden" id="field_id_${i.index }" name="field_id"
								value="${field.id }" />
							<tr>
								<th width="5%" id="sequence" class="sequence">${i.index+1}</th>
								<th width="10%" id="field_name_${i.index }">${field.name }</th>
								<th width="5%" id="field_type_${i.index }">${field.type }</th>
								<th width="10%"><input type="text"
									id="field_fieldDesc_${i.index }" name="field_fieldDesc"
									value="${field.fieldDesc }" /></th>
								<th width="5%"><input name="field_isShow"
									id="field_isShow_${i.index }" type="checkbox"
									value="${field.isShow }"
									<c:if test="${field.isShow eq 1}">checked</c:if> /></th>
								<th width="5%"><input name="field_isSearch"
									id="field_isSearch_${i.index }" type="checkbox"
									value="${field.isShow }"
									<c:if test="${field.isSearch eq 1}">checked</c:if> /></th>
								<th width="5%"><a href="####" class="querySql-a"
									onclick="openEditDetailDialog('${field.id}')">控件与格式化</a></th>
							</tr>
						</c:forEach>
					</table>
				</div>
			</c:if>
			<!-- 设置field end -->
		</div>
	</div>
	<!-- url模板 -->
	<div id="urlTemplate" style="display: none;">
		<table cellpadding="1" cellspacing="1" class="table-detail">
			<tr var="urlTr">
				<td var="index"><input class="pk" type="checkbox" name="select">
				</td>
				<td><input type="text" var="name" value=""></td>
				<td><input type="text" var="desc" value=""></td>
				<td><input type="text" var="className" value="detail"></td>
				<td><input type="text" style="width: 80%" var="clickName" value="openLinkDialog({scope:this,width:1200,height:800,isFull:false})"></td>
				<td><input type="text" style="width: 80%" var="urlPath"
					value=""> <textarea style="display: none;"
						id="setUrlParams" var="setUrlParams"></textarea>
				</td>
				<td><a class="link moveup" href="####"
					onclick="_SysQuerySqlEdit_.moveTr(this,true)"></a> <a
					class="link movedown" href="####"
					onclick="_SysQuerySqlEdit_.moveTr(this,false)"></a> <a
					class="link del" href="####" onclick="_SysQuerySqlEdit_.delTr(this)"></a>
					<a class="link edit" href="####"
					onclick="_SysQuerySqlEdit_.addOrEditUrlParams(this)"></a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>