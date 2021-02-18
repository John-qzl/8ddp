var globalType=new GlobalType(catKey,"glTypeTree",{onClick:onClick,url:glTypeTreeUrl});
$(function (){
	//加载菜单 
    globalType.loadGlobalTree();
	//布局
    $("#defLayout").ligerLayout({ leftWidth:210,height: '100%',allowLeftResize:true});
});
//左击
function onClick(treeNode){
	var url = getClickUrl(treeNode);
	$("#defFrame").attr("src",url);
};
//展开收起
function treeExpandAll(type){
	globalType.treeExpandAll(type);
};