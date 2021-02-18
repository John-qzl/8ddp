function UndoHistory(b,e,a,d){this.container=b;this.maxDepth=e;this.commitDelay=a;this.editor=d;var c={text:"",from:null,to:null};this.first=c;this.last=c;this.firstTouched=false;this.history=[];this.redoHistory=[];this.touched=[];this.lostundo=0;}UndoHistory.prototype={scheduleCommit:function(){var a=this;parent.clearTimeout(this.commitTimeout);this.commitTimeout=parent.setTimeout(function(){a.tryCommit();},this.commitDelay);},touch:function(a){this.setTouched(a);this.scheduleCommit();},undo:function(){this.commit();if(this.history.length){var a=this.history.pop();this.redoHistory.push(this.updateTo(a,"applyChain"));this.notifyEnvironment();return this.chainNode(a);}},redo:function(){this.commit();if(this.redoHistory.length){var a=this.redoHistory.pop();this.addUndoLevel(this.updateTo(a,"applyChain"));this.notifyEnvironment();return this.chainNode(a);}},clear:function(){this.history=[];this.redoHistory=[];this.lostundo=0;},historySize:function(){return{undo:this.history.length,redo:this.redoHistory.length,lostundo:this.lostundo};},push:function(f,e,b){var d=[];for(var c=0;c<b.length;c++){var a=(c==b.length-1)?e:document.createElement("br");d.push({from:f,to:a,text:cleanText(b[c])});f=a;}this.pushChains([d],f==null&&e==null);this.notifyEnvironment();},pushChains:function(b,a){this.commit(a);this.addUndoLevel(this.updateTo(b,"applyChain"));this.redoHistory=[];},chainNode:function(c){for(var a=0;a<c.length;a++){var d=c[a][0],b=d&&(d.from||d.to);if(b){return b;}}},reset:function(){this.history=[];this.redoHistory=[];this.lostundo=0;},textAfter:function(a){return this.after(a).text;},nodeAfter:function(a){return this.after(a).to;},nodeBefore:function(a){return this.before(a).from;},tryCommit:function(){if(!window||!window.parent||!window.UndoHistory){return;}if(this.editor.highlightDirty()){this.commit(true);}else{this.scheduleCommit();}},commit:function(b){parent.clearTimeout(this.commitTimeout);if(!b){this.editor.highlightDirty(true);}var c=this.touchedChains(),a=this;if(c.length){this.addUndoLevel(this.updateTo(c,"linkChain"));this.redoHistory=[];this.notifyEnvironment();}},updateTo:function(e,a){var d=[],c=[];for(var b=0;b<e.length;b++){d.push(this.shadowChain(e[b]));c.push(this[a](e[b]));}if(a=="applyChain"){this.notifyDirty(c);}return d;},notifyDirty:function(a){forEach(a,method(this.editor,"addDirtyNode"));this.editor.scheduleHighlight();},notifyEnvironment:function(){if(this.onChange){this.onChange(this.editor);}if(window.frameElement&&window.frameElement.CodeMirror.updateNumbers){window.frameElement.CodeMirror.updateNumbers();}},linkChain:function(c){for(var b=0;b<c.length;b++){var a=c[b];if(a.from){a.from.historyAfter=a;}else{this.first=a;}if(a.to){a.to.historyBefore=a;}else{this.last=a;}}},after:function(a){return a?a.historyAfter:this.first;},before:function(a){return a?a.historyBefore:this.last;},setTouched:function(a){if(a){if(!a.historyTouched){this.touched.push(a);a.historyTouched=true;}}else{this.firstTouched=true;}},addUndoLevel:function(a){this.history.push(a);if(this.history.length>this.maxDepth){this.history.shift();lostundo+=1;}},touchedChains:function(){var c=this;var d=null;function b(i){return i?i.historyTemp:d;}function f(j,i){if(j){j.historyTemp=i;}else{d=i;}}function e(i){var k=[];for(var j=i?i.nextSibling:c.container.firstChild;j&&(!isBR(j)||j.hackBR);j=j.nextSibling){if(!j.hackBR&&j.currentText){k.push(j.currentText);}}return{from:i,to:j,text:cleanText(k.join(""))};}var a=[];if(c.firstTouched){c.touched.push(null);}forEach(c.touched,function(j){if(j&&(j.parentNode!=c.container||j.hackBR)){return;}if(j){j.historyTouched=false;}else{c.firstTouched=false;}var i=e(j),k=c.after(j);if(!k||k.text!=i.text||k.to!=i.to){a.push(i);f(j,i);}});function h(l,i){var k=i+"Sibling",j=l[k];while(j&&!isBR(j)){j=j[k];}return j;}var g=[];c.touched=[];forEach(a,function(j){if(!b(j.from)){return;}var k=[],l=j.from,m=true;while(true){var i=b(l);if(!i){if(m){break;}else{i=e(l);}}k.unshift(i);f(l,null);if(!l){break;}m=c.after(l);l=h(l,"previous");}l=j.to;m=c.before(j.from);while(true){if(!l){break;}var i=b(l);if(!i){if(m){break;}else{i=e(l);}}k.push(i);f(l,null);m=c.before(l);l=h(l,"next");}g.push(k);});return g;},shadowChain:function(c){var e=[],d=this.after(c[0].from),b=c[c.length-1].to;while(true){e.push(d);var a=d.to;if(!a||a==b){break;}else{d=a.historyAfter||this.before(b);}}return e;},applyChain:function(a){var k=select.cursorPos(this.container,false),l=this;function b(p,o){var n=p?p.nextSibling:l.container.firstChild;while(n!=o){var i=n.nextSibling;removeElement(n);n=i;}}var c=a[0].from,g=a[a.length-1].to;b(c,g);for(var h=0;h<a.length;h++){var m=a[h];if(h>0){l.container.insertBefore(m.from,g);}var d=makePartSpan(fixSpaces(m.text));l.container.insertBefore(d,g);if(k&&k.node==m.from){var f=0;var e=this.after(m.from);if(e&&h==a.length-1){for(var j=0;j<k.offset&&m.text.charAt(j)==e.text.charAt(j);j++){}if(k.offset>j){f=m.text.length-e.text.length;}}select.setCursorPos(this.container,{node:m.from,offset:Math.max(0,k.offset+f)});}else{if(k&&(h==a.length-1)&&k.node&&k.node.parentNode!=this.container){select.setCursorPos(this.container,{node:m.from,offset:m.text.length});}}}this.linkChain(a);return c;}};