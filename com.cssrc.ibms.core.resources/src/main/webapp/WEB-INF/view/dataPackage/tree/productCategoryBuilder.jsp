<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>产品结构维度--产品类别页面</title>
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
	.layui-tab-brief > .layui-tab-title .layui-this{
		color: #347EFE;
		font-weight: bold;
	}
	.layui-tab-brief > .layui-tab-more li.layui-this:after, .layui-tab-brief > .layui-tab-title .layui-this:after {
		border-color: #347EFE;
	}
</style>
<script type="text/javascript">
		
		var categoryId = "<%=request.getParameter("categoryId")%>";
		var categoryKey = "<%=request.getParameter("categoryKey")%>";
		
		function openDefaultFrame() {
			// 产品-类别（屏）节点
			var iframe = '<iframe class="w100" name="baseInfo" id="baseInfo" frameborder="0" style="height:126px" src=""></iframe>';
		//	iframe=iframe+'<iframe class="w100" name="allPlan" id="allPlan" frameborder="0" style="height:540px" src=""></iframe>';

			$(".layui-tab-item.layui-show.baseInfo").append(iframe);
			var url="${ctx}"+"/oa/form/dataTemplate/detailData.do?__displayId__=10000021190185&__pk__="+categoryId;
			$("#baseInfo").attr("src",url);
/*
			//获取所有的displayId
			var moduleDisplayId;	//型号的displayid
			var rangeTestPlanDisplayId;	//靶场策划的displayID

				$.ajax({
					url:"${ctx}"+"/oa/form/dataTemplate/getDisplayIdByFormAliases.do",
					data:{'allFormAliases':"xhgllb,bcsychbgb"},
					dataType:"json",
					async:false,
					success:function(result){
						console.log("请求成功");
						console.log(result);
						moduleDisplayId=result[0].xhgllb;
						rangeTestPlanDisplayId=result[1].bcsychbgb;
					},
					error:function () {
						console.log("ajax请求失败");
					}
				})

*/
	  		var allPlanUrl="${ctx}"+"/oa/form/dataTemplate/preview.do?__displayId__=10000021200168&__dbomSql__=F_SSCPLB="+categoryId;
			$("#allPlan").attr("src",allPlanUrl);
		}
		
		$(function (){
			layui.use('element',function(){
				var element = layui.element;
				var content = $('.layui-tab-content');
				var _height = content.closest('.layui-tab').height() 
				- content.prev('.layui-tab-title').height() - 18;
				$('.layui-tab-content').height(_height)
				
			})
			// 设置默认打开的第一个页面
			openDefaultFrame();
		  	
		  	$('.layui-tab-title').on('click','li',function(){
				switch ($(this).attr("tabid")){
				case "baseInfo":
					var currentUrl="${ctx}"+"/oa/form/dataTemplate/detailData.do?__displayId__=10000021190185&__pk__="+categoryId;
					$("#baseInfo").attr("src",currentUrl);
					break;
				case "productTemplate":
					// 表单模板
		            var productTemplateUrl = "${ctx}/dp/form/manage.do?fcId=" + categoryId + "&fcName=" + categoryKey;
		            $("#productTemplate").attr("src", encodeURI(encodeURI(productTemplateUrl)));
					break;
				case "statistic":
					// 产品统计--批次、策划、产品数据
		            $.ligerDialog.warn("暂未开放！");
					break;
				}
			});
		});
		
	</script> 
</head>
<body>
	<div style="width: 100%; top: 0px; left: 0px; height: 100%;">
		<div class="layui-tab layui-tab-brief template1">
			<ul class="layui-tab-title">
				<li tabid="baseInfo" class="layui-this">基本属性</li>
				<li tabid="productTemplate" class="item2">表单模板</li>
				<%--<li tabid="statistic" class="item3">统计</li>--%>
			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item layui-show baseInfo" style="overflow: auto;">
					<div class="l-tab-loading" style="display: none;"></div>
				</div>
            	<div class="layui-tab-item">
                	<iframe id="productTemplate" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
            	<%--<div class="layui-tab-item">
                	<iframe id="statistic" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>--%>
			</div>
		</div>
	</div>
</body>
</html>
