package com.cssrc.ibms.core.resources.datapackage.model;

import javax.servlet.http.HttpServletRequest;

import com.cssrc.ibms.core.util.http.RequestUtil;

/**wbs节点的过滤model
 * simple introduction.
 *
 * <p>detailed comment</p>
 * @author [创建人]  CL <br/> 
 * 		   [创建时间] 2017年9月18日 下午2:56:59 <br/> 
 * 		   [修改人]  CL <br/>
 * 		   [修改时间] 2017年9月18日 下午2:56:59
 * @see
 */
public class NodeFilter {
	
	//节点名称
	String name;
	//计划开始时间
	String startTime; 
	//计划结束时间
	String endTime; 
	//负责人ID
	String assignedID; 
	//责任部门ID
	String departmentID; 
	//节点ID
	String nodeKey;
	
	//返回的sql
	String sql;
	
	//通过request 构造参数
	public NodeFilter(HttpServletRequest request){
		this.assignedID=RequestUtil.getString(request, "assignedID");
		this.name=RequestUtil.getString(request, "name");
		this.startTime=RequestUtil.getString(request, "startTime");
		this.endTime=RequestUtil.getString(request, "endTime");
		this.departmentID=RequestUtil.getString(request, "departmentID");
		this.nodeKey=RequestUtil.getString(request, "nodeKey");
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getAssigned() {
		return assignedID;
	}
	public void setAssigned(String assignedID) {
		this.assignedID = assignedID;
	}
	public String getDepartment() {
		return departmentID;
	}
	public void setDepartment(String departmentID) {
		this.departmentID = departmentID;
	}
	public String getNodeKey() {
		return nodeKey;
	}
	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}
	public String getSql() {
		String result="";
		if(sql==null){
			if(name!=null && !name.equals("")){
				result+=" and f_rwmc like'%"+this.name+"%'";
			}
			if(startTime!=null  && !startTime.equals("")){
				result+=" and f_jhkssj>=to_date('"+this.startTime+"','yyyy-mm-dd')";
			}
			if(endTime!=null  && !endTime.equals("")){
				result+=" and f_jhjssj<=to_date('"+this.endTime+"','yyyy-mm-dd')";
			}			
			if(assignedID!=null  && !assignedID.equals("")){
				result+=" and f_rwfzrid='"+this.assignedID+"'";
			}
			if(departmentID!=null  && !departmentID.equals("")){
				result+=" and f_rwzrbmid='"+this.departmentID+"'";
			}
//			if(nodeKey!=null  && !nodeKey.equals("")){
//				result+=" and ID='"+this.nodeKey+"' connect by prior f_sjrw=id";
//			}
			return result;
		}else{			
			return sql;
		}
	}
	public void setSql(String sql) {
		this.sql = sql;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NodeFilter [name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + ", assignedID="
				+ assignedID + ", departmentID=" + departmentID + ", nodeKey=" + nodeKey + ", sql=" + sql + "]";
	} 
	
}
