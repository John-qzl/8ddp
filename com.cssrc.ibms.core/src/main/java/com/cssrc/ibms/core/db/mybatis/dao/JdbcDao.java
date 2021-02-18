package com.cssrc.ibms.core.db.mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.cssrc.ibms.core.db.mybatis.dialect.Dialect;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;

public class JdbcDao
{
    
    private JdbcTemplate jdbcTemplate;
    
    private Dialect dialect;
    
    private NamedParameterJdbcTemplate parameterJdbcTemplate;
    
    public JdbcDao()
    {
    }
    
    public JdbcDao(JdbcTemplate jdbcTemplate, Dialect dialect)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.dialect = dialect;
        this.parameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void setDialect(Dialect dialect)
    {
        this.dialect = dialect;
    }
    
    public NamedParameterJdbcTemplate getParameterJdbcTemplate()
    {
        return parameterJdbcTemplate;
    }
    
    public void setParameterJdbcTemplate(NamedParameterJdbcTemplate parameterJdbcTemplate)
    {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }
    
    @SuppressWarnings({"rawtypes"})
    public List getPage(int currentPage, int pageSize, String sql, Map paraMap, PagingBean pageBean)
        throws Exception
    {
        int offset = (currentPage - 1) * pageSize;
        String pageSql = dialect.getLimitString(sql, offset, pageSize);
        String totalSql = dialect.getCountSql(sql);
        List list = queryForList(pageSql, paraMap);
        int total = queryForInt(totalSql, paraMap);
        pageBean.setTotalCount(total);
        
        return list;
    }
    
    /**
     * 分页，list中对象为model对象。<br>
     * 如果使用分页查询需要设置方言。<br>
     * 使用方法：<br>
     * <pre>
     * PageModel pageModel= jdbcHelper.getPage(1, 2, "select * from sys_role" , null,new RowMapper<Role>() {
     *		@Override
     *		public Role mapRow(ResultSet rs, int arg1) throws SQLException {
     *			Role role=new Role();
     *			role.setName(rs.getString("name"));
     *			return role;
     *		}
     *	});
     * </pre>
     * @param currentPage 当前页， 从1开始
     * @param pageSize 每页显示记录数
     * @param sql sql语句，如果需要参数，请输入如下sql语句，sql="select * from user where name=:name";
     * @param paraMap 参数，为map对象
     * @param rowMap 映射对象的方法。
     * @return
     * @throws Exception 
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List getPage(PagingBean pageBean, String sql, RowMapper rowMap)
        throws Exception
    {
        int pageSize = pageBean.getPageSize();
        int offset = pageBean.getFirst();
        Map map = new HashMap();
        String pageSql = dialect.getLimitString(sql, offset, pageSize);
        String totalSql = dialect.getCountSql(sql);
        List list = queryForList(pageSql, map, rowMap);
        int total = queryForInt(totalSql, map);
        pageBean.setTotalCount(total);
        return list;
    }
    
    public <T> T getPage(String sql, ResultSetExtractor<T> rse, PagingBean pageBean)
        throws Exception
    {
        
        int pageSize = pageBean.getPageSize();
        int offset = pageBean.getFirst();
        String pageSql = dialect.getLimitString(sql, offset, pageSize);
        String totalSql = dialect.getCountSql(sql);
        T result = jdbcTemplate.query(pageSql, rse);
        int total = jdbcTemplate.queryForInt(totalSql);
        pageBean.setTotalCount(total);
        return result;
    }
    
    /**
     * 输入查询语句和查询条件查询列表。
     * @param sql 查询语句  示例：select * from sys_role where name=:name
     * @param parameter Map map=new HashMap(); 		map.put("name", "li");
     * @param rowMap  需要实现RowMapper接口,有两种实现方式，一种是匿名类，另外一种是实现RowMapper接口。
     * 使用方法如下：<br/>
     * <pre>
     * List<Role> list =jdbcHelper.queryForList(sql, null, new RowMapper<Role>() {
     *		@Override
     *		public Role mapRow(ResultSet rs, int arg1) throws SQLException {
     *			Role role=new Role();
     *			role.setName(rs.getString("name"));
     *			return role;
     *		}
     *	});
     * </pre>
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map<String, Object>> queryForList(String sql, Map parameter, RowMapper rowMap)
    {
        return parameterJdbcTemplate.query(sql, parameter, rowMap);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map<String, Object>> queryForList(String sql, Map parameter)
    {
        return parameterJdbcTemplate.queryForList(sql, parameter);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int queryForInt(String sql, Map parameter)
    {
        return parameterJdbcTemplate.queryForInt(sql, parameter);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int exesql(String sql, Map parameter)
    {
        return parameterJdbcTemplate.update(sql, parameter);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, Object> queryForMap(String sql, Map parameter)
    {
        try
        {
            return parameterJdbcTemplate.queryForMap(sql, parameter);
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
    
}
