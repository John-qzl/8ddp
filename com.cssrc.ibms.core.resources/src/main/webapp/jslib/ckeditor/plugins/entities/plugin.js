(function(){var a="nbsp,gt,lt,amp";var d="quot,iexcl,cent,pound,curren,yen,brvbar,sect,uml,copy,ordf,laquo,"+"not,shy,reg,macr,deg,plusmn,sup2,sup3,acute,micro,para,middot,"+"cedil,sup1,ordm,raquo,frac14,frac12,frac34,iquest,times,divide,"+"fnof,bull,hellip,prime,Prime,oline,frasl,weierp,image,real,trade,"+"alefsym,larr,uarr,rarr,darr,harr,crarr,lArr,uArr,rArr,dArr,hArr,"+"forall,part,exist,empty,nabla,isin,notin,ni,prod,sum,minus,lowast,"+"radic,prop,infin,ang,and,or,cap,cup,int,there4,sim,cong,asymp,ne,"+"equiv,le,ge,sub,sup,nsub,sube,supe,oplus,otimes,perp,sdot,lceil,"+"rceil,lfloor,rfloor,lang,rang,loz,spades,clubs,hearts,diams,"+"circ,tilde,ensp,emsp,thinsp,zwnj,zwj,lrm,rlm,ndash,mdash,lsquo,"+"rsquo,sbquo,ldquo,rdquo,bdquo,dagger,Dagger,permil,lsaquo,rsaquo,"+"euro";var c="Agrave,Aacute,Acirc,Atilde,Auml,Aring,AElig,Ccedil,Egrave,Eacute,"+"Ecirc,Euml,Igrave,Iacute,Icirc,Iuml,ETH,Ntilde,Ograve,Oacute,Ocirc,"+"Otilde,Ouml,Oslash,Ugrave,Uacute,Ucirc,Uuml,Yacute,THORN,szlig,"+"agrave,aacute,acirc,atilde,auml,aring,aelig,ccedil,egrave,eacute,"+"ecirc,euml,igrave,iacute,icirc,iuml,eth,ntilde,ograve,oacute,ocirc,"+"otilde,ouml,oslash,ugrave,uacute,ucirc,uuml,yacute,thorn,yuml,"+"OElig,oelig,Scaron,scaron,Yuml";var e="Alpha,Beta,Gamma,Delta,Epsilon,Zeta,Eta,Theta,Iota,Kappa,Lambda,Mu,"+"Nu,Xi,Omicron,Pi,Rho,Sigma,Tau,Upsilon,Phi,Chi,Psi,Omega,alpha,"+"beta,gamma,delta,epsilon,zeta,eta,theta,iota,kappa,lambda,mu,nu,xi,"+"omicron,pi,rho,sigmaf,sigma,tau,upsilon,phi,chi,psi,omega,thetasym,"+"upsih,piv";function b(k,l){var o={},n=[];var j={nbsp:"\u00A0",shy:"\u00AD",gt:"\u003E",lt:"\u003C",amp:"\u0026"};k=k.replace(/\b(nbsp|shy|gt|lt|amp)(?:,|$)/g,function(q,p){var r=l?"&"+p+";":j[p],i=l?j[p]:"&"+p+";";o[r]=i;n.push(r);return"";});if(!l&&k){k=k.split(",");var f=document.createElement("div"),m;f.innerHTML="&"+k.join(";&")+";";m=f.innerHTML;f=null;for(var h=0;h<m.length;h++){var g=m.charAt(h);o[g]="&"+k[h]+";";n.push(g);}}o.regex=n.join(l?"|":"");return o;}CKEDITOR.plugins.add("entities",{afterInit:function(l){var j=l.config;var o=l.dataProcessor,g=o&&o.htmlFilter;if(g){var m="";if(j.basicEntities!==false){m+=a;}if(j.entities){m+=","+d;if(j.entities_latin){m+=","+c;}if(j.entities_greek){m+=","+e;}if(j.entities_additional){m+=","+j.entities_additional;}}var h=b(m);var n=h.regex?"["+h.regex+"]":"a^";delete h.regex;if(j.entities&&j.entities_processNumerical){n="[^ -~]|"+n;}n=new RegExp(n,"g");function p(q){return j.entities_processNumerical=="force"||!h[q]?"&#"+q.charCodeAt(0)+";":h[q];}var k=b([a,"shy"].join(","),true),i=new RegExp(k.regex,"g");function f(q){return k[q];}g.addRules({text:function(q){return q.replace(i,f).replace(n,p);}});}}});})();CKEDITOR.config.basicEntities=true;CKEDITOR.config.entities=true;CKEDITOR.config.entities_latin=true;CKEDITOR.config.entities_greek=true;CKEDITOR.config.entities_additional="#39";