package com.cssrc.ibms.dbom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.intf.IQueryFieldService;
import com.cssrc.ibms.api.form.intf.IQuerySettingService;
import com.cssrc.ibms.api.form.intf.IQuerySqlService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IQueryField;
import com.cssrc.ibms.api.form.model.IQuerySetting;
import com.cssrc.ibms.api.form.model.IQuerySql;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dbom.dao.DBomDao;
import com.cssrc.ibms.dbom.dao.DBomNodeDao;
import com.cssrc.ibms.dbom.model.DBom;
import com.cssrc.ibms.dbom.model.DBomNode;

/**
 * @ClassName: DbomMetaDataService
 * @Description: dbom 元数据 service
 * @author zxg
 * @date 2017年3月14日 下午3:05:49
 * 
 */
@Service("dbomMetaDataService")
public class DbomMetaDataService extends BaseService<DBomNode>
{
    @Resource
    private DBomDao dBomDao;
    
    @Resource
    private DBomNodeDao dBomNodeDao;
    
    @Resource
    IFormTableService formTableService;
    
    @Resource
    IQuerySqlService querySqlService;
    
    @Resource
    IFormFieldService formFieldService;
    
    @Resource
    IQueryFieldService queryFieldService;
    
    @Resource
    IFormDefService formDefService;
    
    @Resource
    IDataTemplateService dataTemplateService;
    
    @Resource
    IQuerySettingService querySettingService;
    
    @Override
    protected IEntityDao<DBomNode, Long> getEntityDao()
    {
        return dBomNodeDao;
    }
    
    /**
     * 获取dbom 树
     * 
     * @param code
     * @return
     */
    public DBom getDbomByCode(String code)
    {
        return dBomDao.getUniqueByCode(code);
    }
    
    /**
     * 根据dbom code 查找 dbom 分类下的所有树节点
     * 
     * @param dBomCode
     * @return
     */
    public List<DBomNode> getByDbomCode(String dBomCode)
    {
        return dBomNodeDao.getByDbomCode(dBomCode);
    }
    
    /**
     * 删除某个dbom 节点
     * 
     * @param id
     */
    public void deleteById(Long nodeId)
    {
        
        DBomNode dbomNode = dBomNodeDao.getById(nodeId);
        if (dbomNode.getCode() != null)
        {
            List<DBomNode> dbomNodeList = dBomNodeDao.getByPCode(dbomNode.getCode());
            for (DBomNode delNode : dbomNodeList)
            {
                deleteById(delNode.getId());
                dBomNodeDao.delById(delNode.getId());
            }
        }
        dBomNodeDao.delById(dbomNode.getId());
        
    }
    
    /**
     * 获取 dbom 节点元数据 返回 树结构 map list
     * 
     * @param pCode
     * @return
     */
    public List<Map<String, Object>> getMetaDbomNodeTree(String dBomCode)
    {
        List<Map<String, Object>> mnodes = new ArrayList<>();
        DBom dbom = this.dBomDao.getUniqueByCode(dBomCode);
        // 树根节点
        if(dbom!=null){
	        Map<String, Object> root = new HashMap<>();
	        root.put("id", dbom.getId());
	        root.put("text", dbom.getName());
	        root.put("type", "-1");
	        root.put("code", dbom.getCode());
	        root.put("children", getChild(dBomCode));
	        root.put("iconCls", "rootNode");
	        mnodes.add(root);
        }
        return mnodes;
    }
    
    /**
     * 递归获取 dbom 子节点 元数据
     * 
     * @param node
     * @return
     */
    private List<Map<String, Object>> getChild(String pcode)
    {
        List<DBomNode> lnodes = dBomNodeDao.getByPCode(pcode);
        List<Map<String, Object>> mnodes = new ArrayList<>();
        for (DBomNode n : lnodes)
        {
            Map<String, Object> mn = new HashMap<>();
            mn.put("id", n.getId());
            mn.put("text", n.getName());
            mn.put("code", n.getCode());
            mn.put("children", getChild(n.getCode()));
            mnodes.add(mn);
        }
        return mnodes;
    }
    
    /**
     * 获取dbom node 节点详细信息
     * 
     * @param id
     * @return
     */
    public JSONObject getDbomNodeDetail(Long id)
    {
        try
        {
            // 初始化点击节点
            JSONObject result = new JSONObject();
            DBomNode dbomNode = dBomNodeDao.getById(id);
            if(StringUtil.isNotEmpty(dbomNode.getPcode())&&StringUtil.isEmpty(dbomNode.getpNodeKey())){
                List<DBomNode> pnodes=dBomNodeDao.getByCode(dbomNode.getPcode());
                if(pnodes!=null&&pnodes.size()==1){
                    dbomNode.setpNodeKey(pnodes.get(0).getNodeKey());
                }
            }
            JSONObject dnode = JSONObject.fromObject(dbomNode);
            // 1、获取节点数据源
            String dataSource = CommonTools.null2String(dbomNode.getDataSource());
            if (dataSource.startsWith("W_"))
            {
                // 查询表
                String tableName = dataSource;
                tableName = tableName.substring(2, tableName.length());
                IFormTable formTable = formTableService.getByTableName(tableName.toLowerCase());
                dnode.put("hiddenDataSource", dataSource);
                dnode.put("dataSource", formTable.getTableDesc());
                dnode.put("hiddenTargetDataSource", dnode.get("targetDataSource"));
            }
            else if (!"".equals(dataSource))
            {
                // 查询视图
                String alias = dataSource;
                alias = alias.substring(2, alias.length());
                IQuerySql querySql = querySqlService.getByAlias(alias);
                dnode.put("hiddenDataSource", dataSource);
                dnode.put("dataSource", querySql.getName());
                dnode.put("hiddenTargetDataSource", dnode.get("targetDataSource"));
                
            }
            
            result.put("success", "true");
            result.put("data", dnode);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 校验 dbom node code 是否在数据库中已经存在
     * 
     * @param id
     * @param code
     * @return
     */
    public boolean check(Long id, String code)
    {
        List<DBomNode> datas = new ArrayList<DBomNode>();
        if (id != null && id != 0)
        { // 编辑
            DBomNode oldDbomNode = dBomNodeDao.getById(id);
            if (oldDbomNode!=null&&!code.equals(oldDbomNode.getCode()))
            {
                // 当前code 不等于 数据库code
                datas = dBomNodeDao.getByCode(code);
            }
        }
        else
        {
            datas = dBomNodeDao.getByCode(code);
        }
        return datas.size() > 0 ? true : false;
    }
    
    /**
     * 保存dbom node
     * 
     * @param dbomNode
     */
    public void saveDbomNode(DBomNode dbomNode)
    {
        try
        {
            Long id = dbomNode.getId();
            if (id != null)
            {
                // 如果编辑DBOM节点，需要进行判断其代号是否更改；如果更改，要同时对其子节点的父code更改
                DBomNode oldDBomNode = dBomNodeDao.getById(id);
                String oldCode = CommonTools.null2String(oldDBomNode.getCode());
                String newCode = CommonTools.null2String(dbomNode.getCode());
                if (!oldCode.equals(newCode))
                {
                    List<DBomNode> dbomNodeList = dBomNodeDao.getByPCode(oldCode);
                    for (DBomNode dbomnode : dbomNodeList)
                    {
                        dbomnode.setPcode(newCode);
                        dBomNodeDao.update(dbomnode);
                    }
                }
                BeanUtils.copyProperties(dbomNode, oldDBomNode);
                dBomNodeDao.update(oldDBomNode);
            }
            else
            {
                id = UniqueIdUtil.genId();
                dbomNode.setId(id);
                dBomNodeDao.add(dbomNode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取数据源
     * 
     * @param filter
     * @param mainTableName
     * @param relationType
     * @param modelType
     * @return
     */
    public Map<String, Object> getTables(QueryFilter filter, String mainTableName, String relationType, String modelType)
    {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try
        {
            // 重置当前页码
            int currentPage = filter.getPagingBean().getCurrentPage();
            int pageSize = filter.getPagingBean().getPageSize();
            currentPage = currentPage / pageSize + 1;
            filter.getPagingBean().setCurrentPage(currentPage);
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            if (!"数据视图".equals(modelType))
            { // 业务表
                if (!"关系表".equals(relationType))
                {
                    List<? extends IFormTable> tableList = formTableService.getAll(filter);
                    for (int index = 0; index < tableList.size(); index++)
                    {
                        Map<String, Object> tempMap = new HashMap<String, Object>();
                        tempMap.put("tableName", CommonTools.Obj2String(tableList.get(index).getTableName()));
                        tempMap.put("tableDesc", CommonTools.Obj2String(tableList.get(index).getTableDesc()));
                        tempMap.put("modelType", "数据类");
                        dataList.add(tempMap);
                    }
                }
                else
                {
                    mainTableName = mainTableName.substring(2, mainTableName.length());
                    IFormTable mainTable = formTableService.getByTableName(mainTableName);
                    List<? extends IFormField> subTableList =
                        formFieldService.getRelFieldsByTableId(null, mainTable.getTableId());
                    for (int index = 0; index < subTableList.size(); index++)
                    {
                        IFormTable subTable = formTableService.getById(subTableList.get(index).getTableId());
                        Map<String, Object> tempMap = new HashMap<String, Object>();
                        tempMap.put("tableName", CommonTools.Obj2String(subTable.getTableName()));
                        tempMap.put("tableDesc", CommonTools.Obj2String(subTable.getTableDesc()));
                        tempMap.put("modelType", "数据类");
                        dataList.add(tempMap);
                    }
                }
            }
            else
            { // 视图
                List<? extends IQuerySql> viewList = querySqlService.getAll(filter);
                for (int index = 0; index < viewList.size(); index++)
                {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("tableName", CommonTools.Obj2String(viewList.get(index).getAlias()));
                    tempMap.put("tableDesc", CommonTools.Obj2String(viewList.get(index).getName()));
                    tempMap.put("modelType", "数据视图");
                    dataList.add(tempMap);
                }
            }
            dataMap.put("success", "true");
            dataMap.put("result", dataList);
            dataMap.put("totalCounts", filter.getPagingBean().getTotalCount());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dataMap;
    }
    
    /**
     * 获取节点显示字段
     * 
     * @param request
     * @param filter
     * @return
     */
    public Map<String, Object> getShowFiled(HttpServletRequest request, QueryFilter filter)
    {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try
        {
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            // 重置当前页码
            int currentPage = filter.getPagingBean().getCurrentPage();
            int pageSize = filter.getPagingBean().getPageSize();
            currentPage = currentPage / pageSize + 1;
            filter.getPagingBean().setCurrentPage(currentPage);
            String tableName = filter.getRequest().getParameter("tableName");
            if (tableName.startsWith(ITableModel.CUSTOMER_TABLE_PREFIX))
            { // 查询表
                request.setAttribute("modelType", 1);
                tableName = tableName.substring(2, tableName.length());
                IFormTable formTable = formTableService.getByTableName(tableName);
                filter.addFilterForIB("tableId", formTable.getTableId());
                //添加ID字段
                Map<String, Object> id = new HashMap<String, Object>();
                id.put("fieldName", "ID");
                id.put("fieldDesc", "ID");
                id.put("tableDesc", formTable.getTableDesc());
                dataList.add(id);
                // 获取业务表字段
                List<? extends IFormField> fieldList = formFieldService.getAll(filter);
                for (int index = 0; index < fieldList.size(); index++)
                {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("fieldName", CommonTools.Obj2String(fieldList.get(index).getFieldName()));
                    tempMap.put("fieldDesc", CommonTools.Obj2String(fieldList.get(index).getFieldDesc()));
                    tempMap.put("tableDesc", formTable.getTableDesc());
                    
                    dataList.add(tempMap);
                }
            }
            else
            {// 查询视图
                request.setAttribute("modelType", 0);
                IQuerySql querySql = querySqlService.getByAlias(tableName.substring(2, tableName.length()));
                filter.addFilterForIB("sqlId", querySql.getId());
                List<? extends IQueryField> fieldList = queryFieldService.getAll(filter);
                for (int index = 0; index < fieldList.size(); index++)
                {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("fieldName", CommonTools.Obj2String(fieldList.get(index).getName()));
                    tempMap.put("fieldDesc", CommonTools.Obj2String(fieldList.get(index).getFieldDesc()));
                    tempMap.put("tableDesc", querySql.getName());
                    dataList.add(tempMap);
                }
            }
            dataMap.put("success", "true");
            dataMap.put("result", dataList);
            dataMap.put("totalCounts", filter.getPagingBean().getTotalCount());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dataMap;
    }
    
    public Map<String, Object> getTargetDataRelation(String id)
    {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try
        {
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dataMap;
    }
    
    /**
     * 
     * @param filter
     * @param dataSource
     * @return
     */
    public Map<String, Object> getFormDefList(QueryFilter filter, String dataSource)
    {
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try
        {
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            // 重置当前页码
            int currentPage = filter.getPagingBean().getCurrentPage();
            int pageSize = filter.getPagingBean().getPageSize();
            filter.getPagingBean().setCurrentPage(0);
            filter.getPagingBean().setPageSize(500);
            if (StringUtil.isEmpty(dataSource))
            {
                // 查询表 相关业务数据模板
                getTableDataTemplate(filter, dataSource, dataList);
                // 查询视图 相关业务数据模板
                getQueryDataTemplate(filter, dataSource, dataList);
            }
            else if (dataSource.startsWith("W_"))
            {
                // 查询表 相关业务数据模板
                getTableDataTemplate(filter, dataSource, dataList);
            }
            else
            {
                // 查询视图 相关业务数据模板
                getQueryDataTemplate(filter, dataSource, dataList);
            }
            // 重新分页显示数据
            List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
            int total = 1;
            for (int index = currentPage; index < dataList.size(); index++)
            {
                if (total % (pageSize + 1) == 0){
                    break;
                }
                finalList.add(dataList.get(index));
                total++;
            }
            dataMap.put("success", "true");
            dataMap.put("result", finalList);
            dataMap.put("totalCounts", dataList.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dataMap;
    }
    
    /**
     * 查询视图相关 业务数据模板
     * @param filter
     * @param dataSource
     * @param dataList
     * @throws Exception
     */
    private void getQueryDataTemplate(QueryFilter filter, String dataSource, List<Map<String, Object>> dataList)
        throws Exception
    {
        // 查询视图 相关业务数据模板
        List<? extends IQuerySql> querySqlList = querySqlService.getAll(filter);
        for (IQuerySql querySql : querySqlList)
        {
            if (StringUtil.isEmpty(dataSource) || dataSource.contains(querySql.getAlias()))
            {
                IQuerySetting querySetting = querySettingService.getBySqlId(querySql.getId());
                Map<String, Object> tempMap = new HashMap<String, Object>();
                tempMap.put("subject", CommonTools.Obj2String(querySql.getName()));
                tempMap.put("tableName", CommonTools.Obj2String(querySql.getAlias()));
                tempMap.put("categoryName", "数据视图");
                tempMap.put("dataTemplateURL", querySetting.getId());
                dataList.add(tempMap);
            }
        }
    }
    
    /**
     * 查询业务表相关业务数据模板
     * @param filter
     * @param dataSource
     * @param dataList
     */
    private void getTableDataTemplate(QueryFilter filter, String dataSource, List<Map<String, Object>> dataList)
    {
        // 查询表
        List<? extends IFormDef> formDefList = formDefService.getAll(filter);
        for (int index = 0; index < formDefList.size(); index++)
        {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            IFormDef formDef = formDefList.get(index);
            if (StringUtil.isEmpty(dataSource) || dataSource.contains(formDef.getTableName()))
            {
                // 只过滤出和当前选择的数据源/子节点数据源相关的业务模板
                tempMap.put("subject", CommonTools.Obj2String(formDef.getSubject()));
                tempMap.put("tableName", CommonTools.Obj2String(formDef.getTableName()));
                tempMap.put("categoryName", CommonTools.Obj2String(formDef.getCategoryName()));
                Long formKey = formDef.getFormKey();
                IDataTemplate dataTemplate = dataTemplateService.getByFormKey(formKey);
                if (dataTemplate == null)
                {
                    tempMap.put("dataTemplateURL", "");
                }
                else
                {
                    tempMap.put("dataTemplateURL", dataTemplate.getId());
                }
                dataList.add(tempMap);
            }
        }
    }

    public List<JSONObject> getDataSourceByName(String subDataSource){
        List<JSONObject> result = new ArrayList<JSONObject>();
        try {
            //查询表
            String[]tableNameArray = subDataSource.split(",");
            for(String tablename : tableNameArray){
                if(tablename.startsWith("W_")){
                    tablename = tablename.substring(2, tablename.length());
                    IFormTable formTable = formTableService.getByTableName(tablename);
                    JSONObject object = new JSONObject();
                    object.put("name", "W_" + formTable.getTableName());
                    object.put("desc", formTable.getTableDesc());
                    result.add(object);
                }else{
                    tablename = tablename.substring(2, tablename.length());
                    IQuerySql querySql = querySqlService.getByAlias(tablename);
                    JSONObject object = new JSONObject();
                    object.put("name", "V_" + querySql.getAlias());
                    object.put("desc", querySql.getName());
                    result.add(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
