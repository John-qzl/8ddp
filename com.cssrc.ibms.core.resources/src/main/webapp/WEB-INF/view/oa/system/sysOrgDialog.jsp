<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>组织选择</title>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="from-jsp.css"></f:link>
<style type="text/css">
.l-layout-right{left:578px;}
.l-layout{background: #f5f5f5;}
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
</style>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript">

		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		//这个是系统用户获取范围控制，默认是system all
		var type ="";
		var typeVal="";

		var scope =eval("("+dialog.get("scope")+")");
		if(scope){
			type = scope.type;
			typeVal = scope.value;
		}

		var isSingle='${param.isSingle}';
		var orgTree = null;
		findStr = '';
		//树展开层数
		var expandDepth = 2;
		var type ="";
		var typeVal="";
		if(scope){
			type = scope.type;
			typeVal = scope.value;
		}
		forbidF5("Chrome");//禁止刷新页面
		$(function() {
			$("#defLayout").ligerLayout({
				leftWidth: 190,
				allowRightResize:false,
				allowLeftResize:false,
				allowTopResize:false,
				allowBottomResize:false,
				height: '90%',
				minLeftWidth:170,
				rightWidth:170
			});

			$('#demensionId').change(function(){
        		var demensionId=$(this).val();
        		loadTree(1);
             });
			//首先加载行政维度的数据。
			loadTree(1);

			//快速查找
			handQuickFind();

			initData();

			var  src="${ctx}/oa/system/sysOrg/selector.do?isSingle=${param.isSingle}&type="+type+"&typeVal="+typeVal;
			 $("#orgFrame").attr("src",src);

		});

		function handQuickFind(){
			$("input.quick-find").bind('keyup',function(){
				var str = $(this).val();
				if(!str)return;
				if(str==findStr)return;
				findStr = str;
				var  tbody = $("#orgList"),
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
		}


		//初始化父级窗口传进来的数据
		function initData(){
			var obj=dialog.get("arrys");

			if(obj&&obj.length>0){
				for(var i=0,c;c=obj[i++];){
					var data = c.id+'#'+c.name;
					if(c.name!=undefined&&c.name!="undefined"&&c.name!=null&&c.name!=""){
						add(data);
					}
				}
			}
		};
		function add(data) {
			var aryTmp = data.split("#");
			var orgId = aryTmp[0];
			var len = $("#org_" + orgId).length;
			if (len > 0) return;
			var roleTemplate = $("#orgTemplate").val();
			var html = roleTemplate.replace("#orgId", orgId)
				.replace("#data", data)
				.replace("#name", aryTmp[1]);
			$("#orgList").append(html);
		};
		//展开收起
		function treeExpandAll(type) {
			orgTree = $.fn.zTree.getZTreeObj("orgTree");
			orgTree.expandAll(type);
		};

		function treeExpand() {
			orgTree = $.fn.zTree.getZTreeObj("orgTree");
			var treeNodes = orgTree.transformToArray(orgTree.getNodes());
			for(var i=1;i<treeNodes.length;i++){
				if(treeNodes[i].children){
					orgTree.expandNode(treeNodes[i], true, false, false);
				}
			}
		};

		function treeFlesh(){
			var demId=$("#demensionId").val();
			loadTree(1);
		}

		function loadTree(value) {
			var setting = {
					data: {
						key : {
							name: "orgName"
						},

						simpleData: {
							enable: true,
							idKey: "orgId",
							pIdKey: "orgSupId",
							rootPId: 0
						}
					},
					async: {
						enable: true,
						url:"${ctx}/oa/system/sysOrg/getTreeData.do?demId="+value+"&type="+type+"&typeVal="+typeVal,
						autoParam:["orgId","orgSupId"]
					},
					callback:{
						onClick : treeClick,
						onAsyncSuccess: orgTreeOnAsyncSuccess
					}

				};
				orgTree=$.fn.zTree.init($("#orgTree"), setting);
		};
		//判断是否为子结点,以改变图标
		function orgTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			if(treeNode){
		  	 	var children=treeNode.children;
			  	 if(children.length==0){
			  		treeNode.isParent=true;
			  		orgTree = $.fn.zTree.getZTreeObj("orgTree");
			  		orgTree.updateNode(treeNode);
			  	 }
			}
		};
		//选择分类
		function getSelectNode() {
			orgTree = $.fn.zTree.getZTreeObj("orgTree");
			var nodes = orgTree.getSelectedNodes();
			var node = nodes[0];
			if (node == null || node.orgId == 0)
				return '';
			return node.orgId;
		}

		function treeClick(event, treeId, treeNode) {
			debugger;
			//取得组织id
			var orgId = treeNode.code;

			var demId = 1001;
			var url = "${ctx}/oa/system/sysOrg/selector.do?orgId=" + orgId + "&type="+type+"&typeVal="+typeVal+"&demId=" + demId+"&isSingle=${param.isSingle}";
			$("#orgFrame").attr("src", url);
			setOrgId(orgId,demId);
		}

		function setOrgId(orgId,demId){
			$("#orgId").val(orgId);
			$("#demId").val(demId);
		}
		function selectOrg() {
			var pleaseSelect= "请选择组织!";
			var aryOrg;
				if(isSingle=='true'){
					aryOrg = $('#orgFrame').contents().find(":input[name='orgId'][checked]");
				}else{
					aryOrg = $("input[name='org']", $("#orgList"));
				}
				if (aryOrg.length == 0) {
					return;
				}
				var aryId = [];
				var aryName = [];
				var orgJson = [];
				aryOrg.each(function() {
					var data = $(this).val();
					var aryTmp = data.split("#");
					aryId.push(aryTmp[0]);
					aryName.push(aryTmp[1]);
					orgJson.push({
						id: aryTmp[0],
						name: aryTmp[1]
					});
				});
				var orgIds = aryId.join(",");
				var orgNames = aryName.join(",");

				var obj = {};
				obj.orgId = orgIds;
				obj.orgName = orgNames;
				obj.orgJson = orgJson;

				dialog.get("sucCall")(obj);
				dialog.close();
		}
		/*KILLDIALOG*/
		function dellAll() {
			$("#orgList").empty();
		};
		//清空
		function clearOrg() {
			var rtn={
				orgId: '',
				orgName: '',
				orgJson: []
			};
			dialog.get("sucCall")(rtn);
			dialog.close();
		}
		function selectMulti(obj) {
			if ($(obj).attr("checked") == "checked") {
				var data = $(obj).val();
				add(data);
			}
		};
		function del(obj) {
			var tr = $(obj).closest("tr");
			$(tr).remove();
		};
		function treeFresh() {
			loadTree(1);
			setTimeout(function() {
				curStatus = true;
				treeExpandAll(false, true);
			}, 100);
		}

	</script>
</head>
<body>
	<div id="defLayout">
	<div  position="left" style="float: left;width: 100% !important;height:95%;;">
		<!-- <div  position="left" title="组织树" style="float: left;width: 100% !important;height:95%;;"> -->
<%--			 <div style="width:100%;">--%>
<%--		        <select id="demensionId"  style="width:99.8% !important;">  --%>
<%--		              <c:forEach var="dem" items="${demensionList}">  --%>
<%--		              	<option  value="${dem.demId}" <c:if test="${dem.demId==1 }">selected='selected'</c:if> >${dem.demName}</option>  --%>
<%--		              </c:forEach>  --%>
<%--		        </select>--%>
<%--	         </div>--%>

			<!-- <div class="tree-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link reload" id="treeFresh" href="javascript:treeFlesh();">刷新</a>
					</div>
					<div class="group">
						<a class="link expand" id="treeExpandAll" href="javascript:treeExpand()">展开</a>
					</div>
					<div class="group">
						<a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)">收起</a>
					</div>
				</div>
			</div> -->
			<ul id="orgTree" class="ztree" style="overflow:auto;height:87%;"></ul>
		</div>
		<div position="center">
			<div class="l-layout-header">组织列表</div>
			<iframe id="orgFrame" name="orgFrame" height="100%" width="100%" frameborder="0" ></iframe>
		</div>
		<%-- <c:if test="${param.isSingle==false}"> --%>
	   <%-- <div position="right" title="<span><a onclick='javascript:dellAll();' class='link del'>清空 </a><input type='text' class='quick-find' placeholder='快速查询'/></span>" style="overflow: auto;height:95%;width:170px;">
  			<table width="145"   class="table-grid table-list"  cellpadding="1" cellspacing="1">
  				<tbody id="orgList">
  					<tr class="hidden"></tr>
  				</tbody>
			</table>
	  </div> --%>
		<%-- </c:if> --%>
	</div>
	 <div position="bottom"  class="bottom">
	 	<div class="fr">
	 	<a href="javascript:;" class="button empty"  onclick="clearOrg()"><!-- <span class="icon cancel" ></span> --><span class="cance" >清空</span></a>
	 	<a href='####' class='button cancel' onclick="dialog.close()"><!-- <span class="icon cancel"></span> --><span >取消</span></a>
		<a href='####' class='button determine' onclick="selectOrg()" style="margin-right:10px;"><!-- <span class="icon ok"></span> --><span>确定</span></a>
		</div>
	 </div>
	 	<textarea style="display: none;" id="orgTemplate">
		<tr id="org_#orgId">
	  			<td>
	  				<input type="hidden" name="org" value="#data"><span>#name</span>
	  			</td>
	  			<td style="width: 30px;" nowrap="nowrap"><a onclick="javascript:del(this);" class="link del" title="删除">&nbsp;</a> </td>
	  	</tr>
	</textarea>
</body>
</html>