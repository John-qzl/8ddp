
package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.xfire.spring.SpringUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.dao.PositionDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.redis.RedisKey;
import com.cssrc.ibms.core.util.string.StringUtil;


/**
 * 岗位Service
 * <p>Title:PositionService</p>
 * @author Yangbo 
 * @date 2016-8-4上午09:55:33
 */
@Service
public class PositionService extends BaseService<Position> implements IPositionService{
	public static JSONArray allPositionInf = new JSONArray();
	@Resource
	private PositionDao dao;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	UserPositionDao userPositionDao;
	@Resource
	private UserPositionService userPositionService;

	@Override
	protected IEntityDao<Position, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 通过岗位名称查找岗位信息
	 * */
	public List<Position> getByPosName(String posName) {
		return this.dao.getByPosName(posName);
	}

	/**
	 * 删除岗位信息
	 * */
	public void deleteByUpdateFlag(Long posId,Long currentUserId) {
		this.dao.deleteByUpdateFlag(posId,currentUserId);
		this.userPositionService.delByPosId(posId);
	}

	/**
	 * 通过部门查找其下的岗位
	 * */
	public List<Position> getPositionByOrgId(Long orgId) {
		return dao.getPositionByOrgId(orgId);
	}

	public Position getPrimaryPositionByUserId(Long userId) {
		return this.dao.getPrimaryPositionByUserId(userId);
	}

	public List<Position> getByUserId(Long userId) {
		return this.dao.getByUserId(userId);
	}

	public List<Long> getPositonIdsByUserId(Long userId) {
		List list = new ArrayList();
		List<Position> positionList = this.dao.getByUserId(userId);
		for (Position pos : positionList) {
			list.add(pos.getPosId());
		}
		return list;
	}
	/**
	 * 添加新岗位
	 * @param position
	 * @param upList
	 * @throws Exception
	 */
	public void add(Position position, List<UserPosition> upList)
			throws Exception {
		add(position);
		if (BeanUtils.isEmpty(upList))
			return;
		for (UserPosition up : upList) {
			Long posId = position.getPosId();
			Long userId = position.getPosId();

			boolean isPrimary = up.getIsPrimary().shortValue() == 1;
			if (isPrimary) {
				//清除先前主组织岗位信息
				this.userPositionDao.updNotPrimaryByUser(userId);
			}
			up.setPosId(posId);
			up.setUserPosId(Long.valueOf(UniqueIdUtil.genId()));
			this.userPositionDao.add(up);
		}
	}

	public List<Position> getOrgPosListByOrgIds(String orgIds) {
		return this.dao.getOrgPosListByOrgIds(orgIds);
	}

	public List<Position> getOrgListByOrgIds(String orgIds) {
		return this.dao.getOrgListByOrgIds(orgIds);
	}
	/**
	 * 获取用户默认的组织岗位关系  
	 * 有主组织获取主组织，没有主组织获取查询后的第一个组织。
	 * @param userId
	 * @return
	 */
	public Position getDefaultPosByUserId(Long userId) {
		List<UserPosition> list = this.userPositionDao.getByUserId(userId);
		Long posId = Long.valueOf(0L);

		if (BeanUtils.isEmpty(list))
			return null;
		if (list.size() == 1) {
			posId = ((UserPosition) list.get(0)).getPosId();
		} else {
			for (UserPosition userPosition : list) {
				//有主组织获取主组织
				if (userPosition.getIsPrimary().equals(Short.valueOf((short) 1))) {
					posId = userPosition.getPosId();
					break;
				}
			}
			//没有主组织获取查询后的第一个组织。
			if (posId!=null && posId.longValue() == 0L)
				posId = ((UserPosition) list.get(0)).getPosId();
		}
		if(posId!=null){
			return (Position) this.dao.getById(posId);
		}else{
			return null;
		}
		 
	}

	public Position getByPosCode(String posCode) {
		return this.dao.getByPosCode(posCode);
	}

	public List<Position> getBySupOrgId(QueryFilter filter) {
		return this.dao.getBySupOrgId(filter);
	}

	public void deleByJobId(Long jobId,Long currentUserId) {
		this.dao.deleByJobId(jobId,currentUserId);
	}

	//逻辑删除
	public void delLogicByOrgId(List<SysOrg> orgs,Long currentUserId) {
		for (int i = 0; i < orgs.size(); i++) {
			Long orgId = ((SysOrg) orgs.get(i)).getOrgId();
			this.dao.delLogicByOrgId(orgId,currentUserId);
		}
	}
	
	//还原逻辑删除的岗位
	public void restoreLogicByOrgId(List<SysOrg> orgs,Long currentUserId) {
		for (int i = 0; i < orgs.size(); i++) {
			Long orgId = ((SysOrg) orgs.get(i)).getOrgId();
			this.dao.restoreLogicByOrgId(orgId,currentUserId);
		}
	}
	
	//物理删除
	public void delByOrgId(List<SysOrg> orgs) {
		for (int i = 0; i < orgs.size(); i++) {
			Long orgId = ((SysOrg) orgs.get(i)).getOrgId();
			this.dao.delByOrgId(orgId);
		}
	}

	public boolean isJobUsedByPos(Long jobId) {
		return this.dao.isJobUsedByPos(jobId);
	}

	public String getPosCode(Long posId) {
		return this.dao.getPosCode(posId);
	}

	public boolean isPoscodeUsed(String posCode) {
		return this.dao.isPoscodeUsed(posCode);
	}


	@Override
	public IPosition getPosByUserId(Long userId) {
		return this.dao.getPosByUserId(userId);
	}

	@Override
	public IPosition getByOrgJobId(Long orgId, Long jobId) {
		return this.dao.getByOrgJobId(orgId, jobId);
	}

	/**
	 * 将所有岗位放到redis中
	 */
	public void setAllPositionToRedis(){
		//获取所有的岗位
		List<Position> positionList = getAll();
		if(BeanUtils.isEmpty(positionList)){
        	return;
        }
		JSONArray positionArr = new JSONArray();
		for(Position position:positionList){
			Long orgId = position.getOrgId();
			SysOrg sysOrg = this.sysOrgDao.getById(orgId);
			String path = "";
			if(sysOrg!=null){
				path = sysOrg.getPath();
			}
			JSONObject positionObj = new JSONObject();
			positionObj.put("posId", position.getPosId());
			positionObj.put("posName", position.getPosName());
			positionObj.put("orgPath", path);
			positionArr.add(positionObj);
		}
		
		try {
   		 	//RedisClient.set(RedisKey.ALL_POSITION_INF, positionArr.toString());
			allPositionInf=positionArr;
        } catch (Exception e) {
            logger.error("岗位放到redis中初始化出错");
        }
	}
	
	/**
	 * 通过前台传过来的岗位输入框的值进行模糊查询
	 * @param posFuzzyName
	 * @return
	 */
	public JSONArray getFuzzyPositionList(String posFuzzyName,String fieldName,String relvalue,String type,String typeVal){
		//Object allPosition = RedisClient.get(RedisKey.ALL_POSITION_INF);
		//JSONArray allPositionInf = JSONArray.fromObject(allPosition);
		JSONArray posArr = new JSONArray();
		
		//获取当前用户组织
		SysOrg sysCurrentOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		
		Iterator<Object> it = allPositionInf.iterator();
		
		//其次判断是否需要通过typeVal进行数据过滤
		if((StringUtil.isNotEmpty(type)) && (!"all".equals(typeVal))
				&& (BeanUtils.isNotEmpty(sysCurrentOrg))){
			
			//处理当前组织path
			String path = sysCurrentOrg.getPath();
			//上级组织path
			String upOrgPath = path.substring(0,path.length()-1);
			if(upOrgPath.indexOf(".")>0){
				upOrgPath = upOrgPath.substring(0,upOrgPath.lastIndexOf(".")+1);
			}else{
				upOrgPath = "";
			}
			
			while(it.hasNext()){
				JSONObject posObj = (JSONObject) it.next();
				Object fieldValue = posObj.get(fieldName);
				Object orgPath = posObj.get("orgPath");
				
				//当前组织
				if(typeVal.equals("self")){
					if(orgPath!=null&&orgPath.toString().contains(path)){
						//判断是否需要模糊查询 .
						if(fieldValue!=null){
							if(StringUtil.isChinese(posFuzzyName)){
								if(fieldValue.toString().contains(posFuzzyName)){
									posArr.add(posObj);
									//限制查询时的数据数量
									if(posArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(posFuzzyName)){
									posArr.add(posObj);
									//限制查询时的数据数量
									if(posArr.size()>20)
										break;
								}
							}
						}else{
							posArr.add(posObj);
							//限制查询时的数据数量
							if(posArr.size()>20)
								break;
						}
					}
				}else if(typeVal.equals("up")&&StringUtil.isNotEmpty(upOrgPath)){
					//上级组织
					if(orgPath!=null&&orgPath.toString().contains(upOrgPath)){
						//判断是否需要模糊查询 .
						if(fieldValue!=null){
							if(StringUtil.isChinese(posFuzzyName)){
								if(fieldValue.toString().contains(posFuzzyName)){
									posArr.add(posObj);
									//限制查询时的数据数量
									if(posArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(posFuzzyName)){
									posArr.add(posObj);
									//限制查询时的数据数量
									if(posArr.size()>20)
										break;
								}
							}
						}else{
							posArr.add(posObj);
							//限制查询时的数据数量
							if(posArr.size()>20)
								break;
						}
					}
				}
			}
		}else{
			while(it.hasNext()){
				JSONObject posObj = (JSONObject) it.next();
				Object fieldValue = posObj.get(fieldName);
				
				//判断是否需要模糊查询 .
				if(fieldValue!=null){
					if(StringUtil.isChinese(posFuzzyName)){
						if(fieldValue.toString().contains(posFuzzyName)){
							posArr.add(posObj);
							//限制查询时的数据数量
							if(posArr.size()>20)
								break;
						}
					}else{
						if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(posFuzzyName)){
							posArr.add(posObj);
							//限制查询时的数据数量
							if(posArr.size()>20)
								break;
						}
					}
				}else{
					posArr.add(posObj);
					//限制查询时的数据数量
					if(posArr.size()>20)
						break;
				}
			}
		}
		
		return posArr;
	}
}
