package com.cssrc.ibms.system.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.migration.model.IOutDictionary;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 *  数据字典(业务表定义字段)
 * <p>Title:Dictionary</p>
 * @author Yangbo 
 * @date 2016-8-3上午10:15:30
 */
public class Dictionary extends BaseModel implements IDictionary{
	protected Long dicId = Long.valueOf(0L);
	protected Long typeId;
	protected String itemKey;
	protected String itemName;
	protected String itemValue;
	protected String descp;
	protected Long sn;
	protected String nodePath;
	protected Long parentId;
	protected Integer type = Integer.valueOf(0);
	
	protected Short dic_delFlag;// 是否删除
	
	protected Long dic_creatorId;// 创建人ID
	
	protected Date dic_createTime;// 创建时间
	
	protected Long dic_updateId;// 更改人ID
	
	protected Date dic_updateTime;// 更改时间

	protected String open = "true";

	public String getOpen() {
		return this.open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public void setDicId(Long dicId) {
		this.dicId = dicId;
	}

	public Long getDicId() {
		return this.dicId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getTypeId() {
		return this.typeId;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemKey() {
		return this.itemKey;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemValue() {
		return this.itemValue;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public String getDescp() {
		return this.descp;
	}

	public void setSn(Long sn) {
		this.sn = sn;
	}

	public Long getSn() {
		return this.sn;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getNodePath() {
		return this.nodePath;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Short getDic_delFlag() {
		return dic_delFlag;
	}

	public void setDic_delFlag(Short dic_delFlag) {
		this.dic_delFlag = dic_delFlag;
	}

	public Long getDic_creatorId() {
		return dic_creatorId;
	}

	public void setDic_creatorId(Long dic_creatorId) {
		this.dic_creatorId = dic_creatorId;
	}

	public Date getDic_createTime() {
		return dic_createTime;
	}

	public void setDic_createTime(Date dic_createTime) {
		this.dic_createTime = dic_createTime;
	}

	public Long getDic_updateId() {
		return dic_updateId;
	}

	public void setDic_updateId(Long dic_updateId) {
		this.dic_updateId = dic_updateId;
	}

	public Date getDic_updateTime() {
		return dic_updateTime;
	}

	public void setDic_updateTime(Date dic_updateTime) {
		this.dic_updateTime = dic_updateTime;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Dictionary)) {
			return false;
		}
		Dictionary rhs = (Dictionary) object;
		return new EqualsBuilder().append(this.dicId, rhs.dicId).append(
				this.typeId, rhs.typeId).append(this.itemKey, rhs.itemKey)
				.append(this.itemName, rhs.itemName).append(this.itemValue,
						rhs.itemValue).append(this.descp, rhs.descp).append(
						this.sn, rhs.sn).append(this.nodePath, rhs.nodePath)
				.append(this.parentId, rhs.parentId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.dicId)
				.append(this.typeId).append(this.itemKey).append(this.itemName)
				.append(this.itemValue).append(this.descp).append(this.sn)
				.append(this.nodePath).append(this.parentId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("dicId", this.dicId).append(
				"typeId", this.typeId).append("itemKey", this.itemKey).append(
				"itemName", this.itemName).append("itemValue", this.itemValue)
				.append("descp", this.descp).append("sn", this.sn).append(
						"nodePath", this.nodePath).append("parentId",
						this.parentId).toString();
	}
	
	public static Dictionary transDic(IOutDictionary outDic){
		Dictionary dic = new Dictionary();
		dic.setItemKey(outDic.getItemKey());
		dic.setItemName(outDic.getItemName());
		dic.setItemValue(outDic.getItemValue());
		dic.setSn(outDic.getSn());
		dic.setDescp(outDic.getDescp());
		return dic;
	}
}
