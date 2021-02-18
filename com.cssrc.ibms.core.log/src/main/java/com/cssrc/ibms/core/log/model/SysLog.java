package com.cssrc.ibms.core.log.model; 
 
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysLog;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
 
/**
 * 系统日志 Model对象
 *
 * <p>detailed comment</p>
 * @author [创建人]  zhulongchao <br/> 
 * 		   [创建时间] 2015-3-23 下午04:49:36 <br/>  
 * 		   [修改时间] 2015-3-23 下午04:49:36
 * @see
 */
public class SysLog extends BaseModel implements ISysLog{ 
	protected Long auditId;
	// 操作名称
	protected String opName;
	// 执行时间
	protected java.util.Date opTime;
	// 执行人ID
	protected Long executorId;
	// 执行人姓名
	protected String executorName;
	// 执行人账户
	protected String executor;
	// IP
	protected String fromIp;
	// 执行方法
	protected String exeMethod;
	// 请求URL
	protected String requestURI;
	// 请求参数
	protected String reqParams;
	// 归属模块
	protected String ownermodel;
	// 日志类型
	protected String exectype;
	// 归属组织
	protected Long orgid;
	// 操作结果
	protected Short result;
	// 明细信息
	protected String detail;
	//操作后数据
	protected String jsonData;
	
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	public String getJsonData() {
		return jsonData;
	}
	private Short Short(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public Long getAuditId() {
		return auditId;
	}
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	/**
	 * 返回 操作名称
	 * @return
	 */
	public String getOpName() {
		return opName;
	}

	public void setOpTime(java.util.Date opTime) {
		this.opTime = opTime;
	}
	/**
	 * 返回 执行时间
	 * @return
	 */
	public java.util.Date getOpTime() {
		return opTime;
	}

	public void setExecutorId(Long executorId) {
		this.executorId = executorId;
	}
	/**
	 * 返回 执行人ID
	 * @return
	 */
	public Long getExecutorId() {
		return executorId;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}
	/**
	 * 返回 执行结果 1表示操作成功，0表示操作失败
	 * @return
	 */
	public Short getResult() {
		return result;
	}
	public void setResult(Short result) {
		this.result = result;
	}
	/**
	 * 返回 执行人
	 * @return
	 */
	public String getExecutor() {
		return executor;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}
	/**
	 * 返回 IP
	 * @return
	 */
	public String getFromIp() {
		return fromIp;
	}

	public void setExeMethod(String exeMethod) {
		this.exeMethod = exeMethod;
	}
	/**
	 * 返回 执行方法
	 * @return
	 */
	public String getExeMethod() {
		return exeMethod;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	/**
	 * 返回 请求URL
	 * @return
	 */
	public String getRequestURI() {
		return requestURI;
	}

	public void setReqParams(String reqParams) {
		this.reqParams = reqParams;
	}
	/**
	 * 返回 请求参数
	 * @return
	 */
	public String getReqParams() {
		return reqParams;
	}

   
   	public String getOwnermodel() {
		return ownermodel;
	}
	public void setOwnermodel(String ownermodel) {
		this.ownermodel = ownermodel;
	}
	public String getExectype() {
		return exectype;
	}
	public void setExectype(String exectype) {
		this.exectype = exectype;
	}
	public Long getOrgid() {
		return orgid;
	}
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SysLog)) {
			return false;
		}
		SysLog rhs = (SysLog) object;
		return new EqualsBuilder()
		.append(this.auditId, rhs.auditId)
		.append(this.opName, rhs.opName)
		.append(this.opTime, rhs.opTime)
		.append(this.executorId, rhs.executorId)
		.append(this.executor, rhs.executor)
		.append(this.fromIp, rhs.fromIp)
		.append(this.exeMethod, rhs.exeMethod)
		.append(this.requestURI, rhs.requestURI)
		.append(this.reqParams, rhs.reqParams)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.auditId) 
		.append(this.opName) 
		.append(this.opTime) 
		.append(this.executorId) 
		.append(this.executor) 
		.append(this.fromIp) 
		.append(this.exeMethod) 
		.append(this.requestURI) 
		.append(this.reqParams) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("auditId", this.auditId) 
		.append("opName", this.opName) 
		.append("opTime", this.opTime) 
		.append("executorId", this.executorId) 
		.append("executor", this.executor) 
		.append("fromIp", this.fromIp) 
		.append("exeMethod", this.exeMethod) 
		.append("requestURI", this.requestURI) 
		.append("reqParams", this.reqParams) 
		.toString();
	}
  

}