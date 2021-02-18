package com.cssrc.ibms.index.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="indexColumn")
public class IndexColumn
{

	@XmlAttribute(required=true)
	protected String name;

	@XmlAttribute(required=true)
	protected String alias;

	@XmlAttribute
	protected Long catalog;

	@XmlAttribute(required=true)
	protected short colType;

	@XmlAttribute(required=true)
	protected short dataMode;

	@XmlAttribute
	protected String dataFrom;

	@XmlAttribute
	protected String dsAlias;

	@XmlAttribute
	protected String dsName;

	@XmlAttribute
	protected String colHeight;

	@XmlAttribute
	protected String colUrl;

	@XmlAttribute
	protected String templateHtml;

	@XmlAttribute(required=true)
	protected short isPublic;

	@XmlAttribute
	protected Long orgId;

	@XmlAttribute
	protected Short supportRefesh;

	@XmlAttribute
	protected Integer refeshTime;

	@XmlAttribute(required=true)
	protected short showEffect;

	@XmlAttribute
	protected String memo;

	public String getName()
	{
		return this.name;
	}

	public void setName(String value)
	{
		this.name = value;
	}

	public String getAlias()
	{
		return this.alias;
	}

	public void setAlias(String value)
	{
		this.alias = value;
	}

	public Long getCatalog()
	{
		return this.catalog;
	}

	public void setCatalog(Long value)
	{
		this.catalog = value;
	}

	public short getColType()
	{
		return this.colType;
	}

	public void setColType(short value)
	{
		this.colType = value;
	}

	public short getDataMode()
	{
		return this.dataMode;
	}

	public void setDataMode(short value)
	{
		this.dataMode = value;
	}

	public String getDataFrom()
	{
		return this.dataFrom;
	}

	public void setDataFrom(String value)
	{
		this.dataFrom = value;
	}

	public String getDsAlias()
	{
		return this.dsAlias;
	}

	public void setDsAlias(String value)
	{
		this.dsAlias = value;
	}

	public String getDsName()
	{
		return this.dsName;
	}

	public void setDsName(String value)
	{
		this.dsName = value;
	}

	public String getColHeight()
	{
		return this.colHeight;
	}

	public void setColHeight(String value)
	{
		this.colHeight = value;
	}

	public String getColUrl()
	{
		return this.colUrl;
	}

	public void setColUrl(String value)
	{
		this.colUrl = value;
	}

	public String getTemplateHtml()
	{
		return this.templateHtml;
	}

	public void setTemplateHtml(String value)
	{
		this.templateHtml = value;
	}

	public short getIsPublic()
	{
		return this.isPublic;
	}

	public void setIsPublic(short value)
	{
		this.isPublic = value;
	}

	public Long getOrgId()
	{
		return this.orgId;
	}

	public void setOrgId(Long value)
	{
		this.orgId = value;
	}

	public short getSupportRefesh()
	{
		if (this.supportRefesh == null) {
			return 0;
		}
		return this.supportRefesh.shortValue();
	}

	public void setSupportRefesh(Short value)
	{
		this.supportRefesh = value;
	}

	public Integer getRefeshTime()
	{
		return this.refeshTime;
	}

	public void setRefeshTime(Integer value)
	{
		this.refeshTime = value;
	}

	public short getShowEffect()
	{
		return this.showEffect;
	}

	public void setShowEffect(short value)
	{
		this.showEffect = value;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public void setMemo(String value)
	{
		this.memo = value;
	}
}

