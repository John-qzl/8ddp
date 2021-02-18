package com.cssrc.ibms.core.flow.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 对象功能:自定义工具条 Model对象 
 * <pre>
 * 启动节点按钮类型对应数字：
 * 启动流程：1
 * 流程图     ：2
 * 打印          ：3
 * 保存表单：6
 * Web签章：14
 * 手写签章：15
 * 
 * 其他节点按钮类型对应数字：
 * 同意：1
 * 反对：2
 * 弃权：3
 * 驳回：4
 * 驳回到发起人：5
 * 交办：6
 * 补签：7
 * 保存表单：8
 * 流程图：9
 * 打印：10
 * 审批历史：11
 * Web签章：14
 * 手写签章：15
 * 沟通意见:16
 * 加签：17
 * 终止流程：18
 * </pre> 
 */
@XmlRootElement(name = "nodeButton")
@XmlAccessorType(XmlAccessType.NONE)
public class NodeButton extends BaseModel
{
    
    public NodeButton()
    {
        
    }
    
    public NodeButton(String actdefid, Long defId, String nodeid, String btnname, Integer operatortype,
        Integer nodetype)
        throws Exception
    {
        this.id = UniqueIdUtil.genId();
        this.actdefid = actdefid;
        this.defId = defId;
        this.nodeid = nodeid;
        this.btnname = btnname;
        this.operatortype = operatortype;
        this.nodetype = nodetype;
        this.isstartform = 0;
        this.sn = this.id;
    }
    
    public NodeButton(String actdefid, Long defId, String btnname, Integer operatortype)
        throws Exception
    {
        this.id = UniqueIdUtil.genId();
        this.actdefid = actdefid;
        this.defId = defId;
        this.btnname = btnname;
        this.operatortype = operatortype;
        this.isstartform = 1;
        this.sn = this.id;
    }
    
    // 开始按钮
    
    /**
     * 启动流程(提交)
     */
    public final static int START_BUTTON_TYPE_START = 1;
    
    /**
    * 流程示意图
    */
    public final static int START_BUTTON_TYPE_IMAGE = 2;
    
    /**
     * 打印
     */
    public final static int START_BUTTON_TYPE_PRINT = 3;
    
    /**
     * 保存草稿
     */
    public final static int START_BUTTON_TYPE_SAVE = 6;
    
    // end 开始按钮
    
    // 任务按钮
    /**
     * 同意
     */
    public final static int NODE_BUTTON_TYPE_COMPLETE = 1;
    
    /**
     * 反对
     */
    public final static int NODE_BUTTON_TYPE_OPPOSE = 2;
    
    /**
     * 弃权
     */
    public final static int NODE_BUTTON_TYPE_ABSTENT = 3;
    
    /**
     * 驳回
     */
    public final static int NODE_BUTTON_TYPE_BACK = 4;
    
    /**
     * 驳回到发起人
     */
    public final static int NODE_BUTTON_TYPE_BACKTOSTART = 5;
    
    /**
     * 转办
     */
    public final static int NODE_BUTTON_TYPE_ASSIGNEE = 6;
    
    /**
     * 补签
     */
    public final static int NODE_BUTTON_TYPE_ADDSIGN = 7;
    
    /**
     * 保存表单
     */
    public final static int NODE_BUTTON_TYPE_SAVEFORM = 8;
    
    /**
     * 流程示意图
     */
    public final static int NODE_BUTTON_TYPE_IMAGE = 9;
    
    /**
     * 打印
     */
    public final static int NODE_BUTTON_TYPE_PRINT = 10;
    
    /**
     * 审批历史
     */
    public final static int NODE_BUTTON_TYPE_HIS = 11;
    
    /**
     * Web签章
     */
    public final static int NODE_BUTTON_TYPE_WEBSIGN = 14;
    
    /**
     * 手写签章
     */
    public final static int NODE_BUTTON_TYPE_HANDSIGN = 15;
    
    /**
     * 沟通
     */
    public final static int NODE_BUTTON_TYPE_COMMU = 16;
    
    /**
     * 加签
     */
    public final static int NODE_BUTTON_TYPE_PLUSSIGN = 17;
    
    /**
     * 简体中文
     */
    public final static int LANG_ZH_CN = 0;
    
    /**
     * 繁体中文
     */
    public final static int LANG_ZH_TW = 1;
    
    /**
     * English
     */
    public final static int LANG_EN_US = 2;
    
    /**
     *  主键
     */
    @XmlAttribute
    protected Long id = 0L;
    
    /**
     *  流程定义ID
     */
    @XmlAttribute
    protected String actdefid;
    
    /**
     * 流程定义ID
     */
    @XmlAttribute
    protected Long defId = 0L;
    
    /**
     *  流程启动表单 0,启动表单1,任务表单
     */
    @XmlAttribute
    protected Integer isstartform = 0;
    
    /**
     *  节点ID
     */
    @XmlAttribute
    protected String nodeid;
    
    /**
     *  按钮名称
     */
    @XmlAttribute
    protected String btnname;
    
    /**
     *  图标样式
     */
    @XmlAttribute
    protected String iconclsname;
    
    /**
     *  操作类型
     */
    @XmlAttribute
    protected Integer operatortype = 0;
    
    /**
     * 参数脚本
     */
    @XmlAttribute
    protected String paramscript;
    
    /**
     *  前置脚本
     */
    @XmlAttribute
    protected String prevscript;
    
    /**
     *  后置脚本
     */
    @XmlAttribute
    protected String afterscript;
    
    /**
     *  节点类型 0，普通节点1，会签节点
     */
    @XmlAttribute
    protected Integer nodetype = -1;
    
    /**
     *  排序
     */
    @XmlAttribute
    protected Long sn = 0L;
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * 返回 主键
     * @return
     */
    public Long getId()
    {
        return this.id;
    }
    
    public void setActdefid(String actdefid)
    {
        this.actdefid = actdefid;
    }
    
    /**
     * 返回 流程定义ID
     * @return
     */
    public String getActdefid()
    {
        return this.actdefid;
    }
    
    public Long getDefId()
    {
        return defId;
    }
    
    public void setDefId(Long defId)
    {
        this.defId = defId;
    }
    
    public void setIsstartform(Integer isstartform)
    {
        this.isstartform = isstartform;
    }
    
    /**
     * 返回 流程启动表单
     *	1,启动表单
     *	0,任务表单
     * @return
     */
    public Integer getIsstartform()
    {
        return this.isstartform;
    }
    
    public void setNodeid(String nodeid)
    {
        this.nodeid = nodeid;
    }
    
    /**
     * 返回 节点ID
     * @return
     */
    public String getNodeid()
    {
        return this.nodeid;
    }
    
    public void setBtnname(String btnname)
    {
        this.btnname = btnname;
    }
    
    /**
     * 返回 按钮名称
     * @return
     */
    public String getBtnname()
    {
        return this.btnname;
    }
    
    public void setIconclsname(String iconclsname)
    {
        this.iconclsname = iconclsname;
    }
    
    /**
     * 返回 图标样式
     * @return
     */
    public String getIconclsname()
    {
        return this.iconclsname;
    }
    
    public void setOperatortype(Integer operatortype)
    {
        this.operatortype = operatortype;
    }
    
    /**
     * 返回 操作类型
     * @return
     */
    public Integer getOperatortype()
    {
        return this.operatortype;
    }
    
    public void setPrevscript(String prevscript)
    {
        this.prevscript = prevscript;
    }
    
    /**
     * 返回 前置脚本
     * @return
     */
    public String getPrevscript()
    {
        return this.prevscript;
    }
    
    public void setAfterscript(String afterscript)
    {
        this.afterscript = afterscript;
    }
    
    /**
     * 返回 后置脚本
     * @return
     */
    public String getAfterscript()
    {
        return this.afterscript;
    }
    
    public void setNodetype(Integer nodetype)
    {
        this.nodetype = nodetype;
    }
    
    /**
     * 返回 节点类型0，普通节点1，会签节点
     * @return
     */
    public Integer getNodetype()
    {
        return this.nodetype;
    }
    
    public void setSn(Long sn)
    {
        this.sn = sn;
    }
    
    /**
     * 返回 排序
     * @return
     */
    public Long getSn()
    {
        return this.sn;
    }
    
    public String getParamscript()
    {
        return paramscript;
    }
    
    public void setParamscript(String paramscript)
    {
        this.paramscript = paramscript;
    }
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object)
    {
        if (!(object instanceof NodeButton))
        {
            return false;
        }
        NodeButton rhs = (NodeButton)object;
        return new EqualsBuilder().append(this.id, rhs.id)
            .append(this.actdefid, rhs.actdefid)
            .append(this.isstartform, rhs.isstartform)
            .append(this.nodeid, rhs.nodeid)
            .append(this.btnname, rhs.btnname)
            .append(this.iconclsname, rhs.iconclsname)
            .append(this.operatortype, rhs.operatortype)
            .append(this.prevscript, rhs.prevscript)
            .append(this.afterscript, rhs.afterscript)
            .append(this.nodetype, rhs.nodetype)
            .append(this.sn, rhs.sn)
            .isEquals();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return new HashCodeBuilder(-82280557, -700257973).append(this.id)
            .append(this.actdefid)
            .append(this.isstartform)
            .append(this.nodeid)
            .append(this.btnname)
            .append(this.iconclsname)
            .append(this.operatortype)
            .append(this.prevscript)
            .append(this.afterscript)
            .append(this.nodetype)
            .append(this.sn)
            .toHashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id)
            .append("actdefid", this.actdefid)
            .append("isstartform", this.isstartform)
            .append("nodeid", this.nodeid)
            .append("btnname", this.btnname)
            .append("iconclsname", this.iconclsname)
            .append("operatortype", this.operatortype)
            .append("prevscript", this.prevscript)
            .append("afterscript", this.afterscript)
            .append("nodetype", this.nodetype)
            .append("sn", this.sn)
            .toString();
    }
    
}