if(!Array.prototype.indexOf){Array.prototype.indexOf=function(b){var a=this.length;var c=Number(arguments[1])||0;c=(c<0)?Math.ceil(c):Math.floor(c);if(c<0){c+=a;}for(;c<a;c++){if(c in this&&this[c]===b){return c;}}return -1;};}var PHPParser=Editor.Parser=(function(){var b={"atom":true,"number":true,"variable":true,"string":true};function d(j,f,e,i,g,h){this.indented=j;this.column=f;this.type=e;if(i!=null){this.align=i;}this.prev=g;this.info=h;}function a(e){return function(g){var i=g&&g.charAt(0),h=e.type;var f=i==h;if(h=="form"&&i=="{"){return e.indented;}else{if(h=="stat"||h=="form"){return e.indented+indentUnit;}else{if(e.info=="switch"&&!f){return e.indented+(/^(?:case|default)\b/.test(g)?indentUnit:2*indentUnit);}else{if(e.align){return e.column-(f?1:0);}else{return e.indented+(f?0:indentUnit);}}}}};}function c(g,M){var H=tokenizePHP(g);var J=[l];var A=new d((M||0)-indentUnit,0,"block",false);var O=0;var o=0;var B,j;var G={next:h,copy:T};function h(){while(J[J.length-1].lex){J.pop()();}var U=H.next();if(U.type=="whitespace"&&O==0){o=U.value.length;}O+=U.value.length;if(U.content=="\n"){o=O=0;if(!("align" in A)){A.align=false;}U.indentation=a(A);}if(U.type=="whitespace"||U.type=="comment"||U.type=="string_not_terminated"){return U;}if(!("align" in A)){A.align=true;}while(true){B=j=false;var V=J.pop();V(U);if(B){if(j){U.style=j;}return U;}}return 1;}function T(){var V=A,W=J.concat([]),U=H.state;return function X(Y){A=V;J=W.concat([]);O=o=0;H=tokenizePHP(Y,U);return G;};}function S(U){for(var V=U.length-1;V>=0;V--){J.push(U[V]);}}function L(){S(arguments);B=true;}function e(){S(arguments);B=false;}function p(U){j=U;}function z(U){j=j+" "+U;}function m(W,X){var V=function U(){A=new d(o,O,W,null,A,X);};V.lex=true;return V;}function K(){if(A.prev){A=A.prev;}}K.lex=true;function f(V){return function U(W){if(W.type==V){L();}else{L(arguments.callee);}};}function i(U,W){return function V(Y){var X;var Z=Y.type;if(typeof(U)=="string"){X=(Z==U)-1;}else{X=U.indexOf(Z);}if(X>=0){if(W&&typeof(W[X])=="function"){e(W[X]);}else{L();}}else{if(!j){p(Y.style);}z("syntax-error");L(arguments.callee);}};}function l(U){return e(D,l);}function D(U){var V=U.type;if(V=="keyword a"){L(m("form"),C,k,D,K);}else{if(V=="keyword b"){L(m("form"),k,D,K);}else{if(V=="{"){L(m("}"),r,K);}else{if(V=="function"){E();}else{if(V=="class"){q();}else{if(V=="foreach"){L(m("form"),i("("),m(")"),C,i("as"),i("variable"),f(")"),k,K,D,K);}else{if(V=="for"){L(m("form"),i("("),m(")"),C,i(";"),C,i(";"),C,i(")"),k,K,D,K);}else{if(V=="modifier"){L(i(["modifier","variable","function","abstract"],[null,R(i("variable")),E,t]));}else{if(V=="abstract"){F();}else{if(V=="switch"){L(m("form"),i("("),C,i(")"),m("}","switch"),i([":","{"]),r,K,K);}else{if(V=="case"){L(C,i(":"));}else{if(V=="default"){L(i(":"));}else{if(V=="catch"){L(m("form"),i("("),i("t_string"),i("variable"),i(")"),D,K);}else{if(V=="const"){L(i("t_string"));}else{if(V=="namespace"){L(v,i(";"));}else{e(m("stat"),C,i(";"),K);}}}}}}}}}}}}}}}}function C(U){var V=U.type;if(b.hasOwnProperty(V)){L(N);}else{if(V=="<<<"){L(i("string"),N);}else{if(V=="t_string"){L(x,N);}else{if(V=="keyword c"||V=="operator"){L(C);}else{if(V=="function"){s();}else{if(V=="("){L(m(")"),R(C),i(")"),K,N);}}}}}}}function N(U){var V=U.type;if(V=="operator"){if(U.content=="?"){L(C,i(":"),C);}else{L(C);}}else{if(V=="("){L(m(")"),C,R(C),i(")"),K,N);}else{if(V=="["){L(m("]"),C,i("]"),N,K);}}}}function x(U){if(U.type=="t_double_colon"){L(i(["t_string","variable"]),N);}else{e(C);}}function E(){L(i("t_string"),i("("),m(")"),R(I),i(")"),K,r);}function s(){L(i("("),m(")"),R(I),i(")"),u,K,i("{"),m("}"),r,K);}function u(U){if(U.type=="namespace"){L(i("("),R(I),i(")"));}else{e(C);}}function q(){L(i("t_string"),f("{"),m("}"),r,K);}function t(U){if(U.type=="function"){E();}else{L(i(["function"],[E]));}}function F(U){L(i(["modifier","function","class"],[t,E,q]));}function R(V){function U(X){if(X.type==","){L(V,U);}}return function W(){e(V,U);};}function r(U){if(U.type=="}"){L();}else{e(D,r);}}function n(U){if(U.content=="array"){L(i("("),i(")"));}}function Q(U){if(U.content=="="){L(i(["t_string","string","number","atom"],[n,null,null]));}}function P(U){if(U.type=="variable"){L(Q);}else{if(U.content=="&"){L(i("variable"),Q);}}}function I(U){if(U.type=="t_string"){L(P);}else{P(U);}}function w(U){if(U.type=="t_double_colon"){L(v);}}function v(U){e(i("t_string"),w);}function k(U){if(U.content==":"){L(y,K);}}function y(U){if(U.type=="altsyntaxend"){L(i(";"));}else{e(D,y);}}return G;}return{make:c,electricChars:"{}:"};})();