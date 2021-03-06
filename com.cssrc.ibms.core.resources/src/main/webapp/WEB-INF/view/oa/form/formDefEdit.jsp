<%--
	time:2011-11-16 16:34:16
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑自定义表单</title>
<%@include file="/commons/include/form.jsp"%>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="tab/tab.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=bpmFormDef"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormDef.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/PageTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormContainer.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<!-- ueditor -->
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_api.js"></script>
<script type="text/javascript">
	var locale='zh_cn';
	var tabTitle="${bpmFormDef.tabTitle}";
	var formKey=${bpmFormDef.formKey};
	var tableId=${bpmFormDef.tableId};
	var tab;
	
	function showRequest(formData, jqForm, options) {
		return true;
	}
	
	$(function() {
		//验证代码
		valid(showRequest, showResponse);	
		//绑定保存按钮设置
		$("a.save").click(save);		
		var winHeight = $(window).height()-120;
		$("#frmDefLayout").ligerLayout({
			leftWidth : 200,
			height:winHeight,
			onHeightChanged:function(layoutHeight, diffHeight, middleHeight){
				alert(layoutHeight)
			}
		});
		var height = $(".l-layout-center").height();
    $("#colstree").height(height-120);
     
		
		tab = $("#tab").ligerGetTabManager();
		FormDef.getEditor({
			height:240,
			width:227,
			lang:locale
		});
		editor.addListener('ready',function(){
			initTab();
		});
		//ueditor渲染textarea
		editor.render("html");
		editor.tableId = tableId;
		//获取字段显示到左边的字段树中
		getAllFields();
		$("#btnPreView").click(function(){
			preview();
		});
	
		$("#btnHis").click(function(){ // 绑定历史记录按钮
			showHis();
		});
		
   		window.onbeforeunload = function() {				 											
   		  return '你确定吗？';
   	 	};
	});
	
	//预览
	function preview(){
		saveChange();
		window.onbeforeunload  =  null ;
		var objForm = formContainer.getResult();
		var frm=new com.ibms.form.Form();
		frm.creatForm("bpmPreview",__ctx+"/oa/form/formHandler/edit.do");
		frm.addFormEl("name",$("#subject").val());
		frm.addFormEl("title",objForm.title);
		frm.addFormEl("headHtml",$("#headHtml").val());
		frm.addFormEl("html",objForm.form);
		frm.addFormEl("comment",$("#formDesc").val());
		frm.addFormEl("tableId",tableId);
		frm.setTarget("_blank");
		frm.setMethod("post");
		frm.submit();
		frm.clearFormEl();
	};
	
	
	//保存表单数据。
	function save() {
		if (FormDef.isSourceMode) {
			$.ligerDialog.warn('不能在源代码模式下保存表单',"提示信息");
			return;
		}
		saveChange();
		var rtn = $("#bpmFormDefForm").valid();
		if (!rtn)
			return;
		//syncOpinion();
		var data = {};
		//把表单的"表单标题"，"描述"和hidden数据 放入data数组中。
		var arr = $('#bpmFormDefForm').serializeArray();
		$.each(arr, function(i, d) {
			data[d.name] = d.value;
		});

		//保存当前tab的数据。
		var idx = tabControl.getCurrentIndex() - 1;
		//tab切换时保存数据
		saveTabChange(idx);
		//返回整个表单数据结果。
		var objForm = formContainer.getResult();

		data['tabTitle'] = objForm.title;
				
		data['html'] = objForm.form;
		
        while(data['html'].indexOf('？')!=-1){
        	data['html']=data['html'].replace('？','');
       }
        //对应FormDefFormController中的"save"方法
		$.post('save.do', {
			data : JSON2.stringify(data),
			tableName : $('#tableName').val()
		}, FormDef.showResponse);
	}

	function getAllFields() {
		//FormDef.js  根据表获取字段和子表，构建树。
		FormDef.getFieldsByTableId(tableId);
	}

	//tab控件
	var tabControl;
	//存储数据控件。
	var formContainer;
	//添加tab页面
	function addCallBack() {
		var curPage = tabControl.getCurrentIndex();
		var str = "新页面";
		var idx = curPage - 1;
		formContainer.insertForm(str, "", idx);
		//tab切换时保存数据
		saveTabChange(idx-1,idx);		
	}
	//切换tab之前，返回false即中止切换
	function beforeActive(prePage) {
		if (FormDef.isSourceMode) {
			$.ligerDialog.warn('不能在源代码模式下切换页面',"提示信息");
			return 0;
		}
		return 1;
	}
	//点击激活tab时执行。
	function activeCallBack(prePage, currentPage) {
		if (prePage == currentPage)
			return;
		//保存上一个数据。
		saveTabChange(prePage - 1, currentPage - 1);
	}
	//根据索引设置数据
	function setDataByIndex(idx) {
		if (idx == undefined)
			return;
		var obj = formContainer.getForm(idx);
		editor.setContent(obj.form);
		$("b", tabControl.currentTab).text(obj.title);
	}
	//在删除页面之前的事件，返回false即中止删除操作
	function beforeDell(curPage) {
		if (FormDef.isSourceMode) {
			$.ligerDialog.warn('不能在源代码模式下删除页面',"提示信息");
			return 0;
		}
		return 1;
	}
	//点击删除时回调执行。
	function delCallBack(curPage) {
		formContainer.removeForm(curPage - 1);
		var tabPage = tabControl.getCurrentIndex();
		//根据索引设置数据
		setDataByIndex(tabPage - 1);
	}
	//文本返回
	function txtCallBack() {
		var curPage = tabControl.getCurrentIndex();
		var idx = curPage - 1;
		var title = tabControl.currentTab.text();
		//设置标题
		formContainer.setFormTitle(title, idx);
	}
	//tab切换时保存数据
	function saveTabChange(index, curIndex){
		var data = editor.getContent();
		formContainer.setFormHtml(data, index);
		//根据索引设置数据
		setDataByIndex(curIndex);
	}
	//表单或者标题发生变化时保存数据。
	function saveChange() {
		var index = tabControl.getCurrentIndex() - 1;
		var title = tabControl.currentTab.text();
		var data = editor.getContent();
		formContainer.setForm(title, data, index);
	}
	//初始化TAB
	function initTab() {
		var formData = editor.getContent();
		if (tabTitle == ""){
			tabTitle = "页面1";
		}
		//实现在FormContainer.js（ 多页表单容器）
		formContainer = new FormContainer();
		var aryTitle = tabTitle.split(formContainer.splitor);
		var aryForm = formData.split(formContainer.splitor);
		var aryLen = aryTitle.length;
		//初始化
		formContainer.init(tabTitle, formData);
	    //初始化左边供选择的表字段
	    //PageTab实现在PageTab.js中
		tabControl = new PageTab("pageTabContainer", aryLen, {
			addCallBack : addCallBack,
			beforeActive : beforeActive,
			activeCallBack : activeCallBack,
			beforeDell : beforeDell,
			delCallBack : delCallBack,
			txtCallBack : txtCallBack
		});
		tabControl.init(aryTitle);
		if (aryLen > 1) {
			editor.setContent(aryForm[0]);
		};		
	};
	
	// 显示历史记录
	function showHis(){
		var formDefId = $("#formDefId").val();
		DialogUtil.open({
            height:540,
            width: 800,
            title : '历史操作记录',
            url: "${ctx}/oa/form/formDefHi/list.do?formDefId="+formDefId, 
            isResize: true,
            //自定义参数
            sucCall:function(rtn){
            	if(rtn != null){
	            	restoreRecord(rtn);// 恢复数据
            	}
            }
        });
	};
	
	// 恢复数据
	function restoreRecord(hisId){
		$.ajax({
			url: "${ctx}/oa/form/formDefHi/getByAjax.do?hisId="+hisId,
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(result) {
				if(result != null){
					$("#subject").val(result.subject);
					$("#formDesc").val(result.formDesc);
					$("#headHtml").val(result.headHtml);
					tabTitle = result.tabTitle;
					editor.setContent(result.html);
					initTab();
					$("#subject").focus().select();
				}
			}
		});
	}
</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">在线表单编辑</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group">
					<a class="link save" id="dataFormSave" href="####">保存</a>
				</div>
				
				<div class="group">
					<a class="link preview" id="btnPreView" href="####">预览</a>
				</div>
				<div class="group">
					<a class="link" id="btnHis" href="####">查看历史记录</a>
				</div>
				<div class="group">
					<a class="link  del" href="javascript:window.onbeforeunload = null;window.close()">关闭</a>
				</div>
				<div class="group">
				    <a href="####" class="tipinfo">
				    <span>
						1.${fn:escapeXml("如果设置<td style='display:none;'>，则无法显示，但可以将在一个td中放置多个'<input>'，且type=hidden。")}<br/>
					</span>
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<div class="panel-table">
			<form id="bpmFormDefForm" method="post">
				<input id="formDefId" type="hidden" name="formDefId" value="${bpmFormDef.formDefId}" /> 
				<input id="tableId" type="hidden" name="tableId" value="${bpmFormDef.tableId}" />
				<input id="categoryId" type="hidden" name="categoryId" value="${bpmFormDef.categoryId}" /> 
				<input  type="hidden" name="formKey" value="${bpmFormDef.formKey}" /> 
				<input  type="hidden" name="isDefault" value="${bpmFormDef.isDefault}" /> 
				<input  type="hidden" name="formAlias" value="${bpmFormDef.formAlias}" /> 
				<input  type="hidden" name="versionNo" value="${bpmFormDef.versionNo}" /> 
				<input  type="hidden" name="isPublished" value="${bpmFormDef.isPublished}" /> 
				<input  type="hidden" name="publishedBy" value="${bpmFormDef.publishedBy}" /> 
				<input  type="hidden" name="publishTime" value="<fmt:formatDate value="${bpmFormDef.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 
				<input  type="hidden" id="templatesId" name="templatesId" value="${bpmFormDef.templatesId}"/> 
				
				<div class="panel-nav">
					<table cellpadding="0" cellspacing="0" border="0"  class="table-detail">
						<tr>
							<th width="80">表单标题:&nbsp;</th>
							<td style="padding:4px;"><input id="subject" type="text" name="subject" value="${bpmFormDef.subject}" class="inputText" style="width:86%" /></td>
							<th width="80">描述:&nbsp;</th>
							<td style="padding:4px;"><input id="formDesc" type="text" name="formDesc" value="${bpmFormDef.formDesc}" class="inputText" style="width:86%"/></td>
						</tr>
						<tr>
							<th width="10%">表单headHtml:</th>
							<td colspan="3">
								<textarea id="headHtml" name="headHtml" style="width:80%;height:70px" >${fn:escapeXml(bpmFormDef.headHtml)}</textarea>
								<a href="####" class="tipinfo">
									<span>
										作用:添加css、js文件</br>	
										1.	css添加</br>	
										 <b>  ${fn:escapeXml("<link href='${ctx}/jslib/pictureShow/css/jquery.fancybox.css' rel='stylesheet' />")}</b></br>	
										2.	js添加</br>	
										 <b>  ${fn:escapeXml("< script type='text/javascript' src='${ctx}/jslib/ibms/oa/desktop/test.js'/>")}</b></br>	
									</span>
								</a>
							</td>
						</tr>
					</table>
				</div>
			</form>
			<div id="tab" class="panel-nav">
				<div id="frmDefLayout">
					<div position="left" title="表字段">
						<div class="tree-toolbar tree-title">
							<span class="toolBar">
								<div class="group">
									<a class="link reload" onclick="getAllFields()">刷新</a>
								</div>
							</span>
						</div>
						<ul id="colstree" class="ztree"></ul>
					</div>
					<div id="editor" position="center">
						<textarea id="html" name="html">${fn:escapeXml(bpmFormDef.html)}</textarea>
						<div id="pageTabContainer"></div>
					</div>
				</div>
			</div>
		</div>	
	</div>
<div>	
</body>
</html>

