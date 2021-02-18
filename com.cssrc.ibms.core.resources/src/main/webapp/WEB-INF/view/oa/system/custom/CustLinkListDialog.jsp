<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CustLinkLists.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<title>添加表</title>
<script type="text/javascript">
	var dialog = frameElement.dialog;
	function dialogClose(){
		if(dialog.options.sucCall){
			dialog.options.sucCall();
			dialog.close();
		}else{
			dialog.close();
		}
}
	
	function getFileId(obj){
		
		var trObj = $(obj).parents('tr');
		var conf = {isSingle:1,callback:function(rtn){
			var fileId = rtn.fileIds; 
			var href="/ibms/oa/system/sysFile/download.do?fileId="+fileId+"";
			$("input[name='url']",trObj).attr("value",href);
		}};
		FlexUploadDialog(conf);
	}
	$(function(){
		Clist.init();
	})
	
</script>
</head>
<body>
	<div class="panel">
	<div class="panel-top">
		<div class="panel-toolbar">
			<div class="toolBar">
					<div class="group">
						<a class="link save" onclick="Clist.save()"  href="javascript:void(0);">保存</a>
					</div>
					<div class="group">
						<a class="link add" onclick="Clist.add()" href="javascript:void(0);">新增</a>
					</div>
					<div class="group">
						<a class="link close" onclick="dialogClose()" + href="javascript:void(0);">关闭</a>
					</div>
			</div>
		</div>
	</div>
	<div class="panel-body">
			<table id="linkTbl" class="table-grid">
				<thead>
					<tr>
						<th width="20%">名称</th>
						<th width="20%">URL</th>
						<th width="20%">类型</th>
						<th width="10%">管理</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
	</div>
		<%-- table模板 --%>
		<div id="linkList" style="display:none;">
			<table cellpadding="1" cellspacing="1" class="table-detail" var="linkListTemplateTable">
				<tr>
					<td><input name="mc" type="text" value=""/></td>
					<td>
					<input name="url" type="text" value=""/>
					<div class="group">
						<a class="link upload" onclick="getFileId(this)"  href="javascript:void(0);">上传附件</a>
					</div>
					</td>
					<td>
						<select id="ms" name="ms">
								<option value="资料面板" >资料面板</option>
								<option value="附件下载" >附件下载</option>
						</select>
					</td>
					<td>
							<a class="link moveup" href="####" onclick="Clist.moveTr(this,true)"></a> 
							<a	class="link movedown" href="####" onclick="Clist.moveTr(this,false)"></a> 
							<a class="link del" href="####" onclick="Clist.delTr(this)"></a>			
					</td>
				</tr>
			</table>
		</div>
		<textarea style="display: none;" id=custLinkLists name="custLinkLists" >${fn:escapeXml(custLinkLists)}</textarea>
</div>
</body>
</html>