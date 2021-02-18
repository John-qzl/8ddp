<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/commons/dynamic.jspf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Calendar</title>
<link rel="stylesheet" href="${ctx}/styles/home/home.css">
<link rel="stylesheet" href="${ctx}/styles/home/calendar.css">
<script type="text/javascript" src="${ctx}/js/lib/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/dateUtil.js"></script>
<script src="${ctx}/js/fullcalendar/calendar.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/jquery.mousewheel.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/jquery.rollbar.min.js"></script>	
</head>
<body>
	<div class="bd-right fr clearFix">
	
			<div class="block-calendar-left fl" >
				<table id="Calendar" width="100%" border=0>
					<tr>
						<td>
							<div class="pr clearFix" style="padding:5px 0 10px 0;height: 24px;text-align: center;top:18px;border-bottom: 1px solid #ddd;margin:0 5px 20px 5px;">
								<div class="LeftArrow fl" id="prevbtn" title="上一个月" onclick="prev(this)"></div>
								<div class="RightArrow fr" id="nextbtn" title="下一个月" onclick="next(this)"></div>
								<div class="changeMonth " id="showDate"></div>
							</div>
							<table id="LDay" width="100%"></table>
						</td>
					</tr>
				</table>
			</div>
		
			<div class="block-calendar-right fr">
				<div  class="hand" align="center" onclick="todayClick()" title="今天">
					<div id="currentDay"></div>
					<div id="showDateII"></div>
					<div id="currentWeekDay" ></div>
				</div>
				
				<div id="planDataEvent" class="planDataEvent">
					<div id="planDataEventchd">
					</div>
				</div>
				
				<a class="addWorkPlan" href="####" onclick="doAdd()" title="添加">&nbsp;</a>
			</div>
	</div>
</body>
</html>