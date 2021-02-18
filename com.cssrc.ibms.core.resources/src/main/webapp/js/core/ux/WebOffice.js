WebOffice = function(a) {
	this.id = a == null ? "WebOfficeObj" : this.id;
	this.htmlObj = "<object id='"
			+ this.id
			+ "' height='100%' width='100%' style='LEFT: 0px; TOP: 0px'  classid='clsid:E77E049B-23FC-4DB8-B756-60529A35FAD5' codebase='"
			+ __ctxPath + "/js/core/weboffice/HWPostil_V3074.cab#V6,0,4,2'>";
	this.htmlObj += "</object>";
	window.onUnload = function() {
		try {
			this.getDocObject().Close();
		} catch (b) {
		}
	};
};
WebOffice.prototype.openDoc = function(c, a) {
	var b = this.getDocObject();
	b.LoadOriginalFile(c, "doc");
};
WebOffice.prototype.saveDoc = function(config) {
	Ext.applyIf(this, config || {});
	var webOffice = this.getDocObject();
	webOffice.HttpInit();
	webOffice.HttpAddPostString("file_cat", this.fileCat);
	if (this.docPath != null && this.docPath != "") {
		webOffice.HttpAddPostString("file_path", this.docPath);
	}
	webOffice.HttpAddPostCurrFile("AipFile ", "");
	returnValue = webOffice.HttpPost(this.url);
	var obj;
	eval("obj=" + returnValue + ";");
	return obj;
};
WebOffice.prototype.close = function() {
	try {
		this.getDocObject().Close();
	} catch (a) {
	}
};
WebOffice.prototype.getDocObject = function() {
	return document.getElementById(this.id);
};