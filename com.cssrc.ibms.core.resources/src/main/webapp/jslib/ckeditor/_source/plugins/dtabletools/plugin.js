(function(){var b=/^(?:td|th)$/;function s(F){var y=F.createBookmarks();var v=F.getRanges();var w=[];var G={};function D(H){if(w.length>0){return;}if(H.type==CKEDITOR.NODE_ELEMENT&&b.test(H.getName())&&!H.getCustomData("selected_cell")){CKEDITOR.dom.element.setMarker(G,H,"selected_cell",true);w.push(H);}}for(var B=0;B<v.length;B++){var C=v[B];if(C.collapsed){var x=C.getCommonAncestor();var A=x.getAscendant("td",true)||x.getAscendant("th",true);if(A){w.push(A);}}else{var u=new CKEDITOR.dom.walker(C);var z;u.guard=D;while((z=u.next())){var E=z.getAscendant("td")||z.getAscendant("th");if(E&&!E.getCustomData("selected_cell")){CKEDITOR.dom.element.setMarker(G,E,"selected_cell",true);w.push(E);}}}}CKEDITOR.dom.element.clearAllMarkers(G);F.selectBookmarks(y);return w;}function t(v){var w=0,x=v.length-1,z={},u,A,y;while((u=v[w++])){CKEDITOR.dom.element.setMarker(z,u,"delete_cell",true);}w=0;while((u=v[w++])){if((A=u.getPrevious())&&!A.getCustomData("delete_cell")||(A=u.getNext())&&!A.getCustomData("delete_cell")){CKEDITOR.dom.element.clearAllMarkers(z);return A;}}CKEDITOR.dom.element.clearAllMarkers(z);y=v[0].getParent();if((y=y.getPrevious())){return y.getLast();}y=v[x].getParent();if((y=y.getNext())){return y.getChild(0);}return null;}function a(M,z){var A=s(M),x=A[0],J=x.getAscendant("table"),N=x.getDocument(),u=A[0].getParent(),K=u.$.rowIndex,F=A[A.length-1],v=F.getParent().$.rowIndex+F.$.rowSpan-1,H=new CKEDITOR.dom.element(J.$.rows[v]),C=z?K:v,B=z?u:H;var L=CKEDITOR.tools.buildTableMap(J),D=L[C],G=z?L[C-1]:L[C+1],E=L[0].length;var y=N.createElement("tr");for(var I=0;D[I]&&I<E;I++){var w;if(D[I].rowSpan>1&&G&&D[I]==G[I]){w=D[I];w.rowSpan+=1;}else{w=new CKEDITOR.dom.element(D[I]).clone();w.removeAttribute("rowSpan");!CKEDITOR.env.ie&&w.appendBogus();y.append(w);w=w.$;}I+=w.colSpan-1;}z?y.insertBefore(B):y.insertAfter(B);}function c(D){if(D instanceof CKEDITOR.dom.selection){var z=s(D),x=z[0],I=x.getAscendant("table"),L=CKEDITOR.tools.buildTableMap(I),u=z[0].getParent(),K=u.$.rowIndex,E=z[z.length-1],v=E.getParent().$.rowIndex+E.$.rowSpan-1,J=[];for(var H=K;H<=v;H++){var C=L[H],A=new CKEDITOR.dom.element(I.$.rows[H]);for(var F=0;F<C.length;F++){var w=new CKEDITOR.dom.element(C[F]),y=w.getParent().$.rowIndex;if(w.$.rowSpan==1){w.remove();}else{w.$.rowSpan-=1;if(y==H){var M=L[H+1];M[F-1]?w.insertAfter(new CKEDITOR.dom.element(M[F-1])):new CKEDITOR.dom.element(I.$.rows[H+1]).append(w,1);}}F+=w.$.colSpan-1;}J.push(A);}var B=I.$.rows;var G=new CKEDITOR.dom.element(B[v+1]||(K>0?B[K-1]:null)||I.$.parentNode);for(H=J.length;H>=0;H--){c(J[H]);}return G;}else{if(D instanceof CKEDITOR.dom.element){I=D.getAscendant("table");if(I.$.rows.length==1){I.remove();}else{D.remove();}}}return null;}function k(u,v){var z=u.getParent(),A=z.$.cells;var x=0;for(var y=0;y<A.length;y++){var w=A[y];x+=v?1:w.colSpan;if(w==u.$){break;}}return x-1;}function h(x,v){var u=v?Infinity:0;for(var y=0;y<x.length;y++){var w=k(x[y],v);if(v?w<u:w>u){u=w;}}return u;}function n(C,A){var I=s(C),B=I[0],H=B.getAscendant("table"),v=h(I,1),y=h(I),E=A?v:y;var u=CKEDITOR.tools.buildTableMap(H),x=[],w=[],F=u.length;for(var z=0;z<F;z++){x.push(u[z][E]);var G=A?u[z][E-1]:u[z][E+1];G&&w.push(G);}for(z=0;z<F;z++){var D;if(x[z].colSpan>1&&w.length&&w[z]==x[z]){D=x[z];D.colSpan+=1;}else{D=new CKEDITOR.dom.element(x[z]).clone();D.removeAttribute("colSpan");!CKEDITOR.env.ie&&D.appendBogus();D[A?"insertBefore":"insertAfter"].call(D,new CKEDITOR.dom.element(x[z]));D=D.$;}z+=D.rowSpan-1;}}function g(u){var y=s(u),x=y[0],E=y[y.length-1],J=x.getAscendant("table"),L=CKEDITOR.tools.buildTableMap(J),v,z,K=[];for(var I=0,B=L.length;I<B;I++){for(var G=0,C=L[I].length;G<C;G++){if(L[I][G]==x.$){v=G;}if(L[I][G]==E.$){z=G;}}}for(I=v;I<=z;I++){for(G=0;G<L.length;G++){var D=L[G],A=new CKEDITOR.dom.element(J.$.rows[G]),w=new CKEDITOR.dom.element(D[I]);if(w.$){if(w.$.colSpan==1){w.remove();}else{w.$.colSpan-=1;}G+=w.$.rowSpan-1;if(!A.$.cells.length){K.push(A);}}}}var F=J.$.rows[0]&&J.$.rows[0].cells;var H=new CKEDITOR.dom.element(F[v]||(v?F[v-1]:J.$.parentNode));if(K.length==B){J.remove();}return H;}function r(x){var w=[],A=x[0]&&x[0].getAscendant("table"),y,z,v,u;for(y=0,z=x.length;y<z;y++){w.push(x[y].$.cellIndex);}w.sort();for(y=1,z=w.length;y<z;y++){if(w[y]-w[y-1]>1){v=w[y-1]+1;break;}}if(!v){v=w[0]>0?(w[0]-1):(w[w.length-1]+1);}var B=A.$.rows;for(y=0,z=B.length;y<z;y++){u=B[y].cells[v];if(u){break;}}return u?new CKEDITOR.dom.element(u):A.getPrevious();}function j(x,v){var w=x.getStartElement();var u=w.getAscendant("td",1)||w.getAscendant("th",1);if(!u){return;}var y=u.clone();if(!CKEDITOR.env.ie){y.appendBogus();}if(v){y.insertBefore(u);}else{y.insertAfter(u);}}function m(x){if(x instanceof CKEDITOR.dom.selection){var u=s(x);var y=u[0]&&u[0].getAscendant("table");var v=t(u);for(var w=u.length-1;w>=0;w--){m(u[w]);}if(v){e(v,true);}else{if(y){y.remove();}}}else{if(x instanceof CKEDITOR.dom.element){var z=x.getParent();if(z.getChildCount()==1){z.remove();}else{x.remove();}}}}function f(u){var v=u.getBogus();v&&v.remove();u.trim();}function e(u,w){var v=new CKEDITOR.dom.range(u.getDocument());if(!v["moveToElementEdit"+(w?"End":"Start")](u)){v.selectNodeContents(u);v.collapse(w?false:true);}v.select(true);}function q(y,x,u){var v=y[x];if(typeof u=="undefined"){return v;}for(var w=0;v&&w<v.length;w++){if(u.is&&v[w]==u.$){return w;}else{if(w==u){return new CKEDITOR.dom.element(v[w]);}}}return u.is?-1:null;}function o(z,v,u){var x=[];for(var w=0;w<z.length;w++){var y=z[w];if(typeof u=="undefined"){x.push(y[v]);}else{if(u.is&&y[v]==u.$){return w;}else{if(w==u){return new CKEDITOR.dom.element(y[v]);}}}}return(typeof u=="undefined")?x:u.is?-1:null;}function l(ab,H,K){var D=s(ab);var F;if((H?D.length!=1:D.length<2)||(F=ab.getCommonAncestor())&&F.type==CKEDITOR.NODE_ELEMENT&&F.is("table")){return false;}var y,z=D[0],U=z.getAscendant("table"),W=CKEDITOR.tools.buildTableMap(U),R=W.length,T=W[0].length,u=z.getParent().$.rowIndex,E=q(W,u,z);if(H){var P;try{var Y=parseInt(z.getAttribute("rowspan"),10)||1;var A=parseInt(z.getAttribute("colspan"),10)||1;P=W[H=="up"?(u-Y):H=="down"?(u+Y):u][H=="left"?(E-A):H=="right"?(E+A):E];}catch(X){return false;}if(!P||z.$==P){return false;}D[(H=="up"||H=="left")?"unshift":"push"](new CKEDITOR.dom.element(P));}var aa=z.getDocument(),x=u,Z=0,B=0,w=!K&&new CKEDITOR.dom.documentFragment(aa),V=0;for(var S=0;S<D.length;S++){y=D[S];var v=y.getParent(),J=y.getFirst(),I=y.$.colSpan,C=y.$.rowSpan,M=v.$.rowIndex,O=q(W,M,y);V+=I*C;B=Math.max(B,O-E+I);Z=Math.max(Z,M-u+C);if(!K){if(f(y),y.getChildren().count()){if(M!=x&&J&&!(J.isBlockBoundary&&J.isBlockBoundary({br:1}))){var L=w.getLast(CKEDITOR.dom.walker.whitespaces(true));if(L&&!(L.is&&L.is("br"))){w.append("br");}}y.moveChildren(w);}S?y.remove():y.setHtml("");}x=M;}if(!K){w.moveChildren(z);if(!CKEDITOR.env.ie){z.appendBogus();}if(B>=T){z.removeAttribute("rowSpan");}else{z.$.rowSpan=Z;}if(Z>=R){z.removeAttribute("colSpan");}else{z.$.colSpan=B;}var N=new CKEDITOR.dom.nodeList(U.$.rows),G=N.count();for(S=G-1;S>=0;S--){var Q=N.getItem(S);if(!Q.$.cells.length){Q.remove();G++;continue;}}return z;}else{return(Z*B)==V;}}function d(N,B){var w=s(N);if(w.length>1){return false;}else{if(B){return true;}}var v=w[0],u=v.getParent(),J=u.getAscendant("table"),M=CKEDITOR.tools.buildTableMap(J),D=u.$.rowIndex,G=q(M,D,v),x=v.$.rowSpan,F,z,y,H;if(x>1){z=Math.ceil(x/2);y=Math.floor(x/2);H=D+z;var E=new CKEDITOR.dom.element(J.$.rows[H]),K=q(M,H),A;F=v.clone();for(var L=0;L<K.length;L++){A=K[L];if(A.parentNode==E.$&&L>G){F.insertBefore(new CKEDITOR.dom.element(A));break;}else{A=null;}}if(!A){E.append(F,true);}}else{y=z=1;E=u.clone();E.insertAfter(u);E.append(F=v.clone());var C=q(M,D);for(var I=0;I<C.length;I++){C[I].rowSpan++;}}if(!CKEDITOR.env.ie){F.appendBogus();}v.$.rowSpan=z;F.$.rowSpan=y;if(z==1){v.removeAttribute("rowSpan");}if(y==1){F.removeAttribute("rowSpan");}return F;}function p(D,B){var I=s(D);if(I.length>1){return false;}else{if(B){return true;}}var F=I[0],A=F.getParent(),H=A.getAscendant("table"),u=CKEDITOR.tools.buildTableMap(H),C=A.$.rowIndex,E=q(u,C,F),x=F.$.colSpan,w,z,G;if(x>1){z=Math.ceil(x/2);G=Math.floor(x/2);}else{G=z=1;var y=o(u,E);for(var v=0;v<y.length;v++){y[v].colSpan++;}}w=F.clone();w.insertAfter(F);if(!CKEDITOR.env.ie){w.appendBogus();}F.$.colSpan=z;w.$.colSpan=G;if(z==1){F.removeAttribute("colSpan");}if(G==1){w.removeAttribute("colSpan");}return w;}var i={thead:1,tbody:1,tfoot:1,td:1,tr:1,th:1};CKEDITOR.plugins.dtabletools={init:function(u){var v=u.lang.table;u.addCommand("dcellProperties",new CKEDITOR.dialogCommand("dcellProperties"));CKEDITOR.dialog.add("dcellProperties",this.path+"dialogs/tableCell.js");u.addCommand("dtableDelete",{exec:function(A){var z=A.getSelection(),y=z&&z.getStartElement(),B=y&&y.getAscendant("table",1);if(!B){return;}var x=B.getParent();if(x.getChildCount()==1&&!x.is("body","td","th")){B=x;}var w=new CKEDITOR.dom.range(A.document);w.moveToPosition(B,CKEDITOR.POSITION_BEFORE_START);B.remove();w.select();}});u.addCommand("drowDelete",{exec:function(x){var w=x.getSelection();e(c(w));}});u.addCommand("drowInsertBefore",{exec:function(x){var w=x.getSelection();a(w,true);}});u.addCommand("drowInsertAfter",{exec:function(x){var w=x.getSelection();a(w);}});u.addCommand("dcolumnDelete",{exec:function(y){var x=y.getSelection();var w=g(x);w&&e(w,true);}});u.addCommand("dcolumnInsertBefore",{exec:function(x){var w=x.getSelection();n(w,true);}});u.addCommand("dcolumnInsertAfter",{exec:function(x){var w=x.getSelection();n(w);}});u.addCommand("dcellDelete",{exec:function(x){var w=x.getSelection();m(w);}});u.addCommand("dcellMerge",{exec:function(w){e(l(w.getSelection()),true);}});u.addCommand("dcellMergeRight",{exec:function(w){e(l(w.getSelection(),"right"),true);}});u.addCommand("dcellMergeDown",{exec:function(w){e(l(w.getSelection(),"down"),true);}});u.addCommand("dcellVerticalSplit",{exec:function(w){e(d(w.getSelection()));}});u.addCommand("dcellHorizontalSplit",{exec:function(w){e(p(w.getSelection()));}});u.addCommand("dcellInsertBefore",{exec:function(x){var w=x.getSelection();j(w,true);}});u.addCommand("dcellInsertAfter",{exec:function(x){var w=x.getSelection();j(w);}});if(u.addMenuItems){u.addMenuItems({dtablecell:{label:v.cell.menu,group:"dtablecell",order:1,getItems:function(){var x=u.getSelection(),w=s(x);return{dtablecell_insertBefore:CKEDITOR.TRISTATE_OFF,dtablecell_insertAfter:CKEDITOR.TRISTATE_OFF,dtablecell_delete:CKEDITOR.TRISTATE_OFF,dtablecell_merge:l(x,null,true)?CKEDITOR.TRISTATE_OFF:CKEDITOR.TRISTATE_DISABLED,dtablecell_merge_right:l(x,"right",true)?CKEDITOR.TRISTATE_OFF:CKEDITOR.TRISTATE_DISABLED,dtablecell_merge_down:l(x,"down",true)?CKEDITOR.TRISTATE_OFF:CKEDITOR.TRISTATE_DISABLED,dtablecell_split_vertical:d(x,true)?CKEDITOR.TRISTATE_OFF:CKEDITOR.TRISTATE_DISABLED,dtablecell_split_horizontal:p(x,true)?CKEDITOR.TRISTATE_OFF:CKEDITOR.TRISTATE_DISABLED,dtablecell_properties:w.length>0?CKEDITOR.TRISTATE_OFF:CKEDITOR.TRISTATE_DISABLED};}},dtablecell_insertBefore:{label:v.cell.insertBefore,group:"dtablecell",command:"dcellInsertBefore",order:5},dtablecell_insertAfter:{label:v.cell.insertAfter,group:"dtablecell",command:"dcellInsertAfter",order:10},dtablecell_delete:{label:v.cell.deleteCell,group:"dtablecell",command:"dcellDelete",order:15},dtablecell_merge:{label:v.cell.merge,group:"dtablecell",command:"dcellMerge",order:16},dtablecell_merge_right:{label:v.cell.mergeRight,group:"dtablecell",command:"dcellMergeRight",order:17},dtablecell_merge_down:{label:v.cell.mergeDown,group:"dtablecell",command:"dcellMergeDown",order:18},dtablecell_split_horizontal:{label:v.cell.splitHorizontal,group:"dtablecell",command:"dcellHorizontalSplit",order:19},dtablecell_split_vertical:{label:v.cell.splitVertical,group:"dtablecell",command:"dcellVerticalSplit",order:20},dtablecell_properties:{label:v.cell.title,group:"dtablecellproperties",command:"dcellProperties",order:21},dtablerow:{label:v.row.menu,group:"dtablerow",order:1,getItems:function(){return{dtablerow_insertBefore:CKEDITOR.TRISTATE_OFF,dtablerow_insertAfter:CKEDITOR.TRISTATE_OFF,dtablerow_delete:CKEDITOR.TRISTATE_OFF};}},dtablerow_insertBefore:{label:v.row.insertBefore,group:"dtablerow",command:"drowInsertBefore",order:5},dtablerow_insertAfter:{label:v.row.insertAfter,group:"dtablerow",command:"drowInsertAfter",order:10},dtablerow_delete:{label:v.row.deleteRow,group:"dtablerow",command:"drowDelete",order:15},dtablecolumn:{label:v.column.menu,group:"dtablecolumn",order:1,getItems:function(){return{dtablecolumn_insertBefore:CKEDITOR.TRISTATE_OFF,dtablecolumn_insertAfter:CKEDITOR.TRISTATE_OFF,dtablecolumn_delete:CKEDITOR.TRISTATE_OFF};}},dtablecolumn_insertBefore:{label:v.column.insertBefore,group:"dtablecolumn",command:"dcolumnInsertBefore",order:5},dtablecolumn_insertAfter:{label:v.column.insertAfter,group:"dtablecolumn",command:"dcolumnInsertAfter",order:10},dtablecolumn_delete:{label:v.column.deleteColumn,group:"dtablecolumn",command:"dcolumnDelete",order:15}});}if(u.contextMenu){u.contextMenu.addListener(function(w,x){if(!w||w.isReadOnly()){return null;}while(w){if(w.getName() in i){return{dtablecell:CKEDITOR.TRISTATE_OFF,dtablerow:CKEDITOR.TRISTATE_OFF,dtablecolumn:CKEDITOR.TRISTATE_OFF};}w=w.getParent();}return null;});}},getSelectedCells:s};CKEDITOR.plugins.add("dtabletools",CKEDITOR.plugins.dtabletools);})();CKEDITOR.tools.buildTableMap=function(o){var n=o.$.rows;var a=-1;var m=[];for(var f=0;f<n.length;f++){a++;!m[a]&&(m[a]=[]);var l=-1;for(var e=0;e<n[f].cells.length;e++){var k=n[f].cells[e];l++;while(m[a][l]){l++;}var d=isNaN(k.colSpan)?1:k.colSpan;var g=isNaN(k.rowSpan)?1:k.rowSpan;for(var b=0;b<g;b++){if(!m[a+b]){m[a+b]=[];}for(var h=0;h<d;h++){m[a+b][l+h]=n[f].cells[e];}}l+=d-1;}}return m;};