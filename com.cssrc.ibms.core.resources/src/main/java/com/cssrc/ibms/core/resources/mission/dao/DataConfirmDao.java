package com.cssrc.ibms.core.resources.mission.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.mission.model.DataConfirmMapToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 靶场数据确认的dao
 */
@Repository
public class DataConfirmDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    /**
     * 获取流水号
     * 这个流水号的写了一半,去写验收的流水号了,代码里的sql都没改
     * @param moduleId
     * @return
     */
    public String getPlanNumber(String moduleId){

        String sql="SELECT * FROM(SELECT F_CHBGBBH FROM W_BCSYBGB where  F_SSXHID='"+moduleId+"' ORDER BY ID DESC) WHERE ROWNUM<=1";
        Map<String, Object> map=jdbcDao.queryForMap(sql, null);
        String fullNumber="001";
        if(map!=null) {
            fullNumber=map.get("F_CHBGBBH").toString();
        }
        //String number= String.valueOf(jdbcDao.queryForInt(sql, null)+1);
        String lastThreeNumber=fullNumber.substring(fullNumber.length()-3,fullNumber.length());
        Integer lastNumber=Integer.valueOf(lastThreeNumber)+1;
        String number=(lastNumber).toString();
        switch (number.length()) {
            case 1:
                number="00"+number;
                break;
            case 2:
                number="0"+number;
                break;
            case 3:
                number=number;
                break;
        }
        return number;
    }

    /**
     * 根据数据确认的id获取子流程的bean
     * @param reportId
     * @return
     */
    public List<DataConfirmMapToBean> getNeedTodoByReportId(String reportId){
        List<DataConfirmMapToBean> dataConfirmMapToBeanList=new ArrayList<>();
        String sql="SELECT * FROM W_BCSJQRDBB where F_SSZJ=:reportId";
        Map<String,Object> sqlMap=new HashMap<>();
        sqlMap.put("reportId",reportId);
        List<Map<String, Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
        if (resMapList==null){
            return null;
        }else {
            for (Map<String, Object> resMap:resMapList){
                DataConfirmMapToBean dataConfirmMapToBean=new DataConfirmMapToBean(resMap);
                dataConfirmMapToBeanList.add(dataConfirmMapToBean);
            }
            return dataConfirmMapToBeanList;
        }

    }
}
