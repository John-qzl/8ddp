
/**
 * 控件类型。 16是隐藏域 4 用户单选,8,用户多选, 17,角色单选,5,角色多选, 18,组织单选,6,组织多选 19,岗位单选,7,岗位多选,23,外键控件,25,数字范围控件
 * 
 */
var controlList = [{
	key : '1',
	value : '单行文本框'
}, {
	key : '15',
	value : '日期控件'
}, {
	key : '3',
	value : '数据字典'
}, {
	key : '11',
	value : '下拉单选项'
}, {
	key : '4',
	value : '人员选择器(单选)'
},  {
	key : '17',
	value : '角色选择器(单选)'
},  {
	key : '18',
	value : '组织选择器(单选)'
},  {
	key : '19',
	value : '岗位选择器(单选)'
},  {
	key : '23',
	value : '外键控件'
},  {
	key : '25',
	value : '数字范围控件'
}];
/**
 * 数据模板构造方法
 */
var PendingSetting = function() {

}
// 属性及函数
PendingSetting.prototype = {
	/**
	 * 初始化
	 */
	init : function() {
		var _self = this;
		this.initDisplaySetting();
		this.initConditionField();
		this.handlerCheckAll();
		this.selectTr();
		// 绑定选择条件点击事件
		$("#selectCondition").click(function() {
			_self.selectCondition(_self)
		});
		
	},
	initDisplaySetting : function() {

		var tbl = '#displayFieldTbl';
		//权限下拉框事件
		this.handChange(tbl);
		//人员角色a标签点击事件
		this.handClick(tbl);
		
		var displayFieldVal = $("#displayField").val();
		if ($.isEmpty(displayFieldVal)){
			return;
		}
		var displayField = $.parseJSON(displayFieldVal);
		var sb = new StringBuffer();
		for (var i = 0, c; c = displayField[i++];) {
			var tr = $($("#displayFieldTemplate .table-detail tr")[0]).clone(true,
					true);
			$("td[var='index']", tr).html(i);
			$("input[var='style']", tr).attr("value", c.style);
			$("input[var='controltype']", tr).attr("value", c.controltype);
			$("td[var='name']", tr).html(c.name);
			$("input[var='settype']", tr).attr("value", c.settype);
			$("input[var='desc']", tr).attr("value", c.desc);
			var rightHtml = this.getHtmlTd(c.right.type, c.right.id, c.right.name, c.right.script);
			$("[var='fieldRight']", tr).html(rightHtml);
			
			sb.append("<tr var='displayFieldTr'>"+tr.html()+"</tr>");
		}
		$(tbl).append(sb.toString());
	},

	/**
	 * 设置每行的显示的权限
	 * 
	 * @param v
	 *            权限简称
	 * @param full
	 *            权限全称
	 */
	getHtmlTd : function(v, rightId, rightName, rightScript) {
		var aryTd = [
				'<select name="displayFieldRight" class="change_right" var="right"  >',
				'<option value="none" '
						+ (v == 'none' ? 'selected="selected"' : '')
						+ ' >无</option>',
				'<option value="everyone"'
						+ (v == 'everyone' ? 'selected="selected"' : '')
						+ '>所有人</option>',
				'<option value="user"'
						+ (v == 'user' ? 'selected="selected"' : '')
						+ '>用户</option>',
				'<option value="role"'
						+ (v == 'role' ? 'selected="selected"' : '')
						+ '>角色</option>',
				'<option value="org"'
						+ (v == 'org' ? 'selected="selected"' : '')
						+ '>组织</option>',
				'<option value="orgMgr"'
						+ (v == 'orgMgr' ? 'selected="selected"' : '')
						+ '>组织负责人</option>',
				'<option value="pos"'
						+ (v == 'pos' ? 'selected="selected"' : '')
						+ '>岗位</option>',
				'<option value="script"'
						+ (v == 'script' ? 'selected="selected"' : '')
						+ '>脚本</option>',
				'</select>',
				'<span name="displayFieldRight_span"  '
						+ (v == 'script' || v == 'none' || v == 'everyone'
								? 'style="display: none;" '
								: '') + '>',
				'<input type="hidden"  var="rightId"  value="' + rightId + '">',
				'<textarea  readonly="readonly" var="rightName"  cols="20" rows="2">'
						+ rightName + '</textarea>',
				'<a class="link-get" href="####"><span class="link-btn">选择</span></a>',
				'</span>',
				'<span class="displayFieldRight_script_span" '
						+ (v != 'script' ? 'style="display: none;" ' : '')
						+ '>',
				'<textarea  cols="20" rows="2"  var="rightScript" >'
						+ rightScript + '</textarea>',
				'<a  href="####" name="btnScript" class="link var" title="常用脚本" onclick="__PendingSetting__.selectScript(this,false)">常用脚本</a>',
				'</span>'];
		return aryTd.join('');
	},
	/**
	 * 选中行或反选
	 * 
	 * @return void
	 */
	selectTr : function() {
		$("tr.odd,tr.even").each(function() {
			$(this).bind("mousedown", function(event) {
				if (event.target.tagName == "TD") {
					var strFilter = 'input:checkbox[class="pk"],input:radio[class="pk"]';
					var obj = $(this).find(strFilter);
					if (obj.length == 1) {
						var state = obj.attr("checked");
						obj.attr("checked", !state);
					}
				}
			});
		});
	},
	/**
	 * 处理选择改变
	 */
	handChange : function(obj) {
		var _self = this;
		$(obj).delegate("select.change_right", "change", function() {
			var me = $(this), spanObj = me.next(), nextSpanObj = spanObj.next();
			_self.showSpan(me.val(), spanObj);
			var txtObj = $("textarea", spanObj), idObj = $("input:hidden",
					spanObj);
			txtObj.val("");
			idObj.val("");
			var nextTxtObj = $("textarea", nextSpanObj);
			nextTxtObj.val("");
		});
	},
	
	/**
	 * 显示权限的span
	 */
	showSpan : function(permissionType, spanObj) {
		switch (permissionType) {
			case "user" :
			case "role" :
			case "org" :
			case "orgMgr" :
			case "pos" :
				spanObj.show();
				spanObj.next().hide();
				break;
			case "script" :
				spanObj.hide();
				spanObj.next().show();
				break;
			case "everyone" :
			case "none" :
				spanObj.hide();
				spanObj.next().hide();
				break;
		}
	},
	/**
	 * 处理选择
	 */
	handClick : function(obj) {
		$(obj).delegate("a.link-get", "click", function() {
			var me = $(this), txtObj = me.prev(), idObj = txtObj.prev(), selObj = me
					.parent().prev(), selType = selObj.val();

			// 选择回调
			var callback = function(ids, names) {
				txtObj.val(names);
				idObj.val(ids);
			};

			switch (selType) {
				case "user" :
					UserDialog({
						callback : callback
					});
					break;
				case "role" :
					RoleDialog({
						callback : callback
					});
					break;
				case "org" :
				case "orgMgr" :
					OrgDialog({
						callback : callback
					});
					break;
				case "pos" :
					PosDialog({
						callback : callback
					});
					break;
			}
		});
	},
	
	// =====================查询条件==============================================================================
	/**
	 * 初始化条件字段
	 */
	initConditionField : function() {
		var conditionFieldVal = $("#conditionField").val();
		
		if ($.isEmpty(conditionFieldVal)){
			return;
		}
		var conditionField = $.parseJSON(conditionFieldVal);
		for (var i = 0, c; c = conditionField[i++];) {
			//初始化时将修改的日期格式更新进来
			$("#condition-columnsTbl input:[name='select'][fieldtype='date']").each(function() {
				
				var me = $(this), na = me.attr("fieldname"), ftm = me.attr("fieldfmt");
				if(na == c.na){
					var ppt;
					if(ftm!=undefined&&ftm!=null&&ftm!=""){
						var qq = $.parseJSON(ftm);
						for(var x in qq){
							if(x=="format")
								ppt = qq[x];
						}
					}
					c.ftm = ppt;
					
				}
				
			});
			var tr = this.constructConditionTr(c);
			$("#conditionTbl tbody").append(tr);
		}
	},
	/**
	 * 选择条件
	 * 
	 */
	selectCondition : function(_self) {

		var conditionType = $("input[name='conditionType']:checked").val();
		$("#condition-columnsTbl input:[name='select']:checked")
				.each(function() {
							var me = $(this), na = me.attr("fieldname"), ty = me
									.attr("fieldtype"), cm = me
									.attr("fielddesc"), ct = me
									.attr("controltype"), ftm = me
									.attr("fieldfmt"), settype = me
									.attr("settype"), obj = $("#conditionTbl");
					var ppt;
					if(ftm!=undefined&&ftm!=null&&ftm!=""){
						var qq = $.parseJSON(ftm);
						for(var x in qq){
							if(x=="format")
								ppt = qq[x];
						}
					}
				if(_self.isExistName(obj,'name',na)){
					var condition = {
						na : na,
						ty : ty,
						op : 1,
						cm : cm,
						va : "",
						vf : 1,
						ct : ct,
						ftm : ftm,
						settype:settype,
						qt : _self.getQueryType(ty, 1)
					};
					var tr = _self.constructConditionTr(condition);
					$("#conditionTbl tbody").append(tr);
				}
				});

	},
	/**
	 * 构造条件的列
	 * 
	 * @param {}
	 *            condition
	 * @return {}
	 */
	constructConditionTr : function(condition) {

		var ty = condition.ty,ftm = condition.ftm, na = condition.na, cm = condition.cm, op = condition.op, va = condition.va, vf = condition.vf;
		// 控件类型
		ct = condition.ct;
		// 查询类型
		qt = condition.qt;
		// 名称
		var naTd = this.constructTag("td", {"var":"name"}, na);

		var hiddenInput = this.constructTag("input", {
			type : "hidden",
			value : JSON2.stringify(condition)
		});
		naTd.append(hiddenInput);
		// 注解
		var cmTd = this.constructTag("td");
		var cmInput = this.constructTag("input", {
			name : "cm",
			value : cm,
			type : "text",
			'class' : "inputText"
		});
		cmTd.append(cmInput);

		var ctTd = this.constructTag("td");
		var ctSelect = this.constructTag("select", {
			name : "ct",
			style : "width:70px;"
		});
		ctSelect = this.getCtSelect(ctSelect, ct, ty);
		ctTd.append(ctSelect);

		// 条件
		var opTd = this.constructTag("td");
		var opSelect = this.constructTag("select", {
			name : "op",
			style : "width:70px;"
		});
		
		if(ct == '28'){//对于下拉多选项做特殊处理
			opSelect.append(this.constructOption(op, "7", "in"));
			opSelect.append(this.constructOption(op, "8", "not in"));
		}else{
			opSelect = this.getOpSelect(opSelect, op, ty);
		}

		opTd.append(opSelect);
		// 值来源
		var vfTd = this.constructTag("td");
		var vfSelect = this.constructTag("select", {
			name : "vf",
			onchange : "__PendingSetting__.selectValueFrom(this)"
		});
		vfSelect.append(this.constructOption(vf, 1, "表单输入"));
		// vfSelect.append(this.constructOption(vf, 2, "固定值"));
		// vfSelect.append(this.constructOption(vf, 3, "脚本"));
		// vfSelect.append(this.constructOption(vf, 4, "变量"));
		vfTd.append(vfSelect);
		// 值
		var vaTd = this.constructTag("td");
		var a = this.constructTag("a", {
			href : '#',
			name : 'btnScript',
			'class' : 'hide link var',
			title : '常用脚本',
			onclick : "__PendingSetting__.selectScript(this,true)"
		}, "常用脚本");
		var vaInput = {};
		if (vf == 1) {
			vaInput = this.constructTag("input", {
				name : "va",
				type : "text",
				'class' : "hide",
				readonly : "readonly"
			});
		} else if (vf == 2) {
			vaInput = this.constructTag("input", {
				name : 'va',
				type : 'text'
			});
		} else if (vf == 3) {
			vaInput = this.constructTag("textarea", {
				name : 'va'
			});
			a.show();
		} else {
			vaInput = this.constructTag("select", {
				name : "va"
			});
			var parameters = this.getParameters();
			for (var i = 0; i < parameters.length; i++) {
				p = parameters[i];
				vaInput.append(this.constructTag("option", {
					value : p.na
				}, p.cm + "(" + p.na + ")"));
			}
		}
		vaInput.val(va);
		vaTd.append(a);
		vaTd.append(vaInput);
		// 管理
		var manageTd = this.constructTag("td");
		var moveupA = this.constructTag("a", {
			href : "####",
			'class' : "link moveup",
			onclick : '__PendingSetting__.moveTr(this,true)'
		}, "");

		var movedownA = this.constructTag("a", {
			href : "####",
			'class' : "link movedown",
			onclick : '__PendingSetting__.moveTr(this,false)'
		}, "");
		var deleteA = this.constructTag("a", {
			href : "####",
			'class' : "link del",
			onclick : '__PendingSetting__.delTr(this)'
		}, "");

		manageTd.append(moveupA).append(movedownA).append(deleteA);

		var tr = this.constructTag("tr");
		tr.append(naTd).append(cmTd).append(ctTd).append(opTd)
				.append(vfTd).append(vaTd).append(manageTd);
		return tr;
	},
	// TODO
	getCtSelect : function(ctSelect, ct, ty) {
		var _self = this;
		if(ct == '28'){//控件类型为下拉多选项时只显示下拉多选项控件
			ctSelect.append(_self.constructOption(ct, '28', '下拉框'));
		}else if(ty == 'number'){
			ctSelect.append(_self.constructOption(ct, '25', '数字范围控件'));
		}else if (ty == 'date') {
			ctSelect.append(_self.constructOption(ct, '15', '日期控件'));
		} else {
			$(controlList).each(function(i, d) {
				switch (d.key) {
					case '1' :
					case '2' :
					case '9' :
					case '10' :
					case '16' :
					case '21' :
						ctSelect
								.append(_self.constructOption(ct, '1', d.value));
						break;
					default :
						ctSelect.append(_self.constructOption(ct, d.key,
								d.value));
						break;
				}
			});
		}
		return ctSelect;
	},
	getOpSelect : function(opSelect, op, ty) {
		switch (ty) {
			case 'varchar' :
				opSelect.append(this.constructOption(op, "1", "="));
				opSelect.append(this.constructOption(op, "2", "!="));
				opSelect.append(this.constructOption(op, "3", "like"));
				//opSelect.append(this.constructOption(op, "4", "左like"));
				//opSelect.append(this.constructOption(op, "5", "右like"));
				opSelect.append(this.constructOption(op, "6", "in"));
				break;
			case 'number' :
				opSelect.append(this.constructOption(op, "7", "数字范围控件"));
				opSelect.append(this.constructOption(op, "1", "="));
				opSelect.append(this.constructOption(op, "2", ">"));
				opSelect.append(this.constructOption(op, "3", "<"));
				opSelect.append(this.constructOption(op, "4", ">="));
				opSelect.append(this.constructOption(op, "5", "<="));
				break;
			case 'int' :
				opSelect.append(this.constructOption(op, "1", "="));
				opSelect.append(this.constructOption(op, "2", ">"));
				opSelect.append(this.constructOption(op, "3", "<"));
				opSelect.append(this.constructOption(op, "4", ">="));
				opSelect.append(this.constructOption(op, "5", "<="));
				break;
			case 'date' :
				opSelect.append(this.constructOption(op, "6", "日期范围"));
				opSelect.append(this.constructOption(op, "1", "="));
				opSelect.append(this.constructOption(op, "2", ">"));
				opSelect.append(this.constructOption(op, "3", "<"));
				opSelect.append(this.constructOption(op, "4", ">="));
				opSelect.append(this.constructOption(op, "5", "<="));
				break;
			default :
				opSelect.append(this.constructOption(op, "1", "="));
				opSelect.append(this.constructOption(op, "2", ">"));
				opSelect.append(this.constructOption(op, "3", "<"));
				opSelect.append(this.constructOption(op, "4", ">="));
				opSelect.append(this.constructOption(op, "5", "<="));
		}
		return opSelect;
	},
	/**
	 * 构造选项
	 */
	constructOption : function(cond1, cond2, text) {
		if (!text)
			text = cond2;
		var option = this.constructTag("option", {
			value : cond2
		}, text);
		if (cond1 == cond2)
			option.attr("selected", "selected");
		return option;
	},

	/**
	 * 值来源更改事件处理
	 */
	selectValueFrom : function(obj) {
		var tr = $(obj).closest("tr");
		var a = tr.find("a:[name='btnScript']");
		a.hide();
		var vf = $(obj).val();
		var valueInput;
		if (vf == 1) {
			valueInput = this.constructTag("input", {
				name : 'va',
				type : "text",
				'class' : "hide",
				readonly : "readonly"
			});
		} else if (vf == 2) {
			valueInput = this.constructTag("input", {
				name : 'va',
				type : "text"
			});
		} else if (vf == 3) {
			a.show();
			valueInput = this.constructTag("textarea", {
				name : "va"
			});
		} else {
			valueInput = this.constructTag("select", {
				name : "va"
			});
			var parameters = this.getParameters();
			for (var i = 0; i < parameters.length; i++) {
				var p = parameters[i];
				valueInput.append(this.constructTag("option", {
					value : p.na
				}, p.cm + "(" + p.na + ")"));
			}
		}
		tr.find("[name='va']").replaceWith(valueInput);
	},
	/**
	 * 取得参数设置
	 */
	getParameterSetting : function() {
		var setting = {
			fieldSetting : new Array(),
			conditionField : new Array(),
			parameters : new Array()
		};
		return setting;
	},
	/**
	 * 构造标签
	 */
	constructTag : function(name, props, text) {
		var tag = $("<" + name + "></" + name + ">");
		if (props) {
			for (var key in props) {
				tag.attr(key, props[key]);
			}
		}
		if (text) {
			tag.text(text);
		}
		return tag;
	},
	/**
	 * 返回查询条件
	 * 
	 * @param {}
	 *            type
	 * @param {}
	 *            op
	 */
	getQueryType : function(type, op) {
		var qt = "S";
		switch (type) {
			case 'varchar' :
				if (op) {
					switch (op) {
						case 1 :
						case 2 :
							qt = 'S';
							break
						case 3 :
							qt = 'SL';
							break
						case 4 :
							qt = 'SLL';
							break
						case 5 :
							qt = 'SLR';
							break
						default :
							qt = 'S';
							break
					}
				}
				break;
			case 'number' :
				/*qt = 'L';
				break;*/
				if (op == 7)
					qt = 'NR';
				else 
					qt = 'NL';
				break;
			case 'int' :
				qt = 'N';
				break;
			case 'date' :
				if (op == 6)
					qt = 'DR';
				else 
					qt = 'DL';
				break;
			default :
				qt = 'S';
				break;
		}
		return qt;
	},
	
	
	
	// =====================通用的处理方法==============================================================================
	/**
	 * 切换分页
	 */
	switchNeedPage : function(obj) {
		var me = $(obj), pagSize = $("#spanPageSize");
		var isNeedPage = me.val();
		if (isNeedPage == 1) {
			pagSize.show();
		} else {
			pagSize.hide();
		}
	},
	/**
	 * 删除TR
	 */
	delTr : function(obj) {
		$(obj).closest("tr").remove();
	},
	/**
	 * 删除选择的tr
	 * 
	 * @param {}
	 *            obj 选择的tr的ID
	 */
	delSelectTr : function(obj) {
		var _self = this;
		$("#" + obj + " input:[name='select']:checked").each(function() {
			_self.delTr(this);
		});
	},
	/**
	 * 上下移动
	 * 
	 * @param {}
	 *            obj
	 * @param {}
	 *            isUp 是否上移
	 */
	moveTr : function(obj, isUp) {
		var thisTr = $(obj).parents("tr");
		if (isUp) {
			var prevTr = $(thisTr).prev();
			if (prevTr) {
				thisTr.insertBefore(prevTr);
			}
		} else {
			var nextTr = $(thisTr).next();
			if (nextTr) {
				thisTr.insertAfter(nextTr);
			}
		}
	},
	
	/**
	 * 处理选择字段赋值
	 * 
	 * @param {}
	 *            obj
	 */
	handSelect : function(obj) {
		$(obj).delegate("select[var='name']", "change", function() {
			var me = $(this), text = me.children('option:selected').text();
			tr = me.closest("tr");
			var desc = $("[var='desc']", tr);
			desc.val(text);
		});
	},
	/**
	 * 显示类型处理
	 * @param {} obj
	 */
	displayType: function(obj){
		var me = $(obj);
		var value = me.children('option:selected').val();
		var clickObj = me.parents("tr").find("[var=onclick]");
		var actionObj = me.parents("tr").find("[var=action]");
		var urlParamsObj = me.parents("tr").find("[var=setUrlParams]");
		if(value=="simple"){
			clickObj.attr("readonly","readonly");
			actionObj.attr("readonly","readonly");
			clickObj.val("");
			actionObj.val("");
			urlParamsObj.text();
		}else if( value == "hyperlink"){
			clickObj.removeAttr("readonly");
			actionObj.removeAttr("readonly");
			clickObj.val("displayTypeClick({scope:this,type:'hyperlink'})");
			actionObj.val("");
			urlParamsObj.text();
		}else if(value == "processWatch"){
			clickObj.removeAttr("readonly");
			actionObj.removeAttr("readonly");
			clickObj.val("displayTypeClick({scope:this,type:'processWatch'})");
			actionObj.val("/ibms/oa/form/dataTemplate/processView.do?__displayId__=[displayId]&defId=[defId]&__pk__=[pk]");
			urlParamsObj.text();
		}else if(value == "detail"){
			clickObj.removeAttr("readonly");
			actionObj.removeAttr("readonly");
			clickObj.val("displayTypeClick({scope:this,type:'detail'})");
			actionObj.val("/ibms/oa/form/dataTemplate/detailData.do?__displayId__=[displayId]&__pk__=[pk]");
			urlParamsObj.text();
		}else{
			
		}		
	},
	
	/**
	 * 改value的Td
	 */
	changeTd : function(obj) {
		if (typeof $(obj).attr("checked") == 'undefined') {
			this.addSelectTd(obj);
		} else {
			this.addTextTd(obj);
		}

	},
	/**
	 * 判断选择是否存在
	 */
	isExistName : function(obj,o,name) {
		var rtn = true;
		obj.find("[var='"+o+"']").each(function() {
			var me = $(this), n = me.html();
			if (n == name) {
				rtn = false;
				return true;
			}
		});
		return rtn;
	},
	/**
	 * 改变为下拉框
	 */
	addSelectTd : function(obj) {
		var selectTemplate = $("#selectField").clone(true, true);
		var $tr = $(obj).closest("tr");
		$("td[var='valueTd']", $tr).html(selectTemplate);
	},
	/**
	 * 改变为文本框
	 */
	addTextTd : function(obj) {
		var valueTemplate = $("#valueTemplate").clone(true, true);
		var $tr = $(obj).closest("tr");
		$("td[var='valueTd']", $tr).html(valueTemplate);
	},
	handlerCheckAll : function() {
		var _self = this;
		$("#chkall").click(function(){
			var state=$(this).attr("checked");
			if(state==undefined)
				_self.checkAll(false);
			else
				_self.checkAll(true);
		});
	},
	checkAll : function(checked) {
		$("input[type='checkbox'][class='pk']").each(function(){
			$(this).attr("checked", checked);
		});
	},
	getDisplayField : function() {

		var json = [];
		var _self=this;
		$("#displayFieldTbl tr[var='displayFieldTr']").each(function(){
			var me = $(this),obj={};
			obj.name =$("[var='name']",me).html();
			obj.desc =$("[var='desc']",me).val();
			obj.type =$("[var='type']",me).val();
			obj.style =$("[var='style']",me).val();
			obj.settype =$("input[var='settype']",me).val();
			obj.controltype =$("[var='controltype']",me).val();
			obj.right = _self.getRightJson(me,1);
			json.push(obj);
		});
		return json;
	},
	getRightJson : function(me,flag) {
		var _self=this;
		var rightJson = {};
		var fieldRight = $("[var='fieldRight']",me);
		rightJson=_self.getRight(fieldRight);
		return rightJson;
	},
	getConditionField : function() {
		var fields=new Array();
		$("#conditionTbl tbody tr").each(function(){
			var tr=$(this);
			var field=$.parseJSON(tr.find("input[type='hidden']").val());
			//var jt=tr.find("[name='jt']").val();
			var ct =tr.find("[name='ct']").val();
			var op =tr.find("[name='op']").val();
			var vf =parseInt(tr.find("[name='vf']").val());
			var va =tr.find("[name='va']").val();
			var cm =tr.find("[name='cm']").val();
			var qt =__PendingSetting__.getQueryType(field.ty,op);
			//field.jt=jt;
			field.op=op;
			field.ct=ct;
			field.vf=vf;
			field.va=va;
			field.cm=cm;
			field.qt=qt;
			fields.push(field);
		});
		return fields;
	},	
	getRight : function(rightTd) {
		var obj={};
		obj.type =$("[var='right']",rightTd).val();
		obj.id =$("[var='rightId']",rightTd).val();
		obj.name =$("[var='rightName']",rightTd).val();
		obj.script =$("[var='rightScript']",rightTd).val();
		return obj;
	}
	
};

var __PendingSetting__ = new PendingSetting();// 默认生成一个对象

