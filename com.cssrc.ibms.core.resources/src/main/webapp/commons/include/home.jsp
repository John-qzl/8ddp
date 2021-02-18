<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

	<%@include file="/commons/include/color.jsp"%>
	<f:link href="ibms/index.css"></f:link>
	<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
		<script type="text/javascript" src="${ctx}/jslib/fullcalendar/moment.min.js"></script>
	<!--[if !IE]> -->
	<script type="text/javascript">
		window.jQuery || document.write("<script src='${ctx}/jslib/bootstrap/jquery.min.js'>"+"<"+"/script>");
	</script>
	<!-- <![endif]-->
	<!--[if IE]>
	<script type="text/javascript">
		 window.jQuery || document.write("<script src='${ctx}/jslib/bootstrap/jquery1x.min.js'>"+"<"+"/script>");
	</script>
	<![endif]-->
  	<script type="text/javascript" src="${ctx}/jslib/bootstrap/jquery.easypiechart.min.js"></script>
  	<script type="text/javascript" src="${ctx}/jslib/bootstrap/jquery.sparkline.min.js"></script>
	 <script type="text/javascript" src="${ctx}/jslib/fullcalendar/fullcalendar.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lang/fullcalendar/zh_CN.js"></script>
	<script type="text/javascript" src="${ctx }/jslib/bootstrap/bootstrap.min.js"></script>
	<!--[if lte IE 8]>
		<script type="text/javascript" src="${ctx}/jslib/bootstrap/html5shiv.min.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/bootstrap/respond.min.js"></script>
	<![endif]-->
	<script type="text/javascript" src="${ctx}/jslib/bootstrap/bootstrap-dialog.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.slimscroll.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.blockUI.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.carouFredSel.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/echarts/echarts.js"></script>
		 <!-- BEGIN LayerSlider -->
	
    <script src="${ctx}/jslib/bootstrap/slider-layer-slider/greensock.js" type="text/javascript"></script>
    <script src="${ctx}/jslib/bootstrap/slider-layer-slider/layerslider.transitions.js" type="text/javascript"></script>
    <script src="${ctx}/jslib/bootstrap/slider-layer-slider/layerslider.kreaturamedia.jquery.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
    <script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
    <!-- END LayerSlider -->
    <script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
    	<!-- 判断不是IE就在home.jsp的引用中加载页面初始化js--indexPage.js-->
	<!--[if !IE]> -->
	<script type="text/javascript">
	document.write("<script src='${ctx}/jslib/ibms/index/indexPage.js'>"+"<"+"/script>");
	</script>
	<!-- <![endif]-->
  


	
	