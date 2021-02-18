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
            <div class="workitem border_column fl" style="width:100%">
                <div class="mini-panel-border">
                    <div class="mini-panel-header">
                        <div class="mini-panel-header-inner">
                            <span></span>
                            <div class="mini-panel-title">工作项</div>
                            <div class="mini-tools"></div>
                        </div>
                    </div>
                    <div class="mini-panel-viewport">
                        <iframe id="workItem" style="width: 100%; height: 100%;" frameborder="0"></iframe>
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
            $("#workItem").attr("src","/dp/oa/form/dataTemplate/preview.do?__displayId__=" +workItemDisplayId);
        })
        	var isIE = !+"\v1";
        	if(isIE){
        		var h = $("body").height() - 20;
        		$(".pad20").css("height",h+"px");
        	}
        	  var h = $(".workitem .mini-panel-border").height()-3 ;
              $(".workitem .mini-panel-border").css("height", h + "px");
              $(".workitem .mini-panel-viewport").css("height", (h - 52) + "px");
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
                    title: subject,
                    url: url + Id,
                    isResize: true,
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
        	else if(type=="试验策划"){
        		url="${ctx}/oa/form/dataTemplate/detailData.do?__displayId__=10000028760024&__pk__="+id;
        	}
        	$.ajax({
    		    url: __ctx+"/oa/home/setMessageRead.do?businesskey="+businesskey,
    		    async:false,
    		    success:function(data){
    		    	
    		    }
        	});
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
    </script>
</body>

</html>