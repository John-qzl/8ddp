package com.cssrc.ibms.core.user.service.custom;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.custom.intf.IAdvancedQueryService;
import com.cssrc.ibms.api.custom.model.IAdvancedQuery;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.model.UserCustom;
import com.cssrc.ibms.core.user.model.custom.AdvancedQuery;
import com.cssrc.ibms.core.user.service.UserCustomService;
import com.cssrc.ibms.core.util.bean.BeanUtils;

@Service
public class AdvancedQueryService implements IAdvancedQueryService{
	@Resource
	private UserCustomService userCustomService;	
	
	/**
	 * 删除
	 * @param displayId
	 * @param queryKey
	 */
	public void del(String displayId,String queryKey){
		del(displayId,new String[]{queryKey});
	}
	/**
	 * 批量删除
	 * @param aq
	 */
	public void del(String displayId,String[] queryKey){
		UserCustom uc = userCustomService.getById(UserContextUtil.getCurrentUserId());
		String kIndex = AdvancedQuery.KEY+"."+displayId+"."+AdvancedQuery.KEYINDEX;
		
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		String str = obj.getString(kIndex);
		str = BeanUtils.isEmpty(str)?"":str;
		
		for(String key : queryKey){
			String k = AdvancedQuery.KEY+"."+displayId+"."+key;
			if(obj.containsKey(k)){
				obj.remove(k);
			}
			if(str.contains(key+",")){
				str = str.replaceAll(key+",","");
			}
		}
		obj.put(kIndex, str);
		uc.setCustomInfo(obj.toString());
		userCustomService.update(uc);
	}
	/**
	 * 保存
	 * @param aq
	 */
	public void save(AdvancedQuery aq){
		String displayId = aq.getDisplayId();
		String queryKey = aq.getQueryKey();
		String k = AdvancedQuery.KEY+"."+displayId+"."+queryKey;
		String kIndex = AdvancedQuery.KEY+"."+displayId+"."+AdvancedQuery.KEYINDEX;
		UserCustom uc = userCustomService.getById(UserContextUtil.getCurrentUserId());
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		obj.put(k, JSONObject.fromObject(aq).toString());
		
		//保存所有query的
		String str = obj.containsKey(kIndex)?obj.getString(kIndex):"";
		if(!str.contains(queryKey+",")){
			str+=queryKey+",";
		}
		obj.put(kIndex,str);
		uc.setCustomInfo(obj.toString());
		userCustomService.update(uc);
	}
	/**从Usercutom获取displayId对应的高级查询bean集合
	 * @param uc
	 * @param displayId
	 * @return
	 */
	public List<IAdvancedQuery> getAdvancedQuery(String displayId){
		UserCustom uc = userCustomService.getById(UserContextUtil.getCurrentUserId());
		List<IAdvancedQuery> list = new ArrayList<IAdvancedQuery>();
		String[] keys = getAllKey(uc,displayId);
		if(keys!=null){
			for(String key : keys){
				list.add(getAdvancedQuery(displayId,key));
			}
			return list;
		}else{
			return list;
		}
	}
	/**
	 * 获取当前业务数据模板queryKey对应高级查询bean
	 * @param uc
	 * @param displayId
	 * @param queryKey
	 * @return
	 */
	/**
	 * @param displayId
	 * @param queryKey
	 * @return
	 */
	public  AdvancedQuery getAdvancedQuery(String displayId,String queryKey){
		UserCustom uc = userCustomService.getById(UserContextUtil.getCurrentUserId());
		String k = AdvancedQuery.KEY+"."+displayId+"."+queryKey;
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		if(obj.containsKey(k)){
			JSONObject obj1 = JSONObject.fromObject(obj.get(k));
			return AdvancedQuery.toBean(obj1);
		}else{ 
			return new AdvancedQuery();
		}
	}
	/**
	 * 获取所有当前业务数据模板下的所有 高级查询设置
	 * @param uc
	 * @param displayId
	 * @return
	 */
	public String[] getAllKey(UserCustom uc,String displayId){
		String k = AdvancedQuery.KEY+"."+displayId+"."+AdvancedQuery.KEYINDEX;
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());		
		if(obj.containsKey(k)){
			String str = obj.getString(k);
			if(str.endsWith(",")){
				str = str.substring(0,str.length()-1);
				String[] arr = str.split(",");
				return arr;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}
