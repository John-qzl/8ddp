package com.cssrc.ibms.dp.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 为什么已经有CheckResultDao,还要单独写这个dao
 *      两者用的CheckResult的bean不同!!
 */
@Repository
public class CheckResultBeanDao {

    @Resource
    private JdbcDao jdbcDao;

    /**
     * 新增数据
     * @param checkResult
     */
    public void insert(CheckResult checkResult){
        String sql="insert into W_CK_RESULT (id,F_RESULT,F_VALUE,F_IFNUMM,F_SKETCHMAP,F_ITEMDEF_ID,F_TB_INSTAN) values ("
                +checkResult.getId()+",'"
                +checkResult.getResult()+"','"
                +checkResult.getValue()+"','"
                +checkResult.getIfNumm()+"','"
                +checkResult.getSketchMap()+"','"
                +checkResult.getItemDef_id()+"','"
                +checkResult.getTb_instan_id()+"')";
        jdbcDao.exesql(sql,null);
    }
    /**
     * 以当前id为主键,利用传来的entity更新自身除id外的全部数据
     */
    public void update(CheckResult checkResult){
        String sql="update W_CK_RESULT set "
                +"F_RESULT=:F_RESULT,"
                +"F_VALUE=:F_VALUE,"
                +"F_IFNUMM=:F_IFNUMM,"
                +"F_SKETCHMAP=:F_SKETCHMAP,"
                +"F_ITEMDEF_ID=:F_ITEMDEF_ID,"
                +"F_TB_INSTAN=:F_TB_INSTAN "
                +"where id=:ID";
        Map map=new HashMap<>();
        map.put("F_RESULT",checkResult.getResult());
        map.put("F_VALUE",checkResult.getValue());
        map.put("F_IFNUMM",checkResult.getIfNumm());
        map.put("F_SKETCHMAP",checkResult.getSketchMap());
        map.put("F_ITEMDEF_ID",checkResult.getItemDef_id());
        map.put("F_TB_INSTAN",checkResult.getTb_instan_id());
        map.put("ID",checkResult.getId());
        jdbcDao.exesql(sql,map);
    }

}
