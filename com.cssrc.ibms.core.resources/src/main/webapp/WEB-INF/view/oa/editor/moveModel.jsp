<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>表单模板管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/moveModelTree.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/FormTableDefMenu.js"></script>
<style type="text/css">
 	.tree-title{overflow:hidden;width:100%;}
	html,body{ padding:0px; margin:0; width:100%;height:100%;overflow: hidden;}
	#defLayout .btns{
		text-align: center;
	}
	#defLayout .btns{
		position: absolute;
		bottom:20px;
		width:100%;
	}
</style>
<script type="text/javascript">
		//表单模板页面
		//
		var dialog = window;// 调用页面的dialog对象(ligerui对象)
		if (frameElement) {
			dialog = frameElement.dialog;
		}
		var catKey="${CAT_FORM}";
		var curMenu=null;
		var formDefMenu=new FormTableDefMenu();
		var mappingUrl = "/dp/form/";
		var id = "${fcId}";
		<%-- var pid = "<%=request.getParameter("pid")%>"; --%>
		var nodeName = "${fcName}";
		var pTree=new GlobalPackageType(catKey,"pTree",{onClick:onClick},mappingUrl,id,nodeName);
		$(function (){
			//ligerui Tab
			layui.use('element',function(){
				var element = layui.element;
			})
		  	//布局
		    loadLayout();
		  	//加载树
			pTree.loadGlobalTree();
			
		});

		//布局
		function loadLayout(){
			$("#defLayout").ligerLayout( {
				leftWidth : 200,
				onHeightChanged: heightChanged,
				allowLeftResize:false
			});
			//取得layout的高度
		    var height = $(".l-layout-center").height();
		    $("#pTree").height(height-90);
		}
		//布局大小改变的时候通知tab，面板改变大小
		function heightChanged(options){
		 	$("#pTree").height(options.middleHeight -90);
		}
		
		function onClick(event, treeId, treeNode){
			
		}
		
	function move(){
		var node=pTree.currentNode;
		if(node!=null){
				url="${ctx}/dp/form/moveModel.do?nodeid="+${nodeid}+"&pid="+node.id;
				$.post(url,function(data){
	    			if(data.success=="true"){
	    				$.ligerDialog.confirm('模板移动成功，是否关闭该页面',function (rtn){
							if(rtn){
								$(window.parent.document).find('a.link.reload').trigger('click');
								dialog.close();
							}
						});
	    			}else{
	    				$.ligerDialog.error(data.msg,"移动失败");
	    			}
	    		});	
			return;
	}
	$.ligerDialog.error("请选择移动模板的目标文件夹","提示");
	}
	function cancel(){
		dialog.close();
	}
	</script>
</head>
<body>
		<div id="defLayout" >
			<div id="pTree" class="ztree" style="overflow:auto;clear:left" ></div>
			<div class='btns'>
				<input type="button" value="移动" onClick="move()"/ class="layui-btn layui-btn-normal layui-btn-sm">
				<input type="button" value="取消" onClick="cancel()" class="layui-btn layui-btn-normal layui-btn-sm"/>
			</div>
		</div>
</body>
</html>
