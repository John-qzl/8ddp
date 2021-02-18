/**
 * Author :  sgl.
 * Date : 2018/12/21.
 * Time : 9:22.
 * Description :zTree树的默认高度宽度自适应滚动
 */
$(function(){
    var layUiHeight =$('#defLayout').find(".l-layout-left").height();
    var layUiHeaderHeight = $('#defLayout').find(".l-layout-header").height();
    var zTreeToolbarHeight = $('#defLayout').find(".tree-toolbar").height();

    var zTreeContentHeight = layUiHeight - layUiHeaderHeight - zTreeToolbarHeight ;

    $('.ztree').height(zTreeContentHeight);
})