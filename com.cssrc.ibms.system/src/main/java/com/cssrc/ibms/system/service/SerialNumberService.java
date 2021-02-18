package com.cssrc.ibms.system.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.system.dao.SerialNumberDao;
import com.cssrc.ibms.system.model.SerialNumber;
import com.cssrc.ibms.system.xml.SerialNumberXml;
import com.cssrc.ibms.system.xml.SerialNumberXmlList;

@Service
public class SerialNumberService extends BaseService<SerialNumber> implements ISerialNumberService{
	private static Logger logger = LoggerFactory
			.getLogger(SerialNumberService.class);

	@Resource
	private SerialNumberDao dao;

	protected IEntityDao<SerialNumber, Long> getEntityDao() {
		return this.dao;
	}

    @Override
    public Class<? extends ISerialNumber> getSerialNumberClass()
    {
        return SerialNumber.class;
    }
    
	public boolean isAliasExisted(String alias) {
		return this.dao.isAliasExisted(alias);
	}

	public boolean isAliasExistedByUpdate(SerialNumber serialNumber) {
		return this.dao.isAliasExistedByUpdate(serialNumber);
	}

	public String getCurIdByAlias(String alias) {
		SerialNumber serialNumber = this.dao.getByAlias(alias);
		Long curValue = serialNumber.getCurValue();
		if (curValue == null)
			curValue = new Long(serialNumber.getInitValue().intValue());
		String rtn = getByRule(serialNumber.getRule(), serialNumber
				.getNoLength().intValue(), curValue);

		return rtn;
	}

	public synchronized String nextId(String alias) {
		Result result = getResult(alias);
		int i = 0;
		while (result.getResult() == 0) {
			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			result = getResult(alias);
		}
		if (i > 0) {
			logger.error("nextId:" + alias + ",i:" + i);
		}

		return result.getNo();
	}

	public String preview(String alias) {
		SerialNumber serialNumber = this.dao.getByAlias(alias);
		String rule = serialNumber.getRule();
		int step = serialNumber.getStep().shortValue();
		short genType = serialNumber.getGenType().shortValue();
		Long curValue = serialNumber.getCurValue();
		if (curValue.longValue() == -1L)
			curValue = new Long(serialNumber.getInitValue().intValue());

		if (genType > 0) {
			String curDate = getCurDate(genType);
			String oldDate = serialNumber.getCurDate();
			if (!oldDate.startsWith(curDate)) {
				serialNumber.setCurDate(curDate);
				curValue = new Long(serialNumber.getInitValue().intValue());
			} else {
				curValue = Long.valueOf(curValue.longValue() + step);
			}
		} else {
			curValue = Long.valueOf(curValue.longValue() + step);
		}
		serialNumber.setNewCurValue(curValue);

		String rtn = getByRule(rule, serialNumber.getNoLength().intValue(),
				curValue);

		return rtn;
	}

	private Result getResult(String alias) {
		SerialNumber serialNumber = this.dao.getByAlias(alias);
		String rule = serialNumber.getRule();
		int step = serialNumber.getStep().shortValue();
		short genType = serialNumber.getGenType().shortValue();
		Long curValue = serialNumber.getCurValue();
		if (curValue.longValue() == -1L)
			curValue = new Long(serialNumber.getInitValue().intValue());

		if (genType > 0) {
			String curDate = getCurDate(genType);
			String oldDate = serialNumber.getCurDate();
			if (!oldDate.startsWith(curDate)) {
				serialNumber.setCurDate(curDate);
				curValue = new Long(serialNumber.getInitValue().intValue());
			} else {
				curValue = Long.valueOf(curValue.longValue() + step);
			}
		} else {
			curValue = Long.valueOf(curValue.longValue() + step);
		}
		serialNumber.setNewCurValue(curValue);

		int i = this.dao.updateVersion(serialNumber);

		Result result = new Result();

		if (i > 0) {
			//返回流水号
			String rtn = getByRule(rule, serialNumber.getNoLength().intValue(), curValue);
			result = new Result();
			result.setResult(1);
			result.setNo(rtn);
		}

		return result;
	}
	
	/**
	 * 去当前日期
	 * @param genType
	 * @return
	 */
	public static String getCurDate(short genType) {
		Date date = new Date();
		String str = "";
		String curDate = DateFormatUtil.format(date,"yyyyMMdd");
		String noDay = curDate.substring(0,curDate.length()-2);//yyyyMM格式
		String onlyYear = curDate.substring(0,curDate.length()-4);//yyyy格式
		switch (genType) {
		case 1:
			str = curDate;
			break;
		case 2:
			str = noDay;
			break;
		case 3:
			str = onlyYear;
		}

		return str;
	}
	
	/**
	 * 通过规则获取流水号
	 * @param rule 规则
	 * @param length 流水号{NO}长度
	 * @param curValue 当前值
	 * @return
	 */
	private String getByRule(String rule, int length, Long curValue) {
		Date date = new Date();
		String curDate = DateFormatUtil.format(date,"yyyyMMdd");
		String year = curDate.substring(0,curDate.length()-4);
		String yearForYY = curDate.substring(2,curDate.length()-4);
		String month = curDate.substring(4,curDate.length()-2);
		String day = curDate.substring(6);
		String shortMonth = String.valueOf(month);
		String longMonth = String.valueOf(month);

		String seqNo = getSeqNo(rule, curValue, length);

		String shortDay = String.valueOf(day);
		String longDay = String.valueOf(day);

		String rtn = rule.replace("{yyyy}", year).replace("{yy}", yearForYY)
						 .replace("{MM}", longMonth).replace("{mm}", shortMonth)
						 .replace("{DD}", longDay).replace("{dd}", shortDay)
						 .replace("{NO}", seqNo).replace("{no}", seqNo);

		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		if ((sysOrg != null) && (sysOrg.getCode() != null))
			rtn = rtn.replace("{ORG}", sysOrg.getCode());
		else {
			rtn = rtn.replace("{ORG}", "");
		}

		return rtn;
	}

	private static String getSeqNo(String rule, Long curValue, int length) {
		String tmp = String.valueOf(curValue);
		int len = 0;
		if (rule.indexOf("no") > -1)
			len = length;
		else {
			len = length - tmp.length();
		}
		String rtn = "";
		switch (len) {
		case 1:
			rtn = "0";
			break;
		case 2:
			rtn = "00";
			break;
		case 3:
			rtn = "000";
			break;
		case 4:
			rtn = "0000";
			break;
		case 5:
			rtn = "00000";
			break;
		case 6:
			rtn = "000000";
			break;
		case 7:
			rtn = "0000000";
			break;
		case 8:
			rtn = "00000000";
			break;
		case 9:
			rtn = "000000000";
			break;
		case 10:
			rtn = "0000000000";
			break;
		case 11:
			rtn = "00000000000";
			break;
		case 12:
			rtn = "000000000000";
		}

		if (rule.indexOf("no") > -1) {
			return tmp + rtn;
		}
		return rtn + tmp;
	}

	public List<SerialNumber> getList() {
		return this.dao.getList();
	}

	public SerialNumber getByAlias(String alias) {
		SerialNumber serialNumber = this.dao.getByAlias(alias);
		return serialNumber;
	}

	public String exportXml(List<SerialNumber> identities) throws Exception {
		SerialNumberXmlList serialNumberXmlList = new SerialNumberXmlList();
		List list = new ArrayList();
		for (SerialNumber serialNumber : identities) {
			SerialNumberXml serialNumberXml = exportSerialNumberXml(serialNumber);
			list.add(serialNumberXml);
		}
		serialNumberXmlList.setSerialNumberXmlList(list);
		return XmlBeanUtil.marshall(serialNumberXmlList,
				SerialNumberXmlList.class);
	}

	public String exportXml(Long[] tableIds) throws Exception {
		SerialNumberXmlList serialNumberXmlList = new SerialNumberXmlList();
		List list = new ArrayList();
		for (int i = 0; i < tableIds.length; i++) {
			SerialNumber serialNumber = (SerialNumber) this.dao.getById(tableIds[i]);
			SerialNumberXml serialNumberXml = exportSerialNumberXml(serialNumber);
			list.add(serialNumberXml);
		}
		serialNumberXmlList.setSerialNumberXmlList(list);
		return XmlBeanUtil.marshall(serialNumberXmlList, SerialNumberXmlList.class);
	}

	private SerialNumberXml exportSerialNumberXml(SerialNumber serialNumber) throws Exception {
		SerialNumberXml serialNumberXml = new SerialNumberXml();
		Long id = serialNumber.getId();
		if (BeanUtils.isNotIncZeroEmpty(id)) {
			serialNumberXml.setSerialNumber(serialNumber);
		}
		return serialNumberXml;
	}

	public void importXml(InputStream inputStream) throws Exception {
		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();

		XmlUtil.checkXmlFormat(root, "system", "identities");

		String xmlStr = root.asXML();
		SerialNumberXmlList serialNumberXmlList = (SerialNumberXmlList) XmlBeanUtil
				.unmarshall(xmlStr, SerialNumberXmlList.class);

		List<SerialNumberXml> list = serialNumberXmlList.getSerialNumberXmlList();

		for (SerialNumberXml serialNumberXml : list) {
			importSerialNumberXml(serialNumberXml);
		}
	}

	private void importSerialNumberXml(SerialNumberXml serialNumberXml) throws Exception {
		Long serialNumberId = Long.valueOf(UniqueIdUtil.genId());
		SerialNumber serialNumber = serialNumberXml.getSerialNumber();
		if (BeanUtils.isEmpty(serialNumber)) {
			throw new Exception();
		}
		String alias = serialNumber.getAlias();

		if (isAliasExisted(alias)) {
			MsgUtil.addMsg(2, "别名为‘" + alias + "’的流水号已经存在，请检查你的xml文件！");
			return;
		}
		serialNumber.setId(serialNumberId);
		this.dao.add(serialNumber);
		MsgUtil.addMsg(1, "别名为" + alias + "的流水号导入成功！");
	}

	class Result {
		private int result = 0;
		private String no = "";

		Result() {
		}

		public int getResult() {
			return this.result;
		}

		public void setResult(int result) {
			this.result = result;
		}

		public String getNo() {
			return this.no;
		}

		public void setNo(String no) {
			this.no = no;
		}
	}

	@Override
	public void add(ISerialNumber serial) {
		this.add(serial);
	}


}
