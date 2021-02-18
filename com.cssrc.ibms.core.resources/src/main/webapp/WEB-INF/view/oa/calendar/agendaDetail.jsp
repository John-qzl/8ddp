<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日程明细</title>
<%@include file="/commons/portalCustom.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<style type="text/css">
	.tab_menu li{
		float:left;
		height:40px;
		line-height:43px;
		text-align:center;
		cursor:pointer;
		list-style-type:none;
		font-size:14px;
	}
	.tab_menu-selected{
		border-color:#168eec;
		color:#168eec
	}
	li{
		display:list-item;
	}
</style>
</head>
<body>
	<div id="toolbar1" class="mini-toolbar">
				<input id="agendaId" name="agendaId" class="mini-hidden" value="${agendaId}" />
	        	<div style="width:314px;float:left">
	        		<ul class="tab_menu" style="padding-left:5px;padding-top:1px;display:initial">
		        		<li class="li1 tab_menu-selected" id="detail" ><a>详细信息</a><span>|</span></li>
		        		<li class="li2" id="msg" ><a>相关交流</a></li>
	        		</ul>
	        	</div>
	        	
		        <div style="width:257px;float:right">
		        	
		           	 	<a class="mini-button" iconCls="icon-remove" plain="true" onclick="onDelete()">删除</a>
		            	<a class="mini-button" iconCls="icon-ok" plain="true" onclick="onFinish()">完成</a>
		           		<a class="mini-button" iconCls="icon-edit" plain="true" onclick="onEdit()">编辑</a>
		            
		        </div>
	</div>
	
	<div id="form1" class="form-outer">
		<iframe id="agendaframe" name="agendaframe" src="${ctx}/oa/calendar/agenda/get.do?agendaId=${agendaId}" height="100%" width="100%">
		</iframe>
	</div>
	
	<script type="text/javascript">
		$(function() {
			$('.tab_menu li').click(function(){
				var liindex = $('.tab_menu li').index(this);
				$(this).addClass('tab_menu-selected').siblings().removeClass("tab_menu-selected");
				var currentId = $(this).attr("id");
				if(currentId == "detail"){
					$("#agendaframe").attr("src", "${ctx}/oa/calendar/agenda/get.do?agendaId=${agendaId}");
				}else{
					$("#agendaframe").attr("src", "${ctx}/oa/calendar/agendaMsg/get.do?agendaId=${agendaId}");
				}
			});
		})
	
         mini.parse();
         function onDelete(){
             if (confirm("确定删除当前记录？")) {
                var id=$("#pkId").val();
                var pk = $("#agendaId").val();
                
                $.ajax({
                    url: "${ctx}/oa/calendar/agenda/del.do?agendaId="+pk,
                    success: function() {
                       //关闭当前窗口
                    },
                    error: function(err) {
                    	//报错窗口
                    }
                });
            }
         };
         
         function onFinish(){
             var pk = $("#agendaId").val();
        	 $.ajax({
                 url: "${ctx}/oa/calendar/agenda/finish.do?agendaId="+pk,
                 success: function(result) {
                	 dialog = frameElement.dialog;
					 dialog.close();
                 },
                 error: function(err) {
                 	//报错窗口
                 }
             });
         };
         
         function onEdit(){
        	 $("#agendaframe").attr("src", "${ctx}/oa/calendar/agenda/edit.do?agendaId=${agendaId}");
         };
  </script>
</body>
</html>