<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>皮肤管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	var skinJson = ${skinJson};

	$(function() {		
		$('select').change(function() {
			if (this.value == "1") {
				$('.selectOrg').remove();
				$('.skin-app').append('<div class="selectOrg">'
						+ '<input data-select="org" name="sxbm" type="text" readonly="readonly"/>'
						+ '<a data-select="reset" href="javascript:reset();">'
						+ '<a data-select="org" href="javascript:selectOrg();">'
						+ '</div>'
				);
			} else {
				$('.selectOrg').remove();
			}
		})
		
		loadData();
	})
	
	function loadData() {
		var ul = $('.skins ul');
		for (var k in skinJson) {
			if (k.indexOf('_n') > 0) {
				var span = '<span id="'+k+'">'+skinJson[k]+'：</span>';
				k = k.substring(0, k.indexOf('_n'));
				var input = '<input type="text" id="'+k+'" value="'+skinJson[k]+'">'
				ul.append('<li>' + span + input + '</li>');
			}
		}
	}
	
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
		switch(layoutApp) {
			case '0': 
				break;
			case '1':
				layoutApp = $('.selectOrg').find('input[name="sxbm"]').attr('id');
				break;
			case '2':
				layoutApp = '${userId}';
				break;
		}
		
		$.ajax({
			async: false,
			type: 'POST',
			dataType: 'json',
			url: "${ctx}/oa/webPortal/sysLayout/save.do",
			data: {
				selectedModel: selectedModel,
				layoutApp: layoutApp
			},
			success: function(data) {
				if (data.success) {
					$.ligerDialog.confirm("布局已更改，是否刷新", "提示信息", function(rtn) {
						if(rtn) {
							//window.location.href = "${ctx}/oa/webPortal/sysLayout/list.do";
							window.location.href = "${ctx}/oa/console/main.do"
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
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="l-layout-header">平台配置【皮肤管理】</div>
			<div class="panel-toolbar">
				<div class="btn-component">
					<div class="x-btn x-btn-save">
						<a class="link save" onclick="save()">保存</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div class="skin-app">
				<span>皮肤应用:</span>
				<select class="x-Psearch-label">
					<option value="2">个人</option>
					<option value="1">部门</option>
					<option value="0">全局</option>
				</select>
			</div>
			<div class="skins">
				<ul></ul>
			</div>
		</div>
	</div>
</body>
</html> --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>系统皮肤管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<%-- <%@ taglib prefix="ibms" uri="http://www.cssrc.com.cn/paging" %> --%>

	<%-- <script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/CopyRoleDialog.js"></script> --%>
    <script type="text/javascript">
	   /*  function copyRole(roleId,roleName){
	    	CopyRoleDialog({roleId:roleId});
	    }

		function editRoleRes(roleId){
	        var url=__ctx+"/oa/system/roleResources/edit.do?roleId="+roleId;
	    	var winArgs="dialogWidth=350px;dialogHeight=460px;status=0;help=0;";
	    	url=url.getNewUrl();
	    	DialogUtil.open({
	    		height:500,
	    		width: 350,
	    		title : '资源分配',
	    		url: url,
	    		isResize: true
	    	});
	    }

		function batchGrant(){
			var url=__ctx+"/oa/system/roleResources/batchGrant.do";
			var winArgs="dialogWidth=600px;dialogHeight=460px;status=0;help=0;";
			url=url.getNewUrl();
			//window.location.href=url;
			DialogUtil.open({
				height:550,
				width: 600,
				title : '批量资源授权',
				url: url,
				isResize: true
			});
		} */
    </script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-top-title">
				<span class="tbar-label">系统皮肤管理列表</span>
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
							<ul class="psearch-component">
								<li class="x-Psearch x-Psearch-li">
									<span class="x-Psearch-label">皮肤名称:</span>
									<input type="text" name="Q_roleName_SL" class="x-Psearch-input" value="${param['Q_roleName_SL']}"/>
								</li>
								<li class="x-Psearch x-Psearch-li">
									<span class="x-Psearch-label">状态:</span>
									<select name="Q_allowDel_S" class="x-Psearch-select"  value="${param['Q_allowDel_S']}">
										<option value="">--全部--</option>
										<option value="1" <c:if test="${param['Q_allowDel_S'] ==1}">selected</c:if> >已发布</option>
										<option value="0" <c:if test="${param['Q_allowDel_S'] ==0}">selected</c:if>  >未发布</option>
									</select>
								</li>
							</ul>
					</form>
			</div>
			<div class="panel-toolbar">
				<div class="btn-table">
					<div class="x-btn x-btn-search"><f:a alias="searchRole" css="link search" id="btnSearch" >查询</f:a></div>

					<div class="x-btn x-btn-add">
						<f:a alias="addRole" css="link add" href="edit.do">添加</f:a>
					</div>

					<div class="x-btn x-btn-del">
						<f:a alias="delRole" css="link del" action="del.do">删除</f:a>
					</div>
					
					<div class="x-btn x-btn-del">
						<f:a alias="delRole" css="link del" action="del.do">导入</f:a>
					</div>
					
					<div class="x-btn x-btn-del">
						<f:a alias="delRole" css="link del" action="del.do">导出</f:a>
					</div>

					<div class="x-btn x-btn-del">
						<f:a alias="delRole" css="link del" action="del.do">更换皮肤</f:a>
					</div>
					
					<div class="x-btn x-btn-reset"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>

				</div>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysRoleList" id="sysRoleItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="x-table x-table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
				<c:if test="${sysRoleItem.allowDel==0}"><input type="checkbox" class="disabled" name="roleId" id="roleId" value="${sysRoleItem.roleId}" disabled="disabled"></c:if>
				<c:if test="${sysRoleItem.allowDel==1}"><input type="checkbox" class="pk" name="roleId" id="roleId" value="${sysRoleItem.roleId}"></c:if>
				<c:if test="${sysRoleItem.allowDel==null}"><input type="checkbox" class="pk" name="roleId" id="roleId" value="${sysRoleItem.roleId}"></c:if>

				</display:column>
				<display:column property="roleName" title="皮肤名称" style="text-align:left" sortable="true" sortName="roleName"></display:column>
				<display:column property="roleDesc" title="描述" style="text-align:left"></display:column>
				<display:column title="状态" >
					<c:choose>
					    <c:when test="${sysRoleItem.status eq 0}"><span class="red">已发布</span></c:when>
						<c:when test="${sysRoleItem.status eq 1}"><span class="green">未发布</span></c:when>
					</c:choose>
				</display:column>
				<display:column title="管理" media="html"  style="width:50px;text-align:center" class="rowOps">
					<f:a alias="updRole" css="link edit" href="edit.do?roleId=${sysRoleItem.roleId}">编辑</f:a>
					<f:a alias="roleDetail" css="link detail" href="get.do?roleId=${sysRoleItem.roleId}">明细</f:a>
				</display:column>
			</display:table>
		</div>
		<ibms:paging tableId="sysRoleItem"/>
	</div>
</body>
</html>
