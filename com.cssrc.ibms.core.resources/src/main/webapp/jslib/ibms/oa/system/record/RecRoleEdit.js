/**
 * 数据模板构造方法
 */
var RecRoleEdit = function() {

}
// 属性及函数
RecRoleEdit.prototype = {
	/**
	 * 初始化
	 */
	init : function() {
		var _self = this;
		this.initFilterHtml();
	},
	/**
	 * 初始化角色人员信息管理
	 */
	initFilterHtml : function() {
		this.handChange('.table-detail');
		this.handClick('.table-detail');
		this.handSelect('.table-detail');
		
		var td = $('.table-detail td[var=filter]');
		var filterFieldVal = $("#filterField").val();
		if ($.isEmpty(filterFieldVal))
			return;
		var filterField = $.parseJSON(filterFieldVal);
		var sb = new StringBuffer();
		for (var j = 0, k; k = filterField[j++];) {
			var filterHtml = this.getHtmlTd(k.type, k.id, k.name, k.script);
			$(td).html(filterHtml);
		}
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
				/*'<option value="script"'
						+ (v == 'script' ? 'selected="selected"' : '')
						+ '>脚本</option>',*/
				'</select>',
				'<span name="displayFieldRight_span"  '
						+ (v == 'script' || v == 'none' || v == 'everyone'
								? 'style="display: none;" '
								: '') + '>',
				'<input type="hidden"  var="rightId"  value="' + rightId + '">',
				'<textarea  readonly="readonly" var="rightName"  cols="40" rows="3">'
						+ rightName + '</textarea>',
				'<a class="link-get" href="####"><span class="link-btn">选择</span></a>',
				'</span>',
				'<span class="displayFieldRight_script_span" '
						+ (v != 'script' ? 'style="display: none;" ' : '')
						+ '>',
				'<textarea  cols="40" rows="3"  var="rightScript" >'
						+ rightScript + '</textarea>',
				'<a  href="####" name="btnScript" class="link var" title="常用脚本" onclick="__RecRole__.selectScript(this,false)">常用脚本</a>',
				'</span>'];
		return aryTd.join('');
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
	 * 选择脚本
	 * 
	 * @param {}
	 *            obj
	 */
	selectScript : function(obj, isNext) {
		var linkObj = $(obj), txtObj = {};
		if (isNext)
			txtObj = linkObj.next()[0];
		else
			txtObj = linkObj.prev()[0];
		if (txtObj) {
			ScriptDialog({
				callback : function(script) {
					$.insertText(txtObj, script);
				}
			});
		}
	}
};

var __RecRole__ = new RecRoleEdit();// 默认生成一个对象

