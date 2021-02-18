<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>资源管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerWindow.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/IconDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=resources"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/clipboard.min.js"></script>
<script type="text/javascript"	src="${ctx}/jslib/ibms/oa/system/ResourcesMenu.js"></script>

<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	var rootId=0;
	var winArgs =dialog.get('params');
	$(function(){
		$("#defaultUrl").val(winArgs.addUrl);
		$("#resName").val(winArgs.name);
		
		//布局
		loadLayout();
		//菜单
		_ResourcesMenu_.init("listFrame");
		loadTree();
		
		//-----Center Area
		function showRequest(formData, jqForm, options) { 
			return true;
		}
		
		$("#resName").blur(function(){
			var obj=$(this);
			autoPingin(obj);
		});
		
		valid(showRequest,showResponse);
		$("a.save").click(function() {
			var parentId = $("#parentId").val();
			if(!parentId){
				$.ligerDialog.warn('请从左边的资源树上选择父节点！');
				return;
			}
			$('#resourcesForm').submit(); 
		});
		
	});

	//布局
	function loadLayout(){
		$("#layout").ligerLayout( {
			leftWidth : 300,
			height:800,
			onHeightChanged: heightChanged,
			allowLeftResize:false
		});
		//取得layout的高度
        var height = $(".l-layout-center").height();
        $("#resourcesTree").height(height-90);
	};
	//布局大小改变的时候通知tab，面板改变大小
    function heightChanged(options){
     	$("#resourcesTree").height(options.middleHeight -90);
    };
    //树
	var resourcesTree;
	//加载树
	function loadTree(){
		var setting = {
			async: {enable: false},
			data: {
				key:{name:"resName"},
				simpleData: {
					enable: true,
					idKey: "resId",
					pIdKey: "parentId",
					rootPId: "${ROOT_PID}"
				}
			},
			view: {
				selectedMulti: false
			},
			callback:{
				onClick: zTreeOnLeftClick,
				onRightClick: zTreeOnRightClick
			}
		};
		var url="${ctx}/oa/system/resources/getSystemTreeData.do";
		var params=null;
		$.post(url,params,function(result){
			resourcesTree=$.fn.zTree.init($("#resourcesTree"), setting,result);
			resourcesTree.expandAll(true);
		});
	
	};


	//左击
	function zTreeOnLeftClick(event, treeId, treeNode){
		$("#listFrame").hide();
		$("#addResourceDiv").show();
		var isfolder=treeNode.isFolder;
		var msg=$("#msg");
		if(isfolder==1){
			msg.text('');
		}else if(isfolder==0){
			msg.text('选择的父节点必须要有权限放子节点！');
			return;
		}
		var resId=treeNode.resId;
		$("#parentId").val(resId);
	};
	//右击
	function zTreeOnRightClick(e, treeId, treeNode) {
		$("#addResourceDiv").hide();
		$("#listFrame").show();
		_ResourcesMenu_.zTreeOnRightClick(e, treeId, treeNode);
	}
	
	//展开收起
	function treeExpandAll(type){
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		resourcesTree.expandAll(type);
	};

	//选择资源节点。
	function getSelectNode(){
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		var nodes  = resourcesTree.getSelectedNodes();
		var node   = nodes[0];
		return node;
	}
	//刷新
	function reFresh(){
		loadTree();
	};
	
	//添加完成后调用该函数
	function addResource(id,text,icon,isFolder){
		var parentNode=getSelectNode();
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		var treeNode= {resId:id,parentId:parentNode.resId,resName:text,icon:icon,isFolder:isFolder};
		resourcesTree.addNodes(parentNode,treeNode);
	}
	//编辑完成后调用该函数。
	function editResource(text,icon,isFolder){
		var selectNode=getSelectNode();
		selectNode.resName=text;
		selectNode.icon=icon;
		selectNode.isFolder=isFolder;
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		resourcesTree.updateNode(selectNode);
	}

	function showResponse(responseText){
		var json=eval("("+responseText+")");
		if(json.result==1){
			var resName=$("#resName").val();
			var isFolder=$("#isFolder").val();
			var icon=$("#icon").val();
			addResource(json.resId,resName,icon,isFolder);
			$.ligerDialog.confirm("添加为菜单成功！是否继续操作",'提示信息',function(rtn){
				if(!rtn){
					dialog.close();
				}
			});
		}
		else{
			$.ligerDialog.err('出错信息',"查看资源管理失败",json.message);
		}
	}
	
	//展示选择的图片
	function selectIcon(){
		 IconDialog({callback:function(src){
			$("#icon").val(src);
			// $("#iconImg").attr("src",src);
			$("#iconImg").attr("class","iconfont icon-"+src)
			$("#iconImg").show();
		}});
	};
	//调用生成拼音方法
	function autoPingin(obj){
		var value=obj.val();
		if(value!=""&&value!=undefined){
			Share.getPingyin({
				input:value,
				postCallback:function(data){
					$("#alias").val(data.output);
				}
			});
		}
	};
</script>
<f:link href="from-jsp.css"></f:link></head>
<body>
<div id="layout">
<div id="left" position="left" title="资源管理" >
		<div class="tree-toolbar tree-title" id="pToolbar">
			<span class="toolBar">
				<div class="group"><a class="link reload" id="treeFresh" href="javascript:reFresh();">刷新</a></div>
				
				<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)">展开</a></div>
				
				<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)">收起</a></div>
			</span>
		</div>
		<ul id="resourcesTree" class="ztree"></ul>
	</div>
	<div position="center">
		<div id="addResourceDiv">
		<form id="resourcesForm" method="post" action="save.do">
			<div class="panel">
			<div class="panel-top">
						<div class="tbar-title">
							<span class="tbar-label">添加子系统资</span>
						</div>
						<div class="panel-toolbar">
							<div class="toolBar">
								<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
								
								<div><span style="margin-left: 50px" class="red" id="msg"></span></div>
							</div>
						</div>
					</div>
					<div class="panel-body">
							<table id="resourcesTable" class="table-detail" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<th width="20%">资源名称:  <span class="required">*</span></th>
										<td><input type="text" id="resName" name="resName"   class="inputText"/></td>
									</tr>
									<tr>
										<th width="20%">资源别名: </th>
										<td><input type="text" id="alias" name="alias" class="inputText"/></td>
									</tr>
									
									<tr>
										<th width="20%">资源图标: </th>
										<td>
											<input type="hidden" id="icon" name="icon"  class="inputText"/>
											
											<span id="iconImg" class="iconfont" style="display:none"></span>
											<a class="link detail" href="javascript:selectIcon();">选择</a>
										</td>
									</tr>
									<tr>
										<th width="20%">默认地址: </th>
										<td><input type="text" id="defaultUrl" name="defaultUrl" style="width:400px"  class="inputText" readonly="readonly"/>
										<button  class="copybtn" data-clipboard-action="copy" data-clipboard-target="#defaultUrl">复制</button></td>
									</tr>
									<tr>
										<th width="20%">是否有子节点: </th>
										<td>
											<select id="isFolder" name="isFolder">
												<option value="0" >否</option>
												<option value="1" >是</option>
											</select>
										</td>
									</tr>
									<tr>
										<th width="20%">显示到菜单: </th>
										<td>
											<select id="isDisplayInMenu" name="isDisplayInMenu">
												<option value="0" >否</option>
												<option value="1" selected="selected" >是</option>
											</select>
										</td>
									</tr>
									<tr>
										<th width="20%">默认打开: </th>
										<td>
											<select id="isOpen" name="isOpen">
												<option value="0" >否</option>
												<option value="1" selected="selected" >是</option>
											</select>
										</td>
									</tr>
									
									<tr style="display: none;">
										<th width="20%">同级排序: </th>
										<td><input type="text" id="sn" name="sn" value="1" class="inputText"/></td>
									</tr>
									<tr style="display: none;">
										<th width="20%">父ID: </th>
										<td><input type="text" id="parentId" name="parentId"  class="inputText"/></td>
									</tr>
									<input type="hidden" id="resId" name="resId" />
							</table>
					</div>
			</div>
			</form>
		</div>
		<iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
	</div>
</div>
<script>
//目前firefox低版本不支持execCommand
var clipboard = new Clipboard('.copybtn');
clipboard.on('success', function(e) {
    console.info('Action:', e.action);
    console.info('Text:', e.text);
    console.info('Trigger:', e.trigger);
	$.ligerDialog.success('复制成功!');
    e.clearSelection();
});

clipboard.on('error', function(e) {
    console.error('Action:', e.action);
    console.error('Trigger:', e.trigger);
});
</script>
</body>
</html>

