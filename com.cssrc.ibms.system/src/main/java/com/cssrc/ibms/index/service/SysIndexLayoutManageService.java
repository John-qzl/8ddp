package com.cssrc.ibms.index.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.intf.ICurrentUserService;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysObjRights;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.index.dao.SysIndexLayoutManageDao;
import com.cssrc.ibms.index.model.SysIndexColumn;
import com.cssrc.ibms.index.model.SysIndexLayoutManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysIndexLayoutManageService extends
		BaseService<SysIndexLayoutManage> {

	@Resource
	private SysIndexLayoutManageDao dao;
	@Resource
	private SysIndexColumnService sysIndexColumnService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	private IPositionService positionService;
	@Resource
	private ICurrentUserService currentUserService;

	protected IEntityDao<SysIndexLayoutManage, Long> getEntityDao() {
		return this.dao;
	}

	public SysIndexLayoutManage getLayoutList(Long id,
			List<SysIndexColumn> columnList) {
		SysIndexLayoutManage sysIndexLayoutManage = (SysIndexLayoutManage) this.dao
				.getById(id);
		if (BeanUtils.isEmpty(sysIndexLayoutManage))
			return getDefaultIndexLayout();
		sysIndexLayoutManage.setDesignHtml(this.sysIndexColumnService
				.parserDesignHtml(sysIndexLayoutManage.getDesignHtml(),
						columnList));
		return sysIndexLayoutManage;
	}

	public String getDefaultDesignHtml() {
		return "<div class=\"lyrow ui-draggable\" style=\"display: block;\"><a href=\"#close\" class=\"remove label label-danger\"><i class=\"glyphicon-remove glyphicon\"></i> 删除</a><span class=\"drag label label-default\"><i class=\"glyphicon glyphglyphicon glyphicon-move\"></i> 拖动</span><div class=\"preview\"><input type=\"text\" value=\"一列(12)\" readonly=\"readonly\" class=\"form-control\"></div><div class=\"view\"><div class=\"row clearfix\"><div class=\"col-md-12 column ui-sortable\"></div></div></div></div>";
	}

	private SysIndexLayoutManage getDefaultIndexLayout() {
		String designHtml = getDefaultDesignHtml();
		SysIndexLayoutManage sysIndexLayoutManage = new SysIndexLayoutManage();
		sysIndexLayoutManage.setDesignHtml(designHtml);
		sysIndexLayoutManage.setIsDef(Short.valueOf((short) 0));
		return sysIndexLayoutManage;
	}



	public String getManagerLayout() {
		List list = this.dao.getManageLayout(null, Short.valueOf((short) 1));
		if (BeanUtils.isNotEmpty(list))
			return ((SysIndexLayoutManage) list.get(0)).getTemplateHtml();
		return null;
	}



	public void save(SysIndexLayoutManage sysIndexLayoutManage, int type) {
		short isDef = sysIndexLayoutManage.getIsDef().shortValue();
		if (isDef == 1) {
			Long orgId = sysIndexLayoutManage.getOrgId();
			this.dao.updateIsDef(orgId);
		}
		if (type == 0)
			this.dao.add(sysIndexLayoutManage);
		else
			this.dao.update(sysIndexLayoutManage);
	}
	/**
	 * 根据权限和布局类型获取权限布局
	 *@author YangBo @date 2016年11月30日下午3:19:02
	 *@return
	 */
	 public String getMyHasRightsLayout(){
		 Map<String, List<Long>> relationMap = this.currentUserService.getUserRelation();
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("relationMap", relationMap);
		 params.put("objType", ISysObjRights.RIGHT_TYPE_INDEX_MANAGE);
		 List<SysIndexLayoutManage> list = this.dao.getBySqlKey("getByUserIdFilter", params);
		 if (BeanUtils.isNotEmpty(list))
			 return ((SysIndexLayoutManage)list.get(0)).getTemplateHtml();
		 return "";
	 }
	 
	 
	 public List<SysIndexLayoutManage> getList(QueryFilter filter)
	 {
		 Map params = this.currentUserService.getUserRelation();

		 filter.addFilterForIB("relationMap", params);
		 filter.addFilterForIB("objType", ISysObjRights.RIGHT_TYPE_INDEX_MANAGE);

		 return this.dao.getByUserIdFilter(filter);
	 }
}
