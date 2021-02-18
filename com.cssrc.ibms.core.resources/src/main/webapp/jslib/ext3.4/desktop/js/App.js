/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.app.App = function(cfg){
    Ext.apply(this, cfg);
    this.addEvents({
        'ready' : true,
        'beforeunload' : true
    });

    Ext.onReady(this.initApp, this);
};

Ext.extend(Ext.app.App, Ext.util.Observable, {
    isReady: false,
    startMenu: null,
    modules: null,
    getStartConfig : function(){

    },

    initApp : function(){
    	    	
    	this.startConfig = this.startConfig || this.getStartConfig();

        this.desktop = new Ext.Desktop(this);

		this.launcher = this.desktop.taskbar.startMenu;

		this.modules = this.getModules();
        if(this.modules){
            this.initModules(this.modules);
        }

        this.init();

        Ext.EventManager.on(window, 'beforeunload', this.onUnload, this);
		this.fireEvent('ready', this);
        this.isReady = true;
    },

    getModules : Ext.emptyFn,
    init : Ext.emptyFn,

    initModules : function(ms){
		for(var i = 0, len = ms.length; i < len; i++){
            var m = ms[i];
            this.launcher.add(m.launcher);
            m.app = this;
        }
    },
//被重写的方法<getModule>
//    getModule : function(name){
//    	var ms = this.modules;
//    	for(var i = 0, len = ms.length; i < len; i++){
//    		if(ms[i].id == name || ms[i].appType == name){
//    			return ms[i];
//			}
//        }
//        return '';
//    },

    onReady : function(fn, scope){
        if(!this.isReady){
            this.on('ready', fn, scope);
        }else{
            fn.call(scope, this);
        }
    },

    getDesktop : function(){
        return this.desktop;
    },

    onUnload : function(e){
        if(this.fireEvent('beforeunload', this) === false){
            e.stopEvent();
        }
    },
    
    getItem : function(name,items){
    	var res = null;
    	for(var i=0;i<items.length;i++){
    		var ob = items[i];
    		if(ob.menu!=null&&ob.menu.items.length>0){
    			res = this.getItem(name,ob.menu.items);
    		}
    		if(ob.id==name){
    			res = ob;
    		}
    		if(res!=null){
    			break;
    		}
    	}
    	return res;
    },
    
    getModule : function(name){  
        var ms = this.modules;  
        for(var i = 0, len = ms.length; i < len; i++){  
            if(ms[i].id == name || ms[i].appType == name){  
                return ms[i];  
            }  
            else  
            {  
                if(Ext.isDefined(ms[i].launcher.menu) == true && ms[i].launcher.menu.items.length > 0)  
                {  
                    for(var j = 0, lens = ms[i].launcher.menu.items.length; j < lens ; j++)  
                    {  
                    	var res = this.getItem(name,ms[i].launcher.menu.items[j]);
                    	if(res!=null){
                    		return res;
                    	}                    	
                    }  
                }  
            }  
        }  
        return '';  
    } ,
    

    
});