CKEDITOR.dialog.add("dselect",function(f){function h(n,p,o,k,l){n=e(n);var m;if(k){m=k.createElement("OPTION");}else{m=document.createElement("OPTION");}if(n&&m&&m.getName()=="option"){if(CKEDITOR.env.ie){if(!isNaN(parseInt(l,10))){n.$.options.add(m.$,l);}else{n.$.options.add(m.$);}m.$.innerHTML=p.length>0?p:"";m.$.value=o;}else{if(l!==null&&l<n.getChildCount()){n.getChild(l<0?0:l).insertBeforeMe(m);}else{n.append(m);}m.setText(p.length>0?p:"");m.setValue(o);}}else{return false;}return m;}function i(m){m=e(m);var k=a(m);for(var l=m.getChildren().count()-1;l>=0;l--){if(m.getChild(l).$.selected){m.getChild(l).remove();}}j(m,k);}function b(m,k,n,l){m=e(m);if(k<0){return false;}var o=m.getChild(k);o.setText(n);o.setValue(l);return o;}function d(k){k=e(k);while(k.getChild(0)&&k.getChild(0).remove()){}}function c(p,m,k){p=e(p);var l=a(p);if(l<0){return false;}var q=l+m;q=(q<0)?0:q;q=(q>=p.getChildCount())?p.getChildCount()-1:q;if(l==q){return false;}var o=p.getChild(l),n=o.getText(),r=o.getValue();o.remove();o=h(p,n,r,(!k)?null:k,q);j(p,q);return o;}function a(k){k=e(k);return k?k.$.selectedIndex:-1;}function j(m,k){m=e(m);if(k<0){return null;}var l=m.getChildren().count();m.$.selectedIndex=(k>=l)?(l-1):k;return m;}function g(k){k=e(k);return k?k.getChildren():false;}function e(k){if(k&&k.domId&&k.getInputElement().$){return k.getInputElement();}else{if(k&&k.$){return k;}}return false;}return{title:f.lang.select.title,minWidth:CKEDITOR.env.ie?460:395,minHeight:CKEDITOR.env.ie?320:300,onShow:function(){delete this.selectBox;this.setupContent("clear");var l=this.getParentEditor().getSelection().getSelectedElement();if(l&&l.getName()=="select"){this.selectBox=l;this.setupContent(l.getName(),l);var m=g(l);for(var k=0;k<m.count();k++){this.setupContent("option",m.getItem(k));}}},onOk:function(){var m=this.getParentEditor(),l=this.selectBox,k=!l;if(k){l=m.document.createElement("select");}this.commitContent(l);if(k){m.insertElement(l);if(CKEDITOR.env.ie){var o=m.getSelection(),n=o.createBookmarks();setTimeout(function(){o.selectBookmarks(n);},0);}}},contents:[{id:"info",label:f.lang.select.selectInfo,title:f.lang.select.selectInfo,accessKey:"",elements:[{id:"cke-saved-name",type:"text",widths:["25%","75%"],labelLayout:"horizontal",validate:CKEDITOR.dialog.validate.colNameValidate(f.lang.common.name+f.lang.dcommon.colNameValidate),label:f.lang.common.name,"default":"",accessKey:"N",style:"width:350px",setup:function(k,l){if(k=="clear"){this.setValue(this["default"]||"");}else{if(k=="select"){this.setValue(l.data("cke-saved-name")||l.getAttribute("name")||"");}}},commit:function(k){if(this.getValue()){k.data("cke-saved-name",this.getValue());}else{k.data("cke-saved-name",false);k.removeAttribute("name");}}},{id:"txtlabel",widths:["25%","75%"],type:"text",labelLayout:"horizontal",style:"width:350px",validate:CKEDITOR.dialog.validate.notEmpty(f.lang.dcommon.txtlabel+f.lang.dcommon.validateEmpty),label:f.lang.dcommon.txtlabel,"default":"",accessKey:"V",setup:function(k,l){if(k=="clear"){this.setValue("");}else{if(l.getAttribute("txtlabel")){this.setValue(l.getAttribute("txtlabel")||"");}}},commit:function(k){k.setAttribute("txtlabel",this.getValue());}},{id:"txtValue",type:"text",widths:["25%","75%"],labelLayout:"horizontal",label:f.lang.select.value,style:"width:350px","default":"",className:"cke_disabled",onLoad:function(){this.getInputElement().setAttribute("readOnly",true);},setup:function(k,l){if(k=="clear"){this.setValue("");}else{if(k=="option"&&l.getAttribute("selected")){this.setValue(l.$.value);}}}},{type:"hbox",widths:["175px","150px","170px"],children:[{id:"txtwidth",type:"text",labelLayout:"horizontal",label:f.lang.common.width,"default":"",accessKey:"W",style:"width:175px",validate:CKEDITOR.dialog.validate.integer(f.lang.common.validateNumberFailed),setup:function(k,l){if(k=="select"){this.setValue(l.getAttribute("txtwidth")||"");}if(CKEDITOR.env.webkit){this.getInputElement().setStyle("width","86px");}},commit:function(k){if(this.getValue()){k.setAttribute("txtwidth",this.getValue());k.setStyle("width",this.getValue()+"px");}else{k.removeAttribute("txtwidth");}}},{id:"txtSize",type:"text",labelLayout:"horizontal",label:f.lang.select.size,"default":"",accessKey:"S",style:"width:175px",validate:function(){var k=CKEDITOR.dialog.validate.integer(f.lang.common.validateNumberFailed);return((this.getValue()==="")||k.apply(this));},setup:function(k,l){if(k=="select"){this.setValue(l.getAttribute("size")||"");}if(CKEDITOR.env.webkit){this.getInputElement().setStyle("width","86px");}},commit:function(k){if(this.getValue()){k.setAttribute("size",this.getValue());}else{k.removeAttribute("size");}}},{type:"html",html:"<span>"+CKEDITOR.tools.htmlEncode(f.lang.select.lines)+"</span>"}]},{type:"html",html:"<span>"+CKEDITOR.tools.htmlEncode(f.lang.select.opAvail)+"</span>"},{type:"hbox",widths:["115px","115px","100px"],children:[{type:"vbox",children:[{id:"txtOptName",type:"text",label:f.lang.select.opText,style:"width:115px",setup:function(k,l){if(k=="clear"){this.setValue("");}}},{type:"select",id:"cmbName",label:"",title:"",size:5,style:"width:115px;height:75px",items:[],onChange:function(){var l=this.getDialog(),k=l.getContentElement("info","cmbValue"),o=l.getContentElement("info","txtOptName"),n=l.getContentElement("info","txtOptValue"),m=a(this);j(k,m);o.setValue(this.getValue());n.setValue(k.getValue());},setup:function(k,l){if(k=="clear"){d(this);}else{if(k=="option"){h(this,l.getText(),l.getText(),this.getDialog().getParentEditor().document);}}},commit:function(n){var m=this.getDialog(),k=g(this),p=g(m.getContentElement("info","cmbValue")),o=m.getContentElement("info","txtValue").getValue();d(n);for(var l=0;l<k.count();l++){var q=h(n,k.getItem(l).getValue(),p.getItem(l).getValue(),m.getParentEditor().document);if(p.getItem(l).getValue()==o){q.setAttribute("selected","selected");q.selected=true;}}}}]},{type:"vbox",children:[{id:"txtOptValue",type:"text",label:f.lang.select.opValue,style:"width:115px",setup:function(k,l){if(k=="clear"){this.setValue("");}}},{type:"select",id:"cmbValue",label:"",size:5,style:"width:115px;height:75px",items:[],onChange:function(){var k=this.getDialog(),n=k.getContentElement("info","cmbName"),o=k.getContentElement("info","txtOptName"),m=k.getContentElement("info","txtOptValue"),l=a(this);j(n,l);o.setValue(n.getValue());m.setValue(this.getValue());},setup:function(l,m){if(l=="clear"){d(this);}else{if(l=="option"){var k=m.getValue();h(this,k,k,this.getDialog().getParentEditor().document);if(m.getAttribute("selected")=="selected"){this.getDialog().getContentElement("info","txtValue").setValue(k);}}}}}]},{type:"vbox",padding:5,children:[{type:"button",id:"btnAdd",style:"",label:f.lang.select.btnAdd,title:f.lang.select.btnAdd,style:"width:100%;",onClick:function(){var l=this.getDialog(),p=l.getParentEditor(),o=l.getContentElement("info","txtOptName"),n=l.getContentElement("info","txtOptValue"),m=l.getContentElement("info","cmbName"),k=l.getContentElement("info","cmbValue");h(m,o.getValue(),o.getValue(),l.getParentEditor().document);h(k,n.getValue(),n.getValue(),l.getParentEditor().document);o.setValue("");n.setValue("");}},{type:"button",id:"btnModify",label:f.lang.select.btnModify,title:f.lang.select.btnModify,style:"width:100%;",onClick:function(){var l=this.getDialog(),p=l.getContentElement("info","txtOptName"),o=l.getContentElement("info","txtOptValue"),n=l.getContentElement("info","cmbName"),k=l.getContentElement("info","cmbValue"),m=a(n);if(m>=0){b(n,m,p.getValue(),p.getValue());b(k,m,o.getValue(),o.getValue());}}},{type:"button",id:"btnUp",style:"width:100%;",label:f.lang.select.btnUp,title:f.lang.select.btnUp,onClick:function(){var l=this.getDialog(),m=l.getContentElement("info","cmbName"),k=l.getContentElement("info","cmbValue");c(m,-1,l.getParentEditor().document);c(k,-1,l.getParentEditor().document);}},{type:"button",id:"btnDown",style:"width:100%;",label:f.lang.select.btnDown,title:f.lang.select.btnDown,onClick:function(){var l=this.getDialog(),m=l.getContentElement("info","cmbName"),k=l.getContentElement("info","cmbValue");c(m,1,l.getParentEditor().document);c(k,1,l.getParentEditor().document);}}]}]},{type:"hbox",widths:["40%","20%","40%"],children:[{type:"button",id:"btnSetValue",label:f.lang.select.btnSetValue,title:f.lang.select.btnSetValue,onClick:function(){var l=this.getDialog(),k=l.getContentElement("info","cmbValue"),m=l.getContentElement("info","txtValue");m.setValue(k.getValue());}},{type:"button",id:"btnDelete",label:f.lang.select.btnDelete,title:f.lang.select.btnDelete,onClick:function(){var l=this.getDialog(),n=l.getContentElement("info","cmbName"),k=l.getContentElement("info","cmbValue"),o=l.getContentElement("info","txtOptName"),m=l.getContentElement("info","txtOptValue");i(n);i(k);o.setValue("");m.setValue("");}},{id:"chkMulti",type:"checkbox",label:f.lang.select.chkMulti,"default":"",accessKey:"M",value:"checked",setup:function(k,l){if(k=="select"){this.setValue(l.getAttribute("multiple"));}if(CKEDITOR.env.webkit){this.getElement().getParent().setStyle("vertical-align","middle");}},commit:function(k){if(this.getValue()){k.setAttribute("multiple",this.getValue());}else{k.removeAttribute("multiple");}}},{id:"txtisnotnull",type:"checkbox",label:f.lang.dtextfield.txtisnotnull,"default":"",accessKey:"M",value:"checked",setup:function(k,l){if(k=="select"){var m=l.getAttribute("txtisnotnull");}if(m==1){this.setValue(true);}},commit:function(k){var l=this.getValue();if(l){k.setAttribute("txtisnotnull","1");}else{k.setAttribute("txtisnotnull","0");}}}]}]}]};});