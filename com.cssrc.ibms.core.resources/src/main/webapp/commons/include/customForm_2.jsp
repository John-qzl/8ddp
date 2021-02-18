<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/commons/include/color.jsp"%>
<f:link href="web.css" ></f:link>
<f:link href="Aqua/css/ligerui-all.css" ></f:link>
<f:link href="tree/zTreeStyle.css" ></f:link>
<f:link href="form.css" ></f:link>
<f:link href="form/form-process.css" ></f:link>
<f:js pre="jslib/lang/common" ></f:js>
<f:js pre="jslib/lang/js" ></f:js>

<%
	//关闭浏览器XSS Auditor保护，使其可以加载跨域脚本
	response.addHeader("X-XSS-Protection", "0")	;
%>
<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/base.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/dicCombo.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenuBar.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenu.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTextBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTip.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMessageBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMsg.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDrag.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/calendar/My97DatePicker/WdatePicker.js" ></script>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>

<%@include file="/commons/include/ueditor.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/rule.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorInit.js"></script><!-- 初始化加载  人员、角色、组织、岗位选择器 html初始化 -->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CustomForm.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/AttachMent.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SubtablePermission.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/ReltablePermission.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/ReadOnlyQuery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/OfficeControl.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/OfficePlugin.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ntkoWebSign/NtkoAddSecSign.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ntkoWebSign/NtkoWebSign.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ntkoWebSign/WebSignPlugin.js"></script>

<link href="${ctx}/jslib/pictureShow/css/jquery.fancybox.css" rel="stylesheet" />
<link href="${ctx}/jslib/pictureShow/css/pictureShow.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/jslib/pictureShow/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="${ctx}/jslib/pictureShow/PictureShowControl.js"></script>
<script type="text/javascript" src="${ctx}/jslib/pictureShow/PictureShowPlugin.js"></script>

<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormMath.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormDate.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormFKShowUtil.js"></script><!-- 初始化表单中的外键显示值。 -->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ShowExeInfo.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysAuditLink.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/Cascadequery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ImageQtip.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormInit.js"></script><!-- 加载  初始化表单-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>

<!--file-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/file/AttachFileDisplay.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/file/FormDefTableFile.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/util/iframeSwitch.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/util/IbmsRecord.js"></script>

<!--流程示意图-->
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery-powerFloat.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ViewSubFlowWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ViewSuperExecutionFlowWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessUrgeDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ShowExeInfo.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/UserInfo.js"></script>

