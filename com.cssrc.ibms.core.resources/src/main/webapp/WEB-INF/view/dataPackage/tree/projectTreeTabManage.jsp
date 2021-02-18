<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
	<title>型号管理tab页管理</title>
	<%@include file="/commons/include/form.jsp" %>
	<link rel="stylesheet" href="${ctx}/layui/css/layui.css">
	<script type="text/javascript" src="${ctx}/layui/layui.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>
     <script type="text/javascript">
     $(function () {
         //ligerui Tab
         debugger;
         layui.use('element', function () {
             var element = layui.element;
             var content = $('.layui-tab-content');
             var _height = content.closest('.layui-tab').height()
                     - content.prev('.layui-tab-title').outerHeight();
             $('.layui-tab-content').height(_height);
             element.on('tab(layui_tab)', function(data){
            	 getPanelBodyHeigt($($(data.elem).find('#listFrame'+data.index)[0].contentWindow.document).find('.panel-body'));
         		 console.log(this); //当前Tab标题所在的原始DOM元素
            	 console.log(data.index); //得到当前Tab的所在下标
            	 console.log(data.elem); //得到当前的Tab大容器 */ 
            	});
            	  
             loadLayout();
           //点击Tab切换iframe
            changeIframe();
         })
         //点击Tab切换iframe
         function changeIframe(){
        	 $("#cpysTab").click(function(){
        		 $("#listFrame0").attr("src","${ctx}/dataPackage/tree/projectTree/manage.do?flag=carry");
        		 var a = $(this).attr("class");
            	 if( a == "tab" ){
            		 $(this).children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/acceptanceCheck2.png");
            	 }else{
            		 $(this).children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/acceptanceCheck.png");
            	 }
            	 $("#wqsjTab").children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/inspection.png");
            	 $("#bcsyTab").children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/test.png");
        	 });
        	 $("#wqsjTab").click(function(){
        		 $("#listFrame0").attr("src","${ctx}/dataPackage/tree/weaponCheckTree/manage.do?flag=carry");
        		 var a = $(this).attr("class");
            	 if( a == "tab" ){
            		 $(this).children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/inspection2.png");
            	 }else{
            		 $(this).children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/inspection.png");
            	 }
            	 $("#cpysTab").children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/acceptanceCheck.png");
            	 $("#bcsyTab").children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/test.png");
        	 });
        	 $("#bcsyTab").click(function(){
        		 debugger;
        		 $("#listFrame0").attr("src","${ctx}/dataPackage/tree/rangeTree/manage.do?flag=carry");
        		 var a = $(this).attr("class");
            	 if( a == "tab" ){
            		 $(this).children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/test2.png");
            	 }else{
            		 $(this).children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/test.png");
            	 }
            	 $("#cpysTab").children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/acceptanceCheck.png");
            	 $("#wqsjTab").children(".Tabimg").attr("src","${ctx}/styles/images/modelManagement/inspection.png");
        	 });
        	 
         }
         //布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 200,
                onHeightChanged: heightChanged,
                allowLeftResize: true
            });
            //取得layout的高度
            debugger;
            var height = $(".l-layout-center").height() - 28;
            var _height = $('.tree-toolbar').height();
            $("#sTree").height(height - _height);
        }
        //布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#sTree").height(options.middleHeight - 90);
        }
	/* 	$('.layui-tab-title').on('click','li',function(){
	  		debugger;
	  		getPanelBodyHeigt('.panel-body');
	  	});  */

		
     });
     
     </script>
     <style>
     .Tabimg{
     	position: relative;
    	bottom: 1px;
     	margin-right: 5px;
    	width: 28px;
    	height: 28px;
     }
     .layui-tab-title{
     	padding: 4px 10px 4px 10px;
     }
     .layui-tab-content{
     	padding: 0;
     }
     .layui-tab-title li{
     	padding: 0 8px;
     	margin-right: 10px;
     }
     .layui-tab-title li:hover{
     	color: #fff;
		background: #347EFE;
		border-radius: 3px;
     }
     .layui-tab-brief > .layui-tab-title .layui-this{
		color: #fff;
		background: #347EFE;
		border-radius: 3px;
	}
	.layui-tab-brief > .layui-tab-more li.layui-this:after, .layui-tab-brief > .layui-tab-title .layui-this:after {
	    border-bottom: 0px solid #347EFE;
	}
	.layui-tab.layui-tab-brief.template2 {
    	padding: 10px 10px 0 10px;
	}
     </style>
</head>
<body>
   <div position="center" style="height:100%">
        <div class="layui-tab layui-tab-brief template1" lay-filter="layui_tab">
            <ul class="layui-tab-title">
<!--                 <li class="layui-this" style="display:none">型号管理</li> -->
                <li class="tab layui-this" tabid="listFrame2" id="cpysTab"><img class="Tabimg" src="${ctx}/styles/images/modelManagement/acceptanceCheck2.png">产品验收</li>
                <li class="tab" id="wqsjTab"><img class="Tabimg" src="${ctx}/styles/images/modelManagement/inspection.png">武器系统所检</li>
                <li class="tab" id="bcsyTab"><img class="Tabimg" src="${ctx}/styles/images/modelManagement/test.png">靶场试验</li>
            </ul>
            <div class="layui-tab-content">
           <%--      <div class="layui-tab-item layui-show">
                    <iframe id="listFrame0" src="${ctx}/dataPackage/tree/projectTree/manage.do?flag=carry" frameborder="no" width="100%" height="100%"></iframe>
                </div> --%>
                <!-- 产品验收tab页面 -->
                 <div class="layui-tab-item layui-show ">
                   <iframe id="listFrame0" src="${ctx}/dataPackage/tree/projectTree/manage.do?flag=carry" frameborder="no" width="100%" height="100%"></iframe>
                </div>
 
<%--                  <div class="layui-tab-item ">
                 <!-- 靶场试验tab页面 -->
                    <iframe id="listFrame3" src="${ctx}/dataPackage/tree/rangeTree/manage.do?flag=carry" frameborder="no" width="100%" height="100%"></iframe>
                </div> --%>
            </div>
        </div>
    </div>

</body>
</html>