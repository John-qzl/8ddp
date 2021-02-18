<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 SYS_DATA_SOURCE_DEF</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/angular.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/service/baseServices.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/service/arrayToolService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/service/commonListService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/sysDataSourceDef/SysDataSourceDefService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/sysDataSourceTemplate/SysDataSourceTemplateService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/sysDataSourceDef/EditController.js"></script>
	<script type="text/javascript">
		var id="${param.id}";
	</script>
</head>
<body ng-app="app" ng-controller="EditController">
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
	        <span class="tbar-label">数据源</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link test" ng-click="checkConnection(prop)">测试连接</a></div>
				<div class="group"><a class="link save" ng-click="save(prop)" href="javascript:;">保存</a></div>
				<div class="group"><a class="link back" href="list.do">返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="sysDataSourceForm" method="post" action="save.do">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
				<tr>
					<th width="20%">名称: </th>
					<td><input type="text" ng-model="prop.name" validate="{required:true,maxlength:64}" class="inputText"/></td>
				</tr>
				<tr>
					<th width="20%">别名: </th>
					<td><input id="alias" type="text" ng-model="prop.alias" ng-change="aliasChange(prop.alias);" validate="{required:true,maxlength:64}" class="inputText" <c:if test="${param.id!=null}">disabled="disabled"</c:if>/></td>
				</tr>
				<tr>
					<th width="20%">数据源类型: </th>
					<td>
						<select ng-model="prop.dbType" ng-change="dbTypeChange(prop.dbType)" ng-options="m.value as m.value for m in dbTypeList">
						</select>
					</td>
				</tr>
				<tr>
					<th width="20%">初始化容器: </th>
					<td>
						<select ng-model="prop.initContainer" ng-options="m.value as m.key for m in CommonList.yesOrNoList2">
						</select>
					</td>
				</tr>
				<tr>
					<th width="20%">是否生效: </th>
					<td>
						<select ng-model="prop.isEnabled" ng-options="m.value as m.key for m in CommonList.yesOrNoList2">
						</select>
					</td>
				</tr>
				<tr ng-if="!prop.id">
					<th width="20%">数据源模板: </th>
					<td>
						<select ng-model="selectedSysDataSourceTemplate" ng-change="sysDataSourceTemplateChange(selectedSysDataSourceTemplate);" ng-options="m as m.name for m in sysDataSourceTemplateList">
						</select>
					</td>
				</tr>
				<tr ng-repeat="attr in prop.settingJson">
					<th><span ng-class="{red:attr.baseAttr==1}">{{attr.comment}}</span></th>
					<td ng-if="attr.name.indexOf('alias')==-1&&attr.baseAttr==1"><input ng-model="attr.value" type="text" class="inputText" validate="{required:true}" style="width:600px"/>({{attr.type}})</td>
					<td ng-if="attr.name.indexOf('alias')==-1&&attr.baseAttr!=1"><input ng-model="attr.value" type="text" class="inputText" validate="{required:true}" style="width:600px"/>({{attr.type}})</td>
					<td ng-if="attr.name.indexOf('alias')!=-1"><input ng-model="attr.value" type="text" class="inputText" validate="{required:true}" style="width:600px" disabled="disabled"/>({{attr.type}})</td>
				</tr>
			</table>
			<input type="hidden" name="id" value="${sysDataSourceDef.id}" />					
		</form>
	</div>
</div>
</body>
</html>
