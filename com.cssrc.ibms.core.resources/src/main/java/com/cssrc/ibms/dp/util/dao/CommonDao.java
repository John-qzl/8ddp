package com.cssrc.ibms.dp.util.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.util.IOConstans;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description 通用数据库操作类
 * @Author ZMZ
 * @Date 2021/1/4 16:20
 */
@Repository
public class CommonDao {
    @Resource
    private JdbcDao jdbcDao;
    /**
     * @Description 查找当前记录
     * @Author ZMZ
     * @Date 2020/12/10 0:03
     * @param id
     * @param tableName
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> getById(Object id, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from ").append(tableName).append(" where id=:id");
        Map<String, Object> sqlMap=new HashMap<>();
        sqlMap.put("id",id);
        return jdbcDao.queryForMap(sql.toString(),sqlMap);
    }
    /**
     * @Description 覆盖当前记录
     * @Author ZMZ
     * @Date 2020/12/10 0:04
     * @param map
     * @param tableName
     * @Return void
     */
    public void update(Map<String, Object> map, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(tableName);
        sql.append(" set ");
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            boolean isDateData=false;
            String key = (String) it.next();
            for (String dateData: IOConstans.DATE_DATA_ARRAY){
                if (key.equals(dateData)){
                    //是日期格式
                    //to_date('2010-01-01','yyyy-mm-dd')
                    if (!"".equals(map.get(key))){
                        //有日期数据
                        sql.append(key).append("=to_date('").append(map.get(key).toString().substring(0,10)).append("','yyyy-mm-dd'),");
                    }
                    isDateData=true;
                }
            }
            if (!isDateData){
                //不是日期
                sql.append(key).append("=:").append(key).append(",");
            }

        }
        sql.replace(sql.length() - 1, sql.length(), "");
        sql.append(" where ID=:ID");
        jdbcDao.exesql(sql.toString(), map);
    }

    /**
     * @Description 新增一条记录
     * @Author ZMZ
     * @Date 2020/12/10 0:04
     * @param map
     * @param tableName
     * @Return void
     */
    public void insert(Map<String, Object> map, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ").append(tableName).append("(");
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            sql.append(key).append(",");
        }
        sql.replace(sql.length() - 1, sql.length(), "");
        sql.append(") values (");
        it = map.keySet().iterator();
        while (it.hasNext()) {
            boolean isDateData=false;
            String key = (String) it.next();
            for (String dateData:IOConstans.DATE_DATA_ARRAY){
                if (key.equals(dateData)){
                    //是日期格式
                    //to_date('2010-01-01','yyyy-mm-dd')
                    if (!"".equals(map.get(key))){
                        //有日期数据
                        sql.append("to_date('").append(map.get(key).toString().substring(0,10)).append("','yyyy-mm-dd'),");
                    }
                    isDateData=true;
                }
            }
            if (!isDateData){
                //不是日期
                sql.append(":").append(key).append(",");
            }
        }
        sql.replace(sql.length() - 1, sql.length(), "");
        sql.append(")");
        jdbcDao.exesql(sql.toString(),map);

    }
    /**
     * Description : 根据displayId获取表信息
     * Author : ZMZ
     * Date : 2019年10月13日下午12:07:04
     * Return : List<Map<String,Object>>
     */
    public List<Map<String,Object>> getTableInfo(String displayId){
        String sql = "SELECT b.* FROM IBMS_DATA_TEMPLATE a LEFT JOIN IBMS_FORM_TABLE b ON a.TABLEID=b.TABLEID WHERE a.ID="+displayId;
        return jdbcDao.queryForList(sql, null);
    }
    /**
     * @Description 如果有记录,就覆盖,没有记录就新建
     * @Author ZMZ
     * @Date 2021/1/4 16:31
     * @param entityMap
     * @param tableName
     * @Return void
     */
    private void insertOrUpdate(Map<String, Object> entityMap, String tableName) {
        //查询是否存在
        Map<String, Object> resMap=getById(entityMap.get("ID"),tableName);
        if (resMap!=null){
            //存在记录,直接覆盖
            update(entityMap,tableName);
        }else {
            //没有记录,新建
            insert(entityMap,tableName);
        }
    }
}


