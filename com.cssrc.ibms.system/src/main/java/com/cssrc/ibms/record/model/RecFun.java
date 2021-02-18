package com.cssrc.ibms.record.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.rec.model.IRecFunction;
import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * Description:表单功能点
 * <p>RecFunction.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
@XmlRootElement(name = "funource")
@XmlAccessorType(XmlAccessType.NONE)
public class RecFun extends BaseModel implements Cloneable,IRecFunction
{
		// 表单功能点类型id
		@XmlAttribute
		@SysFieldDescription(detail="表单功能点类型id")
		protected Long typeId;
		// 表单功能点id
		@XmlAttribute
		@SysFieldDescription(detail="表单功能点id")
		protected Long funId;
		// 表单功能点名称
		@XmlAttribute
		@SysFieldDescription(detail="表单功能点名称")
		protected String funName;
		// 别名（系统中唯一)
		@XmlAttribute
		@SysFieldDescription(detail="别名")
		protected String alias;
		// 描述（系统中唯一)
		@XmlAttribute
		@SysFieldDescription(detail="描述")
		protected String funDesc;
		// 同级排序
		@XmlAttribute
		@SysFieldDescription(detail="同级排序")
		protected Integer sn;
		// 图标
		@XmlAttribute
		@SysFieldDescription(detail="图标")
		protected String icon;
		// 父ID
		@XmlAttribute
		@SysFieldDescription(detail="资源 ID")
		protected Long parentId;
		// 默认地址
		@XmlAttribute
		@SysFieldDescription(detail="默认地址")
		protected String defaultUrl;
		// 栏目(没有字节的为0)
		@XmlAttribute
		@SysFieldDescription(detail="栏目")
		protected Short isFolder=IS_FOLDER_N; 
		// 显示到菜单
		@XmlAttribute
		@SysFieldDescription(detail="是否显示到菜单",maps="{\"1\":\"是\",\"0\":\"否\"}")
		protected Short isDisplayInMenu;
		// 默认打开
		@XmlAttribute
		@SysFieldDescription(detail="是否默认打开",maps="{\"1\":\"是\",\"0\":\"否\"}")
		protected Short isOpen;
		@XmlAttribute
		@SysFieldDescription(detail="是否打开新窗口",maps="{\"1\":\"是\",\"0\":\"否\"}")
		protected Short isNewOpen;
		//路径
		@XmlAttribute
		@SysFieldDescription(detail="路径")
		protected String path;
		//顶部按钮、管理列按钮信息
		@XmlAttribute
		@SysFieldDescription(detail="按钮信息")
		protected String buttonArr;
		//是否选中(数据库中没有)
		@XmlAttribute
		@SysFieldDescription(detail="是否选中",maps="{\"1\":\"是\",\"0\":\"否\"}")
		protected String checked=IS_CHECKED_N;		
		/*语言资源属性(数据库中没有)*/
		@SysFieldDescription(detail="lanId")
		protected Long lanId;
		@SysFieldDescription(detail="lanfunId")
		protected Long lanfunId;
		@SysFieldDescription(detail="lanType")
		protected String lanType;
		@SysFieldDescription(detail="lanMsg")
		protected String lanMsg;
		@SysFieldDescription(detail="lanMemo")
		protected String lanMemo;
		
		private List<RecFun> childMenuList = new ArrayList<RecFun>();

		public List<RecFun> getChildMenuList() {
			return childMenuList;
		}
		public void setChildMenuList(List<RecFun> childMenuList) {
			this.childMenuList = childMenuList;
		}
		/**
		 * @see java.lang.Object#equals(Object)
		 */
		public boolean equals(Object object) 
		{
			if (!(object instanceof RecFun)) 
			{
				return false;
			}
			RecFun rhs = (RecFun) object;
			return new EqualsBuilder()
			.append(this.typeId, rhs.typeId)
			.append(this.funId, rhs.funId)
			.append(this.funName, rhs.funName)
			.append(this.alias, rhs.alias)	
			.append(this.funDesc, rhs.funDesc)	
			.append(this.sn, rhs.sn)
			.append(this.icon, rhs.icon)
			.append(this.parentId, rhs.parentId)
			.append(this.defaultUrl, rhs.defaultUrl)
			.append(this.isFolder, rhs.isFolder)
			.append(this.isDisplayInMenu, rhs.isDisplayInMenu)
			.append(this.isOpen, rhs.isOpen)
			.append(this.isNewOpen, rhs.isNewOpen)
			.append(this.buttonArr, rhs.buttonArr)
			.isEquals();
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() 
		{
			return new HashCodeBuilder(-82280557, -700257973)
			.append(this.typeId) 
			.append(this.funId) 
			.append(this.funName) 
			.append(this.alias) 
			.append(this.funDesc) 
			.append(this.sn) 
			.append(this.icon) 
			.append(this.parentId) 
			.append(this.defaultUrl) 
			.append(this.isFolder) 
			.append(this.isDisplayInMenu) 
			.append(this.isOpen) 
			.append(this.isNewOpen)
			.append(this.buttonArr)
			.toHashCode();
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() 
		{
			return new ToStringBuilder(this)
			.append("typeId", this.typeId) 
			.append("funId", this.funId) 
			.append("funName", this.funName) 
			.append("alias", this.alias) 
			.append("funDesc",this.funDesc) 
			.append("sn", this.sn) 
			.append("icon", this.icon) 
			.append("parentId", this.parentId) 
			.append("defaultUrl", this.defaultUrl) 
			.append("isFolder", this.isFolder) 
			.append("isDisplayInMenu", this.isDisplayInMenu) 
			.append("isOpen", this.isOpen) 
			.append("isNewOpen", this.isNewOpen)
			.append("buttonArr", this.buttonArr)
			.toString();
		}
		
		public Long getTypeId() {
			return typeId;
		}
		public void setTypeId(Long typeId) {
			this.typeId = typeId;
		}
		public Long getFunId() {
			return funId;
		}
		public void setFunId(Long funId) {
			this.funId = funId;
		}
		public String getFunName() {
			return funName;
		}
		public void setFunName(String funName) {
			this.funName = funName;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		public String getFunDesc() {
			return funDesc;
		}
		public void setFunDesc(String funDesc) {
			this.funDesc = funDesc;
		}
		public Integer getSn() {
			return sn;
		}
		public void setSn(Integer sn) {
			this.sn = sn;
		}
		public String getIcon() 
		{
			if(icon==null||icon.indexOf(".")<0){
				if(isFolder!=null&&isFolder.equals(IS_FOLDER_Y)){
					icon=ICON_DEFAULT_FOLDER;
				}else if(isFolder!=null&&isFolder.equals(IS_FOLDER_N)){
					icon=ICON_DEFAULT_LEAF;
				}
			}
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public Long getParentId() {
			return parentId;
		}
		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}
		public String getDefaultUrl() {
			return defaultUrl;
		}
		public void setDefaultUrl(String defaultUrl) {
			this.defaultUrl = defaultUrl;
		}
		public Short getIsFolder() {
			return isFolder;
		}
		public void setIsFolder(Short isFolder) {
			this.isFolder = isFolder;
		}
		public Short getIsDisplayInMenu() {
			return isDisplayInMenu;
		}
		public void setIsDisplayInMenu(Short isDisplayInMenu) {
			this.isDisplayInMenu = isDisplayInMenu;
		}
		public Short getIsOpen() {
			return isOpen;
		}
		public void setIsOpen(Short isOpen) {
			this.isOpen = isOpen;
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
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getbuttonArr() {
			return buttonArr;
		}
		public void setbuttonArr(String buttonArr) {
			this.buttonArr = buttonArr;
		}
		public String getChecked() {
			return checked;
		}
		public void setChecked(String checked) {
			this.checked = checked;
		}
		public Long getLanId() {
			return lanId;
		}
		public void setLanId(Long lanId) {
			this.lanId = lanId;
		}
		public Long getLanfunId() {
			return lanfunId;
		}
		public void setLanfunId(Long lanfunId) {
			this.lanfunId = lanfunId;
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
}
