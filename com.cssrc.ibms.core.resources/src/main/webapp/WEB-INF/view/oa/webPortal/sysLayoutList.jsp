<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>布局管理</title>
<%@include file="/commons/include/get.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="l-layout-header">平台配置【布局管理】</div>
			<div class="panel-toolbar">
				<div class="btn-component">
					<div class="x-btn x-btn-save">
						<a class="link save" onclick="save()">保存</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div class="layout-app">
				<span>布局应用：</span>
				<select class="x-Psearch-label">
					<option value="0" <c:if test="${appType==0}">selected</c:if> >全局</option>
					<%-- <option value="1" <c:if test="${appType==1}">selected</c:if> >部门</option>
					<option value="2" <c:if test="${appType==2}">selected</c:if> >个人</option> --%>
				</select>
			</div>
			<div class="select-model">
				<div class="title">
					<span>模板选择：</span>
				</div>
				<div class="models">
					<ul>
						<li>
							<img src="../../../styles/images/layout/main.png" data-id="main"/>
							<div class="layout-name">布局一</div>
						</li>
						<li>
							<img src="../../../styles/images/layout/main_topMenu.png" data-id="main_topMenu"/>
							<div class="layout-name">布局二</div>
						</li>
						<li>
							<img src="../../../styles/images/layout/main_topLeftMenu.png" data-id="main_topLeftMenu"/>
							<div class="layout-name">布局三</div>
						</li>
						<li>
							<img src="../../../styles/images/layout/main_leftLeftMenu.png" data-id="main_leftLeftMenu"/>
							<div class="layout-name">布局四</div>
						</li>
						<li>
							<img src="../../../styles/images/layout/main_leftFloatMenu.png" data-id="main_leftFloatMenu"/>
							<div class="layout-name">布局五</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	var layout = "${layout}";
	var appType = "${appType}";
	
	$(function() {		
		$('select').change(function() {
			if (this.value == "1") {
				$('.selectOrg').remove();
				$('.layout-app').append('<div class="selectOrg">'
						+ '<input data-select="org" name="sxbm" type="text" readonly="readonly"/>'
						+ '<a data-select="reset" href="javascript:reset();">'
						+ '<a data-select="org" href="javascript:selectOrg();">'
						+ '</div>'
				);
			} else {
				$('.selectOrg').remove();
			}
		})
		
		var $imgs = $('li img');
		$imgs.click(function() {
			$('li').removeClass('selected');
			$(this).parent('li').addClass('selected');
		})
		$imgs.each(function(){
			if ($(this).attr("data-id") == layout) {
				$(this).parent('li').addClass('selected');
				return false;
			}
		})
	})
	
	function reset() {
		$('input[name="sxbm"]').val("");
	}
	
	function selectOrg() {
		OrgDialog({isSingle:true,callback:function(orgIds, orgnames){
			var input = $('.selectOrg').find('input[name="sxbm"]');
			input.val(orgnames);
			input.attr("id", orgIds);
		}});
	}
	
	function save() {
		var selected = $('.selected');
		if (selected.length == 0) {
			$.ligerDialog.warn('请选择模板');
			return;
		}
		//所选模板
		var selectedModel = selected.find('img').attr('data-id');
		
		//布局应用
		var layoutApp = $('select').val();
		var appId = "0";
		switch(layoutApp) {
			case '0': 
				break;
			case '1':
				appId = $('.selectOrg').find('input[name="sxbm"]').attr('id');
				break;
			case '2':
				appId = '${userId}';
				break;
		}
		
		$.ajax({
			async: false,
			type: 'POST',
			dataType: 'json',
			url: "${ctx}/oa/webPortal/sysLayout/save.do",
			data: {
				selectedModel: selectedModel,
				appType: layoutApp,
				appId: appId
			},
			success: function(data) {
				if (data.success) {
					$.ligerDialog.confirm("布局已更改，是否刷新", "提示信息", function(rtn) {
						if(rtn) {
							//window.location.href = "${ctx}/oa/webPortal/sysLayout/list.do";
							top.location = "${ctx}/oa/console/main.do";
						}
					});
				} else {
					$.ligerDialog.success(data.message, "提示信息");
				}
			},
			error: function() {
				$.ligerDialog.error("保存失败")
			}
		})
	}
</script>
</body>
</html>