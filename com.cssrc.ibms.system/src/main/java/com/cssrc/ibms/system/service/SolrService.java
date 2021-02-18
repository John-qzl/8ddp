package com.cssrc.ibms.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.com.cssrc.ibms.solrclient.data.FileQueryResult;
import org.com.cssrc.ibms.solrclient.data.SolrData;
import org.com.cssrc.ibms.solrclient.intf.ISolrFile;
import org.com.cssrc.ibms.solrclient.intf.ISolrQueryService;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.system.intf.ISolrService;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * Solr搜索相关的操作类
 *@author vector
 *@date 2017年9月26日 下午3:11:19
 *     0      #      0       #[doc,docx,pdf,ppt,pptx,xls,xlsx,txt]
 *是否开启文件搜索#是否开启数据库搜索#[            可搜索的文件类型                           ]
 *--0不开启，1开启，不需要的文件类型请删除
 *-------------------2017.10.24---------
 *修改：将索引建立过程改为多线程队列的形式。
 */
@Service
public class SolrService implements ISolrService{
	@Resource(name="fileSolrQueryService")
	ISolrQueryService<FileQueryResult> solrService;
	@Resource
	private SysParameterService sysParameterService;
	/*{key : '4' ,value :'人员选择器(单选)'}, {key : '8',value : '人员选择器(多选)'}, 
    {key : '17',value :'角色选择器(单选)'}, {key : '5',value : '角色选择器(多选)'}, 
    {key : '18',value :'组织选择器(单选)'}, {key : '6',value : '组织选择器(多选)'}, 
    {key : '19',value :'岗位选择器(单选)'}, {key : '7',value : '岗位选择器(多选)'},
    {key : '26',value :'密级管理'}
    {key : '23',value :'关联关系列'}*/
	public static String controlTypeStr="a4a,a8a,a17a,a5a,a18a,a6a,a19a,a7a,a23a";
	//文件索引线程池
	private static ExecutorService filefixedThreadPool;
	//数据库索引线程池
	private static ExecutorService sqlfixedThreadPool;
	//CPU核心数-该值不可信
	private static int processNum=Runtime.getRuntime().availableProcessors();
	/**
	 * 此方法执行前已经做过判断是否进行索引！
	 * 在 SysFileService - solrService.createIndex(solrFile);
	 */
	public void createIndex(final ISolrFile solrFile) {
		if(filefixedThreadPool==null){
			processNum=processNum>4?processNum:4;
			filefixedThreadPool=initThreadPool(processNum);
		}
		filefixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					//System.out.println("开始解析:"+solrFile.getFilename()+"\t线程队列："+filefixedThreadPool.toString());
					solrService.createIndex(solrFile);
					//System.out.println("解析完成:"+solrFile.getFilename()+"\t线程队列："+filefixedThreadPool.toString());
				} catch (Exception e) {
					System.err.println("创建文件索引出错！");
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * 删除文件索引
	 */
	public void deleteFileIndex(Long[] ids) {
		try {
			String s=sysParameterService.getByAlias("SOLRSERVER");
			if(s.startsWith("1")){
				for(long id:ids){
					try {
						solrService.deleteByStr("docid:"+id);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			System.err.println("删除文件索引出错！");
			e.printStackTrace();
		}
		
	}
	/**
	 * 删除数据库数据的索引
	 */
	public void deleteSqlDataIndex(String pk) {
		try {
			String s=sysParameterService.getByAlias("SOLRSERVER");
			if(s.charAt(2)!='1'){
				return ;
			}
			String[] pks=pk.split(",");
			for(String id:pks){
				try {
					solrService.deleteByStr("docid:"+id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.err.println("删除数据库数据的索引出错！");
			e.printStackTrace();
		}
		
	}
	/**
	 * 创建数据库数据的索引
	 * @param bpmFormData 更新时需要的数据
	 * 在 FormHandlerDao 
	 */
	public void createSqlDataIndex(final IFormData formData){
		String s=sysParameterService.getByAlias("SOLRSERVER");
		if(s.charAt(2)!='1'){
			return ;
		}
		if(sqlfixedThreadPool==null){
			processNum=processNum>4?processNum:4;
			sqlfixedThreadPool=initThreadPool(processNum*2);
		}
		sqlfixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				//System.out.println(sqlfixedThreadPool);
				createSqlDataIndexByThread(formData);
			}
		});
	}
	private void createSqlDataIndexByThread(IFormData formData) {
		try {
			SolrData data=new SolrData();
			data.setId(formData.getPkValue().getValue().toString());
			data.setExt("sql");
			Map<String,IFormField> fields=  convertFieldToMap(formData.getFormTable().getFieldList());
			Map<String,Object> mainFields=formData.getMainFields();
			StringBuffer title=new StringBuffer();
			StringBuffer content=new StringBuffer();
			for(String fieldName:mainFields.keySet()){
				String fn=fieldName.replace("f_", "");
				if(fields.containsKey(fn)){
					IFormField field=fields.get(fn);
					Object val=mainFields.get(fieldName);
					if(val==null){
						continue;
					}
					if(field.getControlType()!=null&&field.getControlType()==26){//密级字段 特殊处理
						try {
							data.setSecurityRank(Integer.valueOf(val.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//包括了两个字段，例如人员选择器:请假人 包括两个字段  qjr和qjrID,此处只对qjr进行处理
					else if(field.getControlType()!=null&&controlTypeStr.contains("a"+field.getControlType()+"a")){
						if(fn.endsWith("id")&&fields.containsKey(fn.substring(0, fn.length()-2))){//qjrID 不处理
							continue;
						}else{
							//标题数据
							if(field.getIsMainData()==1){
								title.append(field.getFieldDesc()+":"+val.toString()+" ");
							}
							//内容数据
							if(field.getIsList()==1){
								content.append(field.getFieldDesc()+":"+val.toString()+" ");
							}
						}
						
					}else{   
						if(StringUtil.isNotEmpty(field.getOptions())){
							String key=val.toString();
							StringBuffer value=new StringBuffer();
							
							key="a"+key.replaceAll(",","a,a")+"a";
							
							JSONArray jsons = JSONArray.fromObject(field.getOptions());
							for(Object json:jsons){
								JSONObject jsonObj = (JSONObject) json;
								jsonObj.get("key");
								if(key.contains("a"+jsonObj.get("key").toString()+"a")){
									value.append(jsonObj.get("value")+",");
								}
							}
							val=value.toString().substring(0, value.toString().length()-1);
						}
						//标题数据
						if(field.getIsMainData()==1){
							title.append(field.getFieldDesc()+":"+val.toString()+" ");
						}
						//内容数据
						if(field.getIsList()==1){
							content.append(field.getFieldDesc()+":"+val.toString()+" ");
						}
					}
				}
			}
			data.setTitle(title.toString());
			data.setContent(content.toString());
			solrService.createIndex(data);
			
		} catch (Exception e) {
			System.err.println("数据索引处理出错！");
			e.printStackTrace();
		}
	}

	/**
	 * 将字段列表转成字段map。
	 * @param list
	 * @return
	 */
	private  Map<String, IFormField> convertFieldToMap(List<? extends IFormField>  list){
		Map<String, IFormField> map=new HashMap<String,IFormField>();
		for(IFormField field:list){
			map.put(field.getFieldName().toLowerCase(), field);
		}
		return map;
	}
	private static ExecutorService initThreadPool(int k){
		return Executors.newFixedThreadPool(k);
	}
}
