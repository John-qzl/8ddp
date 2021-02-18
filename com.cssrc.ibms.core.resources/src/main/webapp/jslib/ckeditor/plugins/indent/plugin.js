(function(){var b={ol:1,ul:1},g=CKEDITOR.dom.walker.whitespaces(true),f=CKEDITOR.dom.walker.bookmark(false,true);function e(i){if(i.editor.readOnly){return null;}var m=i.editor,l=i.data.path,o=l&&l.contains(b),n=l.block||l.blockLimit;if(o){return this.setState(CKEDITOR.TRISTATE_OFF);}if(!this.useIndentClasses&&this.name=="indent"){return this.setState(CKEDITOR.TRISTATE_OFF);}if(!n){return this.setState(CKEDITOR.TRISTATE_DISABLED);}if(this.useIndentClasses){var k=n.$.className.match(this.classNameRegex),j=0;if(k){k=k[1];j=this.indentClassMap[k];}if((this.name=="outdent"&&!j)||(this.name=="indent"&&j==m.config.indentClasses.length)){return this.setState(CKEDITOR.TRISTATE_DISABLED);}return this.setState(CKEDITOR.TRISTATE_OFF);}else{var h=parseInt(n.getStyle(c(n)),10);if(isNaN(h)){h=0;}if(h<=0){return this.setState(CKEDITOR.TRISTATE_DISABLED);}return this.setState(CKEDITOR.TRISTATE_OFF);}}function a(k,h){this.name=h;this.useIndentClasses=k.config.indentClasses&&k.config.indentClasses.length>0;if(this.useIndentClasses){this.classNameRegex=new RegExp("(?:^|\\s+)("+k.config.indentClasses.join("|")+")(?=$|\\s)");this.indentClassMap={};for(var j=0;j<k.config.indentClasses.length;j++){this.indentClassMap[k.config.indentClasses[j]]=j+1;}}this.startDisabled=h=="outdent";}function c(i,h){return(h||i.getComputedStyle("direction"))=="ltr"?"margin-left":"margin-right";}function d(h){return h.type==CKEDITOR.NODE_ELEMENT&&h.is("li");}a.prototype={exec:function(k){var t=this,r={};function y(F){var U=s.startContainer,V=s.endContainer;while(U&&!U.getParent().equals(F)){U=U.getParent();}while(V&&!V.getParent().equals(F)){V=V.getParent();}if(!U||!V){return;}var N=U,M=[],R=false;while(!R){if(N.equals(V)){R=true;}M.push(N);N=N.getNext();}if(M.length<1){return;}var O=F.getParents(true);for(var S=0;S<O.length;S++){if(O[S].getName&&b[O[S].getName()]){F=O[S];break;}}var T=t.name=="indent"?1:-1,C=M[0],P=M[M.length-1];var K=CKEDITOR.plugins.list.listToArray(F,r);var A=K[P.getCustomData("listarray_index")].indent;for(S=C.getCustomData("listarray_index");S<=P.getCustomData("listarray_index");S++){K[S].indent+=T;var J=K[S].parent;K[S].parent=new CKEDITOR.dom.element(J.getName(),J.getDocument());}for(S=P.getCustomData("listarray_index")+1;S<K.length&&K[S].indent>A;S++){K[S].indent+=T;}var G=CKEDITOR.plugins.list.arrayToList(K,r,null,k.config.enterMode,F.getDirection());if(t.name=="outdent"){var B;if((B=F.getParent())&&B.is("li")){var E=G.listNode.getChildren(),I=[],L=E.count(),H;for(S=L-1;S>=0;S--){if((H=E.getItem(S))&&H.is&&H.is("li")){I.push(H);}}}}if(G){G.listNode.replace(F);}if(I&&I.length){for(S=0;S<I.length;S++){var Q=I[S],D=Q;while((D=D.getNext())&&D.is&&D.getName() in b){if(CKEDITOR.env.ie&&!Q.getFirst(function(W){return g(W)&&f(W);})){Q.append(s.document.createText("\u00a0"));}Q.append(D);}Q.insertAfter(B);}}}function h(){var B=s.createIterator(),A=k.config.enterMode;B.enforceRealBlocks=true;B.enlargeBr=A!=CKEDITOR.ENTER_BR;var C;while((C=B.getNextParagraph(A==CKEDITOR.ENTER_P?"p":"div"))){i(C);}}function i(D,C){if(D.getCustomData("indent_processed")){return false;}if(t.useIndentClasses){var B=D.$.className.match(t.classNameRegex),A=0;if(B){B=B[1];A=t.indentClassMap[B];}if(t.name=="outdent"){A--;}else{A++;}if(A<0){return false;}A=Math.min(A,k.config.indentClasses.length);A=Math.max(A,0);D.$.className=CKEDITOR.tools.ltrim(D.$.className.replace(t.classNameRegex,""));if(A>0){D.addClass(k.config.indentClasses[A-1]);}}else{var G=c(D,C),F=parseInt(D.getStyle(G),10);if(isNaN(F)){F=0;}var E=k.config.indentOffset||40;F+=(t.name=="indent"?1:-1)*E;if(F<0){return false;}F=Math.max(F,0);F=Math.ceil(F/E)*E;D.setStyle(G,F?F+(k.config.indentUnit||"px"):"");if(D.getAttribute("style")===""){D.removeAttribute("style");}}CKEDITOR.dom.element.setMarker(r,D,"indent_processed",1);return true;}var z=k.getSelection(),n=z.createBookmarks(1),j=z&&z.getRanges(1),s;var u=j.createIterator();while((s=u.getNextRange())){var q=s.getCommonAncestor(),o=q;while(o&&!(o.type==CKEDITOR.NODE_ELEMENT&&b[o.getName()])){o=o.getParent();}if(!o){var w=s.getEnclosedNode();if(w&&w.type==CKEDITOR.NODE_ELEMENT&&w.getName() in b){s.setStartAt(w,CKEDITOR.POSITION_AFTER_START);s.setEndAt(w,CKEDITOR.POSITION_BEFORE_END);o=w;}}if(o&&s.startContainer.type==CKEDITOR.NODE_ELEMENT&&s.startContainer.getName() in b){var v=new CKEDITOR.dom.walker(s);v.evaluator=d;s.startContainer=v.next();}if(o&&s.endContainer.type==CKEDITOR.NODE_ELEMENT&&s.endContainer.getName() in b){v=new CKEDITOR.dom.walker(s);v.evaluator=d;s.endContainer=v.previous();}if(o){var p=o.getFirst(d),l=!!p.getNext(d),x=s.startContainer,m=p.equals(x)||p.contains(x);if(!(m&&(t.name=="indent"||t.useIndentClasses||parseInt(o.getStyle(c(o)),10))&&i(o,!l&&p.getDirection()))){y(o);}}else{h();}}CKEDITOR.dom.element.clearAllMarkers(r);k.forceNextSelectionCheck();z.selectBookmarks(n);}};CKEDITOR.plugins.add("indent",{init:function(i){var h=i.addCommand("indent",new a(i,"indent")),j=i.addCommand("outdent",new a(i,"outdent"));i.ui.addButton("Indent",{label:i.lang.indent,command:"indent"});i.ui.addButton("Outdent",{label:i.lang.outdent,command:"outdent"});i.on("selectionChange",CKEDITOR.tools.bind(e,h));i.on("selectionChange",CKEDITOR.tools.bind(e,j));if(CKEDITOR.env.ie6Compat||CKEDITOR.env.ie7Compat){i.addCss("ul,ol"+"{"+"	margin-left: 0px;"+"	padding-left: 40px;"+"}");}i.on("dirChanged",function(r){var p=new CKEDITOR.dom.range(i.document);p.setStartBefore(r.data.node);p.setEndAfter(r.data.node);var k=new CKEDITOR.dom.walker(p),l;while((l=k.next())){if(l.type==CKEDITOR.NODE_ELEMENT){if(!l.equals(r.data.node)&&l.getDirection()){p.setStartAfter(l);k=new CKEDITOR.dom.walker(p);continue;}var n=i.config.indentClasses;if(n){var s=(r.data.dir=="ltr")?["_rtl",""]:["","_rtl"];for(var o=0;o<n.length;o++){if(l.hasClass(n[o]+s[0])){l.removeClass(n[o]+s[0]);l.addClass(n[o]+s[1]);}}}var m=l.getStyle("margin-right"),q=l.getStyle("margin-left");m?l.setStyle("margin-left",m):l.removeStyle("margin-left");q?l.setStyle("margin-right",q):l.removeStyle("margin-right");}}});},requires:["domiterator","list"]});})();