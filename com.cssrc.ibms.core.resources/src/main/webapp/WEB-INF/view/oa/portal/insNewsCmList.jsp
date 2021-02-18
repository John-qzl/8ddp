<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻评论列表管理</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
</head>
<body>

	<ibms:toolbar entityName="com.cssrc.ibms.index.model.InsNewsCm" excludeButtons="popupAddMenu,edit,popupSearchMenu,popupAttachMenu,popupSettingMenu"/>
	<ibms:pager pagerId="pagerButtons" entityName="com.cssrc.ibms.index.model.InsNewsCm" />

	<div class="mini-fit" style="height: 100px;">
		<div id="datagrid1" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="false" url="${ctx}/oa/portal/insNewsCm/listData.do" idField="commId" multiSelect="true" showColumnsMenu="true" sizeList="[5,10,20,50,100,200,500]" pageSize="20" allowAlternating="true" pagerButtons="#pagerButtons">
			<div property="columns">
				<div type="checkcolumn" width="20"></div>
				<div name="action" cellCls="actionIcons" width="50" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">#</div>
				<div field="newsTitle" width="120" headerAlign="center" allowSort="true">新闻题目</div>
				<div field="fullName" width="120" headerAlign="center" allowSort="true">评论人名</div>
				<div field="content" width="120" headerAlign="center" allowSort="true">评论内容</div>
				<div field="agreeNums" width="120" headerAlign="center" allowSort="true">赞同与顶</div>
				<div field="refuseNums" width="120" headerAlign="center" allowSort="true">反对与鄙视次数</div>
				<div field="isReply" width="120" headerAlign="center" allowSort="true">是否为回复</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
        	//编辑
	        function onActionRenderer(e) {
	            var record = e.record;
	            var uid = record.pkId;
	            var s = '<span class="icon-detail" title="明细" onclick="detailRow(\'' + uid + '\')"></span>'
	          //          + ' <span class="icon-edit" title="编辑" onclick="editRow(\'' + uid + '\')"></span>'
	                    + ' <span class="icon-remove" title="删除" onclick="delRow(\'' + uid + '\')"></span>';
	            return s;
	        }
        </script>
	<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
	<ibms:gridScript gridId="datagrid1" entityName="com.cssrc.ibms.index.model.InsNewsCm" winHeight="450" winWidth="700" entityTitle="评论" baseUrl="oa/portal/insNewsCm" />
</body>
</html>