<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/get.jsp" %>
<title>待办事宜</title>

<script type="text/javascript">
//更多
function moreFile(){
	var c = {
			id : '${funcitonId}',
			text:'${title}',
			attributes:{
				url:'${moreUrl}',
				iconCls:'${iconCls}'
			}
	}
	if('${moreUrl}'.indexOf('/js/')>=0){
	     App.clickTopSeaTab(c,null,null,null);
	}else{
		 App.clickTopTabUrl(c);
	}
}
//刷新
function fileRefresh(){
   var items = Ext.getCmp("DeskTop").items;
   for (var i = 0; i < items.getCount(); i++) {  
        var c = items.get(i);  
        c.items.each(function(portlet,index) {
                    if(portlet.getId()=='p'+ ${portalId}){
                       portlet.getUpdater().refresh();
                    }
                });  
    }
   
}
//下载
function downLoad (a) {
    var h=screen.availHeight-200;
	var w=screen.availWidth-50;
	var vars="top=50,left=50,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
	window.open(__ctx + "/oa/system/sysFile/download.do?fileId=" + a,"_blank",vars);
}

</script>
</head>
<body >
	<div class="panel" >
		<div class="panel-body">
			    <display:table name="taskList" id="fileItem" requestURI="sharedFile.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
					<display:column title="文件名"   style="text-align:left;" >
						${fileItem.filename}.${fileItem.ext}
					</display:column>
					<display:column  title="创建时间"  style="text-align:left">
					    <fmt:formatDate value="${fileItem.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</display:column>
					<display:column title="上传者"   property="creator" >
					</display:column>
					<display:column title="下载"  >
				    	<a href="javascript:downLoad('${fileItem.fileId}');">下载</a>
					</display:column>
				</display:table>
		</div>
		 
	</div>
	<div style="float: inherit;"><a id="fileMore" href="##" onclick="moreFile()" style="float: right; ">更多(more)</a></div>
    <!--<div><a href="##" onclick="fileRefresh()">刷新</a></div>-->
</body>
</html>


