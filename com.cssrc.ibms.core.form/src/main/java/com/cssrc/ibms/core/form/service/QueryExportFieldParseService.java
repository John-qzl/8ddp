package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.excel.Excel;
import com.cssrc.ibms.core.excel.editor.IFontEditor;
import com.cssrc.ibms.core.excel.editor.RowEditor;
import com.cssrc.ibms.core.excel.style.Color;
import com.cssrc.ibms.core.excel.style.font.BoldWeight;
import com.cssrc.ibms.core.excel.style.font.Font;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class QueryExportFieldParseService {

	@Resource
	private QueryRightParseService sysQueryRightParseService;

	@Resource
	private QueryFieldService sysQueryFieldService;

	public boolean hasExportField(String exportField) {
		if (StringUtil.isEmpty(exportField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(exportField);
		return jsonAry.size() > 0;
	}

	public String getDefaultExportField(Long sqlId) {
		List<QueryField> sysQueryFields = this.sysQueryFieldService
				.getDisplayFieldListBySqlId(sqlId);
		if (sysQueryFields == null)
			return null;
		JSONArray jsonArray = new JSONArray();
		for (QueryField field : sysQueryFields) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("id", field.getId());
			jsonObject.accumulate("name", field.getName());
			jsonObject.accumulate("desc", field.getFieldDesc());
			jsonObject.accumulate("right", getDefaultExportFieldRight());
			jsonArray.add(jsonObject);
		}
		return jsonArray.toString();
	}

	private String getDefaultExportFieldRight() {
		return this.sysQueryRightParseService.getDefaultRight(Integer
				.valueOf(2));
	}

	public Map<String, Boolean> getExportFieldPermission(int rightTypeExport,
			String exportField, Map<String, Object> rightMap) {
		return this.sysQueryRightParseService.getPermission(rightTypeExport,
				exportField, rightMap);
	}
	public String[]  getSortedExportField(String exportField,
			Map<String, Boolean> exportFieldMap,boolean isDesc) {
		List<String> list = new ArrayList();
		JSONArray exportFieldJsonAry = JSONArray.fromObject(exportField);
		if ((JSONUtil.isEmpty(exportFieldJsonAry))|| (BeanUtils.isEmpty(exportFieldMap)))
			return list.toArray(new String[]{});
		for (Iterator localIterator = exportFieldJsonAry.iterator(); localIterator.hasNext();) {
			Object obj = localIterator.next();
			JSONObject exportFieldJson = JSONObject.fromObject(obj);
			String nameField = exportFieldJson.getString("name");
			Boolean isShowField = (Boolean) exportFieldMap.get(nameField);
			if (isShowField.booleanValue()) {
				if(isDesc) {
					list.add(exportFieldJson.getString("desc"));
				}else {
					list.add(nameField);
				}			
			}
		}
		return list.toArray(new String[]{});
	}
	public Map<String, String> getExportFieldShowName(String exportField,
			Map<String, Boolean> exportFieldMap) {
		Map returnShowName = new HashMap();
		JSONArray exportFieldJsonAry = JSONArray.fromObject(exportField);
		if ((JSONUtil.isEmpty(exportFieldJsonAry))
				|| (BeanUtils.isEmpty(exportFieldMap)))
			return returnShowName;
		for (Iterator localIterator = exportFieldJsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject exportFieldJson = JSONObject.fromObject(obj);
			String nameField = exportFieldJson.getString("name");
			Boolean isShowField = (Boolean) exportFieldMap.get(nameField);
			if (isShowField.booleanValue()) {
				returnShowName
						.put(nameField, exportFieldJson.getString("desc"));
			}
		}
		return returnShowName;
	}

	public List<Map<String, Object>> getRightDataList(
			List<Map<String, Object>> dataList,
			Map<String, Boolean> exportFieldMap) {
		List returnList = new ArrayList();
		for (Map dataMap : dataList) {
			Map flagObj = new HashMap();
			for (Object obj : dataMap.entrySet()) {
				Map.Entry entry = (Map.Entry) obj;
				Boolean isShow = (Boolean) exportFieldMap.get(entry.getKey());
				if ((isShow != null) && (isShow.booleanValue())) {
					flagObj.put((String) entry.getKey(), entry.getValue());
				}
			}
			returnList.add(flagObj);
		}
		return returnList;
	}

	public Excel getExcel(List<Map<String, Object>> rightDataList,String[] sortFieldKey,String[] sortFieldDesc) {
		try {
			Excel excel = new Excel();

			List columnName = Arrays.asList(sortFieldDesc);//mapValueToList(displayFieldName);

			((RowEditor) excel.row(0, 0).value(columnName.toArray()).font(
					new IFontEditor() {
						public void updateFont(Font font) {
							font.boldweight(BoldWeight.BOLD);
						}
					})).bgColor(Color.GREY_25_PERCENT);
			
			for (int i = 0; i < rightDataList.size(); i++) {
				if (BeanUtils.isNotEmpty(rightDataList.get(i))) {
					List values = mapValueToList((Map) rightDataList.get(i),sortFieldKey);

					excel.row(i + 1).value(values.toArray()).dataFormat("@");
				}
			}
			return excel;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private List mapValueToList(Map map,String[] sortedExportField) {
		List list = new ArrayList();
		for(String field : sortedExportField) {
			String value = "";
			if(!BeanUtils.isEmpty(map.get(field))) {
				value = CommonTools.Obj2String(map.get(field));
			}
			list.add(value);
		}
		return list;
	}
	private List mapValueToList(Map map) {
		List list = new ArrayList();
		Set key = map.keySet();
		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			list.add(map.get(s));
		}
		return list;
	}
}
