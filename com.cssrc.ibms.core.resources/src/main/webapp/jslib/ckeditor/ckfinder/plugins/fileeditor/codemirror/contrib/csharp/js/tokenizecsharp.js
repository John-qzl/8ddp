var tokenizeCSharp=(function(){function a(j,h){var k=false;var i;while(!j.endOfLine()){var i=j.next();if(i==h&&!k){return false;}k=!k&&i=="\\";}return k;}var b=function(){function h(p,o){return{type:p,style:"csharp-"+o};}var n=h("keyword a","keyword");var m=h("keyword b","keyword");var l=h("keyword c","keyword");var i=h("operator","keyword");var j=h("atom","atom");var k=h("keyword d","keyword");return{"if":n,"while":n,"with":n,"else":m,"do":m,"try":m,"finally":m,"return":l,"break":l,"continue":l,"new":l,"delete":l,"throw":l,"in":i,"typeof":i,"instanceof":i,"var":h("var","keyword"),"function":h("function","keyword"),"catch":h("catch","keyword"),"for":h("for","keyword"),"switch":h("switch","keyword"),"case":h("case","keyword"),"default":h("default","keyword"),"true":j,"false":j,"null":j,"class":h("class","keyword"),"namespace":h("class","keyword"),"public":k,"private":k,"protected":k,"internal":k,"extern":k,"override":k,"virtual":k,"abstract":k,"static":k,"out":k,"ref":k,"const":k,"foreach":h("for","keyword"),"using":l,"int":k,"double":k,"long":k,"bool":k,"char":k,"void":k,"string":k,"byte":k,"sbyte":k,"decimal":k,"float":k,"uint":k,"ulong":k,"object":k,"short":k,"ushort":k,"get":k,"set":k,"value":k};}();var d=/[+\-*&%=<>!?|]/;var g=/[0-9A-Fa-f]/;var e=/[\w\$_]/;function c(h,i){return function(l,n){var j=h;var k=f(h,i,l,function(o){j=o;});var m=k.type=="operator"||k.type=="keyword c"||k.type.match(/^[\[{}\(,;:]$/);if(m!=i||j!=h){n(c(j,m));}return k;};}function f(m,n,i,j){function l(){i.next();i.nextWhileMatches(g);return{type:"number",style:"csharp-atom"};}function q(){i.nextWhileMatches(/[0-9]/);if(i.equals(".")){i.next();i.nextWhileMatches(/[0-9]/);}if(i.equals("e")||i.equals("E")){i.next();if(i.equals("-")){i.next();}i.nextWhileMatches(/[0-9]/);}return{type:"number",style:"csharp-atom"};}function p(){i.nextWhileMatches(e);var u=i.get();var t=b.hasOwnProperty(u)&&b.propertyIsEnumerable(u)&&b[u];return t?{type:t.type,style:t.style,content:u}:{type:"variable",style:"csharp-variable",content:u};}function k(){a(i,"/");i.nextWhileMatches(/[gi]/);return{type:"regexp",style:"csharp-string"};}function r(w){var u="/*";var t=(w=="*");while(true){if(i.endOfLine()){break;}var v=i.next();if(v=="/"&&t){u=null;break;}t=(v=="*");}j(u);return{type:"comment",style:"csharp-comment"};}function o(){i.nextWhileMatches(d);return{type:"operator",style:"csharp-operator"};}function s(t){var u=a(i,t);j(u?t:null);return{type:"string",style:"csharp-string"};}if(m=='"'||m=="'"){return s(m);}var h=i.next();if(m=="/*"){return r(h);}else{if(h=='"'||h=="'"){return s(h);}else{if(/[\[\]{}\(\),;\:\.]/.test(h)){return{type:h,style:"csharp-punctuation"};}else{if(h=="0"&&(i.equals("x")||i.equals("X"))){return l();}else{if(/[0-9]/.test(h)){return q();}else{if(h=="/"){if(i.equals("*")){i.next();return r(h);}else{if(i.equals("/")){a(i,null);return{type:"comment",style:"csharp-comment"};}else{if(n){return k();}else{return o();}}}}else{if(h=="#"){a(i,null);return{type:"comment",style:"csharp-comment"};}else{if(d.test(h)){return o();}else{return p();}}}}}}}}}return function(i,h){return tokenizer(i,h||c(false,true));};})();