<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="ibms" uri="http://www.cssrc.com.cn/paging" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=IE8" />

<%@include file="/commons/include/color.jsp"%>

<f:link href="iconfont.css"></f:link>
<f:link href="iconImg.css"></f:link>
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<f:link href="web.css"></f:link>
<f:link href="jquery/plugins/rowOps.css"></f:link><!--é¼ æ æ»å¨css  -->
<f:link href="layout/layout.all.css"></f:link>
<f:js pre="jslib/lang/common" ></f:js>
<f:js pre="jslib/lang/js" ></f:js>
<script type="text/javascript">
  var __ctx='<%=request.getContextPath()%>';
  var __jsessionId='<%=session.getId() %>';
</script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script><!-- jqueryæ©å±æ¹æ³å¯å¤ç¨ -->
<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script><!-- è¡¨ååå»ºæäº¤jsç±» -->
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script><!-- å¼å¥ligerui.all.js -->
<script type="text/javascript" src="${ctx}/js/lib/ligerDialog.js"></script><!--引入有定制的ligerDialog.js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/displaytag.js" ></script><!-- è¡¨åæé®åç®¡çæ ç­¾ç»å®äºä»¶js -->
<script type="text/javascript" src="${ctx}/jslib/calendar/My97DatePicker/WdatePicker.js"></script><!-- æ¶é´æ§ä»¶æ¾ç¤º-->
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.htselect.js"></script> <!-- ææ½æä»¶ -->
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.rowOps.js"></script><!--  é¼ æ æ»å¨ç®¡çåå¼¹åºèåjs -->
<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script><!--å°è£ligerDialogæ¹æ³å¨å½åé¡µé¢é¡¶é¨æ¾ç¤º-->
<script type="text/javascript" src="${ctx}/jslib/ibms/foldBox.js" ></script><!-- panel-searchæå¯»æ¡ç¼è¾jsåcssæ·»å  -->
<script type="text/javascript" src="${ctx}/jslib/ibms/tipsOps.js" ></script><!-- ç®¡çåæ¾ç¤º-->
<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>  <!--panel-bodyé«åº¦è®¡ç®-->
<%-- <script type="text/javascript" src="${ctx}/jslib/ibms/oa/test/test.js"></script>
 --%><script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/AdvancedQuery.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/util/iframeSwitch.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/util/IbmsRecord.js"></script>
 