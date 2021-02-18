package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.BusBak;

@Repository
public class BusBakDao extends BaseDao<BusBak>
{
    
    @Override
    public Class<?> getEntityClass()
    {
        return BusBak.class;
    }
    
    public BusBak getByPk(Long pk)
    {
        return this.getUnique("getByPk", pk);
    }
    
    public BusBak getByPkStr(String pk)
    {
        return this.getUnique("getByPkStr", pk);
    }
    
    public void delByPk(Long pk)
    {
        this.delBySqlKey("delByPk", pk);
    }
    
    public void delByPkStr(String pk)
    {
        this.delBySqlKey("delByPkStr", pk);
    }
    
    /**
     * 
     * @param pk
     * @param tableId
     */
    public List<BusBak> getBakData(String pk, Long tableId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("busPk", pk);
        params.put("tableId", tableId);
        return this.getBySqlKey("getAll", params);
        
    }
    
}
