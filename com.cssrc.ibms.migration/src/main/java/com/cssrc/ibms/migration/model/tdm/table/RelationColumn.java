package com.cssrc.ibms.migration.model.tdm.table;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.migration.model.IOutField;

@XmlRootElement(name = "关系属性")
@XmlAccessorType(XmlAccessType.NONE)
public class RelationColumn  implements IOutField{
	@XmlAttribute(name = "名称")
	private String name;

	@XmlAttribute(name = "显示名")
	private String displayName;

	@XmlAttribute(name = "描述")
	private String description;

	@XmlAttribute(name = "关系类型")
	private String relationType;
	
	/** True表示显示，False表示不显示 */
	@XmlAttribute(name = "是否显示")
	private String isShow;

	@XmlAttribute(name = "是否必要")
	private String isNeed;

	@XmlAttribute(name = "用途")
	private String purpose;

	@XmlAttribute(name = "数据类型")
	private String type = "String";

	@XmlAttribute(name = "是否被引用")
	private String isNotFk;

	@XmlAttribute(name = "所有权")
	private String ownerShip;

	@XmlAttribute(name = "关联数据类")
	private String relTable;
	
	private Integer sn;
	private boolean isSingleUserSelector = false;
	private boolean isSingleDeptSelector = false;
	private boolean isMultiUserSelector = false;
	private boolean isMultiDeptSelector = false;
	/**---------------------------------IOutField接口实现------------------------------------------*/
	public Integer getCharLen(){
		Integer len = 100;
		return len;
	}
	public Short getControlType(){	
		Short ct = 23;//关联关系列
		if(isSingleUserSelector){
			ct = 4;
		}else if(isSingleDeptSelector){
			ct = 18;
		}else if(isMultiUserSelector){
			ct = 8;
		}else if(isMultiDeptSelector){
			ct = 6;
		}
		return ct;
	}
	public String getCtlProperty(){
		JSONObject obj = new JSONObject();
		obj.put("displayHoverTitle", "0");
		obj.put("hoverTitle", "请填写字段"+this.getFieldDesc()+"的信息");
		switch(getFieldType()){
    	case IFormField.DATATYPE_NUMBER:
    		obj.put("coinValue", "");
    		obj.put("isShowComdify", "1");
    		obj.put("decimalValue", "0");
    		break;
    	case IFormField.DATATYPE_VARCHAR:
    		break;
    	case IFormField.DATATYPE_DATE:
    		if(type.equals("DateTime")){
    			obj.put("format", "yyyy-MM-dd HH:mm:ss");
    		}else{
    			obj.put("format", "yyyy-MM-dd");
    		}
    	case IFormField.DATATYPE_CLOB:
    		break;
    	}
		return obj.toString();
	}
	public Integer getDecimalLen(){
		return 0;
	}
	public String getDictType(){
		return "";
	}
	public String getEncrypt(){
		return "";
	}
	public String getFieldDesc(){
		return displayName;
	}
	public Long getFieldId(){
		return null;
	}
	public String getFieldName(){
		return name;
	}
	public String getFieldType(){
		String fieldType = IFormField.DATATYPE_VARCHAR;
		return fieldType;
	}
	public Integer getIntLen(){
		Integer len = 0;
		switch(getFieldType()){
    	case IFormField.DATATYPE_NUMBER:
    		len = 18;
    		break;
    	}
		return len;
	}
	public Short getIsDeleted(){
		return 0;
	}
	public Short getIsFlowVar(){
		if(isSingleUserSelector||isMultiUserSelector){//人员选择器字段自动设置流程变量
			return 1;
		}else{
			return 0;
		}
	}
	public Short getIsHidden(){
		return 0;
	}
	public Short getIsMainData(){
		return 0;
	}
	public Short getIsPkShow(){
		return 0;
	}
	public Short getIsReference(){
		return null;
	}
	public Short getIsRequired(){
		return isNeed.equals("False")
				?new Short("0"):new Short("1");
	}
	public Short getIsShowComdify(){
		return null;
	}
	public Short getIsUnique(){
		return 0;
	}
	public Short getIsWebSign(){
		return 0;
	}
	public String getOptions(){
		JSONArray arr = new JSONArray();
		switch(type){
    	case IOutField.COLUMN_TYPE_BOOLEAN:
    		JSONObject yes = new JSONObject();
    		JSONObject no = new JSONObject();
    		no.put("key", "0");no.put("value", "否");
    		no.put("key", "1");no.put("value", "是");
    		arr.add(no);
    		arr.add(yes);
    		break;
    	}
		return arr.size()>0?arr.toString():null;
	}
	public String getOriginalName(){
		return "";
	}
	public Short getRelDelLMType(){
		if(!isSelector()){
			return 0;
		}
		return null;
	}
	public Short getRelDelType(){
		if(!isSelector()){
			return 0;
		}
		return null;
	}
	public String getRelFormDialog(){
		return "";
	}
	public String getRelFormDialogStripCData(){
		return "";
	}
	public Long getRelTableId(){
		return null;
	}
	public String getRelTableName(){
		return "";
	}
	public Short getRelTableType(){
		if(!isSelector()){
			if(relationType.equals("多对一")){
				return 2;//多对一
			}else{
				return 0;//一对一
			}
		}
		return null;
	}
	public String getScript(){
		return "";
	}
	public String getScriptID(){
		return "";
	}
	public String getSerialNumber(){
		return "";
	}
	public Integer getSn(){
		return sn;
	}
	public String getStyle(){
		return null;
	}
	public Long getTableId(){
		return null;
	}
	public String getValidRule(){
		return "";
	}
	public Short getValueFrom(){
		return 0;
	}	
	/**----------------------------------------------------------------------------------------*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsNotFk() {
		return isNotFk;
	}

	public void setIsNotFk(String isNotFk) {
		this.isNotFk = isNotFk;
	}

	public String getOwnerShip() {
		return ownerShip;
	}

	public void setOwnerShip(String ownerShip) {
		this.ownerShip = ownerShip;
	}

	public String getRelTable() {
		return relTable;
	}

	public void setRelTable(String relTable) {
		this.relTable = relTable;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public boolean isMultiUserSelector() {
		return isMultiUserSelector;
	}
	public void setMultiUserSelector(boolean isMultiUserSelector) {
		this.isMultiUserSelector = isMultiUserSelector;
	}
	public boolean isMultiDeptSelector() {
		return isMultiDeptSelector;
	}
	public void setMultiDeptSelector(boolean isMultiDeptSelector) {
		this.isMultiDeptSelector = isMultiDeptSelector;
	}
	public boolean isSingleUserSelector() {
		return isSingleUserSelector;
	}
	public void setSingleUserSelector(boolean isSingleUserSelector) {
		this.isSingleUserSelector = isSingleUserSelector;
	}
	public boolean isSingleDeptSelector() {
		return isSingleDeptSelector;
	}
	public void setSingleDeptSelector(boolean isSingleDeptSelector) {
		this.isSingleDeptSelector = isSingleDeptSelector;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	private boolean isSelector(){
		return isMultiUserSelector||isMultiDeptSelector||
				isSingleUserSelector||isSingleDeptSelector;
	}
}
