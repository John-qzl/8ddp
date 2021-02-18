package com.cssrc.ibms.system.service;

import java.util.Map;

import javax.annotation.Resource;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysBusEventService;
import com.cssrc.ibms.api.system.model.ISysBusEvent;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.SysBusEventDao;
import com.cssrc.ibms.system.model.SysBusEvent;

@Service
public class SysBusEventService extends BaseService<SysBusEvent> implements ISysBusEventService{

	@Resource
	private SysBusEventDao dao;

	protected IEntityDao<SysBusEvent, Long> getEntityDao() {
		return this.dao;
	}
    @Override
    public Class<SysBusEvent> getBusClass()
    {
        return SysBusEvent.class;
    }
    
	public void processHandler(IProcessCmd cmd) throws Exception {
		Map data = cmd.getFormDataMap();
		if (BeanUtils.isNotEmpty(data)) {
			String json = data.get("json").toString();
			SysBusEvent sysBusEvent = getSysBusEvent(json);
			if (StringUtil.isEmpty(cmd.getBusinessKey())) {
				Long genId = Long.valueOf(UniqueIdUtil.genId());
				sysBusEvent.setId(genId);
				add(sysBusEvent);
			} else {
				sysBusEvent.setId(Long.valueOf(Long.parseLong(cmd
						.getBusinessKey())));
				update(sysBusEvent);
			}
			cmd.setBusinessKey(sysBusEvent.getId().toString());
		}
	}

	public SysBusEvent getSysBusEvent(String json) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));
		if (StringUtil.isEmpty(json))
			return null;
		JSONObject obj = JSONObject.fromObject(json);
		SysBusEvent sysBusEvent = (SysBusEvent) JSONObject.toBean(obj,
				SysBusEvent.class);
		return sysBusEvent;
	}

	public void save(SysBusEvent sysBusEvent) {
		Long id = sysBusEvent.getId();
		if ((id == null) || (id.longValue() == 0L)) {
			id = Long.valueOf(UniqueIdUtil.genId());
			sysBusEvent.setId(id);
			add(sysBusEvent);
		} else {
			update(sysBusEvent);
		}
	}

	public SysBusEvent getByFormKey(String formKey) {
		return this.dao.getByFormKey(formKey);
	}
	/**
	 * 业务保存设置
	 * @param sysBusEvent
	 * @param formKey
	 */
	@Override
	public void importSysBusEvent(ISysBusEvent sysBusEvent, Long formKey) {
		SysBusEvent sysBusEventTemplet = (SysBusEvent)this.getById(sysBusEvent.getId());
		if (BeanUtils.isNotEmpty(sysBusEventTemplet)) {
			this.update((SysBusEvent)sysBusEvent);
			MsgUtil.addMsg(2, " 数据模板已经存在,数据模板ID：" + sysBusEventTemplet.getId() + ",已经存在，该数据模板进行更新!");
			return;
		}
		sysBusEvent.setFormkey(formKey.toString());
		this.add((SysBusEvent)sysBusEvent);
		MsgUtil.addMsg(1, " 数据模板成功导入!");
	}


}
