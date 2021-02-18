<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html>
    <head>
		<%@include file="/commons/include/get.jsp" %>
        <title>待办任务</title>
		<f:link href="tree/zTreeStyle.css"></f:link>
		<f:gtype name="CAT_FLOW" alias="CAT_FLOW"></f:gtype>
<f:link href="from-jsp.css"></f:link>
		<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>
        <script type="text/javascript">
    		var catKey="${CAT_FLOW}";
     		var glTypeTreeUrl = __ctx+'/oa/system/globalType/forPending.do';
     		function getClickUrl(treeNode){
     			if($.isEmpty(treeNode.parentId) || treeNode.parentId == 0 ){
     				return "${ctx}/oa/flow/task/pendingMattersList.do"
     			}else if(treeNode.isLeaf==1){
     				return "${ctx}/oa/flow/userTask/pendingMattersList.do?defId="+treeNode.typeId;
     			}else{
     				return "${ctx}/oa/flow/task/pendingMattersList.do?nodePath="+treeNode.nodePath;
     			}
     		}
         </script> 
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalTypeByFlow.js"></script>
	
    </head>
    <body>
      	<div id="defLayout" >
            <div position="left" title="流程分类" style="overflow: auto;float:left;width:100%;">
            	<div class="tree-toolbar">
					<span class="toolBar">
						<div class="group"><a class="link reload" id="treeFresh" href="javascript:globalType.loadGlobalTree();"  title="刷新"></a></div>
						
						<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)"  title="展开"></a></div>
						
						<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)"  title="收起"></a></div>
					</span>
				</div>
				<ul id="glTypeTree" class="ztree"></ul>
            </div>
            <div position="center">
          		<iframe id="defFrame" height="100%" width="100%" frameborder="0" src="${ctx}/oa/flow/task/pendingMatters.do"></iframe>
            </div>
        </div>
    </body>
</html>
