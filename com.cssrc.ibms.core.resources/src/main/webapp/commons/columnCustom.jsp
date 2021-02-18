<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<link rel="stylesheet" href="${ctx}/styles/home/home.css">
<link rel="stylesheet" href="${ctx}/styles/home/responsiveslides.css">	
<link rel="stylesheet" href="${ctx}/styles/home/calendar.css">
<f:link href="from-jsp.css"></f:link>
	<script type="text/javascript">
	
	if(document.documentElement.clientWidth < 780) {
		document.documentElement.className += 'minW';
	}
	
	</script>
<f:js pre="jslib/lang/common" ></f:js>
<f:js pre="jslib/lang/js" ></f:js>
<f:js pre="jslib/lang/view/oa/system" ></f:js>
<script type="text/javascript" src="${ctx}/js/lib/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/jquery.mousewheel.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/jquery.rollbar.min.js"></script>

<script src="${ctx}/js/home/responsiveslides.js"></script>
<!-- jquery扩展代码 -->
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<script src="${ctx}/js/home/home.js"></script>
<script src="${ctx}/jslib/lg/util/DialogUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>