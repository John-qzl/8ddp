<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>业务数据模板预览</title>
	<%@include file="/commons/include/get.jsp" %>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<link rel="stylesheet" href="${ctx}/jslib/jquery/multiple-select.css" />
	<link rel="stylesheet" href="${ctx}/styles/default/css/8ddp/dataTemplatePreview.css" />
	<!--初始化数据字典的样式-->
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/dicCombo.js"></script>
	
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/ajaxgrid.js"></script><!--所有列表和按钮操作的js在 /jslib/ibms/ajaxgrid.js中-->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ReportUtil.js"></script><!-- 报表打印浏览js-->
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script><!-- 自定义表单验证插件 -->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/Export.js"></script><!-- 初始化导出按钮事件 -->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script><!-- 组织选择器 -->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorUtil.js"></script><!-- *选择器帮助类 -->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorFKShowUtil.js"></script><!-- *查询区域有外键字段的情况-->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script><!-- *查询区域有外键字段的情况----弹出自定义查询框-->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/QueryAndListUtil.js"></script> <!-- *查询区域事件、列表的脚本事件-->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/LineValueFilter.js"></script><!-- 列值过滤 -->
	<script type="text/javascript" src="${ctx}/jslib/util/fileUtil.js"></script><!-- *文件下载js-->
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/ComplexDetail.js"></script><!-- *复杂表单初始化-->
	<script type="text/javascript" src="${ctx}/jslib/jquery/multiple-select.js"></script><!-- 下拉多选框 -->
	<!--start  自定义js文件，css文件  -->
		${headHtml}
	<!--end    自定义js文件，css文件  -->
	<script type="text/javascript">
	$(function(){
		//删除一条记录时的刷新
//		if(window.parent.document.getElementById("treeFresh")) {
//			window.parent.document.getElementById("treeFresh").click();
//		};

		handleAjaxSearchKeyPress();//初始化查询 处理回车查询  方法在ajaxgrid.js中
		Export.initExportMenu();   //初始化导出按钮，方法在Export.js中
		SelectorFKShowUtil.initFKColumnShow();//初始化  绑定查询条件中的 外键按钮,方法在SelectorFKShowUtil.js中
		__complexDetail__.init();//生成权限按钮，并在url后添加类别信息	
	});
	</script>
</head>
<body style="overflow: hidden;">
	<div id="content" style="height: 100%;">
		${html}
	</div>
</body>
<!-- 系统业务数据模板结构  
   *****所有列表和按钮操作的js在 /jslib/ibms/ajaxgrid.js中
   ***** "查询" 按钮执行action /ibms/oa/form/dataTemplate/getDisplay.do
   *****displayId="10000006150001"   为IBMS_DATA_TEMPLATE表的id = 10000006150001
<body>
<div id="content">
	<div class="panel" ajax="ajax"  displayId="10000006150001" filterKey="syr" >
		<div class="panel-top">
		查询区域 ***************************** (对应ajaxgrid.js中的function handlerSearchAjax(obj)方法)
			<form id="searchForm" name="searchForm" method="post" action="/ibms/oa/form/dataTemplate/getDisplay.do">
			</form>
		</div>
		<div class="panel-body">
			<table cellpadding="1" cellspacing="1" class="table-grid table-list">
			业务数据
			</table>
			<div class="panel-page">	
			翻页
			</div>

		</div>
	</div>
	<div style="display: none;" id="exportField" >
	<form id="exportForm" name="exportForm" method="post" target="download" action="exportData.do" style="display: none;">
	</form>  
	<iframe id="download" name="download" height="0px" width="0px" style="display: none;">
	</iframe>  	 
</div>
</body>
 -->
</html>
