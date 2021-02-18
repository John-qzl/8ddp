package com.cssrc.ibms.core.form.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.model.BaseFormRights;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormDefXml;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.api.system.intf.ISysBusEventService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.BaseSysBusEvent;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.api.system.model.ISysBusEvent;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.form.dao.FormDefDao;
import com.cssrc.ibms.core.form.dao.FormDefTreeDao;
import com.cssrc.ibms.core.form.dao.FormRightsDao;
import com.cssrc.ibms.core.form.dao.FormTableDao;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormDefHi;
import com.cssrc.ibms.core.form.model.FormDefTree;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormModel;
import com.cssrc.ibms.core.form.model.FormRights;
import com.cssrc.ibms.core.form.model.FormRun;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.util.FormFieldUtil;
import com.cssrc.ibms.core.form.util.FormTableUtil;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.form.util.XmlToBeanUtil;
import com.cssrc.ibms.core.form.xml.form.FormDefXml;
import com.cssrc.ibms.core.form.xml.form.FormDefXmlList;
import com.cssrc.ibms.core.form.xml.table.FormTableXml;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;

import freemarker.template.TemplateException;

/**
 * 对象功能:IBMS_FORM_DEF Service类 开发人员:zhulongchao
 */
@Service
public class FormDefService extends BaseService<FormDef> implements IFormDefService
{
    @Resource
    private FormDefDao dao;
    
    @Resource
    private FormRightsService formRightsService;
    
    @Resource
    private FormHandlerService formHandlerService;
    
    @Resource
    private FormRunService formRunService;
    
    @Resource
    private FormTableService formTableService;
    
    @Resource
    private FormRightsDao formRightsDao;
    
    @Resource
    private INodeSetService nodeSetService;
    
    @Resource
    private FormFieldService formFieldService;
    
    @Resource
    private IDefinitionService definitionService;
    
    @Resource
    private IGlobalTypeService globalTypeService;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private ISysRoleService sysRoleService;
    
    @Resource
    private ISysOrgService sysOrgService;
    
    @Resource
    private IPositionService positionService;
    
    @Resource
    private DataTemplateDao dataTemplateDao;
    
    @Resource
	private FreemarkEngine freemarkEngine;
    
    @Resource
    private FormTableDao formTableDao;
    
    @Resource
    private ISysBusEventService sysBusEventService;
    
    @Resource
    private ISysFileService sysFileService;
    
    @Resource
    private FormDefTreeDao formDefTreeDao;
    
    @Resource
    private FormDefHiService formDefHiService;
    
    @Resource
    private FormTemplateService formTemplateService;
    
    @Resource
    ISerialNumberService serialNumberService;
    public FormDefService()
    {
    }
    
    @Override
    protected IEntityDao<FormDef, Long> getEntityDao()
    {
        return dao;
    }
    
    @Override
    public ArrayList<Class<?>> getAllClass()
    {
        ArrayList<Class<?>> class_=new ArrayList<Class<?>>();
        class_.add(FormDefXmlList.class);
        class_.add(sysBusEventService.getBusClass());
        class_.add(sysFileService.getSysFileClass());
        class_.add(serialNumberService.getSerialNumberClass());
        class_.add(FormDefXml.class);
        class_.add(FormRights.class);
        class_.add(FormTableXml.class);
        return class_;
    }
    
    public ArrayList<Class<?>> getFormDefClass()
    {
        ArrayList<Class<?>> class_=new ArrayList<Class<?>>();
        class_.add(FormDefXmlList.class);
        class_.add(sysBusEventService.getBusClass());
        class_.add(sysFileService.getSysFileClass());
        class_.add(sysFileService.getSysFileClass());
        return class_;
    }
    
    /**
     * 获得已发布版本数量
     * 
     * @param formKey 在表单版本中使用
     * @return
     */
    public Integer getCountByFormKey(Long formKey)
    {
        return dao.getCountByFormKey(formKey);
    }
    
    /**
     * 获得默认版本
     * 
     * @param formKey 在表单版本中使用
     * @return
     */
    public FormDef getDefaultVersionByFormKey(Long formKey)
    {
        return dao.getDefaultVersionByFormKey(formKey);
    }
    
    /**
     * 处理权限的人员
     * 
     * @param permission
     * @param flag 标记是导出（true）还是导入(false)
     * @return
     */
    public String parsePermission(String permission, boolean flag)
    {
        JSONObject json = JSONObject.fromObject(permission);
        Object read = json.get("read");
        Object write = json.get("write");
        if (read == null)
            read = "{}";
        if (write == null)
            write = "{}";
        json.element("read", parseUseInfo(read.toString(), flag));
        json.element("write", parseUseInfo(write.toString(), flag));
        return json.toString();
    }
    
    /**
     * 处理权限的人员
     * 
     * @param mode 处理json
     * @param flag 标记是导出（true）还是导入(false)
     * @return
     */
    private String parseUseInfo(String mode, boolean flag)
    {
        JSONObject node = JSONObject.fromObject(mode);
        if (JSONUtils.isNull(node))
            return "";
        if (JSONUtils.isNull(node.get("type")))
            return mode;
        if (JSONUtils.isNull(node.get("id")))
            return mode;
        String type = node.get("type").toString();
        String id = (String)node.get("id");
        String tempFullname = (String)node.get("fullname");
        if (FormRights.TYPE_NONE.equals(type) || FormRights.TYPE_EVERYONE.equals(type))
            return mode;
        String[] idArr = id.split(",");
        if (flag)
        {// 导出只对人员处理
            if (FormRights.TYPE_USER.equals(type))
            {
                StringBuffer accounts = new StringBuffer();
                ISysUser sysUser = null;
                for (String tempId : idArr)
                {
                    sysUser = sysUserService.getById(Long.parseLong(tempId));
                    accounts.append(sysUser.getUsername()).append(",");
                }
                if (BeanUtils.isNotEmpty(accounts))
                    node.element("fullname", accounts.deleteCharAt(accounts.length() - 1).toString());
            }
        }
        // 导入
        else
        {
            if (StringUtil.isEmpty(tempFullname))
                return cleanPermission(node);
            // 因传入的fullname可能为“用户1,用户2”，因此将传入的fullname进行分割再处理
            String[] fullnameArr = tempFullname.split(",");
            int count = 0;// 用于统计找不到记录的次数
            StringBuffer ids = new StringBuffer();// 存放过滤后的用户ID
            StringBuffer names = new StringBuffer();// 存放过滤后的用户工号
            for (int i = 0; i < fullnameArr.length; i++)
            {
                String fullname = fullnameArr[i];
                Object[] args = {fullname};
                // 用户
                if (FormRights.TYPE_USER.equals(type))
                {
                    ISysUser sysUser = sysUserService.getById(Long.parseLong(idArr[i]));
                    if (BeanUtils.isEmpty(sysUser))
                    {
                        MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmFormDef.parseUseInfo.accuntIsEmpt", args));
                        count++;
                    }
                    else
                    {
                        ids.append(sysUser.getUserId()).append(",");
                        names.append(sysUser.getFullname()).append(",");
                        
                    }
                }
                // 角色
                else if (FormRights.TYPE_ROLE.equals(type))
                {
                    ISysRole sysRole = sysRoleService.getByRoleName(fullname).get(0);// 会有多个重名rolename情况，有错误可能
                    if (BeanUtils.isEmpty(sysRole))
                    {
                        MsgUtil.addMsg(MsgUtil.ERROR,
                            getText("service.bpmFormDef.parseUseInfo.roleName.notExist", args));
                        count++;
                    }
                    else
                    {
                        ids.append(sysRole.getRoleId()).append(",");
                        names.append(sysRole.getRoleName()).append(",");
                    }
                }
                // 组织或组织负责人
                else if (FormRights.TYPE_ORG.equals(type) || FormRights.TYPE_ORGMGR.equals(type))
                {
                    ISysOrg sysOrg = sysOrgService.getByOrgName(fullname).get(0);
                    if (BeanUtils.isEmpty(sysOrg))
                    {
                        MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmFormDef.parseUseInfo.orgName.notExist", args));
                        count++;
                    }
                    else
                    {
                        ids.append(sysOrg.getOrgId()).append(",");
                        names.append(sysOrg.getOrgName()).append(",");
                    }
                }
                // 岗位
                else if (FormRights.TYPE_POS.equals(type))
                {
                    List<? extends IPosition> position = positionService.getByPosName(fullname);
                    if (BeanUtils.isEmpty(position))
                    {
                        MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmFormDef.parseUseInfo.posName.notExist", args));
                        count++;
                    }
                    else
                    {
                        ids.append(position.get(0).getPosId()).append(",");
                        names.append(position.get(0).getPosName()).append(",");
                    }
                }
            }
            // 若数组中所有的名称在数据库中都无对应记录
            if (count == fullnameArr.length)
            {
                return cleanPermission(node);
            }
            else
            {
                node.element("id", ids.deleteCharAt(ids.length() - 1).toString());
                node.element("fullname", names.deleteCharAt(names.length() - 1).toString());
            }
        }
        return node.toString();
        
    }
    
    /**
     * 清理权限
     * 
     * @param node 权限
     * @return
     */
    private String cleanPermission(JSONObject permission)
    {
        permission.element("id", "");
        permission.element("fullname", "");
        return permission.toString();
    }
    
    /**
     * 根据formkey查询所有的表单定义版本。
     * 
     * @param formKey 在表单版本中使用
     * @return
     */
    public List<FormDef> getByFormKey(Long formKey)
    {
        return dao.getByFormKey(formKey);
    }
    
    /**
     * 增加表单定义。
     * 
     * @param bpmFormDef 自定义表单对象
     * @throws Exception
     */
    public void addForm(FormDef formDef)
        throws Exception
    {
        long id = UniqueIdUtil.genId();
        formDef.setFormDefId(id);
        formDef.setFormKey(id);
        formDef.setVersionNo(1);
        formDef.setIsDefault((short)1);
        formDef.setIsPublished((short)0);
        dao.add(formDef);
        // 添加自定义表单修改历史
        FormDefHi formDefHi = new FormDefHi(formDef);
        formDefHiService.addHisRecord(formDefHi);
        
    }
    
    /**
     * 更新表单及权限。
     * 
     * @param bpmFormDef 自定义表单对象
     * @throws Exception
     */
    public void updateForm(FormDef formDef)
        throws Exception
    {
        
        // 更新table
        dao.update(formDef);
        // 添加自定义表单修改历史
        FormDefHi formDefHi = new FormDefHi(formDef);
        formDefHiService.addHisRecord(formDefHi);
        
    }
    
    /**
     * 发布
     * 
     * @param formDefId 自定义表单Id
     * @param operator 发布人
     * @throws Exception
     */
    public void publish(Long formDefId, String operator)
        throws Exception
    {
        // 设为已发布
        FormDef formDef = dao.getById(formDefId);
        
        formDef.setIsPublished((short)1);
        formDef.setPublishedBy(operator);
        formDef.setPublishTime(new Date());
        dao.update(formDef);
        
    }
    
    /**
     * 设为默认版本。
     * 
     * @param formDefId 自定义表单Id
     * @param formKey 在表单版本使用
     */
    public void setDefaultVersion(Long formDefId, Long formKey)
    {
        dao.setDefaultVersion(formKey, formDefId);
    }
    
    /**
     * 根据表单定义id创建新的表单版本。 表单定义ID
     * 
     * @param formDefId 自定义表单Id
     * @throws Exception
     */
    public void newVersion(Long formDefId)
        throws Exception
    {
        FormDef formDef = dao.getById(formDefId);
        Integer rtn = dao.getMaxVersionByFormKey(formDef.getFormKey());
        Long newFormDefId = UniqueIdUtil.genId();
        // 创建新的版本
        FormDef newVersion = (FormDef)formDef.clone();
        newVersion.setFormDefId(newFormDefId);
        newVersion.setIsDefault((short)0);
        newVersion.setIsPublished((short)0);
        newVersion.setPublishedBy("");
        
        newVersion.setVersionNo(rtn + 1);
        dao.add(newVersion);
        // 拷贝表单权限
        
    }
    
    /**
     * 添加复制的表单，包括表单权限信息
     * 
     * @param bpmFormDef
     * @param oldFormkey
     */
    public void copyForm(FormDef bpmFormDef, Long oldFormkey)
    {
        dao.add(bpmFormDef);
        Long formKey = bpmFormDef.getFormKey();
        if (bpmFormDef.getDesignType() == 0)
        {
            List<FormRights> list = formRightsDao.getByFormDefId(oldFormkey);
            for (FormRights bpmFormRights : list)
            {
                Long newId = UniqueIdUtil.genId();
                bpmFormRights.setId(newId);
                bpmFormRights.setFormDefId(formKey);
                formRightsDao.add(bpmFormRights);
            }
        }
    }
    
    /**
     * 根据BpmFormRun取得表单。 表单分为： 1.在线表单。 2.url表单。
     * 
     * <pre>
     * 1.首先去bpmformrun中获取表单数据。
     * 2.没有获取到则获取当前节点的表单设置。
     * 3.获取全局表单设置。
     * </pre>
     * 
     * @param processRun
     * @param nodeId
     * @param userId
     * @param ctxPath
     * @param variables
     * @return
     * @throws Exception
     */
    public FormModel getForm(IProcessRun processRun, String nodeId, Long userId, String ctxPath,
        Map<String, Object> variables)
        throws Exception
    {
        String instanceId = processRun.getActInstId();
        String actDefId = processRun.getActDefId();
        String businessKey = processRun.getBusinessKey();
        
        FormRun bpmFormRun = formRunService.getByInstanceAndNode(instanceId, nodeId);
        
        INodeSet bpmNodeSet = null;
        String parentActDefId = "";
        if (variables.containsKey("parentActDefId"))
        {
            parentActDefId = (String)variables.get("parentActDefId");
            bpmNodeSet = this.nodeSetService.getByActDefIdNodeId(actDefId, nodeId, parentActDefId);
        }
        else
        {
            bpmNodeSet = this.nodeSetService.getByActDefIdNodeId(actDefId, nodeId);
        }
        
        FormModel formModel = new FormModel();
        FormDef bpmFormDef=new FormDef();
        // 运行时存在。
        if (bpmFormRun != null)
        {
            Long formDefId = bpmFormRun.getFormdefId();
            bpmFormDef = dao.getById(formDefId);

            Long formKey = bpmNodeSet.getFormKey();
            // 判断当前是否 更换了表单
            if (bpmFormDef != null && bpmFormDef.getFormKey().longValue() == formKey.longValue())
            {
                String tablename = "";
                Long tableId = bpmFormDef.getTableId();
                if (tableId > 0)
                {
                    FormTable bpmFormTable = formTableService.getById(tableId);
                    if (BeanUtils.isNotEmpty(bpmFormTable))
                    {
                        tablename = bpmFormTable.getTableName();
                        bpmFormDef.setTableName(tablename);
                    }
                }
                // 判断数据是否有效。
                isDataValid(formModel, businessKey, tablename);
                String formHtml =
                    formHandlerService.obtainHtml(bpmFormDef, processRun, userId, nodeId, ctxPath, parentActDefId);
                formHtml += CommonTools.null2String(bpmNodeSet.getInitScriptHandler());
                formModel.setFormHtml(formHtml);
                formModel.setHeadHtml(bpmFormDef.getHeadHtml());
                return formModel;
            }
        }
        
        if (bpmNodeSet == null)
            return formModel;
        // 获取在线表单
        if (BpmConst.OnLineForm.equals(bpmNodeSet.getFormType()))
        {
            bpmFormDef = dao.getDefaultPublishedByFormKey(bpmNodeSet.getFormKey());
            String bussinessKey = processRun.getBusinessKey();
            String tablename = "";
            FormTable bpmFormTable = formTableDao.getById(bpmFormDef.getTableId());
            if (BeanUtils.isNotEmpty(bpmFormTable))
            {
                tablename = bpmFormTable.getTableName();
            }
            // 验证表单数据是否有效。
            isDataValid(formModel, bussinessKey, tablename);
            
            String formHtml =
                formHandlerService.obtainHtml(bpmFormDef, processRun, userId, nodeId, ctxPath, parentActDefId);
            formModel.setFormHtml(formHtml);
        }
        else
        {
            // 获取流程实例ID
            String bussinessKey = processRun.getBusinessKey();
            String formUrl = bpmNodeSet.getFormUrl();
            String detailUrl = bpmNodeSet.getDetailUrl();
            if (StringUtil.isNotEmpty(formUrl))
            {
                
                formUrl = getFormUrl(formUrl, bussinessKey, variables, ctxPath);
                formModel.setFormUrl(formUrl);
                formModel.setFormType(BpmConst.UrlForm);
            }
            if (StringUtil.isNotEmpty(detailUrl))
            {
                detailUrl = getFormUrl(detailUrl, bussinessKey, variables, ctxPath);
                formModel.setDetailUrl(detailUrl);
            }
        }

        return formModel;
    }
    
    /**
     * 获取表单URL。
     * 
     * @param formUrl
     * @param bussinessKey
     * @param variables
     * @param ctxPath
     * @return
     */
    private String getFormUrl(String formUrl, String bussinessKey, Map<String, Object> variables, String ctxPath)
    {
        String url = formUrl;
        if (StringUtil.isNotEmpty(bussinessKey))
        {
            url = formUrl.replaceFirst(BpmConst.FORM_PK_REGEX, bussinessKey);
        }
        
        if (variables != null)
            url = getUrlByVariables(url, variables);
        if (!formUrl.startsWith("http"))
        {
            url = ctxPath + url;
        }
        return url;
    }
    
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
    public String getFormUrl(String taskId, Long defId, String nodeId, String businessKey, String ctxPath)
    {
        String formUrl = "";
        
        INodeSet nodeSet = nodeSetService.getByDefIdNodeId(defId, nodeId);
        if (nodeSet != null)
        {
            formUrl = nodeSet.getFormUrl();
        }
        if (StringUtil.isEmpty(formUrl))
        {
            INodeSet node = nodeSetService.getBySetType(defId, INodeSet.SetType_GloabalForm);
            formUrl = node.getFormUrl();
        }
        if (StringUtil.isEmpty(formUrl))
            return formUrl;
        formUrl = formUrl.replaceFirst(BpmConst.FORM_PK_REGEX, businessKey).replaceFirst("\\{taskId\\}", taskId);
        if (!formUrl.startsWith("http"))
        {
            formUrl = ctxPath + formUrl;
        }
        return formUrl;
        
    }
    
    /**
     * 替换地址 orderNo={orderNo}
     * 
     * @param url
     * @param variables
     * @return
     */
    private String getUrlByVariables(String url, Map<String, Object> variables)
    {
        Pattern regex = Pattern.compile("\\{(.*?)\\}");
        Matcher regexMatcher = regex.matcher(url);
        while (regexMatcher.find())
        {
            String toreplace = regexMatcher.group(0);
            String varName = regexMatcher.group(1);
            if (!variables.containsKey(varName))
                continue;
            url = url.replace(toreplace, variables.get(varName).toString());
        }
        return url;
    }
    
    /**
     * 取得发布的表单。
     * 
     * @param queryFilter
     * @return
     */
    public List<FormDef> getPublished(QueryFilter queryFilter)
    {
        return dao.getPublished(queryFilter);
    }
    
    public List<FormDef> getAllPublished(String formDesc)
    {
        return dao.getAllPublished(formDesc);
    }
    
    /**
     * 根据表单key获取默认的表单定义。
     * 
     * @param formKey
     * @return
     */
    public FormDef getDefaultPublishedByFormKey(Long formKey)
    {
        return dao.getDefaultPublishedByFormKey(formKey);
    }
    
    /**
     * 判断表单是否已经被使用。
     * 
     * @param formKey 表单key
     * @return
     */
    public int getFlowUsed(Long formKey)
    {
        int rtn = dao.getFlowUsed(formKey);
        return rtn;
    }
    
    /**
     * 根据formKey获取其关联的流程定义，结果返回 表单名，关联的流程定义（List<String>）
     * 
     * @param formKey
     * @return
     */
    public List<Object> getFormRelatedDef(Long formKey)
    {
        List<Object> result = new ArrayList<Object>();
        List<String> actDefName = new ArrayList<String>();
        List<? extends INodeSet> bpmNodeSetList = nodeSetService.getByFormKey(formKey);
        IDefinition bpmDefinition = null;
        for (INodeSet bpmNodeSet : bpmNodeSetList)
        {
            bpmDefinition = definitionService.getByActDefId(bpmNodeSet.getActDefId());
            actDefName.add(bpmDefinition.getSubject());
        }
        if (BeanUtils.isNotEmpty(bpmNodeSetList))
        {
            result.add(bpmNodeSetList.get(0).getFormDefName());
            result.add(actDefName);
        }
        return result;
    }
    /**
     * 根据FormDefId删除数据。
     * 
     * 
     * @param formDefId
     * @throws SQLException
     */
    public void delByFormDefId(Long formDefId)
        throws SQLException
    {
        dao.delByFormDefId(formDefId);
    }
    /**
     * 根据formkey删除数据。
     * 
     * <pre>
     *  如果表已经生成并且表单是通过设计器进行设计的那么将删除所创建的表。
     * </pre>
     * 
     * @param formKey
     * @throws SQLException
     */
    public void delByFormKey(Long formKey)
        throws SQLException
    {
        FormDef bpmFormDef = dao.getDefaultVersionByFormKey(formKey);
        Long tableId = bpmFormDef.getTableId();
        // 删除表单权限
        formRightsService.deleteByFormKey(formKey, true);
        // 先删除表单，后判断是否还有表单使用该表
        dao.delByFormKey(formKey);
        // 删除数据模版。
        dataTemplateDao.delByFormKey(formKey);
        // tableId大于零并且有表单生成。
        if (tableId > 0 && bpmFormDef.getDesignType() == 1)
        {
            FormTable bpmFormTable = formTableService.getTableById(tableId);
            // 是否还有表单使用该表
            boolean tableHasForm = dao.isTableHasFormDef(tableId);
            if (bpmFormTable != null && !tableHasForm)
            {
                formTableService.dropTable(bpmFormTable);
                formTableService.delTable(bpmFormTable);
            }
        }
        this.formDefTreeDao.delByFormDefKey(bpmFormDef.getFormKey());
    }
    
    /**
     * 保存表单。
     * 
     * <pre>
     *  1.表单输入新创建的表单。
     *      1.保存表单。
     *      
     *  2.表单未发布。
     *      1.保存表单。
     *      
     *  3.表单已经发布的情况，表单已经发布，数据库表已经创建。
     *      1.保存表单。
     *      2.表单是否有其他的表单定义情况。
     *          1.相同的表不止对应一个表单的情况，对表做更新处理。
     *          2.没有数据的情况，表删除重建。
     * </pre>
     * 
     * @param bpmFormdef
     * @param bpmFormTable
     * @throws Exception
     */
    public void saveForm(FormDef formDef, FormTable bpmFormTable, boolean isPublish)
        throws Exception
    {
        if (formDef.getFormDefId() == 0)
        {
            Long formDefId = UniqueIdUtil.genId();
            formDef.setFormDefId(formDefId);
            formDef.setFormKey(formDefId);
            formDef.setDesignType(FormDef.DesignType_CustomDesign);
            formDef.setIsDefault((short)1);
            formDef.setVersionNo(1);
            Long tableId = 0L;
            if (isPublish)
            {
                tableId = formTableService.saveTable(bpmFormTable);
                formDef.setIsPublished((short)1);
                formDef.setPublishTime(new Date());
            }
            else
            {
                formDef.setIsPublished((short)0);
                formDef.setPublishedBy("");
            }
            formDef.setTableId(tableId);
            dao.add(formDef);
        }
        else
        {
            // 当前为发布或者表单已经分布。
            if (isPublish || formDef.getIsPublished() == 1)
            {
                Long tableId = formDef.getTableId();
                bpmFormTable.setTableId(tableId);
                tableId = formTableService.saveTable(bpmFormTable);
                formDef.setTableId(tableId);
                formDef.setIsPublished((short)1);
                formDef.setPublishTime(new Date());
            }
            dao.update(formDef);
        }
        // 添加自定义表单修改历史
        FormDefHi formDefHi = new FormDefHi(formDef);
        formDefHiService.addHisRecord(formDefHi);
        
    }
    
    /**
     * 获取现有表单Id函数
     * 
     * @param nodeSet
     * @return
     */
    public Long getCurrentTableId(INodeSet nodeSet)
    {
        Long formId = 0L;
        FormDef bpmFormDef;
        // 节点挂钩表单不为空时取节点表单
        if (nodeSet.getFormType().equals(Short.parseShort("0")))
        {
            bpmFormDef = dao.getDefaultVersionByFormKey(nodeSet.getFormKey());
            if (bpmFormDef != null)
            {
                formId = bpmFormDef.getFormDefId();
            }
        }
        else
        { // 节点表单为空时取全局表单
            INodeSet globalForm = nodeSetService.getBySetType(nodeSet.getDefId(), INodeSet.SetType_GloabalForm);
            if (globalForm != null)
            {
                bpmFormDef = dao.getDefaultVersionByFormKey(globalForm.getFormKey());
                if (bpmFormDef != null)
                {
                    formId = bpmFormDef.getFormDefId();
                }
            }
        }
        return formId;
    }
    
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
        throws Exception
    {
        FormDefXmlList bpmFormDefXmls = new FormDefXmlList();
        List<FormDefXml> list = new ArrayList<FormDefXml>();
        for (int i = 0; i < formDefIds.length; i++)
        {
            FormDef formDef = (FormDef)this.dao.getById(formDefIds[i]);
            FormDefXml formDefXml = exportFormDef(formDef, FormDef.IS_DEFAULT, map);
            list.add(formDefXml);
        }
        bpmFormDefXmls.setFormDefXmlList(list);
        return XmlBeanUtil.marshall(bpmFormDefXmls, FormDefXmlList.class);

    }
    
    public Map<String, Boolean> getDefaultExportMap(Map<String, Boolean> map)
    {
        if (BeanUtils.isEmpty(map))
        {
            map = new HashMap<String, Boolean>();
            map.put("bpmFormDef", true);
            map.put("bpmFormTable", false);
            map.put("bpmFormDefOther", true);
            map.put("bpmFormRights", true);
            map.put("bpmTableTemplate", true);
            map.put("sysBusEvent", Boolean.valueOf(true));
            map.put("formDefTree", Boolean.valueOf(true));
        }
        return map;
    }
    
    /**
     * 导出表单的信息
     * 
     * @param bpmFormDef 表单
     * @param isDefault 是否是默认 默认则要导出其它表单和模板
     * @param map 是否导出的Map列表
     * @return
     */
    public FormDefXml exportFormDef(FormDef bpmFormDef, Short isDefault, Map<String, Boolean> map)
    {
        FormDefXml bpmFormDefXml = new FormDefXml();
        // 表单
        bpmFormDefXml.setFormDef(bpmFormDef);
        Long formDefId = bpmFormDef.getFormDefId();
        Long formKey = bpmFormDef.getFormKey();
        
        if (isDefault.shortValue() == FormDef.IS_DEFAULT.shortValue())
        {
            // 导出对应的表
            if (map.get("bpmFormTable"))
                exportFormTableXml(bpmFormDef, bpmFormDefXml);
            
            if (BeanUtils.isNotEmpty(formKey))
            {
                // 导出自定义表单 非默认版本
                if (map.get("bpmFormDefOther"))
                    exportFormDefOther(formKey, map, bpmFormDefXml);
                // 数据模板
                if (map.get("bpmTableTemplate"))
                    exportDataTemplate(formKey, bpmFormDefXml);
                
            }
        }
        
        if (BeanUtils.isNotEmpty(formDefId))
        {
            // 表单权限
            if (map.get("bpmFormRights"))
                exportFormRights(formDefId, bpmFormDefXml);
            if (((Boolean)map.get("sysBusEvent")).booleanValue())
            {
                exportSysBusEvent(formKey.toString(), bpmFormDefXml);
            }
            
            if (((Boolean)map.get("formDefTree")).booleanValue())
            {
                exportFormDefTree(formKey, bpmFormDefXml);
            }
            
        }
        return bpmFormDefXml;
    }
    
    private void exportSysBusEvent(String formKey, FormDefXml formDefXml)
    {
        BaseSysBusEvent sysBusEvent = (BaseSysBusEvent)this.sysBusEventService.getByFormKey(formKey);
        if (sysBusEvent != null)
        {
            formDefXml.setSysBusEvent(sysBusEvent);
        }
    }
    
    private void exportFormDefTree(Long formKey, FormDefXml formDefXml)
    {
        FormDefTree formDefTree = this.formDefTreeDao.getByFormKey(formKey);
        if (formDefTree != null)
            formDefXml.setFormDefTree(formDefTree);
    }
    
    /**
     * 导出对于的表
     * 
     * @param bpmFormDef
     * @param bpmFormDefXml
     */
    private void exportFormTableXml(FormDef bpmFormDef, FormDefXml bpmFormDefXml)
    {
        if (BeanUtils.isEmpty(bpmFormDef.getTableId()))
            return;
        if (bpmFormDef.getTableId() == 0 && bpmFormDef.getDesignType() == FormDef.DesignType_CustomDesign)
            return;
        
        FormTable formTable = formTableService.getById(bpmFormDef.getTableId());
        FormTableXml bpmFormTableXml = formTableService.exportTable(formTable, null);
        bpmFormDefXml.setFormTableXml(bpmFormTableXml);
    }
    
    /**
     * 导出其它版本的自定义表单
     * 
     * @param formKey
     * @param map
     * @param bpmFormDefXml
     */
    private void exportFormDefOther(Long formKey, Map<String, Boolean> map, FormDefXml bpmFormDefXml)
    {
        List<FormDef> formDefList = dao.getByFormKeyIsDefault(formKey, FormDef.IS_NOT_DEFAULT);
        if (BeanUtils.isEmpty(formDefList))
            return;
        
        List<FormDefXml> list = new ArrayList<FormDefXml>();
        for (FormDef formDef : formDefList)
        {
            FormDefXml formDefXml = exportFormDef(formDef, FormDef.IS_NOT_DEFAULT, map);
            list.add(formDefXml);
        }
        bpmFormDefXml.setFormDefXmlList(list);
    }
    
    /**
     * 导出表单权限
     * 
     * @param formDefId
     * @param formDefXml
     */
    private void exportFormRights(Long formDefId, FormDefXml formDefXml)
    {
        List<FormRights> formRightsList = formRightsDao.getByFormDefId(formDefId);
        if (BeanUtils.isNotEmpty(formRightsList))
            formDefXml.setFormRightsList(formRightsList);
    }
    
    /**
     * 处理人员
     * 
     * @param bpmFormRightsList
     * @return
     */
    public List<FormRights> exportFormRightsUser(List<FormRights> bpmFormRightsList)
    {
        List<FormRights> formRightsList = new ArrayList<FormRights>();
        // 处理人员
        for (FormRights bpmFormRights : bpmFormRightsList)
        {
            String permission = bpmFormRights.getPermission();
            bpmFormRights.setPermission(parsePermission(permission, true));
            formRightsList.add(bpmFormRights);
        }
        return formRightsList;
    }
    
    /**
     * 导出数据模板
     * 
     * @param formKey
     * @param formDefXml
     */
    private void exportDataTemplate(Long formKey, FormDefXml formDefXml)
    {
        DataTemplate dataTemplate = dataTemplateDao.getByFormKey(formKey);
        if (dataTemplate != null)
            formDefXml.setDataTemplate(dataTemplate);
        
    }
    
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
        throws Exception
    {
        Document doc = Dom4jUtil.loadXml(inputStream);
        Element root = doc.getRootElement();
        
        checkXMLFormat(root);
        
        String xmlStr = root.asXML();
        FormDefXmlList bpmFormDefXmlList = (FormDefXmlList)XmlBeanUtil.unmarshall(xmlStr, FormDefXmlList.class);
        //FormDefXmlList bpmFormDefXmlList = (FormDefXmlList)XmlBeanUtil.unmarshall(inputStream, FormDefXmlList.class);
        logger.info(root.asXML());
        List<FormDefXml> list = bpmFormDefXmlList.getFormDefXmlList();
        for (FormDefXml formDefXml : list)
        {
            logger.info(formDefXml.getFormDef().getHtml());
            StringEscapeUtils.escapeXml(formDefXml.getFormDef().getHtml());
            logger.info(StringEscapeUtils.unescapeXml(formDefXml.getFormDef().getHtml()));
            importFormDef(formDefXml);
            MsgUtil.addSplit();
        }
    }
    
    /**
     * 检查XML格式是否正确
     * 
     * @param root
     * @param msg
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void checkXMLFormat(Element root)
        throws Exception
    {
        String msg = "导入文件格式不对";
        if (!root.getName().equals("form"))
            throw new Exception(msg);
        List<Element> itemLists = root.elements();
        for (Element elm : itemLists)
        {
            if (!elm.getName().equals("formDefs"))
                throw new Exception(msg);
        }
    }
    
    /**
     * 导入表单信息
     * 
     * @param formDefXml
     * @param formKey
     * @param formKey
     * @param isDefault
     * @return
     * @throws Exception
     */
    public Map<String, Map<Long, Long>> importFormDef(FormDefXml formDefXml)
        throws Exception
    {
        Set<ISerialNumber> identitySet = new HashSet<ISerialNumber>();  
        // 导入自定义表
        Map<Long, Long> map = this.importFormTable(formDefXml.getFormTableXml(), identitySet);
        // 流水号
        formTableService.importSerialNumber(identitySet);
        
        FormDef bpmFormDef = formDefXml.getFormDef();
        Long origFormKey = bpmFormDef.getFormKey();
        Long origFormDefId = bpmFormDef.getFormDefId();
        // 导入表单信息
        bpmFormDef = this.importFormDef(bpmFormDef, map);
        Long formKey = bpmFormDef.getFormKey();
        Long formDefId = bpmFormDef.getFormDefId();
        
        // // 导入 表单模板
        DataTemplate dataTemplate = formDefXml.getDataTemplate();
        if (BeanUtils.isNotEmpty(dataTemplate))
        {
            this.importDataTemplate(dataTemplate, formKey);
        }
        // 表单权限
        List<FormRights> bpmFormRightsList = formDefXml.getFormRightsList();
        if (BeanUtils.isNotEmpty(bpmFormRightsList))
        {
            for (FormRights bpmFormRights : bpmFormRightsList)
            {
                this.importmFormRights(bpmFormRights, formKey);
            }
        }
        
        // 业务保存设置
        ISysBusEvent sysBusEvent = formDefXml.getSysBusEvent();
        if (BeanUtils.isNotEmpty(sysBusEvent))
        {
            this.sysBusEventService.importSysBusEvent(sysBusEvent, formKey);
        }
        
        // 树结构设置
        FormDefTree defTree = formDefXml.getFormDefTree();
        if (BeanUtils.isNotEmpty(defTree))
        {
            importFormDefTree(defTree);
        }
        
        Map<String, Map<Long, Long>> defMap = new HashMap<String, Map<Long, Long>>();
        Map<Long, Long> keyMap = new HashMap<Long, Long>();
        Map<Long, Long> idMap = new HashMap<Long, Long>();
        
        idMap.put(origFormDefId, formDefId);
        keyMap.put(origFormKey, formKey);
        defMap.put("id", idMap);
        defMap.put("key", keyMap);
        return defMap;
        
    }
    
    /**
     * 树结构设置
     * 
     * @param formDefTree
     */
    private void importFormDefTree(FormDefTree formDefTree)
    {
        FormDefTree defTree = (FormDefTree)this.formDefTreeDao.getById(formDefTree.getId());
        if (BeanUtils.isNotEmpty(defTree))
        {
            this.formDefTreeDao.update(defTree);
            MsgUtil.addMsg(2, " 数据模板已经存在,数据模板ID：" + defTree.getName() + ",已经存在，该数据模板进行更新!");
            return;
        }
        this.formDefTreeDao.add(defTree);
        MsgUtil.addMsg(1, defTree.getName() + " 数据模板成功导入!");
    }
    
    /**
     * 导入自定义表
     * 
     * @param formTableXml
     * @param identitySet
     * @throws Exception
     */
    private Map<Long, Long> importFormTable(FormTableXml formTableXml, Set<ISerialNumber> serialSet)
        throws Exception
    {
        Map<Long, Long> map = new HashMap<Long, Long>();
        
        if (BeanUtils.isNotEmpty(formTableXml))
        {
            map = formTableService.importFormTableXml(formTableXml);
            formTableService.setSerialNumber(formTableXml.getSerialNumberList(), serialSet);
        }
        return map;
    }
    
    /**
     * 导入的表单信息保存
     * 
     * @param bpmFormDef
     * @param map
     * @param tableMap
     * @param msg
     * @return
     */
    private FormDef importFormDef(FormDef bpmFormDef, Map<Long, Long> map)
        throws Exception
    {
        Long formKey = bpmFormDef.getFormKey();
        Integer maxVersion = dao.getMaxVersionByFormKey(formKey);
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        bpmFormDef.setHtml(StringUtil.convertScriptLine(bpmFormDef.getHtml(), false));
        bpmFormDef.setTemplate(StringUtil.convertScriptLine(bpmFormDef.getTemplate(), false));
        // 设置tableId
        this.setTableId(bpmFormDef, map);
        
        // 设置分类
        this.setCategoryId(bpmFormDef);
        
        bpmFormDef.setPublishTime(new Date());
        bpmFormDef.setCreatetime(new Date());
        bpmFormDef.setPublishedBy(sysUser.getFullname());
        bpmFormDef.setCreateBy(sysUser.getUserId());
        Object[] args = {bpmFormDef.getSubject()};
        if (BeanUtils.isEmpty(maxVersion) || maxVersion.intValue() == 0)
        {
            FormDef def = dao.getById(bpmFormDef.getFormDefId());
            if (BeanUtils.isNotEmpty(def))
                bpmFormDef.setFormDefId(UniqueIdUtil.genId());
            
            bpmFormDef.setVersionNo(1);
            // bpmFormDef.setIsPublished(FormDef.IS_PUBLISHED);
            bpmFormDef.setPublishedBy(sysUser.getFullname());
            bpmFormDef.setPublishTime(new Date());
            bpmFormDef.setCreatetime(new Date());
            bpmFormDef.setCreateBy(sysUser.getUserId());
            dao.add(bpmFormDef);
            MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmFormDef.import.bpmFormDef.isNew", args));
        }
        else
        {
            bpmFormDef.setFormDefId(UniqueIdUtil.genId());
            bpmFormDef.setVersionNo(maxVersion + 1);
            bpmFormDef.setIsDefault(FormDef.IS_NOT_DEFAULT);
            // bpmFormDef.setIsPublished(FormDef.IS_PUBLISHED);
            
            dao.add(bpmFormDef);
            MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmFormDef.import.bpmFormDef.isExist", args));
        }
        return bpmFormDef;
        
    }
    
    /**
     * 设置表ID
     * 
     * @param bpmFormDef
     * @param map
     * @param tableMap
     * @return
     */
    private void setTableId(FormDef bpmFormDef, Map<Long, Long> map)
    {
        if (BeanUtils.isEmpty(bpmFormDef.getTableId()))
            return;
        Long tableId = bpmFormDef.getTableId();
        if (BeanUtils.isNotEmpty(map))
        {
            Long origTableId = map.get(tableId);
            if (BeanUtils.isNotEmpty(origTableId))
            {
                tableId = origTableId;
            }
        }
        
        FormTable bpmFormTable = formTableService.getById(tableId);
        if (BeanUtils.isEmpty(bpmFormTable))
            bpmFormDef.setTableId(null);
        
    }
    
    /**
     * 导入的表单信息保存
     * 
     * @param bpmFormDef
     * @param tableMap
     * @param msg
     * @return
     */
    @SuppressWarnings("unused")
    private void importFormDef(FormDef bpmFormDef)
        throws Exception
    {
        // 设置分类
        this.setCategoryId(bpmFormDef);
        // 设置tableId
        this.setTableId(bpmFormDef);
        bpmFormDef.setIsPublished(FormDef.IS_NOT_PUBLISHED);
        bpmFormDef.setPublishedBy(null);
        bpmFormDef.setPublishTime(null);
        dao.add(bpmFormDef);
        MsgUtil.addMsg(MsgUtil.SUCCESS,
            getText("service.bpmFormDef.import.bpmFormDef.isNew", new Object[] {bpmFormDef.getSubject()}));
    }
    
    /**
     * 设置表ID
     * 
     * @param bpmFormDef
     * @param tableMap
     * @return
     */
    private void setTableId(FormDef bpmFormDef)
    {
        if (BeanUtils.isEmpty(bpmFormDef.getTableId()))
            return;
        
        FormTable bpmFormTable = formTableService.getById(bpmFormDef.getTableId());
        if (BeanUtils.isEmpty(bpmFormTable))
            bpmFormDef.setTableId(null);
        
    }
    
    /**
     * 设置分类
     * 
     * @param formDef
     * @return
     */
    private void setCategoryId(FormDef formDef)
    {
        if (BeanUtils.isEmpty(formDef.getCategoryId()))
            return;
        IGlobalType globalType = globalTypeService.getById(formDef.getCategoryId());
        if (BeanUtils.isEmpty(globalType))
            formDef.setCategoryId(null);
        
    }
    
    /**
     * 保存 表单权限
     * 
     * @param bpmFormRights
     * @param formDefId
     * @param msg
     * @return
     */
    private void importmFormRights(FormRights bpmFormRights, Long formDefId)
        throws Exception
    {
        FormRights formRights = formRightsDao.getById(bpmFormRights.getId());
        Object[] args = {bpmFormRights.getName()};
        if (BeanUtils.isNotEmpty(formRights))
        {
            formRightsDao.update(bpmFormRights);
            MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmFormDef.import.bpmFormRights.isExist", args));
            // MsgUtil.addMsg(MsgUtil.WARN, "表单权限已经存在,表单权限ID："+bpmFormRights.getId()+",该记录终止导入!");
            return;
        }
        bpmFormRights.setFormDefId(formDefId);
        formRightsDao.add(bpmFormRights);
        MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmFormDef.import.bpmFormRights.isNew", args));
    }
    
    /**
     * 保存 数据模板
     * 
     * @param bpmTableTemplate
     * @param long1
     * @param msg
     * @return
     */
    private void importDataTemplate(DataTemplate bpmTableTemplate, Long formKey)
        throws Exception
    {
        DataTemplate tableTemplate = dataTemplateDao.getById(bpmTableTemplate.getId());
        if (BeanUtils.isNotEmpty(tableTemplate))
        {
            dataTemplateDao.update(bpmTableTemplate);
            MsgUtil.addMsg(MsgUtil.WARN,
                getText("service.bpmFormDef.import.bpmDataTemplate.isExist", new Object[] {tableTemplate.getId()}));
            return;
        }
        bpmTableTemplate.setFormKey(formKey);
        dataTemplateDao.add(bpmTableTemplate);
        MsgUtil.addMsg(MsgUtil.SUCCESS,
            getText("service.bpmFormDef.import.bpmDataTemplate.isNew", new Object[] {bpmTableTemplate.getAlias()}));
    }
    
    public void updCategory(Long categoryId, List<Long> formKeyList)
    {
        dao.updCategory(categoryId, formKeyList);
    }
    
    /**
     * 根据流程定义ID，取得Table ID
     * 
     * @param defId 流程定义id
     * @return
     */
    public Long getTableIdByDefId(Long defId)
    {
        List<FormDef> bpmFormDefs = dao.getByDefId(defId);
        if (BeanUtils.isNotEmpty(bpmFormDefs))
        {
            return bpmFormDefs.get(0).getTableId();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 根据流程定义ID，节点ID，取得流程开始表单定义。 在节点没有设置表单时，如果cascade为true，则会查询全局表单和开始表单
     * 
     * @param actDefId
     * @param nodeId
     * @param cascade 是否向上查找标志
     * @return
     */
    public FormDef getNodeFormDef(String actDefId, String nodeId, boolean cascade)
    {
        List<FormDef> defs = dao.getByActDefIdAndNodeId(actDefId, nodeId);
        if (BeanUtils.isNotEmpty(defs))
        {
            return defs.get(0);
        }
        
        if (!cascade)
        {
            return null;
        }
        
        FormDef def = this.getGlobalFormDef(actDefId);
        if (def != null)
        {
            return def;
        }
        
        return def;
        
    }
    
    /**
     * 根据流程定义ID，取得流程全局表单定义
     * 
     * @param actDefId
     * @return
     */
    public FormDef getGlobalFormDef(String actDefId)
    {
        List<FormDef> defs = dao.getByActDefIdAndSetType(actDefId, INodeSet.SetType_GloabalForm);
        if (BeanUtils.isNotEmpty(defs))
        {
            return defs.get(0);
        }
        else
        {
            return null;
        }
    }
    
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
    public FormModel getForm(IProcessRun processRun, Long userId, String ctxPath, Map<String, Object> variables)
        throws Exception
    {
        Long defId = processRun.getDefId();
        Short formType = processRun.getFormType();
        String formKeyUrl = processRun.getFormKeyUrl();
        FormModel formModel = new FormModel();
        String parentActDefId = (String)variables.get("parentActDefId");
        
        // processRun中保存了流程启动时的表单key或者明细表单url
        if (BeanUtils.isNotEmpty(formType))
        {
            if (BpmConst.OnLineForm.equals(formType))
            {
                Long formKey = Long.parseLong(formKeyUrl);
                FormDef bpmFormDef = dao.getDefaultPublishedByFormKey(formKey);
                if (BeanUtils.isEmpty(bpmFormDef))
                    throw new Exception(getText("service.bpmFormDef.getForm.notGetForm"));
                String pkValue = processRun.getBusinessKey();
                FormTable bpmFormTable = formTableDao.getById(bpmFormDef.getTableId());
                String tableName = bpmFormTable.getFactTableName();
                bpmFormDef.setTableName(tableName);
                isDataValid(formModel, pkValue, tableName);
                if (formModel.isValid())
                {
                    String formHtml =
                        formHandlerService.obtainHtml(bpmFormDef, processRun, 0L, "", ctxPath, parentActDefId);
                    formModel.setFormHtml(formHtml);
                }
            }
            else
            {
                String bussinessKey = processRun.getBusinessKey();
                if (StringUtil.isNotEmpty(formKeyUrl) && StringUtil.isNotEmpty(bussinessKey))
                {
                    formKeyUrl = getFormUrl(formKeyUrl, bussinessKey, variables, ctxPath);
                    formModel.setDetailUrl(formKeyUrl);
                }
            }
        }
        // processRun中没有保存表单key时 获取流程的全局表单或开始节点表单来显示表单数据
        else
        {
            INodeSet bpmNodeSet = null;
            if (StringUtil.isEmpty(parentActDefId))
                bpmNodeSet = nodeSetService.getBySetType(defId, INodeSet.SetType_GloabalForm);
            else
            {
                bpmNodeSet = nodeSetService.getBySetType(defId, INodeSet.SetType_GloabalForm, parentActDefId);
            }
            if (bpmNodeSet == null)
                logger.info("temp");
            if (bpmNodeSet != null)
            {
                if (BpmConst.OnLineForm.equals(bpmNodeSet.getFormType()))
                {
                    String bussinessKey = processRun.getBusinessKey();
                    FormDef bpmFormDef = dao.getDefaultPublishedByFormKey(bpmNodeSet.getFormKey());
                    FormTable bpmFormTable = formTableDao.getById(bpmFormDef.getTableId());
                    String tableName = bpmFormTable.getTableName();
                    isDataValid(formModel, bussinessKey, tableName);
                    if (formModel.isValid())
                    {
                        String formHtml =
                            formHandlerService.obtainHtml(bpmFormDef, processRun, 0L, "", ctxPath, parentActDefId);
                        formModel.setFormHtml(formHtml);
                    }
                }
                // URL表单情况
                else
                {
                    String bussinessKey = processRun.getBusinessKey();
                    String formUrl = bpmNodeSet.getFormUrl();
                    String detailUrl = bpmNodeSet.getDetailUrl();
                    if (StringUtil.isNotEmpty(formUrl) && StringUtil.isNotEmpty(bussinessKey))
                    {
                        formUrl = getFormUrl(formUrl, bussinessKey, variables, ctxPath);
                        formModel.setFormUrl(formUrl);
                        formModel.setFormType(BpmConst.UrlForm);
                    }
                    if (StringUtil.isNotEmpty(detailUrl) && StringUtil.isNotEmpty(bussinessKey))
                    {
                        detailUrl = getFormUrl(detailUrl, bussinessKey, variables, ctxPath);
                        formModel.setDetailUrl(detailUrl);
                    }
                }
            }
        }
        return formModel;
    }
    
    /**
     * 提交的主键和表名确定数据表单是否有效。
     * 
     * <pre>
     * 1.获取当前节点的表单数据对应的表。
     * 2.如果主键存在的情况。
     * 3.那么根据主键去这个表中获取数据。
     * 如果能够获取到数据说明表单没有更换。否则说明表单已经变更。
     * </pre>
     * 
     * @param formModel
     * @param pkValue 主键
     * @param tableName 表名
     */
    private void isDataValid(FormModel formModel, String pkValue, String tableName)
    {
        // 判断业务主键是否为空。
        if (StringUtil.isEmpty(pkValue) || StringUtil.isEmpty(tableName))
            return;
        long pk = Long.parseLong(pkValue);
        boolean rtn = formHandlerService.isHasDataByPk(TableModel.CUSTOMER_TABLE_PREFIX + tableName, pk);
        formModel.setValid(rtn);
        
    }
    
    /**
     * 判断自定义表是否绑定了表单
     * 
     * @param tableId 自定义表ID
     * @return <code>true</code> 绑定了表单<br/>
     *         <code>false</code> 末绑定表单
     */
    public boolean isTableHasFormDef(Long tableId)
    {
        return dao.isTableHasFormDef(tableId);
    }
    
    @Override
    public void importFormDef(List<? extends IFormDefXml> bpmFormDefXmlList, List<Map<Long, Long>> mapFormKeyList,
        List<Map<Long, Long>> mapFormIdList)
        throws Exception
    {
        if (BeanUtils.isNotEmpty(bpmFormDefXmlList))
        {
            for (IFormDefXml bpmFormDefXml : bpmFormDefXmlList)
            {
                Map<String, Map<Long, Long>> mapForm = this.importFormDef((FormDefXml)bpmFormDefXml);
                Map<Long, Long> mapFormKey = mapForm.get("key");
                mapFormKeyList.add(mapFormKey);
                Map<Long, Long> mapFormId = mapForm.get("id");
                mapFormIdList.add(mapFormId);
            }
        }
    }
    
    /**
     * 根据actDefId 获取表单权限。
     * 
     * @param actDefId
     * @return
     */
    @Override
    public List<? extends BaseFormRights> exportFormRightsUser(String actDefId)
    {
        List<FormRights> bpmFormRightsList = formRightsService.getFormRightsByActDefId(actDefId);
        return this.exportFormRightsUser(bpmFormRightsList);
    }
    
    /**
     * 
     * @param formKey
     * @param isDefault
     * @return
     */
    @Override
    public List<FormDefXml> getFormDefXmlList(Map<String, Boolean> map, Set<Long> formKeySet, Set<Long> tableIdSet)
    {
        List<FormDefXml> formDefXmlList = new ArrayList<FormDefXml>();
        for (Long formKey : formKeySet)
        {
            // 设置自定义表单
            List<FormDef> bpmFormDefList = this.dao.getByFormKeyIsDefault(formKey, IFormDef.IS_DEFAULT);
            if (BeanUtils.isNotEmpty(bpmFormDefList))
            {
                FormDef bpmFormDef = bpmFormDefList.get(0);
                FormDefXml bpmFormDefXml = this.exportFormDef(bpmFormDef, IFormDef.IS_DEFAULT, map);
                formDefXmlList.add(bpmFormDefXml);
                // 关联的自定义表ID
                if (BeanUtils.isNotEmpty(bpmFormDef) && BeanUtils.isNotEmpty(bpmFormDef.getTableId()))
                {
                    tableIdSet.add(bpmFormDef.getTableId());
                }
            }
        }
        return formDefXmlList;
    }

	/**
	 * 检查表单设计别名是否唯一
	 * 
	 * @param alias
	 * @return xiechen
	 */
	/*
	 * public boolean isExistAlias(String alias) { List<FormDef> list =
	 * dao.getByFormAlias(alias); if (BeanUtils.isEmpty(list)) { return false; }
	 * return true; }
	 */
	public boolean isExistAlias(String alias) {
		FormDef formdef = dao.getByFormAlias(alias);
		if (BeanUtils.isEmpty(formdef)) {
			return false;
		}
		return true;
	}

	/**
	 * 表单设计 xml to bean xiechen
	 */
	public  void saveFromXML(MultipartFile file,StringBuffer log) throws Exception {
		try{
			log.append("------------------开始导入表单设计------------------\r\n");
			Map<String, List> map = XmlToBeanUtil.xmlToFormDef(file);
			for (int i = 0; i < map.get("formdef").size(); i++) {
				// 根据别名的唯一性来判断进行添加还是更新操作
				FormDef fd = (FormDef) map.get("formdef").get(i);
				DataTemplate dt = (DataTemplate) map.get("dataTemplate").get(i);
				String alias = fd.getFormAlias();
				boolean isExist = this.isExistAlias(alias);
				if (isExist) {
					log.append(i+1+"、表单设计别名--》"+alias+"：更新！\r\n");
					FormDef fd2 = dao.getByFormAlias(alias);
					fd2.setSubject(fd.getSubject());
					fd2.setFormAlias(fd.getFormAlias());
					fd2.setCategoryId(fd.getCategoryId());
					fd2.setFormDesc(fd.getFormDesc());
					//对 tableId 与 TemplatesId 特殊处理
					FormTable formTable = formTableDao.getByTableName(fd.getTableName());
					if(BeanUtils.isEmpty(formTable)){
						log.append(i+1+"、业务表别名--》"+fd.getTableName()+"：不存在！\r\n");
						break;
					}
					fd2.setTableId(formTable.getTableId());//根据tableId获取tableName
					fd2.setTemplatesId(fd2.getTableId()+","+fd.getTemplatesId());
					fd2.setIsDefault(fd.getIsDefault());
					fd2.setIsPublished(fd.getIsPublished());
					fd2.setVersionNo(fd.getVersionNo());
					fd2.setTabTitle(fd.getTabTitle());
					fd2.setPublishedBy(UserContextUtil.getCurrentUser()
							.getFullname());
					fd2.setPublishTime(DateUtil.getCurrentDate());
					Long formkey = fd2.getFormKey();
					dao.update(fd2);
					// 进行datatemplate更新(两张表格之间通过formkey关联)
					DataTemplate dt2 = dataTemplateDao.getByFormKey(formkey);
					if (!BeanUtils.isEmpty(dt2)) {
						dt2.setNeedPage(dt.getNeedPage());
						dt2.setPageSize(dt.getPageSize());
						dt2.setIsQuery(dt.getIsQuery());
						dt2.setIsFilter(dt.getIsFilter());
						dt2.setIsBakData(dt.getIsBakData());
						dt2.setTemplateAlias(dt.getTemplateAlias());
						dt2.setDisplayField(dt.getDisplayField());
						dt2.setConditionField(dt.getConditionField());
						dt2.setSortField(dt.getSortField());
						dt2.setManageField(dt.getManageField());
						dt2.setFilterField(dt.getFilterField());
						dt2.setExportField(dt.getExportField());
						dataTemplateDao.update(dt2);
					} else {
						dt.setId(UniqueIdUtil.genId());
						dt.setTableId(formTable.getTableId());
						dt.setFormKey(fd.getFormKey());
						dataTemplateDao.add(dt);
					}
				} else {
					// 进行添加操作
					//根据分类别名
					log.append(i+1+"、表单设计别名--》"+alias+"：新增！\r\n");
					IGlobalType gl = globalTypeService.getByCateKeyAndNodeKey(IGlobalType.CAT_FORM,fd.getCategoryName());
					Long categoryId = BeanUtils.isEmpty(gl)?0L:gl.getTypeId();
					FormTable formTable2 = formTableDao.getByTableName(fd.getTableName());
					if(BeanUtils.isEmpty(formTable2)){
						log.append(i+1+"、业务表别名--》"+fd.getTableName()+"：不存在！\r\n");
						break;
					}					
					fd.setTableId(formTable2.getTableId());
					fd.setTemplatesId(formTable2.getTableId()+","+fd.getTemplatesId());
					fd.setFormDefId(UniqueIdUtil.genId());// 系统自动生成
					fd.setFormKey(fd.getFormDefId());
					fd.setCategoryId(categoryId);
					Long[] tableIds = new Long[1];
					tableIds[0] = fd.getTableId();
					String[] templateAlias = new String[1];
					templateAlias[0] = fd.getTemplatesId().split(",")[1];
					String nodeType = null;
					String reult = this.genTemplate(tableIds, templateAlias,nodeType);
					fd.setHtml(reult);
					String html = fd.getHtml();
					html=html.replace("？", "");
					String template=FormUtil.getFreeMarkerTemplate(html,fd.getTableId());	
					fd.setTemplate(template);
					dao.add(fd);
					dt.setId(UniqueIdUtil.genId());
					dt.setTableId(fd.getTableId());
					dt.setFormKey(fd.getFormKey());
					dataTemplateDao.add(dt);
				}
			}
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	/**
	 * 根据表和指定的html生成表单。
	 * @param tableIds
	 * @param templateAlias
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public String genTemplate(Long[] tableIds, String[] templateAlias,
			String nodeType) throws TemplateException, IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tableIds.length; i++) {
			// 表
			Map<String, Object> fieldsMap = new HashMap<String, Object>();
			FormTable table = formTableService.getById(tableIds[i]);
			List<FormField> fields = formFieldService.getByTableId(tableIds[i]);
			// 删除主键外键字段。
			FormTableUtil.removeField(table, fields);
			FormFieldUtil.removeRelField(table, fields);
			fieldsMap.put("table", table);
			fieldsMap.put("fields", fields);
			// 设置主表和子表分组
			FormFieldUtil.setTeamFields(fieldsMap, table, fields, nodeType);
			// 设置 字段名称 【关系表字段前缀（r），主表字段前缀（m），子表字段前缀（s）】:【表名】:【字段名】
			FormFieldUtil.setFormFieldName(table, fields, nodeType);
			// 根据模版别名取得模版
			FormTemplate tableTemplate = formTemplateService
					.getByTemplateAlias(templateAlias[i]);
			// 根据宏模板别名取得模版
			FormTemplate macroTemplate = formTemplateService
					.getByTemplateAlias(tableTemplate.getMacroTemplateAlias());
			// logger.info(macroTemplate.getHtml());
			String macroHtml = "";
			if (macroTemplate != null) {
				macroHtml = macroTemplate.getHtml();
			}
			// a标签国际化
			/*
			 * FormLanguage formLanguage = FormLanguage.getFormLanguage();
			 * fieldsMap.put("formLanguage", formLanguage);
			 */
			// 根据"参数"和"freemarker字符串模版"解析出内容
			String result = freemarkEngine.parseByStringTemplate(fieldsMap,
					macroHtml + tableTemplate.getHtml());
			if (StringUtils.isNotEmpty(nodeType) && nodeType.equals("rel")) {
				sb.append("<p><br/></p>");
				sb.append("<div type=\"reltable\" tableName=\"");
				sb.append(table.getTableName());
				sb.append("\">\n");
				sb.append(result);
				sb.append("</div>\n");
			} else if (table.getIsMain() == 1) {
				sb.append(result);
			} else {
				sb.append("<p><br/></p>");
				sb.append("<div type=\"subtable\" tableName=\"");
				sb.append(table.getTableName());
				sb.append("\">\n");
				sb.append(result);
				sb.append("</div>\n");
			}
		}
		return sb.toString();
	}
}
