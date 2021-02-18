<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>脚本设置</title>
<script type="text/javascript"src="${ctx}/jslib/ibms/oa/system/PersonScriptAddDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
<script type="text/javascript">
var defId =${defId};
var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
var cmpNames =dialog.get("cmpNames");
$(function(){
	handFlowVars();
	$("textarea#txtScriptData").val(cmpNames);
});

	function selectScript() {
		var valueTemp = InitMirror.editor.getCode();
		var rtn={returnVal:valueTemp};
		dialog.get("sucCall")(rtn);
		dialog.close();
	}
	
	function addPersonScript(obj){
		var _this = $(obj);
		PersonScriptAddDialog({
			data:{
				defId:defId
			},
			callback:addScriptCallBack
		});
	};

	function addScriptCallBack(data){
		var script = data.script;
		var inst = script.classInsName;
		var method = script.methodName;
		var str = "return "+inst +"."+method+ "( ";
		var paramStr="";
		for(var i=0 ; i<script.argument.length; i++){
			var p=script.argument[i];
			switch(p.paraValType){
			case '1':
				paramStr += p.paraVal+" , " ;
				break;
			case '2':
				if(p.paraType.indexOf("String")>0){
					paramStr += "\"" + p.paraVal+ "\" , " ;
				}else{
					paramStr +=  p.paraVal+ " , " ;
				}
				break;
			}
		}
		if(paramStr){
			paramStr = paramStr.substring(0,paramStr.length-2);
		}
		str += paramStr+");" ;
		InitMirror.editor.insertCode(str);
	};
	
	function handFlowVars(){
		$("select[name='selFlowVar']").change(function(){
			var val=$(this).val();
			InitMirror.editor.insertCode(val);
		});
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">

				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" onclick="selectScript()">保存</a>
						</div>
						
						<div class="group">
							<a class="link del" onclick="javasrcipt:dialog.close()">关闭</a>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div id="divScriptData" >
				<a href="####" id="btnScript" class="link var"
					title="常用脚本"
					onclick="addPersonScript()">常用脚本</a>
					&nbsp;&nbsp;表单变量:<f:flowVar defId="${defId}" controlName="selFlowVar" parentActDefId="${parentActDefId}"></f:flowVar>
				<ul>
					<li>1.表达式必须返回Set&lt;String&gt;集合类型的数据,数据项为用户ID。</li>
					<li>2.如将流程变量liuchengzhixingren作为执行人:HashSet userset = new HashSet(); userset.add(String.valueOf(liuchengzhixingren)); return userset; </li>
					<li>3.可以使用放在流程实例中的所有variable变量,相关执行代码在NodeUserCalculationScript.java中,这里不能用task和processCmd变量</li>
				</ul>
				<textarea id="txtScriptData" codemirror="true" mirrorheight="200px"
					name="txtScriptData" rows="20" cols="80" style="height: 95%;width:98%" class="inputText">${cmpNames}</textarea>
			</div>
		</div>
		<input type="hidden" id="defId" value="${defId}" />
	</div>
</body>
</html>