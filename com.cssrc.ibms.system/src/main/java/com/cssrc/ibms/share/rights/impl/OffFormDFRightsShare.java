package com.cssrc.ibms.share.rights.impl;

import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.bus.service.BusQueryRuleService;
import com.cssrc.ibms.share.rights.DataTemplateVO;
import com.cssrc.ibms.share.rights.FormDFRightShare;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:OffFormDFRightsShare</p>
 * @author Yangbo 
 * @date 2016-8-9下午04:38:36
 */
@Service
public class OffFormDFRightsShare extends FormDFRightShare {

	@Resource
	BusQueryRuleService busQueryRuleService;
	BusQueryRule busQueryRule;

	public String getShareType() {
		return "offLineFormDF";
	}

	public String getShareDesc() {
		return "离线表单数据权限";
	}

	public DataTemplateVO getDataObject(String id) {
		DataTemplateVO vo = new DataTemplateVO();
		BusQueryRule busQueryRule = (BusQueryRule) this.busQueryRuleService
				.getById(Long.valueOf(Long.parseLong(id)));
		vo.setDisplayField(busQueryRule.getDisplayField());
		vo.setExportField(busQueryRule.getExportField());
		vo.setFilterField(busQueryRule.getFilterField());
		vo.setPrintField(busQueryRule.getPrintField());
		return vo;
	}

	public DataTemplateVO getDataTemplateVO(String ruleId) {
		this.busQueryRule = ((BusQueryRule) this.busQueryRuleService
				.getById(Long.valueOf(Long.parseLong(ruleId))));
		DataTemplateVO vo = new DataTemplateVO();
		vo.setDisplayField(this.busQueryRule.getDisplayField());
		vo.setExportField(this.busQueryRule.getExportField());
		vo.setFilterField(this.busQueryRule.getFilterField());
		vo.setPrintField(this.busQueryRule.getPrintField());
		return vo;
	}

	public void updateDataTemplateVO(DataTemplateVO vo) {
		this.busQueryRule.setDisplayField(vo.getDisplayField());
		this.busQueryRule.setExportField(vo.getExportField());
		this.busQueryRule.setFilterField(vo.getFilterField());
		this.busQueryRule.setPrintField(vo.getPrintField());
		this.busQueryRuleService.update(this.busQueryRule);
	}
}
