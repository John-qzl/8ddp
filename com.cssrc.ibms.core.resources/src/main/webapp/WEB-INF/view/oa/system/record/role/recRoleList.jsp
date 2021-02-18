<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>表单角色表管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
  function CopyRoleDialog(conf)
  {
  	if(!conf) conf={};
  	var roleId=conf.roleId;
  	var url=__ctx + "/oa/system/recRole/copy.do?roleId=" +roleId;
  	$.extend(conf, {help:0,status:0,scroll:0,center:1});

  	url=url.getNewUrl(); 	
  	DialogUtil.open({
  		height:250,
  		width: 550,
  		title : '复制角色',
  		url: url, 
  		isResize: true,
  		//自定义参数
  		sucCall:function(rtn){
  			if(conf){
  				location.reload();
  			}
  		}
  	});
  }
    function copyRole(roleId,roleName){
    	CopyRoleDialog({roleId:roleId});
    }
    
	function editRoleRes(roleId,typeId){
        var url=__ctx+"/oa/system/recRoleFun/edit.do?roleId="+roleId+"&typeId="+typeId;
    	var winArgs="dialogWidth=600px;dialogHeight=460px;status=0;help=0;";
    	url=url.getNewUrl();
    	DialogUtil.open({
    		height:500,
    		width: 600,
    		title : '功能点分配',
    		url: url, 
    		isResize: true
    	});
    }
	
	function batchGrant(){  
		var url=__ctx+"/oa/system/recRoleFun/batchGrant.do?typeId=${typeId}";
		var winArgs="dialogWidth=600px;dialogHeight=460px;status=0;help=0;";
		url=url.getNewUrl();
		DialogUtil.open({
			height:550,
			width: 600,
			title : '批量功能点授权',
			url: url, 
			isResize: true
		});
	}
  </script>
</head>
<body>
			<div class="panel">
			<div class="hide-panel">
				<div class="hide-panel">
					<div class="panel-top">
						<div class="tbar-title">
							<span class="tbar-label">表单角色管理列表</span>
						</div>
						<div class="panel-toolbar">
							<div class="toolBar">
								<div class="group"><f:a alias="searchRole" css="link search" id="btnSearch" >查询</f:a></div>
								
								<div class="group">
									<f:a alias="addRole" css="link add" href="edit.do?typeId=${typeId}">添加</f:a>
								</div>
								
								<div class="group">
									<f:a alias="delRole" css="link del" action="del.do">删除</f:a>
								</div>
								
								<%-- <div class="group">
									<f:a alias="batResAuth" css="link grant"  onclick="batchGrant()" showNoRight="false"  >批量功能点授权</f:a>
								</div> --%>
								<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
							
							</div>	
						</div>
						<div class="panel-search">
								<form id="searchForm" method="post" action="list.do">
										<input type="hidden" name="typeId" value="${typeId}" />
										<ul class="row plat-row">
											<li><span class="label">角色名:</span><input type="text" name="Q_roleName_SL"  class="inputText" value="${param['Q_roleName_SL']}"/></li>
											<li><span class="label">子系统名称:</span><input type="text" name="Q_sysName_SL"  class="inputText" value="${param['Q_sysName_SL']}"/></li>
											<li><span class="label">允许删除:</span> 
											<select name="Q_allowDel_S" class="select"  value="${param['Q_allowDel_S']}">
												<option value="">--全部--</option>
												<option value="1" <c:if test="${param['Q_allowDel_S'] ==1}">selected</c:if> >允许 </option>
												<option value="0" <c:if test="${param['Q_allowDel_S'] ==0}">selected</c:if>  >不允许</option>
											</select></li>
											<li><span class="label">允许编辑:</span>
											<select name="Q_allowEdit_S" class="select"  value="${param['Q_allowEdit_S']}">
												<option value="" >--全部--</option>
												<option value="1" <c:if test="${param['Q_allowEdit_S'] ==1}">selected</c:if> >允许</option>
												<option value="0" <c:if test="${param['Q_allowEdit_S'] ==0}">selected</c:if> >不允许</option>
											</select></li>
											<%-- <li><span class="label">是否启用:</span>
											<select name="Q_status_S" class="select" value="${param['Q_status_S']}">
												<option value="">--全部--</option>
												<option value="1" <c:if test="${param['Q_status_S'] ==1 }">selected</c:if> >是</option>
												<option value="0" <c:if test="${param['Q_status_S'] ==0 }">selected</c:if> >否</option>
											</select></li>	 --%>										
										</ul>
								</form>
						</div>
					</div>
				</div>
				</div>
				<div class="panel-body">
					
				
				    	<c:set var="checkAll">
							<input type="checkbox" id="chkall"/>
						</c:set>
					    <display:table name="recRoleList" id="recRoleItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
							<display:column title="${checkAll}" media="html" style="width:30px;">
							<c:if test="${recRoleItem.allowDel==0}"><input type="checkbox" class="disabled" name="roleId" id="roleId" value="${recRoleItem.roleId}" disabled="disabled"></c:if>
							<c:if test="${recRoleItem.allowDel==1}"><input type="checkbox" class="pk" name="roleId" id="roleId" value="${recRoleItem.roleId}"></c:if>
							<c:if test="${recRoleItem.allowDel==null}"><input type="checkbox" class="pk" name="roleId" id="roleId" value="${recRoleItem.roleId}"></c:if>
							
							</display:column>
							<display:column property="roleName" title="角色名" style="text-align:left" sortable="true" sortName="roleName"></display:column>
							<display:column property="roleDesc" title="备注" style="text-align:left"></display:column>
 			                <display:column title="允许删除">
							<c:choose>
								<c:when test="${recRoleItem.allowDel eq 0}"><span class="red">不允许</span></c:when>
								<c:when test="${recRoleItem.allowDel eq 1}"><span class="green">允许</font></c:when>
								<c:otherwise>未设定</c:otherwise>
							</c:choose>
							</display:column>
							<display:column  title="允许编辑">
							<c:choose>
								<c:when test="${recRoleItem.allowEdit eq 0}"><span class="red">不允许</span></c:when>
								<c:when test="${recRoleItem.allowEdit eq 1}"><span class="green">允许</font></c:when>
								<c:otherwise>未设定</c:otherwise>
							</c:choose>
							</display:column>
							<display:column  title="允许记录权限设置">
							<c:choose>
								<c:when test="${recRoleItem.allowSet eq 0}"><span class="red">不允许</span></c:when>
								<c:when test="${recRoleItem.allowSet eq 1}"><span class="green">允许</font></c:when>
								<c:otherwise>未设定</c:otherwise>
							</c:choose>
							</display:column>
							<display:column title="记录角色列表中是否隐藏" >
							<c:choose>
							    <c:when test="${recRoleItem.isHide eq 0}"><span class="green">不隐藏</span></c:when>
								<c:when test="${recRoleItem.isHide eq 1}"><span class="red">隐藏</span></c:when>
								<c:otherwise><span class="red">未设定</span></c:otherwise>
							</c:choose>
							</display:column>
							<display:column title="管理" media="html"  style="width:50px;text-align:center" class="rowOps">
								<c:choose>
									<c:when test="${recRoleItem.allowDel==0 }">
										<a href="javascript:;" class="link del disabled" >删除</a>
									</c:when>
									<c:otherwise>
										<f:a alias="delRole" css="link del" href="del.do?roleId=${recRoleItem.roleId}">删除</f:a>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${recRoleItem.allowEdit==0 }">
										<a href="javascript:;" class="link edit disabled" >编辑</a>
									</c:when>
									<c:otherwise>
										<f:a alias="updRole" css="link edit" href="edit.do?roleId=${recRoleItem.roleId}&typeId=${recRoleItem.typeId}">编辑</f:a>
									</c:otherwise>
								</c:choose>
								<f:a alias="roleDetail" css="link detail" href="get.do?roleId=${recRoleItem.roleId}">明细</f:a>
<%-- 								<f:a alias="copyRole"  css="link copy" onclick="copyRole('${recRoleItem.roleId}','${recRoleItem.roleName}')" >复制角色</f:a>--%>								<f:a alias="sourceRole" css="link flowDesign" href="javascript:editRoleRes('${recRoleItem.roleId}','${typeId}');">功能点分配</f:a>
								<%-- <f:a alias="userMeta" css="link auth" href="${ctx}/oa/system/recRoleMeta/edit.do?roleId=${recRoleItem.roleId}&roleName=${recRoleItem.roleName}&typeId=${typeId}" >人员分配</f:a> --%>
							</display:column>
						</display:table>
						<ibms:paging tableId="recRoleItem"/>
				</div> 			
			</div> 
</body>
</html>


