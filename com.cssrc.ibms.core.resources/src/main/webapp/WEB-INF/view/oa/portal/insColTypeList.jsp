<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>栏目类型列表管理</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
</head>
<body>

	<ibms:toolbar entityName="com.cssrc.ibms.index.model.InsColType" excludeButtons="popupSearchMenu,popupAttachMenu,popupSettingMenu"/>
	<ibms:pager pagerId="pagerButtons" entityName="com.cssrc.ibms.index.model.InsColType" /> 

	<div class="mini-fit" style="height: 100%;">
		<div id="datagrid1" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="false" url="${ctx}/oa/portal/insColType/getAll.do" idField="typeId" multiSelect="true" showColumnsMenu="true" sizeList="[5,10,20,50,100,200,500]" pageSize="20" allowAlternating="true" pagerButtons="#pagerButtons">
			<div property="columns">
				<div type="checkcolumn" width="20"></div>
				<div name="action" cellCls="actionIcons" width="50" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">#</div>
				<div field="name" width="100" headerAlign="center" allowSort="true">栏目名称</div>
				<div field="key" width="80" headerAlign="center" allowSort="true">栏目Key</div>
				<div field="url" width="120" headerAlign="center" allowSort="true">栏目映射URL</div>
				<div field="moreUrl" width="120" headerAlign="center" allowSort="true">栏目更多URL</div>
				<div field="memo" width="120" headerAlign="center" allowSort="true">栏目分类描述</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
        	//行功能按钮
	        function onActionRenderer(e) {
	            var record = e.record;
	            var pkId = record.typeId;
	            var s = '<span class="icon-detail" title="明细" onclick="detailRow(\'' + pkId + '\')"></span>'
	                    + ' <span class="icon-edit" title="编辑" onclick="editRow(\'' + pkId + '\')"></span>'
	                    + ' <span class="icon-remove" title="删除" onclick="delRow(\'' + pkId + '\')"></span>';
	            return s;
	        }
        </script>
	<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
	<ibms:gridScript gridId="datagrid1" entityName="com.cssrc.ibms.index.model.InsColType" winHeight="450" winWidth="800" entityTitle="栏目类型" baseUrl="oa/portal/insColType" />
</body>
</html>