<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>

<head>

<link href='${ctx}/styles/fullcalendar/fullcalendar.css' rel='stylesheet' />

<link href='${ctx}/styles/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />

<script src='${ctx}/js/lib/jquery.min.js'></script>

<script src='${ctx}/js/lib/jquery-ui.custom.min.js'></script>

<script src='${ctx}/js/fullcalendar/fullcalendar.min.js'></script>

<script>

	$(document).ready(function() {

		$('#calendar').fullCalendar({

			header: {

				left: 'prev,next today',

				center: 'title',

				right: 'month,agendaWeek,agendaDay'

			},
			monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
			monthNamesShort: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
			dayNames: ['周日','周一','周二','周三','周四','周五','周六'],
			dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			today:["今天"],
			editable: false,
			firstDay : 1,
			buttonText:{
				today:'今天',
				month: '月',
				week: '周',
				day: '日',
				prev: '上一月',
				next: '下一月'
			},
			allDaySlot:false,
            allDayDefault:false,
			viewDisplay: function(view){
				if(view.name=="month"){
					$("#calendar").fullCalendar('removeEvents');
				}
				var a =new Date();
				var fstart = $.fullCalendar.formatDate(view.start,"yyyy-MM-dd HH:mm:ss");
				var fend = $.fullCalendar.formatDate(view.end,"yyyy-MM-dd HH:mm:ss");
				$("#calendar").fullCalendar('removeEvents');
				$.ajax({
						url:"${ctx}/oa/calendar/agenda/calendar.do",
						dataType: 'json',
						data:{"startTime":fstart,"endTime":fend},
						success: function(data){
							for( var i=0;i<data.length;i++){
								var obj = new Object();
								obj.id = data[i].id;
								obj.title = data[i].title;
								obj.start = data[i].start;
								obj.end = data[i].end;
								$("#calendar").fullCalendar('renderEvent',obj,true);
							}
						}
				});
			},

		});

		

	});



</script>

<style>



	body {

		margin-top: 40px;

		text-align: center;

		font-size: 14px;

		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;

		}



	#calendar {

		width: 900px;

		margin: 0 auto;

		}



</style>

</head>

<body>

<div id='calendar'></div>

</body>

</html>

