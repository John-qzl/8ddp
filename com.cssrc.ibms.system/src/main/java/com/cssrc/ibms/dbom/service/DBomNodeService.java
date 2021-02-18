package com.cssrc.ibms.dbom.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.intf.IQueryFieldService;
import com.cssrc.ibms.api.form.intf.IQuerySqlService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IQueryField;
import com.cssrc.ibms.api.form.model.IQuerySql;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.core.util.common.PlaceHolderUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dbom.dao.DBomNodeDao;
import com.cssrc.ibms.dbom.model.DBomNode;

/**
 * @ClassName: DBomNodeService
 * @Description: dbom 树 解析
 * @author zxg
 * @date 2017年3月15日 上午11:39:59
 * 
 */
@Service
public class DBomNodeService
{
    protected Logger logger = LoggerFactory.getLogger(DBomNodeService.class);
    
    @Resource
    private DBomNodeDao dBomNodeDao;
    @Resource
    IQueryFieldService queryFieldService;
    @Resource
    IFormTableService formTableService;
    @Resource
    IFormFieldService formFieldService;
    @Resource
    IDataTemplateService dataTemplateService;
    @Resource
    IQuerySqlService querySqlService;
    @Resource
    DbomSqlFilterService dbomSqlFilterService;
    
    /**
     * DBom前台 目标数据源 动态加载 -动态加载Tab. tab 页个数按照 URl 个数来 控制
     * 
     * @param code
     * @return
     */
    public JSONObject getTab(String code)
    {
        JSONObject result = new JSONObject();
        String[] codes = code.split("\\.");
        List<DBomNode> dbomNodes = dBomNodeDao.getByCode(codes[codes.length - 1]);
        if (dbomNodes == null || dbomNodes.size() != 1)
        {
            logger.warn(code + ":找到多条数据");
        }
        else
        {
            DBomNode dnode = dbomNodes.get(0);
            String[] templates=dnode.getUrl().split(",");
            String[] tabs=new String[templates.length];
            int i=0;
            for(String url:templates){
                String title=this.getParamByUrl(url, "__title__");
                String displyId=this.getParamByUrl(url, "__displayId__");
                if(StringUtil.isEmpty(title)&&StringUtil.isNotEmpty(displyId)){
                    IDataTemplate dataTemplate = this.dataTemplateService.getById(Long.valueOf(displyId.replace("\n", "")));
                    title = this.formTableService.getById(dataTemplate.getTableId()).getTableDesc();
                    url+="&__title__="+title;
                }
                tabs[i]=url;
                i++;
            }
            result.put("tabs", tabs);
        }
        return result;
    }
    
    /**
     * 获取dbom tree 根节点 data 注意 节点数据 ID，parentId 的构造 默认需要获取第一层节点 的data
     * 
     * @param treeCode
     * @return
     */
    public List<Map<String, Object>> getDbomRootNodeData(String treeCode,String customFilter)
    {
        List<DBomNode> subNodes = this.dBomNodeDao.getByPCode(treeCode);
        List<Map<String, Object>> reuslt = new ArrayList<>();
        for (DBomNode sNode : subNodes)
        {
            
            if (StringUtil.isNotEmpty(sNode.getDataSource()) && StringUtil.isNotEmpty(sNode.getShowFiled()))
            {
                /*
                 * 一般情况下，dbom tree 的根节点都是静态数据， 如果需要扩展支持动态节点， 则添加业务数据获取相关代码
                 */
            }
            else
            {
                // dbom 数据根节点 以treeCode+"."+ code 为ID
                String curId = treeCode + "." + sNode.getCode();
                Map<String, Object> nData = new HashMap<>();
                nData.put("id", curId);
                nData.put("parentId", treeCode);// dbom 数据根节点 以 dbom code 为parentId
                nData.put("code", curId);// 根节点是静态节点 code 编号 treeCode+"."+ code 为code
                nData.put("text", sNode.getName());// dbom 数据根节点 以name 为text
                nData.put("key", sNode.getNodeKey());
                nData.put("isParent", true);
                nData.put("tempUrl", sNode.getUrl());
                nData.put("children", this.getDbomSubNodeData(sNode.getCode(), curId,customFilter));
                reuslt.add(nData);
            }
            
        }
        return reuslt;
    }
    
    /**
     * 根据所有当前节点获取子节点数据 因为是获取当前节点的子节点数据，所以 curId 为子节点的父ID， 注意 节点数据 ID，parentId ,code 的构造 id，code 值是具有层级关系的，比如
     * parentcode.code1.code2
     * 
     * @param treeCode CWM_DBOM code
     * @param curcode CWM_DBOM_NODE 当前节点code
     * @param curId jsP当前节点id
     * @return
     */
    public List<Map<String, Object>> getDbomSubNodeData(String curcode, String curId,String customFilter)
    {
        String[] curcodes = curcode.split("\\.", -1);
        List<DBomNode> subNodes = this.dBomNodeDao.getByPCode(curcodes[curcodes.length - 1]);
        List<Map<String, Object>> reuslt = new ArrayList<>();
        for (DBomNode sNode : subNodes)
        {
            boolean isparent = dBomNodeDao.getByPCode(sNode.getCode()).size() > 0;
            if (StringUtil.isNotEmpty(sNode.getDataSource()) && StringUtil.isNotEmpty(sNode.getShowFiled()))
            {
                // 如果设置了节点数据源以及节点显示 filed 则判定为动态节点
                StringBuffer sql = dbomSqlFilterService.createSelect(sNode, curId,customFilter);
                List<Map<String, Object>> nDatas = this.dBomNodeDao.queryForList(sql.toString());
                for (Map<String, Object> n : nDatas)
                {
                    Map<String, Object> nData = new HashMap<>();
                    nData.put("id", curId + "." + n.get("key"));// 动态态节点ID 按规则 为父节点的ID+"."+n.get("key")
                    nData.put("parentId", curId);
                    nData.put("code", curcode + "." + sNode.getCode());
                    nData.put("key", sNode.getNodeKey());
                    nData.put("text", n.get("text"));
                    nData.put("isParent", isparent);
                    nData.put("tempUrl", sNode.getUrl());
                    nData.put("children", new ArrayList<>());
                    reuslt.add(nData);
                }
            }
            else
            {
                // 没有设置节点数据源 以及 显示 filed 都将认为是静态节点
                Map<String, Object> nData = new HashMap<>();
                nData.put("id", curId + "." + sNode.getCode());// 静态节点ID 按规则 为父节点的ID+"."+code
                nData.put("parentId", curId);
                nData.put("code", curcode + "." + sNode.getCode());
                nData.put("key", sNode.getNodeKey());
                nData.put("text", sNode.getName());
                nData.put("isParent", isparent);
                nData.put("tempUrl", sNode.getUrl());
                nData.put("children", new ArrayList<>());
                reuslt.add(nData);
            }
            
        }
        return reuslt;
        
    }
    
    /**
     * 获取当前节点所有父节点 相关的过滤条件
     * 
     * @param request
     * @param code
     * @param value
     * @param tableName
     * @return
     */
    public Map<String, String> getURLParams(String curCode, String curId, String urlIndex)
    {
        try
        {
            Map<String, String> result = new HashMap<>();
            String[] codes = curCode.split("\\.");
            curCode = codes[codes.length - 1];
            List<DBomNode> dbomNodes = this.dBomNodeDao.getByCode(curCode);
            if (dbomNodes == null || dbomNodes.size() != 1)
            {
                logger.warn(curCode + ":找到多条数据");
                return null;
            }
            else
            {
                DBomNode node = dbomNodes.get(0);
                String tDsRelation = node.getTargetDataRelation();
                String tDs = node.getTargetDataSource();
                String[] tDsRelations = null;
                String[] tDses = null;
                // click url
                String url = node.getUrl().split(",")[Integer.parseInt(urlIndex)];

                if (StringUtil.isNotEmpty(tDsRelation))
                {
                    tDsRelations = tDsRelation.split(",", -1);
                }
                if (StringUtil.isNotEmpty(tDs))
                {
                    tDses = tDs.split(",", -1);
                }
                try
                {
                    /*
                     * 优化下不按照index 取，根据 __displayId__ 截取 datatemplate ID 然后获取tableid
                     */
                    // 关联表
                    String reltable = tDses[Integer.parseInt(urlIndex)];
                    // 关联关系
                    String relation = tDsRelations[Integer.parseInt(urlIndex)];
                    // 截取参数 __displayId__
                    String displyId = this.getParamByUrl(url, "__displayId__");
                    // 获取业务数据模板
                    IDataTemplate dataTemplate = this.dataTemplateService.getById(Long.valueOf(displyId.replace("\n", "")));
                    reltable = this.formTableService.getById(dataTemplate.getTableId()).getFactTableName();
                    for (String rel : tDsRelations)
                    {
                        // 如果关联关系 中包含了目标数据源table name 则找到对应的关联关系
                        if (rel.indexOf(reltable) >= 0)
                        {
                            relation = rel;
                            break;
                        }
                    }
                    
                    try
                    {
                        // 节点 key 对应的value
                        String[] curIds = curId.split("\\.");
                        String nodeValue = curIds[curIds.length - 1];
                        String filterSql =
                            dbomSqlFilterService.getFilterSqlByRelation(node, reltable, relation, nodeValue);
                        url=this.getCustomParamByUrl(node, curId, url);
                        url+="&__dbomSql__="+URLEncoder.encode(filterSql, "UTF-8");
                        url+="&__dbomFKValue__="+URLEncoder.encode(nodeValue, "UTF-8");
                        result.put("url", url);
                        return result;
                    }
                    catch (Exception e)
                    {
                        logger.error(e.getMessage());
                        return null;
                    }
                }
                catch (Throwable e)
                {
                    /*
                     * 如果走了catch 说明 没有配置 对应 index 目标数据源以及关联关系 则默认根据节点的key 获取目标数据源过滤条件
                     */
                    String filterSql = dbomSqlFilterService.getFilterSqlByNode(node, curId);
                    // 节点 key 对应的value
                    String[] curIds = curId.split("\\.");
                    String nodeValue = curIds[curIds.length - 1];
                    
                    url=this.getCustomParamByUrl(node, curId, url);
                    url+="&__dbomSql__="+URLEncoder.encode(filterSql, "UTF-8");
                    url+="&__dbomFKValue__="+URLEncoder.encode(nodeValue, "UTF-8");
                    result.put("url", url);
                    
                    return result;
                    
                }
                
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 从url 中截取参数
     * 
     * @param url
     * @param name
     * @return
     */
    public String getParamByUrl(String url, String name)
    {
        String[] params = url.split("\\?",2)[1].split("&");
        for (String param : params)
        {
            if (param.indexOf(name) >= 0)
            {
                String val = param.split("\\=")[1];
                return val;
            }
        }
        return null;
    }
    
    
    
    public  String getCustomParamByUrl(DBomNode node,String curKeyValue,String url){
        String table=node.getDataSource();
        if(table.startsWith(ITableModel.CUSTOMER_TABLE_PREFIX)){
            return getParamByTable(node, curKeyValue, url, table);
        }else if(table.startsWith("V_")){
            table=table.replace("V_", "");
            IQuerySql querySql=this.querySqlService.getByAlias(table);
            if(querySql!=null){
                List<?extends IQueryField> fileds=queryFieldService.getListBySqlId(querySql.getId());
                StringBuffer sql=new StringBuffer("select ");
                int i=0;
                for(IQueryField f:fileds){
                    if(i>0){
                        sql.append(","+f.getName()+" as \""+f.getName()+"\"");
                    }else{
                        sql.append(f.getName()+" as \""+f.getName()+"\"");
                    }
                    i++;
                }
                sql.append(" from ("+querySql.getSql()+")"+node.getDataSource());
                String[] curIds = curKeyValue.split("\\.");
                curKeyValue = curIds[curIds.length - 1];
                String nodeKey=node.getNodeKey();
                sql.append(" where " +nodeKey+"= '"+curKeyValue+"'");
                List<Map<String,Object>> map=this.dBomNodeDao.queryForList(sql.toString());
                if(map!=null&&map.size()==1){
                    return PlaceHolderUtils.resolvePlaceholders(url, map.get(0));
                }else{
                    logger.warn("存在 多条数据，无法匹配自定义参数");
                    return url;
                }
                
            }else{
                
                return url;
            }
        }
        return url;
    }

    private String getParamByTable(DBomNode node, String curKeyValue, String url, String table)
    {
        table=table.replace(ITableModel.CUSTOMER_TABLE_PREFIX, "");
        IFormTable formtable=formTableService.getByAliasTableName(null,table);
        if(formtable!=null){
            List<?extends IFormField> fileds=this.formFieldService.getFieldsByTableId(formtable.getTableId());
            StringBuffer sql=new StringBuffer("select ID as \"ID\"");
            
            for(IFormField f:fileds){
                sql.append(","+f.getFactFiledName()+" as \""+f.getFieldName()+"\"");
            }
            String[] curIds = curKeyValue.split("\\.");
            curKeyValue = curIds[curIds.length - 1];
            sql.append(" from " +formtable.getFactTableName());
            String nodeKey=node.getNodeKey();
            if (nodeKey.indexOf(ITableModel.CUSTOMER_COLUMN_PREFIX) < 0&&!nodeKey.equals(formtable.getFactTableName()+"."+ITableModel.PK_COLUMN_NAME))
            {
            	nodeKey=nodeKey.replace(formtable.getFactTableName()+".", formtable.getFactTableName()+"."+ITableModel.CUSTOMER_COLUMN_PREFIX);
            }
            sql.append(" where " +nodeKey+"= '"+curKeyValue+"'");
            List<Map<String,Object>> map=this.dBomNodeDao.queryForList(sql.toString());
            if(map!=null&&map.size()==1){
                return PlaceHolderUtils.resolvePlaceholders(url, map.get(0));
            }else if(map!=null&&map.size()>1){
                return PlaceHolderUtils.resolvePlaceholders(url, map.get(0));
            }else{
            	logger.warn("数据不对，无法匹配自定义参数");
            	return url;
            }
            
        }else{
            
            return url;
        }
    }
}
