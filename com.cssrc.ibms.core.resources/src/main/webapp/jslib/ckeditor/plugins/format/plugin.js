CKEDITOR.plugins.add("format",{requires:["richcombo","styles"],init:function(e){var c=e.config,g=e.lang.format;var b=c.format_tags.split(";");var f={};for(var d=0;d<b.length;d++){var a=b[d];f[a]=new CKEDITOR.style(c["format_"+a]);f[a]._.enterMode=e.config.enterMode;}e.ui.addRichCombo("Format",{label:g.label,title:g.panelTitle,className:"cke_format",panel:{css:e.skin.editor.css.concat(c.contentsCss),multiSelect:false,attributes:{"aria-label":g.panelTitle}},init:function(){this.startGroup(g.panelTitle);for(var h in f){var i=g["tag_"+h];this.add(h,f[h].buildPreview(i),i);}},onClick:function(j){e.focus();e.fire("saveSnapshot");var i=f[j],h=new CKEDITOR.dom.elementPath(e.getSelection().getStartElement());i[i.checkActive(h)?"remove":"apply"](e.document);setTimeout(function(){e.fire("saveSnapshot");},0);},onRender:function(){e.on("selectionChange",function(k){var j=this.getValue();var i=k.data.path;for(var h in f){if(f[h].checkActive(i)){if(h!=j){this.setValue(h,e.lang.format["tag_"+h]);}return;}}this.setValue("");},this);}});}});CKEDITOR.config.format_tags="p;h1;h2;h3;h4;h5;h6;pre;address;div";CKEDITOR.config.format_p={element:"p"};CKEDITOR.config.format_div={element:"div"};CKEDITOR.config.format_pre={element:"pre"};CKEDITOR.config.format_address={element:"address"};CKEDITOR.config.format_h1={element:"h1"};CKEDITOR.config.format_h2={element:"h2"};CKEDITOR.config.format_h3={element:"h3"};CKEDITOR.config.format_h4={element:"h4"};CKEDITOR.config.format_h5={element:"h5"};CKEDITOR.config.format_h6={element:"h6"};