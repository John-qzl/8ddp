<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>图标库</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
$(function() {
	var height = $('body').height()-$('.l-layout-header').height()-$('.l-tab-links').height();
	//初始化tab页
	 $("#iconframe").ligerTab({
		height: height, 
		contextmehu: false,
		onBeforeSelectTabItem:function(tabid){						
			if(onBeforeSelectTabItemCallBack){
				return onBeforeSelectTabItemCallBack(tabid);
			}
		},
		onAfterSelectTabItem:function(tabid){
			if(onAfterSelectTabItemCallBack){
				onAfterSelectTabItemCallBack(tabid);
			}
	    }
	 });
	
	 var url = __ctx+"/oa/webPortal/icons/iconfont.do";
	 $(".iconfontIconsDiv").load(url);
})

//tab前置回调事件
//前置可以通过返回 return false组织tab切换。
var onBeforeSelectTabItemCallBack=function(id){
	$(".l-tab-content>div[tabid='"+id+"'] .l-text.l-text-combobox").hide();
};
//tab后置回调事件
var onAfterSelectTabItemCallBack=function(id){
	$(".l-tab-content>div[tabid='"+id+"'] .l-text.l-text-combobox").show();
	var icomoonIconsDiv = $(".l-tab-content>div[tabid='"+id+"']").find(".icomoonIconsDiv");
	if(icomoonIconsDiv.length>0) {
		icomoonIconsDiv.each(
			function() {
				$(this).removeClass("icomoonIconsDiv");
				var url = __ctx+"/oa/webPortal/icons/icomoon.do";
				$(this).load(url);
			}
		);	
	}
	var imgIconsDiv = $(".l-tab-content>div[tabid='"+id+"']").find(".imgIconsDiv");
	if(imgIconsDiv.length>0) {
		imgIconsDiv.each(
			function() {
				$(this).removeClass("imgIconsDiv");
				var url = __ctx+"/oa/webPortal/icons/imgIcons.do";
				$(this).load(url);
			}
		);	
	}
};
</script>

</head>
<body>
	<div class="l-layout-header">图标库信息</div>
	<div id="iconframe">
		<div tabid="iconfont" title="iconfont 字体图标" style="overflow: auto;">
			<div class="iconfontIconsDiv"></div>
			<!-- <iframe id="listFrame" src="icons.do" frameborder="no" width="100%" height="100%"></iframe>	 -->
		</div>
		<div tabid="icomoon" title="icomoon 字体图标" style="overflow: auto;">
			<div class="icomoonIconsDiv"></div>
		</div>
		<div tabid="iconImg" title="图片图标" style="overflow: auto;">
			<div class="imgIconsDiv"></div>
			<!-- <iframe id="iconImgFrame" src="imgIcons.do" frameborder="no" width="100%" height="100%"></iframe> -->
		</div>
	</div> 
</body>
</html>