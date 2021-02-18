package com.cssrc.ibms.dbom.model;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.dbom.ITreeNode;

/**
 * 对象功能:DBom节点管理 Model类.
 *
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] WeiLei <br/>
 *         [创建时间] 2016-7-14 上午10:13:18 <br/>
 *         [修改人] WeiLei <br/>
 *         [修改时间] 2016-7-14 上午10:13:18
 * @see
 */
@XmlRootElement(name = "DBomNode")
@XmlAccessorType(XmlAccessType.NONE)
public class DBomNode extends BaseModel implements ITreeNode, Cloneable
{
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    @XmlAttribute
    private String code;
    
    @XmlAttribute
    private String name;
    
    /** 节点类型，-1：DBom节点；0：静态节点；1：动态节点。 */
    @XmlAttribute
    private String nodeType;
    
    /** 数据源显示名.eg:计划任务表 */
    @XmlAttribute
    private String dataSource;
    
    /** 动态节点显示方式，包括：Tab页/树形。 */
    @XmlAttribute
    private String display;
    
    @XmlAttribute
    private String description;
    
    @XmlAttribute
    private String pcode;
    
    private String iconCls;
    
    // dbom 节点所属dbom 分类code
    private String dbomCode;
    
    // dbom 节点显示的filed字段
    private String showFiled;
    
    // dbom 节点key值所在字段
    private String nodeKey;
    
    // dbom 父节点key值所在字段
    private String pNodeKey;
    
    // dbom 节点点击目标数据源
    private String targetDataSource;
    
    // dbom 节点目标数据源关联关系
    private String targetDataRelation;
    
    // dbom 节点点击事件url
    private String url;
    
    
    public DBomNode()
    {
    }

    public Long getId()
    {
        return this.id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getCode()
    {
        return this.code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getNodeType()
    {
        return this.nodeType;
    }
    
    public void setNodeType(String nodeType)
    {
        this.nodeType = nodeType;
    }
    
    public String getDataSource()
    {
        return dataSource;
    }
    
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
    
    
    public String getDisplay()
    {
        return this.display;
    }
    
    public void setDisplay(String display)
    {
        this.display = display;
    }
    
    public String getDescription()
    {
        return this.description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getPcode()
    {
        return this.pcode;
    }
    
    public void setPcode(String pcode)
    {
        this.pcode = pcode;
    }
    
    public String getIconCls()
    {
        return iconCls;
    }
    
    public void setIconCls(String iconCls)
    {
        this.iconCls = iconCls;
    }
    
    public String getDbomCode()
    {
        return dbomCode;
    }
    
    public void setDbomCode(String dbomCode)
    {
        this.dbomCode = dbomCode;
    }
    
    public String getShowFiled()
    {
        return showFiled;
    }
    
    public void setShowFiled(String showFiled)
    {
        this.showFiled = showFiled;
    }
    
    public String getNodeKey()
    {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey)
    {
        this.nodeKey = nodeKey;
    }
    
    public String getpNodeKey()
    {
        return pNodeKey;
    }
    
    public void setpNodeKey(String pNodeKey)
    {
        this.pNodeKey = pNodeKey;
    }
    
    public String getTargetDataSource()
    {
        return targetDataSource;
    }
    
    public void setTargetDataSource(String targetDataSource)
    {
        this.targetDataSource = targetDataSource;
    }
    
    public String getTargetDataRelation()
    {
        return targetDataRelation;
    }
    
    public void setTargetDataRelation(String targetDataRelation)
    {
        this.targetDataRelation = targetDataRelation;
    }
    
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public Map<String, String> getRelation()
    {
        return null;
    }
    
}
