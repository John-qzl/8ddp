CKEDITOR.plugins.add("serialnumber",{init:function(a){var b="serialnumber";CKEDITOR.dialog.add(b,this.path+"dialog/serialnumber.js");var c=a.addCommand(b,new CKEDITOR.dialogCommand(b));a.ui.addButton("SerialNumber",{label:a.lang.serialNumber,command:b,icon:this.path+"images/serialnumber.gif"});}});