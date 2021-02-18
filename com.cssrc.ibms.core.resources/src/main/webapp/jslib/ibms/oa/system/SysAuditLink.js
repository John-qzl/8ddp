if (typeof SysAuditLink == 'undefined') {
	SysAuditLink = {};
}
SysAuditLink.initLink=function(){
$("body").delegate("a[hrefLink]","click",function(){
		var hrefLink=$(this).attr("hrefLink");
		if(!hrefLink){
			return;
		}
		var url=hrefLink+"&canReturn=1";
		
		DialogUtil.open({
			height: 600,
			width: 800,
			title : '',
			url: url, 
			isResize: true
		});
})

}