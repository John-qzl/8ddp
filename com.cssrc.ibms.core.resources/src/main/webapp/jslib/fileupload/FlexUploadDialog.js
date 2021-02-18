Ext.ns("FlexUploadDialog");
FlexUploadDialog = Ext.extend(Ext.Window, {
	constructor : function(j) {
		Ext.applyIf(this, j);
		var b = swfobject;
		var a = "10.0.0";
		var f = __ctxPath + "/jslib/fileupload/playerProductInstall.swf";
		var c = {};
		var d = {};
		d.quality = "high";
		d.bgcolor = "#ffffff";
		d.allowscriptaccess = "sameDomain";
		d.allowfullscreen = "true";
		var e = {};
		e.id = "flexupload";
		e.name = "flexupload";
		e.align = "middle";
		b.embedSWF(__ctxPath + "/jslib/fileupload/flexupload.swf", "flashContent",
				"100%", "100%", a, f, c, d, e);
		b.createCSS("#flashContent", "display:block;text-align:left;");
		var i = '<div id="flashContent"> <p>要查看此页面,确保安装Adobe Flash Player版本10.0.0或更高!</p>';
		var g = ((document.location.protocol == "https:")
				? "https://"
				: "http://");
		i += '<a href="http://www.adobe.com/go/getflashplayer"><img src="'
				+ g
				+ '"www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a>';
		i += "</div>";
		var h = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="100%" id="flexupload">'
				+ '<param name="movie" value="'
				+ __ctxPath
				+ '/jslib/fileupload/flexupload.swf" />'
				+ '<param name="quality" value="high" />'
				+ '<param name="bgcolor" value="#ffffff" />'
				+ '<param name="allowScriptAccess" value="sameDomain" />'
				+ '<param name="allowFullScreen" value="true" />'
				+ '<object type="application/x-shockwave-flash" data="'
				+ __ctxPath
				+ '/jslib/fileupload/flexupload.swf" width="100%" height="100%">'
				+ '<param name="quality" value="high" />'
				+ '<param name="bgcolor" value="#ffffff" />'
				+ '<param name="allowScriptAccess" value="sameDomain" />'
				+ '<param name="allowFullScreen" value="true" />'
				+ "<p>"
				+ "javascript脚本和活跃的内容是不允许运行或没有安装Adobe Flash Player版本10.0.0或更高。"
				+ "</p>"
				+ '<a href="http://www.adobe.com/go/getflashplayer">'
				+ '<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash Player" />'
				+ "</a>" + "</object>" + "</object>";
		FlexUploadDialog.superclass.constructor.call(this, {
					id : "flexUploadDialogWin",
					iconCls : "btn-upload",
					title : "上传文件[flex版]",
					layout : "form",
					width : 500,
					maxHeight : 305,
					modal : true,
					autoScroll : true,
					items : [{
						xtype : "hidden",
						name : "file_cat",
						value : this.file_cat != null
								? this.file_cat
								: "others"
					}, {
						xtype : "hidden",
						name : "fileTypeId",
						value : this.fileTypeId != null ? this.fileTypeId : "0"
					}, {
						xtype : "iframepanel",
						bodyStyle : "margin:0px 0px 0px 0px;height:270%",
						defaultSrc : __ctxPath
								+ "/jslib/fileupload/fileUpload.jsp"
					}]
				});
	}
});