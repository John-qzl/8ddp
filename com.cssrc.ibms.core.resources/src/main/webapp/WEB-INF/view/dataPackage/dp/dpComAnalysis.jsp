<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>工作统计</title>
	<%@include file="/commons/include/form.jsp" %>

</head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/iconfont.css"></link>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/iconImg.css"></link>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/Aqua/css/ligerui-all.css"></link>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/tree/zTreeStyle.css"></link>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/web.css"></link>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/layout/layout.all.css"></link>
<script type="text/javascript" src="/dp/jslib/lang/common/zh_CN.js"></script>
<script type="text/javascript" src="/dp/jslib/lang/js/zh_CN.js"></script>
<script type="text/javascript" src="/dp/jslib/lang/view/oa/system/zh_CN.js"></script>
<link rel="stylesheet" href="/dp/layui/css/layui.css">
<script type="text/javascript" src="/dp/layui/layui.js"></script>
<style>
	/*form管理字体颜色*/
	a.ops_more {
		color: #0378ca;
	}
	.tableNewHeader_con{
		color: #0378ca;
	}
	th, .headRow th{
		color: #0378ca;
	}

	/*form管理背景颜色*/
	.ops_itemDiv li a:hover {
		background-color: #297dbd;
	}
	.l-dialog-buttons {
		border-top: 1px solid #297dbd;
	}




	/*主题颜色*/
	.ops_btn.ops_active:before {
		color:#2787d4;
	}
	.block-calendar-right{
		background: #2787d4;
	}
	.lbox-hd-h1 {
		color: #2787d4;
	}
	.lbox-hd-v {
		color: #2787d4;
	}
	.lbox-hd .down {
		color: #2787d4
	}
	.lbox-hd .down a{
		color: #2787d4
	}
	.btn {
		background: #2787d4;
	}
	.viewFramework-topbar {
		background: #2787d4;
	}
	.v-t-menu .base-tool-arrow b {
		border-top-color: #2787d4
	}
	.block-calendar-right {
		background: #2787d4;
	}
	/*边栏*/
	#layoutMain .l-layout-left .l-layout-header {
		background: #2787d4;
		border-bottom: 1px #ffffff solid;
	}
	.l-layout-content>.l-tab-links{
		background: #2787d4;
	}
	.sidemenu li.firstmenu {
		background-color: #2787d4;
	}
	/*边栏背景*/
	.sidecolor {
		background: #ffffff!important;
	}
	.sidemenu .secondmenu>li {
		background-color: #ffffff;
	}
	.sidemenu .fourmenu li {
		background-color: #ffffff;
	}
	/*form/table*/



	/*tab/btn*/
	.l-dialog-title {
		background-color: #347EFE;
	}
	.l-layout-header {
		background-color: #347EFE;
	}
	#tab_bar li {
		color:#347EFE;
	}
	.l-layout-left .l-layout-header,.l-layout-right .l-layout-header{
		background:#347EFE
	}
	.l-layout-collapse-left-toggle, .l-layout-collapse-right-toggle {
		background-color: #347EFE;
	}
	.l-layout-collapse-left {
		background: #347EFE;
	}
	.l-menu {
		border: 1px solid #347EFE;
	}
	.l-tab-links li.l-selected {
		background: #347EFE;
	}
	td.formHead{
		color: #347EFE;
	}
	.active-li {
		background: #347EFE;
	}
	.ops_btn:before {
		color: #347EFE;
	}
	div.panel-body>span.iconfont:hover{
		color: #347EFE;
	}
	div.panel-body>span.iconfont.selected{
		color: #347EFE;
	}
	.user-defined-table>div.thead-title.checked {
		background: #347EFE;
	}
	ul.col-filter {
		background: #347EFE;
	}
	div.header + .content{
		border: 1px solid #347EFE;
	}
	th a.querySql-a{
		color: #347EFE;
	}
	a {
		color: #347EFE;
	}

	a.link {
		color: #347EFE;
	}
	a.activi {
		color: #347EFE;
	}
	a.tipinfo span {
		border: 1px solid #347EFE;
	}
	.table-top .table-top-left {
		color: #347EFE;
	}
	.panel-container {
		border-color: #347EFE;
	}
	.panel-container .l-tab-links {
		background: #347EFE;
	}
	.panel-container .l-tab-links .l-selected a {
		color: #347EFE;
	}
	.foldBox {
		border-top: 2px solid #347EFE;
	}
	.foldBox .title {
		color: #347EFE;
	}
	.foldBox .drop {
		color: #347EFE;
	}
	.searchShift span,.searchShift label{
		color: #347EFE;
	}
	.panel-toolbar a.link,#searchForm a.link {
		color: #347EFE;
		border: 1px solid #347EFE;
	}
	.csb_btnOpe>p:first-child{
		color: #347EFE;
	}
	.panel-toolbar a.link:hover,#searchForm a.link:hover {
		background-color: #347EFE;
	}
	.panel-search .title {
		color: #347EFE;
	}
	.panel-complex-search .title{
		color: #347EFE;
	}
	.panel-search .title {
		color: #347EFE;
	}
	.sidemenu p:hover {
		background-color: #347EFE;
	}
	.mainColor{
		color: #347EFE;
	}
	.table-grid caption{
		color: #347EFE;
	}
	.table-grid th,
	th.sorted a,
	th.sortable a{
		background: #347EFE;
	}
	.tab-menu li:hover{
		color:#347EFE;
	}
	.l-tab-links li:hover {
		background: #347EFE;
	}

	a.button {
		color: #347EFE;
	}
	a.button:hover {
		border-color: #347EFE;
	}
	.panel-search{
		border-top: solid 1px #347EFE;
	}


</style>
<script type="text/javascript" src="/dp/js/dynamic.jsp"></script>
<script type="text/javascript" src="/dp/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="/dp/jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/dp/jslib/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="/dp/jslib/jquery/additional-methods.min.js"></script>
<script type="text/javascript" src="/dp/jslib/jquery/jquery.validate.ext.js"></script>
<script type="text/javascript" src="/dp/jslib/util/util.js"></script>
<script type="text/javascript" src="/dp/jslib/util/json2.js"></script>
<script type="text/javascript" src="/dp/jslib/util/form.js"></script>
<script type="text/javascript" src="/dp/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="/dp/jslib/lg/ligerui.all.js"></script><!-- å¼å¥ligerui.all.js-->
<script type="text/javascript" src="/dp/jslib/lg/plugins/ligerDialog.js" ></script>
<script type="text/javascript" src="/dp/jslib/lg/plugins/ligerResizable.js" ></script>
<script type="text/javascript" src="/dp/jslib/lg/util/DialogUtil.js" ></script>
<script type="text/javascript" src="/dp/jslib/calendar/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/dp/jslib/ibms/oa/system/Share.js"></script>
<script type="text/javascript" src="/dp/jslib/ibms/tipsOps.js" ></script><!-- ç®¡çåæ¾ç¤º-->
<script type="text/javascript" src="/dp/jslib/ibms/panelBodyHeight.js" ></script><!-- ç®¡çåæ¾ç¤º-->
<script type="text/javascript" src="/dp/jslib/ibms/oa/form/TableFactory.js" ></script><!-- TableFactory-->
<style type="text/css">
	.tree-title{overflow:hidden;width:100%;}
	html,body{ padding:0px; margin:0; width:100%;height:100%;overflow: hidden;}

	.check {margin-bottom: 10px; height: 30px; float: right;}
	.check .group {
		width: 65px;
		height: 20px;
		border: 1px solid #48A1E4;
		text-align: center;
		margin-left: 0;
		color: #2787d4;
		border-radius: 3px;
		cursor: pointer;
	}
	.check .group:Hover {background: #3eaaf5;color: #fff!important}
	.required {color: red;}
	.p-body {display: none;}
	.p-detail {width: 100%; height: 100%;float: left;overflow: auto;}

	table {width: 100%;border: 1px solid #dddddd;}
	table tr {height: 42px; line-height: 42px;}
	table td,table th {text-align: center;}
	table td {
		border-right:1px dashed rgb(206, 206, 206);
		border-bottom:1px dashed rgb(206, 206, 206);
	}
	table th {
		border-right: 1px dotted #fff;
		border-bottom: none;
		color: #fff;
		font-weight: bold;
		background: #3eaaf5;
		font-size: 16px;
		white-space: nowrap;
	}

	.chart {display: none; width: 50%; height: 100%; float: right; overflow: auto;}
	#iecharts {width: 100%; height: 100%;box-sizing: border-box;}
	.xecharts {display: none; width: 100%; height: 100%;}

	.inputText{
		outline: none;
		margin: 0px 15px;
		height: 28px;
		line-height: 28px;
		vertical-align: middle;
		box-sizing: border-box;
		background-color: #ffffff;
		border: 1px solid #cccccc;
		-webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
		transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	}
</style>
<script type="text/javascript" src="/dp/jslib/echarts/chart/echarts.min.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<script>
	//获取当前模块:产品验收,武器所检,靶场试验
	var ofPart=$.getParameter("ofPart");

	//所选型号
	function getModuleId() {
		var moduleIds = [];
			$("#moduleSelectUl").find('input[name="checkbox"]:checked').each(function() {
				moduleIds.push(this.value);
			})
			if (moduleIds.length == 0) {
				alert("请选择型号！");
				return false;
			}
		return moduleIds;
	}

	//查询
	function query() {

		var moduleId = $("#modulesStroge").val();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if (moduleId == false ) {
			alert("请选择型号");
			return false;
		} else {
			var url="${ctx}/dataPackage/dp/dpCom/searchFormByModules.do?modules="+moduleId+"&startTime="+startTime+"&endTime="+endTime+"&ofPart="+ofPart;
			$("#resultIframe").attr("src",url);
		}
	}

	//根据部门查询
	function queryByDep() {

		var departments = $("#departmentStroge").val();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if (departments == false ) {
			alert("请选择部门");
			return false;
		} else {
			var url="${ctx}/dataPackage/dp/dpCom/searchFormByDepartments.do?departments="+departments+"&startTime="+startTime+"&endTime="+endTime+"&ofPart="+ofPart;
			$("#resultIframe").attr("src",url);
		}
	}

	//重置(清除复选框及时间选择器的值)
	function reset() {
		$("#departmentSelector").val("");
		$("#modulesSelector").val("");
		$("#departmentStroge").val("");
		$("#modulesStroge").val("");

		$("#startTime").val("");
		$("#endTime").val("");
	}

	//全选所有型号的复选框
	function checkAllCheckbox() {
		$("#moduleSelectUl").find('input[name="checkbox"]').attr("checked", "true");
	}

	//取消全选所有型号的复选框
	function unCheckAllCheckbox() {
		$("#moduleSelectUl").find('input[name="checkbox"]').removeAttr("checked");
	}

	//修改查询条件: 型号/部门
	function changeAnalysisCondition() {
		//获取当前筛选条件
		var analysisCondition=$(".analysisConditionSelecter").val();
		if (analysisCondition=="module"){
			//依据型号选择,隐藏部门信息及按钮
			$("#moduleSearchInfo").removeAttr("style");
			$("#departmentSearchInfo").attr("style","display:none");

		}else {
			//依据部门选择,隐藏型号信息及按钮
			$("#moduleSearchInfo").attr("style","display:none");
			$("#departmentSearchInfo").removeAttr("style");
		}
	}

	//选择型号
	function selectModules() {
		var paramValueString = "";
		CommonDialog("xzxh",function(data){
			console.log(data);
			var selectedModules;
			var selectedModuleIds;
			for (var i=0;i<data.length;i++){
				if (i==0){
					selectedModuleIds=data[i].ID;
					selectedModules=data[i].F_XHMC;
				}else {
					selectedModuleIds=selectedModuleIds+","+data[i].ID;
					selectedModules=selectedModules+","+data[i].F_XHMC;
				}
			}
			$("#modulesSelector").val(selectedModules);
			$("#modulesStroge").val(selectedModuleIds);
			//data返回 Object { ORGID = "参数值"}，多个则返回 Object 数组
		},paramValueString);
	}

	//选择部门
	function selectDepartment() {
		var paramValueString = "";
		CommonDialog("xzbm",function(data){
			console.log(data);
			var selectedDepartments;
			var selectedDepartmentIds;
			for (var i=0;i<data.length;i++){
				if (i==0){
					selectedDepartmentIds=data[i].ORGID;
					selectedDepartments=data[i].ORGNAME;
				}else {
					selectedDepartmentIds=selectedDepartmentIds+","+data[i].ORGID;
					selectedDepartments=selectedDepartments+","+data[i].ORGNAME;
				}
			}
			$("#departmentSelector").val(selectedDepartments);
			$("#departmentStroge").val(selectedDepartmentIds);
			//data返回 Object { ORGID = "参数值"}，多个则返回 Object 数组
		},paramValueString);
	}

</script>
<body>
<div style="padding: 5px">
	<div>
	<label>查询条件：</label>&nbsp;
	<select name="analysisByModuleOrDepartment" class="analysisConditionSelecter" onchange="changeAnalysisCondition()">
		<option value="module" selected>型号</option>
		<option value="department">部门</option>
	</select>
	</div>
	<div id="moduleSearchInfo">
	<form name="selectedModules" action="" style="height:80px; padding: 5px;" >
		
		<div style="padding-right: 20px;">
			<div style="padding: 5px 0 10px;">
			<span><label>开始时间：</label>&nbsp;<input type="date" class="inputText" name="startTime" id="startTime"/></span>
			<span style="margin-left: 10px"><label>结束时间：</label>&nbsp;<input type="date" class="inputText" name="endTime" id="endTime"/></span>
			</div>
			<div><label>选择型号：</label>&nbsp;<input onclick="selectModules()" class="inputText" placeholder="点击选择型号"  id="modulesSelector"/><div class="check">
			<div class="group" onclick="javascript:query(this);">查询</div>
			<div class="group" onclick="javascript:reset(this);">重置</div>
		</div></div>
			<input id="modulesStroge" style="display: none"/>
		</div>
	</form>
</div>
<div id="departmentSearchInfo" style="display: none">
	<form name="selectedDepartments" action="" style="height:80px; padding: 5px;">
		
		<div style="padding-right: 20px;">
		<div style="padding: 5px 0 10px;">
		<span><label>开始时间：</label>&nbsp;<input type="date" class="inputText"  name="startTime" id="startTimeOfDepartment"/></span>
		<span style="margin-left: 10px"><label>结束时间：</label>&nbsp;<input type="date" class="inputText"  name="endTime" id="endTimeOfDepartment"/></span>
		</div>
		<div><label>选择部门：</label>&nbsp;<input onclick="selectDepartment()" class="inputText"  placeholder="点击选择部门" id="departmentSelector"/>
		<div class="check">
			<div class="group" onclick="javascript:queryByDep(this);">查询</div>
			<div class="group" onclick="javascript:reset(this);">重置</div>
		</div>
		<input id="departmentStroge" style="display: none"/>
		</div>
		</div>
		<%--<ul class="row" id="departmentSelectUl">
			<c:forEach items="${orgList}" var="org">
				<li>
				<span>
					<input type="checkbox" name="checkbox" value="${org.orgId}">
				</span>
					<label>${org.orgName}</label>
				</li>
			</c:forEach>
		</ul>--%>
	</form>
</div>
</div>




<iframe  width="100%"  height="700px" frameborder="0"  scrolling="yes" id="resultIframe" src="" >
</iframe>
</body>
</html>