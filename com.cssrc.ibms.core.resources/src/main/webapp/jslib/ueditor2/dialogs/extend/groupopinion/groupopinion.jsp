<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/commons/include/form.jsp"%>
<link rel="stylesheet" type="text/css" href="../input.css">
<script type="text/javascript" src="${ctx}/jslib/ueditor2/dialogs/internal.js"></script>
<script type="text/javascript">
	var formDefId;
	var _tablegroup;
	var _template;
	var _element;
	$(function() {
		_element = editor.curGroupopinion;
		formDefId = parent.window.document.getElementById("formDefId").value;
		var url="";
		url=__ctx + "/oa/form/formDef/getFlowNode.do?formDefId="+ formDefId;
		$.ajax({
			url: url,
			async:false,
			success:function(nodes){
				$("#input-flow-node").append("<option value=''>请选择......</option>")
				$(nodes).each(function(i,node){
					if(_element&&$(_element).attr("nodeId")==node.nodeId){
						$("#input-flow-node").append("<option selected=true value='"+node.nodeId+"'>"+node.nodeName+"</option>")
					}else{
						$("#input-flow-node").append("<option value='"+node.nodeId+"'>"+node.nodeName+"</option>")
					}
				})
			}
		})
		url=__ctx+ "/oa/form/formDef/getTablegroup.do?formDefId="+ formDefId;
		$.ajax({
			url: url,
			async:false,
			success:function(teams){
				$("#input-tablegroup").append("<option value=''>请选择......</option>")
				$(teams).each(function(i,team){
					if(_element&&$(_element).attr("tablegroup")==team.teamNameKey){
						$("#input-tablegroup").append("<option selected=true value='"+team.teamNameKey+"'>"+team.teamName+"</option>")
					}else{
						$("#input-tablegroup").append("<option value='"+team.teamNameKey+"'>"+team.teamName+"</option>")
					}
				})
			}
		})
		
		url=__ctx + "/oa/form/formDef/opinionTemplate.do";
		$.ajax({
			url: url,
			async:false,
			success:function(templates){

				$("#input-template").append("<option value=''>请选择......</option>")
				$(templates).each(function(i,template){
					if(_element&&$(_element).attr("template")==template.alias){
						$("#input-template").append("<option selected=true value='"+template.alias+"'>"+template.templateName+"</option>")
					}else{
						$("#input-template").append("<option value='"+template.alias+"'>"+template.templateName+"</option>")
					}
				})
			}
		})

		$("#input-tabletitle").val($(_element).attr("opiniontitle"));

	})


	dialog.onok = function() {
		var nodeId = $("#input-flow-node").val();
		var tablegroup = $("#input-tablegroup").val();
		var template = $("#input-template").val();
		var opinionTitle = $("#input-tabletitle").val();
		if (_element) {
			$(_element).attr("nodeId", nodeId);
			$(_element).attr("tablegroup", tablegroup);
			$(_element).attr("template", template);
			$(_element).attr("formDefId", formDefId);
			$(_element).attr("opinionTitle", opinionTitle);
		} else {
			var html = '<div class="groupopinion" name="editable-input" controlName="groupopinion"'+
			' tablegroup="'+tablegroup+'"'+
			' formDefId="'+formDefId+'"'+
			' nodeId="'+nodeId+'"'+
			' template="'+template+'"'+
			' nodeId="'+nodeId+'"'+
			' opinionTitle="'+opinionTitle+'">';
			html += '<input name="input_groupopinion" class="plat-formdesign-ipnut" type="text" value="'+opinionTitle+'"/></div> ';
			var child = utils.parseDomByString(html);
			var start = editor.selection.getStart();
			if (!start || !child)
				return;
			if (start.tagName == 'TD' || start.tagName == 'DIV') {
				start.appendChild(child);
			} else {
				start = domUtils.findEditableInput(start);
				utils.insertAfter(child, start);
			}
			//editor.execCommand('insertHtml', html);
		}
		editor.curGroupopinion = null;
	};
</script>


</head>
<body>
	<div id="inputPanel">
		<fieldset class="base">
			<legend>
				<var id="lang_box_prop"></var>
			</legend>
			<table>
				<tr>
					<th style="width: 150px">请选择流程节点:</th>
					<td style="width: 150px">
						<select name="input-flow-node" id="input-flow-node" class="valid">
						</select>
					</td>
				</tr>
				<tr>
					<th style="width: 150px">请选择模板:</th>
					<td style="width: 150px">
						<select name="input-template" id="input-template" class="valid">
						</select>
					</td>
				</tr>
				<tr>
					<th style="width: 150px">绑定表分组:</th>
					<td style="width: 150px">
						<select name="input-tablegroup" id="input-tablegroup" class="valid">
						</select>
					</td>
				</tr>
				<tr>
					<th style="width: 150px">标题:</th>
					<td style="width: 150px">
						<input id="input-tabletitle" type="text" />
					</td>
				</tr>
			</table>
		</fieldset>
	</div>
</body>
</html>