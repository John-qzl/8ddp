<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/commons/include/form.jsp"%>
<link rel="stylesheet" type="text/css" href="../input.css">
<script type="text/javascript"
	src="${ctx}/jslib/ueditor2/dialogs/internal.js"></script>
<script type="text/javascript">
	var _element;
	$(".button-td").bind("mouseenter mouseleave", function() {
		$(this).toggleClass("button-td-hover");
	});
	$(function() {
		_element = null;
		_element = editor.curTableFile;
		if (_element) {
			bindData();
		}
	});

	function bindData() {
		if (_element) {
			var tablefile=$(_element).find('textarea[name=tablefilesetting]').val();
			var obj = $.parseJSON(tablefile.replaceAll("\'","\""));
			$("[name=input-maindata-open]").val(obj.maindata);
			$("#input-mainfield-key").val(obj.mainfield);
			$("#input-reldata-key").val(obj.reldata);
			$("#input-reldatafield-key").val(obj.reldatafield);
			/* $("[name=input-dimension-open]").val(obj.dimension);
			$("#input-dimension-key").val(obj.dimensionkey); */
		}
	};

	function validata(n, o) {
		//匹配不含空格的字符串
		var reg = /^\S+$/gi;
		var match = reg.exec(n);
		if (match) {
			return true;
		}
		return false;
	};

	dialog.onok = function() {
		var tablefile = {};
		tablefile.maindata = $("[name=input-maindata-open]").val();
		tablefile.mainfield = $("#input-mainfield-key").val();
		tablefile.reldata = $("#input-reldata-key").val();
		tablefile.reldatafield = $("#input-reldatafield-key").val();
		/* tablefile.dimension = $("[name=input-dimension-open]").val();
		tablefile.dimensionkey = $("#input-dimension-key").val(); */
		if(_element){
			$(_element).find('textarea[name=tablefilesetting]').text(JSON2.stringify(tablefile));
		}else{
			var html = '<div class="tablefile" name="editable-input" controlName="tablefile">'+
				'<textarea style="display: none;" name="tablefilesetting" >'+JSON2.stringify(tablefile)+'</textarea>'+
				'<div name="fileList" style="height:100%;"></div>'+
			'</div>';
			var child = utils.parseDomByString(html);
			var start = editor.selection.getStart();
			if(!start||!child)return;
			if(start.tagName=='TD'||start.tagName=='DIV'){
				start.appendChild(child);
			}
			else{
				start = domUtils.findEditableInput(start);
				utils.insertAfter(child, start);
			}
		}
		editor.curTableFile = null;
	};
</script>
</head>
<body>
	<div id="inputPanel">
		<fieldset class="base">
			<legend>附件显示来源</legend>
			<table>
				<tr>
					<th>显示主表所有附件:</th>
					<td title=" 显示：表示显示该条记录主表所有附件；不显示：表示只显示该条记录所挂载的附件"><select
						name="input-maindata-open">
							<option value="0">不显示</option>
							<option value="1">显示</option>
					</select></td>
				</tr>
				<tr>
					<th>显示附件字段的附件:</th>
					<td><input id="input-mainfield-key" type="text" value=""
						title="请填写主表对应的附件上传字段的key，例如：fjzd" /></td>
				</tr>
				<tr>
					<th>显示关联表的附件:</th>
					<td><input id="input-reldata-key" type="text" value=""
						title="qzglb表示关联表,glqzb关联表字段，如果想关联多表加@，如qzglb:glqzb@qzglb1:glqzb1" />
					</td>
				</tr>
				<tr>
					<th>显示关联表附件字段的附件:</th>
					<td><input id="input-reldatafield-key" type="text" value=""
						title="该属性多加了fjzd,即附加了关联表的关联记录的附件上传字段的附件。添加多个多表也是加@" /></td>
				</tr>
			</table>
		</fieldset>

		<!-- <fieldset class="base">
			<legend>附件维度</legend>
			<table>
				<tr>
					<th>多维度显示:</th>
					<td><select name="input-dimension-open">
							<option value="0">不启用</option>
							<option value="1">启用</option>
					</select></td>
				</tr>
				<tr>
					<th>默认加载的维度:</th>
					<td><input id="input-dimension-key" type="text" value=""
						title="请填写辅助开发-分类管理-系统分类-文件分类下的节点key" /></td>
				</tr>
			</table>
		</fieldset> -->
	</div>
</body>
</html>