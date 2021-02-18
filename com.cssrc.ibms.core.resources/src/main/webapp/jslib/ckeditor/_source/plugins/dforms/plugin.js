CKEDITOR.plugins.add("dforms",{lang:["zh_cn"],init:function(b){var d=b.lang;b.addCss("form"+"{"+"border: 1px dotted #FF0000;"+"padding: 2px;"+"}\n");b.addCss("img.cke_hidden"+"{"+"background-image: url("+CKEDITOR.getUrl(this.path+"images/hiddenfield.gif")+");"+"background-position: center center;"+"background-repeat: no-repeat;"+"border: 1px solid #a9a9a9;"+"width: 16px !important;"+"height: 16px !important;"+"}");var c=function(e,g,h){b.addCommand(g,new CKEDITOR.dialogCommand(g));var f=e.charAt(0).toLowerCase()+e.slice(1);b.ui.addButton(e,{label:d.dcommon[f],command:g});CKEDITOR.dialog.add(g,h);};var a=this.path+"dialogs/";c("DCheckbox","dcheckbox",a+"checkbox.js");c("DRadio","dradio",a+"radio.js");c("DTextField","dtextfield",a+"textfield.js");c("DTextArea","dtextarea",a+"textarea.js");c("DateField","datefield",a+"datefield.js");c("UserSelector","userselector",a+"userselector.js");c("DepSelector","depselector",a+"depselector.js");c("PosSelector","posselector",a+"posselector.js");c("DSelect","dselect",a+"select.js");c("DHiddenField","dhiddenfield",a+"hiddenfield.js");c("Ckeditor","ckeditor",a+"ckeditor.js");c("Commoneditor","commoneditor",a+"commoneditor.js");c("Officeeditor","officeeditor",a+"officeeditor.js");c("Fileattach","fileattach",a+"fileattach.js");c("Grid","grid",a+"grid.js");c("GridProperties","gridProperties",a+"grid.js");c("Diccombo","diccombo",a+"diccombo.js");if(b.addMenuItems){b.addMenuItems({dtextfield:{label:d.textfield.title,command:"dtextfield",group:"dtextfield"},dtextarea:{label:d.textarea.title,command:"dtextarea",group:"dtextarea"},dcheckbox:{label:d.checkboxAndRadio.checkboxTitle,command:"dcheckbox",group:"dcheckbox"},dradio:{label:d.checkboxAndRadio.checkboxTitle,command:"dradio",group:"dradio"},userselector:{label:d.userselector.title,command:"userselector",group:"userselector"},datefield:{label:d.datefield.title,command:"datefield",group:"datefield"},depselector:{label:d.depselector.title,command:"depselector",group:"depselector"},posselector:{label:d.posselector.title,command:"posselector",group:"posselector"},dselect:{label:d.dselect.title,command:"dselect",group:"dselect"},dhiddenfield:{label:d.dhiddenfield.title,command:"dhiddenfield",group:"dhiddenfield"},dselect:{label:d.dselect.title,command:"dselect",group:"dselect"},ckeditor:{label:d.ckeditor.title,command:"ckeditor",group:"ckeditor"},officeeditor:{label:d.officeeditor.title,command:"officeeditor",group:"officeeditor"},fileattach:{label:d.fileattach.title,command:"fileattach",group:"fileattach"},grid:{label:d.grid.title,command:"gridProperties",group:"grid"},griddelete:{label:d.griddelete.title,command:"dtableDelete",group:"dtable"},commoneditor:{label:d.commoneditor.title,command:"commoneditor",group:"commoneditor"},diccombo:{label:d.diccombo.title,command:"diccombo",group:"diccombo"}});}if(b.contextMenu){b.contextMenu.addListener(function(e){if(e&&e.hasAscendant("form",true)&&!e.isReadOnly()){return{form:CKEDITOR.TRISTATE_OFF};}});b.contextMenu.addListener(function(g){if(g&&!g.isReadOnly()){var f=g.getName();var e={};if(g.hasAscendant("table",1)&&g.getAscendant("table",1).hasAttribute("isdetail")){e={griddelete:CKEDITOR.TRISTATE_OFF,grid:CKEDITOR.TRISTATE_OFF};}if(f=="select"){e["dselect"]=CKEDITOR.TRISTATE_OFF;return e;}if(f=="textarea"){var h=g.getAttribute("xtype");if(h=="fckeditor"){e["ckeditor"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="officeeditor"){e["officeeditor"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="fileattach"){e["fileattach"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="commoneditor"){e["commoneditor"]=CKEDITOR.TRISTATE_OFF;return e;}else{e["dtextarea"]=CKEDITOR.TRISTATE_OFF;}}}}return e;}if(f=="input"){switch(g.getAttribute("type")){case"button":case"submit":case"reset":e["button"]=CKEDITOR.TRISTATE_OFF;return e;case"checkbox":e["dcheckbox"]=CKEDITOR.TRISTATE_OFF;return e;case"radio":e["dradio"]=CKEDITOR.TRISTATE_OFF;return e;case"hidden":e["dhiddenfield"]=CKEDITOR.TRISTATE_OFF;return e;case"image":e["imagebutton"]=CKEDITOR.TRISTATE_OFF;return e;default:var h=g.getAttribute("xtype");if(h=="userselector"){e["userselector"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="datefield"){e["datefield"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="depselector"){e["depselector"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="posselector"){e["posselector"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h=="diccombo"){e["diccombo"]=CKEDITOR.TRISTATE_OFF;return e;}else{if(h){b.removeMenuItem("textfield");e["dtextfield"]=CKEDITOR.TRISTATE_OFF;return e;}}}}}}e["textfield"]=CKEDITOR.TRISTATE_OFF;return e;}}if(f=="img"&&g.data("cke-real-element-type")=="dhiddenfield"){e["dhiddenfield"]=CKEDITOR.TRISTATE_OFF;return e;}return e;}return null;});}b.on("doubleclick",function(e){var f=e.data.element;if(f.getAttribute("xtype")=="serialnumber"){e.data.dialog="serialnumber";}else{if(f.is("form")){e.data.dialog="form";}else{if(f.is("select")){e.data.dialog="dselect";}else{if(f.is("textarea")){var g=f.getAttribute("xtype");if(g=="fckeditor"){e.data.dialog="ckeditor";}else{if(g=="officeeditor"){e.data.dialog="officeeditor";}else{if(g=="commoneditor"){e.data.dialog="commoneditor";}else{e.data.dialog="dtextarea";}}}}else{if(f.is("img")&&f.data("cke-real-element-type")=="dhiddenfield"){e.data.dialog="dhiddenfield";}else{if(f.is("input")){switch(f.getAttribute("type")){case"button":case"submit":case"reset":e.data.dialog="button";break;case"checkbox":e.data.dialog="dcheckbox";break;case"radio":e.data.dialog="dradio";break;case"image":e.data.dialog="imagebutton";break;default:var g=f.getAttribute("xtype");if(g=="userselector"){e.data.dialog="userselector";}else{if(g=="datefield"){e.data.dialog="datefield";}else{if(g=="diccombo"){e.data.dialog="diccombo";}else{if(g=="depselector"){e.data.dialog="depselector";}else{if(g=="posselector"){e.data.dialog="posselector";}else{if(g){e.data.dialog="dtextfield";}else{e.data.dialog="textfield";}}}}}}break;}}}}}}}});},afterInit:function(b){var a=b.dataProcessor,d=a&&a.htmlFilter,c=a&&a.dataFilter;if(CKEDITOR.env.ie){d&&d.addRules({elements:{input:function(e){var f=e.attributes,g=f.type;if(!g){f.type="text";}if(g=="checkbox"||g=="radio"){f.value=="on"&&delete f.value;}}}});}if(c){c.addRules({elements:{input:function(e){if(e.attributes.type=="hidden"){return b.createFakeParserElement(e,"cke_hidden","dhiddenfield");}}}});}},requires:["image","fakeobjects"]});if(CKEDITOR.env.ie){CKEDITOR.dom.element.prototype.hasAttribute=CKEDITOR.tools.override(CKEDITOR.dom.element.prototype.hasAttribute,function(a){return function(b){var d=this.$.attributes.getNamedItem(b);if(this.getName()=="input"){switch(b){case"class":return this.$.className.length>0;case"checked":return !!this.$.checked;case"value":var c=this.getAttribute("type");return c=="checkbox"||c=="radio"?this.$.value!="on":this.$.value;}}return a.apply(this,arguments);};});}