package com.cssrc.ibms.core.user.model;

import com.alibaba.fastjson.JSONArray;
import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.core.util.bean.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * 
 * <p>Title:ParamSearch</p>
 * @author Yangbo 
 * @date 2016-8-6下午03:27:56
 */
public abstract class ParamSearch<T> {
	public abstract List<T> getFromDataBase(Map<String, String> paramMap);

	public List<T> getByParamCollect(String json) throws Exception {
		if ((json == null) || (json.equals("")))
			return null;
		List struct = JSONArray.parseArray(json, SysParamJsonStruct.class);
		if (BeanUtils.isEmpty(struct))
			return null;

		LinkedList<ParamResult> resultList = new LinkedList();
		getByParam(struct, resultList);

		List returnList = new ArrayList();
		if ((resultList != null) && (resultList.size() > 0)) {
			for (ParamResult res : resultList) {
				if (res.getUserList() != null)
					returnList.addAll(res.getUserList());
			}
		}
		return returnList;
	}

	public ParamResult getByParam(List<SysParamJsonStruct> ml,
			LinkedList<ParamResult> resultList) throws Exception {
		for (int i = 0; i < ml.size(); i++) {
			SysParamJsonStruct m = (SysParamJsonStruct) ml.get(i);
			if (m.getBranch().booleanValue()) {
				LinkedList resultListTemp = new LinkedList();
				List sub = m.getSub();
				String compType = m.getCompType();
				if ((!"".equals(compType)) && (compType != null)) {
					int type = 3;
					if ("or".equals(compType)) {
						type = 1;
					} else {
						type = 2;
					}
					ParamResult res = new ParamResult(type, compType);
					resultList.addLast(res);
				}
				ParamResult pr = getByParam(sub, resultListTemp);
				resultList.addLast(pr);
			} else {
				int type = 3;
				String compType = m.getCompType();

				if ((!"".equals(compType)) && (compType != null)) {
					if ("or".equals(compType)) {
						type = 1;
					} else {
						type = 2;
					}
					ParamResult res1 = new ParamResult(type, compType);
					resultList.addLast(res1);
				}
				ParamResult res2 = new ParamResult(type, new String());

				String expression = m.getExpression();
				String dataType = m.getDataType();

				Map property = handlerParam(expression, dataType);

				List ul = getFromDataBase(property);

				res2.add(expression, ul);
				resultList.addLast(res2);
			}
		}

		if (BeanUtils.isEmpty(resultList))
			return null;
		if (resultList.size() % 2 == 0)
			throw new IllegalArgumentException("表达式逻辑错误");

		if (resultList.size() >= 3) {
			while (resultList.size() > 1) {
				ParamResult cur = (ParamResult) resultList.removeFirst();
				ParamResult mid = (ParamResult) resultList.removeFirst();
				ParamResult nex = (ParamResult) resultList.removeFirst();
				if ((cur != null) && (mid != null) && (nex != null)
						&& (mid.getType() != 3)
						&& (BeanUtils.isEmpty(mid.getUserList()))) {
					ParamResult count = new ParamResult(mid.getType(), mid
							.getTypeName());

					count.add("cur", cur.getUserList());
					count.add("nex", nex.getUserList());

					resultList.addFirst(count);
				} else {
					throw new IllegalArgumentException("表达式逻辑错误");
				}
			}
		}
		return (ParamResult) resultList.getFirst();
	}

	protected Map<String, String> handlerParam(String expression,
			String dataType) throws Exception {
		if (expression == null)
			return null;
		int m = -1;

		String condition = null;
		for (Map.Entry ent : ISysParam.CONDITION_US.entrySet()) {
			condition = (String) ent.getKey();
			m = expression.indexOf(condition);
			if (m < 0) {
				condition = (String) ent.getValue();
				m = expression.indexOf(condition);
			}
			if (m > -1)
				break;
		}
		if (m < 0)
			return null;

		String[] tem = expression.split(condition);
		if (tem.length == 2) {
			String paramKey = tem[0].trim();
			String paramValue = tem[1].trim();
			String paramValueColumn = null;

			paramValueColumn = (String) ISysParam.DATA_COLUMN_MAP.get(dataType);
			if (paramValueColumn == null)
				paramValueColumn = "paramValue";
			Map param = new HashMap();
			param.put("paramKey", paramKey);
			param.put("condition", condition);
			param.put("paramValueColumn", paramValueColumn);
			if ((condition == "like") || (condition == "LIKE"))
				param.put("paramValue", "%" + paramValue + "%");
			else
				param.put("paramValue", paramValue);
			System.out.print("[@_@]" + param.toString());
			return param;
		}
		throw new Exception("sql参数不是xxx" + condition + "x形式:" + expression);
	}
}
