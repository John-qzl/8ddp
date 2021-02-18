<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html>
    <head>
		<%@include file="/commons/include/get.jsp" %>
        <title>新建流程</title>
		<f:link href="tree/zTreeStyle.css"></f:link>
		<f:gtype name="CAT_FLOW" alias="CAT_FLOW"></f:gtype>
		<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>	
        <script type="text/javascript">
        		var catKey="${CAT_FLOW}";
        		var globalType=new GlobalType(catKey,"glTypeTree",{onClick:onClick,url:'${ctx}/oa/system/globalType/getByCatKeyForNewPro.do',expandByDepth:1});
                $(function (){
                	//布局
                    $("#defLayout").ligerLayout({ leftWidth:210,height: '100%',allowLeftResize:false});
                	//加载菜单 
                    globalType.loadGlobalTree();
                	
                });
              	//左击
            	function onClick(treeNode){
                
	            		var url="${ctx}/oa/flow/definition/myList.do?typeId="+treeNode.typeId;
	            		
	            		$("#defFrame").attr("src",url);
            		
            	};
            	
            	//展开收起
            	function treeExpandAll(type){
            		globalType.treeExpandAll(type);
            	};
         </script> 
    </head>
    <body>
      	<div id="defLayout" >
            <div position="left" title="流程分类" style="overflow: auto;float:left;width:100%;height:96%;">
            	<div class="tree-toolbar">
					<span class="toolBar">
						<div class="group"><a class="link reload" id="treeFresh" href="javascript:globalType.loadGlobalTree();" title="刷新"></a></div>
						
						<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" title="展开"></a></div>
						
						<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)"  title="收起"></a></div>
					</span>
				</div>
				<ul id="glTypeTree" class="ztree" ></ul>
            </div>
            <div position="center" title="新建流程">
            	<iframe id="defFrame" height="100%" width="100%" frameborder="0" src="${ctx}/oa/flow/definition/myList.do"></iframe>
            </div>
        </div>
    </body>
</html>
