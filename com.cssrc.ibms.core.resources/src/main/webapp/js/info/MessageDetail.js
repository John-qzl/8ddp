Ext.ns("MessageDetail");MessageDetail=Ext.extend(Ext.Window,{constructor:function(a){Ext.apply(this,a);this.initComponents();MessageDetail.superclass.constructor.call(this,{title:"个人短信详情",iconCls:"menu-mail_box",height:200,id:"MessageDetail",width:350,modal:true,layout:"fit",buttonAlign:"center",items:this.displayPanel,buttons:this.buttons});},initComponents:function(){this.displayPanel=new Ext.Panel({border:false,autoLoad:{url:__ctxPath+"/pages/info/messagedetail.jsp?receiveId="+this.receiveId+"&isSend="+this.isSend}});this.buttons=[{text:"上一条",iconCls:"btn-previous-message",scope:this,handler:this.previous},{text:"下一条",iconCls:"btn-next-message",scope:this,handler:this.next},{text:"回复",iconCls:"btn-mail_reply",scope:this,handler:this.reply,hidden:this.isSend},{text:"重发",iconCls:"btn-mail_reply",scope:this,handler:this.reSend,hidden:this.isSend?false:true},{text:"关闭",iconCls:"btn-del",scope:this,handler:function(){this.close();}}];},previous:function(){var c=document.getElementById("__haveNextMessageFlag");if(c!=null&&c.value!="endPre"){var b=curUserInfo.userId;var a=document.getElementById("__curReceiveId").value;if(this.isSend){this.displayPanel.load({url:__ctxPath+"/pages/info/messagedetail.jsp?opt=_pre&receiveId="+a+"&userId="+b+"&isSend=true",scripts:true});}else{this.displayPanel.load({url:__ctxPath+"/pages/info/messagedetail.jsp?opt=_pre&receiveId="+a+"&userId="+b+"&isSend=false",scripts:true});}}else{Ext.ux.Toast.msg("提示信息","这里已经是第一条了");}},next:function(){var c=document.getElementById("__haveNextMessageFlag");if(c!=null&&c.value!="endNext"){var b=curUserInfo.userId;var a=document.getElementById("__curReceiveId").value;if(this.isSend){this.displayPanel.load({url:__ctxPath+"/pages/info/messagedetail.jsp?opt=_next&receiveId="+a+"&userId="+b+"&isSend=true",scripts:true});}else{this.displayPanel.load({url:__ctxPath+"/pages/info/messagedetail.jsp?opt=_next&receiveId="+a+"&userId="+b+"&isSend=false",scripts:true});}}else{Ext.ux.Toast.msg("提示信息","这里已经是最后一条了.");}},reply:function(){var c=document.getElementById("__ReplyFlag").value;var d=document.getElementById("__SENDID").value;var b=document.getElementById("__SENDERNAME").value;if(c==1){var a=Ext.getCmp("ReplyWindow");if(a!=null){a.close();new ReMessageWin({id:d,name:b}).show();}else{new ReMessageWin({id:d,name:b}).show();}this.close();}else{Ext.ux.Toast.msg("提示信息","系统信息不能回复.");}},reSend:function(){Ext.Ajax.request({scope:this,url:__ctxPath+"/info/sendShortMessage.do",params:{userId:this.userId,content:this.content},method:"post",success:function(){Ext.ux.Toast.msg("操作信息","重发成功！");if(this.callback){this.callback.call(this.scope);}this.close();}});}});