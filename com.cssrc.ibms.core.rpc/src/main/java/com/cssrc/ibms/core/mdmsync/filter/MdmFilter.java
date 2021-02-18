package com.cssrc.ibms.core.mdmsync.filter;

import java.util.List;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/**
 * 主数据同步，主要保护人员，组织，岗位，角色等数据。 同步目标表条件过滤器 接口
 * 
 * @ClassName: MdmFilter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月17日 下午4:50:26
 * 
 */
public interface MdmFilter<T>
{
    
    Class<?extends ISyncMdmDataService<T>> getService();
    /**
     * 获取过滤条件 集合
     * 
     * @param formTable 同步目标表
     * @param f 同步字段
     * @param mdmData 用户、组织、角色、岗位
     * @return
     */
    public List<String> getFilter(IFormTable formTable, IFormField f, T mdmData);
    
    /**
     * 获取过滤条件 集合
     * 
     * @param formTable 同步目标表
     * @param f 同步字段
     * @param mdmData 用户、组织、角色、岗位
     * @return
     */
    public String getUpDateSql(IFormTable formTable, IFormField f, T mdmData,String id,String[] fnameVals);
}
