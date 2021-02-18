package com.cssrc.ibms.record.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.rec.intf.IRecRoleSonFunService;
import com.cssrc.ibms.api.rec.model.IRecRoleSonFun;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.record.dao.RecRoleFunDao;
import com.cssrc.ibms.record.dao.RecRoleSonFunDao;
import com.cssrc.ibms.record.model.RecRoleFun;
import com.cssrc.ibms.record.model.RecRoleSonFun;

@Service
public class RecRoleSonFunService extends BaseService<RecRoleSonFun> implements IRecRoleSonFunService{

	@Resource
	private RecRoleSonFunDao recRoleSonFunDao;
	@Resource
	private RecRoleFunDao recRoleFunDao;
	
	protected IEntityDao<RecRoleSonFun, Long> getEntityDao() {
		return this.recRoleSonFunDao;
	}
	/**
	 * 给某角色赋值功能点
	 * dataJson中 buttons有（更新）或没有（不变）
	 * @param roleSonId
	 * @param dataJson
	 * @throws Exception
	 */
	public void update(Long roleSonId,String dataJson)
			throws Exception {
		List<RecRoleSonFun> rrsfOld = this.recRoleSonFunDao.getByRoleSonId(roleSonId);
		this.recRoleSonFunDao.delByRoleSonId(roleSonId);
		//roleSonId的值要注意
		if (BeanUtils.isNotEmpty(dataJson)){
			JSONArray jsonAry = JSONArray.fromObject(dataJson);
			if(BeanUtils.isNotEmpty(jsonAry)){
				for (Object obj : jsonAry) {
					JSONObject jsonObject = JSONObject.fromObject(obj);
					Object funId = (Object)jsonObject.get("funId");
					Object buttons = (Object)jsonObject.get("buttons");
					if(BeanUtils.isEmpty(buttons)||buttons.equals("[]")){
						for(RecRoleSonFun rrsf : rrsfOld){
							if(rrsf.getFunId().equals(funId)){
								buttons = rrsf.getButtons();
								break;
							}
						}
					}
					RecRoleSonFun rofunson = new RecRoleSonFun();
					rofunson.setRoleSonFunId(Long.valueOf(UniqueIdUtil.genId()));
					rofunson.setFunId(Long.valueOf(funId.toString()));
					rofunson.setButtons(CommonTools.Obj2String(buttons));
					rofunson.setRoleSonId(roleSonId);
					add(rofunson);
				}
			}
		}
	}
	/**
	 * @param funId
	 */
	public void delByFunId(Long funId) {
		this.recRoleSonFunDao.delByFunId(funId.longValue());
	}
	/**
	 * @param roleSonId
	 */
	public void delByRoleSonId(Long roleSonId) {
		this.recRoleSonFunDao.delByRoleSonId(roleSonId);
	}

	/**
	 * 初始化
	 * @param roleId
	 * @param roleSonId
	 */
	public void synRecRoleSonFun(Long roleId,Long roleSonId) {

		List<RecRoleFun> roleFunList = this.recRoleFunDao
				.getByRoleId(Long.valueOf(roleId));
		for (RecRoleFun rofun : roleFunList) {
			RecRoleSonFun rrsf = new RecRoleSonFun();
			rrsf.setRoleSonFunId(Long.valueOf(UniqueIdUtil.genId()));
			rrsf.setRoleSonId(roleSonId);
			rrsf.setFunId(rofun.getFunId());
			rrsf.setButtons(rofun.getButtons());
			this.add(rrsf);
		}
	}
	/* (non-Javadoc)
	 * @see com.cssrc.ibms.api.rec.intf.IRecRoleSonFunService#getByRoleAliasFun(java.lang.String, java.lang.Long)
	 */
	public List<RecRoleSonFun> getByRoleAliasFun(String roleAlias, Long funId,Long dataId){
		return this.recRoleSonFunDao.getByRoleAliasFun(roleAlias, funId,dataId);
	}
	/**
	 * 此功能点对应的按钮权限集合
	 * @param roleAlias :角色别名集合
	 * @param funId： 功能点id
	 * @param dataId：记录id
	 * @return
	 */
	public Map<String,Boolean> getButtonPemission(String roleAlias, Long funId,Long dataId){
		Map<String,Boolean> map = new HashMap();
		List<RecRoleSonFun> list = getByRoleAliasFun(roleAlias,funId,dataId);
		for(RecRoleSonFun rrsf : list){
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
