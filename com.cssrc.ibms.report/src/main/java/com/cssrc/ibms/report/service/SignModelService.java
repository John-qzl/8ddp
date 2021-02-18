package com.cssrc.ibms.report.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.report.inf.ISignModelService;
import com.cssrc.ibms.api.report.model.ISignModel;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.encrypt.Coder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.CertUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.report.dao.SignModelDao;
import com.cssrc.ibms.report.model.SignModel;

@Service("signModelService")
public class SignModelService extends BaseService<SignModel> implements ISignModelService
{
    private static Logger logger = Logger.getLogger("IBMS.REPORT");

    
    @Resource
    private SignModelDao signModelDao;
    
    @Resource
    ISysOrgService sysOrgService;
    private Long DefaultSignModelId=10000028420135L;
    
    @Override
    protected IEntityDao<SignModel, Long> getEntityDao()
    {
        return signModelDao;
    }
    
    @Override
    public List<? extends ISignModel> getByUserId(Long userId)
    {
        return signModelDao.getByUserId(userId);
    }
    
    /**
     * 保存印章模板
     * 
     * @param signModel
     * @return
     */
    public ResultMessage save(SignModel signModel)
    {
        try
        {
            if (signModel.getId() != null)
            {
                this.signModelDao.update(signModel);
                return new ResultMessage(ResultMessage.Success, "更新印章模板成功");
            }
            else
            {
                List<? extends ISysOrg> orgs = sysOrgService.getByUserId(signModel.getUserId());
                signModel.setId(UniqueIdUtil.genId());
                if (orgs != null && orgs.size() > 0)
                {
                    signModel.setOrgId(orgs.get(0).getOrgId());
                }
                signModel.setCode(CertUtil.getNextSerialNumber().toString());
                signModel.setStartDate(new Date());
                signModel.setEndDate(DateUtil.addYear(2));
                signModel.setType(0l);
                signModel.setVersion(CertUtil.getNextVersion());
                this.signModelDao.add(signModel);
                return new ResultMessage(ResultMessage.Success, "添加印章模板成功");
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 设置用户默认印章
     * 
     * @param userid
     * @param id
     * @return
     */
    public ResultMessage updateDefaultYes(Long userid, Long id)
    {
        try
        {
            signModelDao.updateDefaultNot(userid);
            signModelDao.updateDefault(id);
            return new ResultMessage(ResultMessage.Success, "更新印章模板成功");
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 取消用户默认印章
     * 
     * @param userid
     * @param id
     * @return
     */
    public ResultMessage updateDefaultNot(Long userid, Long id)
    {
        try
        {
            signModelDao.updateDefaultNot(userid);
            return new ResultMessage(ResultMessage.Success, "更新印章模板成功");
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 获取用户默认印章模型
     * 
     * @param userId
     * @return
     */
    @Override
    public ISignModel getDefaultByUserId(Long userId)
    {
        return signModelDao.getUnique("getDefaultByUserId", userId);
    }
    
    /**
     * 获取印章模型图片 byte数组 用base64 重新编码
     * 
     * @param signId
     * @return
     */
    public String getImageByte(Long signId)
    {
        String imgData;
        SignModel signModel = this.getById(signId);
        try
        {
            if (signModel.getPathType() == 1)
            {
                imgData = Coder.encryptBASE64(FastDFSFileOperator.getFileByte(signModel.getImgPath()));
                return imgData;
            }
            else if (signModel.getPathType() == 0)
            {
                byte[] b =
                    FileUtil.readByte(SysConfConstant.UploadFileFolder + File.separator + signModel.getImgPath());
                imgData = Coder.encryptBASE64(b);
                return imgData;
            }
            else
            {
                return signModel.getImgPath();
            }
            
        }
        catch (Exception e)
        {
            logger.error(e);
            return null;
        }
        
    }
    
    /**
     * 获取用户的电子签章单图片查找方法路径,若没有,则返回默认签章
     * 
     * @param defaultSignModelPath
     * @return
     * @author zmz
     */
    @Override
    public String getSignModelPath(Long userId,String defaultSignModelPath){
    
	String signModelImageSrc=defaultSignModelPath;
	
	List<? extends ISignModel> signModel = this.getByUserId(userId);
	
	if(signModel.size()>0){
		//不需要担心数据库有这个条目但是该条目Imgpath为空的请况(该字段为必填
		signModelImageSrc="/oa/system/sysFile/getFileById.do?fileId="+signModel.get(0).getImgPath();
		}
	return signModelImageSrc;
    }
    
    /**
     * 获取用户的电子签章id,若没有,则返回默认签章
     * 
     * @param userId
     * @return
     */
    @Override
    public Long getSignModelPathFileID(Long userId){
    	Long signModelImageSrc=DefaultSignModelId;
    	
    	List<? extends ISignModel> signModel = this.getByUserId(userId);
    	
    	if(signModel.size()>0){
    		//不需要担心数据库有这个条目但是该条目Imgpath为空的请况(该字段为必填
    		signModelImageSrc=Long.parseLong(signModel.get(0).getImgPath());
    		}
    	return signModelImageSrc;
    }

    
    /**
     * 新增及修改用户的签章(弃用)
     * 	若signModel表中有该用户的id,则覆盖该条记录
     * 	若没有,则新增
     * 		因为没有需求,所以除了userid和path之外的值不传,存储时使用了平台的默认值,没有实际意义
     * @param userId
     * @param SignModelPath
     * @return
     */
/*    public void saveAndUpdateSignModel(long userId,String SignModelPath){
    	SignModel curSignModel=new SignModel();
    	//还没有好的思路生成不重复的签章id,所以暂时让userId来充当id
		curSignModel.setId(userId);		
		curSignModel.setUserId(userId);
		curSignModel.setImgPath(SignModelPath);
		//以下字段无意义,为表设计中必填字段
		curSignModel.setCode("1");
		curSignModel.setName("当前签章");
		curSignModel.setIsDefault(1);
		curSignModel.setType((long)1);
		curSignModel.setStartDate(new Date());
		curSignModel.setEndDate(DateUtil.addYear(2));
		curSignModel.setPassw("123456");
		curSignModel.setVersion("1.0");
		curSignModel.setPathType(1);
		
    	List<? extends ISignModel> signModel = this.getByUserId(userId);
    	if(signModel.size()>0){
    		this.signModelDao.update(curSignModel);
    	}else{
    		this.signModelDao.add(curSignModel);
    	}
    	
    }*/
    /**
     * 新增及修改用户的签章
     * 	若signModel表中有该用户的id,则覆盖该条记录
     * 	若没有,则新增
     * 		因为没有需求,所以除了userid和path之外的值不传,存储时使用了平台的默认值,没有实际意义
     * @param userId

     * @return
     */
    @Override
    public void saveAndUpdateSignModel(long userId,String signModelId){
    	
    	SignModel curSignModel=new SignModel();
	
		curSignModel.setUserId(userId);
		curSignModel.setImgPath(signModelId);
		//以下字段无意义,为表设计中必填字段
		curSignModel.setCode("1");
		curSignModel.setName("当前签章");
		curSignModel.setIsDefault(1);
		curSignModel.setType((long)1);
		curSignModel.setStartDate(new Date());
		curSignModel.setEndDate(DateUtil.addYear(2));
		curSignModel.setPassw("123456");
		curSignModel.setVersion("1.0");
		curSignModel.setPathType(1);
    	
    	List<? extends ISignModel> signModel = this.getByUserId(userId);
    	
    	if(signModel.size()>0){
    		curSignModel.setId(signModel.get(0).getId());
    		this.signModelDao.update(curSignModel);
    	}else{
        	long id=UniqueIdUtil.genId();
    		curSignModel.setId(id);	
    		this.signModelDao.add(curSignModel);
    	}
    }

    /**
     * 解析data里的值，保存到SignModel表里
     * @param mainFields
     */
    @Override
    public void syncPadToLocal(Map<String, Object> mainFields) {
        String signModelId=mainFields.get("f_qzid").toString();
        Long userId=Long.valueOf(mainFields.get("f_yhid").toString());
        saveAndUpdateSignModel(userId,signModelId);
    }


}
