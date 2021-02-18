package com.cssrc.ibms.core.form.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.form.dao.ALinkDao;
import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.ITaskOpinionService;
import com.cssrc.ibms.api.activity.model.ITaskOpinion;
import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.util.FieldOperateRightUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.json.JSONObjectUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import freemarker.ext.beans.HashAdapter;

import javax.annotation.Resource;

/**
 * 对象功能:控件Service类 用于在freemaker模版中使用。 开发人员:zhulongchao
 */
@Service
public class FormControlService {
	@Resource
	private ALinkDao aLinkDao;

	public static final String NO_PERMISSION = "";

	/**
	 * 获取选项的国际化资源
	 * 
	 * @param fieldName
	 * @param optionKey
	 * @return
	 */
	public String getOptionValue(String fieldName, String optionKey, Map<String, FormField> optionmap) {
		FormField formField = optionmap.get(fieldName);
		if (BeanUtils.isEmpty(formField)) {
			return "";
		}
		Map<String, Object> optsMap = formField.getAryOptions();
		String value = "";
		if (StringUtil.isEmpty(optionKey)) {
			Set<String> keySet = optsMap.keySet();
			StringBuilder sb = new StringBuilder();
			for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
				String resKey = it.next();
				String resValue = optsMap.get(resKey).toString();
				sb.append("<option value=\"");
				sb.append(resKey);
				sb.append("\">");
				sb.append(resValue);
				sb.append("</option>");
			}
			value = sb.toString();
		} else {
			Object valueObj = optsMap.get(optionKey);
			if (BeanUtils.isNotEmpty(valueObj)) {
				value = valueObj.toString();
			}
		}
		return value;
	}

	/**
	 * 显示主表字段值，用于模版中使用。
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param html
	 *            字段html
	 * @param model
	 *            数据对象
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getField(String fieldName, String html, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission) {
		fieldName = fieldName.toLowerCase();
		// 主表字段,取得显示值权限
		if (fieldName.indexOf(IFieldPool.FK_SHOWAppCode.toLowerCase()) > -1) {
			// 主表字段,对外键列显示值的input标签进行标记。
			// 在input标签中加入权限属性 如 right="r"
			return getFieldFKColumnShow(fieldName, html, model, permission);
		}

		Object object = getFieldVal(fieldName, model);
		return getFiledVal(fieldName, html, model, permission, object);
	}

	private Object getFieldVal(String fieldName, Map<String, Map<String, Map>> model) {
		Object object = null;
		if (fieldName.startsWith("model_parentTableData_".toLowerCase())
				|| fieldName.startsWith("model_parentTableData_")) {
			String tableName = fieldName.split("_")[2];
			String fname = fieldName.split("_")[3];
			object = model.get("parentTableData").get(tableName).get(fname);
		} else {
			object = model.get("main").get(fieldName);
		}
		return object;
	}

	private String getFiledVal(String fieldName, String html, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission, Object object) {
		Document elment = Jsoup.parse(html);
		if (fieldName.startsWith("model_parentTableData_".toLowerCase())
				|| fieldName.startsWith("model_parentTableData_")) {
			String[] fieldNames = fieldName.split("_");
			fieldName = fieldNames[fieldNames.length - 1];
		}
		Elements elments = elment.select("[name$=:" + fieldName + "]");
		String value = "";
		if (object != null) {
			// 获取权限值
			value = object.toString();
		}
		Map<String, String> right = permission.get("field");
		// 可写权限
		if (right == null || right.get(fieldName) == null || "w".equals(right.get(fieldName))) {
			String validate = elments.attr("validate");
			if (validate.indexOf("required:true") < 0) {
				elments.addClass("tableHighLight");
			}
			return elments.outerHtml().replaceAll("#value", value);
			// return html.replaceAll("#value", value);
		} else if ("b".equals(right.get(fieldName))) {// 必填
			html = toRequired(html);
			return StringUtil.replaceAll(html, "#value", value);
			// return toRequired(html).replaceAll("#value", value);
		} else if ("r".equals(right.get(fieldName))) {
			// 将换行符转换为html的换行符标签
			value = value.replaceAll("[\\r\\n|\\n|\\r]", "<br/>");
			Document doc = Jsoup.parse(html);
			Elements selectElement = doc.select("select");
			if (selectElement.attr("hidden").equals("hidden")) {// 隐藏字段
				return StringUtil.replaceAll(html, "#value", value);
			}
			Elements inputElement = doc.select("input");
			if (inputElement.attr("hidden").equals("hidden") || inputElement.attr("type").equals("hidden")) {// 隐藏字段
				return StringUtil.replaceAll(html, "#value", value);
			}
			String extLink = getExtLink(fieldName, model, html, value);
			extLink = getOptionValue(html, value, extLink);
			extLink += getHiddenWebSign(html, value);
			return extLink;
		} else if ("rp".equals(right.get(fieldName))) {// ReadPost
			// 将换行符转换为html的换行符标签
			value = value.replaceAll("[\\r\\n|\\n|\\r]", "<br/>");
			String rpostInput = getReadPost(html);
			rpostInput = StringUtil.replaceAll(rpostInput, "#value", value);
			String extLink = getExtLink(fieldName, model, html, value);
			String retval = rpostInput + extLink;
			return retval;
		} else {
			return NO_PERMISSION;
		}
	}

	public String getFieldFKColumnShow(String fKColumnShowName, String html, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission) {
		// 获取外键名称
		String fkFieldName = fKColumnShowName.replace(IFieldPool.FK_SHOWAppCode.toLowerCase(), "");

		// (w:可写权限,b:必填,r:只读权限,rp:只读提交ReadPost)
		String right = "w";
		if (permission.get("field") != null && permission.get("field").get(fkFieldName) != null) {
			right = permission.get("field").get(fkFieldName);
		}
		// 在input标签中加入权限属性 如 right="r"
		StringBuffer sb = new StringBuffer();
		try {
			Document doc = Jsoup.parse(html);
			Elements elList = doc.select("input");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				el.attr("right", right);
				el.attr("value", "");
				sb.append(el);
			}
		} catch (Exception e) {
			return html;
		}
		return sb.toString();

	}

	/**
	 * 获取只读状态的 选项值
	 * 
	 * @param html
	 * @param value
	 * @return
	 */

	private static String getOptionValue(String html, String value, String extLink) {
		Document doc = Jsoup.parse(html);
		Elements selectElement = doc.select("select");
		String sQuery = selectElement.attr("selectquery");
		if (sQuery != null && StringUtil.isNotEmpty(sQuery)) {
			String external = sQuery.replace("&#39;", "\"").replace("&quot;", "\"");
			String temp = "<span selectvalue=" + value + " selectquery=" + external + "><lable></lable></span>";
			return temp.replaceAll("'", "\"");

		} else {
			Elements optElments = doc.select("option");
			if (optElments.size() > 0) {
				for (Element elment : optElments) {
					String key = elment.attr("value");
					if (value.equals(key)) {
						return elment.text();
					}
				}
			}
		}

		return extLink;
	}

	/**
	 * 获取只读状态下有WEB验证的标签要用到input:hidden
	 * 
	 * @param html
	 * @param value
	 * @return
	 */
	private static String getHiddenWebSign(String html, String value) {
		StringBuffer sb = new StringBuffer();
		String str = html.replaceAll(" ", ""); // 去掉空格
		if (str.contains("isWebSign':true") || str.contains("isWebSign:true") || str.contains("isWebSign\":true")) {
			Document doc = Jsoup.parse(html);
			String elStr = "";
			// 输入文本等类型
			Elements elList = doc.select("input");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				el.addClass("hidden");
				el.attr("value", value);
				el.attr("validate", "{'isWebSign':true}");
				el.attr("right", "r");
				sb.append(el + " ");
			}
			// 下拉框
			elList = doc.select("select");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				elStr = "<input type=\"hidden\" name=\"" + el.attr("name") + "\" lablename=\"" + el.attr("lablename")
						+ "\" validate=\"{'isWebSign':true}\" value=\"" + value + "\" right=\"r\" /> ";
				sb.append(elStr + " ");

			}
			// 大文本
			elList = doc.select("textarea");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				elStr = "<textarea name=\"" + el.attr("name") + "\" lablename=\"" + el.attr("lablename")
						+ "\" validate=\"{'isWebSign':true}\"  class=\"hidden\" value=\"" + value + "\"  right=\"r\" >"
						+ value + "</textarea>";
				sb.append(elStr + " ");
			}
		}
		return sb.toString();
	}

	/**
	 * 生成引用超连接
	 * 
	 * @param fieldName
	 * @param model
	 * @param html
	 * @param value
	 * @return
	 */
	private static String getExtLink(String fieldName, Map<String, Map<String, Map>> model, String html, String value) {
		if (StringUtil.isEmpty(value)) {
			return value;
		}
		Object objectId = model.get("main").get(fieldName + "id");
		if (objectId == null) {
			return value;
		}
		String idStr = objectId.toString();

		if (StringUtil.isEmpty(idStr)) {
			return value;
		}
		Element div = new Element(Tag.valueOf("div"), "");
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("[linktype]");

		String nameStr = value;
		String[] names = nameStr.split(",");

		if (links.size() == 0) {
			for (int i = 0; i < names.length; i++) {
				Element span = new Element(Tag.valueOf("span"), "");
				// span.attr("class","backgrounddiv");
				Element lable = new Element(Tag.valueOf("lable"), "");
				lable.text(names[i]);
				span.appendChild(lable);
				div.appendChild(span);
			}
			String valuela = div.outerHtml();
			return valuela;
		}

		Element node = links.get(0);
		String linktype = node.attr("linktype");
		String[] ids = idStr.split(",");

		for (int i = 0; i < ids.length; i++) {
			Element span = new Element(Tag.valueOf("span"), "");
			// span.attr("class","backgrounddiv");

			String name = names[i];
			// span.text(name);
			/*Element link = new Element(Tag.valueOf("a"), "");
			String id = ids[i];
			// String name = names[i];
			link.attr("refid", id);
			link.attr("href", "####");
			link.attr("linktype", linktype);
			link.attr("style", "text-decoration:none");
			link.text(name);*/
			span.text(name);
			/*span.appendChild(link);*/
			div.appendChild(span);
		}
		String retval = div.outerHtml();

		return retval;
	}

	/**
	 * 显示选择器的选择按钮
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param html
	 *            字段html
	 * @param model
	 *            数据对象
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getLink(String fieldName, String html, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission) {
		Document doc = Jsoup.parse(html);
		Elements elList = doc.select("a");
		if (elList.size() > 0) { // 有超链接的
			for (Element el : elList) {
				String name = el.attr("name");
				if (StringUtil.isNotEmpty(name)) {
					name = name.replaceAll(" ", "").toLowerCase().substring(0, 2); // 去掉空格 、改变为小写、并截取前两个字符
					if (name.contains("s:")) { // 并是子表的超链接 直接返回 由页面JS解析 子表字段名称是s:开头的
						return html; // 直接返回
					}
				}
			}
		}
		fieldName = fieldName.toLowerCase();
		// 可写权限
		if (permission.get("field") == null || permission.get("field").get(fieldName) == null
				|| "w".equals(permission.get("field").get(fieldName))
				|| "b".equals(permission.get("field").get(fieldName))) {
			return html;
		} else {
			return NO_PERMISSION;
		}
	}

	/**
	 * 显示主表字段中的隐藏字段
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param html
	 *            字段html
	 * @param model
	 *            数据对象
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getHiddenField(String fieldName, String html, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission) {

		fieldName = fieldName.toLowerCase();
		// 取得值
		Object object = model.get("main").get(fieldName);
		String value = "";
		if (object != null) {
			value = object.toString();
		}

		// 可写权限
		if (permission.get("field") == null || permission.get("field").get(fieldName) == null
				|| "w".equals(permission.get("field").get(fieldName))) {

			return StringUtil.replaceAll(html, "#value", value);
		} else if (permission.get("field") == null || permission.get("field").get(fieldName) == null
				|| "r".equals(permission.get("field").get(fieldName))) {// 只读

			return StringUtil.replaceAll(html, "#value", value);
		} else if ("b".equals(permission.get("field").get(fieldName))) {
			html = toRequired(html);
			return StringUtil.replaceAll(html, "#value", value);
			// 只读和其他情况下不返回值
		} else if ("rp".equals(permission.get("field").get(fieldName))) {
			String rpostInput = getReadPost(html);
			rpostInput = StringUtil.replaceAll(rpostInput, "#value", value);
			return rpostInput;
		} else {
			return NO_PERMISSION;
		}
	}

	/**
	 * 获取子表权限。
	 * 
	 * @param tableName
	 * @param permission
	 * @return
	 */
	public String getSubTablePermission(String tableName, Map<String, Map<String, String>> permission) {
		tableName = tableName.toLowerCase();
		Map map = (Map) permission.get("table");
		String right = "w";
		if (map.containsKey(tableName)) {
			right = (String) map.get(tableName);
		}
		return right;
	}

	/**
	 * 获取字段的权限
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param
	 *
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getFieldRight(String fieldName, Map<String, Map<String, String>> permission) {
		fieldName = fieldName.toLowerCase();
		// 可写权限
		if (permission.get("field").get(fieldName) == null || "w".equals(permission.get("field").get(fieldName))) {
			return "w";
		} else if ("b".equals(permission.get("field").get(fieldName))) {
			return "b";
		} else if ("r".equals(permission.get("field").get(fieldName))) {
			return "r";
		} else {
			return "no";
		}
	}

	/**
	 * @Title: getTableGroup @Description: TODO(获取表达分组权限) @param @param teamNameKey
	 * 分组key @param @param tableId 表Id @param @param html html模板 @param @param
	 * permission 权限map @param @return @return String 返回类型 @throws
	 */
	public String getTableGroup(String teamNameKey, String tableId, String html,
			Map<String, Map<String, String>> permission) {
		Document doc = Jsoup.parse(html);
		Elements team = doc.select("div[teamnamekey=team:" + tableId + ":" + teamNameKey + "]");
		Map<String, String> tableGroupPermission = permission.get("tableGroup");
		if (tableGroupPermission == null || tableGroupPermission.keySet().size() < 1) {
			return html;
		}
		String r = tableGroupPermission.get(teamNameKey);
		try {
			JSONObject jr = JSONObject.fromObject(r);
			Object hightLight = jr.get("hightLight");
			String right = jr.getString("right");

			right = (right == null ? "" : right);
			teamNameKey = teamNameKey.toLowerCase();
			switch (right) {
			case "w":
				if (hightLight != null) {
					team.addClass("true".equals(hightLight) ? "" : "disEdit");
				}
				if (team == null || team.size() < 1) {
					return html;
				}
				return team.outerHtml();
			case "r":
				if (hightLight != null) {
					team.addClass("disEdit");
				}
				if (team == null || team.size() < 1) {
					return html;
				}
				return team.outerHtml();
			case "":
				team.attr("style", "display:none;");
				if (team == null || team.size() < 1) {
					String sb = "teamnamekey=\"team:" + tableId + ":" + teamNameKey + "\"";
					return html.replace(sb, sb + " style=\"display:none;\"");
				}
				return team.outerHtml();
			}
			return html;
		} catch (Exception e) {
			return html;
		}

	}

	/**
	 * 将#value替换为字段的值。
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param
	 *
	 * @param model
	 *            数据对象
	 * @return
	 */
	public String getFieldValue(String fieldName, Map<String, Map<String, Map>> model) {
		fieldName = fieldName.toLowerCase();
		// 取得值
		Object object = this.getFieldVal(fieldName, model);
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		return value;
	}

	/**
	 * 将#value替换为字段的值。将子表，从表列表中的 用户，组织，部门，岗位，数据的hidden赋值。
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param
	 *
	 * @param model
	 *            数据对象
	 * @return
	 */
	public String getSubRelFieldValue(String fieldName, Map<String, Map<String, Map>> model) {
		fieldName = fieldName.toLowerCase();
		// 取得值
		Object object = model.get(fieldName);
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		return value;
	}

	/**
	 * 将#value替换为字段的值。
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param
	 *
	 * @param model
	 *            数据对象
	 * @param type
	 *            控件类型（用于过虑值中的特殊符号）
	 * @return
	 */
	public String getFieldValue(String fieldName, Map<String, Map<String, Map>> model, String type) {
		// 取得值
		String value = getFieldValue(fieldName, model);
		if ("pictureShow".equalsIgnoreCase(type)) {
			value = value.replaceAll("\"", "'");
		}
		return value;
	}

	/**
	 * 取得主表数值型需要千分位显示的字段 值
	 * 
	 * @param fieldName
	 * @param model
	 * @return
	 */
	public String getComdifyValue(String fieldName, Map<String, Map<String, Map>> model) {
		fieldName = fieldName.toLowerCase();
		// 取得值
		Object object = model.get("main").get(fieldName);
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		return StringUtil.comdify(value);
	}

	/**
	 * 取得子表 数值型需要千分位显示的字段 值
	 * 
	 * @param fieldName
	 * @param table
	 * @return
	 */
	public String getSubComdifyValue(String fieldName, Map<String, Map> table) {
		fieldName = fieldName.toLowerCase();
		String value = "";
		Object obj = table.get(fieldName);
		if (obj != null) {
			value = obj.toString();
		}
		return StringUtil.comdify(value);
	}

	/**
	 * 显示意见，用于模版中使用。
	 * 
	 * @param opinionName
	 * @param html
	 *            字段html
	 * @param model
	 *            数据对象
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getOpinion(String opinionName, String html, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission) {
		opinionName = opinionName.toLowerCase();

		Object object = model.get("opinion").get(opinionName);
		if (object == null) {
			object = model.get("opinion").get("opinion:" + opinionName);
		}

		String value = "";
		if (object != null) {
			value = object.toString();
		}
		Map<String, String> opinion = permission.get("opinion");
		String rights = opinion.get(opinionName);
		if (rights == null) {
			rights = opinion.get("opinion:" + opinionName);
		}
		if (opinion == null || rights == null || "w".equals(rights)) {
			return StringUtil.replaceAll(html, "#value", value);
			// return html.replaceAll("#value", value);
		} else if ("b".equals(rights)) {
			html = toRequired(html);
			return StringUtil.replaceAll(html, "#value", value);
		} else if ("r".equals(rights)) {
			return value;
		} else {
			return NO_PERMISSION;
		}
	}

	/**
	 * 取得意见
	 * 
	 * @param opinionName
	 * @param model
	 * @return
	 */
	public String getOpinion(String opinionName, Map<String, Map<String, Map>> model) {
		opinionName = opinionName.toLowerCase();
		Object object = model.get("opinion").get(opinionName);
		if (object == null) {
			object = model.get("opinion").get("opinion:" + opinionName);
		}
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		return value;
	}

	/**
	 * 返回rdo和checkbox控件
	 * 
	 * @param fieldName
	 * @param html
	 * @param ctlVal
	 * @param model
	 * @param permission
	 * @return
	 */
	public String getRdoChkBox(String fieldName, String html, String ctlVal, Map<String, Map<String, Map>> model,
			Map<String, Map<String, String>> permission) {

		Object object = this.getFieldVal(fieldName, model);
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		if (permission.get("field").get(fieldName) == null || "w".equals(permission.get("field").get(fieldName))) {
			html = html.replaceAll("(?s)disabled=\\s*\"?\\s*disabled\\s*\"?", "");
			html = getHtml(html, ctlVal, value, true);
			return html;
		} else if ("b".equals(permission.get("field").get(fieldName))) {
			html = html.replaceAll("(?s)disabled=\\s*\"?\\s*disabled\\s*\"?", "");
			html = getHtml(html, ctlVal, value, true);
			Document doc = Jsoup.parse(html);
			Elements elList = doc.select("input,select,textarea");
			for (Element e : elList) {
				toRequired(e.outerHtml());
				e.after(toRequired(e.outerHtml()));
				e.remove();
			}
			return doc.getElementsByTag("body").html();
		} else if ("r".equals(permission.get("field").get(fieldName))) {
			html = getHtml(html, ctlVal, value, true);
			return html;
		} else {
			return NO_PERMISSION;
		}
	}

	/**
	 * 返回rdo和checkbox控件(用于打印模板)
	 * 
	 * @param fieldName
	 * @param html
	 * @param ctlVal
	 * @param model
	 * @param
	 * @return
	 */
	public String getPrintRdoChkBox(String fieldName, String html, String ctlVal, Map<String, Map<String, Map>> model) {
		Object object = model.get("main").get(fieldName.toLowerCase());
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		html = getHtml(html, ctlVal, value, true);
		return html;
	}

	/**
	 * 返回下拉框值(用于打印模板)
	 * 
	 * @param fieldName
	 * @param html
	 * @param model
	 * @return
	 */
	public String getPrintOptionValue(String fieldName, String html, Map<String, Map<String, Map>> model) {
		fieldName = fieldName.toLowerCase();
		// 取得值
		Object object = model.get("main").get(fieldName);
		String value = "";
		if (object != null) {
			value = object.toString();
		}
		if ("".equals(value))
			return html;

		Document doc = Jsoup.parse(html);
		Elements selectElement = doc.select("select");
		String sQuery = selectElement.attr("selectquery");
		if (sQuery != null && StringUtil.isNotEmpty(sQuery)) {
			String external = sQuery.replace("&#39;", "\"").replace("&quot;", "\"");
			String temp = "<span selectvalue=" + value + " selectquery=" + external + "><lable></lable></span>";
			return temp.replaceAll("'", "\"");

		} else {
			Elements optElments = doc.select("option");
			if (optElments.size() > 0) {
				for (Element elment : optElments) {
					String key = elment.attr("value");
					if (value.equals(key)) {
						return elment.text();
					}
				}
			}
		}
		return html;
	}

	/**
	 * 在子表中选中下checkbox和radio
	 * 
	 * @param fieldName
	 * @param html
	 * @param ctlVal
	 * @param table
	 * @return
	 */
	public String getRdoChkBox(String fieldName, String html, String ctlVal, Map<String, Object> table) {
		String value = "";
		if (!BeanUtils.isEmpty(table)) {
			value = table.get(fieldName.toLowerCase()).toString();
		}
		html = getHtml(html, ctlVal, value, true);
		return html;

	}

	public String getRdoChkBoxNoChcked(String fieldName, String html, String ctlVal, Map<String, Object> table) {
		String value = "";
		if (!BeanUtils.isEmpty(table)) {
			value = table.get(fieldName.toLowerCase()).toString();
		}
		html = getHtml(html, ctlVal, value, false);
		return html;

	}

	/**
	 * 获取子表的文件上传
	 * 
	 * @param fieldName
	 * @param table
	 * @return
	 */
	public String getSubTableAttachMent(String fieldName, Map<String, Object> table) {
		String value = (String) table.get(fieldName.toLowerCase());
		return value;
	}

	/**
	 * 获取子表下拉框的内容
	 * 
	 * @param fieldName
	 * @param html
	 * @param table
	 * @return
	 */
	public String getSubTableOptionValue(String fieldName, String html, Map<String, Object> table) {
		String value = (String) table.get(fieldName.toLowerCase());
		Document doc = Jsoup.parse(html);
		Elements selectElement = doc.select("select");
		String sQuery = selectElement.attr("selectquery");
		if (sQuery != null && StringUtil.isNotEmpty(sQuery)) {
			String external = sQuery.replace("&#39;", "\"").replace("&quot;", "\"");
			String temp = "<span selectvalue=" + value + " selectquery=" + external + "><lable></lable></span>";
			return temp.replaceAll("'", "\"");

		} else {
			Elements optElments = doc.select("option");
			if (optElments.size() > 0) {
				for (Element elment : optElments) {
					String key = elment.attr("value");
					if (value.equals(key)) {
						return elment.text();
					}
				}
			}
		}
		return html;
	}

	/**
	 * 替换html。
	 * 
	 * @param html
	 *            html的代码
	 * @param ctlVal
	 *            控件代表的值。
	 * @param value
	 *            当前字段的值。
	 * @return
	 */
	private String getHtml(String html, String ctlVal, String value, boolean ckecked) {
		// 还没有选择任何的字段
		if (!ckecked) {
			html = html.replaceAll("chk=\"?1\"?", "");
			html = html.replace("checked=\"checked\"", "");
		} else if (StringUtil.isEmpty(value)) {
			html = html.replaceAll("chk=\"?1\"?", "");
		} else {
			html = html.replace("checked=\"checked\"", "");
			if (value.contains(ctlVal)) {
				html = html.replaceAll("chk=\"?1\"?", "checked=\"checked\"");
			} else {
				html = html.replaceAll("chk=\"?1\"?", "");
			}
		}
		return html;
	}

	/**
	 * 转换必填的Html
	 * 
	 * @param html
	 * @return
	 */
	public static String toRequired(String html) {
		StringBuffer sb = new StringBuffer();
		try {
			Document doc = Jsoup.parse(html);
			Elements elList = doc.select("input,select,textarea");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				String validate = el.attr("validate");
				String validateValue = "{required:true}";
				if (StringUtil.isNotEmpty(validate)) {
					JSONObject json = JSONObject.fromObject(validate);
					json.element("required", "true");
					validateValue = json.toString().replace("\"", "").replace("\'", "");
				}
				el.attr("validate", validateValue);
				el.attr("right", "b");
				sb.append(el);
			}
		} catch (Exception e) {
			return html;
		}
		return sb.toString();
	}

	/**/
	public static String getReadOnlyHtml(String html) {
		StringBuffer sb = new StringBuffer();
		try {
			Document doc = Jsoup.parse(html);
			Elements elList = doc.select("input,select,textarea");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				el.attr("right", "r");
				el.attr("readonly", "readonly");
				el.removeAttr("validate");
				sb.append(el);
			}
		} catch (Exception e) {
			return html;
		}
		return sb.toString();
	}

	/**
	 * 只读提交
	 * 
	 * @param html
	 * @return
	 */
	public static String getReadPost(String html) {
		StringBuffer sb = new StringBuffer();
		try {
			Document doc = Jsoup.parse(html);
			Elements elList = doc.select("input,select,textarea");
			for (Iterator<Element> it = elList.iterator(); it.hasNext();) {
				Element el = it.next();
				el.addClass("hidden");
				el.attr("right", "rp");
				el.removeAttr("validate");
				sb.append(el);
			}
		} catch (Exception e) {
			return html;
		}
		return sb.toString();
	}

	/**
	 * 表单其他字段的国际化
	 * 
	 * @param i18n
	 * @param defaultVal
	 * @param lanType
	 * @return
	 */
	public String getFormI18n(String i18n, String defaultVal, String lanType) {
		if (StringUtil.isEmpty(i18n) || StringUtil.isEmpty(lanType)) {
			return defaultVal;
		}
		JSONArray jarray = JSONArray.fromObject(i18n);
		for (Object obj : jarray) {
			JSONObject job = (JSONObject) obj;
			String lantype = job.getString("lantype");
			if (lantype.equals(lanType)) {
				String resval = job.getString("value");
				if (StringUtil.isEmpty(resval))
					return defaultVal;
				else
					return resval;
			} else
				continue;
		}
		return defaultVal;
	}

	/**
	 * 对url进行处理
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public String getCustomUrl(String url, HashAdapter data) {
		String regEx = "\\[{1}.+?\\]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(url);
		if (data.isEmpty()) {
			return url;
		}
		String result = url;
		while (matcher.find()) {
			String groups = matcher.group();
			String gg = groups.replaceAll("\\[", "").replaceAll("\\]", "");
			if (data.containsKey(gg)) {
				result = matcher.replaceFirst(CommonTools.Obj2String(data.get(gg)));
			} else {
				result = matcher.replaceFirst(gg);
			}
			matcher = pattern.matcher(result);
		}
		return result;
	}

	/*
	 * public String getGroupOpinion(String formdefid, String template, String
	 * nodeid,String tablegroup,String instanceId, Map<String, Map<String, String>>
	 * permission) { return this.getGroupOpinion(formdefid, template, nodeid,
	 * tablegroup, "", instanceId, permission); }
	 */
	/**
	 * @Title: getGroupOpinion @Description: TODO(获取审批意见信息) @param @param
	 * teamNameKey @param @param tableId @param @param html @param @param
	 * permission @param @return @return String 返回类型 @throws
	 */
	public String getGroupOpinion(String formdefid, String template, String nodeid, String tablegroup, String title,
			String instanceId, Map<String, Map<String, String>> permission) {
		if (StringUtil.isEmpty(title)) {
			title = "审批历史";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("instanceId", instanceId);
		map.put("flowNodeId", nodeid);
		map.put("title", title);
		map.put("formdefid", formdefid);

		Map<String, String> tableGroupPermission = permission.get("tableGroup");
		if (tableGroupPermission == null || tableGroupPermission.keySet().size() < 1) {
			return parseGroupOpinion(template, tablegroup, map);
		}
		String r = tableGroupPermission.get(tablegroup);
		r = (r == null ? "" : r);
		switch (r) {
		case "w":
			// 获取审批意见
			return parseGroupOpinion(template, tablegroup, map);
		case "":
			return "";
		}
		// 获取审批意见
		return parseGroupOpinion(template, tablegroup, map);
	}

	/**
	 * @Title: parseGroupOpinion @Description: TODO(通过模板解析审批意见) @param @param
	 * formdefid @param @param template @param @param nodeid @param @param
	 * tablegroup @param @return 设定文件 @return String 返回类型 @throws
	 */
	public String parseGroupOpinion(String template, String tablegroup, Map<String, String> map) {
		if (StringUtil.isEmpty(template)) {
			return "";
		}
		String instanceId = map.get("instanceId");
		String nodeid = map.get("flowNodeId");
		Map<String, Object> model = new HashMap<String, Object>();
		model.putAll(map);

		IFormTemplateService formTemplateService = AppUtil.getBean(IFormTemplateService.class);
		ITaskOpinionService taskOpinionService = AppUtil.getBean(ITaskOpinionService.class);
		FreemarkEngine freemarkEngine = AppUtil.getBean(FreemarkEngine.class);
		if (StringUtil.isEmpty(instanceId)) {
			return "";
		}
		IFormTemplate formTemplate = formTemplateService.getByTemplateAlias(template);
		String html = formTemplate.getHtml();
		if (template.indexOf("_onlyCur") > -1 || template.indexOf("_div") > -1) {
			ITaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(Long.valueOf(instanceId), nodeid);
			if (BeanUtils.isEmpty(taskOpinion)) {
				return "";
			}
			model.put("taskOpinion", taskOpinion);

		} else if (template.indexOf("_tablelist") > -1) {
			List<? extends ITaskOpinion> list = taskOpinionService.getByActInstIdTaskKey(Long.valueOf(instanceId),
					nodeid);
			if (BeanUtils.isEmpty(list)) {
				return "";
			}
			model.put("opinions", list);
		}

		try {
			html = freemarkEngine.parseByStringTemplate(model, html);
			return html;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @Title: getFieldRelData @Description: TODO(获取外键关联数据) @param @param
	 * tableId @param @param fieldName @param @param filedVal @param @return @return
	 * String 返回类型 @throws
	 */
	public String getFieldRelData(String tableId, String fieldName, String filedVal) {
		try {
			String fname = ITableModel.CUSTOMER_COLUMN_PREFIX + fieldName;
			DataTemplateService dataTemplateService = AppUtil.getBean(DataTemplateService.class);
			FormFieldService formFieldService = AppUtil.getBean(FormFieldService.class);
			FormField f = formFieldService.getFieldByTidFna(Long.valueOf(tableId), fieldName);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(fname, filedVal);
			Object val = dataTemplateService.getFormatFkData(map, fname, f);
			return val.toString();
		} catch (Exception e) {
			return fieldName + "字段解析出错";
		}
	}

	/**
	 * 获取字段容器的权限
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param type
	 *            字段容器类型 ：attachment
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getContainRight(String fieldName, String type, Map<String, Map<String, String>> permission) {
		return getFieldRight(fieldName, permission);
	}

	/**
	 * @param fieldName
	 *            字段名称
	 * @param
	 * @param permission
	 *            权限对象
	 * @return
	 */
	public String getOperateFieldRight(String fieldName, String tableName,
			Map<String, Map<String, String>> permission) {
		if (BeanUtils.isEmpty(permission.get("field")) || BeanUtils.isEmpty(
				permission.get("field").get(fieldName + "_" + tableName + "_" + FieldOperateRightUtil.OPERATE_RIGHT))) {
			return "";
		} else {
			String right = permission.get("field")
					.get(fieldName + "_" + tableName + "_" + FieldOperateRightUtil.OPERATE_RIGHT);
			JSONObject operate = JSONObject.fromObject(right);
			right = operate.toString().replaceAll("\"", "￥@@￥");
			return right;
		}
	}

	/**
	 * 列表tdHtml定制 模板定制写法： <#if field.name=='sjz'>
	 * <#noparse>${formControlService.getCustomTdHtml('dataPackageInfo','sjz',data)}</#noparse>
	 * <#else> <@genDisplayS field=field /><#noparse>${data.</#noparse>
	 * ${field.name} <#noparse>}</#noparse><@genDisplayE field=field /> </#if>
	 * 
	 * @param tableName
	 * @param fieldName
	 * @param data
	 * @return
	 */
	public String getCustomTdHtml(String tableName, String fieldName, HashAdapter data) {
		String tdHtml = "";
		switch (tableName + "_" + fieldName) {
		case "dataPackageInfo_sjz": // 数据包详细信息表_数据值 onclick=\"javascript:openFormTemplateLink("+id+")\"
			if (data.containsKey("sjlx")) {
				String sjlx = CommonTools.Obj2String(data.get("sjlx"));
				if (sjlx.equals("表单")) {
					tdHtml = CommonTools.Obj2String(data.get("ssmbmc"));
					// a标签
					String id = CommonTools.Obj2String(data.get("ssmb"));
					// 当前数据包信息的记录ID
					String dataId = CommonTools.Obj2String(data.get("ID"));
					tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormManage(" + id
							+ ")\"  ondblclick=\"javascript:openFormTemplateLink(" + id + "," + dataId + ")\"> "
							+ tdHtml + "</a>";
				} else {
					tdHtml = CommonTools.Obj2String(data.get("sjz"));
				}
			}
			break;
		default:
			tdHtml = "";
		}
		return tdHtml;
	}

	/**
	 * @Description 实例表perview页面重写表格名称
	 * @Author ZMZ
	 * @Date 2021/1/4 18:45
	 * @param tableName
 	 * @param fieldName
 	 * @param data
	 * @Return java.lang.String
	 */
	public String getInstantCustomTdHtml(String tableName, String fieldName, HashAdapter data) {
		String tdHtml = "";
		if ("17".equals(data.get("bdzl"))){
			String fileJsonString=data.get("show_number").toString();
			//20200104  暂时先只做单文件下载和上传,
			JSONObject fileJson=JSONArray.fromObject(fileJsonString).getJSONObject(0);
			String fileName=fileJson.get("name").toString();
			String fileId=fileJson.get("id").toString();
			Map<String,Object> fileMap=aLinkDao.getSysFileByFileId(fileId);
			String filePath=CommonTools.Obj2String(fileMap.get("FILEPATH"));
			String path="/dp/oa/system/sysFile/download.do?fileId="+fileId;
			tdHtml="<a href=\"####\" onclick=\"downloadFile(this)\" path=\""+path+"\" >"+fileName+"</a>";
			return tdHtml;
		}
		switch (tableName + "_" + fieldName) {
		case "dataPackageInfo_name": // 数据包详细信息表_数据值 onclick=\"javascript:openFormTemplateLink("+id+")\"
			// 当前数据包信息的记录ID
			tdHtml = CommonTools.Obj2String(data.get("name"));
			String id = CommonTools.Obj2String(data.get("ID"));
			tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormRemoveMask(" + id
					+ ")\"> " + tdHtml + "</a>";
			/*tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormRemoveMask(" + id
					+ ")\"  ondblclick=\"javascript:openFormTemplateLink(" + id + ")\"> " + tdHtml + "</a>";*/
			break;
		default:
			tdHtml = "";
			break;
		}
		return tdHtml;
	}

	public String getfiledataTypeCustomTdHtml(String fieldName, HashAdapter data) {
		String tdHtml = "";
		if (fieldName.equals("sjlb")) {
			String show_sjlb = CommonTools.Obj2String(data.get("show_sjlb"));
			switch (show_sjlb) {
			case "1":
				tdHtml = "依据文件";
				break;
			case "2":
				tdHtml = "常规验收项目表";
				break;
			case "3":
				tdHtml = "功能性检验表";
				break;
			case "4":
				tdHtml = "验收报告";
				break;
			case "10":
				tdHtml="靶场试验问题表";break;
			case "13":
				tdHtml="武器系统所检问题表";break;
			}
		}
		return tdHtml;
	}

	public String getfileCustomTdHtml(String fieldName, HashAdapter data) {
		String tdHtml = "";
		if (fieldName.equals("mz")) {
			String show_sjlb = CommonTools.Obj2String(data.get("show_sjlb"));
			String planId = CommonTools.Obj2String(data.get("planId"));
			String js = CommonTools.Obj2String(data.get("show_wjlj"));
			tdHtml = CommonTools.Obj2String(data.get("mz"));
			switch (show_sjlb) {
			case "1":
				if (js != null && !"[]".equals(js)) {
					js = js.substring(1, js.length() - 1);
					JSONObject jsonObject = new JSONObject();
					jsonObject = JSONObject.fromObject(js);
					String Name = jsonObject.optString("name");
					String id = jsonObject.optString("id");
					tdHtml = "<a href=" + "####" + " onclick=" + '"' + "javascript:downloadFile(" + id + ")" + '"' + ">"
							+ Name + "</a>";
				}
				break;

			case "2":
				String id = CommonTools.Obj2String(data.get("sjId"));
				tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormManage(" + id
						+ ")\"  ondblclick=\"javascript:openFormTemplateLink(" + id + ")\"> " + tdHtml + "</a>";
				break;
			case "3":
				id = CommonTools.Obj2String(data.get("sjId"));
				tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormManage(" + id
						+ ")\"  ondblclick=\"javascript:openFormTemplateLink(" + id + ")\"> " + tdHtml + "</a>";
				break;
			case "10":
				id = CommonTools.Obj2String(data.get("sjId"));
				tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormManage(" + id
							+ ")\"  ondblclick=\"javascript:openFormTemplateLink(" + id + ")\"> " + tdHtml + "</a>";
				break;
			case "13":
				id = CommonTools.Obj2String(data.get("sjId"));
				tdHtml = "<a href=\"####\"  onclick=\"javascript:openFormManage(" + id
							+ ")\"  ondblclick=\"javascript:openFormTemplateLink(" + id + ")\"> " + tdHtml + "</a>";
				break;
			case "4":
				id = CommonTools.Obj2String(data.get("sjId"));
				tdHtml = "<a href=\"####\"  onclick=\"javascript:showDetail(" + id + ")\"  \"> " + tdHtml + "</a>";
				break;
			}
		}
		/*
		 * switch(tableName+"_"+fieldName) { case "dataPackageInfo_name": //数据包详细信息表_数据值
		 * onclick=\"javascript:openFormTemplateLink("+id+")\" //当前数据包信息的记录ID tdHtml =
		 * CommonTools.Obj2String(data.get("name")); String
		 * id=CommonTools.Obj2String(data.get("ID")); tdHtml =
		 * "<a href=\"####\"  onclick=\"javascript:openFormManage("
		 * +id+")\"  ondblclick=\"javascript:openFormTemplateLink("+id+")\"> "+tdHtml+
		 * "</a>"; break; default : tdHtml = ""; break; }
		 */
		return tdHtml;
	}

	/**
	 * 工作板待办项"下一步"的点击事件 <#noparse> <#elseif
	 * '</#noparse>${field.name}<#noparse>'=="xyb"> </#noparse>
	 * <td title="<#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse>">
	 * <#noparse>
	 * ${formControlService.getWorkboardNeedToDoNext('dataPackageInfo','xyb',data)}
	 * </#noparse></td>
	 * 
	 * @param tableName
	 * @param fieldName
	 * @param data
	 * @return
	 * @author zmz
	 */
	public String getWorkboardNeedToDoNext(String tableName, String fieldName, HashAdapter data) {
		String tdHtml = "";
		String useId = UserContextUtil.getCurrentUserId().toString();
		List<Map<String, Object>> planInfo;
		String planId = data.get("show_zjID").toString();
		if (useId.equals(data.get("zzID").toString())) {
			if (data.get("gzm").toString().indexOf("YSCH")!=-1){
				//是验收
				switch (data.get("xyb").toString()) {
					case "下发表单到PAD":
						tdHtml = "下发表单到PAD";
						planInfo = aLinkDao.getPlanInfoById(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000021790753"
								+ "&productBatchId=" + planInfo.get(0).get("F_SSCPPC") + "&batchName="
								+ planInfo.get(0).get("F_CPPC") + "&acceptancePlanId=" + planId + "&productCategoryId="
								+ planInfo.get(0).get("F_SSCPLB") + "&__dbomSql__=F_acceptancePlanId=" + planId + "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					case "PAD数据采集":
						tdHtml = "PAD数据采集";
						planInfo = aLinkDao.getPlanInfoById(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000032920832"
								+ "&&__dbomSql__=F_ssxhhpc=" + planInfo.get(0).get("F_SSCPPC") + "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					case "发起验收总结":
						tdHtml = "发起验收总结";

						planInfo = aLinkDao.getPlanInfoById(planId);

						// 服务器需要跳转到验收报告
						/*tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000021410561"
								+ "&batchId=" + planInfo.get(0).get("F_SSCPPC") + "&batchName=" + planInfo.get(0).get("F_CPPC")
								+ "&acceptancePlanId=" + planId + "&productCategoryId=" + planInfo.get(0).get("F_SSCPLB")
								// +"&__dbomSql__=F_acceptancePlanId="+planId+"\""
								+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						// 中转机不需要跳转到验收报告
						
						  tdHtml=data.get("xyb").toString(); tdHtml="<span>"+tdHtml+"</span>";
						

						/*
						 * http://192.168.8.127:8080/dp/oa/form/dataTemplate/preview.do?__displayId__=
						 * 10000021410561 &batchId=10000028630062 &batchName=cd-222
						 * &acceptancePlanId=10000028630070 &productCategoryId=10000028630055####
						 */
						break;
					case "生成PDF归档":
						tdHtml = "生成PDF归档";

						planInfo = aLinkDao.getPlanInfoById(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000022580139"
								+ "&batchId=" + planInfo.get(0).get("F_SSCPPC") + "&batchName=" + planInfo.get(0).get("F_CPPC")
								// +"&__dbomSql__=F_acceptancePlanId="+planId+"\""
								+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;

					default:
						tdHtml = data.get("xyb").toString();
						tdHtml = "<span>" + tdHtml + "</span>";

				}
			}else if (data.get("gzm").toString().indexOf("BCSY")!=-1){
				//是靶场
				switch (data.get("xyb").toString()) {
					case "下发表单到PAD":
						String xhId=aLinkDao.getxhIdByPlanIdForRangeTest(planId);
// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029900146&acceptancePlanId=10000031880020&xhId=&__dbomSql__=F_acceptancePlanId=10000031880020
						tdHtml = "下发表单到PAD";
						//planInfo = aLinkDao.getPlanInfoById(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000029900146&acceptancePlanId="
								+  planId
								+"&xhId="+xhId+"&__dbomSql__=F_acceptancePlanId="+planId
								+ "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					case "PAD数据采集":
						tdHtml = "PAD数据采集";
						//获取当前型号
						String ssxh = aLinkDao.getxhIdByPlanIdForRangeTest(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000032960087"
								+ "&__dbomSql__=F_ssxhhpc=" + ssxh + "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;

					case "发起数据确认":
						// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029490089&acceptancePlanId=10000031880020&__dbomSql__=F_ssch=10000031880020
						tdHtml = "发起数据确认";


						// 服务器需要跳转到验收报告
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000029490089&acceptancePlanId="
								+planId
								+"&__dbomSql__=F_ssch="+planId
								+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						// 中转机不需要跳转到验收报告
						
						/* tdHtml=data.get("xyb").toString();*/
						 tdHtml="<span>"+tdHtml+"</span>";
						 

						/*
						 * http://192.168.8.127:8080/dp/oa/form/dataTemplate/preview.do?__displayId__=
						 * 10000021410561 &batchId=10000028630062 &batchName=cd-222
						 * &acceptancePlanId=10000028630070 &productCategoryId=10000028630055####
						 */
						break;
					case "生成PDF归档":
						// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029710069&xhId=10000031850246&__dbomSql__=ID=10000031880020
						tdHtml = "生成PDF归档";
						String xhId_1=aLinkDao.getxhIdByPlanIdForRangeTest(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000029710069&xhId="
								+xhId_1+"&__dbomSql__=ID="+planId
								+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;

					default:
						tdHtml = data.get("xyb").toString();
						tdHtml = "<span>" + tdHtml + "</span>";

				}
			}else if (data.get("gzm").toString().indexOf("WQSJ")!=-1){
				//是所检
				switch (data.get("xyb").toString()) {
					case "下发表单到PAD":
						String xhId=aLinkDao.getxhIdByPlanIdForRangeTest(planId);
// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029900146&acceptancePlanId=10000031880020&xhId=&__dbomSql__=F_acceptancePlanId=10000031880020
						tdHtml = "下发表单到PAD";
						//planInfo = aLinkDao.getPlanInfoById(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000031900644&acceptancePlanId="
								+  planId
								+"&xhId="+xhId+"&__dbomSql__=F_acceptancePlanId="+planId
								+ "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					case "PAD数据采集":
						tdHtml = "PAD数据采集";
						//获取当前型号
						String ssxh = aLinkDao.getxhIdByPlanIdForRangeTest(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000032960087"
								+ "&__dbomSql__=F_ssxhhpc=" + ssxh + "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					case "发起数据确认":
						// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029490089&acceptancePlanId=10000031880020&__dbomSql__=F_ssch=10000031880020
						/*tdHtml = "发起数据确认";*/


						// 服务器需要跳转到验收报告
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000031940111&acceptancePlanId="
								+planId
								+"&__dbomSql__=F_ssch="+planId
								+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						// 中转机不需要跳转到验收报告
						
						 tdHtml=data.get("xyb").toString(); tdHtml="<span>"+tdHtml+"</span>";
						 

						/*
						 * http://192.168.8.127:8080/dp/oa/form/dataTemplate/preview.do?__displayId__=
						 * 10000021410561 &batchId=10000028630062 &batchName=cd-222
						 * &acceptancePlanId=10000028630070 &productCategoryId=10000028630055####
						 */
						break;
					case "生成PDF归档":
						// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029710069&xhId=10000031850246&__dbomSql__=ID=10000031880020
						tdHtml = "生成PDF归档";
						String xhId_1=aLinkDao.getxhIdByPlanIdForRangeTest(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000031940152&xhId="
								+xhId_1+"&__dbomSql__=ID="+planId
								+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;

					default:
						tdHtml = data.get("xyb").toString();
						tdHtml = "<span>" + tdHtml + "</span>";

				}
			}else {
				System.out.println("错误代码:14401440");
			}

		} else {
			tdHtml = data.get("xyb").toString();
			tdHtml = "<span>" + tdHtml + "</span>";
		}

		return tdHtml;
	}

	/**
	 * 工作板 当前状态 的点击事件
	 * 
	 * @param tableName
	 * @param fieldName
	 * @param data
	 * @return
	 * @author zmz
	 */
	public String getWorkboardCurStatus(String tableName, String fieldName, HashAdapter data) {
		String tdHtml = "";
		List<Map<String, Object>> planInfo;
		String useId = UserContextUtil.getCurrentUserId().toString();
		String planId = data.get("show_zjID").toString();

		if (useId.equals(data.get("zzID").toString())) {
			//
			if (data.get("gzm").toString().indexOf("YSCH")!=-1){
				//策划名里有验收
				switch (data.get("dqzt").toString()) {
					case "数据已采集":
						tdHtml = "数据已采集";
						planInfo = aLinkDao.getPlanInfoById(planId);
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000022400033"
								+ "&productBatchId=" + planInfo.get(0).get("F_SSCPPC") + "&acceptancePlanId=" + planId
								+ "&productCategoryId=" + planInfo.get(0).get("F_SSCPLB") + "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					default:
						tdHtml = data.get("dqzt").toString();
						tdHtml = "<span>" + tdHtml + "</span>";
				}
			}else if (data.get("gzm").toString().indexOf("BCSY")!=-1){
				//策划名里有靶场
				// /dp/oa/form/dataTemplate/preview.do?__displayId__=10000029900166&missionId=10000031880020
				switch (data.get("dqzt").toString()) {
					case "数据已采集":
						tdHtml = "数据已采集";
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000029900166&missionId="
								+planId+ "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					default:
						tdHtml = data.get("dqzt").toString();
						tdHtml = "<span>" + tdHtml + "</span>";
				}
			}else {
				//默认剩下的是武器所检
				//displayId待填充
				switch (data.get("dqzt").toString()) {
					case "数据已采集":
						tdHtml = "数据已采集";
						tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000031940042&missionId="
								+planId+ "\""
								+ " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
								+ "</a>";
						break;
					default:
						tdHtml = data.get("dqzt").toString();
						tdHtml = "<span>" + tdHtml + "</span>";
				}
			}

		}else {
			tdHtml = data.get("dqzt").toString();
			tdHtml = "<span>" + tdHtml + "</span>";
		}
		return tdHtml;
	}
	/**
	 * 工作板 点击查看明细页面
	 * 是  工作名  那个栏目的超链接
	 * 是点开看策划的详情的
	 * @author fuyong
	 */
	public String getWorkboardDetail(String tableName, String fieldName, HashAdapter data) {
		String tdHtml = "";
		String useId = UserContextUtil.getCurrentUserId().toString();
		if (useId.equals(data.get("zzID").toString())||useId.equals(CommonTools.Obj2String(data.get("fqrID").toString()))) {
			//判断当前是哪个策划
			tdHtml=data.get("show_gzm").toString();
			String planDisplayId="";
			if (tdHtml.indexOf("YSCH")!=-1){
				//策划名里有验收
				planDisplayId="10000021341344";
			}else if (tdHtml.indexOf("BCSY")!=-1){
				//策划名里有靶场
				planDisplayId="10000028760024";
			}else {
				//默认剩下的是武器所检
				planDisplayId="10000031900580";
			}
			tdHtml = "<a class=\"ops_more\" action=\"/dp/oa/form/dataTemplate/detailData.do?__displayId__="+planDisplayId
					+ "&__pk__=" + data.get("zjID").toString()
					// +"&__dbomSql__=F_acceptancePlanId="+planId+"\""
					+ "\"" + " onclick=\"displayTypeClick({scope:this,type:'hyperlink'},'${data.zjID}')\">" + tdHtml
					+ "</a>";
		}
		else {
			tdHtml=data.get("show_gzm").toString();
			tdHtml = "<span>" + tdHtml + "</span>";
		}
		return tdHtml;
		
	}

	/**
	 * 工作板 当前状态 的点击事件
	 *
	 * @param tableName
	 * @param fieldName
	 * @param data
	 * @return
	 * @author zmz
	 */
	public String getWorkboardTeamLeader(String tableName, String fieldName, HashAdapter data) {
		String tdHtml = "";
		List<Map<String, Object>> planInfo;
		String useId = UserContextUtil.getCurrentUserId().toString();
		String planId = data.get("show_zjID").toString();
			tdHtml = data.get("zz").toString();
			tdHtml = "<span>" + tdHtml + "</span>";
		return tdHtml;
	}
}
