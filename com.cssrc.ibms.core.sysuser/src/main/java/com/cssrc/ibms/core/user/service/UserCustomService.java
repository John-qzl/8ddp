package com.cssrc.ibms.core.user.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.sysuser.intf.IUserCustomService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.UserCustomDao;
import com.cssrc.ibms.core.user.model.UserCustom;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.redis.RedisKey;

@Service
public class UserCustomService  extends BaseService<UserCustom> implements IUserCustomService{
	@Resource
	private UserCustomDao userCustomDao;	
	@Resource
	private IDataTemplateService dataTemplateService;
	
	@Resource
	private ISysParameterService sysParameterService;
	@Resource
	private  IFormTableService formTableService;
	@Resource
	private  IGlobalTypeService globalTypeService;
	@Resource
	private  IDictionaryService  dictionaryService;
	protected IEntityDao<UserCustom, Long> getEntityDao() {
		return this.userCustomDao;
	}
	public UserCustom getById(Long userId){
		UserCustom uc = super.getById(userId);
		if(uc==null){
			uc = new UserCustom(userId);
			this.add(uc);
		}
		return uc;
	}
	/**
	 * 获取导出字段
	 * @param displayId
	 * @return
	 */
	public JSONArray getExportField(Long displayId){
		JSONArray rtn = new JSONArray();
		IDataTemplate template = dataTemplateService.getById(displayId);
		if(BeanUtils.isEmpty(template)){
			return rtn;
		}
		JSONArray ep =  JSONArray.fromObject(template.getExportField());
		JSONArray epf = (JSONArray)((JSONObject)ep.get(0)).get("fields");
		Long curUserId = UserContextUtil.getCurrentUserId();
		Long orgId = UserContextUtil.getCurrentOrg()==null?0L:UserContextUtil.getCurrentOrg().getOrgId();
		Map<String, Object> rightMap = dataTemplateService.getRightMap(curUserId, orgId);
		Map<String, Boolean> fieldRight = dataTemplateService.getPermission
				(IDataTemplate.RIGHT_TYPE_EXPORT,epf.toString(), rightMap);
		for(int i=0;i<epf.size();i++){
			JSONObject  obj =  JSONObject.fromObject(epf.get(i));
			String fieldName = (String)obj.get("name");
			if(fieldRight.containsKey(fieldName)&&fieldRight.get(fieldName)){
				obj.put("checked", true);
			}else{
				obj.put("checked", false);
			}
			rtn.add(obj);
		}
		return rtn;
	}
	/**
	 * 获取列值过滤总段设置信息
	 * @param displayId
	 * @param fieldName
	 * @return
	 */
	public JSONObject getLvfField(Long displayId,String fieldName){
		IDataTemplate template = dataTemplateService.getById(displayId);
		IFormTable bpmFormTable = formTableService.getByTableId(template.getTableId(), 1);
		List<? extends IFormField> list = bpmFormTable.getFieldList();
		JSONObject rtn = new JSONObject();
		for(int i= 0 ;i<list.size();i++){
			IFormField field = list.get(i);
			if(field.getFieldName().equals(fieldName)){
				String type = field.getFieldType();
				switch(type){
				case("varchar"):
					short controlType = field.getControlType();
					//数据字典
					if(controlType==3){
						JSONArray jArr = new JSONArray();
						IGlobalType globalType = globalTypeService.getByDictNodeKey(field.getDictType());
						List<? extends IDictionary> dList = dictionaryService.getByTypeId(globalType.getTypeId().longValue(), false);						
						//去除已逻辑删除的数据
						for(int j = 0;j<dList.size();j++){
							IDictionary dic = dList.get(j);
							if(BeanUtils.isEmpty(dic.getDic_delFlag())||dic.getDic_delFlag()!=1){
								JSONObject jObj = new JSONObject();
								jObj.put("value", dic.getItemName());
								jObj.put("key", dic.getItemValue());
								jArr.add(jObj);
							}
						}						
						rtn.put("fieldName", field.getFieldName());
						rtn.put("fieldDesc", field.getFieldDesc());
						rtn.put("fieldType", field.getFieldType());
						rtn.put("controlType",field.getControlType());
						rtn.put("dictType", field.getDictType());
						rtn.put("dictOptions", jArr.toArray());
					}
					//options不为空:为枚举类型
					else if(!field.getOptions().equals("")){
						rtn.put("fieldName", field.getFieldName());
						rtn.put("fieldDesc", field.getFieldDesc());
						rtn.put("fieldType", field.getFieldType());
						rtn.put("controlType","enum");
						rtn.put("options", field.getOptions());
					}else{
						rtn.put("fieldName", field.getFieldName());
						rtn.put("fieldDesc", field.getFieldDesc());
						rtn.put("fieldType", field.getFieldType());
						rtn.put("controlType",field.getControlType());
					}
					break;
				case("number"):
					rtn.put("fieldName", field.getFieldName());
					rtn.put("fieldDesc", field.getFieldDesc());
					rtn.put("fieldType", field.getFieldType());
					rtn.put("controlType",field.getControlType());
					break;
				case("date"):
					rtn.put("fieldName", field.getFieldName());
					rtn.put("fieldDesc", field.getFieldDesc());
					rtn.put("fieldType", field.getFieldType());
					rtn.put("controlType",field.getControlType());
					JSONObject obj = JSONObject.fromObject(field.getCtlProperty());
					rtn.put("format",obj.get("format"));
					break;
				case("clob"):
					rtn.put("fieldName", field.getFieldName());
					rtn.put("fieldDesc", field.getFieldDesc());
					rtn.put("fieldType", field.getFieldType());
					rtn.put("controlType",field.getControlType());
					break;
				default :
					rtn = JSONObject.fromObject(field);
					break;
				}
			}
		}
		return rtn;
	}
	public JSONArray getDisplayField(Long displayId,String displaySetInfo){
		String condition = "";
		JSONArray rtnArr = new JSONArray();
		if(BeanUtils.isEmpty(displaySetInfo)){
			IDataTemplate template = dataTemplateService.getById(displayId);
			if(template!=null){
			    condition = template.getDisplayField();
			}
		}else{
			condition = displaySetInfo;
		}
    	if(BeanUtils.isNotEmpty(condition)){
    		if(BeanUtils.isEmpty(displaySetInfo)){
    			JSONArray josnArray = JSONArray.fromObject(condition);
	    		for(int i=0;i<josnArray.size();i++){
	    			JSONObject obj = JSONObject.fromObject(josnArray.get(i));
	    			JSONObject rtnObj = new JSONObject();
	    			rtnObj.put("name", obj.get("name"));
	    			rtnObj.put("desc", obj.get("desc"));
	    			rtnObj.put("checked", true);
	    			rtnArr.add(rtnObj);
	    		}
    		}else{
    			JSONArray josnArray = JSONArray.fromObject(condition);
	    		for(int i=0;i<josnArray.size();i++){
	    			JSONObject obj = JSONObject.fromObject(josnArray.get(i));
	    			JSONObject rtnObj = new JSONObject();
	    			rtnObj.put("name", obj.get("name"));
	    			rtnObj.put("desc", obj.get("desc"));
	    			rtnObj.put("checked", obj.get("checked"));
	    			rtnArr.add(rtnObj);
	    		}
    		}
    		
    	}
    	return rtnArr;
    }
	/**
	 *  根据业务数据模板，处理表头设置信息
	 * @param displayId
	 * @param displayFieldStr
	 * @return
	 */
	public String dealDisplayField(Long displayId,String displayFieldStr){
		JSONArray rtnArr = new JSONArray();
		IDataTemplate template = dataTemplateService.getById(displayId);
		if(template==null){
		    return null;
		}
		// 获取权限map
		Long curUserId = UserContextUtil.getCurrentUserId();
		Long orgId = UserContextUtil.getCurrentOrg()==null?0L:UserContextUtil.getCurrentOrg().getOrgId();
		Map<String, Object> rightMap = dataTemplateService.getRightMap(curUserId, orgId);
		Map<String, Boolean> fieldRight = dataTemplateService.getPermission
				(IDataTemplate.RIGHT_TYPE_SHOW,template.getDisplayField(), rightMap);
    	String display = template.getDisplayField();
    	JSONArray josnArray = JSONArray.fromObject(display);
    	
    	JSONArray dispalyField = JSONArray.fromObject(displayFieldStr);    	
		for(int i=0;i<josnArray.size();i++){
			boolean isNewField = true;
			JSONObject obj = JSONObject.fromObject(josnArray.get(i));
			if(!fieldRight.get(obj.get("name"))){
				continue;
			}
			JSONObject rtnObj = new JSONObject();
			rtnObj.put("name", obj.get("name"));
			rtnObj.put("desc", obj.get("desc"));
			for(int j=0;j<dispalyField.size();j++){
				JSONObject field = JSONObject.fromObject(dispalyField.get(j));
				if(field.get("name").equals(obj.get("name"))){
					isNewField = false;
				}
				if(field.get("name").equals(obj.get("name"))&&field.getBoolean("checked")){
					rtnObj.put("checked", true);
					break;
				}
			}
			if(!rtnObj.containsKey("checked")){
				rtnObj.put("checked", false);
			}
			//新增字段为true
			if(isNewField){
				rtnObj.put("checked", true);
			}
			rtnArr.add(rtnObj);
		}
		return rtnArr.toString();
	}
	/**
	 * 根据业务数据模板，处理查询设置信息
	 * @param displayId
	 * @param queryFieldStr
	 * @return
	 */
	public String dealQueryField(Long displayId,String queryFieldStr){
		JSONArray rtnArr = new JSONArray();
		
		IDataTemplate template = dataTemplateService.getById(displayId);
		if(template==null){
		    return null;
		}
    	String condition = template.getConditionField();
    	JSONArray josnArray = JSONArray.fromObject(condition);
    	
    	JSONArray queryField = JSONArray.fromObject(queryFieldStr);
		for(int i=0;i<josnArray.size();i++){
			JSONObject obj = JSONObject.fromObject(josnArray.get(i));
			JSONObject rtnObj = new JSONObject();
			rtnObj.put("name", obj.get("na"));
			rtnObj.put("desc", obj.get("cm"));
			for(int j=0;j<queryField.size();j++){
				JSONObject field = JSONObject.fromObject(queryField.get(j));
				if(field.get("name").equals(obj.get("na"))){
					rtnObj.put("checked", field.getBoolean("checked"));
					break;
				}
			}
			if(!rtnObj.containsKey("checked")){
				rtnObj.put("checked", true);
			}
			rtnArr.add(rtnObj);
		}
		return rtnArr.toString();
	}
	/**
	 * @param displayId
	 * @param querySetInfo
	 * @return
	 */
	public JSONArray getQueryField(Long displayId,String querySetInfo){
		String condition = "";
		JSONArray rtnArr = new JSONArray();
		if(BeanUtils.isEmpty(querySetInfo)){
			IDataTemplate template = dataTemplateService.getById(displayId);
			if(template!=null){
			    condition = template.getConditionField();
			}
		}else{
			condition = querySetInfo;
		}
    	if(BeanUtils.isNotEmpty(condition)){
    		if(BeanUtils.isEmpty(querySetInfo)){
    			JSONArray josnArray = JSONArray.fromObject(condition);
	    		for(int i=0;i<josnArray.size();i++){
	    			JSONObject obj = JSONObject.fromObject(josnArray.get(i));
	    			JSONObject rtnObj = new JSONObject();
	    			rtnObj.put("name", obj.get("na"));
	    			rtnObj.put("desc", obj.get("cm"));
	    			rtnObj.put("checked", true);
	    			rtnArr.add(rtnObj);
	    		}
    		}else{
    			JSONArray josnArray = JSONArray.fromObject(condition);
	    		for(int i=0;i<josnArray.size();i++){
	    			JSONObject obj = JSONObject.fromObject(josnArray.get(i));
	    			JSONObject rtnObj = new JSONObject();
	    			rtnObj.put("name", obj.get("name"));
	    			rtnObj.put("desc", obj.get("desc"));
	    			rtnObj.put("checked", obj.get("checked"));
	    			rtnArr.add(rtnObj);
	    		}
    		}
    		
    	}
    	return rtnArr;
    }
	
	/**
	 * 保存或更新
	 * @param userId :
	 */
	public void save(Long userId){
		Object user_custom_info = RedisClient.get(UserCustom.getUserCustomKey(userId));
		if(user_custom_info==null){
			user_custom_info = "";
		}
		UserCustom uc = new UserCustom();
		uc.setUserId(userId);
		uc.setCustomInfo(user_custom_info.toString());
		UserCustom userCustom = userCustomDao.getById(userId);
		if(BeanUtils.isEmpty(userCustom)){
			add(uc);
		}else{
			update(uc);
		}
	}
	/**
	 * 保存或更新
	 * @param userId
	 * @param user_custom_info
	 */
	public void save(Long userId,String user_custom_info){
		UserCustom uc = new UserCustom();
		uc.setUserId(userId);
		uc.setCustomInfo(user_custom_info.toString());
		UserCustom userCustom = userCustomDao.getById(userId);
		if(BeanUtils.isEmpty(userCustom)){
			add(uc);
		}else{
			update(uc);
		}
	}
	/**
	 * 将用户设置信息加载到redis中
	 */
	public void setUserCustomToRedis(){
		// 通过参数判断是否需要放置到redis中
        int setRedis = sysParameterService.getIntByAlias(RedisKey.CUSTOM_INFO_SET);
        if (setRedis == 0)
        {
            return;
        }        
        List<UserCustom> uc_list = this.getAll();
        if(BeanUtils.isEmpty(uc_list)){
        	return;
        }
        for(UserCustom uc : uc_list){
        	Long userId = uc.getUserId();
        	 try
             {
        		 RedisClient.set(UserCustom.getUserCustomKey(userId), uc.getCustomInfo());
             }
             catch (Exception e)
             {
                 logger.error(userId + ":用户个性化初始化出错");
             }
        }

	}

}
