CKEDITOR.scriptLoader=(function(){var a={},b={};return{load:function(e,o,p,f){var d=(typeof e=="string");if(d){e=[e];}if(!p){p=CKEDITOR;}var l=e.length,j=[],k=[];var n=function(i){if(o){if(d){o.call(p,i);}else{o.call(p,j,k);}}};if(l===0){n(true);return;}var h=function(i,q){(q?j:k).push(i);if(--l<=0){f&&CKEDITOR.document.getDocumentElement().removeStyle("cursor");n(q);}};var m=function(q,t){a[q]=1;var s=b[q];delete b[q];for(var r=0;r<s.length;r++){s[r](q,t);}};var c=function(q){if(a[q]){h(q,true);return;}var r=b[q]||(b[q]=[]);r.push(h);if(r.length>1){return;}var i=new CKEDITOR.dom.element("script");i.setAttributes({type:"text/javascript",src:q});if(o){if(CKEDITOR.env.ie){i.$.onreadystatechange=function(){if(i.$.readyState=="loaded"||i.$.readyState=="complete"){i.$.onreadystatechange=null;m(q,true);}};}else{i.$.onload=function(){setTimeout(function(){m(q,true);},0);};i.$.onerror=function(){m(q,false);};}}i.appendTo(CKEDITOR.document.getHead());CKEDITOR.fire("download",q);};f&&CKEDITOR.document.getDocumentElement().setStyle("cursor","wait");for(var g=0;g<l;g++){c(e[g]);}}};})();