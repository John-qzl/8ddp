package com.cssrc.ibms.dbom.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.dbom.dao.DBomDao;
import com.cssrc.ibms.dbom.dao.DBomNodeDao;
import com.cssrc.ibms.dbom.model.DBom;
import com.cssrc.ibms.dbom.model.DBomNode;


/**
 * 对象功能:DBom分类管理 Service类 .
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-13 上午08:10:24 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-13 上午08:10:24
 * @see
 */
@Service
public class DBomService extends BaseService<DBom>{

	@Resource
	private DBomDao dBomDao;
	
	@Resource
	private DBomNodeDao dBomNodeDao;
	
	@Override
	protected IEntityDao<DBom, Long> getEntityDao() {
		return dBomDao;
	}
	
	/**
	 * 检查DBom分类代号是否已存在.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午11:34:23 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:44:18
	 * @param id
	 * @param code
	 * @return
	 * @see
	 */
	public boolean check(Long id, String code){		
		
		List<DBom> datas = new ArrayList<DBom>();
		if(id != 0){ //编辑
			DBom dbom = dBomDao.getById(id);
			if(!code.equals(dbom.getCode())){
				datas = dBomDao.getByCode(code);
			}
		}else{
			datas = dBomDao.getByCode(code);
		}
		return datas.size()>0?true:false;
	}

	/**
	 * 保存DBom分类信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-20 上午09:23:24 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:23:24
	 * @param dbom
	 * @see
	 */
	public ResultMessage save(DBom dbom){
		if(dbom.getId() != null){ //如果编辑DBOM分类，需要进行判断其代号是否更改；如果更改，要同时对CWM_DBOM_NODE表中dbom节点的父code更改
			DBom oldDbom = dBomDao.getById(dbom.getId());
			String oldCode = CommonTools.null2String(oldDbom.getCode());
			String newCode = CommonTools.null2String(dbom.getCode());
			if(!oldCode.equals(newCode)){
				List<DBomNode> nodeList = dBomNodeDao.getByPCode(oldCode);
				for(DBomNode dbomNode : nodeList){
					dbomNode.setPcode(newCode);
					dBomNodeDao.update(dbomNode);
				}
			}
			BeanUtils.copyProperties(dbom, oldDbom);//拷贝信息
			dBomDao.update(oldDbom);
			return new ResultMessage(ResultMessage.Success, "修改dbom分类成功!");
		}else{
			dbom.setId(UniqueIdUtil.genId());
			dBomDao.add(dbom);
			return new ResultMessage(ResultMessage.Success, "新增dbom分类成功!");
		}
	}

	/**
	 * 根据code 获取唯一dbom编号
	 * @param code
	 * @return
	 */
	public DBom getUniqueByCode(String dbomCode) {
		return this.dBomDao.getUniqueByCode(dbomCode);
	}
	
}
