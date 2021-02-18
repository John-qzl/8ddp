<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>内部消息</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerTab.js" ></script>
	<script type="text/javascript">
		$(function (){
			var h=$('body').height()-30;
	        $("#tabMyInfo").ligerTab({         	
	           	height:h
	         });
	        $("#A").height(h);
	        $("#B").height(h);
	        $("iframe[action]").each(function() {
	        	$(this).attr('src', $(this).attr('action'));
	        });
		});
	</script>
</head>
<body>
     <div id="tabMyInfo" style="overflow:hidden; border:1px solid #A3C0E8;position:relative;">                
        <div title="已发消息" tabid="fmegs" icon="${ctx}/styles/default/images/icon/home.png">
	       <iframe id="A" action="list.do?userId=${userId}" frameborder="0" style="width: 100%;position:relative;" scrolling="no" ></iframe>		              		 
        </div> 
       
        <div title="已接消息" tabid="jmegs" icon="${ctx}/styles/default/images/nav-sales.png">	         			    
		    <iframe id="B" action="${ctx}/oa/system/messageReceiver/list.do?receiverId=${userId}" frameborder="0" style="width: 100%;position:relative;" scrolling="no" ></iframe>	 
		</div>                 
    </div> 		
</body>
</html>