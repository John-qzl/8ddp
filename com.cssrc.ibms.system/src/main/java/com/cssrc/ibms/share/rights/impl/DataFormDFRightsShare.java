package com.cssrc.ibms.share.rights.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.share.rights.DataTemplateVO;
import com.cssrc.ibms.share.rights.FormDFRightShare;

@Service
public class DataFormDFRightsShare extends FormDFRightShare {

	@Resource
	IDataTemplateService dataTemplateService;
	@Resource
	IFormDefService formDefService;
	IDataTemplate dataTemplate;
	public String getShareType() {
		return "formDTDF";
	}

	public String getShareDesc() {
		return "业务数据模板数据权限";
	}

	public DataTemplateVO getDataObject(String id) {
		DataTemplateVO vo = new DataTemplateVO();
		IDataTemplate dataTemplate = this.dataTemplateService.getByFormKey(Long
				.valueOf(id).longValue());
		if (dataTemplate == null)
			dataTemplate = this.dataTemplateService
					.getByFormKey(((IFormDef) this.formDefService.getById(Long
							.valueOf(Long.parseLong(id)))).getFormKey());
		vo.setDisplayField(dataTemplate.getDisplayField());
		vo.setExportField(dataTemplate.getExportField());
		vo.setFilterField(dataTemplate.getFilterField());
		vo.setManageField(dataTemplate.getManageField());
		vo.setPrintField(dataTemplate.getPrintField());
		return vo;
	}

	public DataTemplateVO getDataTemplateVO(String ruleId) {
		this.dataTemplate = this.dataTemplateService.getByFormKey(Long.valueOf(
				ruleId).longValue());
		DataTemplateVO vo = new DataTemplateVO();
		vo.setDisplayField(this.dataTemplate.getDisplayField());
		vo.setExportField(this.dataTemplate.getExportField());
		vo.setFilterField(this.dataTemplate.getFilterField());
		vo.setManageField(this.dataTemplate.getManageField());
		vo.setPrintField(this.dataTemplate.getPrintField());
		return vo;
	}

	public void updateDataTemplateVO(DataTemplateVO vo) {
		this.dataTemplate.setDisplayField(vo.getDisplayField());
		this.dataTemplate.setExportField(vo.getExportField());
		this.dataTemplate.setFilterField(vo.getFilterField());
		this.dataTemplate.setManageField(vo.getManageField());
		this.dataTemplate.setPrintField(vo.getPrintField());
		this.dataTemplateService.update(this.dataTemplate);
	}
}
