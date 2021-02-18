package com.cssrc.ibms.share.rights;

import com.cssrc.ibms.core.db.mybatis.query.QueryUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.share.dao.SysShareRightsDao;
import com.cssrc.ibms.share.model.SysShareRights;

import java.util.Iterator;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * <p>Title:FormDFRightShare</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:20:29
 */
public abstract class FormDFRightShare implements IShareRightsService {

	@Resource
	protected SysShareRightsDao sysShareRightsDao;
	protected SysShareRights sysShareRights;

	@Resource
	protected ShareRightsCalc shareRightsCalc;

	public abstract DataTemplateVO getDataTemplateVO(String paramString);

	public abstract void updateDataTemplateVO(DataTemplateVO paramDataTemplateVO);

	public void addShare(SysShareRights sysShareRights) {
		this.sysShareRights = sysShareRights;
		Long id = sysShareRights.getId();
		JSONObject shareItem = JSONObject.fromObject(sysShareRights
				.getShareItem());
		String[] ruleIds = shareItem.getString("ids").split(",");
		SysShareRights oldSysShareRights = (SysShareRights) this.sysShareRightsDao
				.getById(id);
		Boolean isAdd = Boolean.valueOf(BeanUtils.isEmpty(oldSysShareRights));
		if (!isAdd.booleanValue())
			this.shareRightsCalc.removeShareRights(oldSysShareRights);
		if (sysShareRights.getEnable() == 1)
			for (String ruleId : ruleIds) {
				JSONObject shareRule = shareItem.getJSONObject("v_" + ruleId);
				if (!BeanUtils.isEmpty(shareRule)) {
					DataTemplateVO bpmDataTemplate = getDataTemplateVO(ruleId);

					JSONArray brDisplayJa = JSONArray
							.fromObject(bpmDataTemplate.getDisplayField());
					setDetail(shareRule.getJSONObject("display"), brDisplayJa,
							(short) 0);
					bpmDataTemplate.setDisplayField(brDisplayJa.toString());

					JSONArray brManagerJa = JSONArray
							.fromObject(bpmDataTemplate.getManageField());
					setDetail(shareRule.getJSONObject("manager"), brManagerJa,
							(short) 4);
					bpmDataTemplate.setManageField(brManagerJa.toString());

					JSONArray brFilterJa = JSONArray.fromObject(bpmDataTemplate
							.getFilterField());
					setDetail(shareRule.getJSONObject("filter"), brFilterJa,
							(short) 3);
					bpmDataTemplate.setFilterField(brFilterJa.toString());

					JSONArray brExportsJa = JSONArray
							.fromObject(bpmDataTemplate.getExportField());
					setDetail(shareRule.getJSONObject("exports"), brExportsJa
							.getJSONObject(0).getJSONArray("fields"), (short) 2);
					bpmDataTemplate.setExportField(brExportsJa.toString());

					updateDataTemplateVO(bpmDataTemplate);
				}
			}
	}

	public void removeShareRights(SysShareRights sysShareRights) {
		String[] ruleIds = JSONObject.fromObject(sysShareRights.getShareItem())
				.getString("ids").split(",");
		for (String ruleId : ruleIds) {
			DataTemplateVO bpmDataTemplate = getDataTemplateVO(ruleId);
			JSONArray brDisplayJa = JSONArray.fromObject(bpmDataTemplate
					.getDisplayField());
			removeShareDetail(brDisplayJa);
			bpmDataTemplate.setDisplayField(brDisplayJa.toString());

			JSONArray brManagerJa = JSONArray.fromObject(bpmDataTemplate
					.getManageField());
			removeShareDetail(brManagerJa);
			bpmDataTemplate.setManageField(brManagerJa.toString());

			JSONArray brFilterJa = JSONArray.fromObject(bpmDataTemplate
					.getFilterField());
			removeShareDetail(brFilterJa);
			bpmDataTemplate.setFilterField(brFilterJa.toString());

			JSONArray brExportsJa = JSONArray.fromObject(bpmDataTemplate
					.getExportField());
			removeShareDetail(brExportsJa.getJSONObject(0).getJSONArray(
					"fields"));
			bpmDataTemplate.setExportField(brExportsJa.toString());

			updateDataTemplateVO(bpmDataTemplate);
		}
	}

	public void setDetail(JSONObject srDf, JSONArray brJa, short type) {
		JSONObject target = JSONObject.fromObject(this.sysShareRights
				.getTarget());
		boolean isAll = this.sysShareRights.getIsAll() == 1;
		Iterator srDfKeys = srDf.keys();
		while (srDfKeys.hasNext()) {
			String srDfKey = (String) srDfKeys.next();
			JSONObject srDfKeyJo = srDf.getJSONObject(srDfKey);
			if ((isAll) || (srDfKeyJo.getBoolean("r"))) {
				Iterator brIt = brJa.iterator();
				while (brIt.hasNext()) {
					JSONObject dfItNext = (JSONObject) brIt.next();
					String v = QueryUtil.getRightsName(dfItNext);
					if (v.equals(srDfKey)) {
						JSONObject newRights = new JSONObject();
						newRights.put("s", Short.valueOf((short) type));
						String t = "";
						if (target.containsKey("v"))
							t = target.getString("v");
						else if (target.containsKey("type"))
							t = target.getString("type");
						newRights.put("type", t);
						newRights.put("id", target.getString("ids"));
						newRights.put("name", target.getString("names"));
						newRights.put("srid", this.sysShareRights.getId());
						newRights
								.put("source", this.sysShareRights.getSource());
						JSONArray dfRightsArr = dfItNext.getJSONArray("right");
						dfRightsArr.add(newRights);

						break;
					}
				}
			}
		}
	}

	public void removeShareDetail(JSONArray brJa) {
		if (brJa.toString().equalsIgnoreCase("[null]"))
			return;
		Iterator brIt = brJa.iterator();
		while (brIt.hasNext()) {
			JSONObject dfItNext = (JSONObject) brIt.next();
			JSONArray dfRightsArr = dfItNext.getJSONArray("right");
			JSONArray removeArr = new JSONArray();
			for (int i = 0; i < dfRightsArr.size(); i++) {
				JSONObject jsonObject = dfRightsArr.getJSONObject(i);
				if (jsonObject.containsKey("srid")) {
					String srid = jsonObject.getString("srid");
					if ((StringUtil.isNotEmpty(srid))
							&& (srid.equals(this.sysShareRights.getId()
									.toString())))
						removeArr.add(jsonObject);
				}
			}
			dfRightsArr.removeAll(removeArr);
		}
	}
}
