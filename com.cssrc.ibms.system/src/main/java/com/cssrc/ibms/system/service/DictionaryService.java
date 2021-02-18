package com.cssrc.ibms.system.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.DictionaryDao;
import com.cssrc.ibms.system.dao.GlobalTypeDao;
import com.cssrc.ibms.system.model.Dictionary;
import com.cssrc.ibms.system.model.GlobalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 数据字典(业务表定义字段)
 * <p>Title:DictionaryService</p>
 * @author Yangbo 
 * @date 2016-8-3上午10:22:18
 */
@Service
public class DictionaryService extends BaseService<Dictionary> implements IDictionaryService{

	@Resource
	private DictionaryDao dictionaryDao;

	@Resource
	private GlobalTypeDao globalTypeDao;
	
	public List<Dictionary> getByTypeId(Long typeId) {
		return this.dictionaryDao.getByTypeId(typeId);
	}
	
	protected IEntityDao<Dictionary, Long> getEntityDao() {
		return this.dictionaryDao;
	}
	/**
	 * 由节点分类key得出相应数据字典列表
	 * @param nodeKey
	 * @return
	 */
	public List<Dictionary> getByNodeKey(String nodeKey) {
		GlobalType globalType = this.globalTypeDao.getByDictNodeKey(nodeKey);
		if (globalType == null) {
			return new ArrayList();
		}
		long typeId = globalType.getTypeId().longValue();
		return this.dictionaryDao.getByTypeId(typeId);
	}
	/**
	 * 由分类ID得出数字字典列表
	 * @param typeId
	 * @param needRoot 根节点
	 * @return
	 */
	public List<Dictionary> getByTypeId(long typeId, boolean needRoot) {
		GlobalType globalType = (GlobalType) this.globalTypeDao.getById(Long.valueOf(typeId));

		List<Dictionary> list = this.dictionaryDao.getByTypeId(typeId);
		for (Dictionary dic : list) {
			dic.setType(globalType.getType());
		}
		if (needRoot) {
			Dictionary dictionary = new Dictionary();
			dictionary.setDicId(Long.valueOf(typeId));
			dictionary.setParentId(Long.valueOf(0L));
			dictionary.setItemName(globalType.getTypeName());
			dictionary.setType(globalType.getType());
			list.add(0, dictionary);
		}
		return list;
	}

	public List<Dictionary> getByParentId(long parentId) {
		List<Dictionary> list = this.dictionaryDao.getByParentId(parentId);
		return list;
	}

	public void delByDicId(Long dicId) {
		Dictionary dictionary = (Dictionary) this.dictionaryDao.getById(dicId);
		String nodePath = dictionary.getNodePath();
		List<Dictionary> list = this.dictionaryDao.getByNodePath(nodePath);
		for (Dictionary dic : list)
			this.dictionaryDao.delById(dic.getDicId());
	}

	public boolean isItemKeyExists(long typeId, String itemKey) {
		return this.dictionaryDao.isItemKeyExists(typeId, itemKey);
	}

	public boolean isItemKeyExistsForUpdate(long dicId, long typeId,
			String itemKey) {
		return this.dictionaryDao.isItemKeyExistsForUpdate(dicId, typeId,
				itemKey);
	}

	public void updSn(Long[] lAryId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (int i = 0; i < lAryId.length; i++) {
			int sn = i + 1;
			Long dicId = lAryId[i];
			this.dictionaryDao.updSn(dicId, Integer.valueOf(sn));
		}
	}
	
	/**
	 * 根据数据字典id更新所有相关数据字典的是否删除状态
	 */
	public void updateStatus(Long dicId,Long currentUserId,short status) {
		Dictionary dictionary = (Dictionary) this.dictionaryDao.getById(dicId);
		String nodePath = dictionary.getNodePath();
		//子组织路径
		nodePath = StringUtil.isNotEmpty(nodePath) ? nodePath + "%" : "";
		this.dictionaryDao.updateStatusByPath(nodePath,currentUserId, status);
	}

	public void move(Long targetId, Long dragId, String moveType) {
		Dictionary target = (Dictionary) this.dictionaryDao.getById(targetId);
		Dictionary dragged = (Dictionary) this.dictionaryDao.getById(dragId);

		String nodePath = dragged.getNodePath();
		List<Dictionary> list = this.dictionaryDao.getByNodePath(nodePath);

		for (Dictionary dictionary : list) {
			if (("prev".equals(moveType)) || ("next".equals(moveType))) {
				String targetPath = target.getNodePath();
				String parentPath = targetPath.endsWith(".") ? targetPath
						.substring(0, targetPath.length() - 1) : targetPath;

				parentPath = parentPath.substring(0, parentPath
						.lastIndexOf(".") + 1);

				if (dictionary.getDicId().equals(dragId)) {
					dictionary.setParentId(target.getParentId());
					dictionary.setNodePath(parentPath + dragId + ".");
				} else {
					String path = dictionary.getNodePath();
					String tmpPath = parentPath + dragId + "."
							+ path.replaceAll(nodePath, "");
					dictionary.setNodePath(tmpPath);
				}
				if ("prev".equals(moveType))
					dictionary.setSn(Long
							.valueOf(target.getSn().longValue() - 1L));
				else {
					dictionary.setSn(Long
							.valueOf(target.getSn().longValue() + 1L));
				}

			} else if (dictionary.getDicId().equals(dragId)) {
				dictionary.setParentId(targetId);

				dictionary.setNodePath(target.getNodePath()
						+ dictionary.getDicId() + ".");
			} else {
				String path = dictionary.getNodePath();

				String tmpPath = path.replaceAll(nodePath, "");

				String targetPath = target.getNodePath();

				String tmp = targetPath + dragged.getDicId() + "." + tmpPath;

				dictionary.setNodePath(tmp);
			}
			dictionary.setDic_updateId(UserContextUtil.getCurrentUserId());
			dictionary.setDic_updateTime(new Date());
			this.dictionaryDao.update(dictionary);
		}
	}
	
	public List<String> getAllByItemName(String itemName) {
		return this.dictionaryDao.getAllByItemName(itemName);
	}

	public List<Dictionary> getByItemName(String itemName) {
		return this.dictionaryDao.getByItemName(itemName);
	}
	/**
	 * 获得所有itemName
	 * @return
	 */
	public List<String> getAllItems() {
		return this.dictionaryDao.getAllItems();
	}

    @Override
    public List<Map<String, Object>> getByNodeDictKeyGangedSet(String code)
    {
        GlobalType globalType = this.globalTypeDao.getByDictNodeKey(code);
        if (globalType == null) {
            return new ArrayList<Map<String, Object>>();
        }
        long typeId = globalType.getTypeId().longValue();
        return this.dictionaryDao.getByNodeDictKeyGangedSet(typeId);
    }
}
