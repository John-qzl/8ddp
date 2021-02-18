<%--
	desc:edit the 用户表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<f:sysparam paramname="fdfsserver" alias="fdfsserver"></f:sysparam>
<html>
<head>
	<title>编辑 用户表</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=sysUser"></script>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<f:sysUser name="UN_LOCKED" alias="UN_LOCKED"></f:sysUser>
	<f:sysUser name="LOCKED" alias="LOCKED"></f:sysUser>
    <f:sysUser name="DYNPWD_STATUS_BIND" alias="DYNPWD_STATUS_BIND"></f:sysUser>
	<f:sysUser name="DYNPWD_STATUS_UNBIND" alias="DYNPWD_STATUS_UNBIND"></f:sysUser>
	<f:sysUser name="DYNPWD_STATUS_OUT" alias="DYNPWD_STATUS_OUT"></f:sysUser>

	<f:sysUser name="SKILL_TITLE_1" alias="SKILL_TITLE_1"></f:sysUser>
	<f:sysUser name="SKILL_TITLE_2" alias="SKILL_TITLE_2"></f:sysUser>
	<f:sysUser name="SKILL_TITLE_3" alias="SKILL_TITLE_3"></f:sysUser>
	<f:sysUser name="SKILL_TITLE_4" alias="SKILL_TITLE_4"></f:sysUser>
	<f:sysUser name="SKILL_TITLE_5" alias="SKILL_TITLE_5"></f:sysUser>
	<f:sysUser name="SKILL_TITLE_6" alias="SKILL_TITLE_6"></f:sysUser>

	<f:sysparam paramname="uploadType" alias="uploadType"></f:sysparam>

	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/displaytag.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerWindow.js" ></script>
   <script type="text/javascript"  src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
   <script type="text/javascript"  src="${ctx}/jslib/ibms/oa/syssign/userSign.js"></script>
   <script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js"></script>
   <script type="text/javascript" src="${ctx}/jslib/handlebars/handlebars.min.js"></script>
   <script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/HtmlUploadDialog.js"></script>
	<f:link href="signal/imaList.css"></f:link>
	<script type="text/javascript">
	var uploadType = '${uploadType}';

	var orgTree;    //组织树
	var posTree;    //岗位树
	var rolTree;    //角色树

	var orgPosTree;    //组织岗位树

	var height;
	var expandDepth =2;

	var action="${action}";

	var navtab;


    $(function ()
    {



    	
    	setCheckBoxParentCilck();
    	//右键菜单,暂时去掉右键菜单
    	height=$('body').height();
    	navtab=$("#tabMyInfo").ligerTab({});
    	if(${tabid!=null}){
        	navtab.selectTabItem("${tabid}");
    	}
    	function showRequest(formData, jqForm, options)	{
			return true;
		}
    	function showResponse(responseText, statusText)  {
    		var self=this;
    		var obj=new com.ibms.form.ResultMessage(responseText);
    		if(obj.isSuccess()){//成功
    			$.ligerDialog.confirm( obj.getMessage()+",是否继续操作","提示信息",function(rtn){
    				if(rtn){
    					if(self.isReset==1){
    						window.location.reload(true);
    					}
    				}else {
    					window.location.href="${returnUrl}";
    				}
    			});

    	    }else{//失败
    	    	$.ligerDialog.error( obj.getMessage(),"出错了");
    	    }
    	};


    	if(${sysUser.userId==null}){
			valid(showRequest,showResponse,1);
		}else{
			valid(showRequest,showResponse);
		}
		$("a.save").click(function() {
			$('#sysUserForm').submit();
		});
		$("#orgPosAdd").click(function(){
			btnAddRow('orgPosTree');
		});
		$("#orgPosDel").click(function(){
			btnDelRow();
		});
		$("#posAdd").click(function(){
			btnAddRow('posTree');
		});
		$("#posDel").click(function(){
			btnDelRow();
		});

		$("#rolAdd").click(function(){
			btnAddRow('rolTree');
		});
		$("#rolDel").click(function(){
			btnDelRow();
		});
		$("#demensionId").change(function(){
			loadorgTree();
		});
		//组织刷新按钮
		$("#treeReFresh").click(function() {
			loadorgTree();
		});
         //组织展开按钮
		$("#treeExpand").click(function() {
			orgTree = $.fn.zTree.getZTreeObj("orgTree");
			var treeNodes = orgTree.transformToArray(orgTree.getNodes());
			for(var i=1;i<treeNodes.length;i++){
				if(treeNodes[i].children){
					orgTree.expandNode(treeNodes[i], true, false, false);
				}
			}
		});
		$("#treeCollapse").click(function() {
			orgTree.expandAll(false);
		});

		var isRight = ${isRight};

		if(!isRight){
			if("grade"==action){
	    		loadorgGradeTree();
	    	}else{
	    		loadorgTree();
	    	}
		}

	   loadrolTree();
   	   var orgIds="${orgIds}";

	   if( orgIds == undefined || orgIds == null || orgIds == "" || isRight){
	   }else{
		   //编辑页面才调用此方法
		   loadorgPosTree(orgIds);
	   }
    });

	//添加个人照片
	function picCallBack(array){
		if(!array && array!="") return;
		var fileId=array[0].fileId,
			fileName=array[0].fileName;

		var path= __ctx + "/oa/system/sysFile/getFileById.do?fileId=" + fileId;
		if(/\w+.(png|gif|jpg)/gi.test(fileName)){
			$("#photo").val("/oa/system/sysFile/getFileById.do?fileId=" + fileId);
			$("#personPic").attr("src",path);
			$(".person_pic_div").attr("overflow","block");
		}else{
			$.ligerDialog.warn("请选择*png,*gif,*jpg类型图片!");
		}
	};
	
	//添加电子签章回调
	//ie8适配原因弃用
/* 	function signModelCallBack(array){
		if(!array && array!="") return;
		var fileId=array[0].fileId,
			fileName=array[0].fileName;

		var path= __ctx + "/oa/system/sysFile/getFileById.do?fileId=" + fileId;
		if(/\w+.(png|gif|jpg)/gi.test(fileName)){
			$("#signModelNewPath").val("/oa/system/sysFile/getFileById.do?fileId=" + fileId);
			$("#signModelPath").attr("src",path);
			$(".signModelNewPath").attr("overflow","block");
		}else{
			$.ligerDialog.warn("请选择*png,*gif,*jpg类型图片!");
		}
	}; */

	function isFlash(){
		var isIE=!-[1,];
		if(isIE){
			try{
				var swf1=new ActiveXObject('ShonckwaveFlash.ShowckwaveFlash');
				return true;
			}catch(e){
				return false;
			}
		}else{
			try{
				var swf2=navigator.plugins['Shockwave Flash'];
				if(swf2== undefined){
					return false;
				}else{
					return true;
				}
			}catch(e){
				return false;
			}
		}
	}

	//上传照片
	function addPic(){
		if(window.applicationCache){
			HtmlUploadDialog({max:1,size:10,callback:picCallBack});
		}else if(isFlash()){
			DirectUploadDialog({isSingle:true,url:"/upload/flashFileUpload.do",callback:picCallBack});
		} else{
			FormUploadDialog({isSingle:true,url:"/upload/formFileUpload.do",filenum:1,callback:picCallBack});
		}
	};
	//删除照片
	function delPic(){
		$("#photo").val("");
		$("#personPic").attr("src","${ctx}/styles/images/default_image_male.jpg");
	};

	//上传电子签章
	//ie8适配原因弃用
/* 	function addSignModel(){
		if(window.applicationCache){
			HtmlUploadDialog({max:1,size:10,callback:signModelCallBack});
		}else if(isFlash()){
			DirectUploadDialog({isSingle:true,url:"/upload/flashFileUpload.do",callback:signModelCallBack});
		} else{
			FormUploadDialog({isSingle:true,url:"/upload/formFileUpload.do",filenum:1,callback:signModelCallBack});
		}
	}; */
	
	// 上传电子签章
	function importProductCategories(){
    	debugger;
		var h=window.top.document.documentElement.clientHeight;
		var w=window.top.document.documentElement.clientWidth;
		var url=__ctx+"/oa/system/sysUser/getUploadSignModelView.do?userId="+${userId};
		var modUserId=${userId};
		if(modUserId==""){
			$.ligerDialog.warn("您必须先保存用户才可以上传签章",'提示信息');
		}else{
			DialogUtil.open({
				height:h*0.4,
				width: w*0.4,
				title : '上传签章',
				url: url,
				isResize: true,
				//自定义参数?
				sucCall:function(rtn){
					window.location.href = window.location.href.getNewUrl();
				}
			});
		}

	}

	//生成组织树
	function loadorgTree(){
		if(action =='grade'){loadorgGradeTree();return;}

		var demId=$("#demensionId").val();
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
				url:__ctx+"/oa/system/sysOrg/getTreeData.do?demId="+demId,
				autoParam:["orgId","orgSupId"]
			},
			view: {
				selectedMulti: true
			},
			onRightClick: false,
			onClick:false,
			check: {
				enable: true,
				chkboxType: { "Y": "", "N": "" }
			},
			callback:{
				onClick: zTreeOnCheck,
				onCheck:orgPostTreeExpand,
				onAsyncSuccess: orgTreeOnAsyncSuccess
			}
		};
		orgTree=$.fn.zTree.init($("#orgTree"), setting);
	};

	function zTreeOnCheck(event, treeId, treeNode) {
		var target;
		if(treeId=="rolTree"){
			target = rolTree ;
			if(treeNode.isParent){
				var children=treeNode.children;
				if(children){
					for(var i=0;i<children.length;i++){
						target.checkNode(children[i], !treeNode.checked, false, true);
					}
				}
			}
		} else if(treeId=='orgTree'){
			target = orgTree ;
		} else if(treeId=='orgPosTree'){
			target = orgPosTree ;
		}else{
			target = posTree ;
		}
		target.checkNode(treeNode, '', false, true);
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
			var treeNodes = orgTree.transformToArray(orgTree.getNodes());
			if(treeNodes.length>0){
				treeNodes[0].nocheck = true;
				orgTree.updateNode(treeNodes[0]);
			}
	};

	function loadorgGradeTree(){
		var setting = {
				data: {
					key : {
						name: "orgName",
						title: "orgName"
					}
				},
				view : {
					selectedMulti : false
				},
				onRightClick: false,
				onClick:false,
				check: {
					enable: true,
					chkboxType: { "Y": "", "N": "" }
				},
				callback:{
					onClick: zTreeOnCheck,
					onCheck:orgPostTreeExpand
					}
			};
			var orgId = $("#orgAuth").val();
			if(!orgId) orgId =0;
			var url=__ctx + "/oa/system/grade/getOrgJsonByAuthOrgId.do?orgId="+orgId;
		   //一次性加载
		   $.post(url,function(result){
			   var zNodes=eval("(" +result +")");
			   orgTree=$.fn.zTree.init($("#orgTree"), setting,zNodes);
			   if(expandDepth!=0)
				{
					orgTree.expandAll(false);
					var nodes = orgTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						//nodes[0].nocheck = true;
						orgTree.updateNode(nodes[0]);
						for(var i=0;i<nodes.length;i++){
							orgTree.expandNode(nodes[i],true,false);
						}
					}
				}else {
					orgTree.expandAll(true);
					// justifyMargin(10);
				}
		   });
	};




	//勾选组织的时候展开组织岗位树
	function orgPostTreeExpand(){
		var setting = {
				data: {
					key : {
						name: "posName",
						title: "posName"
					},

					simpleData: {
						enable: true,
						idKey: "posId",
						pIdKey: "orgId",
						rootPId: -1
					}
				},

				view: {
					selectedMulti: true
				},
				onRightClick: false,
				onClick:false,
				check: {
					enable: true,
					chkboxType: { "Y": "", "N": "" }
				},
				callback:{
					onClick: zTreeOnCheck,
					onAsyncSuccess: zTreeOnAsyncSuccess,
					onRightClick: zTreeOnRightClick
				}
		};
		  //获取组织树的勾选节点
		  var treeObj = $.fn.zTree.getZTreeObj('orgTree');
	        var nodes = treeObj.getCheckedNodes(true);
	        var a=[];
	        for ( var key in nodes ){
	        	a.push(nodes[key].orgId);
	        }
	       var orgIds=a.join();
	       if(!orgIds) return;
		  var orgUrl=__ctx + "/oa/system/position/getOrgPosTreeData.do?orgIds="+orgIds;
		   //一次性加载
		   $.post(orgUrl,function(result){
			   orgPosTree=$.fn.zTree.init($("#orgPosTree"), setting,result);
			   orgPosTree.expandAll(true);
			   //去掉父节点勾选框,???
			   var treeObj = $.fn.zTree.getZTreeObj("orgPosTree");
               var nodes = treeObj.getNodesByParam("orgId", -1, null);//为啥总是为空
			   for(var key in nodes){
			   			nodes[key].nocheck = true;
			   			orgPosTree.updateNode(nodes[key]);
			   }
		   });
		   orgPostTree=$.fn.zTree.init($("#orgPostTree"), setting);
	};


	//生成组织岗位树
	  function loadorgPosTree(orgIds) {
		  var setting = {
					data: {
						key : {
							name: "posName",
							title: "posName"
						},

						simpleData: {
							enable: true,
							idKey: "posId",
							pIdKey: "orgId",
							rootPId: -1
						}
					},

					view: {
						selectedMulti: true
					},
					onRightClick: false,
					onClick:false,
					check: {
						enable: true,
						chkboxType: { "Y": "s", "N": "ps" }
					},
					callback:{
						onClick: zTreeOnCheck,
						onAsyncSuccess: zTreeOnAsyncSuccess,
						onRightClick: zTreeOnRightClick
					}
			};

		    var orgUrl=__ctx + "/oa/system/position/getOrgPosTreeData.do?orgIds="+orgIds;
			   //一次性加载
			   $.post(orgUrl,function(result){
				   orgPosTree=$.fn.zTree.init($("#orgPosTree"), setting,result);
				   orgPosTree.expandAll(true);
			   });
			      orgPosTree = $.fn.zTree.init($("#orgPosTree"), setting);
	};

	//生成岗位树
	  function loadposTree() {
		  var setting = {
					data: {
						key : {
							name: "posName",
							title: "posName"
						},

						simpleData: {
							enable: true,
							idKey: "posId",
							pIdKey: "parentId",
							rootPId: -1
						}
					},
					async: {
						enable: true,
						url:__ctx+"/oa/system/position/getChildTreeData.do",
						autoParam:["posId","parentId"],
						dataFilter: filter
					},
					view: {
						selectedMulti: true
					},
					onRightClick: false,
					onClick:false,
					check: {
						enable: true,
						chkboxType: { "Y": "", "N": "" }
					},
					callback:{
						onClick: zTreeOnCheck,
						onDblClick: posTreeOnDblClick,
						onAsyncSuccess: zTreeOnAsyncSuccess
					}
			};
		    posTree = $.fn.zTree.init($("#posTree"), setting);
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
		var treeNodes = posTree.transformToArray(posTree.getNodes());
		if(treeNodes.length>0){
			//显示勾选框
			treeNodes[0].nocheck = true;
			posTree.updateNode(treeNodes[0]);
		}
	};

	//过滤节点,默认为父节点,以改变图标
	function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				if(!childNodes[i].isParent){
					alert(childNodes[i].posName);
					childNodes[i].isParent = true;
				}
			}
			return childNodes;
	};


	 //生成角色树
	  function loadrolTree() {
		var setting = {
			data: {
				key : {
					name: "roleName",
					title: "roleName"
				},

				simpleData: {
					enable: true,
					idKey: "roleId",
					pIdKey: "pId",
					rootPId: null
				}
			},
			view: {
				selectedMulti: true
			},
			onRightClick: false,
			onClick:false,
			check: {
				enable: true,
				chkboxType: { "Y": "ps", "N": "ps" }
			},
			callback:{
				onClick: zTreeOnCheck,
				onDblClick: rolTreeOnDblClick}
		   };
			if(action == 'grade'){
				var url="${ctx}/oa/system/sysRole/getGradeTreeData.do";
			}
			else{
				var url="${ctx}/oa/system/sysRole/getTreeData.do";
			}

			$.post(url,function(result){
				rolTree=$.fn.zTree.init($("#rolTree"), setting,result);

			});
	};


	 function btnDelRow() {
		 var $aryId = $("input[type='checkbox'][class='pk']:checked");
		 var len=$aryId.length;
		 if(len==0){
			 $.ligerDialog.warn("你还没选择任何记录!");
			 return;
		 }
		 else{
			 $aryId.each(function(i){
					var obj=$(this);
					delrow(obj.val());
			 });
		 }
	 };

	 function delrow(id)//删除行,用于删除暂时选择的行
	 {
		 $("#"+id).remove();
	 };




	//树按添加按钮
	function btnAddRow(treeName) {
		var treeObj = $.fn.zTree.getZTreeObj(treeName);
        var nodes = treeObj.getCheckedNodes(true);
        if(nodes==null||nodes=="")
        {
        	$.ligerDialog.warn("你还没选择任何节点!");
			return;
        }
        if(treeName.indexOf("orgPos")!=-1) {
	        $.each(nodes,function(i,treeNode){
        		orgPosAddHtml(treeNode);
			});
	    }
	    else if(treeName.indexOf("pos")!=-1){
		     $.each(nodes,function(i,treeNode){
			 	  posAddHtml(treeNode);
		     });
	    }
	    else if(treeName.indexOf("rol")!=-1){
	    	 $.each(nodes,function(i,treeNode){
	    		 //三元角色只允许三元用户担任
				  if(treeNode.roleId>0){
					  rolAddHtml(treeNode);
				  }
	    	 });
	    }
    };


	 function orgPosAddHtml(treeNode){

		 if(treeNode.orgName==null) return;//去掉父节点
		 //添加过的不再添加
		 var obj=$("#" +treeNode.posId);
         if(obj.length>0) return;
		 //公司名称
		 if(typeof treeNode.companyId =="undefined" || treeNode.companyId==null){
			 treeNode.companyId = 0;
		 }
		 if(typeof treeNode.company =="undefined" || treeNode.company==null){
			 treeNode.company = '';
		 }
		    //用jquery获取模板
		    var tpl = $("#orgPosAddHtml-template").html();

		    var content = {treeNode:treeNode};
		    //预编译模板
		    var template = Handlebars.compile(tpl);
		    //匹配json内容
		    var html = template(content);
		    //输入模板
		 $("#orgItem").append(html);

	 };

	//岗位树左键双击
	 function posTreeOnDblClick(event, treeId, treeNode){
		 posAddHtml(treeNode);

	 };

	 function posAddHtml(treeNode){
		 if(treeNode.parentId==-1) return;
		 var obj=$("#" +treeNode.posId);
		 if(obj.length>0) return;
		 //用jquery获取模板
		 var tpl = $("#posAddHtml-template").html();
		 //json数据
		 var content = {treeNode:treeNode};
		 //预编译模板
		 var template = Handlebars.compile(tpl);
		 //匹配json内容
		 var html = template(content);
		 //输入模板
		 $("#posItem").append(html);

	 };
	//角色树左键双击
	 function rolTreeOnDblClick(event, treeId, treeNode){
		 rolAddHtml(treeNode);
	 };

	 function rolAddHtml(treeNode){
		 var obj=$("#" +treeNode.roleId);
		 if(obj.length>0) return;
		 //用jquery获取模板
		 var tpl = $("#rolAddHtml-template").html();
		 var content = {treeNode:treeNode};
		 //预编译模板
		 var template = Handlebars.compile(tpl);
		 //匹配json内容
		 var html = template(content);
		 //输入模板
		 $("#rolItem").append(html);

	 };
	//右键菜单
	 function zTreeOnRightClick(e, treeId, treeNode) {
		// alert(treeNode.orgId);
			if (treeNode.orgId=="-1") {
				orgPostTree.cancelSelectedNode();//取消节点选中状态
				menu_root.show({
					top : e.pageY,
					left : e.pageX
				});
			} else  {
				menu.show({
					top : e.pageY,
					left : e.pageX
				});
			}
		};

	//右键菜单
		function getMenu() {
			menu = $.ligerMenu({
				top : 100,
				left : 100,
				width : 100,
				items:<f:menu>
					[ {
						text : '删除',
						click : 'delNode',
						alias:'delOrg'
					}]
					</f:menu>
			});

			menu_root = $.ligerMenu({
				top : 100,
				left : 100,
				width : 100,
				items : [ {
					text : '增加',
					click : addNode
				}]
			});
		};

		//新增节点
		function addNode() {
			var treeNode=getSelectNode();
			var orgId = treeNode.posId;
			//var demId = treeNode.demId;
			var url = __ctx + "/oa/system/position/edit.do?orgId="+ orgId;
			var url = "edit.do?orgId=" + orgId + "&demId=" + demId + "&action=add";
			$("#viewFrame").attr("src", url);
		};
         //删除节点
		function delNode() {
			var treeNode=getSelectNode();
			var callback = function(rtn) {
				if (!rtn) return;
				var params = "orgId=" + treeNode.orgId;
				$.post("orgdel.do", params, function() {
					orgTree.removeNode(treeNode);
				});
			};
			$.ligerDialog.confirm("确认要删除此组织吗，其下组织也将被删除？", '提示信息', callback);

		};

		function showUserDlg(){
			var superior=$("#superior").val();
			var superiorId=$("#superiorId").val();
			var topOrgId= "${param.topOrgId}";
			var scope;
			if(topOrgId!=null && topOrgId!=""){
				var script="return " + topOrgId +";";
				scope="{type:\"script\",value:\""+script+"\"}";
			}
			else{
				scope="{type:\"system\",value:\"all\"}";
			}
			var script="return "+topOrgId;
			 UserDialog({
 	        	selectUserIds:superiorId,
 	        	selectUserNames:superior,
 	        	scope:scope,
 	        	callback:function(userIds,userNames){
 		        	$('#superior').val(userNames);
 		        	$('#superiorId').val(userIds);
 		        }
 	        });
		};
	</script>
	<script id="orgPosAddHtml-template" type="text/x-handlebars-template" >
    	<tr id="{{treeNode.posId}}" style="cursor: pointer;">
			<td style="text-align: center;">
				{{treeNode.company}}<input type="hidden" name="companyId" value="{{treeNode.companyId}}" />
			</td>
			<td style="text-align: center;">
				{{treeNode.orgName}}<input type="hidden" name="orgId" value="{{treeNode.orgId}}" />
			</td>
			<td style="text-align: center;">
				{{treeNode.posName}}<input type="hidden" name="posId" value="{{treeNode.posId}}" />
			</td>
			<td style="text-align: center;">
				<input type="radio" name="posIdPrimary" value="{{treeNode.posId}}" />
			</td>
			<td style="text-align: center;">
				<input type="checkbox" name="posIdCharge" value="{{treeNode.posId}}" />
			</td>
			<td style="text-align: center;">
				<a href="####" onclick="delrow('{{treeNode.posId}}')" class="link del">移除</a>
			</td>
		</tr>
    </script>
    <script id="rolAddHtml-template"  type="text/x-handlebars-template" >
		<tr id="{{treeNode.roleId}}" style="cursor: pointer;">
			<td style="text-align: center;">
				{{treeNode.roleName}}<input type="hidden"  name="roleId" value="{{treeNode.roleId}}" />
			</td>
			<td style="text-align: center;">
				<a href="####" onclick="delrow('{{treeNode.roleId}}')" class="link del">移除</a>
			</td>
		</tr>
    </script>
    <script id="posAddHtml-template" type="text/x-handlebars-template" >
		<tr id="{{treeNode.posId}}" style="cursor: pointer;">
			<td style="text-align: center;">
				{{treeNode.posName}}<input type="hidden"  name="posId" value="{{treeNode.posId}}" />
			</td>
			<td style="text-align: center;">
				<input type='radio' name="posIdPrimary" value="{{treeNode.posId}}" />
			</td>
			<td style="text-align: center;">
				<a href="####" onclick="delrow('{{treeNode.posId}}')" class="link del">移除</a>
			</td>
		</tr>
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">
				<c:if test="${sysUser.userId==null }">添加用户信息</c:if>
				<c:if test="${sysUser.userId!=null }">编辑【${sysUser.fullname}】用户信息</c:if>
				</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>

					<div class="group"><a class="link back" href="${returnUrl}">返回</a></div>
				</div>
			</div>
		</div>
	   <form id="sysUserForm" method="post" action="save.do?selectedOrgId=${param.selectedOrgId}">

            <div  id="tabMyInfo" class="panel-nav" style="overflow:hidden; position:relative;">
            	<c:if test="${isRight}">
            		<div title="基本信息" tabid="userdetail">
		           		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td rowspan="<c:if test="${not empty sysUser.userId}">15</c:if><c:if test="${empty sysUser.userId}">14</c:if>" align="center" width="26%">
								<div class="person_pic_div">
									<p><img id="personPic" src="${ctx}/${pictureLoad}" style="height: 378px; width:270px" alt="个人相片" /></p>
								</div>
								</td>
								<th width="18%">帐   号: </th>
								<td >
									<c:if test="${bySelf==1}"><input type="hidden" name="bySelf" value="1"></c:if>
									<div>${sysUser.username}</div>
									<input type="hidden" id="username" name="username" value="${sysUser.username}" />
								</td>
							</tr>

							<tr>
							    <th>用户姓名:</th>
								<td >
									<div>${sysUser.fullname}</div>
								</td>
							</tr>
							<tr>
								<th>用户性别: </th>
								<td>
									<div>
										<c:if test="${sysUser.sex==1}">男</c:if>
										<c:if test="${sysUser.sex==0}">女</c:if>
									</div>
								</td>
							</tr>
							<tr>
								<th>入职时间: </th>
								<td>
									<div><fmt:formatDate value='${sysUser.accessionTime}' pattern='yyyy-MM-dd'/></div>
								</td>
							</tr>
							<tr>
								<th>是否锁定: </th>
								<td>
									<div>
										<c:if test="${sysUser.delFlag==0}">未锁定</c:if>
										<c:if test="${sysUser.delFlag==1}">已锁定</c:if>
									</div>
								</td>
							</tr>
							<tr>
							   <th>当前状态: </th>
								<td>
									<div>
										<c:if test="${sysUser.status==1}">激活</c:if>
										<c:if test="${sysUser.status==0}">禁用</c:if>
										<c:if test="${sysUser.status==-1}">删除</c:if>
									</div>
								</td>
							</tr>
							<tr>
							   <th>邮箱地址: </th>
							   <td >
							   		<div>${sysUser.email}</div>
							   </td>
							</tr>
							<tr>
						    	<th>传  真:</th>
								<td >
									<div>${sysUser.fax}</div>
								</td>
							</tr>
							<tr>
								<th>手   机: </th>
								<td >
									<div>${sysUser.mobile}</div>
								</td>
							</tr>

							<tr>
							    <th>电   话: </th>
								<td >
									<div>${sysUser.phone}</div>
								</td>
							</tr>
							<c:if test="${isShowSecurity}">
								<tr>
								    <th>密   级: </th>
									<td >
										<c:forEach items="${securityUserMap}" var="securityUserMap" >
											<c:if test="${securityUserMap.key eq sysUser.security}">${securityUserMap.value}</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:if>
							<tr>
						   		<th>技术职称: </th>
								<td>
									<div>
										<c:if test="${sysUser.skilltitle=='zlgcs'}">助理工程师</c:if>
										<c:if test="${sysUser.skilltitle=='gcs'}">工程师</c:if>
										<c:if test="${sysUser.skilltitle=='gjgcsf'}">高级工程师（副高）</c:if>
										<c:if test="${sysUser.skilltitle=='gjgcsz'}">高级工程师（正高）</c:if>
										<c:if test="${sysUser.skilltitle=='yjy'}">研究员</c:if>
										<c:if test="${sysUser.skilltitle=='zlyjy'}">助理研究员</c:if>
									</div>
								</td>
							</tr>

							<tr>
							    <th>专 业: </th>
								<td >
									<div>${sysUser.major}</div>
								</td>
							</tr>

						</table>
						<input type="hidden" name="userId" value="${sysUser.userId}" />
						<input type="hidden" id="photo" name="photo" value="${sysUser.photo}" />
		           </div>
            	</c:if>
            	<c:if test="${!isRight}">
            		<div title="基本信息" tabid="userdetail">
		           		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td rowspan="<c:if test="${not empty sysUser.userId}">15</c:if><c:if test="${empty sysUser.userId}">14</c:if>" align="center" width="26%">
								<div style="float:top !important;background: none;height: 24px;padding:0px 6px 0px 112px;">
									<a class="link uploadPhoto" href="javascript:;" onclick="addPic();">上传照片</a>
									<a class="link del" href="javascript:;" onclick="delPic();">删除照片</a>
								</div>
								<div class="person_pic_div">
									<p><img id="personPic" src="${ctx}/${pictureLoad}" style="height: 378px; width:270px" alt="个人相片" /></p>
								</div>
								</td>
								<th width="18%">帐   号: <span class="required red">*</span></th>
								<td ><c:if test="${bySelf==1}"><input type="hidden" name="bySelf" value="1"></c:if><input type="text" <c:if test="${bySelf==1}">disabled="disabled"</c:if> id="username" name="username" value="${sysUser.username}" style="width:240px !important" class="inputText"/></td>
							</tr>
								<!-- 密码字段会传递本地已保存的用户密码,导致数据库明文密码BUG -->
				     	<%--<tr style="<c:if test="${not empty sysUser.userId}">display:none</c:if>">--%>
							<tr>
								<th>密   码: <span class="required red">*</span></th>
								<td><input type="word" id="password" name="password"  style="width:240px !important" class="inputText"/></td>
							</tr>

							<tr>
							    <th>用户姓名: <span class="required red">*</span></th>
								<td ><input type="text" id="fullname" name="fullname" value="${sysUser.fullname}" style="width:240px !important" class="inputText" /></td>
							</tr>
							<tr>
								<th>用户性别: </th>
								<td>
								<select name="sex" class="select" style="width:240px !important">
									<option value="1" <c:if test="${sysUser.sex==1}">selected</c:if> >男</option>
									<option value="0" <c:if test="${sysUser.sex==0}">selected</c:if> >女</option>
								</select>
								</td>
							</tr>
							<tr>
								<th>入职时间: </th>
								<td>
									<input style="width:240px !important" type="text" id="accessionTime" name="accessionTime" value="<fmt:formatDate value='${sysUser.accessionTime}' pattern='yyyy-MM-dd'/>" class="inputText date" validate="{date:true}" />
								</td>
							</tr>
							<tr>
								<th>是否锁定: </th>
								<td >
									<select name="lockState" class="select" style="width:240px !important" <c:if test="${bySelf==1}">disabled="disabled"</c:if>>
										<option value="${UN_LOCKED}" <c:if test="${sysUser.lockState==0}">selected</c:if> >未锁定</option>
										<option value="${LOCKED}" <c:if test="${sysUser.lockState==1}">selected</c:if> >已锁定</option>
									</select>
								</td>
							</tr>
							<tr>
							   <th>当前状态: </th>
								<td>
									<select name="status"  class="select" style="width:240px !important" <c:if test="${bySelf==1}">disabled="disabled"</c:if>>
										<option value="${DYNPWD_STATUS_BIND}" <c:if test="${sysUser.status==1}">selected</c:if> >激活</option>
										<option value="${DYNPWD_STATUS_UNBIND}" <c:if test="${sysUser.status==0}">selected</c:if> >禁用</option>
										<option value="${DYNPWD_STATUS_OUT}" <c:if test="${sysUser.status==-1}">selected</c:if>>删除</option>
									</select>
								</td>
							</tr>
							<tr>
							   <th>邮箱地址: </th>
							   <td ><input type="text" id="email" name="email" value="${sysUser.email}" style="width:240px !important" class="inputText"/></td>
							</tr>
							<tr>
						    	<th>传  真:</th>
								<td ><input type="text" id="weixinid" name="fax" value="${sysUser.fax}"  style="width:240px !important" class="inputText"/></td>
							</tr>
							<tr>
								<th>手   机: </th>
								<td ><input type="text" id="mobile" name="mobile" value="${sysUser.mobile}" style="width:240px !important" class="inputText"/></td>
							</tr>

							<tr>
							    <th>电   话: </th>
								<td ><input type="text" id="phone" name="phone" value="${sysUser.phone}"  style="width:240px !important" class="inputText"/></td>
							</tr>
							<c:if test="${isShowSecurity}">
								<tr>
								    <th>密   级: </th>
									<td >
										<select id="security" name="security" class="select" style="width:240px !important">
											<c:forEach items="${securityUserMap}" var="securityUserMap" >
												<option value="${securityUserMap.key}" <c:if test="${securityUserMap.key eq sysUser.security}">
													selected="selected"</c:if> >
													${securityUserMap.value}
												</option>
											</c:forEach>
										 </select>
									</td>
								</tr>
							</c:if>
							<tr>
						   		<th>技术职称: </th>
								<td>
									<select name="skilltitle"  class="select" style="width:240px !important" <c:if test="${bySelf==1}">disabled="disabled"</c:if>>
										<option value="${SKILL_TITLE_1}" <c:if test="${sysUser.skilltitle=='zlgcs'}">selected</c:if> >助理工程师</option>
										<option value="${SKILL_TITLE_2}" <c:if test="${sysUser.skilltitle=='gcs'}">selected</c:if> >工程师</option>
										<option value="${SKILL_TITLE_3}" <c:if test="${sysUser.skilltitle=='gjgcsf'}">selected</c:if>>高级工程师（副高）</option>
										<option value="${SKILL_TITLE_4}" <c:if test="${sysUser.skilltitle=='gjgcsz'}">selected</c:if> >高级工程师（正高）</option>
										<option value="${SKILL_TITLE_5}" <c:if test="${sysUser.skilltitle=='yjy'}">selected</c:if> >研究员</option>
										<option value="${SKILL_TITLE_6}" <c:if test="${sysUser.skilltitle=='zlyjy'}">selected</c:if>>助理研究员</option>
									</select>
								</td>
							</tr>

							<tr>
							    <th>专 业: </th>
								<td ><input type="text" id="major" name="major" value="${sysUser.major}"  style="width:240px !important" class="inputText"/></td>
							</tr>

						</table>
						<input type="hidden" name="userId" value="${sysUser.userId}" />
						<input type="hidden" id="photo" name="photo" value="${sysUser.photo}" />
		           </div>
            	</c:if>
	           <%--不是修改本人信息则--%>
	           <c:if test="${bySelf!=1}">
	           		<c:if test="${!isRight}">
	           			<div title="组织岗位选择" tabid="orgdetail" icon="${ctx}/styles/default/images/icon/home.png" >
				            <table  style="margin-top:-5px;border-top: 6px solid #A0BDBB;" align="center"  cellpadding="0" cellspacing="0" class="table-grid table-list">
							    <tr>
							        <td width="28%" valign="top"  style="padding-left:2px !important;">
										<div class="tbar-title">
											<span class="tbar-label">所有组织</span>
								        </div>
									  	<div class="panel-body" style="height:520px;overflow-y:auto;">
											<div style="width: 100%;">
												<c:choose>
												 	<c:when test="${action=='global' }">
														<select id="demensionId" style="width: 50% !important;">
															<c:forEach var="dem" items="${demensionList}">
																<option value="${dem.demId}" <c:if test="${dem.demId==1}">selected="selected"</c:if> >${dem.demName}</option>
															</c:forEach>
														</select>
												 	</c:when>
												 	<c:otherwise>
												 	<select id="orgAuth" style="width:50% !important;" onchange="javascript:loadorgGradeTree();">
											              <c:forEach var="orgAuth" items="${orgAuthList}">
											         			<option value="${orgAuth.orgId}" dimId="${orgAuth.dimId}" orgPerms="${orgAuth.orgPerms}" <c:if test="${orgAuth.dimId==1}">selected="selected"</c:if>>${orgAuth.orgName}—[${orgAuth.dimName}]</option>
											        	  </c:forEach>
							       					 </select>
												 	</c:otherwise>
												 </c:choose>
											</div>
											<div class="tree-toolbar" id="pToolbar">
												<div class="toolBar"
													style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
													<div class="group">
														<a class="link reload" id="treeReFresh">刷新</a>
													</div>
													<div class="l-bar-separator"></div>
													<div class="group">
														<a class="link expand" id="treeExpand">展开</a>
													</div>
													<div class="l-bar-separator"></div>
													<div class="group">
														<a class="link collapse" id="treeCollapse">收起</a>
													</div>
												</div>
						                   	</div>
							            	<ul id="orgTree" class="ztree" style="width:230px;" >
							           		</ul>
										</div>
									</td>
									<%--组织下的岗位列表 --%>
									<td width="28%" valign="top" style="padding-left:2px !important;">
								        <div class="tbar-title">
											<span class="tbar-label">所有岗位</span>
										</div>
										<div class="panel-body" style="height:520px;">
											<div id="orgPosTree" class="ztree" style="width:200px;height:98%;" >
								          	</div>
										</div>
									</td>
									<%--组织下的岗位列表 end--%>
									<td width="3%" valign="middle"  style="padding-left:2px !important;">
										<input type="button" id="orgPosAdd" value="添加>>" style="width:100px" />
										<br/>
										<br/>
										<br/>
									</td>
								    <td valign="top" style="padding-left:2px !important;">
							            <div class="tbar-title">
												<span class="tbar-label">已选组织</span>
								         </div>
										<div style="overflow-y:auto;">
										      <table id="orgItem" class="table-grid table-list"  cellpadding="1" cellspacing="1">
											   		<thead>

											   			<th style="text-align:center !important;">公司名称</th>
											   			<th style="text-align:center !important;">组织名称</th>
											   			<th style="text-align:center !important;">岗位名称</th>
											    		<th style="text-align:center !important;">是否主岗位</th>
											    		<th style="text-align:center !important;">主要负责人</th>

											    		<th style="text-align:center !important;">操作</th>
											    	</thead>
											    	<c:forEach items="${userPosList}" var="orgItem">
											    		<tr trName="${orgItem.orgName}"  id="${orgItem.posId}" style='cursor:pointer'>

												    		<td style="text-align: center;">
											    				${orgItem.company}<input type="hidden" name="companyId" value="${orgItem.companyId}">
											    			</td>

												    		<td style="text-align: center;">
											    				${orgItem.orgName}<input type="hidden" name="orgId" value="${orgItem.orgId}">

											    			<td style="text-align: center;">
											    				${orgItem.posName}<input type="hidden" name="posId" value="${orgItem.posId}">
											    			</td>

											    			<td style="text-align: center;">
											    			 <input type="radio" name="posIdPrimary" value="${orgItem.posId}" <c:if test='${orgItem.isPrimary==1}'>checked</c:if> />
											    			</td>

											    			<td style="text-align: center;">
											    			 	<input type="checkbox" name="posIdCharge" value="${orgItem.posId}"  <c:if test='${orgItem.isCharge==1}'>checked</c:if>>
											    			</td>

											    			<td style="text-align: center;">
											    			 <a href="javascript:;" onclick="delrow('${orgItem.posId}')" class="link del">移除</a>
											    			</td>
											    		</tr>
											    	</c:forEach>
										   	 </table>
										</div>
						            </td>
					            </tr>
						 	</table>
			        	</div>
	           		</c:if>

		       		<c:if test="${!isSystem}">
			        	<div  title="角色选择" tabid="roldetail">
					        <table style="margin-top:-5px;border-top: 6px solid #A0BDBB;" align="center"  cellpadding="0" cellspacing="0" class="table-grid">
							   <tr>
							       <td width="28%" valign="top" style="padding-left:2px !important;">
								        <div class="tbar-title">
											 <span class="tbar-label">所有角色</span>
										</div>
										<div class="panel-body" style="height:520px;overflow-y:auto;">
								    	    <div id="rolTree" class="ztree" style="width:200px;height:97%;" >
								            </div>
								        </div>
									</td>
									<td width="3%" valign="middle"  style="padding-left:2px !important;">
										<input type="button" id="rolAdd" value="添加>>" style="width:150px"/>
										<br/>
										<br/>
										<br/>
									</td>
								    <td valign="top" style="padding-left:2px !important;">
									   <div class="tbar-title">
											 <span class="tbar-label">已选角色</span>
										</div>
										<div style="overflow-y:auto;">
										 	 <table id="rolItem" class="table-grid"  cellpadding="1" cellspacing="1">
										   		<thead>
										   			<th style="text-align:center !important;">角色名称</th>
										    		<th style="text-align:center !important;">操作</th>
										    	</thead>
										    	<c:forEach items="${roleList}" var="rolItem">
										    		<tr trName="${rolItem.roleName}" id="${rolItem.roleId}" style="cursor:pointer">
											    		<td style="text-align: center;">
										    				${rolItem.roleName}<input type="hidden" name="roleId" value="${rolItem.roleId}">
										    			</td>
										    			<td style="text-align: center;">
										    			 	<a href="javascript:;" onclick="delrow('${rolItem.roleId}')" class="link del">移除</a>
										    			</td>
										    		</tr>
										    	</c:forEach>
										   	 </table>
										</div>
						            </td>
					            </tr>
							 </table>
						</div>
		        	</c:if>
<%--					<c:if test="${userId != 0 }">--%>
<%--						<c:if test="${!isSystem&&!isRight}">--%>
<%--							<div title="所属组织角色">--%>
<%--							    <table   style="margin-top:-5px;border-top: 6px solid #A0BDBB;" align="center"  cellpadding="0" cellspacing="0" class="table-grid">--%>
<%--								    <thead>--%>
<%--										<th style="text-align:center !important;">组织</th>--%>
<%--										<th style="text-align:center !important;">角色</th>--%>
<%--									</thead>--%>
<%--									<c:forEach items="${sysOrgRoles}" var="sysOrgRole">--%>
<%--										<tr>--%>
<%--											<td style="text-align: center;">--%>
<%--												${sysOrgRole.key.orgName}--%>
<%--											</td>--%>
<%--											<td style="text-align: center;">--%>
<%--												<c:forEach items="${sysOrgRole.value}" var="sysRole">--%>
<%--													${sysRole.roleName}--%>
<%--												</c:forEach>--%>
<%--											</td>--%>
<%--										</tr>--%>
<%--									</c:forEach>--%>
<%--							  	</table>--%>
<%--							 </div>--%>
<%--						</c:if>--%>
<%--				  	</c:if>--%>
<%--				  	<c:if test="${!isSystem&&!isRight}">--%>
<%--				  		<div  title="上级（领导）配置" tabid="superior">--%>
<%--						 	<c:set var="ids" value=""></c:set>--%>
<%--						 	<c:set var="names" value=""></c:set>--%>
<%--						 	<c:if test="${!empty userUnders }">--%>
<%--						 		<c:forEach items="${userUnders}" var="user" varStatus="status">--%>
<%--						 			<c:choose>--%>
<%--						 				<c:when test="${!status.last}">--%>
<%--						 					<c:set var="ids" value='${ids }${user.userId  },'></c:set>--%>
<%--						 					<c:set var="names" value='${names}${user.fullname },'></c:set>--%>
<%--						 				</c:when>--%>
<%--						 				<c:otherwise>--%>
<%--						 					<c:set var="ids" value='${ids }${user.userId  }'></c:set>--%>
<%--						 					<c:set var="names" value='${names}${user.fullname }'></c:set>--%>
<%--						 				</c:otherwise>--%>
<%--						 			</c:choose>--%>
<%--						 		</c:forEach>--%>
<%--						 	</c:if>--%>
<%--						 	 <table id="superItem" class="table-grid table-list"  cellpadding="1" cellspacing="1">--%>
<%--						   		<tr>--%>
<%--						   			<th style="text-align:center !important;width:200px">上级（领导）</th>--%>
<%--						   			<td >--%>
<%--						   				<input type="text" name="superior" size="80" id="superior" value="${names}">--%>
<%--						   				<input type="hidden" name="superiorId" id="superiorId" value="${ids}">--%>
<%--						   				<a href='##' class='button' onclick="showUserDlg()"><span>...</span></a>--%>
<%--						   			</td>--%>
<%--						   		</tr>--%>
<%--						   	</table>--%>
<%--						 </div>--%>
<%--				  	</c:if>--%>
	      		</c:if>
	      		<!-- 因ie8不适配,舍弃 -->

 	      			<div title="签章模型" tabid="signmodel">
						<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
							
							
							<!-- 从左侧树的导入产品扒过来的上传按钮 -->
							<div class="group"><a class="link import" id="serverExport" href="javascript:importProductCategories()" >上传图片<span></span></a></div> 
									
									
									<!-- <div style="float:top !important;background: none;height: 24px;padding:20px 0px 20px 150px;">
										<a class="link uploadPhoto" href="javascript:;" onclick="addSignModel();">上传电子签章</a>
									</div> -->
									<div class="person_pic_div">
										<p><img height="400" width="700" id="signModelPath" src="${ctx}/${signModelPath}"  alt="电子签章" /></p>
									</div>
								</td>
							</tr>
						</table>
						<input type="hidden" id="signModelNewPath" name="signModelNewPath" value="${signModelNewPath}" />
					</div>

								
<%-- 								<c:forEach items="${signModelList}" var="item">
									<a class="enterprise fl" href="javascript:editSign('${item.id}');">
										<div class="enterprise-img fl"> 
											<label for="${item.id}">设置默认
											<c:if test="${item.isDefault==1}">
												<input id="${item.id}"
												name="defaultSign" type="checkbox" value="${item.id}"
												onclick="setDefaultSign(this,'${userId}')" checked="checked"/>
											</c:if>
											 <c:if test="${item.isDefault!=1}">
												<input id="${item.id}"
												name="defaultSign" type="checkbox" value="${item.id}"
												onclick="setDefaultSign(this,'${userId}')"/> 
										
											</c:if>
											</label> --%>

 											
										<!-- </div>  --> 
	<%--											<span class="fuwu fl">印章名称：${item.name}</span> <span--%>
<%--										class="fuwu fl">印章描述：${item.desc}</span>--%>
<%--									</a>--%>
<%--								</c:forEach>--%>
<%-- 								<a class="enterprise fl" href="javascript:addSign('${userId}');">
									<li class="zc_upLoadSpase zc_right01"> --%>
<!-- 										<div class="upImg zc_auto">
											<input type="file" id="signImg" name="signImg" onchange="setFileName(this.value)"  />
										</div> -->
 									<!-- </li> -->
<%-- 								</a>
							</div>
						</div>
					</div>
	      		</c:if> --%>
	     	</div>
	 	</form>
	</div>
</body>
</html>
