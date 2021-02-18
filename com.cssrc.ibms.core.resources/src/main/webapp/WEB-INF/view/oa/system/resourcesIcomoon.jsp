<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>设置图标</title>
	<%@include file="/commons/include/form.jsp" %>
<style type="text/css"> 
	.icomoon {
	  font-size:34px;
	}
	.block-icon-list {
		letter-spacing: -4px;
	}
	.icon-item {
		display: inline-block;
		text-align: center;
		margin: 10px 0 15px 0;
		font-size: 14px;
		letter-spacing: normal;
	 	border: 1px solid transparent; 
	}
	.icon-className, .icon-content {
		line-height: 24px;
	}
	#panel,#iconfontList {
		background: #fff !important;
	} 
	.block-icon-list li:hover {
		border: 1px solid #3eaaf5;
		background-color: #f5f9ff;
	}
</style>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js"></script>
	<script type="text/javascript">
		var selectedImg=null;
		$(function(){
			var width = ($('.block-icon-list',$('#icomoonList')).width()-40)/10;
			$('.icon-item').width(width);
			
			$("#icomoonList li").click(function(){
				var flag = $(this).hasClass('selected');
				if(selectedImg){
					$(selectedImg).removeClass('selected');
				}
				if(!flag){
					$(this).addClass('selected');
					selectedImg=this;
				}
			});
		});
	</script>
</head>
<body>
<div class="panel" style="background-color: #fff !important;">
	<div class="panel-body" id="icomoonList" style="background-color: #fff !important;">
		<ul class="block-icon-list">
			<c:forEach items="${iconList}" var="icon" varStatus="status">
				<li class="icon-item" iconclass="icomoon ${icon.iconClassName}">
					<span class="icomoon ${icon.iconClassName}"></span><br/>
					<span class="icon-className">${icon.iconClassName}</span><br/>
					<span class="icon-content">${icon.iconContent}</span>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
</body>
</html>