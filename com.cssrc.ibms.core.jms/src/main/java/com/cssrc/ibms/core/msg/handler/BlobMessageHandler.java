package com.cssrc.ibms.core.msg.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.activemq.BlobMessage;

import com.cssrc.ibms.api.form.intf.ISyncFormJmsWebService;
import com.cssrc.ibms.api.form.model.SyncConstant;
import com.cssrc.ibms.api.jms.model.ISyncModel;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.core.jms.intf.IJmsHandler;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.json.JacksonMapper;
import com.cssrc.ibms.core.util.string.StringUtil;

public class BlobMessageHandler implements IJmsHandler{

	@Override
	public void handMessage(Object model) { 
		BlobMessage blobMessage = (BlobMessage) model;
		try {
			String filePath = StringUtil.trimSufffix(AppUtil.getAttachPath(), File.separator);
			InputStream inputStream = blobMessage.getInputStream(); 
			String jsonData = blobMessage.getStringProperty("FormData");
			JacksonMapper mapper = new JacksonMapper();
			ISyncModel syncModel = (ISyncModel)mapper.toObject(jsonData, ISyncModel.class);
			List<ISysFile> sysFiles = syncModel.getSysFiles();
			int size =1024;
			long hasRead = 0;
			int i=0;
			//保存附件
			for(ISysFile sysFile:sysFiles){
				String realPath = filePath + File.separator + sysFile.getFilepath().replace("/", File.separator); 
				File file = new File(realPath);
				if(file.exists()){
					 file.delete();
				} 
				byte[] buf = new byte[size];
				FileOutputStream fos = new FileOutputStream(realPath);				
				long ts = sysFile.getTotalBytes(); 
				int read = 0;
				while(true){
					if(hasRead+size>=ts){
						byte[] temp = new byte[(int)(ts-hasRead)];
						if((read = inputStream.read(temp))!=-1){
							fos.write(temp,0,read); 							
							hasRead = hasRead+read;
						}else{
							break;
						}
						if(hasRead==ts){
							break;
						}
						
					}
					if((read = inputStream.read(buf))!=-1){
						fos.write(buf,0,read); 
						hasRead = hasRead+read;
					}else{ 
						break;
					}  
					if(hasRead%(8*size)==0){
						fos.flush();
					}
				} 
				fos.flush();
				fos.close();
				hasRead=0;
				i++;
			}
			inputStream.close();
			ISyncFormJmsWebService syncFormWebService = AppUtil.getBean(ISyncFormJmsWebService.class);
			List<?extends ISyncModel> reverseUpdatelist=syncFormWebService.saveFormData(syncModel);
			SimpleMessageProducer producer =  AppUtil.getBean(SimpleMessageProducer.class);
			for(ISyncModel m:reverseUpdatelist){
				Map<String, Object> mainfields = (Map<String, Object>)syncModel.getMain().get("fields");
				producer.send(CommonTools.Obj2String(mainfields.get(SyncConstant.place_from)),m);
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		
		
	}

}
