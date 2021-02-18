<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻公告列表管理</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
</head>
<body>

	<ibms:toolbar entityName="com.cssrc.ibms.index.model.InsNews" excludeButtons="popupSearchMenu,popupAttachMenu,popupSettingMenu">
		<div class="self-toolbar">
			<a class="mini-button" iconCls="icon-agree" plain="true" onclick="morePublish()">批量发布</a>
		</div>
	</ibms:toolbar>
	<ibms:pager pagerId="pagerButtons" entityName="com.cssrc.ibms.index.model.InsNews" />

	<div class="mini-fit" style="height: 100px;">
		<div id="datagrid1" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="false" url="${ctx}/oa/portal/insNews/getAll.do" idField="newId" multiSelect="true" showColumnsMenu="true" sizeList="[5,10,20,50,100,200,500]" pageSize="20" allowAlternating="true" ondrawcell="onDrawCell" pagerButtons="#pagerButtons">
			<div property="columns">
				<div type="checkcolumn" width="20"></div>
				<div field="action" name="action" cellCls="actionIcons" width="60" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">管理</div>
				<div field="subject" width="150" headerAlign="center" allowSort="true">标题</div>
				<div field="keywords" width="100" headerAlign="center" allowSort="true">关键字</div>
				<div field="readTimes" width="80" headerAlign="center" allowSort="true">阅读次数</div>
				<div field="author" width="80" headerAlign="center" allowSort="true">作者</div>
				<div field="status" width="80" headerAlign="center" allowSort="true">状态</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		//编辑
		var grid = mini.get("datagrid1");
		function onActionRenderer(e) {
			var record = e.record;
			var newId = record.newId;
			var s = '<span class="icon-agree" title="发布" onclick="publish(\'' + newId + '\')"></span>' 
			+ '<span class="icon-detail" title="明细" onclick="detailRow1(\'' + newId + '\')"></span>' 
			+ ' <span class="icon-edit" title="编辑" onclick="editRow(\'' + newId + '\')"></span>' 
			+ ' <span class="icon-remove" title="删除" onclick="delRow(\'' + newId + '\')"></span>';
			return s;
		}

		function onDrawCell(e) {
			var record = e.record;
			var status = record.status;
			var newId = record.newId;
			if (e.field == "action") {
				var s='';
				if (status == "Draft") {
					s+='<span class="icon-agree" title="发布" onclick="publish(\'' + newId + '\')"></span>';
				}
				s+= '<span class="icon-detail" title="明细" onclick="detailRow1(\'' + newId + '\')"></span>' ;
				s+= ' <span class="icon-edit" title="编辑" onclick="editRow(\'' + newId + '\')"></span>' ;
				s+= ' <span class="icon-remove" title="删除" onclick="delRow(\'' + newId + '\')"></span>';
				e.cellHtml =s ;
			}
			if(e.field == "status"){
				if(status == "Issued"){
					e.cellHtml = '发布';
				}else if(status == "Draft"){
					e.cellHtml = '草稿';
				}else if(status == "Archived"){
					e.cellHtml = '归档';
				}
			}
		}

		function morePublish() {
			var rows = grid.getSelecteds();
			var pkIds = new Array();
			if (rows.length > 0) {
				for (var i = 0; i < rows.length; i++) {
					if (rows[i].status == "Issued") {
						mini.showTips({
							content : "您选中的有不可发布的新闻。",
							state : "danger",
							x : "center",
							y : "center",
							timeout : 4000
						});
						return;
					}
					pkIds[i] = rows[i].pkId;
				}
				_OpenWindow({
					url : "${ctx}/oa/portal/insNews/publish.do?pkId=" + pkIds,
					title : "新闻发布",
					width : 800,
					height : 400
				});
			} else {
				mini.showTips({
					content : "请选择新闻。",
					state : "info",
					x : "center",
					y : "top",
					timeout : 4000
				});
				return;
			}
		}

		function publish(pkId) {
			_OpenWindow({
				url : "${ctx}/oa/portal/insNews/publish.do?pkId=" + pkId,
				title : "新闻发布",
				width : 800,
				height : 400
			});
		}

		function detailRow1(pkId) {
			var row = grid.getSelected();
			_OpenWindow({
				url : "${ctx}/oa/portal/insNews/get.do??permit=yes&pkId=" + row.newId,
				title : row.subject,
				width : 800,
				height : 800
			//max:true,
			});
		}
	</script>
	<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
	<ibms:gridScript gridId="datagrid1" entityName="com.cssrc.ibms.index.model.InsNews" winHeight="550" winWidth="780" entityTitle="新闻公告" baseUrl="oa/portal/insNews" />
</body>
</html>