package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.intf.IDataTemplatefilterService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.model.FilterJsonStruct;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class QueryFilterFieldParseService {

	@Resource
	private QueryRightParseService sysQueryRightParseService;
	@Resource
	IDataTemplatefilterService dataTemplatefilterService;
	
	public String getRightFilterField(QuerySetting sysQuerySetting,
			Map<String, Object> rightMap, String baseURL) {
		String filterField = sysQuerySetting.getFilterField();
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(filterField);
		String destFilterField = new net.sf.json.JSONArray().toString();
		if (JSONUtil.isEmpty(jsonArray)) {
			return destFilterField;
		}

		net.sf.json.JSONArray jsonAry = new net.sf.json.JSONArray();
		for (Iterator localIterator = jsonArray.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jObj = (JSONObject) obj;
			JSONArray rightAry = JSONArray.fromObject(jObj.get("right"));
			JSONObject permission = rightAry.getJSONObject(0);
			if (this.sysQueryRightParseService.hasRight(permission, rightMap))
				jsonAry.add(obj);
		}
		if (JSONUtil.isEmpty(jsonAry)) {
			return destFilterField;
		}
		net.sf.json.JSONArray destJsonAry = new net.sf.json.JSONArray();
		baseURL = baseURL.replace("/getDisplay.do", "/preview.do");
		String url = baseURL + "?__displayId__=" + sysQuerySetting.getId();
		Iterator iterator = jsonAry.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			JSONObject json = (JSONObject) obj;
			String name = json.getString("name");
			String key = json.getString("key");
			json.accumulate("desc", StringUtil.subString(name, 5, "..."));
			json.accumulate("url", url + "&" + "__filterKey__" + "=" + key);
			destJsonAry.add(json);
		}
		return destJsonAry.toString();
	}

	public String getFilterKey(QuerySetting sysQuerySetting,
			Map<String, Object> params) {
		Object key = params.get("__filterKey__");
		if (BeanUtils.isNotEmpty(key))
			return (String) key;
		String filterField = sysQuerySetting.getFilterField();
		if (StringUtil.isEmpty(filterField))
			return "";
		net.sf.json.JSONArray jsonAry = net.sf.json.JSONArray
				.fromObject(filterField);
		if ((JSONUtils.isNull(jsonAry)) || (jsonAry.size() == 0))
			return "";
		JSONObject jsonObj = jsonAry.getJSONObject(0);
		return jsonObj.getString("key");
	}

	public JSONObject getFilterFieldJson(QuerySetting sysQuerySetting,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject filterJson = getFilterFieldJson(sysQuerySetting
				.getFilterField(), rightMap, params);
		if (JSONUtil.isEmpty(filterJson)) {
			net.sf.json.JSONArray jsonAry = new net.sf.json.JSONArray();
			jsonAry.add(getDefaultFilterJson());
			sysQuerySetting.setFilterField(jsonAry.toString());
		}
		return filterJson;
	}

	private JSONObject getDefaultFilterJson() {
		return JSONObject
				.fromObject("{\"name\":\"默认条件 \",\"key\":\"Default\",\"type\":\"1\",\"condition\":\"[]\",\"right\":[{\"s\":3,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}");
	}

	private JSONObject getFilterFieldJson(String filterField,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject filterJson = getFilterJson(filterField, rightMap, params);
		return filterJson;
	}

	private JSONObject getFilterJson(String filterField,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject jsonObj = null;
		net.sf.json.JSONArray jsonAry = net.sf.json.JSONArray
				.fromObject(filterField);
		if (JSONUtil.isEmpty(jsonAry)) {
			return jsonObj;
		}
		String filterKey = (String) params.get("__filterKey__");
		if (StringUtil.isEmpty(filterKey))
			jsonObj = jsonAry.getJSONObject(0);
		else {
			for (Iterator localIterator = jsonAry.iterator(); localIterator
					.hasNext();) {
				Object obj = localIterator.next();
				JSONObject jObj = (JSONObject) obj;
				String key = (String) jObj.get("key");
				if (key.equals(filterKey)) {
					jsonObj = jObj;
					break;
				}
			}
		}
		return jsonObj;
	}

	public String getFilterSQL(JSONObject filterJson) {
		if (JSONUtil.isEmpty(filterJson)) {
			return "";
		}
		List operatorList = new ArrayList();

		List filters = com.alibaba.fastjson.JSONArray.parseArray(filterJson
				.get("condition").toString(), FilterJsonStruct.class);
		getFilterResult(filters, operatorList);

		return executeOperator(operatorList);
	}

	private void getFilterResult(List<FilterJsonStruct> filters,
			List<Map<String, Object>> operatorList) {
		for (FilterJsonStruct filter : filters) {
			if (filter.getBranch().booleanValue()) {
				List branchResultList = new ArrayList();
				getFilterResult(filter.getSub(), branchResultList);
				String branchResult = executeOperator(branchResultList);
				Map resultMap = getResultMap(filter.getCompType(), branchResult);
				operatorList.add(resultMap);
			} else if (filter.getRuleType().intValue() == 2) {
				String script = filter.getScript();
				if (StringUtil.isNotEmpty(script)) {
					Map resultMap = getResultMap(filter.getCompType(), script);
					operatorList.add(resultMap);
				}
			} else {
				getNormalFilterResult(filter, operatorList);
			}
		}
	}

	private void getNormalFilterResult(FilterJsonStruct filter,
			List<Map<String, Object>> operatorList) {
		String flowvarKey = filter.getFlowvarKey();

		String script = "";
		switch (filter.getOptType().intValue()) {
		case 1:
		case 2:
			if (StringUtils.isNotEmpty(filter.getJudgeVal1())) {
				script = getCompareScript(filter.getJudgeCon1(), flowvarKey,
						filter.getJudgeVal1(), filter.getOptType().intValue(),
						filter.getIsHidden().intValue());
			}

			if (StringUtils.isNotEmpty(filter.getJudgeVal2())) {
				String moreScript = getCompareScript(filter.getJudgeCon2(),
						flowvarKey, filter.getJudgeVal2(), filter.getOptType()
								.intValue(), filter.getIsHidden().intValue());
				if (StringUtils.isNotEmpty(script))
					script = script + " AND ";
				script = script + moreScript;
			}
			break;
		case 3:
			if (StringUtils.isNotEmpty(filter.getJudgeVal1())) {
				String val1 = sqlToDate(filter.getJudgeVal1(), filter
						.getDatefmt());
				script = getCompareScript(filter.getJudgeCon1(), flowvarKey,
						val1, filter.getOptType().intValue(), filter
								.getIsHidden().intValue());
			}

			if (StringUtils.isNotEmpty(filter.getJudgeVal2())) {
				String val2 = sqlToDate(filter.getJudgeVal2(), filter
						.getDatefmt());
				String moreScript = getCompareScript(filter.getJudgeCon2(),
						flowvarKey, val2, filter.getOptType().intValue(),
						filter.getIsHidden().intValue());
				if (StringUtils.isNotEmpty(script))
					script = script + " AND ";
				script = script + moreScript;
			}
			break;
		case 4:
			String[] vals = filter.getJudgeVal1().split("&&");
			for (String val : vals) {
				if (StringUtils.isNotEmpty(script))
					script = script + " AND ";
				script = script
						+ getCompareScript(filter.getJudgeCon1(), flowvarKey,
								val, filter.getOptType().intValue(), filter
										.getIsHidden().intValue());
			}
			break;
		case 5:
			String judgeCon = filter.getJudgeCon1();
			String[] ids = filter.getJudgeVal1().split("&&");
			if (ids.length == 2) {
				script = getCompareScript(judgeCon, filter.getFlowvarKey(),
						ids[0], filter.getOptType().intValue(), filter
								.getIsHidden().intValue());
			} else if (("3".equalsIgnoreCase(judgeCon))
					|| ("4".equalsIgnoreCase(judgeCon))) {
				script = getCompareScript(judgeCon, filter.getFlowvarKey(),
						filter.getJudgeVal1(), filter.getOptType().intValue(),
						filter.getIsHidden().intValue());
			}

			break;
		case 6:// 人员扩展后单独作为一个选择器不和角色、组织、岗位选择器混合使用
            String judgeCon1 = filter.getJudgeCon1();
            String[] ids1 = filter.getJudgeVal1().split("&&");
            if (ids1.length == 2)
            {
                script =
                    getCompareScript(judgeCon1,
                        filter.getFlowvarKey(),
                        ids1[0],
                        filter.getOptType(),
                       // table,
                        //source,
                        filter.getIsHidden());
            }
            else
            {// 特殊类型的
                switch (judgeCon1){
                    case "3":
                    case "4":
                    case "8":
                    case "11":
                    case "12":
                    case "13":
                        script =
                        getCompareScript(judgeCon1,
                            filter.getFlowvarKey(),
                            filter.getJudgeVal1(),
                            filter.getOptType(),
                            //table,
                           // source,
                            filter.getIsHidden()); 
                        break;
                }
                
            }
            break;
		}

		if (StringUtil.isEmpty(script)) {
			return;
		}
		Map resultMap = getResultMap(filter.getCompType(), script);
		operatorList.add(resultMap);
	}

	private String sqlToDate(String val, String datefmt) {
		StringBuffer sb = new StringBuffer();

		sb.append("TO_DATE('").append(val).append("','").append(
				StringUtils.isEmpty(datefmt) ? "yyyy-MM-dd" : datefmt).append(
				"')");
		return sb.toString();
	}

	private String getCompareScript(String judgeCon, String fieldName,
			String judgeVal, int type, int isHidden) {
		StringBuffer sb = new StringBuffer();
		switch (type) {
		case 1:
		case 3:
			if ("1".equals(judgeCon))
				sb.append(fieldName).append("=").append(judgeVal);
			else if ("2".equals(judgeCon))
				sb.append(fieldName).append("!=").append(judgeVal);
			else if ("3".equals(judgeCon))
				sb.append(fieldName).append(">").append(judgeVal);
			else if ("4".equals(judgeCon))
				sb.append(fieldName).append(">=").append(judgeVal);
			else if ("5".equals(judgeCon))
				sb.append(fieldName).append("<").append(judgeVal);
			else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append("<=").append(judgeVal);
			}
			break;
		case 2:
		case 4:
			if ("1".equals(judgeCon))
				sb.append(fieldName).append("=").append("'").append(judgeVal)
						.append("'");
			else if ("2".equals(judgeCon))
				sb.append(fieldName).append("!=").append("'").append(judgeVal)
						.append("'");
			else if ("3".equals(judgeCon))
				sb.append("UPPER(").append(fieldName).append(")=").append(
						" UPPER('").append(judgeVal).append("')");
			else if ("4".equals(judgeCon))
				sb.append(fieldName).append(" LIKE").append(" '%").append(
						judgeVal).append("%'");
			else if ("5".equals(judgeCon))
				sb.append(fieldName).append(" LIKE").append(" '").append(
						judgeVal).append("%'");
			else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%").append(
						judgeVal).append("'");
			}
			break;
		case 5:
			if (isHidden == 0&&!fieldName.endsWith(TableModel.PK_COLUMN_NAME)) {
				fieldName = fieldName + TableModel.PK_COLUMN_NAME;
			}
			if ("1".equals(judgeCon))
				sb.append(fieldName).append(" in (").append("")
						.append(judgeVal).append(")");
			else if ("2".equals(judgeCon))
				sb.append(fieldName).append(" not in (").append("").append(
						judgeVal).append(")");
			else if ("3".equals(judgeCon))
				sb.append(fieldName).append(" = :").append("").append(judgeVal)
						.append("");
			else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" != :").append("")
						.append(judgeVal).append("");
			}
			break;
		case 6:// 人员选择器
            if (isHidden == FormField.NO_HIDDEN&&!fieldName.endsWith(TableModel.PK_COLUMN_NAME)){
                fieldName = fieldName + TableModel.PK_COLUMN_NAME;
            }
            switch(judgeCon){
                case "1":
                    sb.append(fieldName).append(" in (").append("")
                    .append(judgeVal).append(")");
                    break;
                case "2":
                    sb.append(fieldName).append(" not in (").append("")
                    .append(judgeVal).append(")");
                    break;
                case "3":
                    sb.append(fieldName).append(" = :").append("").append(judgeVal)
                    .append("");
                    break;
                case "4":
                    sb.append(fieldName).append(" != :").append("")
                    .append(judgeVal).append("");
                    break;
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                case "11":
                case "12":
                case "13":
                case "14":
                case "15":
                case "16":
                case "17":
                    sb.append(fieldName).append(" in (").append("")
                    .append(getUserByjudgeVal(judgeCon,judgeVal)).append(")");
                    break;
            }
            break;
		default:
			break;
			
		}

		return sb.toString();
	}
	private String getUserByjudgeVal(String judgeCon, String judgeVal)
    {
        StringBuffer userIds=new StringBuffer();
        Long userId=UserContextUtil.getCurrentUserId();
        List<? extends ISysUser> users=dataTemplatefilterService.invoke(judgeCon,judgeVal,userId);
        for(ISysUser u:users){
            userIds.append(u.getUserId()+",");
        }
        if(userIds.length()>0){
            return userIds.substring(0, userIds.length()-1);
        }else{
            return null;
        }
    }
	private Map<String, Object> getResultMap(String operator, String result) {
		Map resultMap = new HashMap();
		resultMap.put("operator", operator);
		resultMap.put("result", result);
		return resultMap;
	}

	private String executeOperator(List<Map<String, Object>> operatorList) {
		if (operatorList.size() == 0)
			return "";
		String returnVal = (String) ((Map) operatorList.get(0)).get("result");
		if (operatorList.size() == 1)
			return returnVal;
		int size = operatorList.size();
		for (int k = 1; k < size; k++) {
			Map resultMap = (Map) operatorList.get(k);
			String operator = resultMap.get("operator").toString();
			if ("or".equals(operator))
				returnVal = "(" + returnVal + ") OR ("
						+ resultMap.get("result") + ")";
			else if ("and".equals(operator)) {
				returnVal = "(" + returnVal + ") AND ("
						+ resultMap.get("result") + ")";
			}
		}
		if (StringUtils.isNotEmpty(returnVal))
			returnVal = "(" + returnVal + ")";
		return returnVal;
	}
}