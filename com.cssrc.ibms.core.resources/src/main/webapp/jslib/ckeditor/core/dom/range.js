CKEDITOR.dom.range=function(a){this.startContainer=null;this.startOffset=null;this.endContainer=null;this.endOffset=null;this.collapsed=true;this.document=a;};(function(){var h=function(i){i.collapsed=(i.startContainer&&i.endContainer&&i.startContainer.equals(i.endContainer)&&i.startOffset==i.endOffset);};var g=function(w,B,m,l){w.optimizeBookmark();var J=w.startContainer;var y=w.endContainer;var E=w.startOffset;var s=w.endOffset;var A;var t;if(y.type==CKEDITOR.NODE_TEXT){y=y.split(s);}else{if(y.getChildCount()>0){if(s>=y.getChildCount()){y=y.append(w.document.createText(""));t=true;}else{y=y.getChild(s);}}}if(J.type==CKEDITOR.NODE_TEXT){J.split(E);if(J.equals(y)){y=J.getNext();}}else{if(!E){J=J.getFirst().insertBeforeMe(w.document.createText(""));A=true;}else{if(E>=J.getChildCount()){J=J.append(w.document.createText(""));A=true;}else{J=J.getChild(E).getPrevious();}}}var G=J.getParents();var o=y.getParents();var F,K,I;for(F=0;F<G.length;F++){K=G[F];I=o[F];if(!K.equals(I)){break;}}var H=m,x,v,q,u;for(var C=F;C<G.length;C++){x=G[C];if(H&&!x.equals(J)){v=H.append(x.clone());}q=x.getNext();while(q){if(q.equals(o[C])||q.equals(y)){break;}u=q.getNext();if(B==2){H.append(q.clone(true));}else{q.remove();if(B==1){H.append(q);}}q=u;}if(H){H=v;}}H=m;for(var z=F;z<o.length;z++){x=o[z];if(B>0&&!x.equals(y)){v=H.append(x.clone());}if(!G[z]||x.$.parentNode!=G[z].$.parentNode){q=x.getPrevious();while(q){if(q.equals(G[z])||q.equals(J)){break;}u=q.getPrevious();if(B==2){H.$.insertBefore(q.$.cloneNode(true),H.$.firstChild);}else{q.remove();if(B==1){H.$.insertBefore(q.$,H.$.firstChild);}}q=u;}}if(H){H=v;}}if(B==2){var n=w.startContainer;if(n.type==CKEDITOR.NODE_TEXT){n.$.data+=n.$.nextSibling.data;n.$.parentNode.removeChild(n.$.nextSibling);}var r=w.endContainer;if(r.type==CKEDITOR.NODE_TEXT&&r.$.nextSibling){r.$.data+=r.$.nextSibling.data;r.$.parentNode.removeChild(r.$.nextSibling);}}else{if(K&&I&&(J.$.parentNode!=K.$.parentNode||y.$.parentNode!=I.$.parentNode)){var p=I.getIndex();if(A&&I.$.parentNode==J.$.parentNode){p--;}if(l&&K.type==CKEDITOR.NODE_ELEMENT){var D=CKEDITOR.dom.element.createFromHtml("<span "+'data-cke-bookmark="1" style="display:none">&nbsp;</span>',w.document);D.insertAfter(K);K.mergeSiblings(false);w.moveToBookmark({startNode:D});}else{w.setStart(I.getParent(),p);}}w.collapse(true);}if(A){J.remove();}if(t&&y.$.parentNode){y.remove();}};var d={abbr:1,acronym:1,b:1,bdo:1,big:1,cite:1,code:1,del:1,dfn:1,em:1,font:1,i:1,ins:1,label:1,kbd:1,q:1,samp:1,small:1,span:1,strike:1,strong:1,sub:1,sup:1,tt:1,u:1,"var":1};function c(j){var k=false,i=CKEDITOR.dom.walker.bookmark(true);return function(l){if(i(l)){return true;}if(l.type==CKEDITOR.NODE_TEXT){if(l.hasAscendant("pre")||CKEDITOR.tools.trim(l.getText()).length){return false;}}else{if(l.type==CKEDITOR.NODE_ELEMENT){if(!d[l.getName()]){if(!j&&!CKEDITOR.env.ie&&l.getName()=="br"&&!k){k=true;}else{return false;}}}}return true;};}function e(i){return i.type!=CKEDITOR.NODE_TEXT&&i.getName() in CKEDITOR.dtd.$removeEmpty||!CKEDITOR.tools.trim(i.getText())||!!i.getParent().data("cke-bookmark");}var f=new CKEDITOR.dom.walker.whitespaces(),a=new CKEDITOR.dom.walker.bookmark();function b(i){return !f(i)&&!a(i);}CKEDITOR.dom.range.prototype={clone:function(){var i=new CKEDITOR.dom.range(this.document);i.startContainer=this.startContainer;i.startOffset=this.startOffset;i.endContainer=this.endContainer;i.endOffset=this.endOffset;i.collapsed=this.collapsed;return i;},collapse:function(i){if(i){this.endContainer=this.startContainer;this.endOffset=this.startOffset;}else{this.startContainer=this.endContainer;this.startOffset=this.endOffset;}this.collapsed=true;},cloneContents:function(){var i=new CKEDITOR.dom.documentFragment(this.document);if(!this.collapsed){g(this,2,i);}return i;},deleteContents:function(i){if(this.collapsed){return;}g(this,0,null,i);},extractContents:function(j){var i=new CKEDITOR.dom.documentFragment(this.document);if(!this.collapsed){g(this,1,i,j);}return i;},createBookmark:function(l){var k,i;var j;var n;var m=this.collapsed;k=this.document.createElement("span");k.data("cke-bookmark",1);k.setStyle("display","none");k.setHtml("&nbsp;");if(l){j="cke_bm_"+CKEDITOR.tools.getNextNumber();k.setAttribute("id",j+"S");}if(!m){i=k.clone();i.setHtml("&nbsp;");if(l){i.setAttribute("id",j+"E");}n=this.clone();n.collapse();n.insertNode(i);}n=this.clone();n.collapse(true);n.insertNode(k);if(i){this.setStartAfter(k);this.setEndBefore(i);}else{this.moveToPosition(k,CKEDITOR.POSITION_AFTER_END);}return{startNode:l?j+"S":k,endNode:l?j+"E":i,serializable:l,collapsed:m};},createBookmark2:function(o){var l=this.startContainer,m=this.endContainer;var i=this.startOffset,j=this.endOffset;var n=this.collapsed;var p,k;if(!l||!m){return{start:0,end:0};}if(o){if(l.type==CKEDITOR.NODE_ELEMENT){p=l.getChild(i);if(p&&p.type==CKEDITOR.NODE_TEXT&&i>0&&p.getPrevious().type==CKEDITOR.NODE_TEXT){l=p;i=0;}if(p&&p.type==CKEDITOR.NODE_ELEMENT){i=p.getIndex(1);}}while(l.type==CKEDITOR.NODE_TEXT&&(k=l.getPrevious())&&k.type==CKEDITOR.NODE_TEXT){l=k;i+=k.getLength();}if(!n){if(m.type==CKEDITOR.NODE_ELEMENT){p=m.getChild(j);if(p&&p.type==CKEDITOR.NODE_TEXT&&j>0&&p.getPrevious().type==CKEDITOR.NODE_TEXT){m=p;j=0;}if(p&&p.type==CKEDITOR.NODE_ELEMENT){j=p.getIndex(1);}}while(m.type==CKEDITOR.NODE_TEXT&&(k=m.getPrevious())&&k.type==CKEDITOR.NODE_TEXT){m=k;j+=k.getLength();}}}return{start:l.getAddress(o),end:n?null:m.getAddress(o),startOffset:i,endOffset:j,normalized:o,collapsed:n,is2:true};},moveToBookmark:function(n){if(n.is2){var o=this.document.getByAddress(n.start,n.normalized),j=n.startOffset;var p=n.end&&this.document.getByAddress(n.end,n.normalized),l=n.endOffset;this.setStart(o,j);if(p){this.setEnd(p,l);}else{this.collapse(true);}}else{var m=n.serializable,k=m?this.document.getById(n.startNode):n.startNode,i=m?this.document.getById(n.endNode):n.endNode;this.setStartBefore(k);k.remove();if(i){this.setEndBefore(i);i.remove();}else{this.collapse(true);}}},getBoundaryNodes:function(){var m=this.startContainer,j=this.endContainer,i=this.startOffset,l=this.endOffset,k;if(m.type==CKEDITOR.NODE_ELEMENT){k=m.getChildCount();if(k>i){m=m.getChild(i);}else{if(k<1){m=m.getPreviousSourceNode();}else{m=m.$;while(m.lastChild){m=m.lastChild;}m=new CKEDITOR.dom.node(m);m=m.getNextSourceNode()||m;}}}if(j.type==CKEDITOR.NODE_ELEMENT){k=j.getChildCount();if(k>l){j=j.getChild(l).getPreviousSourceNode(true);}else{if(k<1){j=j.getPreviousSourceNode();}else{j=j.$;while(j.lastChild){j=j.lastChild;}j=new CKEDITOR.dom.node(j);}}}if(m.getPosition(j)&CKEDITOR.POSITION_FOLLOWING){m=j;}return{startNode:m,endNode:j};},getCommonAncestor:function(j,l){var m=this.startContainer,i=this.endContainer,k;if(m.equals(i)){if(j&&m.type==CKEDITOR.NODE_ELEMENT&&this.startOffset==this.endOffset-1){k=m.getChild(this.startOffset);}else{k=m;}}else{k=m.getCommonAncestor(i);}return l&&!k.is?k.getParent():k;},optimize:function(){var i=this.startContainer;var j=this.startOffset;if(i.type!=CKEDITOR.NODE_ELEMENT){if(!j){this.setStartBefore(i);}else{if(j>=i.getLength()){this.setStartAfter(i);}}}i=this.endContainer;j=this.endOffset;if(i.type!=CKEDITOR.NODE_ELEMENT){if(!j){this.setEndBefore(i);}else{if(j>=i.getLength()){this.setEndAfter(i);}}}},optimizeBookmark:function(){var j=this.startContainer,i=this.endContainer;if(j.is&&j.is("span")&&j.data("cke-bookmark")){this.setStartAt(j,CKEDITOR.POSITION_BEFORE_START);}if(i&&i.is&&i.is("span")&&i.data("cke-bookmark")){this.setEndAt(i,CKEDITOR.POSITION_AFTER_END);}},trim:function(l,o){var m=this.startContainer,i=this.startOffset,p=this.collapsed;if((!l||p)&&m&&m.type==CKEDITOR.NODE_TEXT){if(!i){i=m.getIndex();m=m.getParent();}else{if(i>=m.getLength()){i=m.getIndex()+1;m=m.getParent();}else{var k=m.split(i);i=m.getIndex()+1;m=m.getParent();if(this.startContainer.equals(this.endContainer)){this.setEnd(k,this.endOffset-this.startOffset);}else{if(m.equals(this.endContainer)){this.endOffset+=1;}}}}this.setStart(m,i);if(p){this.collapse(true);return;}}var n=this.endContainer;var j=this.endOffset;if(!(o||p)&&n&&n.type==CKEDITOR.NODE_TEXT){if(!j){j=n.getIndex();n=n.getParent();}else{if(j>=n.getLength()){j=n.getIndex()+1;n=n.getParent();}else{n.split(j);j=n.getIndex()+1;n=n.getParent();}}this.setEnd(n,j);}},enlarge:function(x,u){switch(x){case CKEDITOR.ENLARGE_ELEMENT:if(this.collapsed){return;}var l=this.getCommonAncestor();var t=this.document.getBody();var n,K;var E,I,r;var m=false;var y;var j;var v=this.startContainer;var o=this.startOffset;if(v.type==CKEDITOR.NODE_TEXT){if(o){v=!CKEDITOR.tools.trim(v.substring(0,o)).length&&v;m=!!v;}if(v){if(!(I=v.getPrevious())){E=v.getParent();}}}else{if(o){I=v.getChild(o-1)||v.getLast();}if(!I){E=v;}}while(E||I){if(E&&!I){if(!r&&E.equals(l)){r=true;}if(!t.contains(E)){break;}if(!m||E.getComputedStyle("display")!="inline"){m=false;if(r){n=E;}else{this.setStartBefore(E);}}I=E.getPrevious();}while(I){y=false;if(I.type==CKEDITOR.NODE_TEXT){j=I.getText();if(/[^\s\ufeff]/.test(j)){I=null;}y=/[\s\ufeff]$/.test(j);}else{if((I.$.offsetWidth>0||u&&I.is("br"))&&!I.data("cke-bookmark")){if(m&&CKEDITOR.dtd.$removeEmpty[I.getName()]){j=I.getText();if((/[^\s\ufeff]/).test(j)){I=null;}else{var C=I.$.all||I.$.getElementsByTagName("*");for(var H=0,p;p=C[H++];){if(!CKEDITOR.dtd.$removeEmpty[p.nodeName.toLowerCase()]){I=null;break;}}}if(I){y=!!j.length;}}else{I=null;}}}if(y){if(m){if(r){n=E;}else{if(E){this.setStartBefore(E);}}}else{m=true;}}if(I){var D=I.getPrevious();if(!E&&!D){E=I;I=null;break;}I=D;}else{E=null;}}if(E){E=E.getParent();}}v=this.endContainer;o=this.endOffset;E=I=null;r=m=false;if(v.type==CKEDITOR.NODE_TEXT){v=!CKEDITOR.tools.trim(v.substring(o)).length&&v;m=!(v&&v.getLength());if(v){if(!(I=v.getNext())){E=v.getParent();}}}else{I=v.getChild(o);if(!I){E=v;}}while(E||I){if(E&&!I){if(!r&&E.equals(l)){r=true;}if(!t.contains(E)){break;}if(!m||E.getComputedStyle("display")!="inline"){m=false;if(r){K=E;}else{if(E){this.setEndAfter(E);}}}I=E.getNext();}while(I){y=false;if(I.type==CKEDITOR.NODE_TEXT){j=I.getText();if(/[^\s\ufeff]/.test(j)){I=null;}y=/^[\s\ufeff]/.test(j);}else{if((I.$.offsetWidth>0||u&&I.is("br"))&&!I.data("cke-bookmark")){if(m&&CKEDITOR.dtd.$removeEmpty[I.getName()]){j=I.getText();if((/[^\s\ufeff]/).test(j)){I=null;}else{C=I.$.all||I.$.getElementsByTagName("*");for(H=0;p=C[H++];){if(!CKEDITOR.dtd.$removeEmpty[p.nodeName.toLowerCase()]){I=null;break;}}}if(I){y=!!j.length;}}else{I=null;}}}if(y){if(m){if(r){K=E;}else{this.setEndAfter(E);}}}if(I){D=I.getNext();if(!E&&!D){E=I;I=null;break;}I=D;}else{E=null;}}if(E){E=E.getParent();}}if(n&&K){l=n.contains(K)?K:n;this.setStartBefore(l);this.setEndAfter(l);}break;case CKEDITOR.ENLARGE_BLOCK_CONTENTS:case CKEDITOR.ENLARGE_LIST_ITEM_CONTENTS:var B=new CKEDITOR.dom.range(this.document);t=this.document.getBody();B.setStartAt(t,CKEDITOR.POSITION_AFTER_START);B.setEnd(this.startContainer,this.startOffset);var G=new CKEDITOR.dom.walker(B),L,w,F=CKEDITOR.dom.walker.blockBoundary((x==CKEDITOR.ENLARGE_LIST_ITEM_CONTENTS)?{br:1}:null),s=function(M){var i=F(M);if(!i){L=M;}return i;},A=function(M){var i=s(M);if(!i&&M.is&&M.is("br")){w=M;}return i;};G.guard=s;E=G.lastBackward();L=L||t;this.setStartAt(L,!L.is("br")&&(!E&&this.checkStartOfBlock()||E&&L.contains(E))?CKEDITOR.POSITION_AFTER_START:CKEDITOR.POSITION_AFTER_END);if(x==CKEDITOR.ENLARGE_LIST_ITEM_CONTENTS){var J=this.clone();G=new CKEDITOR.dom.walker(J);var z=CKEDITOR.dom.walker.whitespaces(),q=CKEDITOR.dom.walker.bookmark();G.evaluator=function(i){return !z(i)&&!q(i);};var k=G.previous();if(k&&k.type==CKEDITOR.NODE_ELEMENT&&k.is("br")){return;}}B=this.clone();B.collapse();B.setEndAt(t,CKEDITOR.POSITION_BEFORE_END);G=new CKEDITOR.dom.walker(B);G.guard=(x==CKEDITOR.ENLARGE_LIST_ITEM_CONTENTS)?A:s;L=null;E=G.lastForward();L=L||t;this.setEndAt(L,(!E&&this.checkEndOfBlock()||E&&L.contains(E))?CKEDITOR.POSITION_BEFORE_END:CKEDITOR.POSITION_BEFORE_START);if(w){this.setEndAfter(w);}}},shrink:function(p,u){if(!this.collapsed){p=p||CKEDITOR.SHRINK_TEXT;var k=this.clone();var l=this.startContainer,w=this.endContainer,s=this.startOffset,t=this.endOffset,n=this.collapsed;var j=1,v=1;if(l&&l.type==CKEDITOR.NODE_TEXT){if(!s){k.setStartBefore(l);}else{if(s>=l.getLength()){k.setStartAfter(l);}else{k.setStartBefore(l);j=0;}}}if(w&&w.type==CKEDITOR.NODE_TEXT){if(!t){k.setEndBefore(w);}else{if(t>=w.getLength()){k.setEndAfter(w);}else{k.setEndAfter(w);v=0;}}}var i=new CKEDITOR.dom.walker(k),m=CKEDITOR.dom.walker.bookmark();i.evaluator=function(x){return x.type==(p==CKEDITOR.SHRINK_ELEMENT?CKEDITOR.NODE_ELEMENT:CKEDITOR.NODE_TEXT);};var o;i.guard=function(y,x){if(m(y)){return true;}if(p==CKEDITOR.SHRINK_ELEMENT&&y.type==CKEDITOR.NODE_TEXT){return false;}if(x&&y.equals(o)){return false;}if(!x&&y.type==CKEDITOR.NODE_ELEMENT){o=y;}return true;};if(j){var r=i[p==CKEDITOR.SHRINK_ELEMENT?"lastForward":"next"]();r&&this.setStartAt(r,u?CKEDITOR.POSITION_AFTER_START:CKEDITOR.POSITION_BEFORE_START);}if(v){i.reset();var q=i[p==CKEDITOR.SHRINK_ELEMENT?"lastBackward":"previous"]();q&&this.setEndAt(q,u?CKEDITOR.POSITION_BEFORE_END:CKEDITOR.POSITION_AFTER_END);}return !!(j||v);}},insertNode:function(l){this.optimizeBookmark();this.trim(false,true);var k=this.startContainer;var j=this.startOffset;var i=k.getChild(j);if(i){l.insertBefore(i);}else{k.append(l);}if(l.getParent().equals(this.endContainer)){this.endOffset++;}this.setStartBefore(l);},moveToPosition:function(j,i){this.setStartAt(j,i);this.collapse(true);},selectNodeContents:function(i){this.setStart(i,0);this.setEnd(i,i.type==CKEDITOR.NODE_TEXT?i.getLength():i.getChildCount());},setStart:function(j,i){if(j.type==CKEDITOR.NODE_ELEMENT&&CKEDITOR.dtd.$empty[j.getName()]){i=j.getIndex(),j=j.getParent();}this.startContainer=j;this.startOffset=i;if(!this.endContainer){this.endContainer=j;this.endOffset=i;}h(this);},setEnd:function(i,j){if(i.type==CKEDITOR.NODE_ELEMENT&&CKEDITOR.dtd.$empty[i.getName()]){j=i.getIndex()+1,i=i.getParent();}this.endContainer=i;this.endOffset=j;if(!this.startContainer){this.startContainer=i;this.startOffset=j;}h(this);},setStartAfter:function(i){this.setStart(i.getParent(),i.getIndex()+1);},setStartBefore:function(i){this.setStart(i.getParent(),i.getIndex());},setEndAfter:function(i){this.setEnd(i.getParent(),i.getIndex()+1);},setEndBefore:function(i){this.setEnd(i.getParent(),i.getIndex());},setStartAt:function(j,i){switch(i){case CKEDITOR.POSITION_AFTER_START:this.setStart(j,0);break;case CKEDITOR.POSITION_BEFORE_END:if(j.type==CKEDITOR.NODE_TEXT){this.setStart(j,j.getLength());}else{this.setStart(j,j.getChildCount());}break;case CKEDITOR.POSITION_BEFORE_START:this.setStartBefore(j);break;case CKEDITOR.POSITION_AFTER_END:this.setStartAfter(j);}h(this);},setEndAt:function(j,i){switch(i){case CKEDITOR.POSITION_AFTER_START:this.setEnd(j,0);break;case CKEDITOR.POSITION_BEFORE_END:if(j.type==CKEDITOR.NODE_TEXT){this.setEnd(j,j.getLength());}else{this.setEnd(j,j.getChildCount());}break;case CKEDITOR.POSITION_BEFORE_START:this.setEndBefore(j);break;case CKEDITOR.POSITION_AFTER_END:this.setEndAfter(j);}h(this);},fixBlock:function(i,l){var k=this.createBookmark(),j=this.document.createElement(l);this.collapse(i);this.enlarge(CKEDITOR.ENLARGE_BLOCK_CONTENTS);this.extractContents().appendTo(j);j.trim();if(!CKEDITOR.env.ie){j.appendBogus();}this.insertNode(j);this.moveToBookmark(k);return j;},splitBlock:function(n){var q=new CKEDITOR.dom.elementPath(this.startContainer),k=new CKEDITOR.dom.elementPath(this.endContainer);var p=q.blockLimit,m=k.blockLimit;var o=q.block,l=k.block;var r=null;if(!p.equals(m)){return null;}if(n!="br"){if(!o){o=this.fixBlock(true,n);l=new CKEDITOR.dom.elementPath(this.endContainer).block;}if(!l){l=this.fixBlock(false,n);}}var j=o&&this.checkStartOfBlock(),i=l&&this.checkEndOfBlock();this.deleteContents();if(o&&o.equals(l)){if(i){r=new CKEDITOR.dom.elementPath(this.startContainer);this.moveToPosition(l,CKEDITOR.POSITION_AFTER_END);l=null;}else{if(j){r=new CKEDITOR.dom.elementPath(this.startContainer);this.moveToPosition(o,CKEDITOR.POSITION_BEFORE_START);o=null;}else{l=this.splitElement(o);if(!CKEDITOR.env.ie&&!o.is("ul","ol")){o.appendBogus();}}}}return{previousBlock:o,nextBlock:l,wasStartOfBlock:j,wasEndOfBlock:i,elementPath:r};},splitElement:function(j){if(!this.collapsed){return null;}this.setEndAt(j,CKEDITOR.POSITION_BEFORE_END);var i=this.extractContents();var k=j.clone(false);i.appendTo(k);k.insertAfter(j);this.moveToPosition(j,CKEDITOR.POSITION_AFTER_END);return k;},checkBoundaryOfElement:function(k,i){var l=(i==CKEDITOR.START);var j=this.clone();j.collapse(l);j[l?"setStartAt":"setEndAt"](k,l?CKEDITOR.POSITION_AFTER_START:CKEDITOR.POSITION_BEFORE_END);var m=new CKEDITOR.dom.walker(j);m.evaluator=e;return m[l?"checkBackward":"checkForward"]();},checkStartOfBlock:function(){var l=this.startContainer,i=this.startOffset;if(i&&l.type==CKEDITOR.NODE_TEXT){var k=CKEDITOR.tools.ltrim(l.substring(0,i));if(k.length){return false;}}this.trim();var m=new CKEDITOR.dom.elementPath(this.startContainer);var j=this.clone();j.collapse(true);j.setStartAt(m.block||m.blockLimit,CKEDITOR.POSITION_AFTER_START);var n=new CKEDITOR.dom.walker(j);n.evaluator=c(true);return n.checkBackward();},checkEndOfBlock:function(){var l=this.endContainer,k=this.endOffset;if(l.type==CKEDITOR.NODE_TEXT){var i=CKEDITOR.tools.rtrim(l.substring(k));if(i.length){return false;}}this.trim();var m=new CKEDITOR.dom.elementPath(this.endContainer);var j=this.clone();j.collapse(false);j.setEndAt(m.block||m.blockLimit,CKEDITOR.POSITION_BEFORE_END);var n=new CKEDITOR.dom.walker(j);n.evaluator=c(false);return n.checkForward();},checkReadOnly:(function(){function i(j,k){while(j){if(j.type==CKEDITOR.NODE_ELEMENT){if(j.getAttribute("contentEditable")=="false"&&!j.data("cke-editable")){return 0;}else{if(j.is("html")||j.getAttribute("contentEditable")=="true"&&(j.contains(k)||j.equals(k))){break;}}}j=j.getParent();}return 1;}return function(){var k=this.startContainer,j=this.endContainer;return !(i(k,j)&&i(j,k));};})(),moveToElementEditablePosition:function(k,j){function i(o,m){var n;if(o.type==CKEDITOR.NODE_ELEMENT&&o.isEditable(false)&&!CKEDITOR.dtd.$nonEditable[o.getName()]){n=o[j?"getLast":"getFirst"](b);}if(!m&&!n){n=o[j?"getPrevious":"getNext"](b);}return n;}var l=0;while(k){if(k.type==CKEDITOR.NODE_TEXT){this.moveToPosition(k,j?CKEDITOR.POSITION_AFTER_END:CKEDITOR.POSITION_BEFORE_START);l=1;break;}if(k.type==CKEDITOR.NODE_ELEMENT){if(k.isEditable()){this.moveToPosition(k,j?CKEDITOR.POSITION_BEFORE_END:CKEDITOR.POSITION_AFTER_START);l=1;}}k=i(k,l);}return !!l;},moveToElementEditStart:function(i){return this.moveToElementEditablePosition(i);},moveToElementEditEnd:function(i){return this.moveToElementEditablePosition(i,true);},getEnclosedNode:function(){var i=this.clone();i.optimize();if(i.startContainer.type!=CKEDITOR.NODE_ELEMENT||i.endContainer.type!=CKEDITOR.NODE_ELEMENT){return null;}var l=new CKEDITOR.dom.walker(i),k=CKEDITOR.dom.walker.bookmark(true),n=CKEDITOR.dom.walker.whitespaces(true),m=function(o){return n(o)&&k(o);};i.evaluator=m;var j=l.next();l.reset();return j&&j.equals(l.previous())?j:null;},getTouchedStartNode:function(){var i=this.startContainer;if(this.collapsed||i.type!=CKEDITOR.NODE_ELEMENT){return i;}return i.getChild(this.startOffset)||i;},getTouchedEndNode:function(){var i=this.endContainer;if(this.collapsed||i.type!=CKEDITOR.NODE_ELEMENT){return i;}return i.getChild(this.endOffset-1)||i;}};})();CKEDITOR.POSITION_AFTER_START=1;CKEDITOR.POSITION_BEFORE_END=2;CKEDITOR.POSITION_BEFORE_START=3;CKEDITOR.POSITION_AFTER_END=4;CKEDITOR.ENLARGE_ELEMENT=1;CKEDITOR.ENLARGE_BLOCK_CONTENTS=2;CKEDITOR.ENLARGE_LIST_ITEM_CONTENTS=3;CKEDITOR.START=1;CKEDITOR.END=2;CKEDITOR.STARTEND=3;CKEDITOR.SHRINK_ELEMENT=1;CKEDITOR.SHRINK_TEXT=2;