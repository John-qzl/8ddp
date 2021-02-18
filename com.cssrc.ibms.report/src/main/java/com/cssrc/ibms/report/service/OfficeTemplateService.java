package com.cssrc.ibms.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.report.dao.OfficeItemDao;
import com.cssrc.ibms.report.dao.OfficeTemplateDao;
import com.cssrc.ibms.report.model.OfficeItem;
import com.cssrc.ibms.report.model.OfficeTemplate;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.report.inf.IOfficeExtService;
import com.cssrc.ibms.api.report.inf.IOfficeTemplateService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.zhuozhengsoft.pageoffice.wordwriter.DataRegion;
import com.zhuozhengsoft.pageoffice.wordwriter.WdAutoFitBehavior;
import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;

@Service("officeTemplateService")
public class OfficeTemplateService extends BaseService<OfficeTemplate> implements IOfficeTemplateService
{
    private static Logger logger = Logger.getLogger("IBMS.REPORT");
    
    @Resource
    private OfficeTemplateDao officeTemplateDao;
    
    @Resource
    private OfficeItemDao officeItemDao;
    
    @Resource
    IFormTableService formTableService;
    
    @Resource
    IFormFieldService formFieldService;
    
    @Override
    protected IEntityDao<OfficeTemplate, Long> getEntityDao()
    {
        return officeTemplateDao;
    }
    
    /**
     * 根据officeid 获取所有书签
     * 
     * @param officeId
     * @return
     */
    public List<OfficeItem> getItemByOfficeId(Long officeId)
    {
        return officeItemDao.getItemByOfficeId(officeId);
    }
    
    /**
     * 保存office
     * 
     * @param officeTemplate
     * @param bookmarks
     * @param allmarks
     * @return
     */
    public ResultMessage saveOfficeTemplate(OfficeTemplate officeTemplate)
    {
        try
        {
            if (StringUtil.isEmpty(officeTemplate.getOfficeid()))
            {
                officeTemplate.setOfficeid(String.valueOf(UniqueIdUtil.genId()));
                officeTemplate.setCreatetime(new Date());
                officeTemplate.setCreateUser(UserContextUtil.getCurrentUser().getUsername());
                officeTemplate.setTemplate_updateTime(new Date());
                officeTemplate.setTemplate_updateId(UserContextUtil.getCurrentUserId());
                officeTemplateDao.add(officeTemplate);
                return new ResultMessage(ResultMessage.Success, "新增office报表模板成功");
            }
            else
            {
                officeTemplate.setTemplate_updateTime(new Date());
                officeTemplate.setTemplate_updateId(UserContextUtil.getCurrentUserId());
                officeTemplateDao.update(officeTemplate);
                return new ResultMessage(ResultMessage.Success, "更新office报表模板成功");
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 保存office以及书签类容
     * 
     * @param officeTemplate
     * @param bookmarks
     * @param allmarks
     * @return
     */
    public ResultMessage saveOfficeTemplate(OfficeTemplate officeTemplate, String[] bookmarks, String allmarks)
    {
        try
        {
            if (StringUtil.isEmpty(officeTemplate.getOfficeid()))
            {
                officeTemplate.setOfficeid(String.valueOf(UniqueIdUtil.genId()));
                officeTemplate.setCreateTime(new Date());
                officeTemplate.setCreateUser(UserContextUtil.getCurrentUser().getUsername());
                officeTemplate.setTemplate_updateTime(new Date());
                officeTemplate.setTemplate_updateId(UserContextUtil.getCurrentUserId());
                officeTemplateDao.add(officeTemplate);
                saveOfficeItem(officeTemplate, bookmarks, allmarks);
                return new ResultMessage(ResultMessage.Success, "新增office报表模板成功");
            }
            else
            {
                officeTemplate.setTemplate_updateTime(new Date());
                officeTemplate.setTemplate_updateId(UserContextUtil.getCurrentUserId());
                officeTemplateDao.update(officeTemplate);
                saveOfficeItem(officeTemplate, bookmarks, allmarks);
                return new ResultMessage(ResultMessage.Success, "更新office报表模板成功");
                
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 保存书签，保存之前删除office关联的所有书签
     * 
     * @param officeTemplate
     * @param bookmarks 已有的书签，
     * @param allmarks
     * @return
     */
    public ResultMessage saveOfficeItem(OfficeTemplate officeTemplate, String[] bookmarks, String allmarks)
    {
        
        List<OfficeItem> items = officeItemDao.getItemByOfficeId(Long.valueOf(officeTemplate.getOfficeid()));
        String[] markIds=new String[0];
        if(allmarks!=null){
        	markIds=allmarks.split(",");
        }
        if (bookmarks == null)
        {
            return new ResultMessage(ResultMessage.Success, "新增office报表模板成功");
        }
        
        for (String markId : markIds)
        {
            OfficeItem item = this.getMarkItem(bookmarks, markId);
            item.setOfficeId(officeTemplate.getOfficeid());
            if (containsBookMark(items, markId))
            {
                // 如果数据库中已经存在 书签ID则为更新
                officeItemDao.update(item);
            }
            else
            {
                // 不存在则新增
                officeItemDao.add(item);
            }
        }
        // 最后需要删除以上删除的书签
        for (OfficeItem item : items)
        {
            // 删除 allmarks 中不存在的书签。逻辑删除
            item.setItems_updateId(UserContextUtil.getCurrentUserId());
            item.setItems_updateTime(new Date());
            officeItemDao.updateDelete(item);
        }
        
        
        return new ResultMessage(ResultMessage.Success, "新增office报表模板成功");
    }
    
    
    /**
     * 通过文档中的书签ID获取form表单中的书签信息，表单书签中不存在则表示是word 自带的书签
     * @param bookmarks
     * @param itemId
     * @return
     */
    public OfficeItem getMarkItem(String[] bookmarks,String itemId){
        for (String mark : bookmarks){
            JSONObject mk = JSONObject.parseObject(mark);
            String booMarkId = mk.getString("booMarkId");
            if(booMarkId.equals(itemId)){
                //如果 bookmarks中存在则表示是系统添加的书签
                String tableName = mk.getString("tableName");
                String columnName = mk.getString("columnName");
                String columnId = mk.getString("columnId");
                String type = mk.getString("type");
                
                OfficeItem item = new OfficeItem();
                item.setId(booMarkId);
                item.setColumnId(columnId);
                item.setColumnName(columnName);
                item.setType(type);
                // item.setTableId(tableId);
                item.setTableName(tableName);
                item.setItems_createTime(new Date());
                item.setItems_updateTime(new Date());
                item.setItems_creatorId(UserContextUtil.getCurrentUserId());
                item.setItems_updateId(UserContextUtil.getCurrentUserId());
                
                return item;
            }else{
                //表示是模板一开始就存在的书签。改类书签表示，书签所在的区域可以编辑
            }
        }
        //表示是模板一开始就存在的书签。改类书签表示，书签所在的区域可以编辑
        OfficeItem item = new OfficeItem();
        item.setId(itemId);
        item.setItems_createTime(new Date());
        item.setItems_updateTime(new Date());
        item.setItems_creatorId(UserContextUtil.getCurrentUserId());
        item.setItems_updateId(UserContextUtil.getCurrentUserId());
        item.setType("stat");// 如果是空的则表示是doc 模板自带的书签，这类书签都都需要开启编辑
        return item;
    }
    
    public boolean containsBookMark(List<OfficeItem> items, String booMarkId)
    {
        for (OfficeItem item : items)
        {
            if (booMarkId.equals(item.getId()))
            {
                // 如果已经找到存在的书签，则移除，剩下的集合中都是需要删除的书签
                items.remove(item);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取所有表,只读取id,name,desc等重要数据
     * 
     * @param params
     * @return
     */
    public List<?> getTabs(Map<String, String> params)
    {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        List<Map<String, String>> tabs = (List<Map<String, String>>)formTableService.getTabs(params);
        Map<String, String> tab = new HashMap<String, String>();
        tab.put("ID", "table");
        tab.put("PID", "schema");
        tab.put("NAME_", "table");
        tab.put("DESC_", "数据类");
        tab.put("nocheck", "true");
        
        data.add(tab);
        
        Map<String, String> view = new HashMap<String, String>();
        view.put("ID", "view");
        view.put("PID", "schema");
        view.put("NAME_", "view");
        view.put("DESC_", "视图类");
        view.put("nocheck", "true");
        data.add(view);
        
        Map<String, String> schema = new HashMap<String, String>();
        schema.put("ID", "schema");
        schema.put("PID", "-1");
        schema.put("NAME_", "schema");
        schema.put("DESC_", "XX综合管理系统");
        schema.put("nocheck", "true");
        
        data.add(schema);
        
        // 装在所有实体类
        data.addAll(tabs);
        return data;
    }
    
    /**
     * 获取所有表 的字段
     * 
     * @param params
     * @return
     */
    public List<Map<String, String>> getColumnsByTabIds(String[] tabids)
    {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (String tabid : tabids)
        {
            Map<String, String> tab = formTableService.getTableMapById(Long.valueOf(tabid));
            list.add(tab);
            list.addAll(formTableService.getFiledsByTabId(Long.valueOf(tabid)));
        }
        Map<String, String> schema = new HashMap<String, String>();
        schema.put("ID", "schema");
        schema.put("PID", "-1");
        schema.put("NAME_", "schema");
        schema.put("DESC_", "XX综合管理系统");
        schema.put("nocheck", "true");
        list.add(schema);
        return list;
    }
    
    /**
     * 获取tab列表
     * 
     * @param ids id数组
     * @return
     */
    public List<?> getTablesByIds(String[] ids)
    {
        return formTableService.getByTabIds(ids);
    }
    
    /**
     * @param officeTemplate doc 模板
     * @param wordWriter 输出对象
     * @param dataId 主数据PK
     * @param saveType 判断是生成文本还是更新文本内容
     * @param dataResult 循环书签的数据集合
     * @return
     */
    public ResultMessage extOfficeReport(OfficeTemplate officeTemplate, WordDocument wordWriter, String dataId,
        String saveType, List<Map<String, Object>> dataResult)
    {
        IOfficeExtService extService=null;
        try {
            extService=AppUtil.getBean(IOfficeExtService.class);
        }catch (Exception e) {
            logger.warn(e);
        }
        if (officeTemplate == null)
        {
            return new ResultMessage(ResultMessage.Fail, "没有找到office模板文件");
        }
        List<OfficeItem> marks = officeItemDao.getItemByOfficeId(Long.valueOf(officeTemplate.getOfficeid()));
        // 数据源数据入口主表
        IFormTable mtab = formTableService.getByTableName(officeTemplate.getDataEntry());
        // 根据 模板数据源 获取form_table
        // List<?> tabs=formTableService.getByTabIds(table_id.split(","));
        // 根据 dataId 获取 主表入口数据
        StringBuffer sql =
            new StringBuffer("select * from ").append(getTableName(mtab)).append(" where ID=").append(dataId);
        Map<String, Object> mainData = officeTemplateDao.queryForMap(sql.toString());
        try
        {
            int j = 0;
            int max = 0;
            for (OfficeItem mark : marks)
            {
                if("stat".equals(mark.getType())){
                    //该类书签表示 word 文档中自带的书签，是可以编辑的书签
                    DataRegion dm = wordWriter.openDataRegion(mark.getId());
                    dm.setEditing(true);
                    continue;
                }
                // 获取标签数据所属表
                IFormTable formTable = formTableService.getByTableName(mark.getTableName());
                // 获取表真实field 名
                List<? extends IFormField> feilds =
                    formTableService.getFileds(formTable.getTableId(), mark.getColumnName().split(","));
                if ("col".equals(mark.getType()))
                {
                    // 普通单列书签
                    if (formTable.getTableId().equals(mtab.getTableId()))
                    {
                        // 主表数据
                        wordWriter.openDataRegion(mark.getId()).setValue(getDataByFiledName(mainData, feilds.get(0)));
                    }
                    else if (formTable.getMainTableId().equals(mtab.getTableId()))
                    {
                        // 子表数据--字表数据有可能是多条，单列书签内容不能匹配。
                        StringBuffer subSql =
                            new StringBuffer("select ").append(getFiledName(feilds))
                                .append(" from ")
                                .append(getTableName(formTable))
                                .append(" where " + ITableModel.FK_COLUMN_NAME + "='" + dataId + "'");
                        List<Map<String, Object>> subData = officeTemplateDao.queryForList(subSql.toString());// 获取子表数据
                        if (subData.size() >= 1)
                        {
                            wordWriter.openDataRegion(mark.getId()).setValue(getDataByFiledName(subData.get(0),
                                feilds.get(0)));
                        }
                        else
                        {
                            return new ResultMessage(ResultMessage.Fail, "[" + mark.getTableName() + "."
                                + mark.getColumnName() + "]字表数据不允许设置成单列标签");
                        }
                    }
                    else
                    {
                        // 关系表--只能匹配一对一的关系表类型，其他对应关可能返回多条数据。
                        StringBuffer refSql =
                            new StringBuffer("select ").append(getFiledName(feilds))
                                .append(" from ")
                                .append(getTableName(formTable))
                                .append(" where " + this.getRefFieldName(mtab.getTableId(), formTable.getTableId()))
                                .append("='" + dataId + "'");// 获取关系表的数据
                        
                        List<Map<String, Object>> refData = officeTemplateDao.queryForList(refSql.toString());// 获取关系表的数据
                        if (refData.size() >= 1)
                        {
                            wordWriter.openDataRegion(mark.getId()).setValue(getDataByFiledName(refData.get(0),
                                feilds.get(0)));
                        }
                        else
                        {
                            return new ResultMessage(ResultMessage.Fail, "[" + mark.getTableName() + "."
                                + mark.getColumnName() + "]字表数据不允许设置成单列标签");
                        }
                    }
                }
                else if ("tab".equals(mark.getType()))
                {
                    // 表格型书签
                    StringBuffer tabSql = null;
                    if (formTable.getIsMain() == 1)
                    {
                        // 关系表
                        String relF = this.getRefFieldName(mtab.getTableId(), formTable.getTableId());
                        if (!StringUtil.isEmpty(relF))
                        {
                            tabSql =
                                new StringBuffer("select ").append(getFiledName(feilds))
                                    .append(" from ")
                                    .append(getTableName(formTable))
                                    .append(" where " + relF)
                                    .append("='" + dataId + "'");// 获取关系表的数据
                        }
                    }
                    else if (formTable.getMainTableId() != null)
                    {
                        // 子表
                        tabSql =
                            new StringBuffer("select ").append(getFiledName(feilds))
                                .append(" from ")
                                .append(getTableName(formTable))
                                .append(" where " + ITableModel.FK_COLUMN_NAME + "='")
                                .append(dataId)
                                .append("'");
                    }
                    else
                    {
                        // 自定义关系
                    }
                    if(tabSql==null||tabSql.length()<=0){
                        continue;
                    }
                    List<Map<String, Object>> tabData = officeTemplateDao.queryForList(tabSql.toString());// 获取关系表的数据
                    // 定义word表格
                    com.zhuozhengsoft.pageoffice.wordwriter.Table table =
                        wordWriter.openDataRegion(mark.getId()).createTable(tabData.size() + 1,
                            feilds.size(),
                            WdAutoFitBehavior.wdAutoFitWindow);
                    // 定义表头
                    int row = 1;
                    for (int col = 0; col < feilds.size(); col++)
                    {
                        table.openCellRC(row, col + 1).setValue(feilds.get(col).getFieldDesc());
                    }
                    // 插入表格数据
                    for (Map<String, Object> data : tabData)
                    {
                        row++;
                        int col = 1;
                        for (IFormField filed : feilds)
                        {
                            table.openCellRC(row, col).setValue(getDataByFiledName(data, filed)); // 插入数据
                            col++;
                        }
                        
                    }
                }
                else if ("cell".equals(mark.getType()))
                {
                    
                    // 循环型书签
                    StringBuffer cellSql = null;
                    if (formTable.getIsMain() == 1)
                    {
                        // 关系表
                        String relF = this.getRefFieldName(mtab.getTableId(), formTable.getTableId());
                        if (!StringUtil.isEmpty(relF))
                        {
                            cellSql =
                                new StringBuffer("select ").append(getFiledName(feilds))
                                    .append(" from ")
                                    .append(getTableName(formTable))
                                    .append(" where " + relF)
                                    .append("='" + dataId + "'");// 获取关系表的数据
                        }
                        
                        
                    }
                    else if (formTable.getMainTableId() != null)
                    {
                        // 子表
                        cellSql =
                            new StringBuffer("select ").append(getFiledName(feilds))
                                .append(" from ")
                                .append(getTableName(formTable))
                                .append(" where " + ITableModel.FK_COLUMN_NAME + "='")
                                .append(dataId)
                                .append("'");
                    }
                    else
                    {
                        // 自定义关系
                        cellSql =
                            new StringBuffer("select ").append(getFiledName(feilds))
                                .append(" from ")
                                .append(getTableName(formTable));
                    }
                    if(extService!=null) {
                        StringBuffer extSql=extService.getQuerySql(formTable,feilds,mark);
                        if(extSql!=null&&extSql.length()>0) {
                            cellSql=extSql;
                        }
                    }
                    if(cellSql!=null&&cellSql.length()>0){
                        List<Map<String, Object>> tabData = officeTemplateDao.queryForList(cellSql.toString());// 获取关系表的数据
                        String fname="";
                        if(tabData!=null&&tabData.size()>0){
                            fname=(tabData.get(0).keySet().toArray()[0]).toString();
                        }
                        if(extService!=null) {
                            max = extService.extWordWriter(wordWriter, max, mark, tabData);
                        }
                        Map<String, Object> result = new HashMap<String, Object>();
                        result.put("mid", mark.getId());
                        result.put("mdata", tabData);
                        result.put("mfiled", fname);
                        dataResult.add(result);
                    }
                }
                else
                {
                    // 扩展接口
                }
            }
            return new ResultMessage(ResultMessage.Success, "数据转换成功");
        }
        catch (Exception e)
        {
            logger.error(e);
            return new ResultMessage(ResultMessage.Fail, "数据转换出错");
        }
        finally
        {
            
        }
    }
    
    /**
     * 根据 forma_field 存储的字段信息读取表真实字段
     */
    public String getFiledName(List<? extends IFormField> fields)
    {
        StringBuffer fieldsStr = new StringBuffer("");
        for (IFormField field : fields)
        {
            if (IFormField.DATATYPE_DATE.equals(field.getFieldType())) {
                String format=field.getDatefmt();
                if(SysContextUtil.getJdbcType().equals(ISysDataSource.DBTYPE_MYSQL)){
                    format=format.replace("yyyy", "%Y");
                    format=format.replace("MM", "%m");
                    format=format.replace("dd", "%d");
                    format=format.replace("HH", "%h");
                    format=format.replace("mm", "%i");
                    format=format.replace("ss", "%s");
                    String filed=ITableModel.CUSTOMER_COLUMN_PREFIX + field.getFieldName();
                    fieldsStr.append("date_format("+filed+",'"+format+"')as \""+filed+"\" ").append(",");
                }else if(SysContextUtil.getJdbcType().equals(ISysDataSource.DBTYPE_ORACLE)){
                    format=format.replace("HH", "HH24");
                    format=format.replace("mm", "mi");
                    String filed=ITableModel.CUSTOMER_COLUMN_PREFIX + field.getFieldName();
                    fieldsStr.append("date_format("+filed+",'"+format+"')as \""+filed+"\" ").append(",");
                }
                
            } else{
                fieldsStr.append(ITableModel.CUSTOMER_COLUMN_PREFIX + field.getFieldName()).append(",");
            }
            
        }
        if (fieldsStr.length() > 0)
        {
            return fieldsStr.substring(0, fieldsStr.length() - 1);
        }
        else
        {
            return "";
        }
    }
    
    public String getDataByFiledName(Map<String, Object> data, IFormField field)
    {
        String pre = ITableModel.CUSTOMER_COLUMN_PREFIX;
        Object val=data.get(pre + field.getFieldName());
        if(val!=null){
            return val.toString();
        }else{
            return "";
        }
    }
    
    /**
     * 根据 FormTable 存储的字段信息读取表真实表名
     */
    public String getTableName(IFormTable formTable)
    {
        return ITableModel.CUSTOMER_TABLE_PREFIX + formTable.getTableName();
    }
    
    /**
     * 根据 主表ID 关系表ID获取关系表外键字段名
     */
    public String getRefFieldName(Long mtabId, Long refTbaId)
    {
        
        List<? extends IFormField> formFields = formTableService.getRelFiledByTableId(mtabId, refTbaId);
        if (formFields != null && formFields.size() == 1)
        {
            return ITableModel.CUSTOMER_COLUMN_PREFIX + formFields.get(0).getFieldName();
        }
        else
        {
            for (IFormField f : formFields)
            {
                if (f.getRelTableType().intValue() == 2)
                {
                    return ITableModel.CUSTOMER_COLUMN_PREFIX + f.getFieldName();
                }
            }
            return "";
        }
    }
    
    /**
     * 根据模板标题取得报告模板。
     * 
     * @param tableId
     */
    public OfficeTemplate getByTitle(String title)
    {
        return officeTemplateDao.getUnique("getByTitle", title);
    }
    
    public ResultMessage delOfficeById(Long[] lAryId)
    {
        try
        {
            for (Long officeId : lAryId)
            {
                // 逻辑删除office模板
                OfficeTemplate office = new OfficeTemplate();
                office.setTemplate_updateId(UserContextUtil.getCurrentUserId());
                office.setTemplate_updateTime(new Date());
                office.setOfficeid(officeId.toString());
                officeTemplateDao.updateDelete(office);
                // 逻辑删除office所有相关书签
                OfficeItem item = new OfficeItem();
                item.setOfficeId(officeId.toString());
                item.setItems_updateId(UserContextUtil.getCurrentUserId());
                item.setUpdatetime(new Date());
                officeItemDao.updateDeleteByOfficeId(item);
            }
            return new ResultMessage(ResultMessage.Success, "删除office模板成功");
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 根据table name 获取 表
     * 
     * @param names
     * @return
     */
    public List<?> getTablesByNames(String[] names)
    {
        return formTableService.getByTabNames(names);
    }
    
    /**
     * 根据所有表名 获取所有表 的字段
     * 
     * @param params
     * @return
     */
    public List<Map<String, String>> getColumnsByTabNames(String[] tabNames)
    {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (String tabName : tabNames)
        {
            Map<String, String> tab = formTableService.getTableMapByName(tabName);
            if (tab == null)
            {
                continue;
            }
            list.add(tab);
            String tabId = String.valueOf(tab.get("ID"));
            list.addAll(formTableService.getFiledsByTabId(Long.valueOf(tabId)));
        }
        Map<String, String> schema = new HashMap<String, String>();
        schema.put("ID", "schema");
        schema.put("PID", "-1");
        schema.put("NAME_", "schema");
        schema.put("DESC_", "XX综合管理系统");
        schema.put("nocheck", "true");
        list.add(schema);
        return list;
    }
    
}
