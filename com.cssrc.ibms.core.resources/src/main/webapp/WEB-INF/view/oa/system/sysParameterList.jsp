<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>系统参数表管理列表</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/catCombo.js"></script>
<script type="text/javascript">
	var dialog=null;
	function setCategory(){
		
		var parameterIds=getParameterId();
		if(parameterIds==""){
			$.ligerDialog.warn('还没有选择,请选择一项记录!','提示');
			return;
		}
		//$.initCatComboBox();
		if(dialog==null){
			dialog=$.ligerDialog.open({title:'设置分类',target:$("#dialogCategory"),width:400,height:180,buttons:
				[ {text : '确定',onclick: setCategoryOk},
				{text : '取消',onclick: function (item, dialog) {
						dialog.hide();
					}
				}]});	
		}
		dialog.show();
		
		//先移除dialog绑定的关闭事件
		$(".l-dialog-close").unbind("click");
		//手动添加dialog关闭时处理事件
		$(".l-dialog-close").click(function(event){
			dialog.hide();
		});
	}
	
	function getParameterId(){
		var aryChk=$("input:checkbox[name='id']:checked");
		if(aryChk.length==0) return "";
		var aryParameterId=[];
		aryChk.each(function(){
			aryParameterId.push($(this).val());
		});
		return aryParameterId.join(",");
	}
	
	function setCategoryOk(item, dialog){
		var type=$("#type").val();
		
		var parameterIds=getParameterId();
		var params={parameterIds:parameterIds,type:type};
		var url="${ctx}/oa/system/sysParameter/setCategory.do";
		$.post(url,params,function(responseText){
			var obj=new com.ibms.form.ResultMessage(responseText);
			if(obj.isSuccess()){
				$.ligerDialog.success('保存成功!','提示',function(){
					dialog.hide();
					var url=location.href.getNewUrl();
					location.href=url;
					var types = obj.data.types;
					window.parent.loadGlobalTree(JSON.parse(types));
				});
			}
			else{
				$.ligerDialog.err('提示','保存失败!',obj.getMessage());
			}
		});
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统参数表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do?type=${type}">添加</a></div>
					<%-- 
					<div class="group"><a class="link update" id="btnUpd" href="edit.do?type=${type}">修改</a></div> --%>
					
					<div class="group"><a onclick="setCategory()" href="####" class="link category">设置分类</a></div>
					<!-- 
					<div class="group"><a class="link reload"  href="####" onclick="window.location.reload()">刷新</a></div> -->	
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<input type="hidden" name="type" value="${param['type']}" title="系统参数分类"></input>
						<span class="label">参数名称:</span><input type="text" name="Q_paramname_SL"  class="inputText" value="${param['Q_paramname_SL']}"/>
						<span class="label">参数类型:</span><input type="text" name="Q_datatype_SL"  class="inputText" value="${param['Q_datatype_SL']}" />
						<span class="label">参数值:</span><input type="text" name="Q_paramvalue_SL"  class="inputText" value="${param['Q_paramvalue_SL']}" />
						<span class="label">参数描述:</span><input type="text" name="Q_paramdesc_SL"  class="inputText" value="${param['Q_paramdesc_SL']}" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysParameterList" id="sysParameterItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="id"  value="${sysParameterItem.id}">
				</display:column>
				<display:column property="paramname" title="参数名称" sortable="true" sortName="NAME"></display:column>
				<%-- <display:column property="datatype" title="参数类型" sortable="true" sortName="DATATYPE"></display:column> --%>
				<display:column property="paramvalue" title="参数值" maxLength="35" sortable="true" sortName="VALUE"></display:column>
				<display:column property="type" title="参数分类" style="color:green;" maxLength="35"></display:column>
				<display:column property="paramdesc" title="参数描述" sortable="true" sortName="DESCRIPTION"></display:column>
				<display:column title="管理" media="html" style="width:180px;text-align:center">
					<a href="edit.do?id=${sysParameterItem.id}&type=${type}" class="link edit">编辑</a>
					<!--<a href="del.do?id=${sysParameterItem.id}" class="link del">删除</a>-->
				</display:column>
			</display:table>
			<ibms:paging tableId="sysParameterItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
<div id="dialogCategory" style="width: 395px;display: none;">
	<div class="panel">
		<div class="panel-body">
			<form id="frmDel">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th width="25%">系统参数分类:</th>
						<td >
                            <input id="type" type="text" style="width:260px;" class="inputText"  value="${type}">
                        </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>
</html>


