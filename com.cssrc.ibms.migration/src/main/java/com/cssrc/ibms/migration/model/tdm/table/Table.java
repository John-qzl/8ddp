package com.cssrc.ibms.migration.model.tdm.table;

import java.util.ArrayList;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.migration.model.IOutField;
import com.cssrc.ibms.api.migration.model.IOutTable;

@XmlRootElement(name = "children")
@XmlAccessorType(XmlAccessType.NONE)
public class Table implements IOutTable{
    
    @XmlAttribute(name="显示名")
    private String displayName;
    
    @XmlAttribute(name="名称")
    private String tableName;
    
    /** 排序方向（排序方向，指定数据类记录的排序方向，ASC表示升序，DESC表示降序） */
    @XmlAttribute(name="排序方向")
    private String paiXu;
    
    @XmlAttribute(name="是否显示")
    private String isShow;
    
    @XmlAttribute(name="详细文字")
    private String DetailText;
    
    @XmlAttribute(name="描述")
    private String description;
    
    @XmlAttribute(name="大图标")
    private String bigImage;
    
    @XmlAttribute(name="中图标")
    private String norImage;
    
    @XmlAttribute(name="小图标")
    private String smaImage;
    
    @XmlAttribute(name="类型")
    private String category;
    
    @XmlAttribute(name="主键显示值")
    private String pkDisplay;
    
    @XmlAttribute(name="属性展现顺序")
    private String cite;     
    
    @XmlElement(name="普通属性")
    private List<SimpleColumn> cwmTabColumnses = new ArrayList<SimpleColumn>();	
    
    /** 所有关系属性 */
    @XmlElement(name="关系属性")
    private List<RelationColumn> cwmRelationColumnses = new ArrayList<RelationColumn>();

    /**---------------------------------IOutTable接口实现------------------------------------------*/
    //表名转换：预期：去除"C_",加上"_dqbb"
	public String getTableName() {
		String newName = tableName.toLowerCase();
		if(newName.startsWith("c_")){
			newName = newName.replace("c_", "");
		}
		return newName;
	}
	public String getTableDesc(){
		return displayName;
	}
    public Short getIsMain(){
    	return 1;
    }
    public List<IOutField> getField(){
    	List<IOutField> list = new ArrayList();
    	list.addAll(cwmTabColumnses);
    	list.addAll(cwmRelationColumnses);
    	return list;
    }    
    public boolean isExceptTable(){
    	boolean flag = false;
    	if(tableName.contains(IOutTable.USER_TABLE)){
    		flag = true; 
    	}else if(tableName.contains(IOutTable.DEPT_TABLE)){
    		flag = true; 
    	}
    	return flag;
    }
    /**----------------------------------------------------------------------------------------*/
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPaiXu() {
		return paiXu;
	}

	public void setPaiXu(String paiXu) {
		this.paiXu = paiXu;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getDetailText() {
		return DetailText;
	}

	public void setDetailText(String detailText) {
		DetailText = detailText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}

	public String getNorImage() {
		return norImage;
	}

	public void setNorImage(String norImage) {
		this.norImage = norImage;
	}

	public String getSmaImage() {
		return smaImage;
	}

	public void setSmaImage(String smaImage) {
		this.smaImage = smaImage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPkDisplay() {
		return pkDisplay;
	}

	public void setPkDisplay(String pkDisplay) {
		this.pkDisplay = pkDisplay;
	}

	public String getCite() {
		return cite;
	}

	public void setCite(String cite) {
		this.cite = cite;
	}

	public List<SimpleColumn> getCwmTabColumnses() {
		return cwmTabColumnses;
	}

	public void setCwmTabColumnses(List<SimpleColumn> cwmTabColumnses) {
		this.cwmTabColumnses = cwmTabColumnses;
	}

	public List<RelationColumn> getCwmRelationColumnses() {
		return cwmRelationColumnses;
	}

	public void setCwmRelationColumnses(List<RelationColumn> cwmRelationColumnses) {
		this.cwmRelationColumnses = cwmRelationColumnses;
	}
	
	
	
}
