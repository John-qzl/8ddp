<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>首页</title>
	<link rel="stylesheet" href="${ctx}/styles/home/home1.css">
	<link rel="stylesheet" href="${ctx}/styles/home/responsiveslides.css">	
	<link rel="stylesheet" href="${ctx}/styles/home/calendar.css">
	<style type="text/css">

.rollbar-path-vertical { width: 5px;  right:3px;}
.rollbar-path-vertical, .rollbar-path-horizontal {
	box-shadow: none;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
} 
.rollbar-handle {
    background-color: #555;
}
	</style>


	<script type="text/javascript">
	
	if(document.documentElement.clientWidth < 780) {
		document.documentElement.className += 'minW';
	}
	
	</script>

	<script type="text/javascript" src="${ctx}/js/lib/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.rollbar.min.js"></script>
	<script src="${ctx}/js/home/responsiveslides.js"></script>
	<script src="${ctx}/js/home/home.js"></script>
	<!-- jquery扩展代码 -->
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script>
		$(function() {
			//待办,草稿，已办tab
			/* $("#basic-tabMenu").click(function(){
				
			})
			$("").load("***.do?");
			
			$("").html(${html}); */
			//初始化加载待办任务列表
			/* loadContent("pendingMatters"); */
			
			
			$('.task-tabMenu-ul li').mouseover(function(){
				var liindex = $('.task-tabMenu-ul li').index(this);
				$(this).addClass('basic-tabMenu-selected').siblings().removeClass("basic-tabMenu-selected");
				$('.basic-tabCont-wrap div.task-list').eq(liindex).fadeIn(150).siblings('div.task-list').hide();
			});
		});
		//加载列表数据
/* 		function loadContent(alias){
			var url="${ctx}/oa/system/sysIndexColumn/getData.do";
			var params={ params : '', alias : alias };
			$.ajax({
				type : "POST",
				context:self,
				url : url,
				data : params,
				success : function(data) {
					$('.task-list').html(data);
				}
			
			});
		} */
		
/* 		function loadContent(obj){

			$('.basic-tabMenu').removeClass("basic-tabMenu-selected");
			$(obj).parent().addClass("basic-tabMenu-selected");
		} */
		
		
		
	</script>
	
</head> 
<body style="overflow:hidden" class="viewFramework-product-body" >
	<div class="content mCustomScrollbar clearFix" id="content">
	
		<!-- 首页轮播 [[ -->
		<div class="bd-left fl">
			<div class="callbacks_container">
			  <ul class="rslides callbacks" id="slider">
				<li>
					<a href="####">
						<img src="${ctx}/styles/images/home/1.jpg" alt="">
						<p class="caption">辽宁舰首次以航母编队形式赴南海开展科研训练</p>
					</a>
				</li>
				<li>
					<a href="####">
						<img src="${ctx}/styles/images/home/2.jpg" alt="">
						<p class="caption">辽宁舰首次以航母编队形式赴南海开展科研训练</p>
					</a>
				</li>
				<li>
					<a href="####">
						<img src="${ctx}/styles/images/home/3.jpg" alt="">
						<p class="caption">辽宁舰首次以航母编队形式赴南海开展科研训练</p>
					</a>
				</li>
			  </ul>
			</div>
		</div>
		<!-- ]] 首页轮播 -->
		
		<!-- 新闻公告 [[ -->
		<div class="bd-right fr">
			<div class="block">
				<div class="basic-tabMenu-wrap clearFix">
					<ul class="basic-tabMenu-ul fl clearFix">
						<li class="basic-tabMenu basic-tabMenu-selected">
							<a>新闻公告</a>
						</li>
					</ul>
					
					<div class="fr clearFix">
						<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
						<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
					</div>
					
				</div>
				
				<div class="basic-tabCont-wrap clearFix">
					<ul class="item-list-box">
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						<li class="item-li">
							<a href="####" class="fl item-li-link">辽宁舰编队进南海训练 中方回应称有航行自由</a>
							<span class="fr item-li-date">2016-12-31</span>
						</li>
						
					</ul>
				</div>
			
			</div>
		</div>
		<!-- ]] 新闻公告 -->
	
		<!-- 日常办公 [[ -->
		<div class="bd-left fl clear">
			<div class="block-II">
				<div class="basic-tabMenu-wrap clearFix">
					<ul class="basic-tabMenu-ul fl clearFix">
						<li class="basic-tabMenu basic-tabMenu-selected">
							<a>日常办公</a>
						</li>
					</ul>
					<div class="fr clearFix">
						<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
						<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
					</div>
				</div>
				<div class="basic-tabCont-wrap clearFix">
					<div id="rcbg">
					<ul class="cubeBox">
						<li class="fl base-col04">
							<a href="####" class="bgef7373">
								<div class="cubeBox-icon cubeBox-jp"></div>
								<div class="cubeBox-text">机票申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg63d4fe">
								<div class="cubeBox-icon cubeBox-qj"></div>
								<div class="cubeBox-text">请假申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg6e6eff">
								<div class="cubeBox-icon cubeBox-cc"></div>
								<div class="cubeBox-text">出差申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bgf76fff">
								<div class="cubeBox-icon cubeBox-yc"></div>
								<div class="cubeBox-text">用车申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bgc49d28">
								<div class="cubeBox-icon cubeBox-yz"></div>
								<div class="cubeBox-text">印章使用申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bgb0ca23">
								<div class="cubeBox-icon cubeBox-yw"></div>
								<div class="cubeBox-text">业务招待费申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg28c492">
								<div class="cubeBox-icon cubeBox-gw"></div>
								<div class="cubeBox-text">公务接待费申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg168eec">
								<div class="cubeBox-icon cubeBox-mp"></div>
								<div class="cubeBox-text">名片印制申请</div>
							</a>
						</li>
						<li class="fl base-col04">
							<a href="####" class="bgef7373">
								<div class="cubeBox-icon cubeBox-jp"></div>
								<div class="cubeBox-text">机票申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg63d4fe">
								<div class="cubeBox-icon cubeBox-qj"></div>
								<div class="cubeBox-text">请假申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg6e6eff">
								<div class="cubeBox-icon cubeBox-cc"></div>
								<div class="cubeBox-text">出差申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bgf76fff">
								<div class="cubeBox-icon cubeBox-yc"></div>
								<div class="cubeBox-text">用车申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bgc49d28">
								<div class="cubeBox-icon cubeBox-yz"></div>
								<div class="cubeBox-text">印章使用申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bgb0ca23">
								<div class="cubeBox-icon cubeBox-yw"></div>
								<div class="cubeBox-text">业务招待费申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg28c492">
								<div class="cubeBox-icon cubeBox-gw"></div>
								<div class="cubeBox-text">公务接待费申请</div>
							</a>
						</li>
						
						<li class="fl base-col04">
							<a href="####" class="bg168eec">
								<div class="cubeBox-icon cubeBox-mp"></div>
								<div class="cubeBox-text">名片印制申请</div>
							</a>
						</li>
					</ul>
					

					</div>
				</div>
			</div>
		</div>
		<!-- ]] 日常办公 -->

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
					<div id="planDataEventchd"></div>
				</div>
				<a class="addWorkPlan" onclick="doAdd()" title="添加">&nbsp;</a>
			</div>
		
		</div>
	
	
		<!-- 任务列表 [[ -->
		<div class="block-III clear">
			<div class="basic-tabMenu-wrap clearFix">
				<ul class="task-tabMenu-ul fl clearFix">
				
					<li class="basic-tabMenu basic-tabMenu-selected">
					
						<a href="javascript:void(0);" type="pendingMatters" >代办（6）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" >草稿（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="alreadyMatters" >已办（21）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<!-- <li class="basic-tabMenu">
						<a href="javascript:void(0);" type="completedMatters" >办结（33）</a>
						<span class="base-tool-item-split"></span>
					</li> -->
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="accordingMatters">转办代理（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript::void(0);" >加签流转（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
					
					<li class="basic-tabMenu">
						<a href="javascript:void(0);" type="myList" >抄送转发（0）</a>
						<span class="base-tool-item-split"></span>
					</li>
				</ul>
				
				<div class="fr clearFix">
					<a href="javascript:void(0);" class="basic-tabMenu-refresh fl"></a>
					<a href="${ctx}/oa/flow/task/pendingMatters.do" class="basic-tabMenu-more fl">+more</a>
				</div>
			</div>
			
<%-- 			<div class="basic-tabCont-wrap clearFix">
				<ol class="task-list">
					<c:forEach items="${tasklist}" var="list" >							 				
							 <li>
								<a href="####" onclick="javascript:jQuery.openFullWindow('${ctx}/oa/flow/task/toStart.do?taskId=${list.id}')" class="fl msg">${list.subject}</a>
								<a href="####" class="fl uname">${list.creator}</a>
								<span class="fl date">${list.createTime}</span>
								<span class="fr unread">待办</span>
							</li>											
					</c:forEach> 
				</ol>
			</div> --%>
			<div class="basic-tabCont-wrap clearFix">
				<div class="task-list show" style="display:block;">
					<ol >
						<li>
							<a href="####" class="fl msg">测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批123123</a>
							<a href="####" class="fl uname">用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr unread">未读</span>
						</li>
						<li class="nth-of-typeEeven">
							<a href="####" class="fl msg">测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字用户名字用户名字用户名字用户名字用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr readed">已读</span>
						</li>
						<li>
							<a href="####" class="fl msg">测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr unread">未读</span>
						</li>
						<li class="nth-of-typeEeven">
							<a href="####" class="fl msg">测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字用户名字用户名字用户名字用户名字用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr readed">已读</span>
						</li>
						<li>
							<a href="####" class="fl msg">测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr unread">未读</span>
						</li>
						<li class="nth-of-typeEeven">
							<a href="####" class="fl msg">测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字用户名字用户名字用户名字用户名字用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr readed">已读</span>
						</li>
					</ol>
				</div>
				<div class="task-list" style="display:none;">
					<ol >
						<li>
							<a href="####" class="fl msg">qqqqq</a>
							<a href="####" class="fl uname">用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr unread">未读</span>
						</li>
						<li class="nth-of-typeEeven">
							<a href="####" class="fl msg">测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字用户名字用户名字用户名字用户名字用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr readed">已读</span>
						</li>
						<li>
							<a href="####" class="fl msg">测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr unread">未读</span>
						</li>
						<li class="nth-of-typeEeven">
							<a href="####" class="fl msg">测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字用户名字用户名字用户名字用户名字用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr readed">已读</span>
						</li>
						<li>
							<a href="####" class="fl msg">测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr unread">未读</span>
						</li>
						<li class="nth-of-typeEeven">
							<a href="####" class="fl msg">测试状态-出款合同审批</a>
							<a href="####" class="fl uname">用户名字用户名字用户名字用户名字用户名字用户名字</a>
							<span class="fl date">2016-08-14 15:32:18</span>
							<span class="fr readed">已读</span>
						</li>
					</ol>
				</div>
			
			</div>
			
			
		</div>
		
		<!-- ]] 任务列表 -->
		
		<div class="clearFix">
			
			<!-- 规章制度[[ -->
			<div class="fl base-col03">
				<div class="block">
					<div class="basic-tabMenu-wrap clearFix">
						<ul class="basic-tabMenu-ul fl clearFix">
							<li class="basic-tabMenu basic-tabMenu-selected">
								<a>规章制度</a>
							</li>
						</ul>
						<div class="fr clearFix">
							<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
							<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
						</div>
					</div>
				
					<div class="basic-tabCont-wrap clearFix">
						<div class="institution-box">
							<p>一、安全保卫工作，要认真落实责任制。</p>
							<p>二、发现问题，及时采取措施解决。</p>
							<p>三、根据实际需要，办公室主任兼职安全保卫干事</p>
							<p>四、落实防火措施，会议中心等重要场所设置的消防栓。</p>
							<p>五、抓好安全用电</p>
							<p>六、落实防盗措施</p>
							<p>七、安全保卫人员要有高度的责任感，经常检查</p>
							<p>八、全体员工都有遵守本制度及有关安全规范的义务。</p>
						</div>
					</div>
				
				</div>
			</div>
			<!-- ]] 规章制度 -->
			
			<!-- 会议安排 [[ -->
			<div class="fl base-col03 pr" style="left:1.2%">
				<div class="block">
					<div class="basic-tabMenu-wrap clearFix">
						<ul class="basic-tabMenu-ul fl clearFix">
							<li class="basic-tabMenu basic-tabMenu-selected">
								<a>会议安排</a>
							</li>
						</ul>
						<div class="fr clearFix">
							<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
							<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
						</div>
					</div>
					
					<div class="basic-tabCont-wrap clearFix">
						<div class="meeting-box">
						
							<div class="meeting-list">
								<div class="date">2106-12-31  9:00</div>
								<div class="msg">什么什么会议什么什么会议什么什么会议什么什么会议</div>
								<span class="dot"></span>
							</div>
							
							<div class="meeting-list">
								<div class="date">2106-12-31  9:00</div>
								<div class="msg">什么什么会议什么什么会议什么什么会议什么什么会议</div>
								<span class="dot"></span>
							</div>
							
							<div class="meeting-list">
								<div class="date">2106-12-31  9:00</div>
								<div class="msg">什么什么会议什么什么会议什么什么会议什么什么会议</div>
								<span class="dot"></span>
							</div>
							
							<div class="meeting-list">
								<div class="date">2106-12-31  9:00</div>
								<div class="msg">什么什么会议什么什么会议什么什么会议什么什么会议</div>
								<span class="dot"></span>
							</div>
					
						</div>
					</div>
				</div>
			</div>
			<!-- ]] 会议安排 -->
			
			
			<div class="fr base-col03">
				<div class="block">
					<div class="basic-tabMenu-wrap clearFix">
						<ul class="basic-tabMenu-ul fl clearFix">
							<li class="basic-tabMenu basic-tabMenu-selected">
								<a>联系人</a>
							</li>
						</ul>
						<div class="fr clearFix">
							<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
							<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
						</div>
					</div>
					<div class="basic-tabCont-wrap clearFix">
						<ol class="comment-box">
							<li class="comment-item clearFix">
								<a class="thumb-sm fl"><img src="${ctx}/styles/images/home/avatar.jpg" class="img-circle"> </a>
								<div class="comment-body">
									<a href="####">刘江国</a>
									<div class="clearFix">
										<span class="fl">工号：2391</span>
										<span class="fl">财务主管</span>
										<span class="fr">13998427432</span>
									</div>
								</div>
							</li>
							
							<li class="comment-item clearFix">
								<a class="thumb-sm fl"><img src="${ctx}/styles/images/home/avatar.jpg" class="img-circle"> </a>
								<div class="comment-body">
									<a href="####">刘江国</a>
									<div class="clearFix">
										<span class="fl">工号：2391</span>
										<span class="fl">财务主管</span>
										<span class="fr">13998427432</span>
									</div>
								</div>
							</li>
							
							<li class="comment-item clearFix">
								<a class="thumb-sm fl"><img src="${ctx}/styles/images/home/avatar.jpg" class="img-circle"> </a>
								<div class="comment-body">
									<a href="####">刘江国</a>
									<div class="clearFix">
										<span class="fl">工号：2391</span>
										<span class="fl">财务主管</span>
										<span class="fr">13998427432</span>
									</div>
								</div>
							</li>
							
							<li class="comment-item clearFix">
								<a class="thumb-sm fl"><img src="${ctx}/styles/images/home/avatar.jpg" class="img-circle"> </a>
								<div class="comment-body">
									<a href="####">刘江国</a>
									<div class="clearFix">
										<span class="fl">工号：2391</span>
										<span class="fl">财务主管</span>
										<span class="fr">13998427432</span>
									</div>
								</div>
							</li>
							
							
						
						</ol>
					</div>
				</div>
			</div>
			
			
		</div>
		

		
		<div class="clear bd-left fl">
			<div class="block">
				<div class="basic-tabMenu-wrap clearFix">
					<ul class="basic-tabMenu-ul fl clearFix">
						<li class="basic-tabMenu basic-tabMenu-selected">
							<a>新上计划</a>
						</li>
					</ul>
					<div class="fr clearFix">
						<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
						<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
					</div>
				</div>
				<div class="basic-tabCont-wrap clearFix">
					<ol class="history-list">
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
					
					</ol>
				</div>
			</div>
		</div>
		
		<div class="bd-left fr">
			<div class="block">
				<div class="basic-tabMenu-wrap clearFix">
					<ul class="basic-tabMenu-ul fl clearFix">
						<li class="basic-tabMenu basic-tabMenu-selected">
							<a>新上项目</a>
						</li>
					</ul>
					<div class="fr clearFix">
						<a href="javascript:;" class="basic-tabMenu-refresh fl"></a>
						<a href="javascript:;" class="basic-tabMenu-more fl">+more</a>
					</div>
				</div>
				<div class="basic-tabCont-wrap clearFix">
					<ol class="history-list">
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
						<li>
							<a href="####" class="msg fl">计划名称计划名称计划名称计划名称计划名称计划名称计划名称计划名称</a>
							<a href="####" class="name fl">朱大门朱大门朱大门朱大门朱大门</a>
							<span class="date fr">2016-12-12  21:00:21</span>
						</li>
					
					</ol>
				</div>
			</div>
		</div>
	</div>
</body>	
</html>