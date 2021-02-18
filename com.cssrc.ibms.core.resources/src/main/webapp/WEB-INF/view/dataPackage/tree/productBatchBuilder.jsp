<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>产品结构维度数据包主页面</title>
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
		
		var batchId = "<%=request.getParameter("batchId")%>";
		var batchKey = "<%=request.getParameter("batchKey")%>";
		var checkData;
		function openDefaultFrame() {
			// 产品-批次节点
	  		var acceptancePlanUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000021341344&batchId=" + batchId + "&batchName=" + batchKey+"&__dbomSql__=F_SSCPPC="+batchId;
            $("#acceptancePlan").attr("src", encodeURI(encodeURI(acceptancePlanUrl)));
		}
		
		// 表单下发校验--必须有已审批通过的验收策划
		function formAssignCheck(){
			$.ajax({ 
				url:"${ctx}/product/category/batch/formAssignCheck.do",
				data:{ 
					batchId:batchId,
					batchKey:batchKey
				},
				async:false,
				success:function(data){
					checkData = data;
				} 
			});
		}
		
		// 数据回传查看校验--验收策划通过、表单下发
		function dataReturnViewCheck(){
			$.ajax({ 
				url:"${ctx}/product/category/batch/dataReturnViewCheck.do",
				data:{ 
					batchId:batchId,
					batchKey:batchKey
				},
				async:false,
				success:function(data){
					checkData = data;
				} 
			});
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
				/*
				case "productTemplate":
					// 表单模板
		            var productTemplateUrl = "${ctx}/dp/form/manage.do?fcId=" + batchId + "&fcName=" + batchKey;
		            $("#productTemplate").attr("src", encodeURI(encodeURI(productTemplateUrl)));
					break;
				 */

				case "exportdata":
			         fileurl="${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000025850008&batchId=" + batchId + "&batchName=" + batchKey;
			         $("#exportdata").attr("src", encodeURI(encodeURI(fileurl)));
			         break;
				case "importDataHistory":
					fileurl="${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000032920832&__dbomSql__=F_ssxhhpc=" + batchId ;
					$("#importDataHistory").attr("src", encodeURI(encodeURI(fileurl)));
					break;
				case "file":
					// 归档
		           /*  $.ligerDialog.warn("暂未开放！"); */
		           fileurl="${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000022580139&batchId=" + batchId + "&batchName=" + batchKey;
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
				<li tabid="acceptancePlan" class="item1 layui-this">验收策划</li>
				<!-- 移至验收策划节点
				<li tabid="formAssign" class="item2">表单下发</li>
				 -->
				 <!-- 移至验收策划节点
				<li tabid="dataReturnView" class="item3">数据回传查看</li>
				 -->
				<!-- 移至验收策划节点
				<li tabid="acceptanceSummary" class="item4">验收总结</li>
				 -->
				<!-- 去除批次建模
				<li tabid="productTemplate" class="item5">表单模板</li>
				 -->
				<%--数据导入(历史)--%>
				<li tabid="importDataHistory" class="item2">数据导入</li>
				<%--数据导出--%>
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
            	<!-- 去除批次建模
            	<div class="layui-tab-item">
                	<iframe id="productTemplate" src="" frambuildereborder="no" width="100%" height="100%"></iframe>
            	</div>
            	 -->
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
