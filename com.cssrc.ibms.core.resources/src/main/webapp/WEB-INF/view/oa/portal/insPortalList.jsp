<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>布局管理管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/sysObjRights/SysObjRightsUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysOrgTacticDialog.js"></script>
<script type="text/javascript">
	function design(portId){
		var url = __ctx + "/oa/portal/insPortal/global.do";
		if(portId) {
			url = __ctx + "/oa/portal/insPortal/global.do?portId=" + portId;
		}
		top['index'].showTabFromPage({
			title:'编辑门户',
			tabId:'designPort_'+portId,
			iconCls:'icon-window',
			url: url
		});
		/* var height=screen.availHeight-35;
		var width=screen.availWidth-5;
		top['index']._OpenWindow({
			title : '编辑布局',
			url : url,
			width : width,
			height : height,
			callback: function(){
				location.reload();
			}
		}); */
	}
	function saveOrg(id,orgId){
		$.ajax({
			type : "POST",
			url : __ctx+"/oa/system/sysIndexLayoutManage/saveOrg.do",
			data : {
				id:id,
				orgId:orgId
			},
			success : function(data) {
				var obj = eval('(' + data + ')');
				if (obj.result == 1) {
					$.ligerDialog.success("保存组织成功！","提示信息", function(rtn) {
						if(rtn)
							window.location.reload();
					});
				}

			}});
	}

	function selectOrg(id){
		var orgTactic = '${orgTactic}';
		SysOrgTacticDialog({
			orgTactic:orgTactic,
			callback:function(orgId, orgName){
				saveOrg(id,orgId);
			}
		});
	}

	function orgSelect(){
		var orgTactic = '${orgTactic}';
		SysOrgTacticDialog({
			orgTactic:orgTactic,
			callback:function(orgId, orgName){
				$('#orgId').val(orgId);
				$('#orgName').val(orgName);
				if($('#orgName').val()){
					$('.x-Psearch-cancel').show()
				}
			}
		});
	}

	function cancelVal(obj){
		$(obj).siblings('input[type=hidden]').length?$(obj).siblings('input[type=hidden]').val(""):"";
		$(obj).siblings('input.x-Psearch-input').val("");
		$(obj).hide();
	}

</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">布局管理管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="javascript:design();">添加</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					
					<div class="group"><a href="javascript:;" class="link reset" onclick="$.clearQueryForm();">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
						<li>
							<span class="label">布局名称:</span><input type="text" name="Q_name_SL"  class="inputText" value="${param['Q_name_SL'] }" />
						</li>
						<li>
							<span class="label">布局描述:</span><input type="text" name="Q_memo_SL"  class="inputText" value="${param['Q_memo_SL'] }" />
						</li>
						<li>
							<span class="label">所属组织:</span>
							<input type="hidden" id="orgId" name="Q_orgId_L"  class="inputText" value="${param['Q_orgId_L'] }" />
							<input type="text" id="orgName" name="Q_orgName_S" class="inputText" value="${param['Q_orgName_S'] }" />
							<%-- <span class="x-Psearch-cancel" <c:if test="${param['Q_orgName_S'] == null || param['Q_orgName_S']==''}">style="display:none"</c:if> onclick="cancelVal(this);"></span> --%>
							<a href="javascript:void(0);" onclick="orgSelect()" class="button inputText"><span>...</span></a>

						</li>
						<li>
						<span class="label">是否是默认:</span>
						<select name="Q_isDef_SN" value="${param['Q_isDef_SN']}"  class="inputText">
							<option value="">请选择</option>
							<option value="1" <c:if test="${param['Q_isDef_SN'] == '1'}">selected</c:if>>是</option>	
							<option value="0" <c:if test="${param['Q_isDef_SN'] == '0'}">selected</c:if>>否</option>
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
		    <display:table name="insPortalList" id="insPortalItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  			<input type="checkbox" class="pk"  name="portId" value="${insPortalItem.portId}"  <c:if test="${insPortalItem.isDefault=='YES'}">disabled="disabled"</c:if>  >
				</display:column>
				<display:column property="name" title="布局名称" sortable="true" sortName="NAME_" maxLength="80"></display:column>
				<display:column property="desc" title="布局描述" sortable="true" sortName="DESC_" maxLength="80"></display:column>
				<display:column property="orgName" title="所属组织" sortable="true" sortName="ORG_ID_"></display:column>
				<display:column title="是否是默认" sortable="true" sortName="IS_DEFAULT_">
						<c:choose>
							<c:when test="${insPortalItem.isDefault=='YES'}">
								<span class="red">是</span>
							</c:when>
							<c:otherwise>
								<span class="green">否</span>
							</c:otherwise>
						</c:choose>
				</display:column>
				<display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
					<c:if test="${insPortalItem.isDefault=='NO'}">
						<a href="del.do?portId=${insPortalItem.portId}" class="link del">删除</a>
					</c:if>
					<a  href="javascript:design(${insPortalItem.portId});" class="link edit">编辑</a>
							<a onclick="SysObjRightsUtil.setRights('${insPortalItem.portId}','${objType}');"  class="link detail">授权</a>
					<%-- <c:if test="${isSuperAdmin}">
						<a onclick="selectOrg('${insPortalItem.portId}');"  class="link setting">设置组织</a>
					</c:if> --%>
				</display:column>
			</display:table>
			<ibms:paging tableId="insPortalItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


