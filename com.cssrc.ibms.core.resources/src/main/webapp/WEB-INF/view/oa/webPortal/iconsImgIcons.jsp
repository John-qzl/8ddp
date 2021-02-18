<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.iconImg {
		margin: 0 auto;
		/* background-size: cover; */
	}
	.block-icon-list {
		letter-spacing: -4px;
	}
	.icon-item {
		display: inline-block;
 		width: 9.8%;  
		text-align: center;
		margin: 10px 0 15px 0;
		font-size: 14px;
		letter-spacing: normal;
		border: 1px solid transparent; 
	}
	.iconImgClassName, .iconImgUrl {
		line-height: 24px;
	}
	#panel, #imgIconList {
		background: #fff !important;
	}
	.block-icon-list li:hover {
		border: 1px solid #3eaaf5;
		background-color: #f5f9ff;
	}
</style>
<script type="text/javascript">
	$(function() {
		var width = ($('.block-icon-list').width()-40)/10;
		$('.icon-item').width(width);
	})
</script>
<div class="panel" id="panel">
	<div class="panel-body" id="imgIconList">
		<ul class="block-icon-list">
			<c:forEach items="${iconImgList}" var="icon" varStatus="status">
				<li class="icon-item">
					<div class="iconImg ${icon.iconImgClassName}"></div>
					<span class="iconImgClassName">${icon.iconImgClassName}</span><br/>
					<span class="iconImgUrl">${icon.iconImgUrl}</span>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>