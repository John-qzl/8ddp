<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>数据查看</title>
<%@include file="/commons/include/form.jsp" %>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<%-- <f:link href="tree/zTreeStyle.css"></f:link> --%>
<link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${ctx}/styles/default/css/8ddp/dataPackageShow.css" type="text/css">
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/ProductTree.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/ProductMenu.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/ModuleTree.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/ModuleMenu.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/PackageViewTree.js"></script>
<style type="text/css">
 	.tree-title{overflow:hidden;width:100%;} 
	html,body{ padding:0px; margin:0; width:100%;height:100%;overflow: hidden;}
</style>
<script type="text/javascript">
		var catKey="${CAT_FORM}";
		var curMenu=null;
		var curModuleMenu=null;
		var productMenu = new ProductMenu();
		var moduleMenu = new ModuleMenu();
		// 产品维度结构树数据接口url
		var productMappingUrl = "/product/dimension/tree/";
		// 型号维度结构树数据接口url
		var moduleMappingUrl = "/module/dimension/tree/";
		// 型号id
		var id = "<%=request.getParameter("xhId")%>";
		var nodeName = "<%=request.getParameter("xhName")%>";
		// 产品维度 点击事件
		var prodTree=new ProductTree(catKey,"prodTree", {
								onClick:productTreeOnClick,
								onRightClick: productTreeOnRightClick},productMappingUrl,id,nodeName);
		// 型号维度 点击事件
		var moduleTree=new ModuleTree(catKey,"moduleTree", {
								onClick:moduleTreeOnClick,
								onRightClick: moduleTreeOnRightClick},moduleMappingUrl,id,nodeName);
		
		// 整体布局
		function loadLayout(){
			$("#defLayout").ligerLayout( {
				leftWidth : 245,
				onHeightChanged: heightChanged,
				allowLeftResize:false,
				space: 0
			});
			//取得layout的高度
		    var height = $(".l-layout-center").height()-36;
        	var _height = $('.tree-toolbar').outerHeight();
		    $(".ztree").height(height-_height);
		}
		
		//布局大小改变的时候通知tab，面板改变大小
		function heightChanged(options){
		 	$(".ztree").height(options.middleHeight-90);
		}
		
		$(function (){

		  	// 布局
		    loadLayout(); 
		  	// 加载产品维度结构树(默认)
		  	
			prodTree.loadGlobalTree();
			// 加载根节点（型号）基本信息（默认）
			rootNodeInfo();
			// 不同维度切换 
			treeTabChange();
		});

		// 加载根节点右侧数据包信息 （型号信息） 
		function rootNodeInfo() {
		  	var url="${ctx}"+"/oa/form/dataTemplate/detailData.do?__displayId__=10000021170075&__pk__=" + id;
			url = encodeURI(url); 
			$("#dataPackageFrame").attr("src",url);
			debugger;
			document.getElementById('dataPackageFrame').onload = function() {
				//显示型号下面一级的list信息/oa/form/dataTemplate/preview.do?__displayId__=10000021190185&__dbomSql__=F_SSXH=10000031111007 and F_LBHPC='category'
				var allProductCategoryUrl="${ctx}"+"/oa/form/dataTemplate/preview.do?__displayId__=10000021190185&__dbomSql__=F_SSXH=" + id+" and F_LBHPC='category'";
				allProductCategoryUrl = encodeURI(allProductCategoryUrl);
				$("#allProductCategory").attr("src",allProductCategoryUrl);
			}

		}
		
		//不同维度切换 
		function treeTabChange() {
			//默认 -产品结构维度
			$("#productTree").click(function() {
				$("#productTree").css({
					"color" : "#347EFE",
				})
				$("#projectTree").css({
					"color" : "#333",
				})
				$(".productTree").show();
				$(".projectTree").hide();
			})
			
			//发次维度 
			$("#projectTree").click(function() {
				$("#productTree").css({
					"color" : "#333",
				})
				$("#projectTree").css({
					"color" : "#347EFE",
				})
				
				$(".productTree").hide();
				$(".projectTree").show();
				debugger;
				//加载树
				moduleTree.loadGlobalTree();
				//加载根节点右侧数据包信息 （型号信息） 
				rootNodeInfo();
			})
		}
		
		// 导入产品类别
		function importProductCategories(){
			 var url=__ctx+"/model/user/role/getUseRole.do";
			 var moduleId=$.getParameter("xhId");
			    debugger;
			    $.ajax({
			        async: 'false',
			        dataType: 'json',
			        data: {moduleId:moduleId},
			        /* url: "${ctx}/project/tree/stree/getCurUserInfo.do", */
			        url:url,
			        success: function (data) {
			        	if(data.useRole=="1"){
			        		var h=window.top.document.documentElement.clientHeight;
			    			var w=window.top.document.documentElement.clientWidth;
			    			var url=__ctx+"/product/category/batch/categoryImportView.do?moduleId="+id;
			    			DialogUtil.open({
			    				height:h*0.4,
			    				width: w*0.4,
			    				title : '产品导入',
			    				url: url,
			    				isResize: true,
			    				//自定义参数?
			    				sucCall:function(rtn){
			    					window.location.href = window.location.href.getNewUrl();
			    				}
			    			});
			        	}else{
			        		$.ligerDialog.warn("您没有权限！");
			        	}
			        }
			    });
		
		}
		//产品维度树manager新增点击事件
		function newadd(){
			 var url=__ctx+"/model/user/role/getUseRole.do";
			 var moduleId=$.getParameter("xhId");
			 $.ajax({
			        async: 'false',
			        dataType: 'json',
			        data: {moduleId:moduleId},
			        /* url: "${ctx}/project/tree/stree/getCurUserInfo.do", */
			        url:url,
			        success: function (data) {
			        	if(data.useRole=="1"){
			        		 var dataId=getQueryVariable("xhId");
			    			 url = __ctx +"/oa/form/dataTemplate/editData.do?__displayId__=10000021190185&handleType=add&moduleId="+dataId;
			    				var h=window.top.document.documentElement.clientHeight;
			    				var w=window.top.document.documentElement.clientWidth;
			    				DialogUtil.open({
			    					height:600,
			    					width:900,
			    					title : '新增产品类别',
			    					url: url,
			    					isResize: true,
			    					//自定义参数?
			    					sucCall:function(rtn){
			    						window.location.href = window.location.href.getNewUrl();
			    					}
			    				});
			        	}else{
			        		$.ligerDialog.warn("您没有权限！");
			        	}
			        }
			  });
		
		}
		//获取url中参数
		function getQueryVariable(variable)
		{
		       var query = window.location.search.substring(1);
		       var vars = query.split("&");
		       for (var i=0;i<vars.length;i++) {
		               var pair = vars[i].split("=");
		               if(pair[0] == variable){return pair[1];}
		       }
		       return(false);
		}
        function serverExport() {
            var node = getSelectNode();
            if (!node || !node.id) {
                $.ligerDialog.warn("请选择导出的发次节点！");
                return;
            }
            var fcId = node.id;
            DialogUtil.open({
                height: 500,
                width: 300,
                url: __ctx + '/dataPackage/tree/dataPackageDialog2.do',
                title: "验收策划节点导出",
                params: {projectId: fcId, isSingle: false},
                sucCall: function (rtn) {
                    var ids = rtn.ids;
                    if (!ids || ids == "") {
                        $.ligerDialog.warn("请选择要导出的策划数据！")
                        $.ligerDialog.closeWaitting();
                        return;
                    }
                    $.ligerDialog.waitting("导出中...", "提示信息");

                    var url = __ctx + "/dataPackage/tree/ptree/serverExportPackages.do?";
                    $.get(url, {fcId: fcId, nodeIds: ids}, function (responseText) {
                    	
                        $.ligerDialog.closeWaitting();
                        var obj = new com.ibms.form.ResultMessage(responseText);
                        if (obj.isSuccess()) {
                            var path = obj.data.filePath;
                            if (path != null || path != "") {
                                path = encodeURI(path);
                            } else {
                                $.ligerDialog.error("返回的下载地址无效！", "提示信息");
                                return;
                            }
                            var url = __ctx + "/oa/system/sysFile/downLoadTempFile.do"
                            url += "?tempFilePath=" + path;
                            window.location.href = url;//下载文件
                        } else {
                            $.ligerDialog.err("提示信息", "", obj.getMessage());
                        }
                    });
                }
            });
            $.ligerDialog.closeWaitting();
        }
        
        
        //服务端导入
        function serverImport() {
            var node = getSelectNode();
            if (!node || !node.id) {
                $.ligerDialog.warn("请选择导入的发次节点！");
                return;
            }
            var fcId = node.id;
            var xhId = node.moduleId;
            var url = __ctx + '/io/packageImport.do';
            DialogUtil.open({
                height: 500,
                width: 800,
                url: url,
                title: "数据包导入",
                params: {
                    isClient: false,
                    fcId: fcId,
                    xhId: xhId
                },
                sucCall: function (rtn) {
                    reFresh()
                }
            });
        }

        function getSelectNode() {
        	debugger;
            var tree = prodTree.glTypeTree;
            var nodes = tree.getSelectedNodes();
            return nodes[0];
        }
	</script> 
</head>
<body>
	<div id="defLayout">
		<!-- <div position="left" title="数据包结构树" style="text-align: left; border-style: solid; border-color:  #FFFFFF #EEEEEE #FFFFFF #FFFFFF "> -->
		<div position="left" title="<div class='tree-tab' id='productTree'>产品维度</div><div class='tree-tab' id='projectTree'>型号维度</div>">	
		<!-- 产品验收的菜单 -->
		
			<!-- <div class="tree-tab" id="productTree">产品维度</div>
			<div class="tree-tab" id="projectTree">型号维度</div> -->
			<div class="productTree tree-div">
				<div class="tree-toolbar" id="pToolbar">
					<div class="group"><a class="link" id="productTreeImport" href="javascript:newadd()" >新建产品<span></span></a></div>	
					<div class="group"><a class="link reload" id="treeFresh" href="javascript:prodTree.reFresh()"><span>刷新</span></a></div>	
					<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:prodTree.treeExpandAll(true)" >展开<span></span></a></div>
					<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:prodTree.treeExpandAll(false)" >收起<span></span></a></div>
					<div class="group"><a class="link import" id="serverExport" href="javascript:importProductCategories()" >导入产品<span></span></a></div> 
					<%-- <div class="group" ><img src="${ctx}/styles/default/images/menu/export_new.png"  onclick="serverExport()"/>
                        <a class="link" id="serverExport" href="javascript:serverExport()">导出</a>
                    </div>
					<div class="group" ><img src="${ctx}/styles/default/images/menu/import_new.png"  onclick="serverImport()"/>
                         <a class="link" id="serverImport" href="javascript:serverImport()">导入</a>
          		    </div> --%>
				</div>
					
				<div id="prodTree" class="ztree" style="overflow:auto;clear:left" ></div>
			</div>
		
		
			
			
			
			<div class="projectTree tree-div" style="display:none;">
				<div class="tree-toolbar" id="pToolbar">	
					<div class="group"><a class="link reload" id="treeFresh" href="javascript:moduleTree.reFresh()"><span>刷新</span></a></div>	
					<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:moduleTree.treeExpandAll(true)" >展开<span></span></a></div>
					<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:moduleTree.treeExpandAll(false)" >收起<span></span></a></div>
				</div>
					
				<div id="moduleTree" class="ztree" style="overflow:auto;clear:left" ></div>
			</div>
		</div>
	
		<div position="center">
			<iframe id="dataPackageFrame" src="" frameborder="no" width="100%" height="240"></iframe>
			<iframe id="allProductCategory" src="" frameborder="no" width="100%" height="100%"></iframe>
		</div>
	</div>
</body>
</html>