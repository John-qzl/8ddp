<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>选择用户 </title>
	<%@include file="/commons/include/form.jsp" %>
	<f:sysUser name="SEARCH_BY_POS" alias="SEARCH_BY_POS"></f:sysUser>
	<f:sysUser name="SEARCH_BY_ROL" alias="SEARCH_BY_ROL"></f:sysUser>
	<f:sysUser name="SEARCH_BY_ORG" alias="SEARCH_BY_ORG"></f:sysUser>
	<f:sysUser name="SEARCH_BY_ONL" alias="SEARCH_BY_ONL"></f:sysUser>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<script type="text/javascript" 	src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript">
		debugger;
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		var SEARCH_POS = "${SEARCH_BY_POS}";
		var SEARCH_ROL = "${SEARCH_BY_ROL}";
		var SEARCH_ORG = "${SEARCH_BY_ORG}";
		var SEARCH_ONL = "${SEARCH_BY_ONL}";
		var isSingle=${isSingle};
		var rol_Tree=null;
		var org_Tree=null;
		var pos_Tree=null;
		var onl_Tree=null;
		var accordion = null;
		//这个是系统用户获取范围控制，默认是system all
		var type ="";
		var typeVal="";

		//若用户配置了级联关系则获取相关属性
		var relvalue=dialog.get("relvalue");
		var dataId=dialog.get("dataId");   //定制  过滤
		var dataType=dialog.get("dataType");
		var userIdFilter=dialog.get("userIdFilter");//定制过滤以选择过的用户
		var scope =eval("("+dialog.get("scope")+")");
		if(scope){
			type = scope.type;
			typeVal = scope.value;
			
		}

		//树展开层数
		var expandDepth = 1;
		forbidF5("Chrome");//禁止刷新页面
		$(function(){
			//布局
			debugger
			$("#defLayout").ligerLayout({
				 leftWidth: 220,
				 rightWidth: 170,
				 bottomHeight :40,
				 height: '90%',
				 allowBottomResize:false,
				 allowLeftCollapse:false,
				 allowRightCollapse:false,
				 onHeightChanged: heightChanged,
				 minLeftWidth:200,
				 allowLeftResize:false
			});

			var findStr = '';
			//快速查找
			$("input.quick-find").bind('keyup',function(){
				var str = $(this).val();
				if(!str)return;
				if(str==findStr)return;
				findStr = str;
				var  tbody = $("#userList"),
					 firstTr = $('tr.hidden',tbody);
				$("tr",tbody).each(function(){
					var me = $(this),
						span = $('span',me),
						spanStr = span.html();
					if(!spanStr)return true;
					if(spanStr.indexOf(findStr)>-1){
						$(this).insertAfter(firstTr);
					}
				});
			});
			var height = $(".l-layout-center").height();
			$("#leftMemu").ligerAccordion({ height: height-28, speed: null });
		    accordion = $("#leftMemu").ligerGetAccordionManager();

		    if(typeVal != "all" || relvalue!=""){
		    	//只有在范围为all时才显示角色和岗位
				$("#roleSearch").prev().hide();
				$("#roleSearch").hide();

				$("#positionSearch").prev().hide();
				$("#positionSearch").hide();
			}

		    //加载角色树菜单
	    	load_Rol_Tree();
		    //加载组织树菜单
		    load_Org_Tree();
		    //加载岗位树菜单
	    	load_Pos_Tree();

		    //load_Onl_Tree();

		    heightChanged();

		    handleSelects();

		    var  src="${ctx}/oa/system/sysUser/selector.do?isSingle=${isSingle}&type="+type+"&typeVal="+typeVal+"&relvalue="+relvalue+"&dataId="+dataId+"&dataType="+dataType;
			 $("#userListFrame").attr("src",src);
		});
		function heightChanged(options){
			if(options){
			    if (accordion && options.middleHeight - 28 > 0){
			    	$("#SEARCH_BY_ROL").height(options.middleHeight - 146);
				    $("#SEARCH_BY_ORG").height(options.middleHeight - 126);
				    $("#SEARCH_BY_POS").height(options.middleHeight - 103);
				    $("#SEARCH_BY_ONL").height(options.middleHeight -126);
			        accordion.setHeight(options.middleHeight + 11);
			    }
			}else{
			    var height = $(".l-layout-center").height();
				$("#SEARCH_BY_ROL").height(height - 146);
			    $("#SEARCH_BY_ORG").height(height - 126);
			    $("#SEARCH_BY_POS").height(height - 103);
			    $("#SEARCH_BY_ONL").height(height - 126);
		    }
		}

		function setCenterTitle(title){

			$("#centerTitle").empty();
			$("#centerTitle").append(title);

		};
		 //加载岗位树菜单
		function load_Pos_Tree(){
			var demId=1;
			var setting = {
		    		data: {
						key : {
							name: "posName",
							title: "posName"
						},
						simpleData: {
							enable: true,
							idKey: "posId",
							rootPId: -1
						}
					},
		    		callback: {
						onClick: function(event, treeId, treeNode){
						var url="${ctx}/oa/system/sysUser/selector.do";
						var p="?isSingle=${isSingle}&searchBy= "+SEARCH_POS+" &typeVal= "+typeVal+" &type= "+type+" &posId=" + treeNode.posId+"&dataId="+dataId+"&dataType="+dataType;;
							$("#userListFrame").attr("src", url + p);
							setCenterTitle("按岗位查找:" + treeNode.posName);
						}
					}
				};
				var url="${ctx}/oa/system/position/getAll.do";
				var para= {demId : demId};
				$.post(url,para,function(result){
					pos_Tree = $.fn.zTree.init($("#SEARCH_BY_POS"), setting,result);
					if(expandDepth!=0)
					{
						pos_Tree.expandAll(false);
						var nodes = pos_Tree.getNodesByFilter(function(node){
							return (node.level < expandDepth);
						});
						if(nodes.length>0){
							for(var i=0;i<nodes.length;i++){
								pos_Tree.expandNode(nodes[i],true,false);
							}
						}
					}else pos_Tree.expandAll(true);
				});
		};

		//判断是否为子结点,以改变图标
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			if(treeNode){
		  	 var children=treeNode.children;
			  	 if(children.length==0){
			  		treeNode.isParent=true;
			  		pos_Tree = $.fn.zTree.getZTreeObj("SEARCH_BY_POS");
			  		pos_Tree.updateNode(treeNode);
			  	 }
			}
		};
		//加载组织树菜单
		function load_Org_Tree(){
			var value=1;
			var setting = {
					data: {
						key : {

							name: "orgName",
							title: "orgName"
						},

						simpleData: {
							enable: true,
							idKey: "orgId",
							pIdKey: "orgSupId",
							rootPId: -1
						}
					},
					async: {
						enable: true,
						url:"${ctx}/oa/system/sysOrg/getTreeData.do?demId="+value+"&type="+type+"&typeVal="+typeVal+"&relvalue="+relvalue,
						autoParam:["orgId","orgSupId"]
					},
					callback:{
						onClick: function(event, treeId, treeNode){
							var url="${ctx}/oa/system/sysUser/selector.do";
							var includSub=($("#includSub").attr("checked"))?1:0;
							var p="?isSingle=${isSingle}&searchBy="+SEARCH_ORG+" &typeVal= "+typeVal+" &type= "+type+" &orgId="+treeNode.code+"&includSub="+includSub+"&path="+treeNode.path+"&relvalue="+relvalue+"&dataId="+dataId+"&dataType="+dataType;
							$("#userListFrame").attr("src",url+p);
							setCenterTitle("按组织查找:"+treeNode.orgName);
						},
						onAsyncSuccess: orgTreeOnAsyncSuccess
					}

				};
				org_Tree=$.fn.zTree.init($("#SEARCH_BY_ORG"), setting);
		};
		//判断是否为子结点,以改变图标
		function orgTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			if(treeNode){
		  	 	var children=treeNode.children;
			  	 if(children.length==0){
			  		treeNode.isParent=true;
			  		org_Tree = $.fn.zTree.getZTreeObj("SEARCH_BY_ORG");
			  		org_Tree.updateNode(treeNode);
			  	 }
			}
		};
		function load_Rol_Tree(){
			var roleName=$("#Q_roleName_SL").val();
			var setting = {
		    		data: {
						key : {
							name: "roleName",
							title: "roleName"
						},
						simpleData: {
							enable: true,
							idKey: "roleId",
							rootPId: -1
						}
					},
		    		callback: {
						onClick: function(event, treeId, treeNode){
						var url="${ctx}/oa/system/sysUser/selector.do";
						var p="?isSingle=${isSingle}&searchBy="+SEARCH_ROL+" &typeVal= "+typeVal+" &type= "+type+" &roleId=" + treeNode.roleId+relvalue+"&dataId="+dataId+"&dataType="+dataType;
							$("#userListFrame").attr("src", url + p);
							setCenterTitle("按角色查找:" + treeNode.roleName);
						}
					}
				};
				var url="${ctx}/oa/system/sysRole/getAll.do";
				var para= {Q_roleName_SL: roleName};
				$.post(url,para,function(result){
					rol_Tree = $.fn.zTree.init($("#SEARCH_BY_ROL"), setting,result);
					if(expandDepth!=0)
					{
						rol_Tree.expandAll(false);
						var nodes = rol_Tree.getNodesByFilter(function(node){
							return (node.level < expandDepth);
						});
						if(nodes.length>0){
							for(var i=0;i<nodes.length;i++){
								rol_Tree.expandNode(nodes[i],true,false);
							}
						}
					}else rol_Tree.expandAll(true);
				});
			};


			function load_Onl_Tree(){
				var value=$("#onl").val();
				var setting = {
			    		data: {
							key : {
								name: "orgName",
								title: "orgName"
							},
							simpleData: {
								enable: true,
								idKey: "orgId",
								pIdKey : "orgSupId",
								rootPId: -1
							}
						},
			    		callback: {
							onClick: function(event, treeId, treeNode){
								var url="${ctx}/oa/system/sysUser/selector.do";
								var p="?isSingle=${isSingle}&searchBy="+SEARCH_ONL+" &typeVal= "+typeVal+" &type= "+type+" &path="+treeNode.path;
								$("#userListFrame").attr("src",url+p);
								setCenterTitle("按组织查找:"+treeNode.orgName);
							}
						}
			    };

				var url= "${ctx}/oa/system/sysOrg/getTreeOnlineData.do?type="+type+"&typeVal="+typeVal;
				var para="demId=" + value;
				$.post(url,para,function(result){
					org_Tree = $.fn.zTree.init($("#SEARCH_BY_ONL"), setting,result);
					if(expandDepth!=0)
					{
						org_Tree.expandAll(false);
						var nodes = org_Tree.getNodesByFilter(function(node){
							return (node.level < expandDepth);
						});
						if(nodes.length>0){
							for(var i=0;i<nodes.length;i++){
								org_Tree.expandNode(nodes[i],true,false);
							}
						}
					}else org_Tree.expandAll(true);
				});

			};


			function dellAll() {
				$("#sysUserList").empty();
			};
			function del(obj) {
				var tr = $(obj).parents("tr");
				$(tr).remove();
			};

			function add(data) {
				debugger
				var aryTmp=data.split("#");
				var userId=aryTmp[0];
				var len= $("#user_" + userId).length;
				if(len>0) return;

				var aryData=['<tr id="user_'+userId+'">',
					'<td>',
					'<input type="hidden" class="pk" name="userData" value="'+data +'"><span> ',
					aryTmp[1],
					'</span></td>',
					'<td><a onclick="javascript:del(this);" class="link del" ></a> </td>',
					'</tr>'];
				$("#sysUserList").append(aryData.join(""));
			};

			function selectMulti(obj) {
				if ($(obj).attr("checked") == "checked"){
					var data = $(obj).val();
					add(data);
				}
			};

			function selectAll(obj) {
				var state = $(obj).attr("checked");
				var rtn=state == undefined?false:true;
				checkAll(rtn);
			};

			function checkAll(checked) {
				$("#userListFrame").contents().find("input[type='checkbox'][class='pk']").each(function() {
					$(this).attr("checked", checked);
					if (checked) {
						var data = $(this).val();
						add(data);
					}
				});
			};

			function clearUser(){
				var rtn = {
					userIds: '',
					fullnames: '',
					emails: '',
					mobiles: ''
				};
				dialog.get("sucCall")(rtn);
				dialog.close();
			}

			function selectUser(){
				debugger
				var pleaseSelect= "请选择用户!";
				var chIds;
				if(isSingle==true){
					chIds = $('#userListFrame').contents().find(":input[name='userData'][checked]");

				}else{
					chIds = $("#sysUserList").find(":input[name='userData']");
				}

				if (!chIds||chIds.length == 0) {
					alert(pleaseSelect);
					return;
				}

				var aryuserIds=new Array();
				var aryfullnames=new Array();
				var aryemails=new Array();
				var arymobiles=new Array();

				$.each(chIds,function(i,ch){
					var aryTmp=$(ch).val().split("#");
					aryuserIds.push(aryTmp[0]);
					aryfullnames.push(aryTmp[1]);
					aryemails.push(aryTmp[2]);
					arymobiles.push(aryTmp[3]);

				});

				var obj={userIds:aryuserIds.join(","),fullnames:aryfullnames.join(","),
						emails:aryemails.join(","),mobiles:arymobiles.join(",")};
				try{
					dialog.get("sucCall")(obj);
				}catch(e){
					$.ligerDialog.warn("选择用户回调错误！");
				}
				dialog.close();
			}

			var handleSelects=function(){
				var    selectUsers    =dialog.get("selectUsers");
				if(selectUsers.selectUserIds && selectUsers.selectUserNames){
					var ids=selectUsers.selectUserIds.split(",");
					var names=selectUsers.selectUserNames.split(",");
					for(var i=0;i<ids.length;i++){
						if(names[i]!=undefined&&names[i]!="undefined"&&names[i]!=null&&names[i]!=""){
							add(ids[i]+"#"+names[i]+"##");
						}
					}
				}
			}
	</script>

<style type="text/css">
<f:link href="from-jsp.css"></f:link>.l-layout-right{left:511px;}
.l-layout {background: #f5f5f5;}
.l-layout-left, .l-layout-center, .l-layout-right { height:90%;}
.l-accordion-content { height:324px;}
.l-accordion-content .ztree { height:285px;}
.l-layout-header.l-layout-header {
    display: none;
}
.l-layout-left {
    border-top: 5px solid #f5f5f5;
    border-left: 10px solid #f5f5f5;
}
.l-accordion-panel {
    border-bottom: 0px solid #BED5F3;
}
.l-layout-right .l-layout-header{
	display: block;
	background: #fff;
	border-top: 5px solid #f5f5f5;
}
.l-layout-right .l-layout-header .l-layout-header-inner{
	padding: 0;
}
/* #defLayout{
	padding-bottom: 5px;
} */
div[position="bottom"].bottom{
	border-top: none;
}
a.empty{
	border: 1px solid #F14958;
	color: #F14958;
	background: #fff;
}
a.empty:hover{
	border: 1px solid #F14958;
	color: #fff;
	background: #F14958;
}
a.cancel{
	border: 1px solid #d9d9d9;
	color: #666;
	background: #fff;
}
a.cancel:hover{
	border: 1px solid #d9d9d9;
	background: #d9d9d9;
}
a.determine{
	border: 1px solid #347EFE;
	color: #fff;
	background: #347EFE;
}
a.determine:hover{
	border: 1px solid #2f6edb;
	background: #2f6edb;
}
div.bottom {
    padding-bottom: 0;
    height: 50px;
}
</style>
</head>
<body>
	<div id="defLayout" style="height:100%;">
		<%-- <div id="leftMemu" position="left" title="查询条件<input type='checkbox' id='includSub' style='margin-left:10px;vertical-align:middle'/>包含子分类" style="overflow: auto; float:left;width: 100%; height:95%"> --%>
		<div id="leftMemu" position="left" style="overflow: auto; float:left;width: 100%; height:95%">
			<div title="按组织查找" style="overflow: hidden;">
<%--				<table border="0" width="100%" class="table-detail">--%>
<%--					<tr >--%>
<%--						<td width="30%" nowrap="nowrap"><span class="label">维度:</span>--%>
<%--						</td>--%>
<%--						<td style="width:70%;">--%>
<%--							<select id="dem" name="dem" onchange="load_Org_Tree()">--%>
<%--								<c:forEach var="demen" items="${demensionList}">--%>
<%--									<option  value="${demen.demId}" <c:if test="${demen.demId==1}">selected</c:if>>${ demen.demName}</option>--%>
<%--								</c:forEach>--%>
<%--							</select>--%>
<%--						</td>--%>
<%--					</tr>--%>
<%--				</table>--%>
				<div id="SEARCH_BY_ORG" class='ztree'></div>
			</div>
			<div id="positionSearch" title="按岗位查找" style="overflow: hidden;">
<%--				<table border="0" width="100%" class="table-detail">--%>
<%--					<tr >--%>
<%--						<td width="30%" nowrap="nowrap"><span class="label">维度:</span>--%>
<%--						</td>--%>
<%--						<td style="width:70%;">--%>
<%--							<select id="demPos" name="demPos" onchange="load_Pos_Tree()">--%>
<%--								<c:forEach var="demen" items="${demensionList}">--%>
<%--									<option  value="${demen.demId}" <c:if test="${demen.demId==1}">selected</c:if>>${ demen.demName}</option>--%>
<%--								</c:forEach>--%>
<%--							</select>--%>
<%--						</td>--%>
<%--					</tr>--%>
<%--				</table>--%>
				<div id="SEARCH_BY_POS" class='ztree'></div>
			</div>
			<div id="roleSearch" title="按角色查找" style="overflow: hidden;">
				<div class="tree-title" style="width: 100%;">
					<div class="panel-detail">
						<table border="0" width="100%" class="table-detail">
							<tr>
								<td>
									<span class="label" style="width:60px;">角色:</span>
								</td>
								<td>
									<input style="width:120px;" id="Q_roleName_SL" name="Q_roleName_SL" type="text" size="10">
								</td>
								<td>
									<a class="link detail" href="javascript:load_Rol_Tree();">&ensp;</a>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div id="SEARCH_BY_ROL" class='ztree'></div>
			</div>
            <!--
			<div title="在线用户" style="overflow: hidden;">

				<table border="0" width="100%" class="table-detail">
					<tr >
						<td width="30%" nowrap="nowrap"><span class="label">维度:</span>
						</td>
						<td style="width:70%;">
							<select id="onl" name="onl" onchange="load_Onl_Tree()">
								<c:forEach var="demen" items="${demensionList}">
									<option  value="${demen.demId}" <c:if test="${demen.demId==1}">selected</c:if>>${ demen.demName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>

				<div id="SEARCH_BY_ONL" class='ztree'></div>
			</div>
			 -->

		</div>
		<div position="center">
			<!-- <div id="centerTitle" class="l-layout-header">全部用户</div> -->
			<iframe id="userListFrame" name="userListFrame" height="100%" width="100%" frameborder="0"></iframe>
		</div>
		<c:if test="${isSingle==false}"> 
			 <div position="right" style="overflow:auto;height:95%;width:170px;"  title="<span><a onclick='javascript:dellAll();' class='link del'>清空 </a></span>">
				<table width="145" id="sysUserList" class="table-grid table-list" 	id="0" cellpadding="1" cellspacing="1">
					<tbody id="userList">
       					<tr class="hidden"></tr>
       				</tbody>
				</table>
			</div>
		</c:if> 
	</div>
	<div position="bottom"  class="bottom">
		<div class="fr">
		<a href="####" class="button empty"  onclick="clearUser()"><!-- <span class="icon cancel" ></span> --><span class="chosen" >清空</span></a>
		<a href="####" class="button cancel"  onclick="dialog.close()"><!-- <span class="icon cancel"></span> --><span>取消</span></a>
		<a href="####" class="button determine"  onclick="selectUser()" style="margin-right:10px;" ><!-- <span class="icon ok"></span> --><span>确定</span></a>
		</div>
	</div>
</body>
</html>

