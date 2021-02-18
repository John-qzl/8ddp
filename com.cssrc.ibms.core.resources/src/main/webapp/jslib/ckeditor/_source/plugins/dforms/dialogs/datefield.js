CKEDITOR.dialog.add("datefield",function(b){var a={value:1,size:1,maxLength:1};var c={text:1,password:1};return{title:b.lang.datefield.title,minWidth:350,minHeight:160,onShow:function(){delete this.textField;var d=this.getParentEditor().getSelection().getSelectedElement();if(d&&d.getName()=="input"&&(c[d.getAttribute("type")]||!d.getAttribute("type"))){this.textField=d;this.setupContent(d);}},onOk:function(){var f,e=this.textField,d=!e;if(d){f=this.getParentEditor();e=f.document.createElement("input");e.setAttribute("type","text");}e.setAttribute("xtype","datefield");e.setAttribute("class","x-form-text x-form-field");if(d){f.insertElement(e);}this.commitContent({element:e});},onLoad:function(){var e=function(f){var g=f.hasAttribute(this.id)&&f.getAttribute(this.id);this.setValue(g||"");};var d=function(h){var f=h.element;var g=this.getValue();if(g){f.setAttribute(this.id,g);}else{f.removeAttribute(this.id);}};this.foreach(function(f){if(a[f.id]){f.setup=e;f.commit=d;}});},contents:[{id:"info",label:b.lang.textfield.title,title:b.lang.textfield.title,elements:[{type:"hbox",widths:["50%","50%"],children:[{id:"_cke_saved_name",type:"text",label:b.lang.textfield.name,"default":"",accessKey:"N",validate:CKEDITOR.dialog.validate.colNameValidate(b.lang.textfield.name+b.lang.dcommon.colNameValidate),setup:function(d){this.setValue(d.data("cke-saved-name")||d.getAttribute("name")||"");},commit:function(e){var d=e.element;if(this.getValue()){d.data("cke-saved-name",this.getValue());}else{d.data("cke-saved-name",false);d.removeAttribute("name");}}},{id:"txtlabel",type:"text",validate:CKEDITOR.dialog.validate.notEmpty(b.lang.dcommon.txtlabel+b.lang.dcommon.validateEmpty),label:b.lang.dcommon.txtlabel,"default":"",accessKey:"V",setup:function(d){this.setValue(d.getAttribute("txtlabel")||"");},commit:function(e){var d=e.element;d.setAttribute("txtlabel",this.getValue());}}]},{type:"hbox",widths:["50%","50%"],children:[{id:"txtvaluetype",type:"select",label:b.lang.dtextfield.datatype,"default":"date",accessKey:"T",items:[["date"],["datetime"]],setup:function(d){this.setValue(d.getAttribute("txtvaluetype"));},commit:function(e){var d=e.element;d.setAttribute("txtvaluetype",this.getValue());}},{id:"dataformat",type:"select",label:b.lang.datefield.datetype,"default":"yyyy-MM-dd",accessKey:"T",items:[["yyyy-MM-dd"],["yyyy-MM-dd HH:mm:ss"]],setup:function(d){this.setValue(d.getAttribute("dataformat"));},commit:function(e){var d=e.element;d.setAttribute("dataformat",this.getValue());}}]},{type:"hbox",widths:["50%","50%"],children:[{id:"txtisnotnull",type:"checkbox",label:b.lang.dtextfield.txtisnotnull,"default":"",accessKey:"P",value:"checked",setup:function(d){var e=d.getAttribute("txtisnotnull");if(e==1){this.setValue(true);}},commit:function(f){var d=f.element;var e=this.getValue();if(e){d.setAttribute("txtisnotnull","1");}else{d.setAttribute("txtisnotnull","0");}}},{id:"iscurrent",type:"checkbox",label:b.lang.datefield.istoday,"default":"",accessKey:"P",value:"checked",setup:function(d){var e=d.getAttribute("iscurrent");if(e==1){this.setValue(true);}},commit:function(f){var d=f.element;var e=this.getValue();if(e){d.setAttribute("iscurrent","1");}else{d.setAttribute("iscurrent","0");}}}]}]}]};});