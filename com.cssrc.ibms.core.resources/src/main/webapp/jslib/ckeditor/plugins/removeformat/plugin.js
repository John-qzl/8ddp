CKEDITOR.plugins.add("removeformat",{requires:["selection"],init:function(a){a.addCommand("removeFormat",CKEDITOR.plugins.removeformat.commands.removeformat);a.ui.addButton("RemoveFormat",{label:a.lang.removeFormat,command:"removeFormat"});a._.removeFormat={filters:[]};}});CKEDITOR.plugins.removeformat={commands:{removeformat:{exec:function(k){var j=k._.removeFormatRegex||(k._.removeFormatRegex=new RegExp("^(?:"+k.config.removeFormatTags.replace(/,/g,"|")+")$","i"));var m=k._.removeAttributes||(k._.removeAttributes=k.config.removeFormatAttributes.split(","));var c=CKEDITOR.plugins.removeformat.filter;var a=k.getSelection().getRanges(1),g=a.createIterator(),h;while((h=g.getNextRange())){if(!h.collapsed){h.enlarge(CKEDITOR.ENLARGE_ELEMENT);}var l=h.createBookmark(),d=l.startNode,i=l.endNode,b;var f=function(q){var r=new CKEDITOR.dom.elementPath(q),n=r.elements;for(var o=1,p;p=n[o];o++){if(p.equals(r.block)||p.equals(r.blockLimit)){break;}if(j.test(p.getName())&&c(k,p)){q.breakParent(p);}}};f(d);if(i){f(i);b=d.getNextSourceNode(true,CKEDITOR.NODE_ELEMENT);while(b){if(b.equals(i)){break;}var e=b.getNextSourceNode(false,CKEDITOR.NODE_ELEMENT);if(!(b.getName()=="img"&&b.data("cke-realelement"))&&c(k,b)){if(j.test(b.getName())){b.remove(1);}else{b.removeAttributes(m);k.fire("removeFormatCleanup",b);}}b=e;}}h.moveToBookmark(l);}k.getSelection().selectRanges(a);}}},filter:function(c,b){var d=c._.removeFormat.filters;for(var a=0;a<d.length;a++){if(d[a](b)===false){return false;}}return true;}};CKEDITOR.editor.prototype.addRemoveFormatFilter=function(a){this._.removeFormat.filters.push(a);};CKEDITOR.config.removeFormatTags="b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var";CKEDITOR.config.removeFormatAttributes="class,style,lang,width,height,align,hspace,valign";