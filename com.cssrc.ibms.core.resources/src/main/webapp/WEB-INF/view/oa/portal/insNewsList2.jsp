<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻公告列表管理</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="mini-fit" style="height: 100px;">
		<div id="datagrid1" class="mini-datagrid"
			style="width: 100%; height: 100%; padding:100px;" allowResize="false" showVGridLines="false" showHGridLines="false"
			 idField="newId" ondrawcell="onDrawCell"
			multiSelect="true" showColumnsMenu="true" showPager="false"
			sizeList="[5,50,50]" showColumns="false"
			allowAlternating="true" pagerButtons="#pagerButtons">
			<div property="columns" >
				<div name="action" cellCls="actionIcons" width="15" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">#</div>
				<div field="subject" width="180" headerAlign="center"
					allowSort="true">标题</div>
				<div field="createTime"  headerAlign="center" allowSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"></div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		//编辑
		mini.parse();
		var colId = ${param['colId']};
		var pageSize = ${param['pageSize']};
		var url='${ctx}/oa/portal/insNews/listByColId.do?joinAttName=insNews&colId='+colId+'&pageSize='+pageSize;//显示list的url
		mini.get("datagrid1").setUrl(url);
				
		//在最前面加一个图标
		function onActionRenderer(e) {
			var s = '<span class="icon-detail"></span>';
			return s;
		}
		
		//将新闻名字超链接化
		function onDrawCell(e) {
            var record = e.record;
            if (e.field == "subject") {
                var sub = record.subject;
                e.cellStyle = "text-align:left";
                e.cellHtml = '<a href="javascript:detailRow1(\'' + record.id + '\')">' + sub +'</a>';
            }
        }
		
		//点击新闻超链接弹出新网页显示内容
		
		function detailRow1(pkId) {
			var row = grid.getSelected();
			_SubmitJson({
				url : "${ctx}/oa/portal/insNews/addReadTimes.do",
				showMsg : false,
				data : {
					pkId : row.pkId
				},
				method : "POST",
				success : function() {
				}
			});
			_OpenWindow({
				url : "${ctx}/oa/portal/insNews/get.do?permit=no&pkId=" + row.pkId,
				title : row.subject,
				width : 800,
				height : 800,
			//max:true,
			});
		}
	</script>
	<style type="text/css">
		a:link,a:visited{
		text-decoration:none;  /*超链接无下划线*/
		}
	</style>
	<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
	<ibms:gridScript gridId="datagrid1"
		entityName="com.cssrc.ibms.index.model.InsNews" winHeight="550"
		winWidth="780" entityTitle="新闻公告" baseUrl="oa/portal/insNews" />
</body>
</html>