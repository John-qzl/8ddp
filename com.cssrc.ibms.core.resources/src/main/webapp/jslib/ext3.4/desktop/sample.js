//
//MyDesktop = new Ext.app.App({
//	init :function(){
//		Ext.QuickTips.init();
//	},
//	
//	getModules : function(){
//		  var items = [];  
//		  var itemMenus = curUserInfo.treeNodes;
//		  for(var i=0;i<itemMenus.length;i++){
//		  	items.push(new MyDesktop.BogusMenuModule(itemMenus[i]));
//		  }
//		  return items;
//	},
//	
//    // config for the start menu
//   getStartConfig : function(){
//        return {
//            title: curUserInfo.username,
//            iconCls: 'user',
//            toolItems: [{
//                text:'Settings',
//                iconCls:'settings',
//                scope:this,
//                handler:this.settings
//            },'-',{
//                text:'Logout',
//                iconCls:'logout',
//                scope:this,
//                handler:this.logout
//            }]
//        };
//    },
//    settings : function(){
//      alert('setting');
//    },
//    logout : function(){
//      alert('logout');
//    }
//});
//
//MyDesktop.BogusModule = Ext.extend(Ext.app.Module, {
//    init : function(){
//        this.launcher = {
//            text: this.text,
//            iconCls:this.iconCls,
//            handler : this.createWindow,
//            scope: this,
//            windowId:this.windowId
//        }
//    },
//    createWindow:function(src){
//    	var tag = src.url.indexOf("/js/");
//        var desktop =this.app.getDesktop();
//        var win = desktop.getWindow('bogus'+src.windowId);
//        if(tag>=0){
//        	if(!win){
//        	    seajs.use("ibmsJs" + src.url, function(mainFrame) {
//					var panel = mainFrame.init({
//								text : '',
//								iconCls : '',
//								headerCfg :{}
//							});
//
//					     win = desktop.createWindow({
//			                id: 'bogus'+src.windowId,
//			                layout : "fit",
//			                title:src.text,
//			                width:640,
//			                height:480,
//			                iconCls: src.iconCls,
//			                shim:true,
//			                animCollapse:false,
//			                constrainHeader:true,
//			                hideHeaders : true,
//			                items:[panel]
//			             });
//			             win.show();
//        	    });
//        	}else{
//        	   win.show();
//        	}
//        }
//        else{
//            if(!win){
//               win = desktop.createWindow({
//                id: 'bogus'+src.windowId,
//                title:src.text,
//                width:640,
//                height:480,
//                layout : "fit",
//			    html : '<iframe width="100%" height="100%" marginwidth="0" framespacing="0" marginheight="0" frameborder="0" src = "' +  __ctxPath + src.url+ '"></iframe>',
//                iconCls: src.iconCls,
//                shim:false,
//                animCollapse:false,
//                constrainHeader:true
//             });
//            }win.show();
//        }
//    }
//});
//
//MyDesktop.BogusMenuModule = Ext.extend(MyDesktop.BogusModule, {
//	id:'bogus' + this.id,
//    init : function(){
//        this.launcher = {
//            text: this.text,
//            iconCls: this.iconCls,
//            handler: function() {
//				return false;
//			},
//            menu: {
//                items:[
//                         childMenu(this.children,this)
//                	]
//            }
//        }
//    }
//});
//
//var childMenu = function(children,self){
//	   var items = [];
//	   for(var i=0;i<children.length;i++){
//	   	  var obj = {};
//	   	   obj = {
//	   	   	id:'bogus' + children[i].id,
//            text: children[i].text,
//            iconCls:children[i].iconCls,
//            handler : self.createWindow,
//            scope: self,
//            windowId: 'bogus' + children[i].id,
//            url : children[i].url
//         }
//	   	if(children[i].children!=null&&children[i].children.length>0){
//	   	   obj.menu = {};
//	   	   obj.menu.items = [];
//	   	   obj.menu.items = childMenu(children[i].children,self);
//	   	}
//	   	items.push(obj);
//	   }
// 	   return items;
//};
//
//
//
//
//
//
//
//
//
//
