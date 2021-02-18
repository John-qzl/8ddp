package com.cssrc.ibms.system.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 对象功能:子系统资源 Model对象 
 * 开发人员:zhulongchao 
 */
@XmlRootElement(name = "resource")
@XmlAccessorType(XmlAccessType.NONE)
public class Resources extends BaseModel implements Cloneable,IResources
{
	@XmlAttribute
	@SysFieldDescription(detail="资源 ID")
	protected Long resId;
	@XmlAttribute
	@SysFieldDescription(detail="资源名称")
	protected String resName;
	@XmlAttribute
	@SysFieldDescription(detail="资源别名")
	protected String alias;
	@XmlAttribute
	@SysFieldDescription(detail="同级排序")
	protected Integer sn;
	@XmlAttribute
	@SysFieldDescription(detail="资源图标")
	protected String icon;
	@XmlAttribute
	@SysFieldDescription(detail="父资源ID")
	protected Long parentId;
	@XmlAttribute
	@SysFieldDescription(detail="默认地址")
	protected String defaultUrl;
	@XmlAttribute
	@SysFieldDescription(detail="是否文件夹",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short isFolder=IS_FOLDER_N; 
	@XmlAttribute
	@SysFieldDescription(detail="是否显示到菜单",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short isDisplayInMenu;
	@XmlAttribute
	@SysFieldDescription(detail="是否默认打开",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short isOpen;
	@XmlAttribute
	@SysFieldDescription(detail="路径")
	protected String path;
	@XmlAttribute
	@SysFieldDescription(detail="是否选中",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected String checked=IS_CHECKED_N;
	@XmlAttribute
	@SysFieldDescription(detail="是否打开新的窗口",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short isNewOpen;
	
	/*语言资源属性*/
	protected Long lanId;
	protected Long lanResId;
	protected String lanType;
	protected String lanMsg;
	protected String lanMemo;
	
	private List<Resources> childMenuList = new ArrayList<Resources>();

	public List<Resources> getChildMenuList() {
		return childMenuList;
	}
	public void setChildMenuList(List<Resources> childMenuList) {
		this.childMenuList = childMenuList;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setResId(Long resId) 
	{
		this.resId = resId;
	}
	/**
	 * 返回 resId
	 * @return
	 */
	public Long getResId() 
	{
		return resId;
	}

	public void setResName(String resName) 
	{
		this.resName = resName;
	}
	/**
	 * 返回 资源名称
	 * @return
	 */
	public String getResName() 
	{
		return resName;
	}

	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	/**
	 * 返回 别名（系统中唯一)
	 * @return
	 */
	public String getAlias() 
	{
		return alias;
	}
 
	public void setSn(Integer sn) 
	{
		this.sn = sn;
	}
	/**
	 * 返回 同级排序
	 * @return
	 */
	public Integer getSn() 
	{
		return sn;
	}

	public void setIcon(String icon) 
	{
		this.icon = icon;
	}
	/**
	 * 返回 图标
	 * @return
	 */
	public String getIcon() 
	{
		if(icon==null/*||icon.indexOf(".")<0*/){
			if(isFolder!=null&&isFolder.equals(IS_FOLDER_Y)){
				icon=ICON_DEFAULT_FOLDER;
			}else if(isFolder!=null&&isFolder.equals(IS_FOLDER_N)){
				icon=ICON_DEFAULT_LEAF;
			}
		}
		return icon;
	}

	public void setParentId(Long parentId) 
	{
		this.parentId = parentId;
	}
	/**
	 * 返回 父ID
	 * @return
	 */
	public Long getParentId() 
	{
		return parentId;
	}

	public void setDefaultUrl(String defaultUrl) 
	{
		this.defaultUrl = defaultUrl;
	}
	/**
	 * 返回 默认地址
	 * @return
	 */
	public String getDefaultUrl() 
	{
		return defaultUrl;
	}

	public void setIsFolder(Short isFolder) 
	{
		this.isFolder = isFolder;
	}
	/**
	 * 返回 栏目
	 * @return
	 */
	public Short getIsFolder() 
	{
		return isFolder;
	}

	public void setIsDisplayInMenu(Short isDisplayInMenu) 
	{
		this.isDisplayInMenu = isDisplayInMenu;
	}
	/**
	 * 返回 显示到菜单
	 * @return
	 */
	public Short getIsDisplayInMenu() 
	{
		return isDisplayInMenu;
	}

	public void setIsOpen(Short isOpen) 
	{
		this.isOpen = isOpen;
	}
	/**
	 * 返回 默认打开
	 * @return
	 */
	public Short getIsOpen() 
	{
		return isOpen;
	}
	/**
	 * 返回 默认打开
	 * @return
	 */
	public String getOpen() 
	{
		if(isOpen!=null&&isOpen.shortValue()==1)return "true";
		else return "false";
	}
	
	
	public String getNewOpen()
	{
		if ((this.isNewOpen != null) && (this.isNewOpen.shortValue() == 1)) return "true";
		return "false";
	}

	public Short getIsNewOpen() {
		return isNewOpen;
	}
	public void setIsNewOpen(Short isNewOpen) {
		this.isNewOpen = isNewOpen;
	}
    
	public Long getLanId() {
		return lanId;
	}
	public void setLanId(Long lanId) {
		this.lanId = lanId;
	}
	public Long getLanResId() {
		return lanResId;
	}
	public void setLanResId(Long lanResId) {
		this.lanResId = lanResId;
	}
	public String getLanType() {
		return lanType;
	}
	public void setLanType(String lanType) {
		this.lanType = lanType;
	}
	public String getLanMsg() {
		return lanMsg;
	}
	public void setLanMsg(String lanMsg) {
		this.lanMsg = lanMsg;
	}
	public String getLanMemo() {
		return lanMemo;
	}
	public void setLanMemo(String lanMemo) {
		this.lanMemo = lanMemo;
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Resources)) 
		{
			return false;
		}
		Resources rhs = (Resources) object;
		return new EqualsBuilder()
		.append(this.resId, rhs.resId)
		.append(this.resName, rhs.resName)
		.append(this.alias, rhs.alias)
		 
		.append(this.sn, rhs.sn)
		.append(this.icon, rhs.icon)
		.append(this.parentId, rhs.parentId)
		.append(this.defaultUrl, rhs.defaultUrl)
		.append(this.isFolder, rhs.isFolder)
		.append(this.isDisplayInMenu, rhs.isDisplayInMenu)
		.append(this.isOpen, rhs.isOpen)
		.append(this.isNewOpen, rhs.isNewOpen)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.resId) 
		.append(this.resName) 
		.append(this.alias) 
		 
		.append(this.sn) 
		.append(this.icon) 
		.append(this.parentId) 
		.append(this.defaultUrl) 
		.append(this.isFolder) 
		.append(this.isDisplayInMenu) 
		.append(this.isOpen) 
		.append(this.isNewOpen)
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("resId", this.resId) 
		.append("resName", this.resName) 
		.append("alias", this.alias) 
		 
		.append("sn", this.sn) 
		.append("icon", this.icon) 
		.append("parentId", this.parentId) 
		.append("defaultUrl", this.defaultUrl) 
		.append("isFolder", this.isFolder) 
		.append("isDisplayInMenu", this.isDisplayInMenu) 
		.append("isOpen", this.isOpen) 
		.append("isNewOpen", this.isNewOpen)
		.toString();
	}
   
  

}