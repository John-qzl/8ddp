package com.cssrc.ibms.record.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.rec.intf.IRecRoleFunService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.record.dao.RecRoleFunDao;
import com.cssrc.ibms.record.model.RecRoleFun;
import com.cssrc.ibms.record.model.RecRoleSonFun;

@Service
public class RecRoleFunService extends BaseService<RecRoleFun> implements IRecRoleFunService{

	@Resource
	private RecRoleFunDao recRoleFunDao;

	protected IEntityDao<RecRoleFun, Long> getEntityDao() {
		return this.recRoleFunDao;
	}
	/**
	 * 给某角色赋值功能点
	 * dataJson中 buttons有（更新）或没有（不变）
	 * @param roleId
	 * @param dataJson
	 * @throws Exception
	 */
	public void update(Long roleId,String dataJson)
			throws Exception {
		List<RecRoleFun> rrfOld = this.recRoleFunDao.getByRoleId(roleId);
		this.recRoleFunDao.delByRoleId(roleId);
		//roleId的值要注意
		if (BeanUtils.isNotEmpty(dataJson)){
			JSONArray jsonAry = JSONArray.fromObject(dataJson);
			if(BeanUtils.isNotEmpty(jsonAry)){
				for (Object obj : jsonAry) {
					JSONObject jsonObject = JSONObject.fromObject(obj);
					Object funId = (Object)jsonObject.get("funId");
					Object buttons = (Object)jsonObject.get("buttons");
					if(BeanUtils.isEmpty(buttons)||buttons.equals("[]")){
						for(RecRoleFun rrf : rrfOld){
							if(rrf.getFunId().equals(funId)){
								buttons = rrf.getButtons();
								break;
							}
						}
					}
					RecRoleFun rofun = new RecRoleFun();
					rofun.setRoleFunId(Long.valueOf(UniqueIdUtil.genId()));
					rofun.setFunId(Long.valueOf(funId.toString()));
					rofun.setButtons(CommonTools.Obj2String(buttons));
					rofun.setRoleId(roleId);
					add(rofun);
				}
			}
		}
	}
	/**
	 * 批量角色授权功能点(具有某个岗位和某个个体可拥有多个角色)
	 * @param roleIds
	 * @param funIds
	 */
	public void saveBatch(Long[] roleIds, Long[] funIds) {
		this.recRoleFunDao.delByRoleAndFun(roleIds, funIds);

		if ((roleIds != null)&& (roleIds.length > 0) && (funIds != null)&& (funIds.length > 0)) {
			Long[] arrayOfLong1;
			int j = (arrayOfLong1 = funIds).length;
			for (int i = 0; i < j; i++) {
				long funId = arrayOfLong1[i].longValue();
				Long[] arrayOfLong2;
				int m = (arrayOfLong2 = roleIds).length;
				for (int k = 0; k < m; k++) {
					long roleId = arrayOfLong2[k].longValue();
					RecRoleFun rofun = new RecRoleFun();
					rofun.setRoleFunId(Long.valueOf(UniqueIdUtil.genId()));
					rofun.setFunId(Long.valueOf(funId));
					rofun.setRoleId(Long.valueOf(roleId));
					add(rofun);
				}
			}
		}
	}
	public void delByFunId(Long funId) {
		this.recRoleFunDao.delByFunId(funId.longValue());
	}

	public void copyRecRoleFun(Long oldRoleId,Long newRoleId) {
		List<RecRoleFun> roleFunList = this.recRoleFunDao
				.getByRoleId(Long.valueOf(oldRoleId));
		for (RecRoleFun rofun : roleFunList) {
			RecRoleFun rolefun = (RecRoleFun) rofun.clone();
			rolefun.setRoleFunId(Long.valueOf(UniqueIdUtil.genId()));
			rolefun.setRoleId(newRoleId);
			this.add(rolefun);
		}
	}
	public List<RecRoleFun> getByRoleAliasFun(String roleAlias, Long funId){
		return this.recRoleFunDao.getByRoleAliasFun(roleAlias, funId);
	}
	/**
	 * 此功能点对应的按钮权限集合
	 * @param roleAlias :角色别名集合
	 * @param funId： 功能点id
	 * @return
	 */
	public Map<String,Boolean> getButtonPemission(String roleAlias, Long funId){
		Map<String,Boolean> map = new HashMap();
		List<RecRoleFun> list = getByRoleAliasFun(roleAlias,funId);
		for(RecRoleFun rrsf : list){
			String buttons = rrsf.getButtons();
			if(BeanUtils.isEmpty(buttons)){
				continue;
			}
			JSONArray bArr = JSONArray.fromObject(buttons);
			for(Object obj : bArr){
				JSONObject b = (JSONObject) obj;
				String id = (String)b.get("id");
				if(BeanUtils.isEmpty(map.get(id))){
					map.put(id, false);
				}
				boolean checked = (boolean)b.get("checked");
				if(checked){
					map.put(id, true);
				}
			}
		}
		return map;
	}	
}
