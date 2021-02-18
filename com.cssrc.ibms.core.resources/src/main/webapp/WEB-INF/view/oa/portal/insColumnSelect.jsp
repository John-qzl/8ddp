<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>栏目列表选择</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="mini-toolbar" style="text-align: center; line-height: 30px;" borderStyle="border:0;">
		<label>名称：</label> <input id="key" class="mini-textbox" style="width: 150px;" onenter="onKeyEnter" /> 
		<a class="mini-button" onclick="search1()">查询</a>
	</div>
	<div class="mini-fit" style="height: 100px;">
		<div id="datagrid1" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="false" url="${ctxPath}/oa/portal/insColumn/listData.do" idField="colId" multiSelect="true" showColumnsMenu="true" sizeList="[5,10,20,50,100,200,500]" pageSize="20" allowAlternating="true" pagerButtons="#pagerButtons">
			<div property="columns">

				<div type="checkcolumn" width="20"></div>
				<div field="name" width="160" headerAlign="center" allowSort="true">栏目名称</div>
				<div field="key" width="120" headerAlign="center" allowSort="true">栏目Key</div>
				<div field="enabled" width="60" headerAlign="center" allowSort="true">是否启用</div>
				<div field="colType" width="100" headerAlign="center" allowSort="true">信息栏目类型</div>
			</div>
		</div>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 8px; padding-bottom: 8px;" borderStyle="border:0;">
		<a class="mini-button" onclick="onOk()">确定</a> <span style="display: inline-block; width: 25px;"></span> <a class="mini-button" onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
			mini.parse();
		    var grid = mini.get("datagrid1");
			//栏目查询
			function search1() {
		        var key = mini.get("key").getValue();
		        grid.setUrl(__ctx + '/oa/portal/insColumn/searchByName.do');
		        grid.load({key:key});
		    }
			
			function GetData() {
	       		var row = grid.getSelecteds();
	        	return row;
	    	}
			//关闭页面时返回action
			function CloseWindow(action) {
				if (window.CloseOwnerWindow)
					return window.CloseOwnerWindow(action);
				else
					window.close();
			}
	
			function onOk() {
				CloseWindow("ok");
			}
			function onCancel() {
				CloseWindow("cancel");
			}
		</script>
	<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
	<ibms:gridScript gridId="datagrid1" entityName="com.cssrc.ibms.index.model.InsColumn" winHeight="390" winWidth="700" entityTitle="栏目" baseUrl="oa/portal/insColumn" />
</body>
</html>