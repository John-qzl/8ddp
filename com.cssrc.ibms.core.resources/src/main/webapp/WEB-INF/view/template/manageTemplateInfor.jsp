<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>模板基础信息页</title>
      <c:set var="ctx" value="${pageContext.request.contextPath}" />

   <script type="text/javascript" charset="utf-8" src="${ctx}/editor/jquery.js"></script>
   <%@include file="/commons/include/form.jsp" %>
<style media="screen">
body{padding: 0 6px;}
  input {
    width: 180px!important;
  }
  table textarea {
  	width:auto!important;
  }
   button[type="submit"] {
     width: 65px!important;
     height: 24px;
     border: none;
     cursor:pointer;
     margin: 5px 20px;
   }
  input.add {
    		background:url(${ctx}/dpImg/add.png) no-repeat;
    		width:62px!important;height:21px;
    		border: none!important;
    		padding:0!important;
        vertical-align: text-top;
    	}
    .radioTable td{
        text-align: center;
    }
    .radioTable label{display: block;}
    .radioTable img{margin: 6px 0;}
</style>
<script type="text/javascript">
	//修改时 默认选中的模板类型
	$(function(){
		if(!!"${type}"){
			$("#radio${type}").attr("checked","checked");
		}
		$("#html").val('${html}');
	});
	var flag=false;
	function Sign_add() {
		var input=document.createElement('input');
		input.setAttribute('type', 'text');
		input.setAttribute('value', '');
		input.setAttribute('name', 'sign')
		document.getElementById('3').insertBefore(input, document.getElementById('add1'));
	}
	function Status_add() {
		var input=document.createElement('input');
		input.setAttribute('type', 'text');
		input.setAttribute('value', '');
		input.setAttribute('name', 'status')
		document.getElementById('4').insertBefore(input, document.getElementById('add2'));
	}

	// 检查表编号校验
	function templateCodeCheck(){
   		var id=$("#id").val();
   		if(!id){
   			$.ligerDialog.error("请输入检查表编号");
   			return false;
   		}
   		$.ajax({
			type:"post",
			data:{"id":id,"MID":"${MID}"},
			url: "${ctx}/template/manage/templateCodeCheck.do",
			async:false,
			success:function(data){
				if(data.success=="false"){
	   				flag=false;
	   				$('#msg').css({
	   					'color':'red'
	   				})
	   				$("#msg").text(data.msg);
	   			}
	   			if(data.success=="true"){
	   				flag=true;
	   				$('#msg').css({
	   					'color':'green'
	   				})
	   				$("#msg").text("可以使用");
	   			}
			}
		})
   	}
	function templateTypeCheck(){
		var type=$("#type").val();
		if(!type){
			$.ligerDialog.error("请输入检查表种类");
			return false;
		}
		$.ajax({
			type:"post",
			data:{"templateType":type},
			url: "${ctx}/template/manage/templateTypeCheck.do",
			async:false,
			success:function(data){
				if(data.success=="false"){

					flag=false;
					$('#TypeMsg').css({
						'color':'red'
					})
					$("#TypeMsg").text(data.msg);
				}
				if(data.success=="true"){
					flag=true;
					$('#TypeMsg').css({
						'color':'green'
					})
					$("#TypeMsg").text("可以使用");
				}
			}
		})
	}
	function templateNameCheck(){
		var name=$("#name").val();
		
		if(!name){
			$.ligerDialog.error("请输入检查表名称");
			return false;
		}
		$.ajax({
			type:"post",
			data:{"templateName":name},
			url: "${ctx}/template/manage/templateNameCheck.do",
			async:false,
			success:function(data){
				if(data.success=="false"){

					flag=false;
					$('#msg').css({
						'color':'red'
					})
					$("#warmMsg").text(data.msg);
				}
				if(data.success=="true"){
					flag=true;
					$('#warmMsg').css({
						'color':'green'
					})
					$("#warmMsg").text("可以使用");
				}
			}
		})
	}
	
	function submitFunc(){
		var type=$("input[name='formtype']:checked").val();
		if(!type){
			$.ligerDialog.error("请选择一种模板类型","");
			return;
		}
		templateCodeCheck();
		templateNameCheck();
		if(!!flag){
			$("#type").val(type);
			$('#templateBaseInfor').submit();
		}
	}
	
	function checkRadio(id,val){
		$("#"+id).attr("checked","checked");
	}
</script>
</head>
<body>
	<form action="${ctx}/template/manage/templateBaseSave.do" method="post"  id="templateBaseInfor">
        <fieldset class="layui-elem-field">
            <legend>模板基础信息</legend>
            <div class="layui-field-box">
                <table class="layui-table">
                  <tbody>
                    <tr>
                      <td><span>检查表编号：</span></td>
                      <td><input id="id"  name="id" type="text" value="${id}"  onblur="templateCodeCheck()"/><span id="msg" /></td>
                    </tr>
                    <tr>
                      <td><span>检查表名称：</span></td>
                      <td><input id="name" name="name" type="text" value="${name}" onblur="templateNameCheck()"/><span id="warmMsg" /></td>
                    </tr>
                              <tr>
                      <td><span>检查表种类：</span></td>
                      <td><input id="type" name="type" type="text" value="${modelType}" onblur="templateTypeCheck()"/><span id="TypeMsg" />
                      <span><a>注:输入检查表种类,常规模板:1,功能性模板:2,验收报告模板:6</a></span>
                      </td>
                    </tr>
                    <tr>
                      <td><span>签署：</span></td>
                      <td id="3">
                      	<c:choose>
                      		<c:when test="${not empty sign}">
                      			<c:forEach var="str" items="${sign}">
                      				<input name="sign" type="text" value="${str}"/>
                      			</c:forEach>
                      		</c:when>
                      		<c:otherwise>
                      			<input name="sign" type="text" value="操作人员"/>
                        			<input name="sign" type="text" value=""/>
                        			<input name="sign" type="text" value=""/>
                      		</c:otherwise>
                      	</c:choose>

                        <input id="add1" class="add" type="button" value="" onClick="Sign_add()"/>
                      </td>
                    </tr>
                    <tr>
                      <td><span>检查状态：</span></td>
                      <td id="4">
                      	<c:choose>
                      		<c:when test="${not empty status}">
                      			<c:forEach var="str" items="${status}">
                      				<input name="status" type="text" value="${str}"/>
                      			</c:forEach>
                      		</c:when>
                      		<c:otherwise>
                      			<input name="status" type="text" value=""/>
                        			<input name="status"  type="text" value=""/>
                        			<input name="status"  type="text" value=""/>
                      		</c:otherwise>
                      	</c:choose>
                        <input id="add2" type="button" class="add" value="" onClick="Status_add()"/>
                      </td>
                    </tr>
                    <tr>
                      <td><span>注意事项：</span></td>
                      <td colspan="5"><textarea rows="1" cols="100" name="attention" >${attention }</textarea></td>
                    </tr>

                  </tbody>
                </table>
            </div>
        </fieldset>
        <fieldset class="layui-elem-field">
            <legend>模板类别</legend>
            <table class="radioTable">
           	<tr>
           		<td>
                     <label for="radio1">极简</label>
           			<image type="image" width="130px" height="90px" src="${ctx}/dpImg/simple.png" onclick="checkRadio('radio1','1')"/>
           			<input type="radio" id="radio1" name="formtype" value="1"/>
           		</td>
           	</tr>
            </table>
        </fieldset>
        
   <div class="panel-toolbar" style="background:none;">
     <div class="toolBar">
        <div class="group"><a class="link save" href="####" onclick="submitFunc()">保存</a></div>
        <c:if test="${not empty formTempId}">
        	<div class="group"><a class="link ajaxSearch" href="####" onclick="submitFunc(${formTempId})">返回表单模板设计器</a></div>
        </c:if>
     </div>
   </div>
	<input type="hidden" id="moduleId" name="moduleId" value="${moduleId}"/>
	<input type="hidden" id="MID" name="MID" value="${MID}"/>
	<input type="hidden" id="folderId" name="folderId" value="${folderId}"/>
	<input type="hidden" id="html" name="html" value=""/>
	<input type="hidden" id="type" name="type" value="${type}"/>
	</form>
</body>
</html>
