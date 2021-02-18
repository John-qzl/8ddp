<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css"> 
	.iconfont {
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
<script type="text/javascript">
	$(function() {
		var width = ($('.block-icon-list').width()-40)/10;
		$('.icon-item').width(width);
	})
</script>
<div class="panel" id="panel">
	<div class="panel-body" id="iconfontList">
		<ul class="block-icon-list">
			<c:forEach items="${iconList}" var="icon" varStatus="status">
				<li class="icon-item">
					<span class="iconfont ${icon.iconClassName}" value="iconfont ${icon.iconClassName}"></span><br/>
					<span class="icon-className">${icon.iconClassName}</span><br/>
					<span class="icon-content">${icon.iconContent}</span>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
