package com.cssrc.ibms.migration.model.tdm.table;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.migration.model.IOutField;
@XmlRootElement(name = "普通属性")
@XmlAccessorType(XmlAccessType.NONE)
public class SimpleColumn implements IOutField{
	@XmlAttribute(name = "名称")
	private String name;

	@XmlAttribute(name = "显示名")
	private String displayName;

	@XmlAttribute(name = "描述")
	private String description;

	@XmlAttribute(name = "是否全文检索")
	private String isAllSearch;

	@XmlAttribute(name = "是否适用于全文检索")
	private String isForSearch;

	@XmlAttribute(name = "是否索引")
	private String isIndex;

	@XmlAttribute(name = "用途")
	private String purpose;
	/**
	 * 大小写规定 CaseSensitive表示“区分大小写”，Upper表示“全大写”，Lower表示“全小写”，CaseInsensitive表示“
	 * 不区分大小写”
	 */
	@XmlAttribute(name = "大小写规定")
	private String casesensitive;

	@XmlAttribute(name = "缺省值")
	private String defaultValue;

	@XmlAttribute(name = "是否必要")
	private String isNeed;

	@XmlAttribute(name = "是否唯一")
	private String isOnly;

	@XmlAttribute(name = "数据类型")
	private String type;

	/** 字符型最大长度，默认100 */
	@XmlAttribute(name = "字符型最大长度")
	private Long maxLength;

	/** 字符型最小长度，默认0 */
	@XmlAttribute(name = "字符型最小长度")
	private Long minLength;

	/** True表示显示，False表示不显示 */
	@XmlAttribute(name = "是否显示")
	private String isShow;

	/** 字段是否是多行显示，True表示是，False表示不是 */
	@XmlAttribute(name = "是否多行显示")
	private String isWrap;

	@XmlAttribute(name = "行数")
	private Long linage;

	@XmlAttribute(name = "自增初始值")
	private Long autoAddDefault;

	@XmlAttribute(name = "属性约束")
	private String restriction = "";

	private boolean isPkShow = false;
	private Integer sn;
	private boolean isAttachCol = false;
	private boolean isFlowState = false;
	/**---------------------------------IOutField接口实现------------------------------------------*/
	public Integer getCharLen(){
		Integer len = 0;
		switch(getFieldType()){
    	case IFormField.DATATYPE_VARCHAR:
    		len = maxLength.intValue()*2;//两倍
    		break;
    	}
		return len;
	}
	public Short getControlType(){
		Short ct = 1;
		switch(getFieldType()){
    	case IFormField.DATATYPE_CLOB:
    		ct=2;
    		break;
    	case IFormField.DATATYPE_VARCHAR:
    		if(type.equals(IOutField.COLUMN_TYPE_BOOLEAN)){
    			ct=11;//下拉单选项
        		break;
    		}
    		break;
    	}
		if(!restriction.equals("")){
			ct=3;//数据字典
		}else if(isAttachCol){
			ct=9;//文件上传
		}else if(isFlowState){
			ct=24;//流程控件
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
		if(displayName.equals("表单编号")){//是否显示在启动流程中
			obj = new JSONObject();
			obj.put("isShowidentity", 1);
			obj.put("displayHoverTitle", 0);
			obj.put("hoverTitle", "");
		}
		return obj.toString();
	}
	public Integer getDecimalLen(){
		return 0;
	}
	public String getDictType(){
		return this.restriction;
	}
	public String getEncrypt(){
		return "";
	}
	public String getFieldDesc(){
		return displayName;
	}
	public Long getFieldId(){
		return 0L;
	}
	public String getFieldName(){
		return name;
	}
	public String getFieldType(){
		String fieldType = "";
		switch(type){
		case IOutField.COLUMN_TYPE_BOOLEAN:
			fieldType = IFormField.DATATYPE_VARCHAR;
			break;
		case IOutField.COLUMN_TYPE_DATE:
			fieldType = IFormField.DATATYPE_DATE;
			break;
		case IOutField.COLUMN_TYPE_DATETIME:
			fieldType = IFormField.DATATYPE_DATE;
			break;
		case IOutField.COLUMN_TYPE_INTEGER:
			fieldType = IFormField.DATATYPE_NUMBER;
			break;	
		case IOutField.COLUMN_TYPE_STRING:
			fieldType = IFormField.DATATYPE_VARCHAR;
			break;
		case IOutField.COLUMN_TYPE_TEXT:
			fieldType = IFormField.DATATYPE_VARCHAR;
			break;
		default :
			fieldType = IFormField.DATATYPE_VARCHAR;
			break;
		}
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
		return 0;
	}
	public Short getIsHidden(){
		return 0;
	}
	public Short getIsMainData(){
		return 0;
	}
	public Short getIsPkShow(){
		return isPkShow
				?new Short("1"):new Short("0");
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
		return isOnly.equals("False")
				?new Short("0"):new Short("1");
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
    		yes.put("key", "1");yes.put("value", "是");
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
		return null;
	}
	public Short getRelDelType(){
		return null;
	}
	public String getRelFormDialog(){
		return "";
	}
	public String getRelFormDialogStripCData(){
		return null;
	}
	public Long getRelTableId(){
		return null;
	}
	public String getRelTableName(){
		return null;
	}
	public Short getRelTableType(){
		return null;
	}
	public String getScript(){
		if(displayName.equals("表单编号")){
			return IOutField.LSH_NAME;
		}else{
			return "";
		}
	}
	public String getScriptID(){
		return "";
	}
	public String getSerialNumber(){
		if(displayName.equals("表单编号")){
			return IOutField.LSH_NAME;
		}else{
			return "";
		}
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
		if(displayName.equals("表单编号")){
			return 3;
		}else{
			return 0;
		}
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

	public String getIsAllSearch() {
		return isAllSearch;
	}

	public void setIsAllSearch(String isAllSearch) {
		this.isAllSearch = isAllSearch;
	}

	public String getIsForSearch() {
		return isForSearch;
	}

	public void setIsForSearch(String isForSearch) {
		this.isForSearch = isForSearch;
	}

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCasesensitive() {
		return casesensitive;
	}

	public void setCasesensitive(String casesensitive) {
		this.casesensitive = casesensitive;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}

	public String getIsOnly() {
		return isOnly;
	}

	public void setIsOnly(String isOnly) {
		this.isOnly = isOnly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	public Long getMinLength() {
		return minLength;
	}

	public void setMinLength(Long minLength) {
		this.minLength = minLength;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getIsWrap() {
		return isWrap;
	}

	public void setIsWrap(String isWrap) {
		this.isWrap = isWrap;
	}

	public Long getLinage() {
		return linage;
	}

	public void setLinage(Long linage) {
		this.linage = linage;
	}

	public Long getAutoAddDefault() {
		return autoAddDefault;
	}

	public void setAutoAddDefault(Long autoAddDefault) {
		this.autoAddDefault = autoAddDefault;
	}
	public String getRestriction() {
		return restriction;
	}
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}
	public void setPkShow(boolean isPkShow) {
		this.isPkShow = isPkShow;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public boolean isAttachCol() {
		return isAttachCol;
	}
	public void setAttachCol(boolean isAttachCol) {
		this.isAttachCol = isAttachCol;
	}
	public boolean isFlowState() {
		return isFlowState;
	}
	public void setFlowState(boolean isFlowState) {
		this.isFlowState = isFlowState;
	}

}
