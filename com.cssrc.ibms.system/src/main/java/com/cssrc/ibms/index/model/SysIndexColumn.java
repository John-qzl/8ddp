package com.cssrc.ibms.index.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * @author Yangbo 2016-7-20
 *
 */
public class SysIndexColumn extends BaseModel {
	public static short DATA_MODE_SERVICE = 0;

	public static short DATA_MODE_QUERY = 1;

	public static short COLUMN_TYPE_COMMON = 0;

	public static short COLUMN_TYPE_CHART = 1;

	public static short COLUMN_TYPE_CALENDAR = 2;

	public static short COLUMN_TYPE_ROLL = 3;
	private static final long serialVersionUID = 4018816120529407191L;
	protected Long id;
	protected String name;
	protected String alias;
	protected Long catalog;
	protected Short colType;
	protected Short dataMode;
	protected String dataFrom;
	protected String dataParam;
	protected String dsAlias;
	protected String dsName;
	protected Long colHeight;
	protected String colUrl;
	protected String templateHtml;
	protected Short isPublic;
	protected Long orgId;
	protected Short supportRefesh;
	protected Long refeshTime;
	protected Short showEffect;
	protected String memo;
	protected Short needPage = Short.valueOf((short) 0);
	protected String orgName;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setCatalog(Long catalog) {
		this.catalog = catalog;
	}

	public Long getCatalog() {
		return this.catalog;
	}

	public Short getColType() {
		return this.colType;
	}

	public Short getDataMode() {
		return this.dataMode;
	}

	public void setDataMode(Short dataMode) {
		this.dataMode = dataMode;
	}

	public void setColType(Short colType) {
		this.colType = colType;
	}

	public void setDsAlias(String dsAlias) {
		this.dsAlias = dsAlias;
	}

	public String getDsAlias() {
		return this.dsAlias;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public String getDsName() {
		return this.dsName;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getDataFrom() {
		return this.dataFrom;
	}

	public void setColHeight(Long colHeight) {
		this.colHeight = colHeight;
	}

	public Long getColHeight() {
		return this.colHeight;
	}

	public void setColUrl(String colUrl) {
		this.colUrl = colUrl;
	}

	public String getColUrl() {
		return this.colUrl;
	}

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	public String getTemplateHtml() {
		return this.templateHtml;
	}

	public void setIsPublic(Short isPublic) {
		this.isPublic = isPublic;
	}

	public Short getIsPublic() {
		return this.isPublic;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setSupportRefesh(Short supportRefesh) {
		this.supportRefesh = supportRefesh;
	}

	public Short getSupportRefesh() {
		return this.supportRefesh;
	}

	public void setRefeshTime(Long refeshTime) {
		this.refeshTime = refeshTime;
	}

	public Long getRefeshTime() {
		return this.refeshTime;
	}

	public void setShowEffect(Short showEffect) {
		this.showEffect = showEffect;
	}

	public Short getShowEffect() {
		return this.showEffect;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDataParam() {
		return this.dataParam;
	}

	public void setDataParam(String dataParam) {
		this.dataParam = dataParam;
	}

	public Short getNeedPage() {
		return this.needPage;
	}

	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysIndexColumn)) {
			return false;
		}
		SysIndexColumn rhs = (SysIndexColumn) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name,
				rhs.name).append(this.alias, rhs.alias).append(this.catalog,
				rhs.catalog).append(this.colType, rhs.colType).append(
				this.dataMode, rhs.dataMode)
				.append(this.dataFrom, rhs.dataFrom).append(this.dsAlias,
						rhs.dsAlias).append(this.dsName, rhs.dsName).append(
						this.colHeight, rhs.colHeight).append(this.colUrl,
						rhs.colUrl).append(this.templateHtml, rhs.templateHtml)
				.append(this.isPublic, rhs.isPublic).append(this.orgId,
						rhs.orgId)
				.append(this.supportRefesh, rhs.supportRefesh).append(
						this.refeshTime, rhs.refeshTime).append(
						this.showEffect, rhs.showEffect).append(this.memo,
						rhs.memo).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.alias).append(this.catalog)
				.append(this.colType).append(this.dataMode).append(
						this.dataFrom).append(this.dsAlias).append(this.dsName)
				.append(this.colHeight).append(this.colUrl).append(
						this.templateHtml).append(this.isPublic).append(
						this.orgId).append(this.supportRefesh).append(
						this.refeshTime).append(this.showEffect).append(
						this.memo).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("name",
				this.name).append("alias", this.alias).append("catalog",
				this.catalog).append("colType", this.colType).append(
				"dataMode", this.dataMode).append("dataFrom", this.dataFrom)
				.append("dsAlias", this.dsAlias).append("dsName", this.dsName)
				.append("colHeight", this.colHeight).append("colUrl",
						this.colUrl).append("templateHtml", this.templateHtml)
				.append("isPublic", this.isPublic).append("orgId", this.orgId)
				.append("supportRefesh", this.supportRefesh).append(
						"refeshTime", this.refeshTime).append("showEffect",
						this.showEffect).append("memo", this.memo).toString();
	}
}
