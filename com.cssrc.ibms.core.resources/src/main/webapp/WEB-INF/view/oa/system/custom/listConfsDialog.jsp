<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>个人数据展示面板设置</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/ListConfs.js"></script>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	$(function(){		
		//事件绑定
		$('a.link.save').click(Lconf.save);
		$('a.link.close').click(function(){
			if(dialog.options.sucCall){
				dialog.options.sucCall();
				dialog.close();
			}else{
				dialog.close();
			}
		});
		$('a.link.add').click(Lconf.addTrDefault);
		//数据初始化
		var listConfs = $('#listConfs').text();
		Lconf.init($.parseJSON(listConfs));		
	})
</script>
<style>
.table-grid input, .table-grid select, .table-grid textarea, .table-grid .Wdate{
	max-width:200px
}
</style>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">个人数据展示设置</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save"  href="####">保存</a>
					</div>
					<div class="group">
						<a class="link add" href="####" >新增行</a>
					</div>
					<div class="group">
						<a class="link close" href="####">关闭</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<table id="confsTbl" class="table-grid">
				<thead>
					<tr>
						<th width="20%">名称</th>
						<th width="20%">展示数据量</th>
						<th>业务数据模板</th>
						<th width="20%">高级查询</th>
						<th width="10%">管理</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div id="listConf" style="display: none;">
				<table cellpadding="1" cellspacing="1" class="table-detail" var="listConfTemplateTable">
					<tr>
						<td ><input type="text"   var="name" value="" /></td>
						<td>
							<select id="dataNum" name="dataNum">
									<option value="5" >5</option>
									<option value="10" >10</option>
									<option value="20" >20</option>
									<option value="40" >40</option>
									<option value="100" >100</option>
							</select>
						</td>
						<td>
							<select id="displayId" name="displayId" onchange="Lconf.DataTemplateChange(this)">								
					    		<c:forEach items="${dataTemplates}" var="dataTemplate">
					    			<option value="${dataTemplate.displayId}">${dataTemplate.name}</option>
					    		</c:forEach>						
							</select>
						</td>
						<td>
							<select id="advancedQuery" name="advancedQuery">
								<option value="no" >没有数据...</option>
							</select>
							<a class="link add" href="####" onclick="Lconf.addAdvancedQuery(this)"></a>
							<a class="link edit" href="####" onclick="Lconf.editAdvancedQuery(this)"></a>
						</td>
						<td>
							<a class="link moveup" href="####" onclick="Lconf.moveTr(this,true)"></a> 
							<a	class="link movedown" href="####" onclick="Lconf.moveTr(this,false)"></a> 
							<a class="link del" href="####" onclick="Lconf.delTr(this)"></a>			
						</td>
					</tr>
				</table>
				<textarea style="display: none;" id="listConfs" name="listConfs" >${fn:escapeXml(listConfs)}</textarea>
				</div>
			</div>
			</div>			
</body>
</html>