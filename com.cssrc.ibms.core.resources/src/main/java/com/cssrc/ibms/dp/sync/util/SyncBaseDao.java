package com.cssrc.ibms.dp.sync.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.util.common.CommonTools;

public class SyncBaseDao {
    @Resource
    private JdbcDao jdbcDao;

    /**
     * 根据表名和过滤条件获取list
     *
     * @param tableName
     * @param syncBaseFilterList
     * @return
     */
    public List<Map<String, Object>> getListByTableNameAndFilter(String tableName, List<SyncBaseFilter> syncBaseFilterList) {
        String userId = "";
        String sql = "SELECT * FROM " + tableName + " WHERE 1=1 ";
        StringBuffer sb = new StringBuffer();
        String connection = " AND ";
        if (syncBaseFilterList != null && !syncBaseFilterList.isEmpty()) {
            for (SyncBaseFilter syncBaseFilter : syncBaseFilterList) {
                if (CommonTools.Obj2String(syncBaseFilter.getConnection()).equals("OR")) {
                    connection = " OR ";
                }
                sb.append(connection + syncBaseFilter.getFilterName() + " " + syncBaseFilter.getOperation() + "" + syncBaseFilter.getFilterValue());
            }
        }
        sql += sb.toString();
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sql, null);
        return dataList;
    }

    /**
     * 插入数据库
     *
     * @param tableName
     * @param map
     * @return
     */
    public long insertModelDataByTableName(String tableName, Map<String, String> map) throws Exception {
        long dataId = UniqueIdUtil.genId();
        String mapId = CommonTools.Obj2String(map.get("ID"));
        if (!mapId.equalsIgnoreCase("")) {
            dataId = Long.valueOf(mapId);
            map.remove("ID");
        }
        StringBuffer SB = new StringBuffer();
        StringBuffer keySB = new StringBuffer();
        StringBuffer valueSB = new StringBuffer();
        SB.append("insert into " + tableName);
        if (tableName.equalsIgnoreCase("CWM_SYS_FILE")) {
            keySB.append(" (FILEID ");
        } else {
            keySB.append(" (ID ");
        }
        valueSB.append(" values( " + dataId + "");
        if (map.isEmpty()) {
            throw new Exception("插入的数据为空！");
        } else {
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String keyString = it.next();
                keySB.append("," + keyString);
                if (CommonTools.Obj2String(map.get(keyString)).toUpperCase().indexOf("TO_DATE(") == 0) {
//					if(CommonTools.Obj2String(map.get(keyString)).toUpperCase().indexOf("TO_DATE(")==0||CommonTools.Obj2String(map.get(keyString)).toUpperCase().indexOf("<TABLE")==0){
                    valueSB.append("," + map.get(keyString) + "");
                } else {
//					valueSB.append(",'" + map.get(keyString)+"'");
                    valueSB.append(",:" + keyString + "");

                }
            }
            SB.append(keySB.toString() + " ) ");
            SB.append(valueSB.toString() + " ) ");
            int retV = jdbcDao.exesql(SB.toString(), map);
            if (retV > 0) {// =-1,表示更新失败，=0表示更新了0行；>0，表示更新了几行。
                return dataId;
            } else {
                throw new Exception("插入失败！");
            }
        }

    }

    /**
     * 更新数据库
     *
     * @param tableName
     * @param map
     * @param dataId
     * @return
     * @throws Exception
     */
    public Boolean updateModelDataByTableName(String tableName, Map<String, String> map, String dataId) throws Exception {
        StringBuffer SB = new StringBuffer();
        SB.append("update  " + tableName + " ");
        if (map.isEmpty()) {
            throw new Exception("为更新数据！");
        } else {
            SB.append(" Set  " + " ");
            Iterator<String> it = map.keySet().iterator();
            int flag = 0;
            while (it.hasNext()) {
                flag++;
                String keyString = it.next();
                if (flag == 1) {
                    SB.append(keyString + " = '" + map.get(keyString) + "' ");
                } else {
                    SB.append("," + keyString + " = '" + map.get(keyString) + "' ");
                }
            }
            if (tableName.equalsIgnoreCase("CWM_SYS_FILE")) {
                SB.append(" Where FILEID ='" + dataId + "' ");
            } else {
                SB.append(" Where ID ='" + dataId + "' ");
            }
            int retV = jdbcDao.exesql(SB.toString(), null);
            if (retV == -1) {// =-1,表示更新失败，=0表示更新了0行；>0，表示更新了几行。
                return false;
            } else {
                return true;
            }
        }
    }

    public Boolean deleteModelDataByTableNameAndFilters(String tableName, List<SyncBaseFilter> syncBaseFilterList) {

        String sql = "DELETE FROM " + tableName + " WHERE 1=1 ";
        StringBuffer sb = new StringBuffer();
        String connection = " AND ";
        if (syncBaseFilterList != null && !syncBaseFilterList.isEmpty()) {
            for (SyncBaseFilter syncBaseFilter : syncBaseFilterList) {
                if (CommonTools.Obj2String(syncBaseFilter.getConnection()).equals("OR")) {
                    connection = " OR ";
                }
                sb.append(connection + syncBaseFilter.getFilterName() + " " + syncBaseFilter.getOperation() + "" + syncBaseFilter.getFilterValue());
            }
            sql += sb.toString();
        }
//		if(true){
//			throw new RuntimeException("导入失败，当前型号代号");
//		}
        int retV = jdbcDao.exesql(sql.toString(), null);
        if (retV == -1) {// =-1,表示更新失败，=0表示更新了0行；>0，表示更新了几行。
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据表名查找数据库文件
     *
     * @param tableName
     * @param filters
     * @return
     */
    public List<Map<String, Object>> queryForListByTableName(String tableName,
                                                             String filters) throws Exception {
        if (!tableName.isEmpty()) {
            String sql = "select * from " + tableName + " Where 1=1 " + filters;
            return jdbcDao.queryForList(sql, null);
        } else {
            throw new Exception("插入的数据为空！");

        }
    }


    /**
     * 执行sql语句
     *
     * @param sql
     */
    public void exesql(String sql) {
        jdbcDao.exesql(sql, null);
    }

    /**
     * @Description: 更新对应W_DATAPACKAGEINFO数据状态  已完成
     * @author qiaozhili
     * @date 2020/6/19 10:42
     * @param
     * @return
     */
    public void upDataDatapackage(Map<String, Object> param) {
        String sql = "UPDATE W_DATAPACKAGEINFO SET F_ZXZT=:ZXZT WHERE ID=:taskId";
        jdbcDao.exesql(sql, param);
    }
}
