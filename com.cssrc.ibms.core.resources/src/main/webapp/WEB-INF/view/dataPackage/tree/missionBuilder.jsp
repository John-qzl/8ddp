<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>任务维度数据包主页面</title>
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
		
		var missionId = "<%=request.getParameter("missionId")%>";
		var moduleId = "<%=request.getParameter("moduleId")%>";
		var moduleCode = "<%=request.getParameter("moduleCode")%>";
		var checkData;
		function openDefaultFrame() {
			// 顶层文件夹节点
	  		var acceptancePlanUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000028760024&missionId=" + missionId + "&moduleId="+ moduleId+"&moduleCode="+moduleCode+ "&__dbomSql__=F_XHID="+moduleId;
            $("#acceptancePlan").attr("src", encodeURI(encodeURI(acceptancePlanUrl)));
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
				/* 
				case "baseInfo":
					var currentUrl="${ctx}"+"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200168&__pk__="+batchId;
					$("#baseInfo").attr("src",currentUrl);
					break;
				 */
				case "acceptancePlan":
					// 验收策划
		            var acceptancePlanUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000021341344&batchId=" + batchId + "&batchName=" + batchKey+"&__dbomSql__=F_SSCPPC="+batchId;
		            $("#acceptancePlan").attr("src", encodeURI(encodeURI(acceptancePlanUrl)));
					break;
					/* 表单下发 + 数据回传查看*/
				case "formAssign":
					// 表单下发
					formAssignCheck();
					if(checkData.success){
						var formAssignUrl = "${ctx}/product/batch/productBatchForm/assign.do?batchId=" + batchId + "&batchKey=" + batchKey;
			            $("#formAssign").attr("src", encodeURI(encodeURI(formAssignUrl)));
						break;
					}else{
						$.ligerDialog.warn(checkData.msg);
						break;
					}
				case "dataReturnView":
					// 数据回传查看
					dataReturnViewCheck();
					if(checkData.success){
						var dataReturnViewUrl = "${ctx}/product/batch/productBatchDataReturn/view.do?batchId=" + batchId + "&batchKey=" + batchKey;
			            $("#dataReturnView").attr("src", encodeURI(encodeURI(dataReturnViewUrl)));
						break;
					}else{
						$.ligerDialog.warn(checkData.msg);
						break;
					}
				case "acceptanceSummary":
					// 验收总结
		            var acceptanceSummaryUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000021410561&batchId=" + batchId + "&batchName=" + batchKey;
		            $("#acceptanceSummary").attr("src", encodeURI(encodeURI(acceptanceSummaryUrl)));
					break;

				case "productTemplate":
					// 表单模板 by zmz 20200824
		            var productTemplateUrl = "${ctx}/dp/missionFormManage.do?fcId=" + moduleId + "&fcName=全部任务&type=BCSY";
		            debugger;
		            $("#productTemplate").attr("src", encodeURI(encodeURI(productTemplateUrl)));
					break;
				case "importDataHistory":
					//数据导出
					fileurl="${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000032960087&__dbomSql__=F_ssxhhpc="+ moduleId;
					$("#importDataHistory").attr("src", encodeURI(encodeURI(fileurl)));
					break;


				case "exportdata":
					//数据导出
			         fileurl="${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029710108&xhId="+ moduleId;
			         $("#exportdata").attr("src", encodeURI(encodeURI(fileurl)));
			         break;
						//20200916  把归档调到了策划一级 by zmz
			case "file":
					// 归档
		           fileurl="${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029710069&xhId=" + moduleId;
		           $("#file").attr("src", encodeURI(encodeURI(fileurl)));
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
				<!-- 
				<li tabid="baseInfo" class="layui-this">基本属性</li>
				 -->
				<li tabid="acceptancePlan" class="item1 layui-this">任务策划</li>
				<!-- 移至验收策划节点
				<li tabid="formAssign" class="item2">表单下发</li>
				 -->
				 <!-- 移至验收策划节点
				<li tabid="dataReturnView" class="item3">数据回传查看</li>
				 -->
				<!-- 移至验收策划节点
				<li tabid="acceptanceSummary" class="item4">验收总结</li>
				 -->
				<!-- 表单模板显示在任务的根文件夹下 by zmz 20200824 -->
				<li tabid="productTemplate" class="item5">表单模板</li>
				<li tabid="importDataHistory" class="item2">数据导入</li>
				  <li tabid="exportdata" class="item2">数据导出</li>
				 <li tabid="file" class="item3">归档</li>
			</ul>
			<div class="layui-tab-content">
				<!-- 
				<div class="layui-tab-item layui-show baseInfo" style="overflow: auto;">
					<div class="l-tab-loading" style="display: none;"></div>
				</div>
				 -->
				
            	<div class="layui-tab-item layui-show">
                	<iframe id="acceptancePlan" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
            	<!--
            	<div class="layui-tab-item">
                	<iframe id="formAssign" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
            	 -->
            	<!-- 
            	<div class="layui-tab-item">
                	<iframe id="dataReturnView" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
            	 -->
            	<!-- 
            	<div class="layui-tab-item">
                	<iframe id="acceptanceSummary" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
            	 -->

            	<div class="layui-tab-item">
                	<iframe id="productTemplate" src="" style="border:medium none" width="100%" height="100%"></iframe>
            	</div>
				<div class="layui-tab-item">
					<iframe id="importDataHistory" src="" frameborder="no" width="100%" height="100%"></iframe>
				</div>
            	 <div class="layui-tab-item">
                	<iframe id="exportdata" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
            	 <div class="layui-tab-item">
                	<iframe id="file" src="" frameborder="no" width="100%" height="100%"></iframe>
            	</div>
			</div>
		</div>
	</div>
</body>
</html>
