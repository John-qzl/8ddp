<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>用户列表</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	var isSingle='${isSingle}';
	forbidF5("Chrome");//禁止刷新页面
	$(function(){
		$("#sysUserItem").find("tr").bind('click', function() {
			if(isSingle=='true'){
				var rad=$(this).find('input[name=userData]:radio');
				rad.attr("checked","checked");
			}else{
				var ch=$(this).find(":checkbox[name='userData']");
				window.parent.selectMulti(ch);
			}
		});
		
	});
	
	//单选情况下选中目标便返回信息并关闭页面
	function singleCheck(){
		window.parent.selectUser();
	}
	function btnSearch(){
		debugger;
		var dataType=$("input[name='dataType']").val();
		var dataId=$("input[name='dataId']").val();
		var fullName=$("input[name='Q_fullname_SL']").val();
		
		if(dataType=="modelManageFilter"||dataType=="roleResFilter"){
			$.ajax({
			    url: __ctx+"/model/user/role/isExist.do",
			    data:{
			    	dataType:dataType,
			    	dataId:dataId,
			    	fullName:fullName
			    },
			    async:false,
			    success:function(data){
			    	if(data.exist=="1"){
			    		$.ligerDialog.warn("该用户已添加",'提示信息');
			    	}
			    	else{
			    		$('#searchForm').submit();
			    	}
			    }
			});
		}else{
			$('#searchForm').submit();
		}
	}
    document.onkeydown=keyDownSearch;
   
    function keyDownSearch(e) { 
    	debugger;
        // 兼容FF和IE和Opera 
        var theEvent = e || window.event; 
        var code = theEvent.keyCode || theEvent.which || theEvent.charCode; 
        if (code == 13) {  
        	btnSearch();
            return false; 
        } 
        return true; 
    }
</script>
<style>
div.panel-top{
	border-top: 5px solid #f5f5f5;
	background: #fff;
}
.panel-search .title,.panel-search .drop{
	background: #fff;
}
a.button{
	border: 1px solid #347EFE;
	color: #347EFE;
    background: #fff;
}
a.button:hover{
	background: #347EFE;
	color: #fff;
}
.label{
	margin-right: 5px;
	color: #333;
    font-weight: bold;
}
input.inputText {
	border: 1px solid #D9D9D9;
	color: #666;
}
span.red{
	right: 0px;
    top: 0px;
}
.table-grid .even{
	background-color: #fff;
}
</style>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="panel-search">
			<form id="searchForm" method="post" action="${ctx}/oa/system/sysUser/selector.do" >
				<div class="row">
					<input type="hidden" name="isSingle" value="${isSingle }">
					<input type="hidden" name="type" value="${type }">
					<input type="hidden" name="typeVal" value="${typeVal }">
					<input type="hidden" name="dataId" value="${dataId}">
					<input type="hidden" name="dataType" value="${dataType}">
					<input type="hidden" name="relvalue" value="${relvalue}">
					<span class="label" >姓名:</span><input size="14" type="text" name="Q_fullname_SL"  class="inputText" style="width:25%;" value="${param['Q_fullname_SL']}"/>
					&nbsp;<a href="javascript:;" onclick="btnSearch()" class='button'><span>查询</span></a>
				</div>
			</form>
		</div>
	</div>
	<div class="panel-body">
	   	<c:if test="${isSingle==false}">
	    	<c:set var="checkAll">
				<input onclick="window.parent.selectAll(this);" type="checkbox" />
			</c:set>
		</c:if>
		<display:table  name="sysUserList" id="sysUserItem" requestURI="selector.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
			<display:column title="${checkAll}" media="html" style="width:30px;">
					<c:choose>
						<c:when test="${isSingle==false}">
							<input onchange="window.parent.selectMulti(this);" type="checkbox" class="pk" name="userData" value="${sysUserItem.userId}#${sysUserItem.fullname}#${sysUserItem.email}#${sysUserItem.mobile}#${sysUserItem.orgName}">
						</c:when>
						<c:otherwise>
							<input type="radio" class="pk" name="userData" onclick="singleCheck()" value="${sysUserItem.userId}#${sysUserItem.fullname}#${sysUserItem.email}#${sysUserItem.mobile}#${sysUserItem.orgName}">
						</c:otherwise>
					</c:choose>
			</display:column>
			<display:column  style="width:60px;" property="fullname" title="姓名" sortable="true" sortName="fullname"></display:column>
			<display:column  title="所属组织" sortable="false" >
				<c:choose>
					<c:when test="${sysUserItem.orgName==''}"><span class="red">未设置</span></c:when>
					<c:otherwise>${sysUserItem.orgName}</c:otherwise>
				</c:choose>
			</display:column>
		</display:table>
		<ibms:paging tableId="sysUserItem" showExplain="false"/>
	</div>	
</div>	
</body>
</html>


