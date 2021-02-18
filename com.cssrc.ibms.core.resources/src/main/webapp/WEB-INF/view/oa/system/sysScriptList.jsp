<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统脚本管理</title>
		<%@include file="/commons/include/get.jsp"%>
		<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
	    <script type="text/javascript" src="${ctx}/jslib/lg/plugins/dicCombo.js"></script>
		<script type="text/javascript">
$(function() {
	handlerInit();
	$("div.group > a.link.download").click(function()
			{	
				if($(this).hasClass('disabled')) return false;
				
				var action=$(this).attr("action");
				var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
				
				if($aryId.length == 0){
					$.ligerDialog.warn("请选择记录！");
					return false;
				}
				
				//提交到后台服务器进行日志删除批处理的日志编号字符串
				var delId="";
				var keyName="";
				var len=$aryId.length;
				$aryId.each(function(i){
					var obj=$(this);
					if(i<len-1){
						delId+=obj.val() +",";
					}
					else{
						keyName=obj.attr("name");
						delId+=obj.val();
					}
				});
				var url=action +"?" +keyName +"=" +delId ;
				
				$.ligerDialog.confirm('确认导出吗？','提示信息',function(rtn) {
					if(rtn) {
						var form=new com.ibms.form.Form();
						form.creatForm("form", action);
						form.addFormEl(keyName, delId);
						form.submit();
					}
				});
				return false;
			
			});
  });
function ImportDefTableWindow(conf)
{
	if(!conf) conf={};
	var url=__ctx + "/oa/system/sysScript/import.do";
	conf.url=url;
	var dialogWidth=550;
	var dialogHeight=250;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
		
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '导入脚本',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(rtn!=null)
				window.location.reload(true);
		}
	});
}
//处理初始化模板
function handlerInit() {

}

</script>
	</head>
	<body>
		<div class="panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">系统脚本管理列表</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link search" id="btnSearch">查询</a>
						</div>
						
						<div class="group">
							<a class="link add" href="edit.do">添加</a>
						</div>
						
						<div class="group">
							<a class="link update" id="btnUpd" action="edit.do">编辑</a>
						</div>
						<div class="group"><a class="link del"  action="del.do">删除</a></div>
						<div class="group"><a class="link download"  action="export.do">导出</a></div>
				        <div class="group"><a class="link upload"  href="javascript:;"  onclick="ImportDefTableWindow()">导入</a></div>
					</div>
				</div>
				<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
						<ul class="row plat-row">
							<li>
								<span class="label">脚本名称:</span>
								<input type="text" name="Q_name_SL"
									value="${param['Q_name_SL']}" class="inputText" />
							</li>
							<li>
								<span class="label">脚本分类:</span>
								<select name="Q_category_S">
								<option value="">全部</option>
								<c:forEach items="${categoryList}" var="catName">
									<option value="${catName}" <c:if test="${param['Q_category_S'] == catName}">selected</c:if>>${catName}</option>
								</c:forEach>
							     </select>
							</li>
						</ul>
					</form>
				</div>
			</div>
			<div class="panel-body">
				<c:set var="checkAll">
					<input type="checkbox" id="chkall" />
				</c:set>
				<display:table name="sysScriptList" id="sysScriptItem"
					requestURI="list.do" sort="external" cellpadding="1"
					cellspacing="1" class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:10px;">
						<input type="checkbox" class="pk" name="id" value="${sysScriptItem.id}">
					</display:column>
					<display:column property="name" title="脚本名称" sortable="true" sortName="NAME" maxLength="20"></display:column>
					<display:column property="script" title="脚本内容" maxLength="50"></display:column>
					<display:column property="category" title="脚本分类" sortable="true" sortName="category" ></display:column>
					<display:column property="memo" title="备注" sortable="true" sortName="MEMO" maxLength="40"></display:column>
					<display:column title="管理" media="html"
						style="width:80px;text-align:center">
						<a href="del.do?id=${sysScriptItem.id}" class="link del">删除</a>
						<a href="edit.do?id=${sysScriptItem.id}" class="link edit">编辑</a>
						<a href="get.do?id=${sysScriptItem.id}" class="link detail">明细</a>
					</display:column>
				</display:table>
				<ibms:paging tableId="sysScriptItem" />
			</div>
		</div>
	</body>
</html>


