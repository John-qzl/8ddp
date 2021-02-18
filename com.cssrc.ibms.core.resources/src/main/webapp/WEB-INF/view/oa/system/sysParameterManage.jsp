 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>系统参数管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>

<script type="text/javascript">
	
	function onClick(event,treeId,treeNode){
		var type = treeNode.type;
		
		var url=__ctx+"/oa/system/sysParameter/list.do?type="+type;
		
		$("#defFrame").attr("src",url);
	}	

	$(function (){
		//布局
	    $("#defLayout").ligerLayout({ leftWidth:210,height: '100%',allowLeftResize:true});
	  	
		var zNodes = JSON.parse('${types}');
		var setting = {
			data: {
				key : { name: "type"},
				simpleData: {enable: true,idKey: "type",pIdKey: "0"}
			},
			showLine: true,
			callback:{onClick:onClick}
		};
		
		//初始化ztree	
		$.fn.zTree.init($("#sysParameterTypeTree"), setting,zNodes);
		toolTree = $.fn.zTree.getZTreeObj("sysParameterTypeTree");
		var treeNodes = toolTree.transformToArray(toolTree.getNodes());
		for (var i = 0; i < treeNodes.length; i++) {
			var element = treeNodes[i];
			if (element.isLeaf==0) {
				element.icon = 'folder';
			} else if(element.level==0){
				element.icon = 'closefolder';
			} else{
				element.icon = 'expandfolder';
			}
			toolTree.updateNode(element);
		}
		
		
		
		
		
	});
	
	function loadGlobalTree(zNodes){
		
	    var setting = {
				data: {
					key : { name: "type"},
					simpleData: {enable: true,idKey: "type",pIdKey: "0"}
				},
				showLine: true,
				callback:{onClick:onClick}
		};
		
		//清空原有的
		$("#sysParameterTypeTree").empty();
		//初始化ztree	
		$.fn.zTree.init($("#sysParameterTypeTree"), setting,zNodes);
	}
	        
</script> 
</head>
<body>
   	<div id="defLayout">
         <div position="left" title="系统参数分类管理" style="overflow: auto;float:left;width:100%">
			<ul id="sysParameterTypeTree" class="ztree"></ul>
         </div>
         <div position="center">
        	<iframe id="defFrame" height="100%" width="100%" frameborder="0" src="${ctx}/oa/system/sysParameter/list.do"></iframe>
         </div>
     </div>
</body>
</html>
