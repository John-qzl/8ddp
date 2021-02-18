<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="ibms" uri="http://www.cssrc.com.cn/paging" %>
<%@ taglib prefix="spr" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<f:link href="Aqua/css/ligerui-all.css" ></f:link>
<f:link href="tree/zTreeStyle.css" ></f:link>
<f:link href="web.css" ></f:link>
<f:js pre="jslib/lang/common" ></f:js>
<f:js pre="jslib/lang/js" ></f:js>
<f:js pre="jslib/lang/view/oa/system" ></f:js>


<%@include file="/commons/include/color.jsp"%>

<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script><!-- 引入ligerui.all.js-->
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/tipsOps.js" ></script><!-- 管理列显示-->
<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js" ></script><!-- 管理列显示-->
