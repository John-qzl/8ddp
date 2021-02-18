package com.cssrc.ibms.dp.product.acceptance.service;

import javax.annotation.Resource;

import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.product.acceptance.dao.ManualPreviewDao;

@Service
public class WorkBordService {
	@Resource
    WorkBoardDao workBoardDao;

	public void updateWorkBoard(String planId,String dqzt,String xyb){
	    workBoardDao.updatework(planId, dqzt, xyb);
    }
}
