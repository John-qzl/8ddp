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

<script type="text/javascript" src="${ctx}/jslib/webuploader/webuploader.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/jslib/webuploader/webuploader.css"></script>

<style type="text/css">
	#picker {
		float: left;
		margin: 10px;
	}
	.selfCss {
		background: #00b7ee;
		border-radius: 3px;
		cursor: pointer;
		padding: 10px 15px;
		position: relative;
		color: #fff;
		margin: 10px;
		overflow: hidden;
		border-color: transparent;
		border: 1px solid #00b7ee;
		display: inline-block;
		font-size: 13px;
		font-family: "microsoft yahei"
	}
	#del {
		background: #00b7ee;
		border-radius: 3px;
		cursor: pointer;
		position: relative;
		color: #fff;
		overflow: hidden;
		border-color: transparent;
		border: 1px solid #00b7ee;
		display: inline-block;
		font-size: 13px;
		font-family: "microsoft yahei";
		padding: 5px 10px;
	}
	img {
		margin-left: -92px;
	}
	
	table {
		width: 100%;
		text-align: center;
		margin-top: 10px;
	}
	th, td {
		height: 40px;
		border: 1px solid #00b7ee;
		text-align: center;
	}
</style>
<script type="text/javascript">

$(function() {
	var $list = $("#content");
	var $btn = $("#ctlBtn");
	var thumbnailWidth = 100;
	var thumbnailHeight = 100;
	
	var uploader = WebUploader.create({
		auto: false,
		swf: __ctx +"/jslib/webuploader/Uploader.swf",
		server: __ctx +'/dp/form/diagramsave.do?diaId=${diaId}&chresId=${ckresult.ID}',
		pick: '#picker',
		accept: {
			title: 'Images',
			extensions: 'gif,jpg,ipeg,bmp,png',
			mimeType: 'image/*'
		},
		method: 'POST',
		fileNumLimit: 1
	});
	
	uploader.on('fileQueued', function(file){
		var $li = $(
			'<tr>' +
				'<td>' + file.name + '</td>'+ 
				'<td>' + file.size + 'K</td>'+ 
				'<td id="status">待上传</td>'+ 
				'<td><button id="del">删除</button></td>'+ 
			'</tr>'
		);
		$list.append($li);
		$('#del').on('click',function() {
			uploader.removeFile(file, true);
			$list.empty();
		})
	})
	
	uploader.on('uploadSuccess',function(file, response){
		if (response.success) {
			$list.find('#status').text("上传成功");
			$list.find('#del').hide();
		}
	})
	
	uploader.on('uploadError', function(file){
		if (!response.success) {
			$list.find('#status').text("上传失败");
		}
	})
	
	$btn.on('click',function(){
		uploader.upload();
	})
})
	
</script>
</head>
<body>
	<div id="uploader" class="wu-example">
		<div class="btns">
			<div id="picker">选择图片</div>
			<button id="ctlBtn" class="uploadBtn state-pedding selfCss">开始上传</button>
		</div>	
		<table>
			<thead>
				<th>文件名</th>
				<th>大小</th>
				<th>状态</th>
				<th>管理</th>
			</thead>
			<tbody id="content"></tbody>
		</table>
	</div>
</body>
</html>