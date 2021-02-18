define(function(require, exports, module) {
    var deskTopForm = require("ibmsJs/js/system/DeskTopForm");
    var returnResult = [];
	App.PortletView = Ext.extend(Ext.ux.Portlet, {
			constructor : function(a) {
				Ext.applyIf(this, a);
				this.initTool();
				App.PortletView.superclass.constructor.call(this, {
							id : this.id,
							title : this.title,
							iconCls : this.iconCls,
							tools : this.tools,
							autoLoad : {
								url : this.url,
								scripts : true
							}
						});
			},
			initTool : function() {
				this.tools = [{
							id : "refresh",
							scope : this,
							handler : function() {
								this.getUpdater().update(this.url);
							}
						}, {
							id : "close",
							hidden : this.closed == null ? false : this.closed,
							scope : this,
							handler : function(c, b, a) {
								Ext.Msg.confirm("提示信息", "确认删除此模块吧？",
										function(d) {
											if (d == "yes") {
												a.ownerCt.remove(a, true);
											}
										});
							}
						}];
			}
		});
    exports.init = function(params) {
		var panel = {
			         title : '平台首页',
			         id:'DeskTop',
			         xtype:'portal',  
				     region:'center',
				     closable:false,
				     margins:'5 5 5 5',  
				     iconCls : "deskTop_icon",
					 items:[{
					           columnWidth:.66,  
					                             style:'padding:10px 10px 10px 10px',
					        items:[]
					       },
					       {
					           columnWidth:.33,  
					                             style:'padding:10px 10px 10px 10px',
					        items:[]
					       }],
					 fbar: [{
					        text: '添加面板',
					        scope : this,
							handler : function(){
							   openDeskTopForm(this);
							}
					    },{
					        text: '保存面板',
					        scope : this,
							handler : function(){
							  savePosition(Ext.getCmp("DeskTop")) ;
							}
					    }],
                        listeners : {
								scope : this,
								"afterrender" : function(){
								  initHomePage();
								} 
							}
					  }
		return panel;
	} 
   
   var openDeskTopForm = function(self){
		deskTopForm.init({
			scope : self,
			returnResult :returnResult,
			callback : function(){
			  refreshHomePage(self);
			}
		}).show();
     }
   
   var savePosition = function(portal) {  
            var result = [];  
            var items = portal.items;  
            for (var i = 0; i < items.getCount(); i++) {  
                var c = items.get(i);  
                c.items.each(function(portlet,index) {  
                            var o = {  
                                id : portlet.getId(),  
                                col : i,
    						    row : index
                            };
                            result.push(o); ;  
                        });  
            }  
            savePositionActoin(Ext.encode(result),'update');
    };
    
   var initHomePage = function(){
	 	Ext.Ajax.request({
					url :__ctxPath + "/oa/system/deskTop/initHomePage.do",
					method : "post",
					params : {
						
					},
					scope : this,
					success : function(c) {
						c =  Ext.util.JSON.decode(c.responseText);
						if(c.totalCounts*1>0){
							var portal = Ext.getCmp("DeskTop");
	                        var items = portal.items;
							var initData = c.result;
							    Ext.each(initData,function(item,index){
							    	var portlet = new App.PortletView({id:"p" +item.portalId,title:item.title,iconCls:'',url  : __ctxPath  + item.autoLoad +"?portalId=" + item.portalId +"&timestamp=" + Math.random()});
							        portal.items.get(item.colNum*1).add(portlet);
							        
							    },this);
							    portal.doLayout();
						}

					},
					failure : function() {
						Ext.ux.Toast.msg("操作信息", "个人桌面设置失败");
					}
				});
    };
    
   var createPanel = function(item){
     	var tools = [{
	        id:'refresh',
	        handler: function(e, target, panel){
	        	       panel.getUpdater().refresh();
	        }
	    },{
	        id:'close',
	        handler: function(e, target, panel){
	           panel.ownerCt.remove(panel, true);
	        }
	    }];
	    var result = new Ext.ux.Portlet({
	    	id:"p" +item.portalId ,//纯数字做id第一个panel无法拖动
		    title:item.title,
		    collapsible : true,  
		    draggable : true, 
		    cls : 'x-portlet',
		    tools : tools,
		    width:400,
		    autoScroll:true,
		    autoLoad:{
		      url  : __ctxPath  + item.autoLoad +"?portalId=" + item.portalId +"&timestamp=" + Math.random(),//页面内不能使用$(function(){});
		      scripts : true
		    }
	    });
	   return result;
   }

   var refreshHomePage = function(item){
   	 var result = []; 
     var portal = Ext.getCmp("DeskTop");
     var colNum=2
   	 for(var i=0;i<returnResult.length;i++){
        portal.items.get((i % colNum)*1).add(createPanel(returnResult[i]));
   	 }
   	  portal.doLayout();
    var items = portal.items;  
    for (var i = 0; i < items.getCount(); i++) {  
        var c = items.get(i);  
        c.items.each(function(portlet,index) {
        	        if(getDataById(portlet.getId())){
		        	       var o = {  
		                        id : portlet.getId(),  
		                        col : i,
							    row : index
		                    };
		                    result.push(o);
        	        }
                });  
    }
    for(var i=returnResult.length;i>0;i--){
        returnResult.pop();
    }
    savePositionActoin(Ext.encode(result),'add');
   }
   
   var savePositionActoin = function(data,method){
   	   	 	Ext.Ajax.request({
					url :__ctxPath + "/oa/system/deskTop/savePosition.do",
					method : "post",
					params : {
						data : data,
						method:method
					},
					scope : this,
					success : function(c) {
                          if(method=='update'){
                              c = Ext.util.JSON.decode(c.responseText);
                              if('success'==c.result){
                                 Ext.ux.Toast.msg("操作信息", '保存成功');
                              }else{
                                 Ext.ux.Toast.msg("操作信息", "保存失败");
                              }
                             
                          }
					},
					failure : function() {
						Ext.ux.Toast.msg("操作信息", "个人桌面设置失败");
					}
				});
   }
   
   var getDataById = function(id){
   	   var result = false;
	   for(var i = 0; i < returnResult.length; i++){
	       if('p'+returnResult[i].portalId == id){
	        result = true
	     break;
	    }
	   }
	   return result;
  };
	

});

