package com.cssrc.ibms.share.rights;
/**
 * 
 * <p>Title:DataTemplateVO</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:06:34
 */
public class DataTemplateVO {
	protected String displayField;
	protected String filterField;
	protected String sortField;
	protected String exportField;
	protected String printField;
	protected String manageField;

	public String getDisplayField() {
		return this.displayField;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getFilterField() {
		return this.filterField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	public String getSortField() {
		return this.sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getExportField() {
		return this.exportField;
	}

	public void setExportField(String exportField) {
		this.exportField = exportField;
	}

	public String getPrintField() {
		return this.printField;
	}

	public void setPrintField(String printField) {
		this.printField = printField;
	}

	public String getManageField() {
		return this.manageField;
	}

	public void setManageField(String manageField) {
        this.manageField = manageField;
	}

	public String toString() {
		return "DataTemplateVO [displayField=" + this.displayField
				+ ", filterField=" + this.filterField + ", sortField="
				+ this.sortField + ", exportField=" + this.exportField
				+ ", printField=" + this.printField + ", manageField="
				+ this.manageField + "]";
	}
}
