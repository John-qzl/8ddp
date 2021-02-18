var CodeMirrorConfig=window.CodeMirrorConfig||{};var CodeMirror=(function(){function g(h,j){for(var i in j){if(!h.hasOwnProperty(i)){h[i]=j[i];}}}function e(k,j){for(var h=0;h<k.length;h++){j(k[h]);}}function b(h){if(document.createElementNS&&document.documentElement.namespaceURI!==null){return document.createElementNS("http://www.w3.org/1999/xhtml",h);}else{return document.createElement(h);}}g(CodeMirrorConfig,{stylesheet:[],path:"",parserfile:[],basefiles:["util.js","stringstream.js","select.js","undo.js","editor.js","tokenize.js"],iframeClass:null,passDelay:200,passTime:50,lineNumberDelay:200,lineNumberTime:50,continuousScanning:false,saveFunction:null,onLoad:null,onChange:null,undoDepth:50,undoDelay:800,disableSpellcheck:true,textWrapping:true,readOnly:false,width:"",height:"300px",minHeight:100,autoMatchParens:false,markParen:null,unmarkParen:null,parserConfig:null,tabMode:"indent",enterMode:"indent",electricChars:true,reindentOnLoad:false,activeTokens:null,onCursorActivity:null,lineNumbers:false,firstLineNumber:1,onLineNumberClick:null,indentUnit:2,domain:null,noScriptCaching:false,incrementalLoading:false});function c(i,j){var l=b("div"),h=b("div");l.style.position="absolute";l.style.height="100%";if(l.style.setExpression){try{l.style.setExpression("height","this.previousSibling.offsetHeight + 'px'");}catch(k){}}l.style.top="0px";l.style.left="0px";l.style.overflow="hidden";i.appendChild(l);h.className="CodeMirror-line-numbers";l.appendChild(h);h.innerHTML="<div>"+j+"</div>";return l;}function f(h){if(typeof h.parserfile=="string"){h.parserfile=[h.parserfile];}if(typeof h.basefiles=="string"){h.basefiles=[h.basefiles];}if(typeof h.stylesheet=="string"){h.stylesheet=[h.stylesheet];}var i=['<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head>'];i.push('<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>');var j=h.noScriptCaching?"?nocache="+new Date().getTime().toString(16):"";e(h.stylesheet,function(k){i.push('<link rel="stylesheet" type="text/css" href="'+k+j+'"/>');});e(h.basefiles.concat(h.parserfile),function(k){if(!/^https?:/.test(k)){k=h.path+k;}i.push('<script type="text/javascript" src="'+k+j+'"><'+"/script>");});i.push('</head><body style="border-width: 0;" class="editbox" spellcheck="'+(h.disableSpellcheck?"false":"true")+'"></body></html>');return i.join("");}var d=document.selection&&window.ActiveXObject&&/MSIE/.test(navigator.userAgent);function a(i,j){this.options=j=j||{};g(j,CodeMirrorConfig);if(j.dumbTabs){j.tabMode="spaces";}else{if(j.normalTab){j.tabMode="default";}}if(j.cursorActivity){j.onCursorActivity=j.cursorActivity;}var k=this.frame=b("iframe");if(j.iframeClass){k.className=j.iframeClass;}k.frameBorder=0;k.style.border="0";k.style.width="100%";k.style.height="100%";k.style.display="block";var l=this.wrapping=b("div");l.style.position="relative";l.className="CodeMirror-wrapping";l.style.width=j.width;l.style.height=(j.height=="dynamic")?j.minHeight+"px":j.height;var h=this.textareaHack=b("textarea");l.appendChild(h);h.style.position="absolute";h.style.left="-10000px";h.style.width="10px";h.tabIndex=100000;k.CodeMirror=this;if(j.domain&&d){this.html=f(j);k.src="javascript:(function(){document.open();"+(j.domain?'document.domain="'+j.domain+'";':"")+"document.write(window.frameElement.CodeMirror.html);document.close();})()";}else{k.src="javascript:;";}if(i.appendChild){i.appendChild(l);}else{i(l);}l.appendChild(k);if(j.lineNumbers){this.lineNumbers=c(l,j.firstLineNumber);}this.win=k.contentWindow;if(!j.domain||!d){this.win.document.open();this.win.document.write(f(j));this.win.document.close();}}a.prototype={init:function(){if(this.options.initCallback){this.options.initCallback(this);}if(this.options.onLoad){this.options.onLoad(this);}if(this.options.lineNumbers){this.activateLineNumbers();}if(this.options.reindentOnLoad){this.reindent();}if(this.options.height=="dynamic"){this.setDynamicHeight();}},getCode:function(){return this.editor.getCode();},setCode:function(h){this.editor.importCode(h);},selection:function(){this.focusIfIE();return this.editor.selectedText();},reindent:function(){this.editor.reindent();},reindentSelection:function(){this.focusIfIE();this.editor.reindentSelection(null);},focusIfIE:function(){if(this.win.select.ie_selection&&document.activeElement!=this.frame){this.focus();}},focus:function(){this.win.focus();if(this.editor.selectionSnapshot){this.win.select.setBookmark(this.win.document.body,this.editor.selectionSnapshot);}},replaceSelection:function(h){this.focus();this.editor.replaceSelection(h);return true;},replaceChars:function(i,j,h){this.editor.replaceChars(i,j,h);},getSearchCursor:function(i,h,j){return this.editor.getSearchCursor(i,h,j);},undo:function(){this.editor.history.undo();},redo:function(){this.editor.history.redo();},historySize:function(){return this.editor.history.historySize();},clearHistory:function(){this.editor.history.clear();},grabKeys:function(i,h){this.editor.grabKeys(i,h);},ungrabKeys:function(){this.editor.ungrabKeys();},setParser:function(h,i){this.editor.setParser(h,i);},setSpellcheck:function(h){this.win.document.body.spellcheck=h;},setStylesheet:function(n){if(typeof n==="string"){n=[n];}var j={};var l={};var i=this.win.document.getElementsByTagName("link");for(var h=0,m;m=i[h];h++){if(m.rel.indexOf("stylesheet")!==-1){for(var o=0;o<n.length;o++){var k=n[o];if(m.href.substring(m.href.length-k.length)===k){j[m.href]=true;l[k]=true;}}}}for(var h=0,m;m=i[h];h++){if(m.rel.indexOf("stylesheet")!==-1){m.disabled=!(m.href in j);}}for(var o=0;o<n.length;o++){var k=n[o];if(!(k in l)){var m=this.win.document.createElement("link");m.rel="stylesheet";m.type="text/css";m.href=k;this.win.document.getElementsByTagName("head")[0].appendChild(m);}}},setTextWrapping:function(h){if(h==this.options.textWrapping){return;}this.win.document.body.style.whiteSpace=h?"":"nowrap";this.options.textWrapping=h;if(this.lineNumbers){this.setLineNumbers(false);this.setLineNumbers(true);}},setIndentUnit:function(h){this.win.indentUnit=h;},setUndoDepth:function(h){this.editor.history.maxDepth=h;},setTabMode:function(h){this.options.tabMode=h;},setEnterMode:function(h){this.options.enterMode=h;},setLineNumbers:function(h){if(h&&!this.lineNumbers){this.lineNumbers=c(this.wrapping,this.options.firstLineNumber);this.activateLineNumbers();}else{if(!h&&this.lineNumbers){this.wrapping.removeChild(this.lineNumbers);this.wrapping.style.paddingLeft="";this.lineNumbers=null;}}},cursorPosition:function(h){this.focusIfIE();return this.editor.cursorPosition(h);},firstLine:function(){return this.editor.firstLine();},lastLine:function(){return this.editor.lastLine();},nextLine:function(h){return this.editor.nextLine(h);},prevLine:function(h){return this.editor.prevLine(h);},lineContent:function(h){return this.editor.lineContent(h);},setLineContent:function(h,i){this.editor.setLineContent(h,i);},removeLine:function(h){this.editor.removeLine(h);},insertIntoLine:function(i,h,j){this.editor.insertIntoLine(i,h,j);},selectLines:function(k,h,j,i){this.win.focus();this.editor.selectLines(k,h,j,i);},nthLine:function(i){var h=this.firstLine();for(;i>1&&h!==false;i--){h=this.nextLine(h);}return h;},lineNumber:function(h){var i=0;while(h!==false){i++;h=this.prevLine(h);}return i;},jumpToLine:function(h){if(typeof h=="number"){h=this.nthLine(h);}this.selectLines(h,0);this.win.focus();},currentLine:function(){return this.lineNumber(this.cursorLine());},cursorLine:function(){return this.cursorPosition().line;},cursorCoords:function(h){return this.editor.cursorCoords(h);},activateLineNumbers:function(){var j=this.frame,q=j.contentWindow,s=q.document,n=s.body,t=this.lineNumbers,o=t.firstChild,u=this;var v=null;t.onclick=function(y){var x=u.options.onLineNumberClick;if(x){var z=(y||window.event).target||(y||window.event).srcElement;var w=z==t?NaN:Number(z.innerHTML);if(!isNaN(w)){x(w,z);}}};function i(){if(j.offsetWidth==0){return;}for(var w=j;w.parentNode;w=w.parentNode){}if(!t.parentNode||w!=document||!q.Editor){try{m();}catch(x){}clearInterval(h);return;}if(t.offsetWidth!=v){v=t.offsetWidth;j.parentNode.style.paddingLeft=v+"px";}}function k(){t.scrollTop=n.scrollTop||s.documentElement.scrollTop||0;}var m=function(){};i();var h=setInterval(i,500);function r(A){var z=o.firstChild.offsetHeight;if(z==0){return;}var w=50+Math.max(n.offsetHeight,Math.max(j.offsetHeight,n.scrollHeight||0)),x=Math.ceil(w/z);for(var y=o.childNodes.length;y<=x;y++){var B=b("div");B.appendChild(document.createTextNode(A?String(y+u.options.firstLineNumber):"\u00a0"));o.appendChild(B);}}function p(){function y(){r(true);k();}u.updateNumbers=y;var w=q.addEventHandler(q,"scroll",k,true),x=q.addEventHandler(q,"resize",y,true);m=function(){w();x();if(u.updateNumbers==y){u.updateNumbers=null;}};y();}function l(){var A,D,C,F,I=[],J=u.options.styleNumbers;function G(L,K){if(!D){D=o.appendChild(b("div"));}if(J){J(D,K,L);}I.push(D);I.push(L);F=D.offsetHeight+D.offsetTop;D=D.nextSibling;}function z(){for(var K=0;K<I.length;K+=2){I[K].innerHTML=I[K+1];}I=[];}function H(){if(!o.parentNode||o.parentNode!=u.lineNumbers){return;}var K=new Date().getTime()+u.options.lineNumberTime;while(A){G(C++,A.previousSibling);for(;A&&!q.isBR(A);A=A.nextSibling){var M=A.offsetTop+A.offsetHeight;while(o.offsetHeight&&M-3>F){var L=F;G("&nbsp;");if(F<=L){break;}}}if(A){A=A.nextSibling;}if(new Date().getTime()>K){z();x=setTimeout(H,u.options.lineNumberDelay);return;}}while(D){G(C++);}z();k();}function y(K){k();r(K);A=n.firstChild;D=o.firstChild;F=0;C=u.options.firstLineNumber;H();}y(true);var x=null;function B(){if(x){clearTimeout(x);}if(u.editor.allClean()){y();}else{x=setTimeout(B,200);}}u.updateNumbers=B;var E=q.addEventHandler(q,"scroll",k,true),w=q.addEventHandler(q,"resize",B,true);m=function(){if(x){clearTimeout(x);}if(u.updateNumbers==B){u.updateNumbers=null;}E();w();};}(this.options.textWrapping||this.options.styleNumbers?l:p)();},setDynamicHeight:function(){var j=this,n=j.options.onCursorActivity,m=j.win,i=m.document.body,k=null,l=null,h=2*j.frame.offsetTop;i.style.overflowY="hidden";m.document.documentElement.style.overflowY="hidden";this.frame.scrolling="no";function o(){var p=0,q=i.lastChild,r;while(q&&m.isBR(q)){if(!q.hackBR){p++;}q=q.previousSibling;}if(q){k=q.offsetHeight;r=q.offsetTop+(1+p)*k;}else{if(k){r=p*k;}}if(r){j.wrapping.style.height=Math.max(h+r,j.options.minHeight)+"px";}}setTimeout(o,300);j.options.onCursorActivity=function(p){if(n){n(p);}clearTimeout(l);l=setTimeout(o,100);};}};a.InvalidLineHandle={toString:function(){return"CodeMirror.InvalidLineHandle";}};a.replace=function(h){if(typeof h=="string"){h=document.getElementById(h);}return function(i){h.parentNode.replaceChild(i,h);};};a.fromTextArea=function(k,j){if(typeof k=="string"){k=document.getElementById(k);}j=j||{};if(k.style.width&&j.width==null){j.width=k.style.width;}if(k.style.height&&j.height==null){j.height=k.style.height;}if(j.content==null){j.content=k.value;}function i(){k.value=m.getCode();}if(k.form){if(typeof k.form.addEventListener=="function"){k.form.addEventListener("submit",i,false);}else{k.form.attachEvent("onsubmit",i);}var h=k.form.submit;function n(){i();k.form.submit=h;k.form.submit();k.form.submit=n;}k.form.submit=n;}function l(o){if(k.nextSibling){k.parentNode.insertBefore(o,k.nextSibling);}else{k.parentNode.appendChild(o);}}k.style.display="none";var m=new a(l,j);m.save=i;m.toTextArea=function(){i();k.parentNode.removeChild(m.wrapping);k.style.display="";if(k.form){k.form.submit=h;if(typeof k.form.removeEventListener=="function"){k.form.removeEventListener("submit",i,false);}else{k.form.detachEvent("onsubmit",i);}}};return m;};a.isProbablySupported=function(){var h;if(window.opera){return Number(window.opera.version())>=9.52;}else{if(/Apple Computer, Inc/.test(navigator.vendor)&&(h=navigator.userAgent.match(/Version\/(\d+(?:\.\d+)?)\./))){return Number(h[1])>=3;}else{if(document.selection&&window.ActiveXObject&&(h=navigator.userAgent.match(/MSIE (\d+(?:\.\d*)?)\b/))){return Number(h[1])>=6;}else{if(h=navigator.userAgent.match(/gecko\/(\d{8})/i)){return Number(h[1])>=20050901;}else{if(h=navigator.userAgent.match(/AppleWebKit\/(\d+)/)){return Number(h[1])>=525;}else{return null;}}}}}};return a;})();