<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/commons/include/form.jsp" %>
<f:sysparam paramname="SYS_UITYPE" alias="SYS_UITYPE"></f:sysparam>
<f:link href="web.css"></f:link>
<f:link href="form.css" ></f:link>
<link rel="stylesheet" type="text/css" href="../input.css">
<script type="text/javascript">
	window.name="win";
	
	var selected, cla = "";
	$(function() {
		load();
		$(".button-td").bind("mouseenter mouseleave", function() {
			$(this).toggleClass("button-td-hover");
		});		
	});
	
	function load(){
		$.post(__ctx+"/oa/system/resources/getExtendIcons.do", function(data) {
			var obj=$.parseJSON(data);
			if(obj.result==0){
				alert("出错了:" + obj.getMessage());
				return;
			}else{				
				icons = obj.message.split(",");
				var html = "";
				for ( var i = 0; i < icons.length; i++) {
					if(icons[i].length>0){
						html += '<li><a class="extend '+icons[i]+'"></a></li>';
					}
				}
				$("#icon-ul").html(html);
				$('#icon-ul li').click(function() {
					if (selected) {
						$(selected).removeClass('selected');
					}
					$(this).addClass('selected');
					selected = this;
					cla = $(this).find("a").attr('class');
				});
			}
		});
	};

	function select() {
		if (cla == "") {
			alert("没有选择图标!");
			return;
		}
		var obj = {
			cla : cla
		};
		window.parent.selectIcon(obj);
	};
	
	function closeWin(){
		window.parent.selectIcon();
	};
	
	function refresh(){
		location.href=location.href.getNewUrl();
	};
	
</script>
<base target="_self"></base>
</head>
<body>
	<div id="icon-div">
		<ul id="icon-ul">
		</ul>
	</div>
	<div id="button-div">
		<div class="button-td" onclick="select()">选择</div>
		<div class="button-td" onclick="closeWin()">取消</div>
		<c:if test="${SYS_UITYPE=='0'}">
			<div class="button-td" onclick="refresh()" >刷新</div>
		</c:if>
	</div>
</body>
</html>