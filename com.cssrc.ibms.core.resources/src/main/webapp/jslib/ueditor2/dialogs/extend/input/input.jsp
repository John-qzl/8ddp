<%@page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/commons/include/form.jsp" %>
<link rel="stylesheet" type="text/css" href="../input.css">
<f:link href="form.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/ueditor2/dialogs/internal.js"></script>

<script type="text/javascript">
	var _element;
	$(".button-td").bind("mouseenter mouseleave",function(){
		$(this).toggleClass("button-td-hover");
	});
	$(function() {
		_element = null;
		_element = editor.curInput;
		if (_element) {
			bindData();
		}			
	});
	
	function openIconDialog() {
		var url= __ctx+"/jslib/ueditor2/dialogs/extend/input/icons.jsp";		
		var dialogWidth=500;
		var dialogHeight=400;
		conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1});
		
		DialogUtil.open({
			height:conf.dialogHeight,
			width: conf.dialogWidth,
			title : '',
			url: url, 
			isResize: false,
			//自定义参数
			sucCall:function(rtn){
				if(rtn!=undefined){
					$("#preview-button").attr("class",rtn.cla);
				}
			}
		}); 
	};

	function bindData() {
		var child = _element.childNodes[0];
		if (child) {
			var cla = $(child).attr("class"), label = $(child).text();
			$("#preview-button").attr("class", cla);
			$("#a-label").val(label);
		}
	};

	dialog.onok = function() {
		var html = '<span name="editable-input" style="display:inline-block;">';
		var label = $("#a-label").val(),
		cla = $("#preview-button").attr("class");
		html += '<a href="javascript:;" ';
		if(cla)html+='class="'+cla+'"';
		html +='>' + label + '</a>';
		html += '</span> ';
		if(_element){
			editor.curInput.outerHTML=html;
		}
		else{
			var child = utils.parseDomByString(html);
			var start = editor.selection.getStart();
			if(!start||!child)return;
			if(start.tagName=='TD'){
				start.appendChild(child);
			}
			else{
				start = domUtils.findEditableInput(start);
				utils.insertAfter(child, start);
			}
		}
	};
</script>
</head>
<body>
	<div id="inputPanel">
		<fieldset class="base">
			<legend><var id="lang_button_prop"></var></legend>
			<table>
				<tr>
					<th><var id="lang_button_label"></var>:</th>
					<td><input id="a-label" type="text" /></td>
				</tr>
				<tr>
					<th><var id="lang_button_img"></var>:</th>
					<td>
						<a href="javascript:;" id="preview-button"></a>				
						<div class="button-td" onclick="openIconDialog()">
						   	<var id="lang_choose_img"></var>
						</div>
					</td>
				</tr>				
			</table>
		</fieldset>
	</div>
	
</body>
</html>