<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" charset="utf-8" src="jquery.js"></script>
<style media="screen">
  input {
    width: 180px!important;
  }
  table textarea {
  	width:auto!important;
  }
  button[type="submit"] {
    background: url(../dpImg/submit.png) no-repeat;
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
</style>
<script type="text/javascript">
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

	function checkFormid(){
//
    		var id=$("#id").val();
    		var url="${ctx}/dp/form/checkFormid.do";
    		var params={id:id};
    		$.post(url,params,function(data){
    			/* console.log(data); */
    			if(data.success=="false"){
    				//$.ligerDialog.success(data.msg,"提示");
    				$("#msg").text(data.msg);
    				flag=false;
    			}
    			if(data.success=="true"){
    				flag=true;
    			}
    		});
    	}
    function checkFormName(){
//
      var name=$("#name").val();
      var url="${ctx}/dp/form/checkFormName.do";
      var params={name:name};
      $.post(url,params,function(data){

        if(data.success=="false"){

          $("#warmMsg").text(data.msg);
          flag=false;
        }
        if(data.success=="true"){
          flag=true;
        }
      });
    }
	function checkForm() {
		return flag;
	}
</script>
</head>
<body>
	<form action="${ctx}/dp/form/checkForm.do" method="post" onsubmit="return checkForm() ">

			<!-- <div>
		   	 <p><span>检查表编号：</span>&nbsp;<input id="id"  name="id" type="text"  onblur="checkFormid()"/><span id="msg" style="color: red"></span></p>
		   	 <p><span>检查表名称：</span>&nbsp;<input name="name" type="text" value="XX状态检查表"/></p>
		   	 <p id="3"><span>签署：</span>&nbsp;&nbsp;&nbsp;&nbsp;<input name="sign" type="text" value="操作人员"/>&nbsp;<input name="sign" type="text" value="确认人员1"/>&nbsp;<input name="sign" type="text" value="确认人员2"/>&nbsp;<input id="add1" type="button" value="添加" onClick="Sign_add()"/></p>
		   	 <p id="4"><span>检查状态：</span>&nbsp;&nbsp;<input name="status" type="text" value="湿度"/>&nbsp;<input name="status"  type="text" value="温度"/>&nbsp;<input name="status"  type="text" value="频率"/>&nbsp;<input name="status"  type="text" value="次数"/>&nbsp;<input id="add2" type="button" value="添加" onClick="Status_add()"/></p>
		   	 <p><span>注意事项：</span>&nbsp;&nbsp;<textarea rows="1" cols="100" name="attention" ></textarea>  </p>
		   </div> -->


   <table class="layui-table">
     <tbody>
       <tr>
         <td><span>检查表编号：</span></td>
         <td><input id="id"  name="id" type="text"  onblur="checkFormid()"/><span id="msg" style="color: red"/></td>
       </tr>
       <tr>
         <td><span>检查表名称：</span></td>
         <td><input name="name" type="text" onblur="checkFormName()" value="XX状态检查表" /><span id="warmMsg" style="color: red"/></td>
       </tr>
       <tr>
         <td><span>签署：</span></td>
         <td id="3">
           <input name="sign" type="text" value="操作人员"/>
           <input name="sign" type="text" value="确认人员1"/>
           <input name="sign" type="text" value="确认人员2"/>
           <input id="add1" class="add" type="button" value="" onClick="Sign_add()"/>
         </td>
       </tr>
       <tr>
         <td><span>检查状态：</span></td>
         <td id="4">
           <input name="status" type="text" value="湿度"/>
           <input name="status"  type="text" value="温度"/>
           <input name="status"  type="text" value="频率"/>
           <input name="status"  type="text" value="次数"/>
           <input id="add2" type="button" class="add" value="" onClick="Status_add()"/>
         </td>
       </tr>
       <tr>
         <td><span>注意事项：</span></td>
         <td colspan="5"><textarea rows="1" cols="100" name="attention" ></textarea></td>
       </tr>

     </tbody>
   </table>
   <button type="submit"></button>






   <%
   		String pid=request.getParameter("pid");
   		String fcid=request.getParameter("fcid");
   %>
   	<input type="hidden" id="pid" name="pid" value="<%=pid%>"/>
	<input type="hidden" id="fcid" name="fcid" value="<%=fcid%>"/>
	</form>
</body>
</html>
