/**
 * 
 * FieldsManage对象说明：
 * 字段管理
 * 属性：{Fields:字段信息}
 * 方法： 
 * 		1.setFields：设置Fields属性
 * 		2.操作Fields属性的方法（增删改查）:{addField:向Fields中添加单个字段,delField:删除Fields中的字段,updField:更新字段，替换：字段替换，
 * 				isFieldExist：判断Fields种是否有某字段，getFieldByName：根据字段名称取得字段，getFieldIndex：根据字段名获取Fields中的位置索引，
 * 				getFieldByIndex：根据索引取得字段}
 * 		3.辅助方法：{isExecutorSelector：是否选择器，moveField：字段位置移动，getCheckBox：取得复选框Html，getFieldType：取得字段的类型，getHtml：根据Fields生成各字段html，拼成表html
 * 				getFieldItem：根据字段名获取字段html，delStyle：拼字段html时使用}
 */

/**
 * 
 * TableRow对象说明：
 * 表的行操作集合对象
 * 属性： {fieldManage：字段管理，allowEditColName：设置字段名是否允许编辑，isExternal：？，isEdit：行是否可编辑}
 * 方法： {del：行删除，move：行移动，addColumn：新增字段，editField：编辑字段，editNameComment：编辑列名及注释，editFieldOption：编辑字段选项}
 */
FieldsManage=function(){
	{
		this.Fields=[];
	}
	/**
	 * 设置字段
	 */
	this.setFields=function(aryFields){
		this.Fields=aryFields;
	};
		
	/**
	 * 添加字段。
	 */
	this.addField=function(field){
		var rtn=this.isFieldExist(field.fieldName);
		if(rtn) return ;
		this.Fields.push(field);
	};
	
	
	this.delField=function(fieldName){
		for(var i=this.Fields.length-1;i>=0;i--){
			var field=this.Fields[i];
			if(field.fieldName.toLowerCase()==fieldName.toLowerCase()){
				this.Fields.splice(i,1);
			}
		}
	};
	
	/**
	 * 更新字段。
	 */
	this.updField=function(field){
		for(var i=this.Fields.length-1;i>=0;i--){
			var tmp=this.Fields[i];
			if(tmp.fieldName.toLowerCase()==field.fieldName.toLowerCase()){
				var defaults=this.Fields[i];
				field= $.extend({}, defaults, field);
				this.Fields[i]=field;
			}
		}
	};
	
	/**
	 * 替换列字段。
	 * oldFieldName:原来字段名称。
	 * 代替的字段对象。
	 */
	this.replaceByFieldName=function(oldFieldName,field){
		for(var i=0;i<this.Fields.length;i++){
			var tmpField=this.Fields[i];
			if(tmpField.fieldName.toLowerCase()==oldFieldName.toLowerCase()){
				var defaults=this.Fields[i];
				field= $.extend({}, defaults, field);//字段浅拷贝-将defaults和field进行合并，并将合并结果放入{}中
				this.Fields[i]=field;
			};
		};
	};
	
	/**
	 * 判断字段是否存在。
	 */
	this.isFieldExist=function(name){
		for(var i=0;i<this.Fields.length;i++){
			var field=this.Fields[i];
			var fieldName=field.fieldName.toLowerCase();
			name=name.toLowerCase();
			//判断选择控件。（用户，组织，岗位，角色)
			var rtn=this.isExecutorSelector(field.controlType);
			if(rtn){
				var fieldId=fieldName +"id";
				if(fieldName==name || fieldId==name){
					return true;
				}
			}
			else{
				if(fieldName==name){
					return true;
				}
			}
		}
		return false;
	};
	
	/**
	 * 是否选择器。
	 */
	this.isExecutorSelector =function(ctlType){
		ctlType=parseInt(ctlType);
		if(ctlType==4 || ctlType==8 || ctlType==17 || ctlType==5 || ctlType==18 
				|| ctlType==6 || ctlType==19 || ctlType==7)
			return true;
		return false;
	};
	
	/**
	 * 将字段向上或向下移动。
	 */
	this.moveField=function(fieldName,isUp){
		//获取索引
		var idx=this.getFieldIndex(fieldName);
		
		if(idx==-1)
			return false;
		var next=0;
		var canMove=false;
		if(isUp){
			if(idx>0){
				next=idx-1;
				canMove=true;
			}
		}
		else{
			if(idx<this.Fields.length-1){
				next=idx+1;
				canMove=true;
			}
		}
		//交换位置。
		if(	canMove){
			var temp=this.Fields[idx];
			this.Fields[idx]=this.Fields[next];
			this.Fields[next]=temp;
		}
		return canMove;
	};
	/**
	 * 根据字段名获取索引。
	 */
	this.getFieldIndex=function(fieldName){
		for(var i=0;i<this.Fields.length;i++){
			var field=this.Fields[i];
			if(field.fieldName.toLowerCase()==fieldName.toLowerCase()){
				return i;
			}
		}
		return -1;
	};
	
	/**
	 * 根据字段名称取得字段。
	 */
	this.getFieldByName=function(fieldName){
		for(var i=0;i<this.Fields.length;i++){
			var field=this.Fields[i];
			if(field.fieldName.toLowerCase()==fieldName.toLowerCase()){
				return field;
			}
		}
		return null;
	};
	
	/**
	 * 根据索引取得字段。
	 */
	this.getFieldByIndex=function(idx){
		idx=parseInt(idx);
		if(idx>=0 && idx<this.Fields.length){
			return this.Fields[idx];
		}
		return null;
	};
	
	/**
	 * 取得复选框。
	 */
	this.getCheckBox=function(field,optionType){
		return "<input type='checkbox' name='"+optionType+"'  "+((field[optionType]==1)?"checked":"")+"/>";
	};
	
	/**
	 * 取得字段类型。
	 */
	this.getFieldType=function(field){
		var fieldType=field.fieldType;
		if(fieldType=="varchar"){
			fieldType=  fieldType + "(" + field.charLen +")";
		}
		else if(fieldType=="number"){
			var intLen=field.intLen;
			var decimalLen=field.decimalLen;
			if(decimalLen==0){
				fieldType=  fieldType + "(" + intLen +")";
			}
			else{
				fieldType=  fieldType + "(" + intLen +"," + decimalLen+")";
			}
			
		}
		return fieldType;
	};

		
	/**
	 * 获取业务表定义页面的html
	 */
	this.getHtml=function(conf){
		conf=conf || {showDel:true};
		var sb=new StringBuffer();
		for(var i=0;i<this.Fields.length;i++){
			var clsName=(i%2==0)?"odd":"even";
			var field=this.Fields[i];
			var fieldType=this.getFieldType(field);
						//隐藏字段不显示
			if(field.isHidden==1 ) continue;
			var del= ((field.isDeleted==0)?" ":"√"),isDel = ((field.isDeleted==0)?false:true);
			sb.append("<tr fieldName='"+field.fieldName+"' class='");
			sb.append(clsName);
			sb.append("'>");
			sb.append("<td class='editField' name='fieldName' >"+this.delStyle(field.fieldName,isDel)+"</s></td>");
			sb.append("<td class='editField' name='fieldDesc'>"+this.delStyle(field.fieldDesc,isDel)+"</td>");
			sb.append("<td>"+fieldType+"</td>");
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isRequired")  +"</td>");
			sb.append("<td>"+getControlTypeVal(field.controlType)+"</td>");
			sb.append("<td style='text-align:center;' name='Listtd'>"+this.getCheckBox(field,"isList")+"</td>");
			if(fieldType.indexOf('clob')!=-1 ||  isHideFlowVar(field.controlType)){
				sb.append("<td style='text-align:center;'>&nbsp;</td>");
			}else{
				sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isFlowVar") +"</td>");
			}
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isPkShow")+"</td>");
			if(conf.showDel){
				sb.append("<td style='text-align:center;'>"+del+"</td>");
			}
			
	//		sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isReference") +"</td>");
	//		sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"ccisread") +"</td>");
//			if(fieldType.indexOf('number')!=-1){
//				sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isShowComdify")+"</td>");
//			}else{
//				sb.append("<td style='text-align:center;'>&nbsp;</td>");
//			}
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isUnique")+"</td>");
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isMainData")+"</td>");
			//判断是否为外键
			var isfk= ((field.relTableType ==null ||field.relTableType =='')?" ":"√");
			sb.append("<td style='text-align:center;'>"+isfk+"</td>");
			//关系类型
			var relTableType = ''; 
			if(field.relTableType=='0'){relTableType='一对一';}
			if(field.relTableType=='2'){relTableType='多对一';}
			//rel关联表记录删除类型
			var relDelType = ''; 
			if(field.relDelType=='0'){relDelType='取消关联';}
			if(field.relDelType=='1'){relDelType='直接删除';}
			//主表记录删除，rel关联表记录删除类型
			var relDelLMType = ''; 
			if(field.relDelLMType=='0'){relDelLMType='取消关联';}
			if(field.relDelLMType=='1'){relDelLMType='直接删除';}
			//关联关系表
			var relTableId = ((field.relTableId == null ||field.relTableId=='')?" ":field.relTableId);
			sb.append("<td >");
			if(relTableType!=null && relTableType!='' && relTableId!=null && relTableId!=''){
			sb.append('(1)关系类型:<font color=red>'+relTableType+'</font><br>'+'(2)关联关系表:<font color=red>'+relTableId+'</font><br>'+'(3)rel关联表记录删除类型:<font color=red>'+relDelType+'</font><br>'+'(4)主表记录删除，rel关联表记录删除类型:<font color=red>'+relDelLMType+'</font>');
			}
			sb.append("</td>");
			//编辑按钮
			sb.append("<td>" +((field.isDeleted==0)?"<a href='#"+field.fieldName+"' name='editColumn'  >编辑</a>":" ")+"</td>");
			sb.append("</tr>");
		}
		return sb.toString();
	};
	
	
	/**
	 * 获取指定列名的Element
	 */
	this.getFieldItem=function(fieldName,showDel){
		var sb=new StringBuffer();
		for(var i=0;i<this.Fields.length;i++){
			var field=this.Fields[i];
			if(field.fieldName!=fieldName){
				continue;
			}
			var clsName=(i%2==0)?"odd":"even";
			var fieldType=this.getFieldType(field);
			//隐藏字段不显示
			if(field.isHidden==1 ) continue;
			var del= ((field.isDeleted==0)?" ":"√"),isDel = ((field.isDeleted==0)?false:true);
			sb.append("<tr fieldName='"+field.fieldName+"' class='");
			sb.append(clsName);
			sb.append("'>");
			sb.append("<td class='editField' name='fieldName' >"+this.delStyle(field.fieldName,isDel)+"</s></td>");
			sb.append("<td class='editField' name='fieldDesc'>"+this.delStyle(field.fieldDesc,isDel)+"</td>");
			sb.append("<td>"+fieldType+"</td>");
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isRequired")  +"</td>");
			sb.append("<td>"+getControlTypeVal(field.controlType)+"</td>");
			sb.append("<td style='text-align:center;' name='Listtd'>"+this.getCheckBox(field,"isList")+"</td>");
			if(fieldType.indexOf('clob')!=-1 ||  isHideFlowVar(field.controlType)){
				sb.append("<td style='text-align:center;'>&nbsp;</td>");
			}else{
				sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isFlowVar") +"</td>");
			}
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isPkShow")+"</td>");
			if(showDel==1){
				sb.append("<td style='text-align:center;' name='showdel'>"+del+"</td>");
			}
			
	//		sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isReference") +"</td>");
	//		sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"ccisread") +"</td>");
//			if(fieldType.indexOf('number')!=-1){
//				sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isShowComdify")+"</td>");
//			}else{
//				sb.append("<td style='text-align:center;'>&nbsp;</td>");
//			}
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isUnique")+"</td>");
			sb.append("<td style='text-align:center;'>"+this.getCheckBox(field,"isMainData")+"</td>");
			//判断是否为外键
			var isfk= ((field.relTableType ==null ||field.relTableType =='')?" ":"√");
			sb.append("<td style='text-align:center;'>"+isfk+"</td>");
			//关系类型
			var relTableType = ''; 
			if(field.relTableType=='0'){relTableType='一对一';}
			if(field.relTableType=='2'){relTableType='多对一';}
			//rel关联表记录删除类型
			var relDelType = ''; 
			if(field.relDelType=='0'){relDelType='取消关联';}
			if(field.relDelType=='1'){relDelType='直接删除';}
			//主表记录删除，rel关联表记录删除类型
			var relDelLMType = ''; 
			if(field.relDelLMType=='0'){relDelLMType='取消关联';}
			if(field.relDelLMType=='1'){relDelLMType='直接删除';}
			//关联关系表
			var relTableId = ((field.relTableId == null || field.relTableId=='')?" ":field.relTableId);
			sb.append("<td >");
			if(relTableType!=null && relTableType!='' && relTableId!=null && relTableId!=''){
				sb.append('(1)关系类型:<font color=red>'+relTableType+'</font><br>'+'(2)关联关系表:<font color=red>'+relTableId+'</font><br>'+'(3)rel关联表记录删除类型:<font color=red>'+relDelType+'</font><br>'+'(4)主表记录删除，rel关联表记录删除类型:<font color=red>'+relDelLMType+'</font>');
			}
			sb.append("</td>");
			//编辑按钮
			sb.append("<td>" +((field.isDeleted==0)?"<a href='####' name='editColumn'  >编辑</a>":" ")+"</td>");
			sb.append("</tr>");
		}
		return $(sb.toString());
	};
	
	/**
	 * 删除字段
	 */
	this.delStyle = function(name,isDel){
		if(isDel)
			return '<s>'+name+'</s>';
		return name
	};
	
};




var isEdited = false;

/**
 * 表的行操作
 */
if (typeof TableRow == 'undefined') {
	TableRow = {};
}

/**
 * 设置列管理对象。
 * @param fieldManage
 */
TableRow.setFieldManage=function(fieldManage){
	TableRow.fieldManage=fieldManage;
};

/**
 * 设置字段名是否允许编辑
 * @param allowEditColName
 */
TableRow.setAllowEditColName=function(allowEditColName){
	TableRow.allowEditColName=allowEditColName;
};

TableRow.setIsExternal=function(isExternal){
	TableRow.isExternal=isExternal;
};

TableRow.isEdit=false;
/**
 * 是否编辑。
 */
TableRow.setIsEdit=function(_isEdit){
	TableRow.isEdit=_isEdit;
}


/**
 * 删除行
 */
TableRow.del=function(){
	var objTr=$("#tableColumnItem>tbody .over");
	if(objTr.length==0) {
		$.ligerDialog.warn('还没有选中列!','提示');
		return;
	};
	var fieldName=objTr.attr("fieldName");
	var field=TableRow.fieldManage.getFieldByName(fieldName);
	if(field.isRequired){
		$.ligerDialog.warn('该列为必填列,不能删除!','提示');
		return;
	}
	$.ligerDialog.confirm("确定要删除选中列?","提示",function(rtn){
		if(rtn){
			TableRow.fieldManage.delField(fieldName);
			objTr.remove();
		}
	});
};

/**
 * 移动行
 */
TableRow.move=function(direct){
	var objTr=$("#tableColumnItem>tbody .over");
	if(objTr.length==0) return;
	var fieldName=objTr.attr("fieldName");
	var rtn=TableRow.fieldManage.moveField(fieldName,direct);
	if(!rtn) return;
	if(direct){
		var prevObj=objTr.prev();
		objTr.insertBefore(prevObj);
	}
	else{
		var nextObj=objTr.next();
		objTr.insertAfter(nextObj);
	}
};

/**
 * 添加列。
 */
TableRow.addColumn=function(isMain){
	var isEdit=TableRow.isEdit;
	//实现在ColumnDialog.js中,最终页面去向formTableColumnDialog.jsp
	ColumnDialog({isAdd:true,isMain:isMain,fieldManage:TableRow.fieldManage,isExternal:0, callBack:function(field){
		isEdited = true;
		var rtn=TableRow.fieldManage.isFieldExist(field.fieldName);
		if(rtn) return false;
		//把field加到全局数组Fields中。
		TableRow.fieldManage.addField(field);
		//$("#tableColumnItem>tbody").empty();
	    //在页面上显示列数据。
		var newTr = $(TableRow.fieldManage.getFieldItem(field.fieldName,TableRow.allowEditColName?0:1));
		newTr.addClass("newColumn");
		newTr.attr('style','background-color:FFE2D7;')
		$("#tableColumnItem>tbody").append(newTr);
		handisList();
		return true;
	}});
};
/**
 * 类型为主表时，Listtd和Listth显示
 */
handisList=function(){
	var ismain=$("input[name='isMain']:checked").val();
	var objTd=$("td[name='Listtd']");
	var objTh=$("th[name='Listth']");
	if(ismain=='1'){
		objTd.show();
		objTh.show();
	}else{
		objTh.hide();
		objTd.hide();
	}
}

/**
 * 编辑字段
 */
TableRow.editField=function(fieldName,isMain){
	var tmpField=TableRow.fieldManage.getFieldByName(fieldName);
	if(tmpField==null) return;
	//当前编辑的行
	var curTr =$("tr[fieldname="+fieldName+"]","#tableColumnItem>tbody");
	//最初，是否是流程变量
	var isFlowVar = $("[name=isFlowVar]",curTr).data("isFlowVar");
	ColumnDialog({isAdd:false,
		isMain:isMain,
		allowEditColName:TableRow.allowEditColName||curTr.hasClass("newColumn"), 
		fieldManage:TableRow.fieldManage,
		field:tmpField,
		isFlowVar:isFlowVar,
		isExternal:TableRow.isExternal,
		callBack:function(field){
			if(TableRow.fieldManage.isFieldExist(field.fieldName)){
				TableRow.fieldManage.updField(field);
			}
			else{
				TableRow.fieldManage.replaceByFieldName(fieldName,field);
			}
			//$("#tableColumnItem>tbody").empty();
			var showDel=1;
			if(TableRow.allowEditColName || TableRow.isExternal==1){
				showDel=0;
			}
			
			var newTr = $(TableRow.fieldManage.getFieldItem(field.fieldName,showDel));
			if(curTr.length>0){
				$("[name=isFlowVar]",newTr).data("isFlowVar",isFlowVar);
				if(!TableRow.allowEditColName){
					if(isFlowVar){
						$("[name=isFlowVar]:checked",newTr).each(function(){
							var _this = $(this);
							_this.attr("disabled","disabled");
						});
					}
				}
				curTr.replaceWith(newTr);
			}else{
				newTr.data("isFlowVar",isFlowVar);
				$("#tableColumnItem>tbody").append(newTr);
			}
					
			if(tableId!=0){
				isEdited = true;
				$("#tableColumnItem>tbody>tr:[fieldname="+field.fieldName+"]").attr('style','background-color:#FFE2D7;');
			}
			
			handisList();
			return true;
	}});
};

/**
 * 编辑列名及注释
 */
TableRow.editNameComment=function(tdObj){
	var trObj=tdObj.parent();
	var idx=$("#tableColumnItem>tbody>tr").index(trObj);
	
	var field=TableRow.fieldManage.getFieldByIndex(idx);
	var fieldName=tdObj.attr("name");
	//字段名称不允许编辑
	if(!TableRow.allowEditColName && !trObj.hasClass("newColumn")){
		 //field.fieldId为undefined表示新添加的字段，否则为原来的字段。
		 if(fieldName=="fieldName" )
			 return ;
	}
	var hasInput=tdObj.has("input").length==1;
	if(!hasInput){
		var txtObj=$("<input type='text' class='inputText' maxlength='20' size='20' value='"+tdObj.text()+"' />");
		txtObj.blur(function(){
			var tmpObj=$(this);
			var val=tmpObj.val();
			tmpObj.parent().text(val);
			tmpObj.remove();
			field[fieldName]=val;
			if(tableId!=0){
				isEdited = true;
				$("#tableColumnItem>tbody>tr:[fieldname="+field.fieldName+"]").attr('style','background-color:#FFE2D7;');
			}
		});
		tdObj.empty();
		tdObj.append(txtObj);
		txtObj.focus();
	}
};

/**
 * 编辑字段的选项。
 */
TableRow.editFieldOption=function (chkObj){
	var fieldName=chkObj.attr("name");
	var checked=chkObj.attr("checked");
	var trObj=chkObj.parents("tr");
	var idx=$("#tableColumnItem>tbody>tr").index(trObj);
	var field=TableRow.fieldManage.getFieldByIndex(idx);
	field[fieldName]=(checked!=undefined)?1:0;	
};


//根据字段描述生成字段名--重复的方法
function autoGetKey(inputObj){
	var subject=$(inputObj).val();
	if($.trim(subject).length<1) return;
	$.ajax({
		  url: __ctx + '/oa/form/formTable/getFieldKey.do',
		  //async:false,
		  type:'POST',
		  data: ({subject : subject}),
		  success: function(data){
			  var json=eval('('+data+')');
				if(json.result==1 && $.trim($('#fieldName').val()).length<1    ){			
					$('#fieldName').val(json.message);
					validateField().form();
				}
		  }
	});
}
