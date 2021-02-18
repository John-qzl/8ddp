package com.cssrc.ibms.index.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.index.dao.SysIndexMyLayoutDao;
import com.cssrc.ibms.index.model.SysIndexColumn;
import com.cssrc.ibms.index.model.SysIndexMyLayout;
import com.cssrc.ibms.init.model.Config;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class SysIndexMyLayoutService extends BaseService<SysIndexMyLayout> {

	@Resource
	private SysIndexMyLayoutDao dao;

	@Resource
	private SysIndexLayoutManageService sysIndexLayoutManageService;

	@Resource
	private SysIndexColumnService sysIndexColumnService;

	protected IEntityDao<SysIndexMyLayout, Long> getEntityDao() {
		return this.dao;
	}

	public String obtainMyIndexData(Long userId) {
		String TempalteHtml = "";
		SysIndexMyLayout sysIndexMyLayout = this.dao.getByUserId(userId);
		if ((BeanUtils.isNotEmpty(sysIndexMyLayout)) && (sysIndexMyLayout.getTemplateHtml() != null)) {
			TempalteHtml = sysIndexMyLayout.getTemplateHtml();
		}
		String html = this.sysIndexLayoutManageService.getMyHasRightsLayout();
		if (BeanUtils.isNotEmpty(html)) {
			TempalteHtml = TempalteHtml + html;
		}
		//初始的模板(当未设置布局模板则用默认)
		if (BeanUtils.isEmpty(TempalteHtml)/*&& (UserContextUtil.isSuperAdmin())*/) {
			TempalteHtml = defaultIndexLayout();
		}
		return TempalteHtml;
	}

	private String defaultIndexLayout() {
		String path = SysConfConstant.FTL_ROOT + "index" + File.separator + "templates"
				+ File.separator + "defaultIndexPages.ftl";
		String templateHtml = FileUtil.readFile(path);
		return templateHtml;
	}

	public SysIndexMyLayout getLayoutList(Long userId,
			List<SysIndexColumn> columnList) {
		SysIndexMyLayout sysIndexMyLayout = this.dao.getByUserId(userId);
		if (BeanUtils.isEmpty(sysIndexMyLayout))
			return getDefaultIndexLayout();
		sysIndexMyLayout
				.setDesignHtml(this.sysIndexColumnService.parserDesignHtml(
						sysIndexMyLayout.getDesignHtml(), columnList));
		return sysIndexMyLayout;
	}

	private SysIndexMyLayout getDefaultIndexLayout() {
		SysIndexMyLayout sysIndexMyLayout = new SysIndexMyLayout();
		sysIndexMyLayout.setDesignHtml(this.sysIndexLayoutManageService
				.getDefaultDesignHtml());
		return sysIndexMyLayout;
	}

	public void save(String html, String designHtml) {
		Long userId = UserContextUtil.getCurrentUserId();
		SysIndexMyLayout sysIndexMyLayout = this.dao.getByUserId(userId);
		if (BeanUtils.isEmpty(sysIndexMyLayout)) {
			sysIndexMyLayout = new SysIndexMyLayout();
			sysIndexMyLayout.setDesignHtml(designHtml);
			sysIndexMyLayout.setTemplateHtml(html);
			sysIndexMyLayout.setId(Long.valueOf(UniqueIdUtil.genId()));
			sysIndexMyLayout.setUserId(userId);
			this.dao.add(sysIndexMyLayout);
		} else {
			sysIndexMyLayout.setDesignHtml(designHtml);
			sysIndexMyLayout.setTemplateHtml(html);
			this.dao.update(sysIndexMyLayout);
		}
	}
}
