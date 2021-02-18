package com.cssrc.ibms.dp.rangeTest.mission.controller;

import com.cssrc.ibms.core.resources.io.service.RangeTestInstanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @Description 靶场和所检的实例
 * @Author ZMZ
 * @Date 2021/1/4 15:42
 */
@Controller
@RequestMapping("/rangeTest/instace/")
public class RangeTestInstanceController {

    @Resource
    RangeTestInstanceService rangeTestInstanceService;

    /**
     * @Description 上传实例文件到当前策划
     * @Author ZMZ
     * @Date 2021/1/4 15:46
     * @param missionId
     * @param fileIds
     * @Return boolean
     */
    @RequestMapping("uploadFileInstace")
    public boolean uploadFileInstace(String missionId,String fileIds){
        rangeTestInstanceService.uploadFileInstace(missionId,fileIds);
        return true;
    }
}
