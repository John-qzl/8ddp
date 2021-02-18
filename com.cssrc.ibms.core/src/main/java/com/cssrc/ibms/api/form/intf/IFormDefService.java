package com.cssrc.ibms.api.form.intf;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.model.BaseFormDefXml;
import com.cssrc.ibms.api.form.model.BaseFormRights;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormDefXml;
import com.cssrc.ibms.api.form.model.IFormModel;
import com.cssrc.ibms.api.form.model.IFormRights;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

/**
 * 对象功能:IBMS_FORM_DEF Service类 开发人员:zhulongchao
 */
@Service
public interface IFormDefService
{
    
    /**
     * 获得已发布版本数量
     * 
     * @param formKey 在表单版本中使用
     * @return
     */
    public Integer getCountByFormKey(Long formKey);
    
    /**
     * 获得默认版本
     * 
     * @param formKey 在表单版本中使用
     * @return
     */
    public IFormDef getDefaultVersionByFormKey(Long formKey);
    
    /**
     * 处理权限的人员
     * 
     * @param permission
     * @param flag 标记是导出（true）还是导入(false)
     * @return
     */
    public String parsePermission(String permission, boolean flag);
    
    /**
     * 根据formkey查询所有的表单定义版本。
     * 
     * @param formKey 在表单版本中使用
     * @return
     */
    public List<? extends IFormDef> getByFormKey(Long formKey);
    
    /**
     * 发布
     * 
     * @param formDefId 自定义表单Id
     * @param operator 发布人
     * @throws Exception
     */
    public void publish(Long formDefId, String operator)
        throws Exception;
    
    /**
     * 设为默认版本。
     * 
     * @param formDefId 自定义表单Id
     * @param formKey 在表单版本使用
     */
    public void setDefaultVersion(Long formDefId, Long formKey);
    
    /**
     * 根据表单定义id创建新的表单版本。 表单定义ID
     * 
     * @param formDefId 自定义表单Id
     * @throws Exception
     */
    public void newVersion(Long formDefId)
        throws Exception;
    
    /**
     * 根据任务id 获取API调用的跳转URL 。
     * 
     * @param taskId
     * @param defId
     * @param nodeId
     * @param businessKey
     * @param ctxPath
     * @return
     */
    public String getFormUrl(String taskId, Long defId, String nodeId, String businessKey, String ctxPath);
    
    /**
     * 取得发布的表单。
     * 
     * @param queryFilter
     * @return
     */
    public List<? extends IFormDef> getPublished(QueryFilter queryFilter);
    
    public List<? extends IFormDef> getAllPublished(String formDesc);
    
    /**
     * 根据表单key获取默认的表单定义。
     * 
     * @param formKey
     * @return
     */
    public IFormDef getDefaultPublishedByFormKey(Long formKey);
    
    /**
     * 判断表单是否已经被使用。
     * 
     * @param formKey 表单key
     * @return
     */
    public int getFlowUsed(Long formKey);
    
    /**
     * 根据formKey获取其关联的流程定义，结果返回 表单名，关联的流程定义（List<String>）
     * 
     * @param formKey
     * @return
     */
    public List getFormRelatedDef(Long formKey);
    
    /**
     * 根据formkey删除数据。
     * 
     * <pre>
     * 	如果表已经生成并且表单是通过设计器进行设计的那么将删除所创建的表。
     * </pre>
     * 
     * @param formKey
     * @throws SQLException
     */
    public void delByFormKey(Long formKey)
        throws SQLException;
    
    /**
     * 获取现有表单Id函数
     * 
     * @param nodeSet
     * @return
     */
    public Long getCurrentTableId(INodeSet nodeSet);
    
    /**
     * 
     * 导出表单XML
     * 
     * <pre>
     * 1.导出流程定义
     * 2.导出流程定义权限
     * 3.导出数据模板
     * </pre>
     * 
     * @param formDefIds
     * @param map 是否导出的Map列表
     * @return
     * @throws Exception
     */
    public String exportXml(Long[] formDefIds, Map<String, Boolean> map)
        throws Exception;
    
    public Map<String, Boolean> getDefaultExportMap(Map<String, Boolean> map);
    
    /**
     * 导入xml
     * 
     * <pre>
     * 1.导入流程定义
     * 2.导入流程定义权限
     * 3.导入数据模板
     * </pre>
     * 
     * @param inputStream
     * @return
     * @throws Exception
     */
    public void importXml(InputStream inputStream)
        throws Exception;
    
    public void updCategory(Long categoryId, List<Long> formKeyList);
    
    /**
     * 根据流程定义ID，取得Table ID
     * 
     * @param defId 流程定义id
     * @return
     */
    public Long getTableIdByDefId(Long defId);
    
    /**
     * 根据流程定义ID，节点ID，取得流程开始表单定义。 在节点没有设置表单时，如果cascade为true，则会查询全局表单和开始表单
     * 
     * @param actDefId
     * @param nodeId
     * @param cascade 是否向上查找标志
     * @return
     */
    public IFormDef getNodeFormDef(String actDefId, String nodeId, boolean cascade);
    
    /**
     * 根据流程定义ID，取得流程全局表单定义
     * 
     * @param actDefId
     * @return
     */
    public IFormDef getGlobalFormDef(String actDefId);
    
    /**
     * 查看明细时获取表单
     * 
     * @param processRun
     * @param userId
     * @param ctxPath
     * @param variables
     * @return
     * @throws Exception
     */
    public IFormModel getForm(IProcessRun processRun, Long userId, String ctxPath, Map<String, Object> variables)
        throws Exception;
    
    /**
     * 判断自定义表是否绑定了表单
     * 
     * @param tableId 自定义表ID
     * @return <code>true</code> 绑定了表单<br/>
     *         <code>false</code> 末绑定表单
     */
    public boolean isTableHasFormDef(Long tableId);
    
    
    public IFormModel getForm(IProcessRun processRun, String tempNodeId, Long valueOf, String ctxPath,
        Map<String, Object> variables)
        throws Exception;
    
    
    public IFormDef getById(Long parentFormDefId);
    
    /**
     * 导入自定义表单
     * 
     * @param bpmFormDefXmlList
     */
    public void importFormDef(List<? extends IFormDefXml> bpmFormDefXmlList, List<Map<Long, Long>> mapFormKeyList,
        List<Map<Long, Long>> mapFormIdList)
        throws Exception;
    
    /**
     * 根据actDefId 获取表单权限。
     * 
     * @param actDefId
     * @return
     */
    public List<? extends BaseFormRights> exportFormRightsUser(String actDefId);
    
    /**
     * 
     * @param formKey
     * @param isDefault
     * @return
     */
    public List<?extends BaseFormDefXml> getFormDefXmlList(Map<String, Boolean> map, Set<Long> formKeySet,
        Set<Long> tableIdSet);
    
    public List<? extends IFormDef> getAll(QueryFilter filter);
    
    public ArrayList<Class<?>> getAllClass();
    
}
