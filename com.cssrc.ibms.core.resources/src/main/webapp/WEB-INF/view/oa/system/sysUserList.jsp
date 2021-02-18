<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>用户表管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<f:sysUser name="LOCKED" alias="LOCKED"></f:sysUser>
	<f:sysUser name="UN_LOCKED" alias="UN_LOCKED"></f:sysUser>
	<f:sysUser name="DYNPWD_STATUS_BIND" alias="DYNPWD_STATUS_BIND"></f:sysUser>
	<f:sysUser name="DYNPWD_STATUS_UNBIND" alias="DYNPWD_STATUS_UNBIND"></f:sysUser>
	<f:sysUser name="DYNPWD_STATUS_OUT" alias="DYNPWD_STATUS_OUT"></f:sysUser>
	<f:sysUser name="FEIMI" alias="FEIMI"></f:sysUser>
	<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
	<script type="text/javascript">
	function openUserUnder(userid,obj){
		if($(obj).hasClass('disabled')) return false;

		var conf={};
		var url=__ctx + "/oa/system/userUnder/list.do?userId="+userid;
		conf.url=url;
		var dialogWidth=800;
		var dialogHeight=600;
		conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
		DialogUtil.open({
			height:conf.dialogHeight,
			width: conf.dialogWidth,
			title : '下属管理',
			url: url,
			isResize: true
		});
	}

	$(function(){
		//提示信息初始化
		$("a.tipinfo").each(function(){
			var helpId = $(this).attr('name');
			$(this).ligerTip({content : $("#"+helpId).html(),width:500,auto:true});

		});
	})
	 /**
     * @Description: 八部人员信息与tdm人员信息同步
     * @Author: fy
     * @Date: 2020/05/29
     */
     function dataSynchronization(){
    	 $.ligerDialog.confirm("确认同步？","提示信息", function(rtn) {
				if(rtn){
					var url=__ctx+"/oa/system/sysUser/dataSynchronization.do";
					$.ajax({
						  'url': url,
		    			    'async':false,
		    			    success:function(fileData){

		    			    }
					})
				}
    	 })
     }



	 /**
     * @Description: 用户信息导入
     * @Author: xx
     * @Date: 2019/7/15
     */
	function importUsers(){
    	debugger;
		var h=window.top.document.documentElement.clientHeight;
		var w=window.top.document.documentElement.clientWidth;
		var url=__ctx+"/sysUser/import/importUsers.do";
		DialogUtil.open({
			height:h*0.4,
			width: w*0.4,
			title : '用户导入',
			url: url,
			isResize: true,
			//自定义参数?
			sucCall:function(rtn){
				window.location.href = window.location.href.getNewUrl();
			}
		});
	}
    	 
    function ExuserInfo(){
    	$.ligerDialog.waitting("导出中...", "提示信息");
    	var url = __ctx + "/dataPackage/tree/ptree/ExuserInfo.do";
    	$.get(url,function (responseText) {
        	debugger;
        	 $.ligerDialog.closeWaitting();
            var obj = new com.ibms.form.ResultMessage(responseText);
            if (obj.isSuccess()) {
                var path = obj.data.filePath;
                if (path != null || path != "") {
                    path = encodeURI(path);
                } else {
                    $.ligerDialog.error("返回的下载地址无效！", "提示信息");
                    return;
                }
                var url = __ctx + "/oa/system/sysFile/downLoadTempFile.do"
                url += "?tempFilePath=" + path;
                window.location.href = url;//下载文件
                updateService.update();
            } else {
                $.ligerDialog.err("提示信息", "", obj.getMessage());
            }
        });
    }

  //用户信息导入
          function ImuserInfo() {
              var url = __ctx + '/oa/system/userOrgImport.do';
              DialogUtil.open({
                  height: 500,
                  width: 800,
                  url: url,
                  title: "用户信息导入",
                  sucCall: function (rtn) {
                      reFresh()
                  }
              });
          }
	</script>
</head>
<body>
<c:set var="SysUser_LOCKED" value="${LOCKED}"/>
<c:set var="SysUser_UN_LOCKED" value="${UN_LOCKED}"/>
	<div class="panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">用户表管理列表</span>
				</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<f:a alias="searchUser" css="link search" id="btnSearch">查询</f:a>
					</div>
					<c:if test="${!isRight}">
						<div class="group">
							<f:a alias="addUser" css="link add" href="edit.do">添加</f:a>
						</div>
						<div class="group">
							<f:a alias="delUser" css="link del" name="delUser" action="del.do">删除</f:a>
						</div>
					</c:if>
					<div class="group">
						<a class="link import" onclick="importUsers()">导入</a>
					</div>
					<div class="group">
						<a class="link export" href="${ctx}/office/sysuser/用户信息模板.xls">用户信息模板下载</a>
					</div>
					<div class="group">
					    <a class="link reset" onclick="$.clearQueryForm()">重置</a>
					</div>
					<div class="group">
					    <a class="link" onclick="dataSynchronization()">数据同步</a>
					</div>
					<div class="group">
					    <a class="link" onclick="ExuserInfo()">导出压缩包</a>
					</div>
					<div class="group">
					    <a class="link" onclick="ImuserInfo()">压缩包导入</a>
					</div>
					<div class="tipbox">
						<a href="####" class="tipinfo" >
							<span>
								组织:分为逻辑删除与物理删除(逻辑删除时参数有变化isdelete=1，物理删除则删除记录)</br>
								用户:是物理删除,如需逻辑删除，请设置状态。</br>
								角色:是物理删除</br>
								岗位:分为逻辑删除与物理删除(逻辑删除时参数有变化isdelete=1，物理删除则删除记录)
							</span>
						</a>
					</div>
				</div>
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
						<ul class="row plat-row">
							<li><span class="label">姓名:</span><input type="text" name="Q_fullname_SL"  class="inputText"  value="${param['Q_fullname_SL']}"/></li>
						    <li><span class="label">账号:</span><input type="text" name="Q_username_SL"  class="inputText"  value="${param['Q_username_SL']}"/></li>
							<li>
								<span class="label">状态:</span>
								<select name="Q_status_S" class="select"  value="${param['Q_status_S']}">
									<option value="">--选择--</option>
									<option value="${SysUser.DYNPWD_STATUS_BIND}">激活</option>
									<option value="${SysUser.DYNPWD_STATUS_UNBIND}">禁用</option>
									<option value="${SysUser.DYNPWD_STATUS_OUT}">删除</option>
								</select>
							</li>
							<li>
								<span class="label">入职时间从:</span>
								<input type="text" id="Q_begincreatetime_DL" name="Q_begincreatetime_DL"  class="inputText Wdate" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'Q_endcreatetime_DG\');}'})" value="${param['Q_begincreatetime_DL']}"/>
								</li>
							<li><span class="label">至</span><input type="text" id="Q_endcreatetime_DG" name="Q_endcreatetime_DG"  class="inputText Wdate" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'Q_begincreatetime_DL\');}'})"  value="${param['Q_endcreatetime_DG']}"/></li>

							<li><span class="label">是否锁定:</span>
								<select name="Q_lockState_N" class="select"  value="${param['Q_lockState_N']}">
									<option value="">--选择--</option>
									<option value="1"  <c:if test="${param['Q_lockState_N'] == 1}">selected</c:if>>已锁定 </option>
									<option value="0" <c:if test="${param['Q_lockState_N'] == 0}">selected</c:if>>未锁定 </option>
								</select>
							</li>
						</ul>
					</form>
			</div>
		</div>

		<div class="panel-body">
		    	<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table export="false" name="sysUserList" id="sysUserItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">
						  	<input type="checkbox" class="pk" name="userId" value="${sysUserItem.userId}">
					</display:column>
					<display:column property="fullname" title="姓名" sortable="true" sortName="fullname" style="text-align:left"></display:column>
					<display:column property="username" title="帐号" sortable="true" sortName="username" style="text-align:left"></display:column>
					<display:column  title="入职时间" sortable="true" sortName="accessionTime">
						<fmt:formatDate value="${sysUserItem.accessionTime}" pattern="yyyy-MM-dd"/>
					</display:column>
                    <display:column  property="orgName" title="所属组织" sortable="true" sortName="orgName" style="text-align:left"></display:column>
                    <display:column  property="posNames" title="所属岗位" sortable="true" sortName="posNames" style="text-align:left"></display:column>
                    <display:column  property="roleNames" title="所属角色" sortable="true" sortName="roleNames" style="text-align:left"></display:column>
	                <display:column title="是否可用" sortable="true" sortName="lockState">
						<c:choose>
							<c:when test="${sysUserItem.lockState==1}">
								<span class="red">已锁定</span>
						   	</c:when>
					       	<c:otherwise>
					       		<span class="green">未锁定</span>
						   	</c:otherwise>
						</c:choose>
					</display:column>
                	<display:column title="状态" sortable="true" sortName="status">
						<c:choose>
							<c:when test="${sysUserItem.status==1}">
								<span class="green">激活</span>

						   	</c:when>
						   	<c:when test="${sysUserItem.status==0}">
						   		<span class="red">禁用</span>

						   	</c:when>
					       	<c:otherwise>
					       		<span class="red">删除</span>

						   	</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="数据来源" sortable="true" sortName="fromType">
						<c:choose>
						   	<c:when test="${sysUserItem.fromType == '系统添加'}">
								<span class="green">${sysUserItem.fromType}</span>
						   	</c:when>
					       	<c:otherwise>
					       		<span class="red">${sysUserItem.fromType}</span>
						   	</c:otherwise>
						</c:choose>
					</display:column>
					<c:if test="${isShowSecurity}">
						<display:column title="密级" sortable="true" sortName="security">
							<c:if test="${(sysUserItem.security == null)||(sysUserItem.security eq '')}">
									<span class="green">${FEIMI}</span>
							</c:if>
							<c:forEach var="securityUserMap" items="${securityUserMap}">
								<c:if test="${sysUserItem.security eq securityUserMap.key}">
									<span class="green">${securityUserMap.value}</span>
								</c:if>
							</c:forEach>
						</display:column>
					</c:if>
					<display:column title="管理" media="html" style="text-align:center;width:13%;" class="rowOps">
						<c:if test="${!isRight}">
							<c:if test="${!isSystem}">
								<f:a alias="userUnder" css="link primary" href="javascript:;" onclick="openUserUnder('${sysUserItem.userId}',this)">下属管理</f:a>
							</c:if>
							<f:a alias="delUser" css="link del" name="delUser" href="del.do?userId=${sysUserItem.userId}" >删除 </f:a>
						</c:if>
						<f:a alias="updateUserInfo" css="link edit" href="edit.do?userId=${sysUserItem.userId}" >编辑</f:a>
						<f:a alias="userInfo" css="link detail" href="get.do?userId=${sysUserItem.userId}">明细</f:a>
						<c:if test="${!isRight}">
							<f:a alias="userInfo" css="link parameter" href="${ctx}/oa/system/sysUserParam/editByUserId.do?userId=${sysUserItem.userId}">参数属性</f:a>
							<f:a alias="resetPwd" css="link resetPwd" href="resetPwdView.do?userId=${sysUserItem.userId}">重置密码</f:a>
							<f:a alias="setStatus" css="link setting" href="editStatusView.do?userId=${sysUserItem.userId}">设置状态</f:a>
							<c:if test="${isShowSecurity}">
								<f:a alias="setSecurity" css="link setting" href="editSecurityView.do?userId=${sysUserItem.userId}">设置密级</f:a>
							</c:if>
						</c:if>
						<c:if test="${!isSystem}">
							<f:a alias="lockUser" css="link lock" href="lockUser.do?userId=${sysUserItem.userId}" >
								<c:choose>
									<c:when test="${sysUserItem.lockState eq 1}">解锁</c:when>
									<c:when test="${sysUserItem.lockState eq 0}">锁定</c:when>
								</c:choose>
							</f:a>
						</c:if>
						<c:if test="${cookie.origSwitch==null  && !isSystem && !isRight}">
							<f:a alias="switch" css="link switchuser" target="_top" href="${ctx}/j_spring_security_switch_user?j_username=${sysUserItem.username}" >切换用户</f:a>
						</c:if>
					</display:column>
				</display:table>
				<ibms:paging tableId="sysUserItem"/>
		</div>
	</div>
</body>
</html>


