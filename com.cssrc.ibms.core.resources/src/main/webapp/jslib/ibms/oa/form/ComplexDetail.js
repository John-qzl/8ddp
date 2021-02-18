/**
 * 
 */
var ComplexDetail = function() {
	this.AllowetInfo ={};
}
// 属性及函数
ComplexDetail.prototype = {
	/**
	 * 页面初始化,按钮表单的相关function
	 */
	init : function() {
		var _self = this;
		setAlowSetInfo();
		var obj = _self.getRecRightFieldObj();
		if($.isEmpty(obj))
			return;
		$('div.panel-body table tr').each(function(i,e){
			if(i==0) return;
			var tr = $(e);
			
			//判断是否为多个功能按钮
			var ulObj;
			if($('td.rowOps ul',tr).length==0){
				ulObj = $('td.rowOps',tr);
			}else{
				ulObj = $('td.rowOps',tr);
			}
			
			//添加打开方式属性
			var openType = obj.openType;
			var typeAlias;
			//判断按钮是多个还是一个
			var length = $('td.rowOps a.link.complexDetail').length;
			if(length > 0){
				if($('td.rowOps li a.link.complexDetail').length > 0){//多个的情况
					var clickStr =$('td.rowOps li a.link.complexDetail')
						.attr('onclick').replace("openType:'frist',","openType:'"+openType+"',");
					$('td.rowOps li a.link.complexDetail').attr('onclick',clickStr);
					//添加按钮，action添加表单类别属性
					var url = $('td.rowOps li a.link.complexDetail',tr).attr('action');
					typeAlias = _self.getRecType(obj,tr);
					url+='&typeAlias='+typeAlias;
					$('td.rowOps li a.link.complexDetail',tr).attr('action',url);
				}else{//一个的情况
					var clickStr =$('td.rowOps a.link.complexDetail')
						.attr('onclick').replace("openType:'frist',","openType:'"+openType+"',");
					$('td.rowOps a.link.complexDetail').attr('onclick',clickStr);
					//添加按钮，action添加表单类别属性
					var url = $('td.rowOps a.link.complexDetail',tr).attr('action');
					typeAlias = _self.getRecType(obj,tr);
					url+='&typeAlias='+typeAlias;
					$('td.rowOps a.link.complexDetail',tr).attr('action',url);
				}
			}
			
			if(_self.AllowetInfo[typeAlias]){
				_self.addRecRightButton(url,ulObj);
			}
		});		
	},
	//获取关联关系设置信息
	getRecRightFieldObj : function(){
		var recRightFieldVal = $('#recRightField').val();
		if ($.isEmpty(recRightFieldVal) || "  "==recRightFieldVal)
			return null;
		return  $.parseJSON(recRightFieldVal);
	},
	//是否可以进行记录级权限设置的信息
	alowSet : function(data) {
		
	},
	//获取记录对应的类别
	getRecType : function(obj,tr){
		var type = obj.type,recType;
		if(type=='column'){
			var ckey = obj.ckey,relArr = obj.relation;
			var value = $('td[ckey='+ckey+']',tr).text().trim();
			for(var i=0;i<relArr.length;i++){
				var colVal = relArr[i].colVal;
				if(colVal==value){
					recType = relArr[i].recType;
					break;
				}
			}
		}else{
			recType = obj.recType;
		}
		return recType;
	},
	//添加记录权限按钮
	addRecRightButton : function(url,ulObj){
		var actionUrl = url.replace('form/dataTemplate/multiTabView.do','system/recRoleSon/list.do');
		actionUrl = actionUrl.replace('__displayId__','dataTemplateId');
		actionUrl = actionUrl.replace('__pk__','dataId');
		var sb=new StringBuffer();
		sb.append('<li class="ops_li">');
		sb.append('<a class="link setting ops_more" ');
		sb.append(' action="'+actionUrl+'"');
		sb.append(' onclick="__complexDetail__.recRightSet({scope:this,isFull:false})"');
		sb.append(' href="####">');
		sb.append('记录权限设置</a>');
		sb.append('</li>');
		$(ulObj).append(sb.toString());
		var width = ulObj.css('width').replace('px','');
		ulObj.css('width',Number(width)+120);
	},
	/*** 记录级权限设置 */
	recRightSet :function(conf){
		var obj =conf.scope||this; 
		var url=$(obj).attr("action");
		url=url.getNewUrl();
		DialogUtil.open({
			height:800,
			width: 1000,
			url: url,
			showMax: true,                             //是否显示最大化按钮 
		    showToggle: true,                         //窗口收缩折叠
		    showMin: false,
			sucCall:function(rtn){
				//rtn作为回调对象，可进行定制和扩展
				if(!(rtn == undefined || rtn == null || rtn == '')){
					//刷新原来的当前的页面信息
					locationPrarentPage();
				}
			}
		});
	}
}

var __complexDetail__ = new ComplexDetail();// 默认生成一个对象
function setAlowSetInfo(){
	var url = __ctx+"/oa/system/recRole/getAlowSetInfo.do?";
	$.ajax({
		  type: "POST",
	      url:url,
		  data:{},
		  dataType : "text",
	      async:false,
	      success:function(result){
	    	 	var data = jQuery.parseJSON(result);
	    	 	__complexDetail__.AllowetInfo = data;
		   }		  
	});
}
