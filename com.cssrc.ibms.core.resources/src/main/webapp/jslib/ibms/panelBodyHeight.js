
var getPanelBodyHeigt = function(obj){

    var parent = $(obj).parent('.panel').height()? $(obj).parent('.panel'):$(obj).parent('.panel-container');
    // panel-top的高度
    var panelTopH = parent.children('.panel-top').outerHeight() || null;
    //panel-nav的高度
    var panelNavH = parent.children('.panel-nav').outerHeight() || null;
    //panel-title的高度
    var panelTitleH = parent.children('.panel-title').outerHeight() || null;
    //panel-page的高度
    var panelPageH = $(obj).children('.panel-page').outerHeight() || null;
    //panel的高度
    var height = parent.height();
    //pabel-body的高度
    $(obj).height(height-4)
   
    //判断有没有panel-top
    if(!!panelTopH){
      $(obj).height($(obj).height() - panelTopH)
    }
    //判断有没有panel-nav
    if(!!panelNavH){
      $(obj).height($(obj).height() - panelNavH)
    }
    //判断有没有panel-title
    if(!!panelTitleH){
      $(obj).height($(obj).height() - panelTitleH)
    }
    //判断有没有panel-page
    if(!!panelPageH){
       $(obj).height($(obj).height() - panelPageH)
    }
    //计算tbody的高度
    if(!!$(obj).find('.panel-body-tbody').html()){
      $('.panel-body-tbody').height($(obj).height()-$('.panel-body-tbody').prev('.panel-body-thead').height())
    }
};



$(document).ready(function() {
    setTimeout(function(){
      getPanelBodyHeigt('.panel-body')
    },0) 
})