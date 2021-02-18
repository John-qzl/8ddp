<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@include file="/commons/include/html_doctype.html"%>
<%
String max = request.getParameter("max");
String type = request.getParameter("type");
%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<f:sysparam paramname="maxUploadSize" alias="maxUploadSize"></f:sysparam> 
<title>系统附件上传</title>

<script type="text/javascript" src="${ctx}/layui/layui.js"></script>
<link rel="stylesheet" href="${ctx}/layui/css/layui.css"></script>

<script type="text/javascript">
	layui.use('upload', function() {
		var $ = layui.jquery;
		var upload = layui.upload;
		
		var listView = $("#demoList");
		var uploadListIns = upload.render({
			elem: '#testList',
			url: __ctx +'/dp/form/diagramsave.do?diaId=${diaId}&chresId=${ckresult.ID}',
			accept: 'images',
			auto: false,
			bindAction: '#testListAction',
			choose: function (obj) {
				var files = this.files = obj.pushFile();
				obj.preview(function (index, file, result) {
					var tr = $(['<tr id="upload-'+index+'">'
					            ,'<td>'+file.name+'</td>'
					            ,'<td>'+(file.size/1014).toFixed(1)+'kb</td>'
					            ,'<td>等待上传</td>'
					            ,'<td>'
					            ,'<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
					            ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
					            ,'</td>'
					            ,'</tr>'].join(''));
					tr.find('.demo-reload').on('click', function() {
						obj.upload(index, file);
					})
					tr.find('.demo-delete').on('click',function() {
						delete files[index];
						tr.remove();
						uploadListIns.config.elem.next()[0].value='';
					})
					listView.append(tr);
				})
			},
			done: function(res, index, upload) {
				if (res.success == "true") {
					var tr = listView.find('tr#upload-'+index);
					var tds = tr.children();
					tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
					tds.eq(3).html('');
					return delete this.files[index];
				}
				this.error(index, upload);
			},
			error: function(index, upload) {
				var tr = listView.find('tr#upload-'+index);
				var tds = tr.children();
				tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
				tds.eq(3).find('.demo-reload').removeClass('layui-hide');
			}
		})
	})
</script>
</head>
<body>
	<div class="layui-upload">
		<br/>&nbsp;&nbsp;
		<button class="layui-btn layui-btn-normal" id="testList" type="button">选择文件</button>&nbsp;
		<button class="layui-btn" id="testListAction" type="button">开始上传</button>
		<div class="layui-upload-list">
			<table class="layui-table">
				<thead>
					<tr>
						<th>文件名</th>
						<th>大小</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="demoList"></tbody>
			</table>
		</div>
	</div>
</body>
</html>