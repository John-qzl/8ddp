(function(){function a(b){b.data.mode="html";}CKEDITOR.plugins.add("pastefromword",{init:function(d){var b=0;var c=function(e){e&&e.removeListener();d.removeListener("beforePaste",a);b&&setTimeout(function(){b=0;},0);};d.addCommand("pastefromword",{canUndo:false,exec:function(){b=1;d.on("beforePaste",a);if(d.execCommand("paste","html")===false){d.on("dialogShow",function(e){e.removeListener();e.data.on("cancel",c);});d.on("dialogHide",function(e){e.data.removeListener("cancel",c);});}d.on("afterPaste",c);}});d.ui.addButton("PasteFromWord",{label:d.lang.pastefromword.toolbar,command:"pastefromword"});d.on("pasteState",function(e){d.getCommand("pastefromword").setState(e.data);});d.on("paste",function(e){var g=e.data,h;if((h=g["html"])&&(b||(/(class=\"?Mso|style=\"[^\"]*\bmso\-|w:WordDocument)/).test(h))){var f=this.loadFilterRules(function(){if(f){d.fire("paste",g);}else{if(!d.config.pasteFromWordPromptCleanup||(b||confirm(d.lang.pastefromword.confirmCleanup))){g["html"]=CKEDITOR.cleanWord(h,d);}}});f&&e.cancel();}},this);},loadFilterRules:function(d){var b=CKEDITOR.cleanWord;if(b){d();}else{var c=CKEDITOR.getUrl(CKEDITOR.config.pasteFromWordCleanupFile||(this.path+"filter/default.js"));CKEDITOR.scriptLoader.load(c,d,null,true);}return !b;},requires:["clipboard"]});})();