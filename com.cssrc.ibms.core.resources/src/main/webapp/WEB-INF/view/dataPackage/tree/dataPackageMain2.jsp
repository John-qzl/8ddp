<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>数据包主页面</title>
<%@include file="/commons/include/form.jsp" %>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<link rel="stylesheet" href="${ctx}/layui/css/layui.css">
<script type="text/javascript" src="${ctx}/layui/layui.js"></script>
<style type="text/css">
 	.tree-title{overflow:hidden;width:100%;} 
	html,body{ padding:0px; margin:0; width:100%;height:100%;overflow: hidden;}
	
	.w100{
		width:100%;height:100%;
	}
</style>
<script type="text/javascript">

		var catKey="${CAT_FORM}";		
		var mappingUrl = "/dataPackage/tree/ptree/";
		var id = "<%=request.getParameter("id")%>";
		var type = "<%=request.getParameter("type")%>";
		var parentName = "<%=request.getParameter("__dbomFKName__")%>";
		var currentName = "<%=request.getParameter("name")%>";
		var projectId = "<%=request.getParameter("projectId")%>";
		var projectName = "<%=request.getParameter("projectName")%>";
		
		$(function (){
			layui.use('element',function(){
				var element = layui.element;
				var content = $('.layui-tab-content');
				var _height = content.closest('.layui-tab').height() 
				- content.prev('.layui-tab-title').height() - 18;
				$('.layui-tab-content').height(_height)
				
			})
			var url="${ctx}"+ "/oa/form/dataTemplate/preview.do?__displayId__=10000000680207"
					+"&__dbomFKName__="+currentName
					+"&__dbomFKValue__="+id
					+"&projectId="+projectId
					+"&projectName="+projectName
					+"&__dbomSql__=F_SSSJB="+id;
			$("#dataPackage").attr("src",url);
		  	initTab(type);
		  	
		  	$('.layui-tab-title').on('click','li',function(){
				switch ($(this).attr("tabid")){
				case "dataPackage":					
					$("#dataPackage").attr("src",url);
					break;
				case "baseInfo":
					var currentUrl ="${ctx}"+"/dataPackage/tree/ptree/detailData.do?__displayId__=10000000410255&__pk__="+id
					+"&__dbomFKName__="+parentName;
					$("#baseInfo").attr("src",currentUrl);
					break;
				case "workTeam":
					var currentUrl = "${ctx}"+"/oa/form/dataTemplate/preview.do?__displayId__=10000000650031"
						+"&__dbomFKName__="+currentName
						+"&__dbomFKValue__="+id
						+"&__dbomSql__=F_SSSJB="+id;
					$("#workTeam").attr("src",currentUrl);
					break;
				
				case "workPlan":
					var currentUrl = "${ctx}/project/wbs/init.do?" 
						+"sjbName="+currentName//软件节点，长二次
						+"&sjbId="+id//所属数据包
						+"&projectId="+projectId//发次Id
						+"&projectName="+projectName;//发次名称
					$("#workPlan").attr("src",currentUrl);
					break;
				}
			});
			

		  	
		});
		
		//根据节点类型展示tab页
		function initTab(type){
			if("普通分类节点" == type){
				$("li[tabid='dataPackage']").hide();
				$("div[tabid='dataPackage']").hide();
				$("li[tabid='workTeam']").hide();
				$("div[tabid='workTeam']").hide();
				$("li[tabid='workPlan']").hide();
				$("div[tabid='workPlan']").hide();
			}
			if("软件节点" == type||"单机节点" == type){
				$("li[tabid='workPlan']").hide();
				$("div[tabid='workPlan']").hide();
			}
		}
		
	</script> 
</head>
<body>
	<div style="width: 100%; top: 0px; left: 0px; height: 100%;">
		<div class="layui-tab layui-tab-brief template1">
			<ul class="layui-tab-title">
				<li tabid="baseInfo" >基本属性</li>
				<li tabid="dataPackage" class="layui-this">数据包</li>
				<li tabid="workTeam">工作队</li>
				<li tabid="workPlan">工作规划</li>
			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item">
					<div class="l-tab-loading" style="display: none;"></div>
					<iframe class="w100" name="baseInfo" id="baseInfo" frameborder="0" src=""></iframe>
				</div>
				<div class="layui-tab-item layui-show">
					<div class="l-tab-loading" style="display: none;"></div>
					<iframe class="w100" name="dataPackage" id="dataPackage" frameborder="0" src=""></iframe>
				</div>
				<div class="layui-tab-item">
					<div class="l-tab-loading" style="display: none;"></div>
					<iframe class="w100" name="workTeam" id="workTeam"  frameborder="0" src=""></iframe>
				</div>
				<div class="layui-tab-item">
					<div class="l-tab-loading" style="display: none;"></div>
					<iframe class="w100" name="workPlan" id="workPlan"  frameborder="0" src=""></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
