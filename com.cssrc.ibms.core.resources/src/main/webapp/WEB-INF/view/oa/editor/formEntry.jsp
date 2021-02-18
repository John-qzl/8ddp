<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <c:set var="ctx" value="${pageContext.request.contextPath}" />
<title>Insert title here</title>
<%@include file="/commons/include/form.jsp" %>
<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-all.css">
<link rel="stylesheet" href="${ctx}/styles/default/css/web.css">
<f:js pre="jslib/lang/common" ></f:js>
<script type="text/javascript" charset="utf-8" src="${ctx}/editor/jquery.js"></script>
<f:js pre="jslib/lang/js" ></f:js>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/util/IbmsRecord.js"></script>
<script type="text/javascript" src="${ctx}/js/dataPackage/dataPackage/SJBDetailList.js"></script>
<script type="text/javascript">
	var dialog = window;// 调用页面的dialog对象(ligerui对象)
	$(function() {
		addDEA();
		//根据附件中是否存在附件决定附件按钮的样式
		var fileBtns = $('.dpInputBtn');
		//自行构建json数组保存附件绑定的文本框或者勾选框ID
		var fileIdArray = [] ;
		for(var j= 0,fileCount = fileBtns.length ;j<fileCount;j++) {
			var inputs = $(fileBtns[j]).parent('td').find("input");
			var slId=$("#slid").val();
			var fileObj ={};
			//附件所在单元格唯一id根据 文本框或者勾选框确认(附件按钮不会单独存在)
			for(var i= 0, len = inputs.length;i<len;i++){
				//如果文本框以及勾选框在一个单元格中,附件绑定的ID为文本框ID
				if($(inputs[i]).attr("type")=="text"){
					fileObj['id'] = $(inputs[i]).attr("id");
					fileObj['slId'] = slId ;
					break;
				}else if($(inputs[i]).attr("type")=="checkbox"){
					fileObj['id'] = $(inputs[i]).attr("id");
					fileObj['slId'] = slId ;
				}
			}
			fileIdArray.push(fileObj)
		}
		$.ajax({
			async : false,
			cache : true,
			type: 'POST',
			data : {fileIdArray:JSON.stringify(fileIdArray)},
			url: __ctx+"/dp/form/checkWhetherFileExist.do",
			success:function(data){
				//后台获取的当前表单的所有fileBtn的有无附件情况(json格式存放)
				var result= JSON.parse(data);
				for(var j= 0,fileCount = fileBtns.length ;j<fileCount;j++) {
					//根据是否存在(yes/no),更改按钮样式
					var ifExist = result[0][j] ;
					if(ifExist == 'yes'){
						$(fileBtns[j]).css("background","red")
					}
				}
			},
			error:function(){
				$.ligerDialog.error('');
			}
		});
	})

	if (frameElement) {
		dialog = frameElement.dialog;
	}
	function formSave(){
		var condition=new Array();
		var content=new Array();
		var sign=new Array();
		var conditions=$("#condition").find("input");
		var contents=$("#content").find("input");
		var signs=$("#sign").find("input");
		var slid=$("#slid").val();
		for( var i=0;i<conditions.length;i++){
			var obj={'id':($(conditions[i]).attr("id")),'value':($(conditions[i]).val())};
			condition[i]=obj;
		}
		$("#content").find(':checkbox').each(function(){
			 ;
			if($(this).is(':checked')){
				var obj={'id':($(this).attr("id")),'value':true};
			}else{
				var obj={'id':($(this).attr("id")),'value':false};
			}
			content.push(obj);
		});
		$("#content").find('input[type="text"]').each(function(){
			var obj={'id':($(this).attr("id")),'value':($(this).val())};
			content.push(obj);
		})
		for( var i=0;i<signs.length;i++){
			var obj={'id':($(signs[i]).attr("id")),'value':($(signs[i]).val())};
			sign[i]=obj;
		}
		var allcontent={"condition":condition,"content":content,"sign":sign};
		var jsoncontent=  JSON.stringify(allcontent);
		$.ajax({
			  type: "POST",
		      url:"${ctx}/dp/form/saveForm.do",
			  data:{
				  jsoncontent:jsoncontent,
				  slid:slid
				   },
			  dataType : "json",
		      async:true,
		      success:function(data){
		    	  if(data.success=="true"){
	    				$.ligerDialog.confirm('表单录入成功，是否关闭该页面',function (rtn){
							if(rtn){
								var records = [];
								//更改执行状态 为 网页端进行中
								var record = new IbmsRecord("W_DATAPACKAGEINFO",${packageId},{"F_ZXZT":"网页端进行中"});
								records.push(record);
								var updateService =  new RecordService(records,function(result){
									//刷新问题!!!
									location.href = window.location.href.getNewUrl();
								});
								updateService.update();
								dialog.close();
							}
						});
	    			}else{
	    				$.ligerDialog.error(data.msg,"保存失败");
	    			}
		      }
		});
	}

	/**
	 * @Author  shenguoliang
	 * @Description: 保存并终止录入(禁止修改表单数据)
	 * @Params
	 * @Date 2018/6/4 14:42
	 * @Return
	 */
	function saveAndStop(){
		var condition=new Array();
		var content=new Array();
		var sign=new Array();
		var conditions=$("#condition").find("input");
		var signs=$("#sign").find("input");
		var slid=$("#slid").val();
		for( var i=0;i<conditions.length;i++){
			var obj={'id':($(conditions[i]).attr("id")),'value':($(conditions[i]).val())};
			condition[i]=obj;
		}
		$("#content").find(':checkbox').each(function(){
			if($(this).is(':checked')){
				var obj={'id':($(this).attr("id")),'value':true};
			}else{
				var obj={'id':($(this).attr("id")),'value':false};
			}
			content.push(obj);
		});
		$("#content").find('input[type="text"]').each(function(){
			var obj={'id':($(this).attr("id")),'value':($(this).val())};
			content.push(obj);
		});
		for( var i=0;i<signs.length;i++){
			var obj={'id':($(signs[i]).attr("id")),'value':($(signs[i]).val())};
			sign[i]=obj;
		}
		var allcontent={"condition":condition,"content":content,"sign":sign};
		var jsoncontent=  JSON.stringify(allcontent);
		$.ajax({
			type: "POST",
			url:"${ctx}/dp/form/saveForm.do",
			data:{
				jsoncontent:jsoncontent,
				slid:slid
			},
			dataType : "json",
			async:false,
			success:function(data){
					if(data.success=="true"){
						$.ligerDialog.confirm('表单录入成功，是否确认终止录入',function (rtn){
							if(rtn){
								var records = [];
								//更改执行状态 为 已完成
								var record = new IbmsRecord("W_DATAPACKAGEINFO",${packageId},{"F_ZXZT":"已完成"});
								records.push(record);
								var updateService =  new RecordService(records,function(result){
									//刷新问题!!!
									location.href = window.location.href.getNewUrl();
								});
								updateService.update();
								dialog.close();
							}
						});
					}else{
						$.ligerDialog.error(data.msg,"保存失败");
					}
				}
			});
		}
	function addAndShowPhoto(arg){
		var obj="";
		var inputs=$(arg).parent('td').find("input");
		for(var i=0;i<inputs.length;i++){
			if($(inputs[i]).attr("type")=="text"){
				obj=$(inputs[i]).attr("id");
				break;
			}else if($(inputs[i]).attr("type")=="checkbox"){
				obj=$(inputs[i]).attr("id");
			}
		}
		var slId = $("#slid").val();
		var ckResultName = "" ;
		//根据slId获取对应的型号类型
		$.ajax({
			type: "POST",
			url:"${ctx}/project/tree/stree/getCondResultNameByInsId.do",
			data:{
				slId:slId
			},
			dataType : "json",
			async:false,
			success:function(data){
				ckResultName = data ;
			}
		})
		 DialogUtil.open({
			url:"${ctx}/editor/accessoryUpload.jsp?tableId="+obj+"&ckResultName="+ckResultName,
			 height: window.screen.availHeight*0.3,
			 width: window.screen.availWidth*0.4,
			title:"系统附件上传",
			isResize: true,
			sucCall:function(rtn){
				if (rtn) {
					location.reload();
				}
			}
		});
	}
	
	function addDEA() {
		var tds = $("#content table").find("td");
		for (var i=0; i<tds.length; i++) {
			if ($(tds[i]).find('input[type="text"]').length > 0) {
				// $(tds[i]).append("&nbsp;<button onclick='deaAnalysis(this)'>包络分析</button>");
			}
		}
	}
	
	function deaAnalysis(arg) {
		var inputs=$(arg).parent('td').find("input");
		var ckResultId = $(inputs).attr("id");
		var slid = ${slid};
		
		DialogUtil.open({
			url:__ctx+"/dataPackage/dea/getQueryInfoByFrom.do?ckResultId="+ckResultId+"&slid="+slid,
			height: 650,
			width: 1400,
			title:"包络分析",
			isResize: true,
			sucCall:function(rtn){	
				
			}
		});
	}
	
	//导入历史数据
	function importExcel() {
		var url = '${ctx}/io/modelImport.do?slid='+ ${slid};
		DialogUtil.open({
			height:500,
			width: 800,
			url: url,
		    title: "导入历史数据",
			sucCall:function(rtn){
				location.reload();
			}
		});
	}
</script>
<style type="text/css">
body{
	padding: 0 10px;
}
td{
	/*white-space: nowrap;*/
	/*width: 100%;*/
	/*height: auto;*/
	/*word-wrap:break-word;*/
	/*word-break:break-all;*/
	/*overflow: hidden;*/

}
.dpCheckbox {
    vertical-align: middle!important;
    margin: 0 2px;
}
.dpInputBtn {
	vertical-align: middle!important;
	background-color: #3EAAF5;
	color: white;
	border: none!important;
	margin: 0 2px;
}
.dpInputText {
	vertical-align: middle!important;
	width: 60px!important;
	height:22px!important;
	margin: 0 2px;
}
</style>
</head>
<body>
<script type="text/javascript" src="${ctx}/layui/html5.min.js"></script>
<script type="text/javascript" src="${ctx}/layui/respond.min.js"></script>

  <div class="panel-toolbar"> <!-- style="background:none;" -->
    <div class="toolBar">
      <div class="group"><a class="link save" formId="${id}" href="####" onclick="javascript:formSave()">暂存</a></div>
	  <div class="group"><a class="link save" formId="${id}" href="####" onclick="javascript:saveAndStop()">终止录入</a></div>
      <div class="group"><a class="link import" href="####" onclick="javascript:importExcel()" formId="${id}">导入历史数据</a></div>
    </div>
  </div>
  <c:if test="${condiresSize>0}">
  <fieldset class="layui-elem-field">
    <legend>检查条件：</legend>
    <div id="condition" class="layui-field-box">
  		<div class="layui-row">
  			<c:forEach items="${condires}" var="condition" >
  		        <div class="layui-col-md3">
  	                <label class="layui-form-label">${condition.F_NAME }：</label>
  	                <div class="layui-input-block">
  	                  <input class="layui-input" type="text" id="${condition.ID}" value="${condition.F_VALUE }">
  	                </div>
  		        </div>
  			</c:forEach>
      	</div>
  	</div>
  </fieldset>
  </c:if>
<fieldset class="layui-elem-field">
  <legend>检查内容：</legend>
	<div id="content" class="layui-field-box">
		${content }
	</div>
</fieldset>
<c:if test="${signSize>0}">
<fieldset class="layui-elem-field">
  <legend>签署：</legend>
	<div id="sign" class="layui-field-box">
		<div class="layui-row">
			<c:forEach items="${signs}" var="sign" >
				<div class="layui-col-md3">
					<label class="layui-form-label">${sign.F_NAME }：</label>
					<div class="layui-input-block">
	                  <input class="layui-input" type="text" id="${sign.ID}" value="${sign.F_SIGNUSER }">
	                </div>
		        </div>
			</c:forEach>
    	</div>
	</div>
</fieldset>
</c:if>
	<input type="hidden" id="slid" name="slid" value="${slid }" />
</body>
</html>
