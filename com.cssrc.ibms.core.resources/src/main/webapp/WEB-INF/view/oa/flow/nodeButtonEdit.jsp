<%--
	time:2012-07-25 18:26:13
	desc:edit the 自定义工具条
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 自定义工具条</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=bpmNodeButton"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/BpmNodeButton.js"></script>
	<script type="text/javascript">
		var isStartForm=${bpmNodeButton.isstartform};
		var isSign=${bpmNodeButton.nodetype};
		var buttonStr = ${buttonStr};
		var bpmButtonList = eval(buttonStr);
		
		$(function() {
			function showRequest(formData, jqForm, options) { 
				var operatortype=$("#operatortype").val();
				if(operatortype=="0"){
					$.ligerDialog.warn("请选择操作类型",'提示信息');
					return false;
				}
				return true;
			} 
			valid(showRequest,showResponse);
			$("a.save").click(function() {
				$('#bpmNodeButtonForm').submit(); 
			});
			$("a.back").click(function(){
				var nurl =__ctx + "/oa/flow/nodeButton/getByNode.do?defId=${defId}&nodeId=${nodeId}&buttonFlag=${buttonFlag}&returnUrl=${returnUrl}";
				$.gotoDialogPage(nurl);
			})
			//获取操作类型。
			BpmNodeButton.getOperatorType(bpmButtonList,isStartForm,isSign);
			
			$("#operatortype").change(function(){
				var val=$(this).find("option:selected").text().trim();
				var script=$(this).find("option:selected").attr("script");
				var paramscript=$(this).find("option:selected").attr("paramscript");
				if(val!=""){
					$("#btnname").val(val);
				}
				if(!paramscript||paramscript==0){
					$("#trparamscript").hide();
				}else{
					$("#trparamscript").show();
				}
				if(!script||script==0){
					$("#trprevscript,#trafterscript").hide();
				}
				else{
					$("#trprevscript,#trafterscript").show();
				}
			});
		});
	
		
		function showResponse(data){
			var obj=new com.ibms.form.ResultMessage(data);
			if(obj.isSuccess()){//成功
				$.ligerDialog.confirm('操作成功,继续操作吗?','提示信息',function(rtn){
					if(rtn){
						location.reload();
					}else{
						var nurl =__ctx + "/oa/flow/nodeButton/getByNode.do?defId=${defId}&nodeId=${nodeId}&buttonFlag=${buttonFlag}&returnUrl=${returnUrl}";
						$.gotoDialogPage(nurl);
					}
				});
		    }else{//失败
		    	$.ligerDialog.err('出错信息',"保存按钮失败",obj.getMessage());
		    }
		};


	</script>
</head>
<body>
  <c:if test="${buttonFlag}"> 
	<jsp:include page="incDefinitionHead.jsp">
		<jsp:param value="节点操作按钮" name="title"/>
	</jsp:include>
	<f:tab curTab="button" tabName="flow"/>
</c:if>
<div class="panel">
		<div class="panel-top">
			
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
					
					<div class="group"><a class="link back" href="####">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
				<form id="bpmNodeButtonForm" method="post" action="save.do">
					
						<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th width="20%">按钮名称: </th>
								<td><input type="text" id="btnname"  name="btnname" value="${bpmNodeButton.btnname}"  class="inputText"/></td>
							</tr>
							
							<tr>
							<th width="20%">操作类型:</th>
								<td>
									<select id="operatortype"  name="operatortype" operatortype="${bpmNodeButton.operatortype}" >
									</select>
								</td>	
							</tr>
							<tr id="trparamscript" <c:if test="${curbtn==null||curbtn.paramscript==0}">style="display:none"</c:if> >
								<th width="20%">参数脚本: </th>
								<td><textarea  id="paramscript" name="paramscript" cols="100" rows="15"  class="inputText" style="width:550px;float:left;">${bpmNodeButton.paramscript}</textarea>
									<div class="tipbox">
										<a href="javascript:;" class="tipinfo">
											<span style="width:300px">简单例子：
												<p>参数类型为param和report,param属性可任意设置，report必须按照要求设置</p>
												<p>var param,report;</p>
												<p>report.title='报表模板标题';</p>
												<p>如果需要下载报表，请设置ext属性，如果是在线浏览则不需要设置</p>
												<p>report.ext='pdf|word|excel|text|svg|csv|image';</p>
												<p>如果ext是excel 则 extype='null|simple|sheet' null表示分页导出;simple原样导出;sheet分页分sheet导出;</p>
												<p>如果ext是image 则extype='PNG|JPG|BMP|GIT';</p>
												<p>report.extype='simple|...';</p>
												<p>param可以根据报表模板任意设置值</p>
												<p>pram.id='参数id值';</p>
												<p>pram.name='参数name值';</p>
												<p>pram.key='参数key值';</p>
												<p>openFinerReort(report,pram);</p>
											</span>
										</a>
									</div>
									<br> 
									<b>脚本为javascript，用于设置参数，参数格式为Object。<br>
									类似写法：<br>
									参数类型为param和report,param属性可任意设置，report必须按照要求设置<br>
									var report,pram,conf;<br>
									report.title="title";<br>
									如果需要下载报表，请设置ext属性，如果是在线浏览则不需要设置;<br>
									report.ext='pdf|word|excel|text|svg|csv|image';<br>
									如果ext是excel 则 extype='null|simple|sheet' null表示分页导出;simple原样导出;sheet分页分sheet导出;</br>
									如果ext是image 则extype='PNG|JPG|BMP|GIT';</br>
									report.extype='simple|...';</br>
									pram.pram1="pram1";//参数可任意定制,但必须和报表中的参数匹配<br>
									conf.pram=pram;<br>
									conf.report=report;<br>
									return conf;<br>
									</b>
								</td>
							</tr>
							<tr id="trprevscript" <c:if test="${curbtn==null||curbtn.script==0}">style="display:none"</c:if> >
								<th width="20%">前置脚本: </th>
								<td><textarea  id="prevscript" name="prevscript" cols="100" rows="15"  class="inputText" style="width:550px;float:left;">${bpmNodeButton.prevscript}</textarea>
									<div class="tipbox">
										<a href="####" class="tipinfo">
											<span>
												简单例子：<p>var btn=$.ligerDialog.confirm("是否提交？");</p>
												<p>if(btn){</p>
													<p>&nbsp;&nbsp;alert("do something....");</p>
													<p>&nbsp;&nbsp;return true;</p>
												<p>}</p>
										   		<p>&nbsp;&nbsp;return false;</p>
											</span>
										</a>
									</div>
									<br> 
									<b>脚本为javascript，用于在提交前做些处理，需要返回true或false。返回false时不做提交动作。</b>
								</td>
							</tr>
							<tr id="trafterscript" <c:if test="${curbtn==null||curbtn.script==0}">style="display:none"</c:if> >
								<th width="20%">后置脚本: </th>
								<td><textarea  id="afterscript" name="afterscript" cols="100" rows="15"  class="inputText" style="width:550px;float:left;">${bpmNodeButton.afterscript}</textarea>
									<div class="tipbox">
										<a href="####" class="tipinfo">
											<span>
												简单例子：<p>var btn=$.ligerDialog.confirm("是否提交？");</p>
												<p>if(btn){</p>
													<p>&nbsp;&nbsp;alert("do something....");</p>
													<p>&nbsp;&nbsp;return true;</p>
												<p>}</p>
										   		<p>&nbsp;&nbsp;return false;</p>
											</span>
										</a>
									</div>
									<br>
									<b>
									脚本为javascript，用于在提交后做些处理，需要返回true或false。返回false时可以控制不关闭当前窗口。<br>
									类似写法:<br>
									var res = false;<br>
										 var obj = $("#businessKey");<br>
										 $.ajax({		<br>
										                url:"/ibms/oa/flow/flowscript/setFormDataValue.do",<br>
												data:{ <br>
												  dataId:obj.val(),<br>
												  tableName:'W_CTCSBZJJH',<br>
												  fieldName:'f_zt',<br>
												  value:'1'<br>
												},<br>
												async:false,<br>
												success:function(data){<br>
													res = true;		<br>	 
												}	<br>
											}); <br>
										return res;<br>
										
										<font color=red>其中:$("#businessKey")中的businessKey为com.ibms.core.bpm.model.ProcessCmd.java方法中的对象
										,该类中的其他对象也可以引用过来使用.</font>
									</b>
								</td>
							</tr>							
						</table>
						<input type="hidden" id="returnUrl" value="getByNode.do?defId=${bpmNodeButton.defId}&nodeId=${bpmNodeButton.nodeid}" />
						<input type="hidden" name="actdefid" value="${bpmNodeButton.actdefid}" />
						<input type="hidden" name="nodeid" value="${bpmNodeButton.nodeid}" />
						<input type="hidden" name="defId" value="${bpmNodeButton.defId}" />
						<input type="hidden" name="nodetype" value="${bpmNodeButton.nodetype}" />
						<input type="hidden" name="isstartform" value="${bpmNodeButton.isstartform}" />
						<input type="hidden" name="sn" value="${bpmNodeButton.sn}" />
						<input type="hidden" name="id" value="${bpmNodeButton.id}" />
					
				</form>
		</div>
</div>
</body>
</html>
