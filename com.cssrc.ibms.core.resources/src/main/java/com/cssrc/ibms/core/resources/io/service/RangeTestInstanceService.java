package com.cssrc.ibms.core.resources.io.service;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.resources.io.bean.RangeTestInstance;
import com.cssrc.ibms.core.resources.io.dao.RangeTestInstaceDao;
import com.cssrc.ibms.dp.util.dao.CommonDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RangeTestInstanceService {
    @Resource
    private RangeTestInstaceDao rangeTestInstaceDao;
    @Resource
    private SysFileService sysFileService;
    @Resource
    private CommonDao commonDao;

    /**
     * 获取某一策划下有多少回传的实例
     * 如果没有获取到实例就返回0
     * @param missionId
     * @return
     */
    public String CountHowManyInstanceForRangeTest(String missionId){
        List<RangeTestInstance> rangeTestInstanceList=rangeTestInstaceDao.getByPlanId(missionId);
        if (rangeTestInstanceList.size()==0){
            return "0";
        }else{
            return "1";
        }
    }
    /**
     * @Description 靶场/武器所检上传实例文件
     * @Author ZMZ
     * @Date 2021/1/4 16:05
     * @param missionId
     * @param fileIds
     * @Return void
     */
    public void uploadFileInstace(String missionId,String file) {
            Map<String,Object> fileInstanceMap=new HashMap<>();
            fileInstanceMap.put("ID", UniqueIdUtil.genId());
            fileInstanceMap.put("F_NUMBER",file);
            fileInstanceMap.put("F_STATUS","正在使用");
            fileInstanceMap.put("F_UPLOAD_TIME",new Date().getTime());
            fileInstanceMap.put("F_PLANID",missionId);
            fileInstanceMap.put("F_BDZL","17");
            commonDao.insert(fileInstanceMap,"W_TB_INSTANT");
    }
}
