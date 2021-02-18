package com.cssrc.ibms.record.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.record.dao.RecFunDao;
import com.cssrc.ibms.record.dao.RecRoleSonFunDao;
import com.cssrc.ibms.record.dao.RecTypeDao;
import com.cssrc.ibms.record.dao.RecRoleFunDao;
import com.cssrc.ibms.record.model.RecFun;
import com.cssrc.ibms.record.model.RecRoleFun;
import com.cssrc.ibms.record.model.RecRoleSonFun;
import com.cssrc.ibms.record.model.RecType;
import com.cssrc.ibms.system.model.CurrentSystem;
import com.cssrc.ibms.system.model.Resources;
import com.cssrc.ibms.system.service.SysParameterService;

/**
 * Description:
 * <p>RecFunService.java</p>
 * @author dengwenjie 
 * @date 2017年3月13日
 */
@Service
public class RecFunService  extends BaseService<RecFun> {
	@Resource
	private RecFunDao recFunDao;
	@Resource
	private RecTypeDao recTypeDao;
	@Resource
	private RecRoleFunDao recRoleFunDao;
	@Resource
	private RecRoleSonFunDao recRoleSonFunDao;
	@Resource
	SysParameterService sysParameterService;
	@Resource
	private RecRoleService recRoleService;
	@Resource
	private RecRoleSonService recRoleSonService;
	
	protected IEntityDao<RecFun, Long> getEntityDao() {
		return this.recFunDao;
	}
	
	/**
	 * 查找该角色选中的功能点
	 * 
	 * @param roleId
	 * @return
	 */
	public List<RecFun> getByRecRolResChecked(Long typeId,Long roleId) {
		List<RecFun> funList = this.getByTypeId(typeId);
		List<RecRoleFun> roleFunList = this.recRoleFunDao
				.getByRoleId(roleId);

		Set set = new HashSet();

		if (BeanUtils.isNotEmpty(roleFunList)) {
			for (RecRoleFun rofun : roleFunList) {
				set.add(rofun.getFunId());
			}
		}

		if (BeanUtils.isNotEmpty(funList)) {
			for (RecFun fun : funList) {
				if (set.contains(fun.getFunId()))
					fun.setChecked("true");
				else {
					fun.setChecked("false");
				}
			}
		}
		return funList;
	}
	public String getButtonTreeChecked(Long roleId,Long funId){
		RecFun rf = recFunDao.getById(funId);
		RecRoleFun rrf = recRoleFunDao.getByFunAndRole(roleId, funId);
		JSONArray ja =  new JSONArray();
		if(BeanUtils.isNotEmpty(rf)&&BeanUtils.isNotEmpty(rf.getbuttonArr())){
			JSONArray jsonAry = JSONArray.fromObject(rf.getbuttonArr());
			if(BeanUtils.isNotEmpty(jsonAry)){
				for (Object obj : jsonAry) {
					JSONObject jsO = new JSONObject();
					JSONObject json = JSONObject.fromObject(obj);
					String desc = (String) json.get("desc");
					String unique = (String) json.get("unique");
					jsO.put("id", unique);
					jsO.put("parentId","0");
					jsO.put("desc",desc);
					//是否选中初始化
					if(BeanUtils.isNotEmpty(rrf)&&BeanUtils.isNotEmpty(rrf.getButtons())){
						JSONArray buttonRights = JSONArray.fromObject(rrf.getButtons());
						for(Object obj1 : buttonRights){
							JSONObject buttonRight = JSONObject.fromObject(obj1);
							String uniqueR = (String)buttonRight.get("id");
							boolean checkedR = (boolean)buttonRight.get("checked");
							if(unique.equals(uniqueR)){
								jsO.put("checked", checkedR);
								break;
							}
						}
					}					
					ja.add(jsO);
				}
			}
		}
		return ja.toString();
	}
	/**
	 * 查找记录角色选中的功能点
	 * 
	 * @param roleSonId
	 * @return
	 */
	public List<RecFun> getByRecRolSonResChecked(Long typeId,Long roleSonId) {
		List<RecFun> funList = this.getByTypeId(typeId);
		List<RecRoleSonFun> roleSonFunList = this.recRoleSonFunDao
				.getByRoleSonId(roleSonId);

		Set set = new HashSet();

		if (BeanUtils.isNotEmpty(roleSonFunList)) {
			for (RecRoleSonFun roSonfun : roleSonFunList) {
				set.add(roSonfun.getFunId());
			}
		}

		if (BeanUtils.isNotEmpty(funList)) {
			for (RecFun fun : funList) {
				if (set.contains(fun.getFunId()))
					fun.setChecked("true");
				else {
					fun.setChecked("false");
				}
			}
		}
		return funList;
	}
	public String getButtonSonTreeChecked(Long roleSonId,Long funId){
		RecFun rf = recFunDao.getById(funId);
		RecRoleSonFun rrsf = recRoleSonFunDao.getByFunAndRole(roleSonId, funId);
		JSONArray ja =  new JSONArray();
		if(BeanUtils.isNotEmpty(rf)&&BeanUtils.isNotEmpty(rf.getbuttonArr())){
			JSONArray jsonAry = JSONArray.fromObject(rf.getbuttonArr());
			if(BeanUtils.isNotEmpty(jsonAry)){
				for (Object obj : jsonAry) {
					JSONObject jsO = new JSONObject();
					JSONObject json = JSONObject.fromObject(obj);
					String desc = (String) json.get("desc");
					String unique = (String) json.get("unique");
					jsO.put("id", unique);
					jsO.put("parentId","0");
					jsO.put("desc",desc);
					//是否选中初始化
					if(BeanUtils.isNotEmpty(rrsf)&&BeanUtils.isNotEmpty(rrsf.getButtons())){
						JSONArray buttonRights = JSONArray.fromObject(rrsf.getButtons());
						for(Object obj1 : buttonRights){
							JSONObject buttonRight = JSONObject.fromObject(obj1);
							String uniqueR = (String)buttonRight.get("id");
							boolean checkedR = (boolean)buttonRight.get("checked");
							if(unique.equals(uniqueR)){
								jsO.put("checked", checkedR);
								break;
							}
						}
					}					
					ja.add(jsO);
				}
			}
		}
		return ja.toString();
	}
	/**
	 * 通过父id获得父功能点
	 * 
	 * @param parentId
	 * @return
	 */
	public RecFun getParentFunctionsByParentId(Long parentId,Long typeId) {
		RecFun parent = (RecFun) this.recFunDao.getById(Long
				.valueOf(parentId));
		if (parent != null)
			return parent;
		RecType rft = (RecType) this.recTypeDao.getById(typeId);
		parent = new RecFun();
		parent.setFunId(Long.valueOf(0L));
		parent.setTypeId(typeId);
		parent.setParentId(Long.valueOf(-1L));
		parent.setSn(Integer.valueOf(0));
		parent.setFunName(rft.getTypeName());

		parent.setIsDisplayInMenu(RecFun.IS_DISPLAY_IN_MENU_Y);
		parent.setIsFolder(RecFun.IS_FOLDER_Y);
		parent.setIsOpen(RecFun.IS_OPEN_Y);

		return parent;
	}
	/**
	 * 是否存在该别名的功能点
	 * 
	 * @param function
	 * @return
	 */
	public Integer isAliasExists(RecFun function) {
		String alias = function.getAlias();
		return this.recFunDao.isAliasExists(alias);
	}
	public Long addFunction(RecFun function)
			throws Exception {
		Long funId = Long.valueOf(UniqueIdUtil.genId());
		function.setFunId(funId);
		String path = "";
		Long parentId = function.getParentId();
		RecFun parentFun = (RecFun) this.recFunDao.getById(parentId);
		if (BeanUtils.isNotEmpty(parentFun)) {
			if (StringUtil.isNotEmpty(parentFun.getPath()))
				path = parentFun.getPath() + ":" + funId;
		} else {
			path = funId.toString();
		}
		function.setPath(path);
		this.recFunDao.add(function);
		return funId;
	}
	public void updFunction(RecFun function)
			throws Exception {
		Long funId = function.getFunId();
		String path = "";
		Long parentId = function.getParentId();
		RecFun parentFun = (RecFun) this.recFunDao.getById(parentId);
		if (BeanUtils.isNotEmpty(parentFun)) {
			if (StringUtil.isNotEmpty(parentFun.getPath()))
				path = parentFun.getPath() + ":" + funId;
		} else {
			path = funId.toString();
		}
		function.setPath(path);
		this.recFunDao.update(function);
	}
	/**
	 * 通过返回的整数判断该功能点是否别名重复或者是更新了新的
	 * 
	 * @param function
	 * @return
	 */
	public Integer isAliasExistsForUpd(RecFun function) {
		String alias = function.getAlias();
		Long funId = function.getFunId();
		RecFun fun= recFunDao.getById(funId);
		if(alias.equals(fun.getAlias())){ //未更改直接返回
		    return 0;
		}
		return this.recFunDao.isAliasExistsForUpd(funId, alias);
	}

	/**
	 * 删除子功能点及当前功能点的recFunUrl，recRoleFun，recFun
	 * 
	 * @param funId
	 * @return
	 */
	public void delById(Long funId) {
		List allfun = this.recFunDao.getAll();
		List allChilds = getChildsByResId(funId, allfun);
		//删除子功能点的
		for (Iterator it = allChilds.iterator(); it.hasNext();) {
			RecFun function = (RecFun) it.next();
			Long childId = function.getFunId();

			this.recRoleFunDao.delByFunId(childId);

			this.recFunDao.delById(childId);
		}

		this.recRoleFunDao.delByFunId(funId);

		this.recFunDao.delById(funId);
	}
	/**
	 * 由funid获得其下子功能点
	 * 
	 * @param funId
	 * @param allFun
	 * @return
	 */
	private List<RecFun> getChildsByResId(Long funId, List<RecFun> allFun) {
		List<RecFun> rtnList = new ArrayList<RecFun>();
		for (Iterator<RecFun> it = allFun.iterator(); it.hasNext();) {
			RecFun fun = (RecFun) it.next();
			if (fun.getParentId().equals(funId)) {
				rtnList.add(fun);
				recursiveChilds(fun.getFunId(), rtnList, allFun);
			}
		}
		return rtnList;
	}
	/**
	 * 子节点遍历
	 * 
	 * @param funId
	 * @param rtnList
	 * @param allFun
	 */
	private void recursiveChilds(Long funId, List<RecFun> rtnList,
			List<RecFun> allFun) {
		for (Iterator<RecFun> it = allFun.iterator(); it.hasNext();) {
			RecFun fun = (RecFun) it.next();
			if (fun.getParentId().equals(funId)) {
				rtnList.add(fun);
				recursiveChilds(fun.getFunId(), rtnList, allFun);
			}
		}
	}
	public List<RecFun> getByParentId(Long id) {
		return this.recFunDao.getByParentId(id.longValue());
	}
	/**
	 * @param funtypeId : 功能点类别Id
	 * @return
	 */
	public List<RecFun> getByTypeId(Long funtypeId) {
		return this.recFunDao.getByTypeId(funtypeId.longValue());
	}
	public void updSn(Long funId, long sn) {
		this.recFunDao.updSn(funId, sn);
	}
	/**
	 * 图标路径
	 * 
	 * @param list
	 * @param ctxPath
	 */
	public static void addIconCtxPath(List<RecFun> list, String ctxPath) {
		for (Iterator it = list.iterator(); it.hasNext();) {
			RecFun res = (RecFun) it.next();
			String icon = res.getIcon();
			String path = ctxPath + "/";
			if (StringUtil.isNotEmpty(icon))
				if (icon.contains(path)) {
					res.setIcon(icon);
				} else {
					res.setIcon(ctxPath + icon);
				}
			if(res.getChildMenuList().size() != 0){
				addIconCtxPath(res.getChildMenuList(),ctxPath);
			}

		}
	}
	/**
	 * 先判断：是否存在记录角色的设置
	 * 若存在：则从ibms_rec_roleson_fun中获取资源配置信息
	 * 若不存在 ： 则从ibms_rec_role_fun中获取资源配置信息
	 * 对应用户的功能点菜单
	 * @param user
	 * @return
	 */
	public List<RecFun> getRecMenu(ISysUser user,String typeAlias,Long pk) {
		List<RecFun> funList = new ArrayList();
		boolean flag = recRoleSonService.isExistRoleSon(pk);
		if(flag){
			String roleSonAlias = recRoleSonService.getAllRoleSonAliasByType(user.getUserId(),pk);
			funList = this.recFunDao.getFunByRoleSons(roleSonAlias,pk);
		}else{
			String rolealias = recRoleService.getAllRoleAliasByType(user.getUserId(),typeAlias);
			funList = this.recFunDao.getFunByRoles(rolealias);
		}
		return funList;
	}
	/**
	 * 从业务数据模板，获取按钮信息
	 * @param dataTemplate
	 * @param resultMessage
	 * @return
	 */
	public String analyseManageField(IDataTemplate dataTemplate,ResultMessage resultMessage){
		String manageField = dataTemplate.getManageField();
		String alias = dataTemplate.getTemplateAlias();
		JSONArray ja =  new JSONArray();
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		resultMessage.setMessage("从业务数据模板，获取按钮信息成功！");
		for (Object obj : jsonAry) {
			JSONObject js = new JSONObject();
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			String desc = (String) json.get("desc");
			String unique = (String) json.get("unique");
			js.put("name", name);
			js.put("desc", desc);
			js.put("unique", unique);
			ja.add(js);
			if(BeanUtils.isEmpty(unique)){	
				resultMessage.setResult(ResultMessage.Fail);
				resultMessage.setMessage("从业务数据模板[别名："+alias+"]，获取的按钮信息不完整，按钮ID为空！");
			}
		}
		if(resultMessage.getResult()==ResultMessage.Success)
			resultMessage.setMessage(ja.toString());
		return ja.toString();
	}
	/**
	 * 是否有管理
	 * 
	 * @param manageField
	 * @return
	 */
	public boolean hasManage(String manageField) {
		if (StringUtils.isEmpty(manageField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		return jsonAry.size() > 0 ? true : false;
	}
	
	public RecFun getByAlias(String alias) {
		return this.recFunDao.getByAlias(alias);
	}
}
