package com.cssrc.ibms.core.form.service;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.model.QuerySql;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;


@Service
public class QueryManageFieldParseService {

	@Resource
	private QuerySqlService sysQuerySqlService;

	@Resource
	private QueryRightParseService sysQueryRightParseService;

	public boolean hasManageField(String manageField) {
		if (StringUtil.isEmpty(manageField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		return jsonAry.size() > 0;
	}

	public String getDefaultManageButton(Long sqlId,String manageFields) {
		JSONArray jsonArray = new JSONArray();
		//解析当前管理列json
		if(StringUtil.isNotEmpty(manageFields)){
			jsonArray = JSONArray.fromObject(manageFields);
		}else{
			JSONObject exportJsonObject = getDefaultExportButton();
			jsonArray.add(exportJsonObject);
		}
		//从数据库中查询出设置的管理列
		QuerySql sysQuerySql = this.sysQuerySqlService.getById(sqlId);

		if ((sysQuerySql != null)&& (StringUtil.isNotEmpty(sysQuerySql.getUrlParams()))) {
			JSONArray manageJsonArray = JSONArray.fromObject(sysQuerySql.getUrlParams());
			for (Iterator localIterator = manageJsonArray.iterator(); localIterator
					.hasNext();) {
				Object obj = localIterator.next();
				JSONObject jsonObject = JSONObject.fromObject(obj);
				boolean add = true;
				//比较两者是否为相同的object。
				for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();){
					Object iteratorObj = iterator.next();
					JSONObject iteratorObject = JSONObject.fromObject(iteratorObj);
					if(iteratorObject.get("name").equals(jsonObject.get("name"))
					&&iteratorObject.get("desc").equals(jsonObject.get("desc"))
					&&iteratorObject.get("urlPath").equals(jsonObject.get("urlPath"))
					){
						add = false;
						break;
					}
				}
				//没有相同的情况则加按钮
				if(add){
					jsonObject.accumulate("right", getDefaultManageFieldRight());
					jsonArray.add(jsonObject);
				}
			}
		}
		return jsonArray.toString();
	}

	private JSONObject getDefaultExportButton() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("name", "exportButton");
		jsonObject.accumulate("desc", "导出");
		jsonObject.accumulate("right", getDefaultManageFieldRight());
		return jsonObject;
	}

	public String getDefaultManageFieldRight() {
		return this.sysQueryRightParseService.getDefaultRight(Integer
				.valueOf(4));
	}

	public String getRightManage(QuerySetting sysQuerySetting,
			Map<String, Object> rightMap) {
		JSONArray manageJsonArray = JSONArray.fromObject(sysQuerySetting
				.getManageField());
		Map<String, Boolean> managePermission = this.sysQueryRightParseService.getPermission(4,
				sysQuerySetting.getManageField(), rightMap);
		JSONArray jsonArrayTemp = new JSONArray();
		for (Iterator localIterator = manageJsonArray.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jsonObject = JSONObject.fromObject(obj);
			boolean hasPerminssion = ((Boolean) managePermission.get(jsonObject
					.get("name"))).booleanValue();

			if (hasPerminssion) {
				JSONObject jsonTemp = new JSONObject();

				if (!"exportButton".equals(jsonObject.get("name"))) {
					String url = mergeUrlAndParams(jsonObject.getString("urlPath"), jsonObject.getString("urlParams"));
					jsonTemp.accumulate("url", url);
				}
				jsonTemp.accumulate("name", jsonObject.get("name"));
				jsonTemp.accumulate("desc", jsonObject.get("desc"));
				jsonTemp.accumulate("className", CommonTools.Obj2String(jsonObject.get("className")));
				jsonTemp.accumulate("clickName", CommonTools.Obj2String(jsonObject.get("clickName")));
				jsonTemp.accumulate("paramscript", CommonTools.Obj2String(jsonObject.get("paramscript")));
				jsonTemp.accumulate("prescript", CommonTools.Obj2String(jsonObject.get("prescript")));
				jsonTemp.accumulate("afterscript", CommonTools.Obj2String(jsonObject.get("afterscript")));
				jsonArrayTemp.add(jsonTemp);
			}
		}
		return jsonArrayTemp.toString();
	}

	private String mergeUrlAndParams(String urlPath, String urlParams) {
		urlPath = "${ctx}" + urlPath;
		String otherParam = "";
		if (StringUtil.isEmpty(urlParams))
			return urlPath;
		StringBuffer sb = new StringBuffer();
		int idx1 = urlPath.indexOf("?");
		if (idx1 > 0){
			sb.append(urlPath.substring(0, idx1));
			otherParam = urlPath.substring(idx1+1,urlPath.length());
		}else{
			sb.append(urlPath);
		}
		sb.append("?");
		JSONArray paramsJsonArray = JSONArray.fromObject(urlParams);
		for (Iterator localIterator = paramsJsonArray.iterator(); localIterator.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jsonObject = JSONObject.fromObject(obj);
			String isCustomParam = jsonObject.getString("isCustomParam");
			String key = jsonObject.getString("key");
			String value = jsonObject.getString("value");
			if (1 == Short.valueOf(isCustomParam).shortValue()) {
				sb.append(key);
				sb.append("=");
				if (StringUtil.isNotEmpty(value)) {
					sb.append(value);
				}
				sb.append("&");
			} else {
				sb.append(key);
				sb.append("=");
				sb.append("[" + value + "]");
				sb.append("&");
			}
		}
		sb.append(otherParam);
		return sb.toString();
	}
	
	public Map<String, Boolean> getManagePermission(int rightTypeManage,
			String manageField, Map<String, Object> rightMap) {
		return this.sysQueryRightParseService.getPermission(rightTypeManage,
				manageField, rightMap);
	}

	public boolean hasManageButton(String manageField,
			Map<String, Object> rightMap) {
		Map managePermission = this.sysQueryRightParseService.getPermission(4,
				manageField, rightMap);

		if (managePermission.containsKey("exportButton")) {
			managePermission.remove("exportButton");
		}
		return managePermission.containsValue(Boolean.valueOf(true));
	}
}
