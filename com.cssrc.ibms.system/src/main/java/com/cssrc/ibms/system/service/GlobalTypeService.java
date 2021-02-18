package com.cssrc.ibms.system.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.migration.model.IOutDicGlobalType;
import com.cssrc.ibms.api.migration.model.IOutDictionary;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.model.ISysTypeKey;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.system.dao.DictionaryDao;
import com.cssrc.ibms.system.dao.GlobalTypeDao;
import com.cssrc.ibms.system.dao.SysTypeKeyDao;
import com.cssrc.ibms.system.model.Dictionary;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.model.SysTypeKey;

@Service
public class GlobalTypeService extends BaseService<GlobalType> implements IGlobalTypeService{
	@Resource
	private GlobalTypeDao globalTypeDao;
	@Resource 
	private SysTypeKeyDao sysTypeKeyDao;
	@Resource 
	private DictionaryDao dictionaryDao;
    @Resource 
    private IDefinitionService definitionService;
    
    
	@Override
	protected IEntityDao<GlobalType, Long> getEntityDao() {
		return globalTypeDao;
	}

	
	public GlobalType getByDictNodeKey(String nodeKey)
   {
    return this.globalTypeDao.getByDictNodeKey(nodeKey);
  } 




	/**
	 * 根据catKey获取分类的xml数据。
	 * @param catKEY
	 * @return
	 */
	public String getXmlByCatkey(String catKEY){
		
		List<GlobalType> list = globalTypeDao.getByCatKey(catKEY);// 顶级
		//将已逻辑删除的数据用*标记
		List<GlobalType> typeLists = list;
		for(GlobalType globalType:typeLists){
			String typeName = globalType.getTypeName();
			if(globalType.getGltype_delFlag()!=null&&globalType.getGltype_delFlag()==1){
				globalType.setTypeName(typeName+"*");
			}
			
		}
		SysTypeKey sysTypeKey = this.sysTypeKeyDao.getByKey(catKEY);
		 Long typeId = sysTypeKey.getTypeKeyId();
		
		StringBuffer sb = new StringBuffer("<folder id='0' label='全部'>");
		if(BeanUtils.isNotEmpty(typeLists)){
			for(GlobalType gt : typeLists){
				if (typeId.equals(gt.getParentId())) {
				sb.append("<folder id='" + gt.getTypeId() + "' label='" + gt.getTypeName() + "'>");
				sb.append(getChildList(typeLists,gt.getTypeId()));
				sb.append("</folder>");
				}
			}
		}
		sb.append("</folder>");
		return sb.toString();
	}
	
	/**
	 * 递归调用获取xml。
	 * @param list
	 * @param parentId
	 * @return
	 */
	private String getChildList(List<GlobalType> list,Long parentId){
		StringBuffer sb = new StringBuffer("");
		if(BeanUtils.isNotEmpty(list)){
			for(GlobalType gt : list){
				if(gt.getParentId().equals(parentId)){
					sb.append("<folder id='" + gt.getTypeId() + "' label='" + gt.getTypeName() + "'>");
					sb.append(getChildList(list,gt.getTypeId()));
					sb.append("</folder>");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 根据catkey取得根节点
	 * @param catKey
	 * @return
	 */
	public GlobalType getRootByCatKey(String catKey){
		SysTypeKey sysTypeKey = this.sysTypeKeyDao.getByKey(catKey);
		GlobalType globalType=new GlobalType();
		globalType.setTypeName(sysTypeKey.getTypeName());
		globalType.setCatKey(sysTypeKey.getTypeKey());
		globalType.setParentId(0L);
		globalType.setIsParent("true");
		globalType.setTypeId(sysTypeKey.getTypeKeyId());
		globalType.setType(sysTypeKey.getType());
		globalType.setNodePath("." + sysTypeKey.getTypeKeyId() +".");
		return globalType;
	}
	/**
	 * 分类Key树
	 * @param catKey
	 * @param hasRoot
	 * @return
	 */
	public List<GlobalType> getByCatKey(String catKey, boolean hasRoot)
	{
		List<GlobalType> list = this.globalTypeDao.getByCatKey(catKey);

		if (hasRoot) {
			SysTypeKey sysTypeKey = this.sysTypeKeyDao.getByKey(catKey);
			GlobalType globalType = new GlobalType();
			globalType.setTypeName(sysTypeKey.getTypeName());
			globalType.setCatKey(sysTypeKey.getTypeKey());
			globalType.setParentId(Long.valueOf(0L));
			globalType.setIsParent("true");
			globalType.setTypeId(sysTypeKey.getTypeKeyId());
			globalType.setType(sysTypeKey.getType());
			globalType.setNodePath("." + sysTypeKey.getTypeKeyId() + ".");
			list.add(0, globalType);
		}
		return list;
	}
	
    /**
     * 分类Key树 追加流程定义
     * @param catKey
     * @param hasRoot
     * @return
     */
    public List<GlobalType> getFlowTreeByCatKey()
    {
        String catKey = GlobalType.CAT_FLOW;
        List<GlobalType> list = this.globalTypeDao.getByCatKey(catKey);
        SysTypeKey sysTypeKey = this.sysTypeKeyDao.getByKey(catKey);
        list.add(0, new GlobalType(sysTypeKey));
        List<GlobalType> flowGlobalTypes=new ArrayList<GlobalType>();
        for(GlobalType globalType:list){
            //追加流程 定义
            List<? extends IDefinition> definitions=definitionService.getByTypeId(globalType.getTypeId());
            for(IDefinition def:definitions){
                flowGlobalTypes.add( new GlobalType(sysTypeKey,globalType,def,"false"));
            }
        }
        list.addAll(flowGlobalTypes);
        return list;
    }	
	/**
	 * 根据分类类型和分类别名获取分类数据
	 * @param catKey
	 * @param nodeKey
	 * @return
	 */
    public GlobalType getByCateKeyAndNodeKey(String catKey, String nodeKey){
    	return this.globalTypeDao.getByCateKeyAndNodeKey(catKey, nodeKey);
    }
	/**
	 * 根据分类类型和分类名称获取分类数据
	 * 
	 * @param catKey
	 * @param typeName
	 * @return
	 */
	public List<GlobalType> getByCatKeyAndTypeName(String catKey, String typeName){
		return this.globalTypeDao.getByCatKeyAndTypeName(catKey, typeName);
	}
	/**
	 * 
	 * @param parentId
	 * @return
	 */
	public List<GlobalType> getByParentId(Long parentId)
	{
		return this.globalTypeDao.getByParentId(parentId.longValue());
	}
	
	
	public void delByTypeId(Long typeId)
	{
		if (BeanUtils.isEmpty(typeId)) return;

		GlobalType gt = (GlobalType)this.globalTypeDao.getById(typeId);

		String oldNodePath = gt.getNodePath();

		List<GlobalType> childrenList = this.globalTypeDao.getByNodePath(oldNodePath);

		for (GlobalType globalType : childrenList) {
			long Id = globalType.getTypeId().longValue();
			this.globalTypeDao.delById(Long.valueOf(Id));
			this.dictionaryDao.delByTypeId(Long.valueOf(Id));
		}
	}


	public String exportXml(long typeId)
	{
		String stringXml = "";
		String key = "";
		String name = "";
		String catKey = "";
		int type = 1;
		boolean isDic = false;
		GlobalType globalType = (GlobalType)this.globalTypeDao.getById(Long.valueOf(typeId));
		if (globalType == null) {
			SysTypeKey sysTypeKey = (SysTypeKey)this.sysTypeKeyDao.getById(Long.valueOf(typeId));
			key = sysTypeKey.getTypeKey();
			name = sysTypeKey.getTypeName();
		} else {
			key = globalType.getNodeKey();
			name = globalType.getTypeName();
			catKey = globalType.getCatKey();
			type = globalType.getType().intValue();
		}
		if ((key.equals(GlobalType.CAT_DIC)) || (catKey.equals(GlobalType.CAT_DIC))) {
			isDic = true;
		}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("items");
		Element node = root.addElement("item");
		node.addAttribute("key", key);
		node.addAttribute("name", name);
		node.addAttribute("type", Integer.toString(type));
		if (isDic) {
			List<Dictionary> list = this.dictionaryDao.getByParentId(typeId);
			if ((list != null) && (list.size() != 0)) {
				for (Dictionary dic : list) {
					addElementByDic(dic, node);
				}
			}
		}
		List<GlobalType> subs = this.globalTypeDao.getByParentId(typeId);
		if ((subs != null) && (subs.size() != 0)) {
			for (GlobalType g : subs) {
				addElements(g, node, isDic);
			}
		}
		stringXml = doc.asXML();
		return stringXml;
	}
	
	public void addElements(GlobalType g, Element e, boolean isDic)
	{
		Element nodes = e.addElement("item");
		nodes.addAttribute("key", g.getNodeKey());
		nodes.addAttribute("name", g.getTypeName());
		nodes.addAttribute("type", Integer.toString(g.getType().intValue()));
		if (!isDic) {
			if ((g.getIsLeaf().intValue() == 0) || (g.getIsLeaf() == null)) return; 
		}
		else {
			List<Dictionary> list = this.dictionaryDao.getByTypeId(g.getTypeId().longValue());
			if ((list != null) && (list.size() != 0)) {
				for (Dictionary dic : list) {
					addElementByDic(dic, nodes);
				}
			}
		}
		List<GlobalType> subs = this.globalTypeDao.getByParentId(g.getTypeId().longValue());
		if ((subs != null) && (subs.size() != 0))
			for (GlobalType gt : subs)
				if (gt.getIsLeaf().intValue() == 1) {
					addElements(gt, nodes, isDic);
				} else {
					Element node = nodes.addElement("item");
					node.addAttribute("key", gt.getNodeKey());
					node.addAttribute("name", gt.getTypeName());
					node.addAttribute("type", Integer.toString(g.getType().intValue()));
				}
	}

	
	public void addElementByDic(Dictionary dic, Element e)
	{
		Element data = e.addElement("data");
		data.addAttribute("key", dic.getItemKey());
		data.addAttribute("name", dic.getItemName());
		data.addAttribute("type", Integer.toString(dic.getType().intValue()));
		data.setText(dic.getItemValue());
		List<Dictionary> list = this.dictionaryDao.getByParentId(dic.getDicId().longValue());
		if ((list != null) && (list.size() != 0))
			for (Dictionary dictionary : list) {
				List<?> subs = this.dictionaryDao.getByParentId(dictionary.getDicId().longValue());
				if ((subs != null) && (subs.size() != 0)) {
					addElementByDic(dictionary, data);
				} else {
					Element sub = data.addElement("data");
					sub.addAttribute("name", dictionary.getItemName());
					sub.addAttribute("key", dictionary.getItemKey());
					sub.addAttribute("type", Integer.toString(dic.getType().intValue()));
					sub.setText(dictionary.getItemValue());
				}
			}
	}
	
	
	@SuppressWarnings("unchecked")
	public void importXml(InputStream inputStream, long typeId)
	{
		SysTypeKey sysTypeKey = null;
		GlobalType globalType = (GlobalType)this.globalTypeDao.getById(Long.valueOf(typeId));
		String catKey = "";
		String basePath = "";
		if (globalType == null) {
			sysTypeKey = (SysTypeKey)this.sysTypeKeyDao.getById(Long.valueOf(typeId));
			catKey = sysTypeKey.getTypeKey();
			basePath = typeId + ".";
		} else {
			catKey = globalType.getCatKey();
			basePath = globalType.getNodePath();
		}

		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for (Element node : list)
			if ((catKey.equals(GlobalType.CAT_DIC)) && (node.getName().equals("data")))
				addDicData(node, typeId + ".");
			else{
				boolean isExist = this.isNodeKeyExists(catKey, node.attributeValue("key"));
				if (isExist){
					throw new RuntimeException("节点KEY已存在!");
				}else{
					addGlobalType(node, basePath, catKey);
				}
			}
	}
	
	@SuppressWarnings("unchecked")
	public void addDicData(Element e, String basePath)
	{
		String name = e.attributeValue("name");
		String key = e.attributeValue("key");
		String type = e.attributeValue("type");
		String value = e.getText();
		long dicId = UniqueIdUtil.genId();
		Dictionary dic = new Dictionary();
		dic.setDicId(Long.valueOf(dicId));
		String[] paths = basePath.split("\\.");
		String parentId = paths[(paths.length - 1)];
		dic.setParentId(Long.valueOf(Long.parseLong(parentId)));
		dic.setItemKey(key);
		if (type == null)
			dic.setType(Integer.valueOf(1));
		else {
			dic.setType(Integer.valueOf(Integer.parseInt(type)));
		}
		dic.setItemName(name);
		dic.setItemValue(value);
		dic.setSn(Long.valueOf(0L));
		dic.setNodePath("." + basePath + dicId + ".");
		dic.setTypeId(Long.valueOf(Long.parseLong(paths[0])));
		dic.setDic_createTime(new Date());
		dic.setDic_creatorId(UserContextUtil.getCurrentUserId());
		dic.setDic_updateId(UserContextUtil.getCurrentUserId());
		dic.setDic_updateTime(new Date());
		dic.setDic_delFlag(Short.valueOf((short) 0));
		this.dictionaryDao.add(dic);
		List<Element> subs = e.elements();
		if ((subs != null) && (subs.size() != 0))
			for (Element data : subs)
				addDicData(data, dic.getNodePath());
	}
	
	
	@SuppressWarnings("unchecked")
	public void addGlobalType(Element e, String typeId, String catKey)
	{
		String name = e.attributeValue("name");
		String key = e.attributeValue("key");
		String type = e.attributeValue("type");
		long id = UniqueIdUtil.genId();
		String[] paths = typeId.split("\\.");
		GlobalType g = new GlobalType();
		g.setTypeId(Long.valueOf(id));
		g.setCatKey(catKey);
		g.setDepth(Integer.valueOf(paths.length));
		g.setNodePath("." + typeId + id + ".");
		g.setParentId(Long.valueOf(Long.parseLong(paths[(paths.length - 1)])));
		g.setNodeKey(key);
		g.setTypeName(name);
		g.setGltype_createTime(new Date());
		g.setGltype_creatorId(UserContextUtil.getCurrentUserId());
		g.setGltype_updateId(UserContextUtil.getCurrentUserId());
		g.setGltype_updateTime(new Date());
		g.setGltype_delFlag(Short.valueOf((short) 0));
		if (type == null)
			g.setType(Integer.valueOf(1));
		else {
			g.setType(Integer.valueOf(Integer.parseInt(type)));
		}
		g.setSn(Long.valueOf(0L));
		if ((e.elements() != null) || (e.elements().size() != 0)) {
			g.setIsLeaf(Integer.valueOf(1));
			add(g);
			List<Element> list = e.elements();
			for (Element node : list)
				if ((catKey.equals(GlobalType.CAT_DIC)) && (node.getName().equals("data")))
					addDicData(node, id + ".");
				else{
					boolean isExist = this.isNodeKeyExists(catKey, node.attributeValue("key"));
					if (isExist){
						throw new RuntimeException("节点KEY已存在!");
					}else{
						addGlobalType(node, typeId + id + ".", catKey);
					}
				}
		}
		else
		{
			g.setIsLeaf(Integer.valueOf(0));
			add(g);
		}
	}
	
	public void updSn(Long typeId, Long sn)
	{
		this.globalTypeDao.updSn(typeId, sn);
	}
	
	/**
	 * 根据分类id更新所有相关分类的是否删除状态
	 */
	public void updateStatus(Long typeId,Long currentUserId,short status) {
		GlobalType globalType = (GlobalType) this.globalTypeDao.getById(typeId);
		String globalPath = globalType.getNodePath();
		//子组织路径
		globalPath = StringUtil.isNotEmpty(globalPath) ? globalPath + "%" : "";
		
		//更新关联数据字典
		this.dictionaryDao.updateStatusByTypeId(typeId,currentUserId, status);
		
		this.globalTypeDao.updateStatus(globalPath,currentUserId, status);
	}
	
	public void move(Long targetId, Long dragId, String moveType)
	{
		GlobalType target = (GlobalType)this.globalTypeDao.getById(targetId);
		GlobalType dragged = (GlobalType)this.globalTypeDao.getById(dragId);

		String nodePath = dragged.getNodePath();
		List<GlobalType> list = getByNodePath(nodePath);

		for (GlobalType globalType : list)
		{
			if (("prev".equals(moveType)) || ("next".equals(moveType))) {
				String targetPath = target.getNodePath();
				String parentPath = targetPath.endsWith(".") ? targetPath.substring(0, targetPath.length() - 1) : targetPath;

				parentPath = parentPath.substring(0, parentPath.lastIndexOf(".") + 1);

				if (globalType.getTypeId().equals(dragId)) {
					globalType.setParentId(target.getParentId());
					globalType.setNodePath(parentPath + dragId + ".");
				}
				else {
					String path = globalType.getNodePath();
					String tmpPath = parentPath + dragId + "." + path.replaceAll(nodePath, "");
					globalType.setNodePath(tmpPath);
				}
				globalType.setDepth(target.getDepth());

				if ("prev".equals(moveType))
					globalType.setSn(Long.valueOf(target.getSn().longValue() - 1L));
				else {
					globalType.setSn(Long.valueOf(target.getSn().longValue() + 1L));
				}

			}
			else if (globalType.getTypeId().equals(dragId))
			{
				globalType.setParentId(targetId);

				globalType.setNodePath(target.getNodePath() + globalType.getTypeId() + ".");
			}
			else
			{
				String path = globalType.getNodePath();

				String tmpPath = path.replaceAll(nodePath, "");

				String targetPath = target.getNodePath();

				String tmp = targetPath + dragged.getTypeId() + "." + tmpPath;

				globalType.setNodePath(tmp);
			}

			globalType.setGltype_updateId(UserContextUtil.getCurrentUserId());
			globalType.setGltype_updateTime(new Date());
			this.globalTypeDao.update(globalType);
		}
	}
	
	
	public List<GlobalType> getByNodePath(String nodePath)
	{
		return this.globalTypeDao.getByNodePath(nodePath);
	}
	
	
	public GlobalType getInitGlobalType(int isRoot, long parentId)throws Exception
	{
		GlobalType globalType = new GlobalType();
		Long typeId = Long.valueOf(UniqueIdUtil.genId());

		if (isRoot == 1) {
			SysTypeKey sysTypeKey = (SysTypeKey)this.sysTypeKeyDao.getById(Long.valueOf(parentId));
			globalType.setCatKey(sysTypeKey.getTypeKey());
			globalType.setTypeName(sysTypeKey.getTypeName());
			globalType.setParentId(Long.valueOf(parentId));
			globalType.setNodePath("." + parentId + "." + typeId + ".");
			globalType.setType(sysTypeKey.getType());
		}
		else
		{
			globalType = (GlobalType)this.globalTypeDao.getById(Long.valueOf(parentId));
			String nodePath = globalType.getNodePath();
			globalType.setNodePath(nodePath + typeId + ".");
		}
		globalType.setTypeId(typeId);
		return globalType;
	}
	
	
	public Set<GlobalType> getByFormRightCat(Long userId, String roleIds, String orgIds, boolean hasRoot)
	{
		Set<GlobalType> globalTypeSet = new LinkedHashSet<GlobalType>();

		List<GlobalType> globalTypeList = this.globalTypeDao.getByFormRights(GlobalType.CAT_FORM, userId, roleIds, orgIds);
		globalTypeSet.addAll(globalTypeList);

		for (GlobalType globalType : globalTypeList) {
			if (StringUtils.isNotEmpty(globalType.getNodePath())) {
				String parentNodePath = globalType.getNodePath();
				int index = parentNodePath.indexOf(globalType.getTypeId().toString());
				if (index != -1) {
					parentNodePath = parentNodePath.substring(0, index);
				}
				String[] nodePaths = parentNodePath.split("[.]");

				if (nodePaths.length >= 2) {
					for (int i = 1; i < nodePaths.length; i++) {
						GlobalType parentType = (GlobalType)this.globalTypeDao.getById(new Long(nodePaths[i]));
						globalType.setGltype_createTime(new Date());
						globalType.setGltype_creatorId(UserContextUtil.getCurrentUserId());
						globalType.setGltype_updateId(UserContextUtil.getCurrentUserId());
						globalType.setGltype_updateTime(new Date());
						globalType.setGltype_delFlag(Short.valueOf((short) 0));
						globalTypeSet.add(parentType);
					}
				}
			}
		}

		if (hasRoot) {
			SysTypeKey sysTypeKey = this.sysTypeKeyDao.getByKey(GlobalType.CAT_FORM);
			GlobalType globalType = new GlobalType();
			globalType.setTypeName(sysTypeKey.getTypeName());
			globalType.setCatKey(sysTypeKey.getTypeKey());
			globalType.setParentId(Long.valueOf(0L));
			globalType.setIsParent("true");
			globalType.setTypeId(sysTypeKey.getTypeKeyId());
			globalType.setType(sysTypeKey.getType());
			globalType.setNodePath(sysTypeKey.getTypeKeyId() + ".");
			globalType.setGltype_createTime(new Date());
			globalType.setGltype_creatorId(UserContextUtil.getCurrentUserId());
			globalType.setGltype_updateId(UserContextUtil.getCurrentUserId());
			globalType.setGltype_updateTime(new Date());
			globalType.setGltype_delFlag(Short.valueOf((short) 0));
			globalTypeSet.add(globalType);
		}
		return globalTypeSet;
	}
	/**
	 * 用户个人分类管理节点树
	 *@author YangBo @date 2016年10月11日下午4:52:16
	 *@param catKey
	 *@param userId
	 *@param hasRoot
	 *@return
	 */
	public List<GlobalType> getPersonType(String catKey, Long userId, boolean hasRoot)
	{
		List<GlobalType> list = this.globalTypeDao.getPersonType(catKey, userId);

		if (hasRoot) {
			SysTypeKey sysTypeKey = this.sysTypeKeyDao.getByKey(catKey);
			GlobalType globalType = new GlobalType();
			globalType.setTypeName(sysTypeKey.getTypeName());
			globalType.setCatKey(sysTypeKey.getTypeKey());
			globalType.setParentId(Long.valueOf(0L));
			globalType.setIsParent("true");
			globalType.setTypeId(sysTypeKey.getTypeKeyId());
			globalType.setType(sysTypeKey.getType());
			globalType.setNodePath("." + sysTypeKey.getTypeKeyId() + ".");
			globalType.setNodeKey(catKey);
			list.add(0, globalType);
		}
		return list;
	}
	
	public boolean isNodeKeyExistsForUpdate(Long typeId, String catKey, String nodeKey) {
		return this.globalTypeDao.isNodeKeyExistsForUpdate(typeId, catKey, nodeKey);
	}
	
	public boolean isNodeKeyExists(String catKey, String nodeKey) {
		return this.globalTypeDao.isNodeKeyExists(catKey, nodeKey);
	}

	
	/**
	 * 根据节点Id获取关联的所有子节点
	 * @author YangBo
	 * @param typeId
	 * @return
	 */
	public String getIdsByParentId(Long typeId){
		String nodePath = this.globalTypeDao.getById(typeId).getNodePath();
		List<GlobalType> list = this.getByNodePath(nodePath);
		String typeIds ="";
		for(GlobalType globalType: list){
			typeIds +=globalType.getTypeId()+",";
		}
		typeIds = typeIds.substring(0,typeIds.length()-1);
		return typeIds;
	}
	public Long saveSchemaGlobalType(IOutDicGlobalType outGlobalType,StringBuffer log){
		try{
			GlobalType gl = globalTypeDao.getByDictNodeKey(outGlobalType.getNodeKey());
			GlobalType globalType = GlobalType.transDicGlobalType(outGlobalType);	
			SysTypeKey sysTypeKey = sysTypeKeyDao.getByKey(ISysTypeKey.TYPE_DIC);
			Long typeId = 0L;
			if(BeanUtils.isEmpty(gl)){
				globalType.setTypeId(UniqueIdUtil.genId());
				globalType.setNodePath("."+sysTypeKey.getTypeKeyId()+"."+globalType.getTypeId()+".");
				globalType.setParentId(sysTypeKey.getTypeKeyId());
				this.add(globalType);
				typeId = globalType.getTypeId();
			}else{
				gl.setTypeName(globalType.getTypeName());
				gl.setNodeKey(globalType.getNodeKey());
				gl.setNodePath("."+sysTypeKey.getTypeKeyId()+"."+gl.getTypeId()+".");
				gl.setParentId(sysTypeKey.getTypeKeyId());
				typeId = gl.getTypeId();
			}
			log.append("    --->schema字典"+outGlobalType.getTypeName()+"转换成功").append("\r\n");
			return typeId;
		}catch(Exception e){
    		e.printStackTrace();
    		log.append("    --->"+outGlobalType.getTypeName()+"字典转换失败，原因如下："
    				+e.getMessage()).append("\r\n");
    		return 0L;
    	}
	}
	public String createFormGlobalType(JSONObject obj){
		GlobalType from = (GlobalType)JSONObject.toBean(obj, GlobalType.class);
		GlobalType gl = globalTypeDao.getByCateKeyAndNodeKey(from.getCatKey(), from.getNodeKey());
		String nodeKey = from.getNodeKey();		
		if(BeanUtils.isEmpty(gl)){
			SysTypeKey sysTypeKey = sysTypeKeyDao.getByKey(ISysTypeKey.TYPE_FORM);
			gl = (GlobalType)JSONObject.toBean(obj, GlobalType.class);
			gl.setNodePath("."+sysTypeKey.getTypeKeyId()+"."+from.getTypeId()+".");
			gl.setParentId(sysTypeKey.getTypeKeyId());
			this.add(gl);
		}
		return nodeKey;
	}
	public boolean saveDicGlobalType(IOutDicGlobalType outGlobalType,StringBuffer log){		
		try{
			GlobalType schemaType = globalTypeDao.getById(outGlobalType.getParentId());
			GlobalType globalType = GlobalType.transDicGlobalType(outGlobalType);		
			boolean isExist = this.isNodeKeyExists(globalType.getCatKey(), globalType.getNodeKey());
			Long typeId = 0L;
			if(isExist){
				GlobalType old = this.getByDictNodeKey(globalType.getNodeKey());
				old.setTypeName(outGlobalType.getTypeName());
				old.setNodeKey(outGlobalType.getNodeKey());
				old.setNodePath(schemaType.getNodePath()+old.getTypeId()+".");
				old.setParentId(schemaType.getTypeId());
				this.update(old);
				typeId = old.getTypeId();
			}else{
				globalType.setTypeId(UniqueIdUtil.genId());
				globalType.setNodePath(schemaType.getNodePath()+globalType.getTypeId()+".");
				globalType.setParentId(schemaType.getTypeId());
				this.add(globalType);
				typeId = globalType.getTypeId();
			}
			//添加字典项
			List<IOutDictionary> dicList = outGlobalType.getDicList();
			dictionaryDao.delByTypeId(typeId);
			for(IOutDictionary outDic : dicList){
				Dictionary  dic = Dictionary.transDic(outDic);
				dic.setDicId(UniqueIdUtil.genId());
				dic.setTypeId(typeId);
				dictionaryDao.add(dic);
			}
			log.append("    --->"+outGlobalType.getTypeName()+"字典转换成功").append("\r\n");
			return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		log.append("    --->"+outGlobalType.getTypeName()+"字典转换失败，原因如下："
    				+e.getMessage()).append("\r\n");
    		return false;
    	}
	}
}

