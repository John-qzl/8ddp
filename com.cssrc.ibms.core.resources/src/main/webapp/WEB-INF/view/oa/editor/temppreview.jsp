<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@include file="/commons/include/color.jsp"%>
<link rel="stylesheet" href="${ctx}/layui/css/layui.css">
<link rel="stylesheet" href="${ctx}/styles/default/css/web.css">
<script type="text/javascript" src="${ctx}/layui/layui.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/editor/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-all.css">
<f:js pre="jslib/lang/common" ></f:js>
<style media="screen">
.radioTable label {
    display: block;
}
.radioTable td {
    text-align: center;
}
.radioTable img{
    margin: 6px 0;
}
input {
  width: 180px!important;
}
tr.dpTrFront:hover {
    background-color: #fef897!important;
}
tr.dpTrTitle:hover {
    background-color: #1e9fff!important;
}
</style>
<title>预览页</title>
<script type="text/javascript">
	var dialog = window;// 调用页面的dialog对象(ligerui对象)
	if (frameElement) {
		dialog = frameElement.dialog;
	}
	//修改时 默认选中的模板类型
	$(function(){

		if(!!"${type}"){
			$("#radio${type}").attr("checked","checked");
		}
	});
	function back() {

		var html=$("#html").html();
		//var html='${html}';
		$('#content').val(html);
		$('#htmlform').attr('action','${ctx}/dp/form/backTableTemp.do');
		$('#htmlform').submit();
	}

	function save(){

		var htmlres='${html}';
		var MID=$("#MID").val();
		var type=$("#type").val();
		var url="${ctx}/dp/form/saveFormTemplate.do";
		var params={htmlres:htmlres,MID:MID,type:type};
		window.top.$.ligerDialog.confirm('是否确认保存表单模板',function (rtn){
			if(rtn){
				$.post(url,params,function(data){
					if(data.success=="true"){
								$.ajax({
									  type: "POST",
								      url:"${ctx}/dp/form/saveCreate.do",
									  data:{MID : MID},
								      async:false,
								      success:function(){
								    	   
								      }   		  
								});

						window.top.$.ligerDialog.confirm('数据保存成功，是否关闭该页面',function (rtn){
							if(rtn){
								/* 
								var _document = window.top.document.getElementById('home')
								.contentWindow.document.getElementById('form_table_frame')
								.contentWindow.document;
								$(_document).find('a#treeFresh').trigger('click');
								 */
								dialog.close();
							}
						});
						
					}else{
						$.ligerDialog.error(data.msg,"保存失败");
					}
				});
			}
		});
	}

</script>
</head>
<body>
	<form id="htmlform"action="" method="post" >
        <fieldset class="layui-elem-field">
            <legend>表单信息</legend>
            <div class="layui-field-box">
                <table class="layui-table">
                 <tbody>
                   <tr>
                     <td><span>检查表编号：</span></td>
                     <td>
                        <input class="layui-input" id="id" name="id" type="text" value="${id}" readonly="readonly"/>
                     </td>
                   </tr>
                   <tr>
                     <td><span>检查表名称：</span></td>
                     <td><input class="layui-input" name="name" type="text" value="${name}" readonly="readonly"/></td>
                   </tr>
                   <tr>
                     <td><span>签署：</span></td>
                     <td id="3">
                       <c:forEach items="${signs}" var="sign">
                            <input class="layui-input" name="signs" type="text" value="${sign}" readonly="readonly"/>
                       </c:forEach>
                     </td>
                   </tr>
                   <tr>
                     <td><span>检查状态：</span></td>
                     <td id="4">
                       <c:forEach items="${status}" var="status">
                            <input class="layui-input" name="status" type="text" value="${status}" readonly="readonly"/>
                       </c:forEach>
                     </td>
                   </tr>
                   <tr>
                     <td><span>注意事项：</span></td>
                     <td ><textarea class="layui-textarea" rows="1" cols="100" name="attention" readonly="readonly">${attention}</textarea></td>
                   </tr>
                 </tbody>
               </table>

               <table class="radioTable">
                    <tr>
                        <td>
                             <label for="radio1">极简</label>
                            <image type="image" width="130px" height="90px" src="${ctx}/dpImg/simple.png" onclick="checkRadio('radio1','1')"/>
                            <input type="radio" id="radio1" name="formtype" value="1" readonly="readonly"/>
                        </td>
                        <%--<td>--%>
                             <%--<label for="radio2">极简+页眉</label>--%>
                            <%--<image type="image" width="130px" height="90px" src="${ctx}/dpImg/sfront.png" src="" onclick="checkRadio('radio2','2')"/>--%>
                            <%--<input type="radio" id="radio2" name="formtype" value="2" readonly="readonly"/>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                             <%--<label for="radio3">极简+页脚</label>--%>
                            <%--<image type="image" width="130px" height="90px" src="${ctx}/dpImg/stail.png" onclick="checkRadio('radio3','3')"/>--%>
                            <%--<input type="radio" id="radio3" name="formtype" value="3" readonly="readonly"/>--%>
                        <%--</td>--%>
                        <%--<td >--%>
                             <%--<label for="radio4">标准表</label>--%>
                            <%--<image type="image" width="130px" height="90px" src="${ctx}/dpImg/standard.png" onclick="checkRadio('radio4','4')"/>--%>
                            <%--<input type="radio" id="radio4" name="formtype" value="4" readonly="readonly"/>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                             <%--<label for="radio5">组合表</label>--%>
                            <%--<image type="image" width="130px" height="90px" src="${ctx}/dpImg/complex.png" onclick="checkRadio('radio5','5')"/>--%>
                            <%--<input type="radio" id="radio5" name="formtype" value="5" readonly="readonly"/>--%>
                        <%--</td>--%>
                    </tr>
                </table>


               </div>
               </fieldset>


               <fieldset class="layui-elem-field">
                   <legend>表单模板</legend>
                   <div class="layui-field-box">

                        <div id="html" class="layui-field-box">
                       		${html}
                       	</div>
                    </div>
               </fieldset>








   	<div class="panel-toolbar">
		<div class="group"><a href="####" class="link save" onClick="save()">确认并保存</a></div>
		<div class="group"><a href="####" class="link back" onClick="back()">返回</a></div>
	</div>
   	<input type="hidden" id="pid" name="pid" value="${pid }"/>
	<input type="hidden" id="fcid" name="fcid" value="${fcid }" />
	<input type="hidden" id="MID" name="MID" value="${MID }" />
	<input type="hidden" id="type" name="type" value="${type}" />
	<input type="hidden"  id="content" name="content" value=""/>
	</form>

</body>
<script type="text/javascript">
$('tr').hover(function () {
    console.log(11111);
    return false;
})



</script>
</html>
