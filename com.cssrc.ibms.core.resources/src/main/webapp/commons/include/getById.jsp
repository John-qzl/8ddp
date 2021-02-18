<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<%@include file="/commons/include/color.jsp"%>
<f:link href="web.css" ></f:link>
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<script type="text/javascript">
  var __ctx='<%=request.getContextPath()%>';
  var __jsessionId='<%=session.getId() %>';
</script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.min.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
