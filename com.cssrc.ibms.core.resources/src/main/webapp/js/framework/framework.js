var tab = null;
var currTabId = 'home';
var mian_layout_;
var layoutHeight;
var diff = 0;

$(function(){
	//布局初始化
	initLayout();
	//加载菜单树
	/*loadTree();
	//初始化菜单
	initLeftMenu();*/
	//初始话tab页
	//initTab();
    //加载门户
    //$('#home').attr("src",ctxPath+"/oa/portal/insPortal/show.do?key=personal");

	//日志预警
	if(isWarning!=undefined&&warningMsg!=undefined){
		if(isWarning == "1"){//即将存满，警告
			$.ligerDialog.warn(warningMsg);
		}else if(isWarning == "2"){//已存满，报错
			$.ligerDialog.error(warningMsg);
		}
	}
});

//布局初始化
function initLayout(){
	//布局
	mian_layout_ = $("#layoutMain").ligerLayout( {
		leftWidth : 240,
		height : '100%',
		allowLeftResize :true,
		space:0,
		onHeightChanged : heightChanged,
		onLeftToggle:leftToggle
	});

	//取得layout的高度
	layoutHeight = $("#layoutMain").height() - 2;
}

//添加到tab或者刷新
function addToTab(url, txt, id, icon) {
	if (tab.isTabItemExist(id)) {
		tab.selectTabItem(id);
		tab.reload(id);
	} else {
		tab.addTabItem( {
			tabid : id,
			text : txt,
			url : url,
			icon : '',//icon
	        callback: function (){

		    }
		});
        var tabContent = $(".l-tab-content"), h = tabContent.height();
        tabContent.height(h + diff);
        $("> .l-tab-content-item", tabContent).height(tabContent.height());
	}
};
//添加到tab或者刷新-solr
//如果存在，则先删除tab,再新加一个
function addToTab_solr(url, txt, id, icon) {
	if (tab.isTabItemExist(id)) {
		tab.selectTabItem(id);
		var options={
				tabid : id,
				text : txt,
				url : url,
				icon :icon
			};
		tab.removeTabItem(id);
		tab.addTabItem(options);
	} else {
		tab.addTabItem( {
			tabid : id,
			text : txt,
			url : url,
			icon : ''//icon
		});
	}
};
//更多页到查询页-solr
//将给定选中的的tab删除,新增一个tab
function moreToindexTab(url){
	//alert($(".l-selected").attr('tabid'));
	tab.removeTabItem($(".l-selected").attr('tabid'));
	var id=tab.getNewTabid();
	//alert(id);
	var options={
			tabid : id,
			text : '全文检索',
			url : url,
			icon :''
		};
	tab.addTabItem(options);
}
function showTab(node) {
   	var url=node.attr('url');
    if(url==undefined || url==''){
   	 	return;
    }
    if (!url.startWith("http", false)){
    	url = ctxPath + url;
    }
    var id =  node.attr('menuId');
    var img= node.attr('img');
    var title=node.attr('name');
    addToTab(url, title, id, img)
}

function showTabFromPage(config){
	 var id =  config.tabId;
	 var txt = config.title;
	 var url = config.url;
	 var icon = config.iconCls;
	 addToTab(url, txt, id, icon);
}

//初始化tab页
function initTab(){
 	var tabItems = [];
    tab = $("#framecenter").ligerTab({
        height: layoutHeight,
        //showSwitchInTab : true,
        //showSwitch: true,
        //contextmehu: true,
		dblClickToClose : true,
		onBeforeSelectTabItem : function(tabid) {
			currTabId = tabid;
		},
		onAfterSelectTabItem : function(tabid) {
			//向后台返回当前用户操作的具体节点信息
			$.ajax({
				url : ctxPath+"/oa/form/dataTemplate/setTabId.do?tabid="+tabid,
				type : "POST",
				dataType:'json',
				async : true,
				success : function(data) {

				}
			});
		}/*,
        onAfterAddTabItem: function (tabdata)
        {
            tabItems.push(tabdata);
           // saveTabStatus();
        },
        onAfterRemoveTabItem: function (tabid)
        {
            for (var i = 0; i < tabItems.length; i++)
            {
                var o = tabItems[i];
                if (o.tabid == tabid)
                {
                    tabItems.splice(i, 1);
                    //saveTabStatus();
                    break;
                }
            }
        },
        onReload: function (tabdata)
        {
            var tabid = tabdata.tabid;
           // addFrameSkinLink(tabid);
        }*/
    });
}

/****菜单全部在左侧：菜单加载****/
//初始化左侧菜单
var $SidebarCont, $linkBd, $linka;
var clientHeight = document.documentElement.clientHeight - 108;
function initLeftMenu(layoutType){
	var $body = $(document.body);

	var oRollbar = $('#viewFrameworkSidebar').height(document.documentElement.clientHeight - 108).rollbar({scroll:'vertical', zIndex:100});
	'load|resize'.replace(/\w+/g, function(eType){
		$(window).bind(eType, function(){
			clientHeight = document.documentElement.clientHeight - 108;
			$('#viewFrameworkSidebar').height(clientHeight);
		});
	});
	$SidebarCont = $('#viewFrameworkSidebar'),
		$linkBd = $SidebarCont.find('.viewFramework-sidebar-linkBd'),
		$linka =  $SidebarCont.find('a');

	//判断菜单默认展开还是关闭
	if($.getCookie("isMainMenuOpen")=='false'){
		setMiniMenu($body);
	}

	//菜单点击事件
	$('a',$SidebarCont).unbind("click");
	$SidebarCont.delegate("a","click",function(e){
		$linka.removeClass('active');
		$(this).addClass('active');

		var self = this;
		addToTab(self.href, self.title, this.getAttribute('data-tabid')+this.getAttribute('resId'), this.getAttribute('icon'));

		e.stopPropagation();
		e.preventDefault();

	});

	//$SidebarCont.delegate(".viewFramework-sidebar-linkHd, .viewFramework-sidebar-subHd", "click", function(){
	$('.viewFramework-sidebar-linkHd .viewFramework-sidebar-arrow, .viewFramework-sidebar-subHd .viewFramework-sidebar-arrow',$SidebarCont).unbind("click");
	$('.viewFramework-sidebar-linkHd .viewFramework-sidebar-arrow, .viewFramework-sidebar-subHd .viewFramework-sidebar-arrow',$SidebarCont).click(function(){
		/*if($body.hasClass('viewFramework-sidebar-mini')) {
			return false;
		}*/
		var $this = $(this).parent(),
			$target = $this.next(),
			visible = $target.is(':visible'),
			className = $this[0].className;

		if(/linkHd/i.test($this.parent()[0].className) || /subHd/i.test($this.parent()[0].className)) {
			$this = $this.parent();
			$target = $this.next();
			visible = $target.is(':visible');
			className = $this[0].className;
		}

		if(/linkHd/i.test(className)) { // level 1

			var $sidebarlinkHd = $SidebarCont.find(".viewFramework-sidebar-linkHd");
			$sidebarlinkHd.removeClass('active');
			$linkBd.slideUp();

			if(visible) {
				$this.removeClass('active');
				$target.slideUp(watchSliderviewPort);

				$(this).attr('title','展开');
			} else {
				$this.addClass('active');
				$target.slideDown(watchSliderviewPort);

				$(this).attr('title','收起');
			}

			//return false;
		} else if(/subHd/i.test(className)) { // level 2
			if(visible) {
				$this.removeClass('on');
				$target.slideUp(watchSliderviewPort);

				$(this).attr('title','展开');
			} else {
				$this.addClass('on');
				$target.slideDown(watchSliderviewPort);

				$(this).attr('title','收起');
			}

			//return false;
		}
		oRollbar.init();
	});
	$('#toggleSidebar').unbind("click");
	$('#toggleSidebar').click(function(){
		if($body.hasClass('viewFramework-sidebar-full')) {
			setMiniMenu($body);
		} else {
			setFullMenu($body);
		}
	});
}

function setFullMenu($body){
	var sideMenuWidth =  "220px";
	if($.getCookie("sideMenuWidth")){
		sideMenuWidth = $.getCookie("sideMenuWidth");
	}
	$body.removeClass('viewFramework-sidebar-mini').addClass('viewFramework-sidebar-full');
	//动画效果：可删除
	$('#toggleSidebar').closest(".viewFramework-sidebar-toggle").animate({ "left": sideMenuWidth });
	$(".viewFramework-sidebar").animate({ "width": sideMenuWidth, "opacity": 1 });

    $.setCookie("isMainMenuOpen","true");
}

function setMiniMenu($body){
	$body.removeClass('viewFramework-sidebar-full').addClass('viewFramework-sidebar-mini');
	$('.viewFramework-sidebar-linkBd').slideUp(0,watchSliderviewPort);
	//动画效果：可删除
    $(".viewFramework-sidebar").animate({ "width": "10px", "opacity": 1 });

   /* $(".viewFramework-sidebar-icon").qtip({
		content:{
			text: false
		},
        position: {
        	at:'center center'
        },
        show:{
        	effect: function(offset) {
					$(this).slideDown(200);
				}
        },
        hide: {
        	event:'mouseleave',
        	fixed:true
        },
        style: {
       	  classes:'ui-tooltip-light ui-tooltip-shadow'
        }
 	});*/
    $('#toggleSidebar').closest(".viewFramework-sidebar-toggle").animate({ "left": "10px", "opacity": 1 });

    $.setCookie("isMainMenuOpen","false");
}
function watchSliderviewPort(){
	if($SidebarCont.height() <= clientHeight) {
		$SidebarCont.animate({top : 0});
	}
}
//加载资源树
var aryTreeData;
function loadTree(resId) {
	var url = ctxPath+"/oa/console/getUserResMenu.do";
	$.ajax({
		url: url,
		type : 'POST',
		dataType : 'json',
		async : false,
		success : function(result) {
			aryTreeData = result;
		}
	});
	var setting = {
			data : {
				key : {
					name : "resName"
				},
				simpleData : {
					enable : true,
					idKey : "resId",
					pIdKey : "parentId"
				}
			}
	};
	//加载树
	var nodes = new Array();
	if(resId==undefined || resId==""){
		//根节点 parentId == 1
		var headers = getRootNodes();
		resId = headers[0].resId;
	}
	getChildByParentId(resId, nodes);
	zTreeObj=$.fn.zTree.init($("#sidebar_tree"), setting, nodes);
	//zTreeObj.expandAll(false);
}
//返回根节点
function getRootNodes() {
	var nodes = new Array();
	for ( var i = 0; i < aryTreeData.length; i++) {
		var node = aryTreeData[i];
		if (node.parentId == 1) {
			nodes.push(node);
		}
	}
	return nodes;
};
//关联父子节点
function getChildByParentId(parentId, nodes) {
	for ( var i = 0; i < aryTreeData.length; i++) {
		var node = aryTreeData[i];
		if (node.parentId == parentId) {
			node.icon = node.icon;
			if(node.defaultUrl!=null){
				node.defaultUrl = ctxPath + node.defaultUrl;
			}else{
				node.defaultUrl = "";
			}
			nodes.push(node);
			getChildByParentId(node.resId, nodes);
		}
	}
};

/****菜单全部在头部：菜单加载****/
//菜单初始化
function menuEach(menuList){
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
			$('#sidemenu').append('<li alias='+_obj.alias+' class="firstmenu"><p menuId='+_obj.menuId+' name='+_obj.name+' url='+_obj.url+' img='+_obj.icon+'><span class="menu-icon '+_obj.icon+'"></span><span class="menu-font">'+_obj.name+'</span><span class="menu-select"></span></p></li>')
		}else{
			$('#sidemenu').append('<li alias='+_obj.alias+' class="firstmenu"><p menuId='+_obj.menuId+' name='+_obj.name+' img='+_obj.icon+'><span class="menu-icon '+_obj.icon+'"></span><span class="menu-font">'+_obj.name+'</span><span class="menu-select"></span></p></li>')
		}
		var childList = element.childMenuList;
		var aliasArgu = _obj.alias;
		if (childList.length>0) {
			childEach(childList,aliasArgu);
		}
	}
}
function childEach(childList,aliasArgu){
	$('li[alias='+aliasArgu+']').append('<ul class="secondmenu"></ul>');
	for (var key in childList) {
		var _childObj = {};
		var element = childList[key];
		_childObj.alias = element.alias;
		_childObj.name = element.resName;
		_childObj.icon = element.icon;
		_childObj.menuId = element.resId;
		_childObj.url = element.defaultUrl;
		var _childList = element.childMenuList;
		var _aliasArgu = _childObj.alias;
		var title = '<span style="margin-right: 5px;" class="'+_childObj.icon+'"></span><span>'+_childObj.name+'</span>';

		if(_childObj.url!=null&&_childObj.url!=""){
			title= '<a href="javascript:;">'+title+'</a>';
		}
		if (_childList.length>0) {
			$('li[alias='+aliasArgu+']>ul').append('<li alias='+_childObj.alias+'><p class="hasnext icon-display-2" menuId="'+_childObj.menuId+'" name="'+_childObj.name+'" url="'+_childObj.url+'" img="'+_childObj.icon+'">'+title+'</p></li>');
			childEach(_childList,_aliasArgu);
		}else{
			$('li[alias='+aliasArgu+']>ul').append('<li alias='+_childObj.alias+'><p class="hasnext icon-display-0" menuId="'+_childObj.menuId+'" name="'+_childObj.name+'" url="'+_childObj.url+'" img="'+_childObj.icon+'">'+title+'</p></li>');
		}
	}
}
//加载菜单事件
function attachMenuEvents(){
	$('.sidemenu li.firstmenu>p').click(function(){
		var firsetMenu = $(this).parent();
		var subMenu = firsetMenu.find('>ul');
		if(subMenu.css("display")=="block"){
			subMenu.css("display","none");
			$(this).removeClass('hoverBg');
			$(this).removeClass('clickBg');
		}else{

			var _firstmenu = $('.sidemenu li.firstmenu');
			_firstmenu.find('>p').removeClass('hoverBg');
			_firstmenu.find('>p').removeClass('clickBg');
			_firstmenu.find('>ul').css("display","none");

			subMenu.css("display","block");
			$(this).addClass('clickBg');
			setSubMenuHeight(firsetMenu);
		}
		showTab($(this));
	});
	$('.sidemenu .secondmenu>li>p.hasnext').click(function(){
		showTab($(this));
		dealFirstMenu($(this));
	});

	$('.sidemenu .threemenu>li>p.hasnext').click(function(){
		showTab($(this));
		dealFirstMenu($(this));
	});
	$('.fourmenu>li').click(function(){
		showTab($(this));
		dealFirstMenu($(this));
	});

	$('.sidemenu li.firstmenu').mouseenter(function(){
		/*var firstmenu = $('.sidemenu li.firstmenu');
		firstmenu.find('>p').removeClass('hoverBg');
		firstmenu.find('>ul').css("display","none");*/

		$(this).find('>p').addClass('hoverBg');
		/*$(this).find('>ul').css("display","block");*/
		setSubMenuHeight($(this));
	}).mouseleave(function(){
		$(this).find('>p').removeClass('hoverBg');
	});
	$('.sidemenu .secondmenu>li').mouseenter(function(){
		var me = $(this);
		var secondmenu = me.parent().find(">li");
		secondmenu.each(function() {
			if($(this)!=me){
				$(this).find('>p').removeClass('subHoverBg');
				$(this).find('>ul').css("display","none");
			}
		});

		$(this).find('>p').addClass('subHoverBg');
		$(this).find('>ul').css("display","block");
		setSubMenuHeight($(this));
	}).mouseleave(function(){
		$(this).find('>p').removeClass('subHoverBg');
		/*var me = $(this);
		setTimeout(function(){
			me.find('>ul').css("display","none");
		},300);*/
	});
}

function dealFirstMenu(obj){
	var firstmenu = $('.sidemenu li.firstmenu');
	var url = obj.attr('url');
	if(url!=null && url!=""){
		firstmenu.find('ul').css("display","none");
		firstmenu.find('>p').removeClass('hoverBg');
		firstmenu.find('>p').removeClass('clickBg');
		firstmenu.find('p').removeClass('subHoverBg');
	}
}

function setSubMenuHeight(obj){
	var height = window.top.document.documentElement.clientHeight;
	var menuTop = obj.offset().top;
	var menuHeight = obj.find('>ul.secondmenu').height();

	if(obj.hasClass("firstmenu")){
		menuTop = menuTop + 40;
	}

	if(menuTop + menuHeight >height){
		var subMenu = obj.find('>ul.secondmenu');
		subMenu.height(height-menuTop-10);
		subMenu.css("overflow","auto");
	}
}
//布局大小改变的时候通知tab，面板改变大小
function heightChanged(options) {
    diff = options.diff;
	//主界面距离菜单高度重新计算
	//var top = $("#sidemenu").height();
	//$("#layoutMain").css("top",top+"px");

	$("iframe").each(function() {
		var tabName = $(this).attr("name");
		if (tabName != undefined) {
			$(this).height(options.middleHeight - 35);
		}
		/*if (tabName == "home") {
			$(this).attr('src', $(this).attr('src'));
		}*/
	});
	if (tab) {
		var tabContent = $(".l-tab-content"), h = tabContent.height();
		tabContent.height(h + options.diff + 15);
        $("> .l-tab-content-item", tabContent).height(tabContent.height());
	}
	/*if (accordion && options.middleHeight - 25 > 0){
		accordion.setHeight(options.middleHeight - 25);
	}*/
}

//左边收缩事件
function leftToggle(isCollapse){

}