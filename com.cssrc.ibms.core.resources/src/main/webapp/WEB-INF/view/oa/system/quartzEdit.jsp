<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑定时任务</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript">
	$(function() {
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		var frm = $('#sysJobForm').form();
		$("a.save").click(function() {
			var str=setParameterXml();
			$("#parameterJson").val(str);
			frm.ajaxForm(options);
			if (frm.valid()) {
				$('#sysJobForm').submit();
			}
		});
	});
	function setParameterXml(){
		var objContainer=$("#trContainer");
		var len=objContainer.children().length;
		var children=objContainer.children() ;
		var xml="[";
		children.each(function(i){
			var name=$(this).find('input[name=paramName]').val();
			var type=$(this).find('select[name=paramtype]').val();
			var value=$(this).find('input[name=paramvlaue]').val();
			if(i<len-1)
				xml+="{\"name\":\""+name+"\",\"type\":\""+type+"\",\"value\":\""+value+"\"},";
			else
				xml+="{\"name\":\""+name+"\",\"type\":\""+type+"\",\"value\":\""+value+"\"}";
			 
		});
		xml+="]";
		return xml;
	}

	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作","提示信息",
							function(rtn) {
								if (rtn) {
									this.close();
									$("#sysJobForm").resetForm();
								} else {
									window.location.href = "${ctx}/oa/system/quartz/list.do";

								}
							});
		} else {
			$.ligerDialog.error(obj.getMessage(),'提示信息');
		}
	}
	/**
	 * 生成新增行id
	 * **/
    var rowId = 'r_' + 0 ;
    function getRowId(){
    	var row = rowId.split('_');
    	rowId = row[0]+'_'+ (row[1]*1 + 1);
    }
    /**
     * 生成select标签
     * */
    function createSelect(){
    	var select = document.createElement("select");
    	select.style.width = "70%";
    	select.setAttribute("name","paramtype");
    	select.appendChild(createOption('int','int'));
    	select.appendChild(createOption('long','long'));
    	select.appendChild(createOption('float','float'));
    	select.appendChild(createOption('boolean','boolean'));
    	select.appendChild(createOption('string','string'));
    	return select;
    }
    /**
     * 生成option标签
     * **/
    function createOption(value,text){
    	var option = document.createElement("option");
    	option.value=value;
    	option.innerHTML=text;
    	return option;
    }
    /**
     * table表中增加行
     * */
	function createRow(){
		getRowId();
		var table = document.getElementById('trContainer');
		var tr = document.createElement("tr");
		var paramName = document.createElement("td");
		var paramType = document.createElement("td");
		var paramvlaue = document.createElement("td");
		var deleteBt = document.createElement("td");
		paramName.innerHTML = '<input type=\"text\" name=\"paramName\"   class=\"inputText\"  style=\"width: 70%;\" validate=\"{required:true,maxlength:500}\" />';
		paramType.appendChild(createSelect());
		paramvlaue.innerHTML='<input type=\"text\"  name=\"paramvlaue\"  class=\"inputText\" style=\"width: 70%;\" validate=\"{required:true,maxlength:500}\" />';
		deleteBt.innerHTML='<a onclick=\"deleteJobParam(\''+ rowId +'\')\">删除</a>'; 
		tr.setAttribute("id",rowId); 
		tr.appendChild(paramName);
		tr.appendChild(paramType);
		tr.appendChild(paramvlaue);
		tr.appendChild(deleteBt);
		table.appendChild(tr);
	}
	/**
	 * 删除table中的行
	 * */
	function deleteJobParam(rowTag){
		var table = document.getElementById('jobParamTable');
		var tr = document.getElementById(rowTag);
		table.deleteRow(tr.rowIndex);
	}
    /**
    *检验jobclasss是否存在
    */
   function checkJobClass(){
   	    var url = __ctx + "/oa/system/quartz/checkSysJob.do?";
    	var jobClass = $("#jobClass").val();
    	if(jobClass!=null&&jobClass!=''){
    	   $.ajax({  
			type : "get",  
			url  : url,  
			data : {"jobClass":jobClass},   
			async : true,  
			success : function(data){
				var obj = new com.ibms.form.ResultMessage(data);
				if(obj.isSuccess()){
				   $.ligerDialog.warn(obj.getMessage(),'提示信息');
				}else{
				   $.ligerDialog.error(obj.getMessage(),'提示信息');
				}
		  },
		  failure : function() {
			    $.ligerDialog.error("验证失败，请联系管理员",'提示信息');
			    return ;
		  }
		});
      }else{
    	  	$.ligerDialog.warn("请输入任务类！",'提示信息');
    	  	return ;
    	}
   }
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">添加定时任务</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
				    <c:if test="${jobDetail.name ==null}">
						<div class="group">
							<a class="link save" id="dataFormSave" href="####">保存</a>
						</div>
						
					</c:if>
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysJobForm" method="post" action="save.do">
			    <input id="parameterJson" name="parameterJson" type="hidden" />
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0" type="main">
					<tr>
						<th width="20%">任务名称:</th>
						<td><input type="text" id="name" name="name" value="${jobDetail.name}" class="inputText" style="width: 70%" validate="{required:true,maxlength:50}" /></td>
					</tr>
					<tr>
						<th width="20%">任务类:</th>
						<td>
						  <input type="text" id="jobClass" name="jobClass" value="${jobDetail.jobClass.name}" class="inputText" style="width: 70%" validate="{required:true,maxlength:50}" />
						  <input type="button" id="checkJobClassBt" name="checkJobClassBt" value="验证" onclick="checkJobClass()">
						  </br>
						  <span style="color:red;">任务类必须是全路径，例如：com.cssrc.ibms.core.flow.job.ReminderJob</span>
						  </br>
						</td>
						<td> </td>
					</tr>

					<tr>
						<th width="20%">描述:</th>
						<td><input type="text" id="description" name="description" value="${jobDetail.description}" class="inputText" style="width: 80%;height: 100px" validate="{required:false,maxlength:200}" /></td>
					</tr>
				</table>
				<input type="button" class='button' value="添加" onclick="createRow()"/>
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main" id="jobParamTable">
						<tr >
							<th width="30%" style="text-align: center;">参数名称:</th>
							<th width="30%" style="text-align: center;">参数类型:</th>
							<th width="30%" style="text-align: center;">参数值:</th>
							<th width="10%" style="text-align: center;">参数值:</th>
						</tr>
						<tbody id="trContainer">					  
				   <c:choose>
				      <c:when test="${jobDetail.name !=null }">
				           <c:forEach items="${paramList}" var="item" varStatus="vs">
							<tr id='${vs.count}'>
								<td  style="width: 30%;"><input type="text" id="paramName" name="paramName" value="${item.name}"  class="inputText"  style="width: 70%;" validate="{required:true,maxlength:50}" /></td>
								<td  style="width: 30%;">
								      <select name="paramtype" style="width: 70%">
								         <c:choose>
								           <c:when test="${item.type=='int'}">
								              <option value="${item.type}" selected="selected">${item.type}</option>
								              <option value="varchar">long</option>
								              <option value="varchar">float</option>
								              <option value="varchar">boolean</option>
								              <option value="varchar">string</option>
								           </c:when>
								           <c:when test="${item.type=='long'}">
								              <option value="${item.type}" selected="selected">${item.type}</option>
								              <option value="varchar">int</option>
								              <option value="varchar">float</option>
								              <option value="varchar">boolean</option>
								              <option value="varchar">string</option>
								           </c:when>
								           <c:when test="${item.type=='float'}">
								              <option value="${item.type}" selected="selected">${item.type}</option>
								              <option value="varchar">int</option>
								              <option value="varchar">long</option>
								              <option value="varchar">boolean</option>
								              <option value="varchar">string</option>
								           </c:when>
								          <c:when test="${item.type=='boolean'}">
								              <option value="${item.type}" selected="selected">${item.type}</option>
								              <option value="varchar">int</option>
								              <option value="varchar">long</option>
								              <option value="varchar">float</option>
								              <option value="varchar">string</option>
								           </c:when>
								           <c:when test="${item.type=='string'}">
								              <option value="${item.type}" selected="selected">${item.type}</option>
								              <option value="varchar">int</option>
								              <option value="varchar">long</option>
								              <option value="varchar">float</option>
								              <option value="varchar">boolean</option>
								           </c:when>
								         </c:choose>
								      </select>
								</td>
							    <td  style="width: 30%;"><input type="text" id="paramvlaue" name="paramvlaue" value="${item.value}" class="inputText" style="width: 70%;" validate="{required:true,maxlength:50}" /></td>
							    <td  style="width: 30%;"><a onclick="deleteJobParam('${vs.count}')">删除</a></td>
							</tr>
				           </c:forEach>
				      </c:when>
				      <c:otherwise>
				      </c:otherwise>
				   </c:choose>
				     </tbody>
				</table>

			</form>

		</div>
	</div>
</body>
</html>
