if(CKEDITOR.status=="unloaded"){(function(){CKEDITOR.event.implementOn(CKEDITOR);CKEDITOR.loadFullCore=function(){if(CKEDITOR.status!="basic_ready"){CKEDITOR.loadFullCore._load=1;return;}delete CKEDITOR.loadFullCore;var b=document.createElement("script");b.type="text/javascript";b.src=CKEDITOR.basePath+"ckeditor.js";document.getElementsByTagName("head")[0].appendChild(b);};CKEDITOR.loadFullCoreTimeout=0;CKEDITOR.replaceClass="ckeditor";CKEDITOR.replaceByClassEnabled=1;var a=function(b,d,c,f){if(CKEDITOR.env.isCompatible){if(CKEDITOR.loadFullCore){CKEDITOR.loadFullCore();}var e=c(b,d,f);CKEDITOR.add(e);return e;}return null;};CKEDITOR.replace=function(b,c){return a(b,c,CKEDITOR.editor.replace);};CKEDITOR.appendTo=function(b,c,d){return a(b,c,CKEDITOR.editor.appendTo,d);};CKEDITOR.add=function(b){var c=this._.pending||(this._.pending=[]);c.push(b);};CKEDITOR.replaceAll=function(){var b=document.getElementsByTagName("textarea");for(var e=0;e<b.length;e++){var d=null,c=b[e];if(!c.name&&!c.id){continue;}if(typeof arguments[0]=="string"){var f=new RegExp("(?:^|\\s)"+arguments[0]+"(?:$|\\s)");if(!f.test(c.className)){continue;}}else{if(typeof arguments[0]=="function"){d={};if(arguments[0](c,d)===false){continue;}}}this.replace(c,d);}};(function(){var b=function(){var d=CKEDITOR.loadFullCore,c=CKEDITOR.loadFullCoreTimeout;if(CKEDITOR.replaceByClassEnabled){CKEDITOR.replaceAll(CKEDITOR.replaceClass);}CKEDITOR.status="basic_ready";if(d&&d._load){d();}else{if(c){setTimeout(function(){if(CKEDITOR.loadFullCore){CKEDITOR.loadFullCore();}},c*1000);}}};if(window.addEventListener){window.addEventListener("load",b,false);}else{if(window.attachEvent){window.attachEvent("onload",b);}}})();CKEDITOR.status="basic_loaded";})();}