<%@page pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html"%>
<html>
	<head>
		<title>选择流程表单</title>
		<%@include file="/commons/include/form.jsp" %>
		<f:link href="tree/zTreeStyle.css"></f:link>
		<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
		<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js" ></script>
	    <script type="text/javascript"	src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	    <script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>
	    <script type="text/javascript">
	    	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	   		var catKey="${CAT_FORM}";
			var globalType=new GlobalType(catKey,"glTypeTree",{onClick:treeClick,expandByDepth:2});
			
			$(function(){
				$("#defLayout").ligerLayout({ leftWidth: 200,height: '90%',
						bottomHeight :40,
					 	allowBottomResize:false});
			
				globalType.loadGlobalTree();
			});
			
			//展开收起
			function treeExpandAll(type){
				globalType.treeExpandAll(type);
			};
			
			function treeClick( treeNode){
				if(treeNode.isRoot==undefined){
					var categroyId=treeNode.typeId;
				
					var url="selector.do?categoryId="+categroyId;
	        		$("#formFrame").attr("src",url);
				}
        	}
			
			function selectForm(){
				var formName="";
				var formKey="";
				var tableId="";
				$('#formFrame').contents().find(":radio[name='formKey']:checked").each(function(){
					formName=$(this).siblings("input[name='subject']").val();
					formKey=$(this).val();
					tableId=$(this).siblings("input[name='tableId']").val();
				});
				if(formKey==""){
					$.ligerDialog.warn("请选择表单ID!","提示信息");
					return "";
				}
				
				dialog.get('sucCall')({formKey:formKey,formName:formName,tableId:tableId});
				dialog.close();
			}
			
		</script>
<f:link href="from-jsp.css"></f:link>	</head>
	<body>
			<div id="defLayout" >
	            <div position="left" title="表单分类" style="overflow: auto;float:left;width:103%;height:94%;">
					<ul id="glTypeTree" class="ztree"></ul>
	            </div>
	            <div position="center"  title="表单">
	          		<iframe id="formFrame" name="formFrame" height="100%" width="100%" frameborder="0"  src="selector.do"></iframe>
	            </div>  
       	  </div>
       	 <div position="bottom"  class="bottom" style='margin-top:10px;'>
			  <a href='####' class='button'  onclick="selectForm()" ><span class="icon ok"></span><span >选择</span></a>
			  <a href='####' class='button' style='margin-left:10px;' onclick="dialog.close()"><span class="icon cancel"></span><span >取消</span></a>
		</div>
	
	</body>
</html>