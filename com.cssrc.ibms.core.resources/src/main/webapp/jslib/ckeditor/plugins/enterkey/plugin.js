(function(){CKEDITOR.plugins.add("enterkey",{requires:["keystrokes","indent"],init:function(h){h.addCommand("enter",{modes:{wysiwyg:1},editorFocus:false,exec:function(j){f(j);}});h.addCommand("shiftEnter",{modes:{wysiwyg:1},editorFocus:false,exec:function(j){d(j);}});var i=h.keystrokeHandler.keystrokes;i[13]="enter";i[CKEDITOR.SHIFT+13]="shiftEnter";}});CKEDITOR.plugins.enterkey={enterBlock:function(n,s,t,p){t=t||e(n);if(!t){return;}var E=t.document;var D=t.checkStartOfBlock(),u=t.checkEndOfBlock(),v=new CKEDITOR.dom.elementPath(t.startContainer),q=v.block;if(D&&u){if(q&&(q.is("li")||q.getParent().is("li"))){n.execCommand("outdent");return;}if(q&&q.getParent().is("blockquote")){q.breakParent(q.getParent());if(!q.getPrevious().getFirst(CKEDITOR.dom.walker.invisible(1))){q.getPrevious().remove();}if(!q.getNext().getFirst(CKEDITOR.dom.walker.invisible(1))){q.getNext().remove();}t.moveToElementEditStart(q);t.select();return;}}else{if(q&&q.is("pre")){if(!u){a(n,s,t,p);return;}}else{if(q&&CKEDITOR.dtd.$captionBlock[q.getName()]){a(n,s,t,p);return;}}}var C=(s==CKEDITOR.ENTER_DIV?"div":"p");var z=t.splitBlock(C);if(!z){return;}var o=z.previousBlock,r=z.nextBlock;var k=z.wasStartOfBlock,w=z.wasEndOfBlock;var x;if(r){x=r.getParent();if(x.is("li")){r.breakParent(x);r.move(r.getNext(),1);}}else{if(o&&(x=o.getParent())&&x.is("li")){o.breakParent(x);x=o.getNext();t.moveToElementEditStart(x);o.move(o.getPrevious());}}if(!k&&!w){if(r.is("li")&&(x=r.getFirst(CKEDITOR.dom.walker.invisible(true)))&&x.is&&x.is("ul","ol")){(CKEDITOR.env.ie?E.createText("\xa0"):E.createElement("br")).insertBefore(x);}if(r){t.moveToElementEditStart(r);}}else{var l,m;if(o){if(o.is("li")||!(b.test(o.getName())||o.is("pre"))){l=o.clone();}}else{if(r){l=r.clone();}}if(!l){if(x&&x.is("li")){l=x;}else{l=E.createElement(C);if(o&&(m=o.getDirection())){l.setAttribute("dir",m);}}}else{if(p&&!l.is("li")){l.renameNode(C);}}var h=z.elementPath;if(h){for(var y=0,A=h.elements.length;y<A;y++){var j=h.elements[y];if(j.equals(h.block)||j.equals(h.blockLimit)){break;}if(CKEDITOR.dtd.$removeEmpty[j.getName()]){j=j.clone();l.moveChildren(j);l.append(j);}}}if(!CKEDITOR.env.ie){l.appendBogus();}if(!l.getParent()){t.insertNode(l);}l.is("li")&&l.removeAttribute("value");if(CKEDITOR.env.ie&&k&&(!w||!o.getChildCount())){t.moveToElementEditStart(w?o:l);t.select();}t.moveToElementEditStart(k&&!w?r:l);}if(!CKEDITOR.env.ie){if(r){var B=E.createElement("span");B.setHtml("&nbsp;");t.insertNode(B);B.scrollIntoView();t.deleteContents();}else{l.scrollIntoView();}}t.select();},enterBr:function(p,o,m,k){m=m||e(p);if(!m){return;}var u=m.document;var n=(o==CKEDITOR.ENTER_DIV?"div":"p");var h=m.checkEndOfBlock();var v=new CKEDITOR.dom.elementPath(p.getSelection().getStartElement());var r=v.block,l=r&&v.block.getName();var i=false;if(!k&&l=="li"){g(p,o,m,k);return;}if(!k&&h&&b.test(l)){var q,t;if((t=r.getDirection())){q=u.createElement("div");q.setAttribute("dir",t);q.insertAfter(r);m.setStart(q,0);}else{u.createElement("br").insertAfter(r);if(CKEDITOR.env.gecko){u.createText("").insertAfter(r);}m.setStartAt(r.getNext(),CKEDITOR.env.ie?CKEDITOR.POSITION_BEFORE_START:CKEDITOR.POSITION_AFTER_START);}}else{var s;i=(l=="pre");if(i&&!CKEDITOR.env.gecko){s=u.createText(CKEDITOR.env.ie?"\r":"\n");}else{s=u.createElement("br");}m.deleteContents();m.insertNode(s);if(CKEDITOR.env.ie){m.setStartAt(s,CKEDITOR.POSITION_AFTER_END);}else{u.createText("\ufeff").insertAfter(s);if(h){s.getParent().appendBogus();}s.getNext().$.nodeValue="";m.setStartAt(s.getNext(),CKEDITOR.POSITION_AFTER_START);var j=null;if(!CKEDITOR.env.gecko){j=u.createElement("span");j.setHtml("&nbsp;");}else{j=u.createElement("br");}j.insertBefore(s.getNext());j.scrollIntoView();j.remove();}}m.collapse(true);m.select(i);}};var c=CKEDITOR.plugins.enterkey,a=c.enterBr,g=c.enterBlock,b=/^h[1-6]$/;function d(h){if(h.mode!="wysiwyg"){return false;}return f(h,h.config.shiftEnterMode,1);}function f(h,i,j){j=h.config.forceEnterMode||j;if(h.mode!="wysiwyg"){return false;}if(!i){i=h.config.enterMode;}setTimeout(function(){h.fire("saveSnapshot");if(i==CKEDITOR.ENTER_BR){a(h,i,null,j);}else{g(h,i,null,j);}h.fire("saveSnapshot");},0);return true;}function e(k){var h=k.getSelection().getRanges(true);for(var j=h.length-1;j>0;j--){h[j].deleteContents();}return h[0];}})();