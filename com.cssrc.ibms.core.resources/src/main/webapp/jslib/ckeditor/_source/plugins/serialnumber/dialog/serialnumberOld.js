Ext.ns("SerialNumberForm");(function(){function a(c){var d="";var b="";return{title:"流水号详细信息",minWidth:300,minHeight:80,buttons:[CKEDITOR.dialog.okButton,CKEDITOR.dialog.cancelButton],contents:[{id:"info",elements:[{id:"name",type:"text",style:"width: 50%;",label:"名称:","default":"",required:true,validate:CKEDITOR.dialog.validate.notEmpty("名称不能为空"),setup:function(e){this.setValue(e.getAttribute("name"));e.setAttribute("name",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("name",this.getValue());}},{id:"alias",type:"text",style:"width: 50%;",label:"别名:","default":"",required:true,setup:function(e){this.setValue(e.getAttribute("alias"));e.setAttribute("alias",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("alias",this.getValue());},validate:CKEDITOR.dialog.validate.notEmpty("名称不能为空")},{id:"rule",type:"text",style:"width: 50%;",label:"规则:","default":"",required:true,validate:CKEDITOR.dialog.validate.notEmpty("规则不能为空"),setup:function(e){this.setValue(e.getAttribute("rule"));e.setAttribute("rule",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("rule",this.getValue());}},{id:"ascentType",type:"select",style:"width: 50%;",label:"生成方式:","default":"",items:[["每日生成","byDate"],["每月生成","byMonth"],["每年生成","byYear"],["递增","byStep"]],setup:function(e){this.setValue(e.getAttribute("ascentType"));e.setAttribute("ascentType",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("ascentType",this.getValue());}},{id:"length",type:"text",style:"width: 50%;",label:"流水号长度:","default":"",validate:CKEDITOR.dialog.validate.integer(c.lang.common.validateNumberFailed),setup:function(e){this.setValue(e.getAttribute("length"));e.setAttribute("length",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("length",this.getValue());}},{id:"step",type:"text",style:"width: 50%;",label:"步长:","default":"",validate:CKEDITOR.dialog.validate.integer(c.lang.common.validateNumberFailed),setup:function(e){this.setValue(e.getAttribute("step"));e.setAttribute("step",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("step",this.getValue());}},{id:"default",type:"text",style:"width: 50%;",label:"初始值:","default":"",setup:function(e){this.setValue(e.getAttribute("default"));e.setAttribute("default",this.getValue());},commit:function(f){var e=f.element;e.setAttribute("default",this.getValue());}},]}],onLoad:function(){},onShow:function(){var e=this.getParentEditor().getSelection().getSelectedElement();this.textField=e;this.setupContent(e);},onHide:function(){},onOk:function(){var g,f=this.textField,e=!f;if(e){g=this.getParentEditor();f=g.document.createElement("input");f.setAttribute("type","text");}f.setAttribute("class","x-form-text x-form-field");f.setAttribute("xtype","serialnumber");f.setAttribute("disabled","true");f.setAttribute("style","border:none;border-bottom:1px solid red;background-color:white;");f.setAttribute("value","No."+d);f.setAttribute("name",b);if(e){g.insertElement(f);}this.commitContent({element:f});},onCancel:function(){},resizable:CKEDITOR.DIALOG_RESIZE_HEIGHT};}CKEDITOR.dialog.add("serialnumber",function(b){return a(b);});})();