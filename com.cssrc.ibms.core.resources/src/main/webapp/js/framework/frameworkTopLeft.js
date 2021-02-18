//菜单初始化
function firstMenuEach(menuList){
	for (var key in menuList) {
		var _obj = {};
		var element = menuList[key];
		_obj.alias = element.alias;
		_obj.name = element.resName;
		_obj.icon = element.icon;
		_obj.menuId = element.resId;
		_obj.url = element.defaultUrl;
		/*根节点没有配置链接地址时会默认添加*/
		if(_obj.url!=null&&_obj.url!=""){
			$('#sidemenu').append('<li alias="'+_obj.alias+'" class="top-menu"><a href="javascript:;" menuId="'+_obj.menuId+'" name="'+_obj.name+'" img="'+_obj.icon+'" url="'+_obj.url+'" onclick="initSubtMenu(this,'+_obj.menuId+')"><div class="top-menu-icon"><span class="'+_obj.icon+'"></span></div><div class="top-menu-font">'+_obj.name+'</div></a></li>');
		}else{
			$('#sidemenu').append('<li alias="'+_obj.alias+'" class="top-menu"><a href="javascript:;" menuId="'+_obj.menuId+'" name="'+_obj.name+'" img="'+_obj.icon+'" url="'+_obj.url+'" onclick="initSubtMenu(this,'+_obj.menuId+')"><div class="top-menu-icon"><span class="'+_obj.icon+'"></span></div><div class="top-menu-font">'+_obj.name+'</div></a></li>');
		}
	}
}
//初始化子菜单
function initSubtMenu(obj, menuId){
	//合同管理定制
	if($.getCookie("isMainMenuOpen")!='false'){
		if($(obj).attr('name')=='合同管理'){
			$('#menuListnav').css('width','250px');
			$('#framecenter').css('left','250px');
			$.setCookie("sideMenuWidth","250px");
		}else{
			$('#menuListnav').css('width','220px');
			$('#framecenter').css('left','220px');
			$.setCookie("sideMenuWidth","220px");
		}
	}
	
	$('#viewFrameworkSidebar').empty();
	$('#viewFrameworkSidebar').append('<ol class="viewFramework-sidebar-ol" id="sidebar_tree"></ol>');
	
	$("li",$('#sidemenu')).removeClass('active');
	$(obj).parent().addClass('active');
	for (var key in menuList) {
		var element = menuList[key];
				
		if(menuId == element.resId){
			//加载第一个一级菜单的子菜单（树）
			loadTree(element.resId);
			//初始化子菜单
			initLeftMenu('topLeft');
			return;
		}
	}
}