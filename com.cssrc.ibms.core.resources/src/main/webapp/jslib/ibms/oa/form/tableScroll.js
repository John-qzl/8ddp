/**
 *表格滚动前n列固定 
**/



function tableScroll (n) {
	for (var index = 1; index < n+1; index++) {
		$('.panel-table .table-list thead tr th:nth-child('+index+')').addClass('tableFix');
		$('.panel-table .table-list tbody tr td:nth-child('+index+')').addClass('tableFix');
	}
	$('.panel-body').css({'overflow-x':'hidden','overflow-y':'auto'});
	$('.panel-body').append('<div id="sliderBox_l"><div id="slider_l"></div></div>');
	$('.panel-body .table-grid').attr('id','scrollContent');
	$('.panel-body').attr('id','scrollBox');
	

	var box = document.getElementById('scrollBox');
	var content = document.getElementById('scrollContent');

    // var maxWidth= content.clientWidth-box.offsetWidth;
    // if (maxWidth<0) {
    //     $('#sliderBox_1').remove();
    //     return false;
    // } 
    
    var sliderBox_l = document.getElementById('sliderBox_l');
    var slider_l = document.getElementById('slider_l');



    
    slider_l.onmousedown = function (e) {
        var ev = e||window.event;
        var disX = ev.clientX-slider_l.offsetLeft;
        document.onmousemove= function (e) {
            var ev = e||window.event;
            var x = ev.clientX-disX;
            var t = sliderBox_l.clientWidth-slider_l.offsetWidth;
            if (x<=0) {
                x=0;
            } else if(x>=t){
                x = t;
            }
            var sacle= x/t;
            var maxWidth= content.clientWidth-box.offsetWidth;
            content.style.marginLeft= -sacle*maxWidth +'px';
			$('.tableFix').css('left',sacle*maxWidth+'px');
            slider_l.style.left = x+'px';
        }
        document.onmouseup= function () {
            document.onmousemove = null;
        }
        return false;      
    }

   setTimeout(function () {
        var scrollBoxHeight = $("#scrollBox").height() - 20; 
        $("#scrollBox").height(scrollBoxHeight);
    },0)

}









