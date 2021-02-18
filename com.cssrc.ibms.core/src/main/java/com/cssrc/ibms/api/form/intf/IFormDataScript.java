package com.cssrc.ibms.api.form.intf;

public interface IFormDataScript {


	void setFormDataValue(String dataId,String tableName,String[] fieldNameArray,Object[] valueArray);

	Object getFormDataValue(String pk, String string, String cMsgNo);


}
