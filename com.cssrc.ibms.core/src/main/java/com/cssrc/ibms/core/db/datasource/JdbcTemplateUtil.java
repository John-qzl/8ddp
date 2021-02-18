package com.cssrc.ibms.core.db.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.dialect.Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.DialectUtil;
import com.cssrc.ibms.core.db.mybatis.page.PageList;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 
 * <p>Title:JdbcTemplateUtil</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:29:26
 */
public class JdbcTemplateUtil
{
    private static Log logger = LogFactory.getLog(JdbcTemplateUtil.class);
    private static JdbcTemplate jdbcTemplate=new JdbcTemplate();
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(jdbcTemplate);

    
    /** 
    * @Title: update 
    * @Description: TODO(更新数据) 
    * @param @param dsName
    * @param @param sql
    * @param @param param
    * @param @throws Exception     
    * @return void    返回类型 
    * @throws 
    */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int update(String dsName, String sql, Map param)
        throws Exception
    {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(dsName);
        return namedParameterJdbcTemplate.update(sql, param);
    }
    
    @SuppressWarnings({"rawtypes"})
    public static int insert(String dsName, String sql, Map param)
        throws Exception
    {
        return update(dsName, sql, param);
    }
    
    @SuppressWarnings({"rawtypes"})
    public static int delete(String dsName, String sql, Map param)
        throws Exception
    {
        return update(dsName, sql, param);
    }
    
    @SuppressWarnings({"rawtypes"})
    public static int execute(String dsName, String sql, Map param)
        throws Exception
    {
        return update(dsName, sql, param);
    }
    
    @SuppressWarnings({"rawtypes"})
    public static Map<String, Object> queryForMap(String dsName, String sql, Map param)
        throws Exception
    {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(dsName);
        try
        {
            return namedParameterJdbcTemplate.queryForMap(sql, param);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    @SuppressWarnings({"rawtypes"})
    public static List<Map<String, Object>> queryForList(String dsName, String sql, Map param)
        throws Exception
    {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(dsName);
        return namedParameterJdbcTemplate.queryForList(sql, param);
    }
    
    @SuppressWarnings({"rawtypes"})
    public static int queryForInt(String dsName, String sql, Map param)
        throws Exception
    {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(dsName);
        return namedParameterJdbcTemplate.queryForInt(sql, param);
    }
    
    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        JdbcTemplateUtil.jdbcTemplate.setDataSource(jdbcTemplate.getDataSource());
        return JdbcTemplateUtil.namedParameterJdbcTemplate;
    }
    
    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(String alias)
    {
        try
        {
            JdbcTemplateUtil.jdbcTemplate.setDataSource(DataSourceUtil.getDataSourceByAlias(alias));
            return JdbcTemplateUtil.namedParameterJdbcTemplate;
        }
        catch (Exception e)
        {
           return null;
        }
    }
    
    public static PageList getPage(String alias, PagingBean pageBean, String sql, RowMapper rowMap)
    {
        int pageSize = pageBean.getPageSize();
        int offset = pageBean.getFirst();
        
        Map map = new HashMap();
        
        Dialect dialect = null;
        try
        {
            dialect = DialectUtil.getDialectByDataSourceAlias(alias);
        }
        catch (Exception e)
        {
            return null;
        }
        String pageSql = dialect.getLimitString(sql, offset, pageSize);
        String totalSql = dialect.getCountSql(sql);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
        namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(alias);
        List list = namedParameterJdbcTemplate.query(pageSql, map, rowMap);
        int total = namedParameterJdbcTemplate.queryForInt(totalSql, map);
        
        pageBean.setTotalCount(total);
        PageList pageList = new PageList();
        pageList.setPageBean(pageBean);
        pageList.addAll(list);
        
        return pageList;
    }
    
    public static <T> T getPage(String alias, String sql, ResultSetExtractor<T> rse, PagingBean pageBean,
        Map<String, Object> params)
    {
        T result = null;
        NamedParameterJdbcTemplate template = null;
        template = getNamedParameterJdbcTemplate(alias);
        if (pageBean != null)
        {
            int pageSize = pageBean.getPageSize();
            int offset = pageBean.getFirst();
            Dialect dialect = null;
            try
            {
                dialect = DialectUtil.getDialectByDataSourceAlias(alias);
            }
            catch (Exception e)
            {
                return null;
            }
            
            String pageSql = dialect.getLimitString(sql, offset, pageSize);
            String totalSql = dialect.getCountSql(sql);
            result = template.query(pageSql, params, rse);
            int total = template.queryForInt(totalSql, params);
            pageBean.setTotalCount(total);
        }
        else
        {
            result = template.query(sql, params, rse);
        }
        return result;
    }
    
    public static PageList getPage(String alias, int currentPage, int pageSize, String sql, Map paraMap)
    {
        int offset = (currentPage - 1) * pageSize;
        
        Dialect dialect = null;
        try
        {
            dialect = DialectUtil.getDialectByDataSourceAlias(alias);
        }
        catch (Exception e)
        {
            return null;
        }
        
        String pageSql = dialect.getLimitString(sql, offset, pageSize);
        String totalSql = dialect.getCountSql(sql);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
        namedParameterJdbcTemplate = getNamedParameterJdbcTemplate(alias);
        List list = namedParameterJdbcTemplate.queryForList(pageSql, paraMap);
        int total = namedParameterJdbcTemplate.queryForInt(totalSql, paraMap);
        
        PagingBean pageBean = new PagingBean(currentPage, pageSize);
        
        pageBean.setTotalCount(total);
        
        PageList pageList = new PageList();
        
        pageList.addAll(list);
        
        pageList.setPageBean(pageBean);
        
        return pageList;
    }
    
    public static List getPage(String alias, String sql, Map<?, ?> paraMap, PagingBean pageBean)
    {
        int currentPage = pageBean.getCurrentPage();
        int pageSize = pageBean.getPageSize();
        return getPage(alias, currentPage, pageSize, sql, paraMap);
    }
    
    public static JdbcTemplate getNewJdbcTemplate(String alias)
        throws Exception
    {
        Map map = DataSourceUtil.getDataSources();
        DataSource ds = (DataSource)map.get(alias);
        if ((ds == null) || (alias.equals("dataSource_Default")))
            return (JdbcTemplate)AppUtil.getBean("jdbcTemplate");
        return new JdbcTemplate(ds);
    }
    
    // 获取数据源，支持动态多数据源
    public static JdbcTemplate getFormTableJdbcTemplate(IFormTable formTable)
    {
        
        if (formTable.getIsExternal() == IFormTable.EXTERNAL)
        {
            if (formTable.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE))
            {
                return (JdbcTemplate)AppUtil.getBean("jdbcTemplate");
            }
            else
            {
                // 多数据源
                ISysDataSourceDefService sysDataSourceDefService = AppUtil.getBean(ISysDataSourceDefService.class);
                ISysDataSourceDef sysDataSourceDef = sysDataSourceDefService.getByAlias(formTable.getDsAlias());
                if (sysDataSourceDef == null)
                {
                    logger.error("datasource" + formTable.getDsAlias() + " is null, maybe has deleted...");
                    return null;
                }
                
                try
                {
                    return getNewJdbcTemplate(formTable.getDsAlias());
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
                return null;
            }
        }
        else
        {
            return (JdbcTemplate)AppUtil.getBean("jdbcTemplate");
        }
        
    }
    
}
