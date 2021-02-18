package com.cssrc.ibms.dbom.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.intf.IQueryFieldService;
import com.cssrc.ibms.api.form.intf.IQuerySqlService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IQuerySql;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dbom.dao.DBomNodeDao;
import com.cssrc.ibms.dbom.model.DBomNode;

/**
 * @ClassName: DbomSqlFilterService
 * @Description: dbom tree 数据过滤sql 解析
 * @author zxg
 * @date 2017年3月17日 上午10:13:01
 * 
 */
@Service("dbomSqlFilterService")
public class DbomSqlFilterService
{
    protected Logger logger = LoggerFactory.getLogger(DBomNodeService.class);
    
    @Resource
    DBomNodeDao dBomNodeDao;
    
    @Resource
    IDataTemplateService dataTemplateService;
    
    @Resource
    IFormTableService formTableService;
    
    @Resource
    DBomNodeService dBomNodeService;
    
    @Resource
    IQuerySqlService querySqlService;
    
    @Resource
    IQueryFieldService queryFieldService;
    /**
     * 根据节点获取默认的 filter sql 此情况 只针对没有配置目标数据源 以及目标数据源关联关系
     * 
     * @param node
     * @param reltable
     * @param relation
     * @param nodeValue
     * @return
     */
    public String getFilterSqlByRelation(DBomNode node, String reltable, String relation, String nodeValue)
    {
        try
        {
            StringBuffer sql = new StringBuffer();
            // 节点key
            String nodeKey = node.getNodeKey();
            // 节点key 所属数据源
            String dataSource = node.getDataSource();
            // 目标数据源关联关系
            String[] relations = relation.split("and");
            for (String rel : relations)
            {
                // 获取 node data source key 值
                String filed = rel.split("=")[0];
                String relFiled = rel.split("=")[1];
                
                sql.append(relFiled);
                sql.append("='");
                sql.append(this.getValueByFiled(nodeKey, nodeValue, dataSource, filed) + "'");
                sql.append(" and ");
            }
            
            String filter = sql.substring(0, sql.length() - 5);
            filter = filter.replace(reltable, reltable.replace(ITableModel.CUSTOMER_TABLE_PREFIX, "").toLowerCase());
            
            return filter;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        
    }
    
    private String getValueByFiled(String nodeKey, String nodeValue, String dataSource, String filed)
    {
        StringBuffer sql;
        if(dataSource.startsWith(ITableModel.CUSTOMER_TABLE_PREFIX)){
             sql =
                new StringBuffer("select DISTINCT (" + filed + ") as \"fval\"  from " + dataSource + " where " + nodeKey + "='"
                    + nodeValue + "'");
        }else{
            IQuerySql querySql=this.querySqlService.getByAlias(dataSource.replace("V_", ""));
            sql =
                new StringBuffer("select (" + filed + ") as \"fval\"  from (" + querySql.getSql() + ")"+dataSource+" where " + nodeKey + "='"
                    + nodeValue + "'");
        }
       
        List<Map<String, Object>> result = this.dBomNodeDao.queryForList(sql.toString());
        if(result!=null&&result.size()==1){
        	return result.get(0).get("fval").toString();
        }else if(result!=null&&result.size()>1){
        	logger.warn("找到多条数据，key值："+nodeKey+":"+nodeValue);
        	return result.get(0).get("fval").toString();
        }else{
        	logger.error("数据错误。。。");
        	return null;
        }
    }
    
    /**
     * 根据节点获取默认的 filter sql 此情况 只针对没有配置目标数据源 以及目标数据源关联关系
     * 
     * @param curCode 节点 code
     * @param curId 节点 key
     */
    public String getFilterSqlByNode(DBomNode curNode, String curId)
    {
        try
        {
            String curCode = curNode.getCode();
            List<DBomNode> subNodes = this.dBomNodeDao.getByPCode(curCode);
            
            DBomNode sNode = null;
            String filterSql = null;
            // 如果这里找到多条子节点元数据，where 条件 sql 构造会是错的
            if (subNodes != null && subNodes.size() > 0)
            {
                // 目前 默认先只去第一条
                sNode = subNodes.get(0);
                filterSql = this.createWhereSql(sNode, curId,null).toString();
                String ds = sNode.getDataSource();
                filterSql = filterSql.replace(ds, ds.replace(ITableModel.CUSTOMER_TABLE_PREFIX, "").toLowerCase());
                filterSql = filterSql.replace("where", "");

            }
            else
            {
                filterSql = this.createCnodeWhereSql(curNode, curId).toString();
                filterSql = filterSql.replace(ITableModel.CUSTOMER_TABLE_PREFIX, "");
                filterSql = filterSql.replace("where", "");

            }
            return filterSql;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        
    }
    
    public StringBuffer createSelect(DBomNode cNode, String pNodeId,String customFilter){
        String ds = cNode.getDataSource();
        if(ds.startsWith(ITableModel.CUSTOMER_TABLE_PREFIX)){
            return this.createSelectSql(cNode, pNodeId, customFilter);
        }else if(ds.startsWith("V_")){
            return this.createSelectSqlView(cNode, pNodeId, customFilter);
        }else{
            throw new RuntimeException("数据源出错。。。");
        }
    }

    
    /**
     * 构建 dbom meta node 节点 data 的select sql,针对数据源为视图类型的
     * 
     * @param cNode 节点 meta
     * @param pNodeId 节点的父节点 data key
     * @return
     */
    public StringBuffer createSelectSqlView(DBomNode cNode, String pNodeId,String customFilter)
    {
        String ds = cNode.getDataSource();
        String showF = cNode.getShowFiled();
        String keyF = cNode.getNodeKey();
        IQuerySql querySql=this.querySqlService.getByAlias(ds.replace("V_", ""));
        StringBuffer sql = new StringBuffer("select DISTINCT (" + showF + ")as \"text\"");
        sql.append(", (" + keyF + ") as \"key\"");
        sql.append(" from (" + querySql.getSql()+")"+ds);
        sql.append(this.createWhereSql(cNode, pNodeId,customFilter));
        sql.append(" order by " + keyF + " asc");
        
        return sql;
    }
    
    /**
     * 构建 dbom meta node 节点 data 的select sql
     * 
     * @param cNode 节点 meta
     * @param pNodeId 节点的父节点 data key
     * @return
     */
    public StringBuffer createSelectSql(DBomNode cNode, String pNodeId,String customFilter)
    {
        String ds = cNode.getDataSource();
        String showF = cNode.getShowFiled();
        String keyF = cNode.getNodeKey();
        if (showF.indexOf(ITableModel.CUSTOMER_COLUMN_PREFIX) < 0&&!showF.equals(ds+"."+ITableModel.PK_COLUMN_NAME))
        {
            showF = showF.replace(ds + ".", ds + "." + ITableModel.CUSTOMER_COLUMN_PREFIX);
        }
        if (keyF.indexOf(ITableModel.CUSTOMER_COLUMN_PREFIX) < 0&&!keyF.equals(ds+"."+ITableModel.PK_COLUMN_NAME))
        {
            keyF = keyF.replace(ds + ".", ds + "." + ITableModel.CUSTOMER_COLUMN_PREFIX);
        }
        StringBuffer sql = new StringBuffer("select DISTINCT (" + showF + ")as \"text\"");
        sql.append(", (" + keyF + ") as \"key\"");
        sql.append(" from " + ds);
        sql.append(this.createWhereSql(cNode, pNodeId,customFilter));
        sql.append(" order by " + keyF + " asc");
        
        return sql;
    }
    
    /**
     * 根据业务数据模板中获取 filter 权限过滤sql
     * 
     * @param cNode
     * @return
     */
    public String getWhereSqlFormTemplate(DBomNode cNode)
    {
        List<DBomNode> pnodes = this.dBomNodeDao.getByCode(cNode.getPcode());
        if (pnodes == null || pnodes.size() != 1)
        {
            return "";
        }
        String url = pnodes.get(0).getUrl();
        String datasource = cNode.getDataSource();
        if (StringUtil.isNotEmpty(url))
        {
            String[] tmeplates = url.split(",");
            for (String template : tmeplates)
            {
                String displayId = dBomNodeService.getParamByUrl(template, "__displayId__");
                String filterkey = dBomNodeService.getParamByUrl(template, "__filterKey__");
                if (StringUtil.isNotEmpty(displayId))
                {
                    IDataTemplate dataTemplate = this.dataTemplateService.getById(Long.valueOf(displayId.replace("\n", "")));
                    IFormTable formTable=this.formTableService.getById(dataTemplate.getTableId());
                    String reltable = formTable.getFactTableName();
                    if (reltable.toLowerCase().equals(datasource.toLowerCase()))
                    {
                        String sql = dataTemplateService.getFilterSql(Long.valueOf(displayId), filterkey);
                        sql=sql.replace(formTable.getTableName(), reltable);
                        return sql;
                        
                    }
                }
            }
        }
        return "";
        
    }
    
    /**
     * 获取dbom meta node 的数据的where sql 按照目前设计 暂时不需要 循环递归出所有父节点 条件
     * 
     * @param cNode 节点 meta
     * @param nodeId 节点的父节点 data key
     * @return
     */
    public StringBuffer createWhereSql(DBomNode cNode, String pNodeId,String customFilter)
    {
        String pNodekeyF = cNode.getpNodeKey();
        String ds = cNode.getDataSource();
        StringBuffer sql = new StringBuffer();
        if (StringUtil.isNotEmpty(pNodekeyF))
        {
            if (pNodekeyF.indexOf(ITableModel.CUSTOMER_COLUMN_PREFIX) < 0&&ds.startsWith(ITableModel.CUSTOMER_TABLE_PREFIX))
            {
                pNodekeyF = pNodekeyF.replace(ds + ".", ds + "." + ITableModel.CUSTOMER_COLUMN_PREFIX);
            }
            String[] ids = pNodeId.split("\\.");
            sql.append(" where " + pNodekeyF + "=");
            sql.append("'" + ids[ids.length - 1] + "'");
        }else{
            sql.append(" where 1=1");
        }
        String templateFilter = this.getWhereSqlFormTemplate(cNode);
        if (StringUtil.isNotEmpty(templateFilter))
        {
            sql.append(" and " + templateFilter);
        }
        if(StringUtil.isNotEmpty(customFilter)){
            sql.append(" and " + customFilter);
        }
        return sql;
    }
    
    /**
     * 根据某个节点获取节点的 where sql
     * 
     * @param cNode
     * @param pNodeId
     * @return
     */
    public StringBuffer createCnodeWhereSql(DBomNode cNode, String cNodeId)
    {
        String nodekeyF = cNode.getNodeKey();
        String ds = cNode.getDataSource();
        StringBuffer sql = new StringBuffer();
        if (StringUtil.isNotEmpty(nodekeyF)&&ds.startsWith(ITableModel.CUSTOMER_TABLE_PREFIX))
        {
            nodekeyF = nodekeyF.replace(ds + ".", ds + "." + ITableModel.CUSTOMER_COLUMN_PREFIX);
            String[] ids = cNodeId.split("\\.");
            sql.append(" where " + nodekeyF + "=");
            sql.append("'" + ids[ids.length - 1] + "'");
        }else if(StringUtil.isNotEmpty(nodekeyF)&&ds.startsWith("V_")){
            
            String[] ids = cNodeId.split("\\.");
            nodekeyF=nodekeyF.replace(ds+".", "");
            sql.append(" where " + nodekeyF + "=");
            sql.append("'" + ids[ids.length - 1] + "'");
        }
        return sql;
    }
    
}
