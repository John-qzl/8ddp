<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>滚动展示</title>
	<link rel="stylesheet" href="${ctx}/styles/home/home.css">
	<link rel="stylesheet" href="${ctx}/styles/home/responsiveslides.css">
	
	<style type="text/css">

	.rollbar-path-vertical { width: 5px;  right:3px;}
	.rollbar-path-vertical, .rollbar-path-horizontal {
		box-shadow: none;
		-moz-box-shadow: none;
		-webkit-box-shadow: none;
	} 
	.rollbar-handle {
	    background-color: #555;
	}
	</style>
	<script type="text/javascript">
	
	if(document.documentElement.clientWidth < 780) {
		document.documentElement.className += 'minW';
	}
	
	</script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.rollbar.min.js"></script>	
	<script src="${ctx}/js/home/responsiveslides.js"></script>
	<script src="${ctx}/js/home/home.js"></script>
</head>
	<body style="overflow:hidden" class="viewFramework-product-body" >
		<div class="content mCustomScrollbar clearFix" id="content">
			<div class="bd-left fl">
					<div class="callbacks_container">
					  <ul class="rslides callbacks" id="slider">
					  
						<li>
							<a href="####">
								<img src="${ctx}/styles/images/home/1.jpg" alt="">
								<p class="caption">辽宁舰首次以航母编队形式赴南海开展科研训练</p>
							</a>
						</li>
						
						<li>
							<a href="####">
								<img src="${ctx}/styles/images/home/2.jpg" alt="">
								<p class="caption">辽宁舰首次以航母编队形式赴南海开展科研训练</p>
							</a>
						</li>
						
						<li>
							<a href="####">
								<img src="${ctx}/styles/images/home/3.jpg" alt="">
								<p class="caption">辽宁舰首次以航母编队形式赴南海开展科研训练</p>
							</a>
						</li>
						
					  </ul>
				</div>
			</div>
		</div>	
	</body>
</html>