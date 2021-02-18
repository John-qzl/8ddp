<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<%
    String basePath = request.getContextPath();
	String requestURI = (String) request.getAttribute("requestURI");
	//登录成功后，需要把该用户显示至在线用户
	//AppUtil.addOnlineUser(request.getSession().getId(), ContextUtil.getCurrentUser());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="msthemecompatible" content="no">
<f:sysparam paramname="CUR_JSON_USER" alias="CUR_JSON_USER"></f:sysparam>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/resources/css/ext-all-notheme.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/resources/css/ext-patch.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/ux/css/Portal.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/ux/css/Ext.ux.UploadDialog.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/ux/css/ux-all.css" />

<script type="text/javascript">  
	var __ctxPath ="<%=basePath%>";  
	var photoid;
	var originalphotoid;
	var signphotoid;
	var originalsignphotoid;
</script>
<script type="text/javascript">
	var __ctx="<%=request.getContextPath()%>";
	var __jsessionId="<%=session.getId()%>";
	var requestURIpath="<%=requestURI%>";
	var curUserInfo = "${CUR_JSON_USER}";
</script>
<!-- desktop样式 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/jslib/ext3.4/desktop/css/desktop.css" />
<!-- Ext 核心JS -->

<!-- Ext 核心JS -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ext-all.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ext-basex.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/jit.js"></script>

<!--使用iframe加载的依赖JS  -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/miframe-debug.js"></script>

<!-- CK控件JS -->
<script type="text/javascript" src="<%=basePath%>/jslib/ckeditor/ckeditor_source.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/CKEditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/Ext.ux.IconCombob.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/CheckTreePanel.js"></script>


<!-- 意见编辑器 -->
<script type="text/javascript" src="<%=basePath%>/js/core/ux/CommentEditor.js"></script>

<!-- 附件上传对话框 -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/UploadDialog.js"></script>
<!-- 附件明细JS,多处用到 -->
<script type="text/javascript" src="<%=basePath%>/js/system/FileAttachDetail.js"></script>
<!-- AppUtil.js中引用附件上传的JS -->
<script type="text/javascript" src="<%=basePath%>/js/system/FileUploadManager.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/system/FileUploadImageDetail.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/fileupload/swfobject.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/fileupload/FlexUploadDialog.js"></script>

<!-- 分页栏JS HTExt.js中引用 -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/PageComboResizer.js"></script>

<!-- 提示信息JS -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/Toast.js"></script>

<!-- GirdPanel中引用 -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/Ext.ux.grid.RowActions.js"></script>

<!-- 需要的 JS-->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/XmlTreeLoader.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/TabCloseMenu.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/ux/DateTimeField.js"></script>
<!-- sea配置 -->
<script type="text/javascript" src="<%=basePath%>/jslib/sea.js"></script>

<script type="text/javascript" src="<%=basePath%>/js/seaConfig.js"></script>
<!-- core 工具JS -->
<script type="text/javascript" src="<%=basePath%>/js/core/ux/OrientExt.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/core/AppUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/core/ux/TreePanelEditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/core/ux/WebOffice.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/core/ux/ComboTree.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/core/ux/AttachPanel.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/core/ux/DicCombo.js"></script>

<!-- 用户选择器,多处用到  替换UserDialog -->
<script type="text/javascript" src="<%=basePath%>/js/selector/UserDialog.js"></script>
<!-- 部门选择器,多处用到 替换DepDialog -->
<script type="text/javascript" src="<%=basePath%>/js/selector/DepSelector.js"></script>
<!-- 部门选择器,多处用到 替换DepDialog -->
<script type="text/javascript" src="<%=basePath%>/js/selector/DepDialog.js"></script>
<!-- 在线用户选择器,在主页面显示在线用户时用到 -->
<script type="text/javascript" src="<%=basePath%>/js/selector/OnlineUserDialog.js"></script>

<!-- 接收站内短消息时所用JS -->
<script type="text/javascript" src="<%=basePath%>/js/info/MessageWin.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/info/ReMessageWin.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/info/MessageDetail.js"></script>


<!-- desktop样式 -->
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/desktop/js/StartMenu.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/desktop/js/TaskBar.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/desktop/js/Desktop.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/desktop/js/App.js"></script>
<script type="text/javascript" src="<%=basePath%>/jslib/ext3.4/desktop/js/Module.js"></script>

<script type="text/javascript">
	// seajs配置
	seajs.config({
		base : "../",
		charset : 'utf-8',
		timeout : 20000,
		debug : true,
		paths : {
			'lib' : __ctxPath + "/jslib",
			"ibmsJs" : __ctxPath
		}
	});
	seajs.use(__ctxPath + requestURIpath, function(mainFrame) {

		var panel = mainFrame.init({});
		var tabPanel = new Ext.TabPanel({
			id : "centerTabPanel",
			region : "center",
			deferredRender : true,
			enableTabScroll : true,
			activeTab : 0,
			defaults : {
				autoScroll : true,
				closable : false
			},
			items : [ panel ],
			listeners : {
				"add" : function(d, b, c) {
					if (d.items.length > 8) {
						d.remove(d.items.get(1));
						d.doLayout();
					}
				}
			}
		});

		var viewport = new Ext.Viewport({
			layout : "fit",
			items : [ tabPanel ]
		});
		viewport.doLayout();
		//e = b.add(panel);
		//b.activate(e);
	});
</script>
<script type="text/javascript">
	function closeTab(tabName, userFormPanelId) {
		var tab = Ext.getCmp(tabName);
		App.getContentPanel().remove(tab, true);
		var myPhoto = document.getElementById("myPhoto");
		myPhoto.src = userFormPanelId;
	}
	function closeTabForSign(tabName, userFormPanelId) {
		var tab = Ext.getCmp(tabName);
		App.getContentPanel().remove(tab, true);
		var myPhoto = document.getElementById("mySignPhoto");
		myPhoto.src = userFormPanelId;
	}
</script>
</head>
<body>
</html>