<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/commons/include/html_doctype.html"%>

<html>
<head>
<title>office报表数据源</title>

<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/ibms/oa/system/office/DataSourceTree.js"></script>

<script type="text/javascript">
	var dataSource = new DataSource("datatree","","getDataSource.do", {
		expandAll : false,
		checkedData:"${tableId}"
	});

	$(function() {
		dataSource.loadTree();
	});
	
	
	function selectTables(){
		var tabs=new Array();
		var nodes=dataSource.dsTree.getCheckedNodes();
		var i=0;
		var tabIds="";
		var tabNames="";
		$(nodes).each(function(j,node){
			if(node.ID!="table"&&node.ID!="view"&&node.ID!="schema"){
				tabs[i]=node;
				tabIds+=node.ID+",";
				tabNames+=node.NAME_+",";
				i++;
			}
		})
		if(tabIds!=null&&tabIds!=""){
			tabIds=tabIds.substring(0,tabIds.length-1);
			tabNames=tabNames.substring(0,tabNames.length-1);
		}
		var obj=new Object();
		obj.tabs=tabs;
		obj.tabIds=tabIds;
		obj.tabNames=tabNames;
		window.returnValue=obj;
		window.close();
	}
</script>
</head>


<body style="overflow:auto">
	
	<div position="bottom" class="bottom" style="margin-top: 10px;">
		<a href="javascript:;" class="button" onclick="selectTables()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>确认</span></a>
		<a href="javascript:;"
			class="button" style="margin-left: 10px;" onclick="window.close()"><span
			class="icon cancel"></span><span>取消</span></a>
	</div>
	<br>
	<div class="tree-toolbar" id="pToolbar">
		<div class="toolBar"
			style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
			<div class="group">
				<a class="link reload" id="treeReFresh"
					href="javascript:dataSource.loadTree()">刷新</a>
			</div>
			
			<div class="group">
				<a class="link expand" id="treeExpand"
					onclick="javascript:dataSource.treeExpandAll(true)">展开</a>
			</div>
			
			<div class="group">
				<a class="link collapse" id="treeCollapse"
					onclick="javascript:dataSource.treeExpandAll(false)">收起</a>
			</div>
		</div>
	</div>
	<ul id="datatree" class="ztree"
		style="width: 100%; margin: -2; padding: -2;">
	</ul>
	<br>
	<div position="bottom" class="bottom" style="margin-top: 10px;">
		<a href="javascript:;" class="button" onclick="selectTables()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>确认</span></a>
		<a href="javascript:;"
			class="button" style="margin-left: 10px;" onclick="window.close()"><span
			class="icon cancel"></span><span>取消</span></a>
	</div>


</body>

</html>