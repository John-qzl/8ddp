package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ModelInfoSyncDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 仅保存签章id，需要用户手动绑定签章
     * @param fileId
     */
    public void insert(Long id,String fileId,String unrealFullName){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO w_PADHCQZB (ID,F_QZID,F_PADHCQM) VALUES ("+id+",'"+fileId+"','"+unrealFullName+"')");
        jdbcTemplate.execute(sql.toString());
    }

    /**
     * 保存用户id和签章id
     * @param id
     * @param userId
     * @param fileId
     */
    public void insert(Long id, Long userId,String userName, String fileId,String unrealFullName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO w_PADHCQZB (ID,F_QZID,F_YHID,F_YH,F_PADHCQM) VALUES ("+id+",'"+fileId+"','"+userId+"','"+userName+"','"+unrealFullName+"')");
        jdbcTemplate.execute(sql.toString());
    }
}
