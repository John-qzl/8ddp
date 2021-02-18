function setCheckBoxParentCilck() {
	$(":checkbox[name='defaultSign']").each(function(i, ck) {
		$(ck).parent().on("click", function() {
			setDefaultSign(ck);
		});
	})
}

function addSign(userId){
	SignAddModel({"userId":userId});
}

function setDefaultSign(obj,userId){
	if ($(obj).attr("checked")) {
		$(":checkbox[value!=" + $(obj).val() + "]").attr("checked", false);
		var url=__ctx+"/oa/system/sign/default/"+$(obj).val()+"/yes.do?userId="+userId;
		$.post(url);
	}else{
		var url=__ctx+"/oa/system/sign/default/"+$(obj).val()+"/not.do?userId="+userId;
		$.post(url);
	}
}

function editSign(id){
	SignAddModel({"id":id});
}
function SignAddModel(conf){
	var dialogWidth=900;
	var dialogHeight=700;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	var winArgs="dialogWidth="+conf.dialogWidth+"px;dialogHeight="+conf.dialogHeight
		+"px;help=" + conf.help +";status=" + conf.status +";scroll=" + conf.scroll +";center=" +conf.center;
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/oa/system/sign/edit.do?isSingle='+ conf.isSingle;
	url=url.getNewUrl();
	if(conf.userId){
		url+="&userId="+conf.userId;
	}else if(conf.id){
		url+="&id="+conf.id;
	}
	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '添加印章模型',
        url: url, 
        userId:conf.userId,
        isResize: true,
        reload:function(rtn){
        	var url=location.href;
        	if(url.indexOf("signmodel")>-1){
        		location.href=url;
        	}else{
            	location.href=url+"&tabid=signmodel";
        	}
        }
    });	
}