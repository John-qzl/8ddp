/**
 ***************************
 ******* 2017-04-11 ********
 ***** 判断提示框位置方向  ****
 ***************************
 **/
if (typeof  TipsOps== 'undefined') {
	TipsOps = {};
}

$(document).ready(function() {
	TipsOps.opsBtn();
})


//加载动画
TipsOps.loadingTable=function(){
	$(".panel").prepend("<div class='l-page-loading' style='display:block;'></div>");
}

//滑动操作按钮展示浮动位置
TipsOps.opsBtn = function(){
	$('.ops_btn').bind('mouseover', function() {
		var h1 = $(this).next().next('.ops_box').height();
		var h2 = $(this).offset().top;
		var h3 = $(document).height();
		
		var w1 = $(this).width();
		var w2 = $(this).next().next('.ops_box').width();
		var w3 = 0.5*(w1 - w2);
		
		if(h1 + h2+50 > h3) {
			$(this).next().next('.ops_box').css('top', -h1 + 'px')
		}
		if(w1>w2){
			$(this).next().next('.ops_box').css('right', w3 + 'px')
		}
	})
}


	