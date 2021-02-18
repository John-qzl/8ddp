package com.cssrc.ibms.core.log.model;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysLogSwitch;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 日志开关管理
 * <p>Title:SysLogSwitch</p>
 * @author Yangbo 
 * @date 2016年9月7日上午9:06:34
 */
public class SysLogSwitch extends BaseModel implements ISysLogSwitch
{
	public static final short STATUS_CLOSE = 0;
	public static final short STATUS_OPEN = 1;
	@SysFieldDescription(detail="日志开关 ID")
	protected Long id;
	@SysFieldDescription(detail="日志开关名称")
	protected String model;
	@SysFieldDescription(detail="日志开关状态",maps="{\"1\":\"开启\",\"0\":\"关闭\"}")
	protected Short status = Short.valueOf((short)0);
	@SysFieldDescription(detail="创建时间")
	protected Date createtime;
	@SysFieldDescription(detail="创建人")
	protected String creator;
	@SysFieldDescription(detail="创建人id")
	protected Long creatorid;
	@SysFieldDescription(detail="更新人")
	protected String updby;
	@SysFieldDescription(detail="更新人id")
	protected Long updbyid;
	@SysFieldDescription(detail="描述")
	protected String memo;
	@SysFieldDescription(detail="最后更新时间")
	protected Date lastuptime;
	@SysFieldDescription(detail="日志开启类型")
	protected String execTypes;

	public String getExecTypes() {
		return execTypes;
	}

	public void setExecTypes(String execTypes) {
		this.execTypes = execTypes;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel()
	{
		return this.model;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getStatus()
	{
		return this.status;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getCreatetime()
	{
		return this.createtime;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator()
	{
		return this.creator;
	}

	public void setCreatorid(Long creatorid) {
		this.creatorid = creatorid;
	}

	public Long getCreatorid()
	{
		return this.creatorid;
	}

	public void setUpdby(String updby) {
		this.updby = updby;
	}

	public String getUpdby()
	{
		return this.updby;
	}

	public void setUpdbyid(Long updbyid) {
		this.updbyid = updbyid;
	}

	public Long getUpdbyid()
	{
		return this.updbyid;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo()
	{
		return this.memo;
	}

	

	public Date getLastuptime() {
		return lastuptime;
	}

	public void setLastuptime(Date lastuptime) {
		this.lastuptime = lastuptime;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof SysLogSwitch))
		{
			return false;
		}
		SysLogSwitch rhs = (SysLogSwitch)object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.model, rhs.model)
		.append(this.status, rhs.status)
		.append(this.createtime, rhs.createtime)
		.append(this.creator, rhs.creator)
		.append(this.creatorid, rhs.creatorid)
		.append(this.updby, rhs.updby)
		.append(this.updbyid, rhs.updbyid)
		.append(this.memo, rhs.memo)
		.append(this.lastuptime, rhs.lastuptime)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.model)
		.append(this.status)
		.append(this.createtime)
		.append(this.creator)
		.append(this.creatorid)
		.append(this.updby)
		.append(this.updbyid)
		.append(this.memo)
		.append(this.lastuptime)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("model", this.model)
		.append("status", this.status)
		.append("createtime", this.createtime)
		.append("creator", this.creator)
		.append("creatorid", this.creatorid)
		.append("updby", this.updby)
		.append("updbyid", this.updbyid)
		.append("memo", this.memo)
		.append("lastuptime", this.lastuptime)
		.toString();
	}
}


