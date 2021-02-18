<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="/commons/dynamic.jspf"%>

<head>
    <title>首页</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css">
    <link rel="stylesheet" href="${ctx}/styles/homePage/css/miniui.css">
    <link rel="stylesheet" href="${ctx}/styles/homePage/css/jquery.jscrollpane.css">
    <link rel="stylesheet" href="${ctx}/styles/homePage/css/home.css">
</head>

<body>
    <div class="pad20">
        <div class="page_left">
            <div class="workBench border_column fl">
                <div class="TabList">
                    <ul>
                    <li class="boardwork select">
                        	<div class="tab_icon fl">
                        	<img src="${ctx}/styles/homePage/images/work.png">
                        	</div>
                            <div class="work_tab_name fl">
                                <p class="fs14">工作项</p>
                                <span class="fs16">${workNumber}</span>
                            </div>
                        </li>
                        <li class="agency">
                        	<div class="tab_icon fl">
                        	<img src="${ctx}/styles/homePage/images/agency.png">
                        	</div>
                            <div class="work_tab_name fl">
                                <p class="fs14">待办</p>
                                <span class="fs16">${dbrw}</span>
                            </div>
                        </li>
                        <li class="done">
                        	<div class="tab_icon fl">
                        	<img src="${ctx}/styles/homePage/images/done.png">
                        	</div>
                            <div class="work_tab_name fl">
                                <p class="fs14">请求</p>
                                <span class="fs16">${wdqq}</span>
                            </div>
                        </li>
                        <li class="draft">
                        	<div class="tab_icon fl">
                        	<img src="${ctx}/styles/homePage/images/draft.png">
                        	</div>
                            <div class="work_tab_name fl">
                                <p class="fs14">草稿</p>
                                <span class="fs16">${wdcg}</span>
                            </div>
                        </li>
                       <%--  <li class=" request">
                        	<div class="tab_icon fl">
                        	<img src="${ctx}/styles/homePage/images/request.png">
                        	</div>
                            <div class="work_tab_name fl">
                                <p class="fs14">已办</p>
                                <span class="fs16">${ybrw}</span>
                            </div>
                        </li> --%>
                    </ul>
                </div>
                <div class="mini-panel-border">
                    <div class="mini-panel-header">
                        <div class="mini-panel-header-inner">
                            <span class="label"></span>
                            <div class="mini-panel-title" id="text">
                            	我的工作项
                            </div>
                            <div class="mini-tools">
                            <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
							  <ul class="layui-tab-title workBoradTab">
							 	<li class="layui-this" onclick="workBoardStart(1)">进行中</li>
							    <li id="end" onclick="workBoardStart(2)">已完成</li>
							  </ul>
							   <ul class="layui-tab-title myRequest">
							 	<li class="layui-this" onclick="myRequestStart(1)">进行中</li>
							    <li  onclick="myRequestStart(0)">已完成</li>
							  </ul>
							  <ul class="layui-tab-title myTask">
							 	<li class="layui-this" onclick="getMyTask()">进行中</li>
							    <li onclick="getMyCompleteTask()">已完成</li>
							  </ul>
							</div> 
							
                            <!-- <div class="mini-tab">结束
                            	</div>
                            	<div class="mini-tab">
                            	进行
                            	</div> -->
                                <!-- <span class="mini-iconfont mini-tools-refresh" onclick="reload()"></span> -->
                            </div>
                        </div>
                    </div>

                    <div class="mini-panel-viewport scroll-pane">
	                    <ul id="workItem_box" style="display: block;">
	                        <iframe id="workItem" style="width: 100%; height: 100%;" frameborder="0"></iframe>
	                    </ul>
                        <ul id="dbrw" style="display: none">
                            <c:if test="${not empty tasklist}">
                                <c:forEach items="${tasklist}" var="data">
                                    <li>
                                        <img class="w14 unread" src="${ctx}/styles/homePage/images/unread.png">
                                        <div class="title maL10">
                                            <a href="javascript:show('${ctxPath}/dp/oa/flow/task/toStart.do?taskId=','${data.id}','${data.subject}')">${data.subject}</a>
                                        </div>
                                        <span class="datetime maL10 c-98999a fr">
                                            <fmt:formatDate value="${data.createTime}" pattern="yyyy-MM-dd" />
                                        </span>
                                        <span class="username maR20 c-98999a fr">
	                                        <img class="myuser maL10" src="${ctx}/styles/homePage/images/myuser.png">
	                                        ${data.creator}
                                        </span>
                                    </li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty tasklist}">
                                <div class="NoContent">
                                    <img src="${ctx}/styles/homePage/images/noContent.png" />
                                    <p>暂无待办</p>
                                </div>
                            </c:if>
                        </ul>
                        <ul id="wdcg" style="display: none">
                            <c:if test="${not empty cglist}">
                                <c:forEach items="${cglist}" var="data">
                                    <li>
                                        <img class="w14 unread" src="${ctx}/styles/homePage/images/unread.png">
                                        <div class="title maL10">
                                            <a href="javascript:show('${ctxPath}/dp/oa/flow/task/startFlowForm.do?runId=','${data.runId}')">${data.subject}</a>
                                        </div>
                                        <span class="datetime maL10 c-98999a fr">
                                            <fmt:formatDate value="${data.createtime}" pattern="yyyy-MM-dd" />
                                        </span>
                                        <span class="username maR20 c-98999a fr">
	                                        <img class="myuser maL10" src="${ctx}/styles/homePage/images/myuser.png">
	                                        ${data.creator}
                                        </span>
                                    </li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty cglist}">
                                <div class="NoContent">
                                    <img src="${ctx}/styles/homePage/images/noContent.png" />
                                    <p>暂无草稿</p>
                                </div>
                            </c:if>
                        </ul>
                        <ul id="ybrw" style="display: none">
                            <c:if test="${not empty yblist}">
                                <c:forEach items="${yblist}" var="data">
                                    <li>
                                        <img class="w14 unread" src="${ctx}/styles/homePage/images/unread.png">
                                        <div class="title maL10">
                                            <a href="javascript:show('${ctxPath}/dp/oa/flow/processRun/info.do?runId=','${data.runId}')">${data.subject}</a>
                                        </div>
                                        <span class="datetime maL10 c-98999a fr">
                                            <fmt:formatDate value="${data.createtime}" pattern="yyyy-MM-dd" />
                                        </span>
                                        <span class="username maR20 c-98999a fr">
	                                        <img class="myuser maL10" src="${ctx}/styles/homePage/images/myuser.png">
	                                        ${data.creator}
                                        </span>
                                    </li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty yblist}">
                                <div class="NoContent">
                                    <img src="${ctx}/styles/homePage/images/noContent.png" />
                                    <p>暂无已办</p>
                                </div>
                            </c:if>
                        </ul>
                        <ul id="wdqq" style="display: none">
                            <c:if test="${not empty qqlist}">
                                <c:forEach items="${qqlist}" var="data">
                                    <li>
                                        <img class="w14 unread" src="${ctx}/styles/homePage/images/unread.png">
                                        <div class="title maL10">
                                            <c:if test="${data.endTime==null}">
                                                <a class="titleABlue" href="javascript:show('${ctxPath}/dp/oa/flow/processRun/info.do?runId=','${data.runId}','${data.subject}')">${data.subject}</a>
                                            </c:if>
                                            <c:if test="${data.endTime!=null}">
                                                <a class="titleABlue" href="javascript:show('${ctxPath}/dp/oa/flow/processRun/info.do?runId=','${data.runId}','${data.subject}')">${data.subject}</a>
                                            </c:if>

                                       </div>

                                        <c:if test="${data.endTime==null}">
                                            <span style="color: #3333" class="datetime maL10 c-98999a fr">
                                                <fmt:formatDate value="${data.createtime}" pattern="yyyy-MM-dd" />
                                            </span>
                                        </c:if>
                                        <c:if test="${data.endTime!=null}">
                                            <span style="color: #B2BACA" class="datetime maL10 c-98999a fr">
                                                <fmt:formatDate value="${data.createtime}" pattern="yyyy-MM-dd" />
                                            </span>
                                        </c:if>
                                        <c:if test="${data.endTime==null}">
                                            <span style="color: #3333" class="username maR20 c-98999a fr">
	                                        <img class="myuser maL10" src="${ctx}/styles/homePage/images/myuser.png">
	                                        ${data.creator}
                                        </span>
                                        </c:if>
                                        <c:if test="${data.endTime!=null}">
                                           <span style="color: #B2BACA" class="username maR20 c-98999a fr">
	                                        <img class="myuser maL10" src="${ctx}/styles/homePage/images/myuser.png">
	                                        ${data.creator}
                                        </span>
                                        </c:if>


                                    </li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty qqlist}">
                                <div class="NoContent">
                                    <img src="${ctx}/styles/homePage/images/noContent.png" />
                                    <p>暂无请求</p>
                                </div>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="page_right">
            <!-- <div class="device management">
                        <div class="mini-panel-header">
                            <div class="mini-panel-header-inner">
                                <span></span>
                                <div class="mini-panel-title"></div>
                                <div class="mini-tools"></div>
                            </div>
                        </div>
                        <div class="mini-panel-viewport">

                        </div>
                    </div> -->
            <div class="message border_column fr">
                <div class="mini-panel-border">
                    <div class="mini-panel-header">
                        <div class="mini-panel-header-inner">
                            <span></span>
                            <div class="mini-panel-title">消息</div>
                            <div class="mini-tools">
                            <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
							  <ul class="layui-tab-title">
								<li id="noRead" class="layui-this" onclick="messageRead(0)">未读</li>
							    <li id="read" onclick="messageRead(1)">已读</li>
							  </ul>
							</div> 
                            <!-- <div class="mini-tab">已读</div>
                            <div class="mini-tab">未读</div> -->
                            </div>
                        </div>
                    </div>
                    <div class=" scroll-pane">
                        <ul id="accMessage">
                            <c:if test="${not empty accMessage}">
                                <c:forEach items="${accMessage}" var="data">
                                    <li><img class="fl" src="${ctx}/styles/homePage/images/blue.png">
                                        <p class="c-98999a maL50">
                                            <span> ${data.F_ZW} </span>
                                            <span class="fr"><fmt:formatDate value="${data.F_JSSJ}" pattern="yyyy-MM-dd" /></span>
                                            <c:if test="${data.F_SFYD=='1'}">
                                             <span class="blue_bg fr"> 已读</span>
	 									    </c:if>
	 									     <c:if test="${data.F_SFYD=='0'}">
	                                             <span class="red_bg fr"> 未读</span>
	 									    </c:if>
	 									     <c:if test="${data.F_SFYD==null}">
	                                             <span class="red_bg fr"> 未读</span>
	 									    </c:if>
                                        </p>
                                        <p class="fs14 maL50 hidden">
                                            <span class="blue"> ${data.F_XXZT} </span> - <span class="c-2a2c2d"><a class="msg_a" onclick="openDialog('${data.F_XXID}','${data.F_ZL}','${data.ID}')">${data.F_XXMC}</a></span>
                                        </p>
                                    </li>
                                </c:forEach>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="notice border_column fr">
                <div class="mini-panel-border">
                    <div class="mini-panel-header">
                        <div class="mini-panel-header-inner">
                            <span></span>
                            <div class="mini-panel-title">公告</div>
                            <div class="mini-tools"></div>
                        </div>
                    </div>
                    <div class="mini-panel-viewport scroll-pane">
                        <ul id=systemMessage>
	                        <c:if test="${not empty systemMessage}">
	                           <c:forEach items="${systemMessage}" var="data">
	                              <li>
	                                 <a href="javascript:newInfoshow('${ctxPath}/dp/oa/form/dataTemplate/detailData.do?__displayId__=10000034230022&__pk__=','${data.ID}')">${data.F_BT}</a>
	                                 <span class="noticeTime c-98999a fr"><fmt:formatDate value="${data.F_FBSJ}" pattern="yyyy-MM-dd"/></span>
	                                 </li>
	                           </c:forEach>
	                        </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${ctx}/layui/layui.js"></script>
    <script src="${ctx}/styles/homePage/js/jquery.min.js"></script>
    <script src="${ctx}/styles/homePage/js/jquery.jscrollpane.min.js"></script>
    <script src="${ctx}/styles/homePage/js/jquery.mousewheel.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js"></script>
    <script type="text/javascript">
    layui.use('element', function(){
    	  var $ = layui.jquery
    	  ,element = layui.element;
    })
        $(function () {
        	var workItemDisplayId;
            $.ajax({
            	dataType: "json",
                async: false,
                url: "${ctx}/oa/form/dataTemplate/getDisplayIdByFormAliases.do",
                data: {
                    'allFormAliases': "gzxkbbd"
                },
                success: function (result) {
                    workItemDisplayId = result[0].gzxkbbd;
                },
                error: function () {
                    console.log("ajax请求失败");
                }
            })
            $("#workItem").attr("src","/dp/oa/form/dataTemplate/preview.do?__displayId__=" +workItemDisplayId+"&__dbomSql__=F_XYB!='任务结束'");
            $("#workItem_box").parent().css("height","100%");
	        var isIE = !+"\v1";
	        if(isIE){
	           var boxH = $(".workBench .jspContainer").height() - 15;
	           document.getElementById("workItem_box").style.height = boxH +"px"
	        }
        })
        	var isIE = !+"\v1";
        	if(isIE){
        		var h = $("body").height() - 20;
        		$(".pad20").css("height",h+"px");
                var workBench = $(".workBench").height() - 68;
                var message = $(".message").height() - 37;
                $(".workBench .mini-panel-border").css("height", workBench + "px");
                $(".workBench .mini-panel-viewport").css("height", (workBench - 37) + "px");
                $(".message .scroll-pane").css("height", message + "px");
                
        	}
        	  var h = $(".notice .mini-panel-border, .workitem .mini-panel-border").height() ;
              $(".notice .mini-panel-border, .workitem .mini-panel-border").css("height", h + "px");
              $(".notice .mini-panel-viewport").css("height", (h - 52) + "px");
              var w = $(".notice ul").width() - 85;
              $(".notice li a").css("width", w + "px");
            var bars = '.jspHorizontalBar, .jspVerticalBar';
            $('.scroll-pane').bind('jsp-initialised',
                function (event, isScrollable) {
                    $(this).find(bars).hide();
                }).jScrollPane().hover(function () {
                $(this).find(bars).stop().fadeTo('fast', 0.8);
            }, function () {
                $(this).find(bars).stop().fadeTo('fast', 0);
            });
            function newInfoshow(url,Id) {
                DialogUtil
                    .open({
                        height: window.top.document.documentElement.clientHeight * 0.4,
                        width: window.top.document.documentElement.clientWidth * 0.4,
                        url: url + Id,
                        title:"公告",
                        isResize: true,
                        //自定义参数
                        sucCall: function (rtn) {
                            if (rtn) {
                                //window.location.reload(true);
                            }
                        }
                    });
            }
        function show(url, Id, subject) {
            DialogUtil
                .open({
                    height: window.top.document.documentElement.clientHeight * 0.9,
                    width: window.top.document.documentElement.clientWidth * 0.9,
                    title: "表单信息",
                    url: url + Id,
                    isResize: true,
                    showMax: false,
                    //自定义参数
                    sucCall: function (rtn) {
                        if (rtn) {
                            //window.location.reload(true);
                        }
                    }
                });
        }

        function openDialog(id,type,businesskey){
        	debugger;
        	var url="";
        	if(type=="验收报告"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000021410561&__pk__="+id;
        	}
        	else if(type=="验收策划"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000021341344&__pk__="+id;
        	}
        	else if(type=="靶场试验策划"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000028760024&__pk__="+id;
        	}
        	else if(type=="武器所检策划"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000031900580&__pk__="+id;
        	}
        	else if(type=="所检数据确认"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000031940111&__pk__="+id;
        	}
        	else if(type=="靶场数据确认"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000029490089&__pk__="+id;
        	}
        	debugger;
        	var messageSelect=$("#noRead").attr("class");
        	var check='0';
        	if(messageSelect==undefined){
        		check=="1";
        	}
        	else{
        		check="0";
        	}
        	   $.ajax({
       		    url: __ctx+"/oa/home/setMessageRead.do?businesskey="+businesskey+"&check="+check,
       		    async:false,
       		    success:function(data){
       		  	if(url!=""){
       		     DialogUtil
   		         .open({
   		             height: window.top.document.documentElement.clientHeight * 0.9,
   		             width: window.top.document.documentElement.clientWidth * 0.9,
   		             title: "明细",
   		             url: url,
   		             isResize: true,
   		             //自定义参数
   		             sucCall: function (rtn) {
   		                 if (rtn) {
   		                     //window.location.reload(true);
   		                 }
   		             }
   		         });
       		  	}
       		  	else{
       		  	$.ligerDialog.success("消息已读！",'提示信息');
       		  	}
       		   	var html='';
           		    	if(data!=null){
           		    		for(var i=0;i<data.length;i++){
           		    			html += '<li><img class="fl" src="${ctx}/styles/homePage/images/blue.png">';
           		    			html+='<p class="c-98999a maL50">';
           		    			html+='<span>'+data[i].F_ZW+'</span>';
           		    			html+='<span class="fr">'+data[i].F_JSSJ+'</span>'; 
           		    			if(data[i].F_SFYD=='1'){
           		    				html+='<span class="blue_bg fr"> 已读</span>';
           		    			}
           		    			else{
           		    				html+='<span class="red_bg fr"> 未读</span>';
           		    			}
           		    			html+='</p>';
           		    			html+=' <p class="fs14 maL50 hidden"> <span class="blue">'+data[i].F_XXZT+'</span> - <span class="c-2a2c2d">';
           		    			html+='<a class="msg_a" onclick="openDialog('+"'"+data[i].F_XXID+"'"+','+"'"+data[i].F_ZL+"'"+','+"'"+data[i].ID+"'"+')">'+data[i].F_XXMC+'</a>';
           		    			html+='</li>';
           		    		}
           		    		
           		    		$("#accMessage").children().remove();
           		    		$("#accMessage").append(html);
           		    	
           		    	}
       		       
       		    }
           	});
  		
        }
        function workBoardStart(check){
        	if(check=="1"){
        		$("#workItem").attr("src","/dp/oa/form/dataTemplate/preview.do?__displayId__=10000027710053 &__dbomSql__=F_XYB!='任务结束'");
        	}
        	else{
        		$("#workItem").attr("src","/dp/oa/form/dataTemplate/preview.do?__displayId__=10000027710053 &__dbomSql__=F_XYB='任务结束'");
        	}
        }
        function messageRead(check){
        	 $.ajax({
     		    url: __ctx+"/oa/home/getMessage.do?check="+check,
     		    async:false,
     		    success:function(data){
    		    	var html='';
    		    	$("#accMessage").children().remove();
       		    	if(data!=null){
       		    		for(var i=0;i<data.length;i++){
       		    			html += '<li><img class="fl" src="${ctx}/styles/homePage/images/blue.png">';
       		    			html+='<p class="c-98999a maL50">';
       		    			html+='<span>'+data[i].F_ZW+'</span>';
       		    			html+='<span class="fr">'+data[i].F_JSSJ+'</span>'; 
       		    			if(data[i].F_SFYD=='1'){
       		    				html+='<span class="blue_bg fr"> 已读</span>';
       		    			}
       		    			else{
       		    				html+='<span class="red_bg fr"> 未读</span>';
       		    			}
       		    			html+='</p>';
       		    			html+=' <p class="fs14 maL50 hidden"> <span class="blue">'+data[i].F_XXZT+'</span> - <span class="c-2a2c2d">';
       		    			html+='<a class="msg_a" onclick="openDialog('+"'"+data[i].F_XXID+"'"+','+"'"+data[i].F_ZL+"'"+','+"'"+data[i].ID+"'"+')">'+data[i].F_XXMC+'</a>';
       		    			html+='</li>';
       		    		}
       		    		
       		    		$("#accMessage").children().remove();
       		    		$("#accMessage").append(html);
       		    		var bars = '.jspHorizontalBar, .jspVerticalBar';
       	                $('.scroll-pane').bind('jsp-initialised',
       	                    function (event, isScrollable) {
       	                        $(this).find(bars).hide();
       	                    }).jScrollPane().hover(function () {
       	                    $(this).find(bars).stop().fadeTo('fast', 0.8);
       	                }, function () {
       	                    $(this).find(bars).stop().fadeTo('fast', 0);
       	                });
       		    	
       		    	}
     		    }
        	 });
        }
        function getMyCompleteTask(){
        	 $.ajax({
       		    url: __ctx+"/oa/home/getMyCompleteTask.do",
       		    async:false,	
       		    success:function(data){
       		    	var html='';
       		    	$("#dbrw").children().remove();
       		    	if(data!=null){
       		    		for(var i=0;i<data.length;i++){
       		    			html += '<li>';
       		    			html+='<img class="w14 unread" src="/dp/styles/homePage/images/unread.png">';
       		    			html+='<div class="title maL10">';
       		    			html+='<a class="titleABlue" href="javascript:show(&apos;'+'/dp/oa/flow/processRun/info.do?runId=&apos;,&apos;'+data[i].runId+'&apos;)">'+data[i].subject+'</a></div>';
       		    			html+='<span style="color: #B2BACA" class="datetime maL10 c-98999a fr">';
       		    			html+=data[i].createtime.substring(0, 10)+'  </span>';
       		    			html+='<span style="color: #B2BACA" class="username maR20 c-98999a fr">';
       		    			html+=' <img class="myuser maL10" src="/dp/styles/homePage/images/myuser.png">';
       		    			html+=data[i].creator+'</span>';
       		    			html+='</li>';
       		    			$("#dbrw").children().remove();
       		    			$("#dbrw").append(html);
       		    		}
       		    	}
       		    }
          	 });
        }
        function getMyTask(){
         	 $.ajax({
      		    url: __ctx+"/oa/home/getMyTask.do",
      		    async:false,
      		    success:function(data){
      		    	var html='';
      		    	$("#dbrw").children().remove();
      		    	if(data!=null){
      		    		for(var i=0;i<data.length;i++){
      		    			html += '<li>';
      		    			html+='<img class="w14 unread" src="/dp/styles/homePage/images/unread.png">';
      		    			html+='<div class="title maL10">';
      		    			html+='<a class="titleABlue" href="javascript:show(&apos;'+'/dp/oa/flow/task/toStart.do?taskId=&apos;,&apos;'+data[i].id+'&apos;,&apos;'+data[i].subject+'&apos;)">'+data[i].subject+'</a></div>';
      		    			html+='<span style="color: #B2BACA" class="datetime maL10 c-98999a fr">';
      		    			html+=data[i].createTime.substring(0, 10)+'  </span>';
      		    			html+='<span style="color: #B2BACA" class="username maR20 c-98999a fr">';
      		    			html+=' <img class="myuser maL10" src="/dp/styles/homePage/images/myuser.png">';
      		    			html+=data[i].creator+'</span>';
      		    			html+='</li>';
      		    			$("#dbrw").children().remove();
      		    			$("#dbrw").append(html);
      		    		}
      		    	}
      		    }
         	 });
       }
        function myRequestStart(isStart){
        	 $.ajax({
      		    url: __ctx+"/oa/home/getMyRequest.do?isStart="+isStart,
      		    async:false,
      		    success:function(data){
      		    	var html='';
      		    	$("#wdqq").children().remove();
      		    	if(data!=null){
      		    		for(var i=0;i<data.length;i++){
      		    			html += '<li>';
      		    			html+='<img class="w14 unread" src="/dp/styles/homePage/images/unread.png">';
      		    			html+='<div class="title maL10">';
      		    			html+='<a class="titleABlue" href="javascript:show(&apos;'+'/dp/oa/flow/processRun/info.do?runId=&apos;,&apos;'+data[i].runId+'&apos;,&apos;'+data[i].subject+'&apos;)">'+data[i].subject+'</a></div>';
      		    			/* html+='<span class="datetime maL10 c-98999a fr" style="color: #y">';
      		    			if(data[i].endTime==null){
      		    				html+='审批中</span>';
      		    			}
      		    			else{
      		    				html+='已结束</span>';
      		    			} */
      		    			html+='<span style="color: #B2BACA" class="datetime maL10 c-98999a fr">';
      		    			html+=data[i].createtime.substring(0, 10)+'  </span>';
      		    			html+='<span style="color: #B2BACA" class="username maR20 c-98999a fr">';
      		    			html+=' <img class="myuser maL10" src="/dp/styles/homePage/images/myuser.png">';
      		    			html+=data[i].creator+'</span>';
      		    			html+='</li>'
      		    			
      		    			$("#wdqq").children().remove();
      		    			$("#wdqq").append(html);
      		    		}
      		    	}
      		    }
        	 });
        }
  
        $(".TabList li").click(
            function () {
                var text = "我的" + $(this).children().children("p").text();
                document.getElementById("text").innerHTML = text;
                var txt = $(this).children().children("p").text();
                $(this).addClass("select").siblings().removeClass("select");
                if (txt == "工作项") {
                    $("#workItem_box").css("display", "block").siblings("ul").css(
                        "display", "none");
                    $(".workBoradTab").css("display", "block");
                    $(".myRequest").css("display", "none");
                    $(".myTask").css("display", "none");
                    
                }	
                if (txt == "请求") {
                    $("#wdqq").css("display", "block").siblings("ul").css(
                        "display", "none");
                    $(".workBoradTab").css("display", "none");
                    $(".myRequest").css("display", "block");
                    $(".myTask").css("display", "none");
                }
                if (txt == "已办") {
                    $("#ybrw").css("display", "block").siblings("ul").css(
                        "display", "none");
                    $(".workBoradTab").css("display", "none");
                    $(".myRequest").css("display", "none");
                    $(".myTask").css("display", "none");
                }
                if (txt == "草稿") {
                    $("#wdcg").css("display", "block").siblings("ul").css(
                        "display", "none");
                    $(".workBoradTab").css("display", "none");
                    $(".myRequest").css("display", "none");
                    $(".myTask").css("display", "none");
                }
                if (txt == "待办") {
                    $("#dbrw").css("display", "block").siblings("ul").css(
                        "display", "none");
                    $(".workBoradTab").css("display", "none");
                    $(".myRequest").css("display", "none");
                    $(".myTask").css("display", "block");
                }
                var bars = '.jspHorizontalBar, .jspVerticalBar';
                $('.scroll-pane').bind('jsp-initialised',
                    function (event, isScrollable) {
                        $(this).find(bars).hide();
                    }).jScrollPane().hover(function () {
                    $(this).find(bars).stop().fadeTo('fast', 0.8);
                }, function () {
                    $(this).find(bars).stop().fadeTo('fast', 0);
                });
            })
    </script>
</body>

</html>