package com.cssrc.ibms.api.form.intf;


import java.util.List;

import com.cssrc.ibms.api.jms.model.ISyncModel;

public interface ISyncFormJmsWebService {

	List<?extends ISyncModel> saveFormData(ISyncModel syncModel);

	void updateIDTO(String jsonData);

}
