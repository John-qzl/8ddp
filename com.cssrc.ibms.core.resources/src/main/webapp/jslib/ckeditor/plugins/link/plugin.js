CKEDITOR.plugins.add("link",{init:function(b){b.addCommand("link",new CKEDITOR.dialogCommand("link"));b.addCommand("anchor",new CKEDITOR.dialogCommand("anchor"));b.addCommand("unlink",new CKEDITOR.unlinkCommand());b.addCommand("removeAnchor",new CKEDITOR.removeAnchorCommand());b.ui.addButton("Link",{label:b.lang.link.toolbar,command:"link"});b.ui.addButton("Unlink",{label:b.lang.unlink,command:"unlink"});b.ui.addButton("Anchor",{label:b.lang.anchor.toolbar,command:"anchor"});CKEDITOR.dialog.add("link",this.path+"dialogs/link.js");CKEDITOR.dialog.add("anchor",this.path+"dialogs/anchor.js");var a=(b.lang.dir=="rtl"?"right":"left");var c="background:url("+CKEDITOR.getUrl(this.path+"images/anchor.gif")+") no-repeat "+a+" center;"+"border:1px dotted #00f;";b.addCss("a.cke_anchor,a.cke_anchor_empty"+((CKEDITOR.env.ie&&CKEDITOR.env.version<7)?"":",a[name],a[data-cke-saved-name]")+"{"+c+"padding-"+a+":18px;"+"cursor:auto;"+"}"+(CKEDITOR.env.ie?("a.cke_anchor_empty"+"{"+"display:inline-block;"+"}"):"")+"img.cke_anchor"+"{"+c+"width:16px;"+"min-height:15px;"+"height:1.15em;"+"vertical-align:"+(CKEDITOR.env.opera?"middle":"text-bottom")+";"+"}");b.on("selectionChange",function(d){if(b.readOnly){return;}var f=b.getCommand("unlink"),e=d.data.path.lastElement&&d.data.path.lastElement.getAscendant("a",true);if(e&&e.getName()=="a"&&e.getAttribute("href")&&e.getChildCount()){f.setState(CKEDITOR.TRISTATE_OFF);}else{f.setState(CKEDITOR.TRISTATE_DISABLED);}});b.on("doubleclick",function(d){var e=CKEDITOR.plugins.link.getSelectedLink(b)||d.data.element;if(!e.isReadOnly()){if(e.is("a")){d.data.dialog=(e.getAttribute("name")&&(!e.getAttribute("href")||!e.getChildCount()))?"anchor":"link";b.getSelection().selectElement(e);}else{if(CKEDITOR.plugins.link.tryRestoreFakeAnchor(b,e)){d.data.dialog="anchor";}}}});if(b.addMenuItems){b.addMenuItems({anchor:{label:b.lang.anchor.menu,command:"anchor",group:"anchor",order:1},removeAnchor:{label:b.lang.anchor.remove,command:"removeAnchor",group:"anchor",order:5},link:{label:b.lang.link.menu,command:"link",group:"link",order:1},unlink:{label:b.lang.unlink,command:"unlink",group:"link",order:5}});}if(b.contextMenu){b.contextMenu.addListener(function(e,f){if(!e||e.isReadOnly()){return null;}var d=CKEDITOR.plugins.link.tryRestoreFakeAnchor(b,e);if(!d&&!(d=CKEDITOR.plugins.link.getSelectedLink(b))){return null;}var g={};if(d.getAttribute("href")&&d.getChildCount()){g={link:CKEDITOR.TRISTATE_OFF,unlink:CKEDITOR.TRISTATE_OFF};}if(d&&d.hasAttribute("name")){g.anchor=g.removeAnchor=CKEDITOR.TRISTATE_OFF;}return g;});}},afterInit:function(b){var a=b.dataProcessor,d=a&&a.dataFilter,e=a&&a.htmlFilter,c=b._.elementsPath&&b._.elementsPath.filters;if(d){d.addRules({elements:{a:function(i){var h=i.attributes;if(!h.name){return null;}var j=!i.children.length;if(CKEDITOR.plugins.link.synAnchorSelector){var g=j?"cke_anchor_empty":"cke_anchor";var f=h["class"];if(h.name&&(!f||f.indexOf(g)<0)){h["class"]=(f||"")+" "+g;}if(j&&CKEDITOR.plugins.link.emptyAnchorFix){h.contenteditable="false";h["data-cke-editable"]=1;}}else{if(CKEDITOR.plugins.link.fakeAnchor&&j){return b.createFakeParserElement(i,"cke_anchor","anchor");}}return null;}}});}if(CKEDITOR.plugins.link.emptyAnchorFix&&e){e.addRules({elements:{a:function(f){delete f.attributes.contenteditable;}}});}if(c){c.push(function(g,f){if(f=="a"){if(CKEDITOR.plugins.link.tryRestoreFakeAnchor(b,g)||(g.getAttribute("name")&&(!g.getAttribute("href")||!g.getChildCount()))){return"anchor";}}});}},requires:["fakeobjects"]});CKEDITOR.plugins.link={getSelectedLink:function(d){try{var c=d.getSelection();if(c.getType()==CKEDITOR.SELECTION_ELEMENT){var g=c.getSelectedElement();if(g.is("a")){return g;}}var b=c.getRanges(true)[0];b.shrink(CKEDITOR.SHRINK_TEXT);var a=b.getCommonAncestor();return a.getAscendant("a",true);}catch(f){return null;}},fakeAnchor:CKEDITOR.env.opera||CKEDITOR.env.webkit,synAnchorSelector:CKEDITOR.env.ie,emptyAnchorFix:CKEDITOR.env.ie&&CKEDITOR.env.version<8,tryRestoreFakeAnchor:function(b,a){if(a&&a.data("cke-real-element-type")&&a.data("cke-real-element-type")=="anchor"){var c=b.restoreRealElement(a);if(c.data("cke-saved-name")){return c;}}}};CKEDITOR.unlinkCommand=function(){};CKEDITOR.unlinkCommand.prototype={exec:function(f){var e=f.getSelection(),d=e.createBookmarks(),a=e.getRanges(),g,c;for(var b=0;b<a.length;b++){g=a[b].getCommonAncestor(true);c=g.getAscendant("a",true);if(!c){continue;}a[b].selectNodeContents(c);}e.selectRanges(a);f.document.$.execCommand("unlink",false,null);e.selectBookmarks(d);},startDisabled:true};CKEDITOR.removeAnchorCommand=function(){};CKEDITOR.removeAnchorCommand.prototype={exec:function(b){var d=b.getSelection(),c=d.createBookmarks(),a;if(d&&(a=d.getSelectedElement())&&(CKEDITOR.plugins.link.fakeAnchor&&!a.getChildCount()?CKEDITOR.plugins.link.tryRestoreFakeAnchor(b,a):a.is("a"))){a.remove(1);}else{if((a=CKEDITOR.plugins.link.getSelectedLink(b))){if(a.hasAttribute("href")){a.removeAttributes({name:1,"data-cke-saved-name":1});a.removeClass("cke_anchor");}else{a.remove(1);}}}d.selectBookmarks(c);}};CKEDITOR.tools.extend(CKEDITOR.config,{linkShowAdvancedTab:true,linkShowTargetTab:true});