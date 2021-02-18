/**
 * rel表的权限处理
 * @returns {ReltablePermission}
 */
if (typeof ReltablePermission == 'undefined') {
	ReltablePermission = {};
}

ReltablePermission.init=function(){
	if(!(CustomForm.relFileJsonType)){   //旧版本的
		$("[type='reltable']").each(function(){
			
			var relTable=$(this),
				right=relTable.attr("right");
			if(!right)right="r";
			else
				right=right.toLowerCase();
			//非写权限
			if(right!="w" && right!="b"){
				$("a.add,a.link",relTable).remove();	
				$("input:visible,textarea:visible,select:visible",relTable).each(function(){
					
					var me=$(this);
					var isSelect=me.is("select");    //下拉框标签
					var isCheckboxOrRadio = false;   //单选或者复选
					var type = me.attr("type");  //标签种类
					if(type=="checkbox"||type=="radio"){
						isCheckboxOrRadio = true;
					}
					var val ='';
					var validRule = me.attr("validate");
					if ( validRule != null && validRule != '' && 'undefined' != validRule.toLowerCase() ){
						var json = eval('(' + validRule + ')');		
						if(json.isWebSign){
							val = '<input name="'+me.attr("name")+'" type="hidden" validate="{\'isWebSign\':true}" value="'+me.val()+'" />';
						}				
					}	
					if(isSelect){
						val +=selectValue(me);
					}else if(isCheckboxOrRadio){
						val += "";
					}else{
						val += me.val();
					}
					me.before(val);
					me.remove();
				});
			}
		});	
	}
};


var selectValue=function(select){
	
		var html='';
		var query=select.attr("selectquery")
		var queryJson = "";
		if(query!=undefined && query!=null){
			query=query.replaceAll("'","\"");
			queryJson = JSON2.parse(query);
		}else{
			queryJson = "";
			query = "";
		}
		var sValue=select.attr("val");
		if(sValue==undefined || sValue==null && sValue.length<=0){
			sValue = "";
		}
		html="<span selectvalue="+sValue+" selectquery="+query+"><lable></lable></span>"
		return html;
	}

/**
 * rel表必填判断
 * 
 * @return {} Boolean 如果存在必填数据则返回true，否则则返回false
 */
ReltablePermission.relRequired = function() {
	var r = true;
	var relDiv = $("div[type='relTable']");
	if( $.isEmptyObject(relDiv)||relDiv.length == 0)
		return true;
	if ($.isEmpty(relDiv) )
		return true;

	// rel表的div
	relDiv.each(function() {	
		var visible = $(this).is(":visible");
		if(!visible)return true;
		var right = $(this).attr("right");
		if ($.isEmpty(right))
			right = "r";
		else
			right = right.toLowerCase();
		// 如果必填权限
		if (right != "b")
			return true;
		var tr = $(this).find('[formtype]:visible');
		if ($.isEmpty(tr) || $.isEmptyObject(tr) || tr.length == 0) {
			r = false;
			if(!($(this).hasClass('validError'))){		//去掉必填样式							
				$(this).addClass('validError');
			}
			return false;
		}
		
		//是不是每行都要有数据
		/*
		var t = false;
		// 判断里面的值是否为空，
		tr.each(function() {
			// 判断rel表数据
			t = ReltablePermission.checkRelData($(this));
			if (t)//如果存在数据 则终止循环
				return false;
		});
		if (!t) {
			r = false;
			return false;
		}		
		*/
			
		});
	
  /*	if(!r) relDiv.addClass('validError');
    else relDiv.removeClass('validError');*/
	return r;
};

/**
 * 检查rel表是否存在数据
 * 
 * @param {}
 *            objRow rel表的table
 * @return {} 如果存在数据则返回true，否则则返回false
 */
ReltablePermission.checkRelData = function(objRow) {
	var t = false;
	$("input:text[name^='r:'],input[type='hidden'][name^='r:'],textarea[name^='r:'],select[name^='r:']",
			objRow).each(function() {
				var value = $(this).val();
				// 如果存在值 则终止循环
				if (!$.isEmpty(value)) {
					t = true;
					return false;
				}
			});
	// 判断复选框按钮的数据是否有值
	$('input:checkbox:checked', objRow).each(function() {
				var value = $(this).val();
				// 如果存在值 则终止循环
				if (!$.isEmpty(value)) {
					t = true;
					return false;
				}
			});
	// 判断单选按钮的数据是否有值
	$('input:radio', objRow).each(function() {
				var value = $(this).val();
				// 如果存在值 则终止循环
				if ($(this).attr("checked") != undefined) {
					t = true;
					return false;
				}
			});
	return t;
};

