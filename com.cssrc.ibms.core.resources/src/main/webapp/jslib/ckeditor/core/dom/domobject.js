CKEDITOR.dom.domObject=function(a){if(a){this.$=a;}};CKEDITOR.dom.domObject.prototype=(function(){var a=function(c,b){return function(d){if(typeof CKEDITOR!="undefined"){c.fire(b,new CKEDITOR.dom.event(d));}};};return{getPrivate:function(){var b;if(!(b=this.getCustomData("_"))){this.setCustomData("_",(b={}));}return b;},on:function(b){var d=this.getCustomData("_cke_nativeListeners");if(!d){d={};this.setCustomData("_cke_nativeListeners",d);}if(!d[b]){var c=d[b]=a(this,b);if(this.$.attachEvent){this.$.attachEvent("on"+b,c);}else{if(this.$.addEventListener){this.$.addEventListener(b,c,!!CKEDITOR.event.useCapture);}}}return CKEDITOR.event.prototype.on.apply(this,arguments);},removeListener:function(b){CKEDITOR.event.prototype.removeListener.apply(this,arguments);if(!this.hasListeners(b)){var d=this.getCustomData("_cke_nativeListeners");var c=d&&d[b];if(c){if(this.$.detachEvent){this.$.detachEvent("on"+b,c);}else{if(this.$.removeEventListener){this.$.removeEventListener(b,c,false);}}delete d[b];}}},removeAllListeners:function(){var d=this.getCustomData("_cke_nativeListeners");for(var b in d){var c=d[b];if(this.$.detachEvent){this.$.detachEvent("on"+b,c);}else{if(this.$.removeEventListener){this.$.removeEventListener(b,c,false);}}delete d[b];}}};})();(function(a){var b={};CKEDITOR.on("reset",function(){b={};});a.equals=function(c){return(c&&c.$===this.$);};a.setCustomData=function(c,f){var d=this.getUniqueId(),e=b[d]||(b[d]={});e[c]=f;return this;};a.getCustomData=function(c){var d=this.$["data-cke-expando"],e=d&&b[d];return e&&e[c];};a.removeCustomData=function(d){var e=this.$["data-cke-expando"],f=e&&b[e],c=f&&f[d];if(typeof c!="undefined"){delete f[d];}return c||null;};a.clearCustomData=function(){this.removeAllListeners();var c=this.$["data-cke-expando"];c&&delete b[c];};a.getUniqueId=function(){return this.$["data-cke-expando"]||(this.$["data-cke-expando"]=CKEDITOR.tools.getNextNumber());};CKEDITOR.event.implementOn(a);})(CKEDITOR.dom.domObject.prototype);