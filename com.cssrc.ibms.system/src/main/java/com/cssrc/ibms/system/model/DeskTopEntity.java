/**
 * 
 */
package com.cssrc.ibms.system.model;

/**
 * 个人桌面实体类
 * @see
 */
public class DeskTopEntity {
	  protected String portalId;
  	  protected Integer colNum;
  	  protected Integer rowNum;
  	  private String title;
  	  private String url;
  	  private String autoLoad;
  	  private int actived ;
	/**
	 * @return the portalId
	 */
	public String getPortalId() {
		return portalId;
	}
	/**
	 * @param portalId the portalId to set
	 */
	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}
	/**
	 * @return the colNum
	 */
	public Integer getColNum() {
		return colNum;
	}
	/**
	 * @param colNum the colNum to set
	 */
	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}
	/**
	 * @return the rowNum
	 */
	public Integer getRowNum() {
		return rowNum;
	}
	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the autoLoad
	 */
	public String getAutoLoad() {
		return autoLoad;
	}
	/**
	 * @param autoLoad the autoLoad to set
	 */
	public void setAutoLoad(String autoLoad) {
		this.autoLoad = autoLoad;
	}
	/**
	 * @return the actived
	 */
	public int getActived() {
		return actived;
	}
	/**
	 * @param actived the actived to set
	 */
	public void setActived(int actived) {
		this.actived = actived;
	}
	/**
	 * @param portalId
	 * @param colNum
	 * @param rowNum
	 * @param title
	 * @param url
	 * @param autoLoad
	 * @param actived
	 */
	public DeskTopEntity(String portalId, Integer colNum, Integer rowNum,
			String title, String url, String autoLoad, int actived) {
		super();
		this.portalId = portalId;
		this.colNum = colNum;
		this.rowNum = rowNum;
		this.title = title;
		this.url = url;
		this.autoLoad = autoLoad;
		this.actived = actived;
	}
	public DeskTopEntity(){
		
	}
  	  
}
