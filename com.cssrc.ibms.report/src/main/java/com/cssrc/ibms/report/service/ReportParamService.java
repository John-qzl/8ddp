package com.cssrc.ibms.report.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.report.inf.IReportParamService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.report.dao.ReportParamDao;
import com.cssrc.ibms.report.model.ReportParam;

/**
 * <pre>
 * 对象功能:报表模板参数 Service类 
 * 开发人员:zxg
 * </pre>
 */
@Service
public class ReportParamService extends BaseService<ReportParam> implements IReportParamService{
	private static Logger logger=Logger.getLogger("IBMS.REPORT");
	@Resource
	private ReportParamDao reportParamDao;

	@Override
	protected IEntityDao<ReportParam, Long> getEntityDao() {
		return reportParamDao;
	}

	public ResultMessage saveReportParam(ReportParam reportParam,Date createTime) {
		try{
			createTime=(createTime==null?new Date():createTime);
			if (reportParam.getParamid() == null) {
				reportParam.setParamid(Long.valueOf(UniqueIdUtil.genId()));
				reportParam.setCreatetime(createTime);
				reportParam.setUpdatetime(createTime);
				add(reportParam);
				return new ResultMessage(ResultMessage.Success, "新增报表模板参数成功");
			} else {
				reportParam.setCreatetime(createTime);
				reportParam.setUpdatetime(new Date());
				update(reportParam);
				return new ResultMessage(ResultMessage.Success, "更新报表模板参数成功");
			}
		}catch(Exception e){
			logger.error(e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		 
	}

	/**
	 * 获取报表参数列表
	 * @param reportid
	 * @return
	 */
	public List<ReportParam> getByReportid(Long reportid) {
		return reportParamDao.getByReportid(reportid);
	}
	
}
