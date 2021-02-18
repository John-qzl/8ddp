package com.cssrc.ibms.system.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 
 * <p>Title:ResourceFolder</p>
 * @author Yangbo 
 * @date 2016-8-26上午10:00:08
 */
public class ResourceFolder {
	private Long folderId;
	private String folderName;
	private String folderPath;
	private Long parentId;

	public Long getFolderId() {
		return this.folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderPath() {
		return this.folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Resources)) {
			return false;
		}
		ResourceFolder rhs = (ResourceFolder) object;
		return new EqualsBuilder().append(this.folderId, rhs.folderId).append(
				this.folderName, rhs.folderName).append(this.folderPath,
				rhs.folderPath).append(this.parentId, rhs.parentId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.folderId)
				.append(this.folderName).append(this.folderPath).append(
						this.parentId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("folderId", this.folderId)
				.append("folderName", this.folderName).append("folderPath",
						this.folderPath).append("parentId", this.parentId)
				.toString();
	}
}