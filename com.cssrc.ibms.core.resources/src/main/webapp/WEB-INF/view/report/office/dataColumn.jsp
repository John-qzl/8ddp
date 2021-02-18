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
	var dataSource = new DataSource("datatree","tableName=${tableName}","getDataColumns.do",{
		expandAll : false
	});

	$(function() {
		dataSource.loadTree();
	});
	
	//生成书签的Id作为书签名，且作为数据库中的书签ID
	function getBooMarkId() {
		var now=new Date();
		return "PO_"+now.getTime()+randomstr(6);
	}
	//获取随机数
	function randomstr(L){
		var s="";
		var random=function(){
			var n=Math.floor(Math.random()*62);
			if(n<10)return n;
			if(n<36)return String.fromCharCode(n+55);
			return String.fromCharCode(n+61);
		}
		while(s.length<L)s+=random();
		return s;
	}
	function Ok(){		
		var nodes = dataSource.dsTree.getCheckedNodes();
		if(nodes.length==0){
			$.ligerDialog.warn("请选择要定位的字段！");
			return;
		}
		if(nodes.length!=1){
			$.ligerDialog.warn("一次只可定位一个标签，请重新选择！");
			return;
		}
		var cnode=nodes[0];
		//在word 文档中添加书签
		var booMarkId=getBooMarkId();
		var content="[" + cnode.getParentNode().NAME_+"."+cnode.NAME_ + "]";
		var result=window.external.CallParentFunc("addDocMark",booMarkId+"="+ content);
		//在form 表单中添加书签
		if(result){
			var mark="";
			mark+=booMarkId+";";
			mark+=cnode.getParentNode().NAME_+";";
			mark+=cnode.NAME_+";";
			mark+=cnode.ID+";";
			mark+="col";
			window.external.CallParentFunc("addFormMark",mark);
			window.close();	
		}else{
			alert("添加书签失败");
		}
    	
	}
	
	function tabOK(){		
		var nodes = dataSource.dsTree.getCheckedNodes();		
		if(nodes.length==0){
			$.ligerDialog.warn("请选择要在表格中显示的字段！");
			return;
		}
		var tabid="";
		var colIds="";
		var colNames="";
		var tabName=""
		$(nodes).each(function(i,node){
			if(tabid!="" && node.PID!=tabid){
				$.ligerDialog.warn("请在同一个数据类或者数据视图中选择表字段！");
				return;
			}
			colIds+=node.ID+",";
			colNames+=node.NAME_+","
			tabid = node.PID;
			tabName=node.getParentNode().NAME_;
		})
		if(colIds!=""){
			colIds=colIds.substring(0,colIds.length-1);
			colNames=colNames.substring(0,colNames.length-1);
		}
		//在word 文档中添加书签
		var booMarkId=getBooMarkId();
		var content="[" + tabName + "]";
		var result=window.external.CallParentFunc("addDocMark",booMarkId+"="+ content);
		//在form 表单中添加书签
		if(result){
			var mark="";
			mark+=booMarkId+";";
			mark+=tabName+";";
			mark+=colNames+";";
			mark+=colIds+";";
			mark+="tab";
			window.external.CallParentFunc("addFormMark",mark);
			window.close();	
		}else{
			alert("添加书签失败");
		}
	}
	
	function cellDown(){		
		var nodes = dataSource.dsTree.getCheckedNodes();		
		if(nodes.length==0){
			$.ligerDialog.warn("请选择要定位的字段！");
			return;
		}
		if(nodes.length!=1){
			$.ligerDialog.warn("一次只可定位一个标签，请重新选择！");
			return;
		}
		var cnode=nodes[0];
		//在word 文档中添加书签
		var booMarkId=getBooMarkId();
		var content="[" + cnode.getParentNode().NAME_+"."+cnode.NAME_ + "]";
		var result=window.external.CallParentFunc("addDocMark",booMarkId+"="+ content);
		//在form 表单中添加书签
		if(result){
			var mark="";
			mark+=booMarkId+";";
			mark+=cnode.getParentNode().NAME_+";";
			mark+=cnode.NAME_+";";
			mark+=cnode.ID+";";
			mark+="cell";
			window.external.CallParentFunc("addFormMark",mark);
			window.close();	
		}else{
			alert("添加书签失败");
		}
	}
	
</script>

</head>


<body>
	
	<div position="bottom" class="bottom" style="margin-top: 10px;">
		<a href="javascript:;" class="button" onclick="Ok()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>定  位</span></a>
		<a href="javascript:;" class="button" onclick="tabOK()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>表格定位 </span></a>
		<a href="javascript:;" class="button" onclick="cellDown()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>循  环</span></a>
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
		<a href="javascript:;" class="button" onclick="Ok()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>定  位</span></a>
		<a href="javascript:;" class="button" onclick="tabOK()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>表格定位 </span></a>
		<a href="javascript:;" class="button" onclick="cellDown()"
			style="margin-right: 10px;"><span class="icon ok"></span><span>循  环</span></a>
		<a href="javascript:;"
			class="button" style="margin-left: 10px;" onclick="window.close()"><span
			class="icon cancel"></span><span>取消</span></a>
	</div>


</body>

</html>