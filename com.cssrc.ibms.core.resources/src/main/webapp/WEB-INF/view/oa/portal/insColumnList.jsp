<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>栏目列表管理</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
</head>
<body>

	<ibms:toolbar entityName="com.cssrc.ibms.index.model.InsColumn" excludeButtons="popupSearchMenu,popupAttachMenu,popupSettingMenu" />
	<ibms:pager pagerId="pagerButtons" entityName="com.cssrc.ibms.index.model.InsColumn" />

	<div class="mini-fit" style="height: 100px;">
		<div id="datagrid1" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="false" url="${ctx}/oa/portal/insColumn/getAll.do" idField="colId" multiSelect="true" showColumnsMenu="true" sizeList="[5,10,20,50,100,200,500]" pageSize="20" allowAlternating="true" ondrawcell="onDrawCell" pagerButtons="#pagerButtons">
			<div property="columns">
				<div type="checkcolumn" width="20"></div>
				<div field="action" name="action" cellCls="actionIcons" width="50" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">#</div>
				<div field="name" width="160" headerAlign="center" allowSort="true">栏目名称</div>
				<div field="key" width="120" headerAlign="center" allowSort="true">栏目Key</div>
				<div field="enabled" width="60" headerAlign="center" allowSort="true">是否启用</div>
				<div field="colType" width="100" headerAlign="center" allowSort="true">信息栏目类型</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		//编辑
		
		function onDrawCell(e) {
			var record = e.record;
			var colType = record.colType;
			var uid = record.colId;
			if (e.field == "action") {
				if (colType != "新闻、公告") {
					e.cellHtml = '<span class="icon-detail" title="明细" onclick="detailRow(\'' + uid + '\')"></span>'  
					+ ' <span class="icon-edit" title="编辑" onclick="editRow(\'' + uid + '\')"></span>' 
					+ ' <span class="icon-remove" title="删除" onclick="delRow(\'' + uid + '\')"></span>';
				}
			}
		}
		
		function onActionRenderer(e) {
			var record = e.record;
			var uid = record.colId;
			var name = record.name;
			var s = '<span class="icon-detail" title="明细" onclick="detailRow(\'' + uid + '\')"></span>' 
			+ '<span class="icon-col-news" title="管理公告" onclick="mgrNewsRow(\'' + uid + '\',\'' + name + '\')"></span>' 
			+ ' <span class="icon-edit" title="编辑" onclick="editRow(\'' + uid + '\')"></span>' 
			+ ' <span class="icon-remove" title="删除" onclick="delRow(\'' + uid + '\')"></span>';
			return s;
		}
		//弹出一个Tab进行管理该栏目下的所有公告
		function mgrNewsRow(colId, name) {
			top['index'].showTabFromPage({
				title : name + '-信息公告',
				tabId : 'colNewsMgr_' + colId,
				url : __ctx + '/oa/portal/insNews/byColId.do?colId=' + colId
			});
		}
	</script>
	<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
	<ibms:gridScript gridId="datagrid1" entityName="com.cssrc.ibms.index.model.InsColumn" winHeight="390" winWidth="800" entityTitle="栏目" baseUrl="oa/portal/insColumn" />
</body>
</html>