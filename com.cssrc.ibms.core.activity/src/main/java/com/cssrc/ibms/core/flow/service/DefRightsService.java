package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.DefRightsDao;
import com.cssrc.ibms.core.flow.model.DefRights;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:流程定义权限明细 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class DefRightsService extends BaseService<DefRights>
{
	@Resource
	private DefRightsDao dao;
	@Resource
	private IGlobalTypeService globalTypeService;
	
	public DefRightsService()
	{
	}
	@Override
	protected IEntityDao<DefRights, Long> getEntityDao() 
	{
		return dao;
	}
	/**
	 * 根据流程定义ID和权限类型互获取权限数据。
	 * @param defId
	 * @param rightType
	 * @return
	 */
	public List<DefRights> getDefRight(Long defId,Short rightType){
		return dao.getDefRight(defId,rightType);
	}
	
	/**
	 * 根据分类和权限类型获取权限数据。
	 * @param typeId
	 * @param rightType
	 * @return
	 */
	public List<DefRights> getTypeRight(Long typeId,Short rightType){
		return dao.getTypeRight(typeId,rightType);
	}
	
	/**
	 * 获取流程权限。
	 * @param assignId	流程定义id或流程分类ID,具体由assignType决定
	 * @param assignType	0，流程定义ID,1,流程分类ID。
	 * @return
	 */
	public Map<String,String>  getRights(String assignId,int assignType){
		Map<String,String> rightsMap=new HashMap<String, String>();
		List<DefRights> list=null;
		if(assignType==DefRights.SEARCH_TYPE_DEF){
			list =dao.getByDefKey(assignId);
		}
		else{
			list =dao.getByTypeId(Long.parseLong( assignId));
		}
		List<DefRights> all=new ArrayList<DefRights>();
		List<DefRights> user=new ArrayList<DefRights>();
		List<DefRights> role=new ArrayList<DefRights>();
		List<DefRights> org=new ArrayList<DefRights>();
		List<DefRights> position=new ArrayList<DefRights>();
		List<DefRights> posGroup=new ArrayList<DefRights>();
		List<DefRights> job=new ArrayList<DefRights>();
		List<DefRights> orgGrant=new ArrayList<DefRights>();
		
		for(DefRights rights:list){
			switch (rights.getRightType()) {
				case DefRights.RIGHT_TYPE_ALL:
					all.add(rights);
					break;
				case DefRights.RIGHT_TYPE_USER:
					user.add(rights);
					break;
				case DefRights.RIGHT_TYPE_ROLE:
					role.add(rights);
					break;
				case DefRights.RIGHT_TYPE_ORG:
					org.add(rights);
					break;
				case DefRights.RIGHT_TYPE_POSITION:
					position.add(rights);
					break;
				case DefRights.RIGHT_TYPE_ORG_GRANT:
					orgGrant.add(rights);
					break;
			}
		}
		String allData=coverList2Json(all);
		String userData=coverList2Json(user);
		String roleData=coverList2Json(role);
		String orgData=coverList2Json(org);
		String positionData=coverList2Json(position);
		String posGroupData=coverList2Json(posGroup);
		String jobData=coverList2Json(job);
		String orgGrantData=coverList2Json(orgGrant);
		rightsMap.put("all", allData);
		rightsMap.put("user", userData);
		rightsMap.put("role", roleData);
		rightsMap.put("org", orgData);
		rightsMap.put("position", positionData);
		rightsMap.put("posGroup", posGroupData);
		rightsMap.put("job", jobData);
		rightsMap.put("orgGrant", orgGrantData);
		return rightsMap;
	}
	
	/**
	 * 将流程权限列表转换成为map对象。
	 * 
	 * @param list
	 * @return
	 */
	public String coverList2Json(List<DefRights> list){
		if(BeanUtils.isEmpty(list)) return "";
		JSONArray jarray = new JSONArray();  
		
		for(DefRights r:list){
			JSONObject jobject = new JSONObject();
			jobject.accumulate("id", r.getOwnerId()).accumulate("name", r.getOwnerName());
			jarray.add(jobject);
		}
		return jarray.toString();
	}
	
	/**
	 * 将流程权限列表转换成为map对象。
	 * 
	 * @param list
	 * @return
	 */
	public Map<String,String> coverList2Map(List<DefRights> list){
		Map<String,String> m=new HashMap<String,String>();
		if(BeanUtils.isEmpty(list)) return m;
		
		String ownerId ="";
		String ownerName="";
		for(DefRights r:list){
			ownerId+=r.getOwnerId()+",";
			ownerName+=r.getOwnerName()+",";
		}
		if(ownerId.length()>0)ownerId=ownerId.substring(0,ownerId.length()-1);
		if(ownerName.length()>0)ownerName=ownerName.substring(0,ownerName.length()-1);
		m.put("ownerId", ownerId);
		m.put("ownerName", ownerName);
		
		return m;
	}
	
	public void saveRights(String assignId,int assignType, String[] rightType,String[] ownerId,
			String[] ownerName, int isChange) throws Exception{
		if(assignType==DefRights.SEARCH_TYPE_DEF){
			dao.delByDefKey(assignId);
		}
		/*else{
			if(isChange==1){
				GlobalType gt=globalTypeDao.getById(new Long(assignId));
				String nodePath=gt.getNodePath();
				List<GlobalType> globalTypelist= globalTypeDao.getByNodePath(nodePath);
				for(GlobalType glbtype:globalTypelist){
					dao.delByTypeId(glbtype.getTypeId());
				}
			}else{
				dao.delByTypeId(new Long(assignId));
			}
		}*/
		List<DefRights> rightList=coverTypeRights(assignId,assignType, rightType, ownerId, ownerName, isChange);
		add(rightList);
	}
	
	/**
	 * 添加权限。
	 * @param rightList
	 */
	public void add(List<DefRights> rightList){
		if(rightList==null||rightList.size()==0)return;
		for(DefRights r:rightList){
			dao.add(r);
		}
	}
	
	private List<DefRights> coverTypeRights(String assignId,int assignType, String[] rightType,String[] ownerId,
			String[] ownerName, int isChange) throws Exception{
		
		if(ownerId==null||ownerId.length==0)return null;
	
		List<DefRights> list=new ArrayList<DefRights>();
		//对权限类型进行循环。
		for(int i=0;i<rightType.length;i++){
			String right=rightType[i];
			String[] ids=ownerId[i].split(",");
			String[] names=ownerName[i].split(",");
			if(BeanUtils.isEmpty(ids)) continue;
			
			for(int j=0;j<ids.length;j++){
				String id=ids[j];
				String name=names[j];
				if(StringUtil.isEmpty(id)) continue;

				if(assignType==DefRights.SEARCH_TYPE_DEF){
					DefRights defRight=new DefRights();
					defRight.setId(UniqueIdUtil.genId());
					defRight.setDefKey(assignId);
					defRight.setSearchType(DefRights.SEARCH_TYPE_DEF);
					defRight.setRightType(new Short(right));
					defRight.setOwnerId(new Long(id));
					defRight.setOwnerName(name);
					list.add(defRight);
				}
				/*else{
					if(isChange==1){
						GlobalType gt=globalTypeDao.getById(new Long(assignId));
						String nodePath=gt.getNodePath();
						List<GlobalType> globalTypelist= globalTypeDao.getByNodePath(nodePath);
						for(GlobalType glbtype:globalTypelist){
							DefRights defRight=new DefRights();
							defRight.setId(UniqueIdUtil.genId());
							defRight.setFlowTypeId(glbtype.getTypeId());
							defRight.setSearchType(DefRights.SEARCH_TYPE_GLT);
							defRight.setRightType(new Short(right));
							defRight.setOwnerId(new Long(id));
							defRight.setOwnerName(name);
							list.add(defRight);
						}
					}else{
						DefRights defRight=new DefRights();
						defRight.setId(UniqueIdUtil.genId());
						defRight.setFlowTypeId(new Long(assignId));
						defRight.setSearchType(DefRights.SEARCH_TYPE_GLT);
						defRight.setRightType(new Short(right));
						defRight.setOwnerId(new Long(id));
						defRight.setOwnerName(name);
						list.add(defRight);
					}
				}*/
			}
			
		}
		return list;
	}
	public void saveRights(String assignId,int assignType, String[] rightType,String[] owner, int isChange) throws Exception{
		if(assignType==DefRights.SEARCH_TYPE_DEF){
			String[] assignIds=assignId.split(",");
			for (String defKey : assignIds) {
				dao.delByDefKey(defKey);
			}
		}
	/*	else{
			if(isChange==1){
				GlobalType gt=globalTypeDao.getById(new Long(assignId));
				String nodePath=gt.getNodePath();
				List<GlobalType> globalTypelist= globalTypeDao.getByNodePath(nodePath);
				for(GlobalType glbtype:globalTypelist){
					dao.delByTypeId(glbtype.getTypeId());
				}
			}else{
				dao.delByTypeId(new Long(assignId));
			}
		}*/
		List<DefRights> rightList=coverTypeRights(assignId,assignType, rightType, owner, isChange);
		add(rightList);
	}
	
	private List<DefRights> coverTypeRights(String assignId,int assignType, String[] rightType,String[] owner, int isChange) throws Exception{
		
		if(owner==null||owner.length==0)return null;
	
		String[] assignIds=assignId.split(",");
		List<DefRights> list=new ArrayList<DefRights>();
		//对权限类型进行循环。
		for(int i=0;i<rightType.length;i++){
			String right=rightType[i];
			String ownerObj = owner[i];
			if(StringUtil.isEmpty(ownerObj))continue;
			JSONArray jarray = JSONArray.fromObject(ownerObj);
			int size = jarray.size(); 
			if(size==0) continue;
			
			for(int j=0;j<size;j++){
				JSONObject jObject = (JSONObject)jarray.get(j); 
				String id = jObject.getString("id");
				String name = jObject.getString("name");
				if(StringUtil.isEmpty(id)) continue;

				if(assignType==DefRights.SEARCH_TYPE_DEF){
					for (String assignid:assignIds) {
						DefRights defRight  =setDefRights(assignType, assignid,null, new Short(right), new Long(id), name);
						list.add(defRight);
					}
					
				}
		/*		else{
					if(isChange==1){
						GlobalType gt=globalTypeDao.getById(new Long(assignId));
						String nodePath=gt.getNodePath();
						List<GlobalType> globalTypelist= globalTypeDao.getByNodePath(nodePath);
						for(GlobalType glbtype:globalTypelist){
							DefRights defRight  =setDefRights(assignType,null,glbtype.getTypeId(), new Short(right), new Long(id), name);
							list.add(defRight);
						}
					}else{
						DefRights defRight  =setDefRights(assignType,null,new Long(assignId), new Short(right), new Long(id), name);
						list.add(defRight);
					}
				}*/
			}
			
		}
		return list;
	}

	private DefRights setDefRights(int assignType,String defKey, Long flowTypeId,Short rightType,Long ownerId,String ownerName){
		DefRights defRight=new DefRights();
		defRight.setId(UniqueIdUtil.genId());
	
		if(assignType==DefRights.SEARCH_TYPE_DEF){
			defRight.setSearchType(DefRights.SEARCH_TYPE_DEF);
			defRight.setDefKey(defKey);		
		}else{
			defRight.setSearchType(DefRights.SEARCH_TYPE_GLT);
			defRight.setFlowTypeId(flowTypeId);
		}	
		defRight.setRightType(rightType);
		defRight.setOwnerId(ownerId);
		defRight.setOwnerName(ownerName);
		return defRight;
	}
	
}
