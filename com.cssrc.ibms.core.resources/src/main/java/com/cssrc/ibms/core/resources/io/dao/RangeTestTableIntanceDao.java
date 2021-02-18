package com.cssrc.ibms.core.resources.io.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.ins.RangeTestTableIntance;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * w_bcsybgsl
 * 靶场试验表单实例
 */
@Repository
public class RangeTestTableIntanceDao {
    @Resource
    private JdbcDao jdbcDao;

    /**
     * 直接保存全部数据
     * @param rangeTestTableIntance
     */
    public void insert(RangeTestTableIntance rangeTestTableIntance) {
        String sql="insert into w_bcsybgsl "
                + "(id,F_BGMC,F_BGBH,F_NR,F_JCBMB,F_SSGZXM,F_VERSION,F_KSJCSJ,F_JSJCSJ,F_BDZL,F_SSCH) value ("
                + rangeTestTableIntance.getId()+",'"
                + rangeTestTableIntance.getF_BGMC()+"','"
                + rangeTestTableIntance.getF_BGBH()+"','"
                + rangeTestTableIntance.getF_NR()+"','"
                + rangeTestTableIntance.getF_JCBMB()+"','"
                + rangeTestTableIntance.getF_SSGZXM()+"','"
                + rangeTestTableIntance.getF_VERSION()+"','"
                + rangeTestTableIntance.getF_KSJCSJ()+"','"
                + rangeTestTableIntance.getF_BDZL()+"','"
                + rangeTestTableIntance.getF_SSCH()+"'"
                +")";
        jdbcDao.exesql(sql,null);
    }
}
