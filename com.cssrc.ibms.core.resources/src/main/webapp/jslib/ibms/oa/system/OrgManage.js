$(function() {
	var expandDepth = 1; 
	OrgTree = {
			orgTree:null,
			loadTree:function(){
				var setting = {
					data: {
							key : {
								name: "orgName",
								title: "orgName"
							}
						},
					// 拖动
					edit : {
						enable : true,
						showRemoveBtn : false,
						showRenameBtn : false,
						drag : {
							prev : true,
							inner : true,
							next : true,
							isMove : true,
							isCopy : true
						}
					},
					view : {
						selectedMulti : false
					},
					callback : {
						onClick : this.zTreeOnLeftClick,
						onRightClick : this.zTreeOnRightClick,
						onDrop : this.onDrop
					}
				};
				var orgId = $("#orgAuth").val(); 
				if(!orgId) orgId =0;
				var url=__ctx + "/oa/system/grade/getOrgJsonByAuthOrgId.do?orgId="+orgId;
				
				$.post(url,function(result) {
					if($.isEmpty(result)) return;
					var zNodes=eval("(" +result +")");
					OrgTree.orgTree = $.fn.zTree.init($("#orgTree"), setting,zNodes);
					if(expandDepth!=0)
					{
						var nodes = OrgTree.orgTree.getNodesByFilter(function(node){
							return (node.level < expandDepth);
						});
						if(nodes.length>0){
							for(var i=0;i<nodes.length;i++){
								OrgTree.orgTree.expandNode(nodes[i],true,false);
							}
						}
					}else{
						OrgTree.orgTree.expandAll(true);
					}
				});
				


			},
			
			zTreeOnLeftClick:function(event, treeId, treeNode){
				var orgId = treeNode.orgId;
				var authId = $("#orgAuth").children('option:selected').attr("authId");
				if(!authId) authId = 0;
				
				var url=__ctx + "/oa/system/sysOrg/getGrade.do?orgId=" +orgId +"&authId="+authId;
				
				$("#viewFrame").attr("src", url);
			},
			/**
			 * 右击事件
			 */
			zTreeOnRightClick:function(e, treeId, treeNode) {
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				treeObj.selectNode(treeNode);
				contextMenu.show({
					top : e.pageY,
					left : e.pageX
				});
				
			},
			getSelectNode:function(){
				var nodes = OrgTree.orgTree.getSelectedNodes();
				var treeNode = nodes[0];
				return treeNode;
			},
			//编辑组织参数属性
			orgParam:function(){
				var treeNode = OrgTree.getSelectNode();
				var orgId = treeNode.orgId;
				var url = __ctx + "/oa/system/sysOrgParam/editByOrgId.do?orgId="+ orgId;
				$("#viewFrame").attr("src", url);
			},
			editOrg:function(){
				var treeNode=OrgTree.getSelectNode();
				var orgId = treeNode.orgId;
				var demId = treeNode.demId;
				var url=__ctx + "/oa/system/sysOrg/editGrade.do?demId={0}&orgId={1}&action=edit";
				url=String.format(url,demId,orgId);
				$("#viewFrame").attr("src", url);
			},
			addOrg:function () {
				var treeNode=OrgTree.getSelectNode();
				var orgId = treeNode.orgId;
				var demId = treeNode.demId;
				var url=__ctx + "/oa/system/sysOrg/editGrade.do?demId={0}&orgId={1}&action=add";
				url=String.format(url,demId,orgId);
				$("#viewFrame").attr("src", url);
			},
			delNode:function() {
				var treeNode=OrgTree.getSelectNode();
				var callback = function(rtn) {
					if (!rtn) return;
					var params = "orgId=" + treeNode.orgId;
					var url=__ctx + "/oa/system/sysOrg/orgdel.do";
					$.post(url, params, function() {
						orgTree.removeNode(treeNode);
					});
				};
				$.ligerDialog.confirm("确认要删除此组织吗，其下组织也将被删除？",'提示信息', callback);

			},
			//编辑组织分级管理员
			orgAuth: function() {
				var treeNode=OrgTree.getSelectNode();
				var orgId = treeNode.orgId;
				//获取上级组织id
				var orgSupId = treeNode.orgSupId;
				var url = __ctx + "/oa/system/orgAuth/gradeList.do?isGrade=true&orgId="+ orgId +"&orgSupId="+orgSupId; 
				$("#viewFrame").attr("src", url); 
				
				
				orgTree = $.fn.zTree.getZTreeObj("orgTree");
				var nodes = orgTree.getSelectedNodes();
				var treeNode = nodes[0];
				var orgId = treeNode.orgId;
				$("#viewFrame").attr("src", url);
			},
			onDrop:function (event, treeId, treeNodes, targetNode, moveType) {
				if (targetNode == null || targetNode == undefined)	return;
				var targetId = targetNode.orgId;
				var dragId = treeNodes[0].orgId;
				var url = __ctx + "/oa/system/sysOrg/move.do";
				var params = {
					targetId : targetId,
					dragId : dragId,
					moveType : moveType
				};

				$.post(url, params, function(result) {
					OrgTree.loadTree();
				});
			}
			
	};
});
